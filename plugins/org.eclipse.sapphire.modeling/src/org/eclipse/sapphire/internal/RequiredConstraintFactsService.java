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

package org.eclipse.sapphire.internal;

import java.util.SortedSet;

import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.RequiredConstraintService;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.util.NLS;
import org.eclipse.sapphire.services.FactsService;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;

/**
 * {@link FactsService} implementation that contributes fact statements based on semantical
 * information from {@link RequiredConstraintService}.
 * 
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class RequiredConstraintFactsService extends FactsService
{
    private IModelElement element;
    private ModelProperty property;
    private RequiredConstraintService requiredConstraintService;
    private Listener listener;
    
    @Override
    protected void init()
    {
        super.init();
        
        this.element = context( IModelElement.class );
        this.property = context( ModelProperty.class );
        this.requiredConstraintService = this.element.service( this.property, RequiredConstraintService.class );
        
        this.listener = new Listener()
        {
            @Override
            public void handle( final Event event )
            {
                broadcast();
            }
        };
        
        this.requiredConstraintService.attach( this.listener );
    }

    @Override
    protected void facts( final SortedSet<String> facts )
    {
        if( this.requiredConstraintService.required() )
        {
            boolean applicable = true;
            
            if( this.property instanceof ValueProperty )
            {
                final Value<?> value = this.element.read( (ValueProperty) this.property );
                
                if( value.getDefaultText() != null )
                {
                    applicable = false;
                }
            }
            
            if( applicable )
            {
                facts.add( Resources.statement );
            }
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if( this.listener != null )
        {
            this.requiredConstraintService.detach( this.listener );
        }
    }

    public static final class Factory extends ServiceFactory
    {
        @Override
        public boolean applicable( final ServiceContext context,
                                   final Class<? extends Service> service )
        {
            final IModelElement element = context.find( IModelElement.class );
            final ModelProperty property = context.find( ModelProperty.class );
            return ( element != null && property != null && element.service( property, RequiredConstraintService.class ) != null );
        }
    
        @Override
        public Service create( final ServiceContext context,
                               final Class<? extends Service> service )
        {
            return new RequiredConstraintFactsService();
        }
    }
    
    private static final class Resources extends NLS
    {
        public static String statement;
        
        static
        {
            initializeMessages( RequiredConstraintFactsService.class.getName(), Resources.class );
        }
    }
    
}
