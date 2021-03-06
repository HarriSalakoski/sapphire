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

package org.eclipse.sapphire.modeling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.services.EnablementService;
import org.eclipse.sapphire.services.PossibleTypesService;
import org.eclipse.sapphire.services.ValidationAggregationService;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@SuppressWarnings( "unchecked" )

public final class ModelElementList<T extends IModelElement>

    extends ModelParticle
    implements List<T>, IModelParticle

{
    private final ListProperty property;
    private final boolean readonly;
    private final PossibleTypesService possibleTypesService;
    private ListBindingImpl binding;
    private List<IModelElement> data;
    private Boolean enablement;
    private Status validation;
    private Listener listMemberListener;
    
    public ModelElementList( final IModelElement parent,
                             final ListProperty property )
    {
        super( parent, parent.resource() );
        
        this.property = property;
        this.readonly = property.isReadOnly();
        this.possibleTypesService = parent.service( property, PossibleTypesService.class );
        this.data = Collections.emptyList();
        
        this.listMemberListener = new FilteredListener<ElementValidationEvent>()
        {
            @Override
            protected void handleTypedEvent( final ElementValidationEvent event )
            {
                refreshValidation( true );
            }
        };
    }
    
    public final void init( final ListBindingImpl binding )
    {
        if( binding == null )
        {
            throw new IllegalArgumentException();
        }
        
        this.binding = binding;
        refresh( false );
    }

    @Override
    public IModelElement parent()
    {
        return (IModelElement) super.parent();
    }
    
    public ListProperty property()
    {
        return this.property;
    }
    
    public synchronized boolean enabled()
    {
        if( this.enablement == null )
        {
            refreshEnablement( false );
        }
        
        return this.enablement;
    }

    public synchronized Status validation()
    {
        if( this.validation == null )
        {
            refreshValidation( false );
        }
        
        return this.validation;
    }
    
    public boolean refresh()
    {
        return refresh( true );
    }
    
    private boolean refresh( final boolean broadcastChangeIfNecessary )
    {
        if( this.binding == null )
        {
            throw new IllegalStateException();
        }
        
        boolean changed = false;
        
        synchronized( this )
        {
            final List<? extends Resource> newResources = this.binding.read();
            final int newContentSize = newResources.size();
            
            boolean refreshNeeded;
            
            if( this.data.size() == newContentSize )
            {
                refreshNeeded = false;
                
                for( int i = 0; i < newContentSize; i++ )
                {
                    if( this.data.get( i ).resource() != newResources.get( i ) )
                    {
                        refreshNeeded = true;
                        break;
                    }
                }
            }
            else
            {
                refreshNeeded = true;
            }
            
            if( refreshNeeded )
            {
                final List<IModelElement> newContent = new ArrayList<IModelElement>( newContentSize );
                
                for( Resource resource : newResources )
                {
                    IModelElement modelElement = null;
                    
                    for( IModelElement x : this.data )
                    {
                        if( resource == x.resource() )
                        {
                            modelElement = x;
                            break;
                        }
                    }
                    
                    if( modelElement == null )
                    {
                        final ModelElementType type = this.binding.type( resource );
                        modelElement = type.instantiate( this, this.property, resource );
                    }
                    
                    newContent.add( modelElement );
                }
                
                final List<IModelElement> toBeDisposed = new ArrayList<IModelElement>( 1 );
                
                for( IModelElement x : this.data )
                {
                    boolean retained = false;

                    for( IModelElement y : newContent )
                    {
                        if( x == y )
                        {
                            retained = true;
                            break;
                        }
                    }

                    if( ! retained )
                    {
                        toBeDisposed.add( x );
                    }
                }
                
                this.data = newContent;
                
                for( IModelElement x : toBeDisposed )
                {
                    try
                    {
                        x.dispose();
                    }
                    catch( Exception e )
                    {
                        LoggingService.log( e );
                    }
                }
                
                for( T modelElement : this )
                {
                    modelElement.attach( this.listMemberListener );
                }
                
                changed = true;
            }
        }
        
        if( changed && broadcastChangeIfNecessary )
        {
            ( (ModelElement) parent() ).broadcastPropertyContentEvent( this.property );
        }
        
        refreshEnablement( broadcastChangeIfNecessary );
        refreshValidation( broadcastChangeIfNecessary );
        
        return changed;
    }
    
    private void refreshEnablement( final boolean broadcastChangeIfNecessary )
    {
        boolean newEnablementState = true;
        
        for( EnablementService service : parent().services( this.property, EnablementService.class ) )
        {
            newEnablementState = ( newEnablementState && service.enablement() );
            
            if( newEnablementState == false )
            {
                break;
            }
        }
        
        if( this.enablement == null )
        {
            this.enablement = newEnablementState;
        }
        else if( this.enablement.booleanValue() != newEnablementState )
        {
            final boolean before = this.enablement;
            this.enablement = newEnablementState;
            
            if( broadcastChangeIfNecessary )
            {
                ( (ModelElement) parent() ).broadcastPropertyEnablementEvent( this.property, before, newEnablementState );
            }
        }
    }

    private void refreshValidation( final boolean broadcastChangeIfNecessary )
    {
        final Status.CompositeStatusFactory factory = Status.factoryForComposite();
        
        factory.merge( parent().service( this.property, ValidationAggregationService.class ).validation() );
        
        for( T item : this )
        {
            factory.merge( item.validation() );
        }
        
        final Status newValidationResult = factory.create();
        
        if( this.validation == null )
        {
            this.validation = newValidationResult;
        }
        else if( ! this.validation.equals( newValidationResult ) )
        {
            final Status before = this.validation;
            this.validation = newValidationResult;
            
            if( broadcastChangeIfNecessary )
            {
                ( (ModelElement) parent() ).broadcastPropertyValidationEvent( this.property, before, newValidationResult );
            }
        }
    }
    
    public T insert()
    {
        return insert( this.possibleTypesService.types().first() );
    }
    
    public T insert( final int position )
    {
        return insert( this.possibleTypesService.types().first(), position );
    }
    
    public T insert( final ModelElementType type )
    {
        synchronized( this )
        {
            return insert( type, size() );
        }
    }
    
    public <C extends IModelElement> C insert( final Class<C> cl )
    {
        synchronized( this )
        {
            return insert( cl, size() );
        }
    }
    
    public T insert( final ModelElementType type,
                     final int position )
    {
        ensureNotReadOnly();
        
        T newElement = null;
        
        synchronized( this )
        {
            final Resource newResource = this.binding.insert( type, position );
            
            refresh();
            
            for( IModelElement element : this.data )
            {
                if( element.resource() == newResource )
                {
                    newElement = (T) element;
                    newElement.initialize();
                    break;
                }
            }
        }
        
        return newElement;
    }
    
    public <C extends IModelElement> C insert( final Class<C> cl,
                                               final int position )
    {
        final ModelElementType type = ModelElementType.read( cl );
        
        if( type == null )
        {
            throw new IllegalArgumentException();
        }
        
        return (C) insert( type, position );
    }
    
    public void move( final T element,
                      final int position )
    {
        ensureNotReadOnly();
        
        synchronized( this )
        {
            if( position < 0 || position > size() )
            {
                throw new IllegalArgumentException();
            }
            
            final int oldPosition = indexOf( element );
            
            if( oldPosition == -1 )
            {
                throw new IllegalArgumentException();
            }
            
            if( position != oldPosition )
            {
                this.binding.move( element.resource(), position );
                refresh();
            }
        }
    }

    public void moveUp( final T modelElement )
    {
        ensureNotReadOnly();
        
        synchronized( this )
        {
            final int index = indexOf( modelElement );
            
            if( index == -1 )
            {
                throw new IllegalArgumentException();
            }
            
            if( index > 0 )
            {
                final T previousModelElement = (T) this.data.get( index - 1 );
                swap( modelElement, previousModelElement );
            }
        }
    }
    
    public void moveDown( final T modelElement )
    {
        ensureNotReadOnly();
        
        synchronized( this )
        {
            final int index = indexOf( modelElement );
            
            if( index == -1 )
            {
                throw new IllegalArgumentException();
            }
            
            if( index < this.data.size() - 1 )
            {
                final T nextModelElement = (T) this.data.get( index + 1 );
                swap( modelElement, nextModelElement );
            }
        }
    }
    
    public void swap( final T a,
                      final T b )
    {
        ensureNotReadOnly();
        
        synchronized( this )
        {
            final int aPosition = indexOf( a );
            final int bPosition = indexOf( b );
            
            if( aPosition == -1 || bPosition == -1 )
            {
                throw new IllegalArgumentException();
            }
            
            if( aPosition != bPosition )
            {
                this.binding.move( a.resource(), bPosition );
                this.binding.move( b.resource(), aPosition );
                refresh();
            }
        }
    }

    public synchronized boolean remove( final Object object )
    {
        ensureNotReadOnly();
        
        if( contains( object ) )
        {
            final Resource resource = ( (IModelElement) object ).resource();
            this.binding.remove( resource );
            refresh();
            
            return true;
        }
        
        return false;
    }

    public synchronized T remove( final int index )
    {
        ensureNotReadOnly();
        
        final IModelElement element = this.data.get( index );
        remove( element );
        return (T) element;
    }

    public synchronized boolean removeAll( final Collection<?> collection )
    {
        ensureNotReadOnly();
        
        boolean changed = false;
        
        for( Object object : collection )
        {
            changed = remove( object ) || changed;
        }
        
        return changed;
    }

    public synchronized boolean retainAll( final Collection<?> collection )
    {
        ensureNotReadOnly();
        
        boolean changed = false;
        
        for( IModelElement element : this )
        {
            if( ! collection.contains( element ) )
            {
                changed = remove( element ) || changed;
            }
        }
        
        return changed;
    }

    public synchronized void clear()
    {
        ensureNotReadOnly();
        
        for( IModelElement element : this )
        {
            remove( element );
        }
    }

    public synchronized T get( final int index )
    {
        return (T) this.data.get( index );
    }

    public synchronized int indexOf( final Object object )
    {
        int index = -1;
        
        for( int i = 0, n = this.data.size(); i < n; i++ )
        {
            if( this.data.get( i ) == object )
            {
                index = i;
                break;
            }
        }
        
        return index;
    }

    public synchronized int lastIndexOf( final Object object )
    {
        int index = -1;
        
        for( int i = 0, n = this.data.size(); i < n; i++ )
        {
            if( this.data.get( i ) == object )
            {
                index = i;
            }
        }
        
        return index;
    }

    public synchronized boolean contains( final Object object )
    {
        for( Object x : this.data )
        {
            if( x == object )
            {
                return true;
            }
        }
        
        return false;
    }

    public synchronized boolean containsAll( final Collection<?> collection )
    {
        for( Object x : collection )
        {
            if( ! contains( x ) )
            {
                return false;
            }
        }
        
        return true;
    }

    public synchronized boolean isEmpty()
    {
        return this.data.isEmpty();
    }

    public synchronized int size()
    {
        return this.data.size();
    }

    public synchronized Iterator<T> iterator()
    {
        return new Itr<T>( (Iterator<T>) this.data.iterator() );
    }

    public synchronized ListIterator<T> listIterator()
    {
        return new ListItr<T>( (ListIterator<T>) this.data.listIterator() );
    }

    public synchronized ListIterator<T> listIterator( final int index )
    {
        return new ListItr<T>( (ListIterator<T>) this.data.listIterator( index ) );
    }

    public List<T> subList( final int fromIndex,
                            final int toIndex )
    {
        throw new UnsupportedOperationException();
    }

    public synchronized Object[] toArray()
    {
        return this.data.toArray();
    }

    public synchronized <E> E[] toArray( E[] array )
    {
        return this.data.toArray( array );
    }
    
    public boolean add( final T object )
    {
        throw new UnsupportedOperationException();
    }

    public void add( final int index,
                     final T element )
    {
        throw new UnsupportedOperationException();
    }

    public boolean addAll( final Collection<? extends T> collection )
    {
        throw new UnsupportedOperationException();
    }

    public boolean addAll( final int index,
                           final Collection<? extends T> collection )
    {
        throw new UnsupportedOperationException();
    }

    public T set( final int index,
                  final T element )
    {
        throw new UnsupportedOperationException();
    }
    
    private void ensureNotReadOnly()
    {
        if( this.readonly )
        {
            throw new UnsupportedOperationException();
        }
    }
    
    private static class Itr<T> implements Iterator<T>
    {
        private final Iterator<T> baseIterator;
        
        public Itr( final Iterator<T> baseIterator )
        {
            this.baseIterator = baseIterator;
        }
        
        public boolean hasNext()
        {
            return this.baseIterator.hasNext();
        }

        public T next()
        {
            return this.baseIterator.next();
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
    
    private static final class ListItr<T> extends Itr<T> implements ListIterator<T>
    {
        private final ListIterator<T> baseIterator;
        
        public ListItr( final ListIterator<T> baseIterator )
        {
            super( baseIterator );
            this.baseIterator = baseIterator;
        }

        public int nextIndex()
        {
            return this.baseIterator.nextIndex();
        }

        public boolean hasPrevious()
        {
            return this.baseIterator.hasPrevious();
        }

        public T previous()
        {
            return this.baseIterator.previous();
        }

        public int previousIndex()
        {
            return this.baseIterator.previousIndex();
        }

        public void add( final T object )
        {
            throw new UnsupportedOperationException();
        }

        public void set( final T object )
        {
            throw new UnsupportedOperationException();
        }
    }

}
