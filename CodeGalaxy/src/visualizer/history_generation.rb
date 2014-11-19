###########################################################
# Helper method to generate ship intervals
###########################################################
def ship_array_gen(cur_dep_map, max_dep_map) 
    ships = []
    unless(cur_dep_map.nil?)
        i = 0
        max_dep_map.each do |dep_name, dep_map|
            if(cur_dep_map.keys.include? dep_name)
                if(cur_dep_map[dep_name]["strength"].nil?)
                    cur_dep_map[dep_name]["strength"] = 7
                end
                count = cur_dep_map[dep_name]["strength"]
                x = 0
                while( count > 0 and x < dep_map["strength"])
                    ships.push(i + x)
                    x += 2
                    count -= 1
                end
                x = 1
                while( count > 0 and x < dep_map["strength"])
                    ships.push(i + x)
                    x += 2
                    count -= 1
                end
            end
            i += dep_map["strength"]
        end
    end
    return ships
end

###########################################################
# Helper method to collect history into map for a celestial
###########################################################
def gen_celestial_map( cel_indexed_name, commits, max_version ) 
    map = {}
    map[:indexed_name] = cel_indexed_name
    map[:states] = []
    commits.each do |commit|
        if(commit.nil?)
            map[:states].push({radius: 0, 
                present: false, 
                colour: nil,
                ships: []})
        else
            map[:states].push({radius: commit["radius"],
                present: true,
                colour: (commit["type"].nil?) ? nil : PLANET_COLOURS[commit["type"]],
                ships: ship_array_gen((commit["dependencies"]["uses"] rescue nil), (max_version["dependencies"]["uses"] rescue Hash.new))})
        end
    end
    return map
end

########################################################
# Helper method to generate JS history for one Celestial
########################################################
def gen_celestial_history( cel_indexed_name, commits, max_version ) 
    celestial_history_map = gen_celestial_map(cel_indexed_name, commits, max_version)
    history = celestial_history_map[:states]
    text = "#{celestial_history_map[:indexed_name]}.setValues({his: 
    new History({
        start: new State({
                radius: #{history.first[:radius]},
                visible: #{history.first[:present]},
                colour: #{(history.first[:colour].nil?)? false : history.first[:colour]},
                ships: #{history.first[:ships]}
            }),
        end: new State({
                radius: #{history.last[:radius]},
                visible: #{history.last[:present]},
                colour: #{(history.last[:colour].nil?)? false : history.last[:colour]},
                ships: #{history.last[:ships]}
            }),
        states: [\n"
    history[1..-2].each_with_index do |state, idx|
        text += "           new State({
                radius: #{state[:radius]},
                visible: #{state[:present]},
                colour: #{(state[:colour].nil?)? false : state[:colour]},
                ships: #{state[:ships]}
            })#{(idx == (history[1..-2].length - 1))? "" : ","}\n"
    end
    text += "    ]})
});\n"
    return text;
end