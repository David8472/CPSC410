/**
* @author Raela
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
    
    this.setValues(parameters);

};

Ship.prototype.setValues = function(values) {
    for ( var key in values) {
        var nValue = values[key];
        if(key in this) {
            if(key == 'material') {
                this[key] = nValue;
                this.spr.material = nValue;
            } else if(key == 'eta' || key == 'offset') {
                this[key] = nValue * Math.PI;
            } else {
                this[key] = nValue;
            }
        }
    }
};

var s_up = new THREE.Vector3(0,1,0);

Ship.prototype.updatepos = function(time) {
    if(this.origin == false) {
        this.origin = this.spr.clone();
    }
    if(this.target != false) {
        if((time > this.eta && this.loop == false) || this.eta <= 0) {
            this.spr.position = this.target.position;
        } else {
            var t = time + this.offset;
            if(this.loop == true)
                t = t % this.eta;
            if(t < 0.01) {
                this.start = this.origin.position.clone();
                this.destination = this.target.projectedpos(time + this.eta);
                var y = this.destination.clone().normalize();
                this.spr.material.rotation = Math.acos(s_up.dot(y));
                if(this.start.x <= this.destination.x) {
                    this.spr.material.rotation = 2*Math.PI - this.spr.material.rotation;
                }
            } 
            this.spr.position.x = this.start.x + (this.destination.x - this.start.x)/this.eta*t;
            this.spr.position.y = this.start.y + (this.destination.y - this.start.y)/this.eta*t;
        }
    }
};

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
    
    this.setValues(parameters);

};

Celestial.prototype.setValues = function (values) {
    
    for ( var key in values) {
        var nValue = values[key];
        if(key in this) {
            if(key == 'material') {
                this[key] = nValue;
                this.mesh.material = nValue;
            } else if(key == 'geometry') {
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

Celestial.prototype.updatepos = function ( time ) {
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
        this.light.position = this.mesh.position;
    }
    var i = 0;
    for(i = 0; i < this.trade.length; i++) {
        this.trade[i].updatepos(time);
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

THREE.MOUSE={LEFT:0,MIDDLE:1,RIGHT:2};