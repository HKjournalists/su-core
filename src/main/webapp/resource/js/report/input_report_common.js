var lims = window.lims = window.lims || {}; // 全局命名空间
var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var root = window.lims.root = window.lims.root || {}; // root命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀

/**
 * 温馨提示样式控制
 * @author ZhangHui 2015/4/3
 */
root.changeGuidStyle = function(isShow){
    if (isShow) {
        $("#skin-box-bd").css("display", "block");
    }
    else {
        $("#skin-box-bd").css("display", "none");
    }
};

/**
 * 初始化掉线窗口
 * @author ZhangHui 2015/4/3
 */
root.initDiaoXianWindow = function(){
	$("#diaoxian_logout").kendoWindow({
        width: "235px",
        height: "130px",
        visible: false,
        title: "提示",
        modal: true,
        resizable: false,
        actions: []
    });
    $("#diaoxian_remind_01").kendoWindow({
        width: "440px",
        height: "170px",
        visible: false,
        title: "提示",
        modal: true,
        resizable: false,
        actions: []
    });
    $("#diaoxian_remind_02").kendoWindow({
        width: "340px",
        height: "150px",
        visible: false,
        title: "提示",
        modal: true,
        resizable: false,
        actions: []
    });
};

/**
 * 初始化页面数据
 * @author ZhangHui 2015/4/3
 */
root.initialReportData = function(){
    if (root.edit_id) {
    	root.isNew = false;
    	/* 编辑状态下: 初始化页面信息 */
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/" + root.edit_id,
            type: "GET",
            dataType: "json",
            async: false,
            success: function(returnValue){
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                    root.initBarcode = returnValue.data.sample.product.barcode;
                    root.initBusName = returnValue.data.sample.producer.name;
                    root.ReportNo_old = returnValue.data.serviceOrder;
                    /* 产品赋值 */
                    root.setProductVal(returnValue.data.sample);
                    /* 报告赋值 */
                    root.setReportVal(returnValue.data, false);
                    /* 当前报告是退回报告时，显示退回原因 */
                    if (returnValue.data.publishFlag == '2') {
                        $("#back").css('display', '');
                        $("#backResult").val(returnValue.data.backResult);
                        
                        lims.msg("backMsg", null, returnValue.data.backResult);
                        var backMsgDivH = document.getElementById("backMsg").offsetHeight;
                        if (backMsgDivH > 0) {
                            backMsgDivH = backMsgDivH + 10;
                            $("#content_container").css("padding-bottom", backMsgDivH + "px");
                        }
                    }
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
        
        /* 验证有无已注册的生产企业绑定了该产品  */
    	if (!root.isNew && !root.pro2Bus){
        	root.getProducerByBarcode(root.initBarcode);
        	var bus = root.getBusOfpro2BusByName(root.initBusName);
        	root.setEasyBusunitValue(bus);
        }
    }
    /* 报告新增状态下，从缓存 */
    if (root.isNew) {
        $.ajax({
            url: portal.HTTP_PREFIX + "/tempReport/getTempReport",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(returnValue){
                fsn.endTime = new Date();
                if (returnValue.result.status == "true" && returnValue.data != null) {
                    returnValue.data.id = 1; // (在执行setEasyReportValue中需按照报告id展示检测结论的值)
                    root.setProductVal(returnValue.data.sample);
                    root.setReportVal(returnValue.data, false);
                    root.brand = returnValue.data.sample.product.businessBrand;
                    /* qs号展示  */
                    var qsNoFormatVo = returnValue.data.sample.producer.qsNoAndFormatVo;
                    root.setQs(qsNoFormatVo);
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
    }
    else {
        $("#save").hide();
        $("#clear").hide();
        $("#submit").hide();
    }
};

/**
 * qs号赋值
 */
root.setQs = function(licenceFormatId, qsNo){
	var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
    qsFormatIt.value(licenceFormatId);
    var qsItem = qsFormatIt.dataItem();
    root.formetType = qsItem.formetType;
    root.formetValue = qsItem.formetValue;
    root.formetLength = qsItem.formetLength;
    if(qsItem.formetValue.indexOf("(?)")!=-1){
        $("#jianchengList").show();
        root.showJianCheng = true;
        var first = "";//省的简称
        var socend = qsNo.substring(0,2);//中间字母
        var third = qsNo.substr(2);//数字部分
        if(qsNo.indexOf("(")!=-1 && qsNo.indexOf(")")!=-1 ){
            first = qsNo.charAt(1);//省的简称
        	socend = qsItem.formetValue.replace("(?)","");//中间字母
        	third = qsNo.substr(5);//数字部分
        }
        root.setStyle(0);
        $("#proJianCheng").data("kendoDropDownList").text(first);
        $("#showQsFormat").html(socend);
        $("#bus_qsNo").val(third);
    }else{
    	root.setStyle(1);
        $("#jianchengList").hide();
        root.showJianCheng = false;
        $("#showQsFormat").html(qsItem.formetValue);
        if(qsNo){
        	$("#bus_qsNo").val(qsNo.replace(qsItem.formetValue, ""));
        }
    }
};

/**
 * 清除页面暂存信息
 * @author ZhangHui 2015/4/3
 */
root.clearTempReport = function(){
    $.ajax({
        url: portal.HTTP_PREFIX + "/tempReport/clearTempReport",
        type: "DELETE",
        dataType: "json",
        async: false,
        success: function(data){
            fsn.endTime = new Date();
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
};

/**
 * 清除页面报告信息
 * @author ZhangHui 2015/4/3
 */
root.clearReport = function(){
    root.clearProduct();
    $("#bus_qsNo").val("");
    $("#tri_reportNo").val("");
    $("#tri_testee").val("");
    $("#tri_testOrg").val("");
    $("#tri_conclusion").data("kendoDropDownList").value("1");
    $("#tri_testPlace").val("");
    $("#tri_proDate").val("");
    $("#tri_testDate").val("");
    $("#tri_batchNo").val("");
    $("#foodInfo_ads").val("");
    $("#sampleCounts").val("");
    $("#judgeStandard").val("");
    $("#testResultDescribe").val("");
    $("#sampleAds").val("");
    $("#remarks").val("");
    $("#tri_testType").data("kendoDropDownList").value("企业自检");
    root.clearRepFiles();
    root.aryRepAttachments.length = 0;
    $("#upload_rep").html("");
    $("#upload_rep").html("<input id='upload_report_files' type='file' />");
    root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
    lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
    var itemGrid = $("#report_grid").data("kendoGrid");
    if(itemGrid){
    	itemGrid.setDataSource(root.gridDataSource);
    }
    root.clearTempReport();
    lims.initNotificationMes("清除成功！", true);
};

/**
 * 
 * @author ZhangHui 2015/4/3
 */
root.getItems = function(){
    var items = [];
    var testItems = $("#report_grid").data("kendoGrid").dataSource.data();
    var j = 0;
    for (var i = 0; i < testItems.length; i++) {
        if (testItems[i].name.trim() == "" || testItems[i].result.trim() == "") {
            j++;
            continue;
        }
        items[i - j] = {
            id: testItems[i].id == null ? "" : testItems[i].id,
            name: testItems[i].name,
            unit: testItems[i].unit,
            techIndicator: testItems[i].techIndicator,
            result: testItems[i].result,
            assessment: testItems[i].assessment,
            standard: testItems[i].standard,
        };
    }
    return items;
};

/**
 * 图片转pdf时的预览方法
 * @author ZhangHui 2015/4/3
 */
root.ReviewPdf = function(){
    lims.viewPdf($("#tri_reportNo").val().trim());
};

/**
 * 图片合成pdf时的确定按钮事件
 */
root.picToPdfWinOk = function(){
    lims.picToPdfFun($("#tri_reportNo").val().trim());
};

/**
 * 增加一行检测项目
 */
root.grid_AddItem = function(){
    $("#report_grid").data("kendoGrid").dataSource.add({
        name: "",
        unit: "",
        techIndicator: "",
        result: "",
        assessment: "合格",
        standard: ""
    });
};

/**
 * 验证报告编号是否唯一
 */
root.validateReportNO = function(){
    var unique = true;
    var reportNo = $("#tri_reportNo").val().trim().replace(/\/+/g, "\\0gan0\\");
    var _data = new Array(reportNo, $("#barcodeId").val().trim(), $("#tri_batchNo").val().trim(), root.edit_id);
    $.ajax({
        url: portal.HTTP_PREFIX + "/testReport/validatReportNo",
        type: "PUT",
        dataType: "json",
        async: false,
        data: JSON.stringify(_data),
        contentType: "application/json; charset=utf-8",
        success: function(returnValue){
            fsn.endTime = new Date();
            if (returnValue.result.status == "true") {
                unique = returnValue.data;
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
    return unique;
};

/**
 * 页面刷新
 */
root.refresh = function(){
    window.location.pathname = "/fsn-core/views/market/subBusiness_addReport.html";
};

/**
 * 清空页面产品信息
 */
root.clearProduct = function(){
    $("#barcodeId").val("");
    $("#foodName").text("");
    $("#specification").text("");
    $("#foodinfo_model_no").text("");
    $("#foodinfo_brand").text("");
    $("#foodinfo_standard").text("");
    $("#proExpiratio").text("");
    $("#foodinfo_expirday").text("");
    $("#foodinfo_category").text("");
    $("#foodinfo_minunit").text("");
    $("#foodInfo_Status").text("");
    $("#proAttachmentsListView").html("");
};

/**
 * 绑定Click事件
 */
root.bindClick = function(resID, fileName){
	$("#btn_clearRepFiles").bind("click", root.clearRepFiles);
    $("#btn_clearRepFiles").hide();
    $("#add").bind("click", root.refresh);
    $("#save").bind("click", root.save);
    $("#submit").bind("click", root.submit);
    $("#update").bind("click", root.submit);
    $("#clear").bind("click", root.openClearReportWin);
    $("#logout_btn").bind("click", root.refresh);
    $("#save_btn_01").bind("click", root.save);
    $("#save_btn_02").bind("click", root.save);
    /* 温馨提示 */
    $("#immediate-guide-trigger").bind("mouseenter", function(){
        root.changeGuidStyle(true);
    });
    $("#immediate-guide-trigger").bind("mouseleave", function(){
        root.changeGuidStyle(false);
    });
    /* 返回 */
    $(".gTop").bind("mouseenter", function(){
        root.buileAnimate(".gTop a", "100px");
    });
    $(".gTop").bind("mouseleave", function(){
        root.buileAnimate(".gTop a", "100%");
    });
    $(".gPrev").bind("mouseenter", function(){
        root.buileAnimate(".gPrev a", "100px");
    });
    $(".gPrev").bind("mouseleave", function(){
        root.buileAnimate(".gPrev a", "100%");
    });
    
    $("#yesAuto").click(function(){
        $("#tr_isAuto").hide();
        root.isAutoReport = true;
    });
    
    $("#noAuto").click(function(){
        var repListView = $("#repAttachmentsListView").data("kendoListView");
        var pdfCont = 0; //当前上传的pdf数量
        if (repListView != null) 
            pdfCont = repListView.dataSource._total;
        //如果当前报告为新增状态，并且上传的pdf数量为小于1时，重新激活并初始化上传pdf的控件
        if (root.isNew && (repListView == null || pdfCont < 1)) {
            root.aryRepAttachments.length = 0;
            lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
            $("#upload_rep").html("<input id='upload_report_files' type='file' />");
            root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        }
        $("#tr_isAuto").show();
        root.isAutoReport = false;
    });
};

root.gridDataSource = new kendo.data.DataSource({
    data: [],
    batch: true,
    page: 1,
    //pageSize: 8,
    serverPaging: true,
    serverFiltering: true,
    serverSorting: true
});

root.getAutoItemsDS = function(){
    return new kendo.data.DataSource({
        transport: {
            read: {
                url: function(){
                    return portal.HTTP_PREFIX + "/testReport/autoItems/" + root.colName + "?page=1&pageSize=20&keyword=" + root.keyword;
                },
                type: "GET",
            }
        },
        schema: {
            data: "data"
        },
        change: function(e){
            var date = new Date();
            date.setSeconds(date.getSeconds() + 1);
            fsn.endTime = date;
        },
    });
};

root.buildAutoComplete = function(input, container, options){
    input.attr("name", options.field);
    input.attr("class", "k-textbox");
    input.appendTo(container);
    input.kendoAutoComplete({
        dataSource: [],
        filter: "contains",
        dataBound: function(){
            if (root.keyword == input.val().trim()) {
                return;
            }
            root.keyword = input.val().trim();
            input.data("kendoAutoComplete").setDataSource(root.kmsItemNamePage, root.keyword);
            input.data("kendoAutoComplete").search(root.keyword);
        },
    });
};

root.setDateValue = function(){
	var proDate = $("#tri_proDate_format").data("kendoDatePicker");
	if(proDate){
		proDate.value($("#tri_proDate").val());
	}
    var testDate = $("#tri_testDate_format").data("kendoDatePicker");
    if(testDate){
    	testDate.value($("#tri_testDate").val());
    }
};

/**
 * 条形码控件数据初始化(角色：生产企业)
 * @author Zhanghui 2015/4/7
 */
root.ListOfProductBarCode = new kendo.data.DataSource({
    transport: {
        read: {
            url: portal.HTTP_PREFIX + "/testReport/searchBarCodeAll",
            type: "GET"
        }
    },
    schema: {
        data: function(returnValue){
            fsn.endTime = new Date();
            root.listOfBarCodes = returnValue.data;
            return returnValue.data;
        }
    }
});

/**
 * 验证条形码是否绑定qs号
 * @author TangXin
 */
root.verifyBarcodeIsBindQsNo = function(barcode){
    $.ajax({
        url: portal.HTTP_PREFIX + "/testReport/verifyBarcodeIsBindQsNo/" + barcode,
        type: "GET",
        dataType: "json",
        async: false,
        success: function(returnValue){
            if (returnValue.result.status == "true") {
                if (returnValue.data) {
                    root.autoloadingPageInfoByBarcode($("#barcodeId").val());
                } else {
                    lims.initNotificationMes(lims.l("当前条形码：") + $("#barcodeId").val().trim() + lims.l(" 未绑定QS号，请重新输入。"), false);
                }
            } else {
                lims.initNotificationMes('加载该条形码的产品信息异常！', false);
            }
        }
    });
};

root.onSelectBarcode = function(e){
    root.initBarcode = this.dataItem(e.item.index());
    root.autoloadingPageInfoByBarcode(root.initBarcode);
};

$("#btn_saveOK").click(function(){
    $("#saveWindow").data("kendoWindow").close();
    root.submitReport();
});

$("#btn_saveCancel").click(function(){
    $("#saveWindow").data("kendoWindow").close();
});

$("#barcodeId").kendoValidator().data("kendoValidator");
$("#tri_batchNo").kendoValidator().data("kendoValidator");
$("#tri_reportNo").kendoValidator().data("kendoValidator");
$("#tri_proDate").kendoValidator().data("kendoValidator");
$("#bui_licenseNo").kendoValidator().data("kendoValidator");
$("#tri_testee").kendoValidator().data("kendoValidator");
$("#judgeStandard").kendoValidator().data("kendoValidator");
$("#testResultDescribe").kendoValidator().data("kendoValidator");

//清除输入框的错误提示样式
root.clearErrorInfoStyle = function(){
    $("#tri_reportNo").removeAttr("style");
    $("#tri_proDate").attr("style", "width:100%;height:100%;");
    $("#tri_testDate").attr("style", "width:100%;height:100%");
};

/**
 * 根据条形码自动加载页面信息
 * @author Zhanghui 2015/4/7
 */
root.autoloadingPageInfoByBarcode = function(barcode){
    $.ajax({
        url: portal.HTTP_PREFIX + "/testReport/getReportByBarcode/" + barcode,
        type: "GET",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(returnValue){
            fsn.endTime = new Date();
            if (returnValue.result.status == "true") {
                root.setProductVal(returnValue.data.sample);
                var proBrand = returnValue.data.sample.product.businessBrand;
                root.brand = {
                    id: proBrand.id,
                    name: proBrand.name,
                };
                
                root.pro2Bus = returnValue.data.pro2Bus;
                if(root.pro2Bus){
   				 	var listOfBusunitName = returnValue.data.listOfBusunitName;
   				 	$(".hide").show();
   				 	$("#hid_listBusName").data("kendoComboBox").setDataSource(listOfBusunitName);
   			 	}else{
   			 		$(".hide").hide();
   			 		$("#hid_listBusName").data("kendoComboBox").setDataSource([]);
   			 	}
                /* 报告赋值 */
                if (root.isNew) {
                    root.setReportVal(returnValue.data, true);
                }
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
};

/**
 * 根据条形码获取绑定了qs号的注册生产企业
 * @author Zhanghui 2015/4/7
 */
root.getProducerByBarcode = function(barcode){
    $.ajax({
        url: portal.HTTP_PREFIX + "/testReport/getReportByBarcode/" + barcode,
        type: "GET",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(returnValue){
            fsn.endTime = new Date();
            if (returnValue.result.status == "true") {
                root.pro2Bus = returnValue.data.pro2Bus;
                if(root.pro2Bus){
   				 	var listOfBusunitName = returnValue.data.listOfBusunitName;
   				 	$(".hide").show();
   				 	$("#hid_listBusName").data("kendoComboBox").setDataSource(listOfBusunitName);
   			 	}else{
   			 		$(".hide").hide();
   			 		$("#hid_listBusName").data("kendoComboBox").setDataSource([]);
   			 	}
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
};

root.setProductVal = function(sample){
    if (!sample) {
        return;
    }
    root.initBarcode = sample.product.barcode;
    var product = sample.product;
    if (product.name == null) {
        return;
    }
    $("#sample_id").val(sample.id);
    $("#barcodeId").val(product.barcode);
    $("#foodName").text(product.name != null ? product.name : "");
    $("#specification").text(product.format != null ? product.format : ""); // 规格
    $("#foodinfo_brand").text(product.businessBrand.name != null ? product.businessBrand.name : ""); // 商标
    $("#foodinfo_standard").text(lims.convertRegularityToString(product.regularity)==""?(product.regularityStr==null?"":product.regularityStr):lims.convertRegularityToString(product.regularity)); // 执行标准
    $("#proExpiratio").text(product.expiration != null ? product.expiration : ""); // 质保期（无）
    $("#foodinfo_expirday").text(product.expirationDate != null ? product.expirationDate : ""); // 保质天数
    $("#foodinfo_minunit").text(product.unit != null ? product.unit.name : ""); // 单位
    $("#foodInfo_Status").text(product.status != null ? product.status : ""); // 商品状态 
    var category = product.category;
    if (category != null && category.name != null) {//若该三级分类不存在，则显示二级分类的名称
        var secondCategory = category.category; //二级分类
        var secondCategoryName = (secondCategory == null ? "" : secondCategory.name); //二级分类名称
        $("#foodinfo_category").text(category.name == "--" ? secondCategoryName : category.name);//产品类别
    }
    else {
        $("#foodinfo_category").text("");
    }
	/*如果返回的执行标准数组为空，则提取执行标准字符串进行封装数组*/
	root.regularity = [];
	if(product.regularity.length!=0){
		root.regularity = product.regularity;
	}else if(product.regularityStr!=null){
		var listRegularity = [];
		var regularitys = product.regularityStr.trim().split(";");
		for (var i = 0; i < regularitys.length - 1; i++) {
			listRegularity[i] = {
				name: regularitys[i],
				category: {
					id: category.category.id,
				}
			};
		}
		root.regularity = listRegularity;
	}
    root.category = category;
    root.aryProAttachments = product.proAttachments;
    var mainDiv = $("#proAttachmentsListView");
    mainDiv.html("");
    for (var i = 0; i < product.proAttachments.length; i++) {
        var div = $("<div style='float:left;margin:10px 10px;'></div>");
        var img = $("<img width='50px' src='" + product.proAttachments[i].url + "'></img>");
        var a = $("<a href='" + product.proAttachments[i].url + "' target='_black'></a>");
        a.append(img);
        div.append(a);
        mainDiv.append(div);
    }
    if (sample.producer) {
        var qsNo_last = sample.producer.qsNo;
        var qs_NoList = qsNo_last == null ? "" : qsNo_last.toUpperCase().replace("QS", "");
        $("#bus_qsNo").val(qs_NoList);
    }
};

root.setBusinessVal = function(business){
    root.bussunitName = business.name;
    $("#bus_licenseNo").val("");
    $("#bus_name").val("");
    $("#bus_address").val("");
    if (business != null) {
		$("#bus_licenseNo").val(business.licenseNo);
        $("#bus_name").val(business.name);
        $("#bus_address").val(business.address);
    }
};

//给easy的BusUnit赋值
root.setEasyBusunitValue = function(busUnit){
	/* 清空页面所有样式 */
	$("#bus_licenseNo").val("");
	$("#bus_qsNo").val("");
	$("#bus_address").val("");
	lims.clearInputReadOnly(["bus_address","bus_qsNo","bus_licenseNo"]);
	$(".editQsStyle").css("display","none");
	var qsNoFormat = $("#listqsFormat").data("kendoDropDownList");
    if(qsNoFormat){
        qsNoFormat.readonly(false);
        $("#proJianCheng").data("kendoDropDownList").readonly(false);
    }
	if(busUnit == null) return;
	
	/* 生产企业信息赋值 */
	$("#bus_licenseNo").val(busUnit.license!=null?busUnit.license.licenseNo:"");
	$("#bus_name").val((busUnit.name==null||busUnit.name=="")?"":busUnit.name);
	$("#bus_address").val((busUnit.address==null||busUnit.address=="")?"":busUnit.address);
	/* 判断此生产企业是否为平台注册用户 */
	if(busUnit.organization!=null && busUnit.organization!=0){
		$(".editQsStyle span").html("注意：当前生产企业已经通过注册，此处不允许再修改企业基本信息。");
		$(".editQsStyle").css("display","inline");
		lims.addInputReadOnly(["bus_address","bus_licenseNo"]);
	}
    /* 判断此生产企业对此产品有无绑定qs号 */
	if(busUnit.bindQsFlag){
		$(".editQsStyle span").html("注意：当前生产企业已经通过注册，并且已经对该产品绑定了QS证号，此处不允许再修改。");
		$(".editQsStyle").css("display","inline");
        qsNoFormat.readonly(true);
        $("#proJianCheng").data("kendoDropDownList").readonly(true);
		lims.addInputReadOnly(["bus_qsNo"]);
	}
	/* qs号赋值 */
	if(busUnit.qsNoAndFormatVo){
		root.formatId = busUnit.qsNoAndFormatVo.licenceFormat.id;
		root.setQs(root.formatId, busUnit.qsNoAndFormatVo.qsNo);	
	}
};

root.setReportVal = function(report, isSearch){
    if (!report) {
        return;
    }
    if (!isSearch && report.serviceOrder == null) {
        return;
    }
    $("#tri_reportNo").val(report.serviceOrder);
    $("#tri_testee").val(report.testee == null ? "" : report.testee.name);
    $("#tri_testOrg").val(report.testOrgnization);
    $("#tri_testPlace").val(report.testPlace);
    $("#tri_testType").data("kendoDropDownList").value(report.testType);
    if (report.id == null) {
        $("#tri_conclusion").data("kendoDropDownList").value("1");
    }
    else {
        $("#tri_conclusion").data("kendoDropDownList").value(report.pass ? "1" : "0");
    }
    $("#tri_testPlace").val(); // 检验地点 (无)
    /*如果样品实例存在，当生产日期为空的时候默认显示 0000-00-00，注意后台返回的数据只有当id不为空的时候，
	 * 才表示样品实例存在。
	 */
    if (report.sample != null && report.sample.id != null) {
        $("#tri_proDate").val(report.sample.productionDate == null ? "0000-00-00" : report.sample.productionDate.substr(0, 10));
    }
    if (report.testDate) {
        $("#tri_testDate").val(report.testDate.substr(0, 10));
    }
    $("#tri_batchNo").val(report.sample.batchSerialNo); // 批次
    $("#sampleAds").val(report.samplingLocation); // 抽样地点
    $("#sampleCounts").val(report.sampleQuantity); // 抽样量
    $("#judgeStandard").val(report.standard);
    $("#testResultDescribe").val(report.result);
    $("#remarks").val(report.comment);
    $("#foodInfo_ads").val(report.sample.producer == null ? "" : report.sample.producer.address);
    if (report.autoReportFlag) {
        document.getElementById("yesAuto").checked = true;
        $("#tr_isAuto").hide();
    }
    else {
    	var autobutton = document.getElementById("noAuto");
    	if(autobutton){
    		autobutton.checked = true;
    	}
        $("#tr_isAuto").show();
        if (!isSearch) {
            root.setRepAttachments(report.repAttachments);
        }
    }
    root.isAutoReport = report.autoReportFlag;
};

/**
 * 初始化生产企业信息（角色：生产企业）
 * @author Zhanghui 2015/4/7
 */
root.getCurrentBusiness = function(){
    $.ajax({
        url: portal.HTTP_PREFIX + "/business/getCurrentBusiness",
        type: "GET",
        async: false,
        success: function(data){
            fsn.endTime = new Date();
            if (data.status) {
                root.setBusinessVal(data.result);
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
};

/**
 * 特殊符号/单位选择窗口
 */
root.openSpeCharWin = function(){
    $("#speCharWindow").data("kendoWindow").open().center();
};

/**
 * 打开导入检测项目的窗口
 */
root.openExel_Win = function(){
    $("#up_ex_fileDiv").html("<input id='up_ex_file' type='file' />");
    root.buildUpload("up_ex_file", null, "ex_msg", "items");
    $("#import_Excel_Win").data("kendoWindow").open().center();
};

root.buileAnimate = function buileAnimate(target, widthP){
    $(target).animate({
        right: "0px",
        opacity: '1',
        width: widthP,
    });
};

root.closeClearReportWin = function(){
    $("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
};

root.openClearReportWin = function(){
    $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
};

/**
 * 当页面滚动到距离顶部200像素时，在右下角显示返回顶部按钮
 */
window.onscroll = function(){
    var top = document.documentElement.scrollTop == 0 ? document.body.scrollTop : document.documentElement.scrollTop;
    if (top > 200) {
        $(".gTop a").css("display", "block");
    }
    else {
        $(".gTop a").css("display", "none");
    }
};

/**
 * 根据用户输入的生产企业名称自动加载企业信息
 */
root.onSelectBusUnitName = function(e) {
    root.initBusName = this.dataItem(e.item.index());
    var bus = root.getBusOfpro2BusByName(root.initBusName);
    root.setEasyBusunitValue(bus);
};

/**
 * 根据用户输入的生产企业营业执照号自动加载企业信息
 */
root.onSelectBusUnitLicenseNo = function(e) {
    var busLicenseNo = this.dataItem(e.item.index());
    $.ajax({
        url: portal.HTTP_PREFIX + "/business/getBusinessUnitByLicenseNo/" + busLicenseNo,
        type: "GET",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(data) {
            fsn.endTime = new Date();
            if (data.result.status == "true") {
                lims.setEasyBusunitValue(data.busUnit);
            }
        },
        error: function(e) {
            if (e.status == 911) {
                lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
            } else {
                lims.initNotificationMes('系统异常！', false);
            }
        }
    });
};

/**
 * 根据用户输入的生产企业地址自动加载企业信息
 */
root.onSelectBusUnitAddress = function(e) {
    var busAddr = this.dataItem(e.item.index());
    $.ajax({
        url: portal.HTTP_PREFIX + "/business/getBusinessUnitByAddress/" + busAddr,
        type: "GET",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(data) {
            fsn.endTime = new Date();
            if (data.result.status == "true") {
                lims.setEasyBusunitValue(data.busUnit);
            }
        },
        error: function(e) {
            if (e.status == 911) {
                lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
            } else {
                lims.initNotificationMes('系统异常！', false);
            }
        }
    });
};

root.getBusUnitByName = function(name) {
    var busUnit = null;
    var barcode = $("#barcodeId").val().trim();
    $.ajax({
        url: portal.HTTP_PREFIX + "/business/getBusUnitAndQsByNameAndBarcode/" + name + "?barcode=" + barcode,
        type: "GET",
        async: false,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(data) {
            fsn.endTime = new Date();
            if (data.result.status == "true") {
                busUnit = data.busUnit;
            }
        },
        error: function(e) {
            if (e.status == 911) {
                lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
            } else {
                lims.initNotificationMes('系统异常！', false);
            }
        }
    });
    return busUnit;
};

/* 加载省的名称和简称的下来dataSource */
root.loadProJianCheng = function(){
    return new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType: "json",
                async: false,
                contentType: "application/json",
                url: portal.HTTP_PREFIX + "/erp/address/findPro",
            }
        },
        schema: {
            data: function(d) {
                return d.result;
            }
        }
    });
};

root.changeQsFormat = function(e) {
    var format = this.dataItem(e.item.index());
    root.formatId = format.id;
    var formatId = format.id;
    var oldValue = $("#listqsFormat").data("kendoDropDownList").value();
    if(oldValue!=formatId){
        $("#bus_qsNo").val("");
    }
    if(format.formetValue.indexOf("(?)")!=-1){
        var firstPart = format.formetValue.replace("(?)","");
        root.setStyle(0);
        $("#jianchengList").show();
        root.showJianCheng = true;
        $("#showQsFormat").html(firstPart);
    }else{
    	root.setStyle(1);
        root.showJianCheng = false;
        $("#jianchengList").hide();
        $("#showQsFormat").html(format.formetValue);
        getQsUrl = $("#showQsFormat").html();
    }
    root.formetType = format.formetType;
    root.formetValue = format.formetValue;
    root.formetLength = format.formetLength;
    var getQsUrl = $("#showQsFormat").html()+"?formatId="+formatId;
    if(root.showJianCheng){
        getQsUrl = "("+$("#proJianCheng").data("kendoDropDownList").text()+")" + getQsUrl;
    }
    var listQsDs = lims.getAutoLoadDsByUrl("/business/searchBusQsNoAll/" + getQsUrl);
    $("#bus_qsNo").data("kendoAutoComplete").setDataSource([]);
    $("#bus_qsNo").data("kendoAutoComplete").setDataSource(listQsDs);
};

root.listgetQsNoFormatDataSource = function(url) {
    return new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType: "json",
                async: false,
                contentType: "application/json",
                url: portal.HTTP_PREFIX + url,
            }
        },
        schema: {
            data: function(d) {
                return d.data;
            }
        }
    });
};

root.setStyle = function(type){
	if(type==0){
		/* (?)XK(格式：(?)XKxx-xxx-xxxxx) */
		$("#myqsNo > span:first-child").attr("style","width:221px;left:-59px");
    	$("#showQsFormat").attr("style", "position:relative;left:-55px");
    	$("#bus_qsNo").attr("style","position:relative;width:219px;");
	}else if(type==1){
		/* QS */
		$("#myqsNo > span:first-child").attr("style","width:270px;left:0px");
    	$("#showQsFormat").attr("style", "position:relative;left:0px");
    	$("#bus_qsNo").attr("style","position:relative;width:267px;left:0px");
	}
};

//格式化qs号码
root.formatQsNo = function(obj, value) {
    var result = [];
    $("#bus_qsNo").attr("maxlength", root.formetLength + 2);
    var type = root.formetType;
    switch (root.formetLength) {
    case 12:
        // 格式：QSxx-xxxxx-xxxxx  2-5-5
        for (var i = 0; i < value.length; i++) {
            if (type == "-") {
                var qs_No = $("#bus_qsNo").val();
                lastChar = qs_No.substring(qs_No.length - 1);
                if (i == 2 && $("#bus_qsNo").val().trim().length == 3 && lastChar != "-") {
                    result.push(type + value.charAt(i));
                } else if (i == 8 && $("#bus_qsNo").val().trim().length == 9 && lastChar != "-") {
                    result.push(type + value.charAt(i));
                } else {
                    result.push(value.charAt(i));
                }
            } else {
                value = value.replace(/\s*/g, "");
                if (i % 4 == 0 && i != 0) {
                    result.push(type + value.charAt(i));
                } else {
                    result.push(value.charAt(i));
                }
            }
        }
        obj.value = result.join("");
        break;
    case 10:
        //格式：XKxx-xxx-xxxxx)
        for (var i = 0; i < value.length; i++) {
            var qs_No = $("#bus_qsNo").val();
            lastChar = qs_No.substring(qs_No.length - 1);
            if (i == 2 && $("#bus_qsNo").val().trim().length == 3 && lastChar != "-") {
                result.push(type + value.charAt(i));
            } else if (i == 6 && $("#bus_qsNo").val().trim().length == 7 && lastChar != "-") {
                result.push(type + value.charAt(i));
            } else {
                result.push(value.charAt(i));
            }
        }
        obj.value = result.join("");
        break;
    default:
        ;
    }
};

root.onSelectHideBusName = function(e) {
	root.initBusName = this.value();
    var bus = root.getBusOfpro2BusByName(root.initBusName);
    root.setEasyBusunitValue(bus);
};

root.getBusOfpro2BusByName = function(name) {
    if (name == null || name.length < 1 || root.pro2Bus == null || root.pro2Bus.length < 1) {
        return null;
    }
    for (var i = 0; i < root.pro2Bus.length; i++) {
        var bus = root.pro2Bus[i].businessUnit;
        if (bus != null && name == bus.name) {
            bus.qsNo = (root.pro2Bus[i].productionLicense != null ? root.pro2Bus[i].productionLicense.qsNo: "");
            bus.bindQsFlag = (root.pro2Bus[i].barcord != null && root.pro2Bus[i].barcord.length > 0);
            return bus;
        }
    }
};

/**
 * 验证是否绑定有QS号,以及qs号的及时性校验
 * @author TangXin
 */
root.validate_hasBideQs = function(barcode, busType){
	 var success = true;
	 var bussunitName = $("#bus_name").val().trim();
	 var now_qs =$("#bus_qsNo").val().trim(); // 现有界面的qs号
	 if(busType == "生产企业"){
		return; 
	 }
	 
	 /* 4代表生产许可证中包含省份简称的格式 */
	 if(root.formatId == 4){
		 var jiancheng = $("#proJianCheng").data("kendoDropDownList").text();
		 now_qs = "(" + jiancheng + ")" + $("#showQsFormat").html() + now_qs;
	 }
    
	 $.ajax({
			url:portal.HTTP_PREFIX + "/product/getPro2BusByBarcodeAndBusName/" + barcode + "/" + bussunitName,
			type: "GET",
			dataType: "json",
			async:false,
			success: function(dataValue){
				if(dataValue.pro2Bus==null){
					if(busType == "生产企业"){ 
						success = false;
						lims.initNotificationMes("该产品已解绑QS号，请重新绑定后再操作！",false);
					}
				}else{
					var pro2Bus = dataValue.pro2Bus;
					var qsNo = dataValue.pro2Bus.productionLicense.qsNo;
                    qsNo = qsNo==null?"":qsNo;
					if(pro2Bus.barcord!=null && pro2Bus.barcord!=""  && qsNo != now_qs){
						success = false;
                        var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
                        var qsFormatId = dataValue.pro2Bus.productionLicense.qsnoFormat!=null?dataValue.pro2Bus.productionLicense.qsnoFormat:1;
                        qsFormatIt.value(qsFormatId);
                        var qsItem = qsFormatIt.dataItem();
                        $("#showQsFormat").html(qsItem.formetValue);
                        $("#bus_qsNo").val(qsNo.replace(qsItem.formetValue,""));
						lims.initNotificationMes("该产品的QS号已被生产企业更改。系统已为您自动更新为" + qsNo +
							"。确认无误后，请重新点击提交！",false);
					}
				}
			}
	 });
	 return success;
 };
 
root.setRepAttachments = function(repAttachments) {
     var dataSource = new kendo.data.DataSource();
     root.aryRepAttachments.length = 0;
     if (repAttachments.length > 0) {
         lims.addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
         $("#btn_clearRepFiles").show();
         for (var i = 0; i < repAttachments.length; i++) {
             root.aryRepAttachments.push(repAttachments[i]);
             dataSource.add({
                 attachments: repAttachments[i]
             });
         }
     }

     $("#repAttachmentsListView").kendoListView({
         dataSource: dataSource,
         template: kendo.template($("#uploadedFilesTemplate").html()),
     });
 };
 
 
/**
 * 暂存功能
 * @author ZhangHui 2015/4/7
 */
root.save = function(){
     $("#saving_msg").html("正在保存，请稍候...");
     $("#toSaveWindow").data("kendoWindow").open().center();
     root.validate_save();
     var report_save = root.createInstance();
     $.ajax({
         url: portal.HTTP_PREFIX + "/tempReport/saveTempReport",
         type: "POST",
         dataType: "json",
         async: false,
         timeout: 600000, //10min
         contentType: "application/json; charset=utf-8",
         data: JSON.stringify(report_save),
         success: function(returnValue){
             fsn.endTime = new Date();
             $("#saving_msg").html("");
             $("#toSaveWindow").data("kendoWindow").close();
             if (returnValue.result.status == "true") {
                 lims.initNotificationMes('保存成功！', true);
                 $("#report_grid").data("kendoGrid").dataSource.data(returnValue.data.listOfItems);
             } else {
                 lims.initNotificationMes('保存失败！', false);
             }
             if (fsn.isTimerContinue) {
                 $("#diaoxian_remind_01").data("kendoWindow").close();
                 $("#diaoxian_remind_02").data("kendoWindow").close();
                 timer();
             }
         },
         error: function(e){
             $("#saving_msg").html("");
             $("#toSaveWindow").data("kendoWindow").close();
             if (e.status == 911) {
                 lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
             }
             else {
                 lims.initNotificationMes('系统异常！', false);
             }
         }
     });
 };