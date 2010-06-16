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
package org.eclipse.graphiti.pattern;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;

/**
 * The Class LayoutFeatureForPattern.
 */
public class LayoutFeatureForPattern extends AbstractLayoutFeature {
	private IFeatureForPattern delegate;

	/**
	 * Instantiates a new layout feature for pattern.
	 * 
	 * @param featureProvider
	 *            the feature provider
	 * @param pattern
	 *            the pattern
	 */
	public LayoutFeatureForPattern(IFeatureProvider featureProvider, IPattern pattern) {
		super(featureProvider);
		delegate = new FeatureForPatternDelegate(pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.func.ILayout#canLayout(org.eclipse.graphiti.features.context.ILayoutContext)
	 */
	public boolean canLayout(ILayoutContext context) {
		IPattern pattern = delegate.getPattern();
		return pattern.canLayout(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.func.ILayout#layout(org.eclipse.graphiti.features.context.ILayoutContext)
	 */
	public boolean layout(ILayoutContext context) {
		return delegate.getPattern().layout(context);
	}
}
