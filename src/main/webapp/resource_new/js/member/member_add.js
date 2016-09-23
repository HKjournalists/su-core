var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
var	upload = portal.upload = portal.upload || {};

/**
 * MD5校验数据是否被篡改过
 * @author HuangYog
 */
portal.md5validate = function(str1,md5code){
	var md5 = $.md5(str1);
	if(md5===md5code){
		return str1;
	}else if(str1 && md5code){
		lims.initNotificationMes("产品id有误，请从产品管理编辑进入。", false);
		return ;
	}
};


/**
 * 清空产品图片资源
 * @author TangXin
 */
portal.clearProFiles=function(){
	portal.aryProAttachments.length=0;
	$("#proAttachmentsListView").data("kendoListView").dataSource.data([]);
	$("#logListView").hide();
};

/**
 * 初始化证件照图片，页面显示
 */
portal.setHdAttachments=function(hdAttachments){
	 var dataSource = new kendo.data.DataSource();
	 portal.aryHeadAttachments.length=0;
	 if(hdAttachments.length>0){
		 for(var i=0;i<hdAttachments.length;i++){
			 portal.aryHeadAttachments.push(hdAttachments[i]);
			 dataSource.add({attachments:hdAttachments[i]});
		 }
	 }
	  $("#hdImgList").kendoListView({
         dataSource: dataSource,
         template:kendo.template($("#uploadedHdFilesTemplate").html()),
     });
	if(portal.aryHeadAttachments.length == 0){
			$("#hdImgListtr").hide();
	 }
 };
/**
 * 初始化健康证图片，页面显示
 */
portal.setHthAttachments=function(hthAttachments){
	 var dataSource = new kendo.data.DataSource();
	 portal.aryHealthAttachments.length=0;
	 if(hthAttachments.length>0){
		 for(var i=0;i<hthAttachments.length;i++){
		 	$("#healthNo").val(hthAttachments[i].origin);
			 portal.aryHealthAttachments.push(hthAttachments[i]);
			 dataSource.add({attachments:hthAttachments[i]});
		 }
	 }
	  $("#hthImgList").kendoListView({
         dataSource: dataSource,
         template:kendo.template($("#uploadedHthFilesTemplate").html()),
     });
	if(portal.aryHeadAttachments.length == 0){
			$("#hthImgListtr").hide();
	 }
 };
/**
 * 初始化从业资格证图片，页面显示
 */
portal.setQcAttachments=function(qcAttachments){
	 var dataSource = new kendo.data.DataSource();
	 portal.aryWorkAttachments.length=0;
	 if(qcAttachments.length>0){
		 for(var i=0;i<qcAttachments.length;i++){
		 	$("#qualificationNo").val(qcAttachments[i].origin);
			 portal.aryWorkAttachments.push(qcAttachments[i]);
			 dataSource.add({attachments:qcAttachments[i]});
		 }
	 }
	  $("#qcImgList").kendoListView({
         dataSource: dataSource,
         template:kendo.template($("#uploadedQcFilesTemplate").html()),
     });
		if(portal.aryWorkAttachments.length == 0){
			$("#qcImgListtr").hide();
	 }
 };
/**
 * 荣誉证书图片，页面显示
 */
portal.setHnAttachments=function(hnAttachments){
	 var dataSource = new kendo.data.DataSource();
	 portal.aryHonorAttachments.length=0;
	 if(hnAttachments.length>0){
		 for(var i=0;i<hnAttachments.length;i++){
		 	if(i==0){
		 		$("#honorNo1").val(hnAttachments[i].origin);
		 	}
		 	if(i==1){
		 		$("#honorNo2").val(hnAttachments[i].origin);
		 	}
		 	if(i==2){
		 		$("#honorNo3").val(hnAttachments[i].origin);
		 	}
			 portal.aryHonorAttachments.push(hnAttachments[i]);
			 dataSource.add({attachments:hnAttachments[i]});
		 }
	 }
	  $("#hnImgList").kendoListView({
         dataSource: dataSource,
         template:kendo.template($("#uploadedHnFilesTemplate").html()),
     });
	if(portal.aryHonorAttachments.length == 0){
			$("#hnImgListtr").hide();
	 }
 };

/**
 * 编辑产品时，页面赋值操作
 */
portal.writeMemberInfo = function (data) {
	if (data!=null) {
		$.each(data, function(k,v){
			if (!portal.isBlank(v)) {
				switch (k) {
				case "id":
					$("#id").val(v);
					break;
				case "name":
					$("#name_q").val(v);
					$("#name").html(v);
					break;
				case "credentialsType":
					$("#credentialsType_q").data("kendoDropDownList").text(v);
					$("#credentialsType").html(v);
					break;
				case "position":
					$("#position").data("kendoDropDownList").text(v);
					break;
				case "sex":
					$("#sex").data("kendoDropDownList").text(v);
					break;
				case "nation":
					$("#nation").data("kendoComboBox").text(v);
					break;
				case "personType":
					$("#personType").data("kendoDropDownList").text(v);
					break;
				case "description":
					$("#description").val(v);
					break;
				case "appointUnit":
					$("#appointUnit").val(v);
					break;
				case "workType":
					$("#workType").val(v);
					break;
				case "issueUnit":
					$("#issueUnit").val(v);
					break;
				case "mobilePhone":
					$("#mobilePhone").val(v);
					break;
				case "identificationNo":
					$("#identificationNo_q").val(v);
					$("#identificationNo").html(v);
					break;
				case "tel":
					$("#tel").val(v);
					break;
				case "address":
					$("#address").val(v);
					break;
				case "email":
					$("#email").val(v);
					break;
				case "healthNo":
					$("#healthNo").val(v);
					$("#healthCertificateNo").val(v);
					break;
				case "qualificationNo":
					$("#qualificationNo").val(v);
					break;
				case "honorNo":
					$("#honorNo").val(v);
					break;
				}
			}
		});
		
	}
};


/**
 * 从页面删除证件照图片
 */
fsn.checkPhotoNum = function(type){
	if(type == '1' && portal.aryHeadAttachments.length>=1){
		fsn.initNotificationMes("只能上传一张证件照!",false);
		return false;
	}
	if(type == '2' && portal.aryHealthAttachments.length>=1){
		fsn.initNotificationMes("只能上传一张健康证!",false);
		return false;
	}
	if(type == '3' && portal.aryWorkAttachments.length>=1){
		fsn.initNotificationMes("只能上传一张从业资格证!",false);
		return false;
	}
	if(type == '4' && portal.aryHonorAttachments.length>=3){
		fsn.initNotificationMes("最多只能上传三张荣誉证书!",false);
		return false;
	}
	return true;
};
/**
 * 从页面删除证件照图片
 */
fsn.removeHdRes = function(resID){
	 var dataSource = new kendo.data.DataSource();
	 for(var i=0; i<portal.aryHeadAttachments.length; i++){
	   	 if(portal.aryHeadAttachments[i].id == resID){
	   		 while((i+1)<portal.aryHeadAttachments.length){
	   			 portal.aryHeadAttachments[i] = portal.aryHeadAttachments[i+1];
	   			 i++;
	   		 }
	   		 portal.aryHeadAttachments.pop();
	   		 break;
	   	 }
    }
	 
	 if(portal.aryHeadAttachments.length>0){
		for(i=0; i<portal.aryHeadAttachments.length; i++){
			dataSource.add({attachments:portal.aryHeadAttachments[i]});
		}
	 }
	 $("#hdImgList").data("kendoListView").setDataSource(dataSource);
	if(portal.aryHeadAttachments.length == 0){
			$("#logListView").hide();
	 }
};
/**
 * 从页面删除健康证图片
 */
fsn.removeHthRes = function(resID){
	 var dataSource = new kendo.data.DataSource();
	 for(var i=0; i<portal.aryHealthAttachments.length; i++){
	   	 if(portal.aryHealthAttachments[i].id == resID){
	   		 while((i+1)<portal.aryHealthAttachments.length){
	   			 portal.aryHealthAttachments[i] = portal.aryHealthAttachments[i+1];
	   			 i++;
	   		 }
	   		 portal.aryHealthAttachments.pop();
	   		 break;
	   	 }
    }
	 
	 if(portal.aryHealthAttachments.length>0){
		for(i=0; i<portal.aryHealthAttachments.length; i++){
			dataSource.add({attachments:portal.aryHealthAttachments[i]});
		}
	 }
	 $("#hthImgList").data("kendoListView").setDataSource(dataSource);
	if(portal.aryHealthAttachments.length == 0){
			$("#hthImgListtr").hide();
	 }
};
/**
 * 从页面删除从业资格证图片
 */
fsn.removeQcRes = function(resID){
	 var dataSource = new kendo.data.DataSource();
	 for(var i=0; i<portal.aryWorkAttachments.length; i++){
	   	 if(portal.aryWorkAttachments[i].id == resID){
	   		 while((i+1)<portal.aryWorkAttachments.length){
	   			 portal.aryWorkAttachments[i] = portal.aryWorkAttachments[i+1];
	   			 i++;
	   		 }
	   		 portal.aryWorkAttachments.pop();
	   		 break;
	   	 }
    }
	 
	 if(portal.aryWorkAttachments.length>0){
		for(i=0; i<portal.aryWorkAttachments.length; i++){
			dataSource.add({attachments:portal.aryWorkAttachments[i]});
		}
	 }
	 $("#qcImgList").data("kendoListView").setDataSource(dataSource);
	if(portal.aryWorkAttachments.length == 0){
			$("#logListView").hide();
	 }
};
/**
 * 从页面删除荣誉证书图片
 */
fsn.removeHnRes = function(resID){
	 var dataSource = new kendo.data.DataSource();
	 for(var i=0; i<portal.aryHonorAttachments.length; i++){
	   	 if(portal.aryHonorAttachments[i].id == resID){
	   		 while((i+1)<portal.aryHonorAttachments.length){
	   			 portal.aryHonorAttachments[i] = portal.aryHonorAttachments[i+1];
	   			 i++;
	   		 }
	   		 portal.aryHonorAttachments.pop();
	   		 break;
	   	 }
    }
	 
	 if(portal.aryHonorAttachments.length>0){
		for(i=0; i<portal.aryHonorAttachments.length; i++){
			dataSource.add({attachments:portal.aryHonorAttachments[i]});
		}
	 }
	 $("#hnImgList").data("kendoListView").setDataSource(dataSource);
	if(portal.aryHonorAttachments.length == 0){
			$("#logListView").hide();
	 }
};
 
 /**
  * 验证指定的值只能是整数或者浮点数
  * @author tangxin
  */
portal.validateIntegerAndfractional = function (value,target){
	if(value == null || value == ""){
		return;
	}
	var regStr = /^[0-9]*$|([0-9]*(\.)[0-9]*)$/;
	if(!regStr.test(value)){
		 if(portal.globNutrModel != null){
			 if(target == "nrv"){
				 portal.globNutrModel.nrv = "";
			 }else if(target == "value"){
				 portal.globNutrModel.value = "";
			 }
		 }
		 fsn.initNotificationMes(fsn.l("Enter only integer or fractional")+"!",false);
	 }
};