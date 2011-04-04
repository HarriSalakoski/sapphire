/******************************************************************************
 * Copyright (c) 2011 Oracle
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
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.java.JavaTypeName;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@GenerateImpl
@Label( standard = "item" )

public interface IListItemWithJavaType

    extends IModelElement
    
{
    ModelElementType TYPE = new ModelElementType( IListItemWithJavaType.class );
    
    // *** StringValue ***
    
    @Type( base = JavaTypeName.class )
    @Label( standard = "java type" )
    @XmlBinding( path = "java-type" )
    
    ValueProperty PROP_JAVA_TYPE = new ValueProperty( TYPE, "JavaType" );
    
    Value<JavaTypeName> getJavaType();
    void setJavaType( String value );
    void setJavaType( JavaTypeName value );

}