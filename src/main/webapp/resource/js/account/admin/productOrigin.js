$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var origin = window.fsn.origin = window.fsn.origin || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
    var GOODS_DETAILS_COLUMNS = null;
    var GOODS_DETAILS_DS = null;
    var GOODS_ORIGIN_WIN = null;
    var GOODS_DETAILS_GRID = null;
    var BUS_TRIAL = null;
    var isORIGIN = false;

    function initialize(){
        GOODS_DETAILS_GRID = buildGridWioutToolBar("goods_details_grid",GOODS_DETAILS_COLUMNS,getGoodsDetailsDS(),"500");
        GOODS_ORIGIN_WIN = initKendoWindow("GOODS_ORIGIN_WIN","800px", "500px", "台账溯源", false,true,false,["Close"],null,"");
        BUS_TRIAL = initKendoWindow("BUS_TRIAL","450px", "270px", "台帐信息", false,true,false,["Close"],null,"");

    };

    origin.queryStoreProduct = function(){
        getGoodsDetailsDS();
        GOODS_DETAILS_GRID.dataSource.read();

    }

    function viewOrigin(item){

        if(item!=null&&item.barcode!=null) {
            $("#productName").html(item.productName);
            $("#barcode").val(item.barcode);
            $("#qs_no").html(item.qsNo);
            $("#format").html(item.format);
            if($("#goods_origin_grid").data("kendoTreeView")){
                $("#origin_left").html("");
                $("#origin_left").html('<div id="goods_origin_grid"></div>');
            }

            $("#goods_origin_grid").kendoTreeView({
                dataTextField: "showName",
                dataSource:getGoodsOriginDS(),
                select:onSelect
            });
            if(isORIGIN){
                GOODS_ORIGIN_WIN.open().center();
            }else{
                fsn.initNotificationMes("该产品没有溯源信息!",false);
            }
        }else{
            fsn.initNotificationMes("该产品没有溯源信息!",false);
        }
    }

    function getGoodsOriginDS(){
        var batch = $("#batch").val().trim();
        isORIGIN = false;
        var originDS = new kendo.data.HierarchicalDataSource({
            transport : {
                read : {
                    url : HTTPPREFIX + "/account/origin/" + $("#barcode").val().trim()+"?batch="+batch,
                    async:false,
                    dataType : "json"
                }
            },
            schema : {
                model : {
                    id : "id",
                    leafId : "leafId",
                    hasChildren : "hasChildren"
                },
                data : function(returnValue) {
                    if(returnValue!=null&&returnValue.length>0){
                        isORIGIN = true;
                        return returnValue;
                    }
                    return[];
                }
            }
        });
        return originDS;
    }

    /**
     * 根据产品条形吗和批次查询产品溯源
     */
    origin.queryProductOrigin = function(){
        var origin = getGoodsOriginDS();
        $("#goods_origin_grid").data("kendoTreeView").setDataSource(origin);
    }

    function onSelect(e){
        var dataItem = this.dataItem(this.findByText(this.text(e.node)));
        var index = this.dataSource.indexOf(dataItem);
        if(dataItem && index != 0){
            busTrailClear();
            BUS_TRIAL.open().center();
            $("#supplyBusName").html(dataItem.supplyBusName);
            $("#tradingBusName").html(dataItem.tradingBusName);
            $("#tradingNum").html(dataItem.tradingNum);
            $("#tradingType").html(dataItem.tradingType);
            $("#tradingbatch").html(dataItem.tradingbatch);
            $("#tradingDate").html(dataItem.tradingDate);
        }
    }

    origin.closeOriginWin = function(){
        GOODS_ORIGIN_WIN.close();
    }

    origin.closeTrialWin = function(){
        BUS_TRIAL.close();
    }

    function busTrailClear(){
        $("#productName").html("");
        $("#barcode").html("");
        $("#qs_no").html("");
        $("#format").html("");
        $("#supplyBusName").html("");
        $("#tradingBusName").html("");
        $("#tradingNum").html("");
        $("#tradingType").html("");
        $("#tradingbatch").html("");
        $("#tradingDate").html("");
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
            selectable: true,
            pageable: {
                refresh: true,
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });
        return $("#"+id).data("kendoGrid");
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

    GOODS_DETAILS_COLUMNS = [
        {field: "id",title: fsn.l("编号"),width: 15,filterable: false},
        {field: "productName",title: fsn.l("商品名称"),width: 70,filterable: false,
            template:
                function(dataItem){
                    var pname = dataItem.productName;
                    var tag = "<label title='"+pname+"'>"+pname+"</label>";
                    return tag;
                }
        },
        {field: "barcode",title: fsn.l("商品条码"),width: 60,filterable: false},
        {field: "qsNo",title: fsn.l("QS号"),width: 60,filterable: false},
        {field: "productBand",title: fsn.l("商标"),width: 40,filterable: false},
        {field: "place",title: fsn.l("产地"),width: 60,filterable: false,
            template:
                function(dataItem){
                    var place = dataItem.place;
                    var tag = "<label title='"+place+"'>"+place+"</label>";
                    return tag;
                }
        },
        {field: "format",title: fsn.l("规格"),width: 40,filterable: false},
        {
            command: [{
                name: "edit",
                text: fsn.localized("台账溯源"),
                click: function (e) {
                    e.preventDefault();
                    var editRow = $(e.target).closest("tr");
                    var temp = this.dataItem(editRow);
                    viewOrigin(temp);
                }
            },{
                    name: "check",
                    text: fsn.localized("产品查询"),
                    click: function (e) {
                        e.preventDefault();
                        var editRow = $(e.target).closest("tr");
                        var temp = this.dataItem(editRow);
                        var url = getHttpProtalPrefix()+"/fsn-portal/GYFDA/read.shtml?keyword="+temp.barcode;
                        window.open(url);

                    }
                }
            ],
            title: fsn.l("操作"),
            width: 70
        }
    ];

    function getGoodsDetailsDS(){
        GOODS_DETAILS_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/origin/goodsList/" + options.page + "/" + options.pageSize+"?condition="+ $("#query_condition").val().trim();
                    },
                    dataType : "json",
                    contentType : "application/json"
                }
            },
            batch : true,
            page:1,
            pageSize: 15,
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
        return GOODS_DETAILS_DS;
    }

    getHttpProtalPrefix = function(){
        var httpProtal = "";
        var hostname = window.location.hostname;
        switch(hostname){
            case "qaenterprise.fsnip.com":
                httpProtal ="http://qaportal.fsnip.com";
                break;
            case "stgenterprise.fsnip.com":
                httpProtal ="http://stgportal.fsnip.com";
                break;
            case "enterprise.fsnip.com":
                httpProtal ="http://www.fsnip.com";
                break;
            default://DEV
                //httpProtal ="http://www.fsnip.com";
                httpProtal ="http://qaportal.fsnip.com";
                break;
        }
        console.log("FSN Http Prefix:" + httpProtal);
        return httpProtal;
    };

	initialize();
});