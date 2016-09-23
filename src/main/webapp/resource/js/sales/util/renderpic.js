/**
 * Created by HY on 2015/4/23.
 */
(function(){

	var renderpic = window.renderpic = window.renderpic || {};
	renderpic.callBackFun = {};
	
    /* 获取dom对象 */
    function G(s){
        return document.getElementById(s);
    }

    function getStyle(obj, attr){
        if(obj.currentStyle){
            return obj.currentStyle[attr];
        }else{
            return getComputedStyle(obj, false)[attr];
        }
    }

    function Animate(obj, json){
        if(obj.timer){
            clearInterval(obj.timer);
        }
        obj.timer = setInterval(function(){
            for(var attr in json){
                var iCur = parseInt(getStyle(obj, attr));
                iCur = iCur ? iCur : 0;
                var iSpeed = (json[attr] - iCur) / 5;
                iSpeed = iSpeed > 0 ? Math.ceil(iSpeed) : Math.floor(iSpeed);
                obj.style[attr] = iCur + iSpeed + 'px';
                if(iCur == json[attr]){
                    clearInterval(obj.timer);
                }
            }
        }, 30);
    }
    
    renderpic.initPhotosStyleA = function(formId,pictureSwitchFun){
		var bigPicdiv = G(formId);
        /* 大图片展示区域 */
        var bigPic = '<div id="picBox" class="picBox"><ul class="cf" id = "big_ul"></ul></div>';
        /* 小图片展示区域 */
        var samllPic = '<div id="listBox" class="listBox"><ul class="cf" id = "small_ul"></ul></div>';
        /* 拼接左右按钮 + 大图片展示 + 小图片展示*/
        var butTags = '<span id="prev" class="btn prev" ></span><span id="next" class="btn next" ></span><span id="prevTop" class="btn prev" ></span><span id="nextTop" class="btn next"></span>'+
            bigPic+samllPic+'</div>';
        bigPicdiv.innerHTML = butTags;
        if(pictureSwitchFun != null) {
        	renderpic.callBackFun.switchFun = pictureSwitchFun;
        }
	};
    
    /**
     * 设置图片
     * heigth 图片高度
     * width 图片宽
     * picObj 图片的List对象
     */
    function setMaxPicture(listPic){
        var bLi =""; //渲染大图显示区域
        var sLi =""; //渲染小图显示区域680,380,118,64
        var big_ul = G("big_ul");
        var small_ul = G("small_ul");
        big_ul.innerHTML = "";
        small_ul.innerHTML = "";
		var styleOn = 'class="on"';
        for(var i =0 ;i < listPic.length;i++){//width="680" height="380"
            bLi = bLi+'<li><a target="_black" href= "' + listPic[i].imgUrl + '"><img style="max-height:380px;width:auto;" src="'+ listPic[i].imgUrl + '" /></a><span>' +listPic[i].imgName+'</span></li>';
            sLi = sLi + '<li '+styleOn+' data-fieldtype-text="'+listPic[i].field.fieldType+'"><i class="arr2"></i><img width="118" height="64" src= "'+listPic[i].thumbnailUrl+'" /></li>';
			styleOn = '';
			listPic[i].field.index = i;
        }
        big_ul.innerHTML = bLi;
        small_ul.innerHTML = sLi;
    }

    var oPic = null;
	var oList = null;

	var oPrev = null;
	var oNext = null;
	var oPrevTop = null;
	var oNextTop = null;

	var oPicLi = null;
	var oListLi = null;
	var len1 = null;
	var len2 = null;

	var oPicUl = null;
	var oListUl = null;
	var w1 = null;
	var w2 = null;
	
	var index = 0;

	var num = 5;
	var num2 = 0;
    
	/**
	  * 比较值，隐藏上一张和下一张按钮
	  * @author tangxin 2015-05-17
	  */
	 function hideBtnA(subv, ol){
		 if(subv == 129){
			$("#next").addClass("btn_hied");
			$("#nextTop").addClass("btn_hied");
		}else if(subv >= 129){
			$("#next").removeClass("btn_hied");
			$("#nextTop").removeClass("btn_hied");
		}
		if(ol == 0){
			$("#prev").addClass("btn_hied");
			$("#prevTop").addClass("btn_hied");
		}else if(ol >= 120){
			$("#prev").removeClass("btn_hied");
			$("#prevTop").removeClass("btn_hied");
		}
	 }
	
	renderpic.setStyleADataSource = function(listPicture){
		setMaxPicture(listPicture);
		
		oPic = G("picBox");
		oList = G("listBox");

		oPrev = G("prev");
		oNext = G("next");
		oPrevTop = G("prevTop");
		oNextTop = G("nextTop");

		oPicLi = oPic.getElementsByTagName("li");
		oListLi = oList.getElementsByTagName("li");
		len1 = oPicLi.length;
		len2 = oListLi.length;
		/* 默认隐藏上一按钮 */
		$("#prev").addClass("btn_hied");
		$("#prevTop").addClass("btn_hied");
		/* 当图片只有一张时.隐藏下一张按钮 */
		if(len2 <= 1){
			$("#next").addClass("btn_hied");
			$("#nextTop").addClass("btn_hied");
		}

		oPicUl = oPic.getElementsByTagName("ul")[0];
		oListUl = oList.getElementsByTagName("ul")[0];
		w1 = oPicLi[0].offsetWidth;
		w2 = oListLi[0].offsetWidth;

		oPicUl.style.width = w1 * len1 + "px";
		oListUl.style.width = w2 * len2 + "px";

		index = 0;

		num = 5;
		num2 = Math.ceil(num / 2);

		Change = function(){

			var ul = G("listBox").getElementsByTagName("ul")[0];
			var ow = ul.offsetWidth;
			var olistli = oList.getElementsByTagName("li")[index];
			if(olistli == null){
				return;
			}
			var ol = olistli.offsetLeft;
			var subv = ow - ol;
			hideBtnA(subv, ol);
			Animate(oPicUl, {left: - index * w1});
			if(ow > 645){
				if(index < num2){
					Animate(oListUl, {left: 0});
				}else if(index + num2 <= len2){
					Animate(oListUl, {left: - (index - num2 + 1) * w2});
				}else{
					Animate(oListUl, {left: - (len2 - num) * w2});
				}
			}

			for (var i = 0; i < len2; i++) {
				oListLi[i].className = "";
				if(i == index){
					oListLi[i].className = "on";
				}
			}
			if(renderpic.callBackFun.switchFun != null){
				renderpic.callBackFun.switchFun(getFieldType(index),index);
			}
		};

		oNextTop.onclick = oNext.onclick = function(){
			index ++;
			index = index == len2 ? 0 : index;
			Change();
		};

		oPrevTop.onclick = oPrev.onclick = function(){
			index --;
			index = index == -1 ? len2 -1 : index;
			Change();
		};

		for (var i = 0; i < len2; i++) {
			oListLi[i].index = i;
			oListLi[i].onclick = function(){
				index = this.index;
				Change();
			};
		}
	};
	
	/* 获取相册中当前展示图片的类型 */
	function getFieldType(index){
		if(index == null) return -1;
		var listBox = G("listBox");
		var listLi = listBox.getElementsByTagName("li");
		var fieldType = -1;
		for(var i=0;i<listLi.length;i++){
			if(listLi[i].index == index){
				fieldType = $(listLi[i]).attr("data-fieldtype-text");
				break;
			}
		}
		
		return fieldType;
	}
	
	renderpic.initPhotosStyleB = function(formId,width,height,callBackDelFun, callBackSetCoverFun){
		var samll = G(formId);
        var samllPic = '<div id="listBox" class="listBox"><ul class="cf" id = "small_ul"></ul></div>';
		 /* 拼接左右按钮 小图片展示*/
        var butTags = '<span id="prev" class="btn prev" ></span><span id="next" class="btn next" ></span>'+samllPic+'</div>';
        samll.innerHTML = butTags;
        if(callBackDelFun != null) {
        	renderpic.callBackFun.callBackDelFun = callBackDelFun;
        }
        if(callBackSetCoverFun != null) {
        	 renderpic.callBackFun.callBackSetCoverFun = callBackSetCoverFun;
        }
	};
	
	 function addSamllPic(picObj,sWidth,sHeigth){
	        var ul = $("#small_ul");
	        var li_cover = null;
	        for(var i =0 ;i < picObj.length;i++){
	        	var p_fav_class = (picObj[i].cover == 1 ? "fav_cover" : "fav");
	        	var p_fav_title = (picObj[i].cover == 1 ? "当前图片已经设为封面" : "将此照片设置为封面");
	        	var p_fav_html = (picObj[i].cover == 1 ? "封面" : "设置为封面");
	        	var clo = '<div class="mod-photo-op"><span class="photo-op-tip" guid='+picObj[i].guid+' i='+picObj[i].id+' title="删除此图片">' +
       		  				'<i class="icon-m icon-expansion-m"></i></span>'+
       		  				'<div class="position-absolute" guid='+picObj[i].guid+' i='+picObj[i].id+' style="display:none;">'+
       		  				'<div class="bg"></div><p class="'+p_fav_class+'" title="'+p_fav_title+'">'+p_fav_html+'</p></div></div>';
	        	var aLi = $("<li></li>");
	        	var srcStr = "";
	        	if(!picObj[i].url) {
	        		srcStr = 'src="data:image/jpg;base64,' + picObj[i].file + '"';
	        	} else {
	        		srcStr = 'src="' + picObj[i].url + '"';
	        	}
	        	aLi.html('<i class="arr2"></i><img class="list_img" '+srcStr+'/>'+clo);
	        	$(aLi).mouseover(function(){
	        		$(this).find("span.photo-op-tip").css("display","block");
	        		$(this).find("div.position-absolute").css("display","block");
	        	});
	        	$(aLi).mouseout(function(){
	        		$(this).find("span.photo-op-tip").css("display","none");
	        		$(this).find("div.position-absolute").css("display","none");
	        	});
	        	/* 删除图片 */
	          	$(aLi).find("span.photo-op-tip").click(function(e){
	          		$(this).parent().parent().remove();
	          		/* 重新初始化listBox的状态 */
	          		restartInitListBox();
	        		if(renderpic.callBackFun.callBackDelFun != null) {
	        			renderpic.callBackFun.callBackDelFun($(this).attr("guid"),$(this).attr("i"));
	        		}
	        	});
	          	/* 设置为封面 */
	        	$(aLi).find("div.position-absolute").click(function(e){
	        		var coverP = $("p.fav_cover");
	        		if(coverP != null) {
	        			$(coverP).attr("class","fav");
	        			$(coverP).attr("title","将此照片设置为封面");
	        			$(coverP).html("设置为封面");
	        		}
	        		var pfav = $(this).find("p.fav");
	        		if(pfav != null) {
	        			$(pfav).attr("class","fav_cover");
	        			$(pfav).attr("title","当前图片已经设为封面");
	        			$(pfav).html("封面");
	        		}
	        		if(renderpic.callBackFun.callBackSetCoverFun != null) {
	        			renderpic.callBackFun.callBackSetCoverFun($(this).attr("guid"),$(this).attr("i"));
	        		}
	        	});
	        	if(picObj[i].cover == 0){
	        		 ul.prepend(aLi);
	        	}else{
	        		li_cover = aLi;
	        	}
	        }
	        if(li_cover != null) {
	        	ul.prepend(li_cover);
	        }
	    }
	
	 /**
	  * 有资源被删除时，重新初始化listBox的状态
	  * @author tangxin 2015-05-17
	  */
	 function restartInitListBox(){
		oList = G("listBox");
			
		oPrev = G("prev");
		oNext = G("next");

		oListLi = oList.getElementsByTagName("li");
		len = oListLi.length;
		if(len < 1) return;

		oListUl = oList.getElementsByTagName("ul")[0];
		w2 = oListLi[0].offsetWidth;

		oListUl.style.width = w2 * len + "px";

		index = 0;

		num = 4;
		num2 = Math.ceil(num / 2);
		
		for (var i = 0; i < len; i++) {
			oListLi[i].index = i;
			oListLi[i].onclick = function(){
				index = this.index;
				Change();
			};
		}
	 }
	 
	 /**
	  * 比较值，隐藏上一张和下一张按钮
	  * @author tangxin 2015-05-17
	  */
	 function hideBtn(subv, ol){
		 if(subv == 180){
			$(".minListBox span.next").addClass("btn_hied");
		}else if(subv >= 180){
			$(".minListBox span.next").removeClass("btn_hied");
		}
		if(ol == 0){
			$(".minListBox span.prev").addClass("btn_hied");
		}else if(ol >= 180){
			$(".minListBox span.prev").removeClass("btn_hied");
		}
	 }
	 
	renderpic.addPhotosStyleB = function(aryImgs){
		addSamllPic(aryImgs,175,64);
		oList = G("listBox");
		
		oPrev = G("prev");
		oNext = G("next");

		oListLi = oList.getElementsByTagName("li");
		len = oListLi.length;
		var resId = (aryImgs != null ? aryImgs[0].id : null);
		if(resId != null){
			$(".minListBox span.prev").addClass("btn_hied");
		}
		if(len == 1){
			$(".minListBox span.next").addClass("btn_hied");
		}else if(len > 1){
			$(".minListBox span.next").removeClass("btn_hied");
		}
		
		oListUl = oList.getElementsByTagName("ul")[0];
		w2 = oListLi[0].offsetWidth;

		oListUl.style.width = w2 * len + "px";

		index = 0;

		num = 4;
		num2 = Math.ceil(num / 2);

		Change = function(){
			var ul = G("listBox").getElementsByTagName("ul")[0];
			var ow = ul.offsetWidth;
			var olistli = oList.getElementsByTagName("li")[index];
			if(olistli == null){
				return;
			}
			var ol = olistli.offsetLeft;
			var subv = ow - ol;
			hideBtn(subv, ol);
			if(ow >= 720){
				if(index < num2){
					Animate(oListUl, {left: 0});
				}else if(index + num2 < len){
					Animate(oListUl, {left: - (index - num2 + 1) * w2});
				}else{
					Animate(oListUl, {left: - (len - num) * w2});
				}
			}

			for (var i = 0; i < len; i++) {
				oListLi[i].className = "";
				if(i == index){
					oListLi[i].className = "on";
				}
			}
		};

		oNext.onclick = function(){
			if(index < len) {
				index ++;
				Change();
			}
		};

		oPrev.onclick = function(){
			if(index > 0) {
				index --;
				Change();
			}
		};

		for (var i = 0; i < len; i++) {
			oListLi[i].index = i;
			oListLi[i].onclick = function(){
				index = this.index;
				Change();
			};
		}
	};
    
})();
