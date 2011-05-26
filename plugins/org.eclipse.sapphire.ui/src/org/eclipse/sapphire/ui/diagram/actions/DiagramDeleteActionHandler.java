/******************************************************************************
 * Copyright (c) 2011 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.actions;

import java.util.List;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.ui.ISapphirePart;
import org.eclipse.sapphire.ui.SapphireAction;
import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.eclipse.sapphire.ui.SapphireRenderingContext;
import org.eclipse.sapphire.ui.def.ISapphireActionHandlerDef;
import org.eclipse.sapphire.ui.diagram.SapphireDiagramActionHandler;
import org.eclipse.sapphire.ui.diagram.editor.DiagramConnectionPart;
import org.eclipse.sapphire.ui.diagram.editor.DiagramConnectionTemplate;
import org.eclipse.sapphire.ui.diagram.editor.DiagramEmbeddedConnectionTemplate;
import org.eclipse.sapphire.ui.diagram.editor.DiagramImplicitConnectionPart;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.eclipse.sapphire.ui.diagram.editor.SapphireDiagramEditorPagePart;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public class DiagramDeleteActionHandler extends SapphireDiagramActionHandler
{
	private static final String DELETE_ACTION_ID = "Sapphire.Diagram.Part.Delete";
	
    @Override
    public void init( final SapphireAction action,
                      final ISapphireActionHandlerDef def )
    {
        super.init( action, def );
        ISapphirePart part = getPart();
        setEnabled(!(part instanceof DiagramImplicitConnectionPart));
    }
	
	@Override
	public boolean canExecute(Object obj) 
	{
		return isEnabled();
	}

	@Override
	protected Object run(SapphireRenderingContext context) 
	{
		ISapphirePart part = context.getPart();
		if (part instanceof DiagramConnectionPart)
		{
			DiagramConnectionPart connPart = (DiagramConnectionPart)part;
			final IModelElement element = connPart.getLocalModelElement();
			final ModelElementList<?> list = (ModelElementList<?>) element.parent();
			list.remove(element);			
		}
		else if (part instanceof DiagramNodePart)
		{
			DiagramNodePart nodePart = (DiagramNodePart)part;
			IModelElement nodeModel = nodePart.getLocalModelElement();
			// Need to remove connection parts that are associated with this node
			deleteNodeConnections(nodePart);
			
			// Check top level connections to see whether we need to remove the connection parent element
			SapphireDiagramEditorPagePart editorPart = nodePart.getDiagramNodeTemplate().getDiagramEditorPart();
			List<DiagramConnectionTemplate> connTemplates = editorPart.getConnectionTemplates();
			for (DiagramConnectionTemplate connTemplate : connTemplates)
			{
				if (connTemplate.getConnectionType() == DiagramConnectionTemplate.ConnectionType.OneToMany)
				{
					IModelElement connParentElement = connTemplate.getConnectionParentElement(nodeModel);
					if (connParentElement != null)
					{
						ModelElementList<?> connParentList = (ModelElementList<?>)connParentElement.parent();
						connParentList.remove(connParentElement);
					}
				}
			}
			
			ModelElementList<?> list = (ModelElementList<?>) nodeModel.parent();
			list.remove(nodeModel);
			
		}
		return null;
	}

	@Override
	public boolean hasDoneModelChanges() 
	{
		return true;
	}
	
	private void deleteNodeConnections(DiagramNodePart nodePart)
	{
		IModelElement nodeModel = nodePart.getLocalModelElement();
		
		// Look for embedded connections
		DiagramEmbeddedConnectionTemplate embeddedConn = nodePart.getDiagramNodeTemplate().getEmbeddedConnectionTemplate();
		if (embeddedConn != null)
		{
			for (DiagramConnectionPart connPart : embeddedConn.getDiagramConnections(null))
			{
				if (connPart.getEndpoint1().equals(nodeModel) || 
						connPart.getEndpoint2().equals(nodeModel))
				{
					deleteConnection(connPart);
				}
			}
		}
		// Look for top level connections
		SapphireDiagramEditorPagePart diagramPart = nodePart.getDiagramNodeTemplate().getDiagramEditorPart();
		for (DiagramConnectionTemplate connTemplate : diagramPart.getConnectionTemplates())
		{
			for (DiagramConnectionPart connPart : connTemplate.getDiagramConnections(null))
			{
				if (connPart.getEndpoint1().equals(nodeModel) || 
						connPart.getEndpoint2().equals(nodeModel))
				{
					if (!(connPart instanceof DiagramImplicitConnectionPart))
					{
						deleteConnection(connPart);
					}
				}
			}
		}
	}
	
	private void deleteConnection(DiagramConnectionPart connPart)
	{
		SapphireActionHandler deleteActionHandler = connPart.getAction(DELETE_ACTION_ID).getFirstActiveHandler();
		SapphireRenderingContext renderingCtx = new SapphireRenderingContext(connPart, null);
		deleteActionHandler.execute(renderingCtx);
	}
}