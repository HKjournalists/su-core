(function($){
	var _ = $;
    var fsn = window.fsn = window.fsn || {};
        
    if(!fsn.zh){
        fsn.zh = {};
    }
    
    _.extend(true, fsn.zh, { 
    	    	"Id" : "编号",
    	    	
    	    	"PlanNO" : "计划号",
		    	"Quarter" : "季度",
		    	"Testee" : "被检人",
		    	"Sample" : "检测产品",
		    	"Producer" : "生产企业",
		    	"SamplingQuantity" : "抽样数量",
		    	"SamplingDistrict" : "抽样地区",
		    	
		    	"ParentNode" : "上级",
		    	
		    	"Comment" : "备注",
		    	"Start Date" : "开始时间",
		    	"End Date" : "结束时间",
		    	"Product Group" : "产品组",
		    	"Fda Id" : "管理局编号",
		    	"Fda:" : "食品与药物管理局：",
		    	"Name" : "名称",
		        "Address" : "地址",
		        "Supervisor" : "管理人",
		        "Save" : "保存",
		    	"Reset" : "清空",
		    	"Operation" : "操作",
		    	"Edit" : "编辑",
		        "Delete" : "删除",
		        "Fda List" : "管理局列表",
		        "Add Fda" : "新增管理局",
		    	"Fda Test Plan:" : "管理局测试计划：",
		    	"Fda Test Plan List" : "管理局测试计划列表",
		    	"Add Fda Test Plan" : "新增管理局测试计划",
		    	"Fda Statement:" : "管理局公告：",
		    	"Publish Date" : "发布日期",
		    	"Content" : "内容",
		    	"Product Instance Id" : "产品实例编号",
		    	"Fda Id" : "管理局编号",
		    	"Fda Statement List" : "管理局公告列表",
		    	"Add Fda Statement" : "新增管理局公告",
		    	"Fda Product Group:" : "管理局产品组：",
		    	"Code" : "编码 ",
		    	"Fda Product Group List" : "管理局产品组列表",
		    	"Add Fda Product Group" : "新增管理局产品组",
		    	"Current test cycle progress" : "当前测试周期状态",
		    	"From Business Unit" : "来自企业",
		    	"From Test Lab" : "来自测试中心",
		    	"Sender" : "发送者",
		    	"Sender Type" : "发送者类型",
		    	"Content" : "消息内容",
		    	"Create Date" : "发送时间",
		    	"Plan" : "计划",
		    	"View" : "查看",
		    	"Message List" : "消息列表",
		    	"Add Message" : "发送消息",
		    	"Receiver" : "接收者",
		    	"Sender Type" : "接收者类型",
		    	"displayName":"显示名称",
		    	
		    	"updated sucessfully！":"更新数据成功！",
		    	"created sucessfully！":"增加数据成功！",
		    	
		    	
		    	
		    });
    
	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);