###########################################################
# Helper method to generate ship intervals
###########################################################
def ship_array_gen(cur_dep_map, max_dep_map) 
    ships = []
    i = 0
    max_dep_map.each do |dep_name, dep_map|
        if(cur_dep_map.keys.include? dep_name)
            str = cur_dep_map[dep_name]["strength"]
            count = str
            x = 0
            while( count > 0 and x < str)
                ships.push(i + x)
                x += 2
                count -= 1
            end
            x = 1
            while( count > 0 and x < str)
                ships.push(i + x)
                x += 2
                count -= 1
            end
        end
        i += dep_map["strength"]
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
                ships: []})
        else
            map[:states].push({radius: commit["radius"],
                present: true,
                ships: ship_array_gen(commit["dependencies"], max_version["dependencies"])})
        end
    end
    return map
end

########################################################
# Helper method to generate JS history for one Celestial
########################################################
def gen_celestial_history( celestial_history_map ) 
    history = celestial_history_map[:states]
    text = "#{celestial_history_map[:indexed_name]}.setValues({his: 
    new History({
        start: new State({
                radius: #{history.first[:radius]},
                visible: #{history.first[:present]},
                ships: #{history.first[:ships]}
            }),
        end: new State({
                radius: #{history.first[:radius]},
                visible: #{history.first[:present]},
                ships: #{history.last[:ships]}
            }),
        states: [\n"
    history[1..-2].each do |state|
        text += "           new State({
                radius: #{state[:radius]},
                visible: #{state[:present]},
                ships: #{state[:ships]}
            })#{(state == history[1..-2].last)? "" : ","}\n"
    end
    text += "    ]})
});\n"
    return text;
end