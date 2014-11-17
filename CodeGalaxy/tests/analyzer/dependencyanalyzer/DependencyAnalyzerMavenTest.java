package analyzer.dependencyanalyzer;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created on by Ellina.
 * JUnit test cases for the DependencyAnalyzer that uses Maven to build projects.
 */

public class DependencyAnalyzerMavenTest{

	private DependencyAnalyzer analyzer;

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
