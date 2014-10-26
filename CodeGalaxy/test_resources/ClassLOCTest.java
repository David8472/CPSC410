package mypackage;

public class ClassLOCTest {

    public ClassLOCTest() {
        int x = 1;
        int y = 2;
    }

    public void method() {
        int x = 1;
        String hello = "hello";
        int y = 2;
        int z = 3;
    }

    // This is a comment. Will be ignored in LOC count.


    public class InnerClassLOCTest {

        public void innerMethod() {
            int x = 1;
            String hello = "hello";
            int y = 2;
            int z = 3;
            int e = 3;
        }

        public void innerMethod2() {
            int x = 1;
            String hello = "hello";
            int y = 2;
            int z = 3;
        }


    }




}