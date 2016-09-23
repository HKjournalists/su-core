(function($) {
    var lims = window.lims = window.lims || {};
    
    lims.hostname = window.location.hostname;
    
    
    lims.TEMPLATE_MIN_SIZE = 800;
    lims.PREVIEW_ROW_COLUMNS = 3;
    lims.PREIVEW_PERCENTAGE_ROW_COLUMNS = 4;
    lims.TEMPLATE_FORM_DIALOG_MIN_HEIGHT = 500;
    lims.defaultLocale = "zh";
    lims.DATE_FORMAT = 'YYYY-MM-dd hh:mm:ss';
    lims.ENABLE_GROUP_FIELD = true;
    lims.MAX_FILE_SIZE = 64000;
    
    lims.CUSTOMER_URL = "lims.fsnip.com";
    lims.QA_URL = "qalims.fsnip.com";
    lims.STG_URL = "stglims.fsnip.com";
    lims.PROD_URL = "lims.fsnip.com";
    lims.MARKET_URL = "easy.fsnip.com";
    
    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH ="/fsn-core"; // 项目名称
	portal.HTTP_PREFIX =fsn.getHttpPrefix(); // 业务请求前缀
    
    String.prototype.format = String.prototype.f = function(arg) {
        var s = this,
            i = arg.length;

        while (i--) {
            s = s.replace(new RegExp('\\{' + i + '\\}', 'gm'), arg[i]);
        }
        return s;
    };
    
    lims.uploadSSOJS = function(){
    	switch(this.hostname){
		case "enterprise.fsnip.com":
			$.getScript("http://sso.fsnip.com/js/system/digital_cert.js");
    		break;
		case "stgenterprise.fsnip.com":
			$.getScript("http://stgsso.fsnip.com/js/system/digital_cert.js");
    		break;
		case "qaenterprise.fsnip.com":
			$.getScript("http://qasso.fsnip.com/js/system/digital_cert.js");
    		break;
		default://DEV
			$.getScript("http://devsso.fsnip.com:9090/sso/js/system/digital_cert.js");
    		break;
    	}
    };
    
    lims.getHostName = function(){
    	var httpPrefix = "";
    	switch(this.hostname){
		case "enterprise.fsnip.com":
		case "stgenterprise.fsnip.com":
		case "qaenterprise.fsnip.com":
			httpPrefix = this.hostname + "/fsn-core";
    		break;
		default://DEV
			httpPrefix = this.hostname + ":8080/fsn-core";
    		break;
    	}
    	console.log("LIMS Http Prefix:" + httpPrefix);
    	return httpPrefix;
    };
    
    lims.log = function(d) {
        console.log(d);
    };
    
    lims.closeConfirmWin = function(){
    	$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
    };
    
    lims.updateLocale = function() {
        $('*[data-lims-text]')
            .each(function() {
            var pack = $(this)
                .attr("data-lims-i18n-package");
            var ht = lims.l($(this)
                .attr("data-lims-text"), pack);
            if ($(this)
                .is('button')) {
                try {
                    $(this)
                        .html('<span class="padding: .4em 1em;display: block;line-height: normal">'+ht+'</span>');
                } catch (e) {
                    $(this)
                        .html(ht);
                }
            } else {
                $(this)
                    .html(ht);
            }
        });
        $('input[data-lims-placehoder]')
            .each(function() {
            var pack = $(this)
                .attr("data-lims-i18n-package");
            $(this)
                .attr("placeholder", lims.l($(this)
                .attr("data-lims-placehoder"), pack));
        });

        $("*[data-lims-format]")
            .each(function() {
            var pack = $(this)
                .attr("data-lims-i18n-package");
            $(this)
                .attr("data-lims-format", lims.l($(this)
                .attr("data-lims-format"), pack));
        });
        
        
        $("*[data-required-msg]")
            .each(function() {
            var pack = $(this)  .attr("data-lims-i18n-package");
            $(this).attr("data-required-msg", lims.l($(this)
                .attr("data-required-msg"), pack));
        });
        $("*[validationMessage]")
            .each(function() {
            var pack = $(this)  .attr("data-lims-i18n-package");
            $(this).attr("validationMessage", lims.l($(this)
                .attr("validationMessage"), pack));
        });
        
    };
    
    lims.l = function(string, module, locale) {
        return lims.localized(string, module, locale);
    };
    
    lims.localized = function(string, module, locale) {
        locale = locale || lims.defaultLocale;
        codes = lims[locale];
        commonCodes = lims[locale];

        if (module) {
            if (lims[module] && lims[module][locale]) {
                codes = lims[module][locale];
            }
        }
        if (codes && codes[string]) {
            return codes[string];
        }
        if (commonCodes && commonCodes[string]) {
            return commonCodes[string];
        }
        return string;
    };
    
    lims.initConfirmWindow = function(yesFun,noFun,msg,confirm){
    	$("#CONFIRM_COMMON_WIN").kendoWindow({
			width: "480",
			height:"auto",
	        title: confirm !=undefined ? lims.l("WIN_CONFIRM"):lims.l("WIN_BLANK"),
	        visible: false,
	        resizable: false,
	        draggable:false,
	        modal: true
		});
    	
    	console.log(msg);
    	
    	//add the "k-button" css onto buttons
    	$("#CONFIRM_COMMON_WIN button").addClass("k-button");
    	if(msg){
    		$("#CONFIRM_MSG").html(msg);
    	}else{
    		$("#CONFIRM_MSG").html(lims.l('WIN_DEFAULT_MSG'));
    	}
    	
    	if(yesFun){
    		$("#confirm_yes_btn").bind("click",function(){
    			yesFun();
    			lims.closeConfirmWin();
    		});
    	}else{
    		$("#confirm_yes_btn").bind("click",lims.closeConfirmWin);
    	}
    	
    	if(noFun){
    		$("#confirm_no_btn").bind("click",noFun);
    	}else{
    		$("#confirm_no_btn").bind("click",lims.closeConfirmWin);
    	}
    };
    
    lims.gridfilterMessage = function() {
    	return {
    		info: lims.l("Show items with value that:"),
			and: lims.l("and"),
			or: lims.l("or"),
			filter: lims.l("Filter"),
			clear: lims.l("Clear")
    	};
    };
    
    lims.gridfilterOperatorsDate = function() {
    	return {
    		gte: lims.l("Greater than or equal to"),
    		gt:  lims.l("Greater than"),
    		lte: lims.l("Less than or equal to"),
    		lt:  lims.l("Less than"),
    		eq:  lims.l("Is equal to"),
    		neq: lims.l("Is not equal to"),
    	};
    };
    
    lims.gridfilterOperatorsNumber = function() {
    	return {
    		eq: lims.l("Is equal to"),
    		neq: lims.l("Is not equal to"),
    		gte: lims.l("Greater than or equal to"),
    		gt:  lims.l("Greater than"),
    		lte: lims.l("Less than or equal to"),
    		lt:  lims.l("Less than"),
    	};
    };
    
    lims.gridfilterOperators = function() {
    	return {
		     contains: lims.l("Contains"),
		     doesnotcontain: lims.l("Does not contain"),
		     startswith: lims.l("Starts with"),
		     endswith: lims.l("Ends with"),
		     eq: lims.l("Is equal to"),
		     neq: lims.l("Is not equal to")
    	};
    };
    
    lims.gridPageMessage = function() {
        return {
            empty: lims.l("No items to display"),
            itemsPerPage: lims.l("items per page"),
            of: lims.l("of {0}"),
            last: lims.l("Go to the last page"),
            first: lims.l("Go to the first page"),
            next: lims.l("Go to the next page"),
            previous: lims.l("Go to the previous page"),
            refresh: lims.l("Refresh"),
            page: lims.l("Page"),
            display: lims.l("{0} - {1} of {2} items"),
        };
    };
    
    lims.initNotificationMes = function(mes,isSuccess) {
    	var notification = $("#RETURN_MES").getKendoNotification();
    	if(!notification){
    		notification = $("#RETURN_MES").kendoNotification({
    			position: {
    				pinned: true,
    				bottom: 60,
    			},
    			autoHideAfter: isSuccess ? 3000:10000,
//    			stacking: "down",z-index: 10003;
    			templates: [{
    				type: "error",
    				template: $("#errorTemplate").html()
    			}, {
    				type: "upload-success",
    				template: $("#successTemplate").html()
    			}]
    			
    		}).data("kendoNotification");
    	}
    	 if(isSuccess){
    		 notification.show({
    			 message: mes
    		 }, "upload-success");
    	 }else{
    		 notification.show({
    			 message: mes
    		 }, "error");
    	 }
	};
    
	 lims.msg = function(id,result,mes,width){//html中div的ID ， ResponserVO对象 ， 弹出信息  ,弹出框宽度
	    	$("#" + id).removeAttr("style");
	    	var divcss = { 
	    			background: '#EEE',
	    			margin: '10px 0 0', 
	    			padding: '5px 10px', 
	    			border: '2px solid #33CC33',
	    	}; 
	    	if(width){
	    		divcss.width = width;
	    	}
	    	if(null == result){
	    		divcss.border = "2px solid #519505";
	    		divcss.position="fixed";
	    		divcss.bottom="4em";
	    		divcss.left="15em";
	    		divcss.right="15em";
	        	$("#" + id).css(divcss);
	        	$("#" + id).html('<span class="k-icon k-i-success"></span>&nbsp;<span style="color: #519505;">'+ mes+'</span>');
	    		setTimeout('$("#' +id+'").show("slow")',10);
	    	}else if(result == true){
	    		divcss.border = "2px solid #519505";
	    		divcss.width="100%";
	        	$("#" + id).css(divcss);
	        	$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
	       		setTimeout('$("#' +id+'").show("slow")',10);
	          	setTimeout('$("#' +id+'").hide("slow")',2000);
	        }else if(result.status == "true"){
	    		divcss.border = "2px solid #519505";
	    		divcss.width="100%";
	    		$("#" + id).css(divcss);
	    		$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
	    		setTimeout('$("#' +id+'").show("slow")',10);
	        	setTimeout('$("#' +id+'").hide("slow")',2000);
	    	}else if(result.status == "false"){
	    		divcss.border = "2px solid #FF3333";
	    		divcss.position="fixed";
	    		divcss.bottom="55px";
	    		$("#" + id).css(divcss);
	    		$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
	    		setTimeout('$("#' +id+'").show("slow")',10);
	    	}else {
	    		divcss.border = "2px solid #FF3333";
	    		divcss.position="fixed";
	    		divcss.bottom="55px";
	        	$("#" + id).css(divcss);
	        	$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
	    		setTimeout('$("#' +id+'").show("slow")',10);
	        } 
	    	/*setTimeout('$("#' +id+'").show("slow")',50);
	    	setTimeout('$("#' +id+'").hide("slow")',1500);*/
	    };
    
  //初始化easy预览报告的检测项目,根据reportID查询,
    lims.initEasyItemDS = function(reportID){
    	if(reportID){
    		lims.easyViewReportID = reportID;
    	}else{
    		lims.easyViewReportID = 0;
    	}
    	lims.easyItemFirstPageFlag = 1;
		lims.easyTestItemDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		if(lims.easyViewReportID == 0){
	            			return;
	            		}
	            		if(lims.easyItemFirstPageFlag == 1){  //每次预览新的报告的时候，都从第一页开始显示
	            			options.page=1;
	            			options.pageSize=5;
	            			lims.easyItemFirstPageFlag=0;
	            		}
	            		return portal.HTTP_PREFIX + "/testReport/testItems/" + options.page + "/" + options.pageSize + "/" + lims.easyViewReportID;
	            	},
	            	type:"GET",
	                dataType : "json",
	                contentType : "application/json"
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	            data : function(returnValue) {
	                return returnValue.data.listOfItem;
	            },
	            total : function(returnValue) {       
	                return returnValue.data.counts;
	            }     
	        },
	        change: function(e) {
	            
	        },
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
    
	//初始化easy预览报告时检测项目的Grid
	lims.initEasyItemGrid = function(girdID){
		if(girdID){
			$("#" + girdID).kendoGrid({
				dataSource:[],
		        editable: false,
		        pageSize : 10,
		        pageable: {
		            refresh: true,
		            pageSizes: 5,
		            messages: lims.gridPageMessage(),
		        },
		        toolbar: [
		        		  {template: kendo.template($("#toolbar_excel").html())}
		                  ],
		        columns: [
		                  { field: "name", title:lims.l("Item Name"), width: 40 },
		                  { field: "unit", title:lims.l("Unit"),width: 30 },
		                  { field: "techIndicator", title:lims.l("Specifications"), width: 50},
		                  { field: "result", title:lims.l("Test Result"), width: 40},
		                  { field: "assessment", title:lims.l("Conslusion"), width: 40},
		                  { field: "standard", title:lims.l("Standard"), width: 50},
			             ],
			});
		}
	};
	
	/*将产品信息输入框设置为只读*/
	lims.setProductInfoToReadOnly=function(){
		$(".editProStyle").css("display","inline");
		$(".hos_tr").hide();
		$("#btn_clearProFiles").hide();
		$("#proAttachmentsListView button").hide();
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
		if ($("#regularity").data("kendoComboBox")) {
			$("#regularity").data("kendoComboBox").readonly(true);
		}
		$("table.productTable input").attr("readonly","true");
		$("table.productTable button").attr("disabled","true");
		$("#barcodeId").removeAttr("readonly");
	};
	
	/*将产品信息输入框设置为可编辑*/
	lims.setProductInfoToEdit=function(){
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
		if ($("#regularity").data("kendoComboBox")) {
			$("#regularity").data("kendoComboBox").readonly(false);
		}
		$("table.productTable input").removeAttr("readonly");
		$("table.productTable button").removeAttr("disabled");
	};
	
	//给easy的Product赋值
	lims.setEasyProductValue = function(sample, isReview){
		 lims.setProductInfoToEdit();
		 if(sample==null) return;
		 var product = sample.product;
		 $("#sample_id").val(sample.id);
		 $("#barcodeId").val(product.barcode);
		 $("#foodName").val(product.name);
		 $("#specification").val(product.format); // 规格
		 if(product.businessBrand){
			 $("#foodinfo_brand").val(product.businessBrand.name);  // 商标
		 }
		 $("#foodinfo_standard").val(lims.convertRegularityToString(product.regularity)); // 执行标准
		 $("#foodinfo_expiratio").val(product.expiration);  // 质保期（无）
		 $("#foodinfo_expirday").val(product.expirationDate);  // 保质天数
		 $("#foodinfo_minunit").val(product.unit==null?"":product.unit.name);  // 单位
		 $("#foodInfo_Status").val(product.status);  // 商品状态 
		 if(!isReview){
			 lims.setExpirDay(product.expiration);
			 lims.root.setProAttachments(product.proAttachments);
			 if(product.category){
			 	var code = null;
				if(product.category.categoryOneCode!=null){
					code=product.category.categoryOneCode;
				}
				var secondCategory = product.category.category;
				if(code==null&&secondCategory!=null&&secondCategory.code!=null){
					code = product.category.category.code;
				}
				if(code!=null&&code.indexOf("15")==0) $(".Expiratio").css("display","none");//code以15开头是酒类
				 else $(".Expiratio").css("display","inline");
			   }
			 if(product.isBindOfProducer){
				 lims.setProductInfoToReadOnly();
			 }
		 }else{
			 $("#foodinfo_expiratio").val(product.expiration);
			 $("#foodinfo_category").val(product.category.name);
		 }
	};
	
	/*给检测项目赋值*/
	lims.setEasyItems = function(items){
		if(items==null){return;}
		for(var i=0;i<items.length;i++){
			$("#report_grid").data("kendoGrid").dataSource.add(items[i]);
		}
		$("#report_grid").data("kendoGrid").refresh();
	};
	
	/**
	 * isReview: 是否为预览； isSearch: 是否为搜索。
	 */
	lims.setEasyReportValue = function(report, isReview, isSearch){
		 if(report==null){return;}
		 $("#tri_reportNo").val(report.serviceOrder);
		 $("#tri_testee").val(report.testee==null?"":report.testee.name);
		 $("#tri_testOrg").val(report.testOrgnization);
		 $("#tri_testPlace").val(report.testPlace);
		 $("#reportEndDate").val("");
		 //每次赋值之前都应该先将日期的值清空
		 $("#tri_proDate").val("");
		 $("#tri_testDate").val("");
		 if(isReview){
			 $("#tri_conclusion").val(report.pass?"合格":"不合格");
			 if(report.endDate) $("#reportEndDate").val(report.endDate.substr(0, 10));
             //if(report.endDate) $("#reportEndDate").data("kendoDatePicker").value(report.endDate.substr(0, 10));
		 }else{
			 if(report.id==null){
				 $("#tri_conclusion").data("kendoDropDownList").value("1");
			 }else{
				 $("#tri_conclusion").data("kendoDropDownList").value(report.pass?"1":"0");
			 }
			 if(report.listOfBusunitName!=null&&report.listOfBusunitName.length>1){
				 lims.listOfBusunitName=report.listOfBusunitName;
				 lims.pro2Bus=report.pro2Bus;
				 $(".hideBusName").show();
				 $("#hid_listBusName").data("kendoComboBox").setDataSource(report.listOfBusunitName);
			 }else{
				 lims.listOfBusunitName=null;
				 lims.pro2Bus=null;
				 $(".hideBusName").hide();
				 $("#hid_listBusName").data("kendoComboBox").setDataSource([]);
			 }
			 
		  }
		 
		 $("#tri_batchNo").val(report.sample.batchSerialNo);  // 批次
		 $("#sampleAds").val(report.samplingLocation);  // 抽样地点
		 $("#sampleCounts").val(report.sampleQuantity);  // 抽样量
		 $("#judgeStandard").val(report.standard);
		 $("#testResultDescribe").val(report.result);
		 $("#remarks").val(report.comment);
		 $("#foodInfo_ads").val(report.sample.producer==null?"":report.sample.producer.address);
		 if(!isReview){
			 /*如果样品实例存在，当生产日期为空的时候默认显示 0000-00-00，注意后台返回的数据只有当id不为空的时候，
			  * 才表示样品实例存在。
			  */
			 if(report.sample != null ) {
	        	 if (report.sample.id != null) {
	                $("#tri_proDate").val(report.sample.productionDate == null ? "0000-00-00" : report.sample.productionDate.substr(0, 10));
	             } else {
	            	$("#tri_proDate").val(report.sample.proDateStr == null ? null : report.sample.proDateStr.substr(0, 10));
	             }
		     }
			 if(report.id!=null&&report.testDate) $("#tri_testDate").val(report.testDate.substr(0, 10));
			 $("#tri_testType").data("kendoDropDownList").value(report.testType);
			 if(report.autoReportFlag){
				 $("#yesAuto").attr("checked","checked");
                 document.getElementById("yesAuto").checked=true;
				 $("#tr_isAuto").hide();
			 }else{
				 document.getElementById("noAuto").checked=true;
				 lims.root.setRepAttachments(report.repAttachments);
				 $("#tr_isAuto").show();
			 }
			 $("#report_grid").data("kendoGrid").dataSource.data(report.testProperties==null?[]:report.testProperties);
			 lims.root.isAutoReport = report.autoReportFlag;
		 }else{
			 var proDate = report.sample.productionDate;
			 $("#tri_proDate").val(proDate==null?"0000-00-00":proDate.substr(0, 10));
			 if(report.testDate) $("#tri_testDate").val(report.testDate.substr(0, 10));
			 $("#tri_testType").val(report.testType);
			 var proAttDS = new kendo.data.DataSource();
			 var repAttDS = new kendo.data.DataSource();
			 if(report.sample.product.proAttachments.length>0){
				 $("#proAtt_tr").show();
				 for(var i=0;i<report.sample.product.proAttachments.length;i++){
					 proAttDS.add({attachments:report.sample.product.proAttachments[i]});
				 }
			 }else{
				 $("#proAtt_tr").hide();
			 }
			 $("#proAttachments").kendoListView({
		            dataSource: proAttDS,
		            template:kendo.template($("#uploadedFilesTemplate").html()),
		     });
			 if(report.repAttachments.length>0){
				 for(var i=0;i<report.repAttachments.length;i++){
					$("#repAtt_tr").show();
					repAttDS.add({attachments:report.repAttachments[i]});
				 }
			 }else{
				 $("#repAtt_tr").hide();
			 }
			 $("#repAttachments").kendoListView({
		         dataSource: repAttDS,
		         template:kendo.template($("#uploadedFilesTemplate").html()),
		     });
			 lims.initEasyItemDS(report.id);
			 $("#testItem_grid").data("kendoGrid").setDataSource(lims.easyTestItemDS);
		 }
		 
		 if(isSearch){
			 if($("#repAttachmentsListView").data("kendoListView")){
				 $("#repAttachmentsListView").data("kendoListView").dataSource.data([]);
			 }
			 lims.root.isAutoReport=true;
			 document.getElementById("yesAuto").checked=true;
			 $("#tr_isAuto").hide();
			 $("#btn_clearRepFiles").hide();
			 lims.root.aryRepAttachments.length=0;
		 }
		 
	};
	
	lims.setExpirDay=function(value){
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
	
    lims.getExpirDay=function(){
    	var result="";
    	var year=$("#proExpirYear").val();
    	var month=$("#proExpirMonth").val();
    	var day=$("#proExpirDay").val();
    	year=year.trim().length<1?"0":year.trim();
    	month=month.trim().length<1?"0":month.trim();
    	day=day.trim().length<1?"0":day.trim();
    	if(year=="0"&&month=="0"&&day=="0"){
    		return $("#proExpirOther").val();
    	}
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
    
    lims.countExpirday=function(){
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
    		$("#foodinfo_expirday").val(result);
    	}
    };
    
    lims.easyViewReport = function(viewRepID){
    	if(!viewRepID){return;}
    	$.ajax({
			url:portal.HTTP_PREFIX + "/testReport/" + viewRepID,
			type:"GET",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					lims.setEasyProductValue(returnValue.data.sample, true);
                    // var product = returnValue.data.sample.product;
                    //var busUnit = product.producer!=null?product.producer:returnValue.data.sample.producer;
                    var busUnit = returnValue.data.sample.producer;
					lims.setEasyBusunitValue(busUnit);
					lims.setEasyReportValue(returnValue.data, true, false);
                    var qsNo = returnValue.data.sample.producer.qsNoAndFormatVo.qsNo;
                    $("#bus_qsNo").val(qsNo);
					$("#viewWindow").data("kendoWindow").open().center();
				}
			},
		});
    };
    
    lims.getNextField=function(currentField){
    	if(currentField=="name"){return "unit";}
    	if(currentField=="unit"){return "techIndicator";}
    	if(currentField=="techIndicator"){return "result";}
    	if(currentField=="result"){return "assessment";}
    	if(currentField=="assessment"){return "standard";}
    	if(currentField=="standard"){return null;}
    	return null;
     };
     
     lims.getNextElement=function(currentElement){
    	 if($(currentElement).find("a").length>0){return null;}
    	 return $(currentElement).next();
     };
     
     lims.getInitColumnIndex=function(currentField){
    	if(currentField=="name"){return 1;}
    	if(currentField=="unit"){return 2;}
    	if(currentField=="techIndicator"){return 3;}
    	if(currentField=="result"){return 4;}
    	if(currentField=="standard"){return 6;}
    	return null;
     };
     
     /**
	  * 粘贴excel内容到检测项目
	  */
     lims.pasteExcel=function(){
    	 /* 1.获取剪切板内容 */
    	 var itemStr = window.clipboardData.getData("Text");
		 if(itemStr == null || itemStr == "itemStr"){
			 alert("粘贴板为空。");
			 return;
		 }
		 var itemArys = itemStr.split("\r\n"); // 按换行符分割字符串
		 /* 定义二维数组  */
		 var items = new Array();
	     for(var i=0;i<itemArys.length-1;i++){
	    	 var itemAry = itemArys[i];
	    	 items[i] = itemAry.split("\t");; // 按Tab符分割字符串
	     }
		 /* 2.粘贴到对应检测项目表格中 */
		 var activeElement=$("#report_grid_active_cell");
	     if(!activeElement.length){
	    	lims.initNotificationMes('请先选择检测项目列表中的某一个单元格！',false);
	    	return;
	     }
	     var htmVal=activeElement.html();
	     if(htmVal=="合格"||htmVal=="检测项目名称"||htmVal=="单位"||htmVal=="技术指标"||
	    	 htmVal=="检测结果"||htmVal=="检测依据"||htmVal=="单项评价"||htmVal=="操作"){
	    	 lims.initNotificationMes('请先选择检测项目列表中的某一个单元格！',false);
	    	 return;
	     }
	     if(htmVal.length>18 && htmVal.substr(0,18)=='<a class="k-button'){
	    	 lims.initNotificationMes('请选择可操作的单元格！',false);
	    	 return;
	     }
	     $("#report_grid_active_cell span").remove();
	     /* 获取初始单元格的列索引 */
	     var initColumnIndex = lims.getInitColumnIndex(lims.root.grdItemFiled);
	     var oldField= lims.root.grdItemFiled;
	     var initRowIndex = $("#report_grid_active_cell").parent().index();
	     //root.grdItemFiled:name、unit、techIndicator、assessment、standard
	     for(var i=0;i<items.length;i++){
	    	 for(var j=0;j<items[i].length;j++){
	    		 if(activeElement!=null && lims.root.grdItemFiled!=null){
		    		 if(lims.root.grdItemFiled != "assessment"){
		    			 activeElement.html(items[i][j]);
						 lims.root.grdItem[lims.root.grdItemFiled] = items[i][j];
					 }
					 activeElement=lims.getNextElement(activeElement);
					 lims.root.grdItemFiled=lims.getNextField(lims.root.grdItemFiled);
		    	 }
	    	 }
	    	 //换行
	    	 lims.root.grdItem = $("#report_grid").data("kendoGrid").dataItem("tr:eq("+(++initRowIndex)+")");
	    	 if(lims.root.grdItem == null){
	    		 lims.root.grid_AddItem();
	    		 lims.root.grdItem = $("#report_grid").data("kendoGrid").dataItem("tr:eq("+(initRowIndex)+")");
	    	 }
	    	 var nextRow = $(activeElement).parent().next(); // 获取下一行
	    	 activeElement=$(nextRow).find("td").eq(initColumnIndex);
	    	 lims.root.grdItemFiled=oldField;
	     }
		 /* 3.清空剪切板 */
		 //window.clipboardData.clearData();
     };
    
     lims.getAutoLoadDsByUrl=function(url){
		 return new kendo.data.DataSource({
				transport: {
		            read: {
		            	url:portal.HTTP_PREFIX + url,
		            	type:"GET"
		            }
		        },
		        schema: {
		        	data: function(returnValue){
		        		fsn.endTime = new Date();
		        		if(url.indexOf("BarCode")>-1){
		        			lims.root.listOfBarCodes=returnValue.data;
		        		}
		        		return returnValue.data;
		        	}
		        }
		    });
	 };
     
	 /*给指定的input框添加readonly属性*/
	 lims.addInputReadOnly=function(listInputId){
		if(listInputId==null||listInputId.length<1){return;}
		for(var i=0;i<listInputId.length;i++){
			$("#"+listInputId[i]).attr("readonly",true);
		}
	 };
	 
	 /*清除指定input框添的readonly属性*/
	 lims.clearInputReadOnly=function(listInputId){
			if(listInputId==null||listInputId.length<1){return;}
			for(var i=0;i<listInputId.length;i++){
				$("#"+listInputId[i]).removeAttr("readonly"); 
			}
		 };
	/*通过指定属性来初始话一个kendoWindow*/
	lims.initKendoWindow=function(formId,title,w,h,visible,modal,resizable){
		$("#"+formId).kendoWindow({
			width:w,
			height:h,
	   		visible:visible,
	   		title:title,
	   		modal: modal,
	   		resizable:resizable,
	   		actions:formId=="pictureToPdfWindow"?[]:["Close"],
		});
	};	 
	
    /*通过指定资源的url下载资源*/
	lims.downloadByUrl=function(url){
		try{
			if(!url) return;
            var hiddenIFrameID = 'hiddenDownloader';
            iframe = document.getElementById(hiddenIFrameID);
            if (iframe === null) {
                iframe = document.createElement('iframe');
                iframe.id = hiddenIFrameID;
                iframe.style.display = 'none';
                document.body.appendChild(iframe);
            }
            iframe.src = url;
        }catch(e){
            lims.debug(e);
        }
	};
	 
     Date.daysInMonth = function(year, month){
         if (month == 1) {
             if (year % 4 == 0 && year % 100 != 0) 
                 return 29;
             else 
                 return 28;
         }
         else 
             if ((month <= 6 && month % 2 == 0) || (month = 6 && month % 2 == 1)) 
                 return 31;
             else 
                 return 30;
     };
	 
	 /*让指定的日期加指定的月数*/
	 lims.addMonth = function(curentDate, month) {
		 	var date = new Date(curentDate);
		    var y = date.getFullYear();  
		    var m = date.getMonth();  
		    var nextY = y;  
		    var nextM = m;  
		      
		    if(m < 6){  
		        nextM = date.getMonth() + month;
		    }else{  
		        nextY = y + 1;  
		        nextM = date.getMonth() - month;
		    }  
		      
		    var daysInNextMonth = Date.daysInMonth(nextY, nextM);  
		      
		    var day = date.getDate();  
		    if (day > daysInNextMonth) {  
		        day = daysInNextMonth;  
		    }  
		    return new Date(nextY, nextM, day, 08, 00, 00);  
		};  
	 
	 /*通过知道的参数初始化日期控件*/
	 lims.initDatePicker=function(formId,startDate,endDate){
		 if(("#"+formId).data("kendoDataPicker")){
			 ("#"+formId).data("kendoDataPicker").distery();
		 }
		 $("#"+formId).kendoDataPicker({
			 format: "yyyy-MM-dd", 
	    	 height:30,
	    	 culture : "zh-CN",
	    	 min:startDate,
	    	 max:endDate,
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
	 
     /*从kms获取检查项目名称*/
     lims.getItemsNameDS = function(page,keyword){
		 	var listDs = [];
		 	$.ajax({
		 		async: false,
		 		url:fsn.getKMSPrefix() + "/lims-standard-cloud/service/testitem/search?page="+page+"&pageSize=20&keyword=" + keyword,
		 		type:"GET",
		 		success:function(returnVal){
		 			listDs = returnVal.data.resultList;
		 		}
		 	});
		 	return listDs;
	 	};
	 	
     /*在listbos中追加检测项目*/
	 lims.appendKmsItemName = function(listBox,currentPage,keyword){
		 var scrollTop = $(listBox).scrollTop();
		 var windowHeight = $(listBox).height();
		 var listLi = $("#itemName_listbox li");
		 var scrollHeight = 0;
		 for(var i=0;i<listLi.length;i++){
			 var h = listLi[i].offsetHeight;
			 scrollHeight += h;
		 }
		 scrollHeight = parseInt(scrollHeight);
		 if((scrollTop+windowHeight)>scrollHeight&&currentPage!=-1){
		　　 lims.root.kmsItemNamePage+=1;
		 	$(listBox).scrollTop(scrollTop-25);
		 	var listDs = lims.getItemsNameDS(lims.root.kmsItemNamePage,keyword);
		 	if(listDs==null) return;
		 	if(listDs.length<1) lims.root.kmsItemNamePage = -1;
		 	for(var j=0;j<listDs.length;j++){
		 		$("#itemName").data("kendoAutoComplete").dataSource.add(listDs[j]);
		 	}
		 	$("#itemName").data("kendoAutoComplete").refresh();
		 }
	 };
	 	
	  /*将检测项目发回至KMS*/
	 fsn.sendItemsToKMS = function(){
	 	var testItems=$("#report_grid").data("kendoGrid").dataSource.data();
	 	var productN = $("#foodName").text().trim()==''?$("#foodName").val().trim():$("#foodName").text().trim();
	 	var busUnitName = $("#bus_name").val();
	 	var judgeStandard = $("#judgeStandard").val();
		var itemList = new Array();
		for(var i=0;i<testItems.length;i++){
			var data = {
					name : testItems[i].name,
					teststd : judgeStandard,
			};
			itemList.push(data);
		}
		var itemVo = {
				itemList:itemList,
				productName:productN,
				enterpriseName:busUnitName,
			}
		$.ajax({
			url: fsn.getKMSPrefix()+"/lims-standard-cloud/service/testitem/collect",
			type: "POST",
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(itemVo),
			success: function(dataValue){	
			}
		});
	 };
	 
	 /*指定多个按钮id，设置button为disabled*/
     lims.addDisabledToBtn=function(btnIds){
    	 if(btnIds==null||btnIds.length<1) return;
    	 for(var i=0;i<btnIds.length;i++){
    		 $("#"+btnIds[i]).parent().addClass("k-state-disabled");
    		 var element = document.getElementById(btnIds[i]);
    		 if(element){
    			 document.getElementById(btnIds[i]).disabled=true;
    		 }
    	 }
     };
     
     /*指定多个按钮id，移除button的disabled属性*/
     lims.removeDisabledToBtn=function(btnIds){
    	 if(btnIds==null||btnIds.length<1) return;
    	 for(var i=0;i<btnIds.length;i++){
    		 $("#"+btnIds[i]).parent().removeClass("k-state-disabled");
       		 document.getElementById(btnIds[i]).disabled=false;
    	 }
     };
	 
	 /*将执行标准数组转换为字符串*/
	 lims.convertRegularityToString = function(regularity){
	 	var regularityStr = "";
	 	if(!regularity){
	 		return regularityStr;
	 	}
        for (var i = 0; i < regularity.length; i++) {
			if(regularity[i].name==null||regularity[i].name==""){
				continue;
			}else{
				regularityStr = regularityStr + regularity[i].name + ";"
			}
        }
		return regularityStr;
	 }
	 
	  // 封装执行标准信息
	lims.convertRegularityToItem = function(regularityStr){
	    var listRegularity=[];
		var regularitys = regularityStr.split(";");
	    for(var i=0;i<regularitys.length-1;i++){
	    	listRegularity[i]={
	    		name:regularitys[i],
				category:{
					id: $("#category2").data("kendoComboBox").value(),
				},
				categoryOneCode:$("#category2").data("kendoComboBox").dataItem().code.substring(0,2)
	    	};
	    }
	    return listRegularity;
	 };
})(jQuery);
