(function($) {
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}

	$.extend(true, fsn.zh, {
		"Operation":"操作",
		"id":"序号",
		"Contract Name":"合同名称",
		"Update Time":"最近更新时间",
		"Note":"备注",
		"View":"预览",
		"Edit":"编辑",
		"Delete":"删除",
		"WIN_CONFIRM":"警告",
		"Upload Contract File":"上传合同原件",
		"Empty":"清空",
		"The current resource can not find the download path":"没有找到当前资源的下载路径",
	});
	
	$(document).ready(function() {
		fsn.updateLocale();
	});
	
})(jQuery);