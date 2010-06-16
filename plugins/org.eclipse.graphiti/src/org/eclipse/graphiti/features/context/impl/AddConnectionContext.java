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
package org.eclipse.graphiti.features.context.impl;

import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;

/**
 * The Class AddConnectionContext.
 */
public class AddConnectionContext extends AddContext implements IAddConnectionContext {

	/**
	 * The target anchor.
	 */
	Anchor sourceAnchor, targetAnchor;

	/**
	 * Instantiates a new adds the connection context.
	 * 
	 * @param sourceAnchor
	 *            the source anchor
	 * @param targetAnchor
	 *            the target anchor
	 */
	public AddConnectionContext(Anchor sourceAnchor, Anchor targetAnchor) {
		super();
		this.sourceAnchor = sourceAnchor;
		this.targetAnchor = targetAnchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.features.context.IConnectionContext#getSourceAnchor()
	 */
	public Anchor getSourceAnchor() {
		return sourceAnchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.features.context.IConnectionContext#getTargetAnchor()
	 */
	public Anchor getTargetAnchor() {
		return targetAnchor;
	}

	@Override
	public String toString() {
		String ret = super.toString();
		return ret + " sourceAnchore: " + getSourceAnchor() + " targetAnchor: " + getTargetAnchor(); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
