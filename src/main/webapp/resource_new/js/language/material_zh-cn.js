(function($){
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}
    
    $.extend(true,fsn.zh,{
    	
        "Management Fields (Roles - Sales Manager): " : "管理字段（角色 - 业务经理）：",
        "Number" : "序号",
        "Id" : "序号",
        "Name" : "名称",
        "Format":"规格型号",
        "AnnualNeed":"年需要量",
        "StandardCode":"执行标准代号",
        "Producer":"生产厂",
        "Country":"国别",
        "LicenseNo":"生产许可证编号",
        "Save" : "保存",
    	"Reset" : "清空",
        "Added" : "新增",
        "confirm" :"确定",
        "cancel" : "取消",
        "Operation" : "操作",
        "Create By" : "创建用户",
        "Last Upate Time" : "最后更新时间",
        "Edit" : "编辑",
        "Delete" : "删除",
        "WIN_CONFIRM":"确认窗口",
        "WIN_BATCHCONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;批量删除当前选中原材料和包装材料, 确定继续吗?",
        "WIN_CONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;删除当前原材料和包装材料, 确定继续吗?",
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
        "Material Name" : "材料名称",
        "materialNo":"唯一标识号",
        "inventoryCount":"库存量",
        "inventoryUnit" : "库存单位",
        "Buy" : "购买",
        "Check the purchase history" : "查看购买历史",
        "Please choose a raw materials information!" : "请选择一条原材料信息！",
        "Supplier name" : "供货商名称",
        "The supplier address" :"供货商地址",
        "Batch no":"批次号",
        "Price": "单价",
        "Buying" : "购买量",
        "Total cost": "总费用",
        "Buy time" : "购买时间",
        "Buyer" : "购买者",
        "Raw material purchase history preview" :"原材料购买历史预览",
        "Are you sure you want to delete this?" : "你确定要删除此项吗？",
    });
    
	$(document).ready(function() {
		fsn.updateLocale();
        console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);