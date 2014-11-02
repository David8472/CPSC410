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
<input type=\"button\" id=\"ppbutton\" onClick=\"pause_or_unpause()\" value=\"Pause\"></button>
<input type=\"range\" id=\"speedslider\" onChange=\"update_speed()\" max=7 defaultValue=3></input>
<input type=\"text\" disabled=\"true\" id=\"speedtext\"></input>
<input type=\"button\" id=\"resetb\" onClick=\"restart()\" value=\"Restart\"></button>
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
var speed_val = 1;
var speed_base = Math.PI/360;

var p_button = document.getElementById(\"ppbutton\");
var s_slider = document.getElementById(\"speedslider\");
var s_text = document.getElementById(\"speedtext\");
s_text.value = \"Speed: \" + speed_val;

var pause_or_unpause = function () {
    if(speed_val > 0) {
        speed_val = 0;
        s_text.value = \"Paused\";
        p_button.value = \"Play\";
    } else {
        s_text.value = s_slider.value;
        update_speed();
        p_button.value = \"Pause\";
    }
}

var update_speed = function () {
    if(s_text.value != \"Paused\") {
        speed_val = Math.pow(2, s_slider.value - 3);
        s_text.value = \"Speed: \" + speed_val;
    }
}

var restart = function () {
    t = 0;
}

var index;

cameraControls.target.set(#{max_dist[:x]/2.0},#{max_dist[:y]/2.0},0);
camera.position.set(#{max_dist[:x]/2.0}, #{max_dist[:y]/2.0}, #{(max_dist[:x] > max_dist[:y]) ? max_dist[:x]*0.75 : max_dist[:y]*0.75}); 
cameraControls.update();
var render = function () { 
    requestAnimationFrame(render); 
    for(index = 0; index < celestials.length; index++) {
        celestials[index].updatepos(t, speed_val);
    }
    renderer.render(scene, camera); 
    t += speed_val * speed_base;
}; 
render();
</script>
</body>
</html>"
end