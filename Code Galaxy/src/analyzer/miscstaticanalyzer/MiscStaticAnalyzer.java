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

    /**
     * Gets all .java files from input parameter path
     * @param codebasePath - The path to the codebase
     * @return
     */
    private static Collection<File> getSourceFiles(String codebasePath) {
        return FileUtils.listFiles(FileUtils.getFile(codebasePath), FileFilterUtils.suffixFileFilter(".java"),
                TrueFileFilter.TRUE);
    }


}