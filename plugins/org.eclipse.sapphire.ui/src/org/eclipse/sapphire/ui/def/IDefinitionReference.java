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

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.ReferenceValue;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.MustExist;
import org.eclipse.sapphire.modeling.annotations.Reference;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.ui.def.internal.DefinitionReferenceService;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface IDefinitionReference extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( IDefinitionReference.class );
    
    // *** Path ***
    
    @Reference( target = ISapphireUiDef.class )
    @Label( standard = "definition path" )
    @Required
    @MustExist
    @XmlBinding( path = "" )
    @Service( impl = DefinitionReferenceService.class )
    
    ValueProperty PROP_PATH = new ValueProperty( TYPE, "Path" );
    
    ReferenceValue<String,ISapphireUiDef> getPath();
    void setPath( String path );
    
}
