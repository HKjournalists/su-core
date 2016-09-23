$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var _FSN_HTTP_PRFIX_ = fsn.getResourcePrefix();

	 var  search1="<form action=\""+_FSN_HTTP_PRFIX_+"\" method=\"get\">"+
		"<fieldset>"+
		"<legend>搜索：</legend>"+
		"<label style='color:#D4D4D4' id=\"label_query\" for=\"query\">条形码</label>"+
		"<div class=\"input\"><input type=\"\" class=\"input_query noFocus\" name=\"query\" id=\"query\" value=\"\" title=\"搜索\"/></div>"+
		"<div class=\"input_button\"><input id=\"query_btn\" type=\"button\" value=\"搜索\" onclick='fsn.openViewReportPage()' /></div>"+
		"</fieldset>"+
		"</form>";
	 //var search1="  <a class=\"button1 in-ele btn1current hack1 fr cb\" style='margin-top: -32px' href=\"\javascript:void(0)\" onclick=\"javascript:history.go(-1)\" ><span></span>返&nbsp;&nbsp;回</a>";
	  var menu1="<div id=\"menu\" class=\"fl w180 mt20\"></div>";
	 
	 var clearfix="<div class=\"clearfix\"></div>";
	 
	 headerTemplate = kendo.template("<header class=\"fullview\" id=\"header\">"+
			 "<div class=\"container\">"+
			"<div class=\"row\" >"+
			"<div class=\" fl\" id=\"site_information\" style=\"position: fixed;\">"+
				"<span id=\"logo\"><a href=\""+_FSN_HTTP_PRFIX_+"\"><img src=\""+_FSN_HTTP_PRFIX_+"/resource/img/LOGO.png\" alt=\"食品安全营养信息平台\"></a></span>"+
			"</div>"+
			"<span class=\" fr\" id=\"nav_search_bar\">"+
				"#=search#"+
			"</span>"+
		"</div>"+
		"#=menu#"+
		"</div>"+
		"#=clearfix#"+
	"</header>");
	 
	 
	 footerTemplate = kendo.template("<footer id=\"page_footer\" class=\"fullview \">"+
				"<span class=\"container\">Copyright©2014 食品安全与营养（贵州）信息科技有限公司"+
		" All Rights Reserved <a style='text-decoration:underline;' title='点击查看详情' href='http://www.miitbeian.gov.cn/' "+
		"target='_blank'>黔ICP备13001836号</a></span></footer>");
	 
	 $("body").append("<div id=\"ft\" class=\"cb\"></div>");
	 $("body").prepend("<div id=\"hd\"></div>");
	 
	 $("#ft").html(footerTemplate({})); 
	 if($("#login").length!=0){
		 $("#hd").html(headerTemplate({address:"",search:"",menu:"",clearfix:""})); 	 
	 }else if($("#home").length!=0){
		 $("#hd").html(headerTemplate({address:"",search:search1,menu:menu1,clearfix:""})); 		
	 }else{
		 $("#hd").html(headerTemplate({address:"../../../",search:search1,menu:menu1,clearfix:""})); 	 		 
	 }
	 $("#ft").append("<div id='about_window' style='text-align:center;'><div id='about_content' style='padding:50px 20px 50px 20px;'>lims 3.1</div>" +
			 "<div style='text-align:right;'><button class='k-button' onclick='fsn.closeAboutWindow()'><span class='k-icon k-update'></span>"+
			 "确定</button></div></div>");

	 
	     // 加载菜单项
    	 $.get(_FSN_HTTP_PRFIX_+"/menu_new.html", function(data) {
		 $("#menu").html(data);
	 }); 
});