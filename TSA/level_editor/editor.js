/* global Blob URL location */

const element = document.getElementById("main")
const canvas = element.getContext("2d")

const container = document.getElementById("container")

container.scrollTop = 2517


const map = []
for (let x = 0; x < (element.offsetWidth/30); x++) {
    map.push([])
    for (let y = 0; y < element.offsetHeight/30; y++) {
        map[x][y] = -1
    }
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

const textures = [{name:"Eraser",src:"https://i.ytimg.com/vi/5hqd3knvcWk/maxresdefault.jpg"},{name:"Dirt",src:"http://texturemate.com/image/view/1441/_original",}, {name:"Water",src:"http://www.topdesignmag.com/wp-content/uploads/2012/03/14.-water-texture.jpg"}]
const dropdown = document.getElementById('dropdown_2')
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

element.onclick = function(event) {
    const xSquare = Math.floor(((event.clientX + container.scrollLeft) - this.offsetLeft)/30)
    const ySquare = Math.floor(((event.clientY + container.scrollTop) - this.offsetTop)/30)
    canvas.drawImage(elements[curId], (xSquare*30)+2, (ySquare*30)+2, 27, 27)
    map[xSquare][length-ySquare] = curId === 0 ? -1 : curId
}

let dragging = false

element.onmousedown = function(e) {
    dragging = true
}

element.onmousemove = function(e) {
    if (dragging) {
        const xSquare = Math.floor(((event.clientX + container.scrollLeft) - this.offsetLeft)/30)
        const ySquare = Math.floor(((event.clientY + container.scrollTop) - this.offsetTop)/30)
        canvas.drawImage(elements[curId], (xSquare*30)+2, (ySquare*30)+2, 27, 27)
        map[xSquare][length-ySquare] = curId === 0 ? -1 : curId
    }
}

element.onmouseup = function(e) {
    dragging = false
}


document.getElementById('dropdown_1').onchange = function(e) {
    curId = parseInt(e.target.value)
}

document.getElementById("compile").onclick = function(e) {
    location.href = URL.createObjectURL(new Blob([JSON.stringify(map)],{type:'application/octet-binary'}))
}