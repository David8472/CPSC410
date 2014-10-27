package analyzer.dependencyanalyzer;

import java.util.Vector;

/**
 * Created on October 26, 2014 by Ellina.
 * Mock XML Parser that reads the Classycle's output XML report.
 */

public class MockXmlParser{

	private Vector<ClassDependencyInfo> classVector = new Vector<ClassDependencyInfo>();	// vector of mockClass objects
	private Vector<PackageDependencyInfo> packageVector = new Vector<PackageDependencyInfo>(); // vector of mockPackage objects

	// Steps:
	// Create the DOM from a given XML file.
	// Parse the DOM and fill in dependency objects.
	// Stick objects together in a vector
	//		that can be later passed to the Data Aggregator.

	/**
	 * Status: Mock.
	 * Analyzes the xml file to gather info on class dependencies.
	 * Adds info on each class to the classVector.
	 */
	public void analyzeXmlClassInfo(){

		ClassDependencyInfo c1 = new ClassDependencyInfo(2,1);
		c1.addUsedBy("water.lake");
		c1.addUsedBy("water.river");
		c1.addInternalUse("air.wind");
		classVector.add(c1);

		ClassDependencyInfo c2 = new ClassDependencyInfo(1,3);
		c2.addUsedBy("soil.grass");
		c2.addInternalUse("air.wind");
		c2.addInternalUse("water.lake");
		c2.addInternalUse("water.rain");
		classVector.add(c2);

		ClassDependencyInfo c3 = new ClassDependencyInfo(0,2);
		c3.addInternalUse("soil.grass");
		c3.addInternalUse("air.wind");
		classVector.add(c3);
	}

	/**
	 * Status: Mock.
	 * Analyzes the xml file to gather info on package dependencies.
	 * Adds info on each package to the packageVector.
	 */
	public void analyzeXmlPackageInfo(){

		PackageDependencyInfo p1 = new PackageDependencyInfo(0,1);
		p1.addInternalUse("water");
		packageVector.add(p1);

		PackageDependencyInfo p2 = new PackageDependencyInfo(1,2);
		p2.addUsedBy("soil");
		p2.addInternalUse("water");
		p2.addInternalUse("air");
		packageVector.add(p2);
	}

	/**
	 * Returns a vector of ClassDependencyInfo objects.
	 * Each such object contains info on the given class dependencies.
	 */
	public Vector<ClassDependencyInfo> getClassSummary(){
		return classVector;
	}

	/**
	 * Returns a vector of PackageDependencyInfo objects.
	 * Each such object contains info on the given package dependencies.
	 */
	public Vector<PackageDependencyInfo> getPackageSummary(){
		return packageVector;
	}

	/**
	 * Prints out the summary of all class dependencies.
	 */
	public void printClassSummary(){
		for(int i = 0; i < classVector.size(); i++){
			System.out.println("*** Class # " + i);
			System.out.println("This class is used by " + classVector.get(i).getNumUsedBy() + " classes.");
			System.out.println("This class uses " + classVector.get(i).getNumInternalDependencies() + " internal classes");

			for(int j = 0; j < classVector.get(i).getUsedByVectorLength(); j++){
				System.out.println("Used by " + classVector.get(i).getUsedByVectorElem(j));
			}
			for(int k = 0; k < classVector.get(i).getInternalVectorLength(); k++){
				System.out.println("Uses " + classVector.get(i).getInternalVectorElem(k));
			}
		}
		System.out.println("DONE");
	}

	/**
	 * Prints out the summary of all package dependencies.
	 */
	public void printPackageSummary(){
		for(int i = 0; i < packageVector.size(); i++){
			System.out.println("*** Package # " + i);
			System.out.println("This package is used by " + packageVector.get(i).getNumUsedBy() + " packages.");
			System.out.println("This package uses " + packageVector.get(i).getNumInternalDependencies() + " internal packages.");

			for(int j = 0; j < packageVector.get(i).getUsedByVectorLength(); j++){
				System.out.println("Used by " + packageVector.get(i).getUsedByVectorElem(j));
			}
			for(int k = 0; k < packageVector.get(i).getInternalVectorLength(); k++){
				System.out.println("Uses " + packageVector.get(i).getInternalVectorElem(k));
			}
		}
		System.out.println("DONE");
	}

}
