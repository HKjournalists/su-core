$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var unit = upload.unit = upload.unit || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	unit.templatecolumns = [
					 /*{ command: [{
						name: "Check",
					    text: "<input type='checkbox' class='checkboxes'/>"
					 },],  width: 40 },*/
	                 {field: "id",title: lims.l("Id"),width: 40,filterable: true},
	                 {field: "userName",title: lims.l("UserName"),width: 100,filterable: true},
                     {field: "enterpriteName",title: lims.l("EnterpriteName"),width: 100,filterable: true},
                     {field: "enterptiteAddress",title: lims.l("EnterptiteAddress"),width: 100,filterable: true},
                     {field: "legalPerson",title: lims.l("LegalPerson"),width: 80,filterable: true},
                     {field: "enterpriteDate",title: lims.l("EnterpriteDate"),width: 80,filterable: true},
                     {field: "enterpriteType",title: lims.l("EnterpriteType"),width: 80,filterable: true},
                     {field: "status",title: lims.l("Status"),width: 50,filterable: true,template: function(dataItem) {
                    	 if(dataItem.status=="审核通过"){
                    		 return dataItem.status;
                    	 }else{
                    		 return "<strong style='color:red;'>" + dataItem.status + "</strong>";                    		 
                    	 }
                     }},
                     { command: [{                      
    			                      text: lims.localized("Detail"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  window.location.href = portal.CONTEXT_PATH+"/views/business/enRegiste.html?id="+temp.id;
									  }
								  },
					            ], title:lims.l("Operation"), width: 50 }
                     ];
	unit.templateDS = function(keyword){
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		var configure=null;
	            		if(options.filter){
	            		 configure = filter.configure(options.filter);	            
	            		}
						return portal.HTTP_PREFIX + "/business/enRegiste/" + options.page + "/" + options.pageSize + "/"+configure ;
					},
					type : "GET",
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	 data : function(d) {
	                 return d.result.success?d.data.enRegisteList:null;  //响应到页面的数据
	             },
	             total : function(d) {
	                 return d.result.success?d.data.totalCount:0;   //总条数
	             }         
	        },
	        batch : true,
	        pageSize : 5, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	unit.buildGrid=function(id,columns,ds){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				messages: lims.gridfilterMessage(),
				operators: {
					string: lims.gridfilterOperators(),
				    date: lims.gridfilterOperatorsDate(),
				    number: lims.gridfilterOperatorsNumber()
				}
			},
			height: 260,
	        sortable: true,
	        resizable: true,
	        /*toolbar: [
	        		  {template: kendo.template($("#toolbar_template").html())}
	                  ],*/
	        selectable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: true,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	unit.initialize = function(){
		var dataSource = this.templateDS("");
		unit.buildGrid("enRegiste",this.templatecolumns, dataSource);
		$('#search').kendoSearchBox({
			change:function(e){
				var keyword = e.sender.options.value;
			
				unit.searchTemplateByKeywords("enRegiste", keyword);				
			}
		}); 
		$("#unit .k-grid-content").attr("style","height: 364px");		
	};
	unit.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = unit.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
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
	
});