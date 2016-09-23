(function($) {

    String.prototype.format = String.prototype.f = function(arg) {
        var s = this,
            i = arg.length;

        while (i--) {
            s = s.replace(new RegExp('\\{' + i + '\\}', 'gm'), arg[i]);
        }
        return s;
    };

    var fsn = window.fsn = window.fsn || {};
    fsn.hostname = window.location.hostname;
    //session
    $.cookie.json = true;
    fsn.session = function(name, val){
    	if(arguments.length > 1){
    		$.removeCookie(name, {path : '/'});
    		if(val){
    			$.cookie(name, val, {path : '/'});
    		}
    	}else{
    		return $.cookie(name);
    	}
		
    };
    fsn.getPictureFtpPath=function(){
    	var pictureFtpPath = "";
    	switch(this.hostname){
    		case "enterprise.fsnip.com":pictureFtpPath="http://fsnrec.com:8080";break;
    		case "stgenterprise.fsnip.com":pictureFtpPath="http://stg.fsnrec.com";break;
    		case "qaenterprise.fsnip.com":pictureFtpPath="http://qa.fsnrec.com";break;
    		default:pictureFtpPath="http://qa.fsnrec.com";break;
    	}
    	return pictureFtpPath;
    };
    fsn.getLogoutURL = function(){
    	var httpPrefix = "";
    	switch(this.hostname){
    	case "enterprise.fsnip.com"://PROD
    	case "stgenterprise.fsnip.com"://STG
    	case "qaenterprise.fsnip.com"://QA
    		httpPrefix = "/fsn-core/logout";
    		break;
    	default://DEV
    		httpPrefix = "/fsn-core/logout";
    		break;
    	}
    	console.log("LIMS Logout Prefix:" + httpPrefix);
    	return httpPrefix;
    };
    fsn.getReturnReportURL=function(from,organization){
    	var httpPrefix = "";
    	switch(this.hostname){
    	case "enterprise.fsnip.com": 	
    		switch(from){
    		case "professional":httpPrefix = "http://lims.fsnip.com/lims-core/service/pipeline/fsngoback";break; 
    		case "guangzhou":httpPrefix = "http://zgc.fsnip.com/lims-core/service/pipeline/fsngoback";break; 
    		case "easy":httpPrefix = "http://easy.fsnip.com/lims-core/service/testReport/goBack";break;
    		case "customization": 
    			switch(organization){
    			case 30:httpPrefix = "http://mn.fsnip.com/lims-core/service/epreport/republish";break;
    			case 31:httpPrefix = "http://wcy.fsnip.com/lims-core/service/epreport/republish";break;
    			default :httpPrefix = "http://gzata.fsnip.com/lims-core/service/pipeline/fsngoback";
    			}
    		}
    		break;
    	case "stgenterprise.fsnip.com":
    		switch(from){
    		case "professional":httpPrefix = "http://stglims.fsnip.com/lims-core/service/pipeline/fsngoback";break;
    		case "guangzhou":httpPrefix = "http://stggz.fsnip.com/lims-core/service/pipeline/fsngoback";break; 
    		case "easy":httpPrefix = "http://stgeasy.fsnip.com/lims-core/service/testReport/goBack";break;
    		case "customization": 
    			switch(organization){
    			case 30:httpPrefix = "http://stgmn.fsnip.com/lims-core/service/epreport/republish";break;
    			case 31:httpPrefix = "http://stgwcy.fsnip.com/lims-core/service/epreport/republish";break;
    			default :httpPrefix = "http://ck.fsnip.com/lims-core/service/pipeline/fsngoback";
    			}
    		}
    		break;
    	case "qaenterprise.fsnip.com":
    		switch(from){
    		case "professional":httpPrefix = "http://qalims.fsnip.com/lims-core/service/pipeline/fsngoback";break;
    		case "guangzhou":httpPrefix = "http://qagz.fsnip.com/lims-core/service/pipeline/fsngoback";break; 
    		case "easy":httpPrefix = "http://qaeasy.fsnip.com/lims-core/service/testReport/goBack";break;
    		case "customization": 
    			switch(organization){
    			case 30:httpPrefix = "http://qamn.fsnip.com/lims-core/service/epreport/republish";break;
    			case 31:httpPrefix = "http://qawcy.fsnip.com/lims-core/service/epreport/republish";break;
    			default:httpPrefix = "http://gettec.fsnip.com/lims-core/service/pipeline/fsngoback";
    			}
    		}
    		break;	
    	default:
    		switch(from){
    		case "professional":httpPrefix = "http://qalims.fsnip.com/lims-core/service/pipeline/fsngoback";break;
    		case "guangzhou":httpPrefix = "http://qagz.fsnip.com/lims-core/service/pipeline/fsngoback";break;
    		case "easy":httpPrefix = "http://qaeasy.fsnip.com/lims-core/service/testReport/goBack";break;
    		case "customization": 
    			switch(organization){
    			case 30:httpPrefix = "http://qamn.fsnip.com/lims-core/service/epreport/republish";break;
    			case 31:httpPrefix = "http://qawcy.fsnip.com/lims-core/service/epreport/republish";break;
    			default:httpPrefix = "http://gettec.fsnip.com/lims-core/service/pipeline/fsngoback";
    			}
    		}
			break;
    	}
    	return httpPrefix;
    };
    
    fsn.clrmsg=function(id,mes){
    	$("#"+id).css({ 
			width:'',
			background: '', 
			margin: '', 
			padding: '', 
			border: '',
    	});
		$("#"+id).html(mes);
    };
    
    fsn.error = function(d) {
        console.error(d);
    };
    fsn.log = function(d) {
        console.log(d);
    };
    fsn.debug = function(d) {
        console.debug(d);
    };

    fsn.getContextPath=function(){
        var pathName = document.location.pathname;
        var index = pathName.substr(1).indexOf("/");
        var result = pathName.substr(0,index+1);
        return result;
    };
    fsn.getHttpPrefix = function(){
    	var httpPrefix = "";
    	switch(this.hostname){
    	case "qaenterprise.fsnip.com":
    	case "stgenterprise.fsnip.com":
    	case "enterprise.fsnip.com":
    		httpPrefix =fsn.getContextPath()+ "/service";
    		break;
    	default://DEV
    		httpPrefix = fsn.getContextPath()+"/service";
    		break;
    	}
    	console.log("FSN Http Prefix:" + httpPrefix);
    	return httpPrefix;
    };
    fsn.HTTP_PREFIX = fsn.getHttpPrefix();
    fsn.TEMPLATE_MIN_SIZE = 600;
    fsn.defaultLocale = "zh";
    fsn.DATE_FORMAT = 'YYYY-MM-dd hh:mm:ss';
    fsn.ENABLE_GROUP_FIELD = true;
    fsn.MAX_FILE_SIZE = 64000;
    
    fsn.getResourcePrefix = function(){
    	var resourcePrefix = "";
    	switch(this.hostname){
    	case "qaenterprise.fsnip.com":
    	case "stgenterprise.fsnip.com":
    	case "enterprise.fsnip.com":
    		resourcePrefix = "/fsn-core";
    		break;
    	default://DEV
    		resourcePrefix = "/fsn-core";
    		break;
    	}
    	console.log("FSN Resource Prefix:" + resourcePrefix);
    	return resourcePrefix;
    };
	
	fsn.getKMSPrefix = function(){
    	var resourcePrefix = "";
    	switch(this.hostname){
    	case "qaenterprise.fsnip.com":
			resourcePrefix = "http://qacommunity.fsnip.com";
			break;
    	case "stgenterprise.fsnip.com":
			resourcePrefix = "http://stgcommunity.fsnip.com";
			break;
    	case "enterprise.fsnip.com":
    		resourcePrefix ="http://community.fsnip.com";
    		break;
    	default://DEV
    		resourcePrefix = "http://qacommunity.fsnip.com";
    		break;
    	}
    	console.log("FSN ResourceKMS Prefix:" + resourcePrefix);
    	return resourcePrefix;
    };
    fsn.createCORSRequest = function(method, url) {
        var xhr = new XMLHttpRequest();
        if ("withCredentials" in xhr) {
            xhr.open(method, url, true);
        } else if (typeof XDomainRequest != "undefined") {
            xhr = new XDomainRequest();
            xhr.open(method, url);
        } else {
            xhr = null;
        }
        return xhr;
    };
    fsn.makeCorsRequest = function() {
        var url = fsn.getHttpPrefix() + "/templates";
        var xhr = fsn.createCORSRequest('GET', url);
        if (!xhr) {
            throw new Error('CORS not supported');
        }
        xhr.onload = function() {
            var responseText = xhr.responseText;
            fsn.log(responseText);
        };
        xhr.onerror = function() {
            fsn.error('Woops, there was an error making the request.');
        };
        xhr.send();
    };
    fsn.DEFAULT_LOCALES = ["ZHCN", "ZH", "CN", "ZH_CN"];
    fsn.PREVIEW_ROW_COLUMNS = 3;
    fsn.GRID_PAGE_SIZES = [10, 25, 50];
    fsn.GRID_DEFAULT_PAGESIZE = 10;
    fsn.DATE_PICKER_FORMAT = "yyyy/MM/dd";
    fsn.DATE_PICKER_CULTURE = "zh-CN";
    fsn.findDefaultLocale = function(locales) {
        if (locales && locales.length && locales.length > 0) {

            for (var k = 0; k < fsn.DEFAULT_LOCALES.length; k++) {
                for (var i = 0; i < locales.length; i++) {
                    if (fsn.DEFAULT_LOCALES[k] === locales[i].locale.toUpperCase()) {
                        return locales[i];
                    }
                }
            }
            return locales[0];
        }
        return null;
    };

    //fsn.instrumentTemplateID

    fsn.updateButtons = function(dialog, buttons){
        var win = $("#"+dialog);
        var windowObject = $("#"+dialog).data("kendoWindow");
        
        var buttonsDiv = $("#"+dialog +" div.buttons")[0];
        
        //fsn.log(buttonsDiv);
        
        if(buttons){
            
            var div = $("<div style='margin-right: 35px;margin-top:5px; float: right;'></div>");
            buttons = buttons || {};
            $.each( buttons, function( key, value ) {
                var btn = $("<button class='k-button  k-button-icontext'><span class='padding: .4em 1em;display: block;line-height: normal'>"+key+"</span></button>");
                btn.click(value);
                div.append(btn);
            }); 
            
            if(buttonsDiv){
                //fsn.log(buttonsDiv);
                $(buttonsDiv).empty();
                $(buttonsDiv).append(div);
            }else{
                $(win.children()[0]).height( windowObject.options['height'] - 55);
                var outDiv = $("<div class='buttons'  style='clear: both;border-top: 1px solid #aaaaaa;height: 35px; text-align: right; margin-top:5px; '></div>");
                outDiv.append(div);
                win.append(outDiv);
            }
        } 
        
    };
    
    fsn.msg = function(id,result,mes,width){//html中div的ID ， ResponserVO对象 ， 弹出信息  ,弹出框宽度
    	var divcss = { 
    			background: '#EEE', 
    			margin: '10px 0 0', 
    			padding: '5px 10px', 
    			border: '2px solid #33CC33',
    	}; 
    	if(width){
    		divcss.width = width;
    	}
    	if(null == result){
    		divcss.border = "2px solid #FF3333";
    		divcss.bottom="55px";
        	$("#" + id).css(divcss);
        	$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    		setTimeout('$("#' +id+'").hide("slow")',5000);
    	}else if(result.success ==true){
    		divcss.border = "2px solid #519505";
        	$("#" + id).css(divcss);
        	$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
       		setTimeout('$("#' +id+'").show("slow")',10);
          	setTimeout('$("#' +id+'").hide("slow")',5000);
        }else if(result.success == false){
        	divcss.border = "2px solid #519505";
    		$("#" + id).css(divcss);
    		$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
        	setTimeout('$("#' +id+'").hide("slow")',5000);
    	}else {
    		divcss.border = "2px solid #FF3333";
        	$("#" + id).css(divcss);
        	$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    		setTimeout('$("#' +id+'").hide("slow")',5000);
        } 
    };
    fsn.backMsg = function(id,mes){//html中div的ID ， ResponserVO对象 ， 弹出信息  ,弹出框宽度
    	var divcss = { 
    			background: '#EEE', 
    			margin: '10px 0 0', 
    			padding: '5px 10px', 
    			border: '2px solid #33CC33',
    	}; 
		divcss.border = "2px solid #519505";
		divcss.position="fixed";
		divcss.bottom="2em";
		divcss.left="15em";
		divcss.right="15em";
		$("#" + id).css(divcss);
		
		var html = '';
		if($("#get-html").length<=0){
			$("body").append('<div id="get-html" style="display:none;"></div>')
		}
		$("#get-html").html(mes);
		var _msg=$("#get-html").text();
		var isHidden=false;
		if(mes.length>55||$("#get-html").find("img").size()>0){
			isHidden=true;
		}
		if(isHidden){
			html += '<div  style="word-warp:break-word;word-break:break-all"><span style="color: #FF3333;">'+
			_msg+'</span><a class="in-block folder">[查看更多]</a></div><div class="none hiddenword" style="word-warp:break-word;word-break:break-all;display:none;">'+mes+'</div>';
        	$("#"+id).html(html);
		}else{
			$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
		}
        setTimeout('$("#' +id+'").show("slow")',10);
        var foldercss = { 
        		 cursor: "pointer" 
    	}; 
        $(".folder").css(foldercss);
        $(".folder").bind("click",function(event){
			if($(this).html() == "[查看更多]"){
				$(this).siblings("span").html($(this).parent(this).siblings(".hiddenword").html());			
				$(this).html("[收起]");
			}else{
				$(this).siblings("span").html($(this).parent(this).siblings(".hiddenword").text().substr(0,55));
				$(this).html("[查看更多]");
			}
		});
    	
    };
    fsn.GetLength = function(str) {
    	if(str == null){
    		return 0;
    	}
        //获得字符串实际长度，中文2，英文1
        var realLength = 0, len = str.length, charCode = -1;
        for (var i = 0; i < len; i++) {
            charCode = str.charCodeAt(i);
            if (charCode >= 0 && charCode <= 128) realLength += 1;
            else realLength += 2;
        }
        return realLength;
    };
    
    fsn.initDialog = function(dialog, options, buttons) {
        var win = $("#"+dialog);
        if (!win.data("kendoWindow")) {
            win.kendoWindow();
        }
        var windowObject = $("#"+dialog).data("kendoWindow");
        options = options || {};
        var defaultOptions = {
                 width: 600,
                 height: 300,
                 close: function() {},
                 visible:false,
                 title:"",
        };
        $.extend(defaultOptions, options);
        windowObject.setOptions(  defaultOptions ); 
        windowObject.center();
        
        if(buttons){
            $(win.children()[0]).height( windowObject.options['height'] - 55);
            var div = $("<div class='buttons' style='margin-right: 35px;margin-top:5px; float: right;'></div>");
            buttons = buttons || {};
            $.each( buttons, function( key, value ) {
                var btn = $("<button class='k-button  k-button-icontext'><span class='padding: .4em 1em;display: block;line-height: normal'>"+key+"</span></button>");
                btn.click(value);
                div.append(btn);
            }); 
            var outDiv = $("<div style='clear: both;border-top: 1px solid #aaaaaa;height: 35px; text-align: right; margin-top:5px; '></div>");
            outDiv.append(div);
        
            win.append(outDiv);
        } 
        /*
        
        $("#" + dialog + "")
            .dialog({
            autoOpen: false,
            
            modal: true,
             buttons: _buttons,
        });*/
        return windowObject;
    };

    fsn.localized = function(string, module, locale) {
        locale = locale || fsn.defaultLocale;
        codes = fsn[locale];
        commonCodes = fsn[locale];
        if (module) {
            if (fsn[module] && fsn[module][locale]) {
                codes = fsn[module][locale];
            }
        }
        
        if (codes && codes[string]) {
            return codes[string];
        }
        if (commonCodes && commonCodes[string]) {
            return commonCodes[string];
        }

        return string;

    };

    fsn.l = function(string, module, locale) {
        return fsn.localized(string, module, locale);
    };

    fsn.gridPageMessage = function() {
        return {
            empty: fsn.l("No items to display"),
            itemsPerPage: fsn.l("items per page"),
            of: fsn.l("of {0}"),
            last: fsn.l("Go to the last page"),
            first: fsn.l("Go to the first page"),
            next: fsn.l("Go to the next page"),
            previous: fsn.l("Go to the previous page"),
            refresh: fsn.l("Refresh"),
            page: fsn.l("Page"),
            display: fsn.l("{0} - {1} of {2} items"),
        };
    };

    fsn.updateLocale = function() {
        $('*[data-fsn-text]')
            .each(function() {
            var pack = $(this)
                .attr("data-fsn-i18n-package");
            var ht = fsn.l($(this)
                .attr("data-fsn-text"), pack);
            if ($(this)
                .is('button')) {
                try {
                    $(this)
                        .html('<span class="padding: .4em 1em;display: block;line-height: normal">'+ht+'</span>');
                } catch (e) {
                    $(this)
                        .html(ht);
                }
            } else {
                $(this)
                    .html(ht);
            }
        });
        $('input[data-fsn-placehoder]')
            .each(function() {
            var pack = $(this)
                .attr("data-fsn-i18n-package");
            $(this)
                .attr("placeholder", fsn.l($(this)
                .attr("data-fsn-placehoder"), pack));
        });

        $("*[data-fsn-format]")
            .each(function() {
            var pack = $(this)
                .attr("data-fsn-i18n-package");
            $(this)
                .attr("data-fsn-format", fsn.l($(this)
                .attr("data-fsn-format"), pack));
        });
    };

    fsn.flash = function(zone, args) {
        if (args && args.length > 0) {
            var st = args[0];
            var newArgs = [];
            for (var i = 1; i < args.length; i++) {
                newArgs.push(args[i]);
            }
            $('#' + zone)
                .html(st.format(newArgs));
        } else {
            $('#' + zone)
                .html("");
        }
        $('#' + zone)
            .show();
        $('#' + zone)
            .fadeOut(5000);
    };

    fsn.gridfilterMessage = function() {
    	return {
    		info: fsn.l("Show items with value that:"),
			and: fsn.l("and"),
			or: fsn.l("or"),
			filter: fsn.l("Filter"),
			clear: fsn.l("Clear")
    	};
    };
    fsn.gridfilterOperators = function() {
    	return {
		     contains: fsn.l("Contains"),
		     doesnotcontain: fsn.l("Does not contain"),
		     startswith: fsn.l("Starts with"),
		     endswith: fsn.l("Ends with"),
		     eq: fsn.l("Is equal to"),
		     neq: fsn.l("Is not equal to")
    	};
    };
    fsn.gridfilterOperatorsNumber = function() {
    	return {
    		eq: fsn.l("Is equal to"),
    		neq: fsn.l("Is not equal to"),
    		gte: fsn.l("Greater than or equal to"),
    		gt:  fsn.l("Greater than"),
    		lte: fsn.l("Less than or equal to"),
    		lt:  fsn.l("Less than"),
    	};
    };
    fsn.gridfilterOperatorsDate = function() {
    	return {
    		gte: fsn.l("Greater than or equal to"),
    		gt:  fsn.l("Greater than"),
    		lte: fsn.l("Less than or equal to"),
    		lt:  fsn.l("Less than"),
    		eq:  fsn.l("Is equal to"),
    		neq: fsn.l("Is not equal to"),
    	};
    };
    
    
    
    
    fsn.callBackFunction = function(func, transport) {
        if (func && typeof(func) == 'function') {
            var callBack = func;
            callBack(transport);
            return true;
        } else {
            return false;
        }
    };
    
    fsn.getUrlVars = function() {
        var vars = [],
            hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1)
            .split('&');
        for (var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
    };
    
  
    
    fsn.getPriority = function(){
        return fsn.l("PRIORITY VALUES");
    };
    
    fsn.alert = function(m){
        alert(m);
    };
    
    fsn.initMenu = function(){
    	$.get(fsn.getHttpPrefix()+ "/menu_new.html", function(data) {
			$("#menu").html(data);
			$("#menu_list,#usr_menu").kendoMenu({
				animation: { open: { effects: "fadeIn" } },
				orientation : "vertical"
			});
			var user = fsn.session('user');
			var menubar = fsn.session('menubar');
			if(menubar){
				var menu = $("#menu_list").data("kendoMenu");
				menu.append(menubar);
			}else{
				$.ajax({
					url : fsn.getHttpPrefix() + "/user/menubar/" + user.roleId,
					type : "GET",
					dataType : "json",
					async : false,
					success : function(data) {
						if (data.status == "false") {
							window.location.pathname = data.url;
						} else {
							var menu = $("#menu_list").data("kendoMenu");
							menu.append(data.menubar);
							$("#menu_loginUser").html(data.loginUser);
						}
					}
				});
			}
			
			/* var menu = $("#menu_list").data("kendoMenu");
			menu.append({
				text : "Message",
				cssClass : "menu_item",
				url : "#"
			}); */
			$("#menu_logout").bind("click", function() {
				fsn.session('user', null);
				fsn.session('menubar', null);
				window.location.pathname = fsn.getHttpPrefix();
			});
		});
    };
    
    fsn.initReturnWindow = function(yesFun,noFun,msg,confirm){
    	$("#RETURN_COMMON_WIN").kendoWindow({
    		width: "480",
    		height:"auto",
    		title: confirm !=undefined ? fsn.l("Return Message"):fsn.l("WIN_BLANK"),
    				visible: false,
    				resizable: false,
    				draggable:false,
    				modal: true
    	});

    	$("#RETURN_COMMON_WIN button").addClass("k-button");

    	if(yesFun){
    		$("#return_yes_btn").bind("click",function(){
    			yesFun();
    			fsn.closeReturnWin();
    		});
    	}else{
    		$("#return_yes_btn").bind("click",fsn.closeReturnWin);
    	}
    	
    	if(noFun){
    		$("#return_no_btn").bind("click",noFun);
    	}else{
    		$("#return_no_btn").bind("click",fsn.closeReturnWin);
    	}
    	
    };
    
    fsn.openReturnWin = function(){
    	$("#RETURN_COMMON_WIN").data("kendoWindow").open().center();
    };
    fsn.closeReturnWin = function(){
    	$("#RETURN_COMMON_WIN").data("kendoWindow").close();
    };
    
    
    fsn.initConfirmWindow = function(yesFun,noFun,msg,confirm){
    	$("#CONFIRM_COMMON_WIN").kendoWindow({
			width: "480",
			height:"auto",
	        title: confirm !=undefined ? fsn.l("WIN_CONFIRM"):fsn.l("WIN_BLANK"),
	        visible: false,
	        resizable: false,
	        draggable:false,
	        modal: true
		});
    	
    	console.log(msg);
    	
    	//add the "k-button" css onto buttons
    	$("#CONFIRM_COMMON_WIN button").addClass("k-button");
    	if(msg){
    		$("#CONFIRM_MSG").html(msg);
    	}else{
    		$("#CONFIRM_MSG").html(fsn.l('WIN_DEFAULT_MSG'));
    	}
    	
    	if(yesFun){
    		$("#confirm_yes_btn").bind("click",function(){
    			yesFun();
    			fsn.closeConfirmWin();
    		});
    	}else{
    		$("#confirm_yes_btn").bind("click",fsn.closeConfirmWin);
    	}
    	
    	if(noFun){
    		$("#confirm_no_btn").bind("click",noFun);
    	}else{
    		$("#confirm_no_btn").bind("click",fsn.closeConfirmWin);
    	}
    	
    };
    fsn.closeConfirmWin = function(){
    	$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
    };
    
    fsn.initNotificationMes = function(mes,isSuccess) {
    	var notification = $("#RETURN_MES").getKendoNotification();
    	if(!notification){
    		notification = $("#RETURN_MES").kendoNotification({
    			position: {
    				pinned: true,
    				bottom: 60,
    			},
    			autoHideAfter: isSuccess ? 3000:10000,
//    			stacking: "down",
    			templates: [{
    				type: "error",
    				template: $("#errorTemplate").html()
    			}, {
    				type: "upload-success",
    				template: $("#successTemplate").html()
    			}]
    			
    		}).data("kendoNotification");
    	}
    	 if(isSuccess){
    		 notification.show({
    			 message: mes
    		 }, "upload-success");
    	 }else{
    		 notification.show({
    			 message: mes
    		 }, "error");
    	 }
    	$(".k-animation-container").css("z-index",10050);
	};

	/*按需格式化企业其他认证grid中的截止日期*/
	fsn.formatGridDate = function (value) {
        if (value == null || value.length < 1) {
            return "";
        }
        if (value.length == 10) {
            return value;
        }
        var status = value.toString();
        if (status.indexOf("-") > 3) {
            return value.substring(0, 10);
        }
        var endDate = new Date(value);
        if(endDate == null) {
        	return "";
        }
        var year = endDate.getFullYear();
        var month = endDate.getMonth() + 1;
        var day = endDate.getDate();
        return year + "-" + month + "-" + day;
    };
	
    /**
     * 刷新数据源（解决最后一页无法自动刷新的问题）
     * @author tangxin
     */
    fsn.fleshGirdPageFun = function(dataSource){
    	var page = dataSource.page();
    	var flage = (dataSource.data().length < 2);
    	if(page > 1 && flage) {
    		dataSource.page(--page);
    	} else {
    		dataSource.read();
    	}
    };
    
    /**
     * 
     * @author longxianzhen 2015/08/03
     */
    fsn.getViewimageUrl = function(imgUrl,height,width){
    	
    	if(imgUrl.indexOf("viewimage")>0 ){
    		return imgUrl;
    	}
    	imgUrl=imgUrl.replace("http://","");
    	var imgUrls = imgUrl.split("/");
    	var iUrl="";
    	for(var i=0;i<imgUrls.length;i++){
    		if(i==0){
    			iUrl=imgUrls[i]+"/viewimage";
    		}else if(i==imgUrls.length-1){
    			var lastUrls=imgUrls[i];
    			var postfix=lastUrls.split(".")[1];
    			iUrl=iUrl+"/"+lastUrls.split(".")[0]+"/"+height+"x"+width+"."+postfix;
    		}else{
    			iUrl=iUrl+"/"+imgUrls[i];
    		}
    	}
    	return "http://"+iUrl;
    };
    
    /**
     * 初始化kendoWindow窗体
     * @author ZhangHui 2015/5/5
     */
    fsn.initKendoWindow = function(winId,title,width,height,isVisible,actions){
    	$("#"+winId).kendoWindow({
    		title:title,
    		width:width,
    		height:height,
    		modal:true,
    		visible:isVisible,
    		actions:actions!=null?actions:["Close"],
    	});
    };

    /**
     * 根据数据类型获取基础值域数据集
     * @param type 数据类型
     * @returns 返回数据集
     * @author ZhangHui 2015/11/10
     */
    fsn.data_set=null;
    fsn.getDataSet = function(type){
        var data = null;

        if((!fsn.data_set)){
            var url=fsn.HTTP_PREFIX + "/sys/getDataSet";
            $.ajax({
                url: url,
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnVal){
                    if (returnVal.result.success) {
                        // 整理基础数据集
                        var response = returnVal.data;
                        fsn.data_set = [];
                        for(var i=0; i<response.length; i++){
                            if(!fsn.data_set[response[i].type]){
                                fsn.data_set[response[i].type] = [];
                            }
                            fsn.data_set[response[i].type].push(response[i]);
                        }
                    }
                }
            });
        }
        data = fsn.data_set[type];
        return data;
    };
    
})(jQuery);