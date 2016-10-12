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
	 var proSta = $("#pro_sta").val();
	 if(proSta != undefined && proSta!=''){
		 $("#"+proSta).val(busUnitName);
	 }else{
		 $("#businessName").val(busUnitName);
	 };
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

portal.getBrandByComboBox = function(brandName){
	var brands = $("#businessBrand").data("kendoComboBox").dataSource.data();
	var brandCagegoryDS = new Array();
	if(brands!=null && brandName!=null){
		for(var i=0;i<brands.length;i++){
			if(brands[i].name == brandName){
				brandCagegoryDS.push({id:brands[i].id,name:(brands[i].brandCategory!=null?brands[i].brandCategory.name:"未知类别."+brandName)});
			}
		}
	}
	return brandCagegoryDS;
};

/**
 * MD5校验数据是否被篡改过
 * @author HuangYog
 */
portal.md5validate = function(str1,md5code){
	var md5 = $.md5(str1);
	if(md5===md5code){
		return str1;
	}else if(str1 && md5code){
		lims.initNotificationMes("产品id有误，请从产品管理编辑进入。", false);
		return ;
	}
};

/**
 * 主动设置[销往企业]长度，否则会影响页面展示
 * @author Zhanghui 2015/4/3
 */
portal.setCustomerWidth = function () {
	$("#customerSelect_taglist").attr("style","width:348px;");
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
 * 供应商：清空进口食品中文标签图片按钮
 * @author LongXianZhen
 */
portal.clearLabFiles=function(){
	var labList = $("#labAttachmentsListView").data("kendoListView");
	
	if(labList){
		portal.aryLabAttachments.length=0;
		
		$("#labAttachmentsListView").data("kendoListView").dataSource.data([]);
		$("#labListView").hide();
	}
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
 
/**
 * 产品条形码唯一性校验
 * @author Zhanghui 2015/4/29
 */
portal.validateBarcodeUnique = function(){
	/* 当前页面的barcode */
	var curBarcode = $("#barcode").val().trim();
	if(curBarcode.length<1) return;
	portal.barcodeIsExists = false;
	if(portal.currentBusiness.type=="流通企业.供应商"||portal.currentBusiness.type.indexOf("餐饮企业")!=-1){
		portal.getProductType();
		portal.setCountryByBar3(curBarcode);
	}
	/* 编辑 */
	if(curBarcode==portal.oldBarcode && !portal.isNew){
		return;
	}
	/* 判断当前barcode是否为引进barcode */
	$.ajax({
         url: portal.HTTP_PREFIX + "/product/validateBarcodeUnique/" + curBarcode,
         type: "GET",
         dataType: "json",
         async:false,
         contentType: "application/json; charset=utf-8",
         success: function(returnValue) {
        	 if(returnValue.isExist){
        		 portal.barcodeIsExists = true;
        		 /* 条形码已经存在 */
        		 if(returnValue.isLead){
        			 lims.initNotificationMes("条形码为" + curBarcode + "的产品已经引进，可前往产品管理界面查看详情！", true);
        		 }else if(portal.currentBusiness.type=="流通企业" || portal.currentBusiness.type == "流通企业.供应商"|| portal.currentBusiness.type.indexOf("餐饮企业")!=-1|| portal.currentBusiness.type.indexOf("流通企业.商超")!=-1){
        			 /* 弹出引进产品提示框 */
        			 portal.showLeadMsg();
        		 }else{
        			 lims.initNotificationMes("条形码已存在，请重新填写！", false);
        		 }
        	 }
         }
	 });
};

/**
 * 验证是否是进口产品，是则展示进口产品信息
 * 判断规则：690<=条形码前三位<=699 则该产品为国产，其他均为进口产品
 * @returns 产品类型
 * 		1 代表 国产食品 
 * 		2 代表 进口食品
 * 		4 代表 国内分装食品（本身为进口食品，但经过了国内二次包装）
 * @author longxianzhen 2015/05/22
 */
portal.getProductType = function(){
	var product_type = 1;
	
	if(portal.current_bussiness_type!="0101"){
		return product_type;
	}
	
	var curBarcode = $("#barcode").val().trim();
	var bar3 = curBarcode.substr(0,3);
	if(curBarcode.length==14){
		bar3 = curBarcode.substr(1,3);
	}
	/* 截取前三位 */
	bar3 = bar3.replace("N",3);
	bar3 = parseInt(bar3);
	if(!isNaN(bar3)){
		if(bar3>=690 && bar3<=699){
			$("#imported_product_info").hide();
			$("#labListView").hide();
		}else{
			product_type = 2;
			$("#imported_product_info").show();
			$("#labListView").show();
		}
	}else{
		$("#imported_product_info").hide();
		$("#labListView").hide();
	}
	return product_type;
};

/**
 * 根据条形码前3位设置原产地的值
 * @author longxianzhen 2015/05/22
 */
portal.setCountryByBar3=function(curBarcode){
	/* 截取前三位 */
	var bar3 = curBarcode.substr(0,3);
	if(curBarcode.length==14){
		bar3 = curBarcode.substr(1,3);
	}
	bar3 = parseInt(bar3);
	if(isNaN(bar3)){
		return;
	}
	$.ajax({
         url: portal.HTTP_PREFIX + "/country/getCountryByBar3/" + bar3,
         type: "GET",
         dataType: "json",
         async:false,
         contentType: "application/json; charset=utf-8",
         success: function(returnValue) {
        	 if(returnValue.result.status=="true"){
        		 if(returnValue.data!=null){
        			 $("#countryOfOrigin").data("kendoComboBox").value(returnValue.data.id);
        		 }else{
        			 $("#countryOfOrigin").data("kendoComboBox").text("");
        		 }
        	 }
         }
	 });
};
/**
 * 设置进口食品信息
 * @author longxianzhen 2015/05/22
 */
portal.setImportedProductInfo=function(importedProduct){
	$("#imported_product_id").val(importedProduct.id);
	if(importedProduct.labelAttachments.length>0){
		document.getElementById("yesLab").checked=true;
	}else{
		document.getElementById("noLab").checked=true;
	}
	$("#countryOfOrigin").data("kendoComboBox").value(importedProduct.country.id);
	portal.setLabAttachments(importedProduct.labelAttachments);
	if(importedProduct.importedProductAgents!=null){
		$("#agentName").val(importedProduct.importedProductAgents.agentName); // 国内代理商名称
		$("#agentAddress").val(importedProduct.importedProductAgents.agentAddress); // 国内代理商地址
	}
};

/**
 * 清空进口食品信息
 * @author longxianzhen 2015/05/22
 */
portal.clearImportedProductInfo=function(importedProduct){
	$("#agentName").val(""); // 国内代理商名称
	$("#agentAddress").val(""); // 国内代理商地址
	$("#imported_product_id").val("");
	document.getElementById("yesLab").checked=true;
	portal.clearLabFiles();
	 $("#labFileMsg").html("(注意:为保证更流畅的体验，建议每次上传照片不超过1M！ 可支持文件格式：png .bmp .jpeg .jpg )");
	//portal.setLabAttachments(new Array());
};
/**
 * 为营养报告grid上的条件赋值 
 * @author tangxin
 */
portal.initNutriPer = function(listNutris){
	if(listNutris == null || listNutris.length == 0){
		return ;
	}
	var nutriPer = listNutris[0].per;
	if(nutriPer == null || nutriPer == ""){
		return ;
	}
	
	nutriPer = nutriPer.toLowerCase();
	if(nutriPer.indexOf("g") > -1){
		portal.handleNutriPer(nutriPer, "g");
		return;
	}
	if(nutriPer.indexOf("ml") > -1){
		portal.handleNutriPer(nutriPer, "ml");
		return;
	}
	if(nutriPer.indexOf("kj") > -1){
		portal.handleNutriPer(nutriPer, "KJ");
		return;
	}
};

/**
 * 处理采样样本的单位下拉框赋值
 * @author ZhangHui 2015/4/22
 */
portal.handleNutriPer = function(nutriPer, value){
	var val = "";
	if(nutriPer.indexOf("每") > -1){
		val = nutriPer.substr(1,3);
	}else{
		val = nutriPer.substr(0,2);
	}
	$("#nutriPer").val(val);
	var nutriDS = $("#nutriUnitDropDown").data("kendoDropDownList");
	if(nutriDS){
		$("#nutriUnitDropDown").data("kendoDropDownList").text(value);
	}
};

/**
 * 初始化产品图片，页面显示
 */
portal.setProAttachments=function(proAttachments){
	 var dataSource = new kendo.data.DataSource();
	 portal.aryProAttachments.length=0;
	 if(proAttachments.length>0){
		 $("#btn_clearProFiles").show();
         $("#logListView").show();
		 for(var i=0;i<proAttachments.length;i++){
			 portal.aryProAttachments.push(proAttachments[i]);
			 dataSource.add({attachments:proAttachments[i]});
		 }
	 }else{
		 $("#btn_clearProFiles").hide();
         $("#logListView").hide();
	 }
	 $("#proAttachmentsListView").kendoListView({
         dataSource: dataSource,
         template:kendo.template($("#uploadedFilesTemplate").html()),
     });
 };
 /**
  * 初始化产品图片，页面显示
  */
 portal.aryProBusinessImg=function(strImg,aryProImg){
	 if(strImg=='licImg'){
		 portal.aryProLicImg.length=0;
	 }else if(strImg=='qsImg'){
		 portal.aryProQsImg.length=0;
	 }else if(strImg=='disImg'){
		 portal.aryProDisImg.length=0;
	 }
	 if(aryProImg.length>0){
		 var k = 0 ;
		 for(var i=0;i<aryProImg.length;i++){
			 k = i+1;
			 var dataSource = new kendo.data.DataSource();
			 $("#btn_clearProFiles").show();
			 $("#businessName"+k).val(aryProImg[i].businessName);
			 if(strImg=='licImg'){
				 portal.aryProLicImg.push(aryProImg[i]);
			 }else if(strImg=='qsImg'){
				 portal.aryProQsImg.push(aryProImg[i]);
			 }else if(strImg=='disImg'){
				 portal.aryProDisImg.push(aryProImg[i]);
			 }
			 
			 dataSource.add({attachments:aryProImg[i]});
			 $("#"+strImg+"ListView"+k).show();
			 $("#"+strImg+"FilesView"+k).kendoListView({
				 dataSource: dataSource,
				 template:kendo.template($("#"+strImg+"FilesTemplate").html()),
			 });
		 }
	 }else{
		 $("#btn_clearProFiles").hide();
		 $("#"+strImg+"ListView").hide();
	 }
 };

 /**
  * 初始化进口食品中文标签图片，页面显示
  */
 portal.setLabAttachments=function(labAttachments){
 	 var dataSource = new kendo.data.DataSource();
 	 portal.aryLabAttachments.length=0;
 	 if(labAttachments.length>0){
 		 $("#labListView").show();
 		  $("#btn_clearLabFiles").show();
 		 for(var i=0;i<labAttachments.length;i++){
 			 portal.aryLabAttachments.push(labAttachments[i]);
 			 dataSource.add({attachments:labAttachments[i]});
 		 }
 	 }else{
 		 $("#labListView").hide();
 		  $("#btn_clearLabFiles").hide();
 	 }
 	 $("#labAttachmentsListView").kendoListView({
          dataSource: dataSource,
          template:kendo.template($("#uploadedLabFilesTemplate").html()),
      });
  };
 
fsn.addNutri = function () {
	$("#product-nutri-grid").data("kendoGrid").dataSource.add({ name:"", value:"", unit:"", nrv:""});
};

/**
 * 供应商或者生产厂家推荐的url
 */
fsn.addUrl = function (status) {
	var urlGrid = $("#product-url-grid").data("kendoGrid");
	//生产企业可以推荐2个url，status=0表示生产企业，status=1表示其他销售方
	if(status==0){
		var data = urlGrid._data;
		var k = 0;
		for(var i = 0 ; i < data.length; i++){
			if(data[i].status==0){
				k++;
			}
		}
		if(k>=2){
			lims.initNotificationMes("您最多只能添加2个网址了！", false);	
		}else{
			urlGrid.dataSource.add({ id:"", urlName:"", proUrl:"", status:"0",identify:""});
		}
	}else if(status==1){
		var data = urlGrid._data;
		var k = 0;
		for(var i = 0 ; i < data.length; i++){
			if(data[i].status==1){
				k++;
			}
		}
		if(k>=1){
			lims.initNotificationMes("您只能添加1个网址了！", false);	
		}else{
		urlGrid.dataSource.add({ id:"", urlName:"", proUrl:"", status:"1",identify:""});	
		}
	}
//	$("#product-url-grid").data("kendoGrid").dataSource.add({ name:"", value:"", unit:"", nrv:""});
};

/*编辑商品信息时，给品牌赋值*/
fsn.editProductSetBrand = function (brand){
	$("#businessBrand").data("kendoComboBox").value(brand.id);
	var bval = $("#businessBrand").data("kendoComboBox").value();
	/*判断当前商品关联的品牌是否属于该企业信息，相等则不属于*/
	if(bval==$("#businessBrand").val().trim()){
		$("#businessBrand").data("kendoComboBox").text(brand.name);
		bval = $("#businessBrand").data("kendoComboBox").value();
		if(bval==brand.name){
			$("#businessBrand").data("kendoComboBox").text("");
			lims.initNotificationMes("当前商品关联的品牌信息无效，请重新选择本企业下对应的品牌，如果没有对应的品牌，请先新增。", false);
		}
	}
};

/**
 * 编辑产品时，页面赋值操作
 */
portal.writeProductInfo = function (data) {
	if (data!=null) {
		$.each(data, function(k,v){
			if (!portal.isBlank(v)) {
				switch (k) {
				case "id":
					$("#id").val(v);
					break;
				case "name":
					$("#name").val(v);
					break;
				case "status":
					$("#status").val(v);
					break;
				case "format":
					$("#format").val(v);
					break;
				case "nutriLabel":
					$("#nutriLabel").text(v);
					break;
				case "category":
					portal.setCategoryValue(v);
					break;
				case "regularity":
					var text = "";
					for(var i=0;i<v.length;i++){
						text = text+v[i].name+";";
					}
					$("#regularity").val(text);
					break;
				case "barcode":
					$("#barcode").val(v);
					break;
				case "businessBrand":
					if($("#businessBrand").data("kendoComboBox")){
						/*编辑商品信息时，给品牌赋值*/
						fsn.editProductSetBrand(v);
					}else{
						$("#businessBrand").val(v.name);
						$("#businessBrand").attr("data-id-value",v.id);

					}
					break;							
				case "expirationDate":
					$("#expirationDate-num").val(v);
					break;
				case "expiration":
					$("#expiration").val(portal.setExpirDay(v));
					break;
				case "characteristic":
					$("#characteristic").val(v);
					break;
				case "note":
					$("#note").val(v);
					break;
				case "cstm":
					$("#cstm").val(v);
					break;
				case "des":
					$("#des").val(v);
					break;
				case "ingredient":
					$("#ingredient").val(v);
					break;
				case "unit":
					$("#productUnit").val(v.name);
					break;
				case "otherName":
					$("#otherName").val(v);
					break;
				case "qsNo":
					$("#qs_no").data("kendoComboBox").text(v);
					break;
				}
			}
		});
		
        $("#businessName").val(data.producer!=null?data.producer.name:"");
        
        /**
         * 0101 代表 流通企业.供应商
         * 02   代表 生产企业
         */
        if(portal.current_bussiness_type == "0101"){
        	/* 选择销往客户弹出框，销往客户下拉控件赋值 */
        	$("#customerSelectInfo").data("kendoMultiSelect").value(data.selectedCustomerIds.split(","));
        	/* 页面销往客户显示框赋值 */
        	var customerItems = $("#customerSelectInfo").data("kendoMultiSelect").dataItems();
        	portal.setCustomerSelectValue(customerItems);
        }else if(portal.current_bussiness_type == "02"){
        	/**
			 * qs号赋值
			 * @author ZhangHui 2015/6/5
			 */
        	$("#qs_no").data("kendoComboBox").value(data.qs_info.qsid);
        }
	}
};

/**
 * 页面销往客户显示框，赋值操作
 * @author ZhangHui 2015/4/28
 */
portal.setCustomerSelectValue = function(customerItems){
	// 页面销往企业显示框赋值操作
	var customerNames = "";
	for ( var entry in customerItems) {
		customerNames += (customerItems[entry].name + ";");
	}
	$("#customerSelect").val(customerNames);
};

/**
 * 当产品条形码已经存在时，弹出引进产品的提示框
 * @author ZhangHui 2015/4/8
 */
portal.showLeadMsg = function(){
	if(portal.currentBusiness.type == "生产企业"){
		lims.initNotificationMes("产品条形码已经存在，请重新填写！", false);
	}else if(portal.currentBusiness.type == "流通企业.供应商" || portal.currentBusiness.type.indexOf("餐饮企业")!=-1||portal.currentBusiness.type.indexOf("流通企业.商超")!=-1){
		$("#lead_dealer_window").data("kendoWindow").open().center();
		$("#leadDealerMsg").html("该条形码已存在，可直接引用!");
	}else if(portal.currentBusiness.type == "流通企业"){
		$("#lead_window").data("kendoWindow").open().center();
		$("#leadMsg").html("该条形码已存在，可直接引用!<br><br>");
	}
};

/*增加产品认证信息*/
fsn.addCert = function(){
	$("#certification-grid").data("kendoGrid").dataSource.add({ cert:"", certResource:"", endDate:""});
};

/**
 * 从页面删除产品图片
 */
fsn.removeRes = function(resID){
	 var dataSource = new kendo.data.DataSource();
	 for(var i=0; i<portal.aryProAttachments.length; i++){
   	 if(portal.aryProAttachments[i].id == resID){
   		 while((i+1)<portal.aryProAttachments.length){
   			 portal.aryProAttachments[i] = portal.aryProAttachments[i+1];
   			 i++;
   		 }
   		 portal.aryProAttachments.pop();
   		 break;
   	 }
    }
	 
	 if(portal.aryProAttachments.length>0){
		for(i=0; i<portal.aryProAttachments.length; i++){
			dataSource.add({attachments:portal.aryProAttachments[i]});
		}
	 }
	 $("#proAttachmentsListView").data("kendoListView").setDataSource(dataSource);
		if(portal.aryProAttachments.length == 0){
			$("#logListView").hide();
	 }
};
/**
 * 从页面移除中文标签图片
 */
fsn.removeLabRes = function(resID){
	 var dataSource = new kendo.data.DataSource();
	 for(var i=0; i<portal.aryLabAttachments.length; i++){
   	 if(portal.aryLabAttachments[i].id == resID){
   		 while((i+1)<portal.aryLabAttachments.length){
   			 portal.aryLabAttachments[i] = portal.aryLabAttachments[i+1];
   			 i++;
   		 }
   		 portal.aryLabAttachments.pop();
   		 break;
   	 }
    }
	 
	 if(portal.aryLabAttachments.length>0){
		for(i=0; i<portal.aryLabAttachments.length; i++){
			dataSource.add({attachments:portal.aryLabAttachments[i]});
		}
	 }
	 $("#labAttachmentsListView").data("kendoListView").setDataSource(dataSource);
		if(portal.aryLabAttachments.length == 0){
			$("#labListView").hide();
	 }
};

/**
 * 封装认证信息
 */
fsn.getCerts = function(){
    var listProductCerts=[];
    var certs=$("#certification-grid").data("kendoGrid").dataSource.data();
    var j=0;
    for(var i=0;i<certs.length;i++){
    	if(certs[i].name==""){
    		j++;
    		continue;
    	}
    	listProductCerts[i-j]={
    		id: certs[i].id,
    	};
    }
    return listProductCerts;
};

/**
 * 新增执行标准
 */
portal.addRegularity = function(){
	//空值校验
	if($("#regularity_type").val().replace(/[ ]/g,"")==""){
		lims.initNotificationMes("请填写标准类型！", false);
		return;
	}
	if($("#regularity_year").val().replace(/[ ]/g,"")==""){
		lims.initNotificationMes("请填写执行序号及年份！", false);
		return;
	}
	//如果超过12，则不允许添加
	var ul = document.getElementById("regularityList");
	var count = ul.getElementsByTagName("li").length;
	if(count>=12){
		lims.initNotificationMes("执行标准过多，不能再添加！", false);
		return;
	}
	//将新增标准格式化后添加到列表中
 	var text = $("#regularity_type").val().replace(/[ ]/g,"")+" "+$("#regularity_year").val().replace(/[ ]/g,"")+" "+$("#regularity_name").val();		
	if(!portal.validaRegularity(text)){return;}
	var li= document.createElement("li");
	li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='fsn.portal.delRegularity(this)'></span>"+text+";</a>";
	ul.appendChild(li);
	//清空信息
	$("#regularity_type").val("");
	$("#regularity_year").val("");
	$("#regularity_name").val("");
};

portal.brandChange = function(){
	var sed = $("#businessBrand").data("kendoComboBox").select();
	if(sed < 0){
		$("#businessBrand").data("kendoComboBox").value("");
		lims.initNotificationMes("请选择下拉框内的品牌！", false);
	}
};

portal.countryOfOriginChange = function(){
	if(!portal.isSelectCountry){
		$("#countryOfOrigin").data("kendoComboBox").value("");
		lims.initNotificationMes("请选择下拉框内的原产地！", false);
	}else{
		portal.isSelectCountry=false;
	}
};

/**
 * qs号输入框的change事件
 * @author 2015-05-29 
 */
portal.qsNoChange = function(){
	var qs_no = $("#qs_no").val().trim();
	if(qs_no != ""){
		var sed = $("#qs_no").data("kendoComboBox").select();
		if(sed < 0){
			$("#qs_no").data("kendoComboBox").value("");
			lims.initNotificationMes("手动输入的QS号必须存在下拉列表中，建议通过下拉选择！", false);
		}
	}
};

/**
 * 其他认证信息的下拉选项
 */
portal.certNameDropDownEditor = function(container, options){
	$('<input required data-text-field="name" data-value-field="name" data-bind="value:' + options.field + '"/>')
	.appendTo(container)
	.kendoDropDownList({
		autoBind: false,
		optionLabel:"请选择产品认证信息",
		dataTextField: "name",
	    dataValueField: "id",
		dataSource: {
			transport: {
				read: portal.HTTP_PREFIX + "/business/getCertificationsByOrg"
			},
			schema: {
				data : function(returnValue) {
					fsn.listCert=returnValue.data;
					if(returnValue.data == null) lims.initNotificationMes("认证信息为空，请到企业基本信息页面添加！", false);
					return returnValue.data == null?[]:returnValue.data;  //响应到页面的数据
	            }
			}
		},
		index: 0,
		change: function(e) {
		    var value = this.value();
		    options.model.cert={name:value};
		    if(fsn.listCert){
		    	for(var i=0;i<fsn.listCert.length;i++){
		    		if(value==fsn.listCert[i].name){
		    			options.model.id=fsn.listCert[i].id;
		    			options.model.certResource={name:fsn.listCert[i].fileName,url:fsn.listCert[i].url};
		    			options.model.endDate=fsn.formatGridDate(fsn.listCert[i].endDate);
		    			$("#certification-grid").data("kendoGrid").refresh();
			    			break;
			    		}
			    	}
			    }
			  },
		});
};
	
/*认证信息gird的单元格的编辑事件*/
portal.certResourceEditor = function(container, options){
	if(options.model.certResource.url){
		window.open (options.model.certResource.url);
		return;
	}
	$("<span>"+options.model.certResource +"</span>").appendTo(container);
};

/**
 * 产品认证信息截止日期编辑事件
 * @author tangxin
 */
portal.certEndDateEditor = function (container, options) {
	var input = $("<input />");
	input.attr("name", options.field);
    input.attr("class", "k-textbox");
    input.attr("disabled", "disabled");
    input.val(options.model.endDate);
    input.appendTo(container);
};

/**
 * 移除已经选择的营养标签 
 * @author tangxin
 */
 portal.removeRepeatNutri = function (listNutris){
	 if(listNutris == null && listNutris.length<1){
		 return listNutris;
	 }
	 var selectNutris = portal.getProductNutris();
	 if(selectNutris == null || selectNutris.length < 1){
		 return listNutris;
	 }
	 for(var i=0;i<selectNutris.length;i++){
		 var nutriName = selectNutris[i].name ;
		 for(var j=0;j<listNutris.length;j++){
			 if(nutriName == listNutris[j].name){
				 listNutris.splice(j,1);
			 }
		 }
	 }
	 return listNutris;
 };
 
 portal.autoNutriName=function(container,options){
	 var input = $("<input/>");
	 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(1));
 };
 
 portal.autoNutriUnit=function(container,options){
	 var input = $("<input/>");
	 portal.globNutrModel = options.model;
	 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(3));
 };
 
 /**
  * 全局的营养报实体
  * 营养报告值自动加载方法
  * @author tangxin
  */
 portal.autoNutriValue=function(container,options){
	 var input = $("<input/>");
	 portal.globNutrModel = options.model;
	 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(2));
	 input.blur(function(){
		 var value = $(this).val().trim();
		 /* 验证指定的值只能是整数或者浮点数  */
		 portal.validateIntegerAndfractional(value,"value");
	 });
 };
 
 portal.autoNutriNrv=function(container,options){
	 var input = $("<input/>");
	 portal.globNutrModel = options.model;
	 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(5));
	 input.blur(function(){
		 var value = $(this).val().trim();
		 /* 验证指定的值只能是整数或者浮点数  */
		 portal.validateIntegerAndfractional(value,"nrv");
	 });
 };
 
 /**
  * 验证指定的值只能是整数或者浮点数
  * @author tangxin
  */
portal.validateIntegerAndfractional = function (value,target){
	if(value == null || value == ""){
		return;
	}
	var regStr = /^[0-9]*$|([0-9]*(\.)[0-9]*)$/;
	if(!regStr.test(value)){
		 if(portal.globNutrModel != null){
			 if(target == "nrv"){
				 portal.globNutrModel.nrv = "";
			 }else if(target == "value"){
				 portal.globNutrModel.value = "";
			 }
		 }
		 fsn.initNotificationMes(fsn.l("Enter only integer or fractional")+"!",false);
	 }
};