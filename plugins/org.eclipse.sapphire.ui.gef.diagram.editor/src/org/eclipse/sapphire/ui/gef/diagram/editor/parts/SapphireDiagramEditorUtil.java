/******************************************************************************
 * Copyright (c) 2011 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ling Hao - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.gef.diagram.editor.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.sapphire.ui.diagram.def.IDiagramConnectionDef;
import org.eclipse.sapphire.ui.diagram.editor.DiagramConnectionPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * @author <a href="mailto:ling.hao@oracle.com">Ling Hao</a>
 */

public class SapphireDiagramEditorUtil {
	
	private static List<Color> colors = new ArrayList<Color>();

	private SapphireDiagramEditorUtil() {
	}
	
    public static int getLinkStyle(IDiagramConnectionDef def) {
        int linkStyle = SWT.LINE_SOLID;
        if (def != null) {
            org.eclipse.sapphire.ui.LineStyle style = def.getLineStyle().getContent();
            if (style == org.eclipse.sapphire.ui.LineStyle.DASH ) {
                linkStyle = SWT.LINE_DASH;
            }
            else if (style == org.eclipse.sapphire.ui.LineStyle.DOT) {
                linkStyle = SWT.LINE_DOT;
            }
            else if (style == org.eclipse.sapphire.ui.LineStyle.DASH_DOT) {
                linkStyle = SWT.LINE_DASHDOT;
            }
        }            
        return linkStyle;
    }
	
    public static Color getLineColor(DiagramConnectionPart connection) {
    	IDiagramConnectionDef def = connection.getConnectionDef();
    	Color color = ColorConstants.darkBlue;
    	if (def != null) {
        	return getColor(def.getLineColor().getContent());
    	}
    	return color;
    }
    
    public static Color getColor(org.eclipse.sapphire.ui.Color sapphireColor) {
    	int red = sapphireColor.getRed();
    	int green = sapphireColor.getGreen();
    	int blue = sapphireColor.getBlue();
    	
    	// TODO color management somewhere to dispose
		for (Color existingColor : colors) {
			if (existingColor.getRed() == red && existingColor.getGreen() == green && existingColor.getBlue() == blue) {
				return existingColor;
			}
		}
    	
		final Color newColor = new Color(Display.getCurrent(), red, green, blue);
		colors.add(newColor);
		return newColor;
    }
}