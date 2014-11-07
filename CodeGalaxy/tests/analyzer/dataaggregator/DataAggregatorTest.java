package analyzer.dataaggregator;

import analyzer.DataAggregator;
import analyzer.dependencyanalyzer.ClassDependencyInfo;
import analyzer.dependencyanalyzer.PackageDependencyInfo;
import analyzer.gitcommitcomponent.CommitAnalyzerInfo;
import analyzer.miscstaticanalyzer.PackageInfo;

import java.util.ArrayList;
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

        DataAggregator dataAggregator = new DataAggregator("testSample1.yml");

        // 1st commit
        MockSampleData sampleData1 = new MockSampleData();
        ArrayList<PackageInfo> packageInfos = sampleData1.getPackages();
        Vector<PackageDependencyInfo> packageDependencyInfoVector = sampleData1.getPackageDependencyInfos();
        Vector<ClassDependencyInfo> classDependencyInfoVector = sampleData1.getClassDependencyInfos();
        CommitAnalyzerInfo commitMetaData = sampleData1.getCommitMetaInfo();
        dataAggregator.writeCommitDataToYAMLFile(commitMetaData, packageInfos, packageDependencyInfoVector, classDependencyInfoVector);

        // 2nd commit
        sampleData1.setSampleData1a();
        packageInfos = sampleData1.getPackages();
        packageDependencyInfoVector = sampleData1.getPackageDependencyInfos();
        classDependencyInfoVector = sampleData1.getClassDependencyInfos();
        commitMetaData = sampleData1.getCommitMetaInfo();
        dataAggregator.writeCommitDataToYAMLFile(commitMetaData, packageInfos, packageDependencyInfoVector, classDependencyInfoVector);
    }



}