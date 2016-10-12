$(function(){
  	var fsn = window.fsn = window.fsn || {};
    var ledger= fsn.ledger = fsn.ledger ||{};
    var portal = fsn.portal = fsn.portal ||{}; // portal命名空间
    ledger.aryProLicImg = new Array();
    ledger.aryProQsImg = new Array();
    ledger.aryProDisImg = new Array();
    var ID = null;
    var qiyeID = null;
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    fsn.business_send = false;
	
	ledger.initialize = function(){
		lookLoadDishsNo();
    	ledger.initKendoWindow("k_window","保存状态","300px","60px",false);
    	/* 保存 */
    	$("#save").bind("click", ledger.save);
    	ledger.initUploadfileImg();
	};
	ledger.initKendoWindow=function(winId,title,width,height,isVisible){
    	$("#"+winId).kendoWindow({
			title:title,
			width:width,
			height:height,
			modal:true,
			visible:isVisible,
			actions:["Close"]
		});
    };
    
	function lookLoadDishsNo(){
    	var haveId = window.location.href;
    		if(haveId.indexOf("status=")>0){
    			var status = haveId.split("status=")[1];
    			if(status.indexOf("yes")!=-1){
    				status = "采购管理>>预包装台账列表";
    			}else{
    				status = "采购管理>>添加/编辑原辅料";
    			}
    			$("#status_bar").html(status);
    			
    		}
    	if(haveId.indexOf("id=")>0){
    		var id = haveId.split("id=")[1];
    		$.ajax({
    			url:portal.HTTP_PREFIX  + "/ledgerPrepackNo/findLedgerPrepackNo/"+id,
    			type:"GET",
    			dataType: "json",
    			async:false,
    			success:function(returnValue){
    				$("#productName").val(returnValue.data.productName);
    				$("#standard").val(returnValue.data.standard);
    				$("#alias").val(returnValue.data.alias);
    				$("#number").val(returnValue.data.number);
    				$("#purchaseTime").val(FormatDate(returnValue.data.purchaseTime));
    				$("#companyName").val(returnValue.data.companyName);
    				$("#companyPhone").val(returnValue.data.companyPhone);
    				$("#supplier").val(returnValue.data.supplier);
    				$("#companyAddress").val(returnValue.data.companyAddress);
    				ID = returnValue.data.id;
    				qiyeID = returnValue.data.qiyeId;
    				ledger.setImgResource(returnValue.data.licResource,returnValue.data.disResource); // 产品图片列表
    			}
    		});
    	}else{
				$("#productName").val();
				$("#standard").val();
				$("#alias").val();
				$("#number").val();
				$("#purchaseTime").val();
				$("#companyName").val();
				$("#companyPhone").val();
				$("#supplier").val();
				$("#companyAddress").val();
    	};
    }
     /*
     * 格式化时间控件获取的时间*/
    function FormatDate (strTime) {
        var date = new Date(strTime);
        var dateee = date.format("YYYY-MM-dd");
        return dateee;//date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
    }
   	ledger.goBack = function(){
   		window.location.href="ledger_prepackNo.html";
   	};
   	ledger.createInstance = function(){
   		var distribution = {
   			 id:ID,
   			 qiyeId:qiyeID,
   			 productName:$("#productName").val().trim(),
    		 standard:$("#standard").val().trim(),
    		 alias:$("#alias").val().trim(),
    		 number:$("#number").val().trim(),
    		 purchaseTime:$("#purchaseTime").val().trim(),
    		 companyName:$("#companyName").val().trim(),
    		 companyPhone:$("#companyPhone").val().trim(),
    		 supplier:$("#supplier").val().trim(),
    		 companyAddress:$("#companyAddress").val().trim(),
    		 licResource:ledger.aryProLicImg[0]==undefined||ledger.aryProLicImg[0].fileName==undefined?null:ledger.aryProLicImg[0],
    		 disResource:ledger.aryProDisImg[0]==undefined||ledger.aryProDisImg[0].fileName==undefined?null:ledger.aryProDisImg[0]
   		};
   		return distribution;
   	};

   	/* 保存 */
   ledger.save = function(){
    	 // 1.校验数据的有效性
    	fsn.clearErrorStyle();
    	if(!ledger.validateFormat()) return;
    	
    	// 2.数据封装
    	var subBusiness = ledger.createInstance();
    	 // 3.保存
        $("#winMsg").html("正在保存数据，请稍候....");
        $("#k_window").data("kendoWindow").open().center();
        
        $.ajax({
            url: portal.HTTP_PREFIX + "/ledgerPrepackNo/ledgerPrepackNoSave",
            type: "PUT",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify(subBusiness),
            success: function(returnVal){
            	$("#k_window").data("kendoWindow").close();
            	if(returnVal.result.status == "true"){
			 		fsn.initNotificationMes("非预包装台账信息保存成功", true);
	                window.setTimeout("window.location.href = 'ledger_prepackNo.html'", 1000); 
                }
                else {
			 		lims.initNotificationMes("非预包装台账保存失败", false);
                }
            }
        });
        
    };
    
    fsn.clearErrorStyle=function(){
    	if(fsn.errorInputId){
    		$("#"+fsn.errorInputId).removeAttr("style");
    	}
    };
    
     /* 校验数据的有效性 */
   ledger.validateFormat = function() {
    	if(!ledger.validateMyDate()){ return false; }
        return true;
    };
    ledger.validateMyDate = function(){
     	if(!$("#productName").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('产品名称不能为空！',false);
    		return false;
    	}
    	if(!$("#standard").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('规格不能为空！',false);
    		return false;
    	}
    	if(!$("#number").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('数量不能为空！',false);
    		return false;
    	}
    	if(!$("#companyName").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('供货商名称不能为空！',false);
    		return false;
    	}
    	if(!$("#companyPhone").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('供货商联系电话不能为空！',false);
    		return false;
    	}
    	if(ledger.validaTetelephone()){
    		return false;
    	}
    	
    	if(!$("#supplier").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('供货商联系人不能为空！',false);
    		return false;
    	}
    	if(!$("#companyAddress").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('供货商地址不能为空！',false);
    		return false;
    	}
    	if(!$("#purchaseTime").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('采购时间不能为空！',false);
    		return false;
    	}
    	if(fsn.validateDateFormat("purchaseTime"))
    	{
    		fsn.initNotificationMes('采购时间的日期格式不对！',false); 
    		return false;
    	}
//    	if(ledger.aryProLicImg[0]==undefined||ledger.aryProLicImg[0]==null||ledger.aryProLicImg[0].fileName==undefined){
//    		fsn.initNotificationMes('请上传营业执照！',false); 
//    		return false;
//    	};
    	return true;
     };
     /*验证某个字段时，添加或取消输入框的样式
     * formId:输入框的id，msg：提示信息，valiResult：验证结果，true或false 
     */
    ledger.validateInputStyle=function(formId,msg,valiResult){
    	if(!valiResult){
    		var parentDiv=$("#"+formId).parent();
    		parentDiv.find("span").remove();
  		  	var span=$("<span class='fieldFormatStyle'>"+msg+"</span>");
  		  	parentDiv.append(span);
    	}else{
    		var span=$("#"+formId).next();
    		if(span.length>0){span.remove();}
    	}
    };
    /* 验证手机号码是否正常  */
   ledger.validaTetelephone = function(){
	  var isPhone= /^(^(\d{3,4}-)?\d{7,8})$|(^(0|86|17951)?(13[0-9]|15[0123456789]|18[0123456789]|14[57])[0-9]{8}$)/;
        if(($("#companyPhone").val().trim())!=""){
             if(!(($("#companyPhone").val().trim()).match(isPhone))){
            	 ledger.validateInputStyle("companyPhone","供货商联系电话不正确！",false);
            	 fsn.initNotificationMes('供货商联系电话不正确！',false);
            	 return true;
             }else{
            	 ledger.validateInputStyle("companyPhone","",true);
            	 return false;
             }
        }
        return false;
    };
    //验证日期格式是否有效
    fsn.validateDateFormat=function(formId){
    	if($("#"+formId).val().trim().length>0){
    		var date = $("#"+formId).val().trim();
    		var result = date.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
    		if(result==null){
    			return true;
    		}else{
    			return false;
    		}
    	}
    };
    //===========================================================================================================================
    ledger.initUploadfileImg = function(){
    	$(".upload_licImg_files").each(function(){
    		if(!$(this).data("kendoUpload")){
    			if(ledger.aryProLicImg.length==0){
    				ledger.aryProLicImg[0]={};
    			}
    			ledger.buildUpload(this, ledger.aryProLicImg, "IMG","上传'营业执照'或<br>'统一信用代码",0);
//    			if($(".ListView").size()!=0){
//    				$(this).data("kendoUpload").disable();
//    			}
    		}
    	});
//		$(".upload_qsImg_files").each(function(){
//			var index=parseInt($(this).parents(".delDiv").attr("index"));
//			if(!$(this).data("kendoUpload")){
//				if(ledger.aryProQsImg[index]==null){
//					ledger.aryProQsImg[index]={};
//				}
//				ledger.buildUpload(this, ledger.aryProQsImg, "IMG","上传'生产许可证'",index);
//				if($(this).parents(".delDiv").find(".qsImgListView").size()!=0){
//					$(this).data("kendoUpload").disable();
//				}
//			}
//		});
    	$(".upload_disImg_files").each(function(){
    		if(!$(this).data("kendoUpload")){
    			if(ledger.aryProDisImg.length == 0){
    				ledger.aryProDisImg[0]={};
    			}
    			ledger.buildUpload(this, ledger.aryProDisImg, "IMG","上传'食品流通许可证'或<br>'食品经营许可证'",0);
//    			if($(".ListView").size()!=0){
//    				$(this).data("kendoUpload").disable();
//    			}
    		}
    	});
    };
    
    ledger.buildUpload=function(id, attachments,flag,selectLocal,index){
		$(id).kendoUpload({
       	 async: {
                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments",
       	 },localization: {
                select:selectLocal,//id=="upload_product_files"?fsn.l("upload_Product"):fsn.l("upload_cert"),
                retry:lims.l("retry",'upload'),
                remove:lims.l("remove",'upload'),
                cancel:lims.l("cancel",'upload'),
                dropFilesHere: lims.l("drop files here to upload",'upload'),
                statusFailed: lims.l("failed",'upload'),
                statusUploaded: lims.l("uploaded",'upload'),
                statusUploading: lims.l("uploading",'upload'),
                uploadSelectedFiles: lims.l("Upload files",'upload'),
            },
            multiple:false,
            upload: function(e){
                var files = e.files;
                 $.each(files, function () {
                     var extension = this.extension.toLowerCase();
                     if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
                     lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
                     e.preventDefault();
                 }
               });
            },
            success: function(e){
		           	 if(e.response.typeEror){
		           		lims.initNotificationMes("文件类型有误，该文件不会被保存，请删除后重新上传：.png .bmp .jpeg .jpg格式的文件！", false);
		           		 return;
		           	 }
		           	 if(e.response.morSize){
		           		lims.initNotificationMes("您上传的文件已经超过10M，请删除后重新上传pdf、png、bmp、jpeg .jpg格式的文件!", false);
		           		 return;
		           	 }
		             if (e.operation == "upload") {
		            	 attachments[0]=e.response.results[0];
		                 lims.initNotificationMes("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )", true);
		            }else if(e.operation == "remove"){
		            	attachments[0]={};
		             }
            },
            remove:function(e){
            	lims.initNotificationMes("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )", true);
            },
            error:function(e){
            	lims.initNotificationMes(fsn.l("An exception occurred while uploading the file! Please try again later ..."),false);
            },
      });
    };
    ledger.setImgResource = function(licResource,disResource){
    	var licdataSource = new kendo.data.DataSource();
    	ledger.aryProLicImg.length=0;
   	 if(licResource!=null){
   		 $("#licImgListView").show();
   			ledger.aryProLicImg.push(licResource);
   			licdataSource.add({attachments:licResource});
   	 }else{
   		 $("#licImgListView").hide();
   	 }
   	 $("#licImgListView").kendoListView({
            dataSource: licdataSource,
            template:kendo.template($("#ImgFilesTemplate").html()),
        });
   	 var disdataSource = new kendo.data.DataSource();
   	 ledger.aryProDisImg.length=0;
   	 if(disResource!=null){
   		 $("#disImgListView").show();
   		 ledger.aryProDisImg.push(disResource);
   		 disdataSource.add({attachments:disResource});
   	 }else{
   		 $("#disImgListView").hide();
   	 }
   	 $("#disImgListView").kendoListView({
   		 dataSource: disdataSource,
   		 template:kendo.template($("#ImgFilesTemplate").html()),
   	 });
    };
    
    /**
     * 从页面删除产品图片
     */
    ledger.removeRes = function(resID){
//		 var dataSource = new kendo.data.DataSource();
		 
		 if(ledger.aryProLicImg!=undefined && ledger.aryProLicImg.length>0 && ledger.aryProLicImg[0].id == resID){
			 ledger.aryProLicImg[0] = {}; 
			 ledger.aryProLicImg.pop();
			 $("#licImgListView").hide();
			 
//			 $("#licImgFilesTemplate").data("kendoListView").setDataSource([]); 
		 }
		 if(ledger.aryProDisImg!=undefined && ledger.aryProDisImg.length>0 && ledger.aryProDisImg[0].id == resID){
			 ledger.aryProDisImg[0] = {}; 
			 ledger.aryProDisImg.pop();
			 $("#disImgListView").hide();
//			 $("#disImgFilesTemplate").data("kendoListView").setDataSource([]); 
		 }
//		 for(var i=0; i<ledger.aryProDisImg.length; i++){
//	   	 if(ledger.aryProDisImg[i].id == resID){
//	   		 while((i+1)<ledger.aryProDisImg.length){
//	   			ledger.aryProDisImg[i] = ledger.aryProDisImg[i+1];
//	   			 i++;
//	   		 }
//	   		ledger.aryProDisImg.pop();
//	   		 break;
//	   	 }
//	    }
//		 if(portal.aryProAttachments.length>0){
//			for(i=0; i<portal.aryProAttachments.length; i++){
//				dataSource.add({attachments:portal.aryProAttachments[i]});
//			}
//		 }
//		 $("#ImgFilesTemplate").data("kendoListView").setDataSource(dataSource);
//			if(portal.aryProAttachments.length == 0){
//				$("#logListView").hide();
//		 }
    };
    //===========================================================================================================================
	ledger.initialize();
});
