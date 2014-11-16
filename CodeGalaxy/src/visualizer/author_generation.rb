###########################################################
# Helper method to generate author ship
###########################################################
def author_ship_gen(author_hash, max_dist, probe_list) 
    text = "// Author ship for #{author_hash[:author]}
var #{author_hash[:author].gsub(" ", "_")} = new Ship({
    material: new THREE.SpriteMaterial({map: author_texture, color: 0xffffff, fog: true}),
    his: new History({
        start: new State({\n"
    if(author_hash[:commits].first.empty?)
        text += "            visible: false\n"
    else
        text += "            visible: true,
            destination: #{author_hash[:commits].first.first[:indexed_name]},
            ships: [#{(author_hash[:commits].first[1..-1].map{|v| v[:indexed_name]}).join(", ")}]\n"
    end
    text += "        }),
        end: new State({\n"
    if(author_hash[:commits].last.empty?)
        text += "            visible: false\n"
    else
        text += "            visible: true,
            destination: #{author_hash[:commits].last.first[:indexed_name]},
            ships: [#{(author_hash[:commits].last[1..-1].map{|v| v[:indexed_name]}).join(", ")}]\n"
    end
    text += "        }),
        states: [\n"
    author_hash[:commits][1..-2].each_with_index do |commit, idx|
        text += "           new State({\n"
        if(commit.empty?)
            text += "                visible: false\n"
        else
            text += "                visible: true,
                destination: #{commit.first[:indexed_name]},
                ships: [#{(commit[1..-1].map{|v| v[:indexed_name]}).join(", ")}]\n"
        end
        text += "            })#{(idx == (author_hash[:commits][1..-2].length - 1))? "" : ","}\n"
    end
    text += "]})});
authors[authors.length] = #{author_hash[:author].gsub(" ", "_")};
scene.add(#{author_hash[:author].gsub(" ", "_")}.spr);
#{author_hash[:author].gsub(" ", "_")}.initializeAuthor(#{max_dist[:z] + 1});
#{author_hash[:author].gsub(" ", "_")}.spr.scale.set(4,4,4);\n\n"
    return text
end

###########################################################
# Helper method to generate probes
###########################################################
def probe_ship_gen(probe_count, probe_array)
    text = ""
    probe_count.times.with_index do |v|
        temp = ("a".."zz").to_a[v]
        text += "var author_probe_#{temp} = new Ship({
    material: new THREE.SpriteMaterial({map: probe_texture, color: 0xffffff, fog: true}),
    dis_at_end: true
});
scene.add(author_probe_#{temp}.spr);
probes[probes.length] = author_probe_#{temp};\n\n"
        probe_array.push("author_probe_#{temp}")
    end
    return text
end