package analyzer;

import analyzer.dependencyanalyzer.ClassDependencyInfo;
import analyzer.dependencyanalyzer.PackageDependencyInfo;
import analyzer.gitcommitcomponent.CommitAnalyzerInfo;
import analyzer.miscstaticanalyzer.ClassInfo;
import analyzer.miscstaticanalyzer.MethodInfo;
import analyzer.miscstaticanalyzer.PackageInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DataAggregator {

    private PrintWriter writer;
    private Map<String, String> classToPkgMap;
    private String outputFilePath;

    private final static int COMMIT_METADATA_NEST_LEVEL = 1;
    private final static int PKG_NAME_NEST_LEVEL = 3;
    private final static int PKG_INFO_NEST_LEVEL = 4;
    private final static int CLASS_NAME_NEST_LEVEL = 5;
    private final static int CLASS_INFO_NEST_LEVEL = 6;
    private final static int METHOD_NAME_NEST_LEVEL = 7;
    private final static int METHOD_INFO_NEST_LEVEL = 8;
    private final static int CLASS_DEPENDENCY_INFO_NEST_LEVEL = 7;
    private final static int CLASS_DEPENDENCY_NAME_NEST_LEVEL = 8;


    public DataAggregator(String outputYAMLFilePath) {

        if(!outputYAMLFilePath.contains(".yml")) {
            outputFilePath = outputYAMLFilePath + ".yml";
        } else {
            outputFilePath = outputYAMLFilePath;
        }

        /* Check to see if output YAML file already exists. If so, then delete it. */
        File dataFile = new File(outputFilePath);
        if(dataFile.exists()) {
            dataFile.delete();
        }
    }


    /**
     * Writes commit(s) data to a YAML file
     */
    public void writeCommitDataToYAMLFile(CommitAnalyzerInfo commitMetaData, ArrayList<PackageInfo> packagesInfo,
                                          Vector<PackageDependencyInfo> packageDependencies,
                                          Vector<ClassDependencyInfo> classDependencies) {
        try {

            // Build class to package mapping
            classToPkgMap = buildClassToPackageMapping(packagesInfo);

            // Create new PrintWriter to write to output YAML file
            writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)));

            // Writes commit metadata - commit ID, author, files changed / added / removed
            writeCommitMetaData(commitMetaData);

            writePresentFilesKey();
            writePackagesKey();

            /* Iterate through all packages in commit */
            for(PackageInfo pkg : packagesInfo) {

                String packageName = pkg.getPackageName();

                // Write package name and LOC for a package to output file
                writePackageInfo(packageName, pkg.getLinesOfCode());

                // Write package dependencies key to output file
                writePackageDependenciesKey();

                /*
                Iterate through list of package dependencies for all packages until the dependencies
                for the current package are found
                */
                for(PackageDependencyInfo pkgDependencyInfo : packageDependencies) {

                    /* When found, write the dependencies to output file */
                    if(packageName.equalsIgnoreCase(pkgDependencyInfo.getPackageName())) {
                        writePackageDependencies(pkgDependencyInfo);
                        break;
                    }
                }

                // Write classes key to output file
                writeClassesKey();

                /* Iterate through all classes for a package and write out info to output file */
                for(ClassInfo classInfo : pkg.getListOfClasses()) {

                    String className = classInfo.getClassName();

                    // Write the class info to output file
                    writeClassInfo(classInfo);

                    // Write key for class dependencies to output file
                    writeClassDependenciesKey();

                    /*
                    Iterate through list of class dependencies for all classes until the dependencies
                    for the current class are found
                    */
                    for(ClassDependencyInfo classDependencyInfo : classDependencies) {

                        /* When found, write the dependencies to output file */
                        if(className.equalsIgnoreCase(classDependencyInfo.getClassName())) {
                            writeClassDependencies(classDependencyInfo);
                            break;
                        }
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }


    /**
     * Writes commit metadata to output YAML file
     * @param commitMetaData - CommitAnalyzerInfo object that holds commitID, author, list of files
     *                       changed / removed / added for a single commit
     */
    private void writeCommitMetaData(CommitAnalyzerInfo commitMetaData) {

        // Writes commit ID
        writer.println("commit_" + commitMetaData.getCommitNumber() + ":");

        // Writes author
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL);
        writer.println("author: " + commitMetaData.getAuthorName());

        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL);
        writer.println("modified:");

        /* Writes changed files (each file separated by '|') */
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL + 1);
        writer.println("changed: ");
        // TO DO - files changed not implemented yet in Commit Analyzer
//        ArrayList<String> filesChanged = commitMetaData.getFilesChanged();
//        for(int i = 0; i < filesChanged.size(); i++) {
//            writer.print(filesChanged.get(i));
//            if(i < filesChanged.size() - 1) {
//                writer.print("|");
//            } else {
//                writer.print("\n");
//            }
//        }

        /* Writes added files (each file separated by '|') */
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL + 1);
        writer.print("added: ");
        ArrayList<String> filesAdded = commitMetaData.getFilesAdded();
        for(int i = 0; i < filesAdded.size(); i++) {
            writer.print(filesAdded.get(i));
            if(i < filesAdded.size() - 1) {
                writer.print("|");
            } else {
                writer.print("\n");
            }
        }

        /* Writes removed files (each file separated by '|') */
        writeSpacesCorrespondingToNestedLevel(COMMIT_METADATA_NEST_LEVEL + 1);
        writer.print("removed: ");
        ArrayList<String> filesRemoved = commitMetaData.getFilesDeleted();
        for(int i = 0; i < filesRemoved.size(); i++) {
            writer.print(filesRemoved.get(i));
            if(i < filesRemoved.size() - 1) {
                writer.print("|");
            } else {
                writer.print("\n");
            }
        }
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
        /* Writes package name to output YAML file */
        writeSpacesCorrespondingToNestedLevel(PKG_NAME_NEST_LEVEL);
        writer.println(packageName + ":");

        /* Writes package LOC to output YAML file */
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

        /* Writes class name */
        writeSpacesCorrespondingToNestedLevel(CLASS_NAME_NEST_LEVEL);
        writer.println(classInfo.getClassName() + ":");

        /* Writes lines of code for class */
        writeSpacesCorrespondingToNestedLevel(CLASS_INFO_NEST_LEVEL);
        writer.println("lines: " + classInfo.getLinesOfCode());

        /* Writes type of class - Concrete, Abstract, or Interface */
        writeSpacesCorrespondingToNestedLevel(CLASS_INFO_NEST_LEVEL);
        writer.println("type: " + classInfo.getClassType());

        /* Write information for all methods in class */
        writeSpacesCorrespondingToNestedLevel(CLASS_INFO_NEST_LEVEL);
        writer.println("methods:");
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

        /* Writes method name */
        writeSpacesCorrespondingToNestedLevel(METHOD_NAME_NEST_LEVEL);
        writer.println(methodInfo.getMethodName() + ":");

        /* Writes lines of code for method */
        writeSpacesCorrespondingToNestedLevel(METHOD_INFO_NEST_LEVEL);
        writer.println("lines: " + methodInfo.getLinesOfCode());

        /* Writes accessor type for method - public, protected, private */
        writeSpacesCorrespondingToNestedLevel(METHOD_INFO_NEST_LEVEL);
        writer.println("accessorType: " + methodInfo.getAccessorType());
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
    private void writeClassDependencies(ClassDependencyInfo classDependencyInfo) {

        /* Writes all classes depended on plus their packages */
        Vector<String> classesDependendOn = classDependencyInfo.getEfferentClassDependencies();
        writeSpacesCorrespondingToNestedLevel(CLASS_DEPENDENCY_INFO_NEST_LEVEL);
        writer.println("uses:");
        for(String classDependency : classesDependendOn) {

            /* Writes class dependency name followed by ':' */
            writeSpacesCorrespondingToNestedLevel(CLASS_DEPENDENCY_NAME_NEST_LEVEL);
            writer.println(classDependency + ":");

            /* Writes package the dependency belongs to */
            writeSpacesCorrespondingToNestedLevel(CLASS_DEPENDENCY_NAME_NEST_LEVEL + 1);
            String pkgOfClassDependency = classToPkgMap.get(classDependency);
            writer.println("package: " + pkgOfClassDependency);
        }

        /* Writes all classes that depend on this class plus their packages */
        Vector<String> classesThatDependOnThisClass = classDependencyInfo.getAfferentClassDependencies();
        writeSpacesCorrespondingToNestedLevel(CLASS_DEPENDENCY_INFO_NEST_LEVEL);
        writer.println("usedBy:");
        for(String classDependency : classesThatDependOnThisClass) {

            /* Writes class dependency name followed by ':' */
            writeSpacesCorrespondingToNestedLevel(CLASS_DEPENDENCY_NAME_NEST_LEVEL);
            writer.println(classDependency + ":");

            /* Writes package the dependency belongs to */
            writeSpacesCorrespondingToNestedLevel(CLASS_DEPENDENCY_NAME_NEST_LEVEL + 1);
            String pkgOfClassDependency = classToPkgMap.get(classDependency);
            writer.println("package: " + pkgOfClassDependency);
        }
    }


    /**
     * Writes key name for package dependencies of a package for a commit to output YAML file
     */
    private void writePackageDependenciesKey() {
        writeSpacesCorrespondingToNestedLevel(PKG_INFO_NEST_LEVEL);
        writer.println("packageDependencies:");
    }


    /**
     * Writes key and value pairs for package dependencies to output YAML file.
     * @param packageDependencyInfo
     */
    private void writePackageDependencies(PackageDependencyInfo packageDependencyInfo) {

        /* Writes all packages depended on */
        writeSpacesCorrespondingToNestedLevel(PKG_INFO_NEST_LEVEL + 1);
        writer.print("PackagesDependedOn: ");
        Vector<String> pkgDependencies = packageDependencyInfo.getEfferentPackageDependencies();
        for(int i = 0; i < pkgDependencies.size(); i++) {
            writer.print(pkgDependencies.get(i));
            if(i < pkgDependencies.size() - 1) {
                writer.print("|");
            } else {
                writer.print("\n");
            }
        }

        /* Writes all packages that depend on this package */
        writeSpacesCorrespondingToNestedLevel(PKG_INFO_NEST_LEVEL + 1);
        writer.print("PackagesThatDependOnThisPackage: ");
        Vector<String> pkgsThatDependOnThisPkg = packageDependencyInfo.getEfferentPackageDependencies();
        for(int i = 0; i < pkgsThatDependOnThisPkg.size(); i++) {
            writer.print(pkgsThatDependOnThisPkg.get(i));
            if(i < pkgsThatDependOnThisPkg.size() - 1) {
                writer.print("|");
            } else {
                writer.print("\n");
            }
        }
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


    /**
     * Builds a map where classes are keys and the packages that they belong to are values.
     * @param packages - A list of PackageInfo objects
     * @return - Map with the class to package mappings
     */
    private Map<String, String> buildClassToPackageMapping(ArrayList<PackageInfo> packages) {

        Map<String, String> classToPackageMap = new HashMap<String, String>();

        /* Go through each package */
        for(PackageInfo pkg : packages) {
            /* For a package, iterate through its classes and add class : package key and values to map */
            for(ClassInfo clss : pkg.getListOfClasses()) {
                classToPackageMap.put(clss.getClassName(), clss.getPackageName());
            }
        }

        return classToPackageMap;
    }

}
