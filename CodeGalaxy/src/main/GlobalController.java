package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import analyzer.gitcommitcomponent.*;
import analyzer.miscstaticanalyzer.*;
import analyzer.dependencyanalyzer.*;

public class GlobalController {

    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);
    	System.out.print("Please Enter Repo Directory:");
    	String repoDirectory = input.next();
    	input.close();
    	
    	CommitAnalyzer commitAnalyzer = new CommitAnalyzer(repoDirectory);
    	ArrayList<CommitAnalyzerInfo> commitAnalyzerInfo = new ArrayList<CommitAnalyzerInfo>();
    	commitAnalyzerInfo = commitAnalyzer.analyzeCommits();
    	
    	HashMap<String, Long> sizeTracker = new HashMap<String, Long>();
    	
    	CommitCheckout commitCheckout = new CommitCheckout(repoDirectory);
    	
    	for(int i = 0; i<commitAnalyzerInfo.size() ; i++){
    		commitCheckout.checkout(commitAnalyzerInfo.get(i).getCommitID());
    	}
    }

}
