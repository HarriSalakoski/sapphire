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

package org.eclipse.sapphire.tests.modeling.xml.binding.t0002;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface TestXmlBinding0002ModelElement extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( TestXmlBinding0002ModelElement.class );
    
    // *** TestProperty ***

    @Type( base = TestXmlBinding0002ModelElement.class )
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "", type = TestXmlBinding0002ModelElement.class ) )
    
    ListProperty PROP_TEST_PROPERTY = new ListProperty( TYPE, "TestProperty" );
    
    ModelElementList<TestXmlBinding0002ModelElement> getTestProperty();
    
}
