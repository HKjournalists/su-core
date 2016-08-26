$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
//	var _KNOWL_HTTP_PRFIX_ = "/standard-knowledge";
	var _FSN_HTTP_PRFIX_ = fsn.getResourcePrefix();

	 var  search="<form action=\""+_FSN_HTTP_PRFIX_+"\" method=\"get\">"+
		"<fieldset>"+
		"<legend>搜索：</legend>"+
		"<label id=\"label_query\" for=\"query\">关键字</label>"+
		"<div class=\"input\"><input type=\"\" class=\"input_query noFocus\" name=\"query\" id=\"query\" value=\"\" title=\"搜索\"/></div>"+
		"<div class=\"input_button\"><input type=\"submit\" value=\"搜索\"/></div>"+
		"</fieldset>"+
		"</form>";
	 
	 var menu="<div id=\"menu\"></div>";
	 
	 var clearfix="<div class=\"clearfix\"></div>";
	 
	 headerTemplate = kendo.template("<header class=\"fullview\" id=\"header\">"+
			 "<div class=\"container\">"+
			"<div class=\"row\" style=\"float:left;\">"+
			"<div class=\"column13\" id=\"site_information\">"+
				//"<span id=\"logo\"><img src=\"#=address#resource/img/LOGO.png\" alt=\"食品安全营养信息平台\"></span>"+
				"<span id=\"logo\"><a href=\""+_FSN_HTTP_PRFIX_+"\"><img src=\""+_FSN_HTTP_PRFIX_+"/resource/img/LOGO.png\" alt=\"食品安全营养信息平台\"></a></span>"+
//				"<h1 id=\"site_title\">食品安全营养信息平台"+
//					"<br/>"+
//					"<span id=\"site_title_en\">Food Safety Nutrition Information Platform</span>"+
//				"</h1>"+
			"</div>"+
			"<div class=\"column14\" id=\"nav_search_bar\">"+
				"#=search#"+
			"</div>"+
		"</div>"+
		"#=menu#"+
		"</div>"+
		"#=clearfix#"+
	"</header>");
	 
	 
	 footerTemplate = kendo.template("<footer id=\"page_footer\" class=\"fullview\">"+
		"<span class=\"container\">Copyright@2013 GuiZhou Gettec information Technology Co.,"+
		" Ltd. All Rights Reserved</span></footer>");
	 
	 
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
	  
    	/* $.get(_FSN_HTTP_PRFIX_+"/menu.html", function(data) {
		 $("#menu").html(data);
	 }); */
	 
	 /* $.get(_FSN_HTTP_PRFIX_+ "/menu.html", function(data) {
			$("#menu").html(data);
			$("#menu_list,#usr_menu").kendoMenu({
				animation: { open: { effects: "fadeIn" } }
			});
			$.ajax({
				url : fsn.getHttpPrefix() + "/user/menubar",
				type : "GET",
				dataType : "json",
				async : false,
				success : function(data) {
					var menu = $("#menu_list").data("kendoMenu");
					menu.append(data.menubar);
					$("#menu_loginUser").html(data.loginUser);
				}
			});

			$("#menu_logout").bind("click", function() {
				window.location =  fsn.getHttpPrefix()+"/user/logout";
			});
		});*/
      
	
});