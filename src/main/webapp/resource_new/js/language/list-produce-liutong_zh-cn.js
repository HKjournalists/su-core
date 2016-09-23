(function($) {
	//var fsn = window.fsn = window.fsn || {};
	var lims = window.lims = window.lims || {};
	if (!lims.zh) {
		//fsn.zh = {};
		lims.zh = {};
	}

	$.extend(true,lims.zh, {
		//汉化
		"Producer Enterprise Information:":"生产企业信息：",
		"enterpriseName":"生产企业名称",
		"infoIsFull":"资料是否完整",
		"View Ditail":"查看详细信息",
		"Operation":"操作",
		"Organization Info":"组织机构信息",
		"organization code":"组织机构代码",
		"License Info":"营业执照信息",
		"License No":"营业执照号",
		"Production license":"生产许可证",
		"Production license No":"生产许可证编号",
		"upload Production license picture":"上传许可证图片",
		"uploadQsfiles":"上传许可证图片",
		"uploda picture":"上传图片",
		"uploadOrgfiles":"上传组织机构代码图片",
		"uploadLiceNofiles":"上传营业执照图片",
		"Save":"保存",
		"Cancel":"取消",
		"empty":"清空",
		
		//提示信息
		"Organization code certificate":"组织机构代码证照",
		"Business license":"营业执照号",
		"the pictures cannot be empty":"图片不能为空",
		"Organization code cannot be empty":"组织机构代码不能为空",
		"The business license cannot be empty":"营业执照不能为空",
		"Saved successfully":"保存成功",
		"Save failed":"保存失败",
		"Message":"消息",
		"Status":"状态",
		"The currently selected QS information is not allowed to delete, select the new QS information":"当前选中的qs信息不允许删除，请选择新增的qs信息",
		"Please select a row to delete QS information":"请选择一行要删除的QS信息",
	});
	
	$(document).ready(function() {
		//fsn.updateLocale();
		lims.updateLocale();
	});
})(jQuery);