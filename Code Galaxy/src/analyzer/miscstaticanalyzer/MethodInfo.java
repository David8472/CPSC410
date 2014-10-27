package analyzer.miscstaticanalyzer;

/**
 * Holds information about a java method
 */
public class MethodInfo {

    private String methodName;
    private String containerClass;
    private String returnType;
    private int linesOfCode;

    public MethodInfo(String methodName, String containerClass, String returnType, int linesOfCode) {
        this.methodName = methodName;
        this.containerClass = containerClass;
        this.returnType = returnType;
        this.linesOfCode = linesOfCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getContainerClass() {
        return containerClass;
    }

    public String getReturnType() {
        return returnType;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }
}
