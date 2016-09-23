$(document).ready(function(){
    var lims = window.lims = window.lims || {};
    var root = window.lims.root = window.lims.root || {};
    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    portal.codeFlag = true;
    root.isNew = true;
    portal.type = false;
    
    root.aryProAttachments = new Array();
    root.aryRepAttachments = new Array();
    root.sanRepAttachments = new Array();
    root.sanPdfRepAttachments = new Array();
    
    /*下面是用来判断是不是改变过这3个字段--产品条形码，报告编号和批次号*/
    root.isImportedProduct = false;  //判断是否是进口食品报告
    
    
    /**
     * @param product_html_type
     * 				product_input 代表 产品信息使用input输入框赋值
     * 				product_text  代表 产品信息使用text赋值
     * @author ZhangHui 2015/6/7
     */
    root.product_html_type = "product_text";
    
    
    /**
     * 是否使用预览小图片方式展示产品图片标记
     * true: 使用预览小图片
     * false: 使用文件名
     * @author LongXianzhen 2015/6/12
     */
    root.isViewImage = true;
    
    try {
        root.edit_id = $.cookie("user_0_edit_testreport").id;
        $.cookie("user_0_edit_testreport", JSON.stringify({}), {
            path: '/'
        });
    } catch (e) {
    }
    
   
    
    
    /**
     * 初始化
     */
    root.initialize = function(){
    	 /**
         * 获取超市处理问题的条形码
         */
    	 try {
    		var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    	    	var dealType = arrayParam[0];
    	    	if(dealType == $.md5("dealProblem")){
    	    		var barcode = arrayParam[1]; // 产品条形码(被编码过的产品条形码)
    	    		var barcodeMD5 = arrayParam[2]; // 产品条形码(被编码过的产品条形码)
    	    		if(barcodeMD5 == $.md5(barcode)){
    	    			portal.edit_barcode = barcode;
    	    		}
    	    	}

    	} catch (e) {
    	}
    	/* 初始化报告上传控件 */
        $("#upload_rep").html("<input id='upload_report_files' type='file' />");
        root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
        
        /* 初始化引进产品弹出框的销往客户下拉控件 */
		portal.initCustomerMultiSelect_lead();
		
		root.initComponent();
		
		fsn.initKendoWindow("lead_product_warn_window","友情提示","480px","420px",false,null);
		
		fsn.initKendoWindow("create_product_warn_window","友情提示","300px","200px",false,null);
		
		root.removeDisabledToBtn(["openS2P_btn"]);

		// 经销商报告录入界面，Click事件绑定
		root.bindClick_dealer();
		$("#backMsg").draggable();
		//获取超市处理问题的条形码
		if(portal.type && portal.edit_barcode != undefined){
			 $("#barcodeId").val(portal.edit_barcode).attr('disabled',true);
			root.judgeProductByBarcode(portal.edit_barcode);
		}else{
			/* 可以搜索所有的条形码 */
			$("#barcodeId").kendoAutoComplete({
				dataSource: lims.getAutoLoadDsByUrl("/product/getAllBarCode"),
				filter: "startswith",
				placeholder: "搜索...",
				select: root.onSelectBarcode
			});
		}
       // portal.edit_barcode
        
        
        root.validateComponent();
        
        /* 倒计时 */
        timer();
        /* 为图片合成pdf窗口的按钮绑定事件 */
        lims.showPicToPdfButton();
        
        // 页面数据初始化
        root.initialReportData();
        
        if (root.isNew) {
            $("#update").hide();
            $("#add").hide();
        } else {
            $("#save").hide();
            $("#clear").hide();
            $("#submit").hide();
            
            // 报告编辑状态下，以下字段不允许编辑：产品条形码、企业名称、qs号
//            addInputReadOnly(["barcodeId","bus_qsNo"]);
            addInputReadOnly(["barcodeId"]);
            $(".qs_format_div").hide();
	        $("#proJianCheng").data("kendoDropDownList").readonly(true);
	        $("#showQsFormat").hide();
	        
	        $("#barcodeId").unbind("blur");
	        $("#bus_qsNo").unbind("blur");
        }
        hideListBusNameSelect(); // 隐藏可选生产企业
        portal.fillCustomerSelect();
    };
    
    /**
     * 经销商报告录入界面，Click事件绑定
     * @author ZhangHui 2015/4/29
     */
    root.bindClick_dealer = function(){
    	/**
         * 定义产品条形码失去焦点事件
         * @author ZhangHui 2015/4/30
         */
    	//=====================开始=======================
    	$("#barcodeId").blur(function() {
    		/* 将选择销往企业控件隐藏 */
        	var barcode = $("#barcodeId").val().trim();
        	/*条形码为空不进行判断*/
        	if(barcode==""){
        		return;
        	}
        	if(portal.codeFlag){ 
    		 root.judgeProductByBarcode(barcode);
    		}
        });
    	//=====================结束=======================
    	/**
    	 * 产品条形码不存在时，弹出引进产品提示框的确定按钮的Click事件
    	 * @author ZhangHui 2015/4/29
    	 */
    	$("#lead_product_yes_btn").click(function(){
    			/* 引进该条码的产品 */
	    	    var barcode = $("#barcodeId").val().trim();
	    	    /**
	    	     * 超市添加报告，设置默认的销往企业
	    	     */
	    	    
				var customerItems = $("#customerSelect_lead").data("kendoMultiSelect").dataItems();
				if (customerItems.length<1) {
	                lims.initNotificationMes("请选择销往客户", false);
					return ;
	            }
				var isSuccess = portal.leadProduct(barcode);
				if(isSuccess){
					var customerNames = "";
					var customerIds = "";
					for ( var entry in customerItems) {
						customerNames += (customerItems[entry].name + ",");
						customerIds += (customerItems[entry].id + ",");
					}
					lims.initNotificationMes("条形码为：" + barcode + "的产品引进成功", true);
					/* 保存 产品-当前登录企业-销往企业 的关系 */
					var productVO = {
							barcode: barcode,
							selectedCustomerIds: customerIds,
					};
					portal.saveSelectCustomer(productVO);
					
					root.current_barcode_can_use = true; // 记录当前条形码可以使用
					root.initPageData(barcode);
				}else{
					lims.initNotificationMes("条形码为：" + barcode + "的产品引进失败", false);
				}
				$("#lead_product_warn_window").data("kendoWindow").close();
    	});
    	
    	/**
    	 * 产品条形码不存在时，弹出提示框的取消按钮
    	 * @author ZhangHui 2015/4/29
    	 */
    	$("#lead_product_no_btn").click(function(){
    		$("#lead_product_warn_window").data("kendoWindow").close();
    	});
        
    	/**
    	 * 产品条形码存在时且被被引进，则弹出引进产品提示框的确定按钮的Click事件
    	 * @author ZhangHui 2015/4/29
    	 */
    	$("#create_product_yes_btn").click(function(){
    		/* 如果系统不存在该条码，将条码带到新增产品页面 */
    		$("#lead_product_warn_window").data("kendoWindow").close();
			var product = {
					barcode: $("#barcodeId").val().trim()
			};
			$.cookie("user_0_edit_product", JSON.stringify(product), {
				path : '/'
			});
			window.open("/fsn-core/views/portal_new/product.html");
    	});
    	
    	/**
    	 * 产品条形码存在时且被被引进，则弹出引进产品提示框的取消按钮的Click事件
    	 * @author ZhangHui 2015/4/29
    	 */
    	$("#create_product_no_btn").click(function(){
    		$("#create_product_warn_window").data("kendoWindow").close();
    	});
    };
    
    /**
     * 验证是否是进口产品，是则展示进口产品报告信息
     * @author longxianzhen 2015/05/22
     */
    root.validateIsImportedProduct=function(curBarcode){
    	if(curBarcode==null){
    		/* 当前页面的barcode */
        	curBarcode = $("#barcodeId").val().trim();
    	}
    	/* 截取前三位 */
    	var bar3=curBarcode.substr(0,3);
    	if(curBarcode.length==14){
    		bar3 = curBarcode.substr(1,3);
    	}
    	bar3 = bar3.replace("N",3);
    	bar3=parseInt(bar3);
    	if(!isNaN(bar3)){
    		if((bar3>=690&&bar3<=699)||root.productCountry=="中国"){
    			root.isImportedProduct = false;
    			$("#sanitaryTr").hide();
    			$("#sanAtta").hide();
    			$("#reportNoAndBatchNo").show();
    			$("#testTypeName").show();
    			$("#testTypeValue").show();
    			$("#tr_isAuto").show();
    			$("#repLisView").show();
    		}else{
    			root.isImportedProduct = true;
    			$("#sanitaryTr").show();
    			$("#sanAtta").show();
    			$("#reportNoAndBatchNo").hide();
    			$("#testTypeName").hide();
    			$("#testTypeValue").hide();
    			$("#tr_isAuto").hide();
    			$("#repLisView").hide();
    		}
    	}else{
    		root.isImportedProduct = false;
    		$("#sanitaryTr").hide();
			$("#sanAtta").hide();
			$("#reportNoAndBatchNo").show();
			$("#testTypeName").show();
			$("#testTypeValue").show();
			$("#tr_isAuto").show();
			$("#repLisView").show();
    	}
    	
    };
    
    /**
     * 初始化页面数据
     * @author ZhangHui 2015/4/3
     */
    root.initialReportData = function(){
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
                    	root.edit_report = returnValue.data;  // 记录当前正在编辑的报告
                    	
                    	var report = returnValue.data;
                    	var bus_vo = report.bus_vo;
                    	
                    	root.initBarcode = report.product_vo.barcode;
                        root.initBusName = report.bus_vo.name;
                        root.ReportNo_old = report.serviceOrder;
                        
                        report.product_vo.can_edit_pro = false;
                        
                        /**
                         * 如果该食品是进口食品时取到该食品的原产国
                         * @author LongXianzhen 2015/7/9
                         */
                        if(report.product_vo.importedProduct!=null){
                        	root.productCountry=report.product_vo.importedProduct.country.name;
                        }else{
                        	root.productCountry=null;
                        }
                        
                       /**
                   	    * 验证是否有进口食品报告信息有则给进口食品报告信息赋值
                   	    * @author LongXianZhen 2015/06/09
                   	    */ 
                        root.validateIsImportedProduct(report.product_vo.barcode);
                        if(root.isImportedProduct&&report.impProTestResult!=null){
                        	root.imp_pro_test_edit_id=report.impProTestResult.id; //更新报告时保存进口食品报告信息ID
                        	$("#sanitary_cert_no").val(report.impProTestResult.sanitaryCertNo);
                        	root.setSanRepAttachments(report.impProTestResult.sanitaryPdfAttachments);
                        }
                        
                        var qs_no = "";
                    	if(bus_vo!=null && bus_vo.qs_vo!=null){
                    		qs_no = bus_vo.qs_vo.qsNo;
                    		bus_vo.qs_vo = null; // 将qs置空
                    	}
                    	
                        fsn.setReportValue(report, root.product_html_type);
                        
                        // qs号赋值
                    	$("#bus_qsNo").val(qs_no);
                        
                        root.current_barcode_can_use = true; // 编辑状态下，当前产品条形码有使用权
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
     * 封装报告信息(save代表保存,submit代表提交)
     * @author ZhangHui 2015/4/3
     */
    root.createInstance = function(){
    	
    	var product_vo = {
    			barcode: $("#barcodeId").val().trim(),
    			batchSerialNo: $("#tri_batchNo").val().trim(),
    			productionDate: $("#tri_proDate").val().trim(),
    			expirationDate:$("#inputDay").val().trim(),
    			expireDate:$("#tri_proDate").val().trim(),
    			expire_Date:$("#expiration_date").val().trim(),
    			can_edit_pro: false
    	};
    	
    	if($("#bus_qsNo").val().trim() != ""){
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
        			qsNo: jiancheng + $("#showQsFormat").html() + $("#bus_qsNo").val().trim(),
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
    	var report_vo = {
    			id: root.isNew?null:root.edit_id,
                testType: $("#tri_testType").data("kendoDropDownList").value(),
                pass: $("#tri_conclusion").data("kendoDropDownList").value() == "1" ? true : false,
                serviceOrder: $("#tri_reportNo").val().trim(),
                autoReportFlag: false,
                repAttachments: root.aryRepAttachments,
                dbflag: "dealer", // 数据来源：经销商数据录入
                publishFlag:'4',
                product_vo: product_vo,
    			bus_vo: bus_vo,
    			new_flag: root.isNew
    	};
    	
    	//return report_vo;
    	
        /**
         *  当是进口食品时封装进口食品报告信息 
         *  进口食品报告由产品条形码、卫生证书编号、生产日期唯一确定一份报告
         *  为了唯一性验证时与国产食品报告统一把卫生证书编号赋值给报告编号，生产日期赋值给批次
         *   @author LongXianZhen 2015/06/09
         */
        if(root.isImportedProduct){
        	report_vo.testType="政府抽检";//默认为政府抽检
        	var impProTestResult={
        		id:root.imp_pro_test_edit_id?root.imp_pro_test_edit_id:null,
        		sanitaryCertNo:$("#sanitary_cert_no").val().trim(),
        		sanitaryAttachments:root.sanRepAttachments,
        		sanitaryPdfAttachments:root.sanPdfRepAttachments
        	};

        	report_vo.impProTestResult=impProTestResult;
        	report_vo.serviceOrder=$("#sanitary_cert_no").val().trim(); //卫生证书编号赋值给报告编号
        	report_vo.product_vo.batchSerialNo=$("#tri_proDate").val().trim(); //生产日期赋值给批次
        }

    	return report_vo;
    };
   
    /**
     * 提交前，数据验证
     * @author Zhanghui 2015/4/7
     */
    root.validate_submit = function(){
    	/* 必填项验证 */
        root.clearErrorInfoStyle();
        if (!root.validateData()) {
            return false;
        }
        /* 条形码格式校验 */
        var barcode = $("#barcodeId").val();
        var re_barcode = /^[0-9]*$/;
//        if (!barcode.match(re_barcode)) {
//            $("#barcodeId").attr("style", "border:2px solid red;width:250px");
//            lims.initNotificationMes("产品条形码只能由数字组成！", false);
//            return false;
//        }
        
        if(!root.current_barcode_can_use){
        	$("#barcodeId").attr("style", "border:2px solid red;width:250px;");
            lims.initNotificationMes("当前产品条形码有误，请选择正确的条形码！", false);
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
        }
        
        var inputDay=$("#inputDay").val().trim();//保质日期
	    var productionDateStr = $("#tri_proDate").val().trim(); // 生产日期字符串
	    //日期过期验证
	    var flag = root.setTriPorDate(productionDateStr,inputDay);
	    if(!flag){
	    	return false;
	    }
        /**
         *  进口食品唯一性校验前 先给报告编号和批次号赋值
         *  @author longxianzhen 2015/06/05
         */
        if(root.isImportedProduct){
        	$("#tri_reportNo").val($("#sanitary_cert_no").val().trim());
        	$("#tri_batchNo").val($("#tri_proDate").val().trim());
        }
        
        /* 报告编号唯一性校验*/
        if (root.isNew) {
            if (!root.validateReportNO()) {
				if(portal.type){
					lims.initNotificationMes("当前报告已经存在，您无需录入，请在报告管理列表中点击查看！" , false);
				}else{
					lims.initNotificationMes("当前报告已经存在，您无需录入，商超即可查看！" , false);
				}
                return false;
            }
        } else if (root.ReportNo_old != $("#tri_reportNo").val().trim()) {
            if (!root.validateReportNO()) {
                $("#tri_reportNo").attr("style", "border:2px solid red;;width:250px");
                lims.initNotificationMes("报告编号【" + $("#tri_reportNo").val().trim() + "】、条形码【" +
                $("#barcodeId").val().trim() +
                "】、批次号【" +
                $("#tri_batchNo").val().trim() +
                "】已经存在对应的报告，3个字段不能重复！", false);
                return false;
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
        pass = root.validateLicenNoFormat();
        if(!pass){
        	return false;
        }
		
        /* 是否上传pdf校验 */
        if (!root.isImportedProduct&&root.aryRepAttachments.length == 0) {
            lims.initNotificationMes(lims.l("jxs_not found pdf"), false);
            return false;
        } 
        
        /* 进口食品是否上传卫生许可证图片校验 */
        if (root.isImportedProduct&&root.sanPdfRepAttachments.length == 0) {
            lims.initNotificationMes("请上传卫生许可证图片！", false);
            return false;
        }

        return true;
    };

    /**
     * 提交报告
     * @author Zhanghui 2015/4/7
     */
    root.submit = function(){

    	/* 数据格式校验 */
    	if(!root.validate_submit()){ 
    		return; 
    	}
    	
    	/* 当前产品销往企业提醒 */
    	var barcode = $("#barcodeId").val().trim();
    	var customerNames = portal.getCustomerNamesOfProduct(barcode);
    	if(customerNames == ""){
    		lims.initNotificationMes("当前产品的销往客户为空，请前往产品管理界面添加，否则销往客户无法查看此报告。", false);
    	}else{
    		lims.initNotificationMes("当前产品的销往客户为:【" + customerNames + "】。如果不准确，请前往产品管理界面修改该产品的客户。", true);
    	}
    	
        $("#saving_msg").html("正在提交，请稍候...");
        $("#toSaveWindow").data("kendoWindow").open().center();

        var report_vo = root.createInstance();
        
        $.ajax({
            url: portal.HTTP_PREFIX + "/testReport/false",
            type: "POST",
            dataType: "json",
            timeout: 600000, //10min
            async: false,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(report_vo),
            success: function(returnValue){
                fsn.endTime = new Date();
                $("#save_status").html("");
                $("#toSaveWindow").data("kendoWindow").close();
                
                if (returnValue.result.status == "true") {
                	var report_vo = returnValue.data;
                	
                	lims.initNotificationMes('编号为' + report_vo.serviceOrder + '的检测报告 ' + (root.isNew ? lims.l("Add") : lims.l("Update")) + '成功！', true);
					
                	// 当编辑【食安云】退回的报告时，更新完成后，直接跳转至报告管理界面
                	if(root.isNew==false && root.edit_report && root.edit_report.publishFlag=='7'){
                		if(portal.type){
                			window.location.href="/fsn-core/views/report_new/super_report_manage.html";
                		}else{
                			window.location.href="/fsn-core/views/report_new/manage_report_dealer.html";
                		}
                		return;
                	}
                	
                    // 报告pdf赋值
                    setRepAttachments(report_vo.repAttachments);
                    
                    /**
                     * 如果是进口食品保存进口食品报告信息ID
                     */
                    if(root.isImportedProduct&&returnValue.data.impProTestResult!=null){
                    	root.imp_pro_test_edit_id=returnValue.data.impProTestResult.id;//更新报告时保存进口食品报告信息ID
                    	root.setSanRepAttachments(returnValue.data.impProTestResult.sanitaryPdfAttachments);
                    }
                    
                    // 页面状态赋值
                    root.isNew = false;
                    root.ReportNo_old = report_vo.serviceOrder;
                    root.edit_id = report_vo.id;
                    $("ul.k-upload-files").remove();
                	
                    $("#save").hide();
                    $("#clear").hide();
                    $("#submit").hide();
                    $("#update").show();
                    
                    // 报告编辑状态下，以下字段不允许编辑：产品条形码、企业名称、qs号
//                    addInputReadOnly(["barcodeId","bus_name","bus_qsNo"]);
                    addInputReadOnly(["barcodeId"]);
                    $(".qs_format_div").hide();
        	        $("#proJianCheng").data("kendoDropDownList").readonly(true);
        	        hideListBusNameSelect();
        	        $("#barcodeId").unbind("blur");
        	        //if(portal.type){
        	        //	root.submitPassReport(report_vo.id);
        	        //}
                } else {
                	lims.initNotificationMes((root.isNew ? lims.l("Add") : lims.l("Update")) + '失败！参考原因为：' + returnValue.result.errorMessage, false);
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
    };
    //root.submitPassReport = function(reportId){
	//$.ajax({
	//	url: portal.HTTP_PREFIX + "/report/operation/busSuperCheckReport/true/" + reportId,
	//	type: "GET",
	//	dataType: "json",
	//	 success: function(returnValue){
    //        	  if (returnValue.result.status != "true") {
    //        	     lims.initNotificationMes(lims.l("提交报告在发布给testlab环节时出错!"), false);
    //        	  }
    //          },
    //          error: function(e){
    //          }
    //      });
    //}
    /**
     * 必填数据校验
     * @author Zhanghui 2015/4/3
     */
    root.validateData = function(){
    	/* 验证生产企业名称是否为空 */
        if ($("#bus_name").val().trim() == "") {
            lims.initNotificationMes(lims.l("The BusinessName is null"), false);
            return false;
        }
        //如果用户修改了条形码信息，不允许保存。
        if (root.initBarcode != $("#barcodeId").val().trim() && root.initBarcode != null) {
            lims.initNotificationMes(lims.l("You can not modify this product's barcode information, please change the barcode") + root.initBarcode, false);
            return false;
        }
        //如果用户没有选择产品信息，不允许保存。
        if ($("#barcodeId").val().trim() == "" || $("#foodName").text().trim() == "" || $("#foodinfo_category").text().trim() == "") {
            lims.initNotificationMes(lims.l("Current product information is empty, Please select the product after trying to save barcode search!"), false);
            return false;
        }
        var validate_batchNo = $("#tri_batchNo").kendoValidator().data("kendoValidator").validate();
        var validate_reportNo = $("#tri_reportNo").kendoValidator().data("kendoValidator").validate();
        var validate_proDate = $("#tri_proDate").kendoValidator().data("kendoValidator").validate();
        var validate_sanCertNo = $("#sanitary_cert_no").kendoValidator().data("kendoValidator").validate();
        
        if (!root.isImportedProduct&&!validate_reportNo) {
            lims.initNotificationMes("报告编号不能为空", false);
            return false;
        }
        if (!root.isImportedProduct&&!validate_batchNo) {
            lims.initNotificationMes("批次不能为空", false);
            return false;
        }
        if (!validate_proDate) {
            lims.initNotificationMes("生产日期不能为空", false);
            return false;
        }
        if (root.isImportedProduct&&!validate_sanCertNo) {
            lims.initNotificationMes("卫生许可证编号不能为空", false);
            return false;
        }
        
        //验证报告编号不能包含“？”，“?”，“'”字符,如果包含空格通过验证
        var repNo = $("#tri_reportNo").val().trim();
        if (repNo.indexOf("？") > -1 || repNo.indexOf("?") > -1 || repNo.indexOf("'") > -1) {
            $("#tri_reportNo").attr("style", "border:2px solid red;");
            lims.initNotificationMes(lims.l("Report Number") + lims.l("can not contain “'”、“?” illegal characters!"), false);
            return false;
        }
        return true;
    };
    
    /**
     * 掉线提醒确定事件
     * @author ZhangHui 2015/4/7
     */
    root.remind = function(){
    	 $("#diaoxian_remind_01").data("kendoWindow").close();
    	 $("#diaoxian_remind_02").data("kendoWindow").close();
    };
    
   /**
     * 清除输入框的错误提示样式
     * @author ZhangHui 2015/4/14
     */
    root.clearErrorInfoStyle = function(){
        $("#tri_reportNo").removeAttr("style");
        $("#tri_proDate").attr("style", "width:100%;height:100%;");
        $("#tri_testDate").attr("style", "width:100%;height:100%");
    };
    
	/**
	 * 判断该条形码对应的产品,是否是系统中已存在，或者是否已引进
	 * @author ZhangHui 2015/4/30
	 */
	root.judgeProductByBarcode = function(barcode){
		/*条形码为空不进行判断*/
		if(barcode==""){
			return;
		}
		root.queryCheckByBarcode(barcode);
		/* 1 验证产品条形码是否为系统已存在产品 */
        var productId = root.queryProductIdByBarcode(barcode);
        if(productId == null){
        	/* 1.1 该条形码系统不存在，则引导用户跳转至产品新增界面 */
        	root.current_barcode_can_use = false;
			$("#create_product_warn_window").data("kendoWindow").open().center();
			return;
        }
    	/* 1.2 验证该产品是否为本经销商引进产品 */
    	var count = root.countInitialProduct(productId);
    	if(count == 0){
    		if(portal.type){
    			//设置默认销往企业是当前登录的超市
    			var customerSelectInfo = [{id:portal.business.id,name:portal.business.name}];
    			$("#customerSelect_lead").data("kendoMultiSelect").setDataSource(customerSelectInfo);
    			$("#customerSelect_lead").data("kendoMultiSelect").refresh();
    			$("#customerSelect_lead").data("kendoMultiSelect").value(portal.business.id);
    			root.current_barcode_can_use = false;
				$("#lead_product_warn_window").data("kendoWindow").open().center();
				var customerItems = $("#customerSelect_lead").data("kendoMultiSelect").dataItems();
				if (customerItems.length<1) {
	                lims.initNotificationMes("请选择销往客户", false);
					return ;
	            }
					var isSuccess = portal.leadProduct(barcode);
						if(isSuccess){
						var customerNames = "";
						var customerIds = "";
						for ( var entry in customerItems) {
							customerNames += (customerItems[entry].name + ",");
							customerIds += (customerItems[entry].id + ",");
						}
						lims.initNotificationMes("条形码为：" + barcode + "的产品引进成功", true);
						/* 保存 产品-当前登录企业-销往企业 的关系 */
						var productVO = {
								barcode: barcode,
								selectedCustomerIds: customerIds,
						};
						portal.saveSelectCustomer(productVO);
						
						root.current_barcode_can_use = true; // 记录当前条形码可以使用
						root.initPageData(barcode);
					}else{
						lims.initNotificationMes("条形码为：" + barcode + "的产品引进失败", false);
					}
						$("#lead_product_warn_window").data("kendoWindow").close();
    			} else{
    				/* 1.2.1 不是引进产品，则引导用户填写销往客户 */
    				root.current_barcode_can_use = false;
    				$("#lead_product_warn_window").data("kendoWindow").open().center();
    			}
    	}else{
    		/* 1.2.2 是引进产品或者是自己新增的产品，则进行正常录报告流程 */
    		root.current_barcode_can_use = true;
    		root.initPageData(barcode);
    	}
	};
	root.queryCheckByBarcode = function(barcode){
		$.ajax({
	         url: fsn.getHttpPrefix() + "/product/query/getCheckProductId/" + barcode,
	         type: "GET",
	         dataType: "json",
	         async: false,
	         success: function(returnValue) {
	             if (returnValue.result.status == "true") {
	             	productId = returnValue.data;
	             	if(productId == null){
	                	/* 1.1 该条形码系统不存在，则引导用户跳转至产品新增界面 */
	                	root.current_barcode_can_use = false;
	        			$("#create_product_warn_window").data("kendoWindow").open().center();
	        			return;
	                }
	             	var count = returnValue.count;
	            	if(count == 0){
	            		var customerSelectInfo = [{id:portal.business.id,name:portal.business.name}];
	            		$("#customerSelect_lead").data("kendoMultiSelect").setDataSource(customerSelectInfo);
	            		$("#customerSelect_lead").data("kendoMultiSelect").refresh();
	            		$("#customerSelect_lead").data("kendoMultiSelect").value(portal.business.id);
//	            		$("#customerSelect").val(portal.business.name+";");
	            		/* 1.2.1 不是引进产品，则引导用户填写销往客户 */
	            		root.current_barcode_can_use = false;
	            		$("#lead_product_warn_window").data("kendoWindow").open().center();
	            	}else{
	            		/* 1.2.2 是引进产品或者是自己新增的产品，则进行正常录报告流程 */
	            		root.current_barcode_can_use = true;
	            		root.initPageData(barcode);
	            	}
	             }
	         }
	     });
	}
	/**
	 * 选中下拉框中的条形码事件
	 * @author ZhangHui 2015/4/14
	 */
	root.onSelectBarcode = function(e){
		 root.initBarcode = this.dataItem(e.item.index());
		 $("#barcodeId").val(root.initBarcode);
		 root.judgeProductByBarcode(root.initBarcode);
		 portal.codeFlag = false;
	};
	
	/**
	 * 初始化页面数据
	 * @author ZhangHui 2015/4/30
	 */
    root.initPageData = function(barcode){
    	root.initBarcode = barcode;
        /* 每次当条形码发生改变之后，都要清空上次一加载的数据 */
        lims.listOfBusunitName = null;
        lims.pro2Bus = null;
        $(".hideBusName").hide();
        $("#hid_listBusName").data("kendoComboBox").setDataSource([]);
        lims.setProductInfoToEdit();
        /* 根据条形码初始化页面 */
        var isExist = root.autoloadingPageInfoByBarcode(barcode);
        /* 验证是否是进口产品 */
        root.validateIsImportedProduct(barcode);
        
		var isExist=root.verifyBackReportByBarcode(barcode);
		if(root.isNew){
			if(isExist){
				 var currentBusiness = portal.business;
				 if(currentBusiness!=null&&!currentBusiness.passFlag){
					 $("#save").hide();
				     $("#clear").hide();
				     $("#submit").hide();
				     $("#backBarcode").hide();
				     //fsn.clearReportAndBusinessUnit();
				     fsn.backMsg("backMsg","您的条码为"+barcode+"的产品有未处理的退回报告或二次退回的报告没有通过审核，" +
				     		"请先到报告管理页面进行处理或等待审核通过！");
				 }else{
					 $("#backBarcode").show();
					 $("#save").show();
				     $("#clear").show();
				     $("#backMsg").hide(); 
				 }
			}else{
				 $("#backBarcode").show();
				 $("#save").show();
			     $("#clear").show();
			     $("#submit").show();
			     $("#backMsg").hide();
			}
		}
    };

    /**
     * 根据条形码验证该产品是否有已退回的报告
     */
    root.verifyBackReportByBarcode=function(barcode){
    	/*条形码为空不进行判断*/
    	if(barcode==""){
    		return;
    	}
    	var isExist = false;
     	$.ajax({
             url: fsn.getHttpPrefix() + "/testReport/verifyBackReport/" + barcode,
             type: "GET",
             dataType: "json",
             async: false,
             success: function(returnValue) {
                 if (returnValue.result.status == "true") {
                	 isExist = returnValue.isExist;
                 }
             }
         });
     	 return isExist;
    };
	/**
	 * 当掉线时处理方法
	 * @author ZhangHui 2015/5/9
	 */
	fsn.save = function(){
		// 不做任何处理
	};
	
	root.my_clearTempReport = function(){
    	if(root.isNew){
    		// 清空页面数据
            root.aryRepAttachments.length = 0;
    		fsn.setReportValue({}, root.product_html_type);
    		
    		/* 初始化报告pdf上传控件 */
            $("#upload_rep").html("<input id='upload_report_files' type='file' />");
            root.buildUpload("upload_report_files", root.aryRepAttachments, "repFileMsg", "report");
            removeDisabledToBtn(["openT2P_btn","openS2P_btn"]);
            
            // 清空卫生许可证
            $("#sanitary_cert_no").val("");
            setSanAttachments([]);
            
            $("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
        }
    };
    
	root.initialize();
});