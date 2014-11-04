package analyzer.miscstaticanalyzer;

import java.util.ArrayList;

/**
 * Holds information about a java package
 */
public class PackageInfo {

    private String packageName;
    private int LOC;
    private ArrayList<ClassInfo> listOfClasses;

    public PackageInfo(String packageName) {
        this.packageName = packageName;
        listOfClasses = new ArrayList<ClassInfo>();
        LOC = 0;
    }


    /**
     * Get the package name
     * @return
     */
    public String getPackageName() {
        return packageName;
    }


    /**
     * Get the lines of code for the package (excludes blank lines and comments)
     * @return
     */
    public int getLinesOfCode() {
        return LOC;
    }


    /**
     * Get all ClassInfo objects for this package
     * @return
     */
    public ArrayList<ClassInfo> getListOfClasses() {
        return listOfClasses;
    }


    /**
     * Add class to package. If class is already in package, it is not added twice.
     * @param classToAdd - Class to add to the package
     */
    protected void addClass(ClassInfo classToAdd) {

        String classToAddName = classToAdd.getClassName();

        // Checks if class to be added already exists in list of classes in package
        for(ClassInfo classInfo : listOfClasses) {

            // If it does exist, return
            if(classInfo.getClassName().equals(classToAddName)) {
                return;
            }
        }

        // Otherwise if it doesn't already exist, add it to the list of classes in package
        listOfClasses.add(classToAdd);

        // Update LOC by the class LOC, but only for outer classes (not inner classes as the outer class
        // includes the LOC of its inner classes in its own LOC)
        if(!classToAdd.isInnerClass()) {
            addLinesOfCode(classToAdd.getLinesOfCode());
        }
    }


    /**
     * Increase lines of code for the package
     * @param linesToAdd - The amount to increase LOC by
     */
    private void addLinesOfCode(int linesToAdd) {
        LOC += linesToAdd;
    }


}
