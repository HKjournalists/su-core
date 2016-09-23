(function($) {
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}

	$.extend(true, fsn.zh, {
		"WIN_CONFIRM":"警告",
		"Operation":"操作",
		"Id":"序号",
		"Upload Picture":"图片上传",
		"Upload File":"上传文件",
	});
	
	$(document).ready(function() {
		fsn.updateLocale();
	});
	
})(jQuery);