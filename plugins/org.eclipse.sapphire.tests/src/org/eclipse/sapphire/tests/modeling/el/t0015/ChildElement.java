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

package org.eclipse.sapphire.tests.modeling.el.t0015;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Enablement;
import org.eclipse.sapphire.modeling.annotations.Type;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface ChildElement extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( ChildElement.class );
    
    // *** EnableValue ***
    
    @Type( base = Boolean.class )

    ValueProperty PROP_ENABLE_VALUE = new ValueProperty( TYPE, "EnableValue" );
    
    Value<Boolean> getEnableValue();
    void setEnableValue( String value );
    void setEnableValue( Boolean value );
    
    // *** Value ***
    
    @Enablement( expr = "${ EnableValue }" )
    
    ValueProperty PROP_VALUE = new ValueProperty( TYPE, "Value" );
    
    Value<String> getValue();
    void setValue( String value );
    
}
