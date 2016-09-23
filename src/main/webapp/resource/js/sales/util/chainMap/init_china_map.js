function initChinaMap (listProvince){
	if(listProvince == null) return;
	
	var flag;
	var arr = new Array();
	var nameArr = new Array();
	var listProvinceStr = "";
	
	for(var i=0;i<listProvince.length;i++){
		listProvinceStr += listProvince[i] + ";";
	}
	
	for(k in data){
		var d = data[k].value;
		var name = data[k].cnname;
		if(d<100){
			flag = 0;
		}else if(d>=100 && d<500){
			flag = 1;
		}else if(d>=500 && d<2000){
			flag = 2;
		}else if(d>=2000 && d<5000){
			flag = 3;
		}else if(d>=5000 && d<10000){
			flag = 4;
		}else{
			flag = 5;
		}
		arr.push(flag);
		nameArr.push(name);
	}
	var reTimer;
    var R = Raphael("chainMap", 600, 500);
	//调用绘制地图方法
    paintMap(R);
	
	var textAttr = {
        "fill": "#000",
        "font-size": "12px"
        //"cursor": "pointer"
    };
    var i=0;      
    for (var state in china) {
		china[state]['path'].color = "#97d6f5";
				
        (function (st, state) {
			
			//获取当前图形的中心坐标
            var xx = st.getBBox().x + (st.getBBox().width / 2);
            var yy = st.getBBox().y + (st.getBBox().height / 2);
			
            //***修改部分地图文字偏移坐标
            switch (china[state]['name']) {
                case "江苏":
                    xx += 5;
                    yy -= 10;
                    break;
                case "河北":
                    xx -= 10;
                    yy += 20;
                    break;
                case "天津":
                    xx += 10;
                    yy += 10;
                    break;
                case "上海":
                    xx += 10;
                    break;
                case "广东":
                    yy -= 10;
                    break;
                case "澳门":
                    yy += 10;
                    break;
                case "香港":
                    xx += 20;
                    yy += 5;
                    break;
                case "甘肃":
                    xx -= 40;
                    yy -= 30;
                    break;
                case "陕西":
                    xx += 5;
                    yy += 10;
                    break;
                case "内蒙古":
                    xx -= 15;
                    yy += 65;
                    break;
                default:
            }
            
			//写入文字
			china[state]['text'] = R.text(xx, yy, china[state]['name']).attr(textAttr);
			var fillcolor = "#97d6f5";
			if(listProvinceStr.indexOf(china[state]['name'])>-1){
				fillcolor = "#c3b300";
				st.color = "#c3b300";
			}
			st.attr({fill:fillcolor});
			/*var offsetXY = function(e){
                var mouseX, 
                    mouseY,
                    tipWidth = $('.stateTip').outerWidth(),
                    tipHeight = $('.stateTip').outerHeight();
                if(e && e.pageX){
                    mouseX = e.pageX;
                    mouseY = e.pageY;
                }else{
                    mouseX = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
                    mouseY = event.clientY + document.body.scrollTop + document.documentElement.scrollTop;
                }
                mouseX = mouseX - tipWidth/2 + opt.stateTipX < 0 ? 0 : mouseX - tipWidth/2 + opt.stateTipX;
                mouseY = mouseY - tipHeight + opt.stateTipY < 0 ? mouseY - opt.stateTipY : mouseY - tipHeight + opt.stateTipY;
                return [mouseX, mouseY];
            };*/
			/*var opt= {
			mapName: 'china',
            mapWidth: 500,
            mapHeight: 400,
         	stateData: data,
            
            strokeWidth: 1,
            showTip: true,
            stateTipWidth: 100,
            stateTipX: 0,
            stateTipY: -10,
			};*/
			st[0].onmouseover = function (e) {
                st.animate({fill: "green", stroke: "#eee"}, 500);
				/*china[state]['text'].toFront();
                R.safari();
				if(opt.showTip){
					clearTimeout(reTimer);
					if ($('.stateTip').length == 0) {
                          $(document.body).append('<div class="stateTip"></div');
                    }
					
					$('.stateTip').html(china[state]['name']);
					var _offsetXY = new offsetXY(e);
                    $('.stateTip').css({
						  width: opt.stateTipWidth || 'auto',
						  height: opt.stateTipHeight || 'auto',
						  left: _offsetXY[0],
						  top: _offsetXY[1]
					}).show();
				}*/
            };
            st[0].onmouseout = function (e) {
                st.animate({fill: st.color, stroke: "#eee"}, 500);
				/*china[state]['text'].toFront();
                R.safari();
				if(opt.showTip){
					reTimer = setTimeout(function(){
						$('.stateTip').remove();
					}, 100);
                }*/
            };
			
            st[0].style.cursor = "pointer";
            
			st[0].onclick = function(e){
				branch.viewDetailMap(china[state]['name']);
			};

         })(china[state]['path'], state);
		 i++;
    }
    
    /* 兼容Chrome浏览器 */
    var agent=navigator.userAgent;
    if(agent.indexOf("Chrom") > -1){
    	var tspan = $("tspan");
    	if(tspan != null && tspan.length > 0){
    		for(var i=0;i<tspan.length;i++){
    			$(tspan[i]).attr("dy","0");
    		}
    	}
    }
}