package analyzer.miscstaticanalyzer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.Collection;

/**
 * Analyzes and gets static code metrics for an entire code base
 */
public class MiscStaticAnalyzer {

    private Collection<File> sourceFiles;

    /**
     * Constructs a MiscStaticAnalyzer to get static code analysis metrics of an entire codebases
     * @param codebasePath - the path to the codebase
     */
    public MiscStaticAnalyzer(String codebasePath) {
        getSourceFiles(codebasePath);
    }

    
    /**
     * Gets all .java files from input parameter path
     * @param codebasePath
     */
    private void getSourceFiles(String codebasePath) {
        sourceFiles = FileUtils.listFiles(FileUtils.getFile(codebasePath), FileFilterUtils.suffixFileFilter(".java"),
                TrueFileFilter.TRUE);
    }

}