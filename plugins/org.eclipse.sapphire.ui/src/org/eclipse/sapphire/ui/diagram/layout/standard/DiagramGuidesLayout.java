/******************************************************************************
 * Copyright (c) 2013 Oracle and Liferay
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 *    Gregory Amerson - [377388] IDiagram{Guides/Grids}Def visible property does not affect StandardDiagramLayout persistence
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.layout.standard;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 * @author <a href="mailto:gregory.amerson@liferay.com">Gregory Amerson</a>
 */

public interface DiagramGuidesLayout extends IModelElement 
{
	ModelElementType TYPE = new ModelElementType( DiagramGuidesLayout.class);

	// *** Visible ***
	
	@Type( base = Boolean.class )
	@XmlBinding( path = "visible" )
	@Label( standard = "show guides")
	
	ValueProperty PROP_VISIBLE = new ValueProperty(TYPE, "Visible");
	
	Value<Boolean> isVisible();
	void setVisible( String value );
	void setVisible( Boolean value );

}
