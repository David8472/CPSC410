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

    /**
     * Gets the total number of packages stored in list of packages
     * @return
     */
    public int getNumberOfPackages() {
        return packages.size();
    }


    /**
     * Gets all package names stored in list of packages
     * @return
     */
    public ArrayList<String> getPackages() {
        return packages;
    }


    /**
     * Gets all ClassInfo objects stored in list of ClassInfo objects
     * @return
     */
    public ArrayList<ClassInfo> getClasses() {
        return classes;
    }


    /**
     * Gets all MethodInfo objects stored in list of MethodInfo objects
     * @return
     */
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
     * Gets all ClassInfo objects filtered by container package
     * @param packageName
     * @return
     */
    public ArrayList<ClassInfo> getClassesByPackageName(String packageName) {
        if(!packages.contains(packageName)) {
            return null;
        }

        ArrayList<ClassInfo> classesInPackage = new ArrayList<ClassInfo>();
        for(ClassInfo classInfo : classes) {
            if(classInfo.getPackageName().equals(packageName)) {
                classesInPackage.add(classInfo);
            }
        }

        return classesInPackage;
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
