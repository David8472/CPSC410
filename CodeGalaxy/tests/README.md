## Tests

The tests directory contains the following tests and makes use of resources located in the `test_resources` directory:

- `analyzer.commitanalyzer` package contains all tests related to the Git Commit Analyzer component.
- `analyzer.dataaggregator` package contains all tests related to the Data Aggregator component. This package contains the following classes:
    - MockDataCreator - a helper class that is used to generate mock data
    - MockSampleData - a helper class that uses MockDataCreator to generate sample data for a commit
    - DataAggregatorTest - a class that contains a test driver to test the Data Aggregator.
	The output is then manually inspected to verify that the file format is correct.  
- `analyzer.dependencyanalyzer` package contains all tests related to the Dependency Analyzer component.
- `analyzer.miscstaticanalyzer` package contains all unit tests related to the Misc Static Analyzer component.
 

We manually did integration tests by running AnalysisController and checking the visual output was correct.

Please, see individual classes for more information on test inputs and expected outputs.
