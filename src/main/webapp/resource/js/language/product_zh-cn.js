(function($){
    var fsn = window.fsn = window.fsn || {};
        
    if(!fsn.zh){
        fsn.zh = {};
    }
    
    $.extend(true,fsn.zh,{
    	"Product List" : "产品列表",
    	"Update Product:" : "更新产品：",
    	"Status" : "状态",
    	"Format" : "规格",
    	"Regularity" : "标准化规则",
    	"Barcode" : "条形码",
    	"Business Brand" : "企业品牌",
    	"Fda Product Group" : "管理局产品组",
    	"Note" : "备注",
    	"Add Product" : "添加产品",
    	"Save" : "保存",
    	"Reset" : "清空",
    	"Add New Instance" : "添加新实例",
    	"Product Name" : "产品名",
    	"Batch Serial No" : "批序列号",
    	"Serial" : "序列号",
    	"Production Date" : "生产日期",
    	"Expiration Date" : "有效期限",
    	"Product" : "产品",
    	"Original" : "原型",
    	"Product Image" : "产品图片",
    	"Select..." : "选择...",
    	"Update Product Instance:" : "更新产品实例",
    	"Cancel" : "取消",
    	"Upload files" : "上传",
    	
    	
    	
        "Management Fields (Roles - Sales Manager): " : "管理字段（角色 - 业务经理）：",
        "Add Business Unit" : "新增企业",
        "ID" : "序号",
        "Name" : "名称",
        "Address" : "地址",
        "Address2" : "地址2",
        "Operation" : "操作",
        "Create By" : "创建用户",
        "Last Upate Time" : "最后更新时间",
        "Edit" : "编辑",
        "Delete" : "删除",
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
        
        "updated sucessfully！":"更新数据成功！",
        "created sucessfully！":"增加数据成功！",
        
    });
    
	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);