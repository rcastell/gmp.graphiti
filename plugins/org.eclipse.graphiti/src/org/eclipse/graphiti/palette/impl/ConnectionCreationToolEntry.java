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
package org.eclipse.graphiti.palette.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.palette.IConnectionCreationToolEntry;


/**
 * creates a tool entry for the palette which can create connections.
 */
public class ConnectionCreationToolEntry extends AbstractPaletteToolEntry implements IConnectionCreationToolEntry {

	/**
	 * connection creation features are added seperately.
	 * 
	 * @param label
	 *            the label
	 * @param description
	 *            the description
	 * @param iconId
	 *            the icon id
	 * @deprecated use ConnectionCreationToolEntry(String label, String description, String iconId, String largeIconId) instead
	 */
	@Deprecated
	public ConnectionCreationToolEntry(String label, String description, String iconId) {
		this(label, description, iconId, iconId);
	}

	/**
	 * connection creation features are added seperately.
	 * 
	 * @param label
	 *            the label
	 * @param description
	 *            the description
	 * @param iconId
	 *            the icon id
	 * @param largeIconId
	 *            the large icon id
	 */
	public ConnectionCreationToolEntry(String label, String description, String iconId, String largeIconId) {
		super(label, description, iconId, largeIconId);
	}

	/**
	 * adds a create feature to the tool. you have to provide at least one. if several features are added the tool will display a popup menu
	 * after connection creation which lets the user choose between all features which canExecute.
	 * 
	 * @param createFeature
	 *            the create feature
	 */
	public void addCreateConnectionFeature(ICreateConnectionFeature createFeature) {
		features.add(createFeature);
	}

	/**
	 * Gets the create connection features.
	 * 
	 * @return the provided create connection features
	 */
	public List<IFeature> getCreateConnectionFeatures() {

		List<IFeature> createFeatures = new ArrayList<IFeature>();

		for (IFeature feature : features)
			createFeatures.add(feature);

		return createFeatures;
	}

}
