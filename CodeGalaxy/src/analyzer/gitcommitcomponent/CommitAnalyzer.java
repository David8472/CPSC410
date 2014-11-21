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
    private File directory;//
    
	private ArrayList<CommitAnalyzerInfo> commitAnalyzerInfo = new ArrayList<CommitAnalyzerInfo>();//ArrayList to store all the information gathered
	
	private int numberOfJavaCommits = 0;//Number of commits where Java files are affected
	private int numberOfAllCommits = 0;//Number of commits where ANY files are affected
	
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
		    
		    if(repository.getRef("master") != null){//Checks whether the repository is null
			    Iterable<RevCommit> logs = git.log().call();
			    
			    while(logs.iterator().hasNext()) {
			    	CommitAnalyzerInfo tempCommitAnalyzerInfo = new CommitAnalyzerInfo(repoDirectory);
				    ArrayList<String> tempFileNames = new ArrayList<String>();
			        revCommit = logs.iterator().next();
			        if(revCommit == null){//Checks if the revCommit 
			        	continue;
			        }
			        RevTree tree = revCommit.getTree();//tree for traversing file structure in commit
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
			        
			        if(tempFileNames.size() > 0){//Only bothers with commits where Java files are affected
			        	tempCommitAnalyzerInfo.setAuthorName(revCommit.getAuthorIdent().getName());//stores author name in temp
			        	tempCommitAnalyzerInfo.setCommitID(revCommit.getId().getName());//stores commitID in temp		        	
			        	tempFileNames.clear();
			        	
			        	numberOfJavaCommits++;
			        	tempCommitAnalyzerInfo.setCommitNumber(numberOfJavaCommits);
			        	
			        	commitAnalyzerInfo.add(new CommitAnalyzerInfo(tempCommitAnalyzerInfo));//Adds temp to ArrayList
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
	
	public int getNumberOfJavaCommits() {//Getter for number of Java Commits
		return numberOfJavaCommits;
	}
	
	public int getNumberOfAllCommits() {//Getter for number of all commits.
		return numberOfAllCommits;
	}
	
	public String getRepoDirectory(){//Gets the repo directory
		return repoDirectory;
	}
}
