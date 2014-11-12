#########################################
# Hash object to hold colour references #
#########################################
COLOURS = Hash.new("0xffffff")
COLOURS["full"] = "0x00ff00"
COLOURS["abstract"] = "0x4444ff"
COLOURS["interface"] = "0xff8844"

##################################
# Helper method to generate planets
##################################
def gen_planet(class_map)
    class_map["colour"] = COLOURS[class_map["type"]]
    text = "//#{class_map["package"]} class: #{class_map["name"]}
#{class_map["indexed_name"]}.setValues({
    geometry: new THREE.SphereGeometry(#{class_map["radius"]}, 10, 10),
    material: new THREE.MeshLambertMaterial({emissive: 0x666666, color: #{class_map["colour"]}, map: planet_texture}),
    origin: #{class_map["package"]}, 
    orbitradx: #{(Random.rand(2) == 0)? "" : "-"}#{class_map["orbit"]}, 
    orbitrady: #{(Random.rand(2) == 0)? "" : "-"}#{class_map["orbit"]}, 
    #{(Random.rand(2) == 0)? "ysin: false" : "xsin:false"},
    rotx: 0.01, 
    roty: 0.01, 
    rotz: 0.01,
    tfactor: #{1 + Random.rand}});
celestials[celestials.length] = #{class_map["indexed_name"]};\n\n"
    return text
end

##################################
# Helper method to generate moons
##################################
def gen_moon(method_map)
    text = "//#{method_map["class"]} function: #{method_map["name"]}
var #{method_map["indexed_name"]} = new Celestial({
    geometry: new THREE.SphereGeometry(#{method_map["radius"]}, 8, 8),
    material: new THREE.MeshLambertMaterial({emissive: 0x666666, color: 0xbb8800, map: planet_texture}),
    origin: #{method_map["class"]},
    orbitradx: #{(Random.rand(2) == 0)? "" : "-"}#{method_map["orbit"]}, 
    orbitrady: #{(Random.rand(2) == 0)? "" : "-"}#{method_map["orbit"]}, 
    #{(Random.rand(2) == 0)? "ysin: false" : "xsin:false"},
    rotx: 0.01, 
    roty: 0.01, 
    rotz: 0.01,
    tfactor: #{2 + Random.rand * 2}});
celestials[celestials.length] = #{method_map["indexed_name"]};\n\n"
    return text
end

##################################
# Helper method to generate stars
##################################
def gen_star(package_map)
    text = "//#{package_map["name"]}
var #{package_map["indexed_name"]} = new Celestial({
    geometry: new THREE.SphereGeometry(#{package_map["radius"]}, 12, 12),
    material: new THREE.MeshBasicMaterial({color: 0xffdd22, map: star_texture}),
    light: new THREE.PointLight( 0xffddbb, 1, 1000 ),
    rotx: 0.01, 
    roty: 0.01, 
    rotz: 0.01});
celestials[celestials.length] = #{package_map["indexed_name"]};\n\n"
    return text
end

##################################
# Helper method to generate routes
##################################
def gen_route(dep_map)
    text = "// Dependency Route: #{dep_map["class_indexed_name"]} -> #{dep_map["dclass_indexed_name"]}\n"
    temp = 0
    eta = 0.15
    while(temp < dep_map["strength"])
        text += "var #{dep_map["indexed_name"]}_#{temp} = new Ship({
        material: new THREE.SpriteMaterial({map: trade_texture, color: 0xffffff, fog: true}),
        origin: #{dep_map["class_indexed_name"]}.mesh,
        target: #{dep_map["dclass_indexed_name"]},
        offset: #{eta*temp/dep_map["strength"]},
        eta: #{eta},
        loop: true});
#{dep_map["class_indexed_name"]}.trade[#{dep_map["class_indexed_name"]}.trade.length] = #{dep_map["indexed_name"]}_#{temp};
#{dep_map["indexed_name"]}_#{temp}.spr.scale.set(2,2,1);\n\n"
        temp += 1
    end
    return text
end

#################################
# Helper method to generate star positions
#################################
def gen_star_positions(packages, max_dist_map)
    size = Math.sqrt(packages.length).ceil
    cols = Array.new(size, 0)
    rows = Array.new(size, 0)
    for i in 0..size
        for j in 0..size
            temp_package = packages.values[size*i+j]
            unless(temp_package.nil?)
                cols[j] = (cols[j] < temp_package["orbit_radius"] * 2) ? temp_package["orbit_radius"] * 2 : cols[j]
                rows[i] = (rows[i] < temp_package["orbit_radius"] * 2) ? temp_package["orbit_radius"] * 2 : rows[i]
            end
        end
    end
    text = ""
    for i in 0..size
        for j in 0..size
            temp_package = packages.values[size*i+j]
            unless(temp_package.nil?)
                sum_x = (j > 0)? cols[0..(j-1)].inject{|sum, x| sum + x} : 0
                sum_y = (i > 0)? rows[0..(i-1)].inject{|sum, y| sum + y} : 0
                text += "#{temp_package["indexed_name"]}.mesh.position.x = #{sum_x + (cols[j]/2.0)}\n"
                text += "#{temp_package["indexed_name"]}.mesh.position.y = #{sum_y + (rows[i]/2.0)}\n"
            end
        end
    end
    max_dist_map[:x] = cols.inject{|sum, x| sum + x}
    max_dist_map[:y] = rows.inject{|sum, y| sum + y}
    return text
end