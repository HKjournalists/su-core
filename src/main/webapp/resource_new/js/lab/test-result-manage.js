$(function(){
	var lims = window.lims = window.lims || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    var testResult = window.testResult = window.testResult || {};
	var returnData = {};
	var previewFlag=false;
	fsn.TestResult = function(config){
		this._init( config );
	};
	var proto = fsn.TestResult.prototype;

	proto._init = function(config){
		config = config || {};
		this.has_add_btn = config.has_add_btn;
		this.model = new fsn.Model({
			//data
			data : {},
			ds_name : "testResultList",
			criteria : config.criteria,
			
			//dom
			view : $("#test_result_view"),
			datasource : null,
			grid : $(config.gridDiv),
			grid_obj : null,
			columns : null,
			detail_win : config.detail_win,
			detail_win_obj : null,
			
			//url
			create_url : "/test/test-result",
			fetch_url : "/test/test-result/",
			update_url : "/test/test-result",
			delete_url : "/test/test-result/",
			search_all_url : "/test/test-results2",
			search_url : "/test/test-results",
			publish_url : "/test/publish/",
			backout_url : "/test/backout/",
			//config
			page_size : config.page_size,
			editable : config.editable,
			toolbar : config.toolbar
		});
		
		if(!this.model.criteria){
			var session_user = fsn.session("user");
			//init criteria
			var criteria = this.model.criteria = {};
			if(session_user){
				criteria.roleId = session_user.roleId;
				criteria.testerId = session_user.userId;
			}
		}
		var me=this;
		this.model.columns = [
			                   {field: "id",title: fsn.l("Id"),width: "1px"},
			                   {field: "serviceOrder",title: fsn.l("ReportNO"),width: "70px"},
			                   {field: "sample.product.name",title: fsn.l("Product"),width: "70px"},
			                   {field: "sample.batchSerialNo",title: fsn.l("BatchSerialNo"),width: "55px"},
			                   {field: "sample.producer.name",title: fsn.l("BrandName"),width: "55px"},	                   
			                   {field: "testType",title: fsn.l("TestType"),width: "50px"},
			                   {field: "pubUserName",title: fsn.l("pubUserName"),width: "50px"},
			                   {field: "receiveDate",title: fsn.l("receiveDate"),width: "50px",filterable: false},
			                   {field: "organizationName",title: fsn.l("organizationName"),width: "60px"},
			                   { command: [
				                   {
									  name:"View Detail",
									  text:"<span class='k-icon k-i-search'></span>"+fsn.l("View Detail"),
									  click:function(e){
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
	                            		   var ds = this;
	                            		   var tr = $(e.target).closest("tr");
	                            		   var id = ds.dataItem(tr).id;
	                            		   fsn.testReport = ds.dataItem(tr);
	                            		   fsn.noDetail = true;
	                            		   fsn.getCrentReprotBus();
	                            		   me.model.data.id = id;
	                            		   me.model.publish(function(response){
	                            			   var result = response.RESTResult;
	                            			   if(result.statusCode == 0){
	                            				   ds.dataSource.remove(ds.dataItem(tr));
	                            				   me.model.refreshGrid();
	                            				   $("#test_result_publish_grid").data("kendoGrid").dataSource.read();
	                            				   fsn.msg("msg_success",result,fsn.l("样品发布成功！"));
	                            				   $("#test_result_grid").data("kendoGrid").dataSource.read();
												}
	                            		   });
				                	   }	   
				                   },
				                   {   name:"Return",
				                	   text:"<span class='k-icon k-cancel'></span>"+fsn.l("Return"),  
				                	   click:function(e){
											  var ds = this;
											  var tr = $(e.target).closest("tr");
											  returnData=ds.dataItem(tr);
											  fsn.openReturnWin();
										  }
				                   },
				                  ], 
				                  title:fsn.l("Operation"), 
				                  width: "40px" 
				               }
	       
	   ];
	};
	
	proto.viewDetail = function(e){
		var myTr = this;
		e.preventDefault();
		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
		wnd.content(detailsTemplate(dataItem));
		wnd.center().open();
		
		var params = fsn.getUrlVars();
		myTr.model.data.id = params[params[0]];
		if(!myTr.model.data.id){
			var testResult = fsn.session('testResult');
			myTr.model.data.id = testResult ? testResult.id : null;
		}
		if(!myTr.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				myTr.model.data.id = user.userId;
			}
		}
		
		if(myTr.model.data.id){
			
			myTr.model.fetch(function(response){
				myTr.model.data = response.testResult;
				if(myTr.model.data){
					myTr._bindView(myTr.model.data);
				}
			});
		}else{
			myTr._bindView({});
		}
	};
	
	proto.initView = function(){
		var myTr = this;
		var params = fsn.getUrlVars();
		myTr.model.data.id = params[params[0]];
		if(!myTr.model.data.id){
			var testResult = fsn.session('testResult');
			myTr.model.data.id = testResult ? testResult.id : null;
		}
		if(!myTr.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				myTr.model.data.id = user.userId;
			}
		}
		
		if(myTr.model.data.id){
			
			myTr.model.fetch(function(response){
				myTr.model.data = response.testResult;
				if(myTr.model.data){
					if(myTr.model.data.backResult!=null && lims.hostname!=undefined){
						lims.msg("backMsg", null, ("退回原因："+ myTr.model.data.backResult));
			        }
					myTr._bindView(myTr.model.data);
					fsn.test_property.model.datasource = new kendo.data.DataSource({
				    	data:myTr.model.data.testProperties,
				        batch: true,
				        pageSize: 10
				    });
					fsn.test_property.model.renderGrid();
				}
			});
		}else{
			myTr._bindView({});
		}
	};
	
	/*获取当前报告所属企业类型*/
	fsn.getCrentReprotBus = function(){
		if(fsn.testReport==null) return;
		$.ajax({
			url:portal.HTTP_PREFIX+"/business/getCurrentBusiness?organization="+fsn.testReport.organization,
			type:"GET",
			async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					fsn.ltProducer = false;
					if(returnValue.data.type=="流通企业"&&fsn.testReport.edition=="easy"){
						fsn.ltProducer = true;
						$(".hideBtn").css("display","inline");
					}
				}
			},
		}); 
	};
	
	proto._bindView = function(data){
		var myTr = this;
		//截取时间字符串(如:yyyy-MM-dd)
		var testDate = data.testDate;
		if(testDate!=null&&testDate!=''){
			testDate = testDate.length>10?testDate.substr(0,10):testDate;
		}else{
			testDate = '';
		}
		data.testDate = testDate;
		//抽样时间截取(格式如:yyyy-MM-dd)
		var sampleDate = data.samplingDate;
		if(sampleDate!=null&&sampleDate!=''){
			sampleDate = sampleDate.length>10?sampleDate.substr(0,10):sampleDate;
		}else{
			sampleDate = '';
		}
		data.samplingDate = sampleDate;
		fsn.testReport=data;
		fsn.getCrentReprotBus();
		previewFlag=true;
		$("#msg_success").hide();
		$("#report_View").attr("href", data.fullPdfPath);
		$("#report_View").attr("target","_blank");
		$("#publish").click(function(){
			myTr.model.data.id = data.id;
			myTr.model.publishDetail(function(response){
 			   var result = response.RESTResult;
 			   if(result.statusCode == 0){
 				   fsn.msg("msg_success",result,fsn.l("样品发布成功！"));
 				   setTimeout('testResult.returnView()',2500);
				}
 		   });
		});
		$("#Return").click(function(){
			returnData = data;
			fsn.openReturnWin();
		});
		$("#Backout").click(function(){
			myTr.model.data.id = data.id;
			myTr.model.backout(function(response){
  			   var result = response.RESTResult;
  			   if(result.statusCode == 0){
  				   fsn.msg("msg_success",result,fsn.l("sample backout sucessfully！"));
  				   setTimeout('testResult.returnView()',2500);
  			   }
  		   });
		});
		var viewModel = kendo.observable({
		    testResult: data,
		    hasChanges: false,
		    save: function() {
		    	myTr.model.data = this.get("testResult");
		    	myTr.model.save(function(response){
		    		var result = response.RESTResult;
		    		if(result.message){
		    			return;
		    		}
		    		if(result.statusCode == 0){
		    			if(myTr.model.data.id){
		    				$("#msg_success").html(myTr.model.data.name + " is updated sucessfully.");
		    			}else{
		    				myTr.model.data.id = result.data.id;
		    				$("#msg_success").html(myTr.model.data.name + " is created sucessfully.");
		    			}
		    		}
		    	});
		        this.set("hasChanges", false);
		    },
		    reset: function(){
		    	this.set("testResult", myTr.model.data);
		    },
		    change: function() {
		        this.set("hasChanges", true);
		    }
		    
		});

		kendo.bind(myTr.model.view, viewModel);
		
		/* 查看详情页面，加载产品图片 */
		var proAttDS = new kendo.data.DataSource();
		if(data.sample.product.proAttachments.length>0){
			 $("#proAtt_tr").show();
			 for(var i=0;i<data.sample.product.proAttachments.length;i++){
				 proAttDS.add({attachments:data.sample.product.proAttachments[i]});
			 }
		}else{
			 $("#proAtt_tr").hide();
		}
		$("#proAttachments").kendoListView({
            dataSource: proAttDS,
            template:kendo.template($("#uploadedFilesTemplate").html()),
		});
	};
	
	proto.initGrid = function(){
		var myTr = this;		
		$("#msg_success").hide();
		myTr.model.renderGrid();
	};
	
	fsn.initReturnWindow(function() {
		//var from=returnData.pdfReport.split("/")[2];
		
		var returnMes=$("#RETURN_MES").val().trim();
		testResult.deleteReturnReport(returnMes);
		$("#RETURN_MES").val("");
		fsn.closeReturnWin();
	}, undefined,"退回原因",true);
	
	testResult.ReturnReport=function(from,serviceOrder,returnMes){
		var returnURL=fsn.getReturnReportURL(from);
		$.ajax({
			url:returnURL,
			type:"GET",
			dataType:"json",
    		Accept: "application/json",
    		contentType: "application/x-www-form-urlencoded",
			data:{"reportNO":serviceOrder,"returnMes":returnMes},
			success:function(data){
				if(data.status=="true"){
					testResult.deleteReturnReport();					
				}else{
					fsn.msg("msg_success",data.result,fsn.l("报告"+returnData.serviceOrder+"退回失败！错误在LIMS端"));
				}
			}
		  });
	};
	testResult.deleteReturnReport=function(returnMes){
		if(returnMes == ""){
			returnMes = "无";
		}
		var from=returnData.edition;
		// 如果为easy的数据
		if(from == "easy"){
			/*是否退回生产企业信息*/
			if(fsn.liutong){
				var status = fsn.goBackProducer();
				if(!status){
					fsn.msg("msg_success",null,fsn.l("The failure reason in return, returned to the manufacturer information background error")+"!");
				}
			}
			$.ajax({
				url: portal.HTTP_PREFIX + "/testReport/goBack/"+from+"/" + returnData.id ,
				type:"GET",
				dataType: "json",
	            contentType: "application/json; charset=utf-8",
	            data:{"returnMes":encodeURIComponent(returnMes)},
				success:function(data){
					if(data.result.status == "true"){
						if(previewFlag){
							fsn.msg("msg_success",data.result,fsn.l("报告"+returnData.serviceOrder+"退回成功！"));
							setTimeout('testResult.returnView()',2500);
							previewFlag=false;
						}else{
							$("#test_result_grid").data("kendoGrid").dataSource.remove(returnData);
							$("#test_result_grid").data("kendoGrid").refresh();
							fsn.msg("msg_success",data.result,fsn.l("报告"+returnData.serviceOrder+"退回成功！"));
							//当退回当前grid中的所有报告时，需要自动加载下一页的内容
        					$("#test_result_grid").data("kendoGrid").dataSource.read();
						}
					}else{
						fsn.msg("msg_success",data.result,fsn.l("报告"+returnData.serviceOrder+"退回失败！"));
					}
					
				}
			  });
		}else{
			// 如果为其他的数据
			var organization=returnData.organization;
			var returnURL=fsn.getReturnReportURL(from,organization);
			$.ajax({
				url:fsn.HTTP_PREFIX + "/test/" + returnData.id,
				type:"GET",
				dataType: "json",
	            contentType: "application/json; charset=utf-8",
				data:{"returnURL":returnURL,"returnMes":encodeURIComponent(returnMes),"from":from},
				success:function(data){
					if(data.RESTResult.success == true){	
						if(previewFlag){					
							fsn.msg("msg_success",data.RESTResult,fsn.l("报告"+returnData.serviceOrder+"退回成功！"));
							setTimeout('testResult.returnView()',2500);
							previewFlag=false;
						}else{
							$("#test_result_grid").data("kendoGrid").dataSource.remove(returnData);
							$("#test_result_grid").data("kendoGrid").refresh();
							fsn.msg("msg_success",data.RESTResult,fsn.l("报告"+returnData.serviceOrder+"退回成功！"));
        					$("#test_result_grid").data("kendoGrid").dataSource.read();
						}
					}else{
						fsn.msg("msg_success",data.RESTResult,fsn.l("报告"+returnData.serviceOrder+"退回失败！"));
					}
				}
			  });
		}
	};
	testResult.returnView=function(){
		$("#publish").hide("slow");
		$("#Ruturn").hide("slow");
		window.location.href="/fsn-core/views/lab/test-result-manage.html";
	};
	
});