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

package org.eclipse.sapphire.samples.gallery.internal;

import org.eclipse.sapphire.DisposeEvent;
import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.PropertyContentEvent;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.ui.SapphireAction;
import org.eclipse.sapphire.ui.SapphirePropertyEditorActionHandler;
import org.eclipse.sapphire.ui.SapphireRenderingContext;
import org.eclipse.sapphire.ui.def.ActionHandlerDef;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class PrefixActionHandlerForList extends SapphirePropertyEditorActionHandler
{
    private String prefix;
    
    @Override
    public void init( final SapphireAction action,
                      final ActionHandlerDef def )
    {
        super.init( action, def );
        
        this.prefix = def.getParam( "prefix" ) + " ";
        
        final IModelElement element = getModelElement();
        
        final Listener listener = new FilteredListener<PropertyContentEvent>()
        {
            @Override
            protected void handleTypedEvent( final PropertyContentEvent event )
            {
                refreshEnabledState();
                refreshCheckedState();
            }
        };
        
        final String path = getProperty().getName() + "/*";
        
        element.attach( listener, path );
        
        refreshEnabledState();
        refreshCheckedState();
        
        attach
        (
            new FilteredListener<DisposeEvent>()
            {
                @Override
                protected void handleTypedEvent( final DisposeEvent event )
                {
                    element.detach( listener, path );                        
                }
            }
        );
    }
    
    @Override
    public ListProperty getProperty()
    {
        return (ListProperty) super.getProperty();
    }

    @Override
    protected Object run( final SapphireRenderingContext context )
    {
        final ModelElementList<IModelElement> list = getModelElement().read( getProperty() );
        
        if( ! list.isEmpty() )
        {
            final IModelElement element = list.get( 0 );
            final ValueProperty property = (ValueProperty) element.properties().first().property();
            final String oldValue = element.read( property ).getText();
            final String newValue;
            
            if( isChecked() )
            {
                newValue = this.prefix + oldValue;
            }
            else
            {
                if( oldValue.startsWith( this.prefix ) )
                {
                    if( oldValue.length() == this.prefix.length() )
                    {
                        newValue = null;
                    }
                    else
                    {
                        newValue = oldValue.substring( this.prefix.length() );
                    }
                }
                else
                {
                    newValue = oldValue;
                }
            }
            
            element.write( property, newValue );
        }
        
        return null;
    }
    
    private void refreshEnabledState()
    {
        setEnabled( ! getModelElement().read( getProperty() ).isEmpty() );
    }

    private void refreshCheckedState()
    {
        boolean checked = false;
        
        final ModelElementList<IModelElement> list = getModelElement().read( getProperty() );
        
        if( ! list.isEmpty() )
        {
            final IModelElement element = list.get( 0 );
            final ValueProperty property = (ValueProperty) element.properties().first().property();
            final String value = element.read( property ).getText();
            checked = ( value != null && value.startsWith( this.prefix ) );
        }
        
        setChecked( checked );
    }
    
}
