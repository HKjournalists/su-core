$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
    var relation = window.fsn.relation = window.fsn.relation || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
    var PRODUCT_DETAIL_GRID = null;
    var PRODUCT_DETAIL_COLUMNS = null;
    var PRODUCT_DETAIL_DS = [];
    var accountOutId = null;
    try {
        var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        var id = arrayParam[0]; // 产品id（原始id，未被编码）
        var orig_idmd5 = arrayParam[1]; // 产品id(被编码过的产品id)
        accountOutId = verificationMD5(id,orig_idmd5);
    } catch (e) {}
    
    function initialize(){
        buildGridWioutToolBar("prodcut_detail",PRODUCT_DETAIL_COLUMNS,getProductDetailDS(accountOutId),"300");
        PRODUCT_DETAIL_GRID = $("#prodcut_detail").data("kendoGrid");
        /* 编辑过来的时候初始化页面数据 */
        initPageData(accountOutId);
    };

    function initPageData(id){
        if(id!=null&&id!=""){
            $.ajax({
                url: HTTPPREFIX + "/account/relation/initpage/"+id,
                type: "GET",
                dataType: "json",
                async:false,
                success: function(returnValue){
                    var bus = returnValue.data;
                    if(returnValue.data!=null){
                        $("#return_time").val(returnValue.data.outTime);
                        if(bus.busRelationVO!=null){
                            $("#return_busname").val(bus.busRelationVO.busName);
                            $("#return_lic").val(bus.busRelationVO.licNo);
                        }
                        if(bus.outRelationVO!=null){
                            $("#return_busUnit").val(bus.outRelationVO.busName);
                            $("#return_busUnit_lic").val(bus.outRelationVO.licNo);
                            $("#return_busUnit_contact").val(bus.outRelationVO.contact);
                            $("#return_busUnit_address").val(bus.outRelationVO.address);
                        }
                        if(bus.returnProductVOList!=null&&bus.returnProductVOList.length > 0){
                            var barcodes = "";
                            for(var i = 0; i < bus.returnProductVOList.length; i++){
                                if(barcodes!=""){
                                    barcodes += ","+bus.returnProductVOList[i].barcode;
                                }else{
                                    barcodes += bus.returnProductVOList[i].barcode;
                                }
                            }
                            $("#add_barcode").val(barcodes);
                        }
                    }else {
                        fsn.initNotificationMes("订单编号有误，请从退后台账预览进入。", false);
                        return ;
                    }
                }
            });
        }
    }

    function verificationMD5(str1,md5code){
        var md5 = $.md5(str1);
        if(md5===md5code){
            return str1;
        }else{
            return "";
        }
    };

    /* 返回 */
    relation.goBack = function(){
        window.location.href="admin_returnGoodsManage.html"
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
            height:height,
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

    PRODUCT_DETAIL_COLUMNS = [ {field: "id",title: fsn.l("Id"),width:40,filterable: false},

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
        {title: fsn.l("库存数量"),filterable: false,width: 70,
            template: function (e) {
                var tag = "<label>"+ e.count+"</label>";
                return tag;
            }
        },
        {field: "returnCount",title: fsn.l("退货数量"),filterable: false,width: 50},
        {field: "price",title: fsn.l("单价"),width: 50,filterable: false},
        {field: "productionDate", title: fsn.l("生产日期"), width: 80, filterable: false},
        {width: 60, title: fsn.l("批次"),
            template: function (e) {
                var batch = e.batch;
                if(e.batch==null) batch="";
                var tag = "<label id='batch"+ e.productId+"' title='" + batch + "'>"+ batch +"</label>";
                return tag;
            }
        },
        {width: 70, title: fsn.l("过期日期"),
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
                        return HTTPPREFIX + "/account/relation/loadDetail/"+options.page+"/"+options.pageSize+"?outId="+outId;
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

	initialize();
});