/******************************************************************************
 * Copyright (c) 2011 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.actions;

import org.eclipse.sapphire.ui.SapphireAction;
import org.eclipse.sapphire.ui.SapphireRenderingContext;
import org.eclipse.sapphire.ui.def.ISapphireActionHandlerDef;
import org.eclipse.sapphire.ui.diagram.SapphireDiagramActionHandler;
import org.eclipse.sapphire.ui.diagram.def.IDiagramImageChoice;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodeTemplate;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public class DiagramNodeAddActionHandler extends SapphireDiagramActionHandler 
{
	private DiagramNodeTemplate nodeTemplate;
	
	public DiagramNodeAddActionHandler(DiagramNodeTemplate nodeTemplate)
	{
		this.nodeTemplate = nodeTemplate;
	}
	
    @Override
    public void init( final SapphireAction action,
                      final ISapphireActionHandlerDef def )
    {
    	super.init(action, def);
		if (this.nodeTemplate.getToolPaletteLabel() != null)
		{
			setLabel(this.nodeTemplate.getToolPaletteLabel());
		}    	
    }
    
	@Override
	public boolean canExecute(Object obj) 
	{
		return true;
	}

	@Override
	protected Object run(SapphireRenderingContext context) 
	{
		this.nodeTemplate.removeModelLister();
		DiagramNodePart nodePart = this.nodeTemplate.createNewDiagramNode();
		this.nodeTemplate.addModelListener();
		return nodePart;
	}	
	
	public String getImageId()
	{
		return this.nodeTemplate.getToolPaletteImage().getImageId().getContent();
	}
}