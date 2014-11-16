package analyzer.miscstaticanalyzer;

import japa.parser.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Analyzes and gets static code metrics for an entire code base
 */
public class MiscStaticAnalyzer {

    /**
     * Gets all .java files from input parameter path
     * @param codebasePath - The path to the codebase
     * @return
     */
    private static Collection<File> getSourceFiles(String codebasePath) {
        return FileUtils.listFiles(FileUtils.getFile(codebasePath), FileFilterUtils.suffixFileFilter(".java"),
                TrueFileFilter.TRUE);
    }


    /**
     * Gets static code analysis metrics for a code base including the following metrics:
     * Packages - names, LOC;
     * Classes - names, container package, LOC, type (Concrete, Abstract, Interface);
     * Methods - names, container class, LOC, return type, accessor type (public, private, protected)
     * @param codebasePath - The path to the codebase
     * @return - List of PackageInfo objects, each object containing the following metrics above for each package
     */
    public static CodebaseStats getMiscStaticMetrics(String codebasePath) {

        CodebaseStats codebaseStats = new CodebaseStats();

        // Get all Java source files from codebase
        Collection<File> sourceFiles = getSourceFiles(codebasePath);

        // List of packages
        ArrayList<PackageInfo> packages = new ArrayList<PackageInfo>();

        // Iterate through all source files
        for(File sourceFile : sourceFiles) {
            try {
                ClassAnalyzer analyzer = new ClassAnalyzer(sourceFile);

                // Get package name for a source file
                String packageName = analyzer.getPackage();

                boolean isPackageInList = false;

                // Checks if package was already added to packages list by iterating through packages list and comparing names
                PackageInfo packageInfo = null;
                for(PackageInfo pkg : packages) {

                    // If package was already added to list, then get reference to respective PackageInfo object
                    if(pkg.getPackageName().equals(packageName)) {
                        packageInfo = pkg;
                        isPackageInList = true;
                        break;
                    }
                }

                // If package is not already in list, then create a new PackageInfo object and add to list
                if(!isPackageInList) {
                    packageInfo = new PackageInfo(packageName);
                    packages.add(packageInfo);
                }

                // Get class(es) information from the class analyzer
                ArrayList<ClassInfo> classInfos = analyzer.getClassInfo();

                HashMap<String, Integer> classToLOCMap = new HashMap<String, Integer>();

                // Iterate through all classes (they may be nested classes) for a source file
                for(ClassInfo classInfo : classInfos) {
                    // Add class information to respective PackageInfo object
                    packageInfo.addClass(classInfo);
                    classToLOCMap.put(classInfo.getPackageName() + "." + classInfo.getClassName(), classInfo.getLinesOfCode());
                }

                codebaseStats.setClassToLOCMap(classToLOCMap);
                codebaseStats.setPackagesInfo(packages);

            } catch(IOException e) {
                e.printStackTrace();
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }

        // Return list of packages with all static code analysis metrics for the code base
        return codebaseStats;
    }


}