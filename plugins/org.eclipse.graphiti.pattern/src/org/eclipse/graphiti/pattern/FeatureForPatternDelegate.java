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

/**
 * The Class FeatureForPatternDelegate.
 */
public class FeatureForPatternDelegate implements IFeatureForPattern {

	private IPattern pattern;

	/**
	 * Instantiates a new feature for pattern delegate.
	 * 
	 * @param pattern
	 *            the pattern
	 */
	public FeatureForPatternDelegate(IPattern pattern) {
		super();
		setPattern(pattern);
	}

	/**
	 * Gets the pattern.
	 * 
	 * @return Returns the pattern.
	 */
	public IPattern getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            The pattern to set.
	 */
	private void setPattern(IPattern pattern) {
		this.pattern = pattern;
	}

}
