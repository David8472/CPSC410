#####################################
# First component of the html output
#####################################
def html_start (title)
    debug = true
    return "<html>
<head>
<title>#{title}</title>
<style>canvas { width: 100%; height: 100% }</style>
</head>
<body>
#{(debug)? "<div id=\"debug\"></div>" : ""}
<script src=\"js/three.min.js\"></script>
<script src=\"js/OrbitControls.js\"></script>
<script src=\"js/Galaxy.js\"></script>
<script>
#{(debug)? "var debug = function(text) {
    document.getElementById(\"debug\").innerHTML += text;
    document.getElementById(\"debug\").innerHTML += \"<br>\";
};" : ""}

var scene = new THREE.Scene(); 
var camera = new THREE.PerspectiveCamera(75, window.innerWidth/window.innerHeight, 1, 1000); 
var renderer = new THREE.WebGLRenderer(); 
renderer.setSize(window.innerWidth, window.innerHeight); 
document.body.appendChild(renderer.domElement); 
var planet_texture = THREE.ImageUtils.loadTexture('textures/planet.jpg');
var star_texture = THREE.ImageUtils.loadTexture('textures/star.jpg');
var trade_texture = THREE.ImageUtils.loadTexture('textures/trade_ship.png');
var celestials = [];\n\n

var cameraControls = new THREE.OrbitControls(camera, renderer.domElement);
cameraControls.maxDistance = 1500;
cameraControls.minDistance = 10;\n\n"
end

####################################
# Last component of the html output
####################################
def html_end(max_dist)
    return "var t = 0;
var index;

cameraControls.target.set(#{max_dist[:x]/2.0},#{max_dist[:y]/2.0},0);
camera.position.set(#{max_dist[:x]/2.0}, #{max_dist[:y]/2.0}, #{(max_dist[:x] > max_dist[:y]) ? max_dist[:x]*0.75 : max_dist[:y]*0.75}); 
cameraControls.update();
var render = function () { 
    requestAnimationFrame(render); 
    for(index = 0; index < celestials.length; index++) {
        celestials[index].updatepos(t);
    }
    renderer.render(scene, camera); 
    t += Math.PI/360;
}; 
render();
</script>
</body>
</html>"
end