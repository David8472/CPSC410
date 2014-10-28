require 'yaml'
require_relative "html_pieces"
require_relative "system_generation"

######################
# Main Loop
######################
ARGV.each do |filename|
    # for each file name provided, load the yml and generate the html
    data = YAML.load_file(filename)
    output = html_start(filename)
    # Keep track of maximum of distance spanned along x or y axis for determining starting camera position
    total_dist = {x: 0, y: 0}
    data.each do |commit_key, commit_map|
        # TODO incorporate commit history into a full animation
        # Consider using a merged hash object to determine all created objects
        # Initialize counters at 0
        class_idx = 0
        method_idx = 0
        package_idx = 0
        dep_idx = 0
        # Iterate through what is present in the commit, through each package and component
        commit_map["present"]["packages"].each do |p_name, p_map|
            # Calculate diameter of star, and keep a reference to the radius allowable for the next
            # planetary orbit
            p_map["radius"] = Math.log10(p_map["lines"])
            planet_dist = p_map["radius"] + 0.5
            p_map["indexed_name"] = "#{p_name}_#{package_idx}"
            p_map["name"] = p_name
            # Generate the star text
            output += gen_star(p_map)
            # Iterate through classes
            p_map["classes"].each do |c_name, c_map|
                c_map["name"] = c_name
                c_map["package"] = p_map["indexed_name"]
                # Calculate diameter of planet, and keep a reference to the radius allowable for the next moon's orbit
                c_map["radius"] = Math.log10(c_map["lines"])
                moon_dist = c_map["radius"]
                c_map["indexed_name"] = "#{c_name}_#{class_idx}"
                # Create reference name, but not the text for the planet yet (since the orbit around the star is not yet known)
                output += "var #{c_name}_#{class_idx} = new Celestial();\n"
                # Iterate through methods
                c_map["methods"].each do |m_name, m_map| 
                    m_map["class"] = c_map["indexed_name"]
                    m_map["name"] = m_name
                    # Calculate diameter of moon
                    m_map["radius"] = Math.log10(m_map["lines"])
                    moon_dist += m_map["radius"] + 0.125
                    # Use previously defined radius for moon orbit, and update the reference
                    m_map["orbit"] = moon_dist
                    moon_dist += m_map["radius"] + 0.125
                    m_map["indexed_name"] = "#{m_name}_#{method_idx}"
                    # Generate text for moon
                    output += gen_moon(m_map)
                    method_idx += 1
                end
                # Calculate the orbit of the planet, and update the min planetary orbit reference
                planet_dist += moon_dist + 0.125 + c_map["radius"]
                c_map["orbit"] = planet_dist
                planet_dist += c_map["radius"] + moon_dist + 0.125
                # Generate planet text
                output += gen_planet(c_map)
                class_idx += 1
            end
            p_map["orbit_radius"] = planet_dist
            package_idx += 1
        end
        # Generate star positions
        output += gen_star_positions(commit_map["present"]["packages"], total_dist)
        # generate trade routes and add objects to scene
        commit_map["present"]["packages"].each do |p_name, p_map|
            output += "scene.addC(#{p_map["indexed_name"]});\n"
            p_map["classes"].each do |c_name, c_map|
                # Dependencies might be empty in the yml
                unless(c_map["dependencies"].nil?)
                    # Iterate through the dependencies
                    c_map["dependencies"].each do |d_class, d_map|
                        d_map["indexed_name"] = "dep_#{dep_idx}"
                        d_map["class_indexed_name"] = c_map["indexed_name"]
                        d_map["dclass_indexed_name"] = commit_map["present"]["packages"][d_map["package"]]["classes"][d_class]["indexed_name"]
                        # Generate route text for each dependency
                        output += gen_route(d_map)
                        dep_idx += 1
                    end
                end
                output += "scene.addC(#{c_map["indexed_name"]});\n"
                c_map["methods"].each do |m_name, m_map|
                    output += "scene.addC(#{m_map["indexed_name"]});\n"
                end
                
            end
            
        end
        # remove break after implementing the use of the full history
        break
    end
    output += html_end(total_dist)
    # Generate HTML file
    File.open("HTML Output\\#{filename.gsub(".", "_")}_output.html", 'w') do |new_file|
        new_file.puts output
    end
end