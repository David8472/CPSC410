package analyzer.commitanalyzer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import analyzer.gitcommitcomponent.*;

public class CommitAnalyzerTest {

	@Test
	public void testAnalyzeCommits() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer("test_resources\\PacMan\\.git");
		ArrayList<CommitAnalyzerInfo> commitAnalyzerInfo = commitAnalyzer.analyzeCommits();
		String repoDirectory = commitAnalyzer.getRepoDirectory();
		String author = commitAnalyzerInfo.get(0).getAuthorName();
		int commitNumber = commitAnalyzerInfo.get(0).getCommitNumber();
		
		assertEquals(commitAnalyzer.getNumberOfJavaCommits(),21);
		assertEquals(commitAnalyzer.getNumberOfAllCommits(),21);
		assertTrue(repoDirectory.contentEquals("test_resources\\PacMan\\.git"));
		assertTrue(author.contentEquals("Thiago Nunes"));
		assertEquals(commitNumber,0);
	}

	@Test
	public void testGetNumberOfJavaCommits() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer("test_resources\\PacMan\\.git");
		commitAnalyzer.analyzeCommits();
		assertEquals(commitAnalyzer.getNumberOfJavaCommits(),21);
	}

	@Test
	public void testGetNumberOfAllCommits() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer("test_resources\\PacMan\\.git");
		commitAnalyzer.analyzeCommits();
		assertEquals(commitAnalyzer.getNumberOfAllCommits(),21);
	}

	@Test
	public void testGetRepoDirectory() {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer("test_resources\\PacMan\\.git");
		commitAnalyzer.analyzeCommits();
		String repoDirectory = commitAnalyzer.getRepoDirectory();
		assertTrue(repoDirectory.contentEquals("test_resources\\PacMan\\.git"));
	}

}
