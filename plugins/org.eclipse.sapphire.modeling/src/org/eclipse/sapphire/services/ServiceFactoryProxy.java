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

package org.eclipse.sapphire.services;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public abstract class ServiceFactoryProxy
{
    public abstract String id();
    
    public abstract Class<? extends Service> type();
    
    public Set<String> overrides()
    {
        return Collections.emptySet();
    }
    
    public Map<String,String> parameters()
    {
        return Collections.emptyMap();
    }
    
    public final boolean applicable( final ServiceContext context,
                                     final Class<? extends Service> service )
    {
        boolean result = false;
    
        if( service.isAssignableFrom( type() ) )
        {
            result = applicableHandOff( context, service );
        }
    
        return result;
    }
    
    protected boolean applicableHandOff( final ServiceContext context,
                                         final Class<? extends Service> service )
    {
        return true;
    }
    
    public final Service create( final ServiceContext context,
                                 final Class<? extends Service> service )
    {
        return createHandOff( context, service );
    }

    protected abstract Service createHandOff( ServiceContext context,
                                              Class<? extends Service> service );

}
