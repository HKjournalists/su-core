$(document).ready(function(){
    var lims = window.lims = window.lims ||
    {};
    var root = window.lims.root = window.lims.root ||
    {};
    var isNew = true;
    root.edit_id = null;
    root.aryProAttachments = new Array();
    root.aryRepAttachments = new Array();
    root.isAutoReport = false;
    root.upload_path = null;
    root.brand = {};
    /*下面是用来判断是不是改变过这3个字段--产品条形码，报告编号和批次号*/
    root.barcode = null;
    root.batchNo = null;
    root.checkReport = null;
    root.updateFlag = false;
    /* 报告：1个pdf；多张图片。（不允许出现pdf和图片同时上传） */
    root.keyword = "";
    root.colName = null;
    var fsn = window.fsn = window.fsn ||
    {}; // 全局命名空间
    var portal = fsn.portal = fsn.portal ||
    {}; // portal命名空间
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    fsn.imgMax = 3;//页面引导图片id
    fsn.src = "http://qa.fsnrec.com/portal/guid/unit/subBusiness_addReport_";//引导原地址
    try {
        root.edit_id = $.cookie("user_0_edit_testreport").id;
        $.cookie("user_0_edit_testreport", JSON.stringify({}), {
            path: '/'
        });
    } 
    catch (e) {
    }
    /* 初始化页面 */
    root.initialize = function(){
    	initComponent();
        root.initBarcode = "";
        $("#upload_rep").html("<input id='upload_report_files' type='file' class='btna' />");
        root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        root.initAutoComplete();
        root.initWidow();
        root.initialReportData();
        root.getCurrentBusiness();
        $("#btn_clearRepFiles").bind("click", root.clearRepFiles);
        $("#btn_clearRepFiles").hide();
        $("#add").bind("click", root.refresh);
        $("#save").bind("click", fsn.save);
        $("#submit").bind("click", root.submit);
        $("#update").bind("click", root.submit);
        $("#clear").bind("click", root.openClearReportWin);
        $("#logout_btn").bind("click", root.refresh);
        $("#save_btn_01").bind("click", fsn.save);
        $("#save_btn_02").bind("click", fsn.save);
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
        /* 倒计时 */
        timer();
    };
    
    root.initWidow = function(){
        /*初始化本页面需要使用的window*/
        lims.initConfirmWindow(root.clearReport, root.closeClearReport, "你确定要清空当前页面的报告信息吗？", "警告");
        lims.initKendoWindow("toSaveWindow", "保存状态", "500px", "", false, true, false);
        lims.initKendoWindow("tsWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("saveWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("delItemsWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("import_Excel_Win", "导入检测项目", "500px", "", false, true, false);
        lims.initKendoWindow("speCharWindow", "特殊符号", "450px", "200px", false, true, false);
        lims.initKendoWindow("pictureToPdfWindow", "图片转换Pdf", "800px", "450px", false, true, false);
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
    
    // 温馨提示样式控制
    root.changeGuidStyle = function(isShow){
        if (isShow) {
            $("#skin-box-bd").css("display", "block");
        }
        else {
            $("#skin-box-bd").css("display", "none");
        }
    };
    
    root.initAutoComplete = function(){
        $("#barcodeId").kendoAutoComplete({
            dataSource: root.ListOfProductBarCode,
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectBarcode,
        });
    };
    
    root.initialReportData = function(){
        if (root.edit_id) {
            isNew = false;
            $.ajax({
                url: portal.HTTP_PREFIX + "/testReport/" + root.edit_id,
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnValue){
                    fsn.endTime = new Date();
                    if (returnValue.result.status == "true") {
                        root.initBarcode = returnValue.data.sample.product.barcode;
                        root.setProductVal(returnValue.data.sample);
                        root.setReportVal(returnValue.data, false);
                        root.ReportNo_old = returnValue.data.serviceOrder;
                        root.upload_path = returnValue.data.uploadPath;
                        var proBrand = returnValue.data.sample.product.businessBrand;
                        var qsNoFormatVo = returnValue.data.sample.producer.qsNoAndFormatVo;
                        $("#bus_qsNo").val(qsNoFormatVo.qsNo);
                        root.brand = {
                            id: proBrand.id,
                            name: proBrand.name,
                        };
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
        }
        if (isNew) {
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
                        var qsNo = returnValue.data.sample.producer.qsNoAndFormatVo.qsNo;
                        $("#bus_qsNo").val(qsNo);
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
        if (isNew) {
            $("#update").hide();
            $("#add").hide();
        }
        else {
            $("#save").hide();
            $("#clear").hide();
            $("#submit").hide();
        }
    };
    
    root.setRepAttachments = function(repAttachments){
        var dataSource = new kendo.data.DataSource();
        root.aryRepAttachments.length = 0;
        if (repAttachments.length > 0) {
            $("#btn_clearRepFiles").show();
            for (var i = 0; i < repAttachments.length; i++) {
                root.aryRepAttachments.push(repAttachments[i]);
                dataSource.add({
                    attachments: repAttachments[i]
                });
            }
            lims.addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
        }
        $("#repAttachmentsListView").kendoListView({
            dataSource: dataSource,
            template: kendo.template($("#uploadedFilesTemplate").html()),
        });
    };
    
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
    
    
    
    var conclusion_ = [{
        "value": "合格",
        "text": "合格"
    }, {
        "value": "不合格",
        "text": "不合格"
    }, {
        "value": "--",
        "text": "--"
    }];
    
    /* 初始化页面控件 */
    function initComponent(){
    	/* 报告检验类别 */
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
    }
    
    root.buildUpload = function(id, attachments, msg, flag){
        $("#" + id).kendoUpload({
            async: {
                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb: "POST",
                removeField: "fileNames",
                saveField: "attachments",
            },
            localization: {
                select: lims.l(id),
                retry: lims.l("retry", 'upload'),
                remove: lims.l("remove", 'upload'),
                cancel: lims.l("cancel", 'upload'),
                dropFilesHere: lims.l("drop files here to upload", 'upload'),
                statusFailed: lims.l("failed", 'upload'),
                statusUploaded: lims.l("uploaded", 'upload'),
                statusUploading: lims.l("uploading", 'upload'),
                uploadSelectedFiles: lims.l("Upload files", 'upload'),
            },
            multiple: id == "upload_report_files" ? false : true,
            upload: function(e){
                var files = e.files;
                $.each(files, function(){
                	if(this.name.length > 100){
           		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
	                    e.preventDefault();
	                    return;
           	  		}
                    /*文件扩展名*/
                    var extension = this.extension.toLowerCase();
                    if (this.size > 10400000) {
                        lims.initNotificationMes('单个文件的大小不能超过10M！', false);
                        e.preventDefault();
                        return;
                    }
                    if (flag == "product") {
                        if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
                            lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
                            e.preventDefault();
                            return;
                        }
                    }
                    else 
                        if (flag == "report") {
                            if (id == "upload_report_files") {
                                if (extension != ".pdf") {
                                    lims.initNotificationMes('此处只能上传pdf文件。', false);
                                    e.preventDefault();
                                    return;
                                }
                                if (root.aryRepAttachments.length > 0) {
                                    lims.initNotificationMes('你已经上传了一份pdf了，如果要上传新的pdf，请先删除原有的pdf文件。', false);
                                    e.preventDefault();
                                    return;
                                }
                                lims.addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
                            }
                            else 
                                if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
                                    lims.initNotificationMes('文件格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
                                    e.preventDefault();
                                    return;
                                }
                        }
                        else 
                            if (extension != ".xls") {
                                lims.initNotificationMes('请确保你上传的是Excel文件，并且是2003版本。', false);
                                e.preventDefault();
                                return;
                            }
                });
            },
            success: function(e){
                if (e.operation == "upload") {
                    fsn.endTime = new Date();
                    if (flag == "items") {
                        lims.setEasyItems(e.response.results);
                        if (e.response.results == null || e.response.results.length < 1) {
                            lims.initNotificationMes('excel文件中没有可以导入的检测项目！', false);
                        }
                        $("#import_Excel_Win").data("kendoWindow").close();
                    }
                    else {
                        attachments.push(e.response.results[0]);
                        if (flag == "product") {
                            $("#" + msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
                        }
                        else {
                            if (id == "uploadRepPics") {
                                /*图片合成pdf功能，再次初始化上传控件*/
                                lims.initPicToPdfUpload(e.response.results[0], root.aryRepAttachments);
                            }
                            $("#" + msg).html(lims.l("Document recognition is successful, you can save!"));
                        }
                    }
                }
                else 
                    if (e.operation == "remove") {
                        root.isAllImage = false;
                        root.isPdf = false;
                        if (attachments == null) {
                            return;
                        }
                        for (var i = 0; i < attachments.length; i++) {
                            if (attachments[i].name == e.files[0].name) {
                                while ((i + 1) < attachments.length) {
                                    attachments[i] = attachments[i + 1];
                                    i++;
                                }
                                attachments.pop();
                                break;
                            }
                        }
                        if (root.aryRepAttachments.length == 0) {
                            root.isPdf = false;
                        }
                    }
            },
            remove: function(e){
                if (msg == "repFileMsg") {
                    lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
                    $("#" + msg).html("您可以：上传pdf文件，或上传图片自动合成pdf！");
                }
                else {
                    $("#" + msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )");
                }
            },
            error: function(e){
                $("#" + msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
            },
        });
    };
    
    /*图片转pdf时的预览方法*/
    root.ReviewPdf = function(){
        lims.viewPdf($("#tri_reportNo").val().trim());
    };
    
    /*图片合成pdf时的确定按钮事件*/
    root.picToPdfWinOk = function(){
        lims.picToPdfFun($("#tri_reportNo").val().trim());
    };
    
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
    
    $("#speCharWindow td a").click(function(){
        var activeElement = $("#report_grid_active_cell");
        if (!activeElement.length) {
            lims.initNotificationMes('请先选择检测项目列表中的某一个单元格！', false);
            return;
        }
        var htmVal = activeElement.html();
        if (htmVal == "合格" || htmVal == "检测项目名称" || htmVal == "单位" || htmVal == "技术指标" ||
        htmVal == "检测结果" ||
        htmVal == "检测依据" ||
        htmVal == "单项评价" ||
        htmVal == "操作") {
            //lims.initNotificationMes('【单项评价】此列不可手动输入！',false);
            return;
        }
        if (htmVal.length > 18 && htmVal.substr(0, 18) == '<a class="k-button') {
            return;
        }
        $("#report_grid_active_cell span").remove();
        activeElement.html(activeElement.html().trim() + $(this).html());
        root.grdItem[root.grdItemFiled] = root.grdItem[root.grdItemFiled] + $(this).html();
    });
    
    /*KMS获取检测项目名称*/
    root.getItemsNameDS = function(){
        return new kendo.data.DataSource({
            transport: {
                read: {
                    async: false,
                    url: function(){
                        return fsn.getKMSPrefix() + "/lims-standard-cloud/service/testitem/search?page=1&pageSize=20&keyword=" + root.keyword;
                    },
                    type: "GET",
                }
            },
            schema: {
                data: function(returnValue){
                    return returnValue.data.resultList;
                }
            },
            change: function(e){
                var date = new Date();
                date.setSeconds(date.getSeconds() + 1);
                fsn.endTime = date;
            },
        });
    }
    
    /*kms*/
    root.kmsItemNamePage = 1;
    root.buildAutoCompleteKMS = function(input, container, options){
        /*调用kms接口，获取检测项目名称数据源*/
        input.attr("name", options.field);
        input.attr("class", "k-textbox");
        input.appendTo(container);
        input.kendoAutoComplete({
            dataSource: [],
            filter: "contains",
            dataTextField: "name",
            dataBound: function(){
                if (root.keyword == input.val().trim()) {
                    return;
                }
                root.keyword = input.val().trim();
                root.kmsItemNamePage = 1;
                var listItemDs = lims.getItemsNameDS(root.kmsItemNamePage, root.keyword);
                input.data("kendoAutoComplete").setDataSource(listItemDs);
                input.data("kendoAutoComplete").search(root.keyword);
            },
        });
    };
    
    /*检测项目名称*/
    root.autoTestItems = function(container, options){
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input id='itemName' />");
        root.buildAutoCompleteKMS(input, container, options);
        $("#itemName_listbox").scroll(function(){
            lims.appendKmsItemName($(this), root.kmsItemNamePage, root.keyword);
        });
    };
    
    root.autoTestUnits = function(container, options){
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input/>");
        root.keyword = "";
        root.colName = 2;
        root.buildAutoComplete(input, container, options);
    };
    
    root.autoSpecification = function(container, options){
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input/>");
        root.keyword = "";
        root.colName = 3;
        root.buildAutoComplete(input, container, options);
    };
    
    root.autoTestResult = function(container, options){
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input/>");
        root.keyword = "";
        root.colName = 4;
        root.buildAutoComplete(input, container, options);
    };
    
    root.autoStandard = function(container, options){
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input/>");
        root.keyword = "";
        root.colName = 5;
        root.buildAutoComplete(input, container, options);
    };
    
    if (!$("#report_grid").data("kendoGrid")) {
        $("#report_grid").kendoGrid({
            dataSource: root.gridDataSource,
            navigatable: true,
            editable: true,
            pageable: {
                messages: lims.gridPageMessage(),
            },
            toolbar: [{
                template: kendo.template($("#toolbar_template").html())
            }],
            columns: [{
                fild: "id",
                title: "id",
                editable: false,
                width: 1
            }, {
                field: "name",
                title: lims.l("Item Name"),
                editor: root.autoTestItems,
                width: 40
            }, {
                field: "unit",
                title: lims.l("Unit"),
                editor: root.autoTestUnits,
                width: 30
            }, {
                field: "techIndicator",
                title: lims.l("Specifications"),
                editor: root.autoSpecification,
                width: 50
            }, {
                field: "result",
                title: lims.l("Test Result"),
                editor: root.autoTestResult,
                width: 40
            }, {
                field: "assessment",
                title: lims.l("Conslusion"),
                values: conclusion_,
                editable: false,
                width: 40
            }, {
                field: "standard",
                title: lims.l("Standard_"),
                editor: root.autoStandard,
                width: 50
            }, {
                command: [{
                    name: "Remove",
                    text: "<span class='k-icon k-cancel'></span>" + lims.l("Delete"),
                    click: function(e){
                        e.preventDefault();
                        root.delItem = this.dataItem($(e.currentTarget).closest("tr"));
                        if (root.delItem.id == null) {
                            $("#report_grid").data("kendoGrid").dataSource.remove(root.delItem);
                            return;
                        }
                        $("#report_grid").data("kendoGrid").dataSource.remove(root.delItem);
                    }
                }],
                title: lims.l("Operation"),
                width: 30
            }]
        });
    }
    
    fsn.setDateValue = function(){
        $("#tri_proDate_format").data("kendoDatePicker").value($("#tri_proDate").val());
        $("#tri_testDate_format").data("kendoDatePicker").value($("#tri_testDate").val());
    };
    
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
        $("#upload_rep").html("<input id='upload_report_files' type='file' class='btna' />");
        root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
        $("#report_grid").data("kendoGrid").setDataSource(root.gridDataSource);
        root.clearTempReport();
        lims.initNotificationMes("清除成功！", true);
    };
    
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
    //way:save代表保存,submit代表提交
    root.createInstance = function(way){
        var license = {
            licenseNo: $("#bus_licenseNo").val().trim(),
        };
        var qsNoAndFormatVo = {
            qsNo: $("#bus_qsNo").val().trim()
        }
        var busUnit = {
            name: $("#bus_name").val().trim(),
            license: license,
            qsNoAndFormatVo: qsNoAndFormatVo,
            address: $("#bus_address").val().trim(),
        
        };
        var businessBrand = {
            name: $("#foodinfo_brand").text().trim(),
        };
        var unit = {
            name: $("#foodinfo_minunit").text().trim(),
        };
        /*处理三级分类*/
        if (root.category != null) {
            var secondCagetory = root.category.category;
            var seCode = (secondCagetory == null ? null : secondCagetory.code);
            var onCode = root.category.categoryOneCode;
            root.category.categoryOneCode = (seCode != null ? seCode.substr(0, 2) : onCode);
        }
        var product = {
            barcode: $("#barcodeId").val().trim(),
            name: $("#foodName").text().trim(),
            format: $("#specification").text().trim(),
            unit: unit,
            status: $("#foodInfo_Status").text().trim(),
            expiration: $("#proExpiratio").text().trim(),
            expirationDate: $("#foodinfo_expirday").text().trim(),
            regularity: root.regularity,
            regularityStr: lims.convertRegularityToString(root.regularity),
            businessBrand: root.brand,
            category: root.category,
            characteristic: "无",
            proAttachments: root.aryProAttachments,
            producerFlag: true,
            local: true,
        };
        var proDate = $("#tri_proDate").val().trim();
        var sample = {
            id: $("#sample_id").val().trim(),
            productionDate: (proDate == "0000-00-00" ? null : proDate),
            batchSerialNo: $("#tri_batchNo").val().trim(),
            producer: busUnit,
            product: product,
            /* 暂存生产日期格式为000-00-00类型 */
            proDateStr:proDate,
        };
        
        var testee = {
            name: $("#tri_testee").val().trim(),
        };
        
        root.TestReport = {
            id: root.edit_id,
            testOrgnization: $("#tri_testOrg").val().trim(),
            testType: $("#tri_testType").data("kendoDropDownList").value(),
            pass: $("#tri_conclusion").data("kendoDropDownList").value() == "1" ? true : false,
            testDate: $("#tri_testDate").val().trim(),
            serviceOrder: $("#tri_reportNo").val().trim(),
            sampleQuantity: $("#sampleCounts").val().trim(),
            standard: $("#judgeStandard").val().trim(),
            result: $("#testResultDescribe").val().trim(),
            samplingLocation: $("#sampleAds").val().trim(),
            testPlace: $("#tri_testPlace").val().trim(),
            comment: $("#remarks").val().trim(),
            testProperties: way == "save" ? $("#report_grid").data("kendoGrid").dataSource.data() : root.getItems(),
            newFlag: isNew,
            autoReportFlag: root.isAutoReport,
            sample: sample,
            testee: testee,
            uploadPath: root.upload_path,
            repAttachments: root.aryRepAttachments,
            updateFlag: root.updateFlag,
            endDate: null,//$("#reportEndDate").val(),
            publishFlag:'3',
        };
    };
    
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
    
    root.validate_save = function(){
    
        var expirday = $("#tri_proDate").val();
        var re1 = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
        if (!expirday.match(re1) && expirday.trim() != "") {
            $("#tri_proDate").val("");
        }
        
        var testday = $("#tri_testDate").val();
        if (!testday.match(re1) && testday.trim() != "") {
            $("#tri_testDate").val("");
        }
    };
    
    /* 暂存功能
     * */
    fsn.save = function(){
        $("#saving_msg").html("");
        $("#saving_msg").html("正在保存，请稍候...");
        $("#toSaveWindow").data("kendoWindow").open().center();
        root.validate_save();
        root.createInstance("save");
        $.ajax({
            url: portal.HTTP_PREFIX + "/tempReport/saveTempReport",
            type: "POST",
            dataType: "json",
            async: false,
            timeout: 600000, //10min
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(root.TestReport),
            success: function(returnValue){
                fsn.endTime = new Date();
                $("#saving_msg").html("");
                $("#toSaveWindow").data("kendoWindow").close();
                if (returnValue.result.status == "true") {
                    lims.initNotificationMes('保存成功！', true);
                    $("#report_grid").data("kendoGrid").dataSource.data(returnValue.data.listOfItems);
                }
                else {
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
        $("#save_status").html("");
    };
    
    root.submit = function(){
        root.clearErrorInfoStyle();
        if (!root.validateData()) { // 必填项验证
            return;
        }
        /*var barcode = $("#barcodeId").val();
         var re_barcode = /^[0-9]{13}$/;
         if(!barcode.match(re_barcode)){
         lims.msg("success_msg", null, ("产品条形码只能由13位数字组成！"));
         return;
         }*/
        //var qsNo=$("#qsFname").val()+($("#bus_qsNo").val().trim()).replace(/\s*/g, "").replace(/-/g,"").replace(/\s+/g,"");
        /*if(qsNo.indexOf("qs")==-1||!status){
         lims.initNotificationMes("【生产企业信息】中的QS证格式不正确！编号格式应该为【QS+12位数字】。</br>请到基本企业基本信息中修改！",false);
         return false;
         }*/
        var gridItem = $("#report_grid").data("kendoGrid").dataSource.data();
        if (gridItem.length < 1 && root.isAutoReport) {
            lims.initNotificationMes("至少需要一条检测项目！", false);
            return;
        }
        else {
            var count = 0;
            var message = "";
            for (var i = 0; i < gridItem.length; i++) {
                if (gridItem[i].name != "" || gridItem[i].unit != "" || gridItem[i].techIndicator != "" || gridItem[i].result != "" || gridItem[i].standard != "") {
                    if (gridItem[i].name == "") {
                        message = "【检查项目名称】";
                    }
                    /*if(gridItem[i].unit==""){
                     message += "【单位】";
                     }
                     if(gridItem[i].techIndicator==""){
                     message += "【技术指标】";
                     }*/
                    if (gridItem[i].result == "") {
                        message += "【检测结果】";
                    }
                    /*if(gridItem[i].standard==""){
                     message += "【检测依据】";
                     }*/
                    if (message != "") {
                        lims.initNotificationMes('检测项目列表的第' + (i + 1) + '行' + message + '为必填项！', false);
                        return;
                    }
                    count++;
                }
            }
            if (root.isAutoReport && count == 0) {
                lims.initNotificationMes('至少需要一条检测项目！', false);
                return;
            }
        }
//        var barcode = $("#barcodeId").val();
//        var re_barcode = /^[0-9]*$/;
//        if (!barcode.match(re_barcode)) {
//            $("#barcodeId").attr("style", "border:2px solid red;");
//            lims.initNotificationMes("产品条形码只能由数字组成！", false);
//            return;
//        }
        fsn.setDateValue();
        if ($("#tri_proDate_format").val().trim() == "" && $("#tri_proDate").val().trim() != "0000-00-00") {
            lims.initNotificationMes("生产日期格式不正确，请重新填写！", false);
            return;
        }
        if ($("#tri_testDate").val().trim() != "") {
            if ($("#tri_testDate_format").val().trim() == "") {
                lims.initNotificationMes("检验日期格式不正确，请重新填写！", false);
                return;
            }
        }
        if (isNew) {
            if (!root.validateReportNO()) {
                $("#tri_reportNo").attr("style", "border:2px solid red;");
                lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" +
                $("#barcodeId").val().trim() +
                "】、批次号【" +
                $("#tri_batchNo").val().trim() +
                "】已经存在对应的报告，3个字段不能重复！", false);
                return;
            }
        }
        else {
            if (root.ReportNo_old != $("#tri_reportNo").val().trim()) {
                if (!root.validateReportNO()) {
                    $("#tri_reportNo").attr("style", "border:2px solid red;");
                    lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" +
                    $("#barcodeId").val().trim() +
                    "】、批次号【" +
                    $("#tri_batchNo").val().trim() +
                    "】已经存在对应的报告，3个字段不能重复！", false);
                    return;
                }
            }
        }
        if (!root.isAutoReport && root.aryRepAttachments.length == 0) {
            lims.initNotificationMes(lims.l("not found pdf"), false);
            return;
        }
        else 
            if (root.isAutoReport && root.aryRepAttachments.length > 0) {
                lims.initNotificationMes(lims.l("auto create pdf"), false);
                return;
            }
        /*else if(root.aryRepAttachments.length>1){
         lims.initNotificationMes(lims.l("pdf more then"), false);
         return;
         }*/
        if (root.isAutoReport && $("#tri_testType").data("kendoDropDownList").value() != "企业自检") {
            lims.initNotificationMes("只有在【检验类别】为企业自检的情况下，才能选择自动生成pdf，否则请手动上传pdf!", false);
            return;
        }
        $("#success_msg").html("");
        $("#success_msg").attr("style", "");
        
        if (!lims.validate_hasBideQs(barcode, "生产企业")) {
            return;
        }
        root.submitReport();
    };
    
    root.submitReport = function(){
        $("#saving_msg").html("");
        $("#saving_msg").html("正在提交，请稍候...");
        $("#toSaveWindow").data("kendoWindow").open().center();
        
        root.createInstance("submit");
        var save_report = root.TestReport;
        if (!isNew) {
            save_report.id = root.edit_id;
        }
        var updateApply = root.updateApply ==true?true:false;
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/"+updateApply,
            type: isNew ? "POST" : "PUT",
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
                        }
                        else {
                            lims.initNotificationMes(returnValue.result.errorMessage, false);
                        }
                    }
                    else {
                        lims.initNotificationMes('编号为' + returnValue.data.serviceOrder + '的检测报告 ' + (isNew ? lims.l("Add") : lims.l("Update")) + '成功！', true);
                        fsn.sendItemsToKMS();/*将检测项目发回至KMS*/
                    }
                    /* 更新成功 */
                    isNew = false;
                    root.ReportNo_old = returnValue.data.serviceOrder;
                    $("#save").hide();
                    $("#clear").hide();
                    $("#submit").hide();
                    $("#update").show();
                    $("#add").show();
                    root.clearTempReport();
                    root.edit_id = returnValue.data.id;
                    root.upload_path = returnValue.data.uploadPath;
                    $("ul.k-upload-files").remove();
                    if (!returnValue.data.autoReportFlag) {
                        root.setRepAttachments(returnValue.data.repAttachments);
                    }
                    $("#report_grid").data("kendoGrid").dataSource.data(returnValue.data.testProperties == null ? [] : returnValue.data.testProperties);
                }
                else {
                    lims.initNotificationMes(isNew ? lims.l("Add") : lims.l("Update") + '失败！', false);
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
    
    
    root.refresh = function(){
        window.location.pathname = "/fsn-core/views/market/subBusiness_addReport.html";
    };
    
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
        if (isNew && (repListView == null || pdfCont < 1)) {
            root.aryRepAttachments.length = 0;
            lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
            $("#upload_rep").html("<input id='upload_report_files' type='file' class='btna'/>");
            root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        }
        $("#tr_isAuto").show();
        root.isAutoReport = false;
    });
    
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
    
    $("#barcodeId").blur(function(){
        if ($("#barcodeId").val().trim() == "" || root.initBarcode == $("#barcodeId").val().trim()) {
            return;
        }
        root.initBarcode = $("#barcodeId").val().trim();
        root.verifyBarcodeIsBindQsNo(root.initBarcode);
        var isExistsFlag = false;
        if (root.listOfBarCodes != null && root.listOfBarCodes.length > 0) {
            for (var i = 0; i < root.listOfBarCodes.length; i++) {
                if ($("#barcodeId").val().trim() == root.listOfBarCodes[i]) {
                    isExistsFlag = true;
                    root.initBarcode = root.listOfBarCodes[i];
                    root.autoloadingPageInfoByBarcode($("#barcodeId").val().trim());
                }
            }
        }
        else {
            flag = true;
        }
        /*
         if(!isExistsFlag){
         lims.initNotificationMes(lims.l("当前条形码：")+ $("#barcodeId").val().trim() + lims.l(" 未绑定QS号，请重新输入。"), false);
         $("#bus_qsNo").val("");
         var tempVal = $("#barcodeId").val().trim();
         root.clearProduct();
         $("#barcodeId").val(tempVal);
         }
         */
    });
    
    root.onSelectBarcode = function(e){
        root.initBarcode = this.dataItem(e.item.index());
        root.autoloadingPageInfoByBarcode(root.initBarcode);
    };
    
    root.clearRepFiles = function(){ //清空报告资源
        root.aryRepAttachments.length = 0;
        fsn.pdfRes = null;
        lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
		if($("#repAttachmentsListView").data("kendoListView"))
        $("#repAttachmentsListView").data("kendoListView").dataSource.data([]);
        $("#btn_clearRepFiles").hide();
    };
    
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
    
    root.removeRes = function(resID, fileName){ //从页面删除
        root.isPro = false;
        var dataSource = new kendo.data.DataSource();
        for (var i = 0; i < root.aryProAttachments.length; i++) {
            if ((resID != null && root.aryProAttachments[i].id == resID) ||
            root.aryProAttachments[i].fileName == fileName) {
                root.isPro = true;
                while ((i + 1) < root.aryProAttachments.length) {
                    root.aryProAttachments[i] = root.aryProAttachments[i + 1];
                    i++;
                }
                root.aryProAttachments.pop();
                break;
            }
        }
        if (!root.isPro) {
            for (var i = 0; i < root.aryRepAttachments.length; i++) {
                if ((resID != null && root.aryRepAttachments[i].id == resID) ||
                root.aryRepAttachments[i].fileName == fileName) {
                    isPro = false;
                    while ((i + 1) < root.aryRepAttachments.length) {
                        root.aryRepAttachments[i] = root.aryRepAttachments[i + 1];
                        i++;
                    }
                    root.aryRepAttachments.pop();
                    break;
                }
            }
            if (root.aryRepAttachments.length > 0) {
                for (i = 0; i < root.aryRepAttachments.length; i++) {
                    dataSource.add({
                        attachments: root.aryRepAttachments[i]
                    });
                }
            }
            $("#repAttachmentsListView").data("kendoListView").setDataSource(dataSource);
            if (root.aryRepAttachments.length == 0) {
                $("#btn_clearRepFiles").hide();
                fsn.pdfRes = null;
                lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
            }
        }
        else {
            if (root.aryProAttachments.length > 0) {
                for (i = 0; i < root.aryProAttachments.length; i++) {
                    dataSource.add({
                        attachments: root.aryProAttachments[i]
                    });
                }
            }
            $("#proAttachmentsListView").data("kendoListView").setDataSource(dataSource);
            if (root.aryProAttachments.length == 0) {
                $("#btn_clearProFiles").hide();
            }
        }
    };
    
    root.validateData = function(){
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
        var validate_testOrg = $("#tri_testee").kendoValidator().data("kendoValidator").validate();
        $("#judgeStandard").kendoValidator().data("kendoValidator").validate();
        $("#testResultDescribe").kendoValidator().data("kendoValidator").validate();
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
        if (!validate_testOrg) {
            lims.initNotificationMes("被检测单位/人不能为空", false);
            return false;
        }
        if ($("#judgeStandard").val().length < 1) {
            lims.initNotificationMes("执行标准不能为空", false);
            return false;
        }
        if ($("#testResultDescribe").val().length < 1) {
            lims.initNotificationMes("检验结论描述不能为空", false);
            return false;
        }
        
        //验证报告编号不能包含“？”，“?”，“'”字符,如果包含空格通过验证
        var repNo = $("#tri_reportNo").val().trim();
        if (repNo.indexOf("？") > -1 || repNo.indexOf("?") > -1 || repNo.indexOf("'") > -1) {
            $("#tri_reportNo").attr("style", "border:2px solid red;");
            lims.initNotificationMes(lims.l("Report Number") + lims.l("can not contain “'”、“?” illegal characters!"), false);
            return false;
        }
        var testDate = $("#tri_testDate").val().trim();
        if (testDate.length < 1) {
            lims.initNotificationMes("报告检测日期不能空！", false);
            return false;
        }
        //检验日期不能小于生产日期
        var startDate = $("#tri_proDate").val().trim().split("-");
        var newStartDate = new Date(startDate[0], startDate[1], startDate[2]);
        var endDate = $("#tri_testDate").val().split("-");
        var newEndDate = new Date(endDate[0], endDate[1], endDate[2]);
        var dtime = new Date();
        var nowTime = new Date(dtime.getFullYear(), (dtime.getMonth() + 1), dtime.getDate()); //当前日期（不包括时分秒）
        if (newEndDate > nowTime) {
            lims.initNotificationMes('检验日期和生产日期不能大于当前日期！', false);
            return false;
        }
        else 
            if (newEndDate < newStartDate) {
                lims.initNotificationMes('检验日期不能小于生产日期！', false);
                return false;
            }
        return true;
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
    //$("#reportEndDate").kendoValidator().data("kendoValidator");
    //清除输入框的错误提示样式
    root.clearErrorInfoStyle = function(){
        $("#tri_reportNo").removeAttr("style");
        $("#tri_proDate").attr("style", "width:100%;height:100%;");
        $("#tri_testDate").attr("style", "width:100%;height:100%");
    };
    
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
                        name: proBrand.name
                    };
                    /* qs赋值 */
                    var pro2Bus = returnValue.data.pro2Bus;
                    if (pro2Bus) {
                        for (var i = 0; i < pro2Bus.length; i++) {
                            if (pro2Bus[i].businessUnit.name == root.bussunitName) {
                                //将QS号中的QS转为剃掉。
                                var qs_No = pro2Bus[i].productionLicense.qsNo;
                                $("#bus_qsNo").val(qs_No);
                                break;
                            }
                        }
                    }
                    if (isNew) {
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
        if(report.sample != null ) {
        	if (report.sample.id != null) {
                $("#tri_proDate").val(report.sample.productionDate == null ? "0000-00-00" : report.sample.productionDate.substr(0, 10));
            } else {
            	$("#tri_proDate").val(report.sample.proDateStr == null ? null : report.sample.proDateStr.substr(0, 10));
            }
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
        $("#report_grid").data("kendoGrid").dataSource.data(report.testProperties == null ? [] : report.testProperties);
        if (report.autoReportFlag) {
            document.getElementById("yesAuto").checked = true;
            $("#tr_isAuto").hide();
        }
        else {
            document.getElementById("noAuto").checked = true;
            $("#tr_isAuto").show();
            if (!isSearch) {
                root.setRepAttachments(report.repAttachments);
            }
        }
        root.isAutoReport = report.autoReportFlag;
    };
    
    root.getCurrentBusiness = function(){
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/getCurrentBusiness",
            type: "GET",
            async: false,
            success: function(returnValue){
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                    root.setBusinessVal(returnValue.data);
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
    
    /*打开导入检测项目的窗口*/
    lims.openExel_Win = function(){
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
    
    //当页面滚动到距离顶部200像素时，在右下角显示返回顶部按钮。
    window.onscroll = function(){
        var top = document.documentElement.scrollTop == 0 ? document.body.scrollTop : document.documentElement.scrollTop;
        if (top > 200) {
            $(".gTop a").css("display", "block");
        }
        else {
            $(".gTop a").css("display", "none");
        }
    };
    
    try {
        //点击报告更新申请处理后传过来的参数 (产品条形码)
         var _barcode = $.cookie("product_barcode");
         $.cookie("product_barcode", JSON.stringify({}), {
            path: '/'
        });
    } catch(e) {}
    
     try {
         //是否是 报告更新申请处理页面跳转来的
         root.updateApply = $.cookie("updateApply");
         $.cookie("updateApply", JSON.stringify({}), {
            path: '/'
        });
    } catch(e) {}
    
    try {
        //报告类型 （ 报告更新申请处理）
         root.updateReportType = $.cookie("updateReportType");
         $.cookie("updateReportType", JSON.stringify({}), {
            path: '/'
        });
    } catch(e) {}
    
    /* 初始化页面信息，请确保该方法始终在当前页代码的最后一行 */
    root.initialize();

    if(root.updateApply==true){
        root.autoloadingPageInfoByBarcode(_barcode);
        $("#barcodeId").attr("disabled",true);
        $("#tri_testType").data("kendoDropDownList").value(root.updateReportType);
    }else{
        root.updateApply==false;
    }
});
