#Hierarchical Structure

- **classycle/** - Contains the Classycle tool
    
- **libs/** - Contains all library jar files used
- **src/** - Contains all the source files
   - **analyzer/** -  represents the code analyzer aspect of the project (Java)
   - **fuser/** - represents the data aggregation aspect of the project where data from the analyzer is combined and written to a YAML file for the visualizer to utilize (Java)
   - **visualizer/** - represents the data visualizer aspect of the project (Ruby)
   - **main/** - represents the global controller and contains the entry point to the project (Java)
   
- **scripts/** - Contains scripts (batch file and bash script) to run the visualizer ruby script from the global controller
- **test_resources/** - Contains test resources used by tests in **tests** directory
    
- **tests/** - Contains all unit tests, integration tests, and manual tests for analyzer and data aggregator component. More detailed test documentation is provided in a README outlining the different tests that were performed.
