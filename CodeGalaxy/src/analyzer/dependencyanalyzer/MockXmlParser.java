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
		c1.setClassName("Earth");
		c1.addAfferentClass("water.lake");
		c1.addAfferentClass("water.river");
		c1.addEfferentClass("air.wind");
		classVector.add(c1);

		ClassDependencyInfo c2 = new ClassDependencyInfo(1,3);
		c2.setClassName("Jupiter");
		c2.addAfferentClass("soil.grass");
		c2.addEfferentClass("air.wind");
		c2.addEfferentClass("water.lake");
		c2.addEfferentClass("water.rain");
		classVector.add(c2);

		ClassDependencyInfo c3 = new ClassDependencyInfo(0,2);
		c3.setClassName("Saturn");
		c3.addEfferentClass("soil.grass");
		c3.addEfferentClass("air.wind");
		classVector.add(c3);
	}

	/**
	 * Status: Mock.
	 * Analyzes the xml file to gather info on package dependencies.
	 * Adds info on each package to the packageVector.
	 */
	public void analyzeXmlPackageInfo(){

		PackageDependencyInfo p1 = new PackageDependencyInfo(0,1);
		p1.setPackageName("Sun");
		p1.addEfferentPackage("water");
		packageVector.add(p1);

		PackageDependencyInfo p2 = new PackageDependencyInfo(1,2);
		p2.setPackageName("Altair");
		p2.addAfferentPackage("soil");
		p2.addEfferentPackage("water");
		p2.addEfferentPackage("air");
		packageVector.add(p2);
	}

	/**
	 * Returns a vector of ClassDependencyInfo objects.
	 * Each object contains info on the given class dependencies.
	 */
	public Vector<ClassDependencyInfo> getClassesXmlSummary(){
		return classVector;
	}

	/**
	 * Returns a vector of PackageDependencyInfo objects.
	 * Each object contains info on the given package dependencies.
	 */
	public Vector<PackageDependencyInfo> getPackagesXmlSummary(){
		return packageVector;
	}
}
