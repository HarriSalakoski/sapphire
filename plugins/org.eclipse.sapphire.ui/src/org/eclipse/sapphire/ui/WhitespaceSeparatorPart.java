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

package org.eclipse.sapphire.ui;

import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gdhfill;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gdhhint;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.gdhspan;
import static org.eclipse.sapphire.ui.swt.renderer.GridLayoutUtil.glayout;

import org.eclipse.sapphire.ui.def.WhitespaceSeparatorDef;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class WhitespaceSeparatorPart extends SeparatorPart
{
    @Override
    public void render( final SapphireRenderingContext context )
    {
        if( ! visible() )
        {
            return;
        }
        
        final WhitespaceSeparatorDef def = (WhitespaceSeparatorDef) this.definition;
        
        final Composite spacer = new Composite( context.getComposite(), SWT.NONE );
        spacer.setLayoutData( gdhhint( gdhspan( gdhfill(), 2 ), def.getSize().getContent() ) );
        spacer.setLayout( glayout( 1, 0, 0, 0, 0 ) );
    }
    
}
