package analyzer.dependencyanalyzer;

import java.io.IOException;

/**
 * Dependency reporter based on the Classycle tool.
 * Reference: http://classycle.sourceforge.net/measures.html
 * Created on October 25, 2014.
 */
public class DependencyChecker {

	public static void main(String args[]){

		Runtime rt = Runtime.getRuntime();
		Process proc;
		try {
			// -------------- Instructions for Classycle -------------------------------------//
			// Execute: java -jar <location>\classycle.jar -xmlFile=<filename>.xml <directory>
			// <location> is the location on your machine where classycle.jar is located.
			// <filename> is the name of the report file that will be created by the tool.
			//				This file will be stored in the project directory.
			// <directory> is the address of the directory containing class files to be analysed.
			// --------------------------------------------------------------------------------//
			proc = rt.exec("java -jar C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\classycle.jar -xmlFile=yik.xml C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\samplepayment");
			int exitVal = proc.waitFor();
			System.out.println("Process exitValue: " + exitVal);
			
			// Parse the XML output file
			MockXmlParser parser = new MockXmlParser();
			parser.analyzeXmlClassInfo();
			parser.analyzeXmlPackageInfo();
			parser.printClassSummary();
			parser.printPackageSummary();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IllegalThreadStateException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
