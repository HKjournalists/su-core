$(document).ready(function() {
	// 认证信息下拉框
	var portal = fsn.portal = fsn.portal || {};
	var	upload = fsn.upload = fsn.upload || {};
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    portal.isNew = true;
    portal.aryHeadAttachments = new Array();//头像证件照
    portal.aryHealthAttachments = new Array();//健康证
    portal.aryWorkAttachments = new Array();//从业资格证
    portal.aryHonorAttachments = new Array();//荣誉证书
    portal.identify = '';
	portal.memberOrigin="1";  //  人员来源  1：系统录入  2：监管
    /**
     * 从cookie中获取产品id(报告录入界面，产品不存在时，跳转至产品新增界面，并自动加载产品条形码)
     * @author ZhangHui 2015/4/22
     */
    try {
		portal._barcode = $.cookie("user_0_edit_product").barcode;
		$.cookie("user_0_edit_product", JSON.stringify({}), {
			path : '/'
		});
    } catch (e) {}
    
	/**
	 * 编辑产品，从url中获取产品id。（F5页面刷新后，数据丢失的问题）
	 * 可对比pid和orig_pidmd5，判断有无被篡改
	 * @author HuangYong 2015/4/22
	 */
    try {
    	var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    	var pid = arrayParam[0]; // 产品id（原始id，未被编码）
    	var orig_pidmd5 = arrayParam[1]; // 产品id(被编码过的产品id)
    	portal.edit_id = portal.md5validate(pid,orig_pidmd5);
    } catch (e) {}
	
	/**
	 * 页面初始化
	 */
	portal.init = function() {
		portal.initialComponent();  // 初始化页面控件
		portal.loadDataSource();
		/**
		 * Click事件绑定
		 * @author ZhangHui 2015/4/22
		 */
		portal.bindClick();
	};
	
	/**
	 * 初始化页面组件
	 */
	portal.initialComponent = function(){
		/* 根据当前企业类型，自定义初始化 */
		portal.initComponentByType();
		/* 初始化产品图片上传控件 */
		upload.buildUpload("upload_head_files", portal.aryHeadAttachments, "proFileMsg", "IMG",false);
		upload.buildUpload("upload_health_files", portal.aryHealthAttachments, "healthFileMsg", "IMG",false);
		upload.buildUpload("upload_qualification_files", portal.aryWorkAttachments, "qualificationFileMsg","IMG",false);
		upload.buildUpload("upload_honor_files", portal.aryHonorAttachments, "honorFileMsg", "IMG",true);
		/* 初始化Window弹框 */
		fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');
		fsn.initKendoWindow("saveWindow","警告","400px","",false,null);

		//初始化性别下拉框
		$("#sex").kendoDropDownList();
		//初始化民族下拉框
		$("#nation").kendoComboBox({
			dataTextField: "value",
			dataValueField: "id",
			optionLabel:"--请选择--",
			dataSource: fsn.getDataSet("mz"),
			index: -1
		});
		//初始化证件类型下拉框
		$("#credentialsType_q").kendoDropDownList({
			dataTextField: "value",
			dataValueField: "id",
			optionLabel:"--请选择--",
			dataSource: fsn.getDataSet("zjlx")
		});
		//初始化职务下拉框
		$("#position").kendoDropDownList({
			dataTextField: "value",
			dataValueField: "id",
			optionLabel:"--请选择--",
			dataSource: fsn.getDataSet("zw")
		});

		//初始化人员类型下拉框
		$("#personType").kendoDropDownList({
			dataTextField: "value",
			dataValueField: "id",
			optionLabel:"--请选择--",
			dataSource: fsn.getDataSet("rylx"),
			change: function(e) {
				var text = this.text();
				if(text=="从业人员"){
					 $(".cyry").show();
				}else{
					$(".cyry").hide();
				}

			}
		});

	};
	
	/**
	 * Click绑定事件
	 * @author ZhangHui 2015/4/22
	 */
	portal.bindClick = function(){
		$("#queryMember").bind("click", portal.queryMemberInfo);
		/* save 保存按钮 */
		$("#save").on("click", portal.save);
		/* 清空产品图片按钮  */
		$("#btn_clearProFiles").bind("click", portal.clearProFiles);
		$("#healthCertificateNo").bind("blur", function(){
			var healthCertificateNo=$("#healthCertificateNo").val().trim();
			$("#healthNo").val(healthCertificateNo);
		});
		$("#healthNo").bind("blur", function(){
			var healthCertificateNo=$("#healthNo").val().trim();
			$("#healthCertificateNo").val(healthCertificateNo);
		});
	};
	/**
	 * 查询人员信息
	 */
	portal.queryMemberInfo=function () {
		$("#queryFinish").hide();
		$("#isJg").hide();
		var name=$("#name_q").val().trim();
		if(name==""){
			lims.initNotificationMes("请填写姓名！", false);
			return false;
		}
		var credentialsType=$("#credentialsType_q").data("kendoDropDownList").value();
		var credentialsTypeName=$("#credentialsType_q").data("kendoDropDownList").text();
		if(credentialsType==""){
			lims.initNotificationMes("请选择证件类型！", false);
			return false;
		}
		var identificationNo=$("#identificationNo_q").val().trim();
		if(identificationNo==""){
			lims.initNotificationMes("请填写证件号码！", false);
			return false;
		} else if(credentialsTypeName=="中华人民共和国居民身份证"){
//			15位数身份证验证正则表达式：
			var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
//			18位数身份证验证正则表达式 ：
			var isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
			if(!identificationNo.match(isIDCard1) && !identificationNo.match(isIDCard2) ){
				$("#identificationNo").focus();
				lims.initNotificationMes(fsn.l("身份证号不正确!"), false);
				return false;
			}
		}
		/**
		 * 到监管查询人员是否存在
		 */
		$.ajax({
			url : portal.HTTP_PREFIX + "/member/queryMemberByJg",
			type : 'GET',
			datatype : 'json',
			data:{name:name,credentialsType:credentialsTypeName,identificationNo:identificationNo},
			async:false,
			success : function(returnValue) {
				    if(returnValue.result.status=="true"){
						if(returnValue.data==""){
							portal.memberOrigin= "1";
							portal.baseInfoReadOnly(false);
							$("#isJg").hide();
						}else{
							portal.memberOrigin= "2";
							$("#isJg").show();
							portal.setMemberBaseInfo(returnValue.data);
							portal.baseInfoReadOnly(true);
						}
					}
			}
		});

		$("#name").html(name);
		$("#credentialsType").html($("#credentialsType_q").data("kendoDropDownList").text());
		$("#identificationNo").html(identificationNo);
		$("#queryFinish").show();
	} ;
	/**
	 * 设置人员基本信息只读
	 */
	portal.baseInfoReadOnly=function(status){
		$("#address").attr("disabled",status);
		$("#appointUnit").attr("disabled",status);
		$("#sex").data("kendoDropDownList").readonly(status);
		$("#nation").data("kendoComboBox").readonly(status);
		$("#position").data("kendoDropDownList").readonly(status);
		$("#personType").data("kendoDropDownList").readonly(status);
		$("#workType").attr("disabled",status);
		$("#healthCertificateNo").attr("disabled",status);
		$("#issueUnit").attr("disabled",status);
		$("#email").attr("disabled",status);
		$("#mobilePhone").attr("disabled",status);
		$("#tel").attr("disabled",status);
	}  ;
	/**
	 * 设置人员基本信息
	 * @param data
     */
	portal.setMemberBaseInfo=function (data) {
		$("#address").val("");
		$("#appointUnit").val("");
		$("#sex").data("kendoDropDownList").text("");
		$("#nation").data("kendoComboBox").text("");
		$("#position").data("kendoDropDownList").text("");
		$("#personType").data("kendoDropDownList").text("");
		$("#workType").val("");
		$("#healthCertificateNo").val("");
		$("#issueUnit").val("");
		$("#email").val("");
		$("#mobilePhone").val("");
		$("#tel").val("");
	} ;
	/**
	 * 根据企业类型初始化页面
	 * @author Zhanghui 2015/4/3
	 */
	portal.initComponentByType = function() {
		portal.currentBusiness = getCurrentBusiness();
		var busType = portal.currentBusiness.type.trim();
	};

	/**
	 * 设置查询信息为只读
	 */
	portal.queryReadOnly=function(){
		$("#name_q").attr("disabled",true);
		$("#identificationNo_q").attr("disabled",true);
		$("#queryMember").hide();
		$("#credentialsType_q").data("kendoDropDownList").readonly(true);
	}  ;
	
	/**
	 * 产品编辑时，初始化数据
	 */
	portal.loadDataSource = function() {
		if(portal.edit_id){
			portal.queryReadOnly();
			portal.isNew = false;
			$.ajax({
				url : portal.HTTP_PREFIX + "/member/" + portal.edit_id+"/"+portal.identify,
				type : 'GET',
				datatype : 'json',
				data:{identify:portal.identify},
				async:false,
				success : function(returnValue) {
					if(returnValue.data != null){
						portal.writeMemberInfo(returnValue.data); // product信息show
						portal.setHdAttachments(returnValue.data.hdAttachments); // 人员证件照图片
						portal.setHthAttachments(returnValue.data.hthAttachments); // 健康证图片
						portal.setQcAttachments(returnValue.data.qcAttachments); // 从业资格证图片
						portal.setHnAttachments(returnValue.data.hnAttachments); // 荣誉证书图片
						portal.memberOrigin= returnValue.data.origin;
						if(portal.memberOrigin=="2"){   //如果为人员来自监管基本信息不能修改，设为只读
							portal.baseInfoReadOnly(true);
							$("#isJg").show();
						} else{
							portal.baseInfoReadOnly(false);
							$("#isJg").hide();
						}

					}
				}
			});
			/**
	         * 发布按钮初始化
	         * @author ZhangHui 2015/5/7
	         */

	        $("#save").hide();
	        $("#update").bind("click", portal.save);
	        $("#update").css("display", "block");
			$("#queryFinish").show();
		}else{
	         $("#logListView").hide();
			$("#queryFinish").hide();
		}
	};
	
	
	/**
	 * 提交前验证必填项
	 */
	portal.validateProduct = function () {	
		// 检测用户输入，如果不合法直接提示并中断提交操作
		var name = $("#name").html().trim();
		if(portal.isBlank(name)){
			lims.initNotificationMes("请输入姓名！", false);
			return false;
		}

		var credentialsType = $("#credentialsType").html().trim();
		if(portal.isBlank(credentialsType)){
			lims.initNotificationMes("请选择证件类型！", false);
			return false;
		}

		var identificationNo = $("#identificationNo").html().trim();
		if (portal.isBlank(identificationNo)) {
			lims.initNotificationMes("请输入身份证号！", false);
			return false;
		}else if(credentialsType=="中华人民共和国居民身份证"){
//			15位数身份证验证正则表达式：
			var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/; 
//			18位数身份证验证正则表达式 ：
			var isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
			 if(!identificationNo.match(isIDCard1) && !identificationNo.match(isIDCard2) ){
				 $("#identificationNo").focus();
				 lims.initNotificationMes(fsn.l("身份证号不正确!"), false);
	    		 return false;
			 }
		}

		var sex=$("#sex").data("kendoDropDownList").value();
		if (sex=="0") {
			lims.initNotificationMes("请选择性别！", false);
			return false;
		}

		var nation=$("#nation").data("kendoComboBox").value();
		if (nation=="") {
			lims.initNotificationMes("请选择民族！", false);
			return false;
		}

		var position=$("#position").data("kendoDropDownList").value();
		if (position=="") {
			lims.initNotificationMes("请选择职务！", false);
			return false;
		}

		var personType=$("#personType").data("kendoDropDownList").value();
		if (personType=="") {
			lims.initNotificationMes("请选择人员类型！", false);
			return false;
		}
		var personTypeName=$("#personType").data("kendoDropDownList").text();
		if(personTypeName=="从业人员"){
			var workType=$("#workType").val().trim();
			if(portal.isBlank(workType)){
				$("#workType").focus();
				lims.initNotificationMes("请输入工种！", false);
				return false;
			}
			var healthCertificateNo=$("#healthCertificateNo").val().trim();
			if(portal.isBlank(healthCertificateNo)){
				$("#healthCertificateNo").focus();
				lims.initNotificationMes("请输入健康证编号！", false);
				return false;
			}
			var issueUnit=$("#issueUnit").val();
			if(portal.isBlank(issueUnit)){
				$("#issueUnit").focus();
				lims.initNotificationMes("请输入发证单位！", false);
				return false;
			}
		}
		var tel = $("#mobilePhone").val();
		if (portal.isBlank(tel)) {

		}else{
			var telReg=/(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/;
			if(!tel.match(telReg)){
				$("#mobilePhone").focus();
				lims.initNotificationMes("手机号码不正确！", false);
				return false;
			}
		}

		var email=$("#email").val();
		if(!portal.isBlank(email)){
			var emailReg=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
			if(!email.match(emailReg)){
				$("#email").focus();
				lims.initNotificationMes("邮箱格式不正确！", false);
				return false;
			}
		}

		return true;
	};
	
	/**
	 * 提交时，构建产品对象
	 */
	portal.createProduct = function(){

		var product = {
				id: $("#id").val().trim(),
				origin: portal.memberOrigin,
				name: $("#name").html(),  // 产品名称
				identificationNo: $("#identificationNo").html(),  // 身份证号
				credentialsType: $("#credentialsType").html(),  // 身份证号
				sex: $("#sex").data("kendoDropDownList").text(),
				nation: $("#nation").data("kendoComboBox").text(),
				position: $("#position").data("kendoDropDownList").text(),
				appointUnit: $("#appointUnit").val().trim(),  // 任免单位
				personType: $("#personType").data("kendoDropDownList").text(),
				workType: $("#workType").val().trim(),  // 工种
				issueUnit: $("#issueUnit").val().trim(),  // 发证单位
				mobilePhone: $("#mobilePhone").val().trim(),  // 手机
				description: $("#description").val().trim(),  // 个人简介
				address: $("#address").val().trim(),  // 住址
				email: $("#email").val().trim(),  // 邮箱
				tel: $("#tel").val().trim(), // 固定电话
				healthNo: $("#healthNo").val().trim(),  // 健康证号
				qualificationNo: $("#qualificationNo").val().trim(),  // 从业资格证号
				honorNo1: $("#honorNo1").val().trim(),  // 荣誉证书证号1
				honorNo2: $("#honorNo2").val().trim(),  // 荣誉证书证号2
				honorNo3: $("#honorNo3").val().trim(),  // 荣誉证书证号3
				hdAttachments: portal.aryHeadAttachments,// 产品图片
				hthAttachments: portal.aryHealthAttachments,// 健康证图片
				qcAttachments: portal.aryWorkAttachments,// 从业资格证图片
				hnAttachments: portal.aryHonorAttachments// 荣誉证书图片
    	};
		
		// 返回
		return product;
	};
	
	/**
	 * save 数据保存
	 */
	portal.save = function(){
		if(portal.memberOrigin!="2"){ //来自监管的人员 不验证必填项
			if(!portal.validateProduct()){
				return;
			}
		}
		portal.upload();
	};
	
	
	/**
	 * save 提交验证通过的数据
	 */
	portal.upload = function() {
		var save_product = portal.createProduct();
		
		if(portal.isNew){
			$("#winMsg").html("正在保存数据，请稍候....");
		}else{
			$("#winMsg").html("正在执行更新操作，请稍候....");
		}
		
		$("#k_window").data("kendoWindow").open().center();

		$.ajax({
				url : portal.HTTP_PREFIX + "/member/" + portal.currentBusiness.name + "/" + portal.isNew,
				type : "POST",
				datatype : "json",
				contentType: "application/json; charset=utf-8",
				data : JSON.stringify(save_product),
				success : function(returnValue) {
					$("#k_window").data("kendoWindow").close();
					if (returnValue.result.status == "true") {
						lims.initNotificationMes(portal.isNew ? '保存成功！' : '更新成功！', true);
						if (portal.isNew) {
							$("#id").val(returnValue.data.id);
							portal.isNew = false;
						}
						portal.setHdAttachments(returnValue.data.hdAttachments); // 人员证件照图片
						portal.setHthAttachments(returnValue.data.hthAttachments); // 健康证图片
						portal.setQcAttachments(returnValue.data.qcAttachments); // 从业资格证图片
						portal.setHnAttachments(returnValue.data.hnAttachments); // 荣誉证书图片
						$("ul.k-upload-files").remove();
						location.href="member_list.html";
						
					}else{
						lims.initNotificationMes(returnValue.result.errorMessage, false);
					}
				}
		});
	};
	
	portal.init();
});