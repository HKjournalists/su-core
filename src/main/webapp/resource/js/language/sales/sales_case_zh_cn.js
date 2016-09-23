(function($) {
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}

	$.extend(true, fsn.zh, {
		"WIN_CONFIRM":"警告",
		"Operation":"操作",
		"Id":"序号",
		"Sales Case Name":"销售案例名称",
		"Sales Details":"销售详情",
		"Upload Picture":"图片上传",
		"Sales case name":"销售案例名称!",
		"Sales case name does not allow more than 100 characters":"销售案例名称不允许超过100字符!",
		"Sales case profile does not allow more than 200 characters":"销售案例简介不允许超过200字符!",
		"Sales case name already exists, please input again":"销售案例名称已经存在，请重新输入!"
	});
	
	$(document).ready(function() {
		fsn.updateLocale();
	});
	
})(jQuery);