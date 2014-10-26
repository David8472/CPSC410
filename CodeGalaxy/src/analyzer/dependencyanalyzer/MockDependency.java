package analyzer.dependencyanalyzer;

/*
 * Created on Oct 26, 2014 by Ellina.
 * Mock class for Dependency Analyzer.
 */


public class MockDependency extends GenericDependency{
	
	private int numUsedBy; // number of classes/packages this one is used by
	private int numInternalDependencies; // number of classes/packages that this one uses
	private int numExternalDependencies; // may be not so useful
	
	public void setNumUsedBy(int num){
		numUsedBy = num;
	}
	
	public void setNumExternalDependencies(int num){
		numExternalDependencies = num;
	}
	
	public void setNumInternalDependencies(int num){
		numInternalDependencies = num;
	}
	
	public int getNumUsedBy(){
		return numUsedBy;
	}
	
	public int getNumInternalDependencies(){
		return numInternalDependencies;
	}
	
	public int getNumExternalDependencies(){
		return numExternalDependencies;
	}
	
}