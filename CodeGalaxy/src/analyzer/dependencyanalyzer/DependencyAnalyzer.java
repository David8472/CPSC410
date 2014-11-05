package analyzer.dependencyanalyzer;

import java.io.IOException;
import java.util.Vector;

/**
 * Dependency reporter based on the Classycle tool.
 * Reference: http://classycle.sourceforge.net/measures.html
 * Created: October 25, 2014.
 */
public class DependencyAnalyzer {

	private static String command;
	private static int exitValue;
	private static MockXmlParser parser;
	private static Vector<ClassDependencyInfo> classesDepInfo = new Vector<ClassDependencyInfo>();
	private static Vector<PackageDependencyInfo> packagesDepInfo = new Vector<PackageDependencyInfo>();
	private static boolean XmlParserInProgress = true;

	/**
	 * Constructs a DependencyAnalyzer to perform analysis on the given command;
	 */
	public DependencyAnalyzer(String stringCommand){
		command = stringCommand;
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

				// Gatekeeper
				if(XmlParserInProgress){
					parser = new MockXmlParser();
					parser = new MockXmlParser();
					parser.analyzeXmlClassInfo();
					parser.analyzeXmlPackageInfo();
					classesDepInfo = parser.getClassesXmlSummary();
					packagesDepInfo = parser.getPackagesXmlSummary();
					printClassSummary();
					printPackageSummary();
				}
				else{
					XmlParser realParser = new XmlParser();
					System.out.println("Using a real XML Parser...");
				}
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
	 * Returns a vector with dependencies info of all classes.
	 */
	public Vector<ClassDependencyInfo> getAllClassesDependencies(){
		return classesDepInfo;
	}

	/**
	 * Returns a vector with dependencies info of all packages.
	 */
	public Vector<PackageDependencyInfo> getAllPackagesDependencies(){
		return packagesDepInfo;
	}
	
	/**
	 * Prints out the summary of all class dependencies.
	 */
	public static void printClassSummary(){

		if(classesDepInfo.size() == 0){
			System.out.println("No information on class dependencies was found.");
		}
		else{
			for(int i = 0; i < classesDepInfo.size(); i++){
				System.out.println("*** Class # " + i + " named "+ classesDepInfo.get(i).getClassName());
				System.out.println("This class is used by " + classesDepInfo.get(i).getAfferentNum() + " classes.");
				System.out.println("This class uses " + classesDepInfo.get(i).getEfferentNum() + " classes");

				for(int j = 0; j < classesDepInfo.get(i).getAfferentVectorSize(); j++){
					System.out.println("Used by " + classesDepInfo.get(i).getAfferentVectorElemAt(j));
				}
				for(int k = 0; k < classesDepInfo.get(i).getEfferentVectorSize(); k++){
					System.out.println("Uses " + classesDepInfo.get(i).getEfferentVectorElemAt(k));
				}
			}
			System.out.println("DONE");
		}
	}

	/**
	 * Prints out the summary of all package dependencies.
	 */
	public static void printPackageSummary(){

		if(packagesDepInfo.size() == 0){
			System.out.println("No information on package dependencies was found.");
		}
		else{
			for(int i = 0; i < packagesDepInfo.size(); i++){
				System.out.println("*** Package # " + i + " named " + packagesDepInfo.get(i).getPackageName());
				System.out.println("This package is used by " + packagesDepInfo.get(i).getAfferentNum() + " packages.");
				System.out.println("This package uses " + packagesDepInfo.get(i).getEfferentNum() + " packages.");

				for(int j = 0; j < packagesDepInfo.get(i).getAfferentVectorSize(); j++){
					System.out.println("Used by " + packagesDepInfo.get(i).getAfferentVectorElemAt(j));
				}
				for(int k = 0; k < packagesDepInfo.get(i).getEfferentVectorSize(); k++){
					System.out.println("Uses " + packagesDepInfo.get(i).getEfferentVectorElemAt(k));
				}
			}
			System.out.println("DONE");
		}
	}
	
}
