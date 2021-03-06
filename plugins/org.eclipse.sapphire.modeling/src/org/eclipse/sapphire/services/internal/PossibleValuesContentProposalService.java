/*******************************************************************************
 * Copyright (c) 2013 Oracle and Accenture Services Pvt Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation
 *    Kamesh Sampath - initial implementation
 ******************************************************************************/

package org.eclipse.sapphire.services.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.services.ContentProposal;
import org.eclipse.sapphire.services.ContentProposalService;
import org.eclipse.sapphire.services.PossibleValuesService;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */

public final class PossibleValuesContentProposalService extends ContentProposalService
{
    private PossibleValuesService possibleValuesService;

    @Override
    protected void init() 
    {
        super.init();
        
        final IModelElement element = context( IModelElement.class );
        final ValueProperty property = context( ValueProperty.class );
        
        this.possibleValuesService = element.service( property, PossibleValuesService.class );
        
        if( this.possibleValuesService == null )
        {
            throw new IllegalStateException();
        }
    }

    @Override
    public Session session() 
    {
        return new Session() 
        {
            @Override
            protected List<ContentProposal> compute() 
            {
                final String filter = filter().toLowerCase();
                final List<ContentProposal> proposals = new ArrayList<ContentProposal>();
                
                if( filter.length() == 0 )
                {
                    for( String value : PossibleValuesContentProposalService.this.possibleValuesService.values() ) 
                    {
                        final ContentProposal proposal = new ContentProposal( value );
                        proposals.add( proposal );
                    }
                }
                else
                {
                    for( ContentProposal proposal : proposals() )
                    {
                        if( proposal.content().toLowerCase().startsWith( filter ) )
                        {
                            proposals.add( proposal );
                        }
                    }
                }
                
                return proposals;
            }
        };
    }

    public static final class Factory extends ServiceFactory
    {
        @Override
        public boolean applicable( final ServiceContext context,
                                   final Class<? extends Service> service ) 
        {
            final IModelElement element = context.find( IModelElement.class );

            if( element != null )
            {
                final ValueProperty property = context.find( ValueProperty.class );
                
                if( property != null && element.service( property, PossibleValuesService.class ) != null )
                {
                    return true;
                }
            }

            return false;
        }

        @Override
        public Service create( final ServiceContext context,
                               final Class<? extends Service> service )
        {
            return new PossibleValuesContentProposalService();
        }
    }

}
