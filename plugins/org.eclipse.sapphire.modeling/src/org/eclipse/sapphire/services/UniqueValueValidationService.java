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

package org.eclipse.sapphire.services;

import org.eclipse.sapphire.modeling.CapitalizationType;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.IModelParticle;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.util.NLS;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public class UniqueValueValidationService extends ValidationService
{
    @Override
    public Status validate()
    {
        final Value<?> value = (Value<?>) context( IModelElement.class ).read( context( ModelProperty.class ) );
        
        if( isUniqueValue( value ) == false )
        {
            final ValueProperty property = value.property();
            final String label = property.getLabel( true, CapitalizationType.NO_CAPS, false );
            final String str = value.getText();
            final String msg = NLS.bind( Resources.message, label, str );
            return Status.createErrorStatus( msg );
        }
        
        return Status.createOkStatus();
    }
    
    protected boolean isUniqueValue( final Value<?> value )
    {
        final String str = value.getText();
        
        if( str != null )
        {
            final IModelElement modelElement = value.parent();
            final ValueProperty property = value.property();
            final IModelParticle parent = modelElement.parent();
            
            if( parent instanceof ModelElementList<?> )
            {
                final ModelElementList<?> list = (ModelElementList<?>) parent;
                
                for( IModelElement x : list )
                {
                    if( x != modelElement )
                    {
                        final Value<?> xval = x.read( property );
                        
                        if( str.equals( xval.getText() ) )
                        {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    private static final class Resources extends NLS
    {
        public static String message; 
        
        static
        {
            initializeMessages( UniqueValueValidationService.class.getName(), Resources.class );
        }
    }
    
}
