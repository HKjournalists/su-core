$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var affirmOrder=window.excel=window.affirmOrder||{};
	var wdnServiceBaseUrl = fsn.getHttpPrefix()+"/sampling/import/";
	stepBar.init("stepBar", {
		step : 3,
		change : false	,
		animation : true
	});
});
