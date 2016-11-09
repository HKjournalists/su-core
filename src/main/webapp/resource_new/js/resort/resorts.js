$(function() {
//	var upload = window.fsn.upload = window.fsn.upload || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	var resorts = fsn.resorts = fsn.resorts || {};
	portal.CONTEXT_PATH = fsn.getContextPath(); // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var CONFIRM = null;
	var resortsById = null;
	var TZID = null;
	
	resorts.initialize = function(){
		
		// 初始化表
		buildGridWioutToolBar("resorts_div_grid", resorts.templatecolumns,getTemplateDS(""), "400");
		
//		resorts.InitGrid();
		resorts.initKendoWindow("CONFIRM_COMMON", "确定删除吗？", "300px",
				"60px", false);
		
//		CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "删除提示！", false,true,false,["Close"],null,"");
		
	};
	
	
	resorts.templatecolumns  = [
	                 {field: "id",title: "编号",filterable: false,width: 35},
                     {field: "name",title: "景区名称",filterable : false,width: 40},
                     {field: "resortType",title: "景区类型",filterable : false,width: 40},
                     {field: "reserveTelephone",title: "订票电话",filterable : false,width: 40},
                     {field: "resortPrice",title: "门票价格",filterable : false,width: 50},
                     {field: "resortInfo",title: "景区简介",filterable : false,width: 50},
                     { command: [{
                                	  name:"edit",
    			                      text: "编辑",
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  resorts.edit(temp.id);
									  }
								  },
//								   {
//									  name:"look",
//									  text: "查看",
//									  click: function(e){
//										  var editRow = $(e.target).closest("tr");
//										  var temp = this.dataItem(editRow);
//										  resorts.edit(temp.id);
//									  }
//								  },
			                     {
			                    	 name:"delete",
			                    	 text: "删除",
			                    	 click: function(e){
			                    		 var editRow = $(e.target).closest("tr");
			                    		 var temp = this.dataItem(editRow);
			                    		 resorts.save(temp.id);
			                    	 }
			                     }
					     ], title:"操作",filterable : false, width: 60 }
                     ];

	
	function getTemplateDS(name){
		var name ="";
		dataSource = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : function(options) {
						console.log(options);
						return fsn.getHttpPrefix()+"/resorts/getResortsList/"+options.page+"/" + options.pageSize;
					},
					data:{"name":name},
					dataType : "json",
					contentType : "application/json"
				}
			},
			schema : {
				data : function(data) {
					console.log(data);
					return data.data;
				},
				total : function(data) {
					return data.total;// 总条数
				}
			},
			batch : true,
			page : 1,
			pageSize : 10, // 每页显示个数
			serverPaging : true,
			serverFiltering : true,
			serverSorting : true
		});
		return dataSource;
//		var grid = $("#resorts_div_grid").data("kendoGrid");
//		console.log(grid);
//		grid.setDataSource(dataSource);
//		grid.refresh();
	};
	


	//删除调用方法
//	resorts.deletetResort = function(id){
//		alert(id + "----");
//		 TZID = id;
//		 CONFIRM.open().center();
//	};
//	
//	resorts.close = function(){
//		CONFIRM.close();
//	};
	
	
	
	
	//编辑数据库景区
	resorts.edit = function(id){
//		window.location.href = "addResort.html?id=" + id;
		var material = {
				id : id
		};
		$.cookie("user_0_edit_resort", JSON.stringify(material), {
			path : '/'
		});
		window.location.pathname = "/fsn-core/views/resort/addResort.html";
	};
	
	
	
	
	
	//删除数据库景区
	resorts.remove = function(){
			$.ajax({
				url:fsn.getHttpPrefix() + "/resorts/delete/"+TZID,
				type:"GET",
				dataType: "json",
				async:false,
				success:function(returnValue){
					if (returnValue.status == true) {
						fsn.initNotificationMes("成功删除！", true);
					} else {
						fsn.initNotificationMes("删除失败!", false);
					}
					resorts.close();
					resorts.initialize();
				}
			});
	};
	
	
	/**
  	 * 列表显示grid
  	 */
	resorts.InitGrid  = function(){
  		$("#resorts_div_grid").kendoGrid({
  		  columns: resorts.templatecolumns,
          editable: "popup",
          filterable: {
              extra: false,
              messages: fsn.gridfilterMessage(),
              operators: {
                  string: fsn.gridfilterOperators(),
                  date: fsn.gridfilterOperatorsDate(),
                  number: fsn.gridfilterOperatorsNumber()
              }
          },
          toolbar: [{
              template: kendo.template($("#toolbar_template").html())
          }],
          pageable: {
        	  refresh: true,
        	  pageSizes: [10, 20,50,80,100],
        	  messages: fsn.gridPageMessage()
          },
          dataSource: []
  		});
  		resorts.getTemplateDS();
  	};
	
  	
  	
  	/**
	 * 初始化grid方法
	 * @param id
	 * @param columns
	 * @param ds
     * @param height
     */
	function buildGridWioutToolBar(id,columns,ds, height){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				extra: false,
				messages: fsn.gridfilterMessage(),
				operators: {
					string: fsn.gridfilterOperators(),
					date: fsn.gridfilterOperatorsDate(),
					number: fsn.gridfilterOperatorsNumber()
				}
			},
	            toolbar: [{
	                template: kendo.template($("#toolbar_template").html())
	            }],
			editable: false,
			height:height,
			width: "100%",
			sortable: true,
			resizable: true,
			selectable: false,
			pageable: {
				refresh: true,
				messages: fsn.gridPageMessage()
			},
			columns: columns
		});
	};
  	
  	
	
  	/*
	 * 初始化弹框方法
	 */
	resorts.initKendoWindow = function(winId, title, width,
			height, isVisible) {
		$("#" + winId).kendoWindow({
			title : title,
			width : width,
			height : height,
			modal : true,
			visible : isVisible,
			actions : [ "Close" ]
		});
	};
	
	
	/*
	 * 删除弹窗
	 */
	resorts.save = function(id) {
		TZID = id;
		$("#CONFIRM_COMMON").data("kendoWindow").open().center();
	};

	/*
	 * 关闭弹窗
	 */
	resorts.close = function() {
		$("#CONFIRM_COMMON").data("kendoWindow").close();
	};
	
	
	/*resorts.edit = function(id){
		var material = {
			id : id
		};
		$.cookie("user_0_edit_resorts", JSON.stringify(material), {
			path : '/'
		});
		window.location.pathname = "/fsn-core/views/resort/resorts.html";
	};*/
	
	
	
	
	resorts.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = resorts.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
	};
	
	
	resorts.initialize();
	
});