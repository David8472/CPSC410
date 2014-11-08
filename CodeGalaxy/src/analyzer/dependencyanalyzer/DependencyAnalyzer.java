package analyzer.dependencyanalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private static String compilerCommand;
	private static Vector<String> fileAddresses = new Vector<String>();

	/**
	 * Default constructor.
	 */
	public DependencyAnalyzer() {
	}
	
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
		
		Path dir = Paths.get("samplesource");
		listJavaFiles(dir);
		
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

			proc = rt.exec("java -jar classycle\\classycle.jar -xmlFile=tryinghard.xml classycle\\samplepayment");
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
	 * For testing.
	 * Runs the Classycle tool in the command line and calls XML Parser afterwards.
	 * Requires a command to be set in the command field before calling this method.
	 */
	public void runClassycleWithCommand(){
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
	
	/**
	 * Finds and lists Java files in a given directory.
	 * References: http://stackoverflow.com/questions/1844688/read-all-files-in-a-folder
	 * http://docs.oracle.com/javase/tutorial/essential/io/dirs.html#listdir
	 * @param dir A directory where Java files are located.
	 */
	private static void listJavaFiles(Path dir){

		Vector<String> singleFileVector;

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path file: stream) {
				//System.out.println("Path object: " + file.getFileName());
				//System.out.println("Path object parent: " + file.getParent());

				File myFile = file.toFile();
				//System.out.println("Abs path: " + myFile.getAbsolutePath());

				if(myFile.isFile()){
					if(myFile.getName().endsWith(".java")){

						//System.out.println(" " + myFile.getName());
						//System.out.println("Path object parent: " + file.getParent());

						// --- 1. Extract String representations ---//
						String fileNameStr = myFile.getName();
						String pathStr = file.getParent().toString();
						System.out.println(" File: " + fileNameStr + " at path: " + pathStr);

						// --- 2. Parse the strings into pieces (store in a vector) --- //

						singleFileVector = new Vector<String>();
						for (String retval: pathStr.split("\\\\")){
							//System.out.println(" " + retval);
							singleFileVector.add(retval);
						}
						singleFileVector.add(fileNameStr);

						// --- 3. Use these pieces to build the command string --- //
						String newAddress = buildFileAddressString(singleFileVector);

						// --- 4. Record this new address in a cumulative vector of all files' addresses. ---//
						if(newAddress != null){
							fileAddresses.add(newAddress);
						}
					}
					//System.out.println("Ends with java: " + myFile.getName().endsWith(".java"));
				}
				else{
					System.out.println("Directory: " + myFile.getName());
					listJavaFiles(myFile.toPath());
				}
			}		
		} catch (IOException | DirectoryIteratorException x) {
			// IOException can be thrown by newDirectoryStream.
			System.err.println(x);
		}
	}
	

	/**
	 * Builds a formatted string representation of a file address.
	 * @param pathVector A vector that contains pieces of an address of a single file.
	 */
	private static String buildFileAddressString(Vector<String> pathVector){

		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < pathVector.size(); i++){
			if(i == pathVector.size()-1){
				stringBuilder.append(pathVector.elementAt(i));
			}
			else{
				stringBuilder.append(pathVector.elementAt(i));
				stringBuilder.append("\\\\");
			}
		}
		String finalString = stringBuilder.toString();
		//System.out.println("We've built: " + finalString);
		return finalString;
	}


}
