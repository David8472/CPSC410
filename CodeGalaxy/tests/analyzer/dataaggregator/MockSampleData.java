package analyzer.dataaggregator;

import analyzer.dependencyanalyzer.ClassDependencyInfo;
import analyzer.dependencyanalyzer.PackageDependencyInfo;
import analyzer.gitcommitcomponent.CommitAnalyzerInfo;
import analyzer.miscstaticanalyzer.PackageInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * Class that provides sample mock data
 */
public class MockSampleData {

    private ArrayList<PackageInfo> packages;
    private Vector<PackageDependencyInfo> packageDependencyInfos;
    private Vector<ClassDependencyInfo> classDependencyInfos;
    private CommitAnalyzerInfo commitMetaInfo;

    protected MockSampleData() {
        setSampleData1();
    }


    // DEFAULT
    protected void setSampleData1() {

        //******* COMMIT ANALYZER INFO ***************
        String[] addedFilenames = {"Class0", "Class1", "Class0", "Class1"};
        String[] deletedFilenames = {"DeletedClass0", "DeletedClass1", "DeletedClass2", "DeletedClass3"};

        ArrayList<String> addedFiles = new ArrayList<String>(Arrays.asList(addedFilenames));
        ArrayList<String> deletedFiles = new ArrayList<String>(Arrays.asList(deletedFilenames));

        commitMetaInfo = MockDataCreator.createMockCommitAnalyzerInfo(0, "MrPopo", addedFiles, deletedFiles);
        //******* END OF COMMIT ANALYZER INFO ***************


        //******* PACKAGE INFO ******************
        packages = MockDataCreator.createMockListOfPackageInfo(2, 2, 5);
        //******* END OF PACKAGE INFO ******************


        //******* PACKAGE DEPENDENCY INFO *************
        Vector<String> usesForPkg0 = new Vector<String>();
        usesForPkg0.add("package1");
        Vector<String> usedByForPkg0 = new Vector<String>();
        usedByForPkg0.add("package1");

        Vector<String> usesForPkg1 = new Vector<String>();
        usesForPkg1.add("package0");
        Vector<String> usedByForPkg1 = new Vector<String>();
        usedByForPkg1.add("package0");

        PackageDependencyInfo pkgDependencyInfo0 = MockDataCreator.createMockPackageDependencyInfo("package0",
                usesForPkg0, usedByForPkg0);
        PackageDependencyInfo pkgDependencyInfo1 = MockDataCreator.createMockPackageDependencyInfo("package1",
                usesForPkg1, usedByForPkg1);

        packageDependencyInfos = new Vector<PackageDependencyInfo>();
        packageDependencyInfos.add(pkgDependencyInfo0);
        packageDependencyInfos.add(pkgDependencyInfo1);
        //********* END OF PACKAGE DEPENDENCY INFO ***************

        //******* CLASS DEPENDENCY INFO ***********
        Vector<String> usesForClass0Pkg0 = new Vector<String>();
        usesForClass0Pkg0.add("Class1");
        Vector<String> usedByForClass0Pkg0 = new Vector<String>();
        usedByForClass0Pkg0.add("Class0");
        ClassDependencyInfo class0Pkg0DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class0", usesForClass0Pkg0, usedByForClass0Pkg0);

        Vector<String> usesForClass1Pkg0 = new Vector<String>();
        usesForClass1Pkg0.add("Class1");
        Vector<String> usedByForClass1Pkg0 = new Vector<String>();
        ClassDependencyInfo class1Pkg0DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class1", usesForClass1Pkg0, usedByForClass1Pkg0);

        Vector<String> usesForClass0Pkg1 = new Vector<String>();
        usesForClass0Pkg1.add("Class0");
        Vector<String> usedByForClass0Pkg1 = new Vector<String>();
        usedByForClass0Pkg1.add("Class0");
        ClassDependencyInfo class0Pkg1DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class0", usesForClass0Pkg1, usedByForClass0Pkg1);

        Vector<String> usesForClass1Pkg1 = new Vector<String>();
        Vector<String> usedByForClass1Pkg1 = new Vector<String>();
        usedByForClass1Pkg1.add("Class0");
        usedByForClass1Pkg1.add("Class1");
        ClassDependencyInfo class1Pkg1DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class1", usesForClass1Pkg1, usedByForClass1Pkg1);

        classDependencyInfos = new Vector<ClassDependencyInfo>();
        classDependencyInfos.add(class0Pkg0DependencyInfo);
        classDependencyInfos.add(class1Pkg0DependencyInfo);
        classDependencyInfos.add(class0Pkg1DependencyInfo);
        classDependencyInfos.add(class1Pkg1DependencyInfo);
        //******* END OF CLASS DEPENDENCY INFO ***********
    }

    // SAMPLE DATA 2
    protected void setSampleData1a() {

        //******* COMMIT ANALYZER INFO ***************
        String[] addedFilenames = {"Class0", "Class1", "Class0", "Class1"};
        String[] deletedFilenames = {"DeletedClass0", "DeletedClass1", "DeletedClass2", "DeletedClass3"};

        ArrayList<String> addedFiles = new ArrayList<String>(Arrays.asList(addedFilenames));
        ArrayList<String> deletedFiles = new ArrayList<String>(Arrays.asList(deletedFilenames));

        commitMetaInfo = MockDataCreator.createMockCommitAnalyzerInfo(1, "WalterWhite", addedFiles, deletedFiles);
        //******* END OF COMMIT ANALYZER INFO ***************


        //******* PACKAGE INFO ******************
        packages = MockDataCreator.createMockListOfPackageInfo(2, 2, 5);
        //******* END OF PACKAGE INFO ******************


        //******* PACKAGE DEPENDENCY INFO *************
        Vector<String> usesForPkg0 = new Vector<String>();
        usesForPkg0.add("package1");
        Vector<String> usedByForPkg0 = new Vector<String>();
        usedByForPkg0.add("package1");

        Vector<String> usesForPkg1 = new Vector<String>();
        usesForPkg1.add("package0");
        Vector<String> usedByForPkg1 = new Vector<String>();
        usedByForPkg1.add("package0");

        PackageDependencyInfo pkgDependencyInfo0 = MockDataCreator.createMockPackageDependencyInfo("package0",
                usesForPkg0, usedByForPkg0);
        PackageDependencyInfo pkgDependencyInfo1 = MockDataCreator.createMockPackageDependencyInfo("package1",
                usesForPkg1, usedByForPkg1);

        packageDependencyInfos = new Vector<PackageDependencyInfo>();
        packageDependencyInfos.add(pkgDependencyInfo0);
        packageDependencyInfos.add(pkgDependencyInfo1);
        //********* END OF PACKAGE DEPENDENCY INFO ***************

        //******* CLASS DEPENDENCY INFO ***********
        Vector<String> usesForClass0Pkg0 = new Vector<String>();
        usesForClass0Pkg0.add("Class1");
        Vector<String> usedByForClass0Pkg0 = new Vector<String>();
        usedByForClass0Pkg0.add("Class0");
        ClassDependencyInfo class0Pkg0DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class0", usesForClass0Pkg0, usedByForClass0Pkg0);

        Vector<String> usesForClass1Pkg0 = new Vector<String>();
        usesForClass1Pkg0.add("Class1");
        Vector<String> usedByForClass1Pkg0 = new Vector<String>();
        ClassDependencyInfo class1Pkg0DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class1", usesForClass1Pkg0, usedByForClass1Pkg0);

        Vector<String> usesForClass0Pkg1 = new Vector<String>();
        usesForClass0Pkg1.add("Class0");
        Vector<String> usedByForClass0Pkg1 = new Vector<String>();
        usedByForClass0Pkg1.add("Class0");
        ClassDependencyInfo class0Pkg1DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class0", usesForClass0Pkg1, usedByForClass0Pkg1);

        Vector<String> usesForClass1Pkg1 = new Vector<String>();
        Vector<String> usedByForClass1Pkg1 = new Vector<String>();
        usedByForClass1Pkg1.add("Class0");
        usedByForClass1Pkg1.add("Class1");
        ClassDependencyInfo class1Pkg1DependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class1", usesForClass1Pkg1, usedByForClass1Pkg1);

        classDependencyInfos = new Vector<ClassDependencyInfo>();
        classDependencyInfos.add(class0Pkg0DependencyInfo);
        classDependencyInfos.add(class1Pkg0DependencyInfo);
        classDependencyInfos.add(class0Pkg1DependencyInfo);
        classDependencyInfos.add(class1Pkg1DependencyInfo);
        //******* END OF CLASS DEPENDENCY INFO ***********
    }



    public ArrayList<PackageInfo> getPackages() {
        return packages;
    }

    public Vector<PackageDependencyInfo> getPackageDependencyInfos() {
        return packageDependencyInfos;
    }

    public Vector<ClassDependencyInfo> getClassDependencyInfos() {
        return classDependencyInfos;
    }

    public CommitAnalyzerInfo getCommitMetaInfo() {
        return commitMetaInfo;
    }
}
