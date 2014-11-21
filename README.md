CPSC 410 Project - Code Galaxy
=======
By Programmers of the Galaxy
----------------------------
Contributors:
- CS-Raela
- David8472
- ellina-s
- kevalshah

Overview
--------

Code Galaxy creates a dynamic space-themed visualization of a Git repository containing Java projects by analyzing the Git repository commit by commit. The output is in the form of an HTML file to be displayed in a browser. 

In the visualization, starship(s) representing author(s) are exploring a codebase represented by a galaxy with the following mappings:
- Packages are represented as stars
- Classes within packages are represented as planets orbiting stars
- Methods within classes are represented as moons orbiting planets
- Authors adding new packages/classes/methods are represented as starships exploring the galaxy and discovering new stars/planets/moons
- Authors editing previously committed classes/methods are represented as starships visiting previously discovered planets/moons
- Dependencies between classes are represented as trade routes between planets where tiny trade ships will be moving from planet to planet 

Initially, before any commits are made, the galaxy will be empty. Gradually, as commits are made and packages/classes/methods are added, parts of the galaxy will start to appear as celestial objects are “discovered” by ship(s) that represent authors. If an author interacts with more than one object during a commit, the spaceship will visit one celestial body and send probes to the others. Finally, when all commits are traced through, the entire galaxy will be visible, and the current state of the codebase is reached.

Analysis Performed (Per Git Commit)
------------------
- Git commit meta properties: 
  - Author of commit
  - List of Java source files changed and the amount of changes in terms of LOC 
  - List of Java source files created
  - List of Java source files removed
- Package information: 
  - Package names
  - LOC (excluding comments and blank lines)
  - Package dependencies
- Class information: 
  - Container package
  - Class names
  - Class types (concrete, abstract, interface)
  - LOC (excluding comments and blank lines)
  - Class dependencies
  - Method names 
- Method information: 
  - Container class
  - Method names
  - LOC (excluding comments and blank lines)
  - Method accessor type (public, private, protected)
  
  
How to run the project
--------------
1. Ensure the following is installed first: Ruby, Java, and Maven.
    - Additional instructions for Windows: Ensure that the ruby/bin directory and the maven/bin directory are on the machine’s system path.
2. Run `GlobalController.java` and enter the path to a git repo stored on the local machine.
    - NOTE: It is important that the git repo selected uses Maven as its build system for the Java project. It is also assumed that a Maven instructions file is named “pom.xml”. Other git repos that use other build systems or do not use any build system are currently not supported.
3. The visualization output will be generated in an HTML file in the `HTML_Output` directory in the visualizer directory (location and name of HTML file will be provided in the console as well if needed), which the user can then manually open in a browser.
    - NOTE: Currently the visualization HTML works on Firefox. To open in Chrome, a new shortcut needs to be used that invokes the --allow-file-access-from-files option.

Libraries / Tools Used
--------------
- Classycle - http://classycle.sourceforge.net/
- JavaParser - https://code.google.com/p/javaparser/
- JGit - http://www.eclipse.org/jgit/
- three.js - http://threejs.org/
- SAX API - http://sax.sourceforge.net/
