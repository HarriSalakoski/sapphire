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

package org.eclipse.sapphire.samples.architecture;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.ReferenceValue;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.LongString;
import org.eclipse.sapphire.modeling.annotations.PossibleValues;
import org.eclipse.sapphire.modeling.annotations.Reference;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;
import org.eclipse.sapphire.samples.architecture.internal.ComponentReferenceService;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface ComponentDependency extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( ComponentDependency.class );
    
    // *** Name ***
    
    @Reference( target = Component.class )
    @Service( impl = ComponentReferenceService.class )
    @Required
    @PossibleValues( property = "/Components/Name" )
    @XmlBinding( path = "name" )

    ValueProperty PROP_NAME = new ValueProperty( TYPE, "Name" );

    ReferenceValue<String,Component> getName();
    void setName( String value );
    
    // *** Description ***
    
    @LongString
    @XmlBinding( path = "description" )
    
    ValueProperty PROP_DESCRIPTION = new ValueProperty( TYPE, "Description" );
    
    Value<String> getDescription();
    void setDescription( String value );
    
    // *** ConnectionBendpoints***

    @Type( base = ConnectionBendpoint.class )
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "bendpoint", type = ConnectionBendpoint.class ) )
    
    ListProperty PROP_CONNECTION_BENDPOINTS = new ListProperty( TYPE, "ConnectionBendPoints" );
    
    ModelElementList<ConnectionBendpoint> getConnectionBendpoints();
    
}
