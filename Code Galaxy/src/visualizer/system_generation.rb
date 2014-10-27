##################################
# Helper method to generate planets
##################################
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

##################################
# Helper method to generate moons
##################################
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

##################################
# Helper method to generate stars
##################################
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

##################################
# Helper method to generate routes
##################################
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