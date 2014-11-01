package analyzer;

import analyzer.dependencyanalyzer.ClassDependencyInfo;
import analyzer.dependencyanalyzer.PackageDependencyInfo;
import analyzer.miscstaticanalyzer.ClassInfo;
import analyzer.miscstaticanalyzer.MethodInfo;
import analyzer.miscstaticanalyzer.MiscCodeBaseStats;

import java.io.*;

public class DataAggregator {

    private PrintWriter writer;
    private final static String outputFilePath = "data.yml";

    private final static int COMMIT_METADATA_NEST_LEVEL = 1;
    private final static int PKG_NAME_NEST_LEVEL = 3;
    private final static int PKG_INFO_NEST_LEVEL = 4;
    private final static int CLASS_NAME_NEST_LEVEL = 5;
    private final static int CLASS_INFO_NEST_LEVEL = 6;
    private final static int METHOD_NAME_NEST_LEVEL = 7;
    private final static int METHOD_INFO_NEST_LEVEL = 8;
    private final static int CLASS_DEPENDENCY_NAME_NEST_LEVEL = 7;
    private final static int CLASS_DEPENDENCY_INFO_NEST_LEVEL = 8;


    public DataAggregator() {
        // Check to see if data.yml already exists. If so, then delete it.
        File dataFile = new File(outputFilePath);
        if(dataFile.exists()) {
            dataFile.delete();
        }
    }


    /**
     * Writes commit(s) data to a YAML file
     */
    public void writeDataToYAMLFile(MiscCodeBaseStats miscCodeBaseStats) {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)));
            writeCommitIDKey(0);
            writeCommitMetaData();
            writePresentFilesKey();
            writePackagesKey();

            // TO DO - For all packages
            // writePackageInfo();

            writeClassesKey();

            // TO DO - For all classes
            // writeClassInfo();

            writeClassDependenciesKey();

            // TO DO - For all class dependencies for a class
            // writeClassDependency();

            writePackageDependenciesKey();

            // TO DO - For all package dependencies for a package
            // writePackageDependency();

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }


    /**
     * Writes commit key name for a commit to output YAML file
     * @param commitID - ID of commit
     */
    private void writeCommitIDKey(int commitID) {
        writer.println("commit_" + commitID + ":");
    }


    // TO DO
    private void writeCommitMetaData() {
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL);
        writer.println("author:");
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL);
        writer.println("modified:");
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL + 1);
        writer.println("changed:");
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL + 1);
        writer.println("added:");
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL + 1);
        writer.println("removed:");
    }


    /**
     * Writes key name for present file(s) in commit to output YAML file
     */
    private void writePresentFilesKey() {
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL);
        writer.println("present:");
    }


    /**
     * Writes key name for packages in commit to output YAML file
     */
    private void writePackagesKey() {
        writeSpacesCorrespondingToNestedLevel(PKG_NAME_NEST_LEVEL - 1);
        writer.println("packages:");
    }


    /**
     * Writes key and values for a package, in particular name and LOC,
     * to output YAML file
     * @param packageName - Name of package
     * @param LOC         - Lines of code in package
     */
    private void writePackageInfo(String packageName, int LOC) {
        writeSpacesCorrespondingToNestedLevel(PKG_NAME_NEST_LEVEL);
        writer.println(packageName);
        writeSpacesCorrespondingToNestedLevel(PKG_INFO_NEST_LEVEL);
        writer.println("lines: " + LOC);
    }


    /**
     * Writes key name for classes in a package for a commit to output YAML file
     */
    private void writeClassesKey() {
        writeSpacesCorrespondingToNestedLevel(PKG_INFO_NEST_LEVEL);
        writer.println("classes:");
    }


    /**
     * Writes key and value pairs for a class to output YAML file,
     * including the following properties: name, LOC, type, and methods.
     * @param classInfo - ClassInfo object that holds the properties of the class
     */
    private void writeClassInfo(ClassInfo classInfo) {
        writeSpacesCorrespondingToNestedLevel(CLASS_NAME_NEST_LEVEL);
        writer.println(classInfo.getClassName() + ":");
        writeSpacesCorrespondingToNestedLevel(CLASS_INFO_NEST_LEVEL);
        writer.println("lines: " + classInfo.getLinesOfCode());
        writeSpacesCorrespondingToNestedLevel(CLASS_INFO_NEST_LEVEL);
        writer.println("type: " + classInfo.getClassType());
        writeSpacesCorrespondingToNestedLevel(CLASS_INFO_NEST_LEVEL);
        writer.println("methods:");

        // Write all method information in class
        for(MethodInfo methodInfo : classInfo.getMethods()) {
            writeMethodInfo(methodInfo);
        }
    }


    /**
     * Writes key and value pairs for a method to output YAML file,
     * including the following properties: name, LOC, accessor type
     * @param methodInfo - MethodInfo object that holds properties of the method
     */
    private void writeMethodInfo(MethodInfo methodInfo) {
        writeSpacesCorrespondingToNestedLevel(METHOD_NAME_NEST_LEVEL);
        writer.println(methodInfo.getMethodName());
        writeSpacesCorrespondingToNestedLevel(METHOD_INFO_NEST_LEVEL);
        writer.println("lines: " + methodInfo.getLinesOfCode());

        // TO DO - Add method accessor type
    }


    /**
     * Writes key name for class dependencies of a class for a commit to output YAML file
     */
    private void writeClassDependenciesKey() {
        writeSpacesCorrespondingToNestedLevel(CLASS_INFO_NEST_LEVEL);
        writer.println("dependencies:");
    }


    /**
     * Writes key and value pairs for a class dependency.
     * @param classDependencyInfo
     */
    private void writeClassDependency(ClassDependencyInfo classDependencyInfo) {
        // TO DO
    }


    /**
     * Writes key name for package dependencies of a package for a commit to output YAML file
     */
    private void writePackageDependenciesKey() {
        writeSpacesCorrespondingToNestedLevel(PKG_INFO_NEST_LEVEL);
        writer.println("dependencies:");
    }


    /**
     * Writes key and value pairs for package dependencies to output YAML file.
     * @param packageDependencyInfo
     */
    private void writePackageDependency(PackageDependencyInfo packageDependencyInfo) {
        // TO DO
    }


    /**
     * Writes a number of spaces to the output YAML file equal to the nested level
     * desired multiplied by 4. <br> <br>
     * For example, B has a nested level of 1 in the following YAML file: <br>
     * A: <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;B:
     * @param nestedLevel - Represents the nested level desired in the YAML file.
     */
    private void writeSpacesCorrespondingToNestedLevel(int nestedLevel) {
        for(int i = 0; i < nestedLevel * 4; i++) {
            writer.print(" ");
        }
    }

}
