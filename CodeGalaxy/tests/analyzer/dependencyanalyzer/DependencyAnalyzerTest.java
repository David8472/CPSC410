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
	 * Tests if the command executed in a separate process exited correctly
	 * 		given the correct command string.
	 * 0 = successful exit, non-zero = error.
	 */
	@Test
	public void succesfulExitValueTest(){
		// given
		String commandStr = "java -jar classycle\\classycle.jar -xmlFile=test1.xml classycle\\samplepayment";
		analyzer = new DependencyAnalyzer(commandStr);
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getExitStatus() != 0){
			fail();
		}
		assertEquals(analyzer.getExitStatus(), 0);
	}

	/**
	 * Tests if the command executed in a separate process exited correctly
	 * 			given the incorrect command string.
	 */
	@Test
	public void failExitValueTest(){
		// given
		String commandStr = "java -jar classycle\\classyce.jar -xmlFile=test2.xml classycle\\samplepayment";
		analyzer = new DependencyAnalyzer(commandStr);
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getExitStatus() == 0){
			fail();
		}
		assertEquals(analyzer.getExitStatus(), 1);
	}

	/**
	 * Tests the analyzer given the null command string;
	 */
	@Test
	public void nullCommandTest(){
		// given
		String commandStr = null;
		analyzer = new DependencyAnalyzer(commandStr);
		//when
		analyzer.runClassycle();
		//then
		if(analyzer.getExitStatus() == 0){
			fail();
		}
		assertEquals(analyzer.getExitStatus(), -1);
	}
}
