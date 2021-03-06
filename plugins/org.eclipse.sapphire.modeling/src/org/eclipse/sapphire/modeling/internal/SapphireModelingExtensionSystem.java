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

package org.eclipse.sapphire.modeling.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.sapphire.Context;
import org.eclipse.sapphire.modeling.ExtensionsLocator;
import org.eclipse.sapphire.modeling.LoggingService;
import org.eclipse.sapphire.modeling.el.Function;
import org.eclipse.sapphire.modeling.el.FunctionException;
import org.eclipse.sapphire.modeling.util.NLS;
import org.eclipse.sapphire.services.Service;
import org.eclipse.sapphire.services.ServiceContext;
import org.eclipse.sapphire.services.ServiceFactory;
import org.eclipse.sapphire.services.ServiceFactoryProxy;
import org.eclipse.sapphire.util.ListFactory;
import org.eclipse.sapphire.util.SetFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin Komissarchik</a>
 */

public final class SapphireModelingExtensionSystem
{
    private static final String EL_SERVICE = "service";
    private static final String EL_FUNCTION = "function";
    private static final String EL_NAME = "name";
    private static final String EL_TYPE = "type";
    private static final String EL_FACTORY = "factory";
    private static final String EL_IMPLEMENTATION = "implementation";
    private static final String EL_IMPL = "impl";
    private static final String EL_ID = "id";
    private static final String EL_OPERAND_COUNT = "operand-count";
    private static final String EL_OVERRIDES = "overrides";
    private static final String EL_CONTEXT = "context";

    private static boolean initialized = false;
    private static List<ServiceFactoryProxy> serviceFactories;
    private static Map<String,List<FunctionFactory>> functionFactories;

    public static List<ServiceFactoryProxy> getServiceFactories()
    {
        initialize();
        return serviceFactories;
    }

    public static Function createFunction( final String name,
                                           final Function... operands )
    {
        initialize();

        final List<FunctionFactory> factories = functionFactories.get( name.toLowerCase() );

        if( factories != null )
        {
            for( FunctionFactory factory : factories )
            {
                final Function function = factory.create( operands );
                
                if( function != null )
                {
                    return function;
                }
            }
            
            if( operands.length == 1 )
            {
                throw new FunctionException( NLS.bind( Resources.undefinedFunctionMessageExt1, name ) );
            }
            else
            {
                throw new FunctionException( NLS.bind( Resources.undefinedFunctionMessageExt, name, operands.length ) );
            }
        }
        
        throw new FunctionException( NLS.bind( Resources.undefinedFunctionMessage, name ) );
    }
    
    private static synchronized void initialize()
    {
        if( ! initialized )
        {
            initialized = true;
            serviceFactories = new ArrayList<ServiceFactoryProxy>();
            functionFactories = new HashMap<String,List<FunctionFactory>>();

            for( final ExtensionsLocator.Handle handle : ExtensionsLocator.instance().find() )
            {
                final Element root = parse( handle.extension() );

                if( root != null )
                {
                    final NodeList nodes = root.getChildNodes();
                    final Context context = handle.context();

                    for( int i = 0, n = nodes.getLength(); i < n; i++ )
                    {
                        final Node node = nodes.item( i );

                        if( node instanceof Element )
                        {
                            final Element el = (Element) node;
                            final String elname = el.getLocalName();

                            try
                            {
                                if( elname.equals( EL_SERVICE ) )
                                {
                                    final String id = value( el, EL_ID );
                                    final Set<String> contexts = values( el, EL_CONTEXT );
                                    final Set<String> overrides = values( el, EL_OVERRIDES );
                                    
                                    try
                                    {
                                        final Class<? extends Service> serviceImplClass = context.findClass( value( el, EL_IMPLEMENTATION ) );
                                        
                                        if( serviceImplClass != null )
                                        {
                                            serviceFactories.add
                                            (
                                                new ServiceFactoryProxy()
                                                {
                                                    @Override
                                                    public String id()
                                                    {
                                                        return id;
                                                    }
                                                    
                                                    @Override
                                                    public Class<? extends Service> type()
                                                    {
                                                        return serviceImplClass;
                                                    }
                                
                                                    @Override
                                                    public Set<String> overrides()
                                                    {
                                                        return overrides;
                                                    }
                                                    
                                                    @Override
                                                    protected boolean applicableHandOff( final ServiceContext context,
                                                                                         final Class<? extends Service> service )
                                                    {
                                                        return contexts.contains( context.type() );
                                                    }

                                                    @Override
                                                    protected Service createHandOff( ServiceContext context,
                                                                                     Class<? extends Service> service )
                                                    {
                                                        Service instance = null;
                                                        
                                                        try
                                                        {
                                                            instance = serviceImplClass.newInstance();
                                                        }
                                                        catch( Exception e )
                                                        {
                                                            LoggingService.log( e );
                                                        }
                                                        
                                                        return instance;
                                                    }
                                                }
                                            );
                                        }
                                    }
                                    catch( InvalidExtensionException e )
                                    {
                                        final Class<? extends Service> serviceType = context.findClass( value( el, EL_TYPE ) );
                                        final Class<? extends ServiceFactory> serviceFactory = context.findClass( value( el, EL_FACTORY ) );
                                        
                                        if( serviceType == null || serviceFactory == null )
                                        {
                                            // TODO: Report this.
                                        }
                                        else
                                        {
                                            serviceFactories.add( new ServiceFactoryProxyImpl( id, serviceType, serviceFactory, contexts, overrides ) );
                                        }
                                    }
                                }
                                else if( elname.equals( EL_FUNCTION ) )
                                {
                                    final String name = value( el, EL_NAME ).toLowerCase();
                                    final Class<? extends Function> impl = context.findClass( value( el, EL_IMPL ) );
                                    final Set<Integer> compatibleOperandCounts = integers( el, EL_OPERAND_COUNT );
                                    
                                    final ListFactory<FunctionFactory> factories = ListFactory.start();
                                    
                                    factories.add( functionFactories.get( name ) );
                                    factories.add( new FunctionFactory( impl, compatibleOperandCounts ) );
                                    
                                    functionFactories.put( name, factories.result() );
                                }
                            }
                            catch( InvalidExtensionException e ) {}
                        }
                    }
                }
            }
        }
    }
    
    private static Element parse( final URL url )
    {
        try
        {
            final InputStream in = url.openStream();

            try
            {
                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                factory.setValidating( false );
                factory.setNamespaceAware( true );
                factory.setIgnoringComments( false );

                final DocumentBuilder docbuilder = factory.newDocumentBuilder();

                docbuilder.setEntityResolver
                (
                    new EntityResolver()
                    {
                        public InputSource resolveEntity( final String publicID,
                                                          final String systemID )
                        {
                            return new InputSource( new StringReader( "" ) );
                        }
                    }
                );

                final Document document = docbuilder.parse( in );

                return document.getDocumentElement();
            }
            finally
            {
                try
                {
                    in.close();
                }
                catch( IOException e ) {}
            }
        }
        catch( Exception e )
        {
            // TODO: Log the problem.
            return null;
        }
    }

    private static String value( final Element element )
    {
        final StringBuilder buf = new StringBuilder();
        final NodeList nodes = element.getChildNodes();
    
        for( int i = 0, n = nodes.getLength(); i < n; i++ )
        {
            final Node node = nodes.item( i );
    
            if( node instanceof Text )
            {
                buf.append( ( (Text) node ).getData() );
            }
        }
    
        return buf.toString().trim();
    }

    private static String value( final Element element,
                                 final String valueElementName )
    {
        final NodeList nodes = element.getChildNodes();

        for( int i = 0, n = nodes.getLength(); i < n; i++ )
        {
            final Node node = nodes.item( i );

            if( node instanceof Element )
            {
                final Element el = (Element) node;

                if( valueElementName.equals( el.getLocalName() ) )
                {
                    return value( el );
                }
            }
        }

        // TODO: Log the problem.
        throw new InvalidExtensionException();
    }

    private static Set<String> values( final Element root,
                                       final String entryElementName )
    {
        final SetFactory<String> factory = SetFactory.start();
        final NodeList nodes = root.getChildNodes();
    
        for( int i = 0, n = nodes.getLength(); i < n; i++ )
        {
            final Node node = nodes.item( i );
    
            if( node instanceof Element && node.getLocalName().equals( entryElementName ) )
            {
                final String text = value( (Element) node );
                
                if( text.length() > 0 )
                {
                    factory.add( text );
                }
            }
        }
        
        return factory.result();
    }
    
    private static Set<Integer> integers( final Element root,
                                          final String entryElementName )
    {
        final SetFactory<Integer> factory = SetFactory.start();
        
        for( String string : values( root, entryElementName ) )
        {
            final int integer;
            
            try
            {
                integer = Integer.parseInt( string );
            }
            catch( NumberFormatException e )
            {
                throw new InvalidExtensionException();
            }
            
            factory.add( integer );
        }
        
        return factory.result();
    }

    public static final class InvalidExtensionException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
    }

    private static final class ServiceFactoryProxyImpl extends ServiceFactoryProxy
    {
        private final String id;
        private final Class<? extends Service> type;
        private final Class<? extends ServiceFactory> factoryClass;
        private ServiceFactory factoryInstance;
        private boolean factoryInstantiationFailed;
        private final Set<String> contexts;
        private final Set<String> overrides;

        public ServiceFactoryProxyImpl( final String id,
                                        final Class<? extends Service> type,
                                        final Class<? extends ServiceFactory> factoryClass,
                                        final Set<String> contexts,
                                        final Set<String> overrides )
        {
            this.id = id;
            this.type = type;
            this.factoryClass = factoryClass;
            this.contexts = contexts;
            this.overrides = overrides;
        }
        
        @Override
        public String id()
        {
            return this.id;
        }

        @Override
        public Class<? extends Service> type()
        {
            return this.type;
        }

        @Override
        public Set<String> overrides()
        {
            return this.overrides;
        }

        @Override
        protected boolean applicableHandOff( final ServiceContext context,
                                             final Class<? extends Service> service )
        {
            boolean result = false;
            final ServiceFactory factory = factory();
            
            if( factory != null && this.contexts.contains( context.type() ) )
            {
                result = factory.applicable( context, service );
            }
            
            return result;
        }

        @Override
        protected Service createHandOff( final ServiceContext context,
                                         final Class<? extends Service> service )
        {
            Service result = null;
            final ServiceFactory factory = factory();
            
            if( factory != null )
            {
                result = factory.create( context, service );
            }
            
            return result;
        }
        
        private synchronized ServiceFactory factory()
        {
            if( this.factoryInstance == null && ! this.factoryInstantiationFailed )
            {
                try
                {
                    this.factoryInstance = this.factoryClass.newInstance();
                }
                catch( Exception e )
                {
                    LoggingService.log( e );
                    this.factoryInstantiationFailed = true;
                }
            }

            return this.factoryInstance;
        }
    }

    private static final class FunctionFactory
    {
        private final Class<? extends Function> functionClass;
        private boolean functionInstantiationFailed;
        private final Set<Integer> compatibleOperandCounts;

        public FunctionFactory( final Class<? extends Function> functionClass,
                                final Set<Integer> compatibleOperandCounts )
        {
            this.functionClass = functionClass;
            this.compatibleOperandCounts = compatibleOperandCounts;
        }

        public Function create( final Function... operands )
        {
            Function function = null;

            if( ! this.functionInstantiationFailed )
            {
                if( this.compatibleOperandCounts.isEmpty() || this.compatibleOperandCounts.contains( operands.length ) )
                {
                    try
                    {
                        function = this.functionClass.newInstance();
                        function.init( operands );
                    }
                    catch( Exception e )
                    {
                        LoggingService.log( e );
                        function = null;
                        this.functionInstantiationFailed = true;
                    }
                }
            }

            return function;
        }
    }
    
    private static final class Resources extends NLS
    {
        public static String undefinedFunctionMessage;
        public static String undefinedFunctionMessageExt;
        public static String undefinedFunctionMessageExt1;
        
        static
        {
            initializeMessages( SapphireModelingExtensionSystem.class.getName(), Resources.class );
        }
    }

}