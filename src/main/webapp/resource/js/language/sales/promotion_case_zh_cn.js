(function($) {
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}

	$.extend(true, fsn.zh, {
		"WIN_CONFIRM":"警告",
		"Operation":"操作",
		"Id":"序号",
		"Activity Name":"活动名称",
		"Activity Time":"活动时间",
		"Cover Area":"惠及地区",
		"Discount Time":"优惠时间",
		"Activity Introduction":"活动简介",
		"Upload Picture":"图片上传",
	});
	
	$(document).ready(function() {
		fsn.updateLocale();
	});
	
})(jQuery);