$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var brand = upload.brand = upload.brand || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = fsn.getContextPath(); // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	var deleteItem;
	brand.templatecolumns = [{field: "id",title: lims.l("Id"),width: 50},
                     {field: "name",title: lims.l("Name"),width: 50},
                     {field: "businessUnit.name",title: lims.l("Owned Business Unit"),width: 100},
                     {field: "registrationDate",title: lims.l("Registration Date"),width: 80},
                     { command: [{name:"View",
    			                  text: lims.localized("View"),
    			                  click: function(e){
									var editRow = $(e.target).closest("tr");
									var temp = this.dataItem(editRow);
									brand.viewBrandInfo(temp.id);
									  }},], title:lims.l("Operation"), width: 30 }
                     ];
	
	brand.templateDS = function(){
		return new kendo.data.DataSource({
			transport: {
	            read : {
	                type : "GET",
	                url : function(options){
	                	var configure = null;
	                	if(options.filter){
	            			configure = filter.configure(options.filter);
	            		}
	                	return portal.HTTP_PREFIX + "/business/business-brand/getAllBranByParenOgranizationId/" + configure + "/" + options.page + "/" + options.pageSize;
	                },
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	 data : function(data) {
	                 return data.listBrand;;  //响应到页面的数据
	             },
	             total : function(data) {       
	            	 return data.counts;   //总条数
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
	
	brand.buildGrid = function (id,columns,ds){
		var element = $("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				extra: false,
				messages: lims.gridfilterMessage(),
				operators: {
					string: lims.gridfilterOperators(),
				    date: lims.gridfilterOperatorsDate(),
				    number: lims.gridfilterOperatorsNumber()
				}
			},
			height:470,
	        width: "100%",
	        sortable: true,
	        resizable: true,
	        selectable: true,
	        pageable: {
	            refresh: true,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	
	brand.clearBusinessBrandInfo=function(){
		$("#brand").val("");
		$("#registeredDate").val("");
		$("#listBrandPic").html("");
	}
	
	brand.writeBusinessBrand=function(data){
		brand.clearBusinessBrandInfo();
		if(data!=null){
			$("#brandName").val(data.name);
			$("#registeredDate").val(data.registrationDate);
			var conDiv=$("#listBrandPic");
			for(var i=0;i<data.logAttachments.length;i++){
				var div=$("<div style='float:left;margin:10px 10px;'></div>");
				var img=$("<img width='50px' src='"+data.logAttachments[i].url+"'></img>");
				var a=$("<a href='"+data.logAttachments[i].url+"' target='_black'></a>");
				a.append(img);
				div.append(a);
				conDiv.append(div);
			}
		}
	}
	
	brand.viewBrandInfo=function(brandId){
		$.ajax({
			url:portal.HTTP_PREFIX + "/business/business-brand/" + brandId,
			type:"GET",
			datatype: "json",
			success: function (returnValue) {
				if(returnValue.result.status == "true") {
					brand.writeBusinessBrand(returnValue.data);
					$("#viewWindow").data("kendoWindow").open().center();
				}
			}
		});
	}
	
	brand.initWindow=function(winId,title,width){
		$("#"+winId).kendoWindow({
			title:title,
			actions:["Close"],
			modal:true,
			width:width,
			visible:false,
		});
	};
	
	brand.initialize = function(){
		var dataSource = this.templateDS("");
		brand.buildGrid("brand",this.templatecolumns, dataSource);
		brand.initWindow("viewWindow","品牌信息预览","400px");
	};
	
	brand.initialize();
});