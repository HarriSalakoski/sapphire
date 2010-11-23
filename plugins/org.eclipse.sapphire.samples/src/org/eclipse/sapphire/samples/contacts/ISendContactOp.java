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

package org.eclipse.sapphire.samples.contacts;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.sapphire.modeling.IExecutableModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Transient;
import org.eclipse.sapphire.modeling.TransientProperty;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DelegateImplementation;
import org.eclipse.sapphire.modeling.annotations.DependsOn;
import org.eclipse.sapphire.modeling.annotations.DerivedValue;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.HtmlContent;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.NonNullValue;
import org.eclipse.sapphire.modeling.annotations.PossibleValues;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.samples.contacts.internal.SendContactOpContentProvider;
import org.eclipse.sapphire.samples.contacts.internal.SendContactOpMethods;
import org.eclipse.sapphire.samples.contacts.internal.SendContactToPossibleValuesProvider;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

@GenerateImpl

public interface ISendContactOp 

    extends IExecutableModelElement
    
{
    ModelElementType TYPE = new ModelElementType(ISendContactOp.class);
    
    // *** Contact ***

    @Type( base = IContact.class )

    TransientProperty PROP_CONTACT = new TransientProperty( TYPE, "Contact" );

    Transient<IContact> getContact();
    void setContact( IContact value );

    // *** To ***

    @Label( standard = "to", full = "message destination" )
    @NonNullValue
    @PossibleValues( service = SendContactToPossibleValuesProvider.class )
    
    ValueProperty PROP_TO = new ValueProperty( TYPE, "To" );

    Value<String> getTo();
    void setTo( String value );
    
    // *** MessageBody ***
    
    @Label( standard = "message body" )
    @DerivedValue( service = SendContactOpContentProvider.class )
    @DependsOn( "Contact" )
    @HtmlContent
    
    ValueProperty PROP_MESSAGE_BODY = new ValueProperty( TYPE, "MessageBody" );
    
    Value<String> getMessageBody();
    
    // ** execute **
    
    @DelegateImplementation( SendContactOpMethods.class )
    
    IStatus execute( IProgressMonitor monitor );

}