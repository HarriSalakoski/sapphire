/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.shape.def;

import org.eclipse.sapphire.modeling.ElementProperty;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementHandle;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.CountConstraint;
import org.eclipse.sapphire.modeling.annotations.Image;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlElementBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@Label( standard = "shape factory" )
@Image( path = "ShapeFactoryDef.png" )

public interface ShapeFactoryDef extends ShapeDef 
{
	ModelElementType TYPE = new ModelElementType( ShapeFactoryDef.class );
	
    // *** Property ***
    
    @Label( standard = "property" )
    @XmlBinding( path = "property" )
    @Required
    
    ValueProperty PROP_PROPERTY = new ValueProperty( TYPE, "Property" );
    
    Value<String> getProperty();
    void setProperty( String property );
    
    // *** Cases ***
    
    @Type( base = ShapeFactoryCaseDef.class )
    @CountConstraint( min = 1 )
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "case", type = ShapeFactoryCaseDef.class ) )
                             
    ListProperty PROP_CASES = new ListProperty( TYPE, "Cases" );
    
    ModelElementList<ShapeFactoryCaseDef> getCases();

    // *** Separator ***
    
	@Type
	(
		base = ShapeDef.class, 
		possible =
		{
			TextDef.class, 
			ImageDef.class, 
			ValidationMarkerDef.class, 
		    RectangleDef.class,
		    LineShapeDef.class,
		    ShapeFactoryDef.class
		}
	)
	
	@Label( standard = "separator" )
	
	@XmlElementBinding
	(
		path = "separator",
	    mappings = 
	    {
	        @XmlElementBinding.Mapping( element = "text", type = TextDef.class ),
	        @XmlElementBinding.Mapping( element = "image", type = ImageDef.class ),
	        @XmlElementBinding.Mapping( element = "validation-marker", type = ValidationMarkerDef.class ),
	        @XmlElementBinding.Mapping( element = "rectangle", type = RectangleDef.class ),
	        @XmlElementBinding.Mapping( element = "line", type = LineShapeDef.class ),
	        @XmlElementBinding.Mapping( element = "shape-factory", type = ShapeFactoryDef.class )
	    }
	)	
	
    ElementProperty PROP_SEPARATOR = new ElementProperty( TYPE, "Separator" );
    
    ModelElementHandle<ShapeDef> getSeparator();
    
}
