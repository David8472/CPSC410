package analyzer.gitcommmitcomponent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private ArrayList<ArrayList<String>> repoFiles = new ArrayList<ArrayList<String>>();
	private ArrayList<String> authorNames = new ArrayList<String>();
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
		    
		    
		   // Ref head = repository.getRef("HEAD");

		    while(logs.iterator().hasNext()) {
		    	
			    //RevCommit commit = revWalk.parseCommit(head.getObjectId());
		        
		        revCommit = logs.iterator().next();
		        
		        RevTree tree = revCommit.getTree();
		        TreeWalk treeWalk = new TreeWalk(repository);
		        treeWalk.addTree(tree);
		        treeWalk.setRecursive(true);
		        
		       //tempInfo.add(new String (revCommit.getAuthorIdent().getName()));
		        
		        while (treeWalk.next()) {
		        	if(javaFileCheck(treeWalk.getNameString())){
		        		tempInfo.add(new String(treeWalk.getNameString()));
		        		System.out.println(treeWalk.getNameString());
		        	}
		        }
		        //treeWalk.reset();
		        if(tempInfo.size()>1){
		        	authorNames.add(revCommit.getAuthorIdent().getName());
		        	repoFiles.add(new ArrayList<String>(tempInfo));
		        }
		        tempInfo.clear();
		        System.out.println(revCommit.getAuthorIdent().getName());
		        //System.out.println(revCommit.getFullMessage());
		        
		        
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
		Collections.reverse(repoFiles);
		Collections.reverse(authorNames);
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
