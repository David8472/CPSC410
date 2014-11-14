require 'yaml'
require_relative "html_pieces"
require_relative "system_generation"
require_relative "history_generation"
require_relative "author_generation"

######################
# Helper function to merge hash objects together to retain the most information rather than overwrite
######################
def combination_merge(a, b)
    result = {}
    a.each do |key, value|
        if(value.nil?)
            result[key] = b[key]
        elsif(value.is_a? Hash and b[key].is_a? Hash)
            result[key] = combination_merge(value, b[key])
        elsif(value.is_a? Fixnum and b[key].is_a? Fixnum)
            result[key] = (value > b[key])? value : b[key]
        else
            result[key] = (b[key].nil?) ? value : b[key]
        end
    end
    b.keys.reject{|x| a.keys.include? x}.each do |key|
        result[key] = b[key]
    end
    return result
end

######################
# Main Loop
######################
ARGV.each do |filename|
    # for each file name provided, load the yml and generate the html
    data = YAML.load_file(filename)
    commit_keys = data.keys.sort{|x, y| 
        x.match(/\d+/).to_s.to_i <=> y.match(/\d+/).to_s.to_i
    }
    output = html_start(filename)
    # Keep track of maximum of distance spanned along x or y axis for determining starting camera position
    total_dist = {x: 0, y: 0, z: 0}
    #################################################################
    # Generate the combined history to gather ALL generated objects #
    #################################################################
    combined_present = {}
    commit_keys.each do |key|
        combined_present = combination_merge(combined_present, data[key]["present"]["packages"])
    end
    # Initialize counters at 0
    class_idx = 0
    method_idx = 0
    package_idx = 0
    dep_idx = 0
    ###############################################################################################
    # Iterate through what is present in the combined history, through each package and component #
    ###############################################################################################
    combined_present.each do |p_name, p_map|
        # Calculate diameter of star, and keep a reference to the radius allowable for the next
        # planetary orbit
        p_map["radius"] = Math.log10(p_map["lines"])
        planet_dist = p_map["radius"] + 0.5
        total_dist[:z] = (total_dist[:z] < p_map["radius"]) ? p_map["radius"] : total_dist[:z]
        p_map["indexed_name"] = "#{p_name}_#{package_idx}"
        p_map["name"] = p_name
        # Generate the star text
        output += gen_star(p_map)
        ###########################
        # Iterate through classes #
        ###########################
        p_map["classes"].each do |c_name, c_map|
            c_map["name"] = c_name
            c_map["package"] = p_map["indexed_name"]
            # Calculate diameter of planet, and keep a reference to the radius allowable for the next moon's orbit
            c_map["radius"] = Math.log10(c_map["lines"])
            moon_dist = c_map["radius"]
            c_map["indexed_name"] = "#{c_name}_#{class_idx}"
            # Create reference name, but not the text for the planet yet (since the orbit around the star is not yet known)
            output += "var #{c_name}_#{class_idx} = new Celestial();\n"
            ###########################
            # Iterate through methods #
            ########################### 
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
    ###########################
    # Generate star positions #
    ###########################
    output += gen_star_positions(combined_present, total_dist)
    ##################################################
    # generate trade routes and add objects to scene #
    ##################################################
    combined_present.each do |p_name, p_map|
        output += "scene.addC(#{p_map["indexed_name"]});\n"
        p_map["classes"].each do |c_name, c_map|
            # Dependencies might be empty in the yml
            unless(c_map["dependencies"].nil?)
                # Iterate through the dependencies
                c_map["dependencies"].each do |d_class, d_map|
                    d_map["indexed_name"] = "dep_#{dep_idx}"
                    d_map["class_indexed_name"] = c_map["indexed_name"]
                    d_map["dclass_indexed_name"] = combined_present[d_map["package"]]["classes"][d_class]["indexed_name"]
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
    #########################################
    # Generate History Hash for each object #
    #########################################
    combined_present.each do |p_name, p_map|
        # Top Level History (Packages)
        p_history = []
        commit_keys.each do |commit_num|
            temp = data[commit_num]["present"]["packages"][p_name] rescue nil
            unless(temp.nil?) 
                temp["radius"] = Math.log10(temp["lines"])
            end
            p_history.push(temp)
        end
        output += gen_celestial_history(p_map["indexed_name"], p_history, p_map)
        # Iterate through each class
        p_map["classes"].each do |c_name, c_map|
            # Med Level History (Classes)
            c_history = []
            commit_keys.each do |commit_num|
                temp = data[commit_num]["present"]["packages"][p_name]["classes"][c_name] rescue nil
                unless(temp.nil?) 
                    temp["radius"] = Math.log10(temp["lines"])
                end
                c_history.push(temp)
            end
            output += gen_celestial_history(c_map["indexed_name"], c_history, c_map)
            # Iterate through methods
            c_map["methods"].each do |m_name, m_map|
                # Low Level History (Methods)
                m_history = []
                commit_keys.each do |commit_num|
                    temp = data[commit_num]["present"]["packages"][p_name]["classes"][c_name]["methods"][m_name] rescue nil
                    unless(temp.nil?) 
                        temp["radius"] = Math.log10(temp["lines"])
                    end
                    m_history.push(temp)
                end
                output += gen_celestial_history(m_map["indexed_name"], m_history, m_map)
            end
        end
    end
    #########################
    # Generate Author Ships #
    #########################
    authors = {}
    commit_keys.each do |commit_key|
        if(authors[data[commit_key]["author"]].nil?)
            authors[data[commit_key]["author"]] = {author: data[commit_key]["author"], commits: [], max_ships: 0}
        end
        temp = []
        data[commit_key]["modified"].each do |key, value|
            path = key.split(".")
            map = nil
            case(path.length)
            when 1
                map = combined_present[path[0]]
            when 2
                begin
                    map = combined_present[path[0]]["classes"][path[1]]
                rescue
                    puts "Nil encountered when traversing hash. Path given: #{path}"
                    map = combined_present[path[0]]["classes"][path[1]]
                end
            when 3
                begin
                    map = combined_present[path[0]]["classes"][path[1]]["methods"][path[2]]
                rescue
                    puts "Nil encountered when traversing hash. Path given: #{path}"
                    map = combined_present[path[0]]["classes"][path[1]]["methods"][path[2]]
                end
            end
            temp.push({indexed_name: map["indexed_name"], magnitude: value.values.inject(:+)
            })
        end
        authors[data[commit_key]["author"]][:max_ships] = (authors[data[commit_key]["author"]][:max_ships] < temp.length) ? temp.length : authors[data[commit_key]["author"]][:max_ships]
        authors[data[commit_key]["author"]][:commits].push(temp.sort{|x, y| -(x[:magnitude] <=> y[:magnitude])})
        authors.keys.each do |author_key|
            unless(author_key == data[commit_key]["author"])
                authors[author_key][:commits].push({})
            end
        end
    end
    authors.each_value do |map|
        output += author_ship_gen(map, total_dist)
    end
    output += html_end(total_dist)
    # Generate HTML file
    Dir.chdir("HTML Output")
    File.open("#{filename.gsub(".", "_")}_output.html", 'w') do |new_file|
        new_file.puts output
    end
end