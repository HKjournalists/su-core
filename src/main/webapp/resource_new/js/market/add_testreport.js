  $(document).ready(function() {
    var lims = window.lims = window.lims || {};
    var root = window.lims.root = window.lims.root || {};
    var isNew = true;
    root.edit_id = null;
    root.aryProAttachments = new Array();
    root.aryRepAttachments = new Array();
    root.isAutoReport = false;
    root.upload_path = null;
    /*下面是用来判断是不是改变过这3个字段--产品条形码，报告编号和批次号*/
    root.barcode = null;
    root.batchNo = null;
    root.checkReport = null;
    root.updateFlag = false;
    /* 报告：1个pdf；多张图片。（不允许出现pdf和图片同时上传） */
    root.isAllImage = false;
    root.isPdf = false;
    root.keyword = "";
    root.colName = null;
    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    fsn.imgMax = 1; //页面引导图片id
    fsn.src = "http://qa.fsnrec.com/portal/guid/unit/unit_add_testreport_"; //引导原地址
    try {
        root.edit_id = $.cookie("user_0_edit_testreport").id;
        root.backUploadPdfPageFlag = $.cookie("user_0_edit_testreport").backUploadPdfPage;
        $.cookie("user_0_edit_testreport", JSON.stringify({}), {
            path: '/'
        });
    } catch(e) {}

    root.initialize = function() {
        $("#upload_pro").html("<input id='upload_prodPhoto_btn' type='file' />");
        root.buildUpload("upload_prodPhoto_btn", root.aryProAttachments, "fileEroMsg", "product");
        $("#upload_rep").html("<input id='upload_report_files' type='file' />");
        root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        lims.removeDisabledToBtn(["openT2P_btn"]); // 解决：刚录入的报告提交之后，刷新界面，此时点击图片合成无响应，而上传PDF按钮却正常
        root.initAutoComplete();
        root.getCurrentBusiness();
        root.initWidow();
        root.initialCategories();
        root.initialReportData();
        $("#btn_clearRepFiles").bind("click", root.clearRepFiles);
        $("#btn_clearProFiles").bind("click", root.clearProFiles);
        $("#btn_clearProFiles").hide();
        $("#btn_clearRepFiles").hide();
        $("#add").bind("click", root.refresh);
        $("#save").bind("click", fsn.save);
        $("#submit").bind("click", root.submit);
        $("#update").bind("click", root.submit);
        $("#clear").bind("click", root.openClearReportWin);
        $("#logout_btn").bind("click", root.refresh);
        $("#save_btn_01").bind("click", fsn.save);
        $("#save_btn_02").bind("click", fsn.save);
		$("#regularityDiv").bind("click",root.searchRegularity);
		
        //新增执行标准新增、确定和取消按钮
        $("#btn_regularity_save").click(function() {
            $("#foodinfo_standard").val($("#regularityList").text());
            $("#addRegularity_window").data("kendoWindow").close();
        });
        //取消新增标准
        $("#btn_regularity_cancel").click(function() {
            $("#addRegularity_window").data("kendoWindow").close();
        });
        //添加执行标准
        $("#btn_regularity_add").click(function() {
            root.addRegularity();
        });
        /* 温馨提示 */
        $("#immediate-guide-trigger").bind("mouseenter",
        function() {
            root.changeGuidStyle(true);
        });
        $("#immediate-guide-trigger").bind("mouseleave",
        function() {
            root.changeGuidStyle(false);
        });
        /* 返回 */
        $(".gTop").bind("mouseenter",
        function() {
            root.buileAnimate(".gTop a", "100px");
        });
        $(".gTop").bind("mouseleave",
        function() {
            root.buileAnimate(".gTop a", "100%");
        });
        $(".gPrev").bind("mouseenter",
        function() {
            root.buileAnimate(".gPrev a", "100px");
        });
        $(".gPrev").bind("mouseleave",
        function() {
            root.buileAnimate(".gPrev a", "100%");
        });
        /* 倒计时 */
        timer();
        /* 初始化qs号相关控件 */
        root.initQsComponent();
    };

    /* 初始化qs号相关控件 */
    root.initQsComponent = function(){
    	var qsFormat = $("#listqsFormat").data("kendoDropDownList");
        var item = qsFormat.dataItem();
        root.formatId = item.id;
        root.formetType = item.formetType;  // qs号数字之间的分隔符
        root.formetLength = item.formetLength; // qs号数字的长度
        root.formetValue = item.formetValue; // qs号字母部分
        if(item.formetValue.indexOf("(?)")!=-1){
        	root.setStyle(0);
            root.showJianCheng = true;
        }else{root.setStyle(1);}

        $("#showQsFormat").html(root.formetValue.replace("(?)",""));
        var getJianCheng = $("#proJianCheng").data("kendoDropDownList");
        var loadQsNoDS = $("#bus_qsNo").data("kendoAutoComplete");
        var url = $("#showQsFormat").html()+"?formatId="+root.formatId;
        if(root.showJianCheng){
            url = "("+ getJianCheng.text() +")"+url;
        }

        var listQsDs = lims.getAutoLoadDsByUrl("/business/searchBusQsNoAll/" + url);
        $("#bus_qsNo").data("kendoAutoComplete").setDataSource([]);
        $("#bus_qsNo").data("kendoAutoComplete").setDataSource(listQsDs);
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

    /*初始化本页面需要使用的window*/
    root.initWidow = function() {
        lims.initConfirmWindow(root.clearReport, root.closeClearReport, "你确定要清空当前页面的报告信息吗？", "警告");
        lims.initKendoWindow("toSaveWindow", "保存状态", "500px", "", false, true, false);
        lims.initKendoWindow("tsWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("saveWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("delItemsWindow", "警告", "400px", "", false, true, false);
        lims.initKendoWindow("import_Excel_Win", "导入检测项目", "500px", "", false, true, false);
        lims.initKendoWindow("speCharWindow", "特殊符号", "450px", "200px", false, true, false);
        lims.initKendoWindow("pictureToPdfWindow", "图片转换Pdf", "840px", "450px", false, true, false);
        lims.initKendoWindow("addRegularity_window", "新增执行标准", "800px", "400px", false, true, false);

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

    root.initAutoComplete = function() {
        $("#barcodeId").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/product/getAllBarCode"),
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectBarcode
        });

        $("#bus_qsNo").kendoAutoComplete({
            dataSource: [],
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectQsNo
        });

        $("#bus_name").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitName"),
            filter: "contains",
            placeholder: "搜索...",
            select: root.onSelectBusUnitName
        });
        $("#bus_licenseNo").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/business/searchAllLicenseNo"),
            filter: "startswith",
            placeholder: "搜索...",
            select: root.onSelectBusUnitLicenseNo
        });
        $("#bus_address").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitAddress"),
            filter: "contains",
            placeholder: "搜索...",
            select: root.onSelectBusUnitAddress
        });
        $("#foodinfo_minunit").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/product/getAllUnitName"),
            filter: "contains",
            //placeholder: "搜索...",
            placeholder: "例如：60g、箱、500ml/瓶、2瓶/盒、克/袋",
            select: root.onSelectUnitName
        });
        $("#foodinfo_brand").kendoAutoComplete({
            dataSource: lims.getAutoLoadDsByUrl("/business/business-brand/getAllName"),
            filter: "contains",
            placeholder: "搜索...",
            select: root.onSelectBrandName
        });

        //执行标准
        $("#regularityInfo").kendoComboBox({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: [],
            filter: "contains",
            placeholder: "请选择...",
            minLength: 0,
            index: 0,
            change: function() {
                var index =$("#regularityInfo").data("kendoComboBox").select();
				if (index!=-1) {
					//如果超过12，则不允许添加
					var ul = document.getElementById("regularityList");
					var count = ul.getElementsByTagName("li").length;
					if(count>=12){
						lims.initNotificationMes("执行标准过多，不能再添加！", false);
						return;
					}
					//将选中的值添加到列表中
					var text = $("#regularityInfo").data("kendoComboBox").dataItem().name;
					if(!root.validaRegularity(text)){return;}
					var li= document.createElement("li");
						li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>"+text+";</a>";
					ul.appendChild(li);
					//将Item从dataSource中移除
					$("#regularityInfo").data("kendoComboBox").dataSource.remove($("#regularityInfo").data("kendoComboBox").dataItem());
				}
				this.value("");
            }
        });
        //三级分类
        $("#category3").kendoComboBox({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: [],
            filter: "contains",
            placeholder: "请选择...",
            minLength: 0,
            index: 0,
            change: function() {
                var index = $("#category2").data("kendoComboBox").select();
                if (index == -1) {
                    $("#category3").data("kendoComboBox").value("");
                    return;
                }
            }
        });
        //二级分类
        $("#category2").kendoComboBox({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: [],
            filter: "contains",
            placeholder: "请选择...",
            minLength: 0,
            index: 0,
            change: function() {
                var index = $("#category2").data("kendoComboBox").select();
                if (index == -1) {
                    $("#category2").data("kendoComboBox").value("");
                    $("#category3").data("kendoComboBox").setDataSource([]);
                    $("#category3").data("kendoComboBox").value("");
                    $("#foodinfo_standard").val("");
                    return;
                }
                $.ajax({
                    url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + this.value() + "?categoryFlag=" + true,
                    type: "GET",
                    dataType: "json",
                    async: false,
                    success: function(returnValue) {
						fsn.endTime = new Date();
                        if (returnValue.result.status == "true") {
                            $("#category3").data("kendoComboBox").setDataSource(returnValue.data);
                            $("#foodinfo_standard").val("");
                            if (returnValue.data.length == 1) {
                                $("#category3").data("kendoComboBox").select(0);
                            } else {
                                $("#category3").data("kendoComboBox").value("");
                            }
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
            },
        });
        //一级分类
        $("#category1").kendoComboBox({
            dataTextField: "name",
            dataValueField: "code",
            dataSource: [],
            filter: "contains",
            placeholder: "请选择...",
            minLength: 0,
            index: 0,
            change: function() {
                var index = $("#category1").data("kendoComboBox").select();
                if (index == -1) {
                    $("#category1").data("kendoComboBox").value("");
                    $("#category2").data("kendoComboBox").setDataSource([]);
                    $("#category2").data("kendoComboBox").value("");
                    $("#category3").data("kendoComboBox").setDataSource([]);
                    $("#category3").data("kendoComboBox").value("");
                    $("#foodinfo_standard").val("");
                    return;
                }
                $.ajax({
                    url: portal.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode=" + this.value(),
                    type: "GET",
                    dataType: "json",
                    async: false,
                    success: function(returnValue) {
						fsn.endTime = new Date();
                        if (returnValue.result.status == "true") {
                            $("#category2").data("kendoComboBox").setDataSource(returnValue.data);
                            $("#category3").data("kendoComboBox").setDataSource([]);
                            $("#category3").data("kendoComboBox").value("");
                            $("#category2").data("kendoComboBox").value("");
                            $("#foodinfo_standard").val("");
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
            },
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

    root.changeQsFormat = function(e) {
        var format = this.dataItem(e.item.index());
        root.formatId = format.id;
        var oldValue = $("#listqsFormat").data("kendoDropDownList").value();
        if(oldValue!=root.formatId){
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
        var getQsUrl = $("#showQsFormat").html()+"?formatId="+root.formatId;
        if(root.showJianCheng){
            getQsUrl = "("+$("#proJianCheng").data("kendoDropDownList").text()+")" + getQsUrl;
        }
        var listQsDs = lims.getAutoLoadDsByUrl("/business/searchBusQsNoAll/" + getQsUrl);
        $("#bus_qsNo").data("kendoAutoComplete").setDataSource([]);
        $("#bus_qsNo").data("kendoAutoComplete").setDataSource(listQsDs);
    };

    root.listFormatQs = root.listgetQsNoFormatDataSource("/product/loadlistFormatqs"); // 加载企业qs号绑定标准
    //初始化初始化qs格式选择的下啦列表
    $("#listqsFormat").kendoDropDownList({
        dataTextField: "formetName",
        dataValueField: "id",
        dataSource: root.listFormatQs != null ? root.listFormatQs: [],
        select: root.changeQsFormat,
    });



    root.getBusOfpro2BusByName = function(name) {
        if (name == null || name.length < 1 || lims.pro2Bus == null || lims.pro2Bus.length < 1) {
            return null;
        }
        for (var i = 0; i < lims.pro2Bus.length; i++) {
            var bus = lims.pro2Bus[i].businessUnit;
            if (bus != null && name == bus.name) {
                bus.qsNo = (lims.pro2Bus[i].productionLicense != null ? lims.pro2Bus[i].productionLicense.qsNo: "");
                bus.bindQsFlag = (lims.pro2Bus[i].barcord != null && lims.pro2Bus[i].barcord.length > 0);
                return bus;
            }
        }
    };

    root.onSelectHideBusName = function(e) {
        root.initBusName = this.value();
        var bus = root.getBusOfpro2BusByName(root.initBusName);
        if (bus == null) {
            return;
        }
        lims.setEasyBusunitValue(bus);
    };

    root.initialCategories = function() {
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/searchCategory/1?parentCode=",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(returnValue) {
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                    $("#category1").data("kendoComboBox").setDataSource(returnValue.data);
                    if (root.edit_id == null) {
                        $("#category1").data("kendoComboBox").value("");
                    }
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
    root.initialReportData = function() {
        if (root.edit_id) {
            isNew = false;
            $.ajax({
                url: portal.HTTP_PREFIX + "/testReport/" + root.edit_id,
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnValue) {
                    fsn.endTime = new Date();
                    if (returnValue.result.status == "true") {
                        lims.setEasyProductValue(returnValue.data.sample, false);
                        root.initBarcode = returnValue.data.sample.product.barcode;
                        root.initBusName = returnValue.data.sample.producer != null ? returnValue.data.sample.producer.name: "";
                        lims.setEasyBusunitValue(returnValue.data.sample.producer);
                        lims.setEasyReportValue(returnValue.data, false, false);
                        root.ReportNo_old = returnValue.data.serviceOrder;
                        root.upload_path = returnValue.data.uploadPath;
                        root.busunit_name = returnValue.data.organizationName;

                        var qsNoFormatVo = returnValue.data.sample.producer.qsNoAndFormatVo;
                        var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
                        qsFormatIt.value(qsNoFormatVo.licenceFormat.id);
                        var qsItem = qsFormatIt.dataItem();
                        root.formetType = qsItem.formetType;
                        root.formetValue = qsItem.formetValue;
                        root.formetLength = qsItem.formetLength;
                        if(qsItem.formetValue.indexOf("(?)")!=-1){
                            $("#jianchengList").show();
                            root.showJianCheng = true;
                            var first = "";//省的简称
                            var socend = qsNoFormatVo.qsNo.substring(0,2);//中间字母
                            var third = qsNoFormatVo.qsNo.substr(2);//数字部分
                            if(qsNoFormatVo.qsNo.indexOf("(")!=-1 && qsNoFormatVo.qsNo.indexOf(")")!=-1 ){
                                first = qsNoFormatVo.qsNo.charAt(1);//省的简称
                            	socend = qsItem.formetValue.replace("(?)","");//中间字母
                            	third = qsNoFormatVo.qsNo.substr(5);//数字部分
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
                            $("#bus_qsNo").val(qsNoFormatVo.qsNo.replace(qsItem.formetValue, ""));
                        }
                        //编辑状态时，产品的条形码、名称、规格、品牌、保质期是不允许修改的。
                        //fsn.setInputToReadOnly(["barcodeId","foodName","specification","foodinfo_brand"]);
                        //fsn.setInputToReadOnly(["proExpirYear","proExpirMonth","proExpirDay","foodinfo_expirday"]);
                        if (returnValue.data.publishFlag == '2') {
                            $("#back").css('display', '');
                            $("#backResult").val(returnValue.data.backResult);
                            if (returnValue.data.backResult != null) {
                                lims.msg("backMsg", null, ("退回原因：" + returnValue.data.backResult));
                            } else {
                                lims.msg("backMsg", null, ("退回原因：无"));
                            }

                            var backMsgDivH = document.getElementById("backMsg").offsetHeight;
                            if (backMsgDivH > 0) {
                                backMsgDivH = backMsgDivH + 20;
                                $("#content_container").css("padding-bottom", backMsgDivH + "px");
                            }
                        }
                        //分类赋值
                        //一级分类
                        $("#category1").data("kendoComboBox").value(returnValue.data.sample.product.category.category.code.substring(0, 2));
                        $.ajax({
                            url: portal.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode=" + returnValue.data.sample.product.category.category.code.substring(0, 2),
                            type: "GET",
                            dataType: "json",
                            async: false,
                            success: function(value) {
								fsn.endTime = new Date();
                                if (value.result.status == "true") {
                                    $("#category2").data("kendoComboBox").setDataSource(value.data);
                                    $("#category2").data("kendoComboBox").value(returnValue.data.sample.product.category.category.id);
                                }
                            },
							error: function(e) {
              				  if (e.status == 911) {
                    				lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
                			  } else {
                  			 	 	lims.initNotificationMes('系统异常！', false);
              				  }
           					 },
                        });
                        //二级分类
                        $.ajax({
                            url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + returnValue.data.sample.product.category.category.id + "?categoryFlag=" + true,
                            type: "GET",
                            dataType: "json",
                            async: false,
                            success: function(value) {
								fsn.endTime = new Date();
                                if (value.result.status == "true") {
                                    $("#category3").data("kendoComboBox").setDataSource(value.data);
                                }
                            },
							error: function(e) {
               					 if (e.status == 911) {
                   					 lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
                				} else {
                  					  lims.initNotificationMes('系统异常！', false);
             					}
         				  },
                        });
                        //三级分类
                        $("#category3").data("kendoComboBox").value(returnValue.data.sample.product.category.id);
                        //执行标准
                        var text = "";
                        for (var i = 0; i < returnValue.data.sample.product.regularity.length; i++) {
                            text = text + returnValue.data.sample.product.regularity[i].name + ";"
                        }
                        $("#foodinfo_standard").val(text);
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
            if (root.backUploadPdfPageFlag) {
                $(".gPrev a").attr('href', '/fsn-core/views/upload/uploadPdf.html');
            }
        }
        if (isNew) {
            $.ajax({
                url: portal.HTTP_PREFIX + "/tempReport/getTempReport",
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnValue) {
                    fsn.endTime = new Date();
                    if (returnValue.result.status == "true") {
                        var report = returnValue.data;
                        if (!report) {return;}
                        report.id = 1; // (在执行setEasyReportValue中需按照报告id展示检测结论的值)
                        lims.setEasyProductValue(returnValue.data.sample, false);
                        lims.setEasyBusunitValue(returnValue.data.sample.producer);
                        lims.setEasyReportValue(returnValue.data, false, false);
                        var qsAndFormatVo = report.sample.producer.qsNoAndFormatVo;
                        var qsNo = qsAndFormatVo.qsNo == null ? "": qsAndFormatVo.qsNo;
                        var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
                        qsFormatIt.value(qsAndFormatVo.licenceFormat.id);
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
                            	root.setStyle(0);
                                first = qsNo.charAt(1);//省的简称
                            	socend = qsItem.formetValue.replace("(?)","");//中间字母
                            	third = qsNo.substr(5);//数字部分
                            }
                            if(first != ""){
                                $("#proJianCheng").data("kendoDropDownList").text(first);
                            }
                            $("#showQsFormat").html(socend);
                            $("#bus_qsNo").val(third);
                        }else{
                            $("#jianchengList").hide();
                            root.showJianCheng = false;
                            $("#showQsFormat").html(qsItem.formetValue);
                            $("#bus_qsNo").val(qsNo.replace(qsItem.formetValue, ""));
                        }

                        if ($("#barcodeId").val() == "") {
                            return;
                        };
                        //分类赋值
                        //一级分类
                        if (returnValue.data.sample.product.category) {
                            $("#category1").data("kendoComboBox").value(returnValue.data.sample.product.category.categoryOneCode);
                            $.ajax({
                                url: portal.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode=" + returnValue.data.sample.product.category.categoryOneCode,
                                type: "GET",
                                dataType: "json",
                                async: false,
                                success: function(value) {
									fsn.endTime = new Date();
                                    if (value.result.status == "true") {
                                        $("#category2").data("kendoComboBox").setDataSource(value.data);
                                        if (returnValue.data.sample.product.category.category != null) {
                                            $("#category2").data("kendoComboBox").value(returnValue.data.sample.product.category.category.id);
                                        }
                                    }
                                },
								error: function(e) {
					                if (e.status == 911) {
					                    lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
					                } else {
					                    lims.initNotificationMes('系统异常！', false);
					                }
					            },
                            });
                            if (returnValue.data.sample.product.category.category == null) {
                                return;
                            }
                            //二级分类
                            $.ajax({
                                url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + returnValue.data.sample.product.category.category.id + "?categoryFlag=" + true,
                                type: "GET",
                                dataType: "json",
                                async: false,
                                success: function(value) {
									fsn.endTime = new Date();
                                    if (value.result.status == "true") {
                                        $("#category3").data("kendoComboBox").setDataSource(value.data);
                                    }
                                },
								error: function(e) {
					                if (e.status == 911) {
					                    lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
					                } else {
					                    lims.initNotificationMes('系统异常！', false);
					                }
					            },
                            });
                            //三级分类
                            if (returnValue.data.sample.product.category.name) {
                                $("#category3").data("kendoComboBox").text(returnValue.data.sample.product.category.name);
                            }
                            //执行标准
                            $("#foodinfo_standard").val(returnValue.data.sample.product.regularityStr);
                        }
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
        }
        if (isNew) {
            $("#update").hide();
            $("#add").hide();
        } else {
            $("#save").hide();
            $("#clear").hide();
            $("#submit").hide();
        }
    };

    root.setProAttachments = function(proAttachments) {
        var dataSource = new kendo.data.DataSource();
        root.aryProAttachments.length = 0;
        if (proAttachments.length > 0) {
            $(".proListVStyle").show();
            $("#btn_clearProFiles").show();
            for (var i = 0; i < proAttachments.length; i++) {
                root.aryProAttachments.push(proAttachments[i]);
                dataSource.add({
                    attachments: proAttachments[i]
                });
            }
        } else {
            $(".proListVStyle").hide();
        }
        $("#proAttachmentsListView").kendoListView({
            dataSource: dataSource,
            template: kendo.template($("#uploadedFilesTemplate").html()),
        });
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
        },
        {
            text: "不合格",
            value: "0"
        },
        ],
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
        },
        {
            text: "企业送检",
            value: "企业送检"
        },
        {
            text: "政府抽检",
            value: "政府抽检"
        },
        ],
        filter: "contains",
        suggest: true,
        index: 0
    });

    var conclusion_ = [{
        "value": "合格",
        "text": "合格"
    },
    {
        "value": "不合格",
        "text": "不合格"
    },
    {
        "value": "--",
        "text": "--"
    }];

    root.buildUpload = function(id, attachments, msg, flag) {
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
            multiple: id == "upload_report_files" ? false: true,
            upload: function(e) {
                var files = e.files;
                $.each(files,
                function() {
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
                    } else if (flag == "report") {
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
                        } else if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
                            lims.initNotificationMes('文件格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
                            e.preventDefault();
                            return;
                        }
                    } else if (extension != ".xls") {
                        lims.initNotificationMes('请确保你上传的是Excel文件，并且是2003版本。', false);
                        e.preventDefault();
                        return;
                    }
                });
            },
            success: function(e) {
                if (e.operation == "upload") {
                    fsn.endTime = new Date();
                    if (flag == "items") {
                        lims.setEasyItems(e.response.results);
                        if (e.response.results == null || e.response.results.length < 1) {
                            lims.initNotificationMes('excel文件中没有可以导入的检测项目！', false);
                        }
                        $("#import_Excel_Win").data("kendoWindow").close();
                    } else {
                        attachments.push(e.response.results[0]);
                        if (flag == "product") {
                            $("#" + msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
                        } else {
                            if (id == "uploadRepPics") {
                                /*图片合成pdf功能，再次初始化上传控件*/
                                lims.initPicToPdfUpload(e.response.results[0], root.aryRepAttachments);
                            }
                            $("#" + msg).html(lims.l("Document recognition is successful, you can save!"));
                        }
                    }
                } else if (e.operation == "remove") {
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
                }
            },
            remove: function(e) {
                if (flag == "report") {
                    lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
                    $("#" + msg).html("您可以：上传pdf文件，或上传图片自动合成pdf！");
                } else {
                    $("#" + msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )");
                }
            },
            error: function(e) {
                $("#" + msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
            },
        });
    };

    /*图片转pdf时的预览方法*/
    root.ReviewPdf = function() {
        lims.viewPdf($("#tri_reportNo").val().trim());
    };

    /*图片合成pdf时的确定按钮事件*/
    root.picToPdfWinOk = function() {
        lims.picToPdfFun($("#tri_reportNo").val().trim());
    };

    root.grid_AddItem = function() {
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
        data: [{
            name: "",
            unit: "",
            techIndicator: "",
            result: "",
            assessment: "合格",
            standard: ""
        },
        {
            name: "",
            unit: "",
            techIndicator: "",
            result: "",
            assessment: "合格",
            standard: ""
        },
        {
            name: "",
            unit: "",
            techIndicator: "",
            result: "",
            assessment: "合格",
            standard: ""
        },
        {
            name: "",
            unit: "",
            techIndicator: "",
            result: "",
            assessment: "合格",
            standard: ""
        },
        {
            name: "",
            unit: "",
            techIndicator: "",
            result: "",
            assessment: "合格",
            standard: ""
        }],
        batch: true,
        page: 1,
        //pageSize: 8,
        serverPaging: true,
        serverFiltering: true,
        serverSorting: true
    });

    root.getAutoItemsDS = function() {
        return new kendo.data.DataSource({
            transport: {
                read: {
                    url: function() {
                        return portal.HTTP_PREFIX + "/testReport/autoItems/" + root.colName + "?page=1&pageSize=20&keyword=" + root.keyword;
                    },
                    type: "GET",
                }
            },
            schema: {
                data: "data"
            },
            change: function(e) {
                var date = new Date();
                date.setSeconds(date.getSeconds() + 1);
                fsn.endTime = date;
            },
        });
    };

    root.buildAutoComplete = function(input, container, options) {
        input.attr("name", options.field);
        input.attr("class", "k-textbox");
        input.appendTo(container);
        input.kendoAutoComplete({
            dataSource: [],
            filter: "contains",
            dataBound: function() {
                if (root.keyword == input.val().trim()) {
                    return;
                }
                root.keyword = input.val().trim();
                input.data("kendoAutoComplete").setDataSource(root.getAutoItemsDS());
                input.data("kendoAutoComplete").search(root.keyword);
            },
        });
    };

    $("#speCharWindow td a").click(function() {
        var activeElement = $("#report_grid_active_cell");
        if (!activeElement.length) {
            lims.initNotificationMes('请先选择检测项目列表中的某一个单元格！', false);
            return;
        }
        var htmVal = activeElement.html();
        if (htmVal == "合格" || htmVal == "检测项目名称" || htmVal == "单位" || htmVal == "技术指标" || htmVal == "检测结果" || htmVal == "检测依据" || htmVal == "单项评价" || htmVal == "操作") {
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

    /*kms检查项目名称加载*/
    root.kmsItemNamePage = 1;
    root.buildAutoCompleteKMS = function(input, container, options) {
        /*调用kms接口，获取检测项目名称数据源*/
        input.attr("name", options.field);
        input.attr("class", "k-textbox");
        input.appendTo(container);
        input.kendoAutoComplete({
            dataSource: [],
            filter: "contains",
            dataTextField: "name",
            dataBound: function() {
                if (root.keyword == input.val().trim()) {
                    return;
                }
                root.keyword = input.val().trim();
                /*关键字被更改后，将page设置为1*/
                root.kmsItemNamePage = 1;
                var listItemDs = lims.getItemsNameDS(root.kmsItemNamePage, root.keyword);
                input.data("kendoAutoComplete").setDataSource(listItemDs);
                input.data("kendoAutoComplete").search(root.keyword);
            },
        });
    };

    /*检测项目名称*/
    root.autoTestItems = function(container, options) {
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input id='itemName' />");
        root.buildAutoCompleteKMS(input, container, options);
        $("#itemName_listbox").scroll(function() {
            lims.appendKmsItemName($(this), root.kmsItemNamePage, root.keyword);
        });
    };

    root.autoTestUnits = function(container, options) {
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input/>");
        root.keyword = "";
        root.colName = 2;
        root.buildAutoComplete(input, container, options);
    };

    root.autoSpecification = function(container, options) {
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input/>");
        root.keyword = "";
        root.colName = 3;
        root.buildAutoComplete(input, container, options);
    };

    root.autoTestResult = function(container, options) {
        root.grdItem = options.model;
        root.grdItemFiled = options.field;
        var input = $("<input/>");
        root.keyword = "";
        root.colName = 4;
        root.buildAutoComplete(input, container, options);
    };

    root.autoStandard = function(container, options) {
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
            },
            {
                field: "name",
                title: lims.l("Item Name"),
                editor: root.autoTestItems,
                width: 40
            },
            {
                field: "unit",
                title: lims.l("Unit"),
                editor: root.autoTestUnits,
                width: 30
            },
            {
                field: "techIndicator",
                title: lims.l("Specifications"),
                editor: root.autoSpecification,
                width: 50
            },
            {
                field: "result",
                title: lims.l("Test Result"),
                editor: root.autoTestResult,
                width: 40
            },
            {
                field: "assessment",
                title: lims.l("Conslusion"),
                values: conclusion_,
                editable: false,
                width: 40
            },
            {
                field: "standard",
                title: lims.l("Standard_"),
                editor: root.autoStandard,
                width: 50
            },
            {
                command: [{
                    name: "Remove",
                    text: "<span class='k-icon k-cancel'></span>" + lims.l("Delete"),
                    click: function(e) {
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

    root.clearTempReport = function() {
        var success = false;
        $.ajax({
            url: portal.HTTP_PREFIX + "/tempReport/clearTempReport",
            type: "DELETE",
            dataType: "json",
            async: false,
            success: function(data) {
                fsn.endTime = new Date();
                if (data.result.status == "true") {
                    success = true;
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
        return success;
    };

    fsn.setDateValue = function() {
        $("#tri_proDate_format").data("kendoDatePicker").value($("#tri_proDate").val());
        $("#tri_testDate_format").data("kendoDatePicker").value($("#tri_testDate").val());
    };

    root.clearReport = function() {
        var success = root.clearTempReport();
        if (success) {
            $("#barcodeId").val("");
            root.clearProduct();
            $("#bus_qsNo").val("");
            root.clearBusUnit();
            $("#ti_regist_no").val("");
            //root.clearTestee();
            $("#tri_reportNo").val("");
            $("#tri_testee").val("");
            $("#tri_testOrg").val("");
            $("#proExpirYear").val("");
            $("#proExpirMonth").val("");
            $("#proExpirDay").val("");
            $("#tri_conclusion").data("kendoDropDownList").value("1");
            $("#tri_testPlace").val("");
            $("#tri_proDate").val("");
            $("#tri_testDate").val("");
            $("#tri_batchNo").val("");
            $("#foodInfo_ads").val("");
            $("#sampleCounts").val("");
            $("#judgeStandard").val("");
            $("#testResultDescribe").val("");
            $("#remarks").val("");
            $("#sampleAds").val("");
            $("#tri_testType").data("kendoDropDownList").value("企业自检");
            root.aryRepAttachments.length = 0;
            $("#upload_pro").html("");
            $("#upload_pro").html("<input id='upload_prodPhoto_btn' type='file' />");
            root.buildUpload("upload_prodPhoto_btn", root.aryProAttachments, "fileEroMsg", "product");
            //清除自动加载进来的图片
            root.clearProFiles();
            root.clearRepFiles();

            $("#report_grid").data("kendoGrid").setDataSource(root.gridDataSource);
            lims.initNotificationMes("清除成功！", true);
        }
        // window.location.href="/fsn-core/views/market/add_testreport.html";
    };

    root.clearBusUnit = function() {
        $("#bus_qsNo").val("");
        $("#bus_licenseNo").val("");
        $("#bus_name").val("");
        $("#bus_address").val("");
    };
    //way:判断是保存还是提交方式
    root.createInstance = function(way) {
        var license = {
            licenseNo: $("#bus_licenseNo").val().trim(),
        };
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
        var busUnit = {
            name: $("#bus_name").val().trim(),
            address: $("#bus_address").val().trim(),
            license: license,
            qsNoAndFormatVo: qsNoAndFormatVo
        };
        var businessBrand = {
            name: $("#foodinfo_brand").val().trim(),
        };
        var category = null;
        if ($("#category1").data("kendoComboBox").select() != -1) {
            category = {
                name: $("#category3").data("kendoComboBox").select() == -1 ? $("#category3").val().trim() : $("#category3").data("kendoComboBox").dataItem().name,
                category: {
                    id: $("#category2").data("kendoComboBox").select() == -1 ? null: $("#category2").data("kendoComboBox").value()
                },
                categoryOneCode: $("#category1").data("kendoComboBox").value()
            };
        }
        var unit = {
            name: $("#foodinfo_minunit").val().trim()
        };
        var isLocal = false;
        if (root.enterpriseName == $("#bus_name").val().trim()) isLocal = true;
        var product = {
            barcode: $("#barcodeId").val().trim(),
            name: $("#foodName").val().trim(),
            format: $("#specification").val().trim(),
            unit: unit,
            status: $("#foodInfo_Status").val().trim(),
            expiration: lims.getExpirDay(),
            expirationDate: $("#foodinfo_expirday").val().trim(),
            businessBrand: businessBrand,
            category: category,
            characteristic: "无",
            proAttachments: root.aryProAttachments,
            producerFlag: false,
            local: isLocal,
        };
        if (way == "save") {
            product.regularityStr = $("#foodinfo_standard").val().trim();
        } else if (way == "submit") {
            product.regularity = root.getRegularity();
        }
        var proDate = $("#tri_proDate").val().trim();
        var sample = {
            id: $("#sample_id").val().trim(),
            productionDate: (proDate == "0000-00-00" ? null: proDate),
            batchSerialNo: $("#tri_batchNo").val().trim(),
            producer: busUnit,
            product: product,
            proDateStr:proDate,
        };
        var testee = {
            name: $("#tri_testee").val().trim(),
        };
        root.TestReport = {
            id: root.edit_id,
            testOrgnization: $("#tri_testOrg").val().trim(),
            testType: $("#tri_testType").data("kendoDropDownList").value(),
            pass: $("#tri_conclusion").data("kendoDropDownList").value() == "1" ? true: false,
            testDate: $("#tri_testDate").val().trim(),
            serviceOrder: $("#tri_reportNo").val().trim(),
            sampleQuantity: $("#sampleCounts").val().trim(),
            standard: $("#judgeStandard").val().trim(),
            result: $("#testResultDescribe").val().trim(),
            samplingLocation: $("#sampleAds").val().trim(),
            testPlace: $("#tri_testPlace").val().trim(),
            comment: $("#remarks").val().trim(),
            testProperties: way == "save" ? $("#report_grid").data("kendoGrid").dataSource.data() : root.getItems(),
            //保存时不用做验证
            newFlag: isNew,
            autoReportFlag: root.isAutoReport,
            sample: sample,
            testee: testee,
            uploadPath: root.upload_path,
            repAttachments: root.aryRepAttachments,
            updateFlag: root.updateFlag,
            endDate: null,
            publishFlag:'3',
            //$("#reportEndDate").val(),
        };
    };

    root.validateReportNO = function() {
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
            success: function(returnValue) {
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                    unique = returnValue.data;
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
        return unique;
    };

    root.isExist_category = function(categoryName) {
        var isExist = true;
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/validatCategory/" + categoryName,
            type: "GET",
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function(returnValue) {
                fsn.endTime = new Date();
                if (returnValue.data != null) {
                    $("#foodinfo_category").data("kendoComboBox").value(returnValue.data.code);
                    isExist = true;
                } else {
                    isExist = false;
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
        return isExist;
    };

    root.validate_save = function() {
        var expirday = $("#foodinfo_expirday").val();
        if (expirday.length == 0 || expirday.trim() == "") {
            $("#foodinfo_expirday").val("");
        } else {
            var re1 = /^[0-9]*$/;
            if (!expirday.match(re1) || expirday > 10000000) {
                $("#foodinfo_expirday").val("");
            }
        }

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

    /* 暂存功能 */
    fsn.save = function() {
        $("#saving_msg").html("正在保存，请稍候...");
        $("#toSaveWindow").data("kendoWindow").open().center();
        root.validate_save();
        root.createInstance("save");
        $.ajax({
            url: portal.HTTP_PREFIX + "/tempReport/saveTempReport",
            type: "POST",
            dataType: "json",
            async: false,
            timeout: 600000,
            //10min
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(root.TestReport),
            success: function(returnValue) {
                fsn.endTime = new Date();
                $("#saving_msg").html("");
                $("#toSaveWindow").data("kendoWindow").open().close();
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
            error: function(e) {
                $("#saving_msg").html("");
                $("#toSaveWindow").data("kendoWindow").close();
                if (e.status == 911) {
                    lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
                } else {
                    lims.initNotificationMes('系统异常！', false);
                }
            }
        });
        $("#save_status").html("");
    };

    root.submit = function() {
        root.clearErrorInfoStyle();
        if (!root.validateData()) { // 必填项验证
            return;
        }

        var firstPart = $("#showQsFormat").html().indexOf("?");
        if (firstPart!=-1) {
            lims.initNotificationMes("您选择的输入规则没有加载出正确的省份简称，请在企业基本信息中完善企业地址保存后再试！", false);
            return;
        }
        /*var barcode = $("#barcodeId").val();
			 var re_barcode = /^[0-9]{13}$/;
			 if(!barcode.match(re_barcode)){
				 lims.msg("success_msg", null, ("产品条形码只能由13位数字组成！"));
	    		 return;
			 }*/

        /* 对"检查项目"grid 提交时 的数据验证 */
        var gridItem = $("#report_grid").data("kendoGrid").dataSource.data();
        if (gridItem.length < 1 && root.isAutoReport) {
            lims.initNotificationMes("至少需要一条检测项目！", false);
            return;
        } else {
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

        var categoryCode = $("#category2").data("kendoComboBox").dataItem().code;
        if (root.busunit_name != "永辉超市" && categoryCode.indexOf("15") != 0) {
            var expirday = $("#foodinfo_expirday").val();
            if (expirday.length == 0 || expirday.trim() == "") {
                lims.initNotificationMes("保质天数不能为空,请输入保质期来自动计算保质天数", false);
                return;
            } else {
                var re1 = /^[0-9]*$/;
                if (!expirday.match(re1)) {
                    $("#foodinfo_expirday").attr("style", "border:2px solid red;");
                    lims.initNotificationMes("保质天数只能为数字！", false);
                    return;
                }
                if (expirday > 10000000) {
                    $("#foodinfo_expirday").attr("style", "border:2px solid red;");
                    lims.initNotificationMes("保质天数应小于等于999999999！", false);
                    return;
                }
            }
        }

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
                lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" + $("#barcodeId").val().trim() + "】、批次号【" + $("#tri_batchNo").val().trim() + "】已经存在对应的报告，3个字段不能重复！", false);
                return;
            }
        } else {
            if (root.ReportNo_old != $("#tri_reportNo").val().trim()) {
                if (!root.validateReportNO()) {
                    $("#tri_reportNo").attr("style", "border:2px solid red;");
                    lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" + $("#barcodeId").val().trim() + "】、批次号【" + $("#tri_batchNo").val().trim() + "】已经存在对应的报告，3个字段不能重复！", false);
                    return;
                }
            }
        }

        if (!root.isAutoReport && root.aryRepAttachments.length == 0) {
            lims.initNotificationMes(lims.l("not found pdf"), false);
            return;
        } else if (root.isAutoReport && root.aryRepAttachments.length > 0) {
            lims.initNotificationMes(lims.l("auto create pdf"), false);
            return;
        }
        /*else if(root.aryRepAttachments.length > 1){
	        	 lims.initNotificationMes(lims.l("pdf more then"),false);
	        	 return;
	         }*/
        if (root.isAutoReport && $("#tri_testType").data("kendoDropDownList").value() != "企业自检") {
            lims.initNotificationMes("只有在【检验类别】为企业自检的情况下，才能选择自动生成pdf，否则请手动上传pdf!", false);
            return;
        }
        $("#success_msg").html("");
        $("#success_msg").attr("style", "");
        
        /* 验证qs的有效性 */
        /*if (!lims.validate_hasBideQs(barcode, "流通企业", root.qsFormatId)) {
            return;
        }*/
        
        if (root.aryProAttachments.length == 0) {
            $("#saveWindow").data("kendoWindow").open().center();
        } else {
            root.submitReport();
        }
    };

    root.submitReport = function() {
        $("#save_status").html("正在提交，请稍候...");
        $("#saving_msg").html("正在提交，请稍候...");
        $("#toSaveWindow").data("kendoWindow").open().center();

        root.createInstance("submit");
        var save_report = root.TestReport;
        if (!isNew) {
            save_report.id = root.edit_id;
        }
        var updateApply = root.updateApply==true?true:false;
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/"+updateApply,
            type: isNew ? "POST": "PUT",
            dataType: "json",
            //async:false,
            timeout: 600000,
            //10min
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(save_report),
            success: function(returnValue) {
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
                        lims.initNotificationMes('编号为' + returnValue.data.serviceOrder + '的检测报告 ' + (isNew ? lims.l("Add") : lims.l("Update")) + '成功！', true);
                        fsn.sendItemsToKMS();
                        /*将检测项目发回至KMS*/
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
                    root.setProAttachments(returnValue.data.sample.product.proAttachments);
                    if (!returnValue.data.autoReportFlag) {
                        root.setRepAttachments(returnValue.data.repAttachments);
                    }
                    $("#report_grid").data("kendoGrid").dataSource.data(returnValue.data.testProperties == null ? [] : returnValue.data.testProperties);

                    //二级分类
                    $.ajax({
                        url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + $("#category2").data("kendoComboBox").value() + "?categoryFlag=" + true,
                        type: "GET",
                        dataType: "json",
                        async: false,
                        success: function(value) {
							fsn.endTime = new Date();
                            if (value.result.status == "true") {
                                $("#category3").data("kendoComboBox").setDataSource("");
                                $("#category3").data("kendoComboBox").setDataSource(value.data);
                                if (returnValue.data.sample.product.category.id) {
                                    $("#category3").data("kendoComboBox").value(returnValue.data.sample.product.category.id);
                                }
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
                } else {
                    lims.initNotificationMes((isNew ? lims.l("Add") : lims.l("Update")) + '失败！', false);
                }
                $("#save_status").html("");
                $("#saving_msg").html("");
            },
            error: function(e) {
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

    root.refresh = function() {
        window.location.pathname = "/fsn-core/views/market/add_testreport.html";
    };

    /*打开导入检测项目的窗口*/
    lims.openExel_Win = function() {
        $("#up_ex_fileDiv").html("<input id='up_ex_file' type='file' />");
        root.buildUpload("up_ex_file", null, "ex_msg", "items");
        $("#import_Excel_Win").data("kendoWindow").open().center();
    };

    $("#yesAuto").click(function() {
        document.getElementById("yesAuto").checked = true;
        $("#tr_isAuto").hide();
        root.isAutoReport = true;
    });

    $("#noAuto").click(function() {
        var repListView = $("#repAttachmentsListView").data("kendoListView");
        var pdfCont = 0; //当前上传的pdf数量
        if (repListView != null) pdfCont = repListView.dataSource._total;
        //如果当前报告为新增状态，并且上传的pdf数量为小于1时，重新激活并初始化上传pdf的控件
        if (isNew && (repListView == null || pdfCont < 1)) {
            root.aryRepAttachments.length = 0;
            lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
            $("#upload_rep").html("<input id='upload_report_files' type='file' />");
            root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        }
        document.getElementById("noAuto").checked = true;
        $("#tr_isAuto").show();
        root.isAutoReport = false;
    });

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

    $("#bus_name").blur(function() {
        if ($("#bus_name").val().trim() == "" || root.initBusName == $("#bus_name").val().trim()) {
            return;
        }
        root.initBusName = $("#bus_name").val().trim();
        var bus = root.getBusOfpro2BusByName(root.initBusName);
        if (bus != null) {
            lims.setEasyBusunitValue(bus);
            return;
        }
        lims.clearInputReadOnly(["bus_address", "bus_qsNo", "bus_licenseNo"]);
        $(".editQsStyle").css("display", "none");
        $("#bus_licenseNo").val("");
        $("#bus_qsNo").val("");
        $("#bus_address").val("");
        //root.validateQsNo();
        var busUnit = root.getBusUnitByName(root.initBusName);
        root.handleBusUnitInfo(busUnit);
    });

    root.onSelectBarcode = function(e) {
        root.initBarcode = this.dataItem(e.item.index());
        root.autoloadingPageInfoByBarcode(root.initBarcode);
    };

    root.onSelectQsNo = function(e) {
        var dataItem = $("#showQsFormat").html() + this.dataItem(e.item.index());
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/getBusUnitByQsNo/" + dataItem,
            type: "GET",
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function(returnValue) {
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                    if (returnValue.data != null) {
                        lims.setEasyBusunitValue(returnValue.data);
                        $("#bus_qsNo").val(returnValue.data.qsNoAndFormatVo.qsNo.replace($("#showQsFormat").html(), ""));
                    } else {
                        $("#bus_name").val("");
                        $("#bus_address").val("");
                        $("#bus_licenseNo").val("");
                    }

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
    }

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

    root.handleBusUnitInfo = function(busUnit) {
        if (busUnit == null) {
            return;
        }
        lims.setEasyBusunitValue(busUnit);
    };

    //根据用户输入的生产企业名称自动加载企业信息
    root.onSelectBusUnitName = function(e) {
        root.initBusName = this.dataItem(e.item.index());
        var busUnit = root.getBusUnitByName(root.initBusName);
        root.handleBusUnitInfo(busUnit);
    };

    //根据用户输入的生产企业营业执照号自动加载企业信息
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

    //根据用户输入的生产企业地址自动加载企业信息
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

    root.onSelectUnitName = function(e) {
        var unitName = this.dataItem(e.item.index());
        $("#foodinfo_minunit").val(unitName);
    };

    root.onSelectBrandName = function(e) {
        var brandName = this.dataItem(e.item.index());
        $("#foodinfo_brand").val(brandName);
    };

    root.clearProFiles = function() { //清空产品资源
        var listIds = [];
        var count = 0;
        for (var i = 0; i < root.aryProAttachments.length; i++) {
            if (root.aryProAttachments[i].id != null) {
                listIds.push(root.aryProAttachments[i].id);
            } else {
                count++;
            }
        }
        root.aryProAttachments.length = count;
        if ($("#proAttachmentsListView").data("kendoListView")) {
            $("#proAttachmentsListView").data("kendoListView").dataSource.data([]);
        }
        $("#btn_clearProFiles").hide();
    };

    root.clearRepFiles = function() { //清空报告资源
        root.aryRepAttachments.length = 0;
        fsn.pdfRes = null;
        lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
        $("#repAttachmentsListView").data("kendoListView").dataSource.data([]);
        $("#btn_clearRepFiles").hide();
    };

    root.clearProduct = function() {
        $("#foodName").val("");
        $("#specification").val("");
        $("#foodinfo_model_no").val("");
        $("#foodinfo_brand").val("");
        $("#foodinfo_expiratio").val("");
        $("#foodinfo_expirday").val("");
        $("#foodinfo_minunit").val("");
        $("#foodInfo_Status").val("");
        $("#category1").data("kendoComboBox").value("");
        $("#category2").data("kendoComboBox").value("");
        $("#category3").data("kendoComboBox").value("");
        $("#foodinfo_standard").val("");
        root.initialCategories();
        root.aryProAttachments.length = 0;
        root.isPdf = false;
        $("#upload_rep").html("");
        $("#upload_rep").html("<input id='upload_report_files' type='file' />");
        root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
    };

    root.removeRes = function(resID, fileName) { //从页面删除
        root.isPro = false;
        var dataSource = new kendo.data.DataSource();
        for (var i = 0; i < root.aryProAttachments.length; i++) {
            if ((resID != null && root.aryProAttachments[i].id == resID) || root.aryProAttachments[i].fileName == fileName) {
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
                if ((resID != null && root.aryRepAttachments[i].id == resID) || root.aryRepAttachments[i].fileName == fileName) {
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
        } else {
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

    root.validateData = function() {
        root.validator = $("#content_container").kendoValidator().data("kendoValidator");
        if (!root.validator.validate()) {
            var validate_barcode = $("#barcodeId").kendoValidator().data("kendoValidator").validate();
            var validate_foodName = $("#foodName").kendoValidator().data("kendoValidator").validate();
            if (!validate_barcode || !validate_foodName) {
                lims.initNotificationMes(lims.l("Product Information Bar has required Unfilled"), false);
                return false;
            }
            //验证单位为必填项
            var proUnit = $("#foodinfo_minunit").val().trim();
            if (proUnit.length < 1) {
                $("#foodinfo_minunit").focus();
                lims.initNotificationMes("请填写产品的单位！", false);
                return false;
            }
            var validate_licenseNo = $("#bus_licenseNo").kendoValidator().data("kendoValidator").validate();
            var validate_buiName = $("#bus_name").kendoValidator().data("kendoValidator").validate();
            if (!validate_licenseNo || !validate_buiName) {
                lims.initNotificationMes(lims.l("Manufacturers have required information bar unfilled"), false);
                return false;
            }
            var validate_judgeStandard = $("#judgeStandard").val().trim();
            var validate_batchNo = $("#tri_batchNo").kendoValidator().data("kendoValidator").validate();
            var validate_reportNo = $("#tri_reportNo").kendoValidator().data("kendoValidator").validate();
            var validate_proDate = $("#tri_proDate").kendoValidator().data("kendoValidator").validate();
            var validate_testOrg = $("#tri_testee").kendoValidator().data("kendoValidator").validate();
            $("#judgeStandard").kendoValidator().data("kendoValidator").validate();
            $("#testResultDescribe").kendoValidator().data("kendoValidator").validate();

            if (!validate_batchNo || !validate_reportNo || !validate_proDate || !validate_testOrg) {
                lims.initNotificationMes(lims.l("Test report required information fields have not filled"), false);
                return false;
            }
			if (validate_judgeStandard == "" || validate_judgeStandard == null){
               lims.initNotificationMes(lims.l("Test report required information fields have not filled"), false);
			    return false;
            }
        }
        var qsNo = ($("#bus_qsNo").val().trim()).replace(/\s*/g, "").replace(/-/g, "").replace(/\s+/g, "");
        if (qsNo == "") {
            $("#bus_qsNo").val("");
            $("#bus_qsNo").kendoValidator().data("kendoValidator").validate();
            lims.initNotificationMes(lims.l("QS card number can not be empty") + "!", false);
            return false;
        }
        var indexCategory1 = $("#category1").data("kendoComboBox").select();
        if (indexCategory1 == -1) {
            $("#category1").focus();
            lims.initNotificationMes("请选择产品所属的食品种类一级分类！", false);
            return false;
        }
        var indexCategory2 = $("#category2").data("kendoComboBox").select();
        if (indexCategory2 == -1) {
            $("#category2").focus();
            lims.initNotificationMes("请选择产品所属的食品种类二级分类！", false);
            return false;
        }
        var category3 = $("#category3").data("kendoComboBox").text().trim();
        if (category3 == "" || category3 == "请选择...") {
            $("#category3").focus();
            lims.initNotificationMes("请选择产品所属的食品种类三级分类！", false);
            return false;
        }
        var regularity = $("#foodinfo_standard").val();
        if (regularity == "") {
            $("#foodinfo_standard").focus();
            lims.initNotificationMes("请填写执行标准！", false);
            return false;
        }
        if ($("#testResultDescribe").val().length < 1) {
            lims.initNotificationMes("检验结论描述不能为空", false);
            return false;
        }
        var licenNo = $("#bus_licenseNo").val().trim().replace(/\s+/g, "").replace(/-/g, "");
        if (licenNo == "") {
            $("#bus_licenseNo").val("");
            $("#bus_licenseNo").kendoValidator().data("kendoValidator").validate();
            lims.initNotificationMes(lims.l("Please enter a business license number"), false);
            return false;
        }

        var status = /^[0-9]*$/.test(qsNo);
        if ($("#bus_qsNo").val().trim().indexOf(root.formetType) == -1 || !status || qsNo.length != root.formetLength) {
            $("#bus_qsNo").attr("style", "width:300px;border:2px solid red;");
            fsn.initNotificationMes("生产许可证号格式不正确！", false);
            return false;
        }
        var VlicnNo = /^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{13}$|^[A-Za-z0-9]{18}$/.test(licenNo);
        if (!VlicnNo) {
            $("#bus_licenseNo").attr("style", "width:300px;border:2px solid red;");
            lims.initNotificationMes(lims.l("Business license number is not in the correct format, must be composed of letters or numbers 13 or 15.") + "!", false);
            return false;
        }
        //验证报告编号不能包含“？”、“?”、“'”字符,如果包含空格通过验证
        var repNo = $("#tri_reportNo").val().trim();
        var repNoFormat = true;
        var repNoMes = "报告编号不能包含";
        if (repNo.indexOf("？") > -1 || repNo.indexOf("?") > -1) {
            repNoMes += "?";
            repNoFormat = false;
        }
        if (repNo.indexOf("'") > -1 || repNo.indexOf("’") > -1) {
            repNoMes += "'";
            repNoFormat = false;
        }
        if (repNo.indexOf("&") > -1) {
            repNoMes += "&";
            repNoFormat = false;
        }
        if (repNo.indexOf("<") > -1) {
            repNoMes += "<";
            repNoFormat = false;
        }
        if (repNo.indexOf(">") > -1) {
            repNoMes += ">";
            repNoFormat = false;
        }
        if (!repNoFormat) {
            $("#tri_reportNo").attr("style", "border:2px solid red;");
            lims.initNotificationMes(repNoMes, false);
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
        } else if (newEndDate < newStartDate) {
            lims.initNotificationMes('检验日期不能小于生产日期！', false);
            return false;
        }
        return true;
    };

    $("#btn_saveOK").click(function() {
        $("#saveWindow").data("kendoWindow").close();
        root.submitReport();
    });

    $("#btn_saveCancel").click(function() {
        $("#saveWindow").data("kendoWindow").close();
    });

    $("#barcodeId").kendoValidator().data("kendoValidator");
    $("#bus_qsNo").kendoValidator().data("kendoValidator");
    $("#foodName").kendoValidator().data("kendoValidator");
    $("#bus_name").kendoValidator().data("kendoValidator");
    $("#tri_batchNo").kendoValidator().data("kendoValidator");
    $("#tri_reportNo").kendoValidator().data("kendoValidator");
    $("#tri_proDate").kendoValidator().data("kendoValidator");
    $("#bus_licenseNo").kendoValidator().data("kendoValidator");
    $("#tri_testee").kendoValidator().data("kendoValidator");
    $("#judgeStandard").kendoValidator().data("kendoValidator");
    $("#testResultDescribe").kendoValidator().data("kendoValidator");

    //清除输入框的错误提示样式
    root.clearErrorInfoStyle = function() {
        $("#barcodeId").removeAttr("style");
        $("#foodinfo_expirday").removeAttr("style");
        $("#foodinfo_category").prev().children("input").attr("style", "width:100%;");
        $("#bus_qsNo").attr("style", "width:100%;");
        $("#bus_licenseNo").attr("style", "width:300px;");
        $("#tri_reportNo").removeAttr("style");
        $("#tri_proDate").attr("style", "width:100%;height:100%;");
        $("#tri_testDate").attr("style", "width:100%;height:100%");
    };

    root.autoloadingPageInfoByBarcode = function(barcode) {
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/getReportByBarcode/" + barcode,
            type: "GET",
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function(returnValue) {
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                    lims.setEasyProductValue(returnValue.data.sample, false);
					$("#regularityDiv").bind("click",root.searchRegularity);
					if(returnValue.data.sample.product.producerFlag){
						$("#regularityDiv").unbind("click");
					}
                    if (isNew) {
                        var busunit = returnValue.data.sample.producer;
                        if (busunit == null) busunit = returnValue.data.sample.product.producer;
                        lims.setEasyBusunitValue(busunit);
                        var addFormat = $("#listqsFormat").data("kendoDropDownList");
                        var qsnoFormat = addFormat.dataItem(0);
                        var qsNo = "";
                        if(busunit != null){
                            qsnoFormat = busunit.qsNoAndFormatVo == null ? addFormat.dataItem(0) : busunit.qsNoAndFormatVo.licenceFormat;
                            qsNo = busunit.qsNoAndFormatVo == null ? "": busunit.qsNoAndFormatVo.qsNo;
                        }
                        addFormat.value(qsnoFormat.id); // 给选择qs号的下来列表选择默认值
                        root.formetType = qsnoFormat.formetType;
                        root.formetLength = qsnoFormat.formetLength;
                        root.formetValue = qsnoFormat.formetValue;
                        var qsEditflag = returnValue.data.sample.producer!=null?returnValue.data.sample.producer.bindQsFlag:false;
                        //判断是否包括有省得简称
                        if(qsnoFormat.formetValue.indexOf("(?)")!=-1){
                            $("#jianchengList").show();
                            root.showJianCheng = true;
                            var first = "";//省的简称
                            var socend = qsNo.substring(0,2);//中间字母
                            var third = qsNo.substr(2);//数字部分
                            if(qsNo.indexOf("(")!=-1 && qsNo.indexOf(")")!=-1 ){
                            	root.setStyle(0);
                                first = qsNo.charAt(1);//省的简称
                            	socend = qsnoFormat.formetValue.replace("(?)","");//中间字母
                            	third = qsNo.substr(5);//数字部分
                            }
                            $("#proJianCheng").data("kendoDropDownList").text(first);
                            $("#showQsFormat").html(socend);
                            $("#bus_qsNo").val(third);
                        }else{
                        	root.setStyle(1);
                            $("#jianchengList").hide();
                            root.showJianCheng = false;
                            $("#showQsFormat").html(qsnoFormat.formetValue);
                            $("#bus_qsNo").val(qsNo.replace(qsnoFormat.formetValue, ""));
                        }
                        //判断是否是被绑定过
                        if(qsEditflag){
                            $("#bus_qsNo").attr("readonly",true);
                            $("#proJianCheng").attr("readonly",true);
                            addFormat.readonly(true);
                        }else{
                            $("#bus_qsNo").attr("readonly",false);
                            $("#proJianCheng").attr("readonly",false);
                            addFormat.readonly(false);
                        }

                        if (busunit != null && busunit.id != null) {
                            root.initBusName = busunit.name;
                        }
                        lims.setEasyReportValue(returnValue.data, false, true);
                    }
                    //分类赋值
                    //一级分类
                    $("#category1").data("kendoComboBox").value(returnValue.data.sample.product.category.category.code.substring(0, 2));
                    $.ajax({
                        url: portal.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode=" + returnValue.data.sample.product.category.category.code.substring(0, 2),
                        type: "GET",
                        dataType: "json",
                        async: false,
                        success: function(value) {
							 fsn.endTime = new Date();
                            if (value.result.status == "true") {
                                $("#category2").data("kendoComboBox").setDataSource(value.data);
                                $("#category2").data("kendoComboBox").value(returnValue.data.sample.product.category.category.id);
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
                    //二级分类
                    $.ajax({
                        url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + returnValue.data.sample.product.category.category.id + "?categoryFlag=" + true,
                        type: "GET",
                        dataType: "json",
                        async: false,
                        success: function(value) {
							 fsn.endTime = new Date();
                            if (value.result.status == "true") {
                                $("#category3").data("kendoComboBox").setDataSource(value.data);
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
                    //三级分类
                    $("#category3").data("kendoComboBox").value(returnValue.data.sample.product.category.id);
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

    root.validateExpiration = function() {
        /* 1. 验证年份  */
        var proExpirYear = $("#proExpirYear").val();
        if (!proExpirYear.match(/^[0-9]*$/)) {
            lims.initNotificationMes("【保质期】的年份应该是数字！", false);
            return;
        } else {
            lims.countExpirday();
            // lims.initNotificationMes("输入年份正确",true);
        }
        /* 2. 验证月份  */
        var proExpirMonth = $("#proExpirMonth").val();
        if (!proExpirMonth.match(/^[0-9]*$/)) {
            lims.initNotificationMes("【保质期】的月份应该是数字！", false);
            return;
        } else {
            lims.countExpirday();
            // lims.initNotificationMes("输入月份正确",true);
        }
        /* 3. 验证天数 */
        var proExpirDay = $("#proExpirDay").val();
        if (!proExpirDay.match(/^[0-9]*$/)) {
            lims.initNotificationMes("【保质期】的天数应该是数字！", false);
            return;
        } else {
            lims.countExpirday();
            //lims.initNotificationMes("输入日正确",true);
            //lims.msg("success_msg", true, "");
        }
    };

    /* 对"检查项目"grid 里的 无效数据的过滤 */
    root.getItems = function() {
        var items = [];
        var testItems = $("#report_grid").data("kendoGrid").dataSource.data();
        var j = 0;
        for (var i = 0; i < testItems.length; i++) {
            if (testItems[i].name.trim() == "" || testItems[i].result.trim() == "") {
                j++;
                continue;
            }
            items[i - j] = {
                id: testItems[i].id == null ? "": testItems[i].id,
                name: testItems[i].name,
                unit: testItems[i].unit,
                techIndicator: testItems[i].techIndicator,
                result: testItems[i].result,
                assessment: testItems[i].assessment,
                standard: testItems[i].standard
            };
        }
        return items;
    };

    //将指定id的input标签设置为只读
    fsn.setInputToReadOnly = function(listFormId) {
        $(".editReportStyle").css("display", "inline");
        for (var i = 0; i < listFormId.length; i++) {
            $("#" + listFormId[i]).attr("readonly", "readonly");
        }
    };

    /**
	  * 特殊符号选择窗口
	  */
    root.openSpeCharWin = function() {
        $("#speCharWindow").data("kendoWindow").open().center();
    };

    /*验证产品是否绑定了QS证号*/
    root.validateQsNo = function() {
        var barcode = $("#barcodeId").val().trim();
        var busName = root.initBusName == null ? "": root.initBusName;
        if (barcode.length < 1 || busName.length < 1) {
            return;
        }
        $.ajax({
            url: portal.HTTP_PREFIX + "/product/getPro2BusByBarcodeAndBusName/" + barcode + "/" + busName,
            type: "GET",
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function(returnValue) {
                fsn.endTime = new Date();
                if (returnValue.result.status == "true") {
                    if (returnValue.pro2Bus != null) {
                        var bus = returnValue.pro2Bus.businessUnit;
                        bus.qsNo = returnValue.pro2Bus.productionLicense.qsNo;
                        if (returnValue.pro2Bus.barcord != null && returnValue.pro2Bus.barcord.length > 0) {
                            bus.bindQsFlag = true;
                        }
                        lims.setEasyBusunitValue(bus);
                    }
                } else {
                    lims.initNotificationMes('验证产品是否绑定Qs证号时出现异常！', false);
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

    // 温馨提示样式控制
    root.changeGuidStyle = function(isShow) {
        if (isShow) {
            $("#skin-box-bd").css("display", "block");
        } else {
            $("#skin-box-bd").css("display", "none");
        }
    };

    root.buileAnimate = function buileAnimate(target, widthP) {
        $(target).animate({
            right: "0px",
            opacity: '1',
            width: widthP
        });
    };

    root.closeClearReportWin = function() {
        $("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
    };

    root.openClearReportWin = function() {
        $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
    };

    //当页面滚动到距离顶部200像素时，在右下角显示返回顶部按钮。
    window.onscroll = function() {
        var top = document.documentElement.scrollTop == 0 ? document.body.scrollTop: document.documentElement.scrollTop;
        if (top > 200) {
            $(".gTop a").css("display", "block");
        } else {
            $(".gTop a").css("display", "none");
        }
    };

    root.getCurrentBusiness = function() {
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/getCurrentBusiness",
            type: "GET",
            async: false,
            success: function(data) {
                fsn.endTime = new Date();
                if (data.status) {
                    root.enterpriseName = data.result.name;
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
    //执行标准赋值
    root.setRegularityValue = function() {
	     /*刷新执行标准datasource*/
		$.ajax({
			url:portal.HTTP_PREFIX + "/testReport/searchLastCategory/"+$("#category2").data("kendoComboBox").value()+"?categoryFlag="+false,
			type:"GET",
			dataType:"json",
			async:false,
			success:function(value){
				if(value.result.status == "true"){
					$("#regularityInfo").data("kendoComboBox").setDataSource(value.data);
					if(value.data==null||value.data.length<=0){
						lims.initNotificationMes("该二级分类下没有执行标准，请先新增执行标准！", false);
					}
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
		//将是执行标准格式化添加到已选择的执行标准框里
		var text = $("#foodinfo_standard").val().trim();
		var regularity = text.split(";");
		var ul = document.getElementById("regularityList");
		var ds = $("#regularityInfo").data("kendoComboBox").dataSource.data();
		//将执行标准格式化添加到已选择的执行标准框中
		for (var i = 0; i < regularity.length - 1; i++) {
			var li = document.createElement("li");
			li.innerHTML = "<a herf='#' title='" + regularity[i] + "'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>" + regularity[i] + ";</a>";
			ul.appendChild(li);
		}
		//将执行标准datasource进行筛选
		for(var j=0;j<ds.length;j++){
			if(text.indexOf(ds[j].name)!=-1){
				$("#regularityInfo").data("kendoComboBox").dataSource.remove(ds[j]);
				j=j-1;
			}
		};
    }
    //验证该标准是否已经选择
    root.validaRegularity = function(text) {
        var ul = document.getElementById("regularityList");
        var li = ul.getElementsByTagName("li");
        if (li.length >= 1) {
            for (var i = 0; i < li.length; i++) {
                var value = li[i].innerHTML;
                if (value.length > value.replace(text, "").length) {
                    lims.initNotificationMes(text + "该标准已经被选择，不能再次选择！", false);
                    return false;
                }
            }
        }
        return true;
    }
    //移除执行标准
    delRegularity = function(e) {
		var name = e.parentNode.title;
		$("#regularityInfo").data("kendoComboBox").dataSource.add({ name:name});
        e.parentNode.parentNode.parentNode.removeChild(e.parentNode.parentNode)
    }

    //新增执行标准
    root.addRegularity = function() {
        //空值校验
        if ($("#regularity_type").val().replace(/[ ]/g, "") == "") {
            lims.initNotificationMes("请填写标准类型！", false);
            return;
        }
        if ($("#regularity_year").val().replace(/[ ]/g, "") == "") {
            lims.initNotificationMes("请填写执行序号及年份！", false);
            return;
        }
      //添加执行标准时屏蔽标准名称以为必填
       /* if ($("#regularity_name").val().replace(/[ ]/g, "") == "") {
            lims.initNotificationMes("请填写标准名称！", false);
            return;
        }*/
        //如果超过12，则不允许添加
        var ul = document.getElementById("regularityList");
        var count = ul.getElementsByTagName("li").length;
        if (count >= 12) {
            lims.initNotificationMes("执行标准过多，不能再添加！", false);
            return;
        }
        //将新增标准格式化后添加到列表中
        var text = $("#regularity_type").val().replace(/[ ]/g, "") + " " + $("#regularity_year").val().replace(/[ ]/g, "") + " " + $("#regularity_name").val();
        if (!root.validaRegularity(text)) {
            return;
        }
        var li = document.createElement("li");
        li.innerHTML = "<a herf='#' title='" + text + "'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>" + text + ";</a>";
        ul.appendChild(li);
        //清空信息
        $("#regularity_type").val("");
        $("#regularity_year").val("");
        $("#regularity_name").val("");
    };

	root.searchRegularity = function(){
		 var index = $("#category2").data("kendoComboBox").select();
        if (index == -1) {
            lims.initNotificationMes("请先选择食品分类！", false);
            return;
        } else {
            var index = $("#category2").data("kendoComboBox").select();
            if (index == -1) {
                lims.initNotificationMes("请先选择二级分类后再新增执行标准！", false);
                return;
            }
            //清空信息
            $("#regularity_type").val("");
            $("#regularity_year").val("");
            $("#regularity_name").val("");
            $("#regularityInfo").data("kendoComboBox").value("");
            $("#regularityList").text("");

            //执行标准赋值
            root.setRegularityValue();
            $("#addRegularity_window").data("kendoWindow").open().center();
        }
	};

    // 封装执行标准信息
    root.getRegularity = function() {
        var listRegularity = [];
        var text = $("#foodinfo_standard").val().trim();
        var regularitys = text.split(";");
        for (var i = 0; i < regularitys.length - 1; i++) {
            listRegularity[i] = {
                name: regularitys[i],
                category: {
                    id: $("#category2").data("kendoComboBox").value(),
                }
            };
        }
        return listRegularity;
    };

    /* 初始化页面信息，请确保该方法始终在当前页代码的最后一行 */
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
      try {
          //点击报告更新申请处理后传过来的参数 (产品条形码)
          var _barcode = $.cookie("product_barcode");
          $.cookie("product_barcode", JSON.stringify({}), {
              path: '/'
          });
      } catch(e) {}
      root.initialize();
      if(root.updateApply==true){
          root.autoloadingPageInfoByBarcode(_barcode);
          $("#barcodeId").attr("disabled",true);
          $("#tri_testType").data("kendoDropDownList").value(root.updateReportType);
      }else{
          root.updateApply==false;
      }
  });