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
    authors = {}
    #################################################################
    # Generate the combined history to gather ALL generated objects #
    #################################################################
    combined_present = {}
    val = 0
    commit_keys.each do |key|
        if(authors[data[key]["author"]].nil?)
            authors[data[key]["author"]] = {author: data[key]["author"], commits: [], index: val, colour: COLOURS[authors.length%COLOURS.length]}
        end
        val += 1
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
        p_map["radius"] = (p_map["lines"] <= 0) ? 0 : Math.log10(p_map["lines"])
        planet_dist = p_map["radius"] + 0.5
        total_dist[:z] = (total_dist[:z] < p_map["radius"]) ? p_map["radius"] : total_dist[:z]
        p_map["indexed_name"] = "package_#{p_name.gsub(".", "_")}_#{package_idx}"
        p_map["name"] = p_name
        # Generate the star text
        output += gen_star(p_map)
        ###########################
        # Iterate through classes #
        ###########################
        unless(p_map["classes"].nil?)
            p_map["classes"].each do |c_name, c_map|
                c_map["name"] = c_name
                c_map["package"] = p_map["indexed_name"]
                # Calculate diameter of planet, and keep a reference to the radius allowable for the next moon's orbit
                c_map["radius"] = Math.log10(c_map["lines"])
                moon_dist = c_map["radius"]
                c_map["indexed_name"] = "class_#{c_name.gsub(".", "_")}_#{class_idx}"
                # Create reference name, but not the text for the planet yet (since the orbit around the star is not yet known)
                output += "var #{c_map["indexed_name"]} = new Celestial();\n"
                ###########################
                # Iterate through methods #
                ########################### 
                unless(c_map["methods"].nil?)
                    c_map["methods"].each do |m_name, m_map| 
                        m_map["class"] = c_map["indexed_name"]
                        m_map["name"] = m_name
                        # Calculate diameter of moon
                        m_map["radius"] = Math.log10(m_map["lines"])
                        moon_dist += m_map["radius"] + 0.125
                        # Use previously defined radius for moon orbit, and update the reference
                        m_map["orbit"] = moon_dist
                        moon_dist += m_map["radius"] + 0.125
                        m_map["indexed_name"] = "method_#{m_name.gsub(".", "_")}_#{method_idx}"
                        # Generate text for moon
                        output += gen_moon(m_map)
                        method_idx += 1
                    end
                end
                # Calculate the orbit of the planet, and update the min planetary orbit reference
                planet_dist += moon_dist + 0.125 + c_map["radius"]
                c_map["orbit"] = planet_dist
                planet_dist += c_map["radius"] + moon_dist + 0.125
                # Generate planet text
                output += gen_planet(c_map)
                class_idx += 1
            end
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
        unless(p_map["classes"].nil?)
            p_map["classes"].each do |c_name, c_map|
                # Dependencies might be empty in the yml
                unless(c_map["dependencies"].nil?)
                    unless(c_map["dependencies"]["uses"].nil?)
                        # Iterate through the dependencies
                        c_map["dependencies"]["uses"].each do |d_class, d_map|
                            d_map["indexed_name"] = "dep_#{dep_idx}"
                            d_map["class_indexed_name"] = c_map["indexed_name"]
                            begin
                                d_map["dclass_indexed_name"] = combined_present[d_map["package"]]["classes"][d_class]["indexed_name"]
                                # Generate route text for each dependency
                                output += gen_route(d_map)
                                dep_idx += 1
                            rescue
                                puts "Unable to find class: #{d_class} in #{d_map["package"]}"
                            end
                        end
                    end
                end
                output += "scene.addC(#{c_map["indexed_name"]});\n"
                unless(c_map["methods"].nil?)
                    c_map["methods"].each do |m_name, m_map|
                        output += "scene.addC(#{m_map["indexed_name"]});\n"
                    end
                end
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
                temp["radius"] = (temp["lines"] == 0) ? 0 : Math.log10(temp["lines"])
            end
            p_history.push(temp)
        end
        output += gen_celestial_history(p_map["indexed_name"], p_history, p_map)
        # Iterate through each class
        unless(p_map["classes"].nil?)
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
                unless(c_map["methods"].nil?)
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
        end
    end
    #########################
    # Generate Author Ships #
    #########################
    max_probes = 0
    commit_keys.each do |commit_key|
        temp = []
        gathered = data[commit_key]["modified"]
        if(gathered.nil?)
            gathered = {}
        end
        unless( data[commit_key]["removed"].nil? )
            data[commit_key]["removed"].split("|").each do |key|
                gathered[key] = {lines: 0}
            end
        end
        gathered.each do |key, value|
            path = key.split(".")
            map = nil
            package_key = nil
            idx = path.length-1
            combined_present.keys.each do |p_key|
                path.length.times.with_index do |i|
                    if(path[0..(idx - i)].join(".") == p_key)
                        package_key = p_key
                    end
                end
                unless (package_key.nil?)
                    break
                end
            end
            if(package_key.nil?)
                puts "Error: Package not found. Path given: #{path}"
                return
            end
            map = combined_present[package_key]["classes"][path[idx]]
            temp.push({indexed_name: map["indexed_name"], magnitude: value.values.inject(:+)
            })
        end
        max_probes = (max_probes < temp.length) ? temp.length : max_probes
        authors[data[commit_key]["author"]][:commits].push(temp.sort{|x, y| -(x[:magnitude] <=> y[:magnitude])})
        authors.keys.each do |author_key|
            unless(author_key == data[commit_key]["author"])
                authors[author_key][:commits].push({})
            end
        end
    end
    probes = []
    output += probe_ship_gen(max_probes, probes)
    authors.each_value do |map|
        output += author_ship_gen(map, total_dist, probes)
    end
    output += html_end(total_dist)
    # Generate HTML file
    Dir.chdir("HTML Output")
    File.open("#{filename.gsub(".", "_")}_output.html", 'w') do |new_file|
        new_file.puts output
    end
    puts "#{filename.gsub(".", "_")}_output.html generated at: #{Dir.pwd}"
end