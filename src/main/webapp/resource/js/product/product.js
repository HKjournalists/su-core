var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
var	upload = portal.upload = portal.upload || {};

portal.onSelectUnitName=function(e){
	 var unitName = this.dataItem(e.item.index());
    $("#productUnit").val(unitName);
};

portal.onSelectBusName=function(e){
	 var busUnitName = this.dataItem(e.item.index());
    $("#businessName").val(busUnitName);
};

portal.onSelectBrand=function(e){
	 var brand = this.dataItem(e.item.index());
    $("#businessBrand").val(brand);
};

$("#productUnit").kendoAutoComplete({
    dataSource: lims.getAutoLoadDsByUrl("/product/getAllUnitName"),
    filter: "contains",
    placeholder: "例如：60g、箱、500ml/瓶、2瓶/盒、克/袋",
    select: portal.onSelectUnitName,
}); 

/**
 * 初始化生产企业的企业名称和商标控件
 */
portal.initBusNameAndBrandComplete=function(){
	$("#businessName").kendoAutoComplete({
         dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitName"),
         filter: "contains",
         placeholder: "搜索...",
         select: portal.onSelectBusName,
     });
	$("#businessBrand").kendoAutoComplete({
         dataSource: lims.getAutoLoadDsByUrl("/business/business-brand/getAllName"),
         filter: "contains",
         placeholder: "搜索...",
         select: portal.onSelectBrand,
     });
};

/**
 * 获取当前登录用户所属企业类型
 * @author Zhanghui 2015/4/3
 */
portal.getBusType = function(){
	var type = "";
	$.ajax({
		url:portal.HTTP_PREFIX+"/business/getCurrentBusiness",
		type:"GET",
        async:false,
		success:function(returnValue){
			if(returnValue.result.status == "true"){
				portal.enterpriseName = returnValue.data.name;
				type = returnValue.data.type;
			}
		}
	});
	return type;
};

/**
 * 根据企业类型初始化页面
 * @author Zhanghui 2015/4/3
 */
portal.initComponentByType = function() {
	portal.busType = portal.getBusType();
	if(portal.busType == "流通企业"){
		portal.isLiuTong = true;
		$(".div_busNotSC").css("display","inline");
		$(".div_dealer").css("display","none");
		$(".div_busSC").css("display","none");
		portal.initBusNameAndBrandComplete(); // 初始化所属品牌
	}else if(portal.busType == "流通企业.供应商"){
		portal.isLiuTong = true;
		$(".div_busNotSC").css("display","inline");
		$(".div_busSC").css("display","none");
		portal.initBusNameAndBrandComplete(); // 初始化所属品牌、企业名称控件
		portal.initCustomerMultiSelect(); // 初始化[销往企业]多选框控件
		portal.initCustomerMultiSelect_lead(); // 初始化引进产品[销往企业多选框]控件
		portal.fillCustomerSelect();
	}else{
		/* 生产企业、仁怀酒企 */
		portal.isLiuTong = false;
		upload.Product.initialBrands();    // 初始化所属品牌
		$("#businessName").val(portal.enterpriseName);
	}
};

/**
 * 主动设置[销往企业]长度，否则会影响页面展示
 * @author Zhanghui 2015/4/3
 */
portal.setCustomerWidth = function () {
	$("#customerSelect_taglist").attr("style","width:430px;");
};

/**
 * 填充保质期
 * @author TangXin
 */
portal.setExpirDay = function(value){
	$("#proExpirYear").val("");
	$("#proExpirMonth").val("");
	$("#proExpirDay").val("");
	if(value==null){
		return;
	}
	var tempv=value.trim().split("年");
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
	$("#proExpirYear").val(year=="0"?"":year);
    $("#proExpirMonth").val(month=="0"?"":month);
    $("#proExpirDay").val(day=="0"?"":day);
};

/**
 * 封装页面保质日期
 * @author TangXin
 */
portal.getExpirDay=function(){
   	var result="";
   	var year=$("#proExpirYear").val();
   	var month=$("#proExpirMonth").val();
   	var day=$("#proExpirDay").val();
   	year=year.trim().length<1?"0":year.trim();
   	month=month.trim().length<1?"0":month.trim();
   	day=day.trim().length<1?"0":day.trim();
   	var re1 = /^[0-9]*$/;
   	if (!year.match(re1)||!month.match(re1)||!day.match(re1)) {
   		return $("#proExpirOther").val();
   	}
   	if(year!="0"){
   		result+=year+"年";
   	}
   	if(month!="0"){
   		result+=month+"月";
   	}
   	if(day!="0"){
   		result+=day+"天";
   	}
   	return result;
};

/**
 * 将保质日期转换成保质天数
 * @author TangXin
 */
portal.countExpirday=function(){
   	$("#foodinfo_expirday").val("");
   	var result;
   	var year=$("#proExpirYear").val();
   	var month=$("#proExpirMonth").val();
   	var day=$("#proExpirDay").val();
   	year=year.trim().length<1?"0":year.trim();
   	month=month.trim().length<1?"0":month.trim();
   	day=day.trim().length<1?"0":day.trim();
   	if(year=="0"&&month=="0"&&day=="0"){
   		return;
   	}
   	var rel=/^[0-9]*$/;
   	if (!year.match(rel)||!month.match(rel)||!day.match(rel)) {
   		return;
   	}
   	result=year*365+month*30+day*1;
   	if(result!=0){
   		$("#expirationDate-num").val(result);
           return result;
   	}
};

/**
 * 对用户输入的保质期进行数据规范验证
 * @author TangXin
 */
portal.validateExpiration = function(){
	 /* 1. 验证年份  */
	 var proExpirYear = $("#proExpirYear").val();
	 var proExpirMonth = $("#proExpirMonth").val();
	 var proExpirDay = $("#proExpirDay").val();
    if(proExpirYear ==""&&proExpirMonth==""&&proExpirDay==""){
        $("#expirationDate-num").val("");
    }
	 if(!proExpirYear.match(/^[0-9]*$/)){
		 lims.initNotificationMes("【保质期】的年份应该是数字！", false);
		 return false;
	 }else{
		 portal.countExpirday();
	 }
	 /* 2. 验证月份  */
	 if(!proExpirMonth.match(/^[0-9]*$/)){
		 lims.initNotificationMes("【保质期】的月份应该是数字！", false);
		 return false;
	 }else{
		 portal.countExpirday();
	 }
	 /* 3. 验证天数 */
	 if(!proExpirDay.match(/^[0-9]*$/)){
		 lims.initNotificationMes("【保质期】的天数应该是数字！", false);
		 return false;
	 }else{
		 portal.countExpirday();
	 }
	 return true;
};

/**
 * 保存报告之前验证产品类别是否为系统中的类别选项，
 * @author TangXin
 */
portal.validateCategory = function(categoryName){
	 var isExist = true;
	 $.ajax({
		   url: portal.HTTP_PREFIX + "/testReport/validatCategory/" + categoryName,
		   type: "GET",
		   dataType: "json",
		   async:false,
		   contentType: "application/json; charset=utf-8",
		   success: function(returnValue) {
		  	 if(returnValue.data != null){
		  		$("#category").data("kendoComboBox").value(returnValue.data.code);
		  		isExist = true;
		  	 }else{
		  		 isExist=false;
		  	 }
		   }
	 });
	 return isExist;
};

/**
 * 保存报告之前验证品牌是否为系统中的类别选项，
 * @author TangXin
 */
portal.validateBrandName=function(brandName){
	 var isExist = true;
	 $.ajax({
		    url: portal.HTTP_PREFIX + "/business/validateBrand/" + brandName,
		    type: "GET",
		    dataType: "json",
		    async:false,
		    contentType: "application/json; charset=utf-8",
		    success: function(returnValue) {
			   	 if(returnValue.status == true){
			   		isExist =false;
			   	 }
		    }
	 });
	 return isExist;
};

/**
 * 清空产品图片资源
 * @author TangXin
 */
portal.clearProFiles=function(){
	portal.aryProAttachments.length=0;
	$("#proAttachmentsListView").data("kendoListView").dataSource.data([]);
	$("#logListView").hide();
};


/**
 * 初始化kendoWindow窗体
 */
portal.initKendoWindow=function(winId,title,width,height,isVisible,actions){
	$("#"+winId).kendoWindow({
		title:title,
		width:width,
		height:height,
		modal:true,
		visible:isVisible,
		actions:actions!=null?actions:["Close"],
	});
};

/**
 * 按营养报告的不同列名查找不同列的集合信息
 * @param {Object} colName 当前点击的列号
 */
portal.getAutoItemsDS = function(colName){
	 return new kendo.data.DataSource({
		 transport: {
			 read: {
				 url:portal.HTTP_PREFIX +"/product/autoItems/"+colName,
				 type:"GET",
			 }
		 },
		 schema: {
			 data: "data"
		 }
	 });
 };
 
 portal.buildAutoComplete = function(input, container, options, dataSource){
	 input.attr("name", options.field);
	 input.attr("class", "k-textbox");
	 input.appendTo(container);
	 input.kendoAutoComplete({
		 dataSource: dataSource,
	     filter:"contains",
	 });
 };
 
 portal.buildDatePicker = function(container,options){
	 var input = $("<input/>");
	 input.attr("name", options.field);
	 input.appendTo(container);
	 input.kendoDatePicker({
    	 format: "yyyy-MM-dd",
    	 height:30,
    	 value: options.model.endDate==""?null:new Date(options.model.endDate),
    	 culture : "zh-CN",
    	 animation: {
    	   close: {
    	     effects: "fadeOut zoom:out",
    	     duration: 300
    	   },
    	   open: {
    	     effects: "fadeIn zoom:in",
    	     duration: 300
    	   }
    	  }
    	});
 };