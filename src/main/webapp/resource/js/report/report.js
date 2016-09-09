var lims = window.lims = window.lims || {};
var fsn = window.fsn = window.fsn || {};
var portal = fsn.portal = fsn.portal || {};
var root = window.lims.root = window.lims.root || {};

/**
 * 背景：报告录入页面，报告预览页面
 * 功能描述：报告信息赋值操作（包括产品信息赋值，以及生产企业信息赋值） -- 对外方法
 * @param product_html_type
 * 				product_input 代表 产品信息使用input输入框赋值
 * 				product_text  代表 产品信息使用text赋值
 * @author ZhangHui 2015/6/5
 */
fsn.setReportValue = function(report, product_html_type){
	 if(!report){
		 return;
	 }
	 
	 /**
	  * 第一步：报告赋值
	  */
	 
	 //每次赋值之前都应该先将日期的值清空
	 $("#reportEndDate").val("");
	 $("#tri_proDate").val("");
	 $("#tri_testDate").val("");
	 $(".busnameSelectDiv").hide();
	 
	 $("#tri_reportNo").val(report.serviceOrder?report.serviceOrder:"");
	 $("#tri_testee").val(report.testee?report.testee:"");
	 $("#tri_testOrg").val(report.testOrgnization?report.testOrgnization:"");
	 $("#tri_testPlace").val(report.testPlace?report.testPlace:"");
	 $("#reportEndDate").val(report.endDateStr?report.endDateStr:"");
	 
	 var conclusionDrop = $("#tri_conclusion").data("kendoDropDownList");
	 if(conclusionDrop){
		 if(report.id==null){
			 conclusionDrop.value("1");
		 }else{
			 conclusionDrop.value(report.pass?"1":"0");
		 }
	 }else{
		 $("#tri_conclusion").val(report.pass?"合格":"不合格");
	 }
	 
	 // 生产企业下拉选择
	 var listBusNameCombox =  $("#hid_listBusName").data("kendoComboBox");
	 if(listBusNameCombox){
		 if(report.listOfBusunitName!=null && report.listOfBusunitName.length>0){
			 root.listOfBusunitName = report.listOfBusunitName;
			 root.pro2Bus = report.pro2Bus;
			 $(".busnameSelectDiv").show();
			 listBusNameCombox.setDataSource(report.listOfBusunitName);
		 }else{
			 hideListBusNameSelect();
		 }
	 }
	 
	 $("#sampleAds").val(report.samplingLocation?report.samplingLocation:"");  // 抽样地点
	 $("#sampleCounts").val(report.sampleQuantity?report.sampleQuantity:"");  // 抽样量
	 $("#judgeStandard").val(report.standard?report.standard:"");
	 $("#testResultDescribe").val(report.result?report.result:"");
	 $("#remarks").val(report.comment?report.comment:"");
	 $("#tri_testDate").val(report.testDateStr?report.testDateStr:"");
	 
	 var testTypeDrop =  $("#tri_testType").data("kendoDropDownList");
	 if(testTypeDrop&&!portal.type){
		 $("#tri_testType").data("kendoDropDownList").value(report.testType);
		 if(report.testType=='第三方检测'){
		 	$("#tri_testType").data("kendoDropDownList").refresh();
		 }
	 }else{
		 if($("#tri_testType").data("kendoDropDownList")==undefined){
			 $("#tri_testType").val(report.testType?report.testType:"企业自检");
		 }else{
			 $("#tri_testType").data("kendoDropDownList").value(report.testType?report.testType:"企业自检");
		 }
	 }
	 
	 var autoElement =  document.getElementById("yesAuto");
	 if(autoElement){
		 if(report.autoReportFlag){
			 $("#yesAuto").attr("checked","checked");
	         document.getElementById("yesAuto").checked=true;
			 $("#tr_isAuto").hide();
		 }else{
			 document.getElementById("noAuto").checked=true;
			 setRepAttachments(report.repAttachments?report.repAttachments:[]);
			 $("#tr_isAuto").show();
		 }
		 
		 root.isAutoReport = report.autoReportFlag?report.autoReportFlag:false;
	 }else{
		 setRepAttachments(report.repAttachments?report.repAttachments:[]);
	 }
	 if(root.reportImgList){
	 	setAttachments("reportImgListView",report.reportImgList==null?[]:report.reportImgList,root.reportImgList,'root.reportImgList');
	 }
	 if(root.checkImgList){
	 	setAttachments("checkImgListView",report.checkImgList==null?[]:report.checkImgList,root.checkImgList,'root.checkImgList');
	 }
	 if(root.buyImgList){
	 	setAttachments("buyImgListView",report.buyImgList==null?[]:report.buyImgList,root.buyImgList,'root.buyImgList');
	 }
	 var itemGrid = $("#report_grid").data("kendoGrid");
	 if(itemGrid){
		 itemGrid.dataSource.data(report.testProperties?report.testProperties:[]);
	 }
	 
	 /**
	  * 退回原因
	  * 	2 代表 testlab 退回至企业
	  * 	5 代表 商超退回至供应商
	  * 	7 代表 食安云退回至供应商
	  */
	 if(root.isNew==false && (report.publishFlag == '2' || report.publishFlag == '5' || report.publishFlag == '7')){
         //lims.msg("backMsg", null, report.backResult);
		 var html=report.backResult;
		 html+='<div>';
		 for(var i in report.repBackAttachments){
			 html+='<a style="float:left;margin-right:10px;" target="_blank" href="'+report.repBackAttachments[i].url+'"><img src="'+report.repBackAttachments[i].url+'" width="100" /></a>';
		 }
		 html+='</div>';
		 fsn.backMsg("backMsg",html);
		 //fsn.initBackMes(report.backResult);
     }
	 
	 /**
	  * 第二步：产品信息赋值
	  */
	 setProductValue(report.product_vo, product_html_type);
	 
	 /**
	  * 第三步：生产企业信息赋值
	  */
	 setBusunitValue(report.bus_vo);
	 
	 
	 /**
	  * 第四步：如果是进口食品报告，给进口食品报告赋值
	  * @author longxianzhen 2015/06/16
	  */
	 if(report.impProTestResult!=null){
		setImpProTestInfo(report.impProTestResult);
	 }else{
		$("#sanPdfRepAtt_tr").hide();
		$("#sanNo_tr").hide();
	 }
	 
};

/**
 * 背景：报告录入页面，报告预览页面
 * 功能描述：进口食品报告信息赋值操作
 * @author longxianzhen 2015/06/16
 */
setImpProTestInfo=function(impProTestResult){
	if($("#sanPdfRepAttachments")){
		 var repAttDS = new kendo.data.DataSource();
		 if(impProTestResult.sanitaryPdfAttachments.length>0){
			 for(var i=0;i<impProTestResult.sanitaryPdfAttachments.length;i++){
				$("#sanPdfRepAtt_tr").show();
				$("#sanNo_tr").show();
				repAttDS.add({attachments:impProTestResult.sanitaryPdfAttachments[i]});
			 }
		 }else{
			 $("#sanPdfRepAtt_tr").hide();
			 $("#sanNo_tr").hide();
		 }
		 $("#sanPdfRepAttachments").kendoListView({
	         dataSource: repAttDS,
	         template:kendo.template($("#uploadedFilesTemplate").html()),
	     });
		 $("#sanNo").val(impProTestResult.sanitaryCertNo);
	}else{
		 $("#sanPdfRepAtt_tr").hide();
		 $("#sanNo_tr").hide();
	}
};

/**
 * 背景：报告录入页面，报告预览页面
 * 功能描述：产品信息赋值操作
 * @author ZhangHui 2015/6/5
 */
setProductValue = function(product, product_html_type){
	 if(!product){
		 product = {
			 can_edit_pro: true,
		 };
	 }
	
	 // 将产品信息输入框设置为可编辑
	 setProductInfoToEdit();
	 
	 if(product_html_type == "product_input"){
		 $("#barcodeId").val(product.barcode?product.barcode:"");
		 $("#foodName").val(product.name?product.name:"");
		 $("#specification").val(product.format?product.format:""); // 规格
		 $("#foodinfo_brand").val(product.brand_name?product.brand_name:"");  // 商标
		 $("#regularity").val(product.regularityStr?product.regularityStr:""); // 执行标准
		 $("#foodinfo_expiratio").val(product.expiration?product.expiration:"");  // 质保期
		 $("#foodinfo_expirday").val(product.expirationDate?product.expirationDate:"");  // 保质天数
		 $("#foodinfo_minunit").val(product.unit?product.unit:"");  // 单位
		 $("#foodInfo_Status").val(product.status?product.status:"");
		 
		 // 产品保质期赋值操作
		 setExpirDay(product.expiration);
	 }else if(product_html_type == "product_text"){
		 // 兼容经销商报告录入页面，条形码赋值
		 $("#barcodeId").val(product.barcode?product.barcode:"");
		 // 兼容预览时条形码赋值
		 $("#barcodeId").text(product.barcode?product.barcode:"");
		 // 兼容生产企业报告录入页面，条形码赋值
		 var barCombox = $("#barcodeId").data("kendoComboBox");
		 if(barCombox){
			 barCombox.value(product.barcode?product.barcode:"");
		 }
		 
		 $("#foodName").text(product.name?product.name:"");
		 $("#specification").text(product.format?product.format:""); // 规格
		 $("#foodinfo_brand").text(product.brand_name?product.brand_name:"");  // 商标
		 $("#regularity").text(product.regularityStr?product.regularityStr:""); // 执行标准
		 $("#foodinfo_expiratio").text(product.expiration?product.expiration:"");  // 质保期
		 $("#foodinfo_expirday").text(product.expirationDate?product.expirationDate:"");  // 保质天数
		 $("#foodinfo_minunit").text(product.unit?product.unit:"");  // 单位
		 $("#foodInfo_Status").text(product.status?product.status:"");
		 $("#proExpiratio").text(product.expiration?product.expiration:""); // 质保期
	 }
	 $("#expiration_date").val(product.expiration?product.expiration:""); // 质保期
	 $("#inputDay").val(product.expirationDate?product.expirationDate:"");  // 保质天数
	 
	 // 产品分类赋值
	 portal.setCategoryValue(product.category);
	 
	 // 产品图片页面赋值展示
	 setProAttachments(product.proAttachments?product.proAttachments:[]);
	 
	 // 如果当前产品被生产企业接管，则不允许流通企业编辑
	 if(!product.can_edit_pro){
		 root.current_product_vo_has_claim = product;
		 setProductInfoToReadOnly();
	 }
	 
	 $("#tri_batchNo").val(product.batchSerialNo?product.batchSerialNo:"");  // 批次
     $("#tri_proDate").val(product.productionDateStr?product.productionDateStr:""); // 生产日期
    
   
     portal.setTriPorDate(product.productionDateStr?product.productionDateStr:"");
};

portal.setTriPorDate = function(value){
	if(value==null||value==''){
		return;
	}
	var proDate = value.replace(/-/g,"/");
	var myDate = new Date();
	var myYear = myDate.getFullYear();
	var myMonth = myDate.getMonth()+1;
	var myDay = myDate.getDate();
	
	var sd = new Date(proDate);
	var ed = new Date(myYear+"/"+myMonth+"/"+myDay);
	
	var yearNum = ed.getYear() - sd.getYear();
	var monthNum = ed.getMonth()- sd.getMonth();
	var monn = ((yearNum*12+monthNum)*30+ed.getDate()-sd.getDate()+1);
	var inputDay = $("#inputDay").val(); 
	if(monn > inputDay){
		$("#tr_Message").show();
		$("#inputMessage").text("当前报告中的产品生产日期已超过产品保质期.");
	}else{
		$("#tr_Message").hide();
		$("#inputMessage").text('');	
	}
}

/**
 * 背景：报告录入页面，报告预览页面
 * 功能描述：生产企业信息赋值操作
 * @author ZhangHui 2015/6/5 
 */
setBusunitValue = function(bus){
	if(!bus){
		bus = {
			can_edit_bus: true,
			can_edit_qs: true,
		};
	}
	
	// 清除生产企业的所有不可编辑操作
	var qsNoFormat = $("#listqsFormat").data("kendoDropDownList");
    if(qsNoFormat){
        qsNoFormat.readonly(false);
        $("#proJianCheng").data("kendoDropDownList").readonly(false);
    }
	
	root.current_bus_vo_has_claim = null;  // 该变量用于记录：已经注册到平台的企业信息
		
//	clearInputReadOnly(["bus_address","bus_qsNo","bus_licenseNo"]);
		
	$(".editQsStyle span").html("");
	$("#bus_licenseNo").val("");
	$("#bus_address").val("");
	$(".editQsStyle span").html("");
    
	if(bus != null){
	    $("#bus_name").val(bus.name);
		$("#bus_licenseNo").val(bus==null?"":bus.licenseno);
		$("#bus_address").val(bus==null?"":bus.address);
		
		if(bus.can_edit_bus == false){
			root.current_bus_vo_has_claim = bus;
//			$(".editQsStyle span").html("注意：当前生产企业已经通过注册，此处不允许再修改企业基本信息。");
			$(".editQsStyle").css("display","inline");
//			addInputReadOnly(["bus_address","bus_licenseNo"]);
		}
	}
	
	var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
	if(qsFormatIt){
		if(bus!=null && bus.can_edit_qs==false){
			$(".editQsStyle span").html("注意：当前生产企业已经通过注册，并且已经对该产品绑定了QS证号，此处不允许再修改。");
			$(".editQsStyle").css("display","inline");
	        qsNoFormat.readonly(true);
	        $("#proJianCheng").data("kendoDropDownList").readonly(true);
			addInputReadOnly(["bus_qsNo"]);
		}
		
	    if(bus==null || bus.qs_vo==null){
	    	setQsValue(1, "");
	    }else{
	    	setQsValue(bus.qs_vo.licenceFormat?bus.qs_vo.licenceFormat.id:1, bus.qs_vo.qsNo);
	    }
	}else{
		if(bus==null || bus.qs_vo==null){
			$("#bus_qsNo").val("");
		}else{
			$("#bus_qsNo").val(bus.qs_vo.qsNo);
		}
	}
};

/**
 * qs号赋值
 * @author ZhangHui 2015/6/5
 */
setQsValue = function(licenceFormatId, qsNo){
	var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
	
    if(qsNo==null || qsNo==""){
    	if(qsFormatIt){
    		qsFormatIt.value(1);
    	}
    	$("#bus_qsNo").val("");
    	return;
    }
    
    qsFormatIt.value(licenceFormatId);
    
    var qsItem = qsFormatIt.dataItem();
    root.formetType = qsItem.formetType;
    root.formetValue = qsItem.formetValue;
    root.formetLength = qsItem.formetLength;
    if(qsItem.formetValue.indexOf("(?)")!=-1){
        $("#jianchengList").show();
        root.showJianCheng = true;
        var first = "";//省的简称
        var socend = qsNo.substring(0,2);//中间字母
        var third = qsNo.substr(2);//数字部分
        if(qsNo.indexOf("(")!=-1 && qsNo.indexOf(")")!=-1 ){
            first = qsNo.charAt(1);//省的简称
        	socend = qsItem.formetValue.replace("(?)","");//中间字母
        	third = qsNo.substr(5);//数字部分
        }
        root.setStyle(0);
        $("#proJianCheng").data("kendoDropDownList").text(first);
        $("#showQsFormat").html(socend);
        $("#bus_qsNo").val(third);
    }else{
    	root.setStyle(1);
        $("#jianchengList").hide();
        root.showJianCheng = false;
        $("#showQsFormat").html(qsItem.formetValue);
        if(qsNo != null){
        	$("#bus_qsNo").val(qsNo.replace(qsItem.formetValue, ""));
        }
    }
};

/**
 * 将产品信息输入框设置为可编辑
 * @author ZhangHui 2015/5/1
 */
setProductInfoToEdit = function(){
	$(".hos_tr").show();
	$("#btn_clearProFiles").show();
	$("#proAttachmentsListView button").show();
	$("#foodinfo_standard").removeAttr("readonly");
	$(".editProStyle").css("display","none");
	if($("#foodinfo_category").data("kendoComboBox")){
		$("#foodinfo_category").data("kendoComboBox").readonly(false);
	}
	if ($("#category1").data("kendoComboBox")) {
		$("#category1").data("kendoComboBox").readonly(false);
	}
	if ($("#category2").data("kendoComboBox")) {
		$("#category2").data("kendoComboBox").readonly(false);
	}
	if ($("#category3").data("kendoComboBox")) {
		$("#category3").data("kendoComboBox").readonly(false);
	}
	
	$("#regularityDiv").unbind("click");
	$("#regularityDiv").click(portal.searchRegularity);
	
	$("table.productTable input").removeAttr("readonly");
	$("table.productTable button").removeAttr("disabled");
};

/**
 * 产品保质期赋值操作
 * @author ZhangHui 2015/5/1
 */
setExpirDay = function(value){
	$("#proExpirOther").val("");
	$("#proExpirYear").val("");
	$("#proExpirMonth").val("");
	$("#proExpirDay").val("");
	
	if(value==null){
		return;
	}
	
	var tempv=value.trim().replace("个","").split("年");
	var year="0";
	var month="0";
	var day="0";
	if(tempv.length>1){
		year=tempv[0].trim();
		tempv=tempv[1].trim().split("月");
	}else{
		tempv=tempv[0].trim().split("月");
	}
	if(tempv.length>1){
		month=tempv[0].trim();
		tempv=tempv[1].trim().split("天");
	}else{
		tempv=tempv[0].trim().split("天");
	}
	if(tempv.length>1){
		day=tempv[0];
	}
	if(year=="0"&&month=="0"&&day=="0"){
		$("#proExpirOther").val(value);
		return;
	}
	var rel = /^[0-9]*$/;
	if(!year.match(rel)||!month.match(rel)||!day.match(rel)){
		$("#proExpirOther").val(value);
		return;
	}
	$("#proExpirYear").val(year=="0"?"":year);
    $("#proExpirMonth").val(month=="0"?"":month);
    $("#proExpirDay").val(day=="0"?"":day);
};

/**
 * 产品图片页面赋值展示
 * @author ZhangHui 2015/4/30
 */
setProAttachments = function(proAttachments) {
    var dataSource = new kendo.data.DataSource();
    if(root.aryProAttachments){
    	root.aryProAttachments.length = 0;
    }
    if (proAttachments && proAttachments.length > 0) {
        $(".proListVStyle").show();
        $("#btn_clearProFiles").show();
        for (var i = 0; i < proAttachments.length; i++) {
        	if(root.aryProAttachments){
        		root.aryProAttachments.push(proAttachments[i]);
        	}
            
            dataSource.add({
                attachments: proAttachments[i]
            });
        }
    } else {
        $(".proListVStyle").hide();
    }
    /**
     * 判断使用预览小图片还是使用文件名来展示产品图片
     * proImgFilesTemplate ：预览小图片模板
     * uploadedFilesTemplate ：文件名模板 
     * @author LongXianzhen 2015/6/12
     */
    var proImgTemplate="";
    if(root.isViewImage){
    	proImgTemplate=$("#proImgFilesTemplate").html();
    }else{
    	proImgTemplate=$("#uploadedFilesTemplate").html();
    }
    $("#proAttachmentsListView").kendoListView({
        dataSource: dataSource,
        template: kendo.template(proImgTemplate),
    });
};

/**
 * 清除指定input框添的readonly属性
 */
clearInputReadOnly = function(listInputId){
	if(listInputId==null||listInputId.length<1){return;}
	for(var i=0;i<listInputId.length;i++){
		$("#"+listInputId[i]).removeAttr("readonly"); 
	}
};

/**
 * 给指定的input框添加readonly属性
 */
addInputReadOnly = function(listInputId){
	if(listInputId==null||listInputId.length<1){return;}
	for(var i=0;i<listInputId.length;i++){
		$("#"+listInputId[i]).attr("readonly",true);
	}
};

/*将产品信息输入框设置为只读*/
setProductInfoToReadOnly = function(){
	$(".editProStyle").css("display","inline");
	$(".hos_tr").hide();
	$("#btn_clearProFiles").hide();
	$("#proAttachmentsListView button").hide();
	$("#upload_warn_mes").attr("style", "display:none");
	$("#foodinfo_standard").attr("readonly","true");
	if($("#foodinfo_category").data("kendoComboBox")){
		$("#foodinfo_category").data("kendoComboBox").readonly(true);
	}
	if ($("#category1").data("kendoComboBox")) {
		$("#category1").data("kendoComboBox").readonly(true);
	}
	if ($("#category2").data("kendoComboBox")) {
		$("#category2").data("kendoComboBox").readonly(true);
	}
	if ($("#category3").data("kendoComboBox")) {
		$("#category3").data("kendoComboBox").readonly(true);
	}
	
	$("#regularityDiv").unbind("click");
	
	$("table.productTable input").attr("readonly","true");
	$("table.productTable button").attr("disabled","true");
	$("#barcodeId").removeAttr("readonly");
};

/**
 * 报告pdf页面赋值展示
 * @author ZhangHui 2015/4/30
 */
setRepAttachments = function(repAttachments) {
     var dataSource = new kendo.data.DataSource();
     if(root.aryRepAttachments){
    	 root.aryRepAttachments.length = 0;
     }
     
     if (repAttachments.length > 0) {
         addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
         $("#btn_clearRepFiles").show();
         for (var i = 0; i < repAttachments.length; i++) {
        	 if(root.aryRepAttachments){
        		 root.aryRepAttachments.push(repAttachments[i]);
        	 }
             
             dataSource.add({
                 attachments: repAttachments[i]
             });
         }
     }

     $("#repAttachmentsListView").kendoListView({
         dataSource: dataSource,
         template: kendo.template($("#uploadedFilesTemplate").html()),
     });
};

setAttachments = function(id,repAttachments,rootAttr,attrName) {
     var dataSource = new kendo.data.DataSource();
     rootAttr.length = 0;
     if (repAttachments.length> 0) {
        //lims.addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
         //$("#btn_clearRepFiles").show();
         for (var i = 0; i < repAttachments.length; i++) {
             rootAttr.push(repAttachments[i]);
             dataSource.add({
                 attachments: repAttachments[i],
                 viewId:id,
                 imgList:attrName
             });
         }
     }

     $("#"+id).kendoListView({
         dataSource: dataSource,
         template: kendo.template($("#uploadedImgFilesTemplate").html()),
     });
 };

/**
 * 卫生证pdf赋值展示
 * @author ZhangHui 2015/7/2
 */
setSanAttachments = function(sanAttachments) {
	 if(!root.sanRepAttachments){
		return;
	 }
     var dataSource = new kendo.data.DataSource();
     root.sanRepAttachments.length = 0;
     root.sanPdfRepAttachments.length = 0;
     
     if (sanAttachments.length > 0) {
         addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
         $("#btn_clearRepFiles").show();
         for (var i = 0; i < sanAttachments.length; i++) {
        	 if(root.sanPdfRepAttachments){
        		 root.sanPdfRepAttachments.push(sanAttachments[i]);
        	 }
             
             dataSource.add({
                 attachments: sanAttachments[i]
             });
         }
     }

     $("#sanAttachmentsListView").kendoListView({
         dataSource: dataSource,
         template: kendo.template($("#uploadedSanFilesTemplate").html()),
     });
};

/**
 * 强制隐藏可选生产企业下拉框
 * @author ZhangHui 2015/6/5
 */
hideListBusNameSelect = function(){
	root.listOfBusunitName = null;
	 root.pro2Bus = null;
	 $(".busnameSelectDiv").hide();
	 $("#hid_listBusName").data("kendoComboBox").setDataSource([]);
};

/**
 * 指定多个按钮id，设置button为disabled
 */
addDisabledToBtn = function(btnIds){
	 if(btnIds==null||btnIds.length<1) return;
	 for(var i=0;i<btnIds.length;i++){
		 $("#"+btnIds[i]).parent().addClass("k-state-disabled");
		 var element = document.getElementById(btnIds[i]);
		 if(element){
			 document.getElementById(btnIds[i]).disabled=true;
		 }
	 }
};

/**
 * 指定多个按钮id，移除button的disabled属性
 */
removeDisabledToBtn = function(btnIds){
	 if(btnIds==null||btnIds.length<1) return;
	 for(var i=0;i<btnIds.length;i++){
		 $("#"+btnIds[i]).parent().removeClass("k-state-disabled");
		 var element = document.getElementById(btnIds[i]);
		 if(element){
			 element.disabled=false;
		 }
	 }
};

/**
 * 清除页面报告信息   -- 对外方法
 * @author ZhangHui 2015/4/30
 */
fsn.clearReport = function() {
    var success = root.clearTempReport();
    if (success) {
        clearProduct();
        clearBusUnit();
        $("#ti_regist_no").val("");
        //root.clearTestee();
        $("#tri_reportNo").val("");
        $("#tri_testee").val("");
        $("#tri_testOrg").val("");
        $("#proExpirYear").val("");
        $("#proExpirMonth").val("");
        $("#proExpirDay").val("");
        $("#tri_conclusion").data("kendoDropDownList").value("1");
        $("#tri_testPlace").val("");
        $("#tri_proDate").val("");
        $("#tri_testDate").val("");
        $("#tri_batchNo").val("");
        $("#foodInfo_ads").val("");
        $("#sampleCounts").val("");
        $("#judgeStandard").val("");
        $("#testResultDescribe").val("");
        $("#remarks").val("");
        $("#sampleAds").val("");
        $("#tri_testType").data("kendoDropDownList").value("企业自检");
        
        root.aryProAttachments_new.length = 0;
        root.aryProAttachments.length = 0;
        $("#upload_pro").html("");
        $("#upload_pro").html("<input id='upload_prodPhoto_btn' type='file' />");
        root.buildUpload("upload_prodPhoto_btn", aryProAttachments_new, "fileEroMsg", "product");
        //清除自动加载进来的图片
        root.clearProFiles();
        root.clearRepFiles();

        $("#report_grid").data("kendoGrid").setDataSource(root.gridDataSource);
        lims.initNotificationMes("清除成功！", true);
    }
};

/**
 * 清空产品信息
 */
root.clearProduct = function() {
	$("#barcodeId").val("");
    $("#foodName").val("");
    $("#specification").val("");
    $("#foodinfo_model_no").val("");
    $("#foodinfo_brand").val("");
    $("#foodinfo_expiratio").val("");
    $("#foodinfo_expirday").val("");
    $("#foodinfo_minunit").val("");
    $("#foodInfo_Status").val("");
    $("#category1").data("kendoComboBox").value("");
    $("#category2").data("kendoComboBox").value("");
    $("#category3").data("kendoComboBox").value("");
    $("#regularity").val("");
    root.initialCategories();
    
    root.aryProAttachments_new.length = 0;
    root.aryProAttachments.length = 0;
    $("#upload_rep").html("");
    $("#upload_rep").html("<input id='upload_report_files' type='file' />");
    root.buildUpload("upload_report_files", root.aryProAttachments_new, "repFileMsg", "report");
};

/**
 * 清空页面生产企业信息
 */
clearBusUnit = function() {
	$(".hide").hide();
	var hlb = $("#hid_listBusName").data("kendoComboBox");
	if(hlb){
		hlb.setDataSource([]);
	}
    $("#bus_qsNo").val("");
    $("#bus_licenseNo").val("");
    $("#bus_name").val("");
    $("#bus_address").val("");
};