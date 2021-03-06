/**
 * @author oot
 */
(function($) {
	var fsn = window.fsn = window.fsn || {};

	if (!fsn.zh) {
		fsn.zh = {};
	}

	$.extend(true, fsn.zh, {
        //THML 页面的汉化
		"The label does not exist, please re-enter":"该标签不存在,请重新输入",
		"The current picture can not find the download path":"当前图片没有找到可以下载的路径",
		"Please select the drop-down box brand":"请选择下拉框内的品牌",
		"Please enter the nutritional condition report":"请输入营养报告条件",
		"Units can only be letters":"单位只能是字母",
		"NutritionLabel":"营养指数",
        "Modify the prompt message" : "修改提示消息",
        "Recalculated":"重新计算",
        "Expiration date" : "保质期",
        "Product name" : "产品名称 ",
        "Business Name":"生产企业",
        "Customer":"销往客户",
		"placeCustomer":"请选择销往客户",
        "Product otherName" : "别名 ",
        "Bar code" : "条形码",
        "Government standard" : "执行标准",
		"deleteProduct_Msg":"确定要删除该产品吗?",
        "Specification" : "规格",
        "Status" : "  状态",
        "Feature" : "特色",
        "Suit crowds" :"适宜人群",
        "Cancel" : "取消",
        "Confirm" : "确定",
        "Delete" : "删除",
        "Add" : "添加",
        "Update": "更新",
        "Save": "保存",
        "Authentication information" : "其他认证信息",
        "Product pictures" : "产品图片",
        "Nutrition report" : "营养报告",
        "Remark" : "备注",
        "Product description" : "产品描述",
        "Burdening" : "配料",
        "Classification of food" : "  食品分类",
        "Subordinate to the brand" : "所属品牌",
        "Binding qs" : "绑定qs号",
        "Modify the qs" : "修改qs号",
        "Preview" : "预览",
        "Current login subsidiary has binding qs product information" : "当前组织机构已绑定qs号的产品信息",
        "Company product information" : "公司产品信息",
        "(note: the file size is no more than 10 m! Can support the file format, PNG, BMP, jpeg)":"(注意:文件大小不能超过10M！  可支持文件格式：png .bmp .jpeg .jpg)",
        "(note: In order to ensure a smooth experience, it is recommended that each upload photos not more than 1 m! Can support the file format, PNG, BMP, jpeg)":"(注意:为保证更流畅的体验，建议每次上传照片不超过1M！  可支持文件格式：png .bmp .jpeg .jpg)", 
		"empty":"清空",
        //JS 汉化
       "Operation" : "操作",
       "Item" : "项目",
       "Value" : "值",
       "Unit" : "单位",
       "Condition" : "条件[ 如：每100(mL) ]",
       "Nrv" : "NRV(%)",
       "You don't have any note text input, cannot bind the qs number!" : "你没有输入任何备注文本，不能绑定qs号！",
        "Binding success" : "绑定成功！",
        "Modify the success" : "修改成功！",
        "Binding failure" : "绑定失败！",
        "Modified failure" : "修改失败！",
        "Product preview" : "产品预览",
        "Please enter the qs" : "请输入qs号",
        "Hint" : "提示",
        "Certification category" : "认证类别",
        "Related images" : "相关图片",
        "Is valid as of the time" : "有效期截至时间",
        "Delete success" : "解绑成功！",
        "Delete failure" : "解绑失败！",
        "Please fill out the certification deadline!":"请填写相应认证的截止日期！",
        "Wrong file type, the file will not be saved and re-upload pdf format Please delete!":"文件类型有误，该文件不会被保存，请删除后重新上传.pdf格式文件！",
        "The file you uploaded more than 10M, re-upload, delete pdf, png, bmp, jpeg file format!":"您上传的文件已经超过10M，请删除后重新上传 .png .bmp .jpeg .jpg格式的文件!",
        "Note: The file size can not exceed 10M! Only supports file formats: pdf":"注意:文件大小不能超过10M！  只支持文件格式：pdf",
        "Note: The file size can not exceed 10M! Supported file formats: pdf, png, bmp, jpeg":"注意:文件大小不能超过10M！  可支持的文件格式：pdf .png .bmp .jpeg .jpg！",
        "An exception occurred while uploading the file! Please try again later ...":"上传文件时出现异常！请稍候再试...",
        "saveResMsg" : "由于当前产品没有上传产品图片，会用一张临时图片代替。可以点击取消后，上传相关产品图片；也可以点击确定，先保存产品信息，之后再编辑添加。",
        "Save":"保存",
        "Update":"更新",
        "OK":"确定",
        "Cancel":"取消",
        "upload_Product":"上传产品图片",
        "upload_cert":"上传认证图片",
        "Delete":"删除",
        "Operation":"操作",
        "View Product":"产品基本信息",
        "Product barcode only by numbers!":"产品条形码只能由数字组成!",
        "Enter only integer or fractional":"只能输入整数或小数",
        
        /*二维码产品界面汉化*/
        "Inner Code":"内部码",
        "Product Area":"所属区域",
        "Product Category":"食品类别",
        "Self Support":"自营",
        "Address":"地址",
        "Detail":"详细",
        "Label":"标签",
	});

	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
})(jQuery);