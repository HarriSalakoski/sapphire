/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ling Hao - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.swt.gef.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sapphire.ui.Bounds;
import org.eclipse.sapphire.ui.SapphirePart;
import org.eclipse.sapphire.ui.diagram.editor.ContainerShapePart;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.eclipse.sapphire.ui.diagram.editor.ShapeFactoryPart;
import org.eclipse.sapphire.ui.diagram.editor.ShapePart;
import org.eclipse.sapphire.ui.diagram.editor.TextPart;
import org.eclipse.sapphire.ui.swt.gef.model.ShapeModel.ShapeModelFactory;
import org.eclipse.sapphire.ui.swt.gef.presentation.ShapePresentation;
import org.eclipse.sapphire.ui.swt.gef.presentation.ShapePresentation.ShapePresentationFactory;

/**
 * @author <a href="mailto:ling.hao@oracle.com">Ling Hao</a>
 */

public class DiagramNodeModel extends DiagramModelBase {
	
    public final static String SOURCE_CONNECTIONS = "SOURCE_CONNECTIONS";
	public final static String TARGET_CONNECTIONS = "TARGET_CONNECTIONS";
	public final static String NODE_BOUNDS = "NODE_BOUNDS";
	public final static String SHAPE_UPDATES = "SHAPE_UPDATES";
	public final static String CHANGE_TEXT = "CHANGE_TEXT";
	public final static String SHAPE_VISIBILITY_UPDATES = "SHAPE_VISIBILITY_UPDATES";
	public final static String NODE_START_EDITING = "NODE_START_EDITING";
	
	private DiagramModel parent;
    private DiagramNodePart part;
	private List<DiagramConnectionModel> sourceConnections = new ArrayList<DiagramConnectionModel>();
	private List<DiagramConnectionModel> targetConnections = new ArrayList<DiagramConnectionModel>();
	private ShapePresentation shapePresentation;
	private ShapeModel shapeModel;
	
	public DiagramNodeModel(DiagramModel parent, DiagramNodePart part) 
	{
		this.parent = parent;
		this.part = part;
		ShapePart shapePart = this.part.getShapePart();
		this.shapePresentation = ShapePresentationFactory.createShapePresentation(null, shapePart, parent.getConfigurationManager());
		this.shapeModel = ShapeModelFactory.createShapeModel(this, null, this.shapePresentation);
	}
	
	public DiagramModel getDiagramModel() {
		return parent;
	}

	public SapphirePart getSapphirePart() {
		return getModelPart();
	}

	public DiagramNodePart getModelPart() {
		return part;
	}
		
	public String getLabel() 
	{
		ShapePart shapePart = getModelPart().getShapePart();
		if (shapePart instanceof ContainerShapePart)
		{
			ContainerShapePart containerShapePart = (ContainerShapePart)shapePart;
			List<TextPart> textParts = containerShapePart.getTextParts();
			if (!textParts.isEmpty())
			{
				return textParts.get(0).getContent();
			}
		}
		return null;
	}
			
	public Bounds getNodeBounds() {
		Bounds bounds = getModelPart().getNodeBounds();
		return bounds;
	}
	

	public void handleMoveNode() {
		firePropertyChange(NODE_BOUNDS, null, getModelPart().getNodeBounds());
	}
	
	public void handleUpdateNodeShape(ShapePart shapePart) {
		firePropertyChange(SHAPE_UPDATES, null, shapePart);
	}

	public void handleChangeText(TextPart textPart) {
		firePropertyChange(CHANGE_TEXT, null, textPart);
	}

	public void handleUpdateShapeVisibility(ShapePart shapePart) {
		firePropertyChange(SHAPE_VISIBILITY_UPDATES, null, shapePart);
	}

	public void handleAddShape(ShapePart shapePart) {
		ShapeModel parentModel = ShapeModelUtil.getChildShapeModel(getShapeModel(), (ShapePart)shapePart.getParentPart());
		assert(parentModel instanceof ShapeFactoryModel);
		((ShapeFactoryModel)parentModel).handleAddShape(shapePart);
	}

	public void handleDeleteShape(ShapePart shapePart) {
		ShapeModel parentModel = ShapeModelUtil.getChildShapeModel(getShapeModel(), (ShapePart)shapePart.getParentPart());
		assert(parentModel instanceof ShapeFactoryModel);
		((ShapeFactoryModel)parentModel).handleDeleteShape(shapePart);
	}

	public void handleReorderShapes(ShapeFactoryPart shapeFactory) {
		ShapeModel parentModel = ShapeModelUtil.getChildShapeModel(getShapeModel(), shapeFactory);
		assert(parentModel instanceof ShapeFactoryModel);
		((ShapeFactoryModel)parentModel).handleReorderShapes(shapeFactory);
	}

	public List<DiagramConnectionModel> getSourceConnections() {
		return sourceConnections;
	}

	public List<DiagramConnectionModel> getTargetConnections() {
		return targetConnections;
	}

	public void addSourceConnection(DiagramConnectionModel connection) {
		sourceConnections.add(connection);
		firePropertyChange(SOURCE_CONNECTIONS, null, connection);
	}
	
	public void addTargetConnection(DiagramConnectionModel connection) {
		targetConnections.add(connection);
		firePropertyChange(TARGET_CONNECTIONS, null, connection);
	}

	public void removeSourceConnection(DiagramConnectionModel connection) {
		sourceConnections.remove(connection);
		firePropertyChange(SOURCE_CONNECTIONS, null, connection);
	}
	
	public void removeTargetConnection(DiagramConnectionModel connection) {
		targetConnections.remove(connection);
		firePropertyChange(TARGET_CONNECTIONS, null, connection);
	}

	public void handleStartEditing() {
		firePropertyChange(NODE_START_EDITING, null, null);
	}
	
	@Override
	public String toString() {
		return getLabel();
	}

	public ShapeModel getShapeModel()
	{
		return this.shapeModel;
	}
	
	public ShapePresentation getShapePresentation()
	{
		return this.shapePresentation;
	}
		
}
