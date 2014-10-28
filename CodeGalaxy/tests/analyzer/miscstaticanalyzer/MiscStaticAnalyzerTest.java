package analyzer.miscstaticanalyzer;

import junit.framework.TestCase;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Test class for MiscStaticAnalyzer
 */
public class MiscStaticAnalyzerTest extends TestCase {

    private MiscStaticAnalyzer miscStaticAnalyzer;

    private final static String VALID_PATH = "test_resources/codebase1";
    private final static String INVALID_PATH = "test_resources/non_existing_codebase";


    /**
     * Tests getSourceFiles() with a valid path argument to see if all .java files in
     * argument path are returned
     * Expected Result: No exception is thrown
     * @throws Exception
     */
    public void testGetSourceFilesWithValidPathArgument() throws Exception {
        try {
            Method getSourceFiles = MiscStaticAnalyzer.class.getDeclaredMethod("getSourceFiles", String.class);
            getSourceFiles.setAccessible(true);

            Collection<File> files = (Collection<File>) getSourceFiles.invoke(getSourceFiles, VALID_PATH);

            boolean myClass = false;
            boolean myClass2 = false;
            boolean myClass3 = false;
            boolean myClass4 = false;
            boolean myClass5 = false;

            for(File file : files) {
                if(file.getName().equals("MyClass.java")) {
                    myClass = true;
                } else if(file.getName().equals("MyClass2.java")) {
                    myClass2 = true;
                } else if(file.getName().equals("MyClass3.java")) {
                    myClass3 = true;
                } else if(file.getName().equals("MyClass4.java")) {
                    myClass4 = true;
                } else if(file.getName().equals("MyClass5.java")) {
                    myClass5 = true;
                } else {
                    fail();
                }
            }

            assertTrue(myClass && myClass2 && myClass3 && myClass4 && myClass5);

        } catch(InvocationTargetException e) {
            fail();
        }
    }


    /**
     * Tests getSourceFiles() with a non-existing path argument
     * Expected Result: An IllegalArgumentException is thrown
     * @throws Exception
     */
    public void testGetSourceFilesWithInvalidPathArgument() throws Exception {
        try {
            Method getSourceFiles = MiscStaticAnalyzer.class.getDeclaredMethod("getSourceFiles", String.class);
            getSourceFiles.setAccessible(true);

            Collection<File> files = (Collection<File>) getSourceFiles.invoke(getSourceFiles, INVALID_PATH);
            fail();

        } catch(InvocationTargetException e) {
            assertTrue(e.getTargetException() instanceof IllegalArgumentException);
        }
    }

}