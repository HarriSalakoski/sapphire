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

package org.eclipse.sapphire.ui.diagram.def;

import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.localization.Localizable;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.ui.def.PartDef;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public interface IDiagramNodeDefaultActionDef extends PartDef
{
    ModelElementType TYPE = new ModelElementType( IDiagramNodeDefaultActionDef.class );
    
    // *** Label ***
    
    @Label( standard = "label" )
    @Required
    @Localizable
    @XmlBinding( path = "label" )
    
    ValueProperty PROP_LABEL = new ValueProperty( TYPE, "Label" );
    
    Value<String> getLabel();
    void setLabel( String value );    
    
    // *** Description ***
    
    @Label( standard = "description" )
    @Localizable
    @XmlBinding( path = "description" )
    
    ValueProperty PROP_DESCRIPTION = new ValueProperty( TYPE, "Description" );
    
    Value<String> getDescription();
    void setDescription( String value );    

    // *** ActionId ***
    
    @Label( standard = "action id" )
    @Required
    @XmlBinding( path = "action-id" )
    
    ValueProperty PROP_ACTION_ID = new ValueProperty( TYPE, "ActionId" );
    
    Value<String> getActionId();
    void setActionId( String value );
    
    // *** ActionHandlerId ***
    
    @Label( standard = "action handler id" )
    @XmlBinding( path = "action-handler-id" )
    
    ValueProperty PROP_ACTION_HANDLER_ID = new ValueProperty( TYPE, "ActionHandlerId" );
    
    Value<String> getActionHandlerId();
    void setActionHandlerId( String value );    
    
}
