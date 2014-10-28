package analyzer.miscstaticanalyzer;

import japa.parser.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Analyzes and gets static code metrics for an entire code base
 */
public class MiscStaticAnalyzer {

    /**
     * Gets all .java files from input parameter path
     *
     * @param codebasePath - The path to the codebase
     * @return
     */
    private static Collection<File> getSourceFiles(String codebasePath) {
        return FileUtils.listFiles(FileUtils.getFile(codebasePath), FileFilterUtils.suffixFileFilter(".java"),
                TrueFileFilter.TRUE);
    }

    /**
     * Gets static code analysis metrics for a code base
     *
     * @param codebasePath - The path to the codebase
     * @return
     */
    public static MiscCodeBaseStats getMiscStaticMetrics(String codebasePath) {

        // Get all Java source files from codebase
        Collection<File> sourceFiles = getSourceFiles(codebasePath);

        // Bundled object that holds all the source file statistics
        MiscCodeBaseStats statistics = new MiscCodeBaseStats();

        // Iterate through all source files
        for (File sourceFile : sourceFiles) {
            try {
                ClassAnalyzer analyzer = new ClassAnalyzer(sourceFile);

                // Get package name for a source file and add to MiscCodeBaseStats
                String packageName = analyzer.getPackage();
                statistics.addPackage(packageName);

                // Get class(es) information from the class analyzer
                ArrayList<ClassInfo> classInfos = analyzer.getClassInfo();

                // Iterate through all classes (they may be nested classes) for a source file
                for (ClassInfo classInfo : classInfos) {
                    // Add class information to MiscCodeBaseStats
                    statistics.addClass(classInfo);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Return bundled object with all static code analysis metrics for the code base
        return statistics;
    }

}