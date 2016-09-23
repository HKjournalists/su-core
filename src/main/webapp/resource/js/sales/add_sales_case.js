$(document).ready(function() {
	/****销售案例添加或是编辑****/
	var fsn = window.fsn = window.fsn || {};
	var sales = window.sales = window.sales || {};
	sales.model = {id:null,resource:[]};
	var httpPrefix = fsn.getHttpPrefix();
	var RES_TYPE = ".jpg;.png;.jpeg;.bmp;.gif;";
	var isCover = false; //案例封面标志
	var widget = {
			FIEL_UPLOAD:null,
			START_DATE_PICKER:null,
			END_DATE_PICKER:null,
			MSGWIN:null
	};
	
	try {
		sales.model.id = $.cookie("cook_edit_salescase").calesId;
		$.cookie("cook_edit_salescase", JSON.stringify(null), {
			path : '/'
		});
	} catch (e) {}
	
	/**
	 * 页面初始化方法
	 * @author HY 2015-05-03
	 */
	function initSalesCase(){
		initUpload();
		widget.MSGWIN = saleskendoUtil.initKendoWindow("messageWindow","310","65","保存状态",false,
				true,false,["Close"],"messageLabel","正在保存请稍候...");
		$("#concel_btn").bind("click",cancelFun);
		$("#add_btn").bind("click",saveSalesCaseFun);
		renderpic.initPhotosStyleB("imgListBox",175,64,delSalesCaseRes,setCoverByGUID);
		initEditSalesCase();
	}

	/**
	 * 编辑状态时，根据 id 加载促销活动实体
	 * @author HY 2015-05-03
	 */
	function initEditSalesCase() {
		if(sales.model.id == null) {
			return;
		}
		$.ajax({
	           url: httpPrefix + "/sales/case/findByid/" + sales.model.id,
	           type: "GET",
	           dataType: "json",
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
					  sales.model.id = returnData.data.id;
					  if(returnData.data.resource != null) {
						  isCover = true;
						  for(var i=0;i < returnData.data.resource.length;i++){
							  sales.model.resource.push(returnData.data.resource[i]);
						  }
					  }
	            	  setSalesCaseVal(returnData.data);
	              }
	          }
		});
	}

	/**
	 * 刷新页面时清空输入框内容
	 * @author HY 2015-05-04
	 */
	function clarePageValue(){
		$("#sales_case_name").val("");
		$("#sales_details").val("");
	}

	/**
	 * 初始化图片上传控件
	 * @author HY 2015-05-04
	 */
	function initUpload(){
		$("#uploadFile_div").html('<input id="uploadFile" type="file"/>');
		widget.FIEL_UPLOAD = saleskendoUtil.buildUpload("uploadFile",sales.model.resource,"Upload Picture",RES_TYPE,true,addPicToListBox,null);
	}

	/**
	 * 清除案例 封面
	 * @author tangxin 2015-05-14
	 */
	function setFirstImgCover(){
		if(sales.model == null && sales.model.resource == null){
			return ;
		}
		var len = sales.model.resource.length;
		for(var i = 0; i < len; i++){
			var res = sales.model.resource[i];
			if(res != null){
				if(i == (len - 1)){
					res.cover = 1;
				} else {
					res.cover = 0;
				}
			}
		}
	}
	
	/**
	 * 向相册 listbox 添加资源
	 * @author HY 2015-05-04
	 */
	function addPicToListBox(aryListRes) {
		if(aryListRes == null || aryListRes.length < 1) {
			return;
		}
		document.getElementById("imgListBox").style.display = "block";
		renderpic.addPhotosStyleB(aryListRes);
		initUpload();
		if(!isCover){
			setFirstImgCover();
		}
	}

	/**
	 * 从页面删除相册图片资源
	 * @author HY 2015-05-04
	 */
	function delSalesCaseRes(guid,resId){
		if(!guid || !sales.model.resource) {
			return;
		}
		saleskendoUtil.removeResByGUID(sales.model.resource,guid,resId);
		if(sales.model.resource.length < 1){
			document.getElementById("imgListBox").style.display = "none";
		}
	}

	/**
	 * 设置封面
	 * @author tangxin 2015-05-04
	 */
	function setCoverByGUID(guid,resId){
		if(!guid || !sales.model.resource) {
			return;
		}
		isCover = true;
		saleskendoUtil.setCoverByGUID(sales.model.resource,guid,resId);
	}
	
	/**
	 * 将 mode 的值赋值到页面
	 * @author HY 2015-05-03
	 */
	function setSalesCaseVal(model){
		if(model == null) {
			return;
		}
		$("#sales_case_name").val(model.salesCaseName);
		$("#sales_details").val(model.salesDetails);
		addPicToListBox(model.resource);
	}

	/**
	 * 验证销售案例名称是否重复
	 * @author HY 2015-04-29
	 */
	function validateName(name){
		if(!name) return false;
		var vid = (sales.model == null ? -1 : sales.model.id);
		vid = (vid == null ? -1 : vid);
		var result = false;
		$.ajax({
	           url: httpPrefix + "/sales/case/countName/" + name + "/" + vid,
	           type: "GET",
	           async:false,
	           dataType: "json",
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  result = (returnData.count > 0 ? false : true);
	              } 
	          }
		});
		return result;
	}
	
	/**
	 * 页面信息验证
	 * @author HY 2015-05-03
	 */
	function validate(){
		var vName = $("#sales_case_name").val().trim();
		if(vName == "") {
			fsn.initNotificationMes(fsn.l("Sales case name"), false);
			return false;
		}
		if(vName.length > 100) {
			fsn.initNotificationMes(fsn.l("Sales case name does not allow more than 100 characters"), false);
			return false;
		}
		var vIntro = $("#sales_details").val().trim();
		if(vIntro.length > 200) {
			fsn.initNotificationMes(fsn.l("Sales case profile does not allow more than 200 characters"), false);
			return false;
		}
		if(!validateName(vName)){
			fsn.initNotificationMes(fsn.l("Sales case name already exists, please input again"), false);
			return false;
		}
		return true;
	}
	
	function createModel(){
		sales.model = (sales.model == null ? {id:null} : sales.model);
		sales.model.salesCaseName = $("#sales_case_name").val().trim();
		sales.model.salesDetails = $("#sales_details").val().trim();
	}
	
	/**
	 * 保存销售案例保存方法
	 *  @author HY 2015-05-03
	 */
	function saveSalesCaseFun () {
		if(!validate()) {
			return;
		}
		createModel();
		if(widget.MSGWIN != null) {
			widget.MSGWIN.open().center();
		}
		$.ajax({
	           url: httpPrefix + "/sales/case/" + (sales.model.id == null ? "create" : "update"),
	           type: sales.model.id == null ? "POST" : "PUT",
	           dataType: "json",
	           timeOut:600000, //设置超时时间10分钟
	           data: JSON.stringify(sales.model),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   widget.MSGWIN.close();
	              if(returnData.result.status == "true"){
	            	  fsn.initNotificationMes(fsn.l("保存成功！"), true);
	            	  salesUtil.setWindowLocationHref("sales_case_manager.html");
	              }else {
	            	  fsn.initNotificationMes(fsn.l("保存失败！"), false);
	              }
	           },
	           error: function(e) {
	        	   fsn.initNotificationMes(fsn.l("服务器异常！"), false);
	           }
	    });
	}
	
	/**
	 * 取消按钮的方法
	 * @author HY 2015-05-03
	 */
	function cancelFun(){
		window.location.href = "sales_case_manager.html";
	}
	
	initSalesCase();
});