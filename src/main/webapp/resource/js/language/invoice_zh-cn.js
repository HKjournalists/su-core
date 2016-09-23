(function($){
	var lims = window.lims = window.lims || {};

	if (!lims.zh) {
		lims.zh = {};
	}
    
    $.extend(true,lims.zh,{
    	"Business Unit" : "企业",
        "Management Fields (Roles - Sales Manager): " : "管理字段（角色 - 业务经理）：",
        "Add Business Unit" : "新增企业",
        "Number" : "序号",
        "Id" : "序号",
        "Name" : "名称",
        "UnitName":"企业名称",
        "RegisterAddress":"注册地址",
        "ProduceAddress":"生产地址",
        "Area":"所属区域",
        "Category":"主体分类",
        "Status":"状态",
        "Logo" : "标志",
        "Website" : "网址",
        "Type" : "类型",
        "Update Business Unit:" : "更新企业：",
        "Add New Brand" : "新增商品",
        "Identity" : "身份",
        "Symbol" : "符号",
        "Trademark" : "商标",
        "Cobrand" : "方式",
        "Registration Date" : "注册时间",
        "Registration Id" : "注册号",
        "Contact Way" : "联系方式",
        "Business Unit Id" : "企业编号",
        "Business Unit List" : "企业列表",
        "Business Type:" : "企业类型：",
        "Code" : "编码 ",
        "Business Type List" : "企业类型列表",
        "Add Business Type" : "新增企业类型",
        "Business Category:" : "企业类别：",
        "Business Category List" : "企业类别列表",
        "Add Business Category" : "新增企业类别",
        "Update Business Brand:" : "更新企业商品",
        "Business Brand List" : "企业商品列表",
        "Add Business Brand" : "添加企业商品",
        "Save" : "保存",
    	"Reset" : "清空",
        
        
        
        
        "Operation" : "操作",
        "Create By" : "创建用户",
        "Last Upate Time" : "最后更新时间",
        "Edit" : "编辑",
        "Delete" : "删除",
        "WIN_CONFIRM":"确认窗口",
        "WIN_BATCHCONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;批量删除当前选中票证, 确定继续吗?",
        "WIN_CONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;删除当前票证, 确定继续吗?",
        "Choose Unit" : "请选择要删除的企业！",
        "Selected Unit" : "所选企业",
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
        "User Feedback" : "用户反馈",
        "Number of User Complaint" : "用户抱怨数量",
    });
    
	$(document).ready(function() {
		lims.updateLocale();
	});
    
})(jQuery);