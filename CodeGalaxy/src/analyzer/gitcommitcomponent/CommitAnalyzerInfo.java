package analyzer.gitcommitcomponent;

package gitcommitcomponent;

import java.util.ArrayList;

public class CommitAnalyzerInfo {
	private String repoDirectory;
	private int numberOfCommits;
	private ArrayList<ArrayList<String>> filesAdded = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> filesChanged = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> filesDeleted = new ArrayList<ArrayList<String>>();
	private ArrayList<String> authorNames = new ArrayList<String>();	
	private ArrayList<String> commitHashID = new ArrayList<String>();
	
	public CommitData(String repoDirectory){//Constructor
		this.repoDirectory = repoDirectory;
	}
	
	public int getNumberOfCommits(){
		return numberOfCommits;
	}
	
	public ArrayList<String> getFilesAdded(int commitNumber){
		return filesAdded.get(commitNumber);
	}
	
	public ArrayList<String> getFilesChanged(int commitNumber){
		return filesChanged.get(commitNumber);
	}
	
	public ArrayList<String> getFilesDeleted(int commitNumber){
		return filesDeleted.get(commitNumber);
	}
	
	public String getAuthorName(int commitNumber){
		return authorNames.get(commitNumber);
	}
	
	public String getCommitHashID(int commitNumber){
		return commitHashID.get(commitNumber);
	}
	
}
