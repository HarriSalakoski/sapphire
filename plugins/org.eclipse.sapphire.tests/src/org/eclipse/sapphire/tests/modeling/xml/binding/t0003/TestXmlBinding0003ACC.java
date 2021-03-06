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

package org.eclipse.sapphire.tests.modeling.xml.binding.t0003;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlNamespace;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@XmlNamespace( uri = "http://www.eclipse.org/sapphire/tests/xml/binding/0003/v", prefix = "v" )

public interface TestXmlBinding0003ACC extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( TestXmlBinding0003ACC.class );
    
    // *** Acca ***
    
    @XmlBinding( path = "v:acca" )
    
    ValueProperty PROP_ACCA = new ValueProperty( TYPE, "Acca" );
    
    Value<String> getAcca();
    void setAcca( String value );
    
}
