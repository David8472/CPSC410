====================================
    Visualizer Details
====================================
    Table of Contents
------------------------------------

    1. Description
    2. Requirements
    3. Challenges
        3a. D3 and Three.js
        3b. Orbits
        3c. Star Positioning
        3d. Ship Pathing
        3e. History
        3f. Author Ships
        3g. Author Probes
    4. Tests
    5. References Used

====================================
    1. Description
====================================

This visualizer is a Ruby program that takes in data provided by an external 
source and creates an html file utilizing Three.js to provide a galaxy-like
display. 

Specifically, the Ruby constructs a String using the given information,
and then prints this String into a new file using HTML format.

The core is visualizer.rb, which takes in the arguments provided, and 
uses various helper functions from the other *.rb files.

====================================
    2. Requirements
====================================

As this visualizer is written in Ruby, Ruby v1.93 or later is required.
The visualizer requires information to be provided in a given format,
based by commit history. (see sample.yml for an example)

To run from command line:
ruby visualizer.rb <path to yml>

More than one yml may be given

====================================
    3. Challenges
====================================
    3a. D3 and Three.js
------------------------------------

The first challenge that came up in this component was in figuring out 
what platform would allow for realization of the dynamic galaxy vision.
The requirements of this vision included not only display of the planets,
but also interaction between planets, potentially between different systems.

While D3 provides a good platform to simulate charts, the visualization
desired has a higher degree of complexity than can be provided just by
charts. Hence, an actual visualization-focused framework was needed instead.

Three.js has the additional benefit of being three-dimensional, which affords
for the three dimensional nature of a galaxy.

The challenge specifically was in learning Three.js as well as JavaScript.

------------------------------------
    3b. Orbits
------------------------------------

The next challenge was in making spherical objects that move in relation 
to each other. As this involves animation, it was clear that there would need
to be a time reference in JavaScript, put in an infinite loop.

Ultimately, the choice was done to use a combination of sin and cos functions.
As both functions are continuous, they are valid for all time values, and 
the oscillation combination results in an elliptical motion.

In addition, positioning had to be taken into account, to prevent orbital 
collision between planets or moons.

------------------------------------
    3c. Star Positioning
------------------------------------

Up to this point, experimentation was done only with a single star system.
However, code bases typically involve more than one package. Consequently, 
an algorithm to determine the positioning of stars such that there would be
no collision between the respective systems.

The result was to refer to the orbit limit of the system, and arrange stars
in a grid pattern, with the width and height of the grid corresponding to the
orbit limits.

------------------------------------
    3d. Ship Pathing
------------------------------------

Another issue that arose was to correctly generate the movement of various
ships between planets. In preparation for this, a helper method to determine
a Celestial type's projected position was made. However, it still took effort
to correctly make a ship travel the intended route, including looping back to 
the beginning if desired.

------------------------------------
    3e. History
------------------------------------

The implementation of change over time was another major choice to be made.
There were two main options:
- implement history as a standalone with a reference to everything on screen
- implement history as an array of states, with each thing only updating itself

The latter was chosen in order to reduce the coupling between history and 
everything else. By making it as an array of states, it was also much easier to
provide the generation code for. In retrospect it is a fairly apparent choice,
but it did take a long time to make that choice.

------------------------------------
    3f. Author Ships
------------------------------------

Unlike the route ships which loop a preset path, the author ships need to
traverse from one planet to another, where the order of planets visited is not
known. There were several cases to consider when combining the history
capability with the ship type, which lead to the implementation being more
complicated than had been anticipated.

------------------------------------
    3g. Author Probes
------------------------------------

Similar to the route ships, the probes were meant to travel from one point to
a specified object. However, in implementation, it was found that they were
not all going to the correct location. After a while it was found that the 
error was in the projected position helper method - it correctly projected the 
position of the object, but did not take into account if the origin was moving
as was in the case of a moon that orbits a planet (which is also orbiting a 
star).

====================================
    4. Tests
====================================

The sample.yml was the main mock file used for the duration of development.
The contents grew more complex over time, to match the increasing functionality
being programmed. 

Initial State:
- One Package
- One Class
- One Method
Test for: 
- Correct movement of the orbits
- Correct positioning to avoid visual collisions

Advanced State:
- Two Packages
- Multiple Classes (varying sizes)
- Multiple Methods (varying sizes)
Test for:
- Correct movement of the orbits
- Correct positioning to avoid visual collisions
- Correct ship route path to show dependencies

With History:
- As Advanced State, but incrementally adding more per commit
Test for:
- Appearance of objects over time

With Authors and Author Probes:
- As 'With History'
Test for:
- Correct author ship movement between planets
- Correct probe ship movement between planets
- Correct probe ship setup at the interval for new commit

====================================
    5. References Used
====================================

Ruby Documentation:
http://ruby-doc.org

Three.js Documentation:
http://threejs.org/docs/

Inspiration to use cos/sin for orbit,
as well as source for camera controls:
http://threejs.org/examples/#webgl_mirror
