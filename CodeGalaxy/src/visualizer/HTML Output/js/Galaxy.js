/**
* @author Raela
*/

/**
* History definition
* Encapsulation of a number of states, including special states 'start' and 'end'
*/
var TIME_INTERVAL = 200;
var History = function(parameters) {
    this.start = false;
    this.end = false;
    this.states = [];
    
    this.setValues(parameters);
};

History.prototype.getState = function(time) {
    if(time <= TIME_INTERVAL) {
        return this.start;
    } else if(time > (this.states.length+1)*TIME_INTERVAL) {
        return this.end;
    } else {
        return this.states[parseInt(time/TIME_INTERVAL)-1];
    }
};

History.prototype.getNextState = function(time) {
    if(time <= 0) {
        return this.start;
    } else if(time > (this.states.length)*TIME_INTERVAL) {
        return this.end;
    } else {
        return this.states[parseInt(time/TIME_INTERVAL)];
    }
};

History.prototype.setValues = function(values) {
    for ( var key in values) {
        var nValue = values[key];
        if(key in this)
            this[key] = nValue;
    }
};

/**
* State definition
* Encapsulation of a single state, may include destination, radius, and visible trade ships
*/
var State = function(parameters) {
    this.radius = 0;
    this.visible = false;
    this.destination = false;
    this.colour = false;
    this.ships = [];
    this.probes = [];
    this.setValues(parameters);
};

State.prototype.setValues = function(values) {
    for ( var key in values) {
        var nValue = values[key];
        if(key in this)
            this[key] = nValue;
    }
};

/**
* Ship definition
* Should be able to work as a sprite for both author ships and trade ships
*/
var Ship = function(parameters) {

    this.origin = false;
    this.start = false;
    this.destination = false;
    this.target = false;
    this.eta = 0;
    this.offset = 0;
    this.material = new THREE.SpriteMaterial( { color: 0xffffff, fog: true} );
    this.spr = new THREE.Sprite(this.material);
    this.loop = false;
    this.dis_at_end = false;
    this.orbit = false;
    this.his = false;
    
    this.setValues(parameters);

};

Ship.prototype.initializeAuthor = function(z) {
    this.target = this.his.start.destination;
    this.spr.position.x = this.target.mesh.position.x;
    this.spr.position.y = this.target.mesh.position.y;
    this.spr.position.z = z;
    this.orbit = this.target.mesh.geometry.parameters.radius + 1;
}

Ship.prototype.setValues = function(values) {
    for ( var key in values) {
        var nValue = values[key];
        if(key in this) {
            if(key == 'material') {
                this[key].dispose();
                this[key] = nValue;
                this.spr.material = nValue;
            } else if(key == 'eta' || key == 'offset') {
                this[key] = nValue * speed_base;
            } else {
                this[key] = nValue;
            }
        }
    }
};

var s_up = new THREE.Vector3(0,1,0);

Ship.prototype.updatepos = function(time, speedx) {
    if(this.origin == false) {
        this.origin = this.spr;
    }
    if(this.target != false) {
        if((time > this.eta && this.loop == false) || this.eta <= 0) {
            if(this.dis_at_end == true) {
                this.spr.visible = false;
            } else {
                if(this.his != false) {
                    var the_state = this.his.getNextState(time);
                    if(the_state.destination != this.target) {
                        this.eta = (parseInt(time/TIME_INTERVAL)+1) * TIME_INTERVAL;
                        this.start = this.spr.position.clone();
                        this.target = the_state.destination;
                        this.destination = this.target.projectedpos(this.eta);
                    } else {
                        if(this.orbit == false) {
                            this.spr.position.x = this.target.mesh.position.x;
                            this.spr.position.y = this.target.mesh.position.y;
                        } else {
                            this.spr.position.x = this.target.mesh.position.x + this.orbit*Math.cos(2*time);
                            this.spr.position.y = this.target.mesh.position.y + this.orbit*Math.sin(2*time);
                        }
                    }
                } else {
                    if(this.orbit == false) {
                        this.spr.position.x = this.target.mesh.position.x;
                        this.spr.position.y = this.target.mesh.position.y;
                    } else {
                        this.spr.position.x = this.target.mesh.position.x + this.orbit*Math.cos(2*time);
                        this.spr.position.y = this.target.mesh.position.y + this.orbit*Math.sin(2*time);
                    }
                }
            }
        } else {
            this.spr.visible = true;
            var temp_t = time + this.offset;
            if(this.loop == true)
                temp_t = temp_t % this.eta;
            if(temp_t < 0.01 * speedx) {
                this.start = this.origin.position.clone();
                this.destination = this.target.projectedpos(time + this.eta);
                var y = (new THREE.Vector3(this.destination.x - this.start.x, this.destination.y - this.start.y, 0)).normalize();
                this.spr.material.rotation = Math.acos(s_up.dot(y));
                if(this.start.x <= this.destination.x) {
                    this.spr.material.rotation = 2*Math.PI - this.spr.material.rotation;
                }
            } 
            if(this.loop == true) {
                this.spr.position.x = this.start.x + (this.destination.x - this.start.x)/this.eta*temp_t;
                this.spr.position.y = this.start.y + (this.destination.y - this.start.y)/this.eta*temp_t;
            } else {
                //debug((this.destination.x - this.start.x)/TIME_INTERVAL);
                this.spr.position.x = this.start.x + (this.destination.x - this.start.x)/TIME_INTERVAL*(time % TIME_INTERVAL);
                this.spr.position.y = this.start.y + (this.destination.y - this.start.y)/TIME_INTERVAL*(time % TIME_INTERVAL);
            }
        }
    }
};

/**
* Celestial definition
* For Stars, Planets and Moons, anything with a repeating orbit
*/

var Celestial = function(parameters) {

    this.geometry = new THREE.SphereGeometry(0.1, 8, 8);
    this.material = new THREE.MeshLambertMaterial({color:0xffffff});
    this.mesh = new THREE.Mesh(this.geometry, this.material);
    this.origin = false;
    
    this.orbitradx = 0;
    this.orbitrady = 0;
    this.orbitradz = 0;
    
    this.orbitoffx = 0;
    this.orbitoffy = 0;
    this.orbitoffz = 0;
    
    this.xsin = true;
    this.ysin = true;
    this.zsin = true;
    
    this.rotx = 0;
    this.roty = 0;
    this.rotz = 0;
    
    this.tfactor = 1;
    this.toffx = 0;
    this.toffy = 0;
    this.toffz = 0;
    
    this.light = false;
    
    this.trade = [];
    this.his = false;
    
    this.setValues(parameters);

};

Celestial.prototype.setValues = function (values) {
    
    for ( var key in values) {
        var nValue = values[key];
        if(key in this) {
            if(key == 'material') {
                this[key].dispose();
                this[key] = nValue;
                this.mesh.material = nValue;
            } else if(key == 'geometry') {
                this[key].dispose();
                this[key] = nValue;
                this.mesh.geometry = nValue;
            } else {
                this[key] = nValue;
            }
        }
    }
};

Celestial.prototype.projectedpos = function ( time ) {
    var x = 0;
    var y = 0;
    var z = 0;
    var temp = new THREE.Vector3();
    if(this.origin != false) {
        temp = this.origin.mesh.position;
    }
    if(this.xsin == true) {
        x = temp.x + this.orbitoffx + this.orbitradx * Math.sin(time * this.tfactor + this.toffx);
    } else {
        x = temp.x + this.orbitoffx + this.orbitradx * Math.cos(time * this.tfactor + this.toffx);
    }
    if(this.ysin == true) {
        y = temp.y + this.orbitoffy + this.orbitrady * Math.sin(time * this.tfactor + this.toffy);
    } else {
        y = temp.y + this.orbitoffy + this.orbitrady * Math.cos(time * this.tfactor + this.toffy);
    }
    if(this.zsin == true) {
        z = temp.z + this.orbitoffz + this.orbitradz * Math.sin(time * this.tfactor + this.toffz);
    } else {
        z = temp.z + this.orbitoffz + this.orbitradz * Math.cos(time * this.tfactor + this.toffz);
    }
    return new THREE.Vector3(x, y, z);
};

Celestial.prototype.updatepos = function ( time, speedx ) {
    var temp = this.mesh.position;
    if(this.origin != false) {
        temp = this.origin.mesh.position;
    }
    if(this.xsin == true) {
        this.mesh.position.x = temp.x + this.orbitoffx + this.orbitradx * Math.sin(time * this.tfactor + this.toffx);
    } else {
        this.mesh.position.x = temp.x + this.orbitoffx + this.orbitradx * Math.cos(time * this.tfactor + this.toffx);
    }
    if(this.ysin == true) {
        this.mesh.position.y = temp.y + this.orbitoffy + this.orbitrady * Math.sin(time * this.tfactor + this.toffy);
    } else {
        this.mesh.position.y = temp.y + this.orbitoffy + this.orbitrady * Math.cos(time * this.tfactor + this.toffy);
    }
    if(this.zsin == true) {
        this.mesh.position.z = temp.z + this.orbitoffz + this.orbitradz * Math.sin(time * this.tfactor + this.toffz);
    } else {
        this.mesh.position.z = temp.z + this.orbitoffz + this.orbitradz * Math.cos(time * this.tfactor + this.toffz);
    }
    this.mesh.rotation.x += this.rotx;
    this.mesh.rotation.y += this.roty;
    this.mesh.rotation.z += this.rotz;
    if(this.light != false) {
        this.light.position.x = this.mesh.position.x;
        this.light.position.y = this.mesh.position.y;
    }
    if(this.his == false) {
        var i = 0;
        for(i = 0; i < this.trade.length; i++) {
            this.trade[i].updatepos(time, speedx);
        }
    } else {
        var the_st = this.his.getState(time);
        if(the_st.radius != false) {
            var scale = the_st.radius/this.mesh.geometry.parameters.radius;
            this.mesh.scale.set(scale, scale, scale);
        }
        if(the_st.colour != false) {
            this.mesh.material.setValues({color: the_st.colour});
        }
        if(the_st.visible == true) {
            this.mesh.visible = true;
            if(this.light != false)
                this.light.intensity = 1;
        } else {
            this.mesh.visible = false;
            if(this.light != false)
                this.light.intensity = 0;
        }
        var i = 0;
        for(i = 0; i < this.trade.length; i++) {
            this.trade[i].updatepos(time, speedx);
            if(the_st.ships.indexOf(i) < 0)
                this.trade[i].spr.visible = false;
            else
                this.trade[i].spr.visible = true;
        }
    }
};

THREE.Scene.prototype.addC = function( celes ) {
    this.add(celes.mesh);
    if(celes.light != false)
        this.add(celes.light);
    var i = 0;
    for(i = 0; i < celes.trade.length; i++) {
        this.add(celes.trade[i].spr);
    }
};

// These constants are required to prevent a bug in OrbitalControls.js
THREE.MOUSE={LEFT:0,MIDDLE:1,RIGHT:2};