$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
    var store = window.fsn.store = window.fsn.store || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var STORE_COLUMNS = null;
	var STORE_DETAIL_COLUMNS = null;
	var STORE_DS = [];
    var STORE_DETAIL_DS = [];
    var STORE_GRID = null;
    var STORE_DETAIL_GRID = null;
    var ADD_STORE = null;
    var STORE_DETAIL = null;
    var CONFIRM_ADD = null;
    var QS_LIST = null;
    var BAR_LIST = null;
    var STACK_ID = null;
    var isNew = false;

        function initialize(){
            fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');
        ADD_STORE = initKendoWindow("ADD_STORE_PRODUCT","500px", "430px", "编辑/预览商品库存", false,true,false,["Close"],null,"");
        STORE_DETAIL = initKendoWindow("STORE_DETAIL_WIN","1100px", "380px", "库存明细", false,true,false,["Close"],null,"");
        CONFIRM_ADD = initKendoWindow("CONFIRM_ADD_WIN","480px", "120px", "确定添加库存", false,true,false,["Close"],null,"");
        QS_LIST = initAutoComplete();
        BAR_LIST = initAutoCompBar();
        /* 初始化条形码的下拉列表 */
        initAutoCompBarDS();
        QS_LIST.readonly(true);
        buildGridWioutToolBar("store_product_grid",STORE_COLUMNS,getStoreDS(""),"300");
        STORE_GRID = $("#store_product_grid").data("kendoGrid");
        buildGridWioutToolBar("store_detail_grid",STORE_DETAIL_COLUMNS,STORE_DETAIL_DS,"300");
        STORE_DETAIL_GRID = $("#store_detail_grid").data("kendoGrid");
        $("#product_num").kendoNumericTextBox({
            format: "0"
        });
        $("#product_barcode").bind("blur",getProductByBarcode);
        /* 初始化退货商信息 */
    };

    function initAutoComplete() {
        $("#qs_no").kendoDropDownList({
            dataSource: [],
            index: 0
        });
        return $("#qs_no").data("kendoDropDownList");
    };
    function initAutoCompBar() {
        $("#product_barcode").kendoAutoComplete({
            dataSource: [],
            filter: "startswith",
            placeholder: "请输入条形码"
        });
        return $("#product_barcode").data("kendoAutoComplete");
    };
    function initAutoCompBarDS(){
        $.ajax({
            url: HTTPPREFIX + "/product/getAllBarCode",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(value){
                if (value.result.status == "true") {
                    var DS = value.data!= null ? value.data : [];
                    BAR_LIST.setDataSource(DS);

                }else{
                    fsn.initNotificationMes("条形码数据加载失败");
                }
            }
        });
    }

    /* 添加库存信息 */
    store.addStoreProduct = function(){
    	isNew = true;
        STACK_ID = null;
        $("#product_barcode").val("");
        $("#product_name").val("");
        QS_LIST.text("");
        $("#product_format").val("");
        $("#product_num").data("kendoNumericTextBox").value("");

        $("#product_barcode").attr("readonly",false);
        $("#product_num").data("kendoNumericTextBox").readonly(false);
        $("#yesStoreProduct").show();
        ADD_STORE.open().center();
    };

    store.queryStoreProduct = function(){
        var condition = $("#query_condition").val().trim();
        STORE_GRID.setDataSource(getStoreDS(condition));
        STORE_GRID.refresh();
    };
    store.noyStoreProduct = function(){
        CONFIRM_ADD.close();
    };

    store.closeDetailWin = function(){
        STORE_DETAIL.close();
    };

    store.addProduct = function(ops){
    	if(ops === "close"){
    		 QS_LIST.readonly(true);
             ADD_STORE.close();
             return;
    	}
    	if(isNew){//新增时验证
    		/* 验证信息是否填写完整 */
    		if(!validate()){
    			return ;
    		}
    	}
    	var pcount = $("#product_num").val().trim();
    	if(pcount===null || pcount===""){
    		pcount = 0;
    	}
    	var tag = '<span>' +
    	'确定要添加数量为<label style="color: red;">'+"<strong> "+ pcount +" </strong>"+'</label> '+
    	'条形码为<label style="color: red;">'+"<strong> "+ $("#product_barcode").val().trim() +" </strong>"+'</label>的产品吗? '+
    	'</span>';
    	if(STACK_ID!=null){
    		tag = '<span>' +
    		'确定要将该库存数量手动修改为<label style="color: red;">'+"<strong> "+ pcount +" </strong>"+'</label>吗? '+
    		'</span>';
    	}
    	$("#confirm_add_msg").html(tag);
    	CONFIRM_ADD.open().center();
    };


    store.yesStoreProduct = function(){
        $("#k_window").data("kendoWindow").open().center();
        var barcode = $("#product_barcode").val().trim();
        var qs = $("#qs_no").data("kendoDropDownList").text();
        var num = $("#product_num").val().trim()!=null?$("#product_num").val().trim():0;
        var stackId = STACK_ID!=null?STACK_ID:-1;
        $.ajax({
            url: HTTPPREFIX + "/account/store/addstore/"+barcode+"?qs="+qs+"&num="+num+"&stackid="+stackId,
            type: "GET",
            dataType: "json",
            async: true,
            success: function(value){
                $("#k_window").data("kendoWindow").close();
                if (value.result.status == "true") {
                    if(value.success){
                        fsn.initNotificationMes("信息提交成功!",true);
                        isNew = false;
                        STORE_GRID.dataSource.read();
                        CONFIRM_ADD.close();
                        ADD_STORE.close();
                    }else{
                        fsn.initNotificationMes("信息提交失败!",false);
                    }
                }else{
                    fsn.initNotificationMes(value.result.message,false);
                }
            }
        });
    };

    function viewStore(item,type){
        if(item){
            $("#product_barcode").attr("readonly",true);
            $("#product_barcode").val(item.barcode);
            $("#product_name").val(item.name);
            QS_LIST.text(item.qsNumber);
            $("#product_format").val(item.format);

            if("modify" === type){
            	isNew = false;
                $("#product_num").data("kendoNumericTextBox").readonly(false);
                $("#product_num").data("kendoNumericTextBox").value(item.count);
                $("#yesStoreProduct").show();
                STACK_ID = item.id;

            }else if("view"===type){
                $("#product_num").data("kendoNumericTextBox").readonly(true);
                $("#product_num").data("kendoNumericTextBox").value(item.count);
                $("#yesStoreProduct").hide();
                STACK_ID = null;
            }
            ADD_STORE.open().center();
        }
    }

    /* 预览库存详情信息 */
    function edit(item){

        if(item.id!=null && item.id!=""){
            var DS = storeDetailDS(item.id);
            STORE_DETAIL_GRID.setDataSource(DS);
            STORE_DETAIL_GRID.refresh();
            STORE_DETAIL.open().center();
        }
    }
    /* 验证条形码是否已存在库存中 */
    function getProductByBarcode(){
        var barcode = $("#product_barcode").val().trim();
        if(barcode=="") return;
        $.ajax({
            url: HTTPPREFIX + "/account/store/getProduct/?barcode="+barcode,
            type: "GET",
            dataType: "json",
            async: true,
            success: function(value){
                if (value.result.status == "true") {
                    var product = value.data;
                    if(product!=null){
                        $("#product_barcode").val(product.barcode);
                        $("#product_name").val(product.name);
                        //$("#qs_no").val(product.qsNumber);
                        $("#product_format").val(product.format);
                        QS_LIST.readonly(false);
                        QS_LIST.setDataSource(
                            new kendo.data.DataSource({
                                data:product.qsNoList!=null?product.qsNoList:[]
                            })
                        );
                    }else{
                        fsn.initNotificationMes(fsn.l("该条码对应的产品信息不存在，请先完善产品信息!"), false);
                        return;
                    }
                }
            }
        });
    }

    function validateBarcode(barcode){
        var qs = QS_LIST.text();
        var isExist = false;
            $.ajax({
            url: HTTPPREFIX + "/account/store/validatebarcode/"+barcode+"?qs="+qs,
            type: "GET",
            dataType: "json",
            async: false,
            success: function(value){
                if (value.result.status == "true") {
                    isExist = value.isExist;
                }
            }
        });
        return isExist;
    }

    /* 验证用户填写的信息是否合法 */
    function validate(){
        /* 验证条形码 */
        var barcode = $("#product_barcode").val().trim();
        if(barcode!=""){
//            var barcodeRel = /^[0-9]*$/;
//            if(!barcode.match(barcodeRel)){
//                fsn.initNotificationMes(fsn.l("条形码只能为数字!"), false);
//                return false;
//            }
            if(validateBarcode(barcode)){
                fsn.initNotificationMes(fsn.l("库存中已存在该条形码对应的产品信息!"), false);
                return false;
            }
        }else{
            fsn.initNotificationMes("条形码不能为空!",false);
            return false;
        }
        return true;
    }

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
            editable: false,
            height:height,
            width: "100%",
            sortable: true,
            resizable: true,
            selectable: false,
            pageable: {
                refresh: true,
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });
    };

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

    STORE_COLUMNS = [
        {field: "barcode",title: fsn.l("商品条码"),width: 60,filterable: false},
        {field: "name",title: fsn.l("商品名称"),width: 60,filterable: false},
        {field: "qsNumber",title: fsn.l("QS号"),width: 60,filterable: false},
        {field: "format",title: fsn.l("规格"),width: 40,filterable: false},
        {field: "count",title: fsn.l("数量"),width: 40,filterable: false},
        {
            command: [
                {
                    name: "modify",
                    text: lims.localized("修改"),
                    click: function (e) {
                        e.preventDefault();
                        var editRow = $(e.target).closest("tr");
                        var temp = this.dataItem(editRow);
                        viewStore(temp,"modify");
                    }
                },{
                    name: "view",
                    text: lims.localized("查看"),
                    click: function (e) {
                        e.preventDefault();
                        var editRow = $(e.target).closest("tr");
                        var temp = this.dataItem(editRow);
                        viewStore(temp,"view");
                    }},{
                name: "edit",
                text: lims.localized("明细"),
                click: function (e) {
                    e.preventDefault();
                    var editRow = $(e.target).closest("tr");
                    var temp = this.dataItem(editRow);
                    edit(temp);
                }
            }],
            title: fsn.l("操作"),
            width: 90
        }
    ];
    STORE_DETAIL_COLUMNS = [
        {field: "barcode",title: fsn.l("商品条码"),width: 100,filterable: false},
        {field: "name",title: fsn.l("商品名称"),width: 120,filterable: false},
        {field: "qsNumber",title: fsn.l("QS号"),width: 100,filterable: false},
        {field: "batch",title: fsn.l("生产批号"),width: 60,filterable: false},
        {field: "overDate",title: fsn.l("交易日期"),width: 60,filterable: false},
        {field: "count",title: fsn.l("数量"),width: 50,filterable: false,
            template: function(item){
                var str = "";
                if(item.type==1&&item.count>0){
                    str = "-";
                }
                return str+item.count;
            }
        },{field: "createType",title: fsn.l("台帐类型"),width: 70,filterable: false,
            template: function(item){
                var str = "";
                if(item.createType==="0"){
                    str = "流水库存";
                }else if(item.createType==="1"){
                    str = "台账库存";
                }else if(item.createType==="2"){
                    str = "用户手动修改库存";
                }
                return str;
            }
        },
    ];
    function getStoreDS(condition){
        STORE_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/store/loadStoreInfoList/" + options.page + "/" + options.pageSize+"?condition="+condition;
                    },
                    dataType : "json",
                    contentType : "application/json"
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
        return STORE_DS;
    }
    function storeDetailDS(sid){
        STORE_DETAIL_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/store/loadStoreDetailList/" + options.page + "/" + options.pageSize+"/"+sid;
                    },
                    dataType : "json",
                    contentType : "application/json"
                }
            },
            batch : true,
            page:1,
            pageSize: 5,
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
        return STORE_DETAIL_DS;
    }


	initialize();
});