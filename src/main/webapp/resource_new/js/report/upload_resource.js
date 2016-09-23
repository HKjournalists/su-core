var lims = window.lims = window.lims || {}; // 全局命名空间
var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var root = window.lims.root = window.lims.root || {}; // root命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀

/**
 * 上传控件初始化
 * @author ZhangHui 2015/4/9
 */
root.buildUpload = function(id, attachments, msg, flag){
    $("#" + id).kendoUpload({
        async: {
            saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
            removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
            autoUpload: true,
            removeVerb: "POST",
            removeField: "fileNames",
            saveField: "attachments",
        },
        localization: {
            select: lims.l(id),
            retry: lims.l("retry", 'upload'),
            remove: lims.l("remove", 'upload'),
            cancel: lims.l("cancel", 'upload'),
            dropFilesHere: lims.l("drop files here to upload", 'upload'),
            statusFailed: lims.l("failed", 'upload'),
            statusUploaded: lims.l("uploaded", 'upload'),
            statusUploading: lims.l("uploading", 'upload'),
            uploadSelectedFiles: lims.l("Upload files", 'upload'),
        },
        multiple: id == "upload_report_files" ? false : true,
        upload: function(e){
        	/**
        	 * 执行图片合成pdf功能的上传图片之前，屏蔽相关按钮的操作
        	 * @author ZhangHui 2015/5/7
        	 */
        	if (id == "uploadRepPics") {
                lims.hidePicToPdfButton();
            }
        	/**
        	 * 上传文件格式校验
        	 */
            var files = e.files;
            $.each(files, function(){
            	if(this.name.length > 100){
       		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
                    e.preventDefault();
                    if (id == "uploadRepPics") {
                    	/* 在每次为按钮绑定事件之前，先删除之前绑定的事件，避免点击按钮时触发两次事件 */
                        lims.hidePicToPdfButton();
                        // 倘若图片合成弹出框的上传动作是否完成，如果完成则显示操作按钮
                        lims.showPicToPdfButton();
                    }
                    return;
       	  		}
                /*文件扩展名*/
                var extension = this.extension.toLowerCase();
                if (this.size > 10400000) {
                    lims.initNotificationMes('单个文件的大小不能超过10M！', false);
                    e.preventDefault();
                    if (id == "uploadRepPics") {
                    	/* 在每次为按钮绑定事件之前，先删除之前绑定的事件，避免点击按钮时触发两次事件 */
                        lims.hidePicToPdfButton();
                        // 倘若图片合成弹出框的上传动作是否完成，如果完成则显示操作按钮
                        lims.showPicToPdfButton();
                    }
                    return;
                }
                if (flag == "product") {
                    if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
                        lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
                        e.preventDefault();
                        return;
                    }
                }
                else 
                    if (flag == "report") {
                        if (id == "upload_report_files") {
                            if (extension != ".pdf") {
                                lims.initNotificationMes('此处只能上传pdf文件。', false);
                                e.preventDefault();
                                return;
                            }
                            if (root.aryRepAttachments.length > 0) {
                                lims.initNotificationMes('你已经上传了一份pdf了，如果要上传新的pdf，请先删除原有的pdf文件。', false);
                                e.preventDefault();
                                return;
                            }
                            lims.addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
                        } else if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
                                lims.initNotificationMes('文件格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
                                e.preventDefault();
                                if (id == "uploadRepPics") {
                                	/* 在每次为按钮绑定事件之前，先删除之前绑定的事件，避免点击按钮时触发两次事件 */
                                    lims.hidePicToPdfButton();
                                    // 倘若图片合成弹出框的上传动作是否完成，如果完成则显示操作按钮
                                    lims.showPicToPdfButton();
                                }
                                return;
                        }
                    } else if (extension != ".xls") {
                            lims.initNotificationMes('请确保你上传的是Excel文件，并且是2003版本。', false);
                            e.preventDefault();
                            return;
                    }
            });
        },
        success: function(e){
            if (e.operation == "upload") {
                fsn.endTime = new Date();
                if (flag == "items") {
                    lims.setEasyItems(e.response.results);
                    if (e.response.results == null || e.response.results.length < 1) {
                        lims.initNotificationMes('excel文件中没有可以导入的检测项目！', false);
                    }
                    $("#import_Excel_Win").data("kendoWindow").close();
                } else {
                    attachments.push(e.response.results[0]);
                    if (flag == "product") {
                        $("#" + msg).html("文件识别成功，可以保存!");
                    } else {
                        if (id == "uploadRepPics") {
                            /*图片合成pdf功能，再次初始化上传控件*/
                            lims.initPicToPdfUpload(e.response.results[0], root.aryRepAttachments);
                            /* 在每次为按钮绑定事件之前，先删除之前绑定的事件，避免点击按钮时触发两次事件 */
                            lims.hidePicToPdfButton();
                            // 倘若图片合成弹出框的上传动作是否完成，如果完成则显示操作按钮
                            lims.showPicToPdfButton();
                        }
                        $("#" + msg).html(lims.l("Document recognition is successful, you can save!"));
                    }
                }
            } else if (e.operation == "remove") {
                    if (attachments == null) {
                        return;
                    }
                    for (var i = 0; i < attachments.length; i++) {
                        if (attachments[i].name == e.files[0].name) {
                            while ((i + 1) < attachments.length) {
                                attachments[i] = attachments[i + 1];
                                i++;
                            }
                            attachments.pop();
                            break;
                        }
                    }
                }
        },
        remove: function(e){
            if (msg == "repFileMsg") {
                lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
                $("#" + msg).html("您可以：上传pdf文件，或上传图片自动合成pdf！");
            } else {
                $("#" + msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )");
            }
        },
        error: function(e){
            $("#" + msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
        },
    });
};

/**
 * 报告图片显示
 * @author ZhangHui 2015/4/3
 */
root.setRepAttachments = function(repAttachments){
    var dataSource = new kendo.data.DataSource();
    root.aryRepAttachments.length = 0;
    if (repAttachments.length > 0) {
        $("#btn_clearRepFiles").show();
        for (var i = 0; i < repAttachments.length; i++) {
            root.aryRepAttachments.push(repAttachments[i]);
            dataSource.add({
                attachments: repAttachments[i]
            });
        }
        lims.addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
    }
    $("#repAttachmentsListView").kendoListView({
        dataSource: dataSource,
        template: kendo.template($("#uploadedFilesTemplate").html()),
    });
};

/**
 * 进口食品卫生证书图片显示
 * @author LongXianZhen 2015/05/26
 */
root.setSanRepAttachments = function(repAttachments){
    var dataSource = new kendo.data.DataSource();
    root.sanPdfRepAttachments.length = 0;
    if (repAttachments.length > 0) {
       // $("#btn_clearRepFiles").show();
        for (var i = 0; i < repAttachments.length; i++) {
            root.sanPdfRepAttachments.push(repAttachments[i]);
            dataSource.add({
                attachments: repAttachments[i]
            });
        }
       // lims.addDisabledToBtn(["upload_report_files", "openT2P_btn"]);
    }
    $("#sanAttachmentsListView").kendoListView({
        dataSource: dataSource,
        template: kendo.template($("#uploadedSanFilesTemplate").html()),
    });
};

root.removeAttr = function(resID, fileName,imgList,viewId,imgListName){ //从页面删除
    root.isPro = false;
    var dataSource = new kendo.data.DataSource();
    for (var i = 0; i < imgList.length; i++) {
        if ((resID != null && imgList[i].id == resID) ||
        imgList[i].fileName == fileName) {
            root.isPro = true;
            while ((i + 1) < imgList.length) {
                imgList[i] = imgList[i + 1];
                i++;
            }
            imgList.pop();
            break;
        }
    }
    if (!root.isPro) {
        for (var i = 0; i < imgList.length; i++) {
            if ((resID != null && imgList[i].id == resID) ||
            imgList[i].fileName == fileName) {
                isPro = false;
                while ((i + 1) < imgList.length) {
                    imgList[i] = imgList[i + 1];
                    i++;
                }
                imgList.pop();
                break;
            }
        }
        if (imgList.length > 0) {
            for (i = 0; i < imgList.length; i++) {
                dataSource.add({
                    attachments: imgList[i],
                    viewId:viewId,
                    imgList:imgList[i]
                });
            }
        }
        $("#repAttachmentsListView").data("kendoListView").setDataSource(dataSource);
        if (imgList.length == 0) {
            //$("#btn_clearRepFiles").hide();
            //fsn.pdfRes = null;
            //lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
        }
    }
    else {
        if (imgList.length > 0) {
            for (i = 0; i < imgList.length; i++) {
                dataSource.add({
                    attachments: imgList[i],
                    viewId:viewId,
                    imgList:imgListName
                });
            }
        }
        $("#"+viewId).data("kendoListView").setDataSource(dataSource);
    }
};

/**
 * 删除页面图片
 */
root.removeRes = function(resID, fileName){ //从页面删除
    root.isPro = false;
    var dataSource = new kendo.data.DataSource();
    for (var i = 0; i < root.aryProAttachments.length; i++) {
        if ((resID != null && root.aryProAttachments[i].id == resID) ||
        root.aryProAttachments[i].fileName == fileName) {
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
            if ((resID != null && root.aryRepAttachments[i].id == resID) ||
            root.aryRepAttachments[i].fileName == fileName) {
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
    }
    else {
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
 * 从页面中文标签图片
 */
root.removeSanRes = function(resID){
	 var dataSource = new kendo.data.DataSource();
	 for(var i=0; i<root.sanPdfRepAttachments.length; i++){
   	 if(root.sanPdfRepAttachments[i].id == resID){
   		 while((i+1)<root.sanRepAttachments.length){
   			root.sanPdfRepAttachments[i] = root.sanPdfRepAttachments[i+1];
   			 i++;
   		 }
   		root.sanPdfRepAttachments.pop();
   		 break;
   	 }
    }
	 
	 if(root.sanPdfRepAttachments.length>0){
		for(i=0; i<root.sanPdfRepAttachments.length; i++){
			dataSource.add({attachments:root.sanPdfRepAttachments[i]});
		}
	 }else{
		 lims.removeDisabledToBtn(["openS2P_btn"]);
	 }
	 $("#sanAttachmentsListView").data("kendoListView").setDataSource(dataSource);
		if(root.sanPdfRepAttachments.length == 0){
			$("#SanListView").hide();
	 }
};

/**
 * 清空报告资源
 * @author ZhangHui 2015/4/30
 */
root.clearRepFiles = function(){
    root.aryRepAttachments.length = 0;
    fsn.pdfRes = null;
    lims.removeDisabledToBtn(["upload_report_files", "openT2P_btn"]);
	if($("#repAttachmentsListView").data("kendoListView"))
    $("#repAttachmentsListView").data("kendoListView").dataSource.data([]);
    $("#btn_clearRepFiles").hide();
};

/**
 * 清空产品资源
 * @author ZhangHui 2015/4/30
 */
root.clearProFiles = function() {
    root.aryProAttachments.length = 0;
    if ($("#proAttachmentsListView").data("kendoListView")) {
        $("#proAttachmentsListView").data("kendoListView").dataSource.data([]);
    }
    $("#btn_clearProFiles").hide();
};