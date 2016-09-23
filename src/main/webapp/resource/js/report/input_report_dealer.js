$(document).ready(function(){
    var lims = window.lims = window.lims || {};
    var root = window.lims.root = window.lims.root || {};
    root.isNew = true;
    root.edit_id = null;
    root.aryProAttachments = new Array();
    root.aryRepAttachments = new Array();
    /*下面是用来判断是不是改变过这3个字段--产品条形码，报告编号和批次号*/
    root.updateFlag = false;
    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    
    try {
        root.edit_id = $.cookie("user_0_edit_testreport").id;
        $.cookie("user_0_edit_testreport", JSON.stringify({}), {
            path: '/'
        });
    } catch (e) {
    }
    
    /* 初始化页面 */
    root.initialize = function(){
        root.initBarcode = "";
        $("#upload_rep").html("<input id='upload_report_files' type='file' />");
        root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        root.initAutoComplete_dealer();
        root.initWidow();
        root.initComponent();
        root.initialReportData();
        root.bindClick();
        /* 倒计时 */
        timer();
    };
    
    root.initWidow = function(){
        /*初始化本页面需要使用的window*/
        lims.initConfirmWindow(root.clearReport, root.closeClearReport, "你确定要清空当前页面的报告信息吗？", "警告");
        lims.initKendoWindow("toSaveWindow", "保存状态", "500px", "", false, true, false);
        lims.initKendoWindow("tsWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("saveWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("pictureToPdfWindow", "图片转换Pdf", "800px", "450px", false, true, false);
        root.initDiaoXianWindow();
    };
    
    /**
     * 封装报告信息(save代表保存,submit代表提交)
     * @author ZhangHui 2015/4/3
     */
    root.createInstance = function(){
    	/* 营业执照号 */
        var license = {
            licenseNo: $("#bus_licenseNo").val().trim(),
        };
        /* qs号 */
        var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
        var qsFormat = qsFormatIt.dataItem();
        var jiancheng = "";
        if(root.showJianCheng){
            jiancheng = "("+$("#proJianCheng").data("kendoDropDownList").text()+")";
        }
        var qsNoAndFormatVo = {
            licenceFormat: {
                id: qsFormat.id
            },
            qsNo: jiancheng + $("#showQsFormat").html() + $("#bus_qsNo").val().trim()
        };
        /* 生产企业信息 */
        var busUnit = {
            name: $("#bus_name").val().trim(),
            license: license,
            qsNoAndFormatVo: qsNoAndFormatVo,
            address: $("#bus_address").val().trim(),
        };
        /* 产品信息 */
        var product = {
            barcode: $("#barcodeId").val().trim(),
        };
        /* 产品示例信息 */
        var proDate = $("#tri_proDate").val().trim();
        var sample = {
            id: $("#sample_id").val().trim(),
            productionDate: (proDate == "0000-00-00" ? null : proDate),
            batchSerialNo: $("#tri_batchNo").val().trim(),
            producer: busUnit,
            product: product,
        };
        /* 检测报报告信息 */
        var testReport = {
            id: root.edit_id,
            testType: $("#tri_testType").data("kendoDropDownList").value(),
            pass: $("#tri_conclusion").data("kendoDropDownList").value() == "1" ? true : false,
            serviceOrder: $("#tri_reportNo").val().trim(),
            newFlag: root.isNew,
            autoReportFlag: false,
            sample: sample,
            repAttachments: root.aryRepAttachments,
            dbflag: "dealer", // 数据来源：经销商数据录入
            publishFlag:'4',
        };
        /* 返回封装好的报告信息 */
        return testReport;
    };
    
    /**
     * 保存之前，数据验证
     * @author Zhanghui 2015/4/7
     */
    root.validate_save = function(){
        var expirday = $("#tri_proDate").val();
        var re1 = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
        if (!expirday.match(re1) && expirday.trim() != "") {
            $("#tri_proDate").val("");
        }
    };
    
    /**
     * 提交前，数据验证
     * @author Zhanghui 2015/4/7
     */
    root.validate_submit = function(){
    	/* 必填项验证 */
        root.clearErrorInfoStyle();
        if (!root.validateData()) {
            return false;
        }
        /* 条形码格式校验 */
//        var barcode = $("#barcodeId").val();
//        var re_barcode = /^[0-9]*$/;
//        if (!barcode.match(re_barcode)) {
//            $("#barcodeId").attr("style", "border:2px solid red;");
//            lims.initNotificationMes("产品条形码只能由数字组成！", false);
//            return false;
//        }
        /* 生产日期格式校验 */
        var validate_success = validateDateFormat("tri_proDate", "生产日期格式不正确，请重新填写！", true);
        if(!validate_success){
        	return false;
        }
        /* 报告编号唯一性校验 */
        if (root.isNew) {
            if (!root.validateReportNO()) {
                $("#tri_reportNo").attr("style", "border:2px solid red;");
                lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" +
                $("#barcodeId").val().trim() +
                "】、批次号【" +
                $("#tri_batchNo").val().trim() +
                "】已经存在对应的报告，3个字段不能重复！", false);
                return false;
            }
        } else if (root.ReportNo_old != $("#tri_reportNo").val().trim()) {
            if (!root.validateReportNO()) {
                $("#tri_reportNo").attr("style", "border:2px solid red;");
                lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" +
                $("#barcodeId").val().trim() +
                "】、批次号【" +
                $("#tri_batchNo").val().trim() +
                "】已经存在对应的报告，3个字段不能重复！", false);
                return false;
            }
        }
        /* 是否上传pdf校验 */
        if (root.aryRepAttachments.length == 0) {
            lims.initNotificationMes(lims.l("not found pdf"), false);
            return false;
        } 
        
        /* 验证qs是否已经被生产企业绑定 */
        var hasBideQs = root.validate_hasBideQs(barcode, "流通企业");
        if (!hasBideQs) {
            return false;
        }
        return true;
    };
    
    /**
     * 提交报告
     * @author Zhanghui 2015/4/7
     */
    root.submit = function(){
    	if(!root.validate_submit()){ return; }
    	
        $("#saving_msg").html("正在提交，请稍候...");
        $("#toSaveWindow").data("kendoWindow").open().center();
        
        var save_report = root.createInstance();
        if (!root.isNew) {
            save_report.id = root.edit_id;
        }
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/" +root.updateApply,
            type: root.isNew ? "POST" : "PUT",
            dataType: "json",
            timeout: 600000, //10min
            async: false,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(save_report),
            success: function(returnValue){
                fsn.endTime = new Date();
                $("#save_status").html("");
                $("#toSaveWindow").data("kendoWindow").close();
                if (returnValue.result.status == "true") {
                    /* 判断在更新报告之前，该报告有无发布。若已发布，应提示用户。 */
                    if (returnValue.result.show) {
                        if (!returnValue.result.continueflag) {
                            return;
                        } else {
                            lims.initNotificationMes(returnValue.result.errorMessage, false);
                        }
                    } else {
                        lims.initNotificationMes('编号为' + returnValue.data.serviceOrder + '的检测报告 ' + (root.isNew ? lims.l("Add") : lims.l("Update")) + '成功！', true);
                    }
                    /* 更新成功 */
                    root.isNew = false;
                    root.ReportNo_old = returnValue.data.serviceOrder;
                    $("#save").hide();
                    $("#clear").hide();
                    $("#submit").hide();
                    $("#update").show();
                    $("#add").show();
                    root.clearTempReport();
                    root.edit_id = returnValue.data.id;
                    $("ul.k-upload-files").remove();
                    if (!returnValue.data.autoReportFlag) {
                        root.setRepAttachments(returnValue.data.repAttachments);
                    }
                }
                else {
                    lims.initNotificationMes(root.isNew ? lims.l("Add") : lims.l("Update") + '失败！', false);
                }
                $("#save_status").html("");
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
     * 必填数据校验
     * @author Zhanghui 2015/4/3
     */
    root.validateData = function(){
    	/* 验证生产企业名称是否为空 */
        if ($("#bus_name").val().trim() == "") {
            lims.initNotificationMes(lims.l("The BusinessName is null"), false);
            return false;
        }
        //如果用户修改了条形码信息，不允许保存。
        if (root.initBarcode != $("#barcodeId").val().trim() && root.initBarcode != null) {
            lims.initNotificationMes(lims.l("You can not modify this product's barcode information, please change the barcode") + root.initBarcode, false);
            return false;
        }
        //如果用户没有选择产品信息，不允许保存。
        if ($("#barcodeId").val().trim() == "" || $("#foodName").text().trim() == "" || $("#foodinfo_category").text().trim() == "") {
            lims.initNotificationMes(lims.l("Current product information is empty, Please select the product after trying to save barcode search!"), false);
            return false;
        }
        var validate_batchNo = $("#tri_batchNo").kendoValidator().data("kendoValidator").validate();
        var validate_reportNo = $("#tri_reportNo").kendoValidator().data("kendoValidator").validate();
        var validate_proDate = $("#tri_proDate").kendoValidator().data("kendoValidator").validate();
        
        if (!validate_reportNo) {
            lims.initNotificationMes("报告编号不能为空", false);
            return false;
        }
        if (!validate_batchNo) {
            lims.initNotificationMes("批次不能为空", false);
            return false;
        }
        if (!validate_proDate) {
            lims.initNotificationMes("生产日期不能为空", false);
            return false;
        }
        
        //验证报告编号不能包含“？”，“?”，“'”字符,如果包含空格通过验证
        var repNo = $("#tri_reportNo").val().trim();
        if (repNo.indexOf("？") > -1 || repNo.indexOf("?") > -1 || repNo.indexOf("'") > -1) {
            $("#tri_reportNo").attr("style", "border:2px solid red;");
            lims.initNotificationMes(lims.l("Report Number") + lims.l("can not contain “'”、“?” illegal characters!"), false);
            return false;
        }
        return true;
    };
    
    root.initAutoComplete_dealer = function() {
        $("#barcodeId").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/product/getAllBarCode"),
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectBarcode,
        });
        
        $("#hid_listBusName").kendoComboBox({
            dataTextField: "name",
            dataValueField: "name",
            placeholder: "选择生产企业",
            dataSource: [],
            filter: "contains",
            minLength: 0,
            index: 0,
            change: root.onSelectHideBusName,
        });
        
        //初始化初始化qs格式选择的下啦列表      
        var listFormatQs = root.listgetQsNoFormatDataSource("/product/loadlistFormatqs"); // 加载企业qs号绑定标准
        $("#listqsFormat").kendoDropDownList({
            dataTextField: "formetName",
            dataValueField: "id",
            dataSource: listFormatQs != null ? listFormatQs : [],
            select: root.changeQsFormat,
        });
        
        $("#bus_qsNo").kendoAutoComplete({
            dataSource: [],
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectQsNo,
        });

        $("#bus_name").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitName"),
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectBusUnitName,
        });
        $("#bus_licenseNo").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/business/searchAllLicenseNo"),
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectBusUnitLicenseNo,
        });
        $("#bus_address").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitAddress"),
            filter: "contains",
            placeholder: "搜索...",
            select: root.onSelectBusUnitAddress,
        });
    };
    
    root.onSelectHideBusName = function(e) {
        root.initBusName = this.value();
        var bus = root.getBusOfpro2BusByName(root.initBusName);
        if (bus == null) {
            return;
        }
        /* 企业基本信息赋值 */
        root.setEasyBusunitValue(bus);
        /* qs号赋值 */
        root.setQs(bus.qsNoAndFormatVo.licenceFormat.id, bus.qsNoAndFormatVo.qsNo);	
    };
    
    root.initComponent = function(){
    	$("#tri_proDate,#tri_testDate").kendoDatePicker({
            format: "yyyy-MM-dd",
            height: 30,
            culture: "zh-CN",
            max: new Date(),
            animation: {
                close: {
                    effects: "fadeOut zoom:out",
                    duration: 300
                },
                open: {
                    effects: "fadeIn zoom:in",
                    duration: 300
                }
            }
        });
        
        $("#tri_proDate_format,#tri_testDate_format").kendoDatePicker({
            format: "yyyy-MM-dd",
            height: 30,
            culture: "zh-CN",
            animation: {
                close: {
                    effects: "fadeOut zoom:out",
                    duration: 300
                },
                open: {
                    effects: "fadeIn zoom:in",
                    duration: 300
                }
            }
        });
        
        $("#tri_conclusion").kendoDropDownList({
            dataTextField: "text",
            dataValueField: "value",
            dataSource: [{
                text: "合格",
                value: "1"
            }, {
                text: "不合格",
                value: "0"
            }, ],
            filter: "contains",
            suggest: true,
            index: 0
        });
        
        $("#tri_testType").kendoDropDownList({
            dataTextField: "text",
            dataValueField: "value",
            dataSource: [{
                text: "企业自检",
                value: "企业自检"
            }, {
                text: "企业送检",
                value: "企业送检"
            }, {
                text: "政府抽检",
                value: "政府抽检"
            }, ],
            filter: "contains",
            suggest: true,
            index: 0
        });
        
        $("#proJianCheng").kendoDropDownList({
            dataTextField: "shortProv",
            dataValueField: "province",
            headerTemplate: '<div class="dropdown-header">' +
                '<span class=" k-header">省名</span>' +
                '<span class=" k-header">简称</span>' +
            '</div>',
            template: '<div class="dropdown">' +
                '<span class="k-state-default">#:data.province #</span>' +
                '<span class="k-state-default">#:data.shortProv #</span>' +
            '</div>',
            dataSource: root.loadProJianCheng(),
            filter: "contains",
            minLength: 0,
            index:0,
            select: function(e) {
                var shortProv = this.dataItem(e.item.index()).shortProv;
                var qsFormat = $("#listqsFormat").data("kendoDropDownList");
                var item = qsFormat.dataItem();
                getQsUrl = "("+shortProv +")"+ $("#showQsFormat").html()+"?formatId="+item.id;
                var listQsDs = lims.getAutoLoadDsByUrl("/business/searchBusQsNoAll/" + getQsUrl);
                if($("#proJianCheng").data("kendoDropDownList").text()!=shortProv){$("#bus_qsNo").val("");}
                $("#bus_qsNo").data("kendoAutoComplete").setDataSource([]);
                $("#bus_qsNo").data("kendoAutoComplete").setDataSource(listQsDs);
            }
        });
    };
    
    $("#barcodeId").blur(function() {
        if ($("#barcodeId").val().trim() == "" || root.initBarcode == $("#barcodeId").val().trim()) {
            return;
        }
        root.initBarcode = $("#barcodeId").val().trim();
        var flag = false;
        if (root.listOfBarCodes != null && root.listOfBarCodes.length > 0) {
            for (var i = 0; i < root.listOfBarCodes.length; i++) {
                if ($("#barcodeId").val().trim() == root.listOfBarCodes[i]) {
                    flag = true;
                    root.autoloadingPageInfoByBarcode($("#barcodeId").val());
                }
            }
        } else {
            flag = true;
            root.autoloadingPageInfoByBarcode($("#barcodeId").val());
        }
        if (!flag) {
            /*每次当条形码发生改变之后，都要清空上次一加载的数据*/
            lims.listOfBusunitName = null;
            lims.pro2Bus = null;
            $(".hideBusName").hide();
            $("#hid_listBusName").data("kendoComboBox").setDataSource([]);
            /*使用新的barcode验证是否绑定qs号*/
            root.validateQsNo();
            lims.setProductInfoToEdit();
        }
    });
    
    /**
     * 定义生产企业名称失去焦点事件
     */
    $("#bus_name").blur(function() {
    	var busName = $("#bus_name").val().trim();
        if (busName == "" || root.initBusName == busName) {
            return;
        }
        root.initBusName = $("#bus_name").val().trim();
        /* 企业名称是可选生产企业 */
        var bus = root.getBusOfpro2BusByName(root.initBusName);
        if (!bus) {
        	/* 企业名称不是可选生产企业 */
        	bus = root.getBusUnitByName(root.initBusName);
        }
        root.setEasyBusunitValue(bus);
    });
    
    root.initialize();
    
    try {
        //点击报告更新申请处理后传过来的参数 (产品条形码)
         var _barcode = $.cookie("product_barcode");
         $.cookie("product_barcode", JSON.stringify({}), {
            path: '/'
        });
         root.autoloadingPageInfoByBarcode(_barcode);
         $("#barcodeId").attr("disabled",true);
    } catch(e) {}
    
     try {
         //是否是 报告更新申请处理页面跳转来的
         root.updateApply = $.cookie("updateApply");
         $.cookie("updateApply", JSON.stringify({}), {
            path: '/'
        });
         root.updateApply = root.updateApply?true:false;
    } catch(e) {}
    
    try {
         //报告类型 （ 报告更新申请处理）
         root.updateReportType = $.cookie("updateReportType");
         $.cookie("updateReportType", JSON.stringify({}), {
            path: '/'
        });
         $("#tri_testType").data("kendoDropDownList").value(root.updateReportType);
    } catch(e) {}
});
