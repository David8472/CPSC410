package analyzer.dependencyanalyzer;

import java.util.Vector;

/*
 * Created on October 26, 2014 by Ellina.
 * Mock XML Parser to read Classycle output (an XML file).
 */

public class MockXmlParser{
	
	private Vector<ClassDependencyInfo> classVector = new Vector<ClassDependencyInfo>();	// vector of mockClass objects
	private Vector<PackageDependencyInfo> packageVector = new Vector<PackageDependencyInfo>(); // vector of mockPackage objects

	// Steps:
	// Create the DOM from a given XML file.
	// Parse the DOM and fill in dependency objects.
	// Stick objects together in a vector
	//		that can be later passed to the Data Aggregator.
	
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
	
	public Vector<ClassDependencyInfo> getClassSummary(){
		return classVector;
	}
	
	public Vector<PackageDependencyInfo> getPackageSummary(){
		return packageVector;
	}

}
