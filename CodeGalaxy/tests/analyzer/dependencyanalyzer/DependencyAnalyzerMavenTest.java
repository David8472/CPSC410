package analyzer.dependencyanalyzer;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created on by Ellina.
 * JUnit test cases for the DependencyAnalyzer that uses Maven to build projects.
 */

public class DependencyAnalyzerMavenTest{

	private DependencyAnalyzer analyzer;

	/**
	 * Tests if the component compiles and analyzes the code correctly
	 * 		given a valid path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: valid address that contains a pom.xml file.
	 * Expected output: Maven and Classycle run successfully.
	 */
	@Test
	public void succesfulMavenTest(){
		// given
		analyzer = new DependencyAnalyzer();
		//when
		analyzer.runDependencyAnalyzer("test_resources/crawler4j-master");
		//then
		if(analyzer.getMavenExitStatus() != 0 || analyzer.getClassycleExitStatus() != 0){
			fail();
		}
		assertEquals(0, analyzer.getMavenExitStatus());
		assertEquals(0, analyzer.getClassycleExitStatus());
	}

	/**
	 * Tests if the component behaves correctly
	 * 		given a null address to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: null address string.
	 * Expected output: Null path terminates the component in a graceful way.
	 * 					Maven and Classycle are not able to run successfully.
	 */
	@Test
	public void nullCodeBaseAddressTest(){
		// given
		analyzer = new DependencyAnalyzer();
		//when
		analyzer.runDependencyAnalyzer(null);
		//then
		if(analyzer.getMavenExitStatus() == 0 || analyzer.getClassycleExitStatus() == 0){
			fail();
		}
		assertNotEquals(0, analyzer.getMavenExitStatus());
		assertNotEquals(0, analyzer.getClassycleExitStatus());
	}
	
	/**
	 * Tests if the component behaves correctly
	 * 		given an empty path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: empty address string.
	 * Expected output: Empty address terminates the component in a graceful way.
	 * 					Maven and Classycle are not able to run successfully.
	 */
	@Test
	public void emptyCodeBaseAddressCommandTest(){
		// given
		analyzer = new DependencyAnalyzer();
		//when
		analyzer.runDependencyAnalyzer("");
		//then
		if(analyzer.getMavenExitStatus() == 0 || analyzer.getClassycleExitStatus() == 0){
			fail();
		}
		assertNotEquals(0, analyzer.getMavenExitStatus());
		assertNotEquals(0, analyzer.getClassycleExitStatus());
	}
	
	/**
	 * Tests if the compiler fails given an incorrect path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: path that does not contain a pom.xml file.
	 * Expected output: Incorrect path terminates the component in a graceful way.
	 * 					Maven is not able to run successfully.
	 */
	@Test
	public void incorrectCodeBaseAddressCommandTest(){
		// given
		analyzer = new DependencyAnalyzer();
		//when
		analyzer.runDependencyAnalyzer("test_resources");
		//then
		if(analyzer.getMavenExitStatus() == 0){
			fail();
		}
		assertNotEquals(0, analyzer.getMavenExitStatus());
		assertEquals(1, analyzer.getMavenExitStatus());
	}
	
	/**
	 * Tests if the component behaves correctly
	 * 		given an invalid path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: invalid address string.
	 * Expected output: Invalid address results in no Maven or Classycle execution.
	 */
	@Test
	public void invalidAddressCommandTest(){
		// given
		analyzer = new DependencyAnalyzer();
		//when
		analyzer.runDependencyAnalyzer("test_resources/somefolder");
		//then
		if(analyzer.getMavenExitStatus() == 0 || analyzer.getClassycleExitStatus() == 0){
			fail();
		}
		assertNotEquals(0, analyzer.getMavenExitStatus());
		assertNotEquals(0, analyzer.getClassycleExitStatus());
	}

}
