package analyzer.dependencyanalyzer;

import java.util.Vector;

/*
 * Created on October 26, 2014 by Ellina.
 * Mock XML Parser to read the XML file by converting it to DOM.
 */

public class MockXmlParser{
	
	private Vector<MockClassDependency> classVector = new Vector<MockClassDependency>();	// vector of mockClass objects
	private Vector<MockPackageDependency> packageVector = new Vector<MockPackageDependency>(); // vector of mockPackage objects

	// Create the DOM from a given XML file.
	// Parse the DOM and fill in dependency objects.
	// Stick objects together in a list or vector
	//		that can be later passed to the Data Aggregator.
	
	public void analyzeXmlClassInfo(){
		// create one object at a time and add it to the Vector
	}
	
	public void analyzeXmlPackageInfo(){
		// create one object at a time and add it to the Vector
	}
	
	public Vector<MockClassDependency> getClassSummary(){
		return classVector;
	}
	
	public Vector<MockPackageDependency> getPackageSummary(){
		return packageVector;
	}
	
}
