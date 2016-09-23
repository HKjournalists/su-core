var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
function timer() {  
	var ts = fsn.endTime - (new Date()) + 1000*60*120;//计算剩余的毫秒数
    var mm = parseInt(ts / 1000 / 60 % 60, 10);//计算剩余的分钟数  
    var ss = parseInt(ts / 1000 % 60, 10);//计算剩余的秒数  
    mm = checkTime(mm);
    ss = checkTime(ss);
	if(mm == "15" && ss == "05"){
		$("#diaoxian_remind_01").data("kendoWindow").open().center();
	} else if(mm == "05" && ss == "05"){
    	$("#diaoxian_remind_01").data("kendoWindow").open().center();
    }else if(mm == "00" && ss == "35"){
    	$("#diaoxian_remind_02").data("kendoWindow").open().center();
    }else if(mm == "00" && ss == "05"){
    	fsn.save();
    	fsn.logoutUrl = fsn.getLogoutURL();
    	$("#diaoxian_logout").data("kendoWindow").open().center();
    	return;
    }
    //document.getElementById("timer").innerHTML = mm + "分" + ss + "秒";
    setTimeout("timer()",1000);
}  
function checkTime(i){
    if (i < 10) {    
        i = "0" + i;    
    }    
    return i;    
}
function logout(){
	$.ajax({
		url:portal.HTTP_PREFIX + "/logout",
		type:"GET",
		dataType:"json",
		async:false,
		success:function(e){
		}
	});
}

/**
 * 掉线后跳转登录界面功能
 * @author ZhangHui 2015/4/16
 */
fsn.logoutRefresh = function() {
    window.location.pathname = fsn.logoutUrl;
};