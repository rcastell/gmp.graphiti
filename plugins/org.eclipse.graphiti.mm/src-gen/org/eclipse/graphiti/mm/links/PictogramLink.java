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
 * $Id: PictogramLink.java,v 1.1 2010/06/16 13:24:54 mwenz Exp $
 */
package org.eclipse.graphiti.mm.links;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PropertyContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pictogram Link</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.graphiti.mm.links.PictogramLink#getPictogramElement <em>Pictogram Element</em>}</li>
 *   <li>{@link org.eclipse.graphiti.mm.links.PictogramLink#getDiagramLink <em>Diagram Link</em>}</li>
 *   <li>{@link org.eclipse.graphiti.mm.links.PictogramLink#getBusinessObjects <em>Business Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.graphiti.mm.links.LinksPackage#getPictogramLink()
 * @model
 * @generated
 */
public interface PictogramLink extends PropertyContainer {
	/**
	 * Returns the value of the '<em><b>Pictogram Element</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.graphiti.mm.pictograms.PictogramElement#getLink <em>Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pictogram Element</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pictogram Element</em>' container reference.
	 * @see #setPictogramElement(PictogramElement)
	 * @see org.eclipse.graphiti.mm.links.LinksPackage#getPictogramLink_PictogramElement()
	 * @see org.eclipse.graphiti.mm.pictograms.PictogramElement#getLink
	 * @model opposite="link" transient="false" ordered="false"
	 * @generated
	 */
	PictogramElement getPictogramElement();

	/**
	 * Sets the value of the '{@link org.eclipse.graphiti.mm.links.PictogramLink#getPictogramElement <em>Pictogram Element</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pictogram Element</em>' container reference.
	 * @see #getPictogramElement()
	 * @generated
	 */
	void setPictogramElement(PictogramElement value);

	/**
	 * Returns the value of the '<em><b>Diagram Link</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.graphiti.mm.links.DiagramLink#getPictogramLinks <em>Pictogram Links</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Link</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Link</em>' reference.
	 * @see #setDiagramLink(DiagramLink)
	 * @see org.eclipse.graphiti.mm.links.LinksPackage#getPictogramLink_DiagramLink()
	 * @see org.eclipse.graphiti.mm.links.DiagramLink#getPictogramLinks
	 * @model opposite="pictogramLinks" required="true" ordered="false"
	 * @generated
	 */
	DiagramLink getDiagramLink();

	/**
	 * Sets the value of the '{@link org.eclipse.graphiti.mm.links.PictogramLink#getDiagramLink <em>Diagram Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Link</em>' reference.
	 * @see #getDiagramLink()
	 * @generated
	 */
	void setDiagramLink(DiagramLink value);

	/**
	 * Returns the value of the '<em><b>Business Objects</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Business Objects</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Business Objects</em>' reference list.
	 * @see org.eclipse.graphiti.mm.links.LinksPackage#getPictogramLink_BusinessObjects()
	 * @model
	 * @generated
	 */
	EList<EObject> getBusinessObjects();

} // PictogramLink
