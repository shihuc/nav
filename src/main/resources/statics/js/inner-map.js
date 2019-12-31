function setPanel() {
	var width = $(document.body).width();
	var height = $(document.body).height();
	$('#container').height(height - 120);
	$('#container').width(width - 50);
	$('canvas').attr("height",height - 120);
    $('canvas').attr("width", width - 50);
}
function savePoint(zr, pos, cycle) {
    $.post("./point/save", pos, function(data){
        cycle.pointId = data.info;
        pos.id = data.info;
        createText(zr, pos);
        savePointToLocal(pos.id, {"x": pos.pointx, "y":pos.pointy});
    }, "json");
}
function getAllPoints(zr) {
    $.get("./point/getAll", function(data){
        var jp = data;
        for(var i=0; i<jp.length; i++){
            console.log("id: " + jp[i].id + ", x: " + jp[i].pointx + ", y: " + jp[i].pointy);
            createPoint(zr, jp[i]);
            createText(zr, jp[i]);
            savePointToLocal(jp[i].id, {"x": jp[i].pointx, "y":jp[i].pointy})
        }
    }, "json");
}
function getPaths(zr, src, dst) {
    $.get("./go", {"srcId": src, "dstId": dst}, function(data){
        var jp = data;
        for(var i=0; i<jp.length; i++){
            console.log("id: " + jp[i].id + ", x: " + jp[i].pointx + ", y: " + jp[i].pointy);
        }
        showPath(zr, jp);
    }, "json");
}
function showPath(zr, jp) {
    //jp的长度一定是大于等于2的，否则不可能行程一条路径
    if(jp.length <= 1){
        console.log("不是一个合法的路径");
        return;
    }
    for(var i = 0; i<jp.length-1; i++){
        var fp = jp[i];
        var tp = jp[i+1];
        var path = new zrender.Line({
            shape: {
                x1:fp.pointx,
                y1:fp.pointy,
                x2:tp.pointx,
                y2:tp.pointy
            },
            style: {
                stroke:'green',
                lineWidth: 3
            }
        });
        zr.add(path);
        showedPath.push(path);
    }
}
function savePointToLocal(idx, pos) {
    var spos = JSON.stringify(pos);
    sessionStorage.setItem(idx, spos);
}
function getPointFromLocal(idx) {
    var res = sessionStorage.getItem(idx);
    var pos = JSON.parse(res);
    return pos;
}
function saveEdge(line) {
    if(line.len <= 10){
        console.log("距离太近，不予考虑...");
        return;
    }
    $.post("./edge/save", {"from":line.from, "to": line.to, "len": line.len}, function(data){
        line.lineId = data.info;
    }, "json");
}
function getAllEdges(zr) {
    $.get("./edge/getAll", function(data){
        var je = data;
        for(var i=0; i<je.length; i++){
            console.log("id: " + je[i].id + ", point: " + je[i].point + ", neighbor: " + je[i].neighbor + ", weight: " + je[i].weight);
            createEdge(zr, je[i]);
        }
    }, "json");
}
function delPoint(zr, circle) {
    $.post("./point/del", {"id":circle.pointId}, function(data){
        zr.remove(circle);
        zr.remove(textMap[circle.pointId]);
        for(var i = 0; i<data.length; i++){
            var edgeId = data[i];
            var dline = edgeMap[edgeId];
            zr.remove(dline);
            delete(edgeMap[edgeId])
        }
    }, "json");
}
function delEdge(zr, line) {
    $.post("./edge/del", {"id":line.lineId}, function(data){
        zr.remove(line);
        delete(edgeMap[line.lineId]);
    }, "json");
}
function createEdge(zr, je) {
    var fp = je.point;
    var tp = je.neighbor;
    fpoint = getPointFromLocal(fp);
    tpoint = getPointFromLocal(tp);
    var line = new zrender.Line({
        shape: {
            x1:fpoint.x,
            y1:fpoint.y,
            x2:tpoint.x,
            y2:tpoint.y
        },
        style: {
            stroke:'black'
        }
    }).on("mousedown", function(ev){
        if(ev.which == 3) { //右键
            delEdge(zr, line);
        }
    });
    line.from = fp;
    line.to = tp;
    line.len = je.weight;
    line.lineId = je.id;
    zr.add(line);
    edgeMap[je.id] = line;
}
function calcLen(fpoint, tpoint) {
    var xx = (fpoint.x - tpoint.x) * (fpoint.x - tpoint.x);
    var yy = (fpoint.y - tpoint.y) * (fpoint.y - tpoint.y);
    var edge = Math.sqrt(xx + yy);
    return Math.round(edge);
}
var fpoint = {"x":0, "y":0};
var tpoint = {"x":0, "y":0};
var fcycle = null;
var srcId = null;
var dstId = null;
var step = null;
var edgeMap = {};
var textMap = {};
var showedPath = [];
function createPoint(zr, pos) {
    var circle = new zrender.Circle({
        shape: {
            cx: 0,
            cy: 0,
            r: 10
        },
        position: [
            pos.pointx,
            pos.pointy
        ],
        style: {
            stroke: 'green',
            fill: 'red'
        }
    }).on('mouseover', function(){
        this.animateTo({
           shape: {
               r: 20
           },
           style: {
               stroke: 'green',
               fill: 'blue'
           }
        }, 300)
    }).on('mouseout', function() {
        this.animateTo({
            shape: {
                r: 10
            },
            style: {
                stroke: 'green',
                fill: 'red'
            }
        }, 300)
    }).on("mousedown", function(ev){
        if(ev.which == 1){ //左键
            fpoint = {"id": circle.pointId, "x": pos.pointx, "y": pos.pointy};
        }else if(ev.which == 3){//右轮
            delPoint(zr, circle);
        }else if(ev.which == 2){//中键
             //var step = $('input:radio:checked').val();
             if(step === 'st'){
                srcId = circle.pointId;
             }
             if(step === 'ed'){
                dstId = circle.pointId;
             }
             console.log("step: " + step + ", src: " + srcId + ", dst: " + dstId);
         }
    }).on("mouseup", function(ev){
        if(ev.which == 1){ //左键
            tpoint = {"id": circle.pointId, "x": pos.pointx, "y": pos.pointy};
            var line = new zrender.Line({
                shape: {
                    x1:fpoint.x,
                    y1:fpoint.y,
                    x2:tpoint.x,
                    y2:tpoint.y
                },
                style: {
                    stroke:'black'
                }
            }).on("mousedown", function(ev){
                if(ev.which == 3){ //左键
                    delEdge(zr, line);
                }
            });
            var len = calcLen(fpoint, tpoint);
            line.from = fpoint.id;
            line.to = tpoint.id;
            line.len = len;
            saveEdge(line);
            zr.add(line);
            edgeMap[line.lineId] = line;
        }else if(ev.which == 3){//右轮

        }
    })
    if(pos.id != null && pos.id != undefined){
        circle.pointId = pos.id;
    }
    zr.add(circle);
    return circle;
}
function createText(zr, pos) {
    var posText = new zrender.Text({
        style: {
            stroke: 'blue',
            text: "[" + pos.id + "] (" + pos.pointx + "," + pos.pointy + ")",
            fontSize: '11',
            textAlign:'center'
        },
        position: [pos.pointx, pos.pointy + 13]
    });
    zr.add(posText);
    textMap[pos.id] = posText;
}

$(document).ready(function() {		    
	document.oncontextmenu = function(){
	　　return false;
	}

	var container = document.getElementById('container');
	var zr = zrender.init(container);
	
	setPanel();
	//注意，一定是先加载点，然后再加载边
	getAllPoints(zr);
    getAllEdges(zr);
	zr.on('click', function(e) {
		var pos = {"id": 0, "pointx": e.offsetX, "pointy": e.offsetY};
		var point = createPoint(zr, pos)
		savePoint(zr, pos, point);
	})
	//删除所有的节点
	$('#clearBtn').on('click', function(e) {
		zr.clear()
	})
	//选择起点和终点
	$("input[type=radio]").on("click", function(){
	    step = $('input:radio:checked').val();
	});
	//开始绘制最短路径
	$('#nearest').on('click', function(e) {
        getPaths(zr, srcId, dstId);
    });
    //删除生成的最短路径，将上次的起始和结束点复位
    $('#restgame').on('click', function(e) {
        var len = showedPath.length;
        for(var i=0; i<len; i++){
            zr.remove(showedPath[i]);
        }
        showedPath.splice(0, len);
        srcId = null;
        dstId = null;
    })
});