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

import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@Label( standard = "tab group" )
@XmlBinding( path = "tab-group" )

public interface TabGroupDef extends FormComponentDef
{
    ModelElementType TYPE = new ModelElementType( TabGroupDef.class );
    
    // *** Tabs ***
    
    @Label( standard = "tabs" )
    @Type( base = TabGroupPageDef.class )
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "tab", type = TabGroupPageDef.class ) )
    
    ListProperty PROP_TABS = new ListProperty( TYPE, "Tabs" );
    
    ModelElementList<TabGroupPageDef> getTabs();
    
}
