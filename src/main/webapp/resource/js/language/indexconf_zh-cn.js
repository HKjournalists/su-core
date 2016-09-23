(function($){
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}
    
    $.extend(true,fsn.zh,{
    	"Business Unit" : "企业",
        "Management Fields (Roles - Sales Manager): " : "管理字段（角色 - 业务经理）：",
        "Add Business Unit" : "新增企业",
        "Number" : "序号",
        "Id" : "序号",
        "Name" : "资源名称",
        "Address" : "地址",
        "Address2" : "地址2",
        "Logo" : "标志",
        "Website" : "网址",
        "Type" : "类型",
        "Category" : "类别",
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
    	"Reset" : "重置",
    	"Commit" : "提交",
        
        
        
        "Operation" : "操作",
        "Check" : "勾选",
        "Create By" : "创建用户",
        "Last Upate Time" : "最后更新时间",
        "Edit" : "编辑",
        "Delete" : "删除",
        "WIN_CONFIRM":"确认窗口",
        "WIN_BATCHCONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;批量删除当前选中企业, 确定继续吗?",
        "WIN_CONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;删除当前企业, 确定继续吗?",
        "Choose Unit" : "请选择要删除的企业！",
        "Selected Unit" : "所选企业",
        "Delete Success":"删除成功!",
        "Delete Failed":"删除失败!",
        "Currently field " : "当前字段 ",
        " related by another business record, so " : " 已经与其他业务数据关联，所以",
        "cannot be remove" : "不允许删除 ",
        "Column Limit" : "栏目最多激活5个",
        "Column Equal" : "显示的栏目不能重复",
        "Color Not Selected" : "请选择一种主题色！",
        "Name Connot Null" : "名称不能为空",
        "Name Could Be Used" : "名称可以使用",
        "Name Is Exist" : "您输入的名称已存在",
        "ColName Connot Null" : "展示名称不能为空",
        "ColName Too Long" : "展示名称不能大于8个字节",
        "ColName Could Be Used" : "展示名称可以使用",
        "ColPic Connot Null" : "栏目图片不能为空",
        "Prod Not Exist" : "没有对应产品",
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
        "color-one" : "1",
        "color-two" : "2",
        "color-three" : "3",
        "color-four" : "4",
        "color-five" : "5",
        "color-six" : "6",
        "color-seven" : "7"
    }); 
    
	$(document).ready(function() {
		fsn.updateLocale();
	});
    
})(jQuery);