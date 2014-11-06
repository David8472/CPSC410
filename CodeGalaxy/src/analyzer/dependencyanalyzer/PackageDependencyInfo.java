package analyzer.dependencyanalyzer;

import java.util.Vector;

/*
 * Created on Oct 26, 2014 by Ellina.
 * A class to hold the class dependency information.
 */

public class PackageDependencyInfo {
	
	private String name;
	private int afferentNum; // number of other packages this object is used by
	private int efferentNum; // number of other packages that this object uses
	private Vector<String> afferentVector = new Vector<String>(); // contains names of afferent packages
	private Vector<String> efferentVector = new Vector<String>(); // contains names of efferent packages
	
	/**
	 * Constructs a PackageDependencyInfo given the number of used-by and uses packages.
	 */
	public PackageDependencyInfo(int usedByNum, int usesNum){
		this.afferentNum = usedByNum;
		this.efferentNum = usesNum;
	}
	
	/**
	 * Sets the name of the package.
	 */
	public void setPackageName(String someName){
		name = someName;
	}
	
	/**
	 * Returns the name of the package.
	 */
	public String getPackageName(){
		return name;
	}
	
	/**
	 * Sets the number of afferent packages (aka packages that use this given package).
	 */
	public void setAfferentNum(int num){
		afferentNum = num;
	}
	
	/**
	 * Returns the number of afferent packages.
	 */
	public int getAfferentNum(){
		return afferentNum;
	}
	
	/**
	 * Sets the number of efferent packages.
	 */
	public void setEfferentNum(int num){
		efferentNum = num;
	}
	
	/**
	 * Returns the number of efferent packages.
	 */
	public int getEfferentNum(){
		return efferentNum;
	}
	
	/**
	 * Records an efferent package.
	 */
	public void addEfferentPackage(String s){
		efferentVector.add(s);
	}
	
	/** 
	 * Returns the number of efferent packages.
	 * @return Size of the efferent package vector.
	 */
	public int getEfferentVectorSize(){
		return efferentVector.size();
	}
	
	/**
	 * Returns a specified package from the vector of efferent packages.
	 * @return The efferent package at index i.
	 */
	public String getEfferentVectorElemAt(int i){
		return efferentVector.get(i);
	}
	
	/**
	 * Records an afferent package.
	 */
	public void addAfferentPackage(String s){
		afferentVector.add(s);
	}
	
	/**
	 * Returns the number of afferent packages.
	 * @return Size of the afferent package vector.
	 */
	public int getAfferentVectorSize(){
		return afferentVector.size();
	}
	
	/**
	 * Returns a specified package from the vector of afferent packages.
	 * @return The afferent package at index i.
	 */
	public String getAfferentVectorElemAt(int i){
		return afferentVector.get(i);
	}
	
	/**
	 * Returns a vector with afferent packages.
	 */
	public Vector<String> getAfferentPackageDependencies(){
		
		Vector<String> copyVector = new Vector<String>();
		
		for(int i = 0; i < afferentVector.size(); i++){
			String s = afferentVector.get(i);
			copyVector.add(s);
		}
		return copyVector;
	}
	
	/**
	 * Returns a vector with efferent packages.
	 */
	public Vector<String> getEfferentPackageDependencies(){
		
		Vector<String> copyVector = new Vector<String>();
		for(int i = 0; i < efferentVector.size(); i++){
			String s = efferentVector.get(i);
			copyVector.add(s);
		}
		return copyVector;
	}
	
}