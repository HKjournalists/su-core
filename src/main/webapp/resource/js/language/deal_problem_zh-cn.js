(function($){
    var fsn = window.fsn = window.fsn || {};
        
    if(!fsn.zh){
        fsn.zh = {};
    }
    
    $.extend(true,fsn.zh,{
    	//需要要显示的字段
    	"ID" : "序号",
    	"Privence" : "省",
    	"City" : "市",
    	"Counties" : "区/县",
    	"ProName" : "产品名称",
    	"Business Name" : "企业名称",
    	
    	"License No":"营业执照",
    	"Barcode" : "产品条形码",
    	"Problem Type" : "问题类型",
    	"Product Time" : "生产日期",
    	"Create Time" : "发现时间",
    	"Request Deal Time" : "要求处理时间",
        "Origin" : "信息来源",
        "Operation" : "操作",
        "Search" : "查询",
        "Remark" : "备注",
        "NOTICE EXAMINE" : "通知监管",
        "NOTICE BUSINESS" : "通知企业",
        "IGNORE" : "忽略",
        "COMMIT COMPLETE" : "已提交到",
        "DEAL STATUS" : "状态",
        "RECOVER" : "恢复",
        "REMOVE" : "删除",
        
        
        //page information
        "No items to display" : "没有纪录显示",
        "items per page" : "条每页",
        "of {0}" : "of {0}",
        "Go to the last page" : "最后一页",
        "Go to the first page" : "第一页",
        "Go to the next page" : "后一页",
        "Go to the previous page": "前一页",
        "Refresh" : "刷新",
        "Page" : "页",
        "{0} - {1} of {2} items" : "{0} - {1} 共 {2} 条",
        
        "updated sucessfully！":"更新数据成功！",
        "created sucessfully！":"增加数据成功！",
        
    });
    
	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);