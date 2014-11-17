package analyzer.dataaggregator;

import fuser.DataAggregator;
import analyzer.dependencyanalyzer.ClassDependencyInfo;
import analyzer.dependencyanalyzer.PackageDependencyInfo;
import analyzer.gitcommitcomponent.CommitAnalyzerInfo;
import analyzer.miscstaticanalyzer.PackageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Test driver for DataAggregator
 */
public class DataAggregatorTest {

    public static void main(String[] args) {

        // Writes data to YAML file - testSample1.yml
        testSample1Data();

    }

    private static void testSample1Data() {

        DataAggregator dataAggregator = new DataAggregator("test_resources/testSample1.yml");

        // 1st commit
        MockSampleData sampleData1 = new MockSampleData();
        ArrayList<PackageInfo> packageInfos = sampleData1.getPackages();
        Vector<PackageDependencyInfo> packageDependencyInfoVector = sampleData1.getPackageDependencyInfos();
        Vector<ClassDependencyInfo> classDependencyInfoVector = sampleData1.getClassDependencyInfos();
        CommitAnalyzerInfo commitMetaData = sampleData1.getCommitMetaInfo();
        HashMap<String, Integer> changedClasses = sampleData1.getClassesChangedWithLOCChanges();
        ArrayList<String> removedClasses = sampleData1.getRemovedClasses();

        dataAggregator.writeCommitDataToYAMLFile(commitMetaData, packageInfos, changedClasses, removedClasses, packageDependencyInfoVector, classDependencyInfoVector);

        // 2nd commit
        sampleData1.setSampleCommitData2();
        packageInfos = sampleData1.getPackages();
        packageDependencyInfoVector = sampleData1.getPackageDependencyInfos();
        classDependencyInfoVector = sampleData1.getClassDependencyInfos();
        commitMetaData = sampleData1.getCommitMetaInfo();
        changedClasses = sampleData1.getClassesChangedWithLOCChanges();
        removedClasses = sampleData1.getRemovedClasses();

        dataAggregator.writeCommitDataToYAMLFile(commitMetaData, packageInfos, changedClasses, removedClasses, packageDependencyInfoVector, classDependencyInfoVector);
    }



}