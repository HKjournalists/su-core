(function($){
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}
    
    $.extend(true,fsn.zh,{
    	"Nutrition Index Status":"营养指数计算状态",
    	"WIN_RECALCULATEDCONFIRM_MSG":"当前产品已经计算过营养指数，您确定要重新计算一次吗？",
    	"Recalculated":"重新计算",
    	"Id" : "序号",
    	"Product List" : "产品列表",
    	"Update Product:" : "更新产品：",
    	"Category" : "产品分类",
    	"License Id" : "许可证编号",
    	"License Start" : "发证日期",
    	"License End" : "有效期至",
    	"Produce Status" : "生产状态",
    	"Regularity Category" : "标准分类",
    	"Status" : "状态",
    	"Format" : "规格",
    	"Regularity" : "产品执行标准",
    	"Barcode" : "条形码",
    	"Appropriate Crowd" : "适宜人群",
    	"Business Brand" : "企业品牌",
    	"Fda Product Group" : "管理局产品组",
    	"Note" : "备注",
    	"Add Product" : "添加产品",
    	"Save" : "保存",
    	"Reset" : "清空",
    	"Add New Instance" : "添加新实例",
    	"Product Name" : "产品名称",
    	"Batch Serial No" : "批序列号",
    	"Serial" : "序列号",
    	"Production Date" : "生产日期",
    	"Expiration Date" : "有效期限",
    	"Product" : "产品",
    	"Original" : "原型",
    	"Ingredient" : "配料",
    	"selectedCustomers" : "销往客户",
    	"Product Image" : "产品图片",
    	"Select..." : "选择...",
    	"Update Product Instance:" : "更新产品实例",
    	"OK":"确认",
    	"Cancel" : "取消",
    	"Upload files" : "上传",
    	
    	"ProductInfo Id" : "序号",
    	"ProductInfo Cert Type" : "信息类型",
    	"ProductInfo Unit" : "发证机构",
    	"ProductInfo No" : "证书编号",
    	"ProductInfo End" : "有效期至",
    	
    	"Operation" : "操作",
        "Management Fields (Roles - Sales Manager): " : "管理字段（角色 - 业务经理）：",
        "Add Product" : "新增产品",
        "Name" : "名称",
        "Address" : "地址",
        "Address2" : "地址2",
        "Operation" : "操作",
        "Create By" : "创建用户",
        "Last Upate Time" : "最后更新时间",
        "Owned Business Brand" : "所属品牌",
        "Edit" : "编辑",
        "Delete" : "删除",
        "WIN_CONFIRM":"确认窗口",
        "WIN_BATCHCONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;批量删除当前选中产品, 确定继续吗?",
        "WIN_CONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;删除当前产品, 确定继续吗?",
        "Choose Product" : "请选择要删除的产品！",
        "Choose ProductInfo" : "请选择要删除的产品相关信息",
        "Selected ProductInfo" : "所选产品相关信息",
        "Selected ProductInfo" : "所选产品相关信息",
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
		fsn.updateLocale();
	});
    
})(jQuery);