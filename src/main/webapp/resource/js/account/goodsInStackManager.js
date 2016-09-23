$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var intaked = window.fsn.intaked = window.fsn.intaked || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
    var PRODUCT_INTAKED_GRID = null;
    var PRODUCT_INTAKED_COLUMNS = null;
    function initialize(){
        buildGridWioutToolBar("prodcut_intaked",PRODUCT_INTAKED_COLUMNS,getProductDS("",""),"300");
        PRODUCT_INTAKED_GRID = $("#prodcut_intaked").data("kendoGrid");
    };

    intaked.queryAdd = function(type){
        if(type==="query"){
            var pname = $("#p_name").val().trim();
            var pbarcode = $("#p_barcode").val().trim();
            PRODUCT_INTAKED_GRID.setDataSource(getProductDS(pname,pbarcode));
        }else if(type==="add"){
            window.location.href = "goodsInStack.html";
        }
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
            selectable: true,
            pageable: {
                refresh: true,
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });
    };

    PRODUCT_INTAKED_COLUMNS = [
        {width: 50, title: fsn.l("产品名称"),
            template: function (e) {
                var tag = "<label title='"+ e.name +"'>"+ e.name +"</label>";
                return tag;
            }
        },
        {width: 90, title: fsn.l("商品条形码"),
            template: function (e) {
                var tag = "<label title='"+ e.barcode +"'>"+ e.barcode+"</label>";
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
        {width: 80, title: fsn.l("规格型号"),
            template: function (e) {
                var tag = "<label title='"+ e.format +"' >"+ e.format+"</label>";
                return tag;
            }
        },
        {width: 70,title: fsn.l("添加数量"),
            template: function (e) {
                var tag = "<label title='"+ e.count +"' >"+ e.count +"</label>";
                return tag;
            }
        },
        {title: fsn.l("产品批次"), width: 80,
            template: function (e) {
                var tag = "<label title='"+ e.batch +"' >"+ e.batch +"</label>";
                return tag;
            }
        },
        {width: 70, title: fsn.l("添加日期"),
            template: function (e) {
                var tag = "<label title='"+ e.overDate +"'>"+ e.overDate +"</label>";
                return tag;
            }
        }
    ];

    function getProductDS(name,bar){
        return new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/store/intaked/" + options.page + "/" + options.pageSize+ "?name=" +name+"&bar="+bar;
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
    }

	initialize();
});