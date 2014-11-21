package analyzer.gitcommitcomponent;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

public class CommitCheckout {
	private File directory;
    private RepositoryBuilder builder = new RepositoryBuilder();
    private Repository repository;
    private Git git;
    
	public CommitCheckout(String repoDirectory){//Constructor to create a new CommitCheckout object
		directory = new File(repoDirectory);
		try {
			repository = builder.setGitDir(directory).readEnvironment().findGitDir().build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		git = new Git(repository);
	}
	
	public void checkout(String commitHashID){//Checkout to a specific commit, depending on the unique ID entered
		
		try {
			git.checkout().setName(commitHashID).call();
		} catch (RefAlreadyExistsException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidRefNameException e) {
			e.printStackTrace();
		} catch (CheckoutConflictException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void resetHeadToMaster(){//Resets the head to master
		try {
			git.checkout().setName("master").call();
		} catch (RefAlreadyExistsException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidRefNameException e) {
			e.printStackTrace();
		} catch (CheckoutConflictException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}
}
