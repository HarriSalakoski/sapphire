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

package org.eclipse.sapphire.ui.diagram.layout.standard;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ImpliedElementProperty;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

@XmlBinding( path = "diagram-geometry" )

public interface StandardDiagramLayout extends IModelElement 
{
    ModelElementType TYPE = new ModelElementType( StandardDiagramLayout.class );
    
    // *** GridLayout ***
    
    @Type( base = DiagramGridLayout.class )
    @XmlBinding( path = "grid")
    
    ImpliedElementProperty PROP_GRID_LAYOUT = new ImpliedElementProperty( TYPE, "GridLayout" );

    DiagramGridLayout getGridLayout();    
    
    // *** GuidesLayout ***
    
    @Type( base = DiagramGuidesLayout.class )
    @XmlBinding( path = "guides")
    
    ImpliedElementProperty PROP_GUIDES_LAYOUT = new ImpliedElementProperty( TYPE, "GuidesLayout" );

    DiagramGuidesLayout getGuidesLayout();    
    
    // *** DiagramNodesLayout ***

    @Type( base = DiagramNodeLayout.class )
    
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "node", type = DiagramNodeLayout.class ) )
    
    ListProperty PROP_DIAGRAM_NODES_LAYOUT = new ListProperty( TYPE, "DiagramNodesLayout" );
    
    ModelElementList<DiagramNodeLayout> getDiagramNodesLayout();
    
    // *** DiagramConnectionsLayout ***

    @Type( base = DiagramConnectionLayout.class )
    
    @XmlListBinding( mappings = @XmlListBinding.Mapping( element = "connection", type = DiagramConnectionLayout.class ) )
    
    ListProperty PROP_DIAGRAM_CONNECTIONS_LAYOUT = new ListProperty( TYPE, "DiagramConnectionsLayout" );
    
    ModelElementList<DiagramConnectionLayout> getDiagramConnectionsLayout();

}
