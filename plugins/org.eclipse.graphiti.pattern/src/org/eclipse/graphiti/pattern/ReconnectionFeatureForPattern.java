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
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.impl.AbstractFeature;
import org.eclipse.graphiti.func.IReconnection;
import org.eclipse.graphiti.internal.Messages;

/**
 * The Class ReconnectionFeatureForPattern.
 */
public class ReconnectionFeatureForPattern extends AbstractFeature implements IReconnectionFeature {

	private IReconnection deletegate;

	/**
	 * Instantiates a new reconnection feature for pattern.
	 * 
	 * @param fp
	 *            the fp
	 * @param pattern
	 *            the pattern
	 */
	public ReconnectionFeatureForPattern(IFeatureProvider fp, IReconnection pattern) {
		super(fp);
		this.deletegate = pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.func.IReconnection#canReconnect(org.eclipse.graphiti
	 * .features.context.IReconnectionContext)
	 */
	public boolean canReconnect(IReconnectionContext context) {
		return deletegate.canReconnect(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.func.IReconnection#postReconnnect(org.eclipse.graphiti
	 * .features.context.IReconnectionContext)
	 */
	public void postReconnect(IReconnectionContext context) {
		deletegate.postReconnect(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.func.IReconnection#preReconnnect(org.eclipse.graphiti
	 * .features.context.IReconnectionContext)
	 */
	public void preReconnect(IReconnectionContext context) {
		deletegate.preReconnect(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.func.IReconnection#reconnnect(org.eclipse.graphiti
	 * .features.context.IReconnectionContext)
	 */
	public void reconnect(IReconnectionContext context) {
		deletegate.reconnect(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.graphiti.features.IFeature#canExecute(org.eclipse.graphiti
	 * .features.context.IContext)
	 */
	public boolean canExecute(IContext context) {
		boolean ret = false;
		if (context instanceof IReconnectionContext) {
			ret = canReconnect((IReconnectionContext) context);
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
		if (context instanceof IReconnectionContext) {
			reconnect((IReconnectionContext) context);
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

	private static final String NAME = Messages.ReconnectionFeatureForPattern_0_xfld;

}
