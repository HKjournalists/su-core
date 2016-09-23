(function($) {
	var lims = window.lims = window.lims || {};
	var fsn = window.fsn = window.fsn || {};
	
	if (!fsn.zh||!lims.zh) {
		fsn.zh = {};
		lims.zh = {};
	}

	$.extend(true,lims.zh, {
		//汉化
		"Upload File:":"上传文件",
		"upload_OrgPhoto":"上传组织机构代码证件",
        "upload_LicensePhoto":"上传营业执照图片",
        "upload_DistributionPhoto":"上传流通许可证图片",
        "upload_QsPhoto":"上传生产许可证图片",
        "upload_TaxPhoto":"上传税务登记证图片",
        "upload_LiquorPhoto":"上传酒类销售许可证图片",
        "upload_proDep_files":"上传车间照片",
        
        //上传控件提示信息汉化
        "Wrong file type, the file will not be saved and re-upload pdf format Please delete!":"文件类型有误，该文件不会被保存，请删除后重新上传pdf格式文件！",
	    "Wrong file type, the file will not be saved, please delete the re-upload png, bmp, jpeg file format!":"文件类型有误，该文件不会被保存，请删除重新上传pdf、png、bmp、jpeg格式的文件!",
	    "Document recognition is successful, you can save!":"文件识别成功，可以保存！",
	    "Note: The file size can not exceed 10M! Supported file formats: png, bmp, jpeg":"注意:每个文件大小不能超过10M！  可支持的文件格式：png .bmp .jpeg .jpg！",
	    "An exception occurred while uploading the file! Please try again later ...":"上传文件时出现异常！请稍后再试...",
       
	    //grid page汉化
	    "No items to display": "没有纪录显示",
	    "{0} - {1} of {2} items": "{0} - {1} 共 {2} 条",
	});
	//汉化
	$.extend(true,fsn.zh, {
		 "empty":"清空",
	});
	
	$(document).ready(function() {
		lims.updateLocale();
		fsn.updateLocale();
	});
})(jQuery);