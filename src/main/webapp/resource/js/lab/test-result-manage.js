$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var root = fsn.root = fsn.root || {};
	var upload = fsn.upload = fsn.upload || {};
	
	
	fsn.zh.upload_cert="上传附件";
	
	// 初始化退回原因window控件
	fsn.initKendoWindow("send_back_report_warn_window","退回原因","950px","690px",false,null);
	$("#send_back_report_yes_btn").bind("click", fsn.back_yes);
    $("#send_back_report_no_btn").bind("click", fsn.back_no);
	function initUpload(){
		$("#file-html").html('附件说明:<input type="file" id="return_file" />');
		root.returnFile=[];//退回图片
		upload.buildUpload("return_file",root.returnFile,"error-msg","IMG");
	}
	/**
	 * 第一步：初始化未发布的检测报告Grid
	 * @author ZhangHui 2015/5/6
	 */
	var model_notpub = new fsn.Model({
		//data
		data : {},
		ds_name : "testResultList",
		criteria :{
			publishFlag:false,
			page:1,
			pageSize:5
		},
		//dom
		view : $("#test_result_view"),
		datasource : null,
		grid : $("#test_result_grid"),
		grid_obj : null,
		columns : null,
		//url
		fetch_url : "/test/test-result/",
		search_all_url : "/test/test-results2",
		publish_url : "/test/publish/",
		//config
		page_size : 5,
	});
	model_notpub.columns = [
	                  {field: "id",title: fsn.l("Id"),width: "30px"},
	                  {field: "serviceOrder",title: fsn.l("ReportNO"),width: "55px"},
	                  {field: "sample.product.name",title: fsn.l("Product"),width: "50px"},
	                  {field: "sample.batchSerialNo",title: fsn.l("BatchSerialNo"),width: "50px"},
	                  {field: "sample.producer.name",title: fsn.l("BrandName"),width: "55px"},	                   
	                  {field: "testType",title: fsn.l("TestType"),width: "45px"},
	                  {field: "pubUserName",title: fsn.l("pubUserName"),width: "40px"},
	                  {field: "receiveDate",title: fsn.l("receiveDate"),width: "50px",filterable: false},
	                  {field: "organizationName",title: fsn.l("organizationName"),width: "60px"},
	                  { command: [
		                   {
							  name:"View Detail",
							  text:"<span class='k-icon k-i-search'></span>"+fsn.l("View Detail"),
							  click:function(e){
								  e.preventDefault();
								  var ds = this;
								  var tr = $(e.target).closest("tr");
								  var id = ds.dataItem(tr).id;
								  window.open("/fsn-core/views/lab/test-result-preview.html?id=" + id);
							  }
		                   },
		                   {
		                	   name:"Publish",
		                	   text:"<span class='k-icon k-update'></span>"+fsn.l("Publish"),
		                	   click:function(e){
		                		   e.preventDefault();
		                		   var ds = this;
		                		   var tr = $(e.target).closest("tr");
		                		   var id = ds.dataItem(tr).id;
		                		   fsn.testReport = ds.dataItem(tr);
		                		   //fsn.getCrentReprotBus();
		                		   model_notpub.data.id = id;
		                		   model_notpub.publish(function(response){
		                			   var result = response.result;
		                			   if(result.status == "true"){
		                				   ds.dataSource.remove(ds.dataItem(tr));
		                				   model_notpub.refreshGrid();
		                				   $("#test_result_publish_grid").data("kendoGrid").dataSource.read();
		                				   fsn.initNotificationMes("报告发布成功！", true);
		                				   $("#test_result_grid").data("kendoGrid").dataSource.read();
		                			   }
		                		   });
		                	   }	   
		                   },
		                   {   name:"Return",
		                	   text:"<span class='k-icon k-cancel'></span>"+fsn.l("Return"),  
		                	   click:function(e){
		                		   e.preventDefault();
									  var ds = this;
									  var tr = $(e.target).closest("tr");
									  fsn.returnData=ds.dataItem(tr);
									  
									  // 清空上次的退回原因
									  $("#send_back_msg_other").val("");
									  var inputs = document.getElementsByName("send_back_msg");   
									  for(var i=0;i<inputs.length;i++){   
								 		    inputs[i].checked = false;   
									  }
									  $("#send_back_report_warn_window input").val('');
									  initUpload();
									  // 打开退回window
									  $("#send_back_report_warn_window").data("kendoWindow").open().center();
									  return false;
								  }
		                   },
		                  ], 
		                  title:fsn.l("Operation"), 
		                  width: "40px" 
		               }

	                  ];
	var grid = new fsn.TestResult(model_notpub);
	grid.initGrid();
	
	/**
	 * 第二步：初始化已发布的检测报告Grid
	 * @author ZhangHui 2015/5/6
	 */
	var publish_grid = new fsn.TestResult1({
		has_add_btn : false,
		gridDiv : "#test_result_publish_grid",
		criteria : {
			publishFlag : true,
			page:1,
			pageSize:5
		},
		page_size : 5
	});
	publish_grid.initGrid();
	
	
	/**
	 * 第三步：初始化待审核结构化检测报告Grid
	 * @author ZhangHui 2015/5/6
	 */
	var model_structured = new fsn.Model({
		//data
		data :{},
		ds_name : "testResultList2",
		criteria :{
			publishFlag:false,
			page:1,
			pageSize:5
		},
		//dom
		view : $("#test_result_view"),
		datasource : {
			transport: {
                read: {
                	async:false,
                	url: fsn.getHttpPrefix()+"/test/test-results/getStructureds/",
                	dataType: "json",
                	type: "POST",
                },
                parameterMap : function(options, operation) {
                    if (operation == "read") {     
                    	var data={};
                    	data.page= options.page;    //当前页
                    	data.pageSize = options.pageSize; //每页显示个数
                    	data.configure="";
                    	if(options.filter){
                    		data.configure=filter.configure(options.filter);
                    	}
                    	data.configure=data.configure+"status@eq@已结构化@and@@";
                        return {criteria : kendo.stringify(data)};
                    }
                }
            },
            schema: {	
            	 data : function(response) {
                      return response.testResultList; //响应到页面的数据
                  },
                  total : function(response) {
	                    return response.totalCount;
                  }
            },
            dir:"asc",
            pageSize: 5,
			batch: true,
			serverPaging : true,
		    serverFiltering : true,
		    serverSorting : true
		},
		grid : $("#structured_test_structure_result_grid"),
		grid_obj : null,
		columns : null,
		//url
		fetch_url : "/test/test-result/",
		search_all_url : "/test/test-results/getStructureds/",
		publish_url : "/test/publish/",
		//config
		page_size : 5,
	});
	model_structured.columns = [
	                  {field: "id",title: fsn.l("Id"),width: "1px"},
	                  {field: "serviceOrder",title: fsn.l("ReportNO"),width: "55px"},
	                  {field: "sampleName",title: fsn.l("Product"),width: "50px"},
	                  {field: "barcode",title: "条形码",width: "60px"},
	                  {field: "batchSerialNo",title: fsn.l("BatchSerialNo"),width: "50px"},
	                  {field: "producerName",title: fsn.l("BrandName"),width: "55px"},	                   
	                  {field: "testType",title: fsn.l("TestType"),width: "45px"},
	                  {field: "operator",title: fsn.l("pubUserName"),width: "40px"},
	                  {field: "receiveDate",title: fsn.l("receiveDate"),width: "50px",template: '#= new Date(receiveDate).format("YYYY-MM-dd hh:mm:ss")#',filterable: false},
	                  {field: "status",title: "状态",width: "40px",template: function(dataItem) {
                          if(dataItem.status == 2){
                        	  return "<strong style='color:red;'>已结构化</strong>";
                          }else{
                        	  return "<strong style='color:#006536;'>未结构化</strong>";
                          }
	                  },filterable: false},
	                  {field: "backCount",title: fsn.l("结构化人员退回次数"),width: "75px",filterable:false},
	                  { command: [
		                   {
							  name:"View Detail",
							  text:"<span class='k-icon k-i-search'></span>"+fsn.l("View Detail"),
							  click:function(e){
								  e.preventDefault();
								  var ds = this;
								  var tr = $(e.target).closest("tr");
								  var id = ds.dataItem(tr).id;
								  window.open("/fsn-core/views/lab/test-result-preview.html?id=" + id+"&isStruct=1&status="+ds.dataItem(tr).status);
							  }
		                   },
		                   {
		                	   name:"Publish",
		                	   text:"<span class='k-icon k-update'></span>"+fsn.l("Publish"),
		                	   click:function(e){
		                		   e.preventDefault();
		                		   var ds = this;
		                		   var tr = $(e.target).closest("tr");
		                		   var id = ds.dataItem(tr).id;
		                		   fsn.testReport = ds.dataItem(tr);
		                		   //fsn.getCrentReprotBus();
		                		   model_structured.data.id = id;
		                		   model_structured.publish(function(response){
		                				   ds.dataSource.remove(ds.dataItem(tr));
		                				   model_structured.refreshGrid();
		                				   $("#test_result_publish_grid").data("kendoGrid").dataSource.read();
		                				   fsn.initNotificationMes("发布成功！", true);
		                		   });
		                	   }	   
		                   },
		                   {   name:"Return",
		                	   text:"<span class='k-icon k-cancel'></span>"+fsn.l("Return"),  
		                	   click:function(e){
		                		      //e.preventDefault();
									  var ds = this;
									  var tr = $(e.target).closest("tr");
									  fsn.returnData=ds.dataItem(tr);
									  fsn.isStruct = true;
									  // 清空上次的退回原因
									  $("#send_back_msg_other").val("");
									  $("#error-msg").html('');
									  initUpload();
									  var inputs = document.getElementsByName("send_back_msg");   
									  for(var i=0;i<inputs.length;i++){   
								 		    inputs[i].checked = false;   
									  }
									  $("#send_back_report_warn_window input").val('');
									  // 打开退回window
									  $("#send_back_report_warn_window").data("kendoWindow").open().center();
									  return false;
		                	   }
		                   },
		                  ], 
		                  title:fsn.l("Operation"), 
		                  width: "40px" 
		               }

	                  ];
	var grid_structured = new fsn.TestResult(model_structured);
	grid_structured.initGrid();
	
	/**
	 * 第四步：初始化待审核未结构化检测报告Grid
	 * @author zouhao 2015/5/6
	 */
	var model_no_structured = new fsn.Model({
		//data
		data : {},
		ds_name : "testResultList3",
		criteria :{
			publishFlag:false,
			page:1,
			pageSize:5
		},
		//dom
		view : $("#test_result_view"),
		datasource : {
			transport: {
                read: {
                	async:false,
                	url: fsn.getHttpPrefix()+"/test/test-results/getStructureds/",
                	dataType: "json",
                	type: "POST",
                },
                parameterMap : function(options, operation) {
                    if (operation == "read") {     
                    	var data={};
                    	data.page= options.page;    //当前页
                    	data.pageSize = options.pageSize; //每页显示个数
                    	data.configure="";
                    	if(options.filter){
                    		data.configure=filter.configure(options.filter);
                    	}
                    	console.log(options.filter)
                    	data.configure=data.configure+"status@contains@未结构化@and@@";
                        return {criteria : kendo.stringify(data)};
                    }
                }
            },
            schema: {	
            	 data : function(response) {
                      return response.testResultList; //响应到页面的数据
                  },
                  total : function(response) {
	                    return response.totalCount;
                  }
            },
            dir:"asc",
            pageSize: 5,
			batch: true,
			serverPaging : true,
		    serverFiltering : true,
		    serverSorting : true
		},
		grid : $("#structured_test_no_structure_result_grid"),
		grid_obj : null,
		columns : null,
		//url
		fetch_url : "/test/test-result/",
		search_all_url : "/test/test-results/getStructureds/",
		publish_url : "/test/publish/",
		//config
		page_size : 5,
	});
	model_no_structured.columns = [
	                  {field: "id",title: fsn.l("Id"),width: "1px"},
	                  {field: "serviceOrder",title: fsn.l("ReportNO"),width: "55px"},
	                  {field: "sampleName",title: fsn.l("Product"),width: "50px"},
	                  {field: "barcode",title: "条形码",width: "60px"},
	                  {field: "batchSerialNo",title: fsn.l("BatchSerialNo"),width: "50px"},
	                  {field: "producerName",title: fsn.l("BrandName"),width: "55px"},	                   
	                  {field: "testType",title: fsn.l("TestType"),width: "45px"},
	                  {field: "operator",title: fsn.l("pubUserName"),width: "40px"},
	                  {field: "receiveDate",title: fsn.l("receiveDate"),width: "50px",template: '#= new Date(receiveDate).format("YYYY-MM-dd hh:mm:ss")#',filterable: false},
	                  {field: "status",title: "状态",width: "40px",template: function(dataItem) {
                          if(dataItem.status == 2){
                        	  return "<strong style='color:red;'>已结构化</strong>";
                          }else{
                        	  return "<strong style='color:#006536;'>未结构化</strong>";
                          }
	                  },filterable: false},
	                  { command: [
		                   {
							  name:"View Detail",
							  text:"<span class='k-icon k-i-search'></span>"+fsn.l("View Detail"),
							  click:function(e){
								  e.preventDefault();
								  var ds = this;
								  var tr = $(e.target).closest("tr");
								  var id = ds.dataItem(tr).id;
								  window.open("/fsn-core/views/lab/test-result-preview.html?id=" + id+"&isStruct=1&status="+ds.dataItem(tr).status);
							  }
		                   },
		                   {
		                	   name:"Publish",
		                	   text:"<span class='k-icon k-update'></span>"+fsn.l("Publish"),
		                	   click:function(e){
		                		   e.preventDefault();
		                		   var ds = this;
		                		   var tr = $(e.target).closest("tr");
		                		   var id = ds.dataItem(tr).id;
		                		   fsn.testReport = ds.dataItem(tr);
		                		   //fsn.getCrentReprotBus();
		                		   model_no_structured.data.id = id;
		                		   model_no_structured.publish(function(response){
		                				   ds.dataSource.remove(ds.dataItem(tr));
		                				   model_no_structured.refreshGrid();
		                				   $("#test_result_publish_grid").data("kendoGrid").dataSource.read();
		                				   fsn.initNotificationMes("发布成功！", true);
		                		   });
		                	   }	   
		                   },
		                   {   name:"Return",
		                	   text:"<span class='k-icon k-cancel'></span>"+fsn.l("Return"),  
		                	   click:function(e){
		                		      e.preventDefault();
									  var ds = this;
									  var tr = $(e.target).closest("tr");
									  fsn.returnData=ds.dataItem(tr);
									  fsn.isStruct = true;
									  initUpload();
									  // 清空上次的退回原因
									  $("#send_back_msg_other").val("");
									  var inputs = document.getElementsByName("send_back_msg");   
									  for(var i=0;i<inputs.length;i++){   
								 		    inputs[i].checked = false;   
									  }
									  $("#send_back_report_warn_window input").val('');
									  // 打开退回window
									  $("#send_back_report_warn_window").data("kendoWindow").open().center();
		                	   }
		                   },
		                  ], 
		                  title:fsn.l("Operation"), 
		                  width: "40px" 
		               }

	                  ];
	var grid_no_structured = new fsn.TestResult(model_no_structured);
	grid_no_structured.initGrid();
	
	/* 第五步：初始化重新审核的未结构化检测报告Grid*/
	var model_re_audit_no_structured = new fsn.Model({
		//data
		data : {},
		ds_name : "testResultList4",
		criteria :{
			publishFlag:false,
			page:1,
			pageSize:5
		},
		//dom
		view : $("#test_result_view"),
		datasource : {
			transport: {
                read: {
                	async:false,
                	url: fsn.getHttpPrefix()+"/test/test-results/getStructureds/",
                	dataType: "json",
                	type: "POST",
                },
                parameterMap : function(options, operation) {
                    if (operation == "read") {     
                    	var data={};
                    	data.page= options.page;    //当前页
                    	data.pageSize = options.pageSize; //每页显示个数
                    	data.configure="";
                    	if(options.filter){
                    		data.configure=filter.configure(options.filter);
                    	}
                    	data.configure=data.configure+"status@contains@重审核@and@@";
                    	
                        return {criteria : kendo.stringify(data)};
                    }
                }
            },
            schema: {	
            	 data : function(response) {
                      return response.testResultList; //响应到页面的数据
                  },
                  total : function(response) {
	                    return response.totalCount;
                  }
            },
            pageSize: 5,
			batch: true,
			serverPaging : true,
		    serverFiltering : true,
		    serverSorting : true
		},
		grid : $("#re_audit_no_structure_result_grid"),
		grid_obj : null,
		columns : null,
		//url
		fetch_url : "/test/test-result/",
		search_all_url : "/test/test-results/getStructureds/",
		publish_url : "/test/publish/",
		page_size : 5,
	});
	model_re_audit_no_structured.columns = [
	                  {field: "id",title: fsn.l("Id"),width: "1px"},
	                  {field: "serviceOrder",title: fsn.l("ReportNO"),width: "55px"},
	                  {field: "sampleName",title: fsn.l("Product"),width: "50px"},
	                  {field: "barcode",title: "条形码",width: "60px"},
	                  {field: "batchSerialNo",title: fsn.l("BatchSerialNo"),width: "50px"},
	                  {field: "producerName",title: fsn.l("BrandName"),width: "55px"},	                   
	                  {field: "testType",title: fsn.l("TestType"),width: "45px"},
	                  {field: "operator",title: fsn.l("pubUserName"),width: "40px"},
	                  {field: "receiveDate",title: fsn.l("receiveDate"),width: "50px", template: '#= new Date(receiveDate).format("YYYY-MM-dd hh:mm:ss")#',filterable: false},
	                  {field: "status",title: "状态",width: "40px",template: function(dataItem) {
                          if(dataItem.status == 2){
                        	  return "<strong style='color:red;'>已结构化</strong>";
                          }else{
                        	  return "<strong style='color:#006536;'>未结构化</strong>";
                          }
	                  },filterable: false},
	                  {field: "suppliersBackCount",title: '供应商退回次数',width: "70px"},
	                  { command: [
		                   {
							  name:"View Detail",
							  text:"<span class='k-icon k-i-search'></span>"+fsn.l("View Detail"),
							  click:function(e){
								  e.preventDefault();
								  var ds = this;
								  var tr = $(e.target).closest("tr");
								  var id = ds.dataItem(tr).id;
								  window.open("/fsn-core/views/lab/test-result-preview.html?id=" + id+"&isStruct=1&status="+ds.dataItem(tr).status);
							  }
		                   },
		                   {
		                	   name:"Publish",
		                	   text:"<span class='k-icon k-update'></span>"+fsn.l("Publish"),
		                	   click:function(e){
		                		   e.preventDefault();
		                		   var ds = this;
		                		   var tr = $(e.target).closest("tr");
		                		   var id = ds.dataItem(tr).id;
		                		   fsn.testReport = ds.dataItem(tr);
		                		   //fsn.getCrentReprotBus();
		                		   model_re_audit_no_structured.data.id = id;
		                		   model_re_audit_no_structured.publish(function(response){
		                				   ds.dataSource.remove(ds.dataItem(tr));
		                				   model_re_audit_no_structured.refreshGrid();
		                				   $("#test_result_publish_grid").data("kendoGrid").dataSource.read();
		                				   fsn.initNotificationMes("发布成功！", true);
		                		   });
		                	   }	   
		                   },
		                   {   name:"Return",
		                	   text:"<span class='k-icon k-cancel'></span>"+fsn.l("Return"),  
		                	   click:function(e){
		                		      e.preventDefault();
									  var ds = this;
									  var tr = $(e.target).closest("tr");
									  fsn.returnData=ds.dataItem(tr);
									  fsn.isStruct = true;
									  initUpload();
									  // 清空上次的退回原因
									  $("#send_back_msg_other").val("");
									  var inputs = document.getElementsByName("send_back_msg");   
									  for(var i=0;i<inputs.length;i++){   
								 		    inputs[i].checked = false;   
									  }
									  $("#send_back_report_warn_window input").val('');
									  // 打开退回window
									  $("#send_back_report_warn_window").data("kendoWindow").open().center();
		                	   }
		                   },
		                  ], 
		                  title:fsn.l("Operation"), 
		                  width: "40px" 
		               }

	                  ];
	var grid_re_audit_no_structure = new fsn.TestResult(model_re_audit_no_structured);
	grid_re_audit_no_structure.initGrid();
	
	/**展开收缩功能*/
	$(".contraction").click(function(){
		if($(this).text()=="展开"){
			$(this).parent().next().show('slow');
			$(this).text("收起");
		}else{
			$(this).parent().next().hide('slow');
			$(this).text("展开");
		}
		return false;
	});
});