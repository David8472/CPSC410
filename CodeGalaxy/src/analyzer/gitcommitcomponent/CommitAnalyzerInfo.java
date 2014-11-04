package analyzer.gitcommitcomponent;

import java.util.ArrayList;

public class CommitData {
	private String repoDirectory;
	private int commitNumber;//Commit Number is the order of the commit, Ex.) Initial commit is 1
	private ArrayList<String> filesAdded = new ArrayList<String>();
	private ArrayList<String> filesChanged = new ArrayList<String>();
	private ArrayList<String> filesDeleted = new ArrayList<String>();
	private String authorNames;
	private String commitHashID;
	
	public CommitData(String repoDirectory, int commitNumber){//Constructor
		this.repoDirectory = repoDirectory;
		this.commitNumber = commitNumber;
	}
	
	public int getCommitNumber(){
		return commitNumber;
	}
	
	public ArrayList<String> getFilesAdded(){
		return filesAdded;
	}
	
	public ArrayList<String> getFilesChanged(){
		return filesChanged;
	}
	
	public ArrayList<String> getFilesDeleted(){
		return filesDeleted;
	}
	
	public String getAuthorName(){
		return authorNames;
	}
	
	public String getCommitHashID(int commitNumber){
		return commitHashID;
	}
	
}
