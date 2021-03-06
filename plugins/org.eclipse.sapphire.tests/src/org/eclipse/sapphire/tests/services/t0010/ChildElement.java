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

package org.eclipse.sapphire.tests.services.t0010;

import org.eclipse.sapphire.Since;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface ChildElement extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( ChildElement.class );
    
    // *** ValueUnconstrained ***
    
    ValueProperty PROP_VALUE_UNCONSTRAINED = new ValueProperty( TYPE, "ValueUnconstrained" );
    
    Value<String> getValueUnconstrained();
    void setValueUnconstrained( String value );
    
    // *** ValueSince ***
    
    @Since( "3.0" )
    
    ValueProperty PROP_VALUE_SINCE = new ValueProperty( TYPE, "ValueSince" );
    
    Value<String> getValueSince();
    void setValueSince( String value );
    
    
}
