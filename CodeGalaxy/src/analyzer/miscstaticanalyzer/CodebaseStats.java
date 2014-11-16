package analyzer.miscstaticanalyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class CodebaseStats {

    private ArrayList<PackageInfo> packagesInfo;
    private HashMap<String, Integer> classToLOCMap;

    public ArrayList<PackageInfo> getPackagesInfo() {
        return packagesInfo;
    }

    protected void setPackagesInfo(ArrayList<PackageInfo> packagesInfo) {
        this.packagesInfo = packagesInfo;
    }

    public HashMap<String, Integer> getClassToLOCMap() {
        return classToLOCMap;
    }

    protected void setClassToLOCMap(HashMap<String, Integer> classToLOCMap) {
        this.classToLOCMap = classToLOCMap;
    }
}
