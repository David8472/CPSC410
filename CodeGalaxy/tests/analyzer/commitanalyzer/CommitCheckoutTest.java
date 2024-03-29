package analyzer.commitanalyzer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import analyzer.gitcommitcomponent.*;

public class CommitCheckoutTest {
	
	
	
	@Test
	public void checkoutTest(){
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer("test_resources\\PacMan\\.git");
		CommitCheckout commitCheckout = new CommitCheckout("test_resources\\PacMan\\.git");
		String checkoutID = commitAnalyzer.analyzeCommits().get(0).getCommitID();
		commitCheckout.checkout(checkoutID);
		commitCheckout.resetHeadToMaster();
		assertTrue(checkoutID.contentEquals("5687e5fafb877ded820fd7bace31427dd27d438f"));
	}
	
	@Test
	public void resetHeadToMasterTest(){
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer("test_resources\\PacMan\\.git");
		CommitCheckout commitCheckout = new CommitCheckout("test_resources\\PacMan\\.git");
		ArrayList<CommitAnalyzerInfo> commitAnalyzerInfo = commitAnalyzer.analyzeCommits();
		commitCheckout.resetHeadToMaster();
		String masterID = commitAnalyzerInfo.get(commitAnalyzerInfo.size()-1).getCommitID();
		assertTrue(masterID.contentEquals("e31814903e5935b2337888906544c72635f9f656"));
	}
}
