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
        CONFIRM_DELETE = initKendoWindow("CONFIRM_DELETE","350px", "120px", "确定删除", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("return_account_grid",ACCOUNT_COLUMNS,getAccountDS("",""),"300");
        ACCOUNT_GRID = $("#return_account_grid").data("kendoGrid");
        /* 初始化退货商信息 */
    };

    /* 添加退货信息 */
    manage.addReturnBus = function(){
        window.location.href="returnGoods.html";
    }

    /* 查询退货信息 */
    manage.queryBus = function(){
        var accno = ""; //$("#account_no").val().trim();
        var nameAndLic = $("#product_name_lic").val().trim();
        ACCOUNT_GRID.setDataSource(getAccountDS(accno,encodeURIComponent(nameAndLic)));
        ACCOUNT_GRID.refresh();
    }

    /* 编辑退货信息 */
    manage.edit = function(id){
        var condition = "?"+id+"&"+$.md5(""+id);
        window.location.href="returnGoods.html"+condition;
    }
    /* 未确认的可以删除 */
    manage.delete = function(id){
        manage.deleteId = id;
        CONFIRM_DELETE.open().center();
    }
    /* 确认删除提示 */
    manage.winDelete = function(status){
        CONFIRM_DELETE.close();
        if(!status){
            manage.deleteId = null;
            return ;
        }
        $.ajax({
            url: HTTPPREFIX + "/account/relation/delete/"+manage.deleteId,
            type: "DELETE",
            dataType: "json",
            async:false,
            success: function(returnValue){
                ACCOUNT_GRID.dataSource.read();
                if(returnValue.delete==true){
                    fsn.initNotificationMes(fsn.l("删除成功!"), true);
                }else{
                    fsn.initNotificationMes(fsn.l("删除失败!"), false);
                }
            }
        });
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
        {field: "id",title: fsn.l("编号"),width: 40,filterable: false},
        {field: "inBusName",title: fsn.l("供货商名称"),width: 80,filterable: false},
        {field: "outBusLic",title: fsn.l("供货商营业执照"),width: 60,filterable: false},
        {field: "accountDate",title: fsn.l("交易时间"),width: 60,filterable: false},
        {field: "outBusName",title: fsn.l("购货商名称"),width: 80,filterable: false},
        {field: "outStatus",title: fsn.l("状态"),width: 30,filterable: false,
            template: function(item){
                var status = item.outStatus!=1?"<label style='color: red;font-weight: 800;'>未确认</label>":"<label style='font-weight: 800;'>已确认</label>";
                return status;
            }
        },
        {
            width: 65, title: fsn.l("Operation"),
            template: function (e) {

                var tag = "<a  onclick='return fsn.manage.edit(" + e.id + ")' class='k-button k-button-icontext k-grid-ViewDetail'><span class='k-icon k-edit'> </span>" + fsn.l("查看") + "</a>";
                 if(e.outStatus != 1){
                     tag +="<a onclick='return fsn.manage.delete(" + e.id + ")' class='k-button k-button-icontext k-grid-ViewDetail'><span class='k-icon k-edit'> </span>" + fsn.l("删除") + "</a>"
                 }
                return tag;
            }
        }
    ];
    function getAccountDS(accountNo,nameLic){
        ACCOUNT_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/relation/loadReturnAccount/out/" + options.page + "/" + options.pageSize+"?no="+accountNo+"&nameLic="+nameLic;
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
        return ACCOUNT_DS;
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