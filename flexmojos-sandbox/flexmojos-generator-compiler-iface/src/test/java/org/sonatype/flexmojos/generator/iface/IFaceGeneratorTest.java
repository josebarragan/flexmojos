package org.sonatype.flexmojos.generator.iface;

import java.io.File;
import java.io.IOException;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.FileUtils;
import org.sonatype.flexmojos.generator.api.GenerationException;
import org.sonatype.flexmojos.generator.api.GenerationRequest;
import org.sonatype.flexmojos.generator.api.Generator;

import flex2.tools.CompcConfiguration;

@SuppressWarnings( "restriction" )
public class IFaceGeneratorTest
    extends PlexusTestCase
{

    private Generator generator;

    private File files;

    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        this.generator = lookup( Generator.class, "internal-ifaces" );

        files = new File( "./target/files" );
        FileUtils.forceDelete( files );

    }

    @Override
    protected void tearDown()
        throws Exception
    {
        this.generator = null;

        super.tearDown();
    }

    public void testGenerate()
        throws GenerationException, IOException
    {
        GenerationRequest request = new GenerationRequest();
        request.setTransientOutputFolder( files );
        // request.addClass( Configuration.class.getName(), null );
        request.addClass( CompcConfiguration.class.getName(), null );
        // request.addClass( Builder.class.getName(), null );
        // request.addClass( Library.class.getName(), null );
        // request.addClass( Application.class.getName(), null );
        request.setClassLoader( Thread.currentThread().getContextClassLoader() );

        generator.generate( request );

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager( null, null, null );

        Iterable<? extends JavaFileObject> compilationUnits1 =
            fileManager.getJavaFileObjects( new File( files, "org/sonatype/flexmojos/compiler" ).listFiles() );
        assertTrue( "Problems compiling files", compiler.getTask( null, fileManager, null, null, null,
                                                                  compilationUnits1 ).call() );

        fileManager.close();
    }

}
