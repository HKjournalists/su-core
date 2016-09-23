$(function() {
	var upload = window.fsn.upload = window.fsn.upload || {};
	var brand = upload.brand = upload.brand || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = fsn.getContextPath(); // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	brand.initialize = function(){
		upload.buildGrid("brand",this.templatecolumns, this.templateDS(""));
		$("#brand .k-grid-content").attr("style","height: 364px");
	};
	
	brand.templatecolumns = [
	                 {field: "id",title: lims.l("Id"),width: 35},
                     {field: "name",title: lims.l("Name"),width: 90},
                     {field: "brandCategory.name",title: "品牌类型",width: 110},
                     {field: "businessUnit.name",title: lims.l("Owned Business Unit"),width: 80},
                     {field: "registrationDate",title: lims.l("Registration Date"),width: 50},
                     { command: [{
                                	  name:"edit",
    			                      text: lims.localized("Edit"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  brand.edit(temp.id);
									  }
								  },
					     ], title:lims.l("Operation"), width: 40 }
                     ];
	brand.templateDS = function(keyword){
		return new kendo.data.DataSource({
			transport: {
	            read : {
	                type : "GET",
	                url : function(options){
	                	var configure = null;
	                	if(options.filter){
	            			configure = filter.configure(options.filter);
	            		}
	                	return portal.HTTP_PREFIX + "/business/business-brand/getMyBrands/" + configure + "/" + options.page + "/" + options.pageSize;
	                },
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	 data : function(returnValue) {
	        		 var listOfBrand = returnValue.data.listOfBrand;
	        		 for(var i=0; i<listOfBrand.length; i++){
	        			 if(listOfBrand[i].brandCategory == null){
	        				 listOfBrand[i].brandCategory = {name:"无"};
	        			 }
	        		 }
	                 return listOfBrand;  //响应到页面的数据
	             },
	             total : function(returnValue) {       
	            	 return returnValue.data.counts;   //总条数
	             }         
	        },
	        batch : true,
	        page:1,
	        pageSize : 10, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};

	brand.edit = function(id){
		var material = {
			id : id
		};
		$.cookie("user_0_edit_brand", JSON.stringify(material), {
			path : '/'
		});
		window.location.pathname = "/fsn-core/views/business/brand.html";
	};
	
	brand.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = brand.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
	};
	brand.createNewTemp = function(){
		window.location.pathname = "/lims-core/views/process/template_management.html";
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