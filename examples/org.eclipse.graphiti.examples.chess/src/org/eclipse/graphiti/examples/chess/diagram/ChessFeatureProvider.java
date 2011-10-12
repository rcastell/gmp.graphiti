/**
 * <copyright>
 * 
 * Copyright (c) 2011, 2011 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    SAP AG - initial API, implementation and documentation
 * 
 * </copyright>
 */
package org.eclipse.graphiti.examples.chess.diagram;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.examples.chess.features.AddChessBoardFeature;
import org.eclipse.graphiti.examples.chess.features.AddChessPieceFeature;
import org.eclipse.graphiti.examples.chess.features.CreateAllInitialChessPiecesFeature;
import org.eclipse.graphiti.examples.chess.features.CreateChessBoardFeature;
import org.eclipse.graphiti.examples.chess.features.MoveChessPieceFeature;
import org.eclipse.graphiti.examples.mm.chess.Board;
import org.eclipse.graphiti.examples.mm.chess.Piece;
import org.eclipse.graphiti.examples.mm.chess.Square;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

public class ChessFeatureProvider extends DefaultFeatureProvider implements IFeatureProvider {

	public ChessFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return new ICreateFeature[] { new CreateChessBoardFeature(this), new CreateAllInitialChessPiecesFeature(this) };
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		if (context.getNewObject() instanceof Board) {
			return new AddChessBoardFeature(this);
		} else if (context.getNewObject() instanceof Piece) {
			return new AddChessPieceFeature(this);
		}
		return super.getAddFeature(context);
	}

	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if (bo instanceof Board) {
			// No removal allowed for the board
			return null;
		}
		if (bo instanceof Square) {
			// No removal allowed for squares
			return null;
		}

		return super.getRemoveFeature(context);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if (bo instanceof Board) {
			// No deletion allowed for the board
			return null;
		}
		if (bo instanceof Square) {
			// No deletion allowed for squares
			return null;
		}

		return super.getDeleteFeature(context);
	}

	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if (bo instanceof Board) {
			// No resize allowed for the board
			return null;
		}
		if (bo instanceof Square) {
			// No resize allowed for squares
			return null;
		}

		return super.getResizeShapeFeature(context);
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if (bo instanceof Square) {
			// No move allowed for squares
			return null;
		} else if (bo instanceof Piece) {
			return new MoveChessPieceFeature(this);
		}

		return super.getMoveShapeFeature(context);
	}
}
