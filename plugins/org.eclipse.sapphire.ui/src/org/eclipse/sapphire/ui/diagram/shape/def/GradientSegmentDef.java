/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.shape.def;

import org.eclipse.sapphire.Color;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.NumericRange;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

@Label( standard = "gradient segment" )

public interface GradientSegmentDef extends IModelElement 
{
	ModelElementType TYPE = new ModelElementType( GradientSegmentDef.class );
	
	// *** Color ***
	
    @Type( base = Color.class )
    @Label( standard = "color")
    @Required
    @XmlBinding( path = "color")
    
    ValueProperty PROP_COLOR = new ValueProperty( TYPE, "Color" );
    
    Value<Color> getColor();
    void setColor( String value );
    void setColor( Color value );
	
	// *** Extent ***
    
    @Type( base = Integer.class )
    @Label( standard = "extent" )
    @Required
    @XmlBinding( path = "extent" )
    @NumericRange( min = "0", max = "100")
    
    ValueProperty PROP_EXTENT = new ValueProperty( TYPE, "Extent" );
    
    Value<Integer> getExtent();
    void setExtent( String value );
    void setExtent( Integer value );
}
