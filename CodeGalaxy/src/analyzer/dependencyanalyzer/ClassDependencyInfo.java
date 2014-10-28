package analyzer.dependencyanalyzer;

import java.util.Vector;

/*
 * Created on Oct 26, 2014 by Ellina.
 * A class to hold the class dependency information.
 */

public class ClassDependencyInfo {
	
	private int numUsedBy; // number of classes this object is used by
	private int numInternalDependencies; // number of classes that this object uses
	private Vector<String> usedByVector = new Vector<String>();
	private Vector<String> usesInternalVector = new Vector<String>();
	
	/**
	 * Constructs a ClassDependencyInfo object given the number of used-by and uses classes.
	 */
	public ClassDependencyInfo(int numUsedby, int numInt){
		this.numUsedBy = numUsedby;
		this.numInternalDependencies = numInt;
	}
	
	/**
	 * Sets the number of classes that use this given class.
	 */
	public void setNumUsedBy(int num){
		numUsedBy = num;
	}
	
	/**
	 * Returns the number of classes that use this given class.
	 */
	public int getNumUsedBy(){
		return numUsedBy;
	}
	
	/**
	 * Sets the number of internal classes that the given class is using.
	 */
	public void setNumInternalDependencies(int num){
		numInternalDependencies = num;
	}
	
	/**
	 * Returns the number of internal classes that the given class is using.
	 */
	public int getNumInternalDependencies(){
		return numInternalDependencies;
	}
	
	/**
	 * Adds a class to the vector of internal classes that this given class is using.
	 */
	public void addInternalUse(String s){
		usesInternalVector.add(s);
	}
	
	/** 
	 * Returns the length of the vector containing all internal class dependencies.
	 * @return Length of the internal classes vector.
	 */
	public int getInternalVectorLength(){
		return usesInternalVector.size();
	}
	
	/**
	 * Returns a specified class from the vector of internally used classes.
	 * @return The class at index i.
	 */
	public String getInternalVectorElem(int i){
		return usesInternalVector.get(i);
	}
	
	/**
	 * Adds a class that uses this given class to the vector.
	 */
	public void addUsedBy(String s){
		usedByVector.add(s);
	}
	
	/**
	 * Returns the size of vector containing all classes that use the given class.
	 * @return Length of the referring classes vector.
	 */
	public int getUsedByVectorLength(){
		return usedByVector.size();
	}
	
	/**
	 * Returns a specified class from the vector of classes referring to the given class.
	 * @return The class at index i.
	 */
	public String getUsedByVectorElem(int i){
		return usedByVector.get(i);
	}
	
}