$(document).ready(function() {
    var fsn = window.fsn = window.fsn || {};
    var business_unit = window.fsn.business_unit = window.fsn.business_unit || {};
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    var facility = window.fsn.facility = window.fsn.facility || {};
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    business_unit.id = null;
    business_unit.name = null;
    fsn.business_send = false;
    business_unit.editLic = null;
    business_unit.flagLic = false;
    business_unit.orgExistsFlag = false;
    business_unit.aryOrgAttachments = new Array();
    business_unit.aryLicenseAttachments = new Array();
    business_unit.aryQsAttachments = new Array();
    business_unit.aryLogoAttachments = new Array();
    /**
     * 定义一个存在企业宣传照的资源数组，保存的宣传照用于销售系统展示。
     * @author huangyong 2015-05-06
     */
    business_unit.aryPropagandaAttachments = new Array();
    /**
     * 定义一个存在企业二维码图片的资源数组，保存的二维码图片用于销售系统展示。
     * @author huangyong 2015-05-06
     */
    business_unit.aryQrAttachments = new Array();
    /**
     * 定义一个存在企业税务登记证图片的资源数组，保存的企业税务登记证图片用于销售系统展示。
     * @author huangyong 2015-05-06
     */
    business_unit.aryTaxAttachments = new Array();
    
    business_unit.certMap = new Map();
    business_unit.editBusiness = null;
    business_unit.formetType = "-"; //用于用户输入qs号的分隔
    business_unit.formetLength = 12; //用于限制qs号的输入长度
    business_unit.formetValue = "QS"; // qs号的字母部分
    business_unit.formetId = 1; //qs规则的默认id
    fsn.imgMax = 2; //页面引导图片id
    fsn.src = "http://qa.fsnrec.com/portal/guid/unit/unit_produce_"; //引导原地址
    
    business_unit.initialize = function() {
    	business_unit.initGrid("productionLic_1", business_unit.qsDS, business_unit.column_1, "toolbar_template_1");
    	business_unit.initGrid("productionLic", business_unit.qsDS, business_unit.column, "toolbar_template");
    	business_unit.initUpload();
    	business_unit.initWindow();
        business_unit.bindClick();
        
        $("#btn_clearOrgFiles").hide();
        $("#btn_clearLicenseFiles").hide();
        
        business_unit.initView();
        /* 初始化QS信息中企业名称选择控件 */
        business_unit.initQSInfoBusName();
        
        /**
         * 改变企业宣传照和二维码资源上传控件的状态
         * 1、企业宣传照只能上传一张，如果已经上传了资源时，将上传控件设置为disabled状态。
         * 2、企业二维码资源只能上传一张，如果已经上传了资源时，将上传控件设置为disabled状态。
         * @author tangxin 2015-05-14
         */
        changePropagandaAndQrcodeUploadStatus();
        
        /**
         * 初始化查看qs申请认领消息 Grid
         * @author ZhangHui 2015/6/11
         */
        initQsClaimMsgItemGrid("qs_applicant_grid");

        fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');

    };
    
    business_unit.initUpload = function(){
    	/* 初始化上传控件 */
        $("#upload_logo_div").html("<input id='upload_logo_files' type='file' />");
        business_unit.buildUpload("upload_logo_files", business_unit.aryLogoAttachments, "upload_logo_files_log");
        
        /**
         * 初始化企业宣传照上传控件
         * @author huangyong 2015-05-06
         */ 
        $("#upload_propaganda_div").html("<input id='upload_propaganda_files' type='file' />");
        business_unit.buildUpload("upload_propaganda_files", business_unit.aryPropagandaAttachments, "upload_propaganda_files_log");

        /**
         * 初始化企业二维码维码上传控件
         * @author huangyong 2015-05-06
         */
        $("#upload_qr_div").html("<input id='upload_qr_files' type='file' />");
        business_unit.buildUpload("upload_qr_files", business_unit.aryQrAttachments, "upload_qr_files_log");

        /**
         * 初始化企业税务登记证附件上传控件
         * @author huangyong 2015-05-06
         */
        $("#upload_tax_div").html("<input id='upload_tax_files' type='file' />");
        business_unit.buildUpload("upload_tax_files", business_unit.aryTaxAttachments, "upload_tax_files_log");
        

        $("#upload_license_div").html("<input id='upload_license_files' type='file' />");
        business_unit.buildUpload("upload_license_files", business_unit.aryLicenseAttachments, "upload_license_files_log");

        $("#upload_org_div").html("<input id='upload_orgnization_files' type='file' />");
        business_unit.buildUpload("upload_orgnization_files", business_unit.aryOrgAttachments, "upload_orgnization_files_log");

        $("#upload_certification_div").html("<input id='upload_certification_files' type='file' />");
        business_unit.buildUpload("upload_certification_files", business_unit.certMap, "upload_certification_files_log");
    };
    
    business_unit.initWindow = function(){
    	business_unit.initKendoWindow("k_window", "保存状态", "300px", "60px", false);
        business_unit.initKendoWindow("addProLicWindow", "", "1300px",  "auto", false);
        business_unit.initKendoWindow("PromptWindow", "删除提示框", "450px", "100px", false);
        fsn.initKendoWindow("qs_delete_confirm_window","友情提示","300px","190px",false, []);
        /**
         * 初始化企业其他认证信息上传窗体
         * @author huangyong 2015/5/7
         */
        business_unit.initKendoWindow("addCertWindow", "认证信息", "600px", "500px", false);
        /**
         * 初始化企业上传荣誉认证信息时查看示例窗体
         * @author huangyong 2015/5/7
         */
        business_unit.initKendoWindow("demo_win", "认证信息", "530px", "580px", false);
        
        /**
         * 初始化查看qs认领申请窗体
         * @author ZhangHui 2015/6/11
         */
        fsn.initKendoWindow("view_qs_applicant_msg_window","查看qs申请认领消息","1300px","500px",false, ["Close"]);
    };
    
    business_unit.bindClick = function(){
    	$("#saveInfove").bind("click", business_unit.saveInfove);
    	$("#saveCertificate").bind("click", business_unit.saveCertificate);
        $("#reset").bind("click", business_unit.reset);
        $("#addProLic").bind("click", business_unit.addProLic);
        $("#saveProLic").bind("click", business_unit.saveProLic);
        
        $("#btn_clearOrgFiles").bind("click", business_unit.clearOrgFiles);
        $("#btn_clearLicenseFiles").bind("click", business_unit.clearLicenseFiles);
        $("span[class='k-select']").bind("click", business_unit.changeComboboxStyle);
        
        /**
         * 绑定企业税务登记证清空资源按钮 click 事件
         * @author huangyong 2015/5/6
         */
        $("#btn_clearTaxFiles").bind("click", business_unit.clearTaxFiles);
        
        /**
         * 绑定企业宣传照清空资源按钮 click 事件
         * @author huangyong 2015/5/6
         */
        $("#btn_clearPropagandaFiles").bind("click", business_unit.clearPropagandaFiles);
        
        /**
         * 绑定企业二维码清空资源按钮 click 事件
         * @author huangyong 2015/5/6
         */
        $("#btn_clearQrFiles").bind("click", business_unit.clearQrFiles);
        
        /**
         * 绑定企业其他认证中标准认证清空资源按钮 click 事件
         * @author huangyong 2015/5/6
         */
        $("#btn_clearCertificationFiles").bind("click", business_unit.clearCertificationFiles);
        
        /**
         * 绑定企业其他认证中荣誉认证清空资源按钮 click 事件 （荣誉认证销售系统展示）
         * @author huangyong 2015/5/6
         */
        $("#btn_clearHonorIconFiles").bind("click", business_unit.clearHonorIconFiles);
        
        /**
         * qs权限删除确认提示框 按钮 Click事件绑定
         * @author ZhangHui 2015/5/22
         */
        $("#btn_qs_delete_yes_confirm").click(function(){
        	business_unit.executeDelQsInfo();
			$("#qs_delete_confirm_window").data("kendoWindow").close();
		});
		
		$("#btn_qs_delete_no_confirm").click(function(){
			$("#qs_delete_confirm_window").data("kendoWindow").close();
		});
        
        //失去焦点做提示
        $("#telephone").bind("blur", business_unit.validaTetelephone);
        $("#postalCode").bind("blur", business_unit.validaPostalCode);
        $("#email").bind("blur", business_unit.validateEmail);
        $("#bus_mainAddr").bind("blur",
        function() {
            verifyAddress("bus_mainAddr");
        });
        $("#license_mainAddr").bind("blur",
        function() {
            verifyAddress("license_mainAddr");
        });
        $("#org_mainAddr").bind("blur",
        function() {
            verifyAddress("org_mainAddr");
        });
        $("#accommodation_mainAddr").bind("blur",
        	function() {
            	verifyAddress("accommodation_mainAddr");
        });
        $("#qs_mainAddr").bind("blur",
        	function() {
            	verifyAddress("qs_mainAddr");
        });
        /*组织机构代码输入框失去焦点验证*/
        $("#orgCode").bind("blur",function() {
        	   var orgCode = $("#orgCode").val().trim();
        	   if(orgCode=="") return;
        	   if(business_unit.editBusiness==null) business_unit.editBusiness={};
        	   var orgId = business_unit.editBusiness.organization;
        	   business_unit.verificationOrgCode(orgCode,orgId);
        });
        
        $("#addProLicWindow").data("kendoWindow").bind("close", function(){
            $(".provinceCityAll").css("display","none"); //关闭选择地址的样式
       });//点击关闭窗体时关闭地址选择框
        
        /**
         * #qsNo 失去焦点后，验证当前登录企业有无使用或编辑该qs号的权限
         * @author ZhangHui 2015/5/22
         */
        $("#qsNo").bind("blur", business_unit.getRightOfThisQs);
        
        /**
         * 点击查看qs认领消息按钮后，打开消息提示窗口
         * @author ZhangHui 2015/6/11
         */
        $("#view_qs_applicant_msg_btn").bind("click", viewQsApplicantMsgs);
        /*$("#view_qs_applicant_msg_btn").click(function(){
			$("#view_qs_applicant_msg_window").data("kendoWindow").close();
		});*/
    };
    
	/**
	 * 功能描述：初始化qs认领申请请求处理grid
	 * @author ZhangHui 2015/6/11
	 */
	function initQsClaimMsgItemGrid (girdID){
		if(girdID){
			$("#" + girdID).kendoGrid({
				dataSource:[],
		        editable: false,
		        pageSize : 10,
		        pageable: {
		            refresh: true,
		            pageSizes: 5,
		            messages: fsn.gridPageMessage()
		        },
		        columns: [
		                  { field: "qsno", title:"生产许可证编号", width: 35 },
		                  { field: "applicant", title:"申请认领人",width: 35 },
		                  { field: "applicant_time", title:"申请认领时间", width: 30},
		                  { field: "handle_result", title:"系统处理状态", width: 25, template: function(dataItem) {
                              var statue = "";
                              switch (dataItem.handle_result){
                                  case 1: statue = "待处理" ;break;
                                  case 2: statue = "审核通过" ;break;
                                  case 4: statue = "<font color='red'>审核退回</font>" ;break;
                              }
	                    	  return statue;                    		 
		                  }},
		                  { field: "handle_time", title:"处理时间", width: 30},
		                  { field: "note", title:"处理意见", width: 50, template: function(dataItem) {
                              var msg = "";
                              switch (dataItem.handle_result){
                                  case 1: msg = "请您耐心等待，或者联系业务员！" ;break;
                                  case 2: msg = "已经通过审核，您可以正常使用！" ;break;
                                  case 4: msg = "<font color='red'>很抱歉，您的申请被退回。原因：" + dataItem.note + "</font>";break;
                              }
	                    	  return msg;
		                  }},
			             ]
			});
		}
	};
	
	/**
	 * 点击查看qs认领消息按钮后，打开消息提示窗口
	 * @author ZhangHui 2015-06-11
	 */
	function viewQsApplicantMsgs(){
		var itemDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		if(lims.easyViewReportID == 0){
	            			return;
	            		}
	            		return portal.HTTP_PREFIX + "/product/qsno/applicantClaimHandle/getmsgs/" + options.page + "/" + options.pageSize;
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
	                return returnValue.data.list;
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
		
		$("#qs_applicant_grid").data("kendoGrid").setDataSource(itemDS);
		
		// 打开window 窗口
		$("#view_qs_applicant_msg_window").data("kendoWindow").open().center();
	}
    
    /**
     * 改变企业宣传照和二维码资源上传控件的状态
     * 1、企业宣传照只能上传一张，如果已经上传了资源时，将上传控件设置为disabled状态。
     * 2、企业二维码资源只能上传一张，如果已经上传了资源时，将上传控件设置为disabled状态。
     * @author tangxin 2015-05-14
     */
    function changePropagandaAndQrcodeUploadStatus(){
    	// 企业宣传照资源集合
    	var pubRes = business_unit.aryPropagandaAttachments;

    	// 企业二维码资源集合
    	var qrcodeRes = business_unit.aryQrAttachments;
    	// 首先移除 上传控件的 disabled 属性
    	$("#upload_propaganda_files").removeAttr("disabled");
    	$("#upload_propaganda_div div.k-upload-button").removeClass("k-state-disabled");
    	$("#upload_qr_files").removeAttr("disabled");
    	$("#upload_qr_div div.k-upload-button").removeClass("k-state-disabled");
    	// 如果宣传照资源已经上传 ，则为上传控件添加 disabled 属性
    	if(pubRes != null && pubRes.length >= 3){
    		$("#upload_propaganda_files").attr("disabled","disabled");
    		$("#upload_propaganda_div div.k-upload-button").addClass("k-state-disabled");
    	}
    	// 如果二维码图片已经上传 ，则为上传控件添加 disabled 属性
    	if(qrcodeRes != null && qrcodeRes.length > 0){
    		$("#upload_qr_files").attr("disabled","disabled");
        	$("#upload_qr_div div.k-upload-button").addClass("k-state-disabled");
    	}
    }
    
    /**
     * 生产许可证grid 数据源
     * @author ZhangHui 2015/5/21
     */
    business_unit.qsDS = new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                async:false,
   	                url : function(options){
   	                	return portal.HTTP_PREFIX + "/product/qsno/list/myall/" + options.page + "/" + options.pageSize;
   	                },
   	                dataType : "json",
   	                contentType : "application/json"
   	            },
   	        },
   	        schema: {
   	        	 data : function(returnValue) {
   	        		 return returnValue.data.listOfQs;  //响应到页面的数据
   	             },
   	             total : function(returnValue) {
   	            	 return returnValue.data.counts;   //总条数
   	             }
   	        },
   	        batch : true,
   	        page:1,
   	        pageSize : 5, //每页显示个数
   	        serverPaging : true,
   	        serverFiltering : true,
   	        serverSorting : true
   		});
    
    business_unit.initGrid = function(formId, ds, column, toolbarId) {
        $("#" + formId).kendoGrid({
            dataSource: ds == null ? [] : ds,
            selectable: true,
            pageable: {
            	refresh: true,
                messages: lims.gridPageMessage()
            },
            toolbar: [{
                template: kendo.template($("#" + toolbarId).html())
            }],
            columns: column
        });
    };
    
    business_unit.column_1 = [{
        field: "qsno",
        title: "生产许可证编号",
        width: 60,
        height: 30
    },
    {
        field: "businessName",
        title: "生产企业名称",
        width: 90,
        height: 30
    },
    {
        field: "productName",
        title: "产品名称",
        width: 90,
        height: 30
    }];

    business_unit.column = [{
        field: "qsno",
        title: "生产许可证编号",
        width: 60,
        height: 30
    },
    {
        field: "businessName",
        title: "生产企业名称",
        width: 90,
        height: 30
    },
    {
        field: "productName",
        title: "产品名称",
        width: 90,
        height: 30
    },
    {width:50,title: fsn.l("Operation"),
	      template:function(e){
	    	var tag = "";
	    	if(e.can_eidt){
	    		// 可以编辑
                tag +="<a class=\"k-button k-button-icontext k-grid-edit\" onclick=\"fsn.business_unit.operateQsInfo('" + e.qsId + "','"+ e.qsno +"','" + e.claimed + "','edit')\">";
                tag += "<span class='k-icon k-i-search k-ViewDetail'></span>编辑</a>";
	    	}else{
	    		// 可以预览
	    		tag += "<a  class=\"k-button k-button-icontext k-grid-ViewDetail\" onclick=\"fsn.business_unit.operateQsInfo('" + e.qsId + "','" + e.qsno + "',null,'view')\" >" ;
                tag +='<span class="k-icon k-i-search k-ViewDetail"></span>预览</a>';
	    	}
	    	/*if(!e.claimed || e.local){*/
	    		// 可以删除
	    		tag += "<a class=\"k-button k-button-icontext k-grid-delete\" onclick=\"fsn.business_unit.delQsInfo('" + e.qsId + "','" + e.local + "')\" >" ;
                tag += "<span class='k-icon k-delete'> </span>删除</a>";
	    	/*}*/
			return tag;
	      }
    }];

    /**
     * 初始化页面
     */
    business_unit.initView = function() {
        isNew = false;
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/getBusinessByOrg",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(returnValue) {
                business_unit.editBusiness = returnValue.data;
                business_unit.setValue(returnValue.data, "produce");
                fsn.initialCertification(returnValue.data==undefined?[]:returnValue.data.listOfCertification);
            }
        });
    };
    
    /**
     * 点击添加按钮打开生产许可证窗口
     * @author ZhangHui 2015/5/25
     */
    business_unit.addProLic = function() {
    	fsn.business_unit.operateQsInfo(null, null, null, "add_new_record");
    };
    
    /**
     * 功能描述：qs编辑/预览
     * @param qsId 当前需要操作的qs Id
     * @param qsno 当前需要操作的qs号
     * @param operate 
     *           edit 代表 编辑
     *           view 代表 预览
     * @author ZhangHui 2015/5/20
     */
    fsn.business_unit.operateQsInfo = function(qsId, qsno, claimed, operate){
        if(qsId != null){
            $("#qs_id").val(qsId);
        }else{
            $("#qs_id").val("");
        }


    	$("#addProLicWindow").data("kendoWindow").open().center();
    	$("#qs_msg").hide();
    	$("#proFileMsgQs").html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
    	
    	business_unit.is_add_new_record_Of_qs = false;    // 记录当前操作是否为新增操作
    	business_unit.is_have_be_claimed_Of_qs = claimed; // 记录当前qs号是否已经被认领 
    	business_unit.old_edit_qsId = qsId;  // 记录当前qs号id
		business_unit.old_edit_qsno = qsno;  // 记录当前qs号
    	
		if(operate == "edit"){
    		business_unit.setQsInfoCanEdit();
    		// 生产许可证编号不允许编辑
    		$("#qsNo").attr("readonly", true);
    	}else if(operate == "add_new_record"){
    		business_unit.setQsInfoCanEdit();
    		business_unit.setQsInfoValue(null);
    		
    		// 默认值赋值
    		$("#busunitName").val(business_unit.editBusiness.name);  // 生产企业名称
    		setAddressValue(business_unit.editBusiness.address,business_unit.editBusiness.otherAddress,"qs_mainAddr","qs_streetAddress"); // 生产地址
    		setAddressValue(business_unit.editBusiness.address,business_unit.editBusiness.otherAddress,"accommodation_mainAddr","accommodation_streetAddress");  // 住所
    		
    		business_unit.is_add_new_record_Of_qs = true;
        	business_unit.aryQsAttachments.length = 0;
            $("#upload_qs_files_img_s").hide();
    	}
		
		// 获取qs详细信息（包括qs图片）
    	if(operate!="add_new_record" && qsId != null){
    		var currentQsInfo = null;
    		$.ajax({
    			url:portal.HTTP_PREFIX + "/product/qsno/getQsDetailInfo/" + qsId,
    			type:"GET",
    	        async:false,
    			success:function(returnValue){
    				if(returnValue.result.status == "true"){
    					currentQsInfo = returnValue.data;
    				}
    			}
    		});
    		business_unit.setQsInfoValue(currentQsInfo);
            if(currentQsInfo !=null){
                business_unit.qsAttachmentsImg(currentQsInfo);
            }

    	}


    	if(operate == "view"){
    		business_unit.setQsInfoReadOnly();
    	}
    };

    //显示qs图片
    business_unit.qsAttachmentsImg= function(data){
        // qsAttachments: business_unit.aryQsAttachments,
        if(data != null&&data.qsAttachments != null&&data.qsAttachments.length>0) {
            var imgData = data.qsAttachments;
            var img_a = "";
            //for(var i in imgData){
            for (var i = 0; i < imgData.length; i++) {
                img_a += "<div style='float: left;margin-left:10px;'><img id='show_qs_img" + i + "' src='" + imgData[0].url + "' style='width: 128px;height:128px;' style='display:block;' onclick='fsn.business_unit.amplification(this.src)'></div>"
            }
            $("#upload_qs_files_img_s").show();
            $("#upload_qs_files_img").html(img_a);
        }
    };
    /**
     * qs新增/编辑时，移除input readonly
     * @author ZhangHui 2015/5/25
     */
    business_unit.setQsInfoCanEdit = function(){
    	$("#qsFname").data("kendoDropDownList").enable();
    	$("#qsNo").removeAttr("readonly");
    	$("#busunitName").removeAttr("readonly");
    	$("#productName").data("kendoDropDownList").enable();
    	$("#qsStartTime").data("kendoDatePicker").readonly(false);
    	$("#qsEndTime").data("kendoDatePicker").readonly(false);
    	$("#accommodation_mainAddr").removeAttr("disabled");
    	$("#accommodation_streetAddress").removeAttr("readonly");
    	$("#qs_mainAddr").removeAttr("disabled");
    	$("#qs_streetAddress").removeAttr("readonly");
    	$("#checkType").removeAttr("readonly");
    	
    	$("#proFileMsgQs").show();
    	$("#upload_qs_div").show();
    	$("#saveProLic").show();
    };
    
    /**
     * qs预览时，将页面输入框设置为只读
     * @author ZhangHui 2015/5/25
     */
    business_unit.setQsInfoReadOnly = function(){
    	$("#qsFname").data("kendoDropDownList").enable(false);
    	$("#qsNo").attr("readonly", true);
    	$("#busunitName").attr("readonly", true);
    	$("#productName").data("kendoDropDownList").enable(false);
    	$("#qsStartTime").data("kendoDatePicker").readonly(true);
    	$("#qsEndTime").data("kendoDatePicker").readonly(true);
    	$("#accommodation_mainAddr").attr("disabled", "disabled");
    	$("#accommodation_streetAddress").attr("readonly", true);
    	$("#qs_mainAddr").attr("disabled", "disabled");
    	$("#qs_streetAddress").attr("readonly", true);
    	$("#checkType").attr("readonly", true);
    	
    	$("#proFileMsgQs").hide();
    	$("#upload_qs_div").hide();
    	$("#qsAttachmentsListView button").hide();
    	$("#saveProLic").hide();
    };
    
    /**
     * 点击编辑按钮查看生产许可证信息
     */
    business_unit.setQsInfoValue = function(data) {
        business_unit.changeComboboxStyle();
        var qsFormat = $("#qsFname").data("kendoDropDownList");
        qsFormat.value((data!=null&&data.qsnoFormat!=null) ? data.qsnoFormat.id: 1);
        var item = qsFormat.dataItem();
        business_unit.formetType = item.formetType;
        business_unit.formetLength = item.formetLength;
        business_unit.formetValue = item.formetValue;
        $("#showQsFormat").html(item.formetValue);
        $("#qsNo").val(data!=null?data.qsNo.replace(item.formetValue, ""):"");
        $("#productName").data("kendoDropDownList").value(data!=null?data.productName:"");
        $("#qsStartTime").data("kendoDatePicker").value((data!=null&&data.startTime!=null) ? data.startTime.substr(0, 10) : "");
        $("#qsEndTime").data("kendoDatePicker").value((data!=null&&data.endTime!=null) ? data.endTime.substr(0, 10) : "");
        setAddressValue(data!=null?data.accommodation:" ", data!=null?data.accOtherAddress:" ", "accommodation_mainAddr", "accommodation_streetAddress");
        setAddressValue(data!=null?data.productionAddress:" ", data!=null?data.proOtherAddress:" ", "qs_mainAddr", "qs_streetAddress");
        $("#checkType").val(data!=null?data.checkType:"");
        business_unit.setQsAttachments(data!=null?data.qsAttachments:[]);
        
        if(data != null){
        	$("#busunitName").val(data.busunitName);
        	if(business_unit.is_have_be_claimed_Of_qs && !business_unit.is_add_new_record_Of_qs){
        		// 编辑状态下：当该qs号已经被认领，则不允许改变qs编号、生产企业名称、也不能更换图片
        		$("#qs_msg").show();
        		$("#qs_msg").html("<font class='necessary'>当前生产许可证，已经通过审核，您不能修改证书编号和生产企业名称，也不能更换图片！</font>");
        		
        		$("#qsFname").data("kendoDropDownList").enable(false);
        		$("#busunitName").attr("readonly", true);
        		$("#proFileMsgQs").hide();
        		$("#upload_qs_div").hide();
            	$("#qsAttachmentsListView button").hide();
        	}
        }else{
        	$("#busunitName").val("");
        }
    };
    
    /**
     * 前提：当前登录企业是该qs号的主人
     * 功能描述：qs删除
     * @author ZhangHui 2015/5/22
     */
    fsn.business_unit.delQsInfo = function(qsId, local){
    	business_unit.del_qsId = qsId;
    	
    	var isAuthorized = false;
    	
    	if(local){
    		/**
        	 * 需要判断有无給其他企业授权
        	 */
        	isAuthorized = business_unit.isHaveToOtherAuthorized(qsId);
    	}
    	
    	if(!isAuthorized){
    		$("#qs_delete_confirm_msg").html("<br>您确认删除吗？");
    	}
    	
    	$("#qs_delete_confirm_window").data("kendoWindow").open().center();
    };
    
    /**
     * 功能描述：正式执行删除qs号操作
     * @author ZhangHui 2015/5/22
     */
    business_unit.executeDelQsInfo = function(){
    	$.ajax({
			url:portal.HTTP_PREFIX + "/product/qsno/delQsInfo/" + business_unit.editBusiness.id + "/" + business_unit.del_qsId,
			type:"GET",
	        async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					business_unit.qsDS.read();
                	fsn.initNotificationMes("删除成功！", true);
				}
			}
		});
    };
    
    /**
	 * 功能描述：验证除了自己的主人外，当前qs号有无给其他企业授权
	 * @author ZhangHui 2015/5/19
	 */
    business_unit.isHaveToOtherAuthorized = function(qsId){
		var isAuthorized = false;
		$.ajax({
			url: portal.HTTP_PREFIX + "/product/qsno/isHaveToOtherAuthorized/" + qsId,
			type:"GET",
	        async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					isAuthorized = returnValue.data;
				}
			}
		});
		return isAuthorized;
	};
	
	/**
	 * 验证当前登录企业有无使用或编辑该qs号的权限
	 * @author ZhangHui 2015/5/25
	 */
	business_unit.getRightOfThisQs = function(){
		// 验证qs号格式
		var pass = business_unit.validate_qsno_format();
		if(!pass){
			$("#saveProLic").hide();
			return;
		}
		
		var qsno = $("#showQsFormat").html() + $("#qsNo").val().trim();
		/*if(business_unit.old_edit_qsno == qsno){
			return;
		}*/
		
		$("#saveProLic").show();
		$.ajax({
			url: portal.HTTP_PREFIX + "/product/qsno/getRightOfQs/" + qsno,
			type:"GET",
	        async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					var vo = returnValue.right_vo;
					var qs_info = returnValue.qs_info;
					
					business_unit.is_have_be_claimed_Of_qs = false;
					
					// 1 该qs号没有被任何企业认领(新增状态下)
					if(vo!=null && vo.claimed==false && business_unit.is_add_new_record_Of_qs){
						if(vo.local==true){
							// 1.1 但当前登录企业有该qs号的使用权
							if(business_unit.is_add_new_record_Of_qs){
								fsn.initNotificationMes("当前qs号已经存在于您的列表中，请勿重复添加", false);
								// 隐藏保存按钮
								$("#saveProLic").hide();
								return;
							}
						}else if(vo.applicant_vo != null){
							// 1.2 但当前登录企业在此之前已经申请认领过过该qs号
							if(vo.applicant_vo.handle_result == 1){
								fsn.initNotificationMes("您在 " + vo.applicant_vo.applicant_time + " 已经申请认领当前qs号，请耐心等待系统审核！", false);
								// 隐藏保存按钮
								$("#saveProLic").hide();
								return;
							}else if(vo.applicant_vo.handle_result == 4){
								fsn.initNotificationMes("您在 " + vo.applicant_vo.applicant_time + " 申请认领当前qs号，但被系统审核退回，具体原因为：[" + vo.applicant_vo.note + "]！", false);
							}
						}
					}
					
					// 3 该qs号被当前登录企业认领
					if(vo!=null && vo.claimed==true && vo.local==true){
						business_unit.is_have_be_claimed_Of_qs = true;
						if(business_unit.is_add_new_record_Of_qs){
							// 当前状态为：新增
							fsn.initNotificationMes("当前qs号已经存在于您的列表中，请勿重复添加", false);
							// 隐藏保存按钮
							$("#saveProLic").hide();
							return;
						}else{
							// 当前状态为：编辑
							if(business_unit.old_edit_qsno != qsno){
								fsn.initNotificationMes("当前qs号已经存在于您的列表中，请勿重复添加", false);
								// 隐藏保存按钮
								$("#saveProLic").hide();
								return;
							}
						}
					}
					
					// 4 该qs号已经被其他企业认领，且不存在 当前登录企业-qs号  关系
					if(vo!=null && vo.claimed==true && vo.local==false && vo.id==null){
						business_unit.is_have_be_claimed_Of_qs = true;
						fsn.initNotificationMes("当前qs号属于企业[" + vo.businessName + "]，您没有权限使用！", false);
						// 隐藏保存按钮
						$("#saveProLic").hide();
						return;
					}
					
					// 5 该qs号已经被其他企业认领，且存在  当前登录企业-qs号 关系 
					if(vo!=null && vo.claimed==true && vo.local==false && vo.id!=null){
						business_unit.is_have_be_claimed_Of_qs = true;
						if(business_unit.is_add_new_record_Of_qs){
							// 当前状态为：新增
							fsn.initNotificationMes("当前qs号已经存在于您的列表中，请勿重复添加", false);
							// 隐藏保存按钮
							$("#saveProLic").hide();
							return;
						}else{
							// 当前状态为：编辑
							if(business_unit.old_edit_qsno != qsno){
								fsn.initNotificationMes("当前qs号已经存在于您的列表中，请勿重复添加", false);
								// 隐藏保存按钮
								$("#saveProLic").hide();
								return;
							}
						}
					}
					
					if(qs_info != null){
						// 页面赋值操作
						business_unit.setQsInfoValue(qs_info);
					}
					
					if(business_unit.is_add_new_record_Of_qs && qs_info!=null){
						business_unit.old_edit_qsId = qs_info.id;
					}
					
					// 如果当前qs未被任何企业认领，检查当前企业有无提交认领申请
					if(vo!=null && vo.claimed==false && qs_info != null){
						$.ajax({
			                url: portal.HTTP_PREFIX + "/product/qsno/applicantClaimHandle/checkhasApplicant/" + qs_info.id + "/" + business_unit.editBusiness.id,
			                type: "GET",
			                dataType: "json",
			                async: false,
			                success: function(returnValue){
			                    if (returnValue.result.success) {
			                    	if(returnValue.data!=null && returnValue.data.handle_result == 1){
			                    		fsn.initNotificationMes("贵公司于 " + returnValue.data.applicant_time + "已申请，系统正在审核中，请耐心等候！", true);
			                    		// 隐藏保存按钮
										$("#saveProLic").hide();
			                    		return;
			                    	}
			                    }
			                }
			            });
					}
				}
			}
		});
	};
    	
    /**
     * 保存生产许可证信息
     * @author ZhangHui 2015/5/21
     */
    business_unit.saveProLic = function() {
    	// 保存qs号按钮，解除点击事件，避免用户二次点击
    	$("#saveProLic").unbind("click");
    	
    	// 弹出正在保存的提示窗口
    	$("#k_window").data("kendoWindow").open().center();
    	
    	// 重新绑定点击事件
    	$("#saveProLic").bind("click", business_unit.saveProLic);
    	
        if (!business_unit.validate_DataFormate_of_qs()) {
        	$("#k_window").data("kendoWindow").close();
            return;
        }
        
        var qs_formatId = $("#qsFname").data("kendoDropDownList")._old;
        var qsnoFormat = {
            id: qs_formatId,
        };
        
        var productionLicense = {
        		id: business_unit.is_add_new_record_Of_qs?null:business_unit.old_edit_qsId,
                qsNo: $("#showQsFormat").html() + $("#qsNo").val().trim(),
                busunitName: $("#busunitName").val().trim(),
                productName: $("#productName").data("kendoDropDownList").value().trim(),
                startTime: $("#qsStartTime").val().trim(),
                endTime: $("#qsEndTime").val().trim(),
                accommodation: $("#accommodation_mainAddr").val().trim().replace(/-/g, "") + $("#accommodation_streetAddress").val().trim(),
                accOtherAddress: $("#accommodation_mainAddr").val().trim() + "--" + $("#accommodation_streetAddress").val().trim(),
                productionAddress: $("#qs_mainAddr").val().trim().replace(/-/g, "") + $("#qs_streetAddress").val().trim(),
                proOtherAddress: $("#qs_mainAddr").val().trim() + "--" + $("#qs_streetAddress").val().trim(),
                checkType: $("#checkType").val().trim(),
                qsAttachments: business_unit.aryQsAttachments,
                qsnoFormat: qsnoFormat,
                bussinessId: business_unit.editBusiness.id,
        };
        
        // 如果qs号的生产企业名称与当前登录企业的名称一致，且该qs号并未被任何企业认领，则进入审核环节
        if(productionLicense.busunitName==business_unit.editBusiness.name.trim() && !business_unit.is_have_be_claimed_Of_qs){
        	productionLicense.id = business_unit.old_edit_qsId;
        	
        	$.ajax({
                url: portal.HTTP_PREFIX + "/product/qsno/sendApplicantOfClaimQs/" + business_unit.editBusiness.id,
                type: "PUT",
                dataType: "json", 
                async:false,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(productionLicense),
                success: function(returnValue) {
                	$("#k_window").data("kendoWindow").close();
                    if (returnValue.result.status == "true") {
                    	$("#addProLicWindow").data("kendoWindow").close();
                    	
                    	if(!returnValue.data){
                    		fsn.initNotificationMes("您的申请已成功投递给相关审核人员，请耐心等待！", true);
                    		return;
                    	}
                    	
                    	if(returnValue.data.handle_result == 1){
                    		fsn.initNotificationMes("贵公司于 " + returnValue.data.applicant_time + "已申请，系统正在审核中，请耐心等候！", true);
                    		return;
                    	}else if(returnValue.data.handle_result == 2){
                    		fsn.initNotificationMes("您的申请于 " + returnValue.data.handle_time + " 通过审核!", true);
                    		return;
                    	}
                    }else{
                    	fsn.initNotificationMes("您的申请投递失败，请联系客服！", false);
                    }
                }
            });
        	return;
        }
        
        // 否则，直接保存qs信息
        $.ajax({
            url: portal.HTTP_PREFIX + "/product/qsno/saveQsInfo/" + business_unit.editBusiness.name,
            type: "PUT",
            dataType: "json", 
            async:false,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(productionLicense),
            success: function(returnValue) {
            	$("#k_window").data("kendoWindow").close();
                if (returnValue.result.status == "true") {
                	$("#addProLicWindow").data("kendoWindow").close();
                	business_unit.qsDS.read();
                	fsn.initNotificationMes("生产许可证信息" + (business_unit.is_add_new_record_Of_qs?"新增":"更新") + "成功！", true);
                }else{
                	fsn.initNotificationMes("生产许可证信息" + (business_unit.is_add_new_record_Of_qs?"新增":"更新") + "失败！", false);
                }
            }
        });
    };

    function Cert() {
        var setName = function(name) {
            this.name = name;
        };
        this.name;
        this.setName = setName;
    }
    function CertResource() {
        var set = function(name, url) {
            this.name = name;
            this.url = url;
        };
        this.name;
        this.url;
        this.set = set;
    }

    // 定义一个map集合，用来存放认证信息的图片
    function Map() {
        var struct = function(key, value) {
            this.key = key;
            this.value = value;
        };
        var put = function(key, value) {
            for (var i = 0; i < this.arr.length; i++) {
                if (this.arr[i].key === key) {
                    this.arr[i].value = value;
                    return;
                }
            };
            this.arr[this.arr.length] = new struct(key, value);
        };
        var get = function(key) {
            for (var i = 0; i < this.arr.length; i++) {
                if (this.arr[i].key === key) {
                    return this.arr[i].value;
                }
            }
            return null;
        };
        var remove = function(key) {
            var v;
            for (var i = 0; i < this.arr.length; i++) {
                v = this.arr.pop();
                if (v.key === key) {
                    continue;
                }
                this.arr.unshift(v);
            }
        };
        var size = function() {
            return this.arr.length;
        };
        var isEmpty = function() {
            return this.arr.length <= 0;
        };
        this.arr = new Array();
        this.get = get;
        this.put = put;
        this.remove = remove;
        this.size = size;
        this.isEmpty = isEmpty;
    }

    //改变qs号格式选择的下拉列表
    business_unit.changeComboboxStyle = function() {

        var qsFname_list = document.getElementById("qsFname-list");
        $("#qsFname-list").animate({
            width: '300px',
        });
        //qsFname_list.style.width="280px";
        var qsFname_listbox = document.getElementById("qsFname_listbox");
        qsFname_listbox.style.width = "300px";
    }

    /**
     * 封装企业其他认证信息
     * return Array
     */
    business_unit.getCerts = function () {
        var busUnitCerts = []; // 定义一个返回结果集合
        // 获取 认证信息Grid中的数据
        var certs = $("#certification-grid").data("kendoGrid").dataSource.data();
        var j = 0; //定义一个辅助变量，用来记录不符合保存条件（认证名称为空）的 认证信息条数
        // 遍历页面需要保存的认证信息集合
        for (var i = 0; i < certs.length; i++) {
            if (certs[i].name == "") {
                j++; // 认证信息名称为空，记录值 加 1,进入下一次循环
                continue;
            }
            // 标准认证资源
            var res = certs[i].certResource;
            // 标准认证信息实体
            busUnitCerts[i - j] = certs[i];
            // 对标准认证资源进行深度赋值到返回结果集中
            busUnitCerts[i - j].certResource = {
                id:res.id,
                fileName:res.fileName,
                name:res.fileName,
                url:res.url,
                type:res.type,
                file:res.file
            };
            // 荣誉认证资源
            var iconRes = certs[i].cert.certIconResource;
            // 对标准认证资源进行深度赋值到返回结果集中 cert.stdStatus 值说明，1 代码是荣誉认证，由用户自己上传，0 代表是标准认证，基础数据
            if(busUnitCerts[i - j].cert.stdStatus == 1 && iconRes != null){
                busUnitCerts[i - j].cert.certIconResource = {
                    id:iconRes.id,
                    fileName:iconRes.fileName,
                    name:iconRes.fileName,
                    url:iconRes.url,
                    type:iconRes.type,
                    file:iconRes.file
                };
            } else {
            	// 将荣誉认证资源设置为空 
            	busUnitCerts[i - j].cert.certIconResource = null;
            }
        }
        return busUnitCerts;
    };

    business_unit.createInstance = function() {

        /* 营业执照信息  */
        var license = {
            licenseNo: $("#licenseNo").val().trim(),
            licensename: $("#licenseName").val().trim(),
            legalName: $("#legalName").val().trim(),
            startTime: $("#licenseStartTime").val().trim(),
            endTime: $("#licenseEndTime").val().trim(),
            registrationTime: $("#registrationTime").val().trim(),
            issuingAuthority: $("#issuingAuthority").val().trim(),
            subjectType: $("#subjectType").val().trim(),
            businessAddress: $("#license_mainAddr").val().trim().replace(/-/g, "") + $("#license_streetAddress").val().trim(),
            otherAddress: $("#license_mainAddr").val().trim() + "--" + $("#license_streetAddress").val().trim(),
            toleranceRange: $("#toleranceRange").val().trim(),
            registeredCapital: $("#registeredCapital").val().trim(),
        };
        /* 组织机构代码信息  */
        var orgInstitution = {
            orgCode: $("#orgCode").val().trim(),
            orgName: $("#orgName").val().trim(),
            startTime: $("#orgStartTime").val().trim(),
            endTime: $("#orgEndTime").val().trim(),
            unitsAwarded: $("#unitsAwarded").val().trim(),
            orgType: $("#orgType").val().trim(),
            orgAddress: $("#org_mainAddr").val().trim().replace(/-/g, "") + $("#org_streetAddress").val().trim(),
            otherAddress: $("#org_mainAddr").val().trim() + "--" + $("#org_streetAddress").val().trim(),
        };

        /* 税务登记证VO */
        var taxRegisterVO = {
                id: $("#taxId").val().trim()!=""?$("#taxId").val().trim():null,
                taxerName: $("#taxName").val().trim()
            };
        var subBusiness = {
            id: business_unit.id,
            name: $("#name").val().trim(),
            address: $("#bus_mainAddr").val().trim().replace(/-/g, "") + $("#bus_streetAddress").val().trim(),
            otherAddress: $("#bus_mainAddr").val().trim() + "--" + $("#bus_streetAddress").val().trim(),
            personInCharge: $("#personInCharge").val().trim(),
            contact: $("#contact").val().trim(),
            telephone: $("#telephone").val().trim(),
            postalCode: $("#postalCode").val().trim(),
            email: $("#email").val().trim(),
            fax: $("#fax").val().trim(),
            about: $("#product_about").val().trim(),
            website: $("#website").val().trim(),
            license: license,
            orgInstitution: orgInstitution,
            logoAttachments: business_unit.aryLogoAttachments, //企业logo
            orgAttachments: business_unit.aryOrgAttachments,
            licAttachments: business_unit.aryLicenseAttachments,
            listOfCertification: business_unit.getCerts(),
            liquorAttachments: null,
            propagandaAttachments: business_unit.aryPropagandaAttachments,// 企业宣传照
            qrAttachments: business_unit.aryQrAttachments, // 二维码图片
            taxRegAttachments: business_unit.aryTaxAttachments, // 税务登记证图片
            taxRegister: taxRegisterVO //税务登记证vo
        };
        return subBusiness;
    };

    business_unit.saveCertificate = function() {
        business_unit.save(3);
        //var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
        //    tabStrip.select(2);        // Select by jQuery selector

    }
    business_unit.saveInfove  = function(){
        business_unit.save(1);

    };
    /* 保存 */
    business_unit.save = function(status) {
        /* 校验数据的有效性  */
        fsn.clearErrorStyle();
        if (!business_unit.validateFormat()) {
            return;
        }
        // 数据封装
        var subBusiness = business_unit.createInstance();
        $("#k_window").data("kendoWindow").open().center();
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/business-unit",
            type: "PUT",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(subBusiness),
            success: function(returnVal) {
                $("#k_window").data("kendoWindow").close();
                if (returnVal.result.status == "true") {

                    fsn.initNotificationMes("企业信息修改成功", true);

                    //business_unit.setOrgAttachments(returnVal.data.orgAttachments);
                    //business_unit.setLicenseAttachments(returnVal.data.licAttachments);
                    //business_unit.setQsAttachments(returnVal.data.qsAttachments);
                    //business_unit.setLogoAttachments(returnVal.data.logoAttachments);
                    //// 给企业宣传照 listView 赋值
                    //business_unit.setPropagandaAttachments(returnVal.data.propagandaAttachments);
                    //// 给企业二维码资源的 listView 赋值
                    //business_unit.setQrAttachments(returnVal.data.qrAttachments);
                    //// 给企业税务登记证的 listView 赋值
                    //business_unit.setTaxAttachments(returnVal.data.taxRegAttachments);

                    $("#productionLic").data("kendoGrid").dataSource.data(returnVal.data.productionLicenses);
                    $("#productionLic").data("kendoGrid").refresh();
                    fsn.initialCertification(returnVal.data==undefined?[]:returnVal.data.listOfCertification);
                    /**
                     * 改变企业宣传照和二维码资源上传控件的状态
                     * 1、企业宣传照只能上传一张，如果已经上传了资源时，将上传控件设置为disabled状态。
                     * 2、企业二维码资源只能上传一张，如果已经上传了资源时，将上传控件设置为disabled状态。
                     * @author tangxin 2015-05-14
                     */
                    changePropagandaAndQrcodeUploadStatus();

                    //var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
                    //var index = tabStrip.select().index();
                    //      index = index+1
                    //      tabStrip.select(index);        // Select by jQuery selector

                    //保存成功后，重新初始化页面
                    //business_unit.initView();
                    business_unit.editBusiness = returnVal.data;
                    facility.editBusinessInfo(status);
                } else {
                    fsn.initNotificationMes("企业信息修改失败", false);
                }
            }
        });
    };

    /* 校验数据的有效性 */
    business_unit.validateFormat = function() {
        if (!business_unit.validateCommon()) {
            return false;
        }
        /* 验证企业税务登记证 */
        if(!business_unit.validaTax()){
            return false;
        }
        /* 验证企业宣传照数量最多3张 */
        if(business_unit.aryPropagandaAttachments.length>3){
        	lims.initNotificationMes('宣传照最多能传3张，请删掉多余的！',false);
            return false;
        }
        return true;
    };

    /**
     * 验证税务登记证信息
     * 税务登记证信息信息本身非必填，如果用户填写了纳税人名称而为上传附件，则应提示 请上传税务登记证
     * 如果用户只上传附件而没有填写纳税人名称，则应提示 请填写纳税人名称
     * @author huangyong 2015/05/07
     */
    business_unit.validaTax = function(){
    	// 纳税人名称
    	var taxName = $("#taxName").val().trim();
    	// 如果用户填写了纳税人名称而为上传附件，则应提示 请上传税务登记证
        if(taxName != "" && business_unit.aryTaxAttachments.length < 1){
            lims.initNotificationMes("请上传税务登记证",false);
            return false;
        }
        // 如果用户只上传附件而没有填写纳税人名称，则应提示 请填写纳税人名称
        if(business_unit.aryTaxAttachments.length > 0 && taxName == ""){
            lims.initNotificationMes("请填写纳税人名称",false);
            return false;
        };
        return true;
    };
    
    business_unit.validate_DataFormate_of_qs = function() {
    	var firstPart = $("#showQsFormat").html().indexOf("?");
        if (firstPart != -1) {
            fsn.initNotificationMes("您选择的输入规则没有加载出正确的省份简称，请在企业基本信息中完善企业地址保存后再试！", false);
            return false;
        }
    	
        var pass = business_unit.validate_qsno_format();
        if(!pass){
        	return false;
        }

        if ($("#busunitName").val().trim() == "") {
            fsn.initNotificationMes('【生产许可证信息】中的企业名称不能为空！', false);
            return false;
        }
        if ($("#productName").val().trim() == "" || $("#productName").val().trim() == "--请选择分类--") {
            fsn.initNotificationMes('【生产许可证信息】中的产品名称不能为空！', false);
            return false;
        }
        if ($("#qs_mainAddr").val().trim() == "") {
            fsn.initNotificationMes('【生产许可证信息】中的生产地址不能为空！', false);
            return false;
        }
        if ($("#qs_streetAddress").val().trim() == "") {
            fsn.initNotificationMes('【生产许可证信息】中的生产地址的街道地址不能为空！', false);
            return false;
        }
        if (!fsn.validateMustDate("qsStartTime", "生产许可证的起始日期")) {
            return false;
        }
        if (!fsn.validateMustDate("qsEndTime", "生产许可证的截止日期")) {
            return false;
        }
        var startDate = $("#qsStartTime").data("kendoDatePicker").value();
        var endDate = $("#qsEndTime").data("kendoDatePicker").value();
        if ((endDate - startDate) < 1) {
            fsn.initNotificationMes('生产许可证的起始日期不能大于或等于截止日期！', false);
            return false;
        }
        
        // 当且仅当qs号对应的生产企业名称和当前登录企业名称一致时，产品图片才必须上传
        var qs_busunitName = $("#busunitName").val().trim();
        if (qs_busunitName==business_unit.editBusiness.name.trim() && business_unit.aryQsAttachments.length < 1) {
            fsn.initNotificationMes('检测到当前qs号是您的qs号，请您务必上传生产许可证图片，以便于我们审核！', false);
            return false;
        }

        return true;
    };
    
    /**
     * 验证 input #qsno 格式是否正确
     * @author ZhangHui 2015/5/25
     */
    business_unit.validate_qsno_format = function(){
    	if ($("#qsNo").val().trim() == "") {
            fsn.initNotificationMes('【生产许可证信息】中的证书编号不能为空！', false);
            return false;
        }
        if ($("#qsFname").val().trim() == "") {
            fsn.initNotificationMes('【生产许可证信息】中的证书编号填写格式不正确！', false);
            return false;
        }

        var qsNo = ($("#qsNo").val().trim()).replace(/\s*/g, "").replace(/-/g, "").replace(/\s+/g, "");
        var formatlength = business_unit.formetLength;
        var status = /^[0-9]*$/.test(qsNo);
        
        if (!status) {
            fsn.initNotificationMes("【生产许可证信息】中的证书编号格式不正确！。", false);
            return false;
        }
        if (qsNo.length != formatlength) {
            fsn.initNotificationMes("【生产许可证信息】中的证书编号格式不正确！。", false);
            return false;
        }
        return true;
    };
    
    //格式化qs号码
    business_unit.formatQsNo = function(obj, value) {
        var result = [];
        $("#qsNo").attr("maxlength", business_unit.formetLength + 2);
        var type = business_unit.formetType;
        switch (business_unit.formetLength) {
        case 12:
            // 格式：QSxx-xxxxx-xxxxx  2-5-5
            for (var i = 0; i < value.length; i++) {
                if (type == "-") {
                    var qs_No = $("#qsNo").val();
                    lastChar = qs_No.substring(qs_No.length - 1);
                    if (i == 2 && $("#qsNo").val().trim().length == 3 && lastChar != "-") {
                        result.push(type + value.charAt(i));
                    } else if (i == 8 && $("#qsNo").val().trim().length == 9 && lastChar != "-") {
                        result.push(type + value.charAt(i));
                    } else {
                        result.push(value.charAt(i));
                    }
                } else {
                    value = value.replace(/\s*/g, "");
                    if (i % 4 == 0 && i != 0) {
                        result.push(type + value.charAt(i));
                    } else {
                        result.push(value.charAt(i));
                    }
                }
            }
            obj.value = result.join("");
            break;
        case 10:
            //格式：XKxx-xxx-xxxxx)
            for (var i = 0; i < value.length; i++) {
                var qs_No = $("#qsNo").val();
                lastChar = qs_No.substring(qs_No.length - 1);
                if (i == 2 && $("#qsNo").val().trim().length == 3 && lastChar != "-") {
                    result.push(type + value.charAt(i));
                } else if (i == 6 && $("#qsNo").val().trim().length == 7 && lastChar != "-") {
                    result.push(type + value.charAt(i));
                } else {
                    result.push(value.charAt(i));
                }
            }
            obj.value = result.join("");
            break;
        default:
            ;
        }
    }

    //加载产品分类下拉列表的 DataSource
    business_unit.listCategoryDataSource = function(url) {
        return new kendo.data.DataSource({
            transport: {
                read: {
                    type: "GET",
                    dataType: "json",
                    async: true,
                    contentType: "application/json",
                    url: portal.HTTP_PREFIX + url
                }
            },
            schema: {
                data: function(d) {
                    return d.data;
                }
            }
        });
    }

    //将用户选择的信息填到input框中
    business_unit.onSelectProName = function(e) {
        var productName = this.dataItem(e.item.index());
        $("#productName").data("kendoDropDownList").value(productName);
    }

    business_unit.changeQsFormat = function(e) {
        var format = this.dataItem(e.item.index());
        var oldValue = $("#qsFname").data("kendoDropDownList").value();
   if(oldValue!=format.id){
            $("#qsNo").val("");
        }
        var formatId = format.id;
        $("#showQsFormat").html(format.formetValue);
        business_unit.formetType = format.formetType;
        business_unit.formetValue = format.formetValue;
        business_unit.formetLength = format.formetLength;
    }

    business_unit.listCategory = business_unit.listCategoryDataSource("/product/loadProduCtcategory"); // 加载产品名称分类
    business_unit.listFormatQs = business_unit.listCategoryDataSource("/product/loadlistFormatqs"); // 加载企业qs号绑定标准
    //初始化初始化产品名称分类的下啦列表    
    $("#productName").kendoDropDownList({
        dataTextField: "name",
        dataValueField: "name",
        dataSource: business_unit.listCategory != null ? business_unit.listCategory: [],
        optionLabel: "--请选择分类--",
        select: business_unit.onSelectProName,
    });

    //初始化初始化qs格式选择的下啦列表      
    $("#qsFname").kendoDropDownList({
        dataTextField: "formetName",
        dataValueField: "id",
        dataSource: business_unit.listFormatQs != null ? business_unit.listFormatQs: [],
        select: business_unit.changeQsFormat,
    });

    /* 初始化时间控件  */
    $("#licenseStartTime,#licenseEndTime,#orgStartTime,#orgEndTime," +
    		"#registrationTime,#qsStartTime,#qsEndTime,#test_time_format,#certDate").kendoDatePicker({
        format: "yyyy-MM-dd",
        height: 30,
        culture: "zh-CN",
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

    /*其他认证信息的截止日期*/
    fsn.buildDatePicker = function(container, options) {
        var input = $("<input/>");
        input.attr("name", options.field);
        input.appendTo(container);
        input.kendoDatePicker({
            format: "yyyy-MM-dd",
            height: 30,
            value: options.model.endDate == "" ? null: new Date(options.model.endDate),
            culture: "zh-CN",
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

    /*验证认证信息关联的产品数量*/
    fsn.validateBusUnitCert = function(busCertId) {
        var count = -1;
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/countProductByBusinessCertId/" + busCertId,
            type: "GET",
            async: false,
            success: function(data) {
                if (data.result.status == "true") {
                    count = data.count;
                    if (count > 0) {
                        fsn.initNotificationMes("当前有" + count + "条产品关联到该认证，不允许删除！", false);
                    }
                } else {
                    fsn.initNotificationMes("验证该认证信息是否关联产品时后台出现异常！" + data.result.errorMessage, false);
                }
            },
        });
        return count;
    };

    /**
     * 初始化企业认证信息 grid 
     * cerDs 为 grid 的数据源
     * update huangyong 2015/5/7
     */
    fsn.initialCertification = function (cerDs) {
        /* 其他认证信息如果存在就销毁改grid,避免重复初始化 */
        if ($("#certification-grid").data("kendoGrid")) {
            $("#certification-grid").data("kendoGrid").destroy();
        }

        /* 初始化其他认证信息grid */
        $("#certification-grid_1").kendoGrid({
            dataSource: cerDs == null ? [] : new kendo.data.DataSource({
                data: cerDs,
                page: 1,
                pageSize: 1000
            }),
            navigatable: true,
            pageable: {
                messages: lims.gridPageMessage()
            },
            //toolbar: [{
            //    template: kendo.template($("#toolbar_template_cert").html())
            //}],
            columns: [{
                fild: "id",
                title: "id",
                editable: false,
                width: 1
            },
                {
                    field: "cert.name",
                    title: "认证名称",
                    width: 40
                },
                {
                    title: "上传图片名称",
                    width: 50,
                    template:function(model){
                        if(model.certResource != null){
                            // 方法返回一个a标签，使得在gird中点击认证的资源名称可以在浏览器的新窗口中查看资源图片
                            var url = model.certResource.url;
                            var href = (url == null || url == "") ? "" : 'href="'+url+'" target="_black" title="查看原图"';
                            return '<a ' + href + '>'+model.certResource.fileName+'</a>';
                        }
                    }
                },
                {
                    field: "cert.stdStatus",
                    title: "认证类型",
                    template: function (e) {
                        if (e.cert != null) {
                            // 认证信息Cert 中 stdStauts 说明  0 ：国家标准认证，基础数据 。 1： 荣誉认证，由用户自己上传。
                            if (e.cert.stdStatus == 0) return "普通认证";
                            if (e.cert.stdStatus == 1) return "荣誉认证";
                        }
                        return "普通认证";
                    },
                    width: 50
                },
                {
                    title: "有效期截止时间（如：2014-01-01）",
                    width: 60,
                    template: function(model){
                        model.endDate = fsn.formatGridDate(model.endDate);
                        var date = model.endDate;
                        date = (date != null ? date.toString() : "");
                        /**
                         * 为了方便查询 当用户选择截止日期为 长期有效 时 实际保存的是 2200-01-01
                         * 所有页面展示时需要进行转换
                         */
                        return date.indexOf("2200-01-01")>-1 ? "长期有效" : date;
                    }
                }]
        });


        /* 初始化其他认证信息grid */
        $("#certification-grid").kendoGrid({
            dataSource: cerDs == null ? [] : new kendo.data.DataSource({
                data: cerDs,
                page: 1,
                pageSize: 1000
            }),
            navigatable: true,
            pageable: {
                messages: lims.gridPageMessage()
            },
            toolbar: [{
                template: kendo.template($("#toolbar_template_cert").html())
            }],
            columns: [{
                fild: "id",
                title: "id",
                editable: false,
                width: 1
            },
                {
                    field: "cert.name",
                    title: "认证名称",
                    width: 40
                },
                {
                    title: "上传图片名称",
                    width: 50,
                    template:function(model){
                    	if(model.certResource != null){
                    		// 方法返回一个a标签，使得在gird中点击认证的资源名称可以在浏览器的新窗口中查看资源图片
                    		var url = model.certResource.url;
                    		var href = (url == null || url == "") ? "" : 'href="'+url+'" target="_black" title="查看原图"';
                    		return '<a ' + href + '>'+model.certResource.fileName+'</a>';
                    	}
                    }
                },
                {
                    field: "cert.stdStatus",
                    title: "认证类型",
                    template: function (e) {
                        if (e.cert != null) {
                        	// 认证信息Cert 中 stdStauts 说明  0 ：国家标准认证，基础数据 。 1： 荣誉认证，由用户自己上传。
                            if (e.cert.stdStatus == 0) return "普通认证";
                            if (e.cert.stdStatus == 1) return "荣誉认证";
                        }
                        return "普通认证";
                    },
                    width: 50
                },
                {
                    title: "有效期截止时间（如：2014-01-01）",
                    width: 60,
                    template: function(model){
                    	model.endDate = fsn.formatGridDate(model.endDate);
                    	var date = model.endDate;
                    	date = (date != null ? date.toString() : "");
                    	/**
                    	 * 为了方便查询 当用户选择截止日期为 长期有效 时 实际保存的是 2200-01-01
                    	 * 所有页面展示时需要进行转换
                    	 */ 
                    	return date.indexOf("2200-01-01")>-1 ? "长期有效" : date;
                    }
                },
                {
                    command: [
                        {
                            name: "edit",
                            text: "<span class='k-icon k-edit'></span>" + fsn.l("Edit"),
                            click: function (e) {
                                e.preventDefault();
                                // 当前选中的认证信息所在的行 从 0 开始计 ，在验证认证信息是否重复时需要该标记
                                var rowIndex = $(e.currentTarget).closest("tr").index();
                                var delItem = this.dataItem($(e.currentTarget).closest("tr"));
                                // 编辑认证信息
                                business_unit.editCert(delItem, rowIndex);
                            }
                        },
                        {
                            name: "delete",
                            text: "<span class='k-icon k-delete'></span>" + fsn.l("Delete"),
                            click: function (e) {
                            	// 删除认证信息
                                e.preventDefault();
                                var delItem = this.dataItem($(e.currentTarget).closest("tr"));
                                // 如果被删除的认证信息id不为null，需要判断改认证信息是否跟产品信息有关联，若有，则不允许删除
                                if (delItem.id != null) {
                                	// 统计被删除的认证信息关联的产品数量
                                    var count = fsn.validateBusUnitCert(delItem.id);
                                    if (count == 0) {
                                    	// 从grid中 移除 认证信息实体
                                        $("#certification-grid").data("kendoGrid").dataSource.remove(delItem);
                                    }
                                } else {
                                	// 从grid中 移除 认证信息实体
                                    $("#certification-grid").data("kendoGrid").dataSource.remove(delItem);
                                }

                            }
                        }],
                    title: fsn.l("Operation"),
                    width: 40
                }]
        });
    };
    
    // 全局变量 用来暂存每次编辑or新增时修改的认证信息实体（可以是标准认证信息或者荣誉认证信息）
    var SAVE_CERT_MODE = null;
    var IS_NEW = true; //当新增认证信息时，该值为true 否则为false
    var ROW_INDEX = -1; //当前编辑认证信息所在的行，验证认证信息是否重复时使用
    
    /**
     * 删除资源的标记，
     * DELSTATUS.clearCert 值为true时，表示用户清空了标准认证信息资源
     * DELSTATUS.clearHonor 值为true时，表示用户清空了荣誉认证信息资源
     */
    var DELSTATUS = {clearCert:false,clearHonor:false};
    // 用来暂存 标准认证信息资源，用户新上传的资源
    var TMP_STDCERT_RES = null;
    // 用来暂存 荣誉认证信息资源，用户新上传的资源
    var TMP_HONORCERT_RES = null;
    
    /**
     * 认证信息编辑方法(中转作用)，根据stdStatus 来具体调用 addCertHonor or addCertOrdinary 方法
     * rowIndex 当前编辑认证信息所在的行
     * @author huangyong 2015/05/07
     * @update tangxin 2015/05/15 添加 rowIndex 参数
     */
    business_unit.editCert = function (e, rowIndex) {
    	// SAVE_CERT_MODE 全局变量 用来暂存每次编辑or新增时修改的认证信息实体（可以是标准认证信息或者荣誉认证信息）
        SAVE_CERT_MODE = e;
        if (e.cert != null && e.cert.stdStatus == 1) {
        	// stdStatus 的值为 1 编辑荣誉认证
            business_unit.addCertHonor(false, rowIndex);
        } else {
            // stdStatus 的值为 0  编辑标准认证
            business_unit.addCertOrdinary(false, rowIndex);
        }
    };

    /**
     * 通过kendUpload上传标准认证资源成功后，将资源赋值给全局变量 TMP_STDCERT_RES
     * TMP_STDCERT_RES 全局变量，用来暂存 标准认证信息资源
     * @author huangyong 2015/05/07 
     */
    business_unit.addUploadStdCertRes = function(attachments){
        if(attachments == null || attachments.length < 1){
            return;
        }
        // 标准认证信息资源只能上传一张，返回的数据的list，默认取第一个
        TMP_STDCERT_RES = attachments[0];
        // 一旦调用了此方法，说明标准认证资源处于新增状态，需要将清空标记 clearCert 设置为false
        if(DELSTATUS != null) DELSTATUS.clearCert = false;
        // 设置标准认证资源上传控件的上传状态为false即不允许上传，认证信息只能上传一张
        setStdCertUploadStatus(false);
    }

    /**
     * 添加荣誉认证资源
     * @author huangyong 2015/05/07
     */
    function addUploadHonorCertRes(attachments){
        if(attachments == null || attachments.length < 1){
            return;
        }
        // 荣誉认证信息资源只能上传一张，返回的数据的list，默认取第一个
        TMP_STDCERT_RES = attachments[0];
        // 一旦调用了此方法，说明标准认证资源处于新增状态，需要将清空标记 clearCert 设置为 false
        if(DELSTATUS != null) DELSTATUS.clearCert = false;
        // 设置标准认证资源上传控件的上传状态为false即不允许上传，认证信息图片只能上传一张
        setStdCertUploadStatus(false);
    }

    /**
     * 通过kendUpload上传荣誉认证小图标资源成功后，将资源赋值给全局变量 TMP_HONORCERT_RES
     * TMP_HONORCERT_RES 全局变量，用来暂存荣誉认证小图标资源
     * @author huangyong 2015/05/07
     */
    business_unit.addUploadHonorIconCertRes = function(attachments){
        if(attachments == null || attachments.length < 1){
            return;
        }
        // 荣誉认证小图标只能上传一张，返回的数据的list，默认取第一个
        TMP_HONORCERT_RES = attachments[0];
        // 一旦调用了此方法，说明标准认证资源处于新增状态，需要将清空标记 clearHonor 设置为 false
        if(DELSTATUS != null) DELSTATUS.clearHonor = false;
        // 设置荣誉认证资源上传控件的上传状态为false 即不允许上传，认证信息图片只能上传一张
        setStdHonorCertUploadStatus(false);
    }

    /**
     * 清空认证信息图片
     * @author huangyong 2015/05/07
     */
    business_unit.clearCertificationFiles = function(){
    	// 一旦调用了此方法，说明标准认证资源已经被删除，需要将清空标记 clearCert 设置为 true
        if(DELSTATUS != null) DELSTATUS.clearCert = true;
        //$("#certificationAttachmentsListView").html("");
        //$("#btn_clearCertificationFiles").hide();
        // 设置荣誉认证资源上传控件的上传状态为true 即允许上传，认证信息图片只能上传一张
        setStdCertUploadStatus(true);

    };

    /**
     * 清空荣誉图标
     * @author huangyong 2015/05/07
     */
    business_unit.clearHonorIconFiles = function() {
    	// 一旦调用了此方法，说明荣誉认证资源已经被删除，需要将清空标记 clearHonor 设置为 true
        if(DELSTATUS != null) DELSTATUS.clearHonor = true;
        $("#honorIconAttachmentsListView").html("");
        $("#btn_clearHonorIconFiles").hide();
        // 设置荣誉认证图标资源上传控件的上传状态为true 即允许上传，认证信息图片只能上传一张
        setStdHonorCertUploadStatus(true);
        //$("#upload_honorIcon_files_img_s").hide();
    };
        
    /**
     * kendoupload上传控件 删除资源的回调方法 删除荣誉认证小图标
     * @author tangxin 2015/05/15
     */
    business_unit.kendoDelHonorIconFile = function(file) {
    	// 资源已被删除，将暂存荣誉认证小图标资源的变量设置为空
    	TMP_HONORCERT_RES = null;
    	// 一旦调用了此方法，说明荣誉认证图标资源已经被删除，需要将清空标记 clearHonor 设置为 true
        if(DELSTATUS != null) DELSTATUS.clearHonor = true;
        // 设置荣誉认证图标资源上传控件的上传状态为true 即允许上传，认证信息图片只能上传一张
        setStdHonorCertUploadStatus(true);
    };
        
    /**
     * kendoupload上传控件 删除资源的回调方法 删除认证证照资源
     * @author tangxin 2015/05/15
     */
    business_unit.kendoDelCertificationFile  = function(file) {
    	// 认证证照资源已被删除，将暂认证证照资源的变量设置为空
    	TMP_STDCERT_RES = null;
    	// 一旦调用了此方法，说明认证证照资源已经被删除，需要将清空标记 clearCert 设置为 true
        if(DELSTATUS != null) DELSTATUS.clearCert = true;
        // 设置认证证照资源上传控件的上传状态为 true 即允许上传，认证信息图片只能上传一张
        setStdCertUploadStatus(true);
    };
        
    /**
     * 设置其他认证上传控件的状态 认证证照或图标只能上传一张
     * upload：
     *   1.true 移除上传控件的 disabled 属性，允许上传图片
     *   2.false 为上传控件添加 disabled 属性， 屏蔽上传功能
     * @author tangxin 2015/05/15
     */
    function setStdCertUploadStatus(upload){
    	if(upload){
    		$("#upload_certification_div .k-upload-button").removeClass("k-state-disabled");
    		$("#upload_certification_files").removeAttr("disabled");
    	}else{
    		$("#upload_certification_div .k-upload-button").addClass("k-state-disabled");
    		$("#upload_certification_files").attr("disabled","disabled");
    	}	
    }
        
    /**
     * 设置荣誉认证图标上传控件的状态 荣誉认证图标只能上传一张
     * upload：
     *   1.true 移除上传控件的 disabled 属性，允许上传图片
     *   2.false 为上传控件添加 disabled 属性， 屏蔽上传功能
     * @author tangxin 2015/05/15
     */
    function setStdHonorCertUploadStatus(upload){
    	if(upload){
    		$("#upload_honorIcon_div .k-upload-button").removeClass("k-state-disabled");
    		$("#upload_honorIcon_files").removeAttr("disabled");
    	}else{
    		$("#upload_honorIcon_div .k-upload-button").addClass("k-state-disabled");
    		$("#upload_honorIcon_files").attr("disabled","disabled");
    	}	
    }
     
    /**
     * 认证信息中 长期有效 CheckBox 的 change 事件
     * 1.当用户勾选长期有效时，截止日期输入框不允许输入，阴影显示长期有效
     * 2.用户未勾选长期有效时，截止日期输入框允许输入
     */
    business_unit.changeCheckBox = function(){
    	var check = $("#longValid").is(':checked');
        if(!check){
        	// 长期有效 未勾选
            document.getElementById("longValid").checked = false;
            $("#certDate").data("kendoDatePicker").readonly(false);
            document.getElementById("certDate").disabled = false;
            $("#certDate").attr("placeholder","例如:2015-01-01");
        }else{
            document.getElementById("longValid").checked = true;
            $("#certDate").data("kendoDatePicker").readonly(true);
            document.getElementById("certDate").disabled = true;
            $("#certDate").val("");
            $("#certDate").attr("placeholder","长期有效");
        }
    };

    /**
     * 添加/编辑 标准认证
     * @param isNew true 新增 或 false 编辑
     * @param rowIndex 新增时该值为 -1 ，编辑时为用户选中的认证信息所在 gird 中的行数
     * @author huangyong 2015/05/06
     * @update tangxin 2015/5/16
     */
    business_unit.addCertOrdinary = function (isNew, rowIndex) {
        // IS_NEW 全局变量 标识当前认证信息的状态，true是新增状态，fasle是编辑状态
        IS_NEW = isNew;
        ROW_INDEX = rowIndex; //当前编辑认证信息所在的行，验证认证信息是否重复时使用
        if(DELSTATUS != null) DELSTATUS.clearCert = false; //资源删除状态设置为false
        $("#cert_type_list").show(); //标准认证 需要显示认证的类型（下拉列表）
        $("#certIcon").hide(); // 标准认证不需要上传图标  隐藏荣誉认证图标展示的div
        $("#certName_div").hide(); // 隐藏荣誉认证名称输入框
        $("#show_demo_div").hide(); // 隐藏荣誉认证查看示例
        $("#btn_clearCertificationFiles").hide(); //隐藏资源清空按钮
        $("#certificationAttachmentsListView").html(""); //隐藏资源展示区域
        // 清空认证信息的填写框
        $("#cert_name").val("");
        $("#certDate").val("");
        $("#certDate").attr("placeholder","例如:2015-01-01");
        $("#certDate").data("kendoDatePicker").readonly(false);
        document.getElementById("longValid").checked = false;
        TMP_STDCERT_RES = null; //该变量只存储用户新上传的标准认证资源，每次打开新增/编辑框时都将该值赋值为null
        /* 设置标准认证资源上传控件为可以上传状态 */
        setStdCertUploadStatus(true);
        /* 初始化认证类别的 kendoDropDownList 控件 */
        if (!$("#cert_type").data("kendoDropDownList")) {
            $("#cert_type").kendoDropDownList({
                autoBind: false,
                optionLabel: "请选择...",
                dataTextField: "name",
                dataValueField: "id",
                dataSource: fsn.stdCertDS,
                index: 0
            });
        }
        if (isNew) {
        	// 新增时，将认证类型赋值为未选择状态
        	$("#cert_type").data("kendoDropDownList").text("请选择...");
        	// 定义一个 待保存的 认证信息实体，并赋值给 全局变量 SAVE_CERT_MODE
            SAVE_CERT_MODE = {id: null, certResource: [], cert: {stdStatus: 0}};
        } else {
        	// 编辑状态， 为认证信息输入框赋值
            $("#cert_type").data("kendoDropDownList").text(SAVE_CERT_MODE.cert.name);
            /**
        	 * 为了方便查询 当用户选择截止日期为 长期有效 时 实际保存的是 2200-01-01
        	 * 所有页面展示时需要进行转换
        	 */ 
            var endDate = SAVE_CERT_MODE.endDate;
            endDate = (endDate != null ? endDate.toString() : "");
            $("#certDate").val(endDate.indexOf("2200-01-01")>-1 ? "" : endDate);
            document.getElementById("longValid").checked = (endDate.indexOf("2200-01-01")>-1 ? true : false);
            business_unit.changeCheckBox();
            // 编辑时，认证信息资源的展示处理
            if(SAVE_CERT_MODE.certResource != null) {
                var imgData = SAVE_CERT_MODE.certResource
                var url = imgData.url ? imgData.url : "";
                    if(url != ""){
                        $("#upload_certification_files_img").attr("src",url);
                    }else{
                        url =  imgData.file ? imgData.file : "";
                        if(url != ""){
                            $("#").attr("src","data:"+url);
                            $("#upload_certification_files_img").attr("src","data:"+imgData.type.rtName+";base64,"+imgData.file);
                        }
                    }
                    if(url != ""){
                        $("#upload_certification_files_img_e").hide();
                        $("#upload_certification_files_img_s").show();
                    }else{
                        $("#upload_certification_files_img_e").show();
                        $("#upload_certification_files_img_s").hide();
                    }
                //var filename = SAVE_CERT_MODE.certResource.fileName ? SAVE_CERT_MODE.certResource.fileName : "";
                //var a_href = (url == null || url == "" ? "": 'href="'+url+'" target="_blank"');
                //// 自定义展示样式
                //var strHTML = '<a '+ a_href + ' >' + filename + '</a>';
                //$("#certificationAttachmentsListView").html(strHTML);
                //if (filename != "") {
                //    $("#btn_clearCertificationFiles").show();
                //}
            }
        }
        /* 初始化认证资源的上传控件 */
        $("#upload_certification_div").html("<input id='upload_certification_files' type='file' />");
        saleskendoUtil.buildUpload("upload_certification_files", null,'上传证书','.jpeg,.jpg,.png,.bmp',false, business_unit.addUploadStdCertRes, business_unit.kendoDelCertificationFile);
        // 打开 认证信息 新增/编辑 窗口
        $("#addCertWindow").data("kendoWindow").open().center();
        if(!isNew && SAVE_CERT_MODE.certResource != null) {
        	// 编辑状态，并且 资源已经上传 则设置上传控件的 属性为 disabled
        	setStdCertUploadStatus(false);
        }
    };

    /**
     * 添加/编辑 荣誉认证
     * @param isNew true 新增 或 false 编辑
     * @param rowIndex 新增时该值为 -1, 编辑时为用户选中的认证信息所在 gird 中的行数
     * @author huangyong 2015/05/06
     * @update tangxin 2015/5/16
     */
    business_unit.addCertHonor = function (isNew, rowIndex) {
        $("#cert_type_list").hide();// 荣誉认证需要用户自己手填，隐藏类型的下拉选择框
        $("#certName_div").show(); // 显示荣誉名称输入框
        $("#certIcon").show(); // 显示荣誉认证图标上传区域
        $("#show_demo_div").show(); // 荣誉认证有示例展示，显示展示区域
        $("#btn_clearHonorIconFiles").hide(); //隐藏荣誉认证图标资源清空按钮
        $("#btn_clearCertificationFiles").hide(); //隐藏认证证照资源清空按钮
        $("#honorIconAttachmentsListView").html(""); // 隐藏荣誉认证图标资源展示区域
        $("#certificationAttachmentsListView").html(""); // 隐藏荣誉认证证照资源展示区域
        // 清空荣誉认证信息填写框的内容
        $("#cert_name").val("");
        $("#certDate").val("");
        $("#certDate").attr("placeholder","例如:2015-01-01");
        $("#certDate").data("kendoDatePicker").readonly(false);
        document.getElementById("longValid").checked = false; // 设置长期有效未选中
        if(DELSTATUS != null) DELSTATUS.clearHonor = false; //资源删除状态设置为false
        // TMP_STDCERT_RES 和 TMP_HONORCERT_RES 只存储用户新上传的荣誉认证资源和图标，每次打开新增/编辑框时都将该值赋值为null
        TMP_STDCERT_RES = null;
        TMP_HONORCERT_RES = null;
        // 设置荣誉认证证照和图标上传控件为可以上传状态 
        setStdCertUploadStatus(true);
        setStdHonorCertUploadStatus(true);
        IS_NEW = isNew;
        ROW_INDEX = rowIndex;
        if (isNew) {
        	// 定义一个 待保存的 荣誉认证信息实体，并赋值给 全局变量 SAVE_CERT_MODE
            SAVE_CERT_MODE = {id: null, certResource: [], cert: {certIconResource: [], stdStatus: 1}};
        } else {
            $("#cert_name").val(SAVE_CERT_MODE.cert.name);
            /**
        	 * 为了方便查询 当用户选择截止日期为 长期有效 时 实际保存的是 2200-01-01
        	 * 所有页面展示时需要进行转换
        	 */
            var endDate = SAVE_CERT_MODE.endDate;
            endDate = (endDate != null ? endDate.toString() : "");
            $("#certDate").val(endDate.indexOf("2200-01-01")>-1 ? "" : endDate);
            document.getElementById("longValid").checked = (endDate.indexOf("2200-01-01")>-1 ? true : false);
            business_unit.changeCheckBox();
            // 编辑时，认证证照资源的展示处理
            if (SAVE_CERT_MODE.certResource != null) {
                var url = SAVE_CERT_MODE.certResource.url ? SAVE_CERT_MODE.certResource.url : "";
                //if(url == ""){
                //    $("#upload_honorIcon_files_img_s").hide();
                //}else{
                //    $("#upload_honorIcon_files_img_s").show();
                //    $("#upload_honorIcon_files_img").attr("src",url);
                //}

                var filename = SAVE_CERT_MODE.certResource.fileName ? SAVE_CERT_MODE.certResource.fileName : "";
                var a_href = (url == null || url == "" ? "": 'href="'+url+'" target="_blank"');
                // 自定义展示样式
                var strHTML = '<a '+ a_href + ' >' + filename + '</a>';
                $("#certificationAttachmentsListView").html(strHTML);
                if (filename != "") {
                    $("#btn_clearCertificationFiles").show();
                }
            }
            /**
             *  编辑时，荣誉认证图标资源的展示处理
             *  由于荣誉认证图标后台存储时没有对应的Resource，只保存了图片的url
             *  当荣誉认证的 certIconResource 不为空时，为用户新上传的的资源
             *  否则，当荣誉认证的imgUrl不等于空时，说明认证图标资源是从后台加载的
             */
            if (SAVE_CERT_MODE.cert.certIconResource != null ||
                (SAVE_CERT_MODE.cert.imgUrl != null && SAVE_CERT_MODE.cert.imgUrl != "")){
                if ( SAVE_CERT_MODE.cert.name != null) {
                	var url = SAVE_CERT_MODE.cert.imgUrl;
                	var cres = SAVE_CERT_MODE.cert.certIconResource;
                	var sname = cres != null ? cres.fileName : "";
                	if(sname == "" && url != null && url.length > 15){
                		sname = url.substr(url.length-15);
                	}
                	var a_href = (url == null || url == "" ? "": 'href="'+url+'" target="_blank"');
                	// 自定义展示样式
                    var strIconHTML = '<a ' + a_href + '>' + sname + '</a>';
                    $("#honorIconAttachmentsListView").html(strIconHTML);
                    $("#btn_clearHonorIconFiles").show();
                }
            }
        }
        /* 初始化上传荣誉认证证照控件 */
        $("#upload_certification_div").html("<input id='upload_certification_files' type='file' />");
        saleskendoUtil.buildUpload("upload_certification_files", null,'上传证书','.jpeg,.jpg,.png,.bmp',false, business_unit.addUploadStdCertRes, business_unit.kendoDelCertificationFile);
        /* 初始化上传荣誉图标控件 */
        $("#upload_honorIcon_div").html("<input id='upload_honorIcon_files' type='file' />");
        saleskendoUtil.buildUpload("upload_honorIcon_files", null,'上传图标', '.jpeg,.jpg,.png,.bmp',false, business_unit.addUploadHonorIconCertRes, business_unit.kendoDelHonorIconFile);
        // 打开 荣誉认证信息 新增/编辑 窗口
        $("#addCertWindow").data("kendoWindow").open().center();
        if (!isNew && SAVE_CERT_MODE.certResource != null) {
        	// 编辑状态，并且 资源已经上传 则设置荣誉认证证照上传控件的 属性为 disabled
        	setStdCertUploadStatus(false);
        }
        if(!isNew){
        	if (SAVE_CERT_MODE.cert.certIconResource != null ||
        			(SAVE_CERT_MODE.cert.imgUrl != null && SAVE_CERT_MODE.cert.imgUrl != "")){
        		// 编辑状态，并且 资源已经上传 则设置荣誉图标上传控件的 属性为 disabled
        		setStdHonorCertUploadStatus(false);
        	}
        }
    };

    /**
     * 打开荣誉认证图标实例窗口
     * @author huangyong 2015/5/7
     */
    business_unit.showUploadCertDemo = function () {
        $("#demo_win").data("kendoWindow").open().center();
    };

    /**
     * 标准认证或荣誉认证窗口的保存方法
     * @author huangyong 2015/5/7
     */
    business_unit.yesUploadHonor = function () {
    	// 验证荣誉认证信息 
    	if(!validateCert()) return;
    	// 验证通过后，封装信息并保存到 认证信息的 Grid 中
    	// 处理认证的截止日期
        SAVE_CERT_MODE.endDate = $("#certDate").val().trim();
        if(SAVE_CERT_MODE.endDate == ""){
        	var check = $("#longValid").is(':checked');
        	SAVE_CERT_MODE.endDate = (check ? "2200-01-01" : "");
        }
        // 荣誉认证名称赋值
        if (SAVE_CERT_MODE.cert.stdStatus == 1) {
            SAVE_CERT_MODE.cert.name = $("#cert_name").val().trim();
        }
        // 标准认证名称赋值
        if (SAVE_CERT_MODE.cert.stdStatus == 0) {
            SAVE_CERT_MODE.cert.name = $("#cert_type").data("kendoDropDownList").text();
        }
        // 处理认证信息对应的资源
        if(TMP_STDCERT_RES != null) SAVE_CERT_MODE.certResource = TMP_STDCERT_RES;
        if(TMP_HONORCERT_RES != null) SAVE_CERT_MODE.cert.certIconResource = TMP_HONORCERT_RES;
        if (IS_NEW) {
        	// 新增是添加到认证信息 grid 中
            $("#certification-grid").data("kendoGrid").dataSource.add(SAVE_CERT_MODE);
        }
        // 刷新认证信息 gird
        $("#certification-grid").data("kendoGrid").refresh();
        // 关闭认证信息编辑窗口
        $("#addCertWindow").data("kendoWindow").close();
        // 全局变量的状态恢复
        SAVE_CERT_MODE = {id: null};
        IS_NEW = true;
    };

    /**
     * 认证信息窗口-取消按钮事件，关闭认证信息窗口
     * @author huangyong 2015/5/7
     */
    business_unit.noUpLoadHonor = function () {
        $("#addCertWindow").data("kendoWindow").close();
    };

    /**
     * 验证认证名称是否重复
     * @author tangxin 2015/5/15
     */
    function validateCertName(name){
    	if(name == null || name == "") return true;
    	var certs = $("#certification-grid").data("kendoGrid").dataSource.data();
        var status = true;
        for (var i = 0; i < certs.length; i++) {
        	//判断重复的条件 ，认证信息grid中，对比的两个认证信息不在同一行，且名称相同
           if (certs[i].cert.name == name && (certs[i].id != SAVE_CERT_MODE.id 
            		|| i != ROW_INDEX)) {
        	   status = false;
               break;
            }
        }
        return status;
    }
      
		/**
         * 荣誉认证名称失去焦点事件，验证名称不允许超过100字符
         * @author tangxin 2015/6/4
         */
        $("#cert_name").blur(function(){
        	var value = $("#cert_name").val().trim();
        	if(value.length > 100){
        		saleskendoUtil.msg("v_name_span","荣誉认证名称必须在100字以内!");
        	}else{
        		saleskendoUtil.clearMsg("v_name_span");
        	}
        });
	  
    /**
     * 验证认证信息完整性
     * update huangyong 2015/5/6
     */
    function validateCert(){
    	var name = "";
    	if (SAVE_CERT_MODE.cert.stdStatus == 1) {
    		name = $("#cert_name").val().trim();
        } else if (SAVE_CERT_MODE.cert.stdStatus == 0) {
            name = $("#cert_type").data("kendoDropDownList").text();
        }
        if(name =="" || name == "请选择..."){
            lims.initNotificationMes("请填写认证名称!", false);
            return false;
        }
        var date = $("#certDate").val().trim();
        /* 如果认证的截止日期为空，判断长期有效是否选中 */
        if(date == ""){
        	var check = $("#longValid").is(':checked');
        	date = (check ? "2200-01-01" : "");
        }
        var result = date.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
        if(result==null){
            lims.initNotificationMes("请填写有效截止日期!", false);
            return false;
        }
        // 判断认证资源被删除后，用户是否重新上传
        if(DELSTATUS != null){
            if(DELSTATUS.clearCert){
            	// 用户删除了认证资源，但未重新上传，验证不通过
                if($("#certificationAttachmentsListView").html()=="" && TMP_STDCERT_RES == null){
                    lims.initNotificationMes("请上传认证图片",false);
                    return false;
			    }
			}   
			if(DELSTATUS.clearHonor) {
				// 用户删除了荣誉认证图标，但未重新上传，验证不通过
				if($("#honorIconAttachmentsListView").html()=="" && TMP_HONORCERT_RES == null){
					 lims.initNotificationMes("请上传荣誉认证的小图标",false);
				   return false;
				}
			}
		}
		if(name.length > 100){
			saleskendoUtil.msg("v_name_span","荣誉认证名称必须在100字以内!");
			return false;
		}
		if(name =="" || name == "请选择..."){
			lims.initNotificationMes("请填写认证名称!", false);
			return false;
		}
		var date = $("#certDate").val().trim();
		/* 如果认证的截止日期为空，判断长期有效是否选中 */
		if(date == ""){
			var check = $("#longValid").is(':checked');
			date = (check ? "2200-01-01" : "");
		}
		var result = date.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
		if(result==null){
			lims.initNotificationMes("请填写有效截止日期!", false);
			return false;
		}
		
        // 新增时，判断用户是否上传荣誉认证图标
        if(SAVE_CERT_MODE.cert.stdStatus == 1 && SAVE_CERT_MODE.cert.certIconResource!=null &&
        		SAVE_CERT_MODE.cert.certIconResource.length < 1 && TMP_HONORCERT_RES == null){
            lims.initNotificationMes("请上传荣誉认证的小图标",false);
            return false;
        }
        // 新增时，判断用户是否上传认证证照
        if(SAVE_CERT_MODE.certResource.length < 1 && TMP_STDCERT_RES == null){
            lims.initNotificationMes("请上传认证图片",false);
            return false;
        }
        // 验证认证名称是否重复
        if(!validateCertName(name)){
        	lims.initNotificationMes("认证信息已经存在！",false);
            return false;
        }
        return true;
    }

    /*标准认证的数据源*/
    fsn.stdCertDS = new kendo.data.DataSource({
        transport: {
            read: portal.HTTP_PREFIX + "/product/getStandCertifications"
        },
        schema: {
            data: function (returnValue) {
                return returnValue.data; //响应到页面的数据
            }
        }
    });

    //校验地址格式是否正确
    verifyAddress = function(id) {
        //判断地址选择窗口是否打开，若是没开打则检验
        if ($(".provinceCityAll").is(":hidden")) {
            var text = $("#" + id).val().trim();
            if (text == "") {
                return;
            } //若为空则不进行校验
            var strs = new Array();
            strs = text.split("-"); //分割字符串
            if (strs.length < 2 || strs.length > 3) { //判断字符串数是否为3位
                fsn.initNotificationMes('格式只能为：</br>【省-市】或【省-市-区（县）】</br>请重新填写！', false);
                $("#" + id).val("");
                return;
            } else {
                //判断每个字符串是否有为空
                for (var i = 0; i < strs.length; i++) {
                    if (strs[i].trim() == "") {
                        fsn.initNotificationMes('省、市、区（县）不能为空</br>请重新填写！', false);
                        $("#" + id).val("");
                        return;
                    }
                }
                var reg = /^[\u4e00-\u9fa5]{1,10}$/g; //校验是否全为汉字
                var str = "";
                if (strs.length < 2) {
                    str = strs[0].trim() + strs[1].trim() + strs[2].trim();
                } else {
                    str = strs[0].trim() + strs[1].trim();
                }
                if (!reg.test(str)) {
                    fsn.initNotificationMes('只能输入汉字字符，请重新填写！', false);
                    $("#" + id).val("");
                    return;
                }
            }
        }
    };

    //初始化验证器
    $("#name").kendoValidator().data("kendoValidator");
    $("#licenseNo").kendoValidator().data("kendoValidator");
    $("#personInCharge").kendoValidator().data("kendoValidator");
    $("#email").kendoValidator().data("kendoValidator");
    $("#licenseName").kendoValidator().data("kendoValidator");
    $("#legalName").kendoValidator().data("kendoValidator");
    $("#orgCode").kendoValidator().data("kendoValidator");
    $("#orgName").kendoValidator().data("kendoValidator");
    $("#bus_mainAddr").kendoValidator().data("kendoValidator");
    $("#org_mainAddr").kendoValidator().data("kendoValidator");
    $("#license_mainAddr").kendoValidator().data("kendoValidator");

    business_unit.initialize();
});