package analyzer.gitcommitcomponent;

import java.util.ArrayList;

//This is the data on a single commit
public class CommitAnalyzerInfo {
	private String repoDirectory;
	private int commitNumber;//Commit Number is the order of the commit, Ex.) Initial commit is 0, Only commits with java edits counted except initial
	private ArrayList<String> filesAdded = new ArrayList<String>();
	private ArrayList<String> allJavaFiles = new ArrayList<String>();
	private ArrayList<String> filesDeleted = new ArrayList<String>();
	private ArrayList<String> filePaths = new ArrayList<String>();
	private ArrayList<String> filesChanged = new ArrayList<String>();

	private String authorName;
	private String commitID;
	
	public CommitAnalyzerInfo(String repoDirectory){//Constructor for blank commitData
		this.repoDirectory = repoDirectory;
	}
	
	public CommitAnalyzerInfo(CommitAnalyzerInfo commitData) {//Constructor for copying in data
		this.repoDirectory = commitData.repoDirectory;
		this.setCommitNumber(commitData.getCommitNumber());
		this.filesAdded.addAll(commitData.getFilesAdded());
		this.allJavaFiles.addAll(commitData.getAllJavaFiles());
		this.filesDeleted.addAll(commitData.getFilesDeleted());
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
	
	public ArrayList<String> getFilesAdded(){
		return filesAdded;
	}
	
	protected void setFilesAdded(ArrayList<String> filesAdded){
		this.filesAdded.addAll(filesAdded);
	}
	
	public ArrayList<String> getAllJavaFiles(){
		return allJavaFiles;
	}
	
	protected void setAllJavaFiles(ArrayList<String> allJavaFiles){
		this.allJavaFiles.addAll(allJavaFiles);
	}
	
	public ArrayList<String> getFilesDeleted(){
		return filesDeleted;
	}
	
	protected void setFilesDeleted(ArrayList<String> filesDeleted){
		this.filesDeleted.addAll(filesDeleted);
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
		filesAdded.clear();
		allJavaFiles.clear();
		filesDeleted.clear();
	}

	public ArrayList<String> getFilePaths() {
		return filePaths;
	}

	protected void setFilePaths(ArrayList<String> filePaths) {
		this.filePaths.addAll(filePaths);
	}

	public ArrayList<String> getFilesChanged() {
		return filesChanged;
	}

	protected void setFilesChanged(ArrayList<String> filesChanged) {
		this.filesChanged = filesChanged;
	}
	
}
