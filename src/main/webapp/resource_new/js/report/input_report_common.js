var lims = window.lims = window.lims || {}; // 全局命名空间
var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var root = window.lims.root = window.lims.root || {}; // root命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
portal.type = false;
/**
 * 产品的原产国
 * 当为null或者"中国"时 为国产食品
 * @author LongXianzhen 2015/7/9
 */
root.productCountry = null;

/**
 * 初始化控件
 * @author ZhangHui 2015/4/30
 */
root.initComponent = function(){
	/* 初始化提示框window */
    root.initWidow();
    /* 公共Click事件绑定 */
    root.bindClick_common();
    /* 流通企业报告录入界面Click事件绑定 */
    root.bindClick_circulate();
    /* 初始化执行标准、产品分类 */
    portal.initialCategorys();
    /* 恢复图片合成pdf按钮功能 */
    root.removeDisabledToBtn(["openT2P_btn"]);
    /* 初始化公共下拉选择控件 */
    root.initAutoComplete_common();
    /* 初始化qs号相关控件 */
    root.initQsComponent();
	/* 初始化省份简称下拉控件 */
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
        dataSource: [],
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
	$("#proJianCheng").blur(function() {
		var dataSource = root.loadProJianCheng();
		 $("#proJianCheng").data("kendoDropDownList").setDataSource(dataSource);
	});
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
    portal.business = getCurrentBusiness();
    if(portal.business!=undefined && portal.business!=null && portal.business.type.trim().indexOf("流通企业.商超")!=-1){
        portal.type = true;
    }
    
    var dataSource =[];
    	if(portal.type){
    	   dataSource = [{text: "企业自检",value: "企业自检"},
			    		 {text: "企业送检",value: "企业送检"},
			    		 {text: "政府抽检",value: "政府抽检"}];
    	}else{
		   dataSource = [{text: "企业自检",value: "企业自检"},
		    		  {text: "企业送检",value: "企业送检"},
		    		  {text: "政府抽检",value: "政府抽检"}];
       }

    $("#tri_testType").kendoDropDownList({
        dataTextField: "text",
        dataValueField: "value",
        dataSource: dataSource,
        filter: "contains",
        suggest: true,
        index: 0,
        change: function(e) {
		    var value = this.value();
		   	if(value=='第三方检测'){
		   		$(".third-upload").show();
		   	}else{
		   		$(".third-upload").hide();
		   	}
		},
		dataBound:function(e){
			var value = this.value();
		   	if(value=='第三方检测'){
		   		$(".third-upload").show();
		   	}else{
		   		$(".third-upload").hide();
		   	}
		}
    });
};

root.initAutoComplete_common = function() {
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

/**
 * Click事件绑定
 * @author ZhangHui 2015/4/30
 */
root.bindClick_circulate = function(){
	/* 点击执行标准，弹出执行标准选择框 */
	$("#regularityDiv").click(portal.searchRegularity);
	/* 执行标准保存按钮 */
	$("#btn_regularity_save").click(function(){
		$("#regularity").val($("#regularityList").text());
		$("#addRegularity_window").data("kendoWindow").close();
	});
	/* 执行标准添加按钮 */
	$("#btn_regularity_add").click(function(){
	 	portal.addRegularity();
	});
	/* 执行标准选择取消按钮 */
	$("#btn_regularity_cancel").click(function(){
		 $("#addRegularity_window").data("kendoWindow").close();
	});
	
	$("#btn_saveOK").click(function() {
        $("#saveWindow").data("kendoWindow").close();
        root.submit_report();
    });

    $("#btn_saveCancel").click(function() {
        $("#saveWindow").data("kendoWindow").close();
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
//    	$("#speCharWindow td a").click(function() {
    $("#windbox td a").click(function() {
    	
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
        $("#report_grid").data("kendoGrid").table.focus();
    });
};

/**
 * 初始化本页面需要使用的window
 * @author ZhangHui 2015/4/30
 */
root.initWidow = function() {
	fsn.initKendoWindow("CONFIRM_COMMON_WIN","友情提示","300px","150px",false,null);
	
    lims.initKendoWindow("toSaveWindow", "保存状态", "500px", "", false, true, false);
    lims.initKendoWindow("tsWindow", "警告", "400px", "", false, true, false);
    lims.initKendoWindow("saveWindow", "警告", "400px", "", false, true, false);
    lims.initKendoWindow("delItemsWindow", "警告", "400px", "", false, true, false);
    lims.initKendoWindow("import_Excel_Win", "导入检测项目", "500px", "", false, true, false);
//    lims.initKendoWindow("speCharWindow", "特殊符号", "450px", "300px", false, true, false);
    lims.initKendoWindow("pictureToPdfWindow", "图片转换Pdf", "840px", "450px", false, true, false);
    lims.initKendoWindow("addRegularity_window", "新增执行标准", "800px", "400px", false, true, false);
  
	$("#speCharWindow").kendoWindow({
	    width:"450px",
	    height:"320px",
		visible:false,
		title:"特殊符号",
		modal: true,
		resizable:false,
		actions:["Close"]
	});
	
	
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

root.onSelectUnitName = function(e) {
    var unitName = this.dataItem(e.item.index());
    $("#foodinfo_minunit").val(unitName);
};

root.onSelectBrandName = function(e) {
    var brandName = this.dataItem(e.item.index());
    $("#foodinfo_brand").val(brandName);
};

/**
 * kendo必填项校验
 */
root.validateComponent = function(){
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
};

/**
 * 选中下拉框中的条形码事件
 * @author ZhangHui 2015/4/14
 */
root.onSelectBarcode = function(e){
    root.initBarcode = this.dataItem(e.item.index());
    $("#barcodeId").val(root.initBarcode);
    root.judgeProductByBarcode(root.initBarcode);
};

/**
 * 检测报告grid初始化
 * @author ZhangHui 2015/4/30
 */
portal.initTestPropertyGrid = function(dataSource){

		 var grid = $("#report_grid").data("kendoGrid");
//		 var dataSource = root.gridDataSource();
		 if(!grid){
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
	        $("#report_grid").kendoGrid({
	            dataSource: dataSource,
	            navigatable: true,
	            editable: true,
                filterable: true,
                sortable: true,
                pageable: {
    	            refresh: true,
    	            messages: fsn.gridPageMessage(),
    	        },
	            toolbar: [{
	                template: kendo.template($("#toolbar_template").html())
	            }],
	            columns: [
	            {
//	                field: "rowNumber",
	                title: "序号",
	                template: "<span class='row-number'></span>",
	                editable: false,
	                width: 20
	            },{
	                fild: "id",
	                hidden:true
	            },
	            {
	                field: "temporaryFlag",
	                hidden:true
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
	            {command:[{
					      name: "up",
					      text: "<span class='k-icon k-add'></span>" + lims.l("上移"),
					      click: function(e) {
					        e.preventDefault();
					        root.delItem = this.dataItem($(e.currentTarget).closest("tr"));
					        root.upSelect(e,root.delItem);
					    }
						},{
						    name: "down",
						    text: "<span class='k-icon k-add'></span>" + lims.l("下移"),
						    click: function(e) {
						        e.preventDefault();
						        root.delItem = this.dataItem($(e.currentTarget).closest("tr"));
						        root.downSelect(e,root.delItem);
						    }
						},
	                      {
	                    name: "Remove",
	                    text: "<span class='k-icon k-cancel'></span>" + lims.l("Delete"),
	                    click: function(e) {
	                        e.preventDefault();
	                        root.delItem = this.dataItem($(e.currentTarget).closest("tr"));
//	                        if (root.delItem.id != null && root.delItem.id !='') {
//	                        	root.removeTestProperty(root.delItem);
//	                        }
	                        $("#report_grid").data("kendoGrid").dataSource.remove(root.delItem);
	                    }
	                }
	                ],
	                title: lims.l("Operation"),
	                width: 80
	            }],
	            dataBound: function () {
	                var rows = this.items();
	                $(rows).each(function () {
	                    var index = $(this).index() + 1;
	                    var rowLabel = $(this).find(".row-number");
	                    $(rowLabel).html(index);
	                });
	            }
	        }).data("kendoGrid");
			 $(document.body).keydown(function(e) {
                if (e.keyCode==38||e.keyCode==40) {
						$("#report_grid").data("kendoGrid").table.focus();
				}
             });
		 }else{
			 var dataSources =  $("#report_grid").data("kendoGrid").dataSource.data();
			grid.setDataSource(dataSources);
			grid.refresh();
		} 
		 
//    }
		 
		 
		 

};



root.removeTestProperty = function(data){
	$.ajax({
		url:portal.HTTP_PREFIX + "/tempReport/"+data.id+"/"+data.temporaryFlag,
		type: "DELETE",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(data){
        	if(data.status==true||data.status=="true"){
        		 lims.initNotificationMes('删除成功！', true);
        	}else{
        		lims.initNotificationMes('删除失败！', false);
        		
        	}
        },
	    error:function(e){
	    	 lims.initNotificationMes('系统异常！', false);
	    }
	})
}
//root.gridDataSource = function(){
//	var barcode = $("#barcodeId").val().trim()
//	var orderNo = $("#tri_reportNo").val().trim();
//	var ds; 
//		if(orderNo!=undefined&&orderNo!=''){
//			orderNo = encodeURI(encodeURIComponent(orderNo));
//			ds = new kendo.data.DataSource({
//			transport: {
//		        read: {
//		        	type:"GET",
//		        	url : function(options){
//		        		return portal.HTTP_PREFIX + "/tempReport/getItemList/" + options.page + "/" + options.pageSize+ "/"+barcode+"/" + orderNo;
//		        	},
//		            dataType : "json",
//		            contentType : "application/json"
//		        }
//		    },
//		    data:[],
//		    batch : true,
//		    page:1,
//		    pageSize: 5,
//		    schema: {
//		        data : function(data) {
//		            return data.itemList;
//		        },
//		        total : function(data) {       
//		            return data.counts;
//		        }     
//		    },
//		    serverPaging : true,
//		    serverFiltering : true,
//		    serverSorting : true
//		});
//	}else{
//		ds = new kendo.data.DataSource({data:[]});
//	}
//	return ds;
//};
/**
 * 根据条形码自动加载页面信息
 * @author Zhanghui 2015/4/7
 */
root.autoloadingPageInfoByBarcode = function(barcode){
	if(barcode.trim() == ""){
		return;
	}
	var isExist = false;
    $.ajax({
        url: portal.HTTP_PREFIX + "/testReport/getReportByBarcode/" + barcode,
        type: "GET",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(returnValue){
            fsn.endTime = new Date();
            if (returnValue.result.status == "true") {
            	isExist = returnValue.isExist;
            	var report_vo = returnValue.data;
                /* 报告赋值 */
                if(root.isNew){
                	 // 新增时：直接加载此产品的最近一条报告信息
                	 report_vo.repAttachments = new Array();
                	 fsn.setReportValue(report_vo, root.product_html_type);
                }else{
                	 // 编辑时：只需加载产品信息 + 验证qs号是否正确
                	 setProductValue(report_vo.product_vo);
                	 
                	 // 生产企业下拉选择
	               	 if(report_vo.listOfBusunitName!=null && report_vo.listOfBusunitName.length>0){
	               		 root.listOfBusunitName = report_vo.listOfBusunitName;
	               		 root.pro2Bus = report_vo.pro2Bus;
	               		 $(".busnameSelectDiv").show();
	               		 $("#hid_listBusName").data("kendoComboBox").setDataSource(report_vo.listOfBusunitName);
	               		 
	               		 var bus = root.getBusOfpro2BusByName($("#bus_name").val().trim());
	               		 if(bus != null){
	               			setBusunitValue(bus);
	               		 }
	               	 }else{
	               		 if(root.current_bus_vo_has_claim!=null && !root.current_bus_vo_has_claim.can_edit_qs){
	               			 $(".editQsStyle span").html("注意：当前生产企业已经通过注册，此处不允许再修改企业基本信息。");
	               			 var qsNoFormat = $("#listqsFormat").data("kendoDropDownList");
	               			 if(qsNoFormat){
	               				 qsNoFormat.readonly(false);
	               				 $("#proJianCheng").data("kendoDropDownList").readonly(false);
	               			 }
	               		 }
	               	 }
                }
                /**
                 * 如果该食品是进口食品时取到该食品的原产国
                 * @author LongXianzhen 2015/7/9
                 */
                if(report_vo.product_vo.importedProduct!=null){
                	root.productCountry=report_vo.product_vo.importedProduct.country.name;
                }else{
                	root.productCountry=null;
                }
                // 强制清空pdf
                setRepAttachments(new Array());
                setSanAttachments(new Array());
                removeDisabledToBtn(["upload_report_files", "openT2P_btn","openS2P_btn"]);
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
    return isExist;
};

/**
 * 温馨提示样式控制
 * @author ZhangHui 2015/4/3
 */
root.changeGuidStyle = function(isShow){
    if (isShow) {
        $("#skin-box-bd").css("display", "block");
    } else {
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
 * 清除页面暂存信息
 * @author ZhangHui 2015/4/3
 */
root.clearTempReport = function() {
    $.ajax({
        url: portal.HTTP_PREFIX + "/tempReport/clearTempReport",
        type: "DELETE",
        dataType: "json",
        async: false,
        success: function(data) {
            fsn.endTime = new Date();
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
 * 增加一行检测项目
 */
root.grid_AddItem = function(){
	
	var sumCount = $("#sumCount").val();
	if(sumCount==undefined||sumCount==null||sumCount==''){
		sumCount = 1;
	}
	for ( var i = 0; i < sumCount; i++) {
		$("#report_grid").data("kendoGrid").dataSource.add({
			name : "",
			unit : "",
			techIndicator : "",
			result : "",
			assessment : "合格",
			standard : ""
		});
	}
	 $("#sumCount").val('');
};

/**
 * 验证报告编号是否唯一
 */
root.validateReportNO = function(){
    var unique = true;
    var reportNo = $("#tri_reportNo").val().trim().replace(/\/+/g, "\\0gan0\\");
    
    var url = portal.HTTP_PREFIX + "/testReport/validatReportNo?report_no=" + reportNo + "&barcode=" + $("#barcodeId").val().trim() +
					"&batch_no=" + $("#tri_batchNo").val().trim();
    
    if(root.edit_id){
    	url += "&edit_report_id=" + root.edit_id;
    }
    
    $.ajax({
        url: url,
        type: "PUT",
        dataType: "json",
        async: false,
        //data: JSON.stringify(_data),
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
 * 公共绑定Click事件
 * @author ZhangHui 2015/4/30
 */
root.bindClick_common = function(){
	
	/**
     * 定义产品条形码失去焦点事件
     * @author ZhangHui 2015/4/30
     */
	//=====================开始=======================
//	$("#barcodeId").blur(function() {
//		if(!root.isNew){
//			return;
//		}
//		/* 将选择销往企业控件隐藏 */
//    	var barcode = $("#barcodeId").val().trim();
//    	root.initBarcode = barcode;
//		root.judgeProductByBarcode(barcode);
//    });
	//=====================结束=======================
	/* 图片合成pdf弹出框的 取消按钮 */
	//$("#p2pCancel_btn").bind("click", lims.closePicToPdfWin);
	/* 清空报告按钮 */
	$("#btn_clearRepFiles").bind("click", root.clearRepFiles);
    $("#btn_clearRepFiles").hide();
    /* 清空产品按钮 */
    $("#btn_clearProFiles").bind("click", root.clearProFiles);
    $("#btn_clearProFiles").hide();
    /* 提交按钮 */
    $("#submit").bind("click", root.submit);
    /* 更新按钮 */
    $("#update").bind("click", root.submit);
    /* 继续新增下一条报告按钮 */
    $("#add").bind("click", root.addNext);
    /* 保存按钮 */
    $("#save").bind("click", fsn.save);
    /* 清空按钮 */
    $("#clear").bind("click", root.openClearReportWin);
    /* 强制掉线按钮 */
    $("#logout_btn").bind("click", fsn.logoutRefresh);
    $("#save_btn_01").bind("click", root.remind);
    $("#save_btn_02").bind("click", root.remind);
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
    /* 是否自动生成报告单选框，选择[是]选项的Click事件 */
    $("#yesAuto").click(function(){
        $("#tr_isAuto").hide();
        root.isAutoReport = true;
    });
    /* 是否自动生成报告单选框，选择[否]选项的Click事件 */
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
    /**
     * 定义生产企业名称失去焦点事件
     * @author ZhangHui 2015/4/14
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
        
        if(!bus){
        	bus = {
        		name: $("#bus_name").val().trim(),
        	};
        }
        
        setBusunitValue(bus);
    });
    
    /**
     * 定义营业执照号失去焦点事件
     * @author ZhangHui 2015/4/30
     */
    $("#bus_licenseNo").blur(function() {
    	var licenseNo = $("#bus_licenseNo").val().trim();
		if(licenseNo != ""){
			var VlicnNo = /^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{13}$|^[A-Za-z0-9]{18}$/.test(licenseNo);
	        if (!VlicnNo) {
	            $("#bus_licenseNo").attr("style", "width:300px;border:2px solid red;");
	            lims.initNotificationMes(lims.l("Business license number is not in the correct format, must be composed of letters or numbers 13 , 15 or 18.") + "!", false);
	            return;
	        }
		}
		$("#bus_licenseNo").attr("style", "");
    });
    
    /**
     * 定义生产许可证失去焦点事件
     * @author ZhangHui 2015/4/30
     */
    $("#bus_qsNo").blur(root.validateQsNoFormat);
};

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
	if (!bus) {
        /* 企业名称不是可选生产企业 */
        bus = root.getBusUnitByName(root.initBusName);
    }
	setBusunitValue(bus);
};

/**
 * 根据用户输入的生产企业营业执照号自动加载企业信息
 */
root.onSelectBusUnitLicenseNo = function(e) {
    var busLicenseNo = this.dataItem(e.item.index());
    $.ajax({
        url: portal.HTTP_PREFIX + "/business/getBusUnitOfReportVOByLicenseNo/" + busLicenseNo + "?barcode=" + $("#barcodeId").val().trim(),
        type: "GET",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(returnValue) {
            fsn.endTime = new Date();
            if (returnValue.result.status == "true") {
            	setBusunitValue(returnValue.data);
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
 * 功能描述：根据企业名称查找企业信息，并根据当前产品条码判断该企业有无绑定qs号
 * @author ZhangHui 2015/6/4
 */
root.getBusUnitByName = function(name) {
    var busUnit = null;
    $.ajax({
        url: portal.HTTP_PREFIX + "/business/getBusUnitOfReportVOByBusname/" + name + "?barcode=" + $("#barcodeId").val().trim(),
        type: "GET",
        async: false,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(returnValue) {
            fsn.endTime = new Date();
            if (returnValue.result.status == "true") {
                busUnit = returnValue.data;
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

/**
 * 初始化qs号相关控件
 */
root.initQsComponent = function(){
	var qsFormat = $("#listqsFormat").data("kendoDropDownList");
	if(!qsFormat){
		return;
	}
	
    var item = qsFormat.dataItem();
    root.formatId = item.id;
    root.formetType = item.formetType;  // qs号数字之间的分隔符
    root.formetLength = item.formetLength; // qs号数字的长度
    root.formetValue = item.formetValue; // qs号字母部分
    if(item.formetValue.indexOf("(?)")!=-1){
    	root.setStyle(0);
        root.showJianCheng = true;
    }else{root.setStyle(1);}
	$("#jianchengList").hide();
    $("#showQsFormat").html(root.formetValue.replace("(?)",""));
    var getJianCheng = $("#proJianCheng").data("kendoDropDownList");
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
    setBusunitValue(bus);
};

/**
 * 根据生产企业名称，从可选生产企业中获取一条生产企业信息
 * @author ZhangHui 2015/4/30
 */
root.getBusOfpro2BusByName = function(name) {
    if (name == null || name.length < 1 || root.pro2Bus == null || root.pro2Bus.length < 1) {
        return null;
    }
    for (var i = 0; i < root.pro2Bus.length; i++) {
    	var bus_vo = root.pro2Bus[i];
        if (name == bus_vo.name) {
            return bus_vo;
        }
    }
};

/**
 * 功能描述：验证是否绑定有QS号,以及qs号的及时性校验
 * @author ZhangHui 2015/6/4
 */
root.validate_hasBideQs = function(barcode){
	 var success = true;
	 var bussunitName = $("#bus_name").val().trim();
	 var now_qs = $("#showQsFormat").html().trim()+$("#bus_qsNo").val().trim();
	 
	 /* 4代表生产许可证中包含省份简称的格式 */
	 if(root.formatId == 4){
		 var jiancheng = $("#proJianCheng").data("kendoDropDownList").text();
		 now_qs = "(" + jiancheng + ")" + now_qs;
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
						lims.initNotificationMes("该产品的QS号已被生产企业更改。系统已为您自动更新为" + qsNo + "。确认无误后，请重新点击提交！",false);
					}
				}
			}
	 });
	 return success;
};

/**
 * 根据条形码查找产品id
 * @author ZhangHui 2015/4/14
 */
root.queryProductIdByBarcode = function(barcode){
	/*条形码为空不进行判断*/
	if(barcode==""){
		return;
	}
	var productId = null;
 	$.ajax({
         url: fsn.getHttpPrefix() + "/product/query/getProductId/" + barcode,
         type: "GET",
         dataType: "json",
         async: false,
         success: function(returnValue) {
             if (returnValue.result.status == "true") {
             	productId = returnValue.data;
             }
         }
     });
 	 return productId; 
 };
 
/**
 * 根据条形码查找产品id
 * @author ZhangHui 2015/4/14
 */
root.countInitialProduct = function(productId){
 	var count = "";
  	$.ajax({
          url: fsn.getHttpPrefix() + "/erp/initializeProduct/countInitialProduct/" + productId,
          type: "GET",
          dataType: "json",
          async: false,
          success: function(returnValue) {
              if (returnValue.result.status == "true") {
            	  count = returnValue.counts;
              }
          }
      });
  	 return count; 
};

/**
 * 恢复按钮
 * @author ZhangHui 2015/4/30
 */
root.removeDisabledToBtn = function(btnIds){
	 if(btnIds==null || btnIds.length<1) return;
	 for(var i=0;i<btnIds.length;i++){
		 $("#"+btnIds[i]).parent().removeClass("k-state-disabled");
	  		 document.getElementById(btnIds[i]).disabled = false;
	 }
};

/**
 * 温馨提示样式控制
 * @author ZhangHui 2015/4/30
 */
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

/**
 * 可选生产企业的选择事件
 * @author ZhangHui 2015/4/13
 */
root.onSelectHideBusName = function(e) {
    root.initBusName = this.value();
    var bus = root.getBusOfpro2BusByName(root.initBusName);
    if (bus == null) {
        return;
    }
    /* 企业基本信息赋值 */
    setBusunitValue(bus);
    /* qs号赋值 */
    if(bus.qsNoAndFormatVo && bus.qsNoAndFormatVo.licenceFormat){
    	root.setQs(bus.qsNoAndFormatVo.licenceFormat.id, bus.qsNoAndFormatVo.qsNo);	
    }
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


/*打开导入检测项目的窗口*/
root.openExel_Win = function() {
    $("#up_ex_fileDiv").html("<input id='up_ex_file' type='file' />");
    root.buildUpload("up_ex_file", null, "ex_msg", "items");
    $("#import_Excel_Win").data("kendoWindow").open().center();
};
root.doEnterPress = function(event){
	if(event.keyCode==13){
		root.grid_AddItem();
	} 
}
/**
 * 导出excel文档
 */
root.exportExel_Win = function(isStructured){
	var data = $("#report_grid").data("kendoGrid").dataSource.data();
	 if(data.length>0){
		var orderNo = $("#tri_reportNo").val().trim();
		var barcodeId = $("#barcodeId").val().trim();
		var testResultId = data[0].testResultId;
		if(testResultId == undefined||testResultId==null){
			testResultId = null;
		}
		$.ajax({
			url:portal.HTTP_PREFIX +"/tempReport/exportExcel",
			type:"GET",
			dataType:"json",
			contentType: "application/json; charset=utf-8",
			data:{"orderNo":orderNo,"barcode":barcodeId,"isStructured":isStructured,"testResultId":testResultId},
			success:function(data){
				if(data.status=="true"){
					var hiddenIFrameID = 'hiddenDownloader',
		            iframe = document.getElementById(hiddenIFrameID);
		            if(iframe === null) {
		                iframe = document.createElement('iframe');
		                iframe.id = hiddenIFrameID;
		                iframe.style.display = 'none';
		                document.body.appendChild(iframe);
		            }
		            iframe.src = portal.HTTP_PREFIX +"/tempReport/downLoadExcel";
				}else{
					 lims.initNotificationMes(lims.l("您还没有保存该检测项目或者系统异常!") + "!", false);
				}
			}
		});
	 }else{
		 lims.initNotificationMes(lims.l("目前没有检测项目!") + "!", false); 
	 }
};
/**
 * 特殊符号选择窗口
 */
root.openSpeCharWin = function() {
   $("#speCharWindow").data("kendoWindow").open().center();
};

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


/**
 * 暂存功能
 */
fsn.save = function() {
//	 $("#save_status").html("正在保存，请稍候...");
//     $("#saving_msg").html("正在保存，请稍候...");
//     $("#toSaveWindow").data("kendoWindow").open().center();
//
//     var report_vo = root.createInstance();
//     $.ajax({
//         url: portal.HTTP_PREFIX + "/testReport/" + root.updateApply+"?save=false",
//         type:  "POST",
//         dataType: "json",
//         //async:false,
//         timeout: 600000,
//         //10min
//         contentType: "application/json; charset=utf-8",
//         data: JSON.stringify(report_vo),
//         success: function(returnValue) {
//             fsn.endTime = new Date();
//             $("#save_status").html("");
//             $("#toSaveWindow").data("kendoWindow").close();
//             
//             if (returnValue.result.status == "true") {
//             	var report_vo = returnValue.data;
//             	
//             	lims.initNotificationMes('保存成功', true);
//             	
//             	// 清空缓存信息
//             	root.clearTempReport();
//             	
//             	// 将检测项目发回至KMS
//             	fsn.sendItemsToKMS();
//                 
//                 root.isNew = false;
//                 root.ReportNo_old = report_vo.serviceOrder;
//                 root.edit_id = report_vo.id;
//                 $("ul.k-upload-files").remove();
//                 
////                 $("#report_grid").data("kendoGrid").dataSource.data(report_vo.testProperties == null ? [] : report_vo.testProperties);
//                 
//                 /**
//                  * 当从结构化页面，跳转至本页面时，提交陈功后，页面按钮显示无需改变
//                  * @author ZhangHui 2015/5/7
//                  */
//                 if(root.fromPage == "manage_report_perfect_shianyun"){
//                 	return;
//                 }
//                 
//                 $("#save").hide();
//                 $("#clear").hide();
//                 $("#submit").show();
//                 $("#update").show();
//                 $("#add").show();
//                 
//                 // 产品图片和报告pdf页面赋值
//                 setProAttachments(report_vo.product_vo.proAttachments);
//                 if (!report_vo.autoReportFlag) {
//                     setRepAttachments(report_vo.repAttachments);
//                 }
//
//                 //二级分类
//                 $.ajax({
//                     url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + $("#category2").data("kendoComboBox").value() + "?categoryFlag=" + true,
//                     type: "GET",
//                     dataType: "json",
//                     async: false,
//                     success: function(value) {
//							fsn.endTime = new Date();
//                         if (value.result.status == "true") {
//                             $("#category3").data("kendoComboBox").setDataSource("");
//                             $("#category3").data("kendoComboBox").setDataSource(value.data);
//                             if (report_vo.product_vo.category.id) {
//                                 $("#category3").data("kendoComboBox").value(report_vo.product_vo.category.id);
//                             }
//                         }
//                     },
//						error: function(e) {
//			                if (e.status == 911) {
//			                    lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
//			                } else {
//			                    lims.initNotificationMes('系统异常！', false);
//			                }
//			            }
//                 });
//             } else {
//                 lims.initNotificationMes((root.isNew ? lims.l("Add") : lims.l("Update")) + '失败！原因为：' + returnValue.result.errorMessage, false);
//             }
//             
//             // 报告编辑状态下，以下字段不允许编辑：产品条形码、企业名称、qs号
//             addInputReadOnly(["barcodeId","bus_name","bus_qsNo"]);
//             $(".qs_format_div").hide();
// 	        $("#proJianCheng").data("kendoDropDownList").readonly(true);
// 	        hideListBusNameSelect();
// 	        $("#barcodeId").unbind("blur");
//         },
//         error: function(e) {
//             $("#saving_msg").html("");
//             $("#toSaveWindow").data("kendoWindow").close();
//             if (e.status == 911) {
//                 lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
//             } else {
//                 lims.initNotificationMes('系统异常！', false);
//             }
//         }
//     });
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

//    var testday = $("#tri_testDate").val();
//    if (!testday.match(re1) && testday.trim() != "") {
//        $("#tri_testDate").val("");
//    }
//	
    $("#tri_testDate_format").data("kendoDatePicker").value($("#tri_testDate").val());
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
    
    var report_vo = root.createInstance();
    report_vo.product_vo.regularityStr = $("#regularity").val().trim();  // 执行标准字符串
    
    var inputDay =$("#inputDay").val().trim();//保质日期
     report_vo.product_vo.productionDateStr = $("#tri_proDate").val().trim(); // 生产日期字符串
    //日期过期验证
    var flag = root.setTriPorDate(report_vo.product_vo.productionDateStr,inputDay);
    if(!flag){
    	return;
    }
    $("#saving_msg").html("正在保存，请稍候...");
    $("#toSaveWindow").data("kendoWindow").open().center();
    $.ajax({
        url: portal.HTTP_PREFIX + "/tempReport/saveTempReport",
        type: "POST",
        dataType: "json",
        async: false,
        timeout: 600000,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(report_vo),
        success: function(returnValue) {
            fsn.endTime = new Date();
            $("#saving_msg").html("");
            $("#toSaveWindow").data("kendoWindow").open().close();
            
            if (returnValue.result.status == "true") {
                lims.initNotificationMes('保存成功！', true);
                /*保存成功后刷新页面*/
//                portal.initTestPropertyGrid();
//                $("#report_grid").data("kendoGrid").dataSource.data(returnValue.data.testProperties);
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

root.setTriPorDate = function(value,inputDay){
	if(value==null||value==''){
		return false;
	}
	var proDate = value.replace(/-/g,"/");
	var myDate = new Date();
	var myYear = myDate.getFullYear();
	var myMonth = myDate.getMonth()+1;
	var myDay = myDate.getDate();
	
	var sd = new Date(proDate);
	var ed = new Date(myYear+"/"+myMonth+"/"+myDay);
	
	var yearNum = ed.getYear() - sd.getYear();
	var monthNum = ed.getMonth()- sd.getMonth();
	var monn = ((yearNum*12+monthNum)*30+ed.getDate()-sd.getDate()+1);
	if(inputDay!=''&&monn > inputDay){
		$("#saving_msg").html("");
		 $("#toSaveWindow").data("kendoWindow").open().close();
		 fsn.initNotificationMes("已过期，不能保存,更新或提交操作!", false);
		 return false;
	} 
	return true;
}


/**
 * Qs号格式校验
 */
root.validateQsNoFormat = function(){
	/**
     * qs号为非必填
     */
    var qsNo = ($("#bus_qsNo").val().trim()).replace(/\s*/g, "").replace(/-/g, "").replace(/\s+/g, "");
    if(qsNo != ""){
        var status = /^[0-9]*$/.test(qsNo);
        if (!status) {
            fsn.initNotificationMes("【生产企业信息】中的生产许可证号格式不正确！。", false);
            return false;
        }
        if (qsNo.length != root.formetLength) {
            fsn.initNotificationMes("【生产企业信息】中的生产许可证号格式不正确！。", false);
            return false;
        }
    }
	
    return true;
};

/**
 * 营业执照号格式校验
 */
root.validateLicenNoFormat = function(){
	var licenNo = $("#bus_licenseNo").val().trim();
    if (licenNo != "") {
    	var VlicnNo = /^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{13}$|^[A-Za-z0-9]{18}$/.test(licenNo);
        if (!VlicnNo) {
            $("#bus_licenseNo").attr("style", "width:300px;border:2px solid red;");
            lims.initNotificationMes(lims.l("Business license number is not in the correct format, must be composed of letters or numbers 13 or 15.") + "!", false);
            return false;
        }
    }
    return true;
};

/**
 * 功能描述：去除检测项目中项目名称为空的项
 * @author ZhangHui 2015/6/19
 */
root.removePropertiesOfEmpty = function(grid_id){
	var data = $("#" + grid_id).data("kendoGrid").dataSource.data();
	var length = data.length;
	for(var i=(length-1); i>=0; i--){
		if(data[i].name.trim() == ""){
			data[i].id = null;
			$("#" + grid_id).data("kendoGrid").dataSource.remove(data[i]);
		}
	}
};