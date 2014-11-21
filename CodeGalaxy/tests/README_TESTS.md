## Tests

The tests directory contains the following tests and makes use of resources located in the `test_resources` directory:

- `analyzer.commitanalyzer` package contains all tests related to the Git Commit Analyzer component.
- `analyzer.dataaggregator` package contains all tests related to the Data Aggregator component. This package contains the following classes:
    - MockDataCreator - a helper class that is used to generate mock data
    - MockSampleData - a helper class that uses MockDataCreator to generate sample data for a commit
    - DataAggregatorTest - a class that contains a test driver to test the Data Aggregator.
	The output is then manually inspected to verify that the file format is correct.  
- `analyzer.dependencyanalyzer` package contains all tests related to the Dependency Analyzer component. This package contains the following classes:
    - DependencyAnalyzerHelper - a helper class that is used in DependencyAnalyzerTest tests.
    - DependencyAnalyzerMavenTest - a class that contains tests for a Maven based Dependency Analyzer component.
    - DependencyAnalyzerTest - a class that contains tests for a Java compiler based Dependency Analyzer component.
    - MockXmlParserTest - a class that contains tests for a mock XML parser that has been used in Sprint 1.
    - XmlParserTest - a class that contains tests for an actual implementation of the XML parser.
	In addition, the output of XML parser is also manually checked to see that it matches actual test source code.
- `analyzer.miscstaticanalyzer` package contains all unit tests related to the Misc Static Analyzer component.

Please, see individual classes for more information on test inputs and expected outputs.