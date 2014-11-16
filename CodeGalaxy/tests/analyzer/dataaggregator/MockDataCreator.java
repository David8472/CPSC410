package analyzer.dataaggregator;

import analyzer.dependencyanalyzer.ClassDependencyInfo;
import analyzer.dependencyanalyzer.PackageDependencyInfo;
import analyzer.gitcommitcomponent.CommitAnalyzerInfo;
import analyzer.miscstaticanalyzer.ClassInfo;
import analyzer.miscstaticanalyzer.MethodInfo;
import analyzer.miscstaticanalyzer.PackageInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Class that allows the creation of mock data for CommitAnalyzerInfo,
 * PackageDependencyInfo, ClassDependencyInfo, ClassInfo, and PackageInfo
 */
public class MockDataCreator {

    private static final int MAX_PACKAGES = 10;
    private static final int MAX_METHODS_PER_CLASS = 10;
    private static final int MAX_CLASSES_PER_PACKAGE = 10;
    private static final int METHOD_LOC = 10;


    /**
     * Creates a mock CommitAnalyzerInfo object
     * @param commitNo - The commit number
     * @param author   - Author name
     * @return
     */
    protected static CommitAnalyzerInfo createMockCommitAnalyzerInfo(int commitNo, String author) {

        CommitAnalyzerInfo commitInfo = new CommitAnalyzerInfo("fakeRepo");
        Class<?> c = commitInfo.getClass();

        try {
            Field commitNum = c.getDeclaredField("commitNumber");
            commitNum.setAccessible(true);
            commitNum.setInt(commitInfo, commitNo);

            Field authorName = c.getDeclaredField("authorName");
            authorName.setAccessible(true);
            authorName.set(commitInfo, author);

        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

        return commitInfo;
    }


    /**
     * Creates a mock PackageDependencyInfo object
     * @param packageName - The package name
     * @param uses        - The names of the packages that this package depends on
     * @param usedBy      - The names of the packages that use this package
     * @return
     */
    protected static PackageDependencyInfo createMockPackageDependencyInfo(String packageName, Vector<String> uses,
                                                                           Vector<String> usedBy) {

        PackageDependencyInfo pkgDependencyInfo = new PackageDependencyInfo(usedBy.size(), uses.size());
        Class<?> c = pkgDependencyInfo.getClass();

        try {

            Field name = c.getDeclaredField("name");
            name.setAccessible(true);
            name.set(pkgDependencyInfo, packageName);

            Field afferentVector = c.getDeclaredField("afferentVector");
            afferentVector.setAccessible(true);
            afferentVector.set(pkgDependencyInfo, usedBy);

            Field efferentVector = c.getDeclaredField("efferentVector");
            efferentVector.setAccessible(true);
            efferentVector.set(pkgDependencyInfo, uses);

        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

        return pkgDependencyInfo;
    }


    /**
     * Creates a mock ClassDependencyInfo object
     * @param className - The class name
     * @param uses      - The names of the classes that this class depends on
     * @param usedBy    - The names of the classes that depend on this class
     * @return
     */
    protected static ClassDependencyInfo createMockClassDependencyInfo(String className, Vector<String> uses,
                                                                       Vector<String> usedBy) {

        ClassDependencyInfo classDependencyInfo = new ClassDependencyInfo(usedBy.size(), uses.size());
        Class<?> c = classDependencyInfo.getClass();

        try {
            Field name = c.getDeclaredField("name");
            name.setAccessible(true);
            name.set(classDependencyInfo, className);

            Field afferentVector = c.getDeclaredField("afferentVector");
            afferentVector.setAccessible(true);
            afferentVector.set(classDependencyInfo, usedBy);

            Field efferentVector = c.getDeclaredField("efferentVector");
            efferentVector.setAccessible(true);
            efferentVector.set(classDependencyInfo, uses);

        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

        return classDependencyInfo;

    }


    /**
     * Creates a mock ClassInfo object
     * @param className          - Class name
     * @param packageName        - Package name of the class
     * @param classType          - The class type - Concrete, Abstract, or Interface
     * @param numMethodsToCreate - The number of methods to add for class. Must be > 0 and <= MAX_METHODS_PER_CLASS, or will be set to MAX_METHODS_PER_CLASS.
     * @return
     */
    protected static ClassInfo createMockClassInfo(String className, String packageName, String classType,
                                                   int numMethodsToCreate) {

        ArrayList<MethodInfo> methods = new ArrayList<MethodInfo>();

        int methodLimit = MAX_METHODS_PER_CLASS;
        if(numMethodsToCreate > 0 && numMethodsToCreate < MAX_METHODS_PER_CLASS) {
            methodLimit = numMethodsToCreate;
        }

        String methodNamePrefix = "method";
        int classLOC = METHOD_LOC * methodLimit;
        for(int i = 0; i < methodLimit; i++) {
            String methodName = methodNamePrefix + i;

            String returnType = "";
            switch(i % 3) {
                case 0:
                    returnType = "public";
                    break;
                case 1:
                    returnType = "protected";
                    break;
                case 2:
                    returnType = "private";
                    break;
            }


            MethodInfo method = new MethodInfo(methodName, className, "void", returnType, METHOD_LOC);
            methods.add(method);
        }

        return new ClassInfo(className, packageName, classType, false, classLOC, methods);
    }


    /**
     * Creates a mock PackageInfo object with the list of ClassInfo objects passed in as arguments.
     * @param packageName - Name of package
     * @param classInfos  - List of ClassInfo objects to add to the PackageInfo object.
     * @return
     */
    protected static PackageInfo createMockPackageInfoWithListOfClasses(String packageName,
                                                                        ArrayList<ClassInfo> classInfos) {

        PackageInfo packageInfo = new PackageInfo(packageName);
        Class<?> c = packageInfo.getClass();

        try {
            Method addClass = c.getDeclaredMethod("addClass", ClassInfo.class);
            addClass.setAccessible(true);
            for(ClassInfo classInfo : classInfos) {
                addClass.invoke(packageInfo, classInfo);
            }
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        } catch(InvocationTargetException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }


        return packageInfo;
    }


    /**
     * Creates a mock PackageInfo object
     * @param packageName             - The package name
     * @param numberOfClassesToAdd    - The number of classes to add per package. Must be > 0 and <= MAX_CLASSES_PER_PACKAGE, or will be set to MAX_CLASSES_PER_PACKAGE.
     * @param numberOfMethodsPerClass - The number of methods to add per class. Must be > 0 and <= MAX_METHODS_PER_CLASS, or will be set to MAX_METHODS_PER_CLASS.
     * @return
     */
    protected static PackageInfo createMockPackageInfo(String packageName, int numberOfClassesToAdd,
                                                       int numberOfMethodsPerClass) {

        PackageInfo packageInfo = new PackageInfo(packageName);
        Class<?> c = packageInfo.getClass();

        int classLimit = MAX_CLASSES_PER_PACKAGE;
        if(numberOfClassesToAdd > 0 && numberOfClassesToAdd < MAX_CLASSES_PER_PACKAGE) {
            classLimit = numberOfClassesToAdd;
        }

        String classNamePrefix = "Class";
        for(int i = 0; i < classLimit; i++) {
            String className = classNamePrefix + i;

            String classType = "";
            switch(i % 3) {
                case 0:
                    classType = "Concrete";
                    break;
                case 1:
                    classType = "Abstract";
                    break;
                case 2:
                    classType = "Interface";
                    break;
            }

            ClassInfo classInfo = createMockClassInfo(className, packageName, classType, numberOfMethodsPerClass);

            try {
                Method addClass = c.getDeclaredMethod("addClass", ClassInfo.class);
                addClass.setAccessible(true);
                addClass.invoke(packageInfo, classInfo);
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch(InvocationTargetException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return packageInfo;
    }

    /**
     * Creates a mock list of packages
     * @param numberOfPackagesToAdd   - The number of packages to add. Must be > 0 and <= MAX_PACKAGES, or will be set to MAX_PACKAGES.
     * @param numberOfClassesPerPkg   - The number of classes to add per package. Must be > 0 and <= MAX_CLASSES_PER_PACKAGE, or will be set to MAX_CLASSES_PER_PACKAGE.
     * @param numberOfMethodsPerClass - The number of methods to add per class. Must be > 0 and <= MAX_METHODS_PER_CLASS, or will be set to MAX_METHODS_PER_CLASS.
     * @return
     */
    protected static ArrayList<PackageInfo> createMockListOfPackageInfo(int numberOfPackagesToAdd,
                                                                        int numberOfClassesPerPkg,
                                                                        int numberOfMethodsPerClass) {

        int packageLimit = MAX_PACKAGES;
        if(numberOfPackagesToAdd > 0 && numberOfPackagesToAdd < MAX_PACKAGES) {
            packageLimit = numberOfPackagesToAdd;
        }

        ArrayList<PackageInfo> packages = new ArrayList<PackageInfo>();

        String packagePrefix = "package";
        for(int i = 0; i < packageLimit; i++) {
            String packageName = packagePrefix + i;
            PackageInfo pkg = createMockPackageInfo(packageName, numberOfClassesPerPkg, numberOfMethodsPerClass);
            packages.add(pkg);
        }

        return packages;
    }


}
