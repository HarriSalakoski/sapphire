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
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@Label( standard = "properties view contribution" )

public interface IPropertiesViewContributionDef extends PartDef
{
    ModelElementType TYPE = new ModelElementType( IPropertiesViewContributionDef.class );
    
    // *** Pages ***
    
    @Type( base = IPropertiesViewContributionPageDef.class )
    @Label( standard = "pages" )
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "page", type = IPropertiesViewContributionPageDef.class ) )
    
    ListProperty PROP_PAGES = new ListProperty( TYPE, "Pages" );
    
    ModelElementList<IPropertiesViewContributionPageDef> getPages();

}
