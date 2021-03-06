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

package org.eclipse.sapphire.tests.modeling.events.t0003;

import org.eclipse.sapphire.modeling.ElementProperty;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementHandle;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.Enablement;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface RootElement extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( RootElement.class );
    
    // *** Enablement ***

    @Type( base = Boolean.class )
    @DefaultValue( text = "true" )

    ValueProperty PROP_ENABLEMENT = new ValueProperty( TYPE, "Enablement" );

    Value<Boolean> getEnablement();
    void setEnablement( String value );
    void setEnablement( Boolean value );
    
    // *** Child ***

    @Type( base = ChildElement.class )
    @Required
    @Enablement( expr = "${ Enablement }" )
    
    ElementProperty PROP_CHILD = new ElementProperty( TYPE, "Child" );

    ModelElementHandle<ChildElement> getChild();

}