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

package org.eclipse.sapphire.ui.def;

import org.eclipse.sapphire.java.JavaType;
import org.eclipse.sapphire.java.JavaTypeName;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.ReferenceValue;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Documentation;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Reference;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@Label( standard = "action handler filter" )
@GenerateImpl

public interface ActionHandlerFilterDef extends ActionContextsHostDef
{
    ModelElementType TYPE = new ModelElementType( ActionHandlerFilterDef.class );
    
    // *** ImplClass ***
    
    @Type( base = JavaTypeName.class )
    @Reference( target = JavaType.class )
    @Label( standard = "implementation class" )
    @Required
    @XmlBinding( path = "impl" )
    
    @Documentation( content = "The action handler filter implementation class. Must extend SapphireActionHandlerFilter." )

    ValueProperty PROP_IMPL_CLASS = new ValueProperty( TYPE, "ImplClass" );
    
    ReferenceValue<JavaTypeName,JavaType> getImplClass();
    void setImplClass( String value );
    void setImplClass( JavaTypeName value );
    
    // *** Contexts ***
    
    @Documentation
    ( 
        content = "Every UI part that supports actions will define one or more context. An action handler filter can be " +
                  "constrained to apply only to the specified contexts. If no context is specified, the " +
                  "action handler filter will be treated as applicable to all contexts."
    )

    ListProperty PROP_CONTEXTS = new ListProperty( TYPE, ActionSystemPartDef.PROP_CONTEXTS );
    
}
