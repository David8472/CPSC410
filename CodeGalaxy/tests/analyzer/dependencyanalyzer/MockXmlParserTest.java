package analyzer.dependencyanalyzer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created on October 27, 2014 by Ellina.
 * JUnit test cases for the Mock XML Parser class.
 */

public class MockXmlParserTest{

	private DependencyAnalyzerHelper analyzer;
	
	/**
	 * Check if the number of used-by classes
	 * 		corresponds to the size of the usedByVector.
	 */
	@Test
	public void testClassUsedByVectorLength(){
		// given
		String commandStr = "java -jar classycle\\classycle.jar -xmlFile=test3.xml classycle\\samplepayment";
		analyzer = new DependencyAnalyzerHelper();
		//when
		analyzer.runClassycleWithCommand(commandStr);
		if(analyzer.getExitStatus() != 0){
			fail();
		}
		//then
		int parameter = analyzer.getAllClassesDependencies().get(2).getAfferentNum();
		int length = analyzer.getAllClassesDependencies().get(2).getAfferentVectorSize();
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
		String commandStr = "java -jar classycle\\classycle.jar -xmlFile=test4.xml classycle\\samplepayment";
		analyzer = new DependencyAnalyzerHelper();
		//when
		analyzer.runClassycleWithCommand(commandStr);
		if(analyzer.getExitStatus() != 0){
			fail();
		}
		//then
		int parameter = analyzer.getAllClassesDependencies().get(0).getEfferentNum();
		int length = analyzer.getAllClassesDependencies().get(0).getEfferentVectorSize();
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
		String commandStr = "java -jar classycle\\classycle.jar -xmlFile=test5.xml classycle\\samplepayment";
		analyzer = new DependencyAnalyzerHelper();
		//when
		analyzer.runClassycleWithCommand(commandStr);
		if(analyzer.getExitStatus() != 0){
			fail();
		}
		//then
		int parameter = analyzer.getAllPackagesDependencies().get(0).getAfferentNum();
		int length = analyzer.getAllPackagesDependencies().get(0).getAfferentVectorSize();
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
		String commandStr = "java -jar classycle\\classycle.jar -xmlFile=test6.xml classycle\\samplepayment";
		analyzer = new DependencyAnalyzerHelper();
		//when
		analyzer.runClassycleWithCommand(commandStr);
		if(analyzer.getExitStatus() != 0){
			fail();
		}
		//then
		int parameter = analyzer.getAllPackagesDependencies().get(0).getEfferentNum();
		int length = analyzer.getAllPackagesDependencies().get(0).getEfferentVectorSize();
		if(parameter != 0 && length == 0){
			fail();
		}
		assertEquals(parameter, length);
	}
}
