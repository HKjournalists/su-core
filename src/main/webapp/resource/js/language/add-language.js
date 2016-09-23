$(function() {
    var fsn = window.fsn = window.fsn || {};
	var currentLang;
	currentLang = navigator.language;
	if(!currentLang){
		currentLang = navigator.browserLanguage;
	}
	//if(!currentLang){
		currentLang = "zh-cn";
	//}
	
	if(currentLang.toLowerCase() == "zh-cn")
	{
		$.getScript(fsn.CONTEXT_PATH + "/resource/js/language/system_zh-cn.js", function(){
			$.getScript(fsn.CONTEXT_PATH + "/resource/js/system/management_system.js");
		});
	}else{
		$.getScript(fsn.CONTEXT_PATH + "/resource/js/system/management_system.js");
	}
	
	//load specific module language
	var language_modules = [
	                        "business"
	                        ];
	
	for (var i = 0; i < language_modules.length; i++) {
		$.getScript(fsn.CONTEXT_PATH + "/resource/js/language/" + language_modules[i] + "_" + currentLang + ".js");
	}
	
});