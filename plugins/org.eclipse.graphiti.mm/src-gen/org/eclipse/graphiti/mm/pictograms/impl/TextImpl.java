/**
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
 * $Id: TextImpl.java,v 1.1 2010/06/16 13:24:59 mwenz Exp $
 */
package org.eclipse.graphiti.mm.pictograms.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.graphiti.mm.pictograms.PictogramsPackage;
import org.eclipse.graphiti.mm.pictograms.Text;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Text</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class TextImpl extends AbstractTextImpl implements Text {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TextImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PictogramsPackage.Literals.TEXT;
	}

} //TextImpl
