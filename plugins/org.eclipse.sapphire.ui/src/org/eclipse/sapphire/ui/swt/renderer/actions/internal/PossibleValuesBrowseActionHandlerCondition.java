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

package org.eclipse.sapphire.ui.swt.renderer.actions.internal;

import org.eclipse.sapphire.services.PossibleValuesService;
import org.eclipse.sapphire.ui.PropertyEditorPart;
import org.eclipse.sapphire.ui.SapphirePropertyEditorCondition;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class PossibleValuesBrowseActionHandlerCondition extends SapphirePropertyEditorCondition
{
    @Override
    protected boolean evaluate( final PropertyEditorPart part )
    {
        return ( part.getLocalModelElement().service( part.getProperty(), PossibleValuesService.class ) != null );
    }

}