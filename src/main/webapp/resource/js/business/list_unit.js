$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var unit = upload.unit = upload.unit || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	var deleteItem;
	unit.templatecolumns = [
					 { command: [{
						name: "Check",
					    text: "<input type='checkbox' class='checkboxes'/>"
					 },],  width: 40 },
	                 {field: "unitId",title: lims.l("Id"),width: 50,filterable: false},
                     {field: "unitName",title: lims.l("Name"),width: 110,filterable: false},
                     {field: "unitAddress",title: lims.l("Address"),width: 80,filterable: false},
                     {field: "unitLicenseNo",title: lims.l("Registration Id"),width: 50,filterable: false},
                     {field: "unitContact",title: lims.l("Contact Way"),width: 80,filterable: false},
                     { command: [{
                                	  name:"edit",
    			                      text: lims.localized("Edit"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  window.location.href = portal.CONTEXT_PATH+"/views/business/unit.html?id="+temp.unitId;
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
					    							url:portal.HTTP_PREFIX + "/portal/upload?type=unit&id_list=:"+deleteItem.unitId,
					    							type:"DELETE",
					    							success:function(data){
					    								if(data.RESTResult.status == 1){
					    									$("#unit").data("kendoGrid").dataSource.remove(deleteItem);
					    									lims.message("msg_success", true, '<span class="font_blue_underline">'+deleteItem.unitName+'</span>' + lims.l("Delete Success"));
					    								}else{
					    									var result={success:false};
					    									lims.message("msg_success", result, '<span class="font_red_underline">'+deleteItem.unitName+'</span>' + lims.l("Delete Failed"));
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
	unit.templateDS = function(keyword){
		return new kendo.data.DataSource({
			transport: {
	            read : {
	                type : "GET",
	                async:false,
	                url : function(options){
	                	return portal.HTTP_PREFIX+"/portal/upload/search?type=unit&keyword="+keyword+"&sn="+options.page;
	                },
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	          		 fields: {
	          			   unitId: {
	                           type: "number"
	                       },
	                       unitName: {
	                           type: "string"
	                       },
	                       unitAddress: {
	                           type: "string"
	                       },
	                       unitLicenseNo: {
	                       		type: "string"
	                       },
	                       unitContact: {
	                       		type: "string"
	                       },
	                   }
	               },
	        	 data : function(d) {
	                 return d.RESTResult.status==1?unit.dataFormat(d.RESTResult.data.list):null;  //响应到页面的数据
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
	unit.initialize = function(){
		var dataSource = this.templateDS("");
		upload.buildGrid("unit",this.templatecolumns, dataSource);
		//upload.addCheckBox();
		$('#search').kendoSearchBox({
			change:function(e){
				var keyword = e.sender.options.value;
				//if(keyword.trim().length){
				unit.searchTemplateByKeywords("unit", keyword);
				//}
			}
		}); //Search Box
		$("#unit .k-grid-content").attr("style","height: 364px");
		//$("#templates").data("kendoGrid").refresh();
		
	};
	unit.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = unit.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
		//upload.addCheckBox();
	};
	unit.createNewUnit = function(){
		window.location.href = portal.CONTEXT_PATH+"/views/business/unit.html";
	};
	unit.dataFormat=function(list){
		var unitList=[];
		for(var i=0;i<list.length;i++){
			var unit_obj={};
			unit_obj["unitId"]=list[i].id;
			unit_obj["unitName"]=list[i].name;
			unit_obj["unitAddress"]=list[i].address;
			unit_obj["unitLicenseNo"]=list[i].licenseNo;
			unit_obj["unitContact"]=list[i].contact;
			unitList.push(unit_obj);
		}
		return unitList;
	};
	unit.initialize();
	$("#main").on("click","#batchDelete",function(){
		var _unitIds="",_checkItem=$("input.checkboxes:checked");
		if(_checkItem.length==0){
			alert(lims.l("Choose Unit"));
		}else{
			$("input.checkboxes:checked").each(function(){
				_unitIds+=":"+$(this).closest("td").next("td").text();
			});
			lims.initConfirmWindow(function() {
					lims.clrmsg("msg_success",null);
		      		$.ajax({
		      			url:portal.HTTP_PREFIX + "/portal/upload?type=unit&id_list="+_unitIds,
		    					type:"DELETE",
		    					success:function(data){
		    						if(data.RESTResult.status == 1){
		    							unit.searchTemplateByKeywords("unit", $("#search").val());
		    							lims.message("msg_success", true, '"<span class="font_blue_underline">'+lims.l("Selected Unit")+'</span>"' + lims.l("Delete Success"));
		    						}else{
		    							var result={success:false};
		    							lims.message("msg_success", result, '"<span class="font_red_underline">'+lims.l("Selected Unit")+'</span>"' + lims.l("Delete Failed"));
		    						}
		    					}
		      			});	  		
					lims.closeConfirmWin();
			}, undefined,lims.l("WIN_BATCHCONFIRM_MSG"),true);
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
		};
	});
});