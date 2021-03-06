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

package org.eclipse.sapphire.tests.modeling.el.t0008;

import org.eclipse.sapphire.modeling.ElementProperty;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementHandle;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Type;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface TestModelRoot extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( TestModelRoot.class );
    
    // *** Integer ***
    
    @Type( base = Integer.class )
    
    ValueProperty PROP_INTEGER = new ValueProperty( TYPE, "Integer" );
    
    Value<Integer> getInteger();
    void setInteger( String value );
    void setInteger( Integer value );
    
    // *** Element ***
    
    @Type( base = TestModelElement.class, possible = { TestModelElementA.class, TestModelElementB.class } )
    
    ElementProperty PROP_ELEMENT = new ElementProperty( TYPE, "Element" );
    
    ModelElementHandle<TestModelElement> getElement();
    
}
