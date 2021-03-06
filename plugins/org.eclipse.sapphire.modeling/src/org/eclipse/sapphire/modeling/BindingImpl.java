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

package org.eclipse.sapphire.modeling;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public abstract class BindingImpl
{
    private IModelElement element;
    private ModelProperty property;

    public void init( final IModelElement element,
                      final ModelProperty property,
                      final String[] params )
    {
        if( element == null )
        {
            throw new IllegalArgumentException();
        }
        
        if( property == null )
        {
            throw new IllegalArgumentException();
        }
        
        this.element = element;
        this.property = property;
    }
    
    public final IModelElement element()
    {
        return this.element;
    }
    
    public ModelProperty property()
    {
        return this.property;
    }
    
    public void dispose()
    {
        // The default implementation doesn't do anything.
    }

}
