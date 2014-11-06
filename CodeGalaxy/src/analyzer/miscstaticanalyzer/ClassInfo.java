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
    private boolean isInnerClass;

    public ClassInfo(String className, String packageName, String classType, boolean isInnerClass, int linesOfCode,
                     ArrayList<MethodInfo> methods) {
        this.className = className;
        this.packageName = packageName;
        this.classType = classType;
        this.linesOfCode = linesOfCode;
        this.methods = methods;
        this.isInnerClass = isInnerClass;
    }


    /**
     * Get the class name
     * @return
     */
    public String getClassName() {
        return className;
    }


    /**
     * Get the package name that the class belongs to
     * @return
     */
    public String getPackageName() {
        return packageName;
    }


    /**
     * Get the class type - Concrete, Abstract, or Interface
     * @return
     */
    public String getClassType() {
        return classType;
    }


    /**
     * Get the lines of code for the class (excludes blank lines and comments)
     * @return
     */
    public int getLinesOfCode() {
        return linesOfCode;
    }


    /**
     * Get all MethodInfo objects for this class
     * @return
     */
    public ArrayList<MethodInfo> getMethods() {
        return methods;
    }


    /**
     * Gets the flag indicating whether class is an inner class or not
     * @return
     */
    public boolean isInnerClass() {
        return isInnerClass;
    }
}
