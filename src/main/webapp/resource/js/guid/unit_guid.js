(function($) {
	 var fsn = window.fsn = window.fsn || {};
	 var business_unit = window.fsn.business_unit = window.fsn.business_unit ||{};
	 
	 fsn.imgId = 1;
	 //页面引导
	fsn.startForwMeStep=function(){
			fsn.imgId = 1;
	    	$("body").css("overflow","hidden");
	    	$("#followMeDiv").attr("style","position:fixed;top:50px;right:0;left:0;bottom:0px;");
	    	$("#followMeStep").attr("style","position:relative;top:5em;left:20em;width:470px;");
	    	$("#imgs").attr("src",fsn.src+fsn.imgId+".png");
	    };
	    //下一页
	    fsn.nextStep=function(){
			if(fsn.imgId>=fsn.imgMax){return;}
			fsn.imgId+=1;
	    	$("#imgs").attr("src",fsn.src+fsn.imgId+".png");
	    };
	    //上一页
	    fsn.backStep=function(){
			if(fsn.imgId<=1){return;}
	    	fsn.imgId-=1;
	    	$("#imgs").attr("src",fsn.src+fsn.imgId+".png");
	    };
	    //关闭引导
	    fsn.cancelStep=function(){
	    	$("body").css("overflow","");
	    	$("#followMeDiv").attr("style","");
	    	$("#followMeStep").attr("style","display:none;");
	    };
})(jQuery);