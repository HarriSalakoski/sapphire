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

import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.RequiredConstraintService;
import org.eclipse.sapphire.modeling.ElementProperty;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ImpliedElementProperty;
import org.eclipse.sapphire.modeling.LoggingService;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.el.FailSafeFunction;
import org.eclipse.sapphire.modeling.el.Function;
import org.eclipse.sapphire.modeling.el.FunctionResult;
import org.eclipse.sapphire.modeling.el.Literal;
import org.eclipse.sapphire.modeling.el.ModelElementFunctionContext;
import org.eclipse.sapphire.modeling.el.parser.ExpressionLanguageParser;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;

/**
 * {@link RequiredConstraintService} implementation that derives its behavior from @{@link Required} annotation.
 * 
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class DeclarativeRequiredConstraintService extends RequiredConstraintService
{
    private FunctionResult functionResult;
    
    @Override
    protected void initRequiredConstraintService()
    {
        Function function;
        
        final Required annotation = context( ModelProperty.class ).getAnnotation( Required.class );
        
        if( annotation == null )
        {
            function = Literal.FALSE;
        }
        else
        {
            final String expr = annotation.value().trim();
            
            if( expr.length() == 0 )
            {
                function = Literal.TRUE;
            }
            else
            {
                try
                {
                    function = ExpressionLanguageParser.parse( expr );
                    function = FailSafeFunction.create( function, Boolean.class, false );
                }
                catch( Exception e )
                {
                    LoggingService.log( e );
                    function = Literal.FALSE;
                }
            }
        }

        final ModelElementFunctionContext context = new ModelElementFunctionContext( context( IModelElement.class ) );
        
        this.functionResult = function.evaluate( context );
        
        final Listener listener = new Listener()
        {
            @Override
            public void handle( final Event event )
            {
                refresh();
            }
        };
        
        this.functionResult.attach( listener );
    }

    @Override
    protected Boolean compute()
    {
        return (Boolean) this.functionResult.value();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        
        if( this.functionResult != null )
        {
            try
            {
                this.functionResult.dispose();
            }
            catch( Exception e )
            {
                LoggingService.log( e );
            }
        }
    }
    
    public static final class Factory extends ServiceFactory
    {
        @Override
        public boolean applicable( final ServiceContext context,
                                   final Class<? extends Service> service )
        {
            final ModelProperty property = context.find( ModelProperty.class );
            return ( property instanceof ValueProperty || ( property instanceof ElementProperty && ! ( property instanceof ImpliedElementProperty ) ) );
        }

        @Override
        public Service create( final ServiceContext context,
                               final Class<? extends Service> service )
        {
            return new DeclarativeRequiredConstraintService();
        }
    }
    
}
