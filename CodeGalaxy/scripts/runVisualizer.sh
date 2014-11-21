#! /bin/bash

echo 'Running Visualizer...'

cd src/visualizer

chmod u+x visualizer.rb
ruby visualizer.rb $1
