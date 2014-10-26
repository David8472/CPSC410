package analyzer.dependencyanalyzer;

import java.util.Vector;

/*
 * Created on Oct 26, 2014 by Ellina.
 * Mock class for Dependency Analyzer.
 */


public class MockDependency extends GenericDependency{
	
	private int numUsedBy; // number of classes/packages this object is used by
	private int numInternalDependencies; // number of classes/packages that this object uses
	//private int numExternalDependencies; // may be not so useful
	
	private Vector<String> usedByVector = new Vector<String>();
	private Vector<String> usesInternalVector = new Vector<String>();
	//private Vector<String> usesExternalByVector = new Vector<String>();
	
	public MockDependency(){
	}
	
	public MockDependency(int numUsedby, int numInt){
		this.numUsedBy = numUsedby;
		this.numInternalDependencies = numInt;
		//this.numExternalDependencies = numExt;
	}
	
	public void setNumUsedBy(int num){
		numUsedBy = num;
	}
	
	public int getNumUsedBy(){
		return numUsedBy;
	}
	
	public void setNumInternalDependencies(int num){
		numInternalDependencies = num;
	}
	
	public int getNumInternalDependencies(){
		return numInternalDependencies;
	}
	
	public void addInternalUse(String s){
		usesInternalVector.add(s);
	}
	
	public int getInternalVectorLength(){
		return usesInternalVector.size();
	}
	
	public String getInternalVectorElem(int i){
		return usesInternalVector.get(i);
	}
	
	public void addUsedBy(String s){
		usedByVector.add(s);
	}
	
	public int getUsedByVectorLength(){
		return usedByVector.size();
	}
	
	public String getUsedByVectorElem(int i){
		return usedByVector.get(i);
	}
	
	/*
	public void setNumExternalDependencies(int num){
		numExternalDependencies = num;
	}
	*/
	/*
	public int getNumExternalDependencies(){
		return numExternalDependencies;
	}
	*/
	
}