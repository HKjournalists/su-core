$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
    var manage = window.fsn.manage = window.fsn.manage || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var ACCOUNT_COLUMNS = null;
	var ACCOUNT_DS = [];
    var ACCOUNT_GRID = null;
    var CONFIRM_DELETE = null;

    function initialize(){
        buildGridWioutToolBar("return_account_grid",ACCOUNT_COLUMNS,getAccountDS(""),"400");
        ACCOUNT_GRID = $("#return_account_grid").data("kendoGrid");
        /* 初始化退货商信息 */
    };

    /* 查询退货信息 */
    manage.queryBus = function(){
        var nameAndLic = $("#product_name_lic").val().trim();
        ACCOUNT_GRID.setDataSource(getAccountDS(nameAndLic));
        ACCOUNT_GRID.refresh();
    }

    /* 编辑退货信息 */
    manage.edit = function(id){
        var condition = "?"+id+"&"+$.md5(""+id);
        window.open("admin_returnGoods.html"+condition);
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

    ACCOUNT_COLUMNS = [
        {field: "id",title: fsn.l("台帐ID"),width: 40,filterable: false},
        {field: "outBusLic",title: fsn.l("供货商营业执照"),width: 60,filterable: false},
        {field: "outBusName",title: fsn.l("供货商名称"),width: 80,filterable: false},
        {field: "inBusName",title: fsn.l("购货商名称"),width: 80,filterable: false},
        {field: "inBusLic",title: fsn.l("购货商营业执照"),width: 60,filterable: false},
        {field: "accountDate",title: fsn.l("交易时间"),width: 60,filterable: false},
        {field: "inStatus",title: fsn.l("退回状态"),width: 60,filterable: false,
            template: function (e) {
                if(e.inStatus==1){
                    return "<label>已确认</label>";
                }
                return "<label style='color: red;font-weight: 900;'>未确认</label>";
            }
        },
        {width: 65, title: fsn.l("Operation"),
            template: function (e) {

                var tag = "<a  onclick='return fsn.manage.edit(" + e.id + ")' class='k-button k-button-icontext k-grid-ViewDetail'><span class='k-icon k-edit'> </span>" + fsn.l("查看") + "</a>";
                return tag;
            }
        }
    ];
    function getAccountDS(nameLic){
        ACCOUNT_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/relation/loadReturn/admin/" + options.page + "/" + options.pageSize+"?nameLic="+nameLic;
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
        return ACCOUNT_DS;
    }

	initialize();
});