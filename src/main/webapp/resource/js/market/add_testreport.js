$(document).ready(function() {
	var lims = window.lims = window.lims || {};
	var fsn = window.fsn = window.fsn || {};
	var portal = fsn.portal = fsn.portal || {};
	var root = window.lims.root = window.lims.root || {};
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	root._testProperties = [];
	/**
	 * 是否为新增报告的标识
	 * true: 新增报告
	 * false: 更新报告
	 * @author ZhangHui 2015/4/30
	 */
	root.isNew = true;

	/**
	 * 是否自动生成pdf的标记
	 * true: 自动生成pdf
	 * false: 用户自定义上传pdf
	 * @author ZhangHui 2015/4/30
	 */
	root.isAutoReport = false;

	/**
	 * 待更新报告处理的标记
	 * true 代表的是处理待更新报告时的新增报告
	 * false 代码正常情况下的新增/编辑报告信息
	 */
	root.updateApply = false;

	/**
	 * 产品赋值类型：
	 *  product_input 代表 产品信息使用input输入框赋值
	 *  product_text  代表 产品信息使用text赋值
	 *  @author tangxin 2015/6/8
	 */
	root.product_html_type = "product_input";

	root.aryProAttachments = new Array();  // 当前产品已经有的产品图片
	root.aryProAttachments_new = new Array();  // 当前页面用户通过点击上传产品图片按钮，新上传的产品图片

	root.aryRepAttachments = new Array();

	/**
	 * 页面初始化
	 * @author ZhangHui 2015/4/30
	 */
	root.initialize = function() {
		/* 初始化产品图片上传控件 */
		$("#upload_pro").html("<input id='upload_prodPhoto_btn' type='file' />");
		root.buildUpload("upload_prodPhoto_btn", root.aryProAttachments_new, "fileEroMsg", "product");
		/* 初始化报告pdf上传控件 */
		$("#upload_rep").html("<input id='upload_report_files' type='file' />");
		root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
		/* 初始化控件 */
		root.initComponent();

		// 初始化下拉选择控件
		root.initAutoComplete_circulate();

		/* 可以搜索所有的条形码 */
		$("#barcodeId").kendoAutoComplete({
			dataSource: lims.getAutoLoadDsByUrl("/product/getAllBarCode"),
			filter: "startswith",
			placeholder: "搜索...",
			select: root.onSelectBarcode,
		});

		root.validateComponent();
		/* 如果是结构化页面编辑过来的数据，则隐藏相关的按钮 */
		root.shield_btn();
		/* 倒计时 */
		timer();
		/* 为图片合成pdf窗口的按钮绑定事件 */
		lims.showPicToPdfButton();

		if (root.isNew) {
			$("#update").hide();
			$("#add").hide();
			$("#clear").hide();
			$("#save").hide();
			$("#submit_and_publish").hide();
			addInputReadOnly(["bus_qsNo","bus_licenseNo","bus_name","bus_address"]);
		} else {
			
			$("#save").hide();
			$("#clear").hide();
			$("#submit_and_publish").hide();
			// 报告编辑状态下，以下字段不允许编辑：产品条形码、企业名称、qs号
			addInputReadOnly(["barcodeId","bus_name","bus_qsNo"]);
			$(".qs_format_div").hide();
			$("#proJianCheng").data("kendoDropDownList").readonly(true);
			$("#showQsFormat").hide();
			hideListBusNameSelect();

			$("#barcodeId").unbind("blur");
			$("#bus_qsNo").unbind("blur");
		}

		/**
		 * 当从结构化页面，跳转至本页面时，产品图片和报告无法删除
		 */
		if(root.fromPage == "manage_report_perfect_shianyun"){
			$("#btn_clearProFiles").hide();
			$("#proAttachmentsListView button").hide();
			$("table.productTable button").attr("disabled","true");

			$("#repAttachmentsListView button").hide();
			$("#upload_warn_mes").attr("style", "display:none");
		}
		$("#save-btn").click(function(){
			root.save();
		});
		$("#backMsg").draggable();

		/* 初始化页面数据 */
		root.initialReportData();
		root.gridDataSource = new kendo.data.DataSource({
		    data: root._testProperties,
		    batch: true,
		    page: 1,
		    //pageSize: 8,
		    serverPaging: true,
		    serverFiltering: true,
		    serverSorting: true
		});
//		/* 检测报告grid初始化 */
		portal.initTestPropertyGrid(root.gridDataSource);
	};

	/**
	 * 初始化下拉选择控件
	 * @author ZhangHui 2015/4/30
	 */
	root.initAutoComplete_circulate = function() {
		$("#foodinfo_minunit").kendoAutoComplete({
			dataSource: lims.getAutoLoadDsByUrl("/product/getAllUnitName"),
			filter: "contains",
			placeholder: "例如：60g、箱、500ml/瓶、2瓶/盒、克/袋",
			select: root.onSelectUnitName
		});
		$("#foodinfo_brand").kendoAutoComplete({
			dataSource: lims.getAutoLoadDsByUrl("/business/business-brand/getAllName"),
			filter: "contains",
			placeholder: "搜索...",
			select: root.onSelectBrandName
		});
	};

	/**
	 * 判断该条形码对应的产品,是否是系统中已存在，或者是否已引进
	 * @author ZhangHui 2015/4/30
	 */
	root.judgeProductByBarcode = function(barcode){

		if(barcode == ""){
			return;
		}

		var productId = root.queryProductIdByBarcode(barcode);

		/* 该条形码系统不存在 */
		if(productId == null){
			// 将产品信息输入框设置为可编辑
			setProductInfoToEdit();
			// 强制隐藏可选生产企业下拉框
			hideListBusNameSelect();
			// 倘若当前生产企业已经通过注册，并且已经对该产品绑定了QS证号，此处不允许再修改；需要将qs号置空，并可以编辑qs号
			if(root.current_bus_vo_has_claim != null){
				root.current_bus_vo_has_claim.qs_vo.qsNo = "";
				root.current_bus_vo_has_claim.qs_vo.licenceFormat = null;
				root.current_bus_vo_has_claim.can_edit_qs = true;
				setBusunitValue(root.current_bus_vo_has_claim);
			}
			lims.initNotificationMes('该条形码系统不存在！', false);
			return;
		}

		/* 该条形码系统存在 */
		root.initBarcode = barcode;
		root.autoloadingPageInfoByBarcode(barcode);
		//重新获取检测项目
//		portal.initTestPropertyGrid();
	};

	/**
	 * 初始化页面数据
	 * @author ZhangHui 2015/4/30
	 */
	root.initialReportData = function() {
		// 从报告新增申请处理页面跳转来时，根据条形码直接加载数据
		if(root.fromPage == "show_reportRenew"){
			root.updateApply = true; // 处理待更新报告时，将标记设置为true
			// 处理待更新报告请求时，报告处于新增状态，隐藏页面更新按钮。
			$("#update").hide();
			$("#add").hide();
			// 根据条形码加载产品信息
			root.autoloadingPageInfoByBarcode(root.initBarcode);
			$("#barcodeId").attr("disabled", true);
			$("#tri_testType").data("kendoDropDownList").value(root.testType);
			return;
		}
		if (root.edit_id) {
			root.isNew = false;
			/* 编辑状态下: 初始化页面信息 */
			$.ajax({
				url: portal.HTTP_PREFIX + "/testReport/" + root.edit_id,
				type: "GET",
				dataType: "json",
				async: false,
				success: function(returnValue){
					fsn.endTime = new Date();
					if (returnValue.result.status == "true") {
						var report = returnValue.data;
						var bus_vo = report.bus_vo;

						root.initBarcode = report.product_vo.barcode;
						root.initBusName = report.bus_vo.name;
						root.ReportNo_old = report.serviceOrder;

						/**
						 * 当前编辑的报告为未结构化报告时，需要加载同产品的最近一次的已结构化报告的检测项目
						 * @author ZhangHui 2015/5/8
						 */
						 var listOfItem = root.setTestPropertys(report);
						 if(listOfItem!=null && listOfItem.length>0){
							 root._testProperties = listOfItem;
							 report.testProperties = listOfItem;
						 }else{
							 root._testProperties = report.testProperties;
						 }

						 var qs_no = "";
						 if(bus_vo!=null && bus_vo.qs_vo!=null){
							 qs_no = bus_vo.qs_vo.qsNo;
							 bus_vo.qs_vo = null; // 将qs置空
						 }

						 fsn.setReportValue(report, root.product_html_type);

						 // qs号赋值
						 $("#bus_qsNo").val(qs_no);
					}
				},
				error: function(e){
					if (e.status == 911) {
						lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
					}
					else {
						lims.initNotificationMes('系统异常！', false);
					}
				}
			});
		}
		/* 报告新增状态下，从缓存 */
		if (root.isNew) {
			$.ajax({
				url: portal.HTTP_PREFIX + "/tempReport/getTempReport",
				type: "GET",
				dataType: "json",
				async: false,
				success: function(returnValue){
					fsn.endTime = new Date();
					if (returnValue.result.status == "true" && returnValue.data != null) {
						root._testProperties = returnValue.data.testProperties;
						fsn.setReportValue(returnValue.data, root.product_html_type);
					}
				},
				error: function(e){
					if (e.status == 911) {
						lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
					}
					else {
						lims.initNotificationMes('系统异常！', false);
					}
				}
			});
		}
	};

	/**
	 * 当前编辑的报告为未结构化报告时，需要加载同产品的最近一次的已结构化报告的检测项目
	 * @author ZhangHui 2015/5/8
	 */
	root.setTestPropertys = function(report){
		if(report.testProperties.length>0 && report.publishFlag!='6' && report.publishFlag!='2'){
			return null;
		}
		var listOfItem = null;
		if(report.testProperties.length==0){
			$.ajax({
				url: portal.HTTP_PREFIX + "/testReport/testItems/" + report.id + "/" + report.product_vo.id,
				type: "GET",
				dataType: "json",
				async: false,
				success: function(returnValue) {
					fsn.endTime = new Date();
					if (returnValue.result.status == "true") {
						listOfItem = returnValue.data;
					}
				},
				error: function(e) {
					if (e.status == 911) {
						lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
					} else {
						lims.initNotificationMes('系统异常！', false);
					}
				}
			});
		}else{
			listOfItem=report.testProperties;
		}
		
		return listOfItem;
	};

	/**
	 * 保存和提交时，封装对象
	 * @author Zhanghui 2015/6/5
	 */
	root.createInstance = function() {
		var category = null;
		if ($("#category1").data("kendoComboBox").select() != -1) {
			category = {
					name: $("#category3").data("kendoComboBox").select() == -1 ? $("#category3").val().trim() : $("#category3").data("kendoComboBox").dataItem().name,
							category: {
								id: $("#category2").data("kendoComboBox").select() == -1 ? null: $("#category2").data("kendoComboBox").value()
							},
							categoryOneCode: $("#category1").data("kendoComboBox").value()
			};
		}

		var product_vo = {
				name: $("#foodName").val().trim(),
				barcode: $("#barcodeId").val().trim(),
				brand_name: $("#foodinfo_brand").val().trim(),
				batchSerialNo: $("#tri_batchNo").val().trim(),
				productionDate: $("#tri_proDate").val().trim(),
				expirationDate:$("#inputDay").val().trim(),
				expireDate:$("#tri_proDate").val().trim(),
				status: $("#foodInfo_Status").val().trim(),
				format: $("#specification").val().trim(),
				unit: $("#foodinfo_minunit").val().trim(),
				proAttachments: mergeArray(root.aryProAttachments, root.aryProAttachments_new),
				category: category,
				expirationDate: $("#foodinfo_expirday").val().trim(),
				expiration: lims.getExpirDay(),
				regularity:lims.convertRegularityToItem($("#regularity").val().trim()),
				can_edit_pro: (root.current_product_vo_has_claim==null?true:false),
		};

		var qs_no = $("#bus_qsNo").val().trim();
		if(qs_no != ""){
			var qsFormatIt = $("#listqsFormat").data("kendoDropDownList");
			var qsFormat = qsFormatIt.dataItem();
			var jiancheng = "";
			if(root.showJianCheng){
				jiancheng = "("+$("#proJianCheng").data("kendoDropDownList").text()+")";
			}

			var licenceFormat = {
					id: qsFormat.id,
			};

			var qs_info = {
					qsNo: jiancheng + $("#showQsFormat").html() + qs_no,
					licenceFormat: licenceFormat,
			};

			product_vo.qs_info = qs_info;
		}

		var can_edit_qs = false; // 编辑时，qs默认不能编辑
		if(root.isNew){
			can_edit_qs = root.current_bus_vo_has_claim==null?true:root.current_bus_vo_has_claim.can_edit_qs;
		}

		var bus_vo = {
				name: $("#bus_name").val().trim(),
				licenseno: $("#bus_licenseNo").val().trim(),
				address: $("#bus_address").val().trim(),
				can_edit_qs: can_edit_qs,
				can_edit_bus: (root.current_bus_vo_has_claim==null?true:false)
		};

		// 去除检测项目中检测名称为空的项目
		root.removePropertiesOfEmpty("report_grid");
		var testProperties =  $("#report_grid").data("kendoGrid").dataSource.data();
		console.log(testProperties);
		var report_vo = {
				id: root.isNew?null:root.edit_id,
						serviceOrder: $("#tri_reportNo").val().trim(),
						testee: $("#tri_testee").val().trim(),
						sampleQuantity: $("#sampleCounts").val().trim(),
						samplingLocation: $("#sampleAds").val().trim(),
						testPlace: $("#tri_testPlace").val().trim(),
						testDate: $("#tri_testDate").val().trim(),
						testType: $("#tri_testType").data("kendoDropDownList").value(),
						standard: $("#judgeStandard").val().trim(),
						result: $("#testResultDescribe").val().trim(),
						pass: $("#tri_conclusion").data("kendoDropDownList").value() == "1" ? true: false,
								comment: $("#remarks").val().trim(),
								autoReportFlag: root.isAutoReport,
								testOrgnization: $("#tri_testOrg").val().trim(),
								testProperties:testProperties,
								repAttachments: root.aryRepAttachments,
								publishFlag: '3',
								product_vo: product_vo,
								bus_vo: bus_vo,
								new_flag: root.isNew,
		};

		return report_vo;
	};

	/**
	 * 掉线提醒确定事件
	 * @author ZhangHui 2015/4/16
	 */
	root.remind = function(){
		$("#diaoxian_remind_01").data("kendoWindow").close();
		$("#diaoxian_remind_02").data("kendoWindow").close();

		fsn.save();
	};

	/**
	 * 提交报告
	 */
	root.submit = function() {
		var pass = root.submit_check();
		if(!pass){
			return;
		}
		root.submit_report();
	};

	root.submit_report = function(){
		
		$("#save_status").html("正在提交，请稍候...");
		$("#saving_msg").html("正在提交，请稍候...");
		$("#toSaveWindow").data("kendoWindow").open().center();

		var report_vo = root.createInstance();
		$.ajax({
			url: portal.HTTP_PREFIX + "/testReport/" + root.updateApply,
			type:  "POST",
			dataType: "json",
			//async:false,
			timeout: 600000,
			//10min
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(report_vo),
			success: function(returnValue) {
				fsn.endTime = new Date();
				$("#save_status").html("");
				$("#toSaveWindow").data("kendoWindow").close();

				if (returnValue.result.status == "true") {
					var report_vo = returnValue.data;

					lims.initNotificationMes('编号为' + report_vo.serviceOrder + '的检测报告 ' + (root.isNew ? lims.l("Add") : lims.l("Update")) + '成功！', true);

					// 清空缓存信息
					root.clearTempReport();

					// 将检测项目发回至KMS
					fsn.sendItemsToKMS();

					root.isNew = false;
					root.ReportNo_old = report_vo.serviceOrder;
					root.edit_id = report_vo.id;
					$("ul.k-upload-files").remove();

//					$("#report_grid").data("kendoGrid").dataSource.data(report_vo.testProperties == null ? [] : report_vo.testProperties);

					/**
					 * 当从结构化页面，跳转至本页面时，提交陈功后，页面按钮显示无需改变
					 * @author ZhangHui 2015/5/7
					 */
					if(root.fromPage == "manage_report_perfect_shianyun"){
						return;
					}

					$("#save").hide();
					$("#clear").hide();
					$("#submit").show();
					$("#update").hide();
					$("#add").hide();

					// 产品图片和报告pdf页面赋值
					setProAttachments(report_vo.product_vo.proAttachments);
					if (!report_vo.autoReportFlag) {
						setRepAttachments(report_vo.repAttachments);
					}

					//二级分类
					$.ajax({
						url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + $("#category2").data("kendoComboBox").value() + "?categoryFlag=" + true,
						type: "GET",
						dataType: "json",
						async: false,
						success: function(value) {
							fsn.endTime = new Date();
							if (value.result.status == "true") {
								$("#category3").data("kendoComboBox").setDataSource("");
								$("#category3").data("kendoComboBox").setDataSource(value.data);
								if (report_vo.product_vo.category.id) {
									$("#category3").data("kendoComboBox").value(report_vo.product_vo.category.id);
								}
							}
						},
						error: function(e) {
							if (e.status == 911) {
								lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
							} else {
								lims.initNotificationMes('系统异常！', false);
							}
						}
					});
				} else {
					lims.initNotificationMes((root.isNew ? lims.l("Add") : lims.l("Update")) + '失败！原因为：' + returnValue.result.errorMessage, false);
				}

				// 报告编辑状态下，以下字段不允许编辑：产品条形码、企业名称、qs号
				addInputReadOnly(["barcodeId","bus_name","bus_qsNo"]);
				$(".qs_format_div").hide();
				$("#proJianCheng").data("kendoDropDownList").readonly(true);
				hideListBusNameSelect();
				$("#barcodeId").unbind("blur");
			},
			error: function(e) {
				$("#saving_msg").html("");
				$("#toSaveWindow").data("kendoWindow").close();
				if (e.status == 911) {
					lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
				} else {
					lims.initNotificationMes('系统异常！', false);
				}
			}
		});
	};

	/**
	 * 提交报告前，验证数据合法性
	 */
	root.submit_check = function() {
		root.clearErrorInfoStyle();
		if (!root.validateData()) { // 必填项验证
			return false;
		}

		var firstPart = $("#showQsFormat").html().indexOf("?");
		if (firstPart!=-1) {
			lims.initNotificationMes("您选择的输入规则没有加载出正确的省份简称，请在企业基本信息中完善企业地址保存后再试！", false);
			return false;
		}

		/**
		 * 条形码格式校验
		 */
//		var barcode = $("#barcodeId").val();
//		var re_barcode = /^[0-9]*$/;
//		if (!barcode.match(re_barcode)) {
//			$("#barcodeId").attr("style", "border:2px solid red;");
//			lims.initNotificationMes("产品条形码只能由数字组成！", false);
//			return false;
//		}

		/**
		 * 保质期、保质天数校验
		 */
		var categoryCode = $("#category2").data("kendoComboBox").dataItem().code;
		if (root.busunit_name != "永辉超市" && categoryCode.indexOf("15") != 0) {
			var expirday = $("#foodinfo_expirday").val();
			if (expirday.length == 0 || expirday.trim() == "") {
				lims.initNotificationMes("保质天数不能为空,请输入保质期来自动计算保质天数", false);
				return false;
			} else {
				var re1 = /^[0-9]*$/;
				if (!expirday.match(re1)) {
					$("#foodinfo_expirday").attr("style", "border:2px solid red;");
					lims.initNotificationMes("保质天数只能为数字！", false);
					return false;
				}
				if (expirday > 10000000) {
					$("#foodinfo_expirday").attr("style", "border:2px solid red;");
					lims.initNotificationMes("保质天数应小于等于999999999！", false);
					return false;
				}
			}
		}

		/**
		 * qs号为非必填
		 * 注：只有在报告新增时，需要验证qs格式；编辑时，由于qs号无法更改，所以无需校验
		 */
		if(root.isNew){
			var pass = root.validateQsNoFormat();
			if(!pass){
				return false;
			}
		}

		/**
		 * 营业执照号为非必填
		 */
		var pass = root.validateLicenNoFormat();
		if(!pass){
			return false;
		}

		// 生产日期校验
		var proDatePicker = $("#tri_proDate").data("kendoDatePicker");
		if(proDatePicker){
			var proDate = $("#tri_proDate").val().trim();
			$("#tri_proDate_format").data("kendoDatePicker").value(proDate);
			if(proDate!=""){
				if ($("#tri_proDate_format").val().trim()=="") {
					lims.initNotificationMes("生产日期格式不正确，请重新填写！", false);
					return false;
				}
			}else{
				lims.initNotificationMes("生产日期不能为空！", false);
				return false;
			}

			// 检验日期校验
			var testDate = $("#tri_testDate").val().trim();
			$("#tri_testDate_format").data("kendoDatePicker").value(testDate);
			if(testDate!=""){
				if ($("#tri_testDate_format").val().trim()=="") {
					lims.initNotificationMes("检验日期格式不正确，请重新填写！", false);
					return false;
				}
			}else{
				lims.initNotificationMes("检验日期不能为空！", false);
				return false;
			}

			// 判断生产日期是否小于等于检验日期
			var testDatePicker = $("#tri_testDate").data("kendoDatePicker");
			proDatePicker.value($("#tri_proDate").val().trim());
			testDatePicker.value($("#tri_testDate").val().trim());
//			var proDate = proDatePicker.value();
//			var testDate = testDatePicker.value();
//			if(proDate > testDate){
//				lims.initNotificationMes("检验日期必须大于等于生产日期！", false);
//				return false;
//			}
		}

		var inputDay=$("#inputDay").val().trim();//保质日期
	    var productionDateStr = $("#tri_proDate").val().trim(); // 生产日期字符串
		/**
		 * 报告编号唯一性校验
		 */
		if (root.isNew) {
			if (!root.validateReportNO()) {
				$("#tri_reportNo").attr("style", "border:2px solid red;");
				lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" + $("#barcodeId").val().trim() + "】、批次号【" + $("#tri_batchNo").val().trim() + "】已经存在对应的报告，3个字段不能重复！", false);
				return false;
			}
		} else {
			if (root.ReportNo_old != $("#tri_reportNo").val().trim()) {
				if (!root.validateReportNO()) {
					$("#tri_reportNo").attr("style", "border:2px solid red;");
					lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" + $("#barcodeId").val().trim() + "】、批次号【" + $("#tri_batchNo").val().trim() + "】已经存在对应的报告，3个字段不能重复！", false);
					return false;
				}
			}
		}

		if (!root.isAutoReport && root.aryRepAttachments.length == 0) {
			lims.initNotificationMes(lims.l("not found pdf"), false);
			return false;
		} else if (root.isAutoReport && root.aryRepAttachments.length > 0) {
			lims.initNotificationMes(lims.l("auto create pdf"), false);
			return false;
		}
		if (root.isAutoReport && $("#tri_testType").data("kendoDropDownList").value() != "企业自检") {
			lims.initNotificationMes("只有在【检验类别】为企业自检的情况下，才能选择自动生成pdf，否则请手动上传pdf!", false);
			return false;
		}
		$("#success_msg").html("");
		$("#success_msg").attr("style", "");

		// 判断产品图片是否为空
		var aryProAttas = mergeArray(root.aryProAttachments, root.aryProAttachments_new);
		if (aryProAttas.length == 0) {
			$("#saveWindow").data("kendoWindow").open().center();
			return false;
		}else{
			root.aryProAttachments = aryProAttas;
			root.aryProAttachments_new.length = 0;
		}

		/* 对"检查项目"grid 提交时 的数据验证 */
		var gridItem = $("#report_grid").data("kendoGrid").dataSource.data();
		if (gridItem.length < 1 && root.isAutoReport) {
			lims.initNotificationMes("至少需要一条检测项目！", false);
			return false;
		} else {
			var count = 0;
			var message = "";
			for (var i = 0; i < gridItem.length; i++) {
				if (gridItem[i].name != "" || gridItem[i].unit != "" || gridItem[i].techIndicator != "" || gridItem[i].result != "" || gridItem[i].standard != "") {
					if (gridItem[i].name == "") {
						message = "【检查项目名称】";
					}
					if (gridItem[i].result == "") {
						message += "【检测结果】";
					}
					if (message != "") {
						lims.initNotificationMes('检测项目列表的第' + (i + 1) + '行' + message + '为必填项！', false);
						return false;
					}
					count++;
				}
			}
			if (root.isAutoReport && count == 0) {
				lims.initNotificationMes('至少需要一条检测项目！', false);
				return false;
			}
		}

		return true;
	};

	root.addNext = function() {
		window.location.pathname = "/fsn-core/views/market/add_testreport.html";
	};

	root.onSelectQsNo = function(e) {
		var barcode = $("#barcodeId").val().trim();
		if(barcode == ""){
			lims.initNotificationMes('请先选择产品后，系统才会根据qs号为您自动加载生产企业信息。', false);
			return;
		}

		var dataItem = $("#showQsFormat").html() + this.dataItem(e.item.index());
		$.ajax({
			url: portal.HTTP_PREFIX + "/business/getBusUnitOfReportVOByQsno/" + barcode + "/" + dataItem,
			type: "GET",
			dataType: "json",
			async: false,
			contentType: "application/json; charset=utf-8",
			success: function(returnValue) {
				fsn.endTime = new Date();
				if (returnValue.result.status == "true") {
					if (returnValue.data != null) {
						root.setBusunitValue(returnValue.data);
					}
				}
			},
			error: function(e) {
				if (e.status == 911) {
					lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
				} else {
					lims.initNotificationMes('系统异常！', false);
				}
			}
		});
	};

	//从页面删除
	root.removeRes = function(resID, fileName) { 
		root.isPro = false;
		var dataSource = new kendo.data.DataSource();
		for (var i = 0; i < root.aryProAttachments.length; i++) {
			if ((resID != null && root.aryProAttachments[i].id == resID) || root.aryProAttachments[i].fileName == fileName) {
				root.isPro = true;
				while ((i + 1) < root.aryProAttachments.length) {
					root.aryProAttachments[i] = root.aryProAttachments[i + 1];
					i++;
				}
				root.aryProAttachments.pop();
				break;
			}
		}
		if (!root.isPro) {
			for (var i = 0; i < root.aryRepAttachments.length; i++) {
				if ((resID != null && root.aryRepAttachments[i].id == resID) || root.aryRepAttachments[i].fileName == fileName) {
					isPro = false;
					while ((i + 1) < root.aryRepAttachments.length) {
						root.aryRepAttachments[i] = root.aryRepAttachments[i + 1];
						i++;
					}
					root.aryRepAttachments.pop();
					break;
				}
			}
			if (root.aryRepAttachments.length > 0) {
				for (i = 0; i < root.aryRepAttachments.length; i++) {
					dataSource.add({
						attachments: root.aryRepAttachments[i]
					});
				}
			}
			$("#repAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			if (root.aryRepAttachments.length == 0) {
				$("#btn_clearRepFiles").hide();
				fsn.pdfRes = null;
				lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
			}
		} else {
			if (root.aryProAttachments.length > 0) {
				for (i = 0; i < root.aryProAttachments.length; i++) {
					dataSource.add({
						attachments: root.aryProAttachments[i]
					});
				}
			}
			$("#proAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			if (root.aryProAttachments.length == 0) {
				$("#btn_clearProFiles").hide();
			}
		}
	};

	/**
	 * 提交前，数据必填项校验
	 */
	root.validateData = function() {
		root.validator = $("#content_container").kendoValidator().data("kendoValidator");
		if (!root.validator.validate()) {
			var validate_barcode = $("#barcodeId").kendoValidator().data("kendoValidator").validate();
			var validate_foodName = $("#foodName").kendoValidator().data("kendoValidator").validate();
			if (!validate_barcode || !validate_foodName) {
				lims.initNotificationMes(lims.l("Product Information Bar has required Unfilled"), false);
				return false;
			}
			//验证单位为必填项
			var proUnit = $("#foodinfo_minunit").val().trim();
			if (proUnit.length < 1) {
				$("#foodinfo_minunit").focus();
				lims.initNotificationMes("请填写产品的单位！", false);
				return false;
			}

			// 生产企业名称为必填项
			var validate_bus_name = $("#bus_name").kendoValidator().data("kendoValidator").validate();
			if(!validate_bus_name){
				lims.initNotificationMes("生产企业名称不能为空", false);
				return false;
			}

			var validate_judgeStandard = $("#judgeStandard").val().trim();
			var validate_batchNo = $("#tri_batchNo").kendoValidator().data("kendoValidator").validate();
			var validate_reportNo = $("#tri_reportNo").kendoValidator().data("kendoValidator").validate();
			var validate_proDate = $("#tri_proDate").kendoValidator().data("kendoValidator").validate();
			var validate_testOrg = $("#tri_testee").kendoValidator().data("kendoValidator").validate();
			$("#judgeStandard").kendoValidator().data("kendoValidator").validate();
			$("#testResultDescribe").kendoValidator().data("kendoValidator").validate();

			if (!validate_batchNo || !validate_reportNo || !validate_proDate || !validate_testOrg) {
				lims.initNotificationMes(lims.l("Test report required information fields have not filled"), false);
				return false;
			}
			if (validate_judgeStandard == "" || validate_judgeStandard == null){
				lims.initNotificationMes(lims.l("Test report required information fields have not filled"), false);
				return false;
			}
		}

		var indexCategory1 = $("#category1").data("kendoComboBox").select();
		if (indexCategory1 == -1) {
			$("#category1").focus();
			lims.initNotificationMes("请选择产品所属的食品种类一级分类！", false);
			return false;
		}
		var indexCategory2 = $("#category2").data("kendoComboBox").select();
		if (indexCategory2 == -1) {
			$("#category2").focus();
			lims.initNotificationMes("请选择产品所属的食品种类二级分类！", false);
			return false;
		}
		var category3 = $("#category3").data("kendoComboBox").text().trim();
		if (category3 == "" || category3 == "请选择...") {
			$("#category3").focus();
			lims.initNotificationMes("请选择产品所属的食品种类三级分类！", false);
			return false;
		}
		var regularity = $("#regularity").val();
		if (regularity == "") {
			$("#regularity").focus();
			lims.initNotificationMes("请填写执行标准！", false);
			return false;
		}
		if ($("#testResultDescribe").val().length < 1) {
			lims.initNotificationMes("检验结论描述不能为空", false);
			return false;
		}

		//验证报告编号不能包含“？”、“?”、“'”字符,如果包含空格通过验证
		var repNo = $("#tri_reportNo").val().trim();
		var repNoFormat = true;
		var repNoMes = "报告编号不能包含";
		if (repNo.indexOf("？") > -1 || repNo.indexOf("?") > -1) {
			repNoMes += "?";
			repNoFormat = false;
		}
		if (repNo.indexOf("'") > -1 || repNo.indexOf("’") > -1) {
			repNoMes += "'";
			repNoFormat = false;
		}
		if (repNo.indexOf("&") > -1) {
			repNoMes += "&";
			repNoFormat = false;
		}
		if (repNo.indexOf("<") > -1) {
			repNoMes += "<";
			repNoFormat = false;
		}
		if (repNo.indexOf(">") > -1) {
			repNoMes += ">";
			repNoFormat = false;
		}
		if (!repNoFormat) {
			$("#tri_reportNo").attr("style", "border:2px solid red;");
			lims.initNotificationMes(repNoMes, false);
			return false;
		}
		var testDate = $("#tri_testDate").val().trim();
		if (testDate.length < 1) {
			lims.initNotificationMes("报告检测日期不能空！", false);
			return false;
		}

		return true;
	};

	/**
	 * 清除输入框的错误提示样式
	 */
	root.clearErrorInfoStyle = function() {
		$("#barcodeId").removeAttr("style");
		$("#foodinfo_expirday").removeAttr("style");
		$("#foodinfo_category").prev().children("input").attr("style", "width:100%;");
		$("#bus_qsNo").attr("style", "width:100%;");
		$("#bus_licenseNo").attr("style", "width:300px;");
		$("#tri_reportNo").removeAttr("style");
		$("#tri_proDate").attr("style", "width:100%;height:100%;");
		$("#tri_testDate").attr("style", "width:100%;height:100%");
	};


	root.validateExpiration = function() {
		// 1. 验证年份  
		var proExpirYear = $("#proExpirYear").val();
		if (!proExpirYear.match(/^[0-9]*$/)) {
			lims.initNotificationMes("【保质期】的年份应该是数字！", false);
			return;
		} else {
			lims.countExpirday();
			// lims.initNotificationMes("输入年份正确",true);
		}
		// 2. 验证月份  
		var proExpirMonth = $("#proExpirMonth").val();
		if (!proExpirMonth.match(/^[0-9]*$/)) {
			lims.initNotificationMes("【保质期】的月份应该是数字！", false);
			return;
		} else {
			lims.countExpirday();
			// lims.initNotificationMes("输入月份正确",true);
		}
		//  3. 验证天数 
		var proExpirDay = $("#proExpirDay").val();
		if (!proExpirDay.match(/^[0-9]*$/)) {
			lims.initNotificationMes("【保质期】的天数应该是数字！", false);
			return;
		} else {
			lims.countExpirday();
			//lims.initNotificationMes("输入日正确",true);
			//lims.msg("success_msg", true, "");
		}
	};

	/**
	 * 判断是否是在结构化页面点击编辑过来的报告信，如果是，则将对应的按钮隐藏
	 * @author HuangYong <br>
	 * 最后更新：ZhangHui 2015/5/6<br>
	 * 更新内容：使用root.fromPage进行判断，增加方法的可扩展性
	 */
	root.shield_btn = function(){
		if(root.fromPage != "manage_report_perfect_shianyun"){
			return;
		}
		$("#upload_pro").hide();
		$("#proAttachmentsListView button").hide();
		$("#repAttachmentsListView button").hide();
		$("#autoUp").hide();
		$("#btn_clearProFiles").hide();

		/**
		 * 发布按钮初始化
		 * @author ZhangHui 2015/5/7
		 */
		$("#add").hide();
		$("#update").hide();
		/* 提交并发布按钮 */
		$("#submit_and_publish").bind("click", root.submit);
		$("#submit_and_publish").show();
		$("#save").hide();
	};

	root.my_clearTempReport = function(){
		root.clearTempReport();

		if(root.isNew){
			// 清空页面数据
			root.aryRepAttachments.length = 0;
			fsn.setReportValue({}, root.product_html_type);

			/* 初始化报告pdf上传控件 */
			$("#upload_rep").html("<input id='upload_report_files' type='file' />");
			root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
			removeDisabledToBtn(["openT2P_btn"]);

			$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
		}
	};

	try {
		root.edit_id = sessionStorage.getItem("user_0_edit_testreport");
//		root.edit_id = $.cookie("user_0_edit_testreport").id;
		root.initBarcode = $.cookie("user_0_edit_testreport").barcode;
		root.fromPage = $.cookie("user_0_edit_testreport").from;
		root.testType = $.cookie("user_0_edit_testreport").testType;
		$.cookie("user_0_edit_testreport", JSON.stringify({}), {
			path: '/'
		});
	} catch(e) {}
	root.save=function(){
		 var inputDay=$("#foodinfo_expirday").val().trim();//保质日期
		    var productionDateStr = $("#tri_proDate").val().trim(); // 生产日期字符串
		$("#save_status").html("正在保存，请稍候...");
		$("#saving_msg").html("正在保存，请稍候...");
		$("#toSaveWindow").data("kendoWindow").open().center();

		var report_vo = root.createInstance();
		$.ajax({
			url: portal.HTTP_PREFIX + "/testReport/" + root.updateApply+"?save=false",
			type:  "POST",
			dataType: "json",
			//async:false,
			timeout: 600000,
			//10min
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(report_vo),
			success: function(returnValue) {
				fsn.endTime = new Date();
				$("#save_status").html("");
				$("#toSaveWindow").data("kendoWindow").close();

				if (returnValue.result.status == "true") {
					var report_vo = returnValue.data;

					lims.initNotificationMes('保存成功', true);

					// 清空缓存信息
					root.clearTempReport();

					// 将检测项目发回至KMS
					fsn.sendItemsToKMS();

					root.isNew = false;
					root.ReportNo_old = report_vo.serviceOrder;
					root.edit_id = report_vo.id;
					$("ul.k-upload-files").remove();

//					$("#report_grid").data("kendoGrid").dataSource.data(report_vo.testProperties == null ? [] : report_vo.testProperties);

					/**
					 * 当从结构化页面，跳转至本页面时，提交陈功后，页面按钮显示无需改变
					 * @author ZhangHui 2015/5/7
					 */
					if(root.fromPage == "manage_report_perfect_shianyun"){
						return;
					}

					$("#save").hide();
					$("#clear").hide();
					$("#submit").show();
					$("#update").hide();
					$("#add").hide();

					// 产品图片和报告pdf页面赋值
					setProAttachments(report_vo.product_vo.proAttachments);
					if (!report_vo.autoReportFlag) {
						setRepAttachments(report_vo.repAttachments);
					}

					//二级分类
					$.ajax({
						url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + $("#category2").data("kendoComboBox").value() + "?categoryFlag=" + true,
						type: "GET",
						dataType: "json",
						async: false,
						success: function(value) {
							fsn.endTime = new Date();
							if (value.result.status == "true") {
								$("#category3").data("kendoComboBox").setDataSource("");
								$("#category3").data("kendoComboBox").setDataSource(value.data);
								if (report_vo.product_vo.category.id) {
									$("#category3").data("kendoComboBox").value(report_vo.product_vo.category.id);
								}
							}
						},
						error: function(e) {
							if (e.status == 911) {
								lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
							} else {
								lims.initNotificationMes('系统异常！', false);
							}
						}
					});
				} else {
					lims.initNotificationMes((root.isNew ? lims.l("Add") : lims.l("Update")) + '失败！原因为：' + returnValue.result.errorMessage, false);
				}

				// 报告编辑状态下，以下字段不允许编辑：产品条形码、企业名称、qs号
				addInputReadOnly(["barcodeId","bus_name","bus_qsNo"]);
				$(".qs_format_div").hide();
				$("#proJianCheng").data("kendoDropDownList").readonly(true);
				hideListBusNameSelect();
				$("#barcodeId").unbind("blur");
			},
			error: function(e) {
				$("#saving_msg").html("");
				$("#toSaveWindow").data("kendoWindow").close();
				if (e.status == 911) {
					lims.initNotificationMes('因长时间未操作，为了保障您的账号安全，系统已掉线，请重新登录！', false);
				} else {
					lims.initNotificationMes('系统异常！', false);
				}
			}
		});
	};
//	/**
//	 * 结构化数据模块，添加检测项目grid列表数据正行上移
//	 * @param obj
//	 */
//	root.up = function(obj) {
//		var objParentTR = $(obj).parent().parent();
//		var prevTR = objParentTR.prev();
//		if (prevTR.length > 0) {
//			prevTR.insertAfter(objParentTR);
//		}
//	};
//	/**
//	 *  结构化数据模块，添加检测项目grid列表数据正行下移
//	 * @param obj
//	 */
//	root.down = function(obj) {
//		var objParentTR = $(obj).parent().parent();
//		var nextTR = objParentTR.next();
//		if (nextTR.length > 0) {
//			nextTR.insertBefore(objParentTR);
//		}
//		 var dataSources =  $("#report_grid").data("kendoGrid").dataSource.data();
//		 console.log(dataSources)
//	};
	root.upSelect = function(e,tdDate){
		  var data =  $("#report_grid").data("kendoGrid").dataSource.data();
	      var  trIndex = $(e.currentTarget).closest("tr").index();
          if(trIndex == 0){
        	  lims.initNotificationMes('已经是第一行，不能再往上移动了！', false);
        	  return;
          }
	      for(var i = 0 ; i<data.length;i++){
        	  if(i == trIndex){
        		  data[trIndex] = data[trIndex-1];
        		  data[trIndex-1] = tdDate; 
        		break;
        	  }
         }
         var itemGrid = $("#report_grid").data("kendoGrid");
     	 if(itemGrid){
     		 itemGrid.dataSource.data(data?data:[]);
     	 }
	};
	root.downSelect = function(e,tdDate){
		var data =  $("#report_grid").data("kendoGrid").dataSource.data();
	      var  trIndex = $(e.currentTarget).closest("tr").index();
	      if(trIndex == data.length-1){
        	  lims.initNotificationMes('已经是最后一行，不能再往下移动了！', false);
        	  return;
          }
          for(var i = 0 ; i<data.length;i++){
        	  if(i == trIndex){
        		  data[trIndex] = data[i+1];
        		  data[i+1] = tdDate; 
        		break;
        	  }
         }
         var itemGrid = $("#report_grid").data("kendoGrid");
     	 if(itemGrid){
     		 itemGrid.dataSource.data(data?data:[]);
     	 }
	};
	root.initialize();
});