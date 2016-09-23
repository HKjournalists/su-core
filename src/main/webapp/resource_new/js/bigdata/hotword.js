var fsn = window.fsn = window.fsn || {};
var portal = fsn.portal = fsn.portal || {};
portal.HTTP_PREFIX = fsn.getHttpPrefix();

/**
 * 热词推荐样式控制
 * @author Huangyong 2015/4/10
 */
fsn.changeGuidStyle = function(isShow){
	if(isShow){
		$("#hotworddiv").attr("class",'J_ServiceListWrap service-question-list-wrap');
	}else{
		$("#hotworddiv").attr("class",'J_ServiceListWrap service-question-list-wrap service-question-list-wrap-closed');
	}
};

/* 初始化【热词推荐】栏中，符合当前热词的产品总数 */
fsn.initProductCount = function(){
	/* 1.获取当前热词排行榜  */
	var hotWords = new Array();
	var counts = 0;
	var hotWord = "";
	$.ajax({
		url: portal.HTTP_PREFIX + "/bigdata",
		type:"GET",
		dataType: "json",
		async:false,
        contentType: "application/json; charset=utf-8",
		success:function(returnValue){
			hotWords = returnValue.result;
		},
	});
	/* 2.查询符合条件的产品数量  */
	if(hotWords.length > 0){
		for(var i=0; i<hotWords.length; i++){
			hotWord = hotWords[i].wordName;
			$.ajax({
				url: portal.HTTP_PREFIX + "/bigdata/getProductByHotWord/" + 1 + "/" + 0 + "/" + hotWords[i].wordName,
	    		type:"GET",
	    		dataType: "json",
	    		async:false,
	            contentType: "application/json; charset=utf-8",
				success:function(returnValue){
					counts = returnValue.data.counts;
					if(counts>0){i=hotWords.length;}
				},
			});
		}
	}
	$("#hotWord").html("" + hotWord);
	$("#productCount").html("" + counts);
};