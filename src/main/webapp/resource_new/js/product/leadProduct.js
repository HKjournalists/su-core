var fsn = window.fsn = window.fsn || {};
var portal = fsn.portal = fsn.portal || {};
portal.HTTP_PREFIX = fsn.getHttpPrefix();
	

/**
 * 引进产品
 * @author HuangYog 2015/4/14
 */
portal.leadProduct = function(barcode){
	var isSuccess = false;
	$.ajax({
		url : portal.HTTP_PREFIX + "/erp/initializeProduct/leadProduct/" + barcode,
		type : "GET",
		datatype : "json",
		async:false,
		contentType: "application/json; charset=utf-8",
		success : function(returnValue) {
			if(returnValue.result.status == "true"){
				isSuccess = true;
			}
		}
	});
	return isSuccess;
};