package analyzer.miscstaticanalyzer;

/**
 * Holds information about a java method
 */
public class MethodInfo {

    private String methodName;
    private String containerClass;
    private String returnType;
    private String accessorType;
    private int linesOfCode;

    public MethodInfo(String methodName, String containerClass, String returnType, String accessorType,
                      int linesOfCode) {
        this.methodName = methodName;
        this.containerClass = containerClass;
        this.returnType = returnType;
        this.accessorType = accessorType;
        this.linesOfCode = linesOfCode;
    }

    /**
     * Get the method name
     * @return
     */
    public String getMethodName() {
        return methodName;
    }


    /**
     * Get the name of the class that the method belongs to
     * @return
     */
    public String getContainerClass() {
        return containerClass;
    }

    /**
     * Get the accessor type for the method - public, private, or protected
     * @return
     */
    public String getAccessorType() {
        return accessorType;
    }


    /**
     * Get the return type of the method
     * @return
     */
    public String getReturnType() {
        return returnType;
    }


    /**
     * Get the lines of code for the method (excludes blank lines and comments)
     * @return
     */
    public int getLinesOfCode() {
        return linesOfCode;
    }
}
