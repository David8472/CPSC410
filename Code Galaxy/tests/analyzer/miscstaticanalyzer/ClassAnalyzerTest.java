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

        if(classInfos.size() != 4) {
            fail();
        }

        // Flags for each of the expected class. Set to true if class is visited.
        boolean classWithInnerClasses = false;
        boolean innerClass = false;
        boolean innerClass2 = false;
        boolean innerInnerClass = false;

        for(ClassInfo classInfo : classInfos) {
            String className = classInfo.getClassName();
            if(className.equals("ClassWithInnerClasses")) {
                classWithInnerClasses = true;
            } else if(className.equals("InnerClass")) {
                innerClass = true;
            } else if(className.equals("InnerClass2")) {
                innerClass2 = true;
            } else if(className.equals("InnerInnerClass")) {
                innerInnerClass = true;
            }
        }

        assertTrue(classWithInnerClasses && innerClass && innerClass2 && innerInnerClass);
    }


    /**
     * Tests whether the ClassInfo object returned by getClassInfo() has a class type of
     * 'Abstract' for an Abstract java class
     * @throws Exception
     */
    public void testAbstractClassType() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/AbstractClass.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        if(classInfos.size() != 1) {
            fail();
        }

        assertEquals("Abstract", classInfos.get(0).getClassType());
    }


    /**
     * Tests whether the ClassInfo object returned by getClassInfo() has a class type of
     * 'Interface' for an java Interface
     * @throws Exception
     */
    public void testInterfaceType() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/MyInterface.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        if(classInfos.size() != 1) {
            fail();
        }

        assertEquals("Interface", classInfos.get(0).getClassType());
    }


    /**
     * Tests whether the ClassInfo object returned by getClassInfo() has a class type of
     * 'Concrete' for a concrete java Class
     * @throws Exception
     */
    public void testConcreteClassType() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/ConcreteClass.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        if(classInfos.size() != 1) {
            fail();
        }

        assertEquals("Concrete", classInfos.get(0).getClassType());
    }


    /**
     * Tests whether the ClassInfo object returned by getClassInfo() has a class type of
     * 'Concrete' for a concrete java Class with concrete inner classes
     * @throws Exception
     */
    public void testConcreteClassWithInnerClassesType() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/Test.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        for(ClassInfo classInfo : classInfos) {
            assertEquals("Concrete", classInfo.getClassType());
        }
    }


    /**
     * Tests whether the ClassInfo object returned by getClassInfo() has the expected
     * class LOC of the class(es)
     * Expected Result: ClassLOCTest - 27, InnerClassLOCTest - 15
     * @throws Exception
     */
    public void testClassLOC() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/ClassLOCTest.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        for(ClassInfo classInfo : classInfos) {
            if(classInfo.getClassName().equals("ClassLOCTest")) {
                assertEquals(classInfo.getLinesOfCode(), 27);
            } else if(classInfo.getClassName().equals("InnerClassLOCTest")) {
                assertEquals(classInfo.getLinesOfCode(), 15);
            } else {
                fail();
            }
        }

    }


    /**
     * Tests if method names are as expected for concrete classes in the ClassInfo object
     * returned by getClassInfo()
     * Expected Result: method name = 'method'
     * @throws Exception
     */
    public void testMethodNameForConcreteClass() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/ConcreteClass.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        ArrayList<MethodInfo> methods = classInfos.get(0).getMethods();

        assertEquals("method", methods.get(0).getMethodName());
    }


    /**
     * Tests if method names are as expected for abstract classes in the ClassInfo object
     * returned by getClassInfo()
     * Expected Result: method name = 'abstractMethod'
     * @throws Exception
     */
    public void testMethodNameForAbstractClass() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/AbstractClass.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        ArrayList<MethodInfo> methods = classInfos.get(0).getMethods();

        assertEquals("abstractMethod", methods.get(0).getMethodName());
    }


    /**
     * Tests if method names are as expected for interfaces in the ClassInfo object
     * returned by getClassInfo()
     * Expected Result: method name = 'interfaceMethod'
     * @throws Exception
     */
    public void testMethodNameForInterface() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/MyInterface.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        ArrayList<MethodInfo> methods = classInfos.get(0).getMethods();

        assertEquals("interfaceMethod", methods.get(0).getMethodName());
    }


    /**
     * Tests if method names are as expected for class with inner classes in the ClassInfo object
     * returned by getClassInfo()
     * Expected Result: true
     * @throws Exception
     */
    public void testMethodNamesForClassWithInnerClasses() throws Exception {
        classAnalyzer = new ClassAnalyzer(new File("test_resources/ClassWithInnerClasses.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        // Flags for each of the expected methods. Set to true if method is visited.
        boolean outerMethod = false;
        boolean innerMethod = false;
        boolean innerMethod2 = false;
        boolean innerInnerMethod = false;
        boolean innerInnerMethod2 = false;

        for(ClassInfo classInfo : classInfos) {
            if(classInfo.getClassName().equals("ClassWithInnerClasses")) {
                for(MethodInfo method : classInfo.getMethods()) {
                    if(method.getMethodName().equals("outerMethod")) {
                        outerMethod = true;
                    }
                }
            } else if(classInfo.getClassName().equals("InnerClass")) {
                for(MethodInfo method : classInfo.getMethods()) {
                    if(method.getMethodName().equals("innerMethod")) {
                        innerMethod = true;
                    }
                }
            } else if(classInfo.getClassName().equals("InnerClass2")) {
                for(MethodInfo method : classInfo.getMethods()) {
                    if(method.getMethodName().equals("innerMethod2")) {
                        innerMethod2 = true;
                    }
                }
            } else if(classInfo.getClassName().equals("InnerInnerClass")) {
                for(MethodInfo method : classInfo.getMethods()) {
                    if(method.getMethodName().equals("innerInnerMethod")) {
                        innerInnerMethod = true;
                    } else if(method.getMethodName().equals("innerInnerMethod2")) {
                        innerInnerMethod2 = true;
                    }
                }
            } else {
                fail();
            }
        }

        // Checks if all methods expected were visited
        assertTrue(outerMethod && innerMethod && innerMethod2 && innerInnerMethod && innerInnerMethod2);
    }


    /**
     * Tests if method return types are as expected for class in the ClassInfo object
     * returned by getClassInfo()
     * @throws Exception
     */
    public void testMethodReturnType() throws Exception {

        classAnalyzer = new ClassAnalyzer(new File("test_resources/DifferentMethods.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        // Flags for each of the expected methods. Set to true if method is visited.
        boolean methodInt = false;
        boolean methodVoid = false;
        boolean methodBoolean = false;
        boolean methodArrayListString = false;

        for(MethodInfo method : classInfos.get(0).getMethods()) {
            if(method.getMethodName().equals("methodInt")) {
                assertEquals("int", method.getReturnType());
                methodInt = true;
            } else if(method.getMethodName().equals("methodVoid")) {
                assertEquals("void", method.getReturnType());
                methodVoid = true;
            } else if(method.getMethodName().equals("methodBoolean")) {
                assertEquals("boolean", method.getReturnType());
                methodBoolean = true;
            } else if(method.getMethodName().equals("methodArrayListString")) {
                assertEquals("ArrayList<String>", method.getReturnType());
                methodArrayListString = true;
            } else {
                fail();
            }
        }

        // Ensures that all methods were visited
        assertTrue(methodInt && methodVoid && methodBoolean && methodArrayListString);
    }


    /**
     * Tests if method lines of code are as expected for class in the ClassInfo object
     * returned by getClassInfo()
     * @throws Exception
     */
    public void testMethodLOC() throws Exception {

        classAnalyzer = new ClassAnalyzer(new File("test_resources/DifferentMethods.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        // Flags for each of the expected methods. Set to true if method is visited.
        boolean methodInt = false;
        boolean methodVoid = false;
        boolean methodBoolean = false;
        boolean methodArrayListString = false;

        for(MethodInfo method : classInfos.get(0).getMethods()) {
            if(method.getMethodName().equals("methodInt")) {
                assertEquals(4, method.getLinesOfCode());
                methodInt = true;
            } else if(method.getMethodName().equals("methodVoid")) {
                assertEquals(2, method.getLinesOfCode());
                methodVoid = true;
            } else if(method.getMethodName().equals("methodBoolean")) {
                assertEquals(3, method.getLinesOfCode());
                methodBoolean = true;
            } else if(method.getMethodName().equals("methodArrayListString")) {
                assertEquals(6, method.getLinesOfCode());
                methodArrayListString = true;
            } else {
                fail();
            }
        }

        // Ensures that all methods were visited
        assertTrue(methodInt && methodVoid && methodBoolean && methodArrayListString);
    }


    /**
     * Test for getClassInfo()
     * @throws Exception
     */
    public void testGetClassInfo() throws Exception {

        classAnalyzer = new ClassAnalyzer(new File("test_resources/DifferentMethods.java"));

        ArrayList<ClassInfo> classInfos = classAnalyzer.getClassInfo();

        assertEquals("DifferentMethods", classInfos.get(0).getClassName());
        assertEquals("mypackage", classInfos.get(0).getPackageName());
        assertEquals(17, classInfos.get(0).getLinesOfCode());
        assertEquals("Concrete", classInfos.get(0).getClassType());

        // Flags for each of the expected methods. Set to true if method is visited.
        boolean methodInt = false;
        boolean methodVoid = false;
        boolean methodBoolean = false;
        boolean methodArrayListString = false;

        for(MethodInfo method : classInfos.get(0).getMethods()) {
            if(method.getMethodName().equals("methodInt")) {
                assertEquals("int", method.getReturnType());
                assertEquals(4, method.getLinesOfCode());
                methodInt = true;
            } else if(method.getMethodName().equals("methodVoid")) {
                assertEquals("void", method.getReturnType());
                assertEquals(2, method.getLinesOfCode());
                methodVoid = true;
            } else if(method.getMethodName().equals("methodBoolean")) {
                assertEquals("boolean", method.getReturnType());
                assertEquals(3, method.getLinesOfCode());
                methodBoolean = true;
            } else if(method.getMethodName().equals("methodArrayListString")) {
                assertEquals("ArrayList<String>", method.getReturnType());
                assertEquals(6, method.getLinesOfCode());
                methodArrayListString = true;
            } else {
                fail();
            }
        }

        // Ensures that all methods were visited
        assertTrue(methodInt && methodVoid && methodBoolean && methodArrayListString);
    }
}

