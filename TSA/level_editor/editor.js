/* global Blob URL location */

// Get the main element 
const element = document.getElementById("main")

// Get the 2D context of the main element, which allows drawing onto the canvas
const canvas = element.getContext("2d")

// The container element will hold the canvas
const container = document.getElementById("container")

// scroll to the bottom, so we start in the bottom left corner of the level, not the top left
container.scrollTop = 2517

// this is where the level itself will be stored
const map = []


// the grid is split into 30x30 pixel blocks, so we need to account for that in the loop
for (let x = 0; x < 1000; x++) {
    // start creating the 2-dimensional array that will hold the level data
    map.push([])
    for (let y = 0; y < 1000; y++) {
        // -1 represents an empty block
        map[x][y] = -1
    }
}

let property = 0


const length = map.length-1
const drawLine = (sx, sy, fx, fy) => {
    canvas.beginPath()
    canvas.moveTo(sy, sx)
    canvas.lineTo(fx, fy)
    canvas.stroke()
}

for (let x = 0; x < element.offsetWidth+1; x += 30) {
    // draw two lines, one going horizontal, and one going vertical. These sixty lines will create a grid
    drawLine(x,0,element.offsetWidth,x)
    drawLine(0,x,x,element.offsetWidth)
}

const textures = [{name:"Eraser",src:"https://i.ytimg.com/vi/5hqd3knvcWk/maxresdefault.jpg"},{name:"Dirt",src:"http://texturemate.com/image/view/1441/_original",}, {name:"Water",src:"http://www.topdesignmag.com/wp-content/uploads/2012/03/14.-water-texture.jpg"}]
const dropdown = document.getElementById('dropdown_1')
const elements = textures.map((texture, ind) => {
    
    const option = document.createElement("option")
    option.value = ind
    option.textContent = texture.name
    dropdown.appendChild(option)
    const e = document.createElement("img")
    e.src = texture.src
    return e
})

let curId = 0;

element.onclick = element.onmousemove = function(event) {
    if (dragging) {
        const xSquare = Math.floor(((event.clientX + container.scrollLeft) - this.offsetLeft)/30)
        const ySquare = Math.floor(((event.clientY + container.scrollTop) - this.offsetTop)/30)
        canvas.drawImage(elements[curId], (xSquare*30)+2, (ySquare*30)+2, 27, 27)
        
        if (curId === 0) {
            map[xSquare][length-ySquare] = (-1 << 8) 
        } else {
            let final = curId << 8
            final = final | property
            map[xSquare][length-ySquare] = final
        }
    }
}

let dragging = false

element.onmousedown = function(e) {
    dragging = true
}


element.onmouseup = function(e) {
    dragging = false
}

element.onmouseleave = function(e) {
    dragging = false
}


document.getElementById('dropdown_1').onchange = function(e) {
    curId = parseInt(e.target.value)
}

document.getElementById('dropdown_2').onchange = function(e) {
    property = parseInt(e.target.selectedIndex)
}

document.getElementById("compile").onclick = function(e) {
    const val = new Int32Array(1000000)
    for (let x = 0; x < 1000; x++) {
        for (let y = 0; y < 1000; y++) {
            val[y+(x*1000)] = map[x][y]
        }
    }
    location.href = URL.createObjectURL(new Blob([val.buffer],{type:'application/octet-binary'}))
}