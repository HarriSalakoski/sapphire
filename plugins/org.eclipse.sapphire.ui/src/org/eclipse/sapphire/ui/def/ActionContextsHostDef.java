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
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface ActionContextsHostDef extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( ActionContextsHostDef.class );
    
    // *** Contexts ***
    
    @Type( base = ActionContextRef.class )
    @Label( standard = "contexts" )
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "context", type = ActionContextRef.class ) )
    
    ListProperty PROP_CONTEXTS = new ListProperty( TYPE, "Contexts" );
    
    ModelElementList<ActionContextRef> getContexts();
    
}
