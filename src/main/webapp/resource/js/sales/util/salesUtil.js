(function($) {
	var salesUtil = window.salesUtil = window.salesUtil || {};
	
	/**
	 * 验证电话号码格式,返回 true 或 false, true 验证通过
	 * @author tangxin 2015-04-27
	 */
	salesUtil.validatePhone = function (phone) {
		if(phone == null || phone == ""){
			return false;
		}
		var rel = /^(^(\d{3,4}-)?\d{7,8})$|(^(0|86|17951)?(13[0-9]|15[0123456789]|18[0123456789]|14[57])[0-9]{8}$)/;
		return phone.match(rel);
	};
	
	/**
	 * 在当前页面1.5秒后跳转到指定的页面
	 * @param href 指定页面的相对路径url
	 */
	salesUtil.setWindowLocationHref = function(href) {
		setTimeout("window.location.href = '"+href+"'", 1500);
	};
	
})(jQuery);