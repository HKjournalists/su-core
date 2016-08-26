$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
    var addProduct = window.fsn.addProduct = window.fsn.addProduct || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
    var PRODUCT_COLUMNS = null;
    var PRODUCT_GRID = null;
    var PRODUCT_DETAIL_GRID = null;
    var PRODUCT_DETAIL_COLUMNS = null;
    var PRODUCT_WIN = null;
    var CONFIRM_SUBMIT = null;
    var busUnit = {id:null};
    var LOADING = null;

    function initialize(){
        $("#add_barcode").val("");
        PRODUCT_WIN = initKendoWindow("OPEN_PRODUCT_WIN","1000px", "460px", "选择商品", false,true,false,["Close"],null,"");
        CONFIRM_SUBMIT = initKendoWindow("CONFIRM_SUBMIT","350px", "120px", "确定提交", false,true,false,["Close"],null,"");
        LOADING = initKendoWindow("toSaveWindow","300px", "80px", "提示！", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("prodcut_detail",PRODUCT_DETAIL_COLUMNS,[],"300");
        PRODUCT_DETAIL_GRID = $("#prodcut_detail").data("kendoGrid");
        initMyBusUnitInfo();
    };

    function initMyBusUnitInfo(){
        $.ajax({
            url: HTTPPREFIX + "/account/relation/basicInfo",
            type: "GET",
            dataType: "json",
            async:false,
            success: function(returnValue){
                var bus = returnValue.basicInfo;
                if(bus){
                    $("#busUnit").val(bus.busName);
                    $("#busUnit_lic").val(bus.licNo);
                    $("#busUnit_contact").val(bus.contact);
                    $("#busUnit_address").val(bus.address);
                    busUnit.outBusId = bus.id;
                }
            }
        });
    }

    /**
     * 打开选择条形码弹窗
     * @HY
     */
    addProduct.openBarcode = function(){
        if(!PRODUCT_GRID){
            upload.buildGridWioutToolBar("product_grid",PRODUCT_COLUMNS,getProductDS("",""),"350");
            PRODUCT_GRID = $("#product_grid").data("kendoGrid");
            PRODUCT_GRID.bind("change", grid_change);
        }else{
            $("#pro_name_q").val("");
            $("#pro_bar_q").val("");
            PRODUCT_GRID.setDataSource(getProductDS("",""));
        }
        /* 清空勾选择的CheckBox */
        document.getElementById("topCheckBox").checked = false;
        var selects = document.getElementsByName("topCheckBox");
        if (selects != null && selects.length > 0) {
            for(var i = 0;i<selects.length;i++){
                if($(selects[i]).is(':checked')){
                    selects[i].checked = false;
                }
            }
        }
        PRODUCT_WIN.open().center();
    }

    addProduct.queryPro = function (id){
        if(id==="OPEN_PRODUCT_WIN"){
            var pname = $("#pro_name_q").val().trim();
            var pbar = $("#pro_bar_q").val().trim();
            var proDS = getProductDS(pname,pbar);
            PRODUCT_GRID.setDataSource(proDS);
        }
    }

    function getReturnProduct(tagName,listDS){
        var selects = document.getElementsByName(tagName);
        var itemArr = new Array();
        if (selects != null && selects.length > 0) {
            for(var i = 0;i<selects.length;i++){
                if($(selects[i]).is(':checked')){
	            	 var str = $(selects[i]).attr("id");
	            	 var pid = str.split("|")[0];
	                 var pqs = str.split("|")[1];
	                 var uuid= str.split("|")[2];
                    if(pid && pid !="" && listDS && listDS.length > 0){
                        for(var j = 0; j< listDS.length; j++){
                            if(parseInt(pid)===listDS[j].productId&&pqs==listDS[j].qsNumber&&uuid==listDS[j].uuid){
                                var temp = listDS[j];
                                itemArr.push(temp);
                            }
                        }
                    }
                }
            }
        }
        return itemArr;
    }

    addProduct.yesOrClose = function(id,status){
        /*1.添加产品到产品详情列表中 */
        if(id==="OPEN_PRODUCT_WIN"){
            if(status){
                addProductToDetail();
            }
        }
        if(id==="CONFIRM_SUBMIT"){
            if(status){
                yesSubmit();
            }
        }
        $("#"+id).data("kendoWindow").close();
    }

    /* 新增选择的一行 */
    addProduct.cpoySelectLine = function(){
        /* 判断是否选中一行！ */
        var selectRow = PRODUCT_DETAIL_GRID.select();
        var data = PRODUCT_DETAIL_GRID.dataItem(selectRow);
        if(data){
            var selectedIndex = selectRow[0].rowIndex;
        	var timestamp=new Date().getTime();//时间戳
        	var temp = {
        				"productId":data.productId,
        				"barcode":data.barcode,
        				"qsNumber":data.qsNumber,
        				"name":data.name,
        				"format":data.format,
        				"returnCount":0,
        				"count":data.count,
        				"price":0,
        				"birthDateList":data.birthDateList,
        				"productionDate":"",
        				"batch":"",
        				"overDate":"",
        				"busType":data.busType,
        				"uuid":timestamp
        			};
            PRODUCT_DETAIL_GRID.dataSource.insert(selectedIndex+1,temp);
        }else{
            fsn.initNotificationMes("请先选择一行!", false);
        }

    }


    /* 将用户选择的产品添加到产品详情里 */
    function addProductToDetail(){
        var listDS = PRODUCT_GRID.dataSource.data();
        var itemArr = getReturnProduct("topCheckBox",listDS);
        var list = PRODUCT_DETAIL_GRID.dataSource.data();
        var pdata = [];
        if(itemArr.length > 0){
            var bar = "";
            /* 合并数组到list中 */
            Array.prototype.push.apply(list, itemArr);
            /* 数组元素去重 */
            /*for(var i=0;i<list.length;i++){
            	for(var j=i+1;j<list.length;j++){
            		if(list[i].productId===list[j].productId&&list[i].qsNumber===list[j].qsNumber&&list[i].batch===list[j].batch){
            			list.splice(j,1);
            			j--;
            		}
                }
            }*/
            for(var i=0;i<list.length;i++){
            	 pdata.push(list[i]);
            	 bar +=list[i].barcode+",";
            }
            var DS =  new kendo.data.DataSource({
                data:pdata
            });
            PRODUCT_DETAIL_GRID.setDataSource(DS);
            defaultSelected("topDetail");
            $("#add_barcode").val(bar);
        }
    }

    /* 天加的产品详细中默认是勾选的 */
    function defaultSelected(id){
        var selects = document.getElementsByName(id);
        if (selects != null && selects.length > 0) {
            document.getElementById(id).checked = true;
            for(var i = 0;i<selects.length;i++){
                selects[i].checked = true;
            }
        }
    }

    /**
     * product全选/全不选
     */
    addProduct.selectAll = function (obj) {
        var isSelect = $(obj).is(':checked');
        var name = obj.id;
        var selects = document.getElementsByName(name);
        if (isSelect) {
            if (selects != null && selects.length > 0) {
                for(var i = 0;i<selects.length;i++){
                    selects[i].checked = true;
                }
            }
        }else if(!isSelect){
            if (selects != null && selects.length > 0) {
                for(var i = 0;i<selects.length;i++){
                    selects[i].checked = false;
                }
            }
        }
    }

    function grid_change(e){
        var selectedCells = PRODUCT_GRID.select();
        for(var i = 0; i < selectedCells.length; i++){
            var dataItem = PRODUCT_GRID.dataItem(selectedCells[i]);
            var id = dataItem.productId +'|'+ dataItem.qsNumber+'|'+ dataItem.uuid;
            var selected = document.getElementById(id);
            if($(selected).is(':checked')){
                selected.checked = false;
            }else{selected.checked = true;}
        }
    }
    /* 封装需要提交的信息 */
    function returnProductInfo(status){
        if(status=="submit"){
            busUnit.outStatus = 1;
        }else{
            busUnit.outStatus = 0;
        }
        var data = PRODUCT_DETAIL_GRID.dataSource.data();
        var pArr = getReturnProduct("topDetail",data);
        busUnit.proList = [];
        if(pArr!=null&&pArr.length>0){
            for(var x = 0 ; x< pArr.length;x++){
            	var qs = $.md5(pArr[x].qsNumber+pArr[x].uuid);
                pArr[x].overDate = $("#overDate"+pArr[x].productId+qs).html();
                pArr[x].batch = $("#batch"+pArr[x].productId+qs).html();
                busUnit.proList.push(pArr[x]);
            }
        }else{
            fsn.initNotificationMes("没有选择添加的产品",false);
            return false;
        }

        return true;

    }

    /* 提交数据 */
    function yesSubmit() {
        var data = busUnit;
        CONFIRM_SUBMIT.close();
        $("#saving_msg").html("正在提交数据，请稍候...");
        LOADING.open().center();
        $.ajax({
            url: HTTPPREFIX + "/account/store/selfAddStore",
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function (returnVal) {
                if (returnVal.result.status == "true") {
                    if(returnVal.add===true){
                    	LOADING.close();
                        fsn.initNotificationMes("添加商品库存成功", true);
                        window.location.href = "goodsInStackManager.html";
                    }else{
                    	LOADING.close();
                        fsn.initNotificationMes("添加商品库存失败", false);
                    }
                } else {
                	LOADING.close();
                    fsn.initNotificationMes("添加商品库存失败", false);
                }
            }
        });
    }

    /**
     * 提交页面信息
     */
    addProduct.submit = function(status){
        if(!returnProductInfo(status)){
            return;
        }
        if(status=="submit"){
            CONFIRM_SUBMIT.open().center();
        }
    }
    /* 返回 */
    addProduct.goBack = function(){
        window.location.href="goodsInStackManager.html";
    }

    function initKendoWindow(formId, width, heigth, title,
			visible, modal, resizable, actions, mesgLabelId, message){
		if(mesgLabelId != null) {
			$("#"+mesgLabelId).html(message);
		}
		$("#"+formId).kendoWindow({
			width:width,
			height:heigth,
	   		visible:visible,
	   		title:title,
	   		modal: modal,
	   		resizable:resizable,
	   		actions:actions
		});
		return $("#"+formId).data("kendoWindow");
	};

    function buildGridWioutToolBar(id,columns,ds, height){
        $("#" + id).kendoGrid({
            dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
            filterable: {
                extra: false,
                messages: fsn.gridfilterMessage(),
                operators: {
                    string: fsn.gridfilterOperators(),
                    date: fsn.gridfilterOperatorsDate(),
                    number: fsn.gridfilterOperatorsNumber()
                }
            },
            editable: true,
            height:height,
            sortable: true,
            selectable: true,
            pageable: {
                refresh: true,
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });
    };

    PRODUCT_COLUMNS = [ {field: "id",title: fsn.l("Id"),width: 1,filterable: false},
        { title: "<input type='checkbox' id='topCheckBox' onclick='return fsn.addProduct.selectAll(this)'/>选择", width: 30,
            template: function (e) {
                var tag = "<input name='topCheckBox' type='checkbox' id='"+  e.productId+'|'+e.qsNumber +'|'+e.uuid  +"' class='grid_checkbox'/>";
                return tag;
            }
        },
        {field: "name",title: fsn.l("Name"),width: 80,filterable: false},
        {field: "barcode",title: fsn.l("商品条形码"),width: 60,filterable: false},
        {field: "format",title: fsn.l("商品规格"),width: 60,filterable: false}
    ];

    PRODUCT_DETAIL_COLUMNS = [ {field: "id",title: fsn.l("Id"),width: 1,filterable: false},
        {width: 50, title: "<input type='checkbox' id='topDetail' onclick='return fsn.addProduct.selectAll(this)'/>选择",
            template: function (e) {
                var tag = "<input name='topDetail' type='checkbox' id='"+  e.productId+'|'+e.qsNumber +'|'+e.uuid  +"' class='grid_checkbox'/>";
                return tag;
            }
        },
        {width: 90, title: fsn.l("商品条形码"),
            template: function (e) {
                var tag = "<label>"+ e.barcode+"</label>";
                return tag;
            }
        },
        {width: 80, title: fsn.l("QS号"),
            template: function (e) {
                var value = e.qsNumber
                if(e.qsNumber===null){
                    value = "";
                }
                var tag = "<label title='" + value + "'>"+ value +"</label>";
                return tag;
            }
        },
        {width: 90, title: fsn.l("Name"),
            template: function (e) {
                var tag = "<label title='" + e.name + "'>"+ e.name+"</label>";
                return tag;
            }
        },
        {width: 80, title: fsn.l("规格型号"),
            template: function (e) {
                var tag = "<label>"+ e.format+"</label>";
                return tag;
            }
        },
        {field: "returnCount",title: fsn.l("添加数量"),filterable: false,editor:
            function(container, options) {
                var input = $("<input min='0' value='0' max='999999999' maxlength='9'/>");
                input.attr("name", options.field);
                input.appendTo(container);
                input.kendoNumericTextBox();
            },width: 70},
        {
            field: "productionDate", title: fsn.l("生产日期"), width: 80, filterable: false,
            editor:function(container, options) {
                var item = options.model;
                var tag = "<option selected='selected'>请选择...</option>";
                var list = item.birthDateList;
                if(list!=null && list.length > 0){
                    for(var x=0;x<list.length;x++){
                        tag += "<option id='"+ list[x].batch +"' title='"+ list[x].overDate +"' value='"+ list[x].instanceId +"'>"+list[x].birthDate+"</option>"
                    }
                }
                var selectTag = $("<select class='k-select k-textbox' style='width: 120px;' onchange='return fsn.addProduct.selectDate(this)'>"+ tag+"</select>");

                selectTag.appendTo(container);

                addProduct.selectDate = function(obj){
                    var index = obj.selectedIndex; // 选中索引
                    var overDate = obj.options[index].title;
                    var text = obj.options[index].text;
                    var batch = obj.options[index].id;
                    item.batch = batch;
                    item.overDate = overDate;
                    var uuid = options.model.uuid;
                    var qs = options.model.qsNumber;
                    item.batch = batch;
                    item.overDate = overDate;
                    qs = $.md5(qs==undefined?"":qs+uuid);
                    options.model.productionDate = text;
                    $("#batch"+options.model.productId+qs).html(batch);
                    $("#overDate"+options.model.productId+qs).html(overDate);
                }
            }
        },
        {width: 60, title: fsn.l("批次"),
            template: function (e) {
                var batch = e.batch;
                if(e.batch==null) batch="";
                var uuid = e.uuid;
                var qs = e.qsNumber;
                qs = $.md5(qs==undefined?"":qs+uuid);
                var tag = "<label id='batch"+ e.productId+qs+"'>"+ batch +"</label>";
                return tag;
            }
        },
        {width: 70, title: fsn.l("过期日期"),
            template: function (e) {
            	 var uuid = e.uuid;
                 var qs = e.qsNumber;
                 qs = $.md5(qs==undefined?"":qs+uuid);
                var tag = "<label id='overDate"+e.productId+qs+"'>"+ fsn.formatGridDate(e.overDate)+"</label>";
                return tag;
            }
        }
    ];

    function getProductDS(name,bar){
        return new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/store/selfproductlist/" + options.page + "/" + options.pageSize+ "?name=" +name+"&bar="+bar;
                    },
                    dataType : "json",
                    contentType : "application/json"
                },
                parameterMap : function(options, operation) {
                    if (operation == "read") {
                        if($("#topCheckBox").is(':checked')){
                            document.getElementById("topCheckBox").checked = false;
                        }
                    }
                }
            },
            batch : true,
            page:1,
            pageSize: 10,
            schema: {
                data : function(returnValue) {
                    return returnValue.data!=null?returnValue.data:[];
                },
                total : function(returnValue) {
                    return returnValue.totals!=null?returnValue.totals:0;
                }
            },
            serverPaging : true,
            serverFiltering : true,
            serverSorting : true
        });
    }

	initialize();
});