$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var salesUtil = window.salesUtil = window.salesUtil || {};
	var httpPrefix = fsn.getHttpPrefix();
	var salesBranch = {id:null}; //销售网点实体
	var MSGWIN = null;
	var editBranchId = null;
	var BaiduMap = null;
	var BaiduMarker = null;
	var removeMarket = false; //地图上标注删除的状态
	
	try {
		editBranchId = $.cookie("cook_edit_branchId").branchId;
		$.cookie("cook_edit_branchId", JSON.stringify(null), {
			path : '/'
		});
	} catch (e) {}
	
	/**
	 * 页面容器初始化
	 * @author tangxin 2015-04-27
	 */
	function initComponent(){
		MSGWIN = saleskendoUtil.initKendoWindow("messageWindow","500","","保存状态",false,
				true,false,["Close"],"messageLabel","正在保存请稍候...");
		$("#salesOk_btn").bind("click", save);
		$("#salesCancle_btn").bind("click", cancelFun);
		initBuiduMap();
		initBranch();
	}
	
	/**
	 * 为页面的branch字段赋值
	 * @author tangxin 2015-04-27
	 */
	function setBranchVal(branch){
		if(branch == null) {
			return;
		}
		salesBranch = branch;
		$("#branchName").val(branch.name);
		$("#contact").val(branch.contact);
		$("#telPhone").val(branch.telephone);
		if(branch.province != null && branch.province != "") {
			var addrStr = branch.province + "-" + branch.city + "-" +  branch.counties;
			$("#mainAddr").val(addrStr);
		}
		$("#streetAddr").val(branch.street);
		var pointTrue = (branch.longitude != null && branch.longitude != "") ? true : false;
		if(BaiduMap != null && pointTrue) {
			var point = new BMap.Point(branch.longitude,branch.latitude);
			BaiduMap.centerAndZoom(point,15);
			if(BaiduMarker == null){
				BaiduMarker = new BMap.Marker(point);
				BaiduMarker.enableDragging();
				BaiduMarker.addEventListener("dragend",function(de){
					salesBranch.longitude = de.point.lng;
					salesBranch.latitude = de.point.lat;
				});
				/*BaiduMarker.addEventListener("click",function(e){
					BaiduMap.removeOverlay(this);
					removeMarket = true;
					if(salesBranch != null) {
						salesBranch.longitude = null;
						salesBranch.latitude = null;
					}
				});*/
			}else{
				BaiduMarker.setPosition(e.point);
			}
			BaiduMap.addOverlay(BaiduMarker);
		}
	}
	
	/**
	 * 编辑时的初始化方法
	 * @author tangxin 2015-04-27
	 */
	function initBranch(){
		if(editBranchId == null) {
			return ;
		}
		$.ajax({
           url: httpPrefix + "/sales/branch/findByid/" + editBranchId,
           type: "GET",
           dataType: "json",
           success: function(returnData) {
              if(returnData.result.status == "true"){
            	  salesBranch = returnData.data;
            	  setBranchVal(salesBranch);
              }
          }
	    });
	}
	
	/**
	 * 验证销售网点名称是否重复
	 * @author tangxin 2015-04-29
	 */
	function validateName(name){
		if(name == null || name == "") {
			return false;
		}
		if(salesBranch == null) {
			salesBranch = {id:null};
		}
		var vid = (salesBranch.id == null ? -1 : salesBranch.id);
		var result = true;
		$.ajax({
	           url: httpPrefix + "/sales/branch/countBranchByName/" + name + "/" + vid,
	           type: "GET",
	           dataType: "json",
	           async:false,
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  result = returnData.count > 0 ? false : true;
	              }
	          }
		});
		return result;
	}
	
	$("#branchName").blur(function(){
		var val = $(this).val().trim();
		if(val == ""){
			saleskendoUtil.msg("v_name_span","请填写销售网点名称！");
		}else{
			saleskendoUtil.clearMsg("v_name_span");
		}
	});
	
	$("#contact").blur(function(){
		var val = $(this).val().trim();
		if(val == ""){
			saleskendoUtil.msg("v_contact_span","请填写联系人信息！");
		}else{
			saleskendoUtil.clearMsg("v_contact_span");
		}
	});
	
	$("#telPhone").blur(function(){
		var val = $(this).val().trim();
		if(val != "" && !salesUtil.validatePhone(val)){
			saleskendoUtil.msg("v_phone_span","电话号码格式不正确！");
		}else{
			saleskendoUtil.clearMsg("v_phone_span");
		}
	});
	
	/**
	 * 页面数据验证，返回值 true or false, true验证通过
	 * @author tangxin 2015-04-27
	 */
	function validate() {
		saleskendoUtil.clearAllMsg();
		var branchName = $("#branchName").val().trim();
		if(branchName == "") {
			saleskendoUtil.msg("v_name_span","请填写销售网点名称！");
			return false;
		}
		if(branchName.length > 100) {
			saleskendoUtil.msg("v_name_span","销售网点名称不能超过100字符！");
			return false;
		}
		var contact = $("#contact").val().trim();
		if(contact == ""){
			saleskendoUtil.msg("v_contact_span","请填写联系人信息！");
			return false;
		}
		if(contact.length > 100) {
			saleskendoUtil.msg("v_contact_span","联系人不能超过100字符！");
			return false;
		}
		/* 联系电话格式验证，此页面联系电话非必填，为空时也验证通过 */
		var tel = $("#telPhone").val().trim();
		if(tel != "" && !salesUtil.validatePhone(tel)){
			saleskendoUtil.msg("v_phone_span",fsn.l("电话号码格式不正确！"));
			return false;
		} 
		if($("#streetAddr").val().trim().length > 300) {
			saleskendoUtil.msg("v_streetAddr",fsn.l("详细地址不能超过300字符！"));
			return false;
		}
		var mainAddr = $("#mainAddr").val().trim();
		
		if(mainAddr != "" && mainAddr.split("-").length < 3){
			$("#mainAddr").val("");
			saleskendoUtil.msg("v_mainAddr_span","请填写正确的省、市、县三级地址！");
			salesBranch.longitude = null;
			salesBranch.latitude = null;
			if(BaiduMap != null && BaiduMarker != null){
				if(BaiduMap.removeOverlay(BaiduMarker));
			}
			return false;
		}
		var streetAddr = $("#streetAddr").val().trim();
		if(streetAddr != "" && mainAddr == "") {
			saleskendoUtil.msg("v_mainAddr_span",fsn.l("请选择省市县地址！"));
			return false;
		}
		if(!validateName(branchName)){
			saleskendoUtil.msg("v_name_span",fsn.l("当前销售网点名称已经存在, 请重新填写！"));
			return false;
		}
		return true;
	}
	
	/**
	 * 获取页面地址信息,数组形式 顺依次是 省、市、县、街道地址
	 * @author tangxin 2015-04-27
	 */
	function getAddress(){
		var mainAddr = $("#mainAddr").val().trim();
		var streetAddr = $("#streetAddr").val().trim();
		var returnAddr = ["", "", "", streetAddr];
		if(mainAddr != "") {
			var addrAry = mainAddr.split("-");
			if(addrAry.length > 2) {
				returnAddr[0] = addrAry[0];
				returnAddr[1] = addrAry[1];
				returnAddr[2] = addrAry[2];
			}
		}
		return returnAddr;
	}
	
	/**
	 * 封装保存的销售网点实体
	 * @author tangxin 2015-04-27
	 */
	function createInstance (){
		if(salesBranch == null) {
			salesBranch = {id:null};
		}
		salesBranch.name = $("#branchName").val().trim();
		salesBranch.contact = $("#contact").val().trim();
		salesBranch.telephone = $("#telPhone").val().trim();
		var addrAry = getAddress();
		if(addrAry != null && addrAry.length > 0) {
			salesBranch.province = addrAry[0];
			salesBranch.city = addrAry[1];
			salesBranch.counties = addrAry[2];
			salesBranch.street = addrAry[3];
		}
	}
	
	/**
	 * 页面保存方法
	 * @author tangxin 2015-04-27
	 */
	function save() {
		/* 校验页面数据 */
		if(!validate()){
			return;
		}
		/* 封装页面数据 到 全局对象 salesBranch */
		createInstance();
		MSGWIN.open().center();
		$.ajax({
	           url: httpPrefix + "/sales/branch/" + (salesBranch.id == null ? "create" : "update"),
	           type: salesBranch.id == null ? "POST" : "PUT",
	           dataType: "json",
	           data: JSON.stringify(salesBranch),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   MSGWIN.close();
	              if(returnData.result.status == "true"){
	            	  fsn.initNotificationMes(fsn.l("保存成功！"), true);
	            	  salesBranch = returnData.data;
	            	  salesUtil.setWindowLocationHref("sales_branch_list.html");
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
	 * 省市县地址失去焦点的事件
	 * @author tangxin 2015-04-28
	 */
	salesUtil.onKeyUpFun = function(event, param){
		if(event.keyCode == null) return; //如果是鼠标按键，则不处理
		var sval = $("#streetAddr").val().trim();
		var mval = $("#mainAddr").val().trim();
		if(param == "M" && mval == ""){
			return;
		}else if(param == "S" && sval == ""){
			return;
		}
		var addAry = getAddress();
		if(BaiduMap == null || addAry[0] == "") return;
		var myGeo = new BMap.Geocoder();
		var mainAddr = addAry[0] + addAry[1] + addAry[2] + addAry[3];
		myGeo.getPoint(mainAddr, function(point){
			if (point) {
				BaiduMap.centerAndZoom(point, 15);
				BaiduMap.enableScrollWheelZoom();
				BaiduMap.setCurrentCity(addAry[1]);
				if(BaiduMarker != null) {
					BaiduMarker.setPosition(point);
				}else{
					BaiduMarker = new BMap.Marker(point);
					BaiduMarker.enableDragging();
					/*BaiduMarker.addEventListener("click",function(e){
						BaiduMap.removeOverlay(this);
						removeMarket = true;
						if(salesBranch != null) {
							salesBranch.longitude = null;
							salesBranch.latitude = null;
						}
					});*/
				}
				BaiduMap.addOverlay(BaiduMarker);
				salesBranch.longitude = point.lng;
				salesBranch.latitude = point.lat;
			}
		});
	};
	
	/**
	 * 为地图添加click事件
	 * @author tangxin 2015-04-28
	 */
	function addClickEvent(map){
		if(map == null) {
			return;
		}
		map.addEventListener("click",function(e){
			if(removeMarket){
				removeMarket = false;
				return;
			}
			if(BaiduMarker != null) {
				BaiduMarker.setPosition(e.point);
			}else{
				BaiduMarker = new BMap.Marker(e.point);
				BaiduMarker.enableDragging ();
				BaiduMarker.enableMassClear();
				BaiduMarker.addEventListener("dragend",function(de){
					salesBranch.longitude = de.point.lng;
					salesBranch.latitude = de.point.lat;
				});
				/*BaiduMarker.addEventListener("click",function(e){
					BaiduMap.removeOverlay(this);
					removeMarket = true;
					if(salesBranch != null) {
						salesBranch.longitude = null;
						salesBranch.latitude = null;
					}
				});*/
			}
			BaiduMap.addOverlay(BaiduMarker);
			if(salesBranch != null) {
				salesBranch.longitude = e.point.lng;
				salesBranch.latitude = e.point.lat;
			}
		});
	}
	
	/**
	 * 初始化百度地图
	 * @author tangxin 2015-04-28
	 */
	function initBuiduMap(){
		BaiduMap = new BMap.Map("salesBranchMap");
	    var point = null;
	    var lng = 106.682178;
	    var lat = 26.663011;
	    var city = "贵阳";
	    if(salesBranch != null && salesBranch.id != null){
	    	lng = salesBranch.longitude;
	    	lat = salesBranch.latitude;
	    	city = salesBranch.city;
	    }
	    if(lng != null && lng != "" && lat != null && lat != ""){
	    	point = new BMap.Point(lng,lat);
	    	BaiduMap.setCurrentCity(city);
	    } else {
	    	point = new BMap.Point(106.682178,26.663011);
	    	BaiduMap.setCurrentCity("贵阳");
	    }
	    BaiduMap.centerAndZoom(point, 11);
	    BaiduMap.enableScrollWheelZoom();
	    addClickEvent(BaiduMap);
	}
	
	/**
	 * 页面取消按钮事件
	 * @author tangxin 2015-04-27
	 */
	function cancelFun(){
		window.location.href = "sales_branch_list.html";
	}
	
	/* 初始化方法的调用 */
	initComponent();

});