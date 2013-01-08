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

package org.eclipse.sapphire.samples.catalog;

import static org.eclipse.sapphire.modeling.util.MiscUtil.EMPTY_STRING;
import static org.eclipse.sapphire.modeling.xml.XmlUtil.createQualifiedName;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.modeling.BindingImpl;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ListBindingImpl;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.PropertyContentEvent;
import org.eclipse.sapphire.modeling.Resource;
import org.eclipse.sapphire.modeling.ValueBindingImpl;
import org.eclipse.sapphire.modeling.xml.ChildXmlResource;
import org.eclipse.sapphire.modeling.xml.StandardXmlListBindingImpl;
import org.eclipse.sapphire.modeling.xml.XmlElement;
import org.eclipse.sapphire.modeling.xml.XmlResource;
import org.eclipse.sapphire.util.IdentityHashSet;
import org.eclipse.sapphire.util.ListFactory;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class ManufacturersBinding extends ListBindingImpl
{
    private List<ManufacturerResource> cache = ListFactory.empty();
    private Listener listener;
    
    @Override
    public void init( final IModelElement element,
                      final ModelProperty property,
                      final String[] params )
    {
        super.init( element, property, params );
        
        this.listener = new FilteredListener<PropertyContentEvent>()
        {
            @Override
            protected void handleTypedEvent( final PropertyContentEvent event )
            {
                element.refresh( property );
                
                for( Manufacturer manufacturer : ( (Catalog) element ).getManufacturers() )
                {
                    manufacturer.refresh( Manufacturer.PROP_ITEMS );
                }
            }
        };
        
        element.attach( this.listener, "Items/Manufacturer" );
    }

    @Override
    public List<? extends Resource> read()
    {
        // Compute a sorted list of manufacturer names, ignoring case differences and including a null
        // at the end if uncategorized items are found.
        
        final Set<String> manufacturers = new TreeSet<String>( ManufacturerNamesComparator.INSTANCE );
        
        for( Item item : ( (Catalog) element() ).getItems() )
        {
            manufacturers.add( item.getManufacturer().getText() );
        }
        
        // Compute the list of manufacturer resources, reusing existing resources when possible.
        
        final ListFactory<ManufacturerResource> resourcesListFactory = ListFactory.start();
        final Set<ManufacturerResource> reused = new IdentityHashSet<ManufacturerResource>();
        
        for( String manufacturer : manufacturers )
        {
            boolean found = false;
            
            for( ManufacturerResource resource : this.cache )
            {
                if( ManufacturerNamesComparator.INSTANCE.compare( manufacturer, resource.getName() ) == 0 )
                {
                    resourcesListFactory.add( resource );
                    reused.add( resource );
                    found = true;
                    break;
                }
            }
            
            if( ! found )
            {
                resourcesListFactory.add( new ManufacturerResource( manufacturer ) );
            }
        }
        
        // Dispose previously created resources that were not reused.
        
        for( ManufacturerResource resource : this.cache )
        {
            if( ! reused.contains( resource ) )
            {
                resource.dispose();
            }
        }
        
        // Stash the computed list of resources for future use and return to the caller.
        
        final List<ManufacturerResource> resources = resourcesListFactory.result();
        
        this.cache = resources;
        
        return this.cache = resources;
    }

    @Override
    public ModelElementType type( final Resource resource )
    {
        return Manufacturer.TYPE;
    }
    
    @Override
    public void dispose()
    {
        for( ManufacturerResource resource : this.cache )
        {
            resource.dispose();
        }
        
        this.cache = null;
        
        element().detach( this.listener, "Items/Manufacturer" );
        this.listener = null;
    }

    private static final class ManufacturerNamesComparator implements Comparator<String>
    {
        public static ManufacturerNamesComparator INSTANCE = new ManufacturerNamesComparator();
        
        public int compare( String x, String y )
        {
            x = ( x == null ? EMPTY_STRING : x.trim() );
            y = ( y == null ? EMPTY_STRING : y.trim() );
            
            if( x == y )
            {
                return 0;
            }
            else if( x.length() == 0 )
            {
                return Integer.MAX_VALUE;
            }
            else if( y.length() == 0 )
            {
                return Integer.MIN_VALUE;
            }
            else
            {
                return x.compareToIgnoreCase( y );
            }
        }
    }
    
    private final class ManufacturerResource extends XmlResource
    {
        private String name;
        
        public ManufacturerResource( final String name )
        {
            super( (XmlResource) ManufacturersBinding.this.element().resource() );
            
            this.name = name;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        public void setName( final String name )
        {
            this.name = name;
            
            for( Item item : ( (Manufacturer) element() ).getItems() )
            {
                item.setManufacturer( name );
            }
        }

        @Override
        protected BindingImpl createBinding( final ModelProperty property )
        {
            BindingImpl binding = null;
            
            if( property == Manufacturer.PROP_NAME )
            {
                binding = new ValueBindingImpl()
                {
                    @Override
                    public String read()
                    {
                        return getName();
                    }

                    @Override
                    public void write( final String value )
                    {
                        setName( value );
                    }
                };
            }
            else if( property == Manufacturer.PROP_ITEMS )
            {
                binding = new StandardXmlListBindingImpl()
                {
                    @Override
                    protected void initBindingMetadata( final IModelElement element,
                                                        final ModelProperty property,
                                                        final String[] params )
                    {
                        this.xmlElementNames = new QName[] { createQualifiedName( "Item", null ) };
                        this.modelElementTypes = new ModelElementType[] { Item.TYPE };
                    }
                    
                    @Override
                    protected List<?> readUnderlyingList()
                    {
                        final List<?> all = super.readUnderlyingList();
                        final ListFactory<XmlElement> filtered = ListFactory.start();
                        
                        for( Object obj : all )
                        {
                            final XmlElement element = (XmlElement) obj;
                            final String manufacturer = element.getChildNodeText( "Manufacturer" );
                            
                            if( ManufacturerNamesComparator.INSTANCE.compare( ManufacturerResource.this.name, manufacturer ) == 0 )
                            {
                                filtered.add( element );
                            }
                        }
                        
                        return filtered.result();
                    }
                    
                    protected Object insertUnderlyingObject( final ModelElementType type,
                                                             final int position )
                    {
                        final XmlElement element = (XmlElement) super.insertUnderlyingObject( type, position );
                        
                        element.setChildNodeText( "Manufacturer", ManufacturerResource.this.name, true );
                        
                        return element;
                    }
                    
                    @Override
                    protected Resource resource( final Object obj )
                    {
                        return new ChildXmlResource( ManufacturerResource.this, (XmlElement) obj );
                    }

                    @Override
                    protected XmlElement getXmlElement( final boolean createIfNecessary )
                    {
                        return element().nearest( Catalog.class ).adapt( XmlResource.class ).getXmlElement( createIfNecessary );
                    }
                };
            }
            
            if( binding != null )
            {
                binding.init( element(), property, null );
            }
            
            return binding;
        }

        @Override
        public XmlElement getXmlElement( final boolean createIfNecessary )
        {
            return null;
        }
    }
    
}
