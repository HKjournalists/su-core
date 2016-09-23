$(function(){
    
	Date.prototype.format = function(format) {  
		/*   * eg:format="YYYY-MM-dd hh:mm:ss";   */  
		var o = {   
				"M+" : this.getMonth() + 1, //month   
				"d+" : this.getDate(), //day   
				"h+" : this.getHours(), //hour   
				"m+" : this.getMinutes(), //minute   
				"s+" : this.getSeconds(), //second   
				"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter   
				"S" : this.getMilliseconds()  //millisecond  
		}   
				if (/(Y+)/.test(format)) {   
					format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4     
							- RegExp.$1.length));  
				}   
				for (var k in o) {   
					if (new RegExp("(" + k + ")").test(format)) {    
						format = format.replace(RegExp.$1, RegExp.$1.length == 1      
								? o[k]      
								: ("00" + o[k]).substr(("" + o[k]).length));   
					}  
				}  
				return format; 
			}
           
});

/**
 * 给用户输入的qs号每4位自动补一个空格
 * @author HuangYong 
 */
qsNoFormat_validTab = function(obj,value) {
    value = value.replace(/\s*/g, "");
    var result = [];
    for(var i = 0; i < value.length; i++)
    {
        if (i % 4 == 0 && i != 0)
        {
            result.push(" " + value.charAt(i));
        }
        else
        {
            result.push(value.charAt(i));
        }
    }
    obj.value = result.join("");
     
};

/**
 * 验证日期必填及格式
 * @author ZhangHui 2015/4/9
 */
validateDateFormat = function(div_id, error_msg, isRequired){
 	/* isRequired:true 代表必填项校验； isRequired:false 代表不必做必填项校验 */
 	$("#" + div_id + "_format").data("kendoDatePicker").value($("#" + div_id).val());
 	if(isRequired){
 		if ($("#" + div_id + "_format").val().trim() == "" && $("#" + div_id).val().trim() != "0000-00-00") {
             lims.initNotificationMes(error_msg, false);
             return false;
         }
 	}else{
 		if ($("#" + div_id + "_format").val().trim() == "") {
             lims.initNotificationMes(error_msg, false);
             return false;
         }
 	}
 	return true;
};

var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀

/**
 * 获取当前登录用户所属企业类型
 * @author Zhanghui 2015/5/19
 */
getCurrentBusiness = function(){
	var currentBusiness = null;
	$.ajax({
		url:portal.HTTP_PREFIX+"/business/getCurrentBusiness",
		type:"GET",
        async:false,
		success:function(returnValue){
			if(returnValue.result.status == "true"){
				currentBusiness = returnValue.data;
			}
		}
	});
	return currentBusiness;
};

/**
 * 地址和街道地址赋值
 * @author ZhangHui 2015-6-2
 */
setAddressValue = function(address, otherAddress, mainAddrId, streetAddrId){
	setAddressValue(address, otherAddress, mainAddrId, streetAddrId, "input");
};

/**
 * 地址和街道地址赋值
 * @param htmlType 
 * 			input
 * 			text
 * @author ZhangHui 2015-6-2
 */
setAddressValue = function(address, otherAddress, mainAddrId, streetAddrId, htmlType){
	$("#" + mainAddrId).val("");
	$("#" + streetAddrId).val("");
	
	if(!otherAddress){
		$("#" + streetAddrId).val(address);
		return;
	}
	
	var tmpAryVal = otherAddress.split("--");
	if(tmpAryVal.length > 1){
		if(htmlType == "input"){
			$("#" + mainAddrId).val(tmpAryVal[0]);
			$("#" + streetAddrId).val(tmpAryVal[1]);
		}else if(htmlType == "text"){
			$("#" + mainAddrId).text(tmpAryVal[0]);
			$("#" + streetAddrId).text(tmpAryVal[1]);
		}
		return;
	}
	
	if(htmlType == "input"){
		$("#" + streetAddrId).val(address);
	}else if(htmlType == "text"){
		$("#" + streetAddrId).text(address);
	}
};