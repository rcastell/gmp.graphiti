/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SAP AG - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.ui.internal.editor;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.graphiti.internal.services.GraphitiInternal;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramsPackage;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.internal.Messages;
import org.eclipse.graphiti.ui.internal.parts.ConnectionEditPart;
import org.eclipse.swt.widgets.Display;

/**
 * Listener for model changes in the notational model. Is attached to the
 * <code>ResourceSet</code> provided by the <code>DiagramEditorInternal</code>.
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DiagramChangeListener implements ResourceSetListener {

	private DiagramRefreshJob diagramRefreshJob;
	private DiagramEditorInternal ed;

	public DiagramChangeListener(DiagramEditorInternal ed) {
		this.ed = ed;
	}

	@Override
	public NotificationFilter getFilter() {
		return NotificationFilter.NOT_TOUCH;
	}

	@Override
	public boolean isAggregatePrecommitListener() {
		return false;
	}

	@Override
	public boolean isPostcommitOnly() {
		return true;
	}

	@Override
	public boolean isPrecommitOnly() {
		return false;
	}

	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
		if (!ed.isAutoRefresh()) {
			return;
		}
		DiagramRefreshJob refreshDiagramJob = getRefreshDiagramJob();

		if (!refreshDiagramJob.isRefreshAll()) {
			GraphicalViewer graphicalViewer = ed.getGraphicalViewer();
			if (graphicalViewer == null) {
				return;
			}
			// Compute editparts to refresh.
			List<Notification> notifications = event.getNotifications();
			if (notifications.size() > 5000) {
				refreshDiagramJob.setRefreshAll();
			} else {
				for (Notification notification : notifications) {
					Object notifier = notification.getNotifier();
					if (!(notifier instanceof EObject))
						continue;
					EObject eo = (EObject) notifier;
					// Do not work with dangling objects
					if (!GraphitiInternal.getEmfService().isObjectAlive(eo))
						continue;
					// If the diagrameditpart was added to the job, it is in refresh all mode,
					// and further processing makes no sense.
					if (refreshDiagramJob.isRefreshAll())
						break;
					// Filter non-pictogram model element changes, these are handled by the framework
					EPackage p = eo.eClass().getEPackage();
					if (!(p instanceof PictogramsPackage))
						continue;
					// Compute editpart for eo and add it to job's editpart list.
					PictogramElement activeContainerPe = calculateActiveContainerPe(eo);
					if (activeContainerPe != null) {
						Map editPartRegistry = graphicalViewer.getEditPartRegistry();
						Object o = editPartRegistry.get(activeContainerPe);
						if (o instanceof EditPart) {
							EditPart affectedEditPart = (EditPart) o;
							refreshDiagramJob.addEditPart(affectedEditPart);
							addRelevantChildEditPartsToRefreshJob(activeContainerPe);
						}
					}
				}
			}
		}
		// Avoid unnecessary scheduling.
		if (refreshDiagramJob.shouldBeRun()) {
			// If we are in the UI thread we update immediately to avoid flickering.
			if (Display.getCurrent() != null) {
				refreshDiagramJob.runInUIThread(new NullProgressMonitor());
			} else {
				refreshDiagramJob.schedule();
			}
		}
	}

	private void addRelevantChildEditPartsToRefreshJob(PictogramElement pe) {
		Map editPartRegistry = ed.getGraphicalViewer().getEditPartRegistry();
		DiagramRefreshJob refreshJob = getRefreshDiagramJob();

		if (pe instanceof AnchorContainer) {
			AnchorContainer anchorContainer = (AnchorContainer) pe;
			Collection<Anchor> anchors = anchorContainer.getAnchors();
			for (Iterator<Anchor> iter = anchors.iterator(); iter.hasNext();) {
				Anchor anchor = iter.next();
				Collection<Connection> incomingConnections = anchor.getIncomingConnections();
				for (Iterator<Connection> iterator = incomingConnections.iterator(); iterator.hasNext();) {
					Connection connection = iterator.next();
					refreshJob.addEditPart((EditPart) editPartRegistry.get(connection));
					addRelevantChildEditPartsToRefreshJob(connection);
				}
				Collection<Connection> outgoingConnections = anchor.getOutgoingConnections();
				for (Iterator<Connection> iterator = outgoingConnections.iterator(); iterator.hasNext();) {
					Connection connection = iterator.next();
					refreshJob.addEditPart((EditPart) editPartRegistry.get(connection));
					addRelevantChildEditPartsToRefreshJob(connection);
				}
			}
		}

		if (pe instanceof Connection) {
			Connection connection = (Connection) pe;
			for (Iterator<ConnectionDecorator> iter = connection.getConnectionDecorators().iterator(); iter.hasNext();) {
				ConnectionDecorator connectionDecorator = iter.next();
				if (connectionDecorator.isActive()) {
					Object object = editPartRegistry.get(connectionDecorator);
					if (object instanceof EditPart) {
						refreshJob.addEditPart((EditPart) object);
					} else if (object == null) {
						refreshJob.setRefreshAll();
						return;
					}
				}
			}
			// Compute if a target or source editpart changed and refresh accordingly.
			AnchorContainer end = ((Connection) pe).getEnd() != null ? ((Connection) pe).getEnd().getParent() : null;
			AnchorContainer start = ((Connection) pe).getStart() != null ? ((Connection) pe).getStart().getParent() : null;
			Object endPart = editPartRegistry.get(end);
			Object startPart = editPartRegistry.get(start);
			EditPart connPart = (EditPart) editPartRegistry.get(connection);
			// Only refresh the editpart that changed.
			if (connPart instanceof ConnectionEditPart) {
				ConnectionEditPart cep = (ConnectionEditPart) connPart;
				if (cep.getSource() != null && !cep.getSource().equals(startPart)) {
					refreshJob.addEditPart((EditPart) startPart);
				} else if (cep.getTarget() != null && !cep.getTarget().equals(endPart)) {
					refreshJob.addEditPart((EditPart) endPart);
				}

			}
		}
	}

	private PictogramElement calculateActiveContainerPe(EObject affectedElement) {
		if (affectedElement instanceof PictogramElement) {
			PictogramElement pe = (PictogramElement) affectedElement;
			if (pe instanceof org.eclipse.graphiti.mm.pictograms.ChopboxAnchor) {
				pe = ((Anchor) pe).getParent();
			}
			if (pe.isActive()) {
				return pe;
			}
		}

		GraphicsAlgorithmContainer gac = null;
		if (affectedElement instanceof GraphicsAlgorithmContainer) {
			gac = (GraphicsAlgorithmContainer) affectedElement;
		} else if (affectedElement instanceof org.eclipse.graphiti.mm.datatypes.Point) {
			// org.eclipse.graphiti.mm.datatypes.Point p =
			// (org.eclipse.graphiti.mm.datatypes.Point) affectedElement;
			// RefFeatured refImmediateComposite =
			// p.refImmediateComposite();
			// if (refImmediateComposite instanceof
			// GraphicsAlgorithmContainer) {
			// gac = (GraphicsAlgorithmContainer) refImmediateComposite;
			// }
		}

		PictogramElement ret = null;
		if (gac != null) {
			if (gac instanceof PictogramElement) {
				ret = (PictogramElement) gac;
			} else if (gac instanceof GraphicsAlgorithm) {
				ret = Graphiti.getPeService().getActiveContainerPe((GraphicsAlgorithm) gac);
			}
		}
		return ret;
	}

	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
		return null;
	}

	private DiagramRefreshJob getRefreshDiagramJob() {
		if (diagramRefreshJob == null) {
			diagramRefreshJob = new DiagramRefreshJob(Messages.DiagramEditor_0_xmsg, ed);
		}
		return diagramRefreshJob;
	}

	boolean stopListening() {
		return getRefreshDiagramJob().cancel();
	}

}

//----------------------alternative listener--------------
// listening as EContentAdapter yields tons of single events.
// the above solution works batched, which should result in better performance.

//package org.eclipse.graphiti.ui.internal.editor;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Map;
//
//import org.eclipse.emf.common.notify.Notification;
//import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.ecore.util.EContentAdapter;
//import org.eclipse.gef.EditPart;
//import org.eclipse.gef.GraphicalViewer;
//
//import org.eclipse.graphiti.mm.pictograms.Anchor;
//import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
//import org.eclipse.graphiti.mm.pictograms.Connection;
//import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
//import org.eclipse.graphiti.mm.pictograms.GraphicsAlgorithm;
//import org.eclipse.graphiti.mm.pictograms.GraphicsAlgorithmContainer;
//import org.eclipse.graphiti.mm.pictograms.PictogramElement;
//import org.eclipse.graphiti.GfwTestConfiguration;
//import org.eclipse.graphiti.ui.Messages;
//import org.eclipse.graphiti.util.PeUtil;
//import org.eclipse.graphiti.util.T;
//
///**
// * Package-private listener used to update the DiagramEditorInternal. See
// * DiagramChangeListener2 for a more efficient version.
// * 
// */
//class DiagramChangeListener extends EContentAdapter {
//	private DiagramRefreshJob diagramRefreshJob;
//	private DiagramEditorInternal ed;
//
//	DiagramChangeListener(DiagramEditorInternal ed) {
//		super();
//		this.ed = ed;
//	}
//
//	@Override
//	public void notifyChanged(Notification notification) {
//		super.notifyChanged(notification);
//
//		if (!ed.isAutoRefresh()) {
//			return;
//		}
//		if (GFTestConfiguration.isCPUProfilingTraceActive()) {
//			if (T.racer().info()) {
//				T.racer().info("DiagramChangeListener.notifyChanged(Notification notification)"); //$NON-NLS-1$
//			}
//		}
//		notify(notification);
//	}
//
//	private void addRelevantChildEditPartsToRefreshJob(PictogramElement pe) {
//		Map editPartRegistry = ed.getGraphicalViewer().getEditPartRegistry();
//		DiagramRefreshJob refreshJob = getRefreshDiagramJob();
//
//		if (pe instanceof AnchorContainer) {
//			AnchorContainer anchorContainer = (AnchorContainer) pe;
//			Collection<Anchor> anchors = anchorContainer.getAnchors();
//			for (Iterator<Anchor> iter = anchors.iterator(); iter.hasNext();) {
//				Anchor anchor = iter.next();
//				Collection<Connection> incomingConnections = anchor.getIncomingConnections();
//				for (Iterator<Connection> iterator = incomingConnections.iterator(); iterator.hasNext();) {
//					Connection connection = iterator.next();
//					refreshJob.addEditPart((EditPart) editPartRegistry.get(connection));
//					addRelevantChildEditPartsToRefreshJob(connection);
//				}
//				Collection<Connection> outgoingConnections = anchor.getOutgoingConnections();
//				for (Iterator<Connection> iterator = outgoingConnections.iterator(); iterator.hasNext();) {
//					Connection connection = iterator.next();
//					refreshJob.addEditPart((EditPart) editPartRegistry.get(connection));
//					addRelevantChildEditPartsToRefreshJob(connection);
//				}
//			}
//		}
//
//		if (pe instanceof Connection) {
//			Connection connection = (Connection) pe;
//			for (Iterator<ConnectionDecorator> iter = connection.getConnectionDecorators().iterator(); iter.hasNext();) {
//				ConnectionDecorator connectionDecorator = iter.next();
//				if (connectionDecorator.isActive()) {
//					Object object = editPartRegistry.get(connectionDecorator);
//					if (object instanceof EditPart) {
//						refreshJob.addEditPart((EditPart) object);
//					}
//				}
//			}
//		}
//	}
//
//	private PictogramElement calculateActiveContainerPe(EObject affectedElement) {
//		if (affectedElement instanceof PictogramElement) {
//			PictogramElement pe = (PictogramElement) affectedElement;
//			if (pe.isActive()) {
//				return pe;
//			}
//		}
//
//		GraphicsAlgorithmContainer gac = null;
//		if (affectedElement instanceof GraphicsAlgorithmContainer) {
//			gac = (GraphicsAlgorithmContainer) affectedElement;
//		} else if (affectedElement instanceof org.eclipse.graphiti.mm.datatypes.Point) {
//			// org.eclipse.graphiti.mm.datatypes.Point p =
//			// (org.eclipse.graphiti.mm.datatypes.Point) affectedElement;
//			// RefFeatured refImmediateComposite =
//			// p.refImmediateComposite();
//			// if (refImmediateComposite instanceof
//			// GraphicsAlgorithmContainer) {
//			// gac = (GraphicsAlgorithmContainer) refImmediateComposite;
//			// }
//		}
//
//		PictogramElement ret = null;
//		if (gac != null) {
//			if (gac instanceof PictogramElement) {
//				ret = (PictogramElement) gac;
//			} else if (gac instanceof GraphicsAlgorithm) {
//				ret = PeUtil.getActiveContainerPe((GraphicsAlgorithm) gac);
//			}
//		}
//		return ret;
//	}
//
//	private DiagramRefreshJob getRefreshDiagramJob() {
//		if (diagramRefreshJob == null) {
//			diagramRefreshJob = new DiagramRefreshJob(Messages.DiagramEditor_0_xmsg, ed);
//		}
//		return diagramRefreshJob;
//	}
//
//	private void notify(Notification notification) {
//		boolean singleEditPart = false;
//		DiagramRefreshJob refreshDiagramJob = getRefreshDiagramJob();
//		if (!refreshDiagramJob.isRefreshAll()) {
//			GraphicalViewer graphicalViewer = ed.getGraphicalViewer();
//			if (graphicalViewer == null) {
//				return;
//			}
//			Object notifier = notification.getNotifier();
//			if (notifier instanceof EObject) {
//				EObject eo = (EObject) notifier;
//				PictogramElement activeContainerPe = calculateActiveContainerPe(eo);
//				if (activeContainerPe != null) {
//					Map editPartRegistry = graphicalViewer.getEditPartRegistry();
//					Object o = editPartRegistry.get(activeContainerPe);
//					if (o instanceof EditPart) {
//						EditPart affectedEditPart = (EditPart) o;
//						refreshDiagramJob.addEditPart(affectedEditPart);
//						singleEditPart = true;
//						addRelevantChildEditPartsToRefreshJob(activeContainerPe);
//					}
//				}
//
//			}
//
//			if (!singleEditPart) {
//				refreshDiagramJob.setRefreshAll();
//			}
//		}
//
//		refreshDiagramJob.schedule(1);
//	}
//
//	boolean stopListening() {
//		return getRefreshDiagramJob().cancel();
//	}
//}