package analyzer.dependencyanalyzer;

import java.util.Vector;

/*
 * Created on Oct 26, 2014 by Ellina.
 * A class to hold the class dependency information.
 */

public class ClassDependencyInfo {
	
	private String name;
	private int afferentNum; // number of classes this object is used by
	private int efferentNum; // number of classes that this object uses
	private Vector<String> afferentVector = new Vector<String>(); // contains names of afferent classes
	private Vector<String> efferentVector = new Vector<String>(); // contains names of efferent classes
	
	/**
	 * Constructs a ClassDependencyInfo object given the number of used-by and uses classes.
	 */
	public ClassDependencyInfo(int usedByNum, int usesNum){
		this.afferentNum = usedByNum;
		this.efferentNum = usesNum;
	}
	
	/**
	 * Sets the name of the class.
	 */
	public void setClassName(String someName){
		name = someName;
	}
	
	/**
	 * Returns the name of the class.
	 */
	public String getClassName(){
		return name;
	}
	
	/**
	 * Sets the number of afferent classes.
	 */
	public void setAfferentNum(int num){
		afferentNum = num;
	}
	
	/**
	 * Returns the number of afferent classes.
	 */
	public int getAfferentNum(){
		return afferentNum;
	}
	
	/**
	 * Sets the number of efferent classes.
	 */
	public void setEfferentNum(int num){
		efferentNum = num;
	}
	
	/**
	 * Returns the number of efferent classes.
	 */
	public int getEfferentNum(){
		return efferentNum;
	}
	
	/**
	 * Records an efferent class.
	 */
	public void addEfferentClass(String s){
		efferentVector.add(s);
	}
	
	/** 
	 * Returns the number of efferent classes.
	 * @return Size of the efferent classes vector.
	 */
	public int getEfferentVectorSize(){
		return efferentVector.size();
	}
	
	/**
	 * Returns a specified class from the vector of efferent classes.
	 * @return The efferent class at index i.
	 */
	public String getEfferentVectorElemAt(int i){
		return efferentVector.get(i);
	}
	
	/**
	 * Records an afferent class.
	 */
	public void addAfferentClass(String s){
		afferentVector.add(s);
	}
	
	/**
	 * Returns the number of afferent classes.
	 * @return Size of the afferent classes vector.
	 */
	public int getAfferentVectorSize(){
		return afferentVector.size();
	}
	
	/**
	 * Returns a specified class from the vector of afferent classes.
	 * @return The class at index i.
	 */
	public String getAfferentVectorElemAt(int i){
		return afferentVector.get(i);
	}
	
}