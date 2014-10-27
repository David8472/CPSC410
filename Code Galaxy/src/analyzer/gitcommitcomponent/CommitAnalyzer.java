package analyzer.gitcommmitcomponent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;


public class CommitAnalyzer{
	private String repoDirectory;
	private ArrayList<ArrayList<String>> repoInfo = new ArrayList<ArrayList<String>>();
	
	public CommitAnalyzer(String repoDirectory){
		this.repoDirectory=repoDirectory;		
	}
	
	public void analyzeCommits(){
		File directory = new File(repoDirectory);
	    RepositoryBuilder builder = new RepositoryBuilder();
	    Repository repository;
		try {
			repository = builder.setGitDir(directory).readEnvironment().findGitDir().build();
			Git git = new Git(repository);
		    RevWalk revWalk = new RevWalk(repository);
		    RevCommit revCommit = null;
		    ArrayList<String> tempInfo = new ArrayList<String>();
		    
		    Iterable<RevCommit> logs = git.log().call();
		    
		    Ref head = repository.getRef("HEAD");
		    RevCommit commit = revWalk.parseCommit(head.getObjectId());
	        RevTree tree = commit.getTree();
	        TreeWalk treeWalk = new TreeWalk(repository);
	        treeWalk.addTree(tree);
	        treeWalk.setRecursive(true);
	        

		    while(logs.iterator().hasNext()) {
		        revCommit = logs.iterator().next();
		        
		        tempInfo.add(revCommit.getAuthorIdent().getName());
		        
		        while (treeWalk.next()) {
		        	if(javaFileCheck(treeWalk.getNameString())){
		        		tempInfo.add(treeWalk.getNameString());
		        		//System.out.println(treeWalk.getNameString());
		        	}
		        }
		        
		        
		        repoInfo.add(tempInfo);
		        //System.out.println(revCommit.getAuthorIdent().getName());
		        //System.out.println(revCommit.getFullMessage());
		        
		        
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<ArrayList<String>> getCommitInfo(){
		return repoInfo;
	}
	
	private boolean javaFileCheck(String fileName){
		boolean isJavaFile = false;
		int length = fileName.length();
		if(length<=5){
			isJavaFile = false;
		}
		else if((fileName.charAt(length-5)=='.')
				&&(fileName.charAt(length-4)=='j')
				&&(fileName.charAt(length-3)=='a')
				&&(fileName.charAt(length-2)=='v')
				&&(fileName.charAt(length-1)=='a')){
			isJavaFile = true;
		}
		return isJavaFile;
	}
}
