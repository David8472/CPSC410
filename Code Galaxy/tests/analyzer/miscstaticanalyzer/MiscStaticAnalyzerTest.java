package analyzer.miscstaticanalyzer;

import junit.framework.TestCase;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Test class for MiscStaticAnalyzer
 */
public class MiscStaticAnalyzerTest extends TestCase {

    private MiscStaticAnalyzer miscStaticAnalyzer;

    private final static String VALID_PATH = "test_resources/codebase1";
    private final static String INVALID_PATH = "test_resources/non_existing_codebase";


    /**
     * Tests constructor with a valid path argument
     * Expected Result: No exception is thrown
     *
     * @throws Exception
     */
    public void testConstructorWithValidPathArgument() throws Exception {
        try {
            miscStaticAnalyzer = new MiscStaticAnalyzer(VALID_PATH);
        } catch(IllegalArgumentException e) {
            fail();
        }
    }


    /**
     * Tests constructor with an non-existing path argument
     * Expected Result: An IllegalArgumentException is thrown
     *
     * @throws Exception
     */
    public void testConstructorWithInvalidPathArgument() throws Exception {
        try {
            miscStaticAnalyzer = new MiscStaticAnalyzer(INVALID_PATH);
            fail();
        } catch(IllegalArgumentException e) {

        }
    }


    /**
     * Tests whether getSourceFiles() correctly adds all the .java files in the codebase
     * to private field member 'sourceFiles'
     * Expected Result: Test should pass
     *
     * @throws Exception
     */
    public void testSourceFiles() throws Exception {
        miscStaticAnalyzer = new MiscStaticAnalyzer(VALID_PATH);

        Field field = MiscStaticAnalyzer.class.getDeclaredField("sourceFiles");
        field.setAccessible(true);

        Collection<File> actualFiles = (Collection<File>) field.get(miscStaticAnalyzer);

        String[] filenames = {"MyClass.java", "MyClass2.java", "MyClass3.java", "MyClass4.java", "MyClass5.java"};
        ArrayList<String> expectedFiles = new ArrayList<String>(Arrays.asList(filenames));

        for(File file : actualFiles) {
            // If actual file is not found in list of expected files, test fails
            if(!expectedFiles.contains(file.getName())) {
                fail();
            }
        }
    }
}