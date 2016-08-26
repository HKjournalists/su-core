(function($){
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}
    
    $.extend(true,fsn.zh,{
    	"Id" : "序号",
    	"Operation" : "操作",
    	"Name" : "姓名",
    	"Position" : "岗位",
    	"Address" : "住址",
    	"IdentificationNo" : "身份证号",
    	"Health Certificate No." : "健康证编号",
    	"Qualification Certificate No." : "从业资格证编号",
    	"Honor Certificate No." : "荣誉证书编号",
    	"upload_Certification" : "上传个人证照",
    	"upload_Health" : "上传健康证",
    	"upload_Qualification" : "上传从业资格证",
    	"upload_Honor" : "上传荣誉证书",
    	"deleteMember_Msg" : "确认删除该人员信息",
    	"Tel" : "联系电话",
    	"E-mail" : "邮箱",
    	"Personal Profile" : "个人简介",
        "Edit" : "编辑",
        "Save" : "保存",
        "OK" : "确认",
        "Cancel" : "取消",
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