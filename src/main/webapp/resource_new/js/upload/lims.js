(function($) {
    var lims = window.lims = window.lims || {};
    
    lims.hostname = window.location.hostname;
    
    
    lims.TEMPLATE_MIN_SIZE = 800;
    lims.PREVIEW_ROW_COLUMNS = 3;
    lims.PREIVEW_PERCENTAGE_ROW_COLUMNS = 4;
    lims.TEMPLATE_FORM_DIALOG_MIN_HEIGHT = 500;
    lims.defaultLocale = "zh";
    lims.DATE_FORMAT = 'YYYY-MM-dd hh:mm:ss';
    lims.ENABLE_GROUP_FIELD = true;
    lims.MAX_FILE_SIZE = 64000;
    
    lims.CUSTOMER_URL = "lims.fsnip.com";
    
    String.prototype.format = String.prototype.f = function(arg) {
        var s = this,
            i = arg.length;

        while (i--) {
            s = s.replace(new RegExp('\\{' + i + '\\}', 'gm'), arg[i]);
        }
        return s;
    };
    
    lims.getHttpPrefix = function(){
    	var httpPrefix = "";
    	switch(this.hostname){
    	case lims.CUSTOMER_URL://PROD
    	case "stglims.fsnip.com"://STG
    	case "qalims.fsnip.com"://QA
    		httpPrefix = "/service";
    		break;
    	default://DEV
    		httpPrefix = "/lims-core/service";
    		break;
    	}
    	console.log("LIMS Http Prefix:" + httpPrefix);
    	return httpPrefix;
    };
    lims.getLogoutURL = function(){
    	var httpPrefix = "";
    	switch(this.hostname){
    	case lims.CUSTOMER_URL://PROD
    	case "stglims.fsnip.com"://STG
    	case "qalims.fsnip.com"://QA
    		httpPrefix = "/logout";
    		break;
    	default://DEV
    		httpPrefix = "/lims-core/logout";
    		break;
    	}
    	console.log("LIMS Logout Prefix:" + httpPrefix);
    	return httpPrefix;
    };
    
    lims.getResourcePrefix = function(){
    	var resourcePrefix = "";
    	switch(this.hostname){
    	case lims.CUSTOMER_URL://PROD
    	case "stglims.fsnip.com"://STG
    	case "qalims.fsnip.com"://QA
    		resourcePrefix = "";
    		break;
    	default://DEV
    		resourcePrefix = "/lims-core";
    		break;
    	}
    	console.log("LIMS Resource Prefix:" + resourcePrefix);
    	return resourcePrefix;
    };
    
    lims.getStandardHttpPrefix = function(){
    	var standardHttpPrefix = "";
    	switch(this.hostname){
    	case lims.CUSTOMER_URL://PROD
    		standardHttpPrefix = "http://std.fsnip.com/service";
    		break;
    	case "stglims.fsnip.com"://STG
    		standardHttpPrefix = "http://stgstd.fsnip.com/service";
    		break;
    	case "qalims.fsnip.com"://QA
    		standardHttpPrefix = "http://qastd.fsnip.com/service";
    		break;
    	default://DEV
    		standardHttpPrefix = "http://local.standard.com:8088/service";
    		break;
    	}
    	console.log("Standard Http Prefix:" + standardHttpPrefix);
    	return standardHttpPrefix;
    };
    lims.getFsnHttpPrefix = function(){
    	var fsnHttpPrefix = "";
    	switch(this.hostname){
    	case lims.CUSTOMER_URL://PROD
    		fsnHttpPrefix = "http://enterprise.fsnip.com/service";
    		break;
    	case "stglims.fsnip.com"://STG
    		fsnHttpPrefix = "http://stgenterprise.fsnip.com/service";
    		break;
    	case "qalims.fsnip.com"://QA
    		fsnHttpPrefix = "http://qaenterprise.fsnip.com/service";
    		break;
    	default://DEV
    		fsnHttpPrefix = "http://localhost:8080/fsn-core/service";
    		break;
    	}
    	console.log("fsn Http Prefix:" + fsnHttpPrefix);
    	return fsnHttpPrefix;
    };
    lims.createCORSRequest = function(method, url) {
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
    lims.makeCorsRequest = function() {
        var url = this.getHttpPrefix() + "/templates";
        var xhr = lims.createCORSRequest('GET', url);
        if (!xhr) {
            throw new Error('CORS not supported');
        }
        xhr.onload = function() {
            var responseText = xhr.responseText;
            lims.log(responseText);
        };
        xhr.onerror = function() {
            lims.error('Woops, there was an error making the request.');
        };
        xhr.send();
    };
    lims.DEFAULT_LOCALES = ["ZHCN", "ZH", "CN", "ZH_CN"];
    lims.GRID_PAGE_SIZES = [10, 25, 50];
    lims.GRID_DEFAULT_PAGESIZE = 10;
    lims.DATE_PICKER_FORMAT = "yyyy/MM/dd";
    lims.DATE_PICKER_CULTURE = "zh-CN";
    lims.findDefaultLocale = function(locales) {
        if (locales && locales.length && locales.length > 0) {

            for (var k = 0; k < lims.DEFAULT_LOCALES.length; k++) {
                for (var i = 0; i < locales.length; i++) {
                    if (lims.DEFAULT_LOCALES[k] === locales[i].locale.toUpperCase()) {
                        return locales[i];
                    }
                }
            }
            return locales[0];
        }
        return null;
    };

    lims.error = function(d) {
        console.error(d);
    };
    lims.log = function(d) {
        console.log(d);
    };
    lims.debug = function(d) {
        console.debug("*********");
        console.debug(d);
    };
    lims.openConfirmWin = function(){
    	$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
    };
    lims.closeConfirmWin = function(){
    	$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
    };
    lims.openReturnWin = function(){
    	$("#RETURN_COMMON_WIN").data("kendoWindow").open().center();
    };
    lims.closeReturnWin = function(){
    	$("#RETURN_COMMON_WIN").data("kendoWindow").close();
    };
    lims.initConfirmWindow = function(yesFun,noFun,msg,confirm){
    	$("#CONFIRM_COMMON_WIN").kendoWindow({
			width: "480",
			height:"auto",
	        title: confirm !=undefined ? lims.l("WIN_CONFIRM"):lims.l("WIN_BLANK"),
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
    		$("#CONFIRM_MSG").html(lims.l('WIN_DEFAULT_MSG'));
    	}
    	
    	if(yesFun){
    		$("#confirm_yes_btn").bind("click",function(){
    			yesFun();
    			lims.closeConfirmWin();
    		});
    	}else{
    		$("#confirm_yes_btn").bind("click",lims.closeConfirmWin);
    	}
    	
    	if(noFun){
    		$("#confirm_no_btn").bind("click",noFun);
    	}else{
    		$("#confirm_no_btn").bind("click",lims.closeConfirmWin);
    	}
    	
    };
    lims.initReturnWindow = function(yesFun,noFun,msg,confirm){
    	$("#RETURN_COMMON_WIN").kendoWindow({
    		width: "480",
    		height:"auto",
    		title: confirm !=undefined ? lims.l("Return Message"):lims.l("WIN_BLANK"),
    				visible: false,
    				resizable: false,
    				draggable:false,
    				modal: true
    	});
    	
    	//add the "k-button" css onto buttons
    	$("#RETURN_COMMON_WIN button").addClass("k-button");
//    	if(msg){
//    		$("#RETURN_LABEL").html(msg);
//    	}else{
//    		$("#RETURN_LABEL").html(lims.l('WIN_DEFAULT_MSG'));
//    	}
    	
    	if(yesFun){
    		$("#return_yes_btn").bind("click",function(){
    			yesFun();
    			lims.closeReturnWin();
    		});
    	}else{
    		$("#return_yes_btn").bind("click",lims.closeReturnWin);
    	}
    	
    	if(noFun){
    		$("#return_no_btn").bind("click",noFun);
    	}else{
    		$("#return_no_btn").bind("click",lims.closeReturnWin);
    	}
    	
    };
    
    lims.message = function(id,result,mes,width){//html中div的ID ， ResponserVO对象 ， 弹出信息  ,弹出框宽度
    	var divcss = { 
    			width:'100%',
    			background: '#EEE', 
    			margin: '10px 0 0', 
    			padding: '5px 10px', 
    			border: '2px solid #33CC33',
    	}; 
    	if(width){
    		divcss.width = width;
    	}
    	if(null == result){
    		divcss.border = "2px solid #FF3333"
        	$("#" + id).css(divcss);
        	$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    	}else if(result == true){
    		divcss.border = "2px solid #519505"
    		$("#" + id).css(divcss)
    		$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    		setTimeout('$("#' +id+'").hide("slow")',2000);
        }else if(result.success == true){
    		divcss.border = "2px solid #519505"
    		$("#" + id).css(divcss)
    		$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
        	setTimeout('$("#' +id+'").hide("slow")',2000);
    	}else if(result.success == false){
    		divcss.border = "2px solid #FF3333"
    		$("#" + id).css(divcss)
    		$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    	}else if(result.errorMsg != null){
    		divcss.border = "2px solid #FF3333"
    		$("#" + id).css(divcss)
    		$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ result.errorMsg+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    	}else if(result.warningMsg != null){
    		divcss.border = "2px solid #FFCC00"
    		$("#" + id).css(divcss)
    		$("#" + id).html('<span class="k-icon k-warning"></span>&nbsp;<span style="color: #FFCC00;">'+ result.warningMsg+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    	}
    	//setTimeout('$("#' +id+'").show("slow")',50);
    	//setTimeout('$("#' +id+'").hide("slow")',1500);
    };
    
    lims.msg = function(id,result,mes,width){//html中div的ID ， ResponserVO对象 ， 弹出信息  ,弹出框宽度
    	var divcss = { 
    			width:'100%',
    			background: '#EEE', 
    			margin: '10px 0 0', 
    			padding: '5px 10px', 
    			border: '2px solid #33CC33',
    	}; 
    	if(width){
    		divcss.width = width;
    	}
    	if(null == result){
    		divcss.border = "2px solid #FF3333"
        	$("#" + id).css(divcss)
        	$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    	}else if(result == true){
    		divcss.border = "2px solid #519505"
        	$("#" + id).css(divcss)
        	$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
       		setTimeout('$("#' +id+'").show("slow")',10);
          	setTimeout('$("#' +id+'").hide("slow")',2000);
        }else if(result.status == "true"){
    		divcss.border = "2px solid #519505"
    		$("#" + id).css(divcss)
    		$("#" + id).html('<span class="k-icon k-success"></span>&nbsp;<span style="color: #519505;">'+mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
        	setTimeout('$("#' +id+'").hide("slow")',2000);
    	}else if(result.status == "false"){
    		divcss.border = "2px solid #FF3333"
    		$("#" + id).css(divcss)
    		$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
    	}else {
    		divcss.border = "2px solid #FF3333"
        	$("#" + id).css(divcss)
        	$("#" + id).html('<span class="k-icon k-i-close"></span>&nbsp;<span style="color: #FF3333;">'+ mes+'</span>');
    		setTimeout('$("#' +id+'").show("slow")',10);
        } 
    	/*setTimeout('$("#' +id+'").show("slow")',50);
    	setTimeout('$("#' +id+'").hide("slow")',1500);*/
    };
    
    lims.clrmsg=function(id,mes){
    	$("#"+id).css({ 
			width:'',
			background: '', 
			margin: '', 
			padding: '', 
			border: '',
	});
		$("#"+id).html(mes);
    };
    
    lims.initGoBackConfrimWin = function(yesFun,confirm){
    	$("#CONFIRM_REASON_WIN").kendoWindow({
			width: "480",
			height:"auto",
	        title: confirm !=undefined ? lims.l("WIN_CONFIRM"):lims.l("WIN_BLANK"),
	        visible: false,
	        resizable: false,
	        draggable:false,
	        modal: true
		});
    	$("#goback_submit").bind("click",function(){
    		$("#ERROR_MSG").html('');
    		if($("#GOBACK_REASON").val().trim() != ""){
    			yesFun($("#GOBACK_REASON").val());
    			$("#CONFIRM_REASON_WIN").data("kendoWindow").close();
    		}else{
    			$("#ERROR_MSG").html('请输入退回原因!');
    		}
    	});
    	
    	$("#goback_cancel").bind("click",lims.closeGoBackConfrimWin);
    };
    
    lims.openGoBackConfrimWin = function(){
    	$("#CONFIRM_REASON_WIN").data("kendoWindow").open().center();
    };
    
    lims.closeGoBackConfrimWin = function(){
    	$("#CONFIRM_REASON_WIN").data("kendoWindow").close();
    };
    
    lims.generateName = function(name, locales) {
        var loc = lims.findDefaultLocale(locales);
        if(loc){
            return loc.displayName;
        }else{
            return name;
        }
    };

    //lims.instrumentTemplateID

    lims.updateButtons = function(dialog, buttons, findex){
        var win = $("#"+dialog);
        var windowObject = $("#"+dialog).data("kendoWindow");
        
        var buttonsDiv = $("#"+dialog +" div.buttons")[0];
 
        
        if(buttons){
            var div = $("<div style='margin-right: 35px;margin-top:5px; float: right;'></div>");
            buttons = buttons || {};
            $.each( buttons, function( key, value ) {
                var btn = $("<button class='k-button  k-button-icontext'><span class='padding: .4em 1em;display: block;line-height: normal'>"+key+"</span></button>");
                btn.click(value);
                div.append(btn);
            }); 
            
            if(buttonsDiv){
                //lims.log(buttonsDiv);
                $(buttonsDiv).empty();
                $(buttonsDiv).append(div);
            }else{
                if(findex || findex == 0){
                    $(win.children()[findex]).height( windowObject.options['height'] - 55);
                }
                var outDiv = $("<div class='buttons'  style='clear: both;border-top: 1px solid #aaaaaa;height: 35px; text-align: right; margin-top:5px; '></div>");
                outDiv.append(div);
                win.append(outDiv);
            }
        } 
        
    };
    
    lims.initDialog = function(dialog, options, buttons) {
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

    lims.localized = function(string, module, locale) {
        locale = locale || lims.defaultLocale;
        codes = lims[locale];
        commonCodes = lims[locale];

        if (module) {
            if (lims[module] && lims[module][locale]) {
                codes = lims[module][locale];
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

    lims.l = function(string, module, locale) {
        return lims.localized(string, module, locale);
    };

    lims.gridPageMessage = function() {
        return {
            empty: lims.l("No items to display"),
            itemsPerPage: lims.l("items per page"),
            of: lims.l("of {0}"),
            last: lims.l("Go to the last page"),
            first: lims.l("Go to the first page"),
            next: lims.l("Go to the next page"),
            previous: lims.l("Go to the previous page"),
            refresh: lims.l("Refresh"),
            page: lims.l("Page"),
            display: lims.l("{0} - {1} of {2} items"),
        };
    };
    lims.gridfilterMessage = function() {
    	return {
    		info: lims.l("Show items with value that:"),
			and: lims.l("and"),
			or: lims.l("or"),
			filter: lims.l("Filter"),
			clear: lims.l("Clear")
    	};
    };
    lims.gridfilterOperators = function() {
    	return {
		     contains: lims.l("Contains"),
		     doesnotcontain: lims.l("Does not contain"),
		     startswith: lims.l("Starts with"),
		     endswith: lims.l("Ends with"),
		     eq: lims.l("Is equal to"),
		     neq: lims.l("Is not equal to")
    	};
    };
    lims.gridfilterOperatorsNumber = function() {
    	return {
    		eq: lims.l("Is equal to"),
    		neq: lims.l("Is not equal to"),
    		gte: lims.l("Greater than or equal to"),
    		gt:  lims.l("Greater than"),
    		lte: lims.l("Less than or equal to"),
    		lt:  lims.l("Less than"),
    	};
    };
    lims.gridfilterOperatorsDate = function() {
    	return {
    		gte: lims.l("Greater than or equal to"),
    		gt:  lims.l("Greater than"),
    		lte: lims.l("Less than or equal to"),
    		lt:  lims.l("Less than"),
    		eq:  lims.l("Is equal to"),
    		neq: lims.l("Is not equal to"),
    	};
    };

    lims.updateLocale = function() {
        $('*[data-lims-text]')
            .each(function() {
            var pack = $(this)
                .attr("data-lims-i18n-package");
            var ht = lims.l($(this)
                .attr("data-lims-text"), pack);
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
        $('input[data-lims-placehoder]')
            .each(function() {
            var pack = $(this)
                .attr("data-lims-i18n-package");
            $(this)
                .attr("placeholder", lims.l($(this)
                .attr("data-lims-placehoder"), pack));
        });

        $("*[data-lims-format]")
            .each(function() {
            var pack = $(this)
                .attr("data-lims-i18n-package");
            $(this)
                .attr("data-lims-format", lims.l($(this)
                .attr("data-lims-format"), pack));
        });
        
        
        $("*[data-required-msg]")
            .each(function() {
            var pack = $(this)  .attr("data-lims-i18n-package");
            $(this).attr("data-required-msg", lims.l($(this)
                .attr("data-required-msg"), pack));
        });
        $("*[validationMessage]")
            .each(function() {
            var pack = $(this)  .attr("data-lims-i18n-package");
            $(this).attr("validationMessage", lims.l($(this)
                .attr("validationMessage"), pack));
        });
        
    };
    lims.flash = function(zone, args) {
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

    lims.callBackFunction = function(func, transport) {
        if (func && typeof(func) == 'function') {
            var callBack = func;
            callBack(transport);
            return true;
        } else {
            return false;
        }
    };
    
    lims.getUrlVars = function() {
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
    
  
    
    lims.getPriority = function(){
        return lims.l("PRIORITY VALUES");
    };
    
    lims.alert = function(m){
        alert(m);
    };
    
    lims.PARALLEL_COUNT = 1;
    
    
    lims.correlationCoefficient = function(zon, gid, xs, ys) {
        if(gid){
            var count = $("#"+zon+"_COUNT");
            count = count ?count.val(): 6;
            xs = [];
            ys = [];
            var data = $("#" + gid) .data("kendoGrid").dataSource.data();
            for(var i = 0; i < count; i++){
                xs.push(isNaN(data[0]["value_"+i])?0.0:data[0]["value_"+i]);
                ys.push(isNaN(data[1]["value_"+1])?0.0:data[1]["value_"+1]);
            }
        }
        var avex =  lims.average(xs);
        var avey =  lims.average(ys);
        var m = 0; n1 = 0, n2 = 0;
        for(var i = 0; i < avex.length; i++){
            m += (xs[i] - avex) * (ys[i] - avey);
            n1 += Math.pow(xs[i] - avex, 2);
            n2 += Math.pow(ys[i] - avey, 2);
        }
        var ix = m / Math.pow(n1, 0.5) / Math.pow(n2, 0.5);
        return ix;
    };
    
    lims.linearRegression = function(zon, gid, xs, ys) {
        if(gid){
            var count = $("#"+zon+"_COUNT");
            count = count ?count.val(): 6;
            xs = [];
            ys = [];
            var data = $("#" + gid) .data("kendoGrid").dataSource.data();
            for(var i = 0; i < count; i++){
                xs.push(isNaN(data[0]["value_"+i])?0.0:data[0]["value_"+i]);
                ys.push(isNaN(data[1]["value_"+1])?0.0:data[1]["value_"+1]);
            }
        }
        var avex =  lims.average(xs);
        var avey =  lims.average(ys);
        var m = 0; n = 0;
        for(var i = 0; i < avex.length; i++){
            m += (xs[i] - avex) * (ys[i] - avey);
            n += (xs[i] - avex) * (xs[i] - avex);
        }
        var ix = m / n;
        var k = avey -  avex * ix;
        return [ "y = "+ix+"x + "+k, ix, k];
    };
    
    lims.average = function(xs){
        var avex = 0;
        if(!xs || xs.length == 0 ) return 0.0;
        for(var i = 0 ; i < xs.length; i++){
            avex += xs[i];
        }
        return avex / xs.length;
    };
    
    var sort = window.lims.sort = window.lims.sort || {};
    sort.configure = function(s){
        if(!s) return "";
        var orderby = [];
        for(var i = 0; i < s.length; i++){
           orderby.push(" " + s[i].field+" "+ s[i].dir); 
        }
        return orderby.join(", ");
    };
    //将毫秒值转换为YYYY-MM-DD格式的字符串
    lims.fmtMsDate = function(ms){
		var _date=new Date(ms),_mouth,_day;
		_mouth=_date.getMonth()+1<10?"0"+(_date.getMonth()+1):_date.getMonth()+1;
		_day=_date.getDate()+1<10?"0"+_date.getDate():_date.getDate();
		return _date.getFullYear()+"-"+_mouth+"-"+_day;
	};
})(jQuery);
