/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2012 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SAP AG - initial API, implementation and documentation
 *    mgorning - Bug 368124 - ConnectionDecorator with Text causes problems 
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.testtool.sketch.features;

import java.util.Collection;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

/**
 * The Class ToggleDecorator.
 */
public class ToggleDecorator extends AbstractCustomFeature {

	/**
	 * Instantiates a new toggle decorator.
	 * 
	 * @param fp
	 *            the fp
	 */
	public ToggleDecorator(IFeatureProvider fp) {
		super(fp);
	}

	public void execute(ICustomContext context) {
		Collection<Connection> connections = getDiagram().getConnections();
		for (Connection connection : connections) {
			Collection<ConnectionDecorator> decorators = connection.getConnectionDecorators();
			if (decorators.size() > 0) {
				ConnectionDecorator[] decoratorArray = decorators.toArray(new ConnectionDecorator[0]);
				for (ConnectionDecorator decorator : decoratorArray) {
					EcoreUtil.delete(decorator, true);
				}
			} else {
				IPeCreateService pecService = Graphiti.getPeCreateService();
				ConnectionDecorator decorator = pecService.createConnectionDecorator(connection, true, 0.25, true);
				IGaService gaService = Graphiti.getGaService();
				Rectangle rectangle = gaService.createRectangle(decorator);
				rectangle.setForeground(manageColor(IColorConstant.BLUE));
				rectangle.setBackground(manageColor(IColorConstant.BLUE));
				gaService.setLocationAndSize(rectangle, 0, -20, 5, 40);

				decorator = pecService.createConnectionDecorator(connection, true, 0.75, true);
				Polyline polyline = gaService.createPolyline(decorator, new int[] { 0, -20, 0, 20 });
				polyline.setForeground(manageColor(IColorConstant.RED));
				polyline.setBackground(manageColor(IColorConstant.RED));

				decorator = pecService.createConnectionDecorator(connection, false, 0.5, true);
				Text text = gaService.createText(decorator, "passive text"); //$NON-NLS-1$
				text.setForeground(manageColor(IColorConstant.BLACK));
				Graphiti.getLayoutService().setLocationAndSize(text, 25, 50, 100, 20);

				decorator = pecService.createConnectionDecorator(connection, false, 0.5, true);
				Ellipse e = gaService.createEllipse(decorator);
				e.setForeground(manageColor(IColorConstant.DARK_BLUE));
				Graphiti.getLayoutService().setLocationAndSize(e, 0, 0, 150, 150);

			}
		}

		if (getDiagram().getConnections().size() > 0) {
		}
	}

	@Override
	public String getName() {
		return "Toggle decorator";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		return true;
	}

}
