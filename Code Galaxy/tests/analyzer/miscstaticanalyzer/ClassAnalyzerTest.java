package analyzer.miscstaticanalyzer;

import japa.parser.ParseException;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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


    /**
     * Tests whether the names of the class and its nested classes are as expected in the
     * list of ClassInfo objects returned by getClassInfo()
     * Expected Result: 4
     * @throws Exception
     */
    public void testClassNames() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/ClassWithInnerClasses.java"));

        // Gets the class info for the class and its nested classes
        // Each nested class is its own ClassInfo object
        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        int correctClassNameCount = 0;
        for(ClassInfo classInfo : classInfos) {
            String className = classInfo.getClassName();
            if(className.equals("ClassWithInnerClasses")) {
                correctClassNameCount++;
            } else if(className.equals("InnerClass")) {
                correctClassNameCount++;
            } else if(className.equals("InnerClass2")) {
                correctClassNameCount++;
            } else if(className.equals("InnerInnerClass")) {
                correctClassNameCount++;
            }
        }

        assertEquals(correctClassNameCount, 4);
    }
}