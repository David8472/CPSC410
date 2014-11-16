package analyzer.dataaggregator;

import analyzer.dependencyanalyzer.ClassDependencyInfo;
import analyzer.dependencyanalyzer.PackageDependencyInfo;
import analyzer.gitcommitcomponent.CommitAnalyzerInfo;
import analyzer.miscstaticanalyzer.ClassInfo;
import analyzer.miscstaticanalyzer.PackageInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * Class that provides sample mock data
 */
public class MockSampleData {

    private ArrayList<PackageInfo> packages;
    private Vector<PackageDependencyInfo> packageDependencyInfos;
    private Vector<ClassDependencyInfo> classDependencyInfos;
    private CommitAnalyzerInfo commitMetaInfo;
    private HashMap<String, Integer> classesChangedWithLOCChanges;
    private ArrayList<String> removedClasses;

    protected MockSampleData() {
        setSampleCommitData();
    }


    // DEFAULT
    protected void setSampleCommitData() {

        /* Commit author and number */
        commitMetaInfo = MockDataCreator.createMockCommitAnalyzerInfo(0, "MrPopo");

        /* Shape package information */
        ArrayList<ClassInfo> classesInShapePkg = new ArrayList<ClassInfo>();
        ClassInfo triangleClass = MockDataCreator.createMockClassInfo("Triangle", "shape", "Concrete", 2);
        classesInShapePkg.add(triangleClass);
        ClassInfo squareClass = MockDataCreator.createMockClassInfo("Shape", "shape", "Interface", 0);
        classesInShapePkg.add(squareClass);
        PackageInfo shapePkg = MockDataCreator.createMockPackageInfoWithListOfClasses("shape", classesInShapePkg);

        /* People package information */
        ArrayList<ClassInfo> classesInPeoplePkg = new ArrayList<ClassInfo>();
        ClassInfo personClass = MockDataCreator.createMockClassInfo("Person", "people", "Abstract", 2);
        classesInPeoplePkg.add(personClass);
        ClassInfo superheroClass = MockDataCreator.createMockClassInfo("Superhero", "people", "Concrete", 2);
        classesInPeoplePkg.add(superheroClass);
        PackageInfo peoplePkg = MockDataCreator.createMockPackageInfoWithListOfClasses("people", classesInPeoplePkg);

        packages = new ArrayList<PackageInfo>();
        packages.add(shapePkg);
        packages.add(peoplePkg);

        /* Changed Classes */
        classesChangedWithLOCChanges = new HashMap<String, Integer>();
        for(ClassInfo classInfo : classesInShapePkg) {
            String className = classInfo.getPackageName() + "." + classInfo.getClassName();
            classesChangedWithLOCChanges.put(className, classInfo.getLinesOfCode());
        }
        for(ClassInfo classInfo : classesInPeoplePkg) {
            String className = classInfo.getPackageName() + "." + classInfo.getClassName();
            classesChangedWithLOCChanges.put(className, classInfo.getLinesOfCode());
        }

        /* Removed Classes */
        removedClasses = new ArrayList<String>();
        removedClasses.add("shape.Polygon");

        /* Package Dependency Info */
        Vector<String> usesForShapePkg = new Vector<String>();
        Vector<String> usedByForShapePkg = new Vector<String>();
        usedByForShapePkg.add("people");

        Vector<String> usesForPeoplePkg = new Vector<String>();
        usesForPeoplePkg.add("shape");
        Vector<String> usedByForPeoplePkg = new Vector<String>();

        PackageDependencyInfo pkgDependencyInfo0 = MockDataCreator.createMockPackageDependencyInfo("shape",
                usesForShapePkg, usedByForShapePkg);
        PackageDependencyInfo pkgDependencyInfo1 = MockDataCreator.createMockPackageDependencyInfo("people",
                usesForPeoplePkg, usedByForPeoplePkg);

        packageDependencyInfos = new Vector<PackageDependencyInfo>();
        packageDependencyInfos.add(pkgDependencyInfo0);
        packageDependencyInfos.add(pkgDependencyInfo1);


        /* Class Dependency Info */
        Vector<String> usesForTriangleClass = new Vector<String>();
        Vector<String> usedByForTriangleClass = new Vector<String>();
        usedByForTriangleClass.add("people.Superhero");
        ClassDependencyInfo triangleClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("shape.Triangle",
                usesForTriangleClass, usedByForTriangleClass);

        Vector<String> usesForShapeClass = new Vector<String>();
        Vector<String> usedByForShapeClass = new Vector<String>();
        usedByForShapeClass.add("people.Person");
        ClassDependencyInfo shapeClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("shape.Shape",
                usesForShapeClass, usedByForShapeClass);

        Vector<String> usesForPersonClass = new Vector<String>();
        usesForPersonClass.add("shape.Shape");
        Vector<String> usedByForPersonClass = new Vector<String>();
        ClassDependencyInfo personClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("people.Person",
                usesForPersonClass, usedByForPersonClass);

        Vector<String> usesForSuperheroClass = new Vector<String>();
        usesForSuperheroClass.add("shape.Triangle");
        Vector<String> usedByForSuperheroClass = new Vector<String>();
        ClassDependencyInfo superHeroClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("people.Superhero",
                usesForSuperheroClass, usedByForSuperheroClass);

        classDependencyInfos = new Vector<ClassDependencyInfo>();
        classDependencyInfos.add(triangleClassDependencyInfo);
        classDependencyInfos.add(shapeClassDependencyInfo);
        classDependencyInfos.add(personClassDependencyInfo);
        classDependencyInfos.add(superHeroClassDependencyInfo);
    }


    protected void setSampleCommitData2() {

        /* Commit author and number */
        commitMetaInfo = MockDataCreator.createMockCommitAnalyzerInfo(1, "Heisenberg");

        /* Shape package information */
        ArrayList<ClassInfo> classesInShapePkg = new ArrayList<ClassInfo>();
        ClassInfo triangleClass = MockDataCreator.createMockClassInfo("Triangle", "shape", "Concrete", 2);
        classesInShapePkg.add(triangleClass);
        ClassInfo squareClass = MockDataCreator.createMockClassInfo("Shape", "shape", "Interface", 0);
        classesInShapePkg.add(squareClass);
        PackageInfo shapePkg = MockDataCreator.createMockPackageInfoWithListOfClasses("shape", classesInShapePkg);

        /* People package information */
        ArrayList<ClassInfo> classesInPeoplePkg = new ArrayList<ClassInfo>();
        ClassInfo personClass = MockDataCreator.createMockClassInfo("Person", "people", "Abstract", 2);
        classesInPeoplePkg.add(personClass);
        ClassInfo superheroClass = MockDataCreator.createMockClassInfo("Superhero", "people", "Concrete", 2);
        classesInPeoplePkg.add(superheroClass);
        PackageInfo peoplePkg = MockDataCreator.createMockPackageInfoWithListOfClasses("people", classesInPeoplePkg);

        packages = new ArrayList<PackageInfo>();
        packages.add(shapePkg);
        packages.add(peoplePkg);

        /* Changed Classes */
        classesChangedWithLOCChanges = new HashMap<String, Integer>();

        /* Removed Classes */
        removedClasses = new ArrayList<String>();

        /* Package Dependency Info */
        Vector<String> usesForShapePkg = new Vector<String>();
        Vector<String> usedByForShapePkg = new Vector<String>();
        usedByForShapePkg.add("people");

        Vector<String> usesForPeoplePkg = new Vector<String>();
        usesForPeoplePkg.add("shape");
        Vector<String> usedByForPeoplePkg = new Vector<String>();

        PackageDependencyInfo pkgDependencyInfo0 = MockDataCreator.createMockPackageDependencyInfo("package0",
                usesForShapePkg, usedByForShapePkg);
        PackageDependencyInfo pkgDependencyInfo1 = MockDataCreator.createMockPackageDependencyInfo("package1",
                usesForPeoplePkg, usedByForPeoplePkg);

        packageDependencyInfos = new Vector<PackageDependencyInfo>();
        packageDependencyInfos.add(pkgDependencyInfo0);
        packageDependencyInfos.add(pkgDependencyInfo1);


        /* Class Dependency Info */
        Vector<String> usesForTriangleClass = new Vector<String>();
        Vector<String> usedByForTriangleClass = new Vector<String>();
        usedByForTriangleClass.add("people.Superhero");
        ClassDependencyInfo triangleClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class0",
                usesForTriangleClass, usedByForTriangleClass);

        Vector<String> usesForShapeClass = new Vector<String>();
        Vector<String> usedByForShapeClass = new Vector<String>();
        usedByForShapeClass.add("people.Person");
        ClassDependencyInfo shapeClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class1",
                usesForShapeClass, usedByForShapeClass);

        Vector<String> usesForPersonClass = new Vector<String>();
        usesForPersonClass.add("shape.Shape");
        Vector<String> usedByForPersonClass = new Vector<String>();
        ClassDependencyInfo personClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class0",
                usesForPersonClass, usedByForPersonClass);

        Vector<String> usesForSuperheroClass = new Vector<String>();
        usesForSuperheroClass.add("shape.Triangle");
        Vector<String> usedByForSuperheroClass = new Vector<String>();
        ClassDependencyInfo superHeroClassDependencyInfo = MockDataCreator.createMockClassDependencyInfo("Class1",
                usesForSuperheroClass, usedByForSuperheroClass);

        classDependencyInfos = new Vector<ClassDependencyInfo>();
        classDependencyInfos.add(triangleClassDependencyInfo);
        classDependencyInfos.add(shapeClassDependencyInfo);
        classDependencyInfos.add(personClassDependencyInfo);
        classDependencyInfos.add(superHeroClassDependencyInfo);

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

    public HashMap<String, Integer> getClassesChangedWithLOCChanges() {
        return classesChangedWithLOCChanges;
    }

    public ArrayList<String> getRemovedClasses() {
        return removedClasses;
    }
}
