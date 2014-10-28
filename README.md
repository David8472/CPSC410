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
- Methods within classes are represented as satellites orbiting planets
- Authors adding new packages/classes/methods are represented as starships exploring the galaxy and discovering new stars/planets/moons
- Authors editing previously committed classes/methods are represented as starships visiting previously discovered planets/moons
- Dependencies between packages are represented as trade routes between systems

Initially, before any commits are made, the galaxy is covered in fog. Gradually, as commits are made and packages/classes/methods are added, the fog lifts as their corresponding celestial objects are “discovered” by ship(s). If an author interacts with more than one object during a commit, the spaceship will visit one object and send probes to the others. Finally, when all commits are traced through, the fog is cleared, and the current state of the codebase is reached.

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

Libraries / Tools Used
--------------
- Classycle - http://classycle.sourceforge.net/
- JavaParser - https://code.google.com/p/javaparser/
- JGit - http://www.eclipse.org/jgit/
- three.js - http://threejs.org/



