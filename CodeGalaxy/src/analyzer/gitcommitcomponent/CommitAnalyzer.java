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
	private ArrayList<ArrayList<String>> repoFiles = new ArrayList<ArrayList<String>>();
	private ArrayList<String> authorNames = new ArrayList<String>();
	private ArrayList<CommitData> commitData = new ArrayList<CommitData>();
	
	private int numberOfJavaCommits=0;
	private int numberOfAllCommits=0;
	
	public CommitAnalyzer(String repoDirectory){//The Constructor
		this.repoDirectory=repoDirectory;		
	}
	
	public ArrayList<CommitData> analyzeCommits(){//This will analyze the commits of the Repository
		File directory = new File(repoDirectory);
	    RepositoryBuilder builder = new RepositoryBuilder();
	    Repository repository;
	    
		try {
			repository = builder.setGitDir(directory).readEnvironment().findGitDir().build();
			Git git = new Git(repository);
		    RevCommit revCommit = null;
		    //ArrayList<String> tempInfo = new ArrayList<String>();
		    
		    if(repository.getRef("master") != null){
			    Iterable<RevCommit> logs = git.log().call();
			    //commitData.add(tempCommitData);//First Commit, initial state of empty repository
			    commitData.add(new CommitData(repoDirectory));
			    while(logs.iterator().hasNext()) {
			    	CommitData tempCommitData = new CommitData(repoDirectory);
				    ArrayList<String> tempStringArray = new ArrayList<String>();
			        revCommit = logs.iterator().next();
			        RevTree tree = revCommit.getTree();
			        TreeWalk treeWalk = new TreeWalk(repository);
			        treeWalk.addTree(tree);
			        treeWalk.setRecursive(true);
			        
			        numberOfAllCommits++;
			        while (treeWalk.next()) {
			        	if(treeWalk.getNameString().contains(".java")){//Will only bother with Java files
			        		//tempInfo.add(new String(treeWalk.getNameString()));
			        		//tempCommitData.getFilesChanged().add(new String(treeWalk.getNameString()));
			        		tempStringArray.add(new String(treeWalk.getNameString()));
			        		//System.out.println(tempCommitData.getFilesAdded().size());
			        		
			        		
			        	}
			        }
			        //System.out.println(tempStringArray.toString());
			        if(tempStringArray.size() > 0){
			        	tempCommitData.setAllJavaFiles(new ArrayList<String>(tempStringArray));
			        	tempStringArray.clear();
			        	
			        	//Adds all files in current commit, removes the files in previous commit, ends up with list of files added
			        	tempStringArray.addAll(tempCommitData.getAllJavaFiles());
			        	tempStringArray.removeAll(commitData.get(numberOfJavaCommits).getAllJavaFiles());
			        	tempCommitData.setFilesAdded(new ArrayList<String>(tempStringArray));
			        	tempStringArray.clear();
			        	
			        	//Adds all files of previous commit, removes files in current commit, ends up with list of files deleted
			        	tempStringArray.addAll(commitData.get(numberOfJavaCommits).getAllJavaFiles());
			        	tempStringArray.removeAll(tempCommitData.getAllJavaFiles());
			        	tempCommitData.setFilesDeleted(new ArrayList<String>(tempStringArray));
			        	tempStringArray.clear();
			        	
			        	numberOfJavaCommits++;
			        	commitData.add(new CommitData(tempCommitData));
			        }
			        //if(tempInfo.size()>0){
			        	//authorNames.add(revCommit.getAuthorIdent().getName());
			        	//repoFiles.add(new ArrayList<String>(tempInfo));
			        //}
			        //tempInfo.clear();
			        tempCommitData.clear();
			        tempStringArray.clear();
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
		Collections.reverse(repoFiles);
		Collections.reverse(authorNames);
		return commitData;
		
	}
	
	public ArrayList<ArrayList<String>> getFileNames(){
		return repoFiles;
	}
	
	public ArrayList<ArrayList<String>> getFilesAdded(){
		ArrayList<ArrayList<String>> filesAdded = new ArrayList<ArrayList<String>>();
		filesAdded.add(repoFiles.get(0));
		
		ArrayList<String> temp = new ArrayList<String>();
		for(int i = 1; i < repoFiles.size(); i++){
			temp.addAll(repoFiles.get(i));
			temp.removeAll(repoFiles.get(i-1));
			filesAdded.add(new ArrayList<String>(temp));
			temp.clear();
		}
		
		return filesAdded;
	}
	
	public ArrayList<ArrayList<String>> getFilesRemoved(){
		ArrayList<ArrayList<String>> filesRemoved = new ArrayList<ArrayList<String>>();
		filesRemoved.add(new ArrayList<String>());
		ArrayList<String> temp = new ArrayList<String>();
		
		for(int i = 1; i < repoFiles.size(); i++){
			temp.addAll(repoFiles.get(i-1));
			temp.removeAll(repoFiles.get(i));
			filesRemoved.add(new ArrayList<String>(temp));
			temp.clear();
		}
		
		return filesRemoved;
	}
	
	public ArrayList<String> getAuthorNames(){
		return authorNames;
	}
	

	public int getNumberOfJavaCommits() {
		return numberOfJavaCommits;
	}
	
	public int getNumberOfAllCommits() {
		return numberOfAllCommits;
	}
}
