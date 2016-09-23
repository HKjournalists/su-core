(function($){
	var lims = window.lims = window.lims || {};

	if (!lims.zh) {
		lims.zh = {};
	}
    $.extend(true,lims.zh,{
    	"Id":"序号",
    	"Name":"名称",
    	"Format":"规格型态",
    	"Amount":"数量",
    	"Health":"完好状态",
    	"Place":"使用场所",
    	"Producer":"生产厂及国别",
    	"ProducerDate":"生产日期",
    	"PerchaseDate":"购置日期",    
        "Operation" : "操作",
        "Edit" : "编辑",
        "Delete" : "删除",
        "WIN_CONFIRM":"确认窗口",
        "WIN_BATCHCONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;批量删除当前选中的生产设备配置, 确定继续吗?",
        "WIN_CONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;删除当前选中的生产设备配置, 确定继续吗?",
        "Choose Nutrition" : "请选择要删除的生产设备配置！",
        "Selected Nutrition" : "所选生产设备配置",
        "Delete Success":"删除成功!",
        "Delete Failed":"删除失败!",
        "Currently field " : "当前字段 ",
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
        "User Feedback" : "用户反馈",
        "Number of User Complaint" : "用户抱怨数量",
    });
    
	$(document).ready(function() {
		lims.updateLocale();
	});
    
})(jQuery);