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

package org.eclipse.sapphire.samples.calendar.integrated;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.LongString;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public interface IEvent extends IModelElement
{
    ModelElementType TYPE = new ModelElementType( IEvent.class );

    // *** Subject ***
    
    @Label( standard = "subject" )
    @Required

    ValueProperty PROP_SUBJECT = new ValueProperty( TYPE, "Subject" );

    Value<String> getSubject();
    void setSubject( String subject );

    // *** Location ***
    
    @Label( standard = "location" )

    ValueProperty PROP_LOCATION = new ValueProperty( TYPE, "Location" );

    Value<String> getLocation();
    void setLocation( String location );

    // *** Notes ***
    
    @Label( standard = "notes" )
    @LongString

    ValueProperty PROP_NOTES = new ValueProperty( TYPE, "Notes" );

    Value<String> getNotes();
    void setNotes( String notes );

    // *** StartTime ***
    
    @Label( standard = "start time" )
    @Required

    ValueProperty PROP_START_TIME = new ValueProperty( TYPE, "StartTime" );

    Value<String> getStartTime();
    void setStartTime( String startTime );

    // *** EndTime ***
    
    @Label( standard = "end time" )
    @Required

    ValueProperty PROP_END_TIME = new ValueProperty( TYPE, "EndTime" );

    Value<String> getEndTime();
    void setEndTime( String endTime );
    
    // *** Attendees ***
    
    @Type( base = IAttendee.class )

    ListProperty PROP_ATTENDEES = new ListProperty( TYPE, "Attendees" );
    
    ModelElementList<IAttendee> getAttendees();
    
    // *** Attachments ***
    
    @Type( base = IEventAttachment.class )

    ListProperty PROP_ATTACHMENTS = new ListProperty( TYPE, "Attachments" );
    
    ModelElementList<IEventAttachment> getAttachments();
    
}
