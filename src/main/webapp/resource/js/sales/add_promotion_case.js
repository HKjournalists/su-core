$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var promotion = window.promotions = window.promotions || {};
	promotion.model = {id:null,resource:[]};
	var httpPrefix = fsn.getHttpPrefix();
	var RES_TYPE = ".jpg;.png;.jpeg;.bmp;.gif;";
	var widget = {
			FIEL_UPLOAD:null,
			START_DATE_PICKER:null,
			END_DATE_PICKER:null,
			MSGWIN:null,
	};
	
	try {
		promotion.model.id = $.cookie("cook_edit_promotion").promotionId;
		$.cookie("cook_edit_promotion", JSON.stringify(null), {
			path : '/'
		});
	} catch (e) {}
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-03
	 */
	function initComponent(){
		initUpload();
		widget.START_DATE_PICKER = saleskendoUtil.initDatePicker("startDate");
		widget.END_DATE_PICKER = saleskendoUtil.initDatePicker("endDate");
		clarePageValue();
		widget.MSGWIN = saleskendoUtil.initKendoWindow("messageWindow","300","65","保存状态",false,
				true,false,["Close"],"messageLabel","正在保存请稍候...");
		$("#startDate").bind("blur",validateStartDate);
		$("#endDate").bind("blur",validateEndDate);
		$("#concel_btn").bind("click",cancelFun);
		$("#add_btn").bind("click",savePromotionFun);
		renderpic.initPhotosStyleB("imgListBox",175,64,delPromotionRes,setCoverByGUID);
		initEditPromotion();
	}
	
	/**
	 * 刷新页面时清空输入框内容
	 * @author tangxin 2015-05-04
	 */
	function clarePageValue(){
		$("#name").val("");
		$("#area").val("");
		$("#introduction").val("");
		if(widget.START_DATE_PICKER != null) {
			widget.START_DATE_PICKER.value("");
		}
		if(widget.END_DATE_PICKER != null) {
			widget.END_DATE_PICKER.value("");
		}
	}
	
	/**
	 * 初始化图片上传控件
	 * @author tangxin 2015-05-04
	 */
	function initUpload(){
		$("#uploadFile_div").html('<input id="uploadFile" type="file"/>');
		widget.FIEL_UPLOAD = saleskendoUtil.buildUpload("uploadFile",promotion.model.resource,"Upload Picture",RES_TYPE,true,addPicToListBox);
	}
	
	/**
	 * 向相册 listbox 添加资源
	 * @author tangxin 2015-05-04
	 */
	function addPicToListBox(aryListRes) {
		if(aryListRes == null || aryListRes.length < 1) {
			return;
		}
		document.getElementById("imgListBox").style.display = "block";
		renderpic.addPhotosStyleB(aryListRes);
		initUpload();
	}
	
	/**
	 * 从页面删除相册图片资源
	 * @author tangxin 2015-05-04
	 */
	function delPromotionRes(guid,resId){
		if(!guid || !promotion.model.resource) {
			return;
		}
		saleskendoUtil.removeResByGUID(promotion.model.resource,guid,resId);
		if(promotion.model.resource.length < 1){
			document.getElementById("imgListBox").style.display = "none";
		}
	}
	
	/**
	 * 设置封面
	 * @author tangxin 2015-05-04
	 */
	function setCoverByGUID(guid,resId){
		if(!guid || !promotion.model.resource) {
			return;
		}
		saleskendoUtil.setCoverByGUID(promotion.model.resource,guid,resId);
	}
	
	/**
	 * 编辑状态时，根据 id 加载促销活动实体
	 * @author tangxin 2015-05-03
	 */
	function initEditPromotion() {
		if(promotion.model.id == null) {
			return;
		}
		$.ajax({
	           url: httpPrefix + "/sales/promotion/findByid/" + promotion.model.id,
	           type: "GET",
	           dataType: "json",
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  promotion.model.id = returnData.data.id;
	            	  if(returnData.data.resource != null) {
						  for(var i=0;i < returnData.data.resource.length;i++){
							  promotion.model.resource.push(returnData.data.resource[i]);
						  }
					  }
	            	  setPromotionVal(returnData.data);
	              }
	          }
		});
	}
	
	/**
	 * 将 mode 的值赋值到页面
	 * @author tangxin 2015-05-03
	 */
	function setPromotionVal(model){
		if(model == null) {
			return;
		}
		$("#name").val(model.name);
		$("#area").val(model.area);
		$("#introduction").val(model.introduction);
		if(widget.START_DATE_PICKER != null) {
			widget.START_DATE_PICKER.value(fsn.formatGridDate(model.startDate));
		}
		if(widget.END_DATE_PICKER != null) {
			widget.END_DATE_PICKER.value(fsn.formatGridDate(model.endDate));
		}
		addPicToListBox(model.resource);
	}
	
	/**
	 * 验证起始日期格式
	 * @author tangxin 2015-05-03
	 */
	function validateStartDate(){
		if(widget.START_DATE_PICKER == null) {
			return;
		}
		var val = widget.START_DATE_PICKER.value();
		var text = $("#startDate").val().trim();
		if(val == null && text != ""){
			fsn.initNotificationMes(fsn.l("您输入的日期格式不正确,请重新填写！"), false);
			widget.START_DATE_PICKER.value("");
		}
	};
	
	/**
	 * 验证截止日期格式
	 * @author tangxin 2015-05-03
	 */
	function validateEndDate(){
		if(widget.END_DATE_PICKER == null) {
			return;
		}
		var val = widget.END_DATE_PICKER.value();
		var text = $("#endDate").val().trim();
		if(val == null && text != ""){
			fsn.initNotificationMes(fsn.l("您输入的日期格式不正确,请重新填写！"), false);
			widget.END_DATE_PICKER.value("");
		}
	};
	
	/**
	 * 验证促销活动名称是否重复
	 * @author tangxin 2015-04-29
	 */
	function validateName(name){
		if(!name) return false;
		var vid = (promotion.model == null ? -1 : promotion.model.id);
		vid = (vid == null ? -1 : vid);
		var result = false;
		$.ajax({
	           url: httpPrefix + "/sales/promotion/countName/" + name + "/" + vid,
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
	 * @author tangxin 2015-05-03
	 */
	function validate(){
		var vName = $("#name").val().trim();
		if(vName == "") {
			fsn.initNotificationMes(fsn.l("请填写促销活动名称后再保存！"), false);
			return false;
		}
		if(vName.length > 100) {
			fsn.initNotificationMes(fsn.l("活动名称不允许超过100字符！"), false);
			return false;
		}
		var vArea = $("#area").val().trim();
		if(vArea.length > 100) {
			fsn.initNotificationMes(fsn.l("惠级地区不允许超过100字符！"), false);
			return false;
		}
		var startDate = widget.START_DATE_PICKER.value();
		var endDate = widget.END_DATE_PICKER.value();
		if(endDate != null && startDate == null) {
			fsn.initNotificationMes(fsn.l("请填写促销活动的开始日期！"), false);
			return false;
		}
		if(startDate != null && endDate != null && (endDate-startDate)<0) {
			fsn.initNotificationMes(fsn.l("起始日期不能大于截止日期！"), false);
			return false;
		}
		var vIntro = $("#introduction").val().trim();
		if(vIntro.length > 200) {
			fsn.initNotificationMes(fsn.l("活动简介不允许超过200字符！"), false);
			return false;
		}
		if(!validateName(vName)){
			fsn.initNotificationMes(fsn.l("促销活动名称已经存在，请重新输入！"), false);
			return false;
		}
		return true;
	}
	
	function createModel(){
		promotion.model = (promotion.model == null ? {id:null} : promotion.model);
		promotion.model.name = $("#name").val().trim();
		promotion.model.area = $("#area").val().trim();
		promotion.model.introduction = $("#introduction").val().trim();
		promotion.model.startDate = widget.START_DATE_PICKER.value();
		promotion.model.endDate = widget.END_DATE_PICKER.value();
	}
	
	/**
	 * 促销活动保存方法
	 *  @author tangxin 2015-05-03
	 */
	function savePromotionFun () {
		if(!validate()) {
			return;
		}
		createModel();
		if(widget.MSGWIN != null) {
			widget.MSGWIN.open().center();
		}
		$.ajax({
	           url: httpPrefix + "/sales/promotion/" + (promotion.model.id == null ? "create" : "update"),
	           type: promotion.model.id == null ? "POST" : "PUT",
	           dataType: "json",
	           timeOut:600000, //设置超时时间10分钟
	           data: JSON.stringify(promotion.model),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   widget.MSGWIN.close();
	              if(returnData.result.status == "true"){
	            	  fsn.initNotificationMes(fsn.l("保存成功！"), true);
	            	  salesUtil.setWindowLocationHref("promotion_case_manager.html");
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
	 * @author tangxin 2015-05-03
	 */
	function cancelFun(){
		window.location.href = "promotion_case_manager.html";
	}
	
	initComponent();
});