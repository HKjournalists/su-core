(function($) {
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}

	$.extend(true, fsn.zh, {
		"Business Name":"企业名称",
		"Business About":"企业简介",
		"Address":"企业地址",
		"Telephone":"联系电话",
		"Email":"联系邮箱",
		"Contact":"联系人",
		"Recommend":"推荐购买方式",
		"QR code":"企业二维码",
		"Send EMail":"发送邮件",
		"Download":"资料下载",
		"Contract Name":"合同名称",
		"Sales Address":"销售地址",
		"Sales Branch":"销售网点",
		"Contract Id":"合同编号",
		"Contract Note":"合同备注",
		"View":"预览",
		"Download":"下载",
		"Go Back":"返回",
		"Operation":"操作",
		"Close":"取消",
		"Addressee":"收件人",
		"Theme":"主题",
		"Annex":"附件",
		"Main Text":"正文",
		"The contract does not have the resources to download":"该合同没有资源可以下载",
		"The contract failed to download, please try again":"合同下载失败，请重试",
	});
	
	$(document).ready(function() {
		fsn.updateLocale();
	});
	
})(jQuery);