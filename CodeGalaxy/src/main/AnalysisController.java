package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import dataaggregation.DataAggregator;
import analyzer.gitcommitcomponent.*;
import analyzer.miscstaticanalyzer.*;
import analyzer.dependencyanalyzer.*;

public class AnalysisController {

    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);//input stream for user input
    	System.out.print("Please Enter Repository Directory:");
    	String repoDirectory = input.next();//Stores the user input into repoDirectory
    	input.close();//closes input stream

    	CommitAnalyzer commitAnalyzer = new CommitAnalyzer(repoDirectory+"/.git");//Object for analyzing commits
    	CommitCheckout commitCheckout = new CommitCheckout(repoDirectory+"/.git");//Object for checking out to different commits
        commitCheckout.resetHeadToMaster();

    	ArrayList<CommitAnalyzerInfo> commitMetaData = new ArrayList<CommitAnalyzerInfo>();//Object with author names and unique commit hash id
    	commitMetaData = commitAnalyzer.analyzeCommits();//Will gather the data for the commit analyzer info

        String prefix_yaml_path = "src/visualizer/";
        String yaml_file_name = "data.yml";
    	DataAggregator dataAggregator = new DataAggregator(prefix_yaml_path + yaml_file_name);

    	HashMap<String, Integer> previousCommit = new HashMap<String, Integer>();//For tracking information of previous commits and comparing to current

    	for(int i = 0; i < commitMetaData.size() ; i++){
    		commitCheckout.checkout(commitMetaData.get(i).getCommitID());

    		CodebaseStats codebaseStats = MiscStaticAnalyzer.getMiscStaticMetrics(repoDirectory);
    		ArrayList<String> classesRemoved = new ArrayList<String>();
    		HashMap<String, Integer> currentCommit = new HashMap<String, Integer>();

    		DependencyAnalyzer analyzer = new DependencyAnalyzer();
    		analyzer.runDependencyAnalyzer(repoDirectory);//Gathers data from dependency analyzer

    		if(previousCommit!=null){
	    		Iterator<Map.Entry<String, Integer>> iterator = previousCommit.entrySet().iterator();//iterator for the map of the previous commit's lines of code
	    		currentCommit = codebaseStats.getClassToLOCMap();//mapping of lines of code to class name

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
    	commitCheckout.resetHeadToMaster();//Makes sure the head is reset at master and not stuck elsewhere

        try {

            String operatingSystem = System.getProperty("os.name").toLowerCase();

            if(operatingSystem.contains("win")) {
                // Running on Windows - Run batch file which will run ruby visualizer script
                System.out.println(execCmd("cmd /C scripts\\runVisualizer.bat " + yaml_file_name));

            } else if(operatingSystem.contains("mac") || operatingSystem.contains("nix") || operatingSystem.contains("nux")) {
                // Running UNIX-based OS - Run bash script which will run ruby visualizer script
                execCmd("chmod u+x scripts/runVisualizer.sh");
                System.out.println(execCmd("sh scripts/runVisualizer.sh " + yaml_file_name));
            } else {
                System.out.println("OS not supported");
                System.exit(-1);
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
