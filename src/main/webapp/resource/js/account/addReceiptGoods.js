$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var relation = window.fsn.relation = window.fsn.relation || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
    var PRODUCT_DETAIL_COLUMNS = null;
    var PRODUCT_DETAIL_DS = [];
    var PRODUCT_DETAIL_GRID = null;
    var AccountID = 0;
    var CONFIRM = null;
    var LOADING = null;
    
    function initialize(){
    	lookLoadOutBusInfo();
    	CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "提示！", false,true,false,["Close"],null,"");
    	LOADING = initKendoWindow("toSaveWindow","300px", "80px", "提示！", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("prodcut_detail",PRODUCT_DETAIL_COLUMNS,PRODUCT_DETAIL_DS,"300");
        PRODUCT_DETAIL_GRID = $("#prodcut_detail").data("kendoGrid");
        initMyBusUnitInfo();
    };
    //供货商企业信息
    function lookLoadOutBusInfo(){
    	var id = window.location.href.split("id=")[1];
		AccountID = id;
		$.ajax({
			url:HTTPPREFIX + "/tzAccount/lookReceipt/"+id,
			type:"GET",
			dataType: "json",
			async:true,
			success:function(returnValue){
				if(returnValue.result.success){
					$("#return_lic").val(returnValue.outBusInfo.licNo);
					$("#return_busname").val(returnValue.outBusInfo.outBusName);
					$("#return_time").val(returnValue.outBusInfo.createDate);
					$("#add_barcode").val(returnValue.outBusInfo.barcode);
					//判断是否已确认
    				if(returnValue.outBusInfo.inStatus==1){
    					$("#return_time").attr("disabled","disabled");
    					$("#submit").css("display","none");
    					$("#back").css("display","none");
    				}
				}else{
					fsn.initNotificationMes(fsn.l("进货信息加载失败!"), false);
				}
			}
		});
    }

    //购货商企业信息
    function initMyBusUnitInfo(){
        $.ajax({
            url: HTTPPREFIX + "/account/relation/basicInfo",
            type: "GET",
            dataType: "json",
            async:false,
            success: function(returnValue){
                var bus = returnValue.basicInfo;
                if(bus){
                    $("#return_busUnit").val(bus.busName);
                    $("#return_busUnit_lic").val(bus.licNo);
                    $("#return_busUnit_contact").val(bus.contact);
                    $("#return_busUnit_address").val(bus.address);
                }
            }
        });
    };
    
    /* 返回 */
    relation.goBack = function(){
    	window.location.href = "receiptGoods.html";
    };
    
	 /**
     * 验证数据并打开提示框 或 关闭提示框
     */
    relation.validate = function(status){
    	if(status=='cancel'){
    		CONFIRM.close();
    		return;
    	}
    	if(status=='submit'){
    		$("#comfire").text("确认收货吗？");
        	 CONFIRM.open().center();
        }
    	if(status=='submit_back'){
    		$("#comfire").text("确认退货吗？");
    		CONFIRM.open().center();
    	}
    };
    
    /* 确认退货  */
    function returnOfGoods(){
    	$("#saving_msg").html("正在提交数据，请稍候...");
    	LOADING.open().center();
		 $.ajax({
				url:HTTPPREFIX + "/tzAccount/returnOfGoods/"+AccountID,
				type:"GET",
				dataType: "json",
				async:false,
				success:function(returnValue){
					if (returnValue.result.status == "true") {
						LOADING.close();
						fsn.initNotificationMes("已成功退货", true);
						window.location.href = "receiptGoods.html";
					} else {
						LOADING.close();
						fsn.initNotificationMes("退货失败", false);
						window.location.href = "receiptGoods.html";
					}
				}
			});
			CONFIRM.close();
    };

    function returnCountVDate(d){
        if(d.length>0){
            for(var i = 0; i< d.length; i++){
                if(d[i].returnCount===null){
                    d[i].returnCount = 0;
                }
                if(d[i].countTotal>=d[i].returnCount && d[i].returnCount>=0){
                    continue;
                }else{
                    fsn.initNotificationMes("第 "+(i+1)+"行的数据中,用户输入的数量不能大于总数并且不能小于0", false);
                    return false;
                }

            }
            return true;
        }else{
            return false;
        }
    }
    
    /* 数据提交  */
    relation.submitAll = function(){

        /*获取提交的数据*/
        var data = PRODUCT_DETAIL_GRID.dataSource.data();
        if(!returnCountVDate(data)){
            return;
        }
    	var val = $("#comfire").text();
    	if(val.indexOf("退货")>-1){
    		returnOfGoods(data);
    		return;
    	}
    	$("#saving_msg").html("正在提交数据，请稍候...");
    	LOADING.open().center();
		 $.ajax({
				url:HTTPPREFIX + "/tzAccount/submitReceipt/"+AccountID,
				type:"POST",
				async:false,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(data),
				success:function(returnValue){
					if (returnValue.result.status == "true") {
						LOADING.close();
						fsn.initNotificationMes("已成功确认收货", true);
						window.location.href = "receiptGoods.html";
					} else {
						LOADING.close();
						fsn.initNotificationMes("收货失败", false);
						window.location.href = "receiptGoods.html";
					}
				}
			});
			CONFIRM.close();
	 };
	 
	 /* 提示框调用方法  */
	 function initKendoWindow(formId, width, heigth, title,visible, modal, resizable, actions, mesgLabelId, message){
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

    /*Grid方法调用*/
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

    //产品详情列表
    PRODUCT_DETAIL_COLUMNS = [  {field: "id",title: "编号", width:"5%",filterable: false},
                               {width: 80, title: fsn.l("商品条形码"),
                                   template: function (e) {
                                       var tag = "<label title = "+e.barcode+">"+ e.barcode+"</label>";
                                       return tag;
                                   }
                               },
                               {width: 110, title: fsn.l("QS号"),
                                   template: function (e) {
                                       var value = e.qsNumber;
                                       if(e.qsNumber===null){
                                           value = "";
                                       }
                                       var tag = "<label title = "+value+">"+ value +"</label>";
                                       return tag;
                                   }
                               },
                               {width: 80, title: fsn.l("Name"),
                                   template: function (e) {
                                       var tag = "<label title = "+e.name+">"+ e.name+"</label>";
                                       return tag;
                                   }
                               },
                               {width: 70, title: fsn.l("规格型号"),
                                   template: function (e) {
                                       var tag = "<label title = "+e.format+">"+ e.format+"</label>";
                                       return tag;
                                   }
                               },

                               {title: fsn.l("发货数"),filterable: false,width: 55,
                                   template: function (e) {
                                       var countTotal = e.countTotal;
                                       if(e.productionDate==null) pdate="";
                                       var tag = "<label id='countTotal"+e.productId+"' title = '"+countTotal+"'>"+ countTotal+"</label>";
                                       return tag;
                                   }},
                               {field: "returnCount",title: fsn.l("实收数"),filterable: false,width: 55},
                               {title: fsn.l("单价"),width: 50,template: function (e) {
                                   var price = e.price+"";
                                   if(price==null) price="";
                                   var tag = "<label " + price + "' title = '" + price + "'>"+ price + "</label>";
                                   return tag;
                               }},
                               {title: fsn.l("生产日期"), width: 75,
                                   template: function (e) {
                                       var pdate = e.productionDate;
                                       if(e.productionDate==null) pdate="";
                                       var tag = "<label id='productionDate"+e.productId+"' title = '"+pdate+"'>"+ fsn.formatGridDate(pdate)+"</label>";
                                       return tag;
                                   }},
                               {width: 80, title: fsn.l("批次"),
                                   template: function (e) {
                                       var batch = e.batch;
                                       if(e.batch==null) batch="";
                                       var tag = "<label id='batch"+ e.productId+"' title = '"+batch+"'>"+ batch +"</label>";
                                       return tag;
                                   }
                               },
                               {width: 70, title: fsn.l("过期日期"),
                                   template: function (e) {
                                       var tag = "<label id='overDate"+e.productId+"' title = '"+e.overDate+"'>"+ fsn.formatGridDate(e.overDate)+"</label>";
                                       return tag;
                                   }
                               }
                           ];


    /* 产品详情DS  */
    PRODUCT_DETAIL_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                    	return HTTPPREFIX + "/tzAccount/loadingReceiptGoods/list/0/0"+"?tzId="+AccountID;
                    },
                    dataType : "json",
                    contentType : "application/json"
                }
            },
            batch : true,
           /* page:1,
            pageSize: 10,*/
            schema: {
                data : function(returnValue) {
                    return returnValue.data;
                },
                total : function(returnValue) {
                    return returnValue.total;
                }
            },
           /* pageable: {
                refresh: true,
                messages: fsn.gridPageMessage(),
            },
            serverPaging : true,*/
            serverFiltering : true,
            serverSorting : true
        });
    
    /*全选、全不选*/
    relation.selectAll = function (obj) {
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
    };

	initialize();
});