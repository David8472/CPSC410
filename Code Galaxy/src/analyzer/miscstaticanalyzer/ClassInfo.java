package analyzer.miscstaticanalyzer;

import java.util.ArrayList;

/**
 * Holds information about a java class
 */
public class ClassInfo {

    private String className;
    private String packageName;
    private String classType;
    private int linesOfCode;
    private ArrayList<MethodInfo> methods;

    public ClassInfo(String className, String packageName, String classType, int linesOfCode,
                     ArrayList<MethodInfo> methods) {
        this.className = className;
        this.packageName = packageName;
        this.classType = classType;
        this.linesOfCode = linesOfCode;
        this.methods = methods;
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassType() {
        return classType;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public ArrayList<MethodInfo> getMethods() {
        return methods;
    }
}
