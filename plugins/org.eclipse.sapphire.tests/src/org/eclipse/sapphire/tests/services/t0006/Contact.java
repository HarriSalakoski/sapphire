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

package org.eclipse.sapphire.tests.services.t0006;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@Service( impl = FakeNameValidationService.class )

public interface Contact extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( Contact.class );
    
    // *** FirstName ***
    
    @Required
    
    ValueProperty PROP_FIRST_NAME = new ValueProperty( TYPE, "FirstName" );
    
    Value<String> getFirstName();
    void setFirstName( String value );
    
    // *** LastName ***
    
    @Required
    
    ValueProperty PROP_LAST_NAME = new ValueProperty( TYPE, "LastName" );
    
    Value<String> getLastName();
    void setLastName( String value );
    
}
