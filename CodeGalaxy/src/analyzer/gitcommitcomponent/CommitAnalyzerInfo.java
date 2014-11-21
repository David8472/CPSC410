package analyzer.gitcommitcomponent;

//This is the data on a single commit
public class CommitAnalyzerInfo {
	private String repoDirectory;
	private int commitNumber;//Commit Number is the order of the commit, Ex.) Initial commit is 0, Only commits with java edits counted except initial
	private String authorName;
	private String commitID;
	
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
	
	public int getCommitNumber(){
		return commitNumber;
	}
	
	protected void setCommitNumber(int commitNumber){
		this.commitNumber = commitNumber;
	}
	
	public String getAuthorName(){
		return authorName;
	}
	
	protected void setAuthorName(String authorName){
		this.authorName = authorName;
	}
	
	public String getCommitID(){
		return commitID;
	}
	
	protected void setCommitID(String commitID){
		this.commitID = commitID;
	}
	
	protected void clear(){
		commitNumber = 0;
	}
}
