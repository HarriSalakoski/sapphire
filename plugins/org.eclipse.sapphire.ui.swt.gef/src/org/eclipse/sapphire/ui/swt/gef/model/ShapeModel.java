/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.swt.gef.model;

import org.eclipse.sapphire.ui.SapphirePart;
import org.eclipse.sapphire.ui.swt.gef.presentation.ImagePresentation;
import org.eclipse.sapphire.ui.swt.gef.presentation.RectanglePresentation;
import org.eclipse.sapphire.ui.swt.gef.presentation.ShapeFactoryPresentation;
import org.eclipse.sapphire.ui.swt.gef.presentation.ShapePresentation;
import org.eclipse.sapphire.ui.swt.gef.presentation.TextPresentation;
import org.eclipse.sapphire.ui.swt.gef.presentation.ValidationMarkerPresentation;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public class ShapeModel extends DiagramModelBase 
{
	private DiagramNodeModel nodeModel;
	private ShapeModel parent;
	private ShapePresentation shapePresentation;
    public final static String SHAPE_START_EDITING = "SHAPE_START_EDITING";

    public ShapeModel(DiagramNodeModel nodeModel, ShapeModel parent, ShapePresentation shapePresentation)
    {
    	this.nodeModel = nodeModel;
    	this.parent = parent;
    	this.shapePresentation = shapePresentation;
    }
    
	@Override
	public SapphirePart getSapphirePart() 
	{		
		return this.shapePresentation.getPart();
	}

	public ShapePresentation getShapePresentation()
	{
		return this.shapePresentation;
	}
	
	public ShapeModel getParent()
	{
		return this.parent;
	}
	
	public DiagramNodeModel getNodeModel()
	{
		return this.nodeModel;
	}
	
	public void handleDirectEditing()
	{
		firePropertyChange(SHAPE_START_EDITING, null, null);
	}
	
    @SuppressWarnings( "unchecked" )
    public final <T> T nearest( final Class<T> partType )
    {
        if( partType.isAssignableFrom( getClass() ) )
        {
            return (T) this;
        }
        else
        {
            if( this.parent != null )
            {
                return this.parent.nearest( partType );
            }
            else
            {
                return null;
            }
        }
    }
    
    public static final class ShapeModelFactory
    {
    	public static ShapeModel createShapeModel(DiagramNodeModel nodeModel, ShapeModel parent, ShapePresentation shapePresentation)
    	{
    		ShapeModel childModel = null;
        	if (shapePresentation instanceof TextPresentation)
        	{
    	        childModel = new TextModel(nodeModel, parent, (TextPresentation)shapePresentation);
        	}
        	else if (shapePresentation instanceof ImagePresentation)
        	{
        		childModel = new ImageModel(nodeModel, parent, (ImagePresentation)shapePresentation);
        	}
        	else if (shapePresentation instanceof ValidationMarkerPresentation)
        	{
        		childModel = new ValidationMarkerModel(nodeModel, parent, (ValidationMarkerPresentation)shapePresentation);
        	}
        	else if (shapePresentation instanceof RectanglePresentation)
        	{
        		childModel = new RectangleModel(nodeModel, parent, (RectanglePresentation)shapePresentation);
        	}
        	else if (shapePresentation instanceof ShapeFactoryPresentation)
        	{
            	childModel = new ShapeFactoryModel(nodeModel, parent, (ShapeFactoryPresentation)shapePresentation);        		
        	}
    		return childModel;
    	}
    	
   	
    }
}
