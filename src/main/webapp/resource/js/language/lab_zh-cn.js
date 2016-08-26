(function($){
	var _ = $;
    var fsn = window.fsn = window.fsn || {};
        
    if(!fsn.zh){
        fsn.zh = {};
    }
    
    _.extend(true, fsn.zh, { 
    	    	"Id" : "编号",
		    	"Name" : "名称",
		    	"DisplayName":"显示名称",
		    	"Address" : "地址",
		    	"Test Lab:" : "实验室",
		    	"Save" : "保存",
		    	"Reset" : "清空",
		    	"Operation" : "操作",
		    	"Edit" : "编辑",
		        "Delete" : "删除",
		        "Test Lab List" : "实验室列表",
		        "Add Test Lab" : "新增实验室",
		        "Superior" : "上级",
		        "updated sucessfully！":"数据更新成功！",
		        "created sucessfully！":"数据保存成功！",
		        "Edit sucessfully！":"数据编辑成功！",
		        
		        
		        
		        
		    	
		    	
		    });
    
	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);