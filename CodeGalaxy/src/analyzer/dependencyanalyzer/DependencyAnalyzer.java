package analyzer.dependencyanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	private static MockXmlParser parser;
	private static Vector<ClassDependencyInfo> classesDepInfo = new Vector<ClassDependencyInfo>();
	private static Vector<PackageDependencyInfo> packagesDepInfo = new Vector<PackageDependencyInfo>();
	private static boolean XmlParserInProgress = false;
	private static String compilerCommand;
	private static Vector<String> fileAddresses;
	private static String sourceCodePath;
	private static int classycleExitValue;
	private static int compilerExitValue;
	private static String mavenCommand;
	private static int mavenExitValue;

	/**
	 * Default constructor.
	 */
	public DependencyAnalyzer() {
		classycleExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
		compilerExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
		compilerCommand = null; //initialize to null string (to avoid accidental compilation based on previous value)
		fileAddresses = new Vector<String>(); //reset the vector (to avoid accessing wrong files based on previous values)
		mavenCommand = null; //initialize to null string (to avoid accidental use of old value)
		mavenExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
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
		mavenCommand = null; //initialize to null string (to avoid accidental use of old value)
		mavenExitValue = -1; //initialize to something other than 0 (0 is reserved for successful exit)
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
				System.out.println("Compiler process exit value: " + compilerExitValue);

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
						}
						else{
							System.out.println("Using a real XML Parser...");
							XmlParser realParser = new XmlParser(this);
							realParser.startXmlParser("samplereport.xml", this);
						}
					}
					else{
						System.out.println("Problem: Classycle did not exit correctly. Please, try again.");
					}
				}
				else{
					System.out.println("Problem: Compiler did not exit correctly. Please, try again.");
				}
			}
			else{
				System.out.println("Problem: Compiler string was not built right. Please, try again.");
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
				File myFile = file.toFile();
				if(myFile.isFile()){
					if(myFile.getName().endsWith(".java")){

						// --- 1. Extract String representations ---//
						String fileNameStr = myFile.getName();
						String pathStr = file.getParent().toString();
						
						// --- 2. Parse the strings into pieces (store in a vector) --- //
						singleFileVector = new Vector<String>();
						for (String retval: pathStr.split("/")){
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
		return finalString;
	}


	/**
	 * Entry point for Maven-based projects analysis.
	 * Reference for Maven: http://maven.apache.org/guides/getting-started/index.html
	 * Reference for Classycle: http://classycle.sourceforge.net/index.html
	 * @param codebaseAddress A codebase directory address, where a pom.xml file is located.
	 */
	public void runDependencyAnalyzer(String codebaseAddress){

		if(codebaseAddress != "" && codebaseAddress != null){
			mavenCommand = buildMavenCommand(codebaseAddress);
			System.out.println("Maven Command: " + mavenCommand);
		}
		else{
			System.err.println("Codebase path cannot be null or empty. Please, enter a correct path.");
			return;
		}

		try{
			if(mavenCommand != null){

				// ------- Run Maven ----------/
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(mavenCommand);
				// Handle error messages via a gobbler
				StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");            
				// Handle any output via a gobbler
				StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
				// Start gobblers  
				errorGobbler.start();
				outputGobbler.start();
				// Check exit value
				mavenExitValue = proc.waitFor(); 
				System.out.println("Maven Exit Value: " + mavenExitValue);

				if(mavenExitValue == 0){ // process exited successfully

					// ------- Run the Classycle tool ----------//
					String classycleCommand = buildClassycleCommand(codebaseAddress);
					System.out.println("Classycle Command: " + classycleCommand);

					proc = rt.exec(classycleCommand);
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
						}
						else{
							System.out.println("Using a real XML Parser...");
							XmlParser realParser = new XmlParser(this);
							realParser.startXmlParser("samplereport.xml", this);
						}
					}
					else{
						System.out.println("Problem: Classycle did not exit correctly. Please try again.");
					}
				}
				else{
					System.out.println("Problem: Maven did not exit correctly. Please try again.");
				}
			}
			else{
				System.out.println("Problem: Maven string was not built right. Please try again.");
			}
		} catch (XmlParserException e) {
			System.err.println("XML Parser did not run successfully. Please, try again.");
			System.err.println(e);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch(IllegalThreadStateException e){
			e.printStackTrace();
		}
	}

	/**
	 * Builds a Maven command string given the base address of the codebase.
	 * Formats the command like "cmd /C mvn -f {address}/pom.xml compile"
	 * @param address A relative address of the codebase.
	 * @return A compile command for Maven.
	 */
	private static String buildMavenCommand(String address){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("cmd /C mvn -f ");
		stringBuilder.append(address);
		stringBuilder.append("/pom.xml compile");
		String finalString = stringBuilder.toString();
		return finalString;
	}

	/**
	 * Builds a command for Classycle.
	 * Formats the command like "java -jar classycle\\classycle.jar -xmlFile=samplereport.xml {path}/target/classes"
	 * @param address A relative address of the compiled class files.
	 * @return A command for the Classycle.
	 */
	private static String buildClassycleCommand(String path){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("java -jar classycle\\classycle.jar -xmlFile=samplereport.xml ");
		stringBuilder.append(path);
		stringBuilder.append("/target/classes");
		String finalString = stringBuilder.toString();
		return finalString;
	}
	
	/**
	 * Returns the status of Maven execution.
	 * @return Maven exit status.
	 */
	public int getMavenExitStatus(){
		return mavenExitValue;
	}


}

/**
 * Helper class to handle output and errors that occur while running Runtime.exec()
 * Reference: http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html
 */
class StreamGobbler extends Thread {
	InputStream is;
	String type;

	StreamGobbler(InputStream is, String type){
		this.is = is;
		this.type = type;
	}

	public void run(){
		try{
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line=null;
			while ( (line = br.readLine()) != null)
				System.out.println(type + ">" + line);    
		} catch (IOException ioe){
			System.err.println("Stream Gobbler exception.");
			System.err.println(ioe);  
		}
	}
}

