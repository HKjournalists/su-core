(function($){
	var _ = $;
    var fsn = window.fsn = window.fsn || {};
        
    if(!fsn.zh){
        fsn.zh = {};
    }
    
    _.extend(true,fsn.zh,{ 
    	
    	    	"Id" : "编号",
		    	"Product Instance Id" : "产品实例编号",
		    	"Comment" : "评定结果",
		    	"Remark" : "备注",
		    	"Pass" : "是否合格",
		    	"Test Date" : "实验日期",
		    	"productCategory" : "食品分类",
		    	
		    	"Subsidiary List" : "旗下企业列表",
		    	"Add Test Result" : "添加实验结果",
		    	"Product" : "样品名称",
		    	"Batch Serial No." : "批序列号",
		    	 "Operation" : "操作",
		    	"View Detail" : "详情",
		    	"Test Result:" : "测试结果",
		    	"Save" : "保存",
		    	"Reset" : "清空",
		    	
		    	"Notification" : "公告",
		    	"From FDA" : "来自管理局的公告",
		    	"From Test Lab" : "来自实验室的公告",
		    	
		    	"Name" : "检测项目",
		    	"Standard" : "检测标准",
		    	"TechIndicator" : "技术指标",
		    	"Result" : "检测结果",
		    	"Unit" : "单位",
		    	"Assessment" : "单项评定",
		    	
		    	"Barcode" : "条形码",
		    	"BatchSerialNo" : "批次号",
		    	"Serial" : "序列号",
		    	"KeyTester" : "主检人",
		    	"AuditBy" : "审核人",
		    	"PlanNo" : "FDA测试计划编号",
		    	"BrandName" : "生产企业",
		    	"Testee" : "受检人",
		    	"ApproveBy":"批准人",
		    	
		    	"receiveDate":"接收时间",
		    	"publishDate":"发布时间",
		    	"riskIndexState":"风险指数计算状态",
		    	"organizationName":"组织机构名称",
		    	"pubUserName":"发布者",
		    	
		    	"SamplingLocation" : "抽样地点",
		    	"SamplingDate" : "抽样时间",
		    	"SamplingUnit" : "抽样单位",
		    	"TestType" : "检测类型",
		    	"Test Result Manage":"测试结果管理",
		    	"Publish":"发布",
		    	"Backout":"撤销",
		    	"Return":"退回", 
		    	"Report_View":"查看报告",
		    	"Producer_View":"查看生产企业",
		    	"BatchSerialNo":"批次/日期",
		    	"TestType":"检测类型",
		    	"ReportNO":"报告编号",
		    	
		    	"WIN_CONFIRM_MSG":"<span class='k-icon k-warning'></span> &nbsp;执行当前的操作, 确定继续吗?",
		    	"CONFIRM_MSG":"确认删除吗？？",
		    	"confirm_yes_btn":"确认",
		    	"confirm_no_btn":"取消",
		    	"CONFIRM_COMMON_WIN":"确认",
		    	"WIN_CONFIRM":"确认",
		    	"sample publish sucessfully！":"样品发布成功！",
		    	"sample backout sucessfully！":"样品撤销成功！",
		    	"removed Successfully！":"删除成功!",
		    	"removed not Successfully！":"删除不成功！",
		    	"sample calculate sucessfully！":"重新计算成功！",
		    	    	
		    	"update qs":"修改qs号",
		    	"delete qs":"解绑",
		    	"Are you sure delete qs?":"确认解除该产品的qs绑定吗？",
		    	
		    	"Producer Enterprise Information:":"生产企业信息：",
				"enterpriseName":"生产企业名称",
				"Organization Info":"组织机构信息",
				"organization code":"组织机构代码",
				"License Info":"营业执照信息",
				"License No":"营业执照号",
				"Production license":"生产许可证",
				"Production license No":"生产许可证编号",
				"View picture":"查看图片",
				"Operation":"操作",
				"Current Status":"当前状态",
				"Is Pass":"是否通过",
				"Reject Reason":"退回原因",
				"QS NO":"QS证号",
				"The failure reason in return, returned to the manufacturer information background error":"退回失败，原因在退回生产企业信息时后台出错",
		    });
    
	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);