/* global Blob URL location */

// Everything is nested inside of an onload in case the JS loads faster than the HTML
window.onload = function() {

// Get the main element 
const element = document.getElementById("main")

// Get the 2D context of the main element, which allows drawing onto the canvas
const canvas = element.getContext("2d")

// The container element will hold the canvas
const container = document.getElementById("container")

// scroll to the bottom, so we start in the bottom left corner of the level, not the top left
container.scrollTop = 7680

// this is where the level itself will be stored
const map = []
const used = []

// the grid is split into 30x30 pixel blocks, so we need to account for that in the loop
for (let x = 0; x < 256; x++) {
    // start creating the 2-dimensional array that will hold the level data
    map.push([])
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

const textures = [{name:"Eraser",src:"https://i.ytimg.com/vi/5hqd3knvcWk/maxresdefault.jpg"},{name:"Dirt",src:"Dirt.png"}, {name:"Water",src:"Water.png"},{name:"Top-Dirt",src:"Top-Dirt.png"}, {name:"Deep-Water",src:"Deep-Water.png"}]
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

element.onclick = element.onmousemove = function(event) {
    if (dragging) {
        const xSquare = Math.floor(((event.clientX + container.scrollLeft) - this.offsetLeft)/30)
        const ySquare = Math.floor(((event.clientY + container.scrollTop) - this.offsetTop)/30)
        canvas.drawImage(elements[curId], (xSquare*30)+2, (ySquare*30)+2, 27, 27)
        
        if (curId === 0) {
            delete map[xSquare][length-ySquare]
            for (let o = 0; o < used.length; o++) {
                if (used[o][0] === xSquare && used[o][1] === length-ySquare) {
                    used.splice(o,1)
                    break
                }
            }
        } else {
            map[xSquare][length-ySquare] = { id: curId, property: property }
            let ok = true
            for (let o = 0; o < used.length; o++) {
                if (used[o][0] === xSquare && used[o][1] === length-ySquare) {
                    ok = false
                    break
                }
            }
            if (ok === true) {
                used.push([xSquare, length-ySquare])
            }
        }
    }
}

document.getElementById('dropdown_1').onchange = function(e) {
    curId = parseInt(e.target.value)
}

document.getElementById('dropdown_2').onchange = function(e) {
    property = parseInt(e.target.selectedIndex)
}

document.getElementById("compile").onclick = function(e) {
    console.log(used)
    const exportArr = new Uint8Array(used.length*4)
    used.map((item, ind) => {
        exportArr[(ind*4)] = item[0]
        exportArr[(ind*4)+1] = item[1]
        exportArr[(ind*4)+2] = map[item[0]][item[1]].id
        exportArr[(ind*4)+3] = map[item[0]][item[1]].property
    })
    location.href = URL.createObjectURL(new Blob([exportArr.buffer],{type:'application/octet-stream'}))
}

const handleFiles = files => {
    const reader = new FileReader()
    reader.onload = e => {
        const data = new Uint8Array(e.target.result)
        console.log(data)
        for (let x = 0; x < data.length; x += 4) {
            const xSquare = Math.floor(((data[(x*4)]*30 + container.scrollLeft) - element.offsetLeft)/30)
            const ySquare = Math.floor(((data[(x*4)+1]*30 + container.scrollTop) - element.offsetTop)/30)
            canvas.drawImage(elements[data[(x*4)+2]], (xSquare*30)+2, (ySquare*30)+2, 27, 27)
            
            if (curId === 0) {
                delete map[xSquare][length-ySquare]
                for (let o = 0; o < used.length; o++) {
                    if (used[o][0] === xSquare && used[o][1] === length-ySquare) {
                        used.splice(o,1)
                        break
                    }
                }
            } else {
                map[xSquare][length-ySquare] = { id: data[(x*4)+2], property: data[(x*4)+3] }
                let ok = true
                for (let o = 0; o < used.length; o++) {
                    if (used[o][0] === xSquare && used[o][1] === length-ySquare) {
                        ok = false
                        break
                    }
                }
                if (ok === true) {
                    used.push([xSquare, length-ySquare])
                }
            }
    
        }
    }
    reader.readAsArrayBuffer(files[0])
}

}