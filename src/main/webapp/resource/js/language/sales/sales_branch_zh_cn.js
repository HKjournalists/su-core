(function($) {
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}

	$.extend(true, fsn.zh, {
		"Operation":"操作",
		"id":"序号",
		"Branch Name":"销售网点名称",
		"Address":"联系地址",
		"TelePhone":"联系电话",
		"Sales Address":"销售地址",
		"Sales Branch":"销售网点",
		"Contact":"联系人",
		"Buy Way Name":"购买方式",
		"Buy Way":"购买途径",
		"Main Address":"联系地址",
		"Street Address":"详细地址",
		"OK":"确认",
		"Cancel":"取消",
		"WIN_CONFIRM":"警告",
		"RecommendBuy Name":"推荐购买方式",
		"RecommendBuy Way":"推荐购买途径",
		"Back":"返回",
	});
	
	$(document).ready(function() {
		fsn.updateLocale();
	});
	
})(jQuery);