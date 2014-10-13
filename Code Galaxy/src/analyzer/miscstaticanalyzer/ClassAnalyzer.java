package analyzer.miscstaticanalyzer;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Performs static code analysis operations on a Java file
 */
public class ClassAnalyzer {

    private CompilationUnit compilationUnit;

    /**
     * Constructs a ClassAnalyzer to analyze the java file passed in the argument
     *
     * @param javaFile - Java file to analyze
     * @throws IOException
     * @throws ParseException
     */
    public ClassAnalyzer(File javaFile) throws IOException, ParseException {
        // Check if file exists
        if(!javaFile.exists()) {
            throw new FileNotFoundException();
        }

        // Check if file is a .java file
        if(!javaFile.getName().contains(".java")) {
            throw new IllegalArgumentException();
        }

        // Parses java file and creates abstract syntax tree using JavaParser library
        compilationUnit = JavaParser.parse(javaFile);
    }


    /**
     * Returns the package of the java class being analyzed
     *
     * @return
     */
    public String getPackage() {
        String packageName = compilationUnit.getPackage().toString().trim();
        packageName = packageName.replace("package ", "");
        packageName = packageName.replace(";", "");
        return packageName;
    }

}
