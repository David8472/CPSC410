package analyzer.dependencyanalyzer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created on November 14, 2014 by Ellina.
 * JUnit test cases for the XML Parser class.
 */

public class XmlParserTest{


	/**
	 * Tests if the parser performed correctly given a valid file.
	 * Input: a valid file path.
	 * Expected Output: No exceptions thrown.
	 */
	@Test
	public void validFileTest(){
		//given
		XmlParser parser = new XmlParser();
		//when
		try {
			parser.startXmlParser("test_resources/sample.xml");
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
		XmlParser parser = new XmlParser();
		//when
		try {
			parser.startXmlParser("");
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
		XmlParser parser = new XmlParser();
		//when
		try {
			parser.startXmlParser(null);
			fail("XML Parser failed to throw an exception." );
		} catch (XmlParserException e) {
			System.out.println(e);
		}
	}

	
	// Packages: Test if size of afferent vector = field value

	// Packages: Test if size of efferent vector = field value

	// Classes: Test if size of afferent vector = field value

	// Classes: Test if size of efferent vector = field value

	// Test size of classVector (must be the same as total # of classes)

	// Test size of packageVector (must be the same as total # of packages)

}
