(function($){
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}
    
    $.extend(true,fsn.zh,{
    	"Id" : "序号",
    	"Name":"名称",
    	"Brand":"品牌",
    	"Appropriate Crowd":"适宜人群",
    	"Ingredient":"营养成分",
    	"Status" : "状态",
    	"Format" : "规格",
    	"Regularity" : "产品执行标准",
    	"Barcode" : "条形码",
    	"Operation":"操作",
    	"Edit":"编辑",
    });
    
    $(document).ready(function() {
		fsn.updateLocale();
	});
    
})(jQuery);