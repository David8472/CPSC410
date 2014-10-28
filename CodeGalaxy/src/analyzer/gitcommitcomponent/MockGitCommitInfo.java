package analyzer.gitcommitcomponent;

import java.util.ArrayList;

public class MockGitCommitInfo extends GenericGitCommitInfo {

    private String commitID;
    private String commitAuthor;
    private ArrayList<String> filesDeleted;
    private ArrayList<String> filesCreated;
    private ArrayList<MockChangedFile> filesChanged;


    // TO DO
    public void setupMockData1() {

    }

    public class MockChangedFile {

        private String fileName;
        private int linesChanged;

        public MockChangedFile(String fileName, int linesChanged) {
            this.fileName = fileName;
            this.linesChanged = linesChanged;
        }

        public String getFileName() {
            return fileName;
        }

        public int getLinesChanged() {
            return linesChanged;
        }
    }


}
