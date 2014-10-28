package analyzer.dependencyanalyzer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created on October 27, 2014 by Ellina.
 * JUnit test cases for the Mock XML Parser class.
 */

public class MockXmlParserTest{

	private DependencyAnalyzer analyzer;
	
	/**
	 * Check if the number of used-by classes
	 * 		corresponds to the size of the usedByVector.
	 */
	@Test
	public void testClassUsedByVectorLength(){
		// given
		String commandStr = "java -jar C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\classycle.jar -xmlFile=test3.xml C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\samplepayment";
		analyzer = new DependencyAnalyzer(commandStr);
		//when
		analyzer.runClassycle();
		//then
		int parameter = analyzer.getAllClassesDependencies().get(2).getNumUsedBy();
		int length = analyzer.getAllClassesDependencies().get(2).getUsedByVectorLength();
		if(parameter != 0 && length == 0){
			fail();
		}
		assertEquals(parameter, length);
	}
	
	/**
	 * Check if the number of classes used by the given class
	 * 		corresponds to the size of the usedByVector.
	 */
	@Test
	public void testClassUsesInternalVectorLength(){
		// given
		String commandStr = "java -jar C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\classycle.jar -xmlFile=test4.xml C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\samplepayment";
		analyzer = new DependencyAnalyzer(commandStr);
		//when
		analyzer.runClassycle();
		//then
		int parameter = analyzer.getAllClassesDependencies().get(0).getNumInternalDependencies();
		int length = analyzer.getAllClassesDependencies().get(0).getInternalVectorLength();
		if(parameter != 0 && length == 0){
			fail();
		}
		assertEquals(parameter, length);
	}
	
	/**
	 * Check if the number of used-by packages
	 * 		corresponds to the actual size of the usedByVector.
	 */
	@Test
	public void testPackageUsedByVectorLength(){
		// given
		String commandStr = "java -jar C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\classycle.jar -xmlFile=test5.xml C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\samplepayment";
		analyzer = new DependencyAnalyzer(commandStr);
		//when
		analyzer.runClassycle();
		//then
		int parameter = analyzer.getAllPackagesDependencies().get(0).getNumUsedBy();
		int length = analyzer.getAllPackagesDependencies().get(0).getUsedByVectorLength();
		if(parameter != 0 && length == 0){
			fail();
		}
		assertEquals(parameter, length);
	}
	
	/**
	 * Check if the number of packages used by the given package
	 * 		corresponds to the number of elements in the usesInternalVector.
	 */
	@Test
	public void testPackageUsesInternalVectorLength(){
		// given
		String commandStr = "java -jar C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\classycle.jar -xmlFile=test6.xml C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\samplepayment";
		analyzer = new DependencyAnalyzer(commandStr);
		//when
		analyzer.runClassycle();
		//then
		int parameter = analyzer.getAllPackagesDependencies().get(0).getNumInternalDependencies();
		int length = analyzer.getAllPackagesDependencies().get(0).getInternalVectorLength();
		if(parameter != 0 && length == 0){
			fail();
		}
		assertEquals(parameter, length);
	}
}
