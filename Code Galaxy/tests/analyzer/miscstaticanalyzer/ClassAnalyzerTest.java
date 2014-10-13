package analyzer.miscstaticanalyzer;

import japa.parser.ParseException;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Test class for ClassAnalyzer
 */
public class ClassAnalyzerTest extends TestCase {

    private ClassAnalyzer classAnalyzer;

    /**
     * Tests constructor by passing in a non-existing file as an argument
     * Expected Result: A FileNotFoundException should be thrown
     * @throws Exception
     */
    public void testConstructorWithNonExistingFile() throws Exception {
        try {
            classAnalyzer = new ClassAnalyzer(new File("test_resources/codebase1/non_existing_file.java"));
            fail();
        } catch(FileNotFoundException e) {

        }
    }


    /**
     * Tests constructor with a Java file that has syntax errors as an argument
     * Expected Result: A ParseException should be thrown
     * @throws Exception
     */
    public void testConstructorWithSyntaxErrorJavaFile() throws Exception {
        try {
            classAnalyzer = new ClassAnalyzer(new File("test_resources/FileWithErrors.java"));
            fail();
        } catch(ParseException e) {

        }
    }


    /**
     * Tests constructor with a non-Java file as an argument
     * Expected Result: An IllegalArgumentException should be thrown
     * @throws Exception
     */
    public void testConstructorWithNonJavaFile() throws Exception {
        try {
            classAnalyzer = new ClassAnalyzer(new File("test_resources/Text.txt"));
            fail();
        } catch(IllegalArgumentException e) {

        }
    }


    /**
     * Tests getPackage() method to see if the correct package for a class is returned
     * Expected Result: "test"
     * @throws Exception
     */
    public void testGetPackage() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/Test.java"));
        assertEquals("test", classAnalyzer.getPackage());
    }
}