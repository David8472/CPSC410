package analyzer.miscstaticanalyzer;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClassAnalyzer {

    private CompilationUnit compilationUnit;

    public ClassAnalyzer(File javaFile) throws IOException, ParseException {
        if(!javaFile.exists()) {
            throw new FileNotFoundException();
        }

        if(!javaFile.getName().contains(".java")) {
            throw new IllegalArgumentException();
        }

        compilationUnit = JavaParser.parse(javaFile);
    }

    public String getPackage() {
        String packageName = compilationUnit.getPackage().toString().trim();
        packageName = packageName.replace("package ", "");
        packageName = packageName.replace(";", "");
        return packageName;
    }

}
