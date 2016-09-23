$(function() {
	"use strict";
	var upload = window.lims.upload = window.lims.upload || {};
	var cert = upload.cert = upload.cert || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	var deleteItem;
	cert.templatecolumns = [{
		command: [{
			name: "Check",
			text: "<input type='checkbox' class='checkboxes'/>"
		}], width: 20,
    	encoded : false},
		{field: "id",title: lims.l("Id"),width: 50,filterable: false},
		{field: "name",title: lims.l("Name"), width: 110, filterable: false},
		{field: "message",title: lims.l("Message"), width: 110, filterable: false},
		{command: [{
				name:"edit",
				text: lims.l("Edit"),
				click: function(e){
					var editRow = $(e.target).closest("tr");
					var temp = this.dataItem(editRow);
					window.location.href = portal.CONTEXT_PATH+"/ui/portal/certification?id="+temp.id;
				}
			},
			{
				name: "Delete",
				text: "<span class='k-icon k-cancel'></span>"+ lims.l("Delete"),
				click: function(e){
					lims.clrmsg("msg_success",null);
					var deleteRow = $(e.target).closest("tr");
					deleteItem = this.dataItem(deleteRow);
					lims.initConfirmWindow(function() {
						lims.clrmsg("msg_success",null);
						$.ajax({
							url:portal.HTTP_PREFIX + "/portal/upload?type=cert&id_list=:"+deleteItem.id,
							type:"DELETE",
							success:function(data){
								if(data.RESTResult.status == 1){
									$("#certification").data("kendoGrid").dataSource.remove(deleteItem);
									lims.message("msg_success", true, '<span class="font_blue_underline">'+deleteItem.name+'</span>' + lims.l("Delete Success"));
								}else{
									var result={success:false};
									lims.message("msg_success", result, '<span class="font_red_underline">'+deleteItem.name+'</span>' + lims.l("Delete Failed"));
								}
							}
						});
						lims.closeConfirmWin();
					}, undefined , lims.l("WIN_CONFIRM_MSG"), true);
					
					$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
				}
			}], 
			title:lims.l("Operation"), width: 70 }];
	cert.templateDS = function(keyword){
		return new kendo.data.DataSource({
			transport: {
	            read : {
	                type : "GET",
	                async:false,
	                url : function(options){
	                	return portal.HTTP_PREFIX+"/portal/upload/search?type=cert&keyword="+keyword+"&sn="+options.page;
	                },
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	          		 fields: {
	          			   id: {type: "number"},
	                       name: {type: "string"},
	                       message: {type: "string"},
	                   }
	               },
	        	 data : function(d) {
	                 return d.RESTResult.status==1?cert.dataFormat(d.RESTResult.data.list):null;  //响应到页面的数据
	             },
	             total : function(d) {
	                 return d.RESTResult.status==1?d.RESTResult.data.totalrecord:0;   //总条数
	             }         
	        },
	        batch : true,
	        page:1,
	        pageSize : 15, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	cert.initialize = function(){
		var dataSource = this.templateDS("");
		upload.buildGrid("certification",this.templatecolumns, dataSource);
		//upload.addCheckBox();
		$('#search').kendoSearchBox({
			change:function(e){
				var keyword = e.sender.options.value;
				//if(keyword.trim().length){
				cert.searchTemplateByKeywords("certification", keyword);
				//}
			}
		}); //Search Box
		
		$("#certification .k-grid-content tr td:first-child a").removeAttr("class");
		$("#certification .k-grid-content").attr("style","height: 364px");
		
	};
	cert.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = cert.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
		//upload.addCheckBox();
	};
	cert.createNewcert = function(){
		window.location.href = portal.CONTEXT_PATH+"/ui/portal/certification";
	};
	cert.dataFormat=function(list){
		var certList=[];
		for(var i=0;i<list.length;i++){
			var cert={};
			cert["id"]=list[i].id;
			cert["name"]=list[i].name;
			cert["message"]=list[i].message;
			certList.push(cert);
		}
		return certList;
	};
	cert.initialize();
	$("#main").on("click","#batchDelete",function(){
		var _certIds="",_checkItem=$("input.checkboxes:checked");
		if(_checkItem.length==0){
			alert(lims.l("Choose certification"));
		}else{
			$("input.checkboxes:checked").each(function(){
				_certIds+=":"+$(this).closest("td").next("td").text();
			});
			lims.initConfirmWindow(function() {
					lims.clrmsg("msg_success",null);
		      		$.ajax({
		      			url:portal.HTTP_PREFIX + "/portal/upload?type=cert&id_list="+_certIds,
		    					type:"DELETE",
		    					success:function(data){
		    						if(data.RESTResult.status == 1){
		    							cert.searchTemplateByKeywords("certification", $("#search").val());
		    							lims.message("msg_success", true, '"<span class="font_blue_underline">'+lims.l("Selected Certification")+'</span>"' + lims.l("Delete Success"));
		    						}else{
		    							var result={success:false};
		    							lims.message("msg_success", result, '"<span class="font_red_underline">'+lims.l("Selected Certification")+'</span>"' + lims.l("Delete Failed"));
		    						}
		    					}
		      			});	  		
					lims.closeConfirmWin();
			}, undefined,lims.l("WIN_BATCHCONFIRM_MSG"),true);
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
		};
	});
});