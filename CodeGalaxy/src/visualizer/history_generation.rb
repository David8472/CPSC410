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
                ships: []})
        end
    end
    return map
end

########################################################
# Helper method to generate JS history for one Celestial
########################################################
def gen_celestial_history( celestial_collected_map ) 
    history = celestial_collected_map[:states]
    text = "#{celestial_collected_map[:indexed_name]}.setValues({his: 
    new History({
        start: new State({
                radius: #{history.first[:radius]},
                visible: #{history.first[:present]},
                ships: #{history.first[:ships].to_s}
            }),
        end: new State({
                radius: #{},
                visible: #{},
                ships: #{}
            }),
        states: [\n"
    
    text += "    ]})
});\n"
    return text;
end