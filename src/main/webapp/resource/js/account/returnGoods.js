$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
    var relation = window.fsn.relation = window.fsn.relation || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var BUS_COLUMNS = null;
    var PRODUCT_COLUMNS = null;
    var BUS_GRID = null;
    var PRODUCT_GRID = null;
    var PRODUCT_DETAIL_GRID = null;
    var PRODUCT_DETAIL_COLUMNS = null;
    var PRODUCT_DETAIL_DS = [];
    var PRODUCT_WIN = null;
    var CONFIRM_SUBMIT = null;
    var BUS_WIN = null;
    var busUnit = {id:null};
    var accountOutId = null;
    var LOADING = null;
    try {
        var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        var id = arrayParam[0]; // 产品id（原始id，未被编码）
        var orig_idmd5 = arrayParam[1]; // 产品id(被编码过的产品id)
        accountOutId = verificationMD5(id,orig_idmd5);
    } catch (e) {}
    
    function initialize(){
        clearInputVal();
        //add by ltg 20160812 增加回收原因由后台枚举类返回 start
        initRecycle_reason();
        //add by ltg 20160812 增加回收原因由后台枚举类返回 end
        BUS_WIN = initKendoWindow("BUS_WIN","850px", "310px", "选择供销关系", false,true,false,["Close"],null,"");
        PRODUCT_WIN = initKendoWindow("OPEN_PRODUCT_WIN","1000px", "465px", "选择商品", false,true,false,["Close"],null,"");
        CONFIRM_SUBMIT = initKendoWindow("CONFIRM_SUBMIT","350px", "120px", "确定提交", false,true,false,["Close"],null,"");
        LOADING = initKendoWindow("toSaveWindow","300px", "80px", "提示！", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("prodcut_detail",PRODUCT_DETAIL_COLUMNS,getProductDetailDS(accountOutId),"300");
        PRODUCT_DETAIL_GRID = $("#prodcut_detail").data("kendoGrid");
        /* 编辑过来的时候初始化页面数据 */
        $("#save").show();
        $("#submit").show();
        $("#select_bar").show();
        $("#select_lic").show();
        //$("#return_time").val(new Date());
        initPageData(accountOutId);
        /* 初始化退货商信息 */
        initMyBusUnitInfo();
    };
    //add by ltg 20160812 增加回收原因由后台枚举类返回 start
    var recycle_reason = [];
    var valueToName_jo = {};//值对应名称
    var nameToValue_jo = {};//名称对应值
    //add by ltg 20160812 增加回收原因由后台枚举类返回 end

    function clearInputVal(){
        $("#return_lic").val("");
        $("#return_busname").val("");
        $("#add_barcode").val("");
        $("#return_time").val("");
    }

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
                        busUnit.id = bus.busAccountId;
                        $("#return_time").val(returnValue.data.outTime);
                        if(bus.busRelationVO!=null){
                            $("#return_busname").val(bus.busRelationVO.busName);
                            $("#return_lic").val(bus.busRelationVO.licNo);
                            busUnit.inBusId = bus.busRelationVO.id;
                            busUnit.inBusName = bus.busRelationVO.busName;
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
                        if(bus.outStatus==1){
                            $("#save").hide();
                            $("#submit").hide();
                            $("#select_bar").hide();
                            $("#select_lic").hide();
                            fsn.initNotificationMes("该订单已经提交不能修改！", false);
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
                    $("#return_busUnit").val(bus.busName);
                    $("#return_busUnit_lic").val(bus.licNo);
                    $("#return_busUnit_contact").val(bus.contact);
                    $("#return_busUnit_address").val(bus.address);
                    busUnit.outBusId = bus.id;
                    //add by ltg 20160815 增加字段记录退货商名称和供应商名称 start
                    busUnit.outBusName = bus.busName
                    //add by ltg 20160815 增加字段记录退货商名称和供应商名称 end
                }
            }
        });
    }
    
    //add by ltg 20160812 增加回收原因由后台枚举类返回 start
    function initRecycle_reason(){
        $.ajax({
            url: HTTPPREFIX + "/account/relation/recycle_reason",
            type: "GET",
            dataType: "json",
            async:false,
            success: function(returnValue){
            	var flg = returnValue.flg;
            	if (flg == true || flg == 'true') {
            		var val = eval('(' + returnValue.recycle_reason + ')');
            		if (val == null || val.length == 0) {
                    	fsn.initNotificationMes('未获取到回收原因！', false);
                    	return;
            		}
            		for (var index in val) {
            			var obj = val[index];
            			recycle_reason.push(obj);
            			var name = obj.name;
            			var value = obj.value;
            			valueToName_jo[value] = name;
            			nameToValue_jo[name] = value;
            		}
            	} else {
                	fsn.initNotificationMes('未获取到回收原因！', false);
            	}
            }
        });
    }
    //add by ltg 20160812 增加回收原因由后台枚举类返回 end

    function verificationMD5(str1,md5code){
        var md5 = $.md5(str1);
        if(md5===md5code){
            return str1;
        }else{
            return "";
        }
    };

    relation.openAddBus = function(){
        if(!BUS_GRID){
            upload.buildGridWioutToolBar("bus_grid",BUS_COLUMNS,getBusDS("",""),"200");
            BUS_GRID = $("#bus_grid").data("kendoGrid");
        }
        BUS_GRID.clearSelection();
        BUS_WIN.open().center();
    }

    /**
     * 过滤产功能，过滤企业功能
     */
    relation.queryBus = function(id){
        if(id==="BUS_WIN"){
            var busName = $("#bus_name_q").val().trim();
            var busLic = $("#bus_lic_q").val().trim();
            var busDS = getBusDS(busName,busLic);
            BUS_GRID.setDataSource(busDS);
        }
        if(id==="OPEN_PRODUCT_WIN"){
            var pName = $("#pro_name_q").val().trim();
            var pLic = $("#pro_bar_q").val().trim();
            var proDS = getProductDS(pName,pLic);
            PRODUCT_GRID.setDataSource(proDS);
        }
    }
    
    relation.addReturnBus = function(){
       var row = BUS_GRID.select();
       var data = BUS_GRID.dataItem(row);
       if(data){
           $("#return_lic").val(data.licNo);
           $("#return_busname").val(data.busName);
           busUnit.inBusId = data.id;
           busUnit.inBusName = data.busName;
           BUS_WIN.close();
       }else{
           fsn.initNotificationMes(fsn.l("请选择一条记录!"), false);
       }
    }
    relation.closeReturnBus = function(){
        BUS_WIN.close();
    }

    /**
     * 打开选择条形码弹窗
     * @HY
     */
    relation.openBarcode = function(){
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
    
    function getReturnProduct(tagName,listDS){
        var selects = document.getElementsByName(tagName);
        var itemArr = new Array();
        
        //modify by ltg 20160818 考虑到同一产品不同生产日期的退货情况，判断放到提交来做 start
        //add by ltg 20160812 增加产品重复选择判断 start
        /*var chosed_bar_code = {};
        var chosed_list = $("input[name='topDetail']");
        if (chosed_list != null && chosed_list != '' && chosed_list.length > 0) {
        	var length = chosed_list.length;
        	for (var i = 0; i < length; i++) {
        		var grid = $("#prodcut_detail").data("kendoGrid");
        		var row = grid.dataSource.at(i);
        		var barcode = row.barcode;
        		chosed_bar_code[barcode] = barcode;
        	}
        }
        var repeat = 0;*/
        //add by ltg 20160812 增加产品重复选择判断 end
        
        if (selects != null && selects.length > 0) {
            for(var i = 0;i<selects.length;i++){
                if($(selects[i]).is(':checked')){
	            	var str = $(selects[i]).attr("id");
	            	var pid = str.split("|")[0];
	                var pqs = str.split("|")[1];
	                var uuid= str.split("|")[2];
	                //add by ltg 20160812 增加重复产品判断 start
	                /*var barcode= str.split("|")[3];*/
	                //add by ltg 20160812 增加重复产品判断 end
                    if(pid&&pid!=""&&listDS&&listDS.length>0){
                        for(var j=0;j<listDS.length; j++){
                            if(parseInt(pid)==listDS[j].productId&&pqs==listDS[j].qsNumber&&uuid==listDS[j].uuid){
            	                //add by ltg 20160812 增加重复产品判断 start
            	             	/*var barcode2 = listDS[j].barcode;
            	             	if (chosed_bar_code[barcode2] != null && chosed_bar_code[barcode2] != '') {
            	             		repeat++;
            	             		continue;
            	             	}*/
            	                //add by ltg 20160812 增加重复产品判断 end
                                var temp = listDS[j];
                                itemArr.push(temp);
                            }
                        }
                    }
                }
            }
        }
        //add by ltg 20160812 增加重复产品判断 start
        /*if (repeat > 0) {
        	fsn.initNotificationMes("已自动去掉重复产品" + repeat + '个', false);
        }*/
        //add by ltg 20160812 增加重复产品判断 end
        //modify by ltg 20160818 考虑到同一产品不同生产日期的退货情况，判断放到提交来做 start
        return itemArr;
    }

    relation.addReturnProduct = function(){
        var listDS = PRODUCT_GRID.dataSource.data();
        var itemArr = getReturnProduct("topCheckBox",listDS);
        var list = PRODUCT_DETAIL_GRID.dataSource.data();
        var pdata = [];
        if(itemArr.length > 0){
            var bar = "";
            //合并数组到list中 
            Array.prototype.push.apply(list, itemArr);
            for(var i=0;i<list.length;i++){
            	 pdata.push(list[i]);
            	 bar +=list[i].barcode+",";
            }
            var DS =  new kendo.data.DataSource({
                //modify by ltg 20160812 增加分页信息 start
                pageSize : 10,
                data : pdata,
                total : list.length
//                data:pdata
                //modify by ltg 20160812 增加分页信息 end
            });
            PRODUCT_DETAIL_GRID.setDataSource(DS);
            defaultSelected("topDetail");
            $("#add_barcode").val(bar);
        }
        PRODUCT_WIN.close();
    }

    /* 添加的产品详细中默认是勾选的 */
    function defaultSelected(id){
        var selects = document.getElementsByName(id);
        if (selects != null && selects.length > 0) {
            document.getElementById(id).checked = true;
            for(var i = 0;i<selects.length;i++){
                selects[i].checked = true;
            }
        }
    }

    relation.closeReturnProduct = function(){
        PRODUCT_WIN.close();
    }
    /**
     * product全选/全不选
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
    }

    function grid_change(e){
        var selectedCells = PRODUCT_GRID.select();
        for(var i = 0; i < selectedCells.length; i++){
            var dataItem = PRODUCT_GRID.dataItem(selectedCells[i]);
         	//modify by ltg 20160812 增加重复产品判断 start
            //var id = dataItem.productId +'|'+ dataItem.qsNumber+'|'+ dataItem.uuid;
            var id = dataItem.productId + '|' + dataItem.qsNumber + '|' + dataItem.uuid + '|' + dataItem.barcode;
         	//modify by ltg 20160812 增加重复产品判断 end
            
            var selected = document.getElementById(id);
            if($(selected).is(':checked')){
                selected.checked = false;
            }else{selected.checked = true;}
        }
    }
    
    /* 新增选择的一行 */
    relation.cpoySelectLine = function(){
         //判断是否选中一行！ 
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
        				"productionDate":"请选择生产日期",
        				"batch":"",
        				"overDate":"",
        				"busType":data.busType,
        				"uuid":timestamp
        			};
        	PRODUCT_DETAIL_GRID.dataSource.insert(selectedIndex+1,temp);
        }else{
            fsn.initNotificationMes("请先选择一行!", false);
            return false;
        }

    };
    
    /* 封装需要提交的信息 */
    function returnProductInfo(status){
        if(status=="submit"){
            busUnit.outStatus = 1;
        }else{
            busUnit.outStatus = 0;
        }
    	//modify by ltg 20160815 由于增加了重复判断导致原语句会提示重复问题 start
    	var chosed_list = $("input[name='topDetail']");
    	if (chosed_list == null || chosed_list == '' || chosed_list.length == 0) {
            fsn.initNotificationMes("没有选择退货的产品",false);
            return false;
    	}
    	busUnit.proList = [];
    	var length = chosed_list.length;
    	var selected_product_num = 0;//选择的退货产品数量
        //add by ltg 20160818 考虑到同一产品不同生产日期的退货情况，判断放到提交来做 start
		var repeat_obj = {};//key：条码-生产日期、value：任意值，不为空就行
        //add by ltg 20160818 考虑到同一产品不同生产日期的退货情况，判断放到提交来做 end
		var err_flg = false;
		var msg = null;
    	for (var i = 0; i < length; i++) {
    		var obj = chosed_list[i];
    		if (obj.checked) {
    			selected_product_num++;
        		var grid = $("#prodcut_detail").data("kendoGrid");
        		var row = grid.dataSource.at(i);
            	var prodate = row.productionDate;
          		if(!prodate || prodate.indexOf("选择")>-1){//判断是否选择生产日期
          			if (!err_flg) {
          				err_flg = true;
          				msg = "第"+(i+1)+"条产品请选择生产日期！";
          			}
//          			fsn.initNotificationMes("第"+(i+1)+"条产品请选择生产日期！",false);
//          			return false;
//          			break;
          		}
          		
            	var problem_describe = row.problem_describe;
          		if(problem_describe == null || problem_describe == '' || problem_describe.indexOf("选择")>-1){//判断是否选择问题描述
          			if (!err_flg) {
          				err_flg = true;
          				msg = "第"+(i+1)+"条产品请选择问题描述！";
          			}
//          			fsn.initNotificationMes("第"+(i+1)+"条产品请选择问题描述！",false);
//          			return false;
//          			break;
          		} else {
              		if (nameToValue_jo[problem_describe] != null && nameToValue_jo[problem_describe] != '') {
                  		row.problem_describe = nameToValue_jo[problem_describe];
              		}
          		}
          		
            	var returnCount = row.returnCount;
          		if(returnCount == null || returnCount == '' || returnCount == 0 || returnCount == '0'){//判断退货数量
          			if (!err_flg) {
          				err_flg = true;
          				msg = "第"+(i+1)+"条产品请输入退货数量！";
          			}
//          			fsn.initNotificationMes("第"+(i+1)+"条产品请输入退货数量！",false);
//          			return false;
//          			break;
          		}
                //add by ltg 20160818 考虑到同一产品不同生产日期的退货情况，判断放到提交来做 start
        		var key = row.barcode + '-' + row.productionDate;
        		if (repeat_obj[key] != null && repeat_obj[key] != '') {
        			//同一产品选择了同一生产日期退货
          			if (!err_flg) {
          				err_flg = true;
          				msg = "第"+(i+1)+"条产品重复，已有相同的产品！";
          			}
//          			fsn.initNotificationMes("第"+(i+1)+"条产品重复，已有相同的产品！",false);
//          			return false;
//          			break;
        		}
        		repeat_obj[key] = 1;
                //add by ltg 20160818 考虑到同一产品不同生产日期的退货情况，判断放到提交来做 end
          		
            	var qs = $.md5(row.qsNumber+row.uuid);
                row.overDate = $("#overDate"+row.productId+qs).html();
                row.batch = $("#batch"+row.productId+qs).html();
                busUnit.proList.push(row);
    		}
    	}
    	if (err_flg) {
    		fsn.initNotificationMes(msg ,false);
    		return false;
    	}
    	if (selected_product_num == 0) {
            fsn.initNotificationMes("没有选择退货的产品",false);
            return false;
    	}
        if(busUnit.outBusId==null || busUnit.outBusId==""){
            fsn.initNotificationMes("没有退货商信息",false);
            return false;
        }
        if(busUnit.inBusId==null || busUnit.inBusId==""){
            fsn.initNotificationMes("没有选择销往(退货商)",false);
            return false;
        }
        if($("#return_time").val().trim()!=""){
            busUnit.outDate = $("#return_time").val().trim();
        }else{
            fsn.initNotificationMes("请选择退货时间",false);
            return false;
        }
        return true;
        /*var data = PRODUCT_DETAIL_GRID.dataSource.data();
        var pArr = getReturnProduct("topDetail",data);
        busUnit.proList = [];
        if(pArr!=null&&pArr.length>0){
            for(var x = 0 ; x< pArr.length;x++){
            	var prodate = pArr[x].productionDate;
          		if(!prodate || prodate.indexOf("选择")>-1){//判断是否选择生产日期
          			fsn.initNotificationMes("第"+(x+1)+"条产品请选择生产日期！",false);
          			return false;
          			break;
          		}
          		
            	var problem_describe = pArr[x].problem_describe;
          		if(problem_describe == null || problem_describe == '' || problem_describe.indexOf("选择")>-1){//判断是否选择问题描述
          			fsn.initNotificationMes("第"+(x+1)+"条产品请选择问题描述！",false);
          			return false;
          			break;
          		}
          		
            	var returnCount = pArr[x].returnCount;
          		if(returnCount == null || returnCount == '' || returnCount == 0 || returnCount == '0'){//判断退货数量
          			fsn.initNotificationMes("第"+(x+1)+"条产品请输入退货数量！",false);
          			return false;
          			break;
          		}
          		
            	var qs = $.md5(pArr[x].qsNumber+pArr[x].uuid);
                pArr[x].overDate = $("#overDate"+pArr[x].productId+qs).html();
                pArr[x].batch = $("#batch"+pArr[x].productId+qs).html();
                busUnit.proList.push(pArr[x]);
            };
        }else{
            fsn.initNotificationMes("没有选择退货的产品",false);
            return false;
        }
        if(busUnit.outBusId==null || busUnit.outBusId==""){
            fsn.initNotificationMes("没有退货商信息",false);
            return false;
        }
        if(busUnit.inBusId==null || busUnit.inBusId==""){
            fsn.initNotificationMes("没有选择销往(退货商)",false);
            return false;
        }
        if($("#return_time").val().trim()!=""){
            busUnit.outDate = $("#return_time").val().trim();
        }else{
            fsn.initNotificationMes("请选择退货时间",false);
            return false;
        }
        return true;*/
    	//modify by ltg 20160815 由于增加了重复判断导致原语句会提示重复问题 end
    }

    relation.noSubmit = function(){
        CONFIRM_SUBMIT.close();
    }

    relation.yesSubmit = function(){
        var data = busUnit;
        CONFIRM_SUBMIT.close();
        $("#saving_msg").html("正在提交数据，请稍候...");
        LOADING.open().center();
        $.ajax({
            url: HTTPPREFIX + "/account/relation/submit",
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function (returnVal) {
                if (returnVal.result.status == "true") {
                	LOADING.close();
                    if(returnVal.save==true){
                        if(returnVal.saveBus!=null){
                            busUnit.id = returnVal.saveBus.id;
                            PRODUCT_DETAIL_GRID.setDataSource(getProductDetailDS(returnVal.saveBus.id));
                        }
                        if(status=="submit"){
                            $("#save").hide();
                            $("#submit").hide();
                            $("#select_bar").hide();
                            $("#select_lic").hide();
                        }
                        fsn.initNotificationMes("保存成功", true);
                        if(data.outStatus==1){
                            window.location.href = "returnGoodsManage.html";
                        }
                    }
                } else {
                	LOADING.close();
                    fsn.initNotificationMes("企业信息修改失败", false);
                }
            }
        });
    }

    /**
     * 提交页面信息
     */
    relation.submit = function(status){
        if(!returnProductInfo(status)){
            return;
        }
        if(status=="submit"){
        	//提交
            CONFIRM_SUBMIT.open().center();
        }else{
        	//保存
            relation.yesSubmit();
        }
    }
    /* 返回 */
    relation.goBack = function(){
        window.location.href="returnGoodsManage.html";
    }

    function editPrice(container, options){
        var input = $('<input min="0.00" value="0.00"/>');
        buildNumericTextBox(input,container, options);
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

    BUS_COLUMNS = [
        {field: "id",title: fsn.l("Id"),width: 30,filterable: false},
        {field: "busName",title: fsn.l("Name"),width: 70,filterable: false},
        {field: "licNo",title: fsn.l("营业执照号"),width: 50,filterable: false},
        {field: "type",title: fsn.l("类型"),width: 20,filterable: false,template:
            function(item){
                if (item.type == 1) {
                    return "销往";
                }else{
                    return "来源";
                }
            }}
    ];

    PRODUCT_COLUMNS = [ {field: "id",title: fsn.l("Id"),width: 1,filterable: false},
        { title: "<input type='checkbox' id='topCheckBox' onclick='return fsn.relation.selectAll(this)'/>选择", width: 30,
            template: function (e) {
            	//modify by ltg 20160812 增加商品重复判断 start
//                var tag = "<input name='topCheckBox' type='checkbox' id='"+ e.productId+'|'+e.qsNumber +'|'+e.uuid +"' class='grid_checkbox'/>";
                var tag = "<input name='topCheckBox' type='checkbox' id='"+ e.productId+'|'+e.qsNumber +'|'+e.uuid +'|'+e.barcode +"' class='grid_checkbox'/>";
            	//modify by ltg 20160812 增加商品重复判断 end
                return tag;
            }
        },
        {field: "name",title: fsn.l("Name"),width: 80,filterable: false},
        {field: "barcode",title: fsn.l("商品条形码"),width: 60,filterable: false},
        {field: "qsNumber",title: fsn.l("QS号"),width: 60,filterable: false}
    ];

    PRODUCT_DETAIL_COLUMNS = [ {field: "id",title: fsn.l("Id"),width: 1,filterable: false},
        {width: 50, title: "<input type='checkbox' id='topDetail' onclick='return fsn.relation.selectAll(this)'/>选择",
            template: function (e) {
            	//modify by ltg 20160812 增加商品重复判断 start
                //var tag = "<input name='topDetail' type='checkbox' id='"+ e.productId+'|'+e.qsNumber +'|'+e.uuid +"' class='grid_checkbox'/>";
                var tag = "<input name='topDetail' type='checkbox' id='"+ e.productId+'|'+e.qsNumber +'|'+e.uuid +'|'+e.barcode +"' class='grid_checkbox'/>";
            	//modify by ltg 20160812 增加商品重复判断 end
                return tag;
            }
        },
        {/*field: "barcode",filterable: false,editable:false,*/width: 70, title: fsn.l("条形码"),
            template: function (e) {
                var tag = "<label>"+ e.barcode+"</label>";
                return tag;
            }
        },
        {width: 70, title: fsn.l("QS号"),
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
        {width: 60, title: fsn.l("规格型号"),
            template: function (e) {
                var tag = "<label>"+ e.format+"</label>";
                return tag;
            }
        },
        {title: fsn.l("库存数量"),filterable: false,width: 60,
            template: function (e) {
                var tag = "<label>"+ e.count+"</label>";
                return tag;
            }
        },
        {field: "returnCount",title: fsn.l("退货数量"),filterable: false,editor:
            function(container, options) {
                var input = $("<input min='0' max='"+options.model.count+"' value='0'/>");
                input.attr("name", options.field);
                input.appendTo(container);
                input.kendoNumericTextBox();
                input.data("kendoNumericTextBox").bind("spin",change);
                function change() {
                    var value = input.data("kendoNumericTextBox").value();
                    var stockNum = options.model.count;
                    if(value>stockNum){
                        fsn.initNotificationMes("退货数量超出库存数量",false);
                    }
                };
            },width: 50},
        {field: "price",title: fsn.l("单价"),width: 40,filterable: false,editor:
            function(container, options) {
                var input = $("<input min='0' step='1' type='number'/>");
                input.attr("name", options.field);
                input.appendTo(container);
                input.kendoNumericTextBox();
            }},
        {
            field: "productionDate", title: fsn.l("生产日期"), width: 50, filterable: false,
            editor:function(container, options) {
                var item = options.model;
                var tag = "<option selected='selected'>选择生产日期</option>";
                var list = item.birthDateList;
                if(list!=null && list.length > 0){
                    for(var x=0;x<list.length;x++){
                        tag += "<option id='"+ list[x].batch +"' title='"+ list[x].overDate +"' value='"+ list[x].instanceId +"'>"+list[x].birthDate+"</option>"
                    }
                }
                var selectTag = $("<select class='k-select k-textbox' style='width: 120px;' onchange='return fsn.relation.selectDate(this)'>"+ tag+"</select>");

                selectTag.appendTo(container);

                relation.selectDate = function(obj){
                    var index = obj.selectedIndex; // 选中索引
                    var overDate = obj.options[index].title;
                    var text = obj.options[index].text;
                    var batch = obj.options[index].id;
                    item.batch = batch;
                    item.overDate = overDate;
                    var uuid = options.model.uuid;
                    var qs = options.model.qsNumber;
                    qs = $.md5(qs==undefined?"":qs+uuid);
                    options.model.productionDate = text;
                    $("#batch"+options.model.productId+qs).html(batch);
                    $("#overDate"+options.model.productId+qs).html(overDate);
                }
            }
        },
        {width: 50, title: fsn.l("批次"),
            template: function (e) {
                var batch = e.batch;
                if(e.batch==null) batch="";
                var uuid = e.uuid;
                var qs = $.md5(e.qsNumber==undefined?"":e.qsNumber+uuid);
                var tag = "<label id='batch"+ e.productId+qs+"'>"+ batch +"</label>";
                return tag;
            }
        },
        {width: 60, title: fsn.l("过期日期"),
            template: function (e) {
            	 var uuid = e.uuid;
                 var qs = $.md5(e.qsNumber==undefined?"":e.qsNumber+uuid);
                var tag = "<label id='overDate"+e.productId+qs+"'>"+ fsn.formatGridDate(e.overDate)+"</label>";
                return tag;
            }
        },
        {
            field:'problem_describe',title: fsn.l("问题描述"), width: 60, filterable: false,
            editor:function(container, options) {
                var tag = "<option value=''>选择问题描述</option>";
                //modify by ltg 20160812 增加回收原因由后台枚举类返回 start
                for (var index in recycle_reason) {
                	var obj = recycle_reason[index];
        			var name = obj.name;
        			var value = obj.value;
        			tag += "<option title='" + name + "' value='" + value + "'";
        			if (options.model.problem_describe == name) {
        				tag += ' selected';
        			}
        			tag += ">" + name + "</option>";
        		}
                /*tag += "<option title='国家强制召回' value='国家强制召回'";
                if (options.model.problem_describe == '国家强制召回') {
                	tag += ' selected';
                }
                tag += ">国家强制召回</option>";
                tag += "<option title='产品临期' value='产品临期'";
                if (options.model.problem_describe == '产品临期') {
                	tag += ' selected';
                }
                tag += ">产品临期</option>";
                tag += "<option title='购货商退货' value='购货商退货'";
                if (options.model.problem_describe == '购货商退货') {
                	tag += ' selected';
                }
                tag += ">购货商退货</option>";
                tag += "<option title='其他' value='其他'";
                if (options.model.problem_describe == '其他') {
                	tag += ' selected';
                }
                tag += ">其他</option>";*/
                //modify by ltg 20160812 增加回收原因由后台枚举类返回 end
                
                //var selectTag = $("<select class='k-select k-textbox' style='width: 120px;' onchange='return fsn.relation.selectDescribe(this)'>"+ tag+"</select>");
                var selectTag = $("<input onchange='return fsn.relation.selectDescribe(this)' />");
                selectTag.appendTo(container);
//                relation.selectDescribe = function(obj){
//                    var index = obj.selectedIndex; // 选中索引
//                    var overDate = obj.options[index].title;
//                    var text = obj.options[index].text;
//                    options.model.problem_describe = text;
//                }
                relation.selectDescribe = function(obj){
//                    var val = $(obj).val(); // 选中索引
//                    console.log(obj.val());
                    options.model.problem_describe = obj.value;
                }
            }
        }
    ];

    function getBusDS(name,lic){
        return new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/relation/returnBus/2/" + options.page + "/" + options.pageSize + "?name=" + name + "&lic=" + lic;
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
                    return returnValue.total!=null?returnValue.total:0;
                }
            },
            serverPaging : true,
            serverFiltering : true,
            serverSorting : true
        });
    }

    function getProductDS(name,bar){
        return new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/relation/returnGoods/" + options.page + "/" + options.pageSize + "?name=" + name + "&bar=" + bar;
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

    function getProductDetailDS(outId){
        PRODUCT_DETAIL_DS = new kendo.data.DataSource({
            transport: {
                read: {
                    url : function(options){
                        return HTTPPREFIX + "/account/relation/loadDetail/0/0?outId="+outId;
                    },
                    dataType : "json",
                    contentType : "application/json"
                },
                parameterMap : function(options, operation) {
                    if (operation == "read") {
                        if($("#topDetail").is(':checked')){
                            document.getElementById("topDetail").checked = false;
                        }
                        //add by ltg 20160812 增加分页信息 start
                        var parameter = {
                        		page : options.page,//当前页
                        		pageSize : options.pageSize//每页显示个数
                        };
                        return kendo.stringify(parameter);
                        //add by ltg 20160812 增加分页信息 end
                    }
                }
            },
            batch : true,
            //add by ltg 20160812 增加分页信息 start
            pageSize : 10,
            //add by ltg 20160812 增加分页信息 end
            schema: {
                data : function(returnValue) {
                	//modify by ltg 20160815 将问题描述的值显示为对应名称 start
                	var data = [];
                	if (returnValue.data != null) {
                		data = returnValue.data;
                		for (var index in data) {
                			var problem_describe = data[index].problem_describe;
                			if (problem_describe != null && problem_describe != '' &&
                					valueToName_jo[problem_describe] != null && valueToName_jo[problem_describe] != '') {
                				data[index].problem_describe = valueToName_jo[problem_describe];
                			}
                		}
                	}
                    return data;
                    //return returnValue.data!=null?returnValue.data:[];
                    //modify by ltg 20160815 将问题描述的值显示为对应名称 end
                },
                total : function(returnValue) {
                    return returnValue.totals!=null?returnValue.totals:0;
                }
            },
            serverFiltering : true,
            serverSorting : true
        });
        return PRODUCT_DETAIL_DS;
    }

	initialize();
});