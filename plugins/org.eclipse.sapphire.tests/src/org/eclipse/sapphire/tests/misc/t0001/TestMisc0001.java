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

package org.eclipse.sapphire.tests.misc.t0001;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.sapphire.Sapphire;
import org.eclipse.sapphire.Version;
import org.eclipse.sapphire.VersionConstraint;
import org.eclipse.sapphire.tests.SapphireTestCase;

/**
 * Tests Sapphire.version() method.
 * 
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class TestMisc0001 extends SapphireTestCase
{
    private static final String EXPECTED_VERSION_CONSTRAINT = "[0.7-0.7.1)";

    private TestMisc0001( final String name )
    {
        super( name );
    }
    
    public static Test suite()
    {
        final TestSuite suite = new TestSuite();
        
        suite.setName( "TestMisc0001" );

        suite.addTest( new TestMisc0001( "testSapphireVersion" ) );
        
        return suite;
    }
    
    public void testSapphireVersion() throws Exception
    {
        final Version version = Sapphire.version();
        
        assertNotNull( version );
        
        final VersionConstraint constraint = new VersionConstraint( EXPECTED_VERSION_CONSTRAINT );
        
        assertTrue( constraint.check( version ) );
    }

}
