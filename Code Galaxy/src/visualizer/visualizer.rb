require 'yaml'

# First component of the html output
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
<script>
#{(debug)? "var debug = function(text) {
    document.getElementById(\"debug\").innerHTML += text;
    document.getElementById(\"debug\").innerHTML += \"<br>\";
};" : ""}
var Ship = function(parameters) {

    this.origin = false;
    this.start = false;
    this.destination = false;
    this.target = false;
    this.eta = 0;
    this.offset = 0;
    this.material = new THREE.SpriteMaterial( { color: 0xffffff, fog: true} );
    this.spr = new THREE.Sprite(this.material);
    this.loop = false;
    
    this.setValues(parameters);

};

Ship.prototype.setValues = function(values) {
    for ( var key in values) {
        var nValue = values[key];
        if(key in this) {
            if(key == 'material') {
                this[key] = nValue;
                this.spr.material = nValue;
            } else if(key == 'eta' || key == 'offset') {
                this[key] = nValue * Math.PI;
            } else {
                this[key] = nValue;
            }
        }
    }
};

var s_up = new THREE.Vector3(0,1,0);

Ship.prototype.updatepos = function(time) {
    if(this.origin == false) {
        this.origin = this.spr.clone();
    }
    if(this.target != false) {
        if((time > this.eta && this.loop == false) || this.eta <= 0) {
            this.spr.position = this.target.position;
        } else {
            var t = time + this.offset;
            if(this.loop == true)
                t = t % this.eta;
            if(t < 0.01) {
                this.start = this.origin.position.clone();
                this.destination = this.target.projectedpos(time + this.eta);
                var y = this.destination.clone().normalize();
                this.spr.material.rotation = Math.acos(s_up.dot(y));
                if(this.start.x <= this.destination.x) {
                    this.spr.material.rotation = 2*Math.PI - this.spr.material.rotation;
                }
            } 
            this.spr.position.x = this.start.x + (this.destination.x - this.start.x)/this.eta*t;
            this.spr.position.y = this.start.y + (this.destination.y - this.start.y)/this.eta*t;
        }
    }
};

var Celestial = function(parameters) {

    this.geometry = new THREE.SphereGeometry(0.1, 8, 8);
    this.material = new THREE.MeshLambertMaterial({color:0xffffff});
    this.mesh = new THREE.Mesh(this.geometry, this.material);
    this.origin = false;
    
    this.orbitradx = 0;
    this.orbitrady = 0;
    this.orbitradz = 0;
    
    this.orbitoffx = 0;
    this.orbitoffy = 0;
    this.orbitoffz = 0;
    
    this.xsin = true;
    this.ysin = true;
    this.zsin = true;
    
    this.rotx = 0;
    this.roty = 0;
    this.rotz = 0;
    
    this.tfactor = 1;
    this.toffx = 0;
    this.toffy = 0;
    this.toffz = 0;
    
    this.light = false;
    
    this.trade = [];
    
    this.setValues(parameters);

};

Celestial.prototype.setValues = function (values) {
    
    for ( var key in values) {
        var nValue = values[key];
        if(key in this) {
            if(key == 'material') {
                this[key] = nValue;
                this.mesh.material = nValue;
            } else if(key == 'geometry') {
                this[key] = nValue;
                this.mesh.geometry = nValue;
            } else {
                this[key] = nValue;
            }
        }
    }
};

Celestial.prototype.projectedpos = function ( time ) {
    var x = 0;
    var y = 0;
    var z = 0;
    var temp = new THREE.Vector3();
    if(this.origin != false) {
        temp = this.origin.mesh.position;
    }
    if(this.xsin == true) {
        x = temp.x + this.orbitoffx + this.orbitradx * Math.sin(time * this.tfactor + this.toffx);
    } else {
        x = temp.x + this.orbitoffx + this.orbitradx * Math.cos(time * this.tfactor + this.toffx);
    }
    if(this.ysin == true) {
        y = temp.y + this.orbitoffy + this.orbitrady * Math.sin(time * this.tfactor + this.toffy);
    } else {
        y = temp.y + this.orbitoffy + this.orbitrady * Math.cos(time * this.tfactor + this.toffy);
    }
    if(this.zsin == true) {
        z = temp.z + this.orbitoffz + this.orbitradz * Math.sin(time * this.tfactor + this.toffz);
    } else {
        z = temp.z + this.orbitoffz + this.orbitradz * Math.cos(time * this.tfactor + this.toffz);
    }
    return new THREE.Vector3(x, y, z);
};

Celestial.prototype.updatepos = function ( time ) {
    var temp = new THREE.Vector3();
    if(this.origin != false) {
        temp = this.origin.mesh.position;
    }
    if(this.xsin == true) {
        this.mesh.position.x = temp.x + this.orbitoffx + this.orbitradx * Math.sin(time * this.tfactor + this.toffx);
    } else {
        this.mesh.position.x = temp.x + this.orbitoffx + this.orbitradx * Math.cos(time * this.tfactor + this.toffx);
    }
    if(this.ysin == true) {
        this.mesh.position.y = temp.y + this.orbitoffy + this.orbitrady * Math.sin(time * this.tfactor + this.toffy);
    } else {
        this.mesh.position.y = temp.y + this.orbitoffy + this.orbitrady * Math.cos(time * this.tfactor + this.toffy);
    }
    if(this.zsin == true) {
        this.mesh.position.z = temp.z + this.orbitoffz + this.orbitradz * Math.sin(time * this.tfactor + this.toffz);
    } else {
        this.mesh.position.z = temp.z + this.orbitoffz + this.orbitradz * Math.cos(time * this.tfactor + this.toffz);
    }
    this.mesh.rotation.x += this.rotx;
    this.mesh.rotation.y += this.roty;
    this.mesh.rotation.z += this.rotz;
    if(this.light != false) {
        this.light.position = this.mesh.position;
    }
    var i = 0;
    for(i = 0; i < this.trade.length; i++) {
        this.trade[i].updatepos(time);
    }
};

THREE.Scene.prototype.addC = function( celes ) {
    this.add(celes.mesh);
    if(celes.light != false)
        this.add(celes.light);
    var i = 0;
    for(i = 0; i < celes.trade.length; i++) {
        this.add(celes.trade[i].spr);
    }
};

THREE.MOUSE={LEFT:0,MIDDLE:1,RIGHT:2};
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
cameraControls.target.set( 0, 40, 0);
cameraControls.maxDistance = 1500;
cameraControls.minDistance = 10;
cameraControls.update();\n\n"
end

def html_end(z)
    return "var t = 0;
var index;

camera.position.z = #{z}; 
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

def gen_planet(package_name, class_name, class_map, package_idx, class_idx)
    class_map["colour"] = (class_map["type"] == "full")? "0x00ff00" : ((class_map["type"] == "abstract") ? "0x0000ff" : "0xffff00")
    text = "//#{package_name} class: #{class_name}
#{class_name}_#{class_idx}.setValues({
    geometry: new THREE.SphereGeometry(#{class_map["radius"]}, 10, 10),
    material: new THREE.MeshLambertMaterial({emissive: 0x666666, color: #{class_map["colour"]}, map: planet_texture}),
    origin: #{package_name}_#{package_idx}, 
    orbitradx: #{(Random.rand(2) == 0)? "" : "-"}#{class_map["off"]}, 
    orbitrady: #{(Random.rand(2) == 0)? "" : "-"}#{class_map["off"]}, 
    #{(Random.rand(2) == 0)? "ysin: false" : "xsin:false"},
    rotx: 0.01, 
    roty: 0.01, 
    rotz: 0.01,
    tfactor: #{1 + Random.rand * 4/(class_idx + 1)}});
celestials[celestials.length] = #{class_name}_#{class_idx};\n\n"
    return text
end

def gen_moon(class_name, method_name, method_map, class_idx, method_idx)
    text = "//#{class_name} function: #{method_name}
var #{method_name}_#{method_idx} = new Celestial({
    geometry: new THREE.SphereGeometry(#{method_map["radius"]}, 8, 8),
    material: new THREE.MeshLambertMaterial({emissive: 0x666666, color: 0xbb8800, map: planet_texture}),
    origin: #{class_name}_#{class_idx},
    orbitradx: #{(Random.rand(2) == 0)? "" : "-"}#{method_map["off"]}, 
    orbitrady: #{(Random.rand(2) == 0)? "" : "-"}#{method_map["off"]}, 
    #{(Random.rand(2) == 0)? "ysin: false" : "xsin:false"},
    rotx: 0.01, 
    roty: 0.01, 
    rotz: 0.01,
    tfactor: #{2 + Random.rand * 2}});
celestials[celestials.length] = #{method_name}_#{method_idx};\n\n"
    return text
end

def gen_star(package_name, package_map, package_idx)
    text = "//#{package_name}
var #{package_name}_#{package_idx} = new Celestial({
    geometry: new THREE.SphereGeometry(#{package_map["radius"]}, 12, 12),
    material: new THREE.MeshBasicMaterial({color: 0xffdd22, map: star_texture}),
    light: new THREE.PointLight( 0xffddbb, 1, 1000 ),
    rotx: 0.01, 
    roty: 0.01, 
    rotz: 0.01});
celestials[celestials.length] = #{package_name}_#{package_idx};\n\n"
    return text
end

def gen_route(class_map, target_map, dep_idx, dep_map)
    text = "// Dependency Route: #{class_map["indexed_name"]} -> #{target_map["indexed_name"]}\n"
    temp = 0
    eta = 0.15
    while(temp < dep_map["strength"])
        text += "var dep_#{dep_idx}_#{temp} = new Ship({
        material: new THREE.SpriteMaterial({map: trade_texture, color: 0xffffff, fog: true}),
        origin: #{class_map["indexed_name"]}.mesh,
        target: #{target_map["indexed_name"]},
        offset: #{eta*temp/dep_map["strength"]},
        eta: #{eta},
        loop: true});
#{class_map["indexed_name"]}.trade[#{class_map["indexed_name"]}.trade.length] = dep_#{dep_idx}_#{temp};
dep_#{dep_idx}_#{temp}.spr.scale.set(2,2,1);\n\n"
        temp += 1
    end
    return text
end

ARGV.each do |filename|
    data = YAML.load_file(filename)
    output = html_start(filename)
    total_dist = 0;
    data.each do |commit_key, commit_map|
        # TODO incorporate commit history into a full animation
        # Consider using a merged hash object to determine all created objects
        class_idx = 0
        method_idx = 0
        package_idx = 0
        dep_idx = 0
        commit_map["present"]["packages"].each do |p_name, p_map|
            # Calculate diameter of star
            p_map["radius"] = Math.log10(p_map["lines"])
            planet_dist = p_map["radius"] + 0.5
            p_map["indexed_name"] = "#{p_name}_#{package_idx}"
            output += gen_star(p_name, p_map, package_idx)
            p_map["classes"].each do |c_name, c_map|
                # Calculate diameter of planet
                c_map["radius"] = Math.log10(c_map["lines"])
                moon_dist = c_map["radius"]
                c_map["indexed_name"] = "#{c_name}_#{class_idx}"
                output += "var #{c_name}_#{class_idx} = new Celestial();\n"
                c_map["methods"].each do |m_name, m_map| 
                    # Calculate diameter of moon
                    m_map["radius"] = Math.log10(m_map["lines"])
                    moon_dist += m_map["radius"] + 0.05
                    m_map["off"] = moon_dist
                    moon_dist += m_map["radius"] + 0.05
                    m_map["indexed_name"] = "#{m_name}_#{method_idx}"
                    output += gen_moon(c_name, m_name, m_map, class_idx, method_idx)
                    method_idx += 1
                end
                planet_dist += moon_dist + 0.075
                c_map["off"] = planet_dist
                planet_dist += moon_dist + 0.075
                output += gen_planet(p_name, c_name, c_map, package_idx, class_idx)
                class_idx += 1
            end
            total_dist += planet_dist * 1.5
            package_idx += 1
        end
        # generate trade routes and add objects to scene
        commit_map["present"]["packages"].each do |p_name, p_map|
            output += "    scene.addC(#{p_map["indexed_name"]});\n"
            p_map["classes"].each do |c_name, c_map|
                unless(c_map["dependencies"].nil?)
                    c_map["dependencies"].each do |d_class, d_map|
                        d_map["index"] = dep_idx
                        output += gen_route(c_map, p_map["classes"][d_class], dep_idx, d_map)
                        dep_idx += 1
                    end
                end
                output += "    scene.addC(#{c_map["indexed_name"]});\n"
                c_map["methods"].each do |m_name, m_map|
                    output += "    scene.addC(#{m_map["indexed_name"]});\n"
                end
                
            end
            
        end
        # remove break when using full history
        break
    end
    output += html_end(total_dist)
    File.open("HTML Output\\#{filename.gsub(".", "_")}_output.html", 'w') do |new_file|
        new_file.puts output
    end
end