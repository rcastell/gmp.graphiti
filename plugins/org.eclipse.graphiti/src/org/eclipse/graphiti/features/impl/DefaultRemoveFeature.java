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
package org.eclipse.graphiti.features.impl;

import java.util.Iterator;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.internal.Messages;
import org.eclipse.graphiti.internal.services.GraphitiInternal;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * The Class DefaultRemoveFeature.
 */
public class DefaultRemoveFeature extends AbstractFeature implements IRemoveFeature {

	/**
	 * The Constructor.
	 * 
	 * @param fp
	 *            the fp
	 */
	public DefaultRemoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.features.IDeleteFeature#canDelete(org.eclipse.graphiti
	 * .dt.IContext)
	 */
	public boolean canRemove(IRemoveContext context) {
		return !(context.getPictogramElement() instanceof Diagram);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.features.IRemoveFeature#remove(org.eclipse.graphiti.
	 * features.context.IRemoveContext)
	 */
	public final void remove(IRemoveContext context) {
		if (!getUserDecision()) {
			return;
		}
		preRemove(context);

		PictogramElement pe = context.getPictogramElement();

		// if (pe instanceof ContainerShape) {
		// ContainerShape containerShape = (ContainerShape) pe;
		// // array instead of an unmodifiable list is necessary
		// Shape shapes[] = (Shape[]) containerShape.getChildren().toArray(new
		// Shape[0]);
		// for (Shape shape : shapes) {
		// if (shape.isActive()) {
		// IRemoveContext removeContext = new RemoveContext(shape);
		// IRemoveFeature removeFeature =
		// getFeatureProvider().getRemoveFeature(removeContext);
		// if (removeFeature != null) {
		// removeFeature.remove(removeContext);
		// }
		// }
		// }
		// }

		if (pe instanceof Shape) {
			Shape shape = (Shape) pe;
			removeAllConnections(shape);
		}

		Graphiti.getPeService().deletePictogramElement(pe);

		// IJAMFeatureProvider jamFp = getJamFeatureProvider();
		// if (jamFp != null) {
		// Link link = jamFp.getLinkForPictogramElement(pe);
		// if (link != null) {
		// link.refDelete();
		// }
		// }
		//
		// if (pe != null && MoinHelper.isObjectAlive(pe)) {
		// pe.refDelete();
		// }

		postRemove(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.features.IDeleteFeature#delete(org.eclipse.graphiti.
	 * dt.IContext)
	 */
	public void preRemove(IRemoveContext context) {
	}

	/**
	 * Removes the all connections.
	 * 
	 * @param shape
	 *            the shape
	 */
	protected void removeAllConnections(Shape shape) {
		IFeatureProvider featureProvider = getFeatureProvider();
		for (Iterator<Anchor> iter = shape.getAnchors().iterator(); iter.hasNext();) {
			Anchor anchor = iter.next();
			for (Iterator<Connection> iterator = Graphiti.getPeService().getAllConnections(anchor).iterator(); iterator.hasNext();) {
				Connection connection = iterator.next();
				if (GraphitiInternal.getEmfService().isObjectAlive(connection)) {
					IRemoveContext rc = new RemoveContext(connection);
					IRemoveFeature removeFeature = featureProvider.getRemoveFeature(rc);
					if (removeFeature != null) {
						ConnectionDecorator decorators[] = connection.getConnectionDecorators().toArray(new ConnectionDecorator[0]);
						for (ConnectionDecorator decorator : decorators) {
							if (decorator != null && GraphitiInternal.getEmfService().isObjectAlive(decorator)) {
								EcoreUtil.delete(decorator, true);
							}
						}
						removeFeature.remove(rc);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.features.IDeleteFeature#postDelete(org.eclipse.graphiti
	 * .dt.IContext)
	 */
	public void postRemove(IRemoveContext context) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.features.IFeature#canExecute(org.eclipse.graphiti
	 * .features .context.IContext)
	 */
	public boolean canExecute(IContext context) {
		boolean ret = false;
		if (context instanceof IRemoveContext) {
			ret = canRemove((IRemoveContext) context);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.features.IFeature#execute(org.eclipse.graphiti.features
	 * .context.IContext)
	 */
	public void execute(IContext context) {
		if (context instanceof IRemoveContext) {
			remove((IRemoveContext) context);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.features.impl.AbstractFeature#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	private static final String NAME = Messages.DefaultRemoveFeature_0_xfld;
}