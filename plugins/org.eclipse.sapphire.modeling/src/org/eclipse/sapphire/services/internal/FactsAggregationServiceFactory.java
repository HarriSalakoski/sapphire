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

package org.eclipse.sapphire.services.internal;

import org.eclipse.sapphire.services.FactsAggregationService;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class FactsAggregationServiceFactory extends ServiceFactory
{
    @Override
    public boolean applicable( final ServiceContext context,
                               final Class<? extends Service> service )
    {
        return true;
    }

    @Override
    public Service create( final ServiceContext context,
                           final Class<? extends Service> service )
    {
        return new FactsAggregationService();
    }
    
}
