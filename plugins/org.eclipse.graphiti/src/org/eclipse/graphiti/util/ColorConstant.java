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
package org.eclipse.graphiti.util;

/**
 * The Class ColorConstant.
 */
public class ColorConstant implements IColorConstant {

	private int red;

	private int green;

	private int blue;

	/**
	 * Constructs a new instance of this class given the desired red, green and blue values expressed as ints in the range 0 to 255 (where 0
	 * is black and 255 is full brightness).
	 * 
	 * @param red
	 *            the amount of red in the color
	 * @param green
	 *            the amount of green in the color
	 * @param blue
	 *            the amount of blue in the color
	 */
	public ColorConstant(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.util.IColorConstant#getRed()
	 */
	public int getRed() {
		return red;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.util.IColorConstant#getGreen()
	 */
	public int getGreen() {
		return green;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.graphiti.util.IColorConstant#getBlue()
	 */
	public int getBlue() {
		return blue;
	}

}
