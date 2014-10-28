package analyzer.dependencyanalyzer;

import java.util.Vector;

/*
 * Created on Oct 26, 2014 by Ellina.
 * A class to hold the class dependency information.
 */

public class PackageDependencyInfo {
	
	private int numUsedBy; // number of packages this object is used by
	private int numInternalDependencies; // number of packages that this object uses
	private Vector<String> usedByVector = new Vector<String>();
	private Vector<String> usesInternalVector = new Vector<String>();
	
	/**
	 * Constructs a PackageDependencyInfo given the number of used-by and uses packages.
	 */
	public PackageDependencyInfo(int numUsedby, int numInt){
		this.numUsedBy = numUsedby;
		this.numInternalDependencies = numInt;
	}
	
	/**
	 * Sets the number of packages that use this given package.
	 */
	public void setNumUsedBy(int num){
		numUsedBy = num;
	}
	
	/**
	 * Returns the number of packages that use this given package.
	 */
	public int getNumUsedBy(){
		return numUsedBy;
	}
	
	/**
	 * Sets the number of internal packages that the given package is using.
	 */
	public void setNumInternalDependencies(int num){
		numInternalDependencies = num;
	}
	
	/**
	 * Returns the number of internal packages that the given package is using.
	 */
	public int getNumInternalDependencies(){
		return numInternalDependencies;
	}
	
	/**
	 * Adds a package to the vector of internal packages that this given package is using.
	 */
	public void addInternalUse(String s){
		usesInternalVector.add(s);
	}
	
	/** 
	 * Returns the length of the vector containing all internal package dependencies.
	 * @return Length of the internal packages vector.
	 */
	public int getInternalVectorLength(){
		return usesInternalVector.size();
	}
	
	/**
	 * Returns a specified package from the vector of internally used packages.
	 * @return The package at index i.
	 */
	public String getInternalVectorElem(int i){
		return usesInternalVector.get(i);
	}
	
	/**
	 * Adds a package that uses this given package to the vector.
	 */
	public void addUsedBy(String s){
		usedByVector.add(s);
	}
	
	/**
	 * Returns the size of vector containing all packages that use the given package.
	 * @return Length of the referring packages vector.
	 */
	public int getUsedByVectorLength(){
		return usedByVector.size();
	}
	
	/**
	 * Returns a specified package from the vector of packages that refer to the given package.
	 * @return The package at index i.
	 */
	public String getUsedByVectorElem(int i){
		return usedByVector.get(i);
	}
	
}