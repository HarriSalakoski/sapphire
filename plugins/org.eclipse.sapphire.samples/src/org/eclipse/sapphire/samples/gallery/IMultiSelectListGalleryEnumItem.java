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

package org.eclipse.sapphire.samples.gallery;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.NoDuplicates;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@GenerateImpl

public interface IMultiSelectListGalleryEnumItem

    extends IModelElement
    
{
    ModelElementType TYPE = new ModelElementType( IMultiSelectListGalleryEnumItem.class );
    
    // *** Item ***
    
    @Type( base = Color.class )
    @Label( standard = "color" )
    @XmlBinding( path = "" )
    @NoDuplicates

    ValueProperty PROP_ITEM = new ValueProperty( TYPE, "Item" );
    
    Value<Color> getItem();
    void setItem( String value );
    void setItem( Color value );
    
}
