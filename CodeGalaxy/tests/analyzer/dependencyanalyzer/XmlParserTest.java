package analyzer.dependencyanalyzer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created on November 14, 2014 by Ellina.
 * JUnit test cases for the XML Parser class.
 */

public class XmlParserTest{

	DependencyAnalyzer depAnalyzerTool;
	private final static int CLASSES_TOTAL_NUMBER = 3;
	private final static int PACKAGES_TOTAL_NUMBER = 2;
	private final static int AFFERENT_PACKAGES_NUMBER = 2;
	private final static int EFFERENT_PACKAGES_NUMBER = 3;
	private final static int AFFERENT_CLASSES_NUMBER = 2;
	private final static int EFFERENT_CLASSES_NUMBER = 3;
	private final static int TEST1_INDEX = 0;
	private final static int TEST2_INDEX = 1;
	private final static int TEST3_INDEX = 2;
	private final static int TEST4_INDEX = 1;
	private final static String SAMPLE_PACKAGE_NAME1 = "fruit.exotic";
	private final static String SAMPLE_PACKAGE_NAME2 = "fruit.local";
	private final static String SAMPLE_CLASS_NAME1 = "fruit.local.Cranberry";
	private final static String SAMPLE_CLASS_NAME2 = "fruit.local.Cherry";

	
	/**
	 * Tests if the parser performed correctly given a valid file.
	 * Input: a valid file path.
	 * Expected Output: No exceptions thrown.
	 */
	@Test
	public void validFileTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml", depAnalyzerTool);
		} catch (XmlParserException e) {
			e.printStackTrace();
			fail("Unexpected XML Parser exception was thrown." );
		}
	}

	/**
	 * Tests if the parser performed correctly given an invalid file.
	 * Input: an invalid empty file path.
	 * Expected Output: XmlParserException thrown.
	 */
	@Test
	public void emptyFileTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("", depAnalyzerTool);
			fail("XML Parser failed to throw an exception." );
		} catch (XmlParserException e) {
			System.out.println(e);
		}
	}

	/**
	 * Tests if the parser performed correctly given an invalid file.
	 * Input: an invalid null file path.
	 * Expected Output: XmlParserException thrown.
	 */
	@Test
	public void nullFileTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser(null, depAnalyzerTool);
			fail("XML Parser failed to throw an exception." );
		} catch (XmlParserException e) {
			System.out.println(e);
		}
	}


	/**
	 * Tests the number of afferent packages relative to a given package,
	 * 		which corresponds to the size of a corresponding vector.
	 * Input: the fruit.exotic package that has 2 afferent packages.
	 * Expected output: XML Parser successfully retrieves 2 afferent packages.
	 */
	@Test
	public void afferentPkgNumberTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml", depAnalyzerTool);
			assertEquals(AFFERENT_PACKAGES_NUMBER, depAnalyzerTool.getAllPackagesDependencies().get(TEST1_INDEX).getAfferentVectorSize());
			assertEquals(SAMPLE_PACKAGE_NAME1, depAnalyzerTool.getAllPackagesDependencies().get(TEST1_INDEX).getPackageName());
			assertEquals(AFFERENT_PACKAGES_NUMBER, depAnalyzerTool.getAllPackagesDependencies().get(TEST1_INDEX).getAfferentNum());
		} catch (XmlParserException e) {
			e.printStackTrace();
			fail("Unexpected XML Parser exception was thrown." );
		}
	}


	/**
	 * Tests the number of efferent packages relative to a given package,
	 * 		which corresponds to the size of a corresponding vector.
	 * Input: the fruit.local package that has 3 efferent packages.
	 * Expected output: XML Parser successfully retrieves 3 efferent packages.
	 */
	@Test
	public void efferentPkgNumberTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml", depAnalyzerTool);
			assertEquals(EFFERENT_PACKAGES_NUMBER, depAnalyzerTool.getAllPackagesDependencies().get(TEST2_INDEX).getEfferentVectorSize());
			assertEquals(SAMPLE_PACKAGE_NAME2, depAnalyzerTool.getAllPackagesDependencies().get(TEST2_INDEX).getPackageName());
			assertEquals(EFFERENT_PACKAGES_NUMBER, depAnalyzerTool.getAllPackagesDependencies().get(TEST2_INDEX).getEfferentNum());
		} catch (XmlParserException e) {
			e.printStackTrace();
			fail("Unexpected XML Parser exception was thrown." );
		}
	}

	/**
	 * Tests the number of afferent classes relative to a given class,
	 * 		which corresponds to the size of a corresponding vector.
	 * Input: the fruit.local.Cranberry class that has 2 afferent classes.
	 * Expected output: XML Parser successfully retrieves 2 afferent classes.
	 */
	@Test
	public void afferentClassesNumberTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml", depAnalyzerTool);
			assertEquals(AFFERENT_CLASSES_NUMBER, depAnalyzerTool.getAllClassesDependencies().get(TEST3_INDEX).getAfferentVectorSize());
			assertEquals(SAMPLE_CLASS_NAME1, depAnalyzerTool.getAllClassesDependencies().get(TEST3_INDEX).getClassName());
			assertEquals(AFFERENT_CLASSES_NUMBER, depAnalyzerTool.getAllClassesDependencies().get(TEST3_INDEX).getAfferentNum());
		} catch (XmlParserException e) {
			e.printStackTrace();
			fail("Unexpected XML Parser exception was thrown." );
		}
	}

	/**
	 * Tests the number of efferent classes relative to a given class,
	 * 		which corresponds to the size of a corresponding vector.
	 * Input: the fruit.local.Cherry class that has 3 efferent classes.
	 * Expected output: XML Parser successfully retrieves 3 efferent classes.
	 */
	@Test
	public void efferentClassesNumberTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml", depAnalyzerTool);
			assertEquals(EFFERENT_CLASSES_NUMBER, depAnalyzerTool.getAllClassesDependencies().get(TEST4_INDEX).getEfferentVectorSize());
			assertEquals(SAMPLE_CLASS_NAME2, depAnalyzerTool.getAllClassesDependencies().get(TEST4_INDEX).getClassName());
			assertEquals(EFFERENT_CLASSES_NUMBER, depAnalyzerTool.getAllClassesDependencies().get(TEST4_INDEX).getEfferentNum());
		} catch (XmlParserException e) {
			e.printStackTrace();
			fail("Unexpected XML Parser exception was thrown." );
		}
	}

	/**
	 * Tests the total number of analyzed classes,
	 * 		where each class is represented as an element in the vector.
	 * 		Hence, the vector size should be equal to the number of classes.
	 * Input: 3 classes.
	 * Expected Output: size of the vector = 3.
	 */
	@Test
	public void totalNumberOfClassesTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml", depAnalyzerTool);
			assertEquals(CLASSES_TOTAL_NUMBER, depAnalyzerTool.getAllClassesDependencies().size() );
			if(depAnalyzerTool.getAllClassesDependencies().size() != CLASSES_TOTAL_NUMBER){
				fail("Total number of analyzed classes did not match.");
			}
		} catch (XmlParserException e) {
			e.printStackTrace();
			fail("Unexpected XML Parser exception was thrown." );
		}
	}

	/**
	 * Tests the total number of analyzed packages,
	 * 		where each package is represented as an element in the vector.
	 * 		Hence, the vector size should be equal to the number of packages.
	 * Input: 2 packages.
	 * Expected Output: size of the vector = 2.
	 */
	@Test
	public void totalNumberOfPackagesTest(){
		//given
		depAnalyzerTool = new DependencyAnalyzer();
		XmlParser parser = new XmlParser(depAnalyzerTool);
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml", depAnalyzerTool);
			assertEquals(PACKAGES_TOTAL_NUMBER, depAnalyzerTool.getAllPackagesDependencies().size() );
			if(depAnalyzerTool.getAllPackagesDependencies().size() != PACKAGES_TOTAL_NUMBER){
				fail("Total number of analyzed packages did not match.");
			}
		} catch (XmlParserException e) {
			e.printStackTrace();
			fail("Unexpected XML Parser exception was thrown." );
		}
	}

}
