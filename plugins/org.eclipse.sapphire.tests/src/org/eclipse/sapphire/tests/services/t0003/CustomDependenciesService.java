/******************************************************************************
 * Copyright (c) 2012 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.tests.services.t0003;

import org.eclipse.sapphire.modeling.ModelPath;
import org.eclipse.sapphire.services.DependenciesServiceData;
import org.eclipse.sapphire.services.DependenciesService;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class CustomDependenciesService extends DependenciesService
{
    @Override
    protected DependenciesServiceData compute()
    {
        return new DependenciesServiceData( new ModelPath( "Name" ), new ModelPath( "Id" ) );
    }
    
}