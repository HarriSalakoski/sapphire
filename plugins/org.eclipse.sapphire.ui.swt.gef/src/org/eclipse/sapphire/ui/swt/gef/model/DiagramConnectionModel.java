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

import org.eclipse.sapphire.ui.SapphirePart;
import org.eclipse.sapphire.ui.diagram.editor.DiagramConnectionPart;

/**
 * @author <a href="mailto:ling.hao@oracle.com">Ling Hao</a>
 */

public class DiagramConnectionModel extends DiagramModelBase {
	
	public final static String CONNECTION_UPDATES = "CONNECTION_UPDATES";
	public final static String CONNECTION_LABEL_MOVED = "CONNECTION_LABEL_MOVED";
	public final static String CONNECTION_BEND_POINTS = "CONNECTION_BEND_POINTS";
	public final static String CONNECTION_START_EDITING = "CONNECTION_START_EDITING";

	private DiagramModel parent;
	private DiagramConnectionPart part;
	
	private DiagramNodeModel sourceNode;
	private DiagramNodeModel targetNode;

	public DiagramConnectionModel(DiagramModel parent, DiagramConnectionPart part) {
		this.parent = parent;
		this.part = part;
	}
	
	public DiagramModel getDiagramModel() {
		return parent;
	}
	
	public SapphirePart getSapphirePart() {
		return getModelPart();
	}

	public DiagramConnectionPart getModelPart() {
		return part;
	}
	
	public DiagramNodeModel getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(DiagramNodeModel sourceNode) {
		this.sourceNode = sourceNode;
	}

	public DiagramNodeModel getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(DiagramNodeModel targetNode) {
		this.targetNode = targetNode;
	}

	public void handleUpdateConnection() {
		firePropertyChange(CONNECTION_UPDATES, null, getModelPart().getLabel());
	}
	
	public void handleUpdateConnectionMoveLabel() {
		firePropertyChange(CONNECTION_LABEL_MOVED, null, getModelPart());
	}
	
	public void handleUpdateBendPoints() {
		firePropertyChange(CONNECTION_BEND_POINTS, null, getModelPart());
	}

	public void handleStartEditing() {
		firePropertyChange(CONNECTION_START_EDITING, null, null);
	}
}
