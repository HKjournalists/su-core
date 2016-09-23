(function($){
	var _ = $;
    var fsn = window.fsn = window.fsn || {};
        
    if(!fsn.zh){
        fsn.zh = {};
    }
    
    _.extend(true, fsn.zh, { 
    	"WIN_CONFIRM": "消息提示"
		    });
    
	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);