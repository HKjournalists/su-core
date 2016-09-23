$(document).ready(function(){
   // var root = window.lims.root = window.lims.root || {};
    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    var thirdReport = fsn.thirdReport = fsn.thirdReport || {}; // 全局命名空间 
    thirdReport.reportArry = new Array();
    thirdReport.checkArry = new Array();
    thirdReport.buyArry = new Array();
    /* 初始化页面 */
    thirdReport.initialize = function(){
        /* 初始化条形码下拉框 */
    	thirdReport.initialBarcodeComp();
    	thirdReport.buildUpload("report",thirdReport.reportArry,"请上传报告原件图片:");
    	thirdReport.buildUpload("check", thirdReport.checkArry,"请上传检测凭证图片:");
    	thirdReport.buildUpload("buy",thirdReport.buyArry,"请上传购买凭证图片:");
    };
    
    
    // 初始化条形码下拉框,向后台查询报告编码，根据报告编号上传改报告的详情图片
    thirdReport.initialBarcodeComp = function(){
    	$("#submit").on("click", thirdReport.savePhoto);
		$("#reportNo").kendoComboBox({
	        dataTextField: "serviceOrder",
	        dataValueField: "id",
	       // change:root.judgeProductByBarcode,
	        dataSource: [],
	        filter: "startswith",
	        minLength: 0,
	        index:0,
	    });
		$.ajax({
			url:portal.HTTP_PREFIX + "/thirdReport/searchReportNo",
			type:"GET",
			dataType:"json",
			async:false,
			success:function(returnValue){
				fsn.endTime = new Date();
				thirdReport.listOfBarCodes = returnValue.data;
				if(returnValue.status == "1"){
					console.log(returnValue.data)
					$("#reportNo").data("kendoComboBox").setDataSource(returnValue.data);
					$("#reportNo").data("kendoComboBox").refresh();
				}
			},
		});
	};
	
	thirdReport.buildUpload=function(id, attachments,selectLocal){
		$("#"+id).kendoUpload({
       	 async: {
                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/IMG" ,
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
            multiple:true,
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
		            	 /*for(var key in e.response.results){
		            		 attachments[key] = e.response.results[key];
		            	 }*/
		                 lims.initNotificationMes("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )", true);
		                 attachments.push(e.response.results[0]);
		            }else if(e.operation == "remove"){
		            	 for(var i=0; i<attachments.length; i++){
		                   	 	if(attachments[i].name == e.files[0].name){
		                   	 		while((i+1)<attachments.length){
		                   	 			attachments[i]=attachments[i+1];
		                   	 			i++;
		                   	 		}
		                   	 		attachments.pop();
		                   	 		break;
		                   	 	}
		                    }
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
    thirdReport.savePhoto = function(){
       var combobox = $("#reportNo").data("kendoComboBox").value();
       var comboboxTest = $("#reportNo").data("kendoComboBox").text();
       if(combobox == "" ){
    	   lims.initNotificationMes(fsn.l("请选择报告编号"),false);
    	   return;
    	}
       if(combobox==comboboxTest){
    	   lims.initNotificationMes(fsn.l("请选择报告编号,不能手动输入！"),false);
    	   return;
       }
       if(thirdReport.reportArry.length==0){
    	   lims.initNotificationMes(fsn.l("请上传报告原件！"),false);
    	   return;
       }
       if(thirdReport.checkArry.length==0){
    	   lims.initNotificationMes(fsn.l("请上传检测原件！"),false);
    	   return;
       }
       if(thirdReport.buyArry.length==0){
    	   lims.initNotificationMes(fsn.l("请上传购买凭证！"),false);
    	   return;
       }
		var data = thirdReport.savePhotoInfo();
    	$.ajax({
			url:portal.HTTP_PREFIX + "/thirdReport/save",
			type:"POST",
			dataType:"json",
			contentType:"application/json",
			async:false,
			data:JSON.stringify(data),
			success:function(returnValue){
				if(returnValue.status == true){
					lims.initNotificationMes(fsn.l("提交成功！"),true);
					thirdReport.addNext();
				}else{
					lims.initNotificationMes(fsn.l("提交失败！"),false);
				}
			},
		});
    	
    };
    thirdReport.savePhotoInfo = function(){
    	var comboboxTest = $("#reportNo").data("kendoComboBox").text();
    	var comboboxValue = $("#reportNo").data("kendoComboBox").value();
    	var reportCount = $("#reportCount").val().trim();
    	var dataReport = {
    			testResultId:comboboxValue,
    			serviceOrder:comboboxTest,
    			reportCount:reportCount,
    			reportArray:thirdReport.reportArry,
    			checkArray:thirdReport.checkArry,
    			buyArray:thirdReport.buyArry,
    	};
    	return dataReport;
    };
    thirdReport.addNext = function(){
        window.location.pathname = "/fsn-core/views/thirdReport/third_report_details.html";
    };
    thirdReport.initialize();
});
