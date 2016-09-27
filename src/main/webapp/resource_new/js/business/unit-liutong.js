$(document).ready(function(){
    var fsn = window.fsn = window.fsn ||{};
    var business_unit = window.fsn.business_unit = window.fsn.business_unit ||{};
    var portal = fsn.portal = fsn.portal ||{}; // portal命名空间
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    business_unit.id = null;
    business_unit.name=null;
    fsn.business_send = false;
    business_unit.editLic=null;
    business_unit.flagLic=false;
    business_unit.type =null;
	business_unit.preview_id=null;//查看详细businessId
    business_unit.aryOrgAttachments = new Array();
    business_unit.aryLicenseAttachments = new Array();
    business_unit.aryDisAttachments = new Array();
    business_unit.aryLogoAttachments = new Array();
	fsn.imgMax = 5;//页面引导图片id
	fsn.src = "http://qa.fsnrec.com/portal/guid/unit/unit_liutong_";//引导原地址
	
	/**
	 * 获取需要展示详情的企业组织机构，以及页面来源标记
	 */
	try {
		business_unit.view_business_organization = $.cookie("view_0_business").organization;
		business_unit.view_business_fromFlag = $.cookie("view_0_business").fromFlag;
        $.cookie("view_0_business", JSON.stringify({}), {
            path: '/'
        });
    } catch(e) {}
	
	try {
		 var url = document.location.href;
		 if (url.indexOf("=")>0){
   			business_unit.preview_id = url.substring(url.indexOf("=")+1,url.length);
		 }
	} catch (e) {
	}
    
    business_unit.initialize = function(){
    	/* 初始化上传控件 */
    	$("#upload_logo_files_div").html("<input id='upload_logo_files' type='file' />");
    	business_unit.buildUpload("upload_logo_files",business_unit.aryLogoAttachments,"upload_logo_files_log");
    	$("#upload_orgnization_files_div").html("<input id='upload_orgnization_files' type='file' />");
    	business_unit.buildUpload("upload_orgnization_files",business_unit.aryOrgAttachments,"upload_orgnization_files_log");
    	$("#upload_license_files_div").html("<input id='upload_license_files' type='file' />");
    	business_unit.buildUpload("upload_license_files",business_unit.aryLicenseAttachments,"upload_license_files_log");
    	$("#upload_distribution_files_div").html("<input id='upload_distribution_files' type='file' />");
    	business_unit.buildUpload("upload_distribution_files",business_unit.aryDisAttachments,"upload_distribution_files_log");
    	$("#btn_clearOrgFiles").bind("click",business_unit.clearOrgFiles);
		$("#btn_clearLicenseFiles").bind("click",business_unit.clearLicenseFiles);
		$("#btn_clearDisFiles").bind("click",business_unit.clearDisFiles);
		$("#btn_clearOrgFiles").hide();
		$("#btn_clearLicenseFiles").hide();
		$("#btn_clearDisFiles").hide();
    	business_unit.initKendoWindow("k_window","保存状态","300px","60px",false);
    	//business_unit.bulidDropDownList("liuTongBusType","label","id","/sys/getListDistrictByDescription/主体性质");
		
    	/* 在预览时，屏蔽页面操作 */
		if(business_unit.preview_id){
			business_unit.preview(business_unit.preview_id);
			business_unit.hideComponentOfView();
		}else if(business_unit.view_business_organization){
			business_unit.initView();
			business_unit.hideComponentOfView();
			/* 页面来源为客户/供应商管理界面，点击查看企业信息 */
			if(business_unit.view_business_fromFlag == "customer.html"){
				//$("#fromFlag").html("查看企业信息   >> 基本信息详情展示");
			}
		}else{
			business_unit.initView();
		}
		
        $("#saveBasic").bind("click", business_unit.saveBusinessbasBasic);
        $("#saveLicense").bind("click", business_unit.saveBusinessbasCert);
        $("#reset").bind("click", business_unit.reset);
        $("#upload").bind("click",business_unit.upload);
        
        //失去焦点做提示
        // $("#name").bind("blur",business_unit.verificationName);
        $("#telephone").bind("blur",business_unit.validaTetelephone);
        $("#postalCode").bind("blur",business_unit.validaPostalCode);
        $("#email").bind("blur",business_unit.validateEmail);
        //$("#licenseNo").bind("blur",business_unit.validaLicenseNo);
		$("#bus_mainAddr").bind("blur",function(){verifyAddress("bus_mainAddr");});
		$("#license_mainAddr").bind("blur",function(){verifyAddress("license_mainAddr");});
		$("#org_mainAddr").bind("blur",function(){verifyAddress("org_mainAddr");});
		$("#dis_mainAddr").bind("blur",function(){verifyAddress("dis_mainAddr");});
		/*组织机构代码输入框失去焦点验证*/
		$("#orgCode").bind("blur",function() {
     	   var orgCode = $("#orgCode").val().trim();
     	   if(orgCode=="") return;
     	   if(business_unit.editBusiness==null) business_unit.editBusiness={};
     	   var orgId = business_unit.organization;
     	   business_unit.verificationOrgCode(orgCode,orgId);
		});
		
	/*	$(".folder").bind("click",function(event){
			if($(this).html() == "[查看更多]"){
				$(this).siblings("span").html($(this).parent(this).siblings(".hiddenword").html());			
				$(this).html("[收起]");
			}else{
				$(this).siblings("span").html($(this).parent(this).siblings(".hiddenword").html().substr(0,140));
				$(this).html("[查看更多]");
			}
		});*/
		$("#editBtn").bind("click",function(event){

			$("#Swi_B").hide();
			$("#Swi_A").show();
		});
		$("#backBtn").bind("click",function(event){
			$("#Swi_B").show();
			$("#Swi_A").hide();
		});

		business_unit.initTabStrip();

    };
    
    business_unit.hideComponentOfView = function(){
    	//屏蔽按钮控件
		$("#save").hide();
		$("#upload").removeClass("gzlt-div");
		$("#upload").hide();
		$("#upload_logo_div").hide();
		$("#upload_license_div").hide();
		$("#upload_org_div").hide();
		$("#upload_distribution_div").hide();
		//$("#status_bar").hide();
    };
    
    business_unit.initView = function(){
    	var url = portal.HTTP_PREFIX + "/business/getSCBusinessByOrg";
    	/* 当view_business_organization不为空时，表示是从其他页面跳转过来的 */
    	if(business_unit.view_business_organization){
    		url += ("?organization=" + business_unit.view_business_organization);
    	}
        isNew = false;
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            async:false,
            success: function(returnValue){
            	var busUnit = returnValue.data;
            	business_unit.type=busUnit.type;
                business_unit.setValue(busUnit, "liutong");
                business_unit.setViewValue(busUnit);
               /* if(busUnit.address!=null&&busUnit.address.indexOf("贵州省")>-1){
                	business_unit.isGZLTBus = true;
                	business_unit.organization=busUnit.organization;
                	business_unit.initGZLiutongBusPage();
                	business_unit.setBusinessGZLiutongValue(busUnit);
                }*/
            }
        });
    };
	
	business_unit.preview = function(id){
		 isNew = false;
		 var model={
				id:id,
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/business/market/verifyBusiness",
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(model),
				success: function(dataValue){
					if (dataValue.result.status == "true") {
						if (dataValue.count == 0) {
							fsn.initNotificationMes("当前市场下没有该商户！", false);
						}else {
							$.ajax({
					            url: portal.HTTP_PREFIX + "/business/getBusinessById/"+id,
					            type: "GET",
					            dataType: "json",
					            async:false,
					            success: function(returnValue){
					            	var busUnit = returnValue.data;
					            	business_unit.type=busUnit.type;
					                business_unit.setValue(busUnit, "liutong");
					                if(busUnit.address!=null&&busUnit.address.indexOf("贵州省")>-1){
					                	business_unit.isGZLTBus = true;
					                	business_unit.initGZLiutongBusPage();
					                	business_unit.setBusinessGZLiutongValue(busUnit);
					                }
					            }
					        });
						}
					}
				}
			});
	}
    
    business_unit.createInstance = function(){
		
		//var sysArea = {id: $("#belongAreaGroupName").val().trim()}
		//var office= {id: $("#authoritiesGroupName").val().trim()}
    	var license = {
        		licenseNo: $("#licenseNo").val().trim(),
        		licensename: $("#licenseName").val().trim(),
        		legalName: $("#legalName").val().trim(),
        		startTime: $("#licenseStartTime").val().trim(),
        		endTime: $("#licenseEndTime").val().trim(),
        		registrationTime: $("#registrationTime").val().trim(),
        		issuingAuthority: $("#issuingAuthority").val().trim(),
        		subjectType: $("#subjectType").val().trim(),
        		businessAddress: $("#license_mainAddr").val().trim().replace(/-/g,"")+$("#license_streetAddress").val().trim(),
        		otherAddress:$("#license_mainAddr").val().trim()+"--"+$("#license_streetAddress").val().trim(),
        		toleranceRange: $("#toleranceRange").val().trim(),
        		registeredCapital: $("#registeredCapital").val().trim(),
        	};
        var orgInstitution = {
        		orgCode:$("#orgCode").val().trim(),
        		orgName: $("#orgName").val().trim(),
        		startTime: $("#orgStartTime").val().trim(),
        		endTime: $("#orgEndTime").val().trim(),
        		unitsAwarded: $("#unitsAwarded").val().trim(),
        		orgType: $("#orgType").val().trim(),
        		orgAddress: $("#org_mainAddr").val().trim().replace(/-/g,"")+$("#org_streetAddress").val().trim(),
        		otherAddress:$("#org_mainAddr").val().trim()+"--"+$("#org_streetAddress").val().trim(),
        	};
        var distribution = {
        		distributionNo: $("#distributionNo").val().trim(),
        		licensingAuthority: $("#licensingAuthority").val().trim(),
        		licenseName: $("#distributionName").val().trim(),
        		startTime: $("#disStartTime").val().trim(),
        		endTime: $("#disEndTime").val().trim(),
        		subjectType: $("#disSubjectType").val().trim(),
        		businessAddress: $("#dis_mainAddr").val().trim().replace(/-/g,"")+$("#dis_streetAddress").val().trim(),
        		otherAddress:$("#dis_mainAddr").val().trim()+"--"+$("#dis_streetAddress").val().trim(),
//        		toleranceRange: "",//$("#disToleranceRange").val().trim(),
        		legalName: $("#disLegalName").val().trim(),
        	};
        var subBusiness = {
                id: business_unit.id,
                name: $("#name").val().trim(),
                address:$("#bus_mainAddr").val().trim().replace(/-/g,"")+$("#bus_streetAddress").val().trim(),
                otherAddress:$("#bus_mainAddr").val().trim()+"--"+$("#bus_streetAddress").val().trim(),
                personInCharge: $("#personInCharge").val().trim(),
                contact: $("#contact").val().trim(),
                telephone: $("#telephone").val().trim(),
                postalCode: $("#postalCode").val().trim(),
                email:$("#email").val().trim(),
                fax:$("#fax").val().trim(),
                about:$("#product_about").val().trim(),
                website:$("#website").val().trim(),
                license: license,
                //sysArea: sysArea,
                //office: office,
                orgInstitution: orgInstitution,
                distribution: distribution,
                logoAttachments: business_unit.aryLogoAttachments,
                orgAttachments: business_unit.aryOrgAttachments,
                licAttachments: business_unit.aryLicenseAttachments,
                disAttachments: business_unit.aryDisAttachments,
            };
        return subBusiness;
    };

	business_unit.saveBusinessbasBasic = function(){
		var url = portal.HTTP_PREFIX + "/business/saveBusinessbasBasic";
		business_unit.save(url,0);

	};
	business_unit.saveBusinessbasCert = function(){
		var url = portal.HTTP_PREFIX + "/business/saveBusinessbasCert";
		business_unit.save(url,1);
	};

    /* 保存 */
    business_unit.save = function(url,status){
        // 1.校验数据的有效性
    	fsn.clearErrorStyle();
        if(!business_unit.validateFormat(status)) return;
        /*if(business_unit.isGZLTBus!=null && business_unit.isGZLTBus){
    		if(!business_unit.validateGZLiutongField()) return;
    	}*/
        // 2.数据封装
    	var subBusiness = business_unit.createInstance();
    	// 如果是贵州的流通企业
    	/*if(business_unit.isGZLTBus!=null && business_unit.isGZLTBus){
    		subBusiness = business_unit.createInstanceGZLiutong(subBusiness);
    	}else{
			//普通流通企业和贵州流通企业不相同的两个字段--主体性质和许可范围
			var districtAry = new Array();
			var tmpId = $("#liuTongBusType").data("kendoDropDownList").value();
		 	districtAry.push({id:tmpId});
			subBusiness.district = districtAry;
			subBusiness.distribution.toleranceRange = $("#disToleranceRange").val().trim();
		}*/
        // 3.保存
        $("#winMsg").html("正在修改数据，请稍候....");
        $("#k_window").data("kendoWindow").open().center();
        $.ajax({
            url: url,
            type: "PUT",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(subBusiness),
            success: function(returnVal){
            	$("#k_window").data("kendoWindow").close();
            	if(returnVal.result.status == "true"){
					if(status==0){
						lims.initNotificationMes("企业信息修改成功", true);
					}else{
						lims.initNotificationMes("证照信息修改成功", true);
					}
         		    //business_unit.setOrgAttachments(returnVal.data.orgAttachments);
                    //business_unit.setLicenseAttachments(returnVal.data.licAttachments);
                    //business_unit.setDisAttachments(returnVal.data.disAttachments);
                    //business_unit.setLogoAttachments(returnVal.data.logoAttachments);
                    //business_unit.setViewValue(returnVal.data);
					business_unit.initView();
                    $("#Swi_B").removeClass('none');
        			$("#Swi_A").addClass("none");
        			scrollTo(0,0);
                }
                else {
			 		lims.initNotificationMes("企业信息修改失败", false);
                }
            }
        });
    };
    
    /* 上传企业信息到Fdams */
    business_unit.upload = function(){
        // 1.校验数据的有效性
    	fsn.clearErrorStyle();
        if(!business_unit.validateFormat()) return;
    	//if(!business_unit.validateGZLiutongField()) return;
        // 2.数据封装
    	var subBusiness = business_unit.createInstance();
    	//subBusiness = business_unit.createInstanceGZLiutong(subBusiness);
        // 3.保存并上传
        $("#winMsg").html("正在上传企业信息，请稍候....");
        $("#k_window").data("kendoWindow").open().center();
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/uploadBusUnitToFdams",
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(subBusiness),
            success: function(returnVal){
            	$("#k_window").data("kendoWindow").close();
            	if(returnVal.result.status == "true"){
			 		lims.initNotificationMes("企业信息上传成功", true);
                }
                else {
			 		lims.initNotificationMes("企业信息上传失败", false);
                }
            }
        });
    };
    
    /* 校验数据的有效性 */
    business_unit.validateFormat = function(status) {
    	if(!business_unit.validateCommon(status)){ return false; }
    	if(!business_unit.validateMyDate(status)){ return false; }
        return true;
    };
    
    business_unit.validateMyDate = function(){
    	    	if(!$("#distributionNo").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【食品流通许可证信息】中的食品流通许可证编号不能为空！',false);
			business_unit.wrong("distributionNo","text");
    		return false;
    	}
    	var disbutNo=$("#distributionNo").val().trim().replace(/-/g,"");
    	var vaddisbutNo = /^[A-Za-z0-9]{1,}$/.test(disbutNo);
		 if(!vaddisbutNo){
			 lims.initNotificationMes("【食品流通许可证信息】中的食品流通许可证编号包含中文！",false);		 
			 return false;
		}
    	if(!$("#licensingAuthority").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【食品流通许可证信息】中的许可机关不能为空！',false);
			business_unit.wrong("licensingAuthority","text");
    		return false;
    	}
    	if(!$("#distributionName").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【食品流通许可证信息】中的名称不能为空！',false);
			business_unit.wrong("distributionName","text");
    		return false;
    	}
    	if(!$("#dis_mainAddr").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【食品流通许可证信息】中的经营场所不能为空！',false);
			business_unit.wrong("dis_mainAddr","text");
    		return false;
    	}
    	if(!$("#dis_streetAddress").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【食品流通许可证信息】中的经营场所的街道地址不能为空！',false);
			business_unit.wrong("dis_streetAddress","text");
    		return false;
    	}
    	if(!$("#disLegalName").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【食品流通许可证信息】中的负责人不能为空！',false);
			business_unit.wrong("disLegalName","text");
    		return false;
    	}
    	if(!fsn.validateMustDate("disStartTime","食品流通许可证的起始日期")){return false;}
    	if(!fsn.validateMustDate("disEndTime","食品流通许可证的截止日期")){return false;}
    	var startDate = $("#disStartTime").data("kendoDatePicker").value();
        var endDate = $("#disEndTime").data("kendoDatePicker").value();
        if((endDate-startDate)<0){
        	lims.initNotificationMes('食品流通许可证的起始日期不能大于截止日期！',false);
        	return false;
        }
        if(business_unit.aryDisAttachments.length<1){
        	lims.initNotificationMes('请上传食品流通许可证图片！',false);
			business_unit.wrong("upload_distribution_files","select");
        	return false;
        }
        return true;
    };
    
    /* 初始化时间控件  */
	$("#licenseStartTime,#licenseEndTime,#orgStartTime," +
			"#orgEndTime,#registrationTime,#disStartTime," +
			"#disEndTime,#test_time_format,#licenseDate").kendoDatePicker({
   	 format: "yyyy-MM-dd", 
   	 height:30,
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
	
	//校验地址格式是否正确
	 verifyAddress = function(id){
	 	//判断地址选择窗口是否打开，若是没开打则检验
		if($(".provinceCityAll").is(":hidden")){
				var text = $("#"+id).val().trim();
				if(text==""){return;}			//若为空则不进行校验
				var strs= new Array(); 
				strs=text.split("-");				//分割字符串
					if(strs.length<2||strs.length>3){				//判断字符串数是否为3位
					lims.initNotificationMes('格式只能为：</br>【省-市】或【省-市-区（县）】</br>请重新填写！',false);
					$("#"+id).val("");
					return;
				}else{
					//判断每个字符串是否有为空
					for(var i=0;i<strs.length;i++){
						if(strs[i].trim()==""){
							lims.initNotificationMes('省、市、区（县）不能为空</br>请重新填写！',false);
							$("#"+id).val("");
							return;
						}
					}
					var reg = /^[\u4e00-\u9fa5]{1,10}$/g; 				//校验是否全为汉字
					var str = "";
					if(strs.length<2){
						str = strs[0].trim()+strs[1].trim()+strs[2].trim();
					}else{
						str = strs[0].trim()+strs[1].trim();
					}
					if(!reg.test(str)){
						lims.initNotificationMes('只能输入汉字字符，请重新填写！',false);
						$("#"+id).val("");
						return;
					}
				}
			}
	};
	
	business_unit.bulidDropDownList=function(formId,textField,valueField,url){
		$("#"+formId).kendoDropDownList({
			autoBind: false,
	        optionLabel:"请选择...",
			dataTextField: textField,
	        dataValueField: valueField,
			dataSource: {
				transport: {
					read: portal.HTTP_PREFIX + url
				},
				schema: {
					data : function(returnValue) {
						return returnValue.data;  //响应到页面的数据
		            }
				}
			}
		});
	};
	
	business_unit.bulidDropDownListByDS=function(formId,textField,valueField,dataSource){
		$("#"+formId).kendoDropDownList({
			autoBind: false,
	        optionLabel:"请选择...",
			dataTextField: textField,
	        dataValueField: valueField,
			dataSource:dataSource==null?[]:dataSource,
		});
	};
	
	business_unit.initGZLiutongBusPage = function(){
		business_unit.showGZLiutongField();
		buildCheckboxClickByGZ();
		
		//business_unit.bulidDropDownList("permitLicenseChange","label","id","/sys/getListDistrictByDescription/换证情况");
		var districDs = [{id:8,label:"省会城市"},{id:9,label:"地市"},{id:10,label:"区县"},{id:75,label:"省本级"}];
		business_unit.bulidDropDownListByDS("busDistricType","label","id",districDs);
		var areaDs = [{id:5,label:"城市"},{id:6,label:"农村"},{id:7,label:"城乡结合"}];
		business_unit.bulidDropDownListByDS("busAreaType","label","id",areaDs);
		business_unit.bulidDropDownListByDS("busRunStatus","name","id",[{id:11, name:"正常营业"},{id:12, name:"未正常营业"}]);
		$("#busMarket").kendoComboBox({
	        dataTextField: "name",
	        dataValueField: "organization",
	        dataSource: lims.getAutoLoadDsByUrl("/business/market/getBusMarket"),
	        filter: "contains",
	        minLength: 0,
		});
	};
	 
	 var treeType = null;
	 
	 $("#belongAreaGrouBtn").click(function(event){
		 if(treeType!=null && treeType != "area") {
			 $("#areaAndOfficeWin").data("kendoWindow").destroy();
			 $(".treeWin_div").html("<div id='areaAndOfficeWin'><div id='listView'></div>");
		 }
		 brandCategoryTreeWin("areaAndOfficeWin", "belongAreaGroupName", "belongAreaGroupId", "area");
		 treeType = "area";
		 event.preventDefault();
	});
	 
	$("#authoritiesGrouBtn").click(function(event){
		if(treeType!=null && treeType != "office"){
			$("#areaAndOfficeWin").data("kendoWindow").destroy();
			$(".treeWin_div").html("<div id='areaAndOfficeWin'><div id='listView'></div>");
		}
		brandCategoryTreeWin("areaAndOfficeWin", "authoritiesGroupName", "authoritiesGroupId", "office");
		treeType = "office";
		event.preventDefault();
	});
	
	business_unit.createInstanceGZLiutong=function(saveBusinessUnit){
		 if(saveBusinessUnit==null) return;
		 var districtAry = new Array();
		$('input:checkbox:checked').each(function(i){
			 if($(this).attr("id")!=null && $(this).attr("id").trim().length>0){
				 var district={id:$(this).attr("id").trim()};
				 districtAry.push(district);
			 }
		 });
		 $('input:radio:checked').each(function(i){
			 if($(this).attr("id")!=null && $(this).attr("id").trim().length>0){
				 var district={id:$(this).attr("id").trim(),};
				 districtAry.push(district);
			 }
		 });
		 var tmpId = $("#liuTongBusType").data("kendoDropDownList").value();
		 districtAry.push({id:tmpId});
		 tmpId = $("#permitLicenseChange").data("kendoDropDownList").value();
		 districtAry.push({id:tmpId});
		 tmpId = $("#busDistricType").data("kendoDropDownList").value();
		 districtAry.push({id:tmpId});
		 tmpId = $("#busAreaType").data("kendoDropDownList").value();
		 districtAry.push({id:tmpId});
		 tmpId = $("#busRunStatus").data("kendoDropDownList").value();
		 districtAry.push({id:tmpId});
		 saveBusinessUnit.district = districtAry;
		 saveBusinessUnit.sysArea = {id:$("#belongAreaGroupId").val()};
		 saveBusinessUnit.office = {id:$("#authoritiesGroupId").val()};
		 saveBusinessUnit.distribution.toleranceTime = $("#licenseDate").data("kendoDatePicker").value();
		 saveBusinessUnit.distribution.manageType = $("#busRunType").val().trim();
		 saveBusinessUnit.distribution.manageProject = $("#busRunProject").val().trim();
		 saveBusinessUnit.note = $("#busNote").val().trim();
		 saveBusinessUnit.marketOrg = $("#busMarket").data("kendoComboBox").value();
		 return saveBusinessUnit;
	};
	
	business_unit.setBusinessGZLiutongValue = function(business){
		if(business == null) return;
		$("#busNote").val(business.note);
		if(business.district!=null){
			for(var i=0;i<business.district.length;i++){
				var description = business.district[i].description;
				var discrId = business.district[i].id;
				/*if(description=="主体性质"){
					$("#liuTongBusType").data("kendoDropDownList").value(discrId);
					continue;
				}*/
				if(description=="地区类型"){
					$("#busDistricType").data("kendoDropDownList").value(discrId);
					continue;
				}
				if(description=="营业状态"){
					$("#busRunStatus").data("kendoDropDownList").value(discrId);
					continue;
				}
				if(description=="地域类型"){
					$("#busAreaType").data("kendoDropDownList").value(discrId);
					continue;
				}
				/*if(description=="换证情况"){
					$("#permitLicenseChange").data("kendoDropDownList").value(discrId);
					continue;
				}*/
				document.getElementById(discrId).checked=true;
				var pcheckboxName = $("#"+discrId).attr("data-checkbox");
				if(pcheckboxName != null) {
					document.getElementsByName(pcheckboxName)[0].checked = true;
				}
			}
		}
		if(business.sysArea!=null){
			$("#belongAreaGroupName").val(business.sysArea.name);
			$("#belongAreaGroupId").val(business.sysArea.id);
		}
		if(business.office!=null){
			$("#authoritiesGroupName").val(business.office.name);
			$("#authoritiesGroupId").val(business.office.id);
		}
		if(business.distribution!=null){
			$("#busRunType").val(business.distribution.manageType);
			$("#busRunProject").val(business.distribution.manageProject);
			$("#licenseDate").data("kendoDatePicker").value(business.distribution.toleranceTime);
		}
		if($("#busMarket").data("kendoComboBox")){
			$("#busMarket").data("kendoComboBox").value(business.marketOrg);
		}
	};
	
	/*显示贵州流通企业其他信息字段*/
	business_unit.showGZLiutongField = function(){
		$("#hideRedStar").css("display","inline");
		$("div.disToleranceRange").hide();
		$("#div_hzqk").css("display","inline");
		$(".gzlt-div").css("display","inline");
	};
	
	/*当选中报告了radio 的 checkbox 后 设置radio 的状态*/
	business_unit.setRadioStatus = function(e){
		if(e.data == null || e.data.radioIds == null) return;
		var radioIds = e.data.radioIds;
		var isChecked =document.getElementsByName(e.data.thisElemtName)[0].checked;
		for(var i=0;i<radioIds.length;i++){
			document.getElementById(radioIds[i]).disabled = (!isChecked);
			if(!isChecked){
				document.getElementById(radioIds[i]).checked = false;
			}else if(i==0){
				document.getElementById(radioIds[i]).checked = true;
			}
		}
	};
	
	/*给指定的checkbox绑定 click 事件*/
	function buildCheckboxClickByGZ(){
		$("input[name='is_SJAQSF']").bind("click",{thisElemtName:"is_SJAQSF",radioIds:['21','22','23']},business_unit.setRadioStatus);
		$("input[name='is_ZJQYJH']").bind("click",{thisElemtName:"is_ZJQYJH",radioIds:['28','29']},business_unit.setRadioStatus);
		$("input[name='is_JYRZP']").bind("click",{thisElemtName:"is_JYRZP",radioIds:['32','33']},business_unit.setRadioStatus);
		$("input[name='is_TGRQ']").bind("click",{thisElemtName:"is_TGRQ",radioIds:['35','36','37']},business_unit.setRadioStatus);
		$("input[name='is_JYSYY']").bind("click",{thisElemtName:"is_JYSYY",radioIds:['39','40']},business_unit.setRadioStatus);
		$("input[name='is_JYBJ']").bind("click",{thisElemtName:"is_JYBJ",radioIds:['41','42']},business_unit.setRadioStatus);
	};
	
	business_unit.testFun=function(){
		var hhh = $(this);
	};
	
	/*验证贵州流通企业扩展必填字段*/
	business_unit.validateGZLiutongField = function(){
		/*var tmpId = $("#liuTongBusType").data("kendoDropDownList").value();
		if(!tmpId){
			lims.initNotificationMes('请选择企业基本信息中的主体性质！',false);
			business_unit.wrong("liuTongBusType","select");
			return false;
		}*/
		/*tmpId = $("#permitLicenseChange").data("kendoDropDownList").value();
		if(!tmpId){
			lims.initNotificationMes('请选择食品流通许可证信息中的换证情况！',false);
			business_unit.wrong("permitLicenseChange","select");
			return false;
		}*/
		tmpId = $("#busDistricType").data("kendoDropDownList").value();
		if(!tmpId){
			lims.initNotificationMes('请选择企业其他信息中的地区类型！',false);
			business_unit.wrong("busDistricType","select");
			return false;
		}
		tmpId = $("#busAreaType").data("kendoDropDownList").value();
		if(!tmpId){
			lims.initNotificationMes('请选企业其他信息中的地域类型！',false);
			business_unit.wrong("busAreaType","select");
			return false;
		}
		tmpId = $("#busRunStatus").data("kendoDropDownList").value();
		if(!tmpId){
			lims.initNotificationMes('请选择企业其他信息中的营业状态！',false);
			business_unit.wrong("busRunStatus","select");
			return false;
		}
		var barea = $("#belongAreaGroupName").val().trim();
		if(barea == null || barea.length<1) {
			lims.initNotificationMes('请选择企业其他信息中的所属区划！',false);
			business_unit.wrong("belongAreaGrouBtn","select");
			return false;
		}
		var author = $("#authoritiesGroupName").val().trim();
		if(author == null || author.length<1) {
			lims.initNotificationMes('请选择企业其他信息中的管辖食药监机关！',false);
			business_unit.wrong("authoritiesGrouBtn","select");
			return false;
		}
		var checkbosAry = $("td[name='td_QRXZ'] input:checkbox:checked");
		if(checkbosAry == null || checkbosAry.length < 1){
			lims.initNotificationMes('请勾选企业其他信息中的其他性质至少一项！',false);
			return false;
		}
		var checkbosAry = $("td[name='td_QRXZ'] input:checkbox:checked");
		if(checkbosAry == null || checkbosAry.length < 1){
			lims.initNotificationMes('请勾选企业其他信息中的其他性质至少一项！',false);
			return false;
		}
		checkbosAry = $("td[name='td_JYZTQK'] input:checkbox:checked");
		if(checkbosAry == null || checkbosAry.length < 1){
			lims.initNotificationMes('请勾选企业其他信息中的经营主体情况至少一项！',false);
			return false;
		}
		checkbosAry = $("td[name='td_JYZDSP'] input:checkbox:checked");
		if(checkbosAry == null || checkbosAry.length < 1){
			lims.initNotificationMes('请勾选企业其他信息中的经营重点食品情况至少一项！',false);
			return false;
		}
		var marketId = $("#busMarket").data("kendoComboBox").value();
		var val = $("#busMarket").data("kendoComboBox").text().trim();
		if(val!=""&&marketId==val){
			lims.initNotificationMes('你手动输入的交易市场不存在，请通过下拉选择已有的市场信息！',false);
			return false;
		}
		return true;
	};
	
	$("#name").kendoValidator().data("kendoValidator");
    $("#licenseNo").kendoValidator().data("kendoValidator");
    $("#personInCharge").kendoValidator().data("kendoValidator");
    $("#email").kendoValidator().data("kendoValidator");
    $("#licenseName").kendoValidator().data("kendoValidator");
    $("#legalName").kendoValidator().data("kendoValidator");
    $("#orgCode").kendoValidator().data("kendoValidator");
    $("#orgName").kendoValidator().data("kendoValidator");
    $("#bus_mainAddr").kendoValidator().data("kendoValidator");
    $("#org_mianAddr").kendoValidator().data("kendoValidator");
    $("#license_mianAddr").kendoValidator().data("kendoValidator");
    $("#distributionNo").kendoValidator().data("kendoVlidator");
    $("#distributionName").kendoValidator().data("kendoVlidator");
    $("#dis_mianAddr").kendoValidator().data("kendoValidator");
    $("#disLegalName").kendoValidator().data("kendoVlidator");
    
    /* 查看基本信息页面赋值操作 */
    business_unit.setViewValue = function(data){
    	if(business_unit.view_business_fromFlag=="customer.html"){
    		$("#editBtn").hide();
    		$("#backBtn").removeClass('none');
    		$("#fromFlag").html("供应商："+data.name+" >> 基本信息");
    	}
    	$("#buName").html(data.name);
    	//$("#bu_about").html("");
    	if(data.logoAttachments.length>0){
    		$("#logo_img").attr("src",data.logoAttachments[0].url);
    	}
        var html = '<div class="color_title f20 fontFzzd"><span style="color:#000000;font-size:18px" >企业简介</span></div>';
        if(business_unit.GetLength(data.about)<=280){
        	var buAbout="";
        	if(!(data.about==null||data.about=="null")){
        		buAbout=data.about;
        	}
        	html += '<div class="mt10 fl6 line25 color444" style="word-warp:break-word;word-break:break-all"><span class="fl6 line25 color444">'+buAbout+'</span></div>';
        }else{
        	html += '<div class="mt10 fl6 line25" style="word-warp:break-word;word-break:break-all"><span class="fl6 line25 color444">'+
        	data.about.substr(0,140)+'</span><a class="in-block folder">[查看更多]</a></div><div class="none hiddenword" style="word-warp:break-word;word-break:break-all">'+data.about+'</div>';
        }
        $("#bu_about").html(html);
        $("#bu_legalName").html(data.personInCharge==null?"":data.personInCharge);
        $("#bu_website").html(data.website);
        $("#bu_phone").html(data.telephone);
        $("#bu_postcode").html(data.postalCode);
        $("#bu_address").html(data.address);
        $("#bu_fax").html(data.fax);
        if(data.license!=null){
        	if(data.licAttachments.length>0){
        		$("#bu_lic_img").attr("src",data.licAttachments[0].url);
        		$("#bu_lic_img").attr("onclick","fsn.business_unit.seefacilityImg('lic')");
        		//$("#bu_lic_a").attr("href",data.licAttachments[0].url);
        	}
        	$("#bu_lic_no").html(data.license.licenseNo);
        	$("#bu_lic_disLegalName").html(data.license.legalName==null?"":data.license.legalName);
        	$("#bu_lic_disAddress").html(data.license.businessAddress=null?"":data.license.businessAddress);
        	if(data.license.startTime){
        		$("#bu_lic_expiry").html(data.license.startTime.substr(0,10)+"~"+data.license.endTime.substr(0,10));
        	}
        }
        if(data.orgInstitution!=null){
        	if(data.orgAttachments.length>0){
        		$("#bu_org_img").attr("src",data.orgAttachments[0].url);
        		//$("#bu_org_a").attr("href",data.orgAttachments[0].url);

				$("#bu_org_img").attr("onclick","fsn.business_unit.seefacilityImg('org')");
        	}
            $("#bu_org_code").html(data.orgInstitution.orgCode);
            $("#bu_org_name").html(data.orgInstitution.orgName==null?"":data.orgInstitution.orgName);
            $("#bu_org_address").html(data.orgInstitution.orgAddress==null?"":data.orgInstitution.orgAddress);
            if(data.orgInstitution.startTime){
            	 $("#bu_org_expiry").html(data.orgInstitution.startTime.substr(0,10)+"~"+data.orgInstitution.endTime.substr(0,10));
            }
        }
        if(data.distribution==null){
			$("#dis").addClass("none");
        }else{
        	$("#dis").removeClass('none');
			var str = '';
        	if(data.type!=null&&data.type!=''&&data.type.indexOf("餐饮企业")!=-1){
        		$("#bu_dis_code_title").html("餐饮许可：");
				str = 1;
        	}else{
        		$("#bu_dis_code_title").html("流通许可证：");
				str = 2;
        	}
        	if(data.liquorAttachments.length>0){
        		if(data.liquorAttachments.length>0){
	        		$("#bu_dis_img").attr("src",data.liquorAttachments[0].url);
	         		//$("#bu_dis_a").attr("href",data.liquorAttachments[0].url);
					$("#bu_dis_img").attr("onclick","fsn.business_unit.seefacilityImg('dis"+str+"')");
        		}
        		$("#bu_dis_code").html(data.liquorCode);
        	}else{
	        	 if(data.disAttachments.length>0){
	         		$("#bu_dis_img").attr("src",data.disAttachments[0].url);
	         		//$("#bu_dis_a").attr("href",data.disAttachments[0].url);
					 $("#bu_dis_img").attr("onclick","fsn.business_unit.seefacilityImg('dis"+str+"')");
	         	}
	        	 $("#bu_dis_code").html(data.distribution.distributionNo);
        	}
        	$("#bu_dis_name").html(data.distribution.licenseName==null?"":data.distribution.licenseName);
        	$("#bu_dis_disLegalName").html(data.distribution.legalName==null?"":data.distribution.legalName);
             if(data.distribution.startTime){
            	 $("#bu_dis_expiry").html(data.distribution.startTime.substr(0,10)+"~"+data.distribution.endTime.substr(0,10));
             }
        }
        
        $(".folder").bind("click",function(event){
			if($(this).html() == "[查看更多]"){
				$(this).siblings("span").html($(this).parent(this).siblings(".hiddenword").html());			
				$(this).html("[收起]");
				 var he=document.getElementById("bu_about").offsetHeight+600;
				$("#Swi_B").attr("style","height:"+he+"px");
			}else{
				$(this).siblings("span").html($(this).parent(this).siblings(".hiddenword").html().substr(0,140));
				$(this).html("[查看更多]");
				$("#Swi_B").attr("style","height:650px");
			}
		});
    };
    business_unit.GetLength = function(str) {
    	if(str == null){
    		return 0;
    	}
        //获得字符串实际长度，中文2，英文1
        var realLength = 0, len = str.length, charCode = -1;
        for (var i = 0; i < len; i++) {
            charCode = str.charCodeAt(i);
            if (charCode >= 0 && charCode <= 128) realLength += 1;
            else realLength += 2;
        }
        return realLength;
    };
	business_unit.initTabStrip = function(){
		business_unit.tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
		business_unit.tabStrip.select(0);        // Select by jQuery selector
		// tabStrip.select(1);
		/**
		 * 初始化
		 //      */
		var g = $("#tabstrip").data("kendoTabStrip");
		if(!g){
			g =  $("#tabstrip").kendoTabStrip({
				animation:  {
					open: {
						effects: "fadeIn"
					}
				}
			});
		};
	};
	business_unit.initWindowImg = function(title){
		$('#DIV_IMG_WIN_wnd_title').html(title);
		$("#DIV_IMG_WIN").kendoWindow({
			width: "900",
			height:"700",
			title: title,
			visible: true, //是否可见
			resizable: false,//尺寸是否可调
			draggable:true,//是否可以拖动
			modal: true
		});
	};
	business_unit.seefacilityImg = function(id){
		var imgs = null;
		var title = "";
		if(id == 'lic'){
			imgs = business_unit.aryLicenseAttachments;
			title = "营业执照";
		}else if(id == 'org'){
			imgs = business_unit.aryOrgAttachments;
			title = "组织机构代码证照";
		}else if(id == "dis1"){
			imgs = business_unit.aryDisAttachments;
			title = "餐饮许可证照";
		}else if(id == "dis2"){
			imgs = business_unit.aryDisAttachments;
			title = "流通许可证照";
		}
		if(imgs != null && imgs.length > 0){
			business_unit.initWindowImg(title);
		}
		var slides = $("#slides");
		var img ="<div class=\"slides_container\">";
		for(var i=0;i<imgs.length; i++)
		{
			img =img+ '<div class="slide"><img style="width: 849px;height: 638px" src="'+imgs[i].url+'"/></div>';
		}
		img =img+ '</div><a href="#" class="prev"><img src="../../resource/js/slides/img/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>'+
		'<a href="#" class="next"><img src="../../resource/js/slides/img/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>';
		slides.html(img);
		$('#slides').slides();
		$("#DIV_IMG_WIN").data("kendoWindow").open().center();
	};
	business_unit.initialize();
})
