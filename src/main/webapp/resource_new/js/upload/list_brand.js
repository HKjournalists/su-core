$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var brand = upload.brand = upload.brand || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	var deleteItem;
	brand.templatecolumns = [
						{ command: [{
							name: "Check",
						    text: "<input type='checkbox' class='checkboxes'  />"
						},
						],  width: 40 },
	                 {field: "brandId",title: lims.l("Id"),width: 50,filterable: false},
                     {field: "brandName",title: lims.l("Name"),width: 110,filterable: false},
                     {field: "ownedUnit",title: lims.l("Owned Business Unit"),width: 80,filterable: false},
                     {field: "brandRegistrationDate",title: lims.l("Registration Date"),width: 50,filterable: false},
                     { command: [{
                                	  name:"edit",
    			                      text: lims.localized("Edit"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  window.location.href = portal.CONTEXT_PATH+"/ui/portal/brand?id="+temp.brandId;
									  }
								  },
					            {
								  name: "Delete",
			                      text: "<span class='k-icon k-cancel'></span>"+ lims.l("Delete"),
					          	  click: function(e){
					          		//$("#msg_success").html('');
					          		lims.clrmsg("msg_success",null);
					          		var deleteRow = $(e.target).closest("tr");
					          		deleteItem = this.dataItem(deleteRow);
					          		lims.initConfirmWindow(function() {
					        			lims.clrmsg("msg_success",null);
					        	      		  $.ajax({
					        	    					url:portal.HTTP_PREFIX + "/portal/upload?type=brand&id_list=:"+deleteItem.brandId,
					        	    					type:"DELETE",
					        	    					success:function(data){
					        	    						if(data.RESTResult.status == 1){
					        	    							$("#brand").data("kendoGrid").dataSource.remove(deleteItem);
					        	    							lims.message("msg_success", true,'"<span class="font_blue_underline">'+deleteItem.brandName+'</span>"' + lims.l("Delete Success"));
					        	    						}else{
					        	    							var result={success:false};
					        	    							lims.message("msg_success", result,'"<span class="font_red_underline">'+deleteItem.brandName+'</span>"' + lims.l("Delete Failed"));
					        	    						}
					        	    					}
					        						});	  		
					        			lims.closeConfirmWin();
					        	}, undefined,lims.l("WIN_CONFIRM_MSG"),true);
						          	$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
					          	  }
					            }
					            ], title:lims.l("Operation"), width: 70 }
                     ];
	brand.templateDS = function(keyword){
		return new kendo.data.DataSource({
			transport: {
	            read : {
	                type : "GET",
	                url : function(options){
	                	return portal.HTTP_PREFIX+"/portal/upload/search?type=brand&keyword="+keyword+"&sn="+options.page;
	                },
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	          		 fields: {
	          			   brandId: {
	                           type: "number"
	                       },
	                       brandName: {
	                           type: "string"
	                       },
	                       ownedUnit: {
	                           type: "string"
	                       },
	                       brandRegistrationDate: {
	                       		type: "string"
	                       },
	                   }
	               },
	        	 data : function(d) {
	                 return d.RESTResult.status==1?brand.dataFormat(d.RESTResult.data.list):null;  //响应到页面的数据
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
	brand.initialize = function(){
		var dataSource = this.templateDS("");
		upload.buildGrid("brand",this.templatecolumns, dataSource);
		$('#search').kendoSearchBox({
			change:function(e){
				var keyword = e.sender.options.value;
				//if(keyword.trim().length){
				brand.searchTemplateByKeywords("brand", keyword);
				//}
			}
		}); //Search Box
		
		$("#brand .k-grid-content tr td:first-child a").removeAttr("class");
		$("#brand .k-grid-content").attr("style","height: 364px");
		//$("#templates").data("kendoGrid").refresh();
	};
	brand.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = brand.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
	};
	brand.createNewTemp = function(){
		window.location.pathname = "/lims-core/views/process/template_management.html";
	};
	brand.dataFormat=function(list){
		var brandList=[];
		for(var i=0;i<list.length;i++){
			var brand_obj={};
			brand_obj["brandId"]=list[i].id;
			brand_obj["brandName"]=list[i].name;
			brand_obj["ownedUnit"]=list[i].businessUnit.name;
			brand_obj["brandRegistrationDate"]=list[i].registrationDate;
			brandList.push(brand_obj);
		}
		return brandList;
	};
	brand.initialize();
	$("#main").on("click","#batchDelete",function(){
		var _brandIds="",_checkItem=$("input.checkboxes:checked");
		if(_checkItem.length==0){
			alert(lims.l("Choose Brand"));
		}else{
			$("input.checkboxes:checked").each(function(){
				_brandIds+=":"+$(this).closest("td").next("td").text();
			});
			lims.initConfirmWindow(function() {
					lims.clrmsg("msg_success",null);
		      		$.ajax({
		      			url:portal.HTTP_PREFIX + "/portal/upload?type=brand&id_list="+_brandIds,
    					type:"DELETE",
    					success:function(data){
    						if(data.RESTResult.status == 1){
    							brand.searchTemplateByKeywords("brand", $("#search").val());
    							lims.message("msg_success", true,'"<span class="font_blue_underline">'+lims.l("Selected Brand")+'</span>"' + lims.l("Delete Success"));
    						}else{
    							var result={success:false};
    							lims.message("msg_success", result,'"<span class="font_red_underline">'+lims.l("Selected Brand")+'</span>"' + lims.l("Delete Failed"));
    						}
    					}
		      		});	  		
					lims.closeConfirmWin();
			}, undefined,lims.l("WIN_BATCHCONFIRM_MSG"),true);
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
		};
	});
});