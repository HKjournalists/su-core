$(document).ready(function(){
	var lims = window.lims = window.lims || {};
    var root = window.lims.root = window.lims.root || {};
    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    root._testProperties = [];
    root.reportImgList=[];
    root.checkImgList=[];
    root.buyImgList=[];
    
    /**
     * 当前操作是否为新增报告操作
     * 		true  代表是新增操作
     * 		false 代表是更新操作
     */
    root.isNew = true;
    
    /**
     * 是否自动生成pdf的标记
     * true: 自动生成pdf
     * false: 用户自定义上传pdf
     * @author ZhangHui 2015/4/30
     */
    root.isAutoReport = false;
    
    /**
     * 是否使用预览小图片方式展示产品图片标记
     * true: 使用预览小图片
     * false: 使用文件名
     * @author LongXianzhen 2015/6/12
     */
    root.isViewImage = true;
    
    /**
     * 产品赋值类型：
     *  product_input 代表 产品信息使用input输入框赋值
     *  product_text  代表 产品信息使用text赋值
     *  @author tangxin 2015/6/8
     */
    root.product_html_type = "product_text";
    
    /**
     * 当前操作是否为portal待更新报告申请处理请求
     * 		true  代表是
     * 		false 代表不是
     */
    root.updateApply = false;
    
    /**
     * 生产日期是否必填
     * true: 必填
     * false: 不必填
     * @author LongXianzhen 2015/7/16
     */
    root.proDateIsRequired = true;

    root.aryProAttachments = new Array();
    root.aryRepAttachments = new Array();
   
    /* 初始化页面 */
    root.initialize = function(){
        /* 初始化报告pdf上传控件 */
        $("#upload_rep").html("<input id='upload_report_files' type='file' />");
        root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        root.buildUpload("upload_report_img", root.reportImgList, "repFileMsg", "product");
        root.buildUpload("upload_check_img", root.checkImgList, "repFileMsg", "product");
        root.buildUpload("upload_buy_img", root.buyImgList, "repFileMsg", "product");
        $("#backMsg").draggable();
        /* 初始化控件 */
        root.initComponent();
        
       
        
        /* 初始化条形码下拉框 */
        root.initialBarcodeComp();
        
        root.validateComponent();
        
        /* 倒计时 */
        timer();
        
        /* 为图片合成pdf窗口的按钮绑定事件 */
        lims.showPicToPdfButton();
        
        
        
        /* 初始化页面数据 */
        root.initialReportData();
        root.gridDataSource = new kendo.data.DataSource({
		    data: root._testProperties,
		    batch: true,
		    page: 1,
		    //pageSize: 8,
		    serverPaging: true,
		    serverFiltering: true,
		    serverSorting: true
		});
//		/* 检测报告grid初始化 */
		portal.initTestPropertyGrid(root.gridDataSource);
       
    };
    
    /**
	 * 初始化条形码下拉框
	 * @author ZhangHui 2015/6/12
	 */
	root.initialBarcodeComp = function(){
//	  $("#barcodeId").unbind("change");
//      $("#barcodeId").bind("change", root.judgeProductByBarcode);
		$("#barcodeId").kendoComboBox({
	        change:root.judgeProductByBarcode,
	        dataSource: [],
	        filter: "startswith",
	        minLength: 0,
	        index:0,
	    });
	};
   
    root.initialReportData = function(){
    	// 生产企业信息赋值
        var currentBusiness = getCurrentBusiness();
        root.current_bus_vo = {
        		name: currentBusiness.name,
        		licenseno: currentBusiness.licenseNo,
        		address: currentBusiness.address,
        };
        /**
         * 判断生产日期是否必填，非必填时去掉必填限制
         * @author longxianzhen 2015/07/17
         */
        root.proDateIsRequired=currentBusiness.proDateIsRequired;
    	if(!root.proDateIsRequired){
    		$("#tri_proDate").removeAttr("data-required-msg");
    		$("#tri_proDate").removeAttr("required");
    		$("#pDateRed").html("");
    		$("#tDateRed").html("");
    		
    	}
		// 从报告新增申请处理页面跳转来时，根据条形码直接加载数据
		if(root.fromPage == "show_reportRenew"){
			$("#update").hide();
            $("#add").hide();
			$("#barcodeId").val(root.initBarcode);
			root.judgeProductByBarcode();
            //root.my_autoloadingPageInfoByBarcode(root.initBarcode);
            $("#barcodeId").attr("disabled", true);
            $("#tri_testType").data("kendoDropDownList").value(root.testType);
            root.updateApply = true;
			return;
        }
        if (root.edit_id) {
        	root.isNew = false;
            $.ajax({
                url: portal.HTTP_PREFIX + "/testReport/" + root.edit_id,
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnValue){
                    fsn.endTime = new Date();
                    if (returnValue.result.status == "true") {
                    	var report_vo = returnValue.data;
                    	var bus_vo = report_vo.bus_vo;
                    	root.qs_vo = bus_vo==null?null:bus_vo.qs_vo;
                    	
                    	if(report_vo.autoReportFlag){
                    		report_vo.repAttachments = new Array();
                    	}
                    	
                    	root._testProperties = report_vo.testProperties;
                    	report_vo.bus_vo = null;
                   	 	fsn.setReportValue(report_vo, root.product_html_type);
                    }
                },
                error: function(e){
                    if (e.status == 911) {
                        lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
                    }
                    else {
                        lims.initNotificationMes('系统异常！', false);
                    }
                }
            });
        }
        if (root.isNew) {
            $.ajax({
                url: portal.HTTP_PREFIX + "/tempReport/getTempReport",
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnValue){
                	fsn.endTime = new Date();
                    if (returnValue.result.status == "true" && returnValue.data != null) {
                    	var report_vo = returnValue.data;
                    	var bus_vo = report_vo.bus_vo;
                    	
                    	
                    	if(!bus_vo.can_edit_qs){
                    		root.qs_vo = bus_vo==null?null:bus_vo.qs_vo;
                    	}
                    	
                    	root._testProperties = report_vo.testProperties;
                    	fsn.setReportValue(report_vo, root.product_html_type);
                    }
                },
                error: function(e){
                    if (e.status == 911) {
                        lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
                    }
                    else {
                        lims.initNotificationMes('系统异常！', false);
                    }
                }
            });
        }
        
        if (root.isNew) {
            $("#update").hide();
            $("#add").hide();
        } else {
            $("#save").hide();
            $("#clear").hide();
            $("#submit").hide();
            
            // 报告编辑状态下，以下字段不允许编辑：产品条形码
            $("#barcodeId").data("kendoComboBox").enable(false);
        }
        
        root.current_bus_vo.qs_vo = root.qs_vo;
        setBusunitValue(root.current_bus_vo);
    };
    
    root.judgeProductByBarcode = function(){
    	var pass = root.check_barcode_availability();
    	if(!pass){
    		return;
    	}
		root.my_autoloadingPageInfoByBarcode($("#barcodeId").val().trim());
		
    };
    
    /**
     * 不同于流通企业和经销商的是，此方法无需对生产企业赋值
     */
   
    root.my_autoloadingPageInfoByBarcode = function(barcode){
    	
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/getReportByBarcode/" + barcode,
            type: "GET",
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function(returnValue){
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                	var report_vo = returnValue.data;
                	var bus_vo = report_vo.bus_vo;
                	
                	var qs_no = "";
                	if(bus_vo.can_edit_qs==false && bus_vo!=null && bus_vo.qs_vo!=null){
                		qs_no = bus_vo.qs_vo.qsNo;
                		root.current_bus_vo.qs_vo = bus_vo.qs_vo;
                	}
                	
                	report_vo.bus_vo = root.current_bus_vo;
                	report_vo.repAttachments = new Array();
                	root._testProperties = report_vo.testProperties;
               	 	fsn.setReportValue(report_vo, root.product_html_type);
               	 	
               	 	// qs号赋值
                	$("#bus_qsNo").val(qs_no);
                }
            },
            error: function(e){
                if (e.status == 911) {
                    lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
                }
                else {
                    lims.initNotificationMes('系统异常！', false);
                }
            }
        });
        /* 当条形码改变时，检测项目需要重新获取值 */
//		portal.initTestPropertyGrid();
    };
    
    root.createInstance = function(){
    	var product_vo = {
    			barcode: $("#barcodeId").val().trim(),
    			batchSerialNo: $("#tri_batchNo").val().trim(),
    			productionDate: $("#tri_proDate").val().trim(),
    			expirationDate:$("#inputDay").val().trim(),
    			expireDate:$("#tri_proDate").val().trim(),
    			expire_Date:$("#expiration_date").val().trim(),
    			can_edit_pro: false,
    			
    			// 以下字段在自动生成pdf中会用到
    			name: $("#foodName").text(),
    			format: $("#specification").text(),
    			status: $("#foodInfo_Status").text(),
    			brand_name: $("#foodinfo_brand").text(),
    	};
    	
    	var qs_no = $("#bus_qsNo").val().trim();
    	if(qs_no != ""){
        	var qs_info = {
        			qsNo: qs_no,
        	};
        	
        	product_vo.qs_info = qs_info;
        }
    	
    	var bus_vo = {
    			name: $("#bus_name").val().trim(),
    			licenseno: $("#bus_licenseNo").val().trim(),
    			address: $("#bus_address").val().trim(),
    			can_edit_qs: false,
    			can_edit_bus: false,
    	};
    	
    	// 去除检测项目中检测名称为空的项目
    	root.removePropertiesOfEmpty("report_grid");
    	
    	var report_vo = {
    			id: root.isNew?null:root.edit_id,
    			serviceOrder: $("#tri_reportNo").val().trim(),
    			testee: $("#tri_testee").val().trim(),
    			sampleQuantity: $("#sampleCounts").val().trim(),
    			samplingLocation: $("#sampleAds").val().trim(),
    			testPlace: $("#tri_testPlace").val().trim(),
    			testDate: $("#tri_testDate").val().trim(),
    			testType: $("#tri_testType").data("kendoDropDownList").value(),
    			standard: $("#judgeStandard").val().trim(),
    			result: $("#testResultDescribe").val().trim(),
    			pass: $("#tri_conclusion").data("kendoDropDownList").value() == "1" ? true: false,
    			comment: $("#remarks").val().trim(),
    			autoReportFlag: root.isAutoReport,
    			testOrgnization: $("#tri_testOrg").val().trim(),
    			testProperties: $("#report_grid").data("kendoGrid").dataSource.data(),
    			repAttachments: root.aryRepAttachments,
    			publishFlag: '3',
    			product_vo: product_vo,
    			bus_vo: bus_vo,
    			new_flag: root.isNew
    	};
    	if(report_vo.testType=='第三方检测'){
    		report_vo.reportImgList=root.reportImgList,
    		report_vo.checkImgList=root.checkImgList,
    		report_vo.buyImgList=root.buyImgList
    	}else{
    		report_vo.reportImgList=[];
    		report_vo.checkImgList=[];
    		report_vo.buyImgList=[];
    	}
    	
    	return report_vo;
    };
    
    root.submit = function(){
    	
    	var pass = root.submit_check();
    	if(!pass){
    		return;
    	}
    	
        $("#saving_msg").html("");
        $("#saving_msg").html("正在提交，请稍候...");
        $("#toSaveWindow").data("kendoWindow").open().center();
        
        var report_vo = root.createInstance("submit");
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/" + root.updateApply,
            type: "POST",
            dataType: "json",
            timeout: 600000, //10min
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(report_vo),
            success: function(returnValue){
                fsn.endTime = new Date();
                $("#save_status").html("");
                $("#toSaveWindow").data("kendoWindow").close();
                
                if (returnValue.result.status == "true") {
                	var report_vo = returnValue.data;
                	
                	lims.initNotificationMes('编号为' + report_vo.serviceOrder + '的检测报告 ' + (root.isNew ? lims.l("Add") : lims.l("Update")) + '成功！', true);
                	
                	// 清空缓存信息
                	root.clearTempReport();
                	
                	// 将检测项目发回至KMS
                	fsn.sendItemsToKMS();
                    
                    if (!report_vo.autoReportFlag) {
                        setRepAttachments(report_vo.repAttachments);
                    }
                   //提交成功后，重新查询检测检测项目

                    $("#report_grid").data("kendoGrid").dataSource.data(report_vo.testProperties == null ? [] : report_vo.testProperties);
                    
                    // 页面状态赋值
                    root.isNew = false;
                    root.ReportNo_old = report_vo.serviceOrder;
                    root.edit_id = report_vo.id;
                    $("ul.k-upload-files").remove();
                    $("#save").hide();
                    $("#clear").hide();
                    $("#submit").hide();
                    $("#update").show();
                    $("#add").show();
                    
                    // 报告编辑状态下，以下字段不允许编辑：产品条形
                    $("#barcodeId").data("kendoComboBox").enable(false);
                } else {
                	lims.initNotificationMes((root.isNew ? lims.l("Add") : lims.l("Update")) + '失败！', false);
                }
            },
            error: function(e){
            	$("#saving_msg").html("");
                $("#toSaveWindow").data("kendoWindow").close();
                if (e.status == 911) {
                    lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
                } else {
                    lims.initNotificationMes('系统异常！', false);
                }
            }
        });
    };
    
    root.submit_check = function(){
        root.clearErrorInfoStyle();
       
        /**
         * 产品条形码有效性校验
         */
        var pass = root.check_barcode_availability();
        if(!pass){
        	return false;
        }
        
        // 报告编号校验
        if($("#tri_reportNo").val().trim()==""){
        	lims.initNotificationMes("报告编号不能为空！", false);
            return false;
        }
        
        //  被检测单位/人 校验
        if($("#tri_testee").val().trim()==""){
        	lims.initNotificationMes("被检测单位/人 不能为空！", false);
            return false;
        }
        /**
         * 当生产日期必填时才进行验证
         */
        if(root.proDateIsRequired){
        	// 生产日期校验
            $("#tri_proDate_format").data("kendoDatePicker").value($("#tri_proDate").val());
            if($("#tri_proDate").val().trim()!=""){
            	if ($("#tri_proDate_format").val().trim()=="") {
                    lims.initNotificationMes("生产日期格式不正确，请重新填写！", false);
                    return false;
                }
            }else{
            	lims.initNotificationMes("生产日期不能为空！", false);
                return false;
            }
            
            // 判断生产日期是否小于等于检验日期
            var proDatePicker = $("#tri_proDate").data("kendoDatePicker");
            var testDatePicker = $("#tri_testDate").data("kendoDatePicker");
            proDatePicker.value($("#tri_proDate").val().trim());
            testDatePicker.value($("#tri_testDate").val().trim());
//            var proDate = proDatePicker.value();
//            var testDate = testDatePicker.value();
//            if(proDate > testDate){
//            	lims.initNotificationMes("检验日期必须大于等于生产日期！", false);
//                return false;
//            }
        }
        
        $("#tri_testDate_format").data("kendoDatePicker").value($("#tri_testDate").val());
        $("#tri_proDate_format").data("kendoDatePicker").value($("#tri_proDate").val());
        
        // 生产日期校验
        if($("#tri_proDate").val().trim()!=""){
        	if ($("#tri_proDate_format").val().trim()=="") {
                lims.initNotificationMes("生产日期格式不正确，请重新填写！", false);
                return false;
            }
        }
        
        // 检验日期校验
        if($("#tri_testDate").val().trim() != "") {
            if ($("#tri_testDate_format").val().trim() == "") {
                lims.initNotificationMes("检验日期格式不正确，请重新填写！", false);
                return false;
            }
        }else{
        	lims.initNotificationMes("检验日期不能为空！", false);
        	return false;
        }
       var inputDay=$("#inputDay").val().trim();//保质日期
   	   var productionDateStr = $("#tri_proDate").val().trim(); // 生产日期字符串
   	    //日期过期验证
   	    var flag = root.setTriPorDate(productionDateStr,inputDay);
   	    if(!flag){
   	    	return false;
   	    }
        // 批次校验
        if($("#tri_batchNo").val().trim()==""){
        	lims.initNotificationMes("批次 不能为空！", false);
            return false;
        }
        
        // 报告执行标准校验
        if($("#judgeStandard").val().trim()==""){
        	lims.initNotificationMes("执行标准 不能为空！", false);
            return false;
        }
        
        // 检验结论描述校验
        if($("#testResultDescribe").val().trim()==""){
        	lims.initNotificationMes("检验结论描述 不能为空！", false);
            return false;
        }
        
        // 报告唯一性校验
        if(root.isNew && !root.validateReportNO()) {
	            $("#tri_reportNo").attr("style", "border:2px solid red;");
	            lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" +
	            $("#barcodeId").val().trim() +
	            "】、批次号【" +
	            $("#tri_batchNo").val().trim() +
	            "】已经存在对应的报告，3个字段不能重复！", false);
	            return false;
        }else if(root.ReportNo_old != $("#tri_reportNo").val().trim() && !root.validateReportNO()) {
	             $("#tri_reportNo").attr("style", "border:2px solid red;");
	             lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" +
	             $("#barcodeId").val().trim() +
	             "】、批次号【" +
	             $("#tri_batchNo").val().trim() +
	             "】已经存在对应的报告，3个字段不能重复！", false);
	             return false;
        }
        
        // 是否需要上传pdf校验
        if (!root.isAutoReport && root.aryRepAttachments.length == 0) {
            lims.initNotificationMes(lims.l("not found pdf"), false);
            return false;
        } else if (root.isAutoReport && root.aryRepAttachments.length > 0) {
                lims.initNotificationMes(lims.l("auto create pdf"), false);
                return false;
        }
       
        if (root.isAutoReport && $("#tri_testType").data("kendoDropDownList").value() != "企业自检") {
            lims.initNotificationMes("只有在【检验类别】为企业自检的情况下，才能选择自动生成pdf，否则请手动上传pdf!", false);
            return false;
        }
        
        // 检测项目校验
        var gridItem = $("#report_grid").data("kendoGrid").dataSource.data();
        if(gridItem.length < 1 && root.isAutoReport) {
            lims.initNotificationMes("至少需要一条检测项目！", false);
            return false;
        }else{
            var count = 0;
            var message = "";
            for (var i = 0; i < gridItem.length; i++) {
                if (gridItem[i].name != "" || gridItem[i].unit != "" || gridItem[i].techIndicator != "" || gridItem[i].result != "" || gridItem[i].standard != "") {
                    if (gridItem[i].name == "") {
                        message = "【检查项目名称】";
                    }
                    if (gridItem[i].result == "") {
                        message += "【检测结果】";
                    }
                    if (message != "") {
                        lims.initNotificationMes('检测项目列表的第' + (i + 1) + '行' + message + '为必填项！', false);
                        return false;
                    }
                    count++;
                }
            }
            if (root.isAutoReport && count == 0) {
                lims.initNotificationMes('至少需要一条检测项目！', false);
                return false;
            }
        }
        
        return true;
    };
    
    //清除输入框的错误提示样式
    root.clearErrorInfoStyle = function(){
        $("#tri_reportNo").removeAttr("style");
        $("#tri_proDate").attr("style", "width:100%;height:100%;");
        $("#tri_testDate").attr("style", "width:100%;height:100%");
    };
    
    root.addNext = function(){
        window.location.pathname = "/fsn-core/views/market/subBusiness_addReport.html";
    };
    
    /**
     * 功能描述：校验条形码的合法性
     * @author ZhangHui 2015/6/15
     */
    root.check_barcode_availability = function(){
        var barcode = $("#barcodeId").val().trim();
        if(barcode == ""){
        	lims.initNotificationMes('请选择产品条形码！', false);
    		return false;
    	}
        /*
        if(root.listOfBarCodes==null || root.listOfBarCodes.length==0){
        	lims.initNotificationMes('您当前没有可以使用的产品，请前往产品管理界面新增产品！', false);
        	return false;
        }
        if(root.fromPage != "show_reportRenew"){
        	var selectItem = $("#barcodeId").data("kendoComboBox").select();
     		if(selectItem < 0){
     			$("#barcodeId").data("kendoComboBox").value("");
     			lims.initNotificationMes("手动输入的条形码必须存在下拉列表中，建议通过下拉选择！", false);
     			return false;
     		}
        }
        */
		return true;
    };
    
    /**
     * 掉线提醒确定事件
     * @author ZhangHui 2015/4/16
     */
    root.remind = function(){
    	$("#diaoxian_remind_01").data("kendoWindow").close();
    	$("#diaoxian_remind_02").data("kendoWindow").close();
    	
    	fsn.save();
    };
    
    root.my_clearTempReport = function(){
    	root.clearTempReport();

    	if(root.isNew){
    		// 清空页面数据
    		root.current_bus_vo.qs_vo = null;
    		root.aryRepAttachments.length = 0;
    		var report_vo = {
    				bus_vo: root.current_bus_vo,
    		};
    		
    		fsn.setReportValue(report_vo, root.product_html_type);
    		
    		/* 初始化报告pdf上传控件 */
            $("#upload_rep").html("<input id='upload_report_files' type='file' />");
            root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
            removeDisabledToBtn(["openT2P_btn"]);
            
            $("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
        }
    };
    
    try {
        root.edit_id = $.cookie("user_0_edit_testreport").id;
		root.initBarcode = $.cookie("user_0_edit_testreport").barcode;
        root.fromPage = $.cookie("user_0_edit_testreport").from;
		root.testType = $.cookie("user_0_edit_testreport").testType;
        $.cookie("user_0_edit_testreport", JSON.stringify({}), {
            path: '/'
        });
    } catch(e) {}
    root.upSelect = function(e,tdDate){
		  var data =  $("#report_grid").data("kendoGrid").dataSource.data();
	      var  trIndex = $(e.currentTarget).closest("tr").index();
        if(trIndex == 0){
      	  lims.initNotificationMes('已经是第一行，不能再往上移动了！', false);
      	  return;
        }
	      for(var i = 0 ; i<data.length;i++){
      	  if(i == trIndex){
      		  data[trIndex] = data[trIndex-1];
      		  data[trIndex-1] = tdDate; 
      		break;
      	  }
       }
       var itemGrid = $("#report_grid").data("kendoGrid");
   	 if(itemGrid){
   		 itemGrid.dataSource.data(data?data:[]);
   	 }
	};
	root.downSelect = function(e,tdDate){
		var data =  $("#report_grid").data("kendoGrid").dataSource.data();
	      var  trIndex = $(e.currentTarget).closest("tr").index();
	      if(trIndex == data.length-1){
      	  lims.initNotificationMes('已经是最后一行，不能再往下移动了！', false);
      	  return;
        }
        for(var i = 0 ; i<data.length;i++){
      	  if(i == trIndex){
      		  data[trIndex] = data[i+1];
      		  data[i+1] = tdDate; 
      		break;
      	  }
       }
       var itemGrid = $("#report_grid").data("kendoGrid");
   	 if(itemGrid){
   		 itemGrid.dataSource.data(data?data:[]);
   	 }
	};
	root.initialize();
});
