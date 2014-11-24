package analyzer.commitanalyzer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import analyzer.gitcommitcomponent.*;

public class CommitAnalyzerTest {

	String testRepo = "test_resources\\PacMan\\.git";
	@Test
	
	/*Tests if the values found by AnalyzeCommits are correct
	 * The correct values are determined by checking directly on GitHub
	 * They are then compared with assert statements to ensure the output is correct
	 */
	public void testAnalyzeCommits() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer(testRepo);
		ArrayList<CommitAnalyzerInfo> commitAnalyzerInfo = commitAnalyzer.analyzeCommits();
		String repoDirectory = commitAnalyzer.getRepoDirectory();
		String author = commitAnalyzerInfo.get(0).getAuthorName();
		int commitNumber = commitAnalyzerInfo.get(0).getCommitNumber();
		
		assertEquals(commitAnalyzer.getNumberOfJavaCommits(),21);
		assertEquals(commitAnalyzer.getNumberOfAllCommits(),21);
		assertTrue(repoDirectory.contentEquals(testRepo));
		assertTrue(author.contentEquals("Thiago Nunes"));
		assertEquals(commitNumber,0);
	}

	/* The correct value for the number of Java commits are determined by checking directly on GitHub
	 * It is then compared with assert statements to ensure the output is correct
	 */
	@Test
	public void testGetNumberOfJavaCommits() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer(testRepo);
		commitAnalyzer.analyzeCommits();
		assertEquals(commitAnalyzer.getNumberOfJavaCommits(),21);
	}

	/* The correct value for the number of ALL commits are determined by checking directly on GitHub
	 * It is then compared with assert statements to ensure the output is correct
	 */
	@Test
	public void testGetNumberOfAllCommits() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer(testRepo);
		commitAnalyzer.analyzeCommits();
		assertEquals(commitAnalyzer.getNumberOfAllCommits(),21);
	}

	@Test
	public void testGetRepoDirectory() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer(testRepo);
		commitAnalyzer.analyzeCommits();
		String repoDirectory = commitAnalyzer.getRepoDirectory();
		assertTrue(repoDirectory.contentEquals(testRepo));
	}

}
