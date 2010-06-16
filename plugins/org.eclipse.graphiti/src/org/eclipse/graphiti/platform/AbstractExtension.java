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
/**
 * 
 */
package org.eclipse.graphiti.platform;

/**
 * The Class AbstractExtension.
 */
public abstract class AbstractExtension implements IExtension {

	private String providerId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.platform.IExtension#getProviderId()
	 */
	final public String getProviderId() {
		return providerId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.platform.IExtension#setProviderId(java.lang.String)
	 */
	final public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

}
