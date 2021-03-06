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

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.Path;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.services.RelativePathService;
import org.eclipse.sapphire.ui.PropertyEditorPart;
import org.eclipse.sapphire.ui.SapphirePropertyEditorCondition;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class RelativePathJumpActionHandlerCondition 

    extends SapphirePropertyEditorCondition
    
{
    @Override
    protected boolean evaluate( final PropertyEditorPart part )
    {
        final IModelElement element = part.getModelElement();
        final ModelProperty property = part.getProperty();
        
        return ( property instanceof ValueProperty && Path.class.isAssignableFrom( property.getTypeClass() ) && element.service( property, RelativePathService.class ) != null );
    }

}