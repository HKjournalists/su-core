$(function(){
	var lims = window.lims = window.lims || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    var testResult = window.testResult = window.testResult || {};
	var returnData = {};
	var previewFlag=false;
	
	fsn.TestResult = function(model){
		if(model){
			this.model = model;
		}else{
			this.model= new fsn.Model({
				data : {},
				datasource : null,
				fetch_url : "/test/test-result/",
				publish_url : "/test/publish/",
				view : $("#test_result_view"),
				backout_url:"/test/backout/",
			});
		}
	};
	
	var proto = fsn.TestResult.prototype;

	proto.viewDetail = function(e){
		var myTr = this;
		e.preventDefault();
		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
		wnd.content(detailsTemplate(dataItem));
		wnd.center().open();
		
		var params = fsn.getUrlVars();
		myTr.model.data.id = params[params[0]];
		if(params.length > 1){
			fsn.isStruct = params[params[1]];
		}
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
		if(params.length > 1){
			fsn.isStruct = params[params[1]];
		}
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
					if(myTr.model.data.backResult!=null &&myTr.model.data.backResult!=""&&myTr.model.data.backResult!="undefined"&& lims.hostname!=undefined){
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
			success:function(data){
				if(data.status){
					fsn.ltProducer = false;
					if(data.result.type=="流通企业"&&fsn.testReport.edition=="easy"){
						fsn.ltProducer = true;
						$("#producer_View").show();
						//$(".hideBtn").css("display","inline");
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
		
		/**
		 * 结构化报告无需审核生产企业信息
		 */
		if(!fsn.isStruct){
			fsn.getCrentReprotBus();
		}
		
		previewFlag=true;
		$("#report_View").attr("href", data.fullPdfPath);
		$("#report_View").attr("target","_blank");
		$("#publish").click(function(){
			myTr.model.data.id = data.id;
			myTr.model.publishDetail(function(response){
 			   if(response.result.status == "true"){
 				   fsn.initNotificationMes("样品发布成功！", true);
 				   setTimeout('testResult.returnView()',2500);
				}
 		   });
		});
		$("#Return").click(function(){
			fsn.returnData = data;
			fsn.openReturnWin();
		});
		$("#Backout").click(function(){
			myTr.model.data.id = data.id;
			myTr.model.backout(function(response){
  			   var result = response.result;
  			   if(result.status == "true"){
  				   fsn.initNotificationMes(fsn.l("sample backout sucessfully！"), true);
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
		//$("#msg_success").hide();
		myTr.model.renderGrid();
	};
	
	fsn.initReturnWindow(function() {
		//var from=returnData.pdfReport.split("/")[2];
		
		var returnMes=$("#back_msg").val().trim();
		testResult.deleteReturnReport(returnMes);
		$("#back_msg").val("");
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
					fsn.initNotificationMes("报告"+returnData.serviceOrder+"退回失败！错误在LIMS端", false);
				}
			}
		  });
	};
	testResult.deleteReturnReport=function(returnMes){
		if(returnMes == ""){
			returnMes = "无";
		}
		returnData = fsn.returnData;
		
		/**
		 * 结构化报告退回流程
		 * @author ZhangHui 2015/5/6
		 */
		if(fsn.isStructured||fsn.isStruct){
			var url = portal.HTTP_PREFIX + "/test/sendBack/structured/" + returnData.id;
			var data = {"returnMes":encodeURIComponent(returnMes)};
			testResult.send_back(url, data, returnData, "structured_test_result_grid");
			fsn.isStructured = false;
			return;
		}
		
		/**
		 * 正常退回流程
		 */
		var from = returnData.edition;
		// 如果为easy的数据
		if(from == "easy"){
			/*是否退回生产企业信息*/
			if(fsn.liutong){
				var status = fsn.goBackProducer();
				if(!status){
					fsn.initNotificationMes(fsn.l("The failure reason in return, returned to the manufacturer information background error")+"!", false);
				}
			}
			var url = portal.HTTP_PREFIX + "/testReport/goBack/"+from+"/" + returnData.id;
			var data = {"returnMes":encodeURIComponent(returnMes)};
			testResult.send_back(url, data, returnData, "test_result_grid");
		}else{
			// 如果为其他的数据
			var organization=returnData.organization;
			var returnURL=fsn.getReturnReportURL(from,organization);
			
			var url = fsn.HTTP_PREFIX + "/test/" + returnData.id;
			var data = {"returnURL":returnURL,"returnMes":encodeURIComponent(returnMes),"from":from};
			testResult.send_back(url, data, returnData, "test_result_grid");
		}
	};
	
	/**
	 * 退回报告
	 * @author ZhangHui 2015/5/6
	 */
	testResult.send_back = function(url, data, returnData, divId){
		$.ajax({
			url: url,
			type:"GET",
			dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: data,
			success:function(data){
				if(data.result.status == "true"){
					if(previewFlag){
						fsn.initNotificationMes("报告"+returnData.serviceOrder+"退回成功！", true);
						setTimeout('testResult.returnView()',2500);
						previewFlag=false;
					}else{
						$("#" + divId).data("kendoGrid").dataSource.remove(returnData);
						$("#" + divId).data("kendoGrid").refresh();
						fsn.initNotificationMes("报告"+returnData.serviceOrder+"退回成功！", true);
						//当退回当前grid中的所有报告时，需要自动加载下一页的内容
    					$("#" + divId).data("kendoGrid").dataSource.read();
					}
				}else{
					fsn.initNotificationMes("报告"+returnData.serviceOrder+"退回失败！", false);
				}
			}
		  });
	};
	
	testResult.returnView=function(){
		$("#publish").hide("slow");
		$("#Ruturn").hide("slow");
		window.location.href="/fsn-core/views/lab/test-result-manage.html";
	};
	
});