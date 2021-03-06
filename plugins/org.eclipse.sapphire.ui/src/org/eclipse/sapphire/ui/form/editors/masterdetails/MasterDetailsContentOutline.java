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

package org.eclipse.sapphire.ui.form.editors.masterdetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.ListenerContext;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.ui.form.editors.masterdetails.def.MasterDetailsContentNodeDef;
import org.eclipse.sapphire.ui.form.editors.masterdetails.def.MasterDetailsEditorPageDef;
import org.eclipse.sapphire.ui.form.editors.masterdetails.state.ContentOutlineNodeState;
import org.eclipse.sapphire.ui.form.editors.masterdetails.state.MasterDetailsEditorPageState;
import org.eclipse.sapphire.util.ListFactory;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class MasterDetailsContentOutline
{
    private final MasterDetailsEditorPagePart editorPagePart;
    private final MasterDetailsEditorPageDef editorPageDef;
    private final IModelElement rootModelElement;
    private MasterDetailsContentNode root;
    private List<MasterDetailsContentNode> selection;
    private final ListenerContext listeners;
    private String filterText;
    
    public MasterDetailsContentOutline( final MasterDetailsEditorPagePart editorPagePart )
    {
        this.editorPagePart = editorPagePart;
        this.editorPageDef = editorPagePart.definition();
        this.rootModelElement = editorPagePart.getModelElement();
        this.selection = Collections.emptyList();
        this.listeners = new ListenerContext();
        this.filterText = "";
    }
    
    public MasterDetailsContentNode getRoot()
    {
        if( this.root == null )
        {
            final MasterDetailsContentNodeDef rootNodeDef = this.editorPageDef.getRootNode();
            
            this.root = new MasterDetailsContentNode();
            this.root.init( this.editorPagePart, this.rootModelElement, rootNodeDef, Collections.<String,String>emptyMap() );
            
            loadTreeState();
            
            attach
            (
                new Listener()
                {
                    @Override
                    public void handle( final Event event )
                    {
                        if( event instanceof NodeExpandedStateChangedEvent || event instanceof SelectionChangedEvent )
                        {
                            saveTreeState();
                        }
                    }
                }
            );
        }
        
        return this.root;
    }
    
    public MasterDetailsContentNode getSelectedNode()
    {
        if( this.selection.isEmpty() )
        {
            return null;
        }
        else
        {
            return this.selection.get( 0 );
        }
    }
    
    public List<MasterDetailsContentNode> getSelectedNodes()
    {
        return this.selection;
    }
    
    public void setSelectedNode( final MasterDetailsContentNode selection )
    {
        if( selection == null )
        {
            setSelectedNodes( Collections.<MasterDetailsContentNode>emptyList() );
        }
        else
        {
            setSelectedNodes( Collections.singletonList( selection ) );
        }
    }
    
    public void setSelectedNodes( final List<MasterDetailsContentNode> selection )
    {
        if( ! this.selection.equals( selection ) )
        {
            for( MasterDetailsContentNode node : selection )
            {
                final MasterDetailsContentNode parent = node.getParentNode();
                
                if( parent != null )
                {
                    parent.setExpanded( true );
                }
            }
            
            if( selection.isEmpty() )
            {
                this.selection = Collections.emptyList();
            }
            else
            {
                this.selection = new ArrayList<MasterDetailsContentNode>( selection );
            }
            
            this.listeners.broadcast( new SelectionChangedEvent( this.selection ) );
        }
    }

    public void setSelection( final String path )
    {
        MasterDetailsContentNode node = this.root;
        
        for( String segment : path.split( "/" ) )
        {
            boolean segmentMatched = false;
            
            for( MasterDetailsContentNode n : node.nodes().visible() )
            {
                if( n.getLabel().equals( segment ) )
                {
                    node = n;
                    segmentMatched = true;
                    break;
                }
            }
            
            if( ! segmentMatched )
            {
                break;
            }
        }
        
        if( node != this.root )
        {
            setSelectedNode( node );
        }
    }
    
    void refreshSelection()
    {
        final List<MasterDetailsContentNode> newSelection = new ArrayList<MasterDetailsContentNode>();
        
        for( MasterDetailsContentNode node : this.selection )
        {
            final LinkedList<MasterDetailsContentNode> path = new LinkedList<MasterDetailsContentNode>();
            
            while( node != this.root )
            {
                path.addFirst( node );
                node = node.getParentNode();
            }
            
            node = this.root;
            
            for( MasterDetailsContentNode n : path )
            {
                if( node.nodes().visible().contains( n ) )
                {
                    node = n;
                }
                else
                {
                    break;
                }
            }
            
            if( node == this.root )
            {
                final List<MasterDetailsContentNode> topLevelNodes = this.root.nodes().visible();
                
                if( topLevelNodes.size() > 0 )
                {
                    node = topLevelNodes.get( 0 );
                }
                else
                {
                    node = null;
                }
            }
            
            if( ! newSelection.contains( node ) )
            {
                newSelection.add( node );
            }
        }
        
        setSelectedNodes( newSelection );
    }

    public List<MasterDetailsContentNode> getExpandedNodes()
    {
        final List<MasterDetailsContentNode> result = new ArrayList<MasterDetailsContentNode>();
        
        for( MasterDetailsContentNode node : this.root.nodes().visible() )
        {
            node.getExpandedNodes( result );
        }
        
        return result;
    }
    
    public void setExpandedNodes( final Set<MasterDetailsContentNode> expandedNodes )
    {
        for( MasterDetailsContentNode node : this.root.nodes().visible() )
        {
            setExpandedNodes( node, expandedNodes );
        }
    }
    
    private static void setExpandedNodes( final MasterDetailsContentNode node,
                                          final Set<MasterDetailsContentNode> expandedNodes )
    {
        for( MasterDetailsContentNode child : node.nodes().visible() )
        {
            setExpandedNodes( child, expandedNodes );
        }
        
        final boolean shouldBeExpanded = expandedNodes.contains( node );
        
        if( node.isExpanded() != shouldBeExpanded )
        {
            node.setExpanded( shouldBeExpanded );
        }
    }
    
    public String getFilterText()
    {
        return this.filterText;
    }
    
    public void setFilterText( final String filterText )
    {
        if( ! this.filterText.equals( filterText ) )
        {
            this.filterText = filterText;
            this.listeners.broadcast( new FilterChangedEvent( filterText ) );
        }
    }
    
    public final ListenerContext listeners()
    {
        return this.listeners;
    }
    
    public final boolean attach( final Listener listener )
    {
        return this.listeners.attach( listener );
    }
    
    public final boolean detach( final Listener listener )
    {
        return this.listeners.detach( listener );
    }
    
    public void notifyOfNodeExpandedStateChange( final MasterDetailsContentNode node )
    {
        this.listeners.broadcast( new NodeExpandedStateChangedEvent( node ) );
    }
    
    public void dispose()
    {
        if( this.root != null )
        {
            this.root.dispose();
        }
    }
    
    private void loadTreeState()
    {
        final List<MasterDetailsContentNode> selection = new ArrayList<MasterDetailsContentNode>();
        
        final MasterDetailsEditorPageState editorPageState = this.editorPagePart.state();
        
        if( editorPageState != null )
        {
            final ContentOutlineNodeState rootNodeState = editorPageState.getContentOutlineState().getRoot();
            
            for( MasterDetailsContentNode node : this.root.nodes().visible() )
            {
                loadTreeState( rootNodeState, node, selection );
            }
        }

        if( ! selection.isEmpty() )
        {
            setSelectedNodes( selection );
        }
        else
        {
            MasterDetailsContentNode node = this.root;
            
            final String defaultInitialNodePath = this.editorPageDef.getInitialSelectionPath().getText();
            
            if( defaultInitialNodePath != null )
            {
                for( String segment : defaultInitialNodePath.split( "/" ) )
                {
                    node = node.findNode( segment );
                    
                    if( node != null && node.visible() )
                    {
                        node.setExpanded( true );
                    }
                    else
                    {
                        break;
                    }
                }
            }
            
            if( node != null )
            {
                setSelectedNode( node );
            }
        }
    }
    
    private void loadTreeState( final ContentOutlineNodeState parentNodeState,
                                final MasterDetailsContentNode node,
                                final List<MasterDetailsContentNode> selection )
    {
        final String nodeLabel = node.getLabel();
        
        for( ContentOutlineNodeState childNodeState : parentNodeState.getChildren() )
        {
            if( nodeLabel.equals( childNodeState.getLabel().getText() ) )
            {
                node.setExpanded( childNodeState.getExpanded().getContent() );
                
                if( childNodeState.getSelected().getContent() )
                {
                    selection.add( node );
                }

                for( MasterDetailsContentNode child : node.nodes().visible() )
                {
                    loadTreeState( childNodeState, child, selection );
                }
                
                break;
            }
        }
    }
    
    private void saveTreeState()
    {
        final MasterDetailsEditorPageState editorPageState = this.editorPagePart.state();
        
        if( editorPageState != null )
        {
            final ContentOutlineNodeState rootNodeState = editorPageState.getContentOutlineState().getRoot();
            
            rootNodeState.getChildren().clear();
            
            final List<MasterDetailsContentNode> selection = getSelectedNodes();
            
            for( MasterDetailsContentNode node : this.root.nodes().visible() )
            {
                saveTreeState( rootNodeState, node, selection );
            }
        }
    }

    private void saveTreeState( final ContentOutlineNodeState parentNodeState,
                                final MasterDetailsContentNode node,
                                final List<MasterDetailsContentNode> selection )
    {
        final boolean isExpanded = node.isExpanded();
        final boolean isSelected = selection.contains( node );
        
        if( isExpanded || isSelected )
        {
            final ContentOutlineNodeState childNodeState = parentNodeState.getChildren().insert();
            
            childNodeState.setLabel( node.getLabel() );
            
            if( isExpanded )
            {
                childNodeState.setExpanded( isExpanded );
            }
            
            if( isSelected )
            {
                childNodeState.setSelected( isSelected );
            }
            
            for( MasterDetailsContentNode child : node.nodes().visible() )
            {
                saveTreeState( childNodeState, child, selection );
            }
        }
    }
    
    public static final class NodeExpandedStateChangedEvent extends Event
    {
        private final MasterDetailsContentNode node;
        
        public NodeExpandedStateChangedEvent( final MasterDetailsContentNode node )
        {
            this.node = node;
        }
        
        public MasterDetailsContentNode node()
        {
            return this.node;
        }
    }
    
    public static final class SelectionChangedEvent extends Event
    {
        private final List<MasterDetailsContentNode> selection;
        
        public SelectionChangedEvent( final List<MasterDetailsContentNode> selection )
        {
            this.selection = ListFactory.unmodifiable( selection );
        }
        
        public List<MasterDetailsContentNode> selection()
        {
            return this.selection;
        }
    }
    
    public static final class FilterChangedEvent extends Event
    {
        private final String filter;
        
        public FilterChangedEvent( final String filter )
        {
            this.filter = filter;
        }
        
        public String filter()
        {
            return this.filter;
        }
    }
    
}
