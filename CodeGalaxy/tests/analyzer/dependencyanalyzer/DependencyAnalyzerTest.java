package analyzer.dependencyanalyzer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on October 26, 2014 by Ellina.
 * JUnit test cases for the DependencyAnalyzer.
 */

public class DependencyAnalyzerTest{

	private DependencyAnalyzer analyzer;

	/**
	 * Tests if the component compiles and analyzes the code correctly
	 * 		given a valid path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: valid path string.
	 * Expected output: Compiler and Classycle run successfully.
	 */
	@Test
	public void successfulPathTest(){
		//given
		analyzer = new DependencyAnalyzer("test_resources/samplesource");
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getCompilerExitStatus() !=0 || analyzer.getClassycleExitStatus() != 0){
			fail();
		}
		assertEquals(analyzer.getClassycleExitStatus(), 0);
		assertEquals(analyzer.getCompilerExitStatus(), 0);
	}

	/**
	 * Tests if the component compiles and analyzes the code correctly
	 * 		given an invalid path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: invalid path string.
	 * Expected output: Invalid path raises an exception.
	 * 					Compiler and Classycle are not able to run successfully.
	 */
	@Test
	public void noSuchFileExceptionTest(){
		//given
		analyzer = new DependencyAnalyzer("test_resources/somefolder");
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getCompilerExitStatus() == 0 || analyzer.getClassycleExitStatus() == 0){
			fail();
		}
		assertNotEquals(analyzer.getCompilerExitStatus(), 0);
		assertNotEquals(analyzer.getClassycleExitStatus(), 0);
	}

	/**
	 * Tests if the component compiles and analyzes the code correctly
	 * 		given a null path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: null path string.
	 * Expected output: Null path terminates the component in a graceful way.
	 * 					Compiler and Classycle are not able to run successfully.
	 */
	@Test
	public void nullPathTest(){
		//given
		analyzer = new DependencyAnalyzer(null);
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getCompilerExitStatus() == 0 || analyzer.getClassycleExitStatus() == 0){
			fail();
		}
		assertNotEquals(analyzer.getCompilerExitStatus(), 0);
		assertNotEquals(analyzer.getClassycleExitStatus(), 0);
	}

	/**
	 * Tests if the component compiles and analyzes the code correctly
	 * 		given an empty path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: empty path string.
	 * Expected output: Empty path terminates the component in a graceful way.
	 * 					Compiler and Classycle are not able to run successfully.
	 */
	@Test
	public void emptyPathTest(){
		//given
		analyzer = new DependencyAnalyzer("");
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getCompilerExitStatus() == 0 || analyzer.getClassycleExitStatus() == 0){
			fail();
		}
		assertNotEquals(analyzer.getCompilerExitStatus(), 0);
		assertNotEquals(analyzer.getClassycleExitStatus(), 0);
	}

	/**
	 * Tests if the compiler fails given an incorrect path to the source code.
	 * Notation: 0 = successful exit, non-zero = error.
	 * Input: path that contains multiple code bases.
	 * Expected output: Incorrect path terminates the component in a graceful way.
	 * 					Compiler is not able to run successfully.
	 */
	@Test
	public void compilerFailTest(){
		//given
		analyzer = new DependencyAnalyzer("test_resources");
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getCompilerExitStatus() == 0){
			fail();
		}
		assertNotEquals(analyzer.getCompilerExitStatus(), 0);
		assertNotEquals(analyzer.getClassycleExitStatus(), 0);
	}

}
