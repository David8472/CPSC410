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

    public String getMethodName() {
        return methodName;
    }

    public String getContainerClass() {
        return containerClass;
    }

    public String getAccessorType() {
        return accessorType;
    }

    public String getReturnType() {
        return returnType;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }
}
