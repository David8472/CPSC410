package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import fuser.DataAggregator;
import analyzer.gitcommitcomponent.*;
import analyzer.miscstaticanalyzer.*;
import analyzer.dependencyanalyzer.*;

public class GlobalController {

    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);
    	System.out.print("Please Enter Repository Directory:");
    	String repoDirectory = input.next();
    	input.close();

    	CommitAnalyzer commitAnalyzer = new CommitAnalyzer(repoDirectory+"/.git");
    	CommitCheckout commitCheckout = new CommitCheckout(repoDirectory+"/.git");
        commitCheckout.resetHeadToMaster();

    	ArrayList<CommitAnalyzerInfo> commitMetaData = new ArrayList<CommitAnalyzerInfo>();
    	commitMetaData = commitAnalyzer.analyzeCommits();

    	DataAggregator dataAggregator = new DataAggregator("src/visualizer/data.yml");

    	HashMap<String, Integer> previousCommit = new HashMap<String, Integer>();//For tracking information

    	for(int i = 0; i < commitMetaData.size() ; i++){
    		commitCheckout.checkout(commitMetaData.get(i).getCommitID());

    		CodebaseStats codebaseStats = MiscStaticAnalyzer.getMiscStaticMetrics(repoDirectory);
    		ArrayList<String> classesRemoved = new ArrayList<String>();
    		HashMap<String, Integer> currentCommit = new HashMap<String, Integer>();

    		DependencyAnalyzer analyzer = new DependencyAnalyzer();
    		analyzer.runDependencyAnalyzer(repoDirectory);

    		if(previousCommit!=null){
	    		Iterator<Map.Entry<String, Integer>> iterator = previousCommit.entrySet().iterator();
	    		currentCommit = codebaseStats.getClassToLOCMap();

	    		while(iterator.hasNext()){
	    			Entry<String, Integer> mapping = iterator.next();
	    			String className = mapping.getKey();
	    			Integer linesOfCode = mapping.getValue();
	    			if(currentCommit.containsKey(className) && currentCommit.get(className).equals(linesOfCode)){
	    					currentCommit.remove(className);//Disregard cases where files didn't change size
	    			}
	    			else{
	    				classesRemoved.add(className);//File from previous commit not in current commit, therefore removed
	    				currentCommit.remove(className);
	    			}
	    		}
	    		previousCommit.clear();
    		}
    		dataAggregator.writeCommitDataToYAMLFile(commitMetaData.get(i), codebaseStats.getPackagesInfo(), currentCommit, classesRemoved, analyzer.getAllPackagesDependencies(), analyzer.getAllClassesDependencies());
    		previousCommit = new HashMap<String, Integer>(codebaseStats.getClassToLOCMap());
    	}
    	commitCheckout.resetHeadToMaster();

        try {

            String operatingSystem = System.getProperty("os.name").toLowerCase();

            if(operatingSystem.contains("win")) {
                // Running on Windows



            } else {
                // Running on non-Windows OS

                execCmd("chmod u+x scripts/runVisualizer.sh");
                System.out.println(execCmd("sh scripts/runVisualizer.sh"));
            }





        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public static String execCmd(String cmd) throws java.io.IOException {
        Process proc = Runtime.getRuntime().exec(cmd);
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String val = "";
        if (s.hasNext()) {
            val = s.next();
        }
        else {
            val = "";
        }
        return val;
    }

}
