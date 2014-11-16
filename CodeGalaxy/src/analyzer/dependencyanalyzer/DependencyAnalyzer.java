package analyzer.dependencyanalyzer;

import java.io.File;
import java.io.IOException;
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

	private static int exitValue;
	private static MockXmlParser parser;
	private static Vector<ClassDependencyInfo> classesDepInfo = new Vector<ClassDependencyInfo>();
	private static Vector<PackageDependencyInfo> packagesDepInfo = new Vector<PackageDependencyInfo>();
	private static boolean XmlParserInProgress = false;
	private static boolean XmlTestParserInProgress = true;
	private static String compilerCommand;
	private static Vector<String> fileAddresses;
	private static String sourceCodePath;
	private static int classycleExitValue;
	private static int compilerExitValue;
	
	private String daMessage = "Hola you so Helper"; // helper string for development
	
	/**
	 * Default constructor.
	 */
	public DependencyAnalyzer() {
		classycleExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
		compilerExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
		compilerCommand = null; //initialize to null string (to avoid accidental compilation based on previous value)
		fileAddresses = new Vector<String>(); //reset the vector (to avoid accessing wrong files based on previous values)
	}

	/**
	 * Constructs a DependencyAnalyzer to perform analysis on the given path.
	 * @param pathStr A string that contains the path to the source code
	 * relative to the current directory.
	 */
	public DependencyAnalyzer(String pathStr){
		sourceCodePath = pathStr;
		classycleExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
		compilerExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
		compilerCommand = null; //initialize to null string (to avoid accidental compilation based on previous value)
		fileAddresses = new Vector<String>(); //reset the vector (to avoid accessing wrong files based on previous values)
	}
	

	/**
	 * Entry point of the Dependency Analyzer tool.
	 * Runs the Java compiler and the Classycle tool in the command line,
	 *  and calls XML Parser afterwards.
	 */
	public void runClassycle(){

		if(sourceCodePath != "" && sourceCodePath != null){
			Path dir = Paths.get(sourceCodePath);
			try {
				listJavaFiles(dir);
			} catch (IOException e) { // Note: NoSuchFileException is caught by IOException.
				System.err.println("Invalid path. Please, provide a valid path for the compiler input.");
				System.out.println(e);
				return;
			}
		}
		else{
			System.err.println("Path cannot be null or empty. Please, enter a correct path.");
			return;
		}

		// Build a string that will be passed to the process running the compiler.
		compilerCommand = buildCommandString(fileAddresses);
		System.out.println("Compiler Command: " + compilerCommand);

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

			// -------------- Javac compiler options --------------------------------------------------//
			//-d <directory> sets the destination directory for compiled class files.
			// Note that this directory has to be created before calling the compiler, since
			// compiler does not create it.
			// --------------------------------------------------------------------------------//

			if(compilerCommand != null){

				// ------- Run the Javac compiler ----------//
				proc = rt.exec(compilerCommand);
				compilerExitValue = proc.waitFor();
				System.out.println("Process exit value: " + compilerExitValue);

				if(compilerExitValue == 0){ // process exited successfully

					// ------- Run the Classycle tool ----------//
					proc = rt.exec("java -jar classycle\\classycle.jar -xmlFile=samplereport.xml samplebytecode");
					classycleExitValue = proc.waitFor();
					System.out.println("Classycle Process exit value: " + classycleExitValue);

					if(classycleExitValue == 0){ // process exited successfully
						// Gatekeeper
						if(XmlParserInProgress){
							System.out.println("Using a mock XML Parser...");
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
							System.out.println("Using a real XML Parser...");
							XmlParser realParser = new XmlParser(this);
							realParser.startXmlParser("samplereport.xml", this);
							
							System.out.println(" ");
							System.out.println("***** ORIGINAL DA *****");
							printClassSummary();
							System.out.println(" ");
							printPackageSummary();
							System.out.println("***** END ORIGINAL DA *****");
							System.out.println(" ");
						}
					}
					else{
						System.out.println("Problem: Classycle did not exit correctly. Please try again.");
					}
				}
				else{
					System.out.println("Problem: Compiler did not exit correctly. Please try again.");
				}
			}
			else{
				System.out.println("Problem: Compiler string was not built right. Please try again.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(IllegalThreadStateException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (XmlParserException e) {
			System.err.println("XML Parser did not run successfully. Please, try again.");
			System.err.println(e);
		}
	}

	/**
	 * For testing.
	 * Runs the Classycle tool in the command line and calls XML Parser afterwards.
	 * @param command A string that contains the command for Classycle.
	 */
	public void runClassycleWithCommand(String command){
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
				if(XmlTestParserInProgress){
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
					System.out.println("Using a real XML Parser...");
				}
			}
			else{
				exitValue = -1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(IllegalThreadStateException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
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
	 * Returns the status of the Classycle execution.
	 * @Return Classycle exit status.
	 */
	public int getClassycleExitStatus(){
		return classycleExitValue;
	}

	/**
	 * Returns the status of compiler execution.
	 * @return Compiler exit status.
	 */
	public int getCompilerExitStatus(){
		return compilerExitValue;
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
	 * Returns a vector with dependencies info of all classes.
	 */
	public void setAllClassesDependencies(Vector<ClassDependencyInfo> v){
		classesDepInfo = v;
	}

	/**
	 * Returns a vector with dependencies info of all packages.
	 */
	public void setAllPackagesDependencies(Vector<PackageDependencyInfo> v){
		packagesDepInfo = v;
	}

	/**
	 * Prints out the summary of all class dependencies.
	 */
	public void printClassSummary(){

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
	public void printPackageSummary(){

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
	private static void listJavaFiles(Path dir) throws IOException{

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
						for (String retval: pathStr.split("/")){
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
				}
				else{
					System.out.println("Directory: " + myFile.getName());
					listJavaFiles(myFile.toPath());
				}
			}		
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
				stringBuilder.append("/");
			}
		}
		String finalString = stringBuilder.toString();
		//System.out.println("We've built: " + finalString);
		return finalString;
	}

	/**
	 * Builds a formatted string representation of a compiler command.
	 * @param addresses A vector that contains addresses of all Java files.
	 */
	private static String buildCommandString(Vector<String> addresses){

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("javac -d samplebytecode ");

		for(int i = 0; i < addresses.size(); i++){
			if(i == addresses.size()-1){
				stringBuilder.append(addresses.elementAt(i));
			}
			else{
				stringBuilder.append(addresses.elementAt(i));
				stringBuilder.append(" ");
			}
		}

		String finalString = stringBuilder.toString();
		//System.out.println("We've built: " + finalString);
		return finalString;
	}
	
	/**
	 * Helper method for development.
	 */
	public String getDAMessage(){
		return daMessage;
	}

}
