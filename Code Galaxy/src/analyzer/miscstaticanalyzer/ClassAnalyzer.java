package analyzer.miscstaticanalyzer;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Performs static code analysis operations on a Java file
 */
public class ClassAnalyzer extends VoidVisitorAdapter {

    private CompilationUnit compilationUnit;
    private ArrayList<ClassInfo> classInfos;

    /**
     * Constructs a ClassAnalyzer to perform static analysis operations on the java file passed in the argument
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
     * @return
     */
    public String getPackage() {
        String packageName = compilationUnit.getPackage().toString().trim();
        packageName = packageName.replace("package ", "");
        packageName = packageName.replace(";", "");
        return packageName;
    }


    /**
     * Returns a list of ClassInfo objects associated with the analyzed class.
     * Nested classes are their own ClassInfo objects.
     * @return
     */
    public ArrayList<ClassInfo> getClassInfo() {
        classInfos = new ArrayList<ClassInfo>();

        visit(compilationUnit, null);

        return classInfos;
    }


    /**
     * Visits class and all its nested classes recursively
     * @param javaClass
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration javaClass, Object arg) {
        super.visit(javaClass, arg);
        String className = getClassName(javaClass);
        classInfos.add(new ClassInfo(className, null, null, 0, null));
    }


    /**
     * Gets the name of a class indicated by the argument class node
     * @param javaClass - Node in AST that represents a class
     * @return
     */
    private String getClassName(ClassOrInterfaceDeclaration javaClass) {
        return javaClass.getName();
    }


}
