package analyzer.gitcommitcomponent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

public class CommitAnalyzer{
	private String repoDirectory;
	private RepositoryBuilder builder = new RepositoryBuilder();
    private Repository repository;
    private File directory;
    
	private ArrayList<CommitAnalyzerInfo> commitAnalyzerInfo = new ArrayList<CommitAnalyzerInfo>();
	
	private int numberOfJavaCommits = 0;
	private int numberOfAllCommits = 0;
	
	public CommitAnalyzer(String repoDirectory){//The Constructor
		this.repoDirectory = repoDirectory;
		this.directory = new File(repoDirectory);
	}
	//http://stackoverflow.com/questions/19941597/jgit-use-treewalk-to-list-files-and-folders
	public ArrayList<CommitAnalyzerInfo> analyzeCommits(){//This will analyze the commits of the Repository
	    
		try {
			repository = builder.setGitDir(directory).readEnvironment().findGitDir().build();
			Git git = new Git(repository);
		    RevCommit revCommit = null;
		    
		    if(repository.getRef("master") != null){
			    Iterable<RevCommit> logs = git.log().call();
			    
			    while(logs.iterator().hasNext()) {
			    	CommitAnalyzerInfo tempCommitAnalyzerInfo = new CommitAnalyzerInfo(repoDirectory);
				    ArrayList<String> tempFileNames = new ArrayList<String>();
			        revCommit = logs.iterator().next();
			        if(revCommit == null){
			        	continue;
			        }
			        RevTree tree = revCommit.getTree();
			        TreeWalk treeWalk = new TreeWalk(repository);
			        treeWalk.addTree(tree);
			        treeWalk.setRecursive(true);
			        
			        if(numberOfAllCommits == 0){
			        	commitAnalyzerInfo.add(new CommitAnalyzerInfo(repoDirectory));//First Commit, initial state of empty repository
			        	commitAnalyzerInfo.get(0).setCommitNumber(0);
			        	commitAnalyzerInfo.get(0).setAuthorName(revCommit.getAuthorIdent().getName());
			        	commitAnalyzerInfo.get(0).setCommitID(revCommit.getId().getName());
			        }
			        
			        numberOfAllCommits++;
			        while (treeWalk.next()) {
			        	if(treeWalk.getNameString().contains(".java")){//Will only bother with Java files
			        		tempFileNames.add(new String(treeWalk.getPathString()));
			        	}
			        }
			        
			        if(tempFileNames.size() > 0){
			        	tempCommitAnalyzerInfo.setAuthorName(revCommit.getAuthorIdent().getName());
			        	tempCommitAnalyzerInfo.setCommitID(revCommit.getId().getName());			        	
			        	tempFileNames.clear();
			        	
			        	numberOfJavaCommits++;
			        	tempCommitAnalyzerInfo.setCommitNumber(numberOfJavaCommits);
			        	
			        	commitAnalyzerInfo.add(new CommitAnalyzerInfo(tempCommitAnalyzerInfo));
			        }
			        tempCommitAnalyzerInfo.clear();
			        tempFileNames.clear();
			        treeWalk.reset();
			    }
		    }
		    else{
		    	System.out.println("Invalid Repository at "+repoDirectory);
		    }
		    repository.close();
		    git.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		Collections.reverse(commitAnalyzerInfo);
		
		for(int i = 0; i < commitAnalyzerInfo.size(); i++){
			commitAnalyzerInfo.get(i).setCommitNumber(i);
		}
		
		return commitAnalyzerInfo;
	}
	
	public int getNumberOfJavaCommits() {
		return numberOfJavaCommits;
	}
	
	public int getNumberOfAllCommits() {
		return numberOfAllCommits;
	}
	
	public String getRepoDirectory(){
		return repoDirectory;
	}
}
