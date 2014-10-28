package analyzer.dependencyanalyzer;

import java.io.IOException;
import java.util.Vector;

/**
 * Dependency reporter based on the Classycle tool.
 * Reference: http://classycle.sourceforge.net/measures.html
 * Created on October 25, 2014.
 */
public class DependencyAnalyzer {

	private static String command;
	private static int exitValue;
	private static MockXmlParser parser;
	private static Vector<ClassDependencyInfo> classesDepInfo = new Vector<ClassDependencyInfo>();
	private static Vector<PackageDependencyInfo> packagesDepInfo = new Vector<PackageDependencyInfo>();

	/**
	 * Constructs a DependencyAnalyzer to perform analysis on the given command;
	 */
	public DependencyAnalyzer(String stringCommand){
		command = stringCommand;
	}

	/**
	 * This method is for developing purposes only and will be removed afterwards.
	 * Main() of the Dependency Analyzer tool.
	 * Runs the Classycle tool in the command line and calls XML Parser afterwards.
	 */
	public static void main (String[] args){
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

			proc = rt.exec("java -jar classycle\\classycle.jar -xmlFile=with_no_dot.xml classycle\\samplepayment");
			exitValue = proc.waitFor();
			System.out.println("Process exitValue: " + exitValue);

			// ----- Parse the XML output file -----//
			parser = new MockXmlParser();
			parser.analyzeXmlClassInfo();
			parser.analyzeXmlPackageInfo();
			classesDepInfo = parser.getClassSummary();
			packagesDepInfo = parser.getPackageSummary();	
			//parser.printClassSummary();
			//parser.printPackageSummary();

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


	/**
	 * Entry point of the Dependency Analyzer tool.
	 * Runs the Classycle tool in the command line and calls XML Parser afterwards.
	 */
	public void runClassycle(){
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
			if(command!= null){
				proc = rt.exec(command);
				exitValue = proc.waitFor();
				System.out.println("Process exitValue: " + exitValue);

				// Parse the XML output file
				parser = new MockXmlParser();
				parser.analyzeXmlClassInfo();
				parser.analyzeXmlPackageInfo();
				classesDepInfo = parser.getClassSummary();
				packagesDepInfo = parser.getPackageSummary();	
				//parser.printClassSummary();
				//parser.printPackageSummary();
			}
			else{
				exitValue = -1;
			}
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

	/**
	 * Returns the status of the command execution.
	 * @Return Command execution status.
	 */
	public int getExitStatus(){
		return exitValue;
	}

	/**
	 * Returns dependencies info of all classes.
	 */
	public Vector<ClassDependencyInfo> getAllClassesDependencies(){
		return classesDepInfo;
	}

	/**
	 * Returns dependencies info of all packages.
	 */
	public Vector<PackageDependencyInfo> getAllPackagesDependencies(){
		return packagesDepInfo;
	}
}
