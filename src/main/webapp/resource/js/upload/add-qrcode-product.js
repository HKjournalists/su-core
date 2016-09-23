$(document).ready(function() {
	/* 定义全局变量 */
	var root = fsn.root = fsn.root || {};
	var procom = fsn.procom = fsn.procom || {};
	root.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	root.aryProAttachments = new Array();
	root.edit_id = null;
	root.isNew = true;
	var chanshu = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	var pid = chanshu[0];
	var orig_pidmd5 = chanshu[1];
	try {
		//root.edit_id = $.cookie("user_0_edit_product").id;
		$.cookie("user_0_edit_product", JSON.stringify({}), {
			path : '/'
		});
	} catch (e) {
	}

	/* 页面初始化方法 */
	function init() {
		root.edit_id =root.md5validate(pid,orig_pidmd5);
		/* 获取当前登陆用户的企业信息 */
		getCurrBusUniFun();
		/* 初始化页面产品营业报告gird */
		procom.initProductNutri("product-nutri-grid","toolbar_template_nutri", initNutriDS);
		/* 产品图片上传按钮初始化 */
		$("#uploadZone").html("<input type='file' id='upload_product_files'/>");
		procom.buildUpload("upload_product_files",root.aryProAttachments, "proFileMsg", "product");
		$("#div_qrcodeImg").css("display","none");
		/* 初始化产品分类和执行标准 */
		initCategory();		
		/* 初始化kms标签控件 */
		intiKMSLabel();
		$("#proResListView").hide();
		$("#update").hide();
		loadEditData(root.edit_id);
		/* 保存按钮绑定保存方法 */
		$("#save").bind("click", saveQRCodeProductInfo);
		$("#update").bind("click", saveQRCodeProductInfo);
		$("#btn_clearProFiles").bind("click",procom.clearProFiles);
		//保存新增标准
		 $("#btn_regularity_save").click(function(){
			$("#regularity").val($("#regularityList").text());
			$("#addRegularity_window").data("kendoWindow").close();
		});
	    //取消新增标准
		$("#btn_regularity_cancel").click(function(){
		 	$("#addRegularity_window").data("kendoWindow").close();
		 });
		 $("#btn_regularity_add").click(function(){
			 procom.addRegularity();
		});
		 /* 初始化页面的保存提示窗口 */
		procom.initKendoWindow("k_window", "保存状态", "300px", "60px",false, '[]');
		procom.initKendoWindow("saveWindow", "警告", "400px", "", false,'[]');
		procom.initKendoWindow("addRegularity_window", "选择执行标准", "800px", "400px",false, null);
	};
	//验证标签是否存在
	function checkLableTextExit(){
		var labelBox = $("#kmsLabel").data("kendoComboBox");
		if(labelBox==null) return true;
		var labelId = labelBox.value().trim();
		var labelName = labelBox.text().trim();
		if(labelName!=""&&labelId==labelName){
			$("#kmsLabel").data("kendoComboBox").text("");
			return false;
		}
		return true;
	};
	
	/*二维码产品赋值*/
	function setQRCodeProductInfo(qrcodeProduct) {
		if (qrcodeProduct == null) return;
		var product = qrcodeProduct.product;
		procom.writeProductInfo(product); // product信息show
		procom.initProductNutri("product-nutri-grid","toolbar_template_nutri", product.listOfNutrition); // 营养报告列表show
		procom.initialCertGrid("certification-grid",product.listOfCertification);
		procom.setProAttachments(product.proAttachments);
		$("#div_qrcodeImg").css("display","inline");
		$("#qrcodeImg").attr("src",qrcodeProduct.resource.url);
		$("#qrcodeImg").attr("data-id-text",qrcodeProduct.resource.id);
		var busName = product.producer.name;
		$("#businessName").val(product.producer.name);
		procom.setProducerAddress(product.producer);
		if(root.busType=="流通企业"){
			$("#innerCode").val(qrcodeProduct.innerCode);
			$("#productArea").data("kendoComboBox").value(qrcodeProduct.productAreaCode);
			if(root.enterpriseName==busName){
				document.getElementById("selfSupport").checked=true;
				$("#mainAddress").attr("disabled","disabled");
				$("#streetAddress").attr("disabled","disabled");
				$("#businessName").attr("disabled","disabled");
			}
		}
		if(qrcodeProduct.kmsLabel!=null){
			var label = qrcodeProduct.kmsLabel;
			var ds = {kwdItemId:label.kmsLabelId,kwdItemName:label.labelName};
			$("#kmsLabel").data("kendoComboBox").setDataSource("");
			$("#kmsLabel").data("kendoComboBox").dataSource.add(ds);
			$("#kmsLabel").data("kendoComboBox").text(label.labelName);
		}
	};

	/*实现产品单位自动搜索*/
	$("#productUnit").kendoAutoComplete({
        dataSource: lims.getAutoLoadDsByUrl("/product/getAllUnitName"),
        filter: "contains",
        placeholder: "例如：60g、箱、500ml/瓶、2瓶/盒、克/袋",
        select: function(e){
			 var unitName = this.dataItem(e.item.index());
	         $("#productUnit").val(unitName);
		 },
    });
	
	function loadEditData(editId) {
		if (editId == null)
			return;
		root.isNew = false;
		$("#save").hide();
		$("#update").show();
		$.ajax({
			url : root.HTTP_PREFIX + "/product/getQRCodeByProductId/"+ editId,
			type : 'GET',
			datatype : 'json',
			async : false,
			success : function(returnValue) {
				var qrcodeProduct = returnValue.data;
				if (qrcodeProduct != null) {
					root.serialNumber = qrcodeProduct.serialNumber;
					setQRCodeProductInfo(qrcodeProduct);
				}
			}
		});
	}
	;

	/* 如果是生存企业，隐藏部分字段，调整页面显示样式 */
	function setSCPageStyle() {
		$(".div_busLT").css("display", "none");
		//$("#barcode").css("width", "430px");
		$("#businessBrand").css("width", "430px");
	};

	/*自营checkbox的点击事件*/
	$("#selfSupport").click(function() {
		var flag = document.getElementById("selfSupport").checked;
		$("#businessName").val("");
		$("#streetAddress").val("");
		$("#mainAddress").val("");
		$("#mainAddress").removeAttr("disabled");
		$("#streetAddress").removeAttr("disabled");
		$("#businessName").removeAttr("disabled");
		if (flag) {
			$("#businessName").val(root.enterpriseName);
			procom.setProducerAddress(root.busUnit);
			var mad = $("#mainAddress").val().trim();
			$("#mainAddress").attr("disabled","disabled");
			$("#streetAddress").attr("disabled","disabled");
			$("#businessName").attr("disabled","disabled");
			if(mad==""){
				lims.initNotificationMes("当前企业地址不规范，无法加载赋值！", false);
				return;
			} 
			var areaCode = validateAddress();
			if (areaCode == null || areaCode == "") {
				lims.initNotificationMes("企业地址无法识别出省市县代码，请到企业基本信息页面重新选择！", false);
				return;
			}
		}
	});
	
	/*点击执行标准事件*/
	 $("#regularityDiv").click(function(){
	 	var index = $("#category2").data("kendoComboBox").select();
		if(index==-1){
			lims.initNotificationMes("请先选择食品类别！", false);
			return;
		}else{
			var index = $("#category2").data("kendoComboBox").select();
			if(index==-1){
				lims.initNotificationMes("请先选择二级分类后再新增执行标准！", false);
				return;
			}
			//清空信息
			$("#regularity_type").val("");
			$("#regularity_year").val("");
			$("#regularity_name").val("");
			$("#regularityInfo").data("kendoComboBox").value("");
			$("#regularityList").text("");
			
			//执行标准赋值
			procom.setRegularityValue();
			$("#addRegularity_window").data("kendoWindow").open().center();
		}
	 });
	  
	/* 初始化产品类别 */
	var initCategory = function() {
		//执行标准
				$("#regularityInfo").kendoComboBox({
			        dataTextField: "name",
			        dataValueField: "id",
			        dataSource: [],
			        filter: "contains",
					placeholder : "请选择...",
			        minLength: 0,
			        index:0,
					change:function(){
						var index =$("#regularityInfo").data("kendoComboBox").select();
						if (index!=-1) {
							//如果超过12，则不允许添加
							var ul = document.getElementById("regularityList");
							var count = ul.getElementsByTagName("li").length;
							if(count>=12){
								lims.initNotificationMes("执行标准过多，不能再添加！", false);
								return;
							}
							//将选中的值添加到列表中
							var text = $("#regularityInfo").data("kendoComboBox").dataItem().name;
							if(!procom.validaRegularity(text)){return;}
							var li= document.createElement("li");
	  						li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>"+text+";</a>";
							ul.appendChild(li);
							//将Item从dataSource中移除
							$("#regularityInfo").data("kendoComboBox").dataSource.remove($("#regularityInfo").data("kendoComboBox").dataItem());
						}
						this.value("");
					}
			    });
				//三级分类
				$("#category3").kendoComboBox({
			        dataTextField: "name",
			        dataValueField: "id",
			        dataSource: [],
			        filter: "contains",
					placeholder : "请选择...",
			        minLength: 0,
			        index:0,
					change: function(){
					var index = $("#category2").data("kendoComboBox").select();
					if (index == -1) {
						$("#category3").data("kendoComboBox").value("");
						lims.initNotificationMes("请先选择二级分类！", false);
						return;
					}
				}
			    });
				//二级分类
				$("#category2").kendoComboBox({
			        dataTextField: "name",
			        dataValueField: "id",
			        dataSource: [],
			        filter: "contains",
					placeholder : "请选择...",
			        minLength: 0,
			        index:0,
					change:function(){
						var index = $("#category2").data("kendoComboBox").select();
						if(index==-1){
							$("#category2").data("kendoComboBox").value("");
							$("#category3").data("kendoComboBox").setDataSource([]);
							$("#category3").data("kendoComboBox").value("");
							$("#regularity").val("");
							return;
						}
						$.ajax({
							url:root.HTTP_PREFIX + "/testReport/searchLastCategory/"+this.value()+"?categoryFlag="+true,
							type:"GET",
							dataType:"json",
							async:false,
							success:function(returnValue){
								if(returnValue.result.status == "true"){
									$("#category3").data("kendoComboBox").setDataSource(returnValue.data);
									$("#regularity").val("");
									if(returnValue.data.length==1){
										$("#category3").data("kendoComboBox").select(0);
									}else{
										$("#category3").data("kendoComboBox").value("");
									}
								}
							},	
						});	
					},
			    });
				//一级分类
				$("#category1").kendoComboBox({
			        dataTextField: "name",
			        dataValueField: "code",
			        dataSource: [],
			        filter: "contains",
					placeholder : "请选择...",
			        minLength: 0,
			        index:0,
					change:function(){
						var index = $("#category1").data("kendoComboBox").select();
						if(index==-1){
							$("#category1").data("kendoComboBox").value("");
							$("#category2").data("kendoComboBox").setDataSource([]);
							$("#category2").data("kendoComboBox").value("");
							$("#category3").data("kendoComboBox").setDataSource([]);
							$("#category3").data("kendoComboBox").value("");
							$("#regularity").val("");
							return;
						}else{
							var code =  $("#category1").data("kendoComboBox").dataItem().code;
							if(code=='15'){
								$(".nianYeRi").hide();
							}else{
								$(".nianYeRi").show();
							}
						}
						$.ajax({
							url:root.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode="+this.value(),
							type:"GET",
							dataType:"json",
							async:false,
							success:function(returnValue){
								if(returnValue.result.status == "true"){
									$("#category2").data("kendoComboBox").setDataSource(returnValue.data);
									$("#category3").data("kendoComboBox").setDataSource([]);
									$("#category3").data("kendoComboBox").value("");
									$("#category2").data("kendoComboBox").value("");
									$("#regularity").val("");
								}
							},	
						});	
					},					
			    });
				$.ajax({
					url:root.HTTP_PREFIX + "/testReport/searchCategory/1?parentCode=",
					type:"GET",
					dataType:"json",
					async:false,
					success:function(returnValue){
						if(returnValue.result.status == "true"){
							$("#category1").data("kendoComboBox").setDataSource(returnValue.data);
							if(root.edit_id == null){
								 $("#category1").data("kendoComboBox").value("");
							}
						}
					},
				});				
	};

	/* 初始化产品所属区域--流通企业 */
	var initLTProductArea = function() {
		var proAreaDS = procom.getDataSourceByUrl(procom.HTTP_PREFIX,"/product/getAllProductArea");
		procom.initialComboBox("productArea", proAreaDS, "displayName","code");
	};

	var labelKeyword="";
	
	getKMSLabelDS = function() {
		var ds = [];
		$.ajax({
			url: fsn.getKMSPrefix() + "/lims-standard-cloud/service/knowledgeItem/fetchKwIemListByType?page=1&pageSize=50&keyword="+labelKeyword,
            async:false,
            type: "GET",
            success:function(data){
            	ds = data;
            }
		});
		return ds;
    };
	
	var intiKMSLabel = function() {
		var ds = getKMSLabelDS();
		$("#kmsLabel").kendoComboBox({
	        dataTextField: 'kwdItemName',
	        dataValueField: 'kwdItemId',
	        dataSource: ds,
	        filter: "contains",
	        minLength: 0,
	        dataBound: function() {
                if(labelKeyword == this.text().trim()) {
                	return;
                }
                labelKeyword = this.text().trim();
                this.setDataSource(getKMSLabelDS());
            },
			change:function(){
				var val = this.text().trim();
				var index = this.select();
				if(this !=null &&  val != "" &&  index == -1){
					this.value("");
					lims.initNotificationMes(fsn.l("The label does not exist, please re-enter") + "!", false);
					this.dataSource.read();
				}
			}
	    });
	};

	/* 获取当前企业信息的方法 */
	var getCurrBusUniFun = function() {
		$.ajax({
			url : root.HTTP_PREFIX + "/business/getCurrentBusiness",
			type : "GET",
			async:false,
			success : function(returnValue) {
				if (returnValue.result.status == "true") {
					root.enterpriseName = returnValue.data.name;
					root.busType = returnValue.data.type;
					root.busUnit = returnValue.data;
					if (root.busType == "流通企业") {
						procom.initBusNameAndBrandComplete(); // 初始化所属品牌
						initLTProductArea();
						$(".div_busSC").hide();
					} else {
						setSCPageStyle();
						/*初始化所属品牌*/
						procom.initialBrands();
						/* 初始化产品认证信息Grid */
						procom.initialCertGrid("certification-grid", null);
						$("#businessName").val(root.enterpriseName);
						 var addrCode =  validateAddress();
						 if(addrCode==null||addrCode==""){
							 lims.initNotificationMes("企业地址无法识别出省市县代码，请到企业基本信息页面重新选择！", false);
						 }
						 
					}
				}
			}
		});
	};

	/* 页面数据有效性验证 */
	var validateData = function() {
		/* 页面共有字段的验证 */
		var pubVFlag = procom.valiPubField();
		if (!pubVFlag) return false;
		if(root.busUnit.type=="流通企业"){
			var busName = $("#businessName").val().trim();
			var mainAddr = $("#mainAddress").val().trim();
			if(busName==null||busName==""){
				lims.initNotificationMes("生产企业不能为空！", false);
				return false;
			}
			if(mainAddr==null||mainAddr==""){
				lims.initNotificationMes("生产企业省市县地址不能空！", false);
				return false;
			}
		}
		/* 页面独立字段的验证 */
		var indexCategory1 =$("#category1").data("kendoComboBox").select();
		if (indexCategory1==-1) {
			$("#category1").focus();
			lims.initNotificationMes("请选择产品所属的食品种类一级分类！", false);
			return false;
		}
		var indexCategory2 =$("#category2").data("kendoComboBox").select();
		if (indexCategory2==-1) {
			$("#category2").focus();
			lims.initNotificationMes("请选择产品所属的食品种类二级分类！", false);
			return false;
		}
		var category3 =$("#category3").data("kendoComboBox").text().trim();
		if (category3==""|| category3 == "请选择...") {
			$("#category3").focus();
			lims.initNotificationMes("请选择产品所属的食品种类三级分类！", false);
			return false;
		}
		var year=$("#proExpirYear").val();
    	var month=$("#proExpirMonth").val();
    	var day=$("#proExpirDay").val();
		var code =  $("#category1").data("kendoComboBox").dataItem().code;
    	if(code!='15'&&year==""&&month==""&&day==""){
    		lims.initNotificationMes("请填写产品的保质期！", false);
			return false;
    	}
        var regularity= $("#regularity").val().trim();
		if (regularity=="") {
			$("#regularity").focus();
			lims.initNotificationMes("请填写执行标准！", false);
			return false;
		}
		return true;
	};

	/* 验证企业地址，并解析出省市县代码 */
	function validateAddress() {
		var addr = null;
		var areaCode = null;
		if (root.busType == "流通企业") {
			addr = $("#mainAddress").val();
			areaCode = $("#mainAddress").attr("data-aId-text");
		} else {
			addr = root.busUnit.otherAddress;
		}
		if (areaCode != null && areaCode != "")
			return areaCode;
		addr = addr.split("--")[0];
		if (addr == null || addr == "") {
			return null;
		}
		$.ajax({
			url : root.HTTP_PREFIX + "/erp/address/getAreaCodeByAddress/" + addr,
			type : "GET",
			async : false,
			success : function(data) {
				if (data.result.status) {
					areaCode = data.areaCode;
				}
			}
		});
		return areaCode;
	};
	
	/* 验证流通企业产品内部码  */
	function validateInnerCode() {
		var valStatus = true;
		var innerCode = $("#innerCode").val().trim();
		var proId = $("#id").val();
		if(innerCode==null||innerCode=="") return valStatus;
		$.ajax({
			url : root.HTTP_PREFIX + "/product/validateInnerCode/" + innerCode + "?productId=" + proId,
			type : "GET",
			async : false,
			success : function(data) {
				if (data.result.status) {
					valStatus = data.status;
				}
			}
		});
		return valStatus;
	}

	var createQRCodeProductInfo = function() {
		/* kms提供的标签值 */
		var kmsLabel = null;
		var kmsLabelBox = $("#kmsLabel").data("kendoComboBox");
		if(kmsLabelBox != null && kmsLabelBox.select() != -1){
			kmsLabel = {
				kmsLabelId : $("#kmsLabel").data("kendoComboBox").dataItem().kwdItemId,
				labelName : $("#kmsLabel").data("kendoComboBox").dataItem().kwdItemName,
			};
		}
		var qrcodeProduct = {
			innerCode : $("#innerCode").val().trim(),
			productAreaCode : (root.busUnit.type=="流通企业"?$("#productArea").data("kendoComboBox").value():"00"),
			kmsLabel : kmsLabel,
			proAreaName : (root.busUnit.type=="流通企业"?$("#productArea").data("kendoComboBox").text():""),
			serialNumber:root.serialNumber,
		};
		return qrcodeProduct;
	};

	// save 提交验证通过的数据
	function submitData(){
		if (root.isNew) {
			$("#winMsg").html("正在保存数据，请稍候....");
		} else {
			$("#winMsg").html("正在执行更新操作，请稍候....");
		}
		$("#k_window").data("kendoWindow").open().center();
		$.ajax({
			url : root.HTTP_PREFIX + "/product/saveQRCodeProduct",
			type : root.isNew ? "POST" : "PUT",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(qrcodeProduct),
			success : function(returnValue) {
				$("#k_window").data("kendoWindow").close();
				if (returnValue.result.status == "true") {
					lims.initNotificationMes(root.isNew ? '保存成功！': '更新成功！', true);
					var qrcodeinfo = returnValue.data;
					var product = qrcodeinfo.product;
					//把返回的ID保存在cookie中
					/*try {
						if(product != null && product.id != null && product != ''){
							$.cookie("user_0_edit_product", JSON.stringify({id : returnValue.data.product.id}), {
								path : '/fsn-core/views/portal/'
							});
						}
				    } catch (e) {
				    }*/
					root.serialNumber=qrcodeinfo.serialNumber;
					/* 产品图片上传按钮初始化 */
					$("#uploadZone").html("<input type='file' id='upload_product_files'/>");
					procom.buildUpload("upload_product_files",root.aryProAttachments, "proFileMsg", "product");
					setQRCodeProductInfo(qrcodeinfo);
					if (root.isNew) {
						$("#update").show();
						$("#save").hide();
					}
					root.isNew = false;
					$("#div_qrcodeImg").css("display","inline");
					$("#qrcodeImg").attr("src", qrcodeinfo.resource.url);
					$("#qrcodeImg").attr("data-id-text", qrcodeinfo.resource.id);
					$("ul.k-upload-files").remove();
				}else{
					lims.initNotificationMes(root.isNew ? '保存失败！': '更新失败！', false);
				}
			}
	});
	};
	
	$("#btn_saveOK").click(function(){
		$("#saveWindow").data("kendoWindow").close();
		submitData();
	});

	 $("#btn_saveCancel").click(function(){
		$("#saveWindow").data("kendoWindow").close();
	});
	
	$("#qrcodeImg").click(function(){
		var url = $("#qrcodeImg").attr("src");
		if(url!=null){
			window.open(url);
		}
	});
	
	root.md5validate = function(str1,md5code){
		var md5 = $.md5(str1);
		if(md5===md5code){
			return str1;
		}else if(str1 && md5code){
			lims.initNotificationMes("产品id有误，请从产品管理编辑进入。", false);
			return null;
		}
	}
	
	/* 下載二维码图片 */
	root.downLoad = function(){
		 var resourceId = $("#qrcodeImg").attr("data-id-text");
		 if(resourceId != null) {
			 var httpUrl = root.HTTP_PREFIX + "/resource/download/" + resourceId;
			 lims.downloadByUrl(httpUrl);
		 } else {
			 lims.initNotificationMes(fsn.l("The current picture can not find the download path") + "!", false);
		 }
	 };
	 
	var qrcodeProduct ={};
	/* 保存页面信息 */
	var saveQRCodeProductInfo = function() {
		/* 1、数据有效性验证 */
		var flag = validateData();
		if (!flag) return;
		var areaCode = validateAddress();
		if (areaCode == null || areaCode == "") {
			lims.initNotificationMes("企业地址无法识别出省市县代码，请到企业基本信息页面重新选择！", false);
			return;
		}
		/*流通企业验证产品内部编码*/
		if(root.busUnit.type=="流通企业"){
			var status = validateInnerCode();
			if(!status) {
				lims.initNotificationMes("产品内部码重复！", false);
				return false;
			}
		}
		/* 封装产品营养报告 */
		var proNutrs = procom.createInstanceProductNutris();
		/* 封装页面产品信息 */
		var product = procom.createProduct();
		product.listOfNutrition = proNutrs;
		product.proAttachments = root.aryProAttachments;
		product.packageFlag = (root.busUnit.type == "流通企业" ? "1" : "2");
		var varLocal = false;
		if(fsn.root.busUnit.type!="流通企业"||document.getElementById("selfSupport").checked){
			varLocal = true;
		}
		product.local = varLocal;
		/* 封装二维码产品信息 */
		qrcodeProduct = createQRCodeProductInfo();
		qrcodeProduct.product = product;
		qrcodeProduct.cityCode = areaCode;
		if(root.aryProAttachments.length==0){
				$("#saveWindow").data("kendoWindow").open().center();
		}else{
			submitData();
		}
	};

	/* 营养报告初始数据 */ 
	var initNutriDS = [{ name:"", value:"", unit:"", per:"", nrv:""},
	                   { name:"", value:"", unit:"", per:"", nrv:""},
	                   { name:"", value:"", unit:"", per:"", nrv:""}];
	
	/*页面初始化 */
	init();
		//移除执行标准
	 delRegularity = function(e){
	 	var name = e.parentNode.title;
		$("#regularityInfo").data("kendoComboBox").dataSource.add({ name:name});
 		e.parentNode.parentNode.parentNode.removeChild(e.parentNode.parentNode)
	}
});