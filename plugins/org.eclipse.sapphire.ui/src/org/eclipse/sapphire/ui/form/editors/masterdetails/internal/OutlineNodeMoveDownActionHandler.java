/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.form.editors.masterdetails.internal;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.ui.SapphireRenderingContext;
import org.eclipse.sapphire.ui.form.editors.masterdetails.MasterDetailsContentNode;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public class OutlineNodeMoveDownActionHandler extends OutlineNodeMoveActionHandler
{
    public static final String ID = "Sapphire.Outline.Move.Down";
    
    public OutlineNodeMoveDownActionHandler()
    {
        setId( ID );
    }
    
    @SuppressWarnings( "unchecked" )
    @Override
    
    protected Object run( final SapphireRenderingContext context )
    {
        final MasterDetailsContentNode node = (MasterDetailsContentNode) getPart();
        final IModelElement modelElement = node.getModelElement();
        final ModelElementList<IModelElement> list = (ModelElementList<IModelElement>) modelElement.parent();
        
        list.moveDown( modelElement );
        
        return null;
    }
    
    @Override
    protected boolean computeEnabledState()
    {
        boolean enabled = super.computeEnabledState();
        
        if( enabled )
        {
            final IModelElement element = getModelElement();
            final ModelElementList<?> list = (ModelElementList<?>) element.parent();
            enabled = ( list.indexOf( element ) < ( list.size() - 1 ) );
        }
        
        return enabled;
    }

}
