package analyzer.miscstaticanalyzer;

import java.util.ArrayList;

/**
 * Holds static analysis statistics of a codebase.
 * To be used by DataAggregator.
 */
public class MiscCodeBaseStats {

    private ArrayList<String> packages;
    private ArrayList<ClassInfo> classes;

    public MiscCodeBaseStats() {
        packages = new ArrayList<String>();
        classes = new ArrayList<ClassInfo>();
    }

    public int getNumberOfPackages() {
        return packages.size();
    }

    public ArrayList<String> getPackages() {
        return packages;
    }

    public ArrayList<ClassInfo> getClasses() {
        return classes;
    }


    public ArrayList<MethodInfo> getAllMethods() {
        ArrayList<MethodInfo> methods = new ArrayList<MethodInfo>();
        for(ClassInfo classInfo : classes) {
            for(MethodInfo mthd : classInfo.getMethods()) {
                methods.add(mthd);
            }
        }
        return methods;
    }


    /**
     * Add package to list of packages
     * @param packageName
     */
    public void addPackage(String packageName) {
        if(!packages.contains(packageName)) {
            packages.add(packageName);
        }
    }


    /**
     * Add class info to list of classes info
     * @param classInfo
     */
    public void addClass(ClassInfo classInfo) {
        classes.add(classInfo);
    }

}
