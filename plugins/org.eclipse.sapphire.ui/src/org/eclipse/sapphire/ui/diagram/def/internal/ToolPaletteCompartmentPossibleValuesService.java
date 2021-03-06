/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.def.internal;

import java.util.Set;

import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.services.PossibleValuesService;
import org.eclipse.sapphire.ui.diagram.def.DiagramPaletteCompartmentConstants;
import org.eclipse.sapphire.ui.diagram.def.IDiagramEditorPageDef;
import org.eclipse.sapphire.ui.diagram.def.IDiagramPaletteCompartmentDef;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public class ToolPaletteCompartmentPossibleValuesService extends PossibleValuesService 
{
	@Override
	protected void fillPossibleValues(Set<String> values) 
	{
		IDiagramEditorPageDef diagramPageDef = context(IDiagramEditorPageDef.class);
		ModelElementList<IDiagramPaletteCompartmentDef> compartments = diagramPageDef.getPaletteCompartments();
		if (compartments.size() == 0)
		{
			values.add(DiagramPaletteCompartmentConstants.NODES_COMPARTMENT_ID);
			values.add(DiagramPaletteCompartmentConstants.CONNECTIONS_COMPARTMENT_ID);
		}
		else
		{
			for (IDiagramPaletteCompartmentDef compartment : compartments)
			{
				values.add(compartment.getId().getContent());
			}
		}
	}

}
