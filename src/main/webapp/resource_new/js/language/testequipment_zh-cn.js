(function($){
	var lims = window.lims = window.lims || {};

	if (!lims.zh) {
		lims.zh = {};
	}
    
    $.extend(true,lims.zh,{
    	
        "Management Fields (Roles - Sales Manager): " : "管理字段（角色 - 业务经理）：",
        "Number" : "序号",
        "Id" : "序号",
        "Name" : "名称",
        "Amount":"数量",
        "Health":"完好状态",
        "ExpirationDate":"检定有效截至日期",
        "Producer":"生产厂及国别",
        "ProduceDate":"生产日期",
        "PurchaseDate":"购置日期",
        "Save" : "保存",
    	"Reset" : "清空",
        
        "Operation" : "操作",
        "Create By" : "创建用户",
        "Last Upate Time" : "最后更新时间",
        "Edit" : "编辑",
        "Delete" : "删除",
        "WIN_CONFIRM":"确认窗口",
        "WIN_BATCHCONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;批量删除当前选中检测仪器设备, 确定继续吗?",
        "WIN_CONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;删除当前检测仪器设备, 确定继续吗?",
        "Choose Brand" : "请选择要删除的品牌！",
        "Selected Brand" : "所选品牌",
        "Delete Success":"删除成功!",
        "Delete Failed":"删除失败!",
        "Currently field " : "当前字段 ",
        " related by another business record, so " : " 已经与其他业务数据关联，所以",
        "cannot be remove" : "不允许删除 ",
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
    });
    
	$(document).ready(function() {
		lims.updateLocale();
	});
    
})(jQuery);