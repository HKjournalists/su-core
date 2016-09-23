$(function(){
	var lims = window.lims = window.lims || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    var testResult = window.testResult = window.testResult || {};
    var root = window.fsn.root = window.fsn.root || {};
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
		if(params.length > 1 && params[params[1]] == "1"){
			fsn.isStruct = true;
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
		if(params.length == 3 && params[params[1]]=="1"){
			fsn.isStruct = true;
			fsn.status_of_report = params[params[2]];
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
				fsn.returnData = response.testResult;
//				fsn.returnData.status = fsn.status_of_report;
				
				if(myTr.model.data){
					if(myTr.model.data.backResult!=null &&myTr.model.data.backResult!=""&&myTr.model.data.backResult!="undefined"&& lims.hostname!=undefined){
						//lims.msg("backMsg", null, ("退回原因："+ myTr.model.data.backResult));
						var html=myTr.model.data.backResult;
						html+='<div>';
						 for(var i in myTr.model.data.repBackAttachments){
							 html+='<a style="float:left;margin-right:10px;" target="_blank" href="'+myTr.model.data.repBackAttachments[i].url+'"><img src="'+myTr.model.data.repBackAttachments[i].url+'" width="100" /></a>';
						 }
						 html+='</div>';
						 fsn.backMsg("backMsg","退回原因："+html);
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
		
		previewFlag=true;
		$("#report_View").attr("href", data.fullPdfPath);
		$("#report_View").attr("target","_blank");
		
		// 发布按钮的点击事件
		$("#publish").click(function(){
			myTr.model.data.id = data.id;
			myTr.model.publishDetail(function(response){
 			   if(response.result.status == "true"){
 				   fsn.initNotificationMes("样品发布成功！", true);
 				   setTimeout('testResult.returnView()',2500);
				}
 		   });
		});
		
		// 从portal退回按钮的点击事件
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

	/**
	 * 功能描述：从 testlab 确认退回
	 * @author zhangHui 2015/6/29
	 */
	fsn.back_yes = function(returnMes){
		//避免多次提交
		$("#send_back_report_yes_btn").attr("disabled",'disabled');
		// 获取退回原因msg
 		var send_back_msg = "";
 		var index=1;
 		$("input[name='send_back_msg']:checked").each(function(i,e){
 			index=i+1;
 			send_back_msg+=index+"、"+$(this).attr('alt');
 			if($(this).next().is("input")||$(this).next().is("select")){
 				send_back_msg+=":"+$(this).next().val()+"；";
 			}
 		});
 		if($.trim($("#send_back_msg_other").val())!=""){
 			++index;
 			send_back_msg+=index+":"+$.trim($("#send_back_msg_other").val());
 		}
 		if(send_back_msg == ""){
 			fsn.initNotificationMes("请选择退回原因，或者填写其他原因！", false);
 			$("#send_back_report_yes_btn").removeAttr("disabled");
 			return;
 		}
		var str = location.href; // 取得整个地址栏
		var num = str.indexOf("?");
		var urlStatus = "";
		if (str.indexOf("?") != -1) {
			var arr = str.split("&"); // 各个参数放到数组里
			for ( var i = 0; i < arr.length; i++) {
				num = arr[i].indexOf("=");
				if (num > 0) {
					var name = arr[i].substring(0, num);
					var value = arr[i].substr(num + 1);
					if (name == "status") {
						urlStatus = value;
					}
				}
			}
		}
		returnData = fsn.returnData;
		/**
		 * 结构化报告退回流程
		 * @author ZhangHui 2015/5/6
		 */
		if(fsn.isStruct == true){
			/**
			 * true  代表 直接退回至供应商
			 * false 代表 退回至结构化人员
			 */
			var isSendBackToGYS = true;
			if(returnData.status == 2 || urlStatus== '2'){
				isSendBackToGYS = false;
			}
			var url = portal.HTTP_PREFIX + "/test/sendBack/structured/" + returnData.id + "/" + isSendBackToGYS;
			var data = {"returnMes":encodeURIComponent(send_back_msg),"repBackAttachments":root.returnFile};
//			if(fsn.returnData.status==2){
			if(fsn.status_of_report==2){
				testResult.send_back(url, data, returnData, "structured_test_structure_result_grid",true);
			}else{
//				if(fsn.returnData.back_result==null||fsn.returnData.back_result==""){
					testResult.send_back(url, data, returnData, "structured_test_no_structure_result_grid",true);
//				}else{
//					testResult.send_back(url, data, returnData, "re_audit_no_structure_result_grid",true);
//				}
			}
			fsn.isStruct = false;
			return;
		}
		
		/**
		 * 正常退回流程
		 */
		var from = returnData.edition;
		// 如果为easy的数据
		if(from == "easy"){
			/*是否退回生产企业信息*/
			var url = portal.HTTP_PREFIX + "/testReport/goBack/"+from+"/" + returnData.id;
			var data = {"returnMes":encodeURIComponent(send_back_msg),"repBackAttachments":root.returnFile};
			testResult.send_back(url, data, returnData, "test_result_grid",true);
		}else{
			// 如果为其他的数据
			var organization=returnData.organization;
			var returnURL=fsn.getReturnReportURL(from,organization);
			var url = fsn.HTTP_PREFIX + "/test/" + returnData.id;
			var data = {"returnURL":returnURL,"returnMes":encodeURIComponent(send_back_msg),"from":from};
			testResult.send_back(url, data, returnData, "test_result_grid",false);
		}
	};
	
	/**
 	 * 功能描述：取消从 testlab 退回操作
 	 * @author zhangHui 2015/6/29
 	 */
	fsn.back_no = function(){
 		$("#send_back_report_warn_window").data("kendoWindow").close();
 	};
	
	/**
	 * 退回报告
	 * @author ZhangHui 2015/5/6
	 */
	testResult.send_back = function(url, data, returnData, divId,isJson){
		var contentType="application/x-www-form-urlencoded"; 
		if(isJson){
			data=JSON.stringify(data);
			contentType="application/json;charset=UTF-8";
		}
		$.ajax({
			url: url,
			type:"POST",
			dataType: "json",
            data: data,
            async:false,
            contentType:contentType,
			success:function(data){
				// 关闭退回原因window
				$("#send_back_report_warn_window").data("kendoWindow").close();
				if(data.result.status == "true"){
					if(previewFlag){
						fsn.initNotificationMes("报告"+returnData.serviceOrder+"退回成功！", true);
						testResult.returnView();
						previewFlag=false;
					}else{
						/**没办法判断待审核和未审核状态,所以两个表格都会进行刷新*/
						if(divId=="structured_test_no_structure_result_grid"){
							$("#re_audit_no_structure_result_grid").data("kendoGrid").dataSource.remove(returnData);
							$("#re_audit_no_structure_result_grid").data("kendoGrid").refresh();
							$("#re_audit_no_structure_result_grid").data("kendoGrid").dataSource.read();
						}
						/**代码里下了毒结束*/
						$("#" + divId).data("kendoGrid").dataSource.remove(returnData);
						$("#" + divId).data("kendoGrid").refresh();
						$("#" + divId).data("kendoGrid").dataSource.read();
						fsn.initNotificationMes("报告"+returnData.serviceOrder+"退回成功！", true);
						//当退回当前grid中的所有报告时，需要自动加载下一页的内容
					}
				}else{
					fsn.initNotificationMes("报告"+returnData.serviceOrder+"退回失败！", false);
				}
				$("#send_back_report_yes_btn").removeAttr("disabled");
			}
		  });
	};
	
	testResult.returnView=function(){
		$("#publish").hide("slow");
		$("#Ruturn").hide("slow");
		window.location.href="/fsn-core/views/lab/test-result-manage.html";
	};
	
});