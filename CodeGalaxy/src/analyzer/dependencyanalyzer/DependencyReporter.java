package analyzer.dependencyanalyzer;

import java.io.IOException;

/*
 * Dependency reporter based on Classycle
 * Date: October 25, 2014
 */
public class DependencyReporter {

	public static void main(String args[]){

		Runtime rt = Runtime.getRuntime();
		Process proc;
		try {	
			proc = rt.exec("java -jar C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\classycle.jar -xmlFile=pay.xml C:\\Users\\Ellina\\e410_sprint1\\CodeGalaxy\\classycle\\samplepayment");
			int exitVal = proc.waitFor();
			System.out.println("Process exitValue: " + exitVal);
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
