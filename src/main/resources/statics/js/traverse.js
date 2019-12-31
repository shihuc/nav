//测试用的JSON对象
var arr={
	"a":'a1',
	"b":122,
	"c":{"c1":'c11',"c2":'c22'},
	"d":['d1','d2'],
	"e":{"e1":['e11','e12'], "e2":['e21','e22','e23']},
	"f":[{"f11":11,"f12":12},{"f21":'vf21',"f22":'vf22'}],
	"g":[['g11','g12'],['g21','g22']],
	"h":{"h1":{'h11':'h12'}, "h2":{'h21':'h22'}},
	"i":{"i1":{'i11':{'i111':true, 'i112':false}}, "i2":{'i21':{'i211':'vi211', 'i212':'vi212'}}},
	"j":{"j1":undefined, "j2":null}
}
/**
 * 遍历一个JSON对象的所有成员，然后构建成一段html代码片段。ul、li组合嵌套，实现，li节点的key为json的key 
 */
function traverseJson(jsonObj) {
	var htmlStr='<ul key="rootj" class="ul_class">';
	for(var obj in jsonObj){
		htmlStr += '<li class="li_class" key="'+obj+'">'+'<span class="span_class">' +obj+'</span>:'
		if(!(jsonObj[obj] instanceof Object) && !(jsonObj[obj] instanceof Array)){ //基本key-value类型
			htmlStr += '<span class="span_attr">' + jsonObj[obj] + '</span>'
		}else if(jsonObj[obj] instanceof Object && !(jsonObj[obj] instanceof Array)){ //嵌套json类型
			var str1 = traverseJson(jsonObj[obj])
			htmlStr += str1			
		}else if(jsonObj[obj] instanceof Object && jsonObj[obj] instanceof Array){ //嵌套数组类型
			htmlStr += '<ul class="ul_class" key="'+obj+'">'
			for(var i=0;i<jsonObj[obj].length;i++){
				htmlStr += '<li class="li_class" key="'+i+'">'+ '<span class="span_class">'+i+'</span>:'
				if(!(jsonObj[obj][i] instanceof Object) && !(jsonObj[obj][i] instanceof Array)){
					htmlStr += '<span class="span_attr">' + jsonObj[obj][i] + '</span>'
				}else {
					htmlStr += traverseJson(jsonObj[obj][i]) 
				}								
				htmlStr += '</li>'
			}
			htmlStr += '</ul>'
		}	
		htmlStr += '</li>'
	}	
	htmlStr += '</ul>'
	return htmlStr;
}
/**
 * 实现数据整形，将一个数组对象的成员元素，变形为一个json对象的成员选择逻辑风格。
 * e.g: 数组内容： a,b,1,c,0,0,d
 *      变形结构:  a.b[1].c[0][0].d
 */
function reshape(pathArray){
	var res = ''
	if(pathArray.length <= 0){
		return res
	}
	for(var item in pathArray){
		var el = pathArray[item]
		//console.log(el)
		if(!isNaN(parseInt(el))){
			var ress = res.charAt(res.length-1)			
			if(ress == '.'){
				res = res.substring(0,res.length-1)
			}
			res += '[' + el + '].'
		}else{
			res += el + '.'
		}
	}
	if(res.charAt(res.length-1) === '.'){
		res = res.substring(0,res.length-1)
	}
	return res
}

function getValueByPath(jsonObj, pathArray){
	if(pathArray.length <= 0){
		return null;
	}
	var obj = jsonObj;
	for(var item in pathArray){
		var el = pathArray[item]
		console.log(obj);
		obj = obj[el];
	}

	return obj;
}

/*
 * 基于传递进来的参数，判断这个参数的数据类型，JavaScript领域的数据类型，基础类型主要分为下面几类：
 * String, Number, Boolean
 */
function dataType(param) {
    var res = Object.prototype.toString.call(param)
    var dt = null;
    switch(res) {
        case '[object String]':
             dt = "String";
             break;
        case '[object Number]':
             dt = "Number";
             break;
        case '[object Boolean]':
             dt = "Boolean";
             break;
        default:
             break;
    }
    return dt;
}

$(function(){
	/*
	 * 点击选中json的某个属性名后，获取当前属性名与json根节点之间的路径
	 */
	$(".span_class").click(function (e) {		
		var route=[]
		var key = $(this).text()
		var parents = $(this).parents('.li_class')		
		for(var i=0;i<parents.length;i++){	
			var node = parents[i] 
			if(node.className=='li_class'){				
				route.unshift(node.getAttribute('key'))
			}
		}
		var path = reshape(route)		
		var val = getValueByPath(arr, route);
		var type = dataType(val);
		route=route.join('->')
		var out = route + " : " + path
		var rest = out + ", value: " + val + ", type: " + type;
		console.log(rest);
		$("#rest").text(rest);
//		alert(out)
	})

	$(".span_class").hover(function(){
//	    var key = $(this).text()
//	    var obj = $(this).siblings(".span_attr");
//	    var value = obj.text()
//	    var dt = dataType(value)
//
//	    console.log("key: " + key + ", value: " + value + ", type: " + dt)
	},function(){
        var strings = JSON.stringify(arr);
        console.log(strings);
	});
})