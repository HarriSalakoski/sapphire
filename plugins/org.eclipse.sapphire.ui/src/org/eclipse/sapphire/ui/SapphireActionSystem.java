/******************************************************************************
 * Copyright (c) 2010 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class SapphireActionSystem
{
    private static final String PREFIX = "Sapphire.";
    
    public static final String CONTEXT_ACTION_LINK = PREFIX + "ActionLink";
    public static final String CONTEXT_EDITOR_PAGE = PREFIX + "EditorPage";
    public static final String CONTEXT_EDITOR_PAGE_OUTLINE = PREFIX + "EditorPage.Outline";
    public static final String CONTEXT_EDITOR_PAGE_OUTLINE_HEADER = PREFIX + "EditorPage.Outline.Header";
    public static final String CONTEXT_EDITOR_PAGE_OUTLINE_NODE = PREFIX + "EditorPage.Outline.Node";
    public static final String CONTEXT_ELEMENT_PROPERTY_EDITOR = PREFIX + "ElementPropertyEditor";
    public static final String CONTEXT_LIST_PROPERTY_EDITOR = PREFIX + "ListPropertyEditor";
    public static final String CONTEXT_SECTION = PREFIX + "Section";
    public static final String CONTEXT_VALUE_PROPERTY_EDITOR = PREFIX + "ValuePropertyEditor";
    
    public static final String ACTION_ADD = PREFIX + "Add";
    public static final String ACTION_ASSIST = PREFIX + "Assist";
    public static final String ACTION_BROWSE = PREFIX + "Browse";
    public static final String ACTION_CREATE = PREFIX + "Create";
    public static final String ACTION_DELETE = PREFIX + "Delete";
    public static final String ACTION_HELP = PREFIX + "Help";
    public static final String ACTION_JUMP = PREFIX + "Jump";
    public static final String ACTION_MOVE_DOWN = PREFIX + "Move.Down";
    public static final String ACTION_MOVE_LEFT = PREFIX + "Move.Left";
    public static final String ACTION_MOVE_RIGHT = PREFIX + "Move.Right";
    public static final String ACTION_MOVE_UP = PREFIX + "Move.Up";
    public static final String ACTION_OUTLINE_COLLAPSE_ALL = PREFIX + "Outline.CollapseAll";
    public static final String ACTION_OUTLINE_EXPAND_ALL = PREFIX + "Outline.ExpandAll";
    public static final String ACTION_OUTLINE_HIDE = PREFIX + "Outline.Hide";
    public static final String ACTION_RESTORE_DEFAULTS = PREFIX + "Restore.Defaults";
    
    public static final SapphireActionHandlerFilter createFilterByActionId( final String actionId )
    {
        final SapphireActionHandlerFilter filter = new SapphireActionHandlerFilter()
        {
            @Override
            public boolean check( final SapphireActionHandler handler )
            {
                if( actionId.equalsIgnoreCase( handler.getAction().getId() ) )
                {
                    return false;
                }
                
                return true;
            }
        };
        
        return filter;
    }
    
    public static final SapphireActionHandlerFilter createFilterByActionHandlerId( final String actionHandlerId )
    {
        final SapphireActionHandlerFilter filter = new SapphireActionHandlerFilter()
        {
            @Override
            public boolean check( final SapphireActionHandler handler )
            {
                if( actionHandlerId.equalsIgnoreCase( handler.getId() ) )
                {
                    return false;
                }
                
                return true;
            }
        };
        
        return filter;
    }
    
}