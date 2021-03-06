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

package org.eclipse.sapphire.services.internal;

import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.IModelParticle;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.PropertyEnablementEvent;
import org.eclipse.sapphire.services.EnablementService;
import org.eclipse.sapphire.services.EnablementServiceData;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class ParentBasedEnablementService extends EnablementService
{
    private IModelElement parentElement;
    private ModelProperty parentProperty;
    private Listener listener;
    
    @Override
    protected void initEnablementService()
    {
        final IModelElement element = context( IModelElement.class );
        
        IModelParticle parent = element.parent();
        
        if( ! ( parent instanceof IModelElement ) )
        {
            parent = parent.parent();
        }
        
        this.parentElement = (IModelElement) parent;
        this.parentProperty = element.getParentProperty();
        
        this.listener = new FilteredListener<PropertyEnablementEvent>()
        {
            @Override
            protected void handleTypedEvent( final PropertyEnablementEvent event )
            {
                refresh();
            }
        };
        
        this.parentElement.attach( this.listener, this.parentProperty );
    }

    @Override
    public EnablementServiceData compute()
    {
        return new EnablementServiceData( this.parentElement.enabled( this.parentProperty ) );
    }

    @Override
    public void dispose()
    {
        super.dispose();
        
        if( this.listener != null )
        {
            this.parentElement.detach( this.listener, this.parentProperty );
        }
    }
    
    public static final class Factory extends ServiceFactory
    {
        @Override
        public boolean applicable( final ServiceContext context,
                                   final Class<? extends Service> service )
        {
            return ( context.find( IModelElement.class ).getParentProperty() != null );
        }

        @Override
        public Service create( final ServiceContext context,
                               final Class<? extends Service> service )
        {
            return new ParentBasedEnablementService();
        }
    }
    
}
