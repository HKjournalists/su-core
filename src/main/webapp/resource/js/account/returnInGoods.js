$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var relation = window.fsn.relation = window.fsn.relation || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
    var PRODUCT_DETAIL_GRID = null;
    var PRODUCT_DETAIL_COLUMNS = null;
    var PRODUCT_DETAIL_DS = [];
    var busUnit = {id:null};
    var accountOutId = null;
    var CONFIRM_IN = null;
    try {
        var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        var id = arrayParam[0]; // 产品id（原始id，未被编码）
        var orig_idmd5 = arrayParam[1]; // 产品id(被编码过的产品id)
        accountOutId = verificationMD5(id,orig_idmd5);
    } catch (e) {}
    
    function initialize(){
        $("#submit").show();
        CONFIRM_IN = initKendoWindow("CONFIRM_IN","350px", "120px", "退货收货确认", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("prodcut_detail",PRODUCT_DETAIL_COLUMNS,[],"300");
        PRODUCT_DETAIL_GRID = $("#prodcut_detail").data("kendoGrid");
        /* 编辑过来的时候初始化页面数据 */
        initPageData(accountOutId);
        /* 初始化退货商信息 */
        initMyBusUnitInfo();
    };

    function initPageData(id){
        if(id!=null&&id!=""){
            $.ajax({
                url: HTTPPREFIX + "/account/relation/initIn/"+id,
                type: "GET",
                dataType: "json",
                async:false,
                success: function(returnValue){
                    var bus = returnValue.data;
                    if(returnValue.data!=null){
                        busUnit.id = bus.busAccountId;
                        $("#return_time").html(returnValue.data.outTime);
                        if(bus.busRelationVO!=null){
                            $("#return_busname").html(bus.busRelationVO.busName);
                            $("#return_lic").html(bus.busRelationVO.licNo);
                        }
                        if(bus.returnProductVOList!=null&&bus.returnProductVOList.length > 0){
                            var barcodes = "";
                            for(var i = 0; i < bus.returnProductVOList.length; i++){
                                PRODUCT_DETAIL_GRID.dataSource.add(bus.returnProductVOList[i]);
                                if(barcodes!=""){
                                    barcodes += bus.returnProductVOList[i].barcode+",";
                                }else{
                                    barcodes += bus.returnProductVOList[i].barcode+",";
                                }
                                var count = barcodes.split(",");
                                if((count.length-1)%3==0){
                                    barcodes+="<br/>";
                                }
                            }
                            var lastindex = barcodes.lastIndexOf(",");
                            barcodes = barcodes.substring(0,lastindex);
                            var tag = '<label title="'+barcodes+'" width="300px;" autosize=false>'+barcodes+'</label>';
                            $("#add_barcode").html(tag);
                        }
                        if(bus.inStatus==1){
                            $("#submit").hide();
                           // fsn.initNotificationMes("该订单已经提交不能修改！", false);
                        }
                    }else {
                        fsn.initNotificationMes("订单编号有误，请从退后台账预览进入。", false);
                        return ;
                    }
                }
            });
        }
    }

    function initMyBusUnitInfo(){
        $.ajax({
            url: HTTPPREFIX + "/account/relation/basicInfo",
            type: "GET",
            dataType: "json",
            async:false,
            success: function(returnValue){
                var bus = returnValue.basicInfo;
                if(bus){
                    $("#return_busUnit").html(bus.busName);
                    $("#return_busUnit_lic").html(bus.licNo);
                    $("#return_busUnit_contact").html(bus.contact);
                    $("#return_busUnit_address").html(bus.address);
                }
            }
        });
    }

    function verificationMD5(str1,md5code){
        var md5 = $.md5(str1);
        if(md5===md5code){
            return str1;
        }else{
            return "";
        }
    };

    /**
     * 提交页面信息,确认收货
     */
    $("#submit").click(function(){
        if(accountOutId==null){
            return ;
        }
        CONFIRM_IN.open().center();
    });

    relation.yesIn = function(status){
        CONFIRM_IN.close();
        if(!status){
            return ;
        }
        $.ajax({
            url: HTTPPREFIX + "/account/store/receiptIn",
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(accountOutId),
            success: function (returnVal) {
                if (returnVal.result.status == "true") {
                    if(returnVal.save==true){
                        if(returnVal.saveBus!=null){
                            busUnit.id = returnVal.saveBus.id;
                            PRODUCT_DETAIL_GRID.setDataSource(getProductDetailDS(returnVal.saveBus.id));
                        }

                        fsn.initNotificationMes("确认成功", true);
                        window.location.href="returnInGoodsManage.html"
                    }
                } else {
                    fsn.initNotificationMes("企业信息修改失败", false);
                }
            }
        });
    }
    /* 返回 */
    relation.goBack = function(){
        window.location.href="returnInGoodsManage.html"
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
            editable: true,
            height:height,
            sortable: true,
            pageable: {
                refresh: true,
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });
    };


    PRODUCT_DETAIL_COLUMNS = [ {field: "id",title: fsn.l("Id"),width: 1,filterable: false},
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
                var tag = "<label>"+ value +"</label>";
                return tag;
            }
        },
        {width: 90, title: fsn.l("Name"),
            template: function (e) {
                var tag = "<label>"+ e.name+"</label>";
                return tag;
            }
        },
        {width: 80, title: fsn.l("规格型号"),
            template: function (e) {
                var tag = "<label>"+ e.format+"</label>";
                return tag;
            }
        },
        {title: fsn.l("退货数量"),filterable: false,width: 50,
            template: function (e) {
                var tag = "<label>"+ e.returnCount+"</label>";
                return tag;
            }
        },
        {title: fsn.l("单价"),filterable: false,width: 50,
            template: function (e) {
                var tag = "<label>"+ e.price+"</label>";
                return tag;
            }
        },
        {width: 60, title: fsn.l("生产日期"),
            template: function (e) {
                var productionDate = e.productionDate;
                if(e.productionDate==null) productionDate="";
                var tag = "<label id='batch"+ e.productId+"'>"+ productionDate +"</label>";
                return tag;
            }
        },
        {width: 60, title: fsn.l("批次"),
            template: function (e) {
                var batch = e.batch;
                if(e.batch==null) batch="";
                var tag = "<label id='batch"+ e.batch+"'>"+ batch +"</label>";
                return tag;
            }
        },
        {width: 60, title: fsn.l("过期日期"),
            template: function (e) {
                var tag = "<label id='overDate"+e.productId+"'>"+ fsn.formatGridDate(e.overDate)+"</label>";
                return tag;
            }
        }
    ];

    function getProductDetailDS(outId){
        PRODUCT_DETAIL_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/relation/loadDetail/" + options.page + "/" + options.pageSize+"?outId="+outId;
                    },
                    dataType : "json",
                    contentType : "application/json"
                },
                parameterMap : function(options, operation) {
                    if (operation == "read") {
                        if($("#topDetail").is(':checked')){
                            document.getElementById("topDetail").checked = false;
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
        return PRODUCT_DETAIL_DS;
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
	initialize();
});