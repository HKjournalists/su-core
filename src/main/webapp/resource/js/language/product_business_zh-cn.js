(function($){
	var lims = window.lims = window.lims || {};

	if (!lims.zh) {
		lims.zh = {};
	}
    
    $.extend(true,lims.zh,{
    	"Id" : "序号",
    	"Product List" : "产品列表",
    	 "Name" : "名称",
    	 "Owned Business Brand" : "所属品牌",
    	"Update Product:" : "更新产品：",
    	"License Id" : "许可证编号",
    	"License Start" : "发证日期",
    	"License End" : "有效期至",
    	"Produce Status" : "生产状态",
    	"Product Name":"产品名称",
    	"Barcode" : "条形码",
    	"Format" : "规格",
    	"Status" : "状态",
    	"Standard" : "国家标准",
    	"Expiratio":"保质期",
    	"Status":"状态",
    	"Characteristic":"特色",
    	"Appropriate Crowd" : "适宜人群",
    	"Brand" : "所属品牌",
    	"Category":"产品类别",
    	"Ingredient" : "配料",
    	"Description":"产品描述",
    	"Note" : "备注",
    	"Production Date" : "生产日期",
    	"Expiration Date" : "有效期限",
    	"Product" : "产品",
    	"Original" : "原型",
    	
    	"Product Image" : "产品图片",
    	
    	"ProductInfo Id" : "序号",
    	"ProductInfo Cert Type" : "信息类型",
    	"ProductInfo Unit" : "发证机构",
    	"ProductInfo No" : "证书编号",
    	"ProductInfo End" : "有效期至",
    	
    	"Operation" : "操作",
        "View":"预览",
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