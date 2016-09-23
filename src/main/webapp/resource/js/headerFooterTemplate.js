$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var _FSN_HTTP_PRFIX_ = fsn.getResourcePrefix();
	var theUrl = window.location.href;
	var htmlPage = "";
	var isZfzy = false;//是否是政府专员
	try{
		var urlHead = theUrl.split("?")[0];
		if(urlHead){
			var dir = urlHead.split("/");
			if(dir){
				htmlPage = dir[(dir.length)-1];
				htmlPage = encodeURIComponent(htmlPage);
			}
		}
	} catch (e){};

	$.ajax({
		url:_FSN_HTTP_PRFIX_ + "/service/account/loadbus?page=" + htmlPage,
		type:"GET",
		dataType:"json",
		async:false,
		success:function(returnValue){
			if(returnValue.admin===true){
				isZfzy = true;
			}
		}
	});

	 var  search="<form action=\""+_FSN_HTTP_PRFIX_+"\" method=\"get\">"+
		"<fieldset>"+
		"<legend>搜索：</legend>"+
		"<label id=\"label_query\" for=\"query\">关键字</label>"+
		"<div class=\"input\"><input type=\"\" class=\"input_query noFocus\" name=\"query\" id=\"query\" value=\"\" title=\"搜索\"/></div>"+
		"<div class=\"input_button\"><input type=\"button\" value=\"搜索\" /></div>"+
		"</fieldset>"+
		"</form>";
	 
	 var menu="<div id=\"menu\"></div>";
	 
	 var clearfix="<div class=\"clearfix\"></div>";
	 var logoSrc = "/resource/img/LOGO.png";
	if(isZfzy){
		logoSrc = "/resource/img/fdms_logo.png";
	}
	 headerTemplate = kendo.template("<header class=\"fullview\" id=\"header\">"+
			 "<div class=\"container\">"+
			"<div class=\"row\" style=\"float:left;\">"+
			"<div class=\"column13\" id=\"site_information\">"+
				"<span id=\"logo\"><a href=\""+_FSN_HTTP_PRFIX_+"\"><img src=\""+_FSN_HTTP_PRFIX_+logoSrc +"\" alt=\"食品安全营养信息平台\"></a></span>"+
			"</div>"+
			"<div class=\"column14\" id=\"nav_search_bar\">"+
				"#=search#"+
			"</div>"+
		"</div>"+
		"#=menu#"+
		"</div>"+
		"#=clearfix#"+
	"</header>");
	 
	 
	 footerTemplate = kendo.template("<footer id=\"page_footer\" class=\"fullview\" style=\"z-index: 10009;\">"+
				"<span class=\"container\">Copyright©2014 食品安全与营养（贵州）信息科技有限公司"+
		" All Rights Reserved <a style='text-decoration:underline;' title='点击查看详情' href='http://www.miitbeian.gov.cn/' "+
		"target='_blank'>黔ICP备13001836号</a></span></footer>");
	 
	 $("body").append("<div id=\"ft\"></div>");
	 $("body").prepend("<div id=\"hd\"></div>");
	 
	 $("#ft").html(footerTemplate({})); 
	 if($("#login").length!=0){
		 $("#hd").html(headerTemplate({address:"",search:"",menu:"",clearfix:""})); 	 
	 }else if($("#home").length!=0){
		 $("#hd").html(headerTemplate({address:"",search:search,menu:menu,clearfix:clearfix})); 
	 }else{
		 $("#hd").html(headerTemplate({address:"../../../",search:search,menu:menu,clearfix:clearfix})); 	 		 
	 }
	 $("#ft").append("<div id='about_window' style='text-align:center;'><div id='about_content' style='padding:50px 20px 50px 20px;'>lims 3.1</div>" +
			 "<div style='text-align:right;'><button class='k-button' onclick='fsn.closeAboutWindow()'><span class='k-icon k-update'></span>"+
			 "确定</button></div></div>");

	 
	     // 加载菜单项
    	 $.get(_FSN_HTTP_PRFIX_+"/menu.html", function(data) {
		 $("#menu").html(data);
	 }); 
});