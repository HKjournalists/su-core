$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
    var relation = window.fsn.relation = window.fsn.relation || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var BUS_COLUMNS = null;
	var BUS_DS = null;
    var PRODUCT_COLUMNS = null;
    var PRODUCT_DS = null;
    var BUS_GRID = null;
    var PRODUCT_GRID = null;
    var PRODUCT_DETAIL_GRID = null;
    var PRODUCT_DETAIL_COLUMNS = null;
    var PRODUCT_DETAIL_DS = [];
    var PRODUCT_WIN = null;
    var BUS_WIN = null;
    var RETIME = null;
    var busUnit = {id:null};
    var AccountID = 0;
    var CONFIRM = null;
    
    function initialize(){
    	lookLoadOutBusInfo();
    	CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "进货提示！", false,true,false,["Close"],null,"");
        BUS_WIN = initKendoWindow("BUS_WIN","1000px", "360px", "选择供销关系", false,true,false,["Close"],null,"");
        PRODUCT_WIN = initKendoWindow("OPEN_PRODUCT_WIN","800px", "420px", "选择商品", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("prodcut_detail",PRODUCT_DETAIL_COLUMNS,PRODUCT_DETAIL_DS,"300");//已经选择的商品
        PRODUCT_DETAIL_GRID = $("#prodcut_detail").data("kendoGrid");
        /* 初始化退货商信息 */
        initMyBusUnitInfo();
        /* 查看时加载供应商的相关信息  */
    };
    
    //供应商信息
    function lookLoadOutBusInfo(){
    	var haveId = window.location.href;
    	if(haveId.indexOf("id=")>0){
    		var id = haveId.split("id=")[1];
    		busUnit.id = AccountID = id;
    		$.ajax({
    			url:HTTPPREFIX + "/tzAccount/lookpurchase/"+id+"?type=buyGoods",
    			type:"GET",
    			dataType: "json",
    			async:false,
    			success:function(returnValue){
	    				if(returnValue.result.success){
		    				$("#return_lic").val(returnValue.outBusInfo.licNo);
		    				$("#return_busname").val(returnValue.outBusInfo.outBusName);
		    				$("#return_time").val(returnValue.outBusInfo.createDate);
		    				$("#add_barcode").val(returnValue.outBusInfo.barcode);
		    				busUnit.outBusId = returnValue.outBusInfo.outBusId;
		    				//判断是否已确认
		    				if(returnValue.outBusInfo.inStatus==1){
		    					$("#selectBus").css("display","none");
		    					$("#selectPro").css("display","none");
		    					$("#return_time").attr("disabled","disabled");
		    					$("#save").css("display","none");
		    					$("#submit").css("display","none");
		    				}
	    				}else{
	    					fsn.initNotificationMes(fsn.l("进货信息加载失败!"), false);
	    				}
    			}
    		});
    	}else{
    		$("#return_lic").val("");
			$("#return_busname").val("");
			$("#return_time").val("");
			$("#add_barcode").val("");
    	};
    }

    //购货商信息
    function initMyBusUnitInfo(){
        $.ajax({
            url: HTTPPREFIX + "/tzAccount/basicInfo/1?tzId="+AccountID,
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
                    busUnit.inBusId = bus.id;//本企业ID
                }
            }
        });
    };

    relation.openAddBus = function(){
        if(!BUS_GRID){
            upload.buildGridWioutToolBar("bus_grid",BUS_COLUMNS,BUS_DS,"300");
            BUS_GRID = $("#bus_grid").data("kendoGrid");
        }
        BUS_GRID.clearSelection();
        BUS_WIN.open().center();
    };
    
    relation.addReturnBus = function(){
       var row = BUS_GRID.select();
       var data = BUS_GRID.dataItem(row);
       if(data){
           $("#return_lic").val(data.licNo);
           $("#return_busname").val(data.busName);
           busUnit.outBusId = data.id;//供应商ID
           BUS_WIN.close();
       }else{
           fsn.initNotificationMes(fsn.l("请选择一条记录!"), false);
       }
    };
    relation.closeReturnBus = function(){
        BUS_WIN.close();
    };

    /**
     * 打开选择条形码弹窗
     * @HY
     */
    relation.openBarcode = function(){
    	 if(!PRODUCT_GRID){
             upload.buildGridWioutToolBar("product_grid",PRODUCT_COLUMNS,getProductDetailDS(),"350");
             PRODUCT_GRID = $("#product_grid").data("kendoGrid");
             PRODUCT_GRID.bind("change", grid_change);
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
    };
    
    function getReturnProduct(tagName,listDS){
        var selects = document.getElementsByName(tagName);
        var itemArr = new Array();
        if (selects != null && selects.length > 0) {
            for(var i = 0;i<selects.length;i++){
                if($(selects[i]).is(':checked')){
                    var str = $(selects[i]).attr("id");
                    var pid = str.split("|")[0];
                    var pqs = str.split("|")[1];
                    if(pid&&pid!=""&&listDS&&listDS.length>0){
                        for(var j=0;j<listDS.length; j++){
                            if(parseInt(pid)===listDS[j].productId&&pqs==listDS[j].qsNumber){
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

    //选择商品
    relation.addReturnProduct = function(){
        var listDS = PRODUCT_GRID.dataSource.data();
        var itemArr = getReturnProduct("topCheckBox",listDS);
        var list = PRODUCT_DETAIL_GRID.dataSource.data();
        var pdata = [];
        var json={};
        var bar = "";
        if(itemArr.length > 0){
            /* 合并数组到list中 */
            Array.prototype.push.apply(list, itemArr);
            /* 数组元素去重 */
            for(var i=0;i<list.length;i++){
                if(!(json[list[i].productId]&&json[list[i].qsNumber])){
                    pdata.push(list[i]);
                    json[list[i].productId] = 1;
                    json[list[i].qsNumber] = 1;
                    bar +=list[i].barcode+",";
                }
            }
            var DS =  new kendo.data.DataSource({
                data:pdata
            });
            PRODUCT_DETAIL_GRID.setDataSource(DS);
            $("#add_barcode").val(bar.substring(0, bar.length-1));
        }
        PRODUCT_WIN.close();
    };

    relation.closeReturnProduct = function(){
        PRODUCT_WIN.close();
    };
    
    /**
     * 全选/全不选
     */
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
    
    function grid_change(e){
        var selectedCells = PRODUCT_GRID.select();
        for(var i = 0; i < selectedCells.length; i++){
            var dataItem = PRODUCT_GRID.dataItem(selectedCells[i]);
            var id = dataItem.productId +'|'+ dataItem.qsNumber;
            var selected = document.getElementById(id);
            if($(selected).is(':checked')){
                selected.checked = false;
            }else{selected.checked = true;}
        }
    };
    
    /* 封装需要提交的信息 */
    function purchaseProductInfo(status){
    	 if(status=="submit"){
             busUnit.inStatus = 1;
         }else{
             busUnit.inStatus = 0;
         }
    	 var data = PRODUCT_DETAIL_GRID.dataSource.data();
         var pArr = getReturnProduct("topDetail",data);
         var repeat = {};
         busUnit.proList = [];
         if(pArr!=null&&pArr.length>0){
         	for(var x= 0;x<pArr.length;x++){
         		pArr[x].batch = $("#batch"+pArr[x].productId).html();
         		pArr[x].overDate = $("#overDate"+pArr[x].productId).html();
         	}
         	for(var i=0;i<pArr.length;i++){
                if(!(repeat[pArr[i].productId]&&repeat[pArr[i].qsNumber])){
                	busUnit.proList.push(pArr[i]);
                    repeat[pArr[i].productId] = 1;
                    repeat[pArr[i].qsNumber] = 1;
                }
            }
         }else{
             fsn.initNotificationMes("没有选择进货的产品",false);
             return false;
         }
        if(busUnit.outBusId==null || busUnit.outBusId==""){
            fsn.initNotificationMes("没有供应商商信息",false);
            return false;
        }
        if(busUnit.inBusId==null || busUnit.inBusId==""){
            fsn.initNotificationMes("没有选择销往（退货商）",false);
            return false;
        }
        //进货时间
        if($("#return_time").val().trim()!=""){
            busUnit.inDate = $("#return_time").val().trim();
        }else{
            fsn.initNotificationMes("请选择进货时间",false);
            return false;
        }
        return true;

    }

    /**
     * 验证数据并打开提示框 或 关闭提示框
     */
    relation.validate = function(status){
    	if(status=='cancel'){
    		CONFIRM.close();
    		return false;
    	}
        if(!purchaseProductInfo(status)){
            return;
        };
        if(status=='submit'){
        	 CONFIRM.open().center();
        }else{
        	relation.submitAll("save");
        }
    };
    
    /* 数据提交  */
    relation.submitAll = function(status){
		 var data = busUnit;
			 $.ajax({
		            url: HTTPPREFIX + "/tzAccount/addwholesale/submit/"+status+"/1",
		            type: "POST",
		            dataType: "json",
		            contentType: "application/json; charset=utf-8",
		            data: JSON.stringify(data),
		            success: function (returnVal) {
		                if (returnVal.save) {
		                	AccountID = returnVal.tzId;
		                	if(status=="save"){
		                		fsn.initNotificationMes("进货产品保存成功", true);
		                		window.location.href = 'addpurchase.html?id='+AccountID;
		                	}else{
		                		fsn.initNotificationMes("进货产品提交成功", true);
		                		window.setTimeout("window.location.href = 'purchase.html'", 1000); 
		                	}
		                } else {
		                	fsn.initNotificationMes("进货产品操作失败", false);
		                }
		            }
		        });
				CONFIRM.close();
	 };
    
    /* 返回 */
    relation.goBack = function(){
    	window.location.href = "admin_purchase.html";
    };

    function editPrice(container, options){
        var input = $('<input min="0.00" value="0.00"/>');
        buildNumericTextBox(input,container, options);
    }

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
            sortable: false,
            resizable: true,
            selectable: false,
            pageable: {
                refresh: true,
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });
    };

    //选择供销关系
    BUS_COLUMNS = [  {field: "id",title: fsn.l("序号"),width: 15,filterable: false},
        {field: "busName",title: fsn.l("企业名称"),width: 70,filterable: false},
        {field: "licNo",title: fsn.l("营业执照号"),width: 50,filterable: false},
        {field: "type",title: fsn.l("供销关系"),width: 20,filterable: false,template:
            function(item){
                if (item.type == 1) {
                    return "销往";
                }else{
                    return "来源";
                }
            }},
        {field: "createDate",title: fsn.l("创建时间"),width: 40,filterable: false,template: function(item){
            return fsn.formatGridDate(item.createDate);
        }}
    ];
    
    //选择商品
    PRODUCT_COLUMNS = [ {field: "id",title: fsn.l("Id"),width: 1,filterable: false},
                        { title: "<input type='checkbox' id='topCheckBox' onclick='return fsn.relation.selectAll(this)'/>选择", width: 30,
        				template: function (e) {
        						var tag = "<input name='topCheckBox' type='checkbox' id='"+e.productId+'|'+e.qsNumber+"' class='grid_checkbox'/>";
        						return tag;
        				}
			        },
			        {field: "name",title: fsn.l("Name"),width: 80,filterable: false},
			        {field: "barcode",title: fsn.l("商品条形码"),width: 60,filterable: false},
			        {field: "qsNumber",title: fsn.l("QS号"),width: 60,filterable: false}
    ];

    //已经选择的商品
    PRODUCT_DETAIL_COLUMNS = [ {field: "id",title: fsn.l("Id"),width: 1,filterable: false},
                               {width: 50, title: "<input type='checkbox' id='topDetail' onclick='return fsn.relation.selectAll(this)'/>选择",
                                   template: function (e) {
                                       var tag = "<input name='topDetail' type='checkbox' id='"+ e.productId+'|'+e.qsNumber +"' class='grid_checkbox'/>";
                                       return tag;
                                   }
                               },
                               {width: 80, title: fsn.l("商品条形码"),
                                   template: function (e) {
                                       var tag = "<label>"+ e.barcode+"</label>";
                                       return tag;
                                   }
                               },
                               {width: 80, title: fsn.l("QS号"),
                                   template: function (e) {
                                       var value = e.qsNumber;
                                       if(e.qsNumber===null){
                                           value = "";
                                       }
                                       var tag = "<label>"+ value +"</label>";
                                       return tag;
                                   }
                               },
                               {width: 80, title: fsn.l("Name"),
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
                               {field: "returnCount",title: fsn.l("数量"),filterable: false,editor:
                                   function(container, options) {
	                            	   var input = $("<input min='0' value='0' maxlength='9' />");
	                                   input.attr("name", options.field);
	                                   input.appendTo(container);
	                                   input.kendoNumericTextBox();
	                                   input.data("kendoNumericTextBox").bind("spin",change);
	                                   /* 进货时不考虑库存  */
	                                   function change() {
	                                       var value = input.data("kendoNumericTextBox").value();
	                                       if(value>999999999){
	                                    	   input.data("kendoNumericTextBox").value(999999999);
	                                    	   fsn.initNotificationMes("进货数量不能超过：999999999",false);
	                                       }
	                                   };
                                   },width: 50},
                               {field: "price",title: fsn.l("单价"),width: 50,filterable: false,editor:
                                   function(container, options) {
                                       var input = $("<input min='0' step='1' type='number' maxlength='9' />");
                                       input.attr("name", options.field);
                                       input.appendTo(container);
                                       input.kendoNumericTextBox();
                                       input.data("kendoNumericTextBox").bind("spin",change);
	                                   function change() {
	                                       var value = input.data("kendoNumericTextBox").value();
	                                       if(value>999999999){
	                                    	   input.data("kendoNumericTextBox").value(999999999);
	                                    	   fsn.initNotificationMes("进货单价不能超过：999999999",false);
	                                       }
	                                   };
                                   }},
                               {
                                   field: "productionDate", title: fsn.l("生产日期"), width: 80, filterable: false,
                                   editor:function(container, options) {
                                       var item = options.model;
                                       var tag = "<option selected='selected'>选择生产日期</option>";
                                       var list = item.birthDateList;
                                       if(list!=null && list.length > 0){
                                           for(var x=0;x<list.length;x++){
                                               tag += "<option id='"+ list[x].batch +"' title='"+ list[x].overDate +"' value='"+ list[x].instanceId +"'>"+list[x].birthDate+"</option>";
                                           }
                                       };
                                       var selectTag = $("<select class='k-select k-textbox' style='width: 120px;' onchange='return fsn.relation.selectDate(this)'>"+ tag+"</select>");

                                       selectTag.appendTo(container);

                                       relation.selectDate = function(obj){
                                           var index = obj.selectedIndex; // 选中索引
                                           var overDate = obj.options[index].title;
                                           var text = obj.options[index].text;
                                           var batch = obj.options[index].id;
                                           options.model.productionDate = text;
                                           $("#batch"+options.model.productId).html(batch);
                                           $("#overDate"+options.model.productId).html(overDate);
                                       };
                                   }
                               },
                               {width: 70, title: fsn.l("批次"),
                                   template: function (e) {
                                       var batch = e.batch;
                                       if(e.batch==null) batch="";
                                       var tag = "<label id='batch"+ e.productId+"'>"+ batch +"</label>";
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


    //选择供销关系--数据
    BUS_DS = new kendo.data.DataSource({
        transport: {
            read: {
                url : function(options){
                    return HTTPPREFIX + "/account/relation/returnBus/2/" + options.page + "/" + options.pageSize;
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
                return returnValue.data;
            },
            total : function(returnValue) {
                return returnValue.total;
            }
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
    });

    //选择商品--数据
    getProductDetailDS  = function(){
	    PRODUCT_DS = new kendo.data.DataSource({
	        transport: {
	            read: {
	                url : function(options){
	                    return HTTPPREFIX + "/tzAccount/selectBuyGoods/" + options.page + "/" + options.pageSize+"?licNo=";
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
	                return returnValue.data;
	            },
	            total : function(returnValue) {
	                return returnValue.total;
	            }
	        },
	        pageable: {
	            refresh: true,
	            messages: fsn.gridPageMessage(),
	        },
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
	    });
	    return PRODUCT_DS;
    };
    /* 已经选择的商品  */
    PRODUCT_DETAIL_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/tzAccount/viewpurchse/list/"+AccountID+"/0/0";
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
                    return returnValue.data;
                },
                total : function(returnValue) {
                    return returnValue.total;
                }
            },
            pageable: {
                refresh: true,
                messages: fsn.gridPageMessage(),
            },
            serverPaging : true,
            serverFiltering : true,
            serverSorting : true
        });

	initialize();
});