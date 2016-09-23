$(function(){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var settings = window.settings || {};
	var httpPrefix = fsn.getHttpPrefix();
	var oldData="";
	settings.dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				url :function(options){
            		if (options.filter){
            			var configure = filter.configure(options.filter);
            			return httpPrefix + "/setimentTopic/getTopicsByOrg?configure=" +configure;
            		}
            		return httpPrefix + "/setimentTopic/getTopicsByOrg";
            	},
				type : "GET"
			}
		},
		page:1,
	    pageSize : 10, //每页显示个数
	    serverPaging: false,
	    batch : true,
	    enableServerHandling:true,
	    resizable: true,
	    filterable: true,
	    serverFiltering : false,
		schema : {
			data : "result",
			total : function(response) {
				return response.result.length;
			},
			pageSizes : 10
		},
		page : 1
	});
	
	
	settings.websiteData = new kendo.data.DataSource({
		transport : {
			read : {
				url :function(options){
            		if (options.filter){
            			var configure = filter.configure(options.filter);
            			return httpPrefix + "/website/" + options.page + "/" + options.pageSize + "?configure=" +configure;
            		}
            		return httpPrefix + "/website/" + options.page + "/" + options.pageSize;
            	},
				type : "GET"
			}
		},
		page:1,
	    pageSize : 10, //每页显示个数
	    serverPaging: true,
	    batch : true,
	    enableServerHandling:true,
	    resizable: true,
	    filterable: true,
	    serverFiltering : false,
		schema : {
			data : "result",
			total : function(response) {
				return response.count;
			},
			pageSizes : 10
		},
		page : 1
	});
	settings.columns = [
	                    {
	                    	field: "id",
	                    	title: "序号",
	                    	width: 40,
	                    	filterable: false
	                    },
	                    {
	                    	field: "Subject_Name",
	                    	title: "主题名称",
	                    	width: 60,
	                    },
	                    {
	                    	field:"focuskeyword_A",
	                    	title:"关键词",
	                    	width: 80,
	                    	filterable: false
	                    },
	                    {
	                    	field:"tosearch_keyword",
	                    	title:"搜索词",
	                    	width:80,
	                    	filterable: false
	                    },{
	                    	field:"Subject_ID",
	                    	hidden : true
	                    },
	                    {
	                    	field: "Created_Time",
	                    	title: "创建时间",
	                    	width: 80,
	                    	filterable: false
	                    },
	                    {
	                    	command:[
	                    	         {
	                 					name : "editTopic",
	                					text : '<span class="k-icon k-edit"></span>' + fsn.l("修改"),
	                					click : function(e) {
	                						var tr = $(e.target).closest("tr");
	                						var data = this.dataItem(tr);
	                						settings.editTopic(data);
	                					}
	                				},{
	                 					name : "delTopic",
	                					text : '<span class="k-icon k-edit"></span>' + fsn.l("删除"),
	                					click : function(e) {
	                						var tr = $(e.target).closest("tr");
	                						var data = this.dataItem(tr);
	                						settings.delTopicConfirm(data);
	                					}
	                				}
	                    	         ],
	                    	        title : "操作",
	                 				width : 60
	                    }
	                    ];
	
	
	settings.webSiteColum = [
	                         {
	                        	 field: "id",
	                        	 title: "序号",
	                        	 width: 80
	                         },
	                         {
	                        	 field: "websiteName",
	                        	 title: "网站名称",
	                        	 width: 300
	                         },
	                         {
	                        	 field: "websiteUrl",
	                        	 title: "网站地址",
	                        	 width: 300
	                         },
	                         {
	                        	 field:"organizationName",
	                        	 title:"组织机构名称",
	                        	 width: 300
	                         },
	                         {
	                        	 command:[
	                        	          {
	                        	        	name : "editTopic",
	  	                					text : '<span class="k-icon k-edit"></span>' + fsn.l("修改"),
	  	                					click : function(e) {
	  	                						var tr = $(e.target).closest("tr");
	  	                						var data = this.dataItem(tr);
	  	                						settings.editWebsite(data);
	  	                					}
	                        	          }
	                        	          ]
	                         }
	                         ];
	settings.buildGrid = function(id, columns, width, height, ds) {
		var element = $("#" + id).kendoGrid({
			dataSource : ds == undefined ? [] : ds,
			height : height == null ? 400 : height,
			width : width == null ? 600 : width,
			sortable : false,
			selectable : true,
			resizable : true,
			filterable : {
				messages : fsn.gridfilterMessage(),
				operators : {
					string : fsn.gridfilterOperators()
				}
			},
			pageable : {
				refresh : true,
				pageSizes : 10,
				messages : fsn.gridPageMessage(),
			},
			columns : columns
		});
	};
	
	settings.init = function(){
		$("#addBtn").bind("click", function(){
			settings.addTopic();
		});
		
		$("#btn_save").bind("click", function(){
			settings.saveTopic();
		});
		
		$("#addNetworkBtn").bind("click", function(){
			$("#website_name").val("");
			$("#website_url").val("");
			$("#websiteId").val("");
			var window = $("#webSitePopup").data("kendoWindow");
			window.open();
		});
		
		$("#website_btn_save").bind("click", function(){
			settings.saveWebsite();
		});
		
		$("#btn_reset").bind("click", function(){
			$("#topic_name").val("");
			$("#category_key").val("");
			$("#search_key").val("");
		});
		
		$("#website_btn_reset").bind("click", function(){
			$("#website_name").val("");
			$("#website_url").val("");
		});
		
		settings.initPopUp();
		settings.buildGrid("grid", settings.columns, null, "500px", settings.dataSource);
		//settings.buildGrid("website-grid", settings.webSiteColum, null, "500px", settings.websiteData);
	};
	
	settings.initPopUp = function(){
		var window = $("#topicPopup");
		if (!window.data("kendoWindow")) {
			window.kendoWindow({
				visible : false,
				resizable : false,
				width: 650,
				minHeight:280,
				title: "主题配置",
				open : settings.popupOnOpen,
				close : settings.popupOnClose
			});
		}
		window.data("kendoWindow").center();
		
		var webSitewindow = $("#webSitePopup");
		if (!webSitewindow.data("kendoWindow")) {
			webSitewindow.kendoWindow({
				visible : false,
				resizable : false,
				width: 400,
				minHeight:200,
				title: "监测网站",
				open : settings.popupOnOpen,
				close : settings.popupOnClose
			});
		}
		webSitewindow.data("kendoWindow").center();
	};
	
	settings.popupOnOpen = function(e) {
		$("#placeholder").html('<div class="k-overlay" style="z-index: 10002; display: block; opacity: 0.5;"></div>');
	}

	settings.popupOnClose = function(e) {
		$("#placeholder").html("");
	};
	
	settings.addTopic = function(){
		$("#topic_name").val("");
		$("#category_key").val("");
		$("#search_key").val("");
		settings.displayTopics();
		var window = $("#topicPopup").data("kendoWindow");
		window.open();
	
	};
	
	settings.displayTopics = function(){
		var topicNames = "<strong>已存在的主题名称: </strong>";
		if(settings.dataSource){
			for(var i=0; i<settings.dataSource.data().length; i++){
				topicNames += settings.dataSource.data()[i].Subject_Name + ", ";
			}
		}
		$("#topic_list").empty();
		$("#topic_list").append(topicNames);
	};
	
	settings.saveTopic = function(){
		var data = {};
		data.topicId = $("#topicId").val();
		data.topicName = $("#topic_name").val();
		data.keyword = $("#category_key").val();
		data.searchWords = $("#search_key").val();
		if(data.topicName ==null || data.topicName.trim().length == 0){
			fsn.initNotificationMes("主题名称不能为空", false);
			return false;
		}
		var type = data.topicId != "" ? "PUT": "POST";
		$.ajax({
			url: httpPrefix + "/setimentTopic",
			type: type,
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(data),
			success: function(result){
				if(result.status == true){
					var msg = data.topicId == "" ? "新增主题成功" : "更新主题成功";
					fsn.initNotificationMes(msg, true);
					settings.dataSource.read();
					var grid = $("#grid").data("kendoGrid");
					grid.refresh();
				}else{
					fsn.initNotificationMes(result.msg, false);
				}
				
				var window = $("#topicPopup").data("kendoWindow");
				window.close();
			}
		});
		
	};
	
	settings.delTopicConfirm = function(data){
		fsn.initConfirmWindow(function(){
			settings.delTopic(data.Subject_ID);
		}, null, "信息删除后不能回复，只能重新添加，是否仍需要删除【" + data.Subject_Name + "】主题", false);
		 $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
	};
	
	settings.delTopic = function(topicId){
		$.ajax({
			url:  httpPrefix + "/setimentTopic/"+ topicId,
			type: "DELETE",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			success: function(data){
				if(data.status == true){
					fsn.initNotificationMes("添加成功", true);
					settings.dataSource.read();
					var grid = $("#grid").data("kendoGrid");
					grid.refresh();
				}else{
					fsn.initNotificationMes(result.msg, false);
				}
			}
			
		});
	};
	
	settings.editTopic = function(data){
		$("#topicId").val(data.Subject_ID);
		$("#topic_name").val(data.Subject_Name);
		$("#category_key").val(data.focuskeyword_A);
		$("#search_key").val(data.tosearch_keyword);
		settings.displayTopics();
		var window = $("#topicPopup").data("kendoWindow");
		window.open();
	};
	
	settings.saveWebsite = function(){
		var website_name = $("#website_name").val();
		var website_url = $("#website_url").val();
		var website_id = $("#websiteId").val();
		var flag = false;
		$("#notify").empty();
		if(website_name.trim() == ""){
			$("#website_notify").html("网站名称不能为空");
			return false;
		}
		
		if(website_url.trim() == ""){
			$("#website_notify").html("网站地址不能为空");
			return false;
		}
		
		var re=new RegExp("^((https|http|ftp|rtsp|mms)?://)?[A-Za-z0-9-_]+\\.[A-Za-z0-9-_%&\?\/.=]+$");
		if(!re.test(website_url.trim())){
			$("#website_notify").html("网站地址格式不正确");
			return false;
		}
		
		// check website unique
		$.ajax({
			url: httpPrefix + "/website/checkUnique",
			type: "GET",
			dataType: "json",
			async: false,
			data:{"url":website_url.trim(),
				  "website_id":website_id},
			success: function(data){
				if(data.status){
					flag = data.result;
				}else{
					fsn.initNotificationMes(data.msg, false);
				}
			}
		});
		if(flag){
			var website = {};
			website.websiteName = website_name.trim();
			website.websiteUrl = website_url.trim();
			website.id= website_id;
			var type = website_id != "" ? "PUT": "POST";
			$.ajax({
				url:  httpPrefix + "/website",
				type: type,
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(website),
				success: function(data){
					if(data.status == true){
						if(type == "POST"){
							fsn.initNotificationMes("新增监测网站成功", true);
						}else{
							fsn.initNotificationMes("更新监测网站成功", true);
						}
						
						settings.websiteData.read();
						var grid = $("#website-grid").data("kendoGrid");
						grid.refresh();
					}else{
						fsn.initNotificationMes(data.msg, false);
					}
					
					var window = $("#webSitePopup").data("kendoWindow");
					window.close();
				}
			});
		}else{
			fsn.initNotificationMes("改检测站点已存在", true);
		}
	};
	
	settings.editWebsite = function(e){
		$("#website_name").val(e.websiteName);
		$("#website_url").val(e.websiteUrl);
		$("#websiteId").val(e.id);
		var window = $("#webSitePopup").data("kendoWindow");
		window.open();
	}
	 $(document).ready(function() {
		 settings.tabStrip = $("#tabstrip").kendoTabStrip({
			 animation : {
				 open : {
					 effects : "fadeIn"
				}
			}
		}).data("kendoTabStrip");
		 
		 
		 settings.init();
		 settings.myGroupWebsiteInit();
		 settings.myWebsiteInit();
	 });
	 
	
	 var index = 0;
	 settings.myGroupWebsiteInit = function(){
		 
		 settings.myGroupWebsiteDataSource = new kendo.data.DataSource({
				transport : {
					read : {
						url :function(options){
							index = 0;
		            		return httpPrefix + "/website/website_list";
		            	},
						type : "GET"
					}
				},
				page:1,
			    pageSize : 10, //每页显示个数
			    serverPaging: false,
			    batch : true,
			    enableServerHandling:true,
			    resizable: true,
			    filterable: true,
			    serverFiltering : false,
				schema : {
					data : "result",
					total : function(response) {
						if("true"==response.status){
							return response.result.length;
						}else{
							fsn.initNotificationMes("获取监管网站失败："+response.msg+"！",false);
							return 0;
						}
					},
					pageSizes : 10
				},
				page : 1
			});
		 
		 settings.groupWebsiteGrid = $("#mygroup_website_grid").kendoGrid({
				dataSource:settings.myGroupWebsiteDataSource,
				selectable : "row",
				sortable : true,
				resizable : true,
				height: 489,
				 toolbar: [
				           { template: kendo.template($("#template").html()) }
				],
				//autoBind: false,
				//toolbar: ["create"],
				dataBinding: function(e) {
					    index= 0;
					    console.log(index);
				},
				 dataBound: function(e) {
					 if(settings.myGroupWebsiteDataSource.data().length==0){
						 settings.tabStrip.remove("li:eq(1)");
					 }
				},
				filterable : {
					messages : fsn.gridfilterMessage(),
					operators : {
						string : fsn.gridfilterOperators()
					}
				},
				pageable : {
					refresh : true,
					pageSizes : 10,
					messages : fsn.gridPageMessage(),
				},
				editable:{
					 mode: "inline",
				},
				columns : [{
	            	 title: "序号",
	            	 width: 60,
                 	 filterable: false,
                 	 template:function(e){
                 		 if(index==0){
                 			index = settings.myGroupWebsiteDataSource.pageSize()*(settings.myGroupWebsiteDataSource.page()-1);
                 		 }
                 		 index++;
                 		 return index;
        			 },
	             },{
	            	 field: "website_Name",
	            	 title: "网站名称",
                 	// filterable: false
	             },{
	            	 field: "website_Main_Page_URL",
	            	 title: "网站地址",
                 	 //filterable: false,
                 	 template:function(e){
                 		 return '<a href="'+e.website_Main_Page_URL+'" target="_blank">'+e.website_Main_Page_URL+'</a>';
        			 },
	             },{
	            	 field:"orgName",
	            	 title:"组织机构名称",
                 	 filterable: false
             }]
			}).data("kendoGrid");
		 
		    $("#webSietAdd").bind("click", function(){
		    	 settings.tabStrip.select("li:last");
		   });
	 };
	 
	 
	 settings.myWebsiteInit = function(){
		 settings.myWebsiteDataSource = new kendo.data.DataSource({
				transport:{
					 read:  {
						 type : "POST",
		                 url: httpPrefix + "/website/list",
		                 dataType : "json",
						 contentType : "application/json; charset=utf-8"
		             },
		             update: {
		            	 type : "POST",
		            	  url: httpPrefix + "/website/update",
		                 dataType : "json",
						 contentType : "application/json; charset=utf-8"
		             },
		             destroy: {
		            	 type : "GET",
		            	  url: httpPrefix +"/website/delete",
		                 dataType : "json",
						 contentType : "application/json; charset=utf-8"
		             },
		             create: {	
		            	 type : "POST",
		            	 url: httpPrefix +  "/website/create",
		                 dataType : "json",
						 contentType : "application/json; charset=utf-8"
		             },
		             parameterMap: function(options, operation) {
		            	 var page = new fsn.Page();
		            	if (operation == "read"){
		            		page.page =options.page;
		            		page.pageSize = options.pageSize;
		            		if(options.sort){
		            			page.sort=options.sort[0];
		            		}
		            		if(options.filter){
		            			page.filter=options.filter;
		                	}
		            
		            		return kendo.stringify(page);
		            	}
		            	if(operation == "create"&&options.models){
		            		 return kendo.stringify(options.models[0]);
		            	}
		            	if(operation=="update"&&options.models){
		            		  console.log(options);
		            		return kendo.stringify(options.models[0]);
		            		
		            	}
		            	if(operation=="destroy"&&options.models){
		            		return "id="+options.models[0].id;
		            		//return kendo.stringify({id:options.models[0].id});
		            	}
		             }
				},
				schema:{
					model:{
						id: "id",
						fields:{
							id: { editable: false, nullable: true },
							websiteName: { validation: { required:{ message: "网站名称不能为空!" },}},
							websiteUrl: { validation: { required:{ message: "网站地址不能为空!" },url:{message:"网址有误!"},
								strTrimValidation:function(input){
									if(input.val().indexOf(" ")>=0){
										 input.attr("data-strTrimValidation-msg", "不能包含空格");
										 return false;
									}
									return true;
								},
							} },
							organizationName: { editable: false },
						}
					},
					data :function(d){
						if(d.type=="read"){
							return d.list;
						}
						if(d.type=="create"){
							fsn.initNotificationMes(d.message,d.status);
							settings.myWebsiteDataSource.read();
						}
						if(d.type=="update"){
							fsn.initNotificationMes(d.message,d.status);
							settings.myWebsiteDataSource.read();
						}
						if(d.type=="delete"){
							fsn.initNotificationMes(d.message,d.status);
						}
						
					},
					total:function(d){
						return d.counts;
					}
				},
		        batch : true,
		        page:1,
		        pageSize :10, //每页显示个数
		        serverPaging : true,
		        serverFiltering : true,
		        serverSorting : true
			});
		 
		 
		 settings.myWebsiteGrid = $("#website-grid").kendoGrid({
				dataSource:settings.myWebsiteDataSource,
				selectable : "row",
				sortable : true,
				resizable : true,
				height: 489,
				toolbar: ["create"],
				filterable: { 
					extra: false,
					messages: fsn.gridfilterMessage(),
					operators: {
						string: fsn.gridfilterOperators(),
					    date: fsn.gridfilterOperatorsDate(),
					    number: fsn.gridfilterOperatorsNumber()
					}
				},
				save: function(e){
					if(oldData.websiteName!=e.model.websiteName){
						var data = {
								websiteName:e.model.websiteName,
							};
						var result = settings.validation(data);
						if(result.status){
							fsn.initNotificationMes("网站名称重复！",false);
							e.preventDefault();
							return;
						}
					}
					
					if(oldData.websiteUrl!=e.model.websiteUrl){
						var data_1 = {
								websiteUrl:e.model.websiteUrl,
						};
						
						var result = settings.validation(data_1);
						if(result.status){
							 var msg="网站地址重复";
							 if(result.message){
								 msg = result.message;
							 }
							 fsn.initNotificationMes(msg,false);
							 e.preventDefault();
							 return;
						};
					};
				},
				edit:function(e){
					if (!e.model.isNew()) {
						oldData = $.extend(true, {}, e.model);
					 }else{
						 oldData = "";
					 }
				
				},
				messages:{
					commands:{
						create: "添加",
						edit: "编辑",
						update: "保存",
						destroy: "删除",
						cancel:"取消",
						canceledit: "取消编辑"
					}
				},
				pageable : {
					refresh : true,
					pageSizes : true,
					messages : fsn.gridPageMessage(),
				},
				editable: "inline",
				columns : [{
					 field: "websiteName",
                	 title: "网站名称",
				},{
				 	 field: "websiteUrl",
                	 title: "网站地址",
                	 template:function(e){
                 		 return '<a href="'+e.websiteUrl+'" target="_blank">'+e.websiteUrl+'</a>';
        			 },
                	 editor: function(container, options){
                			var input = $('<input  type="url" name="' + options.field + '" required data-required-msg="网站地址不能为空!"  data-url-msg="网站地址输入有误!" placeholder="https://www.baidu.com/"/>');
                			input.appendTo(container);
                			var textBox = input.kendoMaskedTextBox({
                			}).data("kendoMaskedTextBox");
                			var tooltipElement = $('<span class="k-invalid-msg" data-for="' + options.field + '"></span>');
    						tooltipElement.appendTo(container);
                	 }
				},{
					 field:"organizationName",
                	 title:"组织机构名称",
                	 filterable: false,
				},{
					command: ["edit",{
						name:"versionDelete",
						text:"删除",
						click : function(e) {
					/*		 var notificationElement = $("#RETURN_MES").kendoNotification({
								 height: 50,
								 left: 30,
								 width: 300,
								 position: {
								        bottom:80,
								    }
							 }).data('kendoNotification');;
							 notificationElement.show('欢迎访问极客标签!','error');*/
							var tr = $(e.target).closest("tr");
							var data = this.dataItem(tr);
							$.MsgBox.Confirm("提示","确认需要删除【"+data.websiteName+"】?",function(){
								$.ajax({
									   url: httpPrefix + "/website/delete?id="+data.id,
										dataType : "json",
										type : "GET",
										success : function(d) {
											fsn.initNotificationMes(d.message,d.status);
											if (d.status) {
												settings.myWebsiteDataSource.read();
											}
										}
								});
							});
						}
					}]
				}]
			}).data("kendoGrid");
		 
		 	settings.validation = function(data){
				var result="";
				$.ajax({
					    url:httpPrefix + "/website/unique",
						dataType : "json",
						type : "POST",
						async: false,
						contentType : "application/json; charset=utf-8",
						data:kendo.stringify(data),
						success : function(d) {
							result = d;
						}
					});
				return result;
			};
			
	 };
	 
	
});