/* global Blob URL location */

const element = document.getElementById("main")
const canvas = element.getContext("2d")

const container = document.getElementById("container")

container.scrollTop = 2517


const map = []
for (let x = 0; x < (element.offsetWidth/30); x++) {
    map.push([])
}

const length = map.length-1

const drawLine = (sx, sy, fx, fy) => {
    canvas.beginPath()
    canvas.moveTo(sy, sx)
    canvas.lineTo(fx, fy)
    canvas.stroke()
}

for (let x = 0; x < element.offsetWidth+1; x += 30) {
    drawLine(x,0,element.offsetWidth,x)
    drawLine(0,x,x,element.offsetWidth)
}

const textures = [{name:"Dirt",src:"http://texturemate.com/image/view/1441/_original"}, {name:"Water",src:"http://www.topdesignmag.com/wp-content/uploads/2012/03/14.-water-texture.jpg"}]
const elements = textures.map((texture, ind) => {
    const dropdown = document.getElementById('dropdown')
    const option = document.createElement("option")
    option.value = ind
    option.textContent = texture.name
    dropdown.appendChild(option)
    const e = document.createElement("img")
    e.src = texture.src
    return e
})

let curId = 0;

element.onclick = function(event) {
    const xSquare = Math.floor(((event.clientX + container.scrollLeft) - this.offsetLeft)/30)
    const ySquare = Math.floor(((event.clientY + container.scrollTop) - this.offsetTop)/30)
    map[xSquare][length-ySquare] = curId
    canvas.drawImage(elements[curId], xSquare*30, ySquare*30, 30, 30)
}

let dragging = false

element.onmousedown = function(e) {
    dragging = true
}

element.onmousemove = function(e) {
    if (dragging) {
        const xSquare = Math.floor(((event.clientX + container.scrollLeft) - this.offsetLeft)/30)
        const ySquare = Math.floor(((event.clientY + container.scrollTop) - this.offsetTop)/30)
        canvas.drawImage(elements[curId], xSquare*30, ySquare*30, 30, 30)
        map[xSquare][length-ySquare] = curId
    }
}

element.onmouseup = function(e) {
    dragging = false
}


document.getElementById('dropdown').onchange = function(e) {
    curId = e.target.value
}

document.getElementById("compile").onclick = function(e) {
    const locations = []
    map.map((row, x_ind) => {
        row.map((cell, y_ind) => {
            locations.push({x:x_ind,y:y_ind,id:cell})
        })
    })
    const binary = new DataView(new ArrayBuffer(locations.length*9))
    locations.map((location, ind) => {
        binary.setInt32(4*ind, location.x_ind)
        binary.setInt32((4*ind)+4, location.y_ind)
        binary.setInt8((ind)+8, location.id)
    })
    console.log(binary.getInt32(0))
    console.log(binary.getInt32(9))
    location.href = URL.createObjectURL(new Blob([binary.buffer], {type: 'application/octet-stream'}))
}