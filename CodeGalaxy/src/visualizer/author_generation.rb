###########################################################
# Helper method to generate author ship
###########################################################
def author_ship_gen(author_hash, max_dist) 
    text = "var #{author_hash[:author].gsub(" ", "_")} = new Ship({
    material: new THREE.SpriteMaterial({map: author_texture, color: 0xffffff, fog: true}),
    eta: TIME_INTERVAL,
    his: new History({
        start: new State({
            visible: true,
            destination: #{author_hash[:commits].first.first[:indexed_name]},
            ships: #{author_hash[:commits].first[1..-1].map{|v| v[:indexed_name]}}
        }),
        end: new State({
            visible: true,
            destination: #{author_hash[:commits].last.first[:indexed_name]},
            ships: #{author_hash[:commits].last[1..-1].map{|v| v[:indexed_name]}}
        }),
        states: [\n"
    author_hash[:commits][1..-2].each_with_index do |commit, idx|
        text += "           new State({
                visible: true,
                destination: #{commit.first[:indexed_name]},
                ships: #{commit[1..-1].map{|v| v[:indexed_name]}}
            })#{(idx == (history[1..-2].length - 1))? "" : ","}\n"
    end
    text += "]})});
#{author_hash[:author].gsub(" ", "_")}.initializeAuthor();
scene.add(#{author_hash[:author].gsub(" ", "_")});
#{author_hash[:author].gsub(" ", "_")}.spr.z = #{max_dist[:z] + 0.5};"
    return text
end