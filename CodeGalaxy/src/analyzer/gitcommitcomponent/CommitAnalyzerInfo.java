package analyzer.gitcommitcomponent;

public class CommitAnalyzerInfo {//This is the data on a single commit
	private String repoDirectory;
	private int commitNumber;//Commit Number is the order of the commit, Ex.) Initial commit is 0, Only commits with java edits counted except initial
	private String authorName;
	private String commitID;///SHA-1 hash ID unique to a commit, used for checking
	
	public CommitAnalyzerInfo(String repoDirectory){//Constructor for blank commitData
		this.repoDirectory = repoDirectory;
	}
	
	public CommitAnalyzerInfo(CommitAnalyzerInfo commitData) {//Constructor for copying in data
		this.repoDirectory = commitData.repoDirectory;
		this.setCommitNumber(commitData.getCommitNumber());
		this.authorName  = commitData.getAuthorName();
		this.commitID = commitData.getCommitID();
	}

	public String getRepoDirectory(){
		return repoDirectory;
	}
	
	public int getCommitNumber(){//getter for commit number
		return commitNumber;
	}
	
	protected void setCommitNumber(int commitNumber){//setter for commit number
		this.commitNumber = commitNumber;
	}
	
	public String getAuthorName(){//getter for author name
		return authorName;
	}
	
	protected void setAuthorName(String authorName){//setter for author name
		this.authorName = authorName;
	}
	
	public String getCommitID(){
		return commitID;
	}
	
	protected void setCommitID(String commitID){
		this.commitID = commitID;
	}
	
	protected void clear(){//Clears the commit analyzer info object
		commitNumber = 0;
	}
}
