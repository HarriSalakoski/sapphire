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

package org.eclipse.sapphire.ui.form.editors.masterdetails.internal;

import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.eclipse.sapphire.ui.SapphireRenderingContext;
import org.eclipse.sapphire.ui.form.editors.masterdetails.MasterDetailsEditorPagePart;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class OutlineCollapseAllActionHandler

    extends SapphireActionHandler
    
{
    public static final String ID = "Sapphire.Outline.CollapseAll";
    
    public OutlineCollapseAllActionHandler()
    {
        setId( ID );
    }
    
    @Override
    protected Object run( final SapphireRenderingContext context )
    {
        getPart().nearest( MasterDetailsEditorPagePart.class ).collapseAllNodes();        
        
        return null;
    }
    
}
