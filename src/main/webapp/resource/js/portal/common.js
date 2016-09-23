/**
 * 所有页面必须调用，放在jquery之后
 * 提供fsn.portal命名空间
 */
(function($){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	
	fsn.hostname = window.location.hostname;
	
	fsn.CONTEXT_PATH = (function(){
		var _context_path = "";
		switch (fsn.hostname) {
		case "qaportal.fsnip.com":
			_context_path = "http://qaenterprise.fsnip.com/fsn-core";
			break;
		case "stgportal.fsnip.com":
			_context_path = "http://stgenterprise.fsnip.com/fsn-core";
			break;
		case "www.fsnip.com":
			_context_path = "http://enterprise.fsnip.com/fsn-core";
			break;
		case "fsnip.com":
			_context_path = "http://enterprise.fsnip.com/fsn-core";
			break;	
		default: 
			// _context_path = "http://"+fsn.hostname + "/fsn-portal";
			_context_path = "http://deventerprise.fsnip.com:8080/fsn-core";
			break;
		}
		return _context_path;
	})();
	fsn.HTTP_PREFIX = fsn.CONTEXT_PATH + "/service";
	
	portal.CONTEXT_PATH = "/fsn-portal"; // 项目名称
	
	portal.HTTP_PREFIX = portal.CONTEXT_PATH + "/service"; // 业务请求前缀

	portal.PORTAL_COOKIE_NAME = "fsn_portal_cookie";
	portal.USER_TYPE_DEFAULT = "default";
	
	portal.FILE_TYPE = [".jpg",".png",".gif"]; // 图片格式
	
	portal.session = function(name, val){
		if(arguments.length > 1){
			$.removeCookie(name, {path : '/'});
			if(val){
				$.cookie(name, val, {path : '/'});
			}
		}else{
			return $.cookie(name);
		}
	};
	
	/**
	 * 动态引入页面文件
	 * @param opt {json object} 
	 * {fileName:文件路径, 
	 *  fileType:文件类型,
	 * 	fileId: 要生成的script标签id，如果没有默认load-file-id + Date.getTime(), 
	 * 	asyn:是否采用异步加载的方式，默认为同步加载
	 * }
	 * return DOM
	 */ 
	portal.loadFile = function (opt) {
		// 配置参数
		opt = $.extend({
			fileName:null,
			fileType:null,
			fileId:"load-file-id-" + (new Date()).valueOf(),
			asyn:false
		},opt);
		
		var oldDom = null, 
			newDom = null, 
			fileType = opt.fileType.toUpperCase(), 
			createDom = {
				"CSS": function (id, href) {
					var _cssDom_ = document.createElement("link");
					_cssDom_.setAttribute("type", "text/css");
					_cssDom_.setAttribute("rel", "stylesheet");
					_cssDom_.setAttribute("href", href);
					_cssDom_.setAttribute("id", id);
					return _cssDom_;
				},
				"SCRIPT": function (id, src) {
					var _scriptDom_ = document.createElement("script");
					_scriptDom_.setAttribute("type", "text/javascript");
					_scriptDom_.setAttribute('src', src);
					_scriptDom_.setAttribute('id', id);
					return _scriptDom_;
				}
			};
		
		if (opt.asyn) { // 异步加载
			var headDom = document.getElementsByTagName('head')[0];
			newDom = createDom[fileType](opt.fileId, opt.fileName);
			oldDom = document.getElementById(opt.fileId);
			if (oldDom) {
				headDom.removeChild(oldDom);
			}
			headDom.appendChild(newDom);
		} else { // 同步加载
			// $('head').append(['<script',' type="text/javascript"',' src="',opt.fileName,'" id="',opt.scriptId,'">','<','/script','>'].join(""));
			oldDom = $("#"+opt.fileId);
			if (oldDom[0]) {
				oldDom.remove();
			}
			if (fileType == "CSS") {
				newDom = $(['<link',' type="text/css"',' href="',opt.fileName,'" id="',opt.fileId,'" rel="stylesheet">','<','/link','>'].join("")).appendTo($('head'));
			}else if (fileType == "SCRIPT") {
				newDom = $(['<script',' type="text/javascript"',' src="',opt.fileName,'" id="',opt.fileId,'">','<','/script','>'].join("")).appendTo($('head'));
			}
			
		}
		
		return $(newDom); // 返回新创建的dom，jquery对象
	};
	
	// 得到浏览器的信息
	portal.Sys = (function(ua) {
	    var s = {};
	    s.IE = ua.match(/(msie\s|trident.*rv:)([\w.]+)/) ? true: false;
	    s.Firefox = ua.match(/firefox\/([\d.]+)/) ? true: false;
	    s.Chrome = ua.match(/chrome\/([\d.]+)/) ? true: false;
	    s.IE6 = (s.IE && ([/MSIE (\d)\.0/g.test(navigator.userAgent)][0][1] == 6)) ? true: false;
	    s.IE7 = (s.IE && ([/MSIE (\d)\.0/g.test(navigator.userAgent)][0][1] == 7)) ? true: false;
	    s.IE8 = (s.IE && ([/MSIE (\d)\.0/g.test(navigator.userAgent)][0][1] == 8)) ? true: false;
	    return s;
	})(navigator.userAgent.toLowerCase());
	
	portal.isBlank = function (s) {
		return (!s || $.trim(s) === "");
	};
	
	// 判断是否是图片
	portal.isImg = function (filename) {
		var _filename_suffix = filename.substring(filename.lastIndexOf(".")),
			_bIsImg = false, 
			len, i;
		for (i=0,len=portal.FILE_TYPE.length;i<len; i++) {
			if(_filename_suffix === portal.FILE_TYPE[i]) {
				_bIsImg = true;
				break;
			}
		}
		return _bIsImg;
	};
	
	/**
	 * 查看元素有没有绑定事件
	 * @param dom {DOM} JS元素对象
	 * @param se {String} 事件名称
	 * @param jq 工具可不传入
	 */
	portal.hasEvent = function (dom, se, jq) {
		var _$ = jq||jQuery||$, br = false;
		if (_$) {
			var o = _$._data(dom, "events");
			if (!portal.isBlank(o)) {
				_$.each(o, function(k,v){
					if (k == se) {
						br = true;
					}
				});
			}
		}
		return br;
	};
	
	/**
	 * 数据输入检测，判断是否是数字
	 * @param data {jquery data} 输入的数据
	 */ 
	portal.checkIsNum = function(data){
		var code = parseInt(data);
		if (code >= 96 && code <= 105 || code >= 48 && code <= 57 || code == 8 || code==110 || code==190) {    
            return true;	
		}else{
			return false;
		}
	};
	
	/**
	 * 验证是否是手机号码
	 * @param $Dom {jquery dom} 手机验证的jquery对象
	 */ 
	portal.checkIsPhone = function($Dom){
		var data =$Dom.val(),mobile = /^(0|86|17951)?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/,tel=/^((0?1[358]\d{9})[,，]?|((0(10|2[0-9]{1}|[3-9]{1}\d{2})[-_－—]?)?\d{7,8})[,，]?)*$/;
		if(data.match(mobile)||data.match(tel)){
			return true;
		}else{
			return false;
		}
	};
	
	/**
	 * 文件检测，包括类型和大小
	 * @param $fileDom {jquery dom} file空间jquery对象
	 * @param size {number} 文件的大小，如果有值则使用传入的size作为未见的maxsize
	 */ 
	portal.checkFileSize = function ($fileDom, size) {
		var file_maxsize = size || 2*1024*1024/* 2M */,
			errMsg = "上传的附件文件不能超过2M！！！",
			tipMsg = "您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过2M，建议使用IE、FireFox、Chrome浏览器。",
			filesize = 0/* 存放file的size */;
		try {
			if(this.isBlank($fileDom.val())){
				return false;
			}
			
			/** 
			 * 不同浏览器表现不一
			 * 在FireFox、Chrome浏览器中可以根据
			 * document.getElementById(“fileid”).files[0].size 
			 * 获取上传文件的大小（字节数）;
			 * 而IE浏览器中不支持该属性，只能借助<img>标签的dynsrc属性，来
			 * 间接实现获取文件的大小（但需要同意ActiveX控件的运行）。
			 */
			if (portal.Sys.Firefox || portal.Sys.Chrome) {
				filesize = $fileDom[0].files[0].size;
			} else if (portal.Sys.IE) {
				// 如果是IE，则在body中添加一个临时IMG元素，display=none；
				var $tempImg = $('<img src="'+$fileDom.val()+'" id="temp-img" data-id="'+$fileDom[0].id+'" />').appendTo('body');
				$tempImg[0].dynsrc=$fileDom.val();
				filesize = $tempImg[0].fileSize;
			} else {
				alert(tipMsg);
				return false;
			}
			
			// 判断filesize大小
			if (filesize == -1) {
				alert(tipMsg);
				return false;
			} else if(filesize > file_maxsize) {
				alert(errMsg);
				return false;
			} else {
				return true;
			}
		} catch (e) {
			alert(e);
			return false;
		}
	};
	
	// 解析url请求参数
	portal.getSearchParam = function(url, k){
		var _portal = this, v = "", arrp, key, arrp_len, arrp_i, value, i;
		
		if (_portal.isBlank(url)) {
			return v;
		}
		arrp = url.substring(1).split("&");
		
		for(i=0, arrp_len = arrp.length; i<arrp_len; i++){
			
			arrp_i = arrp[i];
			
			if (_portal.isBlank(arrp_i)) {
				break;
			}
			
			key = arrp[i].split("=")[0];
			value = arrp[i].split("=")[1];
			
			if(key == k){
				v = value;
				break;
			}
		}
		return this.Sys.IE ? v : decodeURIComponent(v);
	};
	
	/**
	 * 键盘事件
	 * @param ev event对象
	 * @param dom 要操作的对象
	 */
	portal.keyDown = function (ev, dom) { 
		var currKey=0,e=ev||event; 
		currKey=e.keyCode||e.which||e.charCode; 
		
		switch(currKey){
			// 回车键：默认在查找的时候click
			case 13:
				if(arguments.length>1){
					$(dom)[0].click();
				}else{
					document.getElementById("sbutton").click();
				}
				break;
				
			default: break;
		}
		
	};
	
	/** 
	 * 使用自定义滚动条，依赖插件jquery.mCustomScrollbar.concat.min.js
	 * @param {String} id
	 * @param {String} h 滚动条高度
	 * @param {Integer} type 1-纵向， 2-横向
	 */
	portal.createCustScroll = function(id, h, type){
		var $dom = $("#"+id);
		switch (type) {
		
			case 1:
				$dom.mCustomScrollbar({
					scrollInertia:h  //设置滚动条高度
				});
				break;
				
			case 2:
				$dom.mCustomScrollbar({
					horizontalScroll:true,
					advanced:{
						autoExpandHorizontalScroll:true
					}
				});
				break;
	
			default:
				break;
			
		}
	};
})(jQuery);