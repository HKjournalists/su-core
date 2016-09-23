(function($){
	/**
	 * @desc 仿oninput的特殊事件，因为onpropertychange在ie下，用js改变值也会被触发，
	 * 在suggest插件中就不是很好用了。故使用这个特殊事件插件, 
     * 原插件位于http://www.zurb.com/playground/jquery-text-change-custom-event，
	 * 这里已经对实现做了修改，将.data('lastValue')改为.attr('lastValue')存储以提高性能。
	 */
	$.event.special.textchange = { //bind('textchange', function(event, oldvalue, newvalue){}) //传入旧值和当前新值
			setup: function (data, namespaces) {
				$(this).attr('lastValue', this.contentEditable === 'true' ? $(this).html() : $(this).val());
				$(this).bind('keyup.textchange', $.event.special.textchange.handler);
				$(this).bind('cut.textchange paste.textchange input.textchange', $.event.special.textchange.delayedHandler);
			},
			teardown: function (namespaces) {
				$(this).unbind('.textchange');
			},
			handler: function (event) {
				$.event.special.textchange.triggerIfChanged($(this));
			},
			delayedHandler: function (event) {
				var element = $(this);
				setTimeout(function () {
					$.event.special.textchange.triggerIfChanged(element);
				}, 25);
			},
			triggerIfChanged: function (element) {
				var current = element[0].contentEditable === 'true' ? element.html() : element.val();
				if (current !== element.attr('lastValue')) {
					element.trigger('textchange',  element.attr('lastValue'), current).attr('lastValue', current);
				};
			}
	};
	
	/**
	 * @author LHFS
     * @date 2013-6-1
     * @update 2013-6-1
     * @desc 输入内容的匹配自动提示 
     * @question: 目前在刚打开FF浏览器时，就会触发input事件，这个需要在实践中发现是否需要init规避
     * @DONE：
     * 1、避免网络延时造成的多次ajax结果混乱，需要根据时间抛弃过时的ajax处理
     * 2、保证唯一的<del>suggest-wrap及其事件绑定</del>,ie6 shim, suggest-wrap不能唯一，
     * 其绑定的click选择事件会调用到c.onselect，而c.onselect可以是多个input的配置项目 
     * 3、唯一的resize绑定
     * @release 1.0
     * 1、添加unselect参数，并修订一些回调传入参数
     * 2、ajax请求做了300ms的延迟，防止太频繁触发。
     * 3、更换了数据过期的处理方法。
     * 4、补上了标准浏览器的 onpaste oncut事件监听
	 */
	$.fn.suggest = function(c){
        c = $.extend({
        	doquery:function(){return true;},
        	relparam:false,
        	url: 'ajax-ok.do',
        	type: "GET", // 默认是以GET的方式发起url
        	queryName: null, //url?queryName=value,默认为输入框的name属性
        	jsonp: null, //设置此参数名，将开启jsonp功能，否则使用json数据结构
        	item: 'li', //下拉提示项目单位的选择器，默认一个li是一条提示，与processData写法相关
        	itemOverClass: 'suggest-curr-item', //当前下拉项目的标记类，可以作为css高亮类名
        	sequential: 0, //按着方向键不动是否可以持续选择，默认不可以，设置值可以是任何等价的boolean。
        	delay:100, //持续选择的延迟时间，默认100ms，是用来提高选择体验的
        	'z-index': 99999, //提示层的层叠优先级设置，css，你懂的
        	processData: function(data){ //自定义处理返回的数据，该方法可以return一个html字符串或jquery对象，将被写入到提示的下拉层中
	            // 生成一个ul对象
        		var _ul = $('<ul class="suggest-list"></ul>');
				var evenOdd = {'0' : 'suggest-item-even', '1': 'suggest-item-odd'}, count = 0;
				var _oList = data.RESTResult.list;
				for(var key in _oList) { //添加奇数，偶数区分
					var _n = _oList[key].name, _obj = _oList[key];
					var _li = $('<li></li>').attr({'class':evenOdd[(++count) % 2]}).html(_n);
					if(_obj){
						_li.get(0).o = _obj;
					}
					_ul.append(_li);
				}
				return _ul;
        	},
        	getCurrItemValue: function($currItem){ //定义如何去取得当前提示项目的值并返回值,插件根据此函数获取当前提示项目的值，并填入input中，此方法应根据processData参数来定义
        		return $currItem.text();
        	},
        	textchange: function($input, event){}, //不同于change事件在失去焦点触发，inchange依赖本插件，只要内容有变化，就会触发，并传入input对象
        	onselect: function($currItem, $input){}, //当选择了下拉的当前项目时执行，并传入当前项目
            unselect: function($input, e) {}, //没有选择任何提示项目，直接TAB或回车
        	onblur: function($input, e){} //当input失去焦点后执行 
        }, c);

		var ie = !-[1,], ie6 = ie && !window.XMLHttpRequest,
        CURRINPUT = 'suggest-curr-input', SUGGESTOVER = 'suggest-panel-overing', suggestShimId = 'suggest-shim-iframe',
        UP = 38, DOWN = 40, ESC = 27, TAB = 9, ENTER = 13,
        CHANGE = 'input.suggest paste.suggest cut.suggest '/*@cc_on + ' textchange.suggest'@*/, RESIZE = 'resize.suggest',
        BLUR = 'blur.suggest', KEYDOWN = 'keydown.suggest', KEYUP = 'keyup.suggest';
       
        return this.each(function(){
        	var $t = $(this).attr('autocomplete', 'off');
        	var hyphen = c.url.indexOf('?') != -1 ? '&' : "?"; //简单判断，如果url已经存在？，则jsonp的连接符应该为&
            var URL = c.jsonp ? [c.url, hyphen, c.jsonp, '=?'].join('') : c.url, //开启jsonp，则修订url，不可以用param传递，？会被编码为%3F
            CURRITEM = c.itemOverClass,  $currItem = $(), sequentialTimeId = null;
        	 
        	var $suggest = $(["<div style='position:absolute;zoom:1;z-index:", c['z-index'], "' class='auto-suggest-wrap'></div>"].join('')).appendTo('body');
        	
            $suggest.bind({
            	'mouseenter.suggest': function(e){
            		$(this).addClass(SUGGESTOVER);
            	},
            	'mouseleave.suggest': function(){
            		$(this).removeClass(SUGGESTOVER);
            	},
            	'click.suggest': function(e){
            		var $item = $(e.target).closest(c.item);
            		if($item.length) {
            			$t.val(c.getCurrItemValue($item));
            			c.onselect($item, $t);
                		suggestClose();
            		};
            	},
            	'mouseover.suggest': function(e) {
            		var $item = $(e.target).closest(c.item), currClass = '.' + CURRITEM;
            		if($item.length && ! $item.is(currClass)) {
            			$suggest.find(currClass).removeClass(CURRITEM);
            			$currItem = $item.addClass(CURRITEM);
            		};
            	}
            });
            
            /* iframe shim遮挡层 ie6, 可以共用一个 */
            if(ie6) {
            	var $suggestShim = $('#' + suggestShimId);
            	if(! $suggestShim.length) {
            		$suggestShim = $(["<iframe src='about:blank' style='position:absolute;filter:alpha(opacity=0);z-index:", c['z-index'] - 2, "' id='", suggestShimId ,"'></iframe>"].join('')).appendTo('body');
            	};
            };

            /*window resize调整层位置 */
            $(window).resize(function(){
            	fixes();
            });
            
            function fixes() {
        		var offset = $t.offset(),
                h = $t.innerHeight(),
                w = $t.innerWidth(),
                css = {
                    'top': offset.top + h + 3,
                    'left': offset.left,
                    'width': w                
                };
            	$suggest.css(css);
            	if(ie6) {
            		css['height'] = $suggest.height();
            		$suggestShim.css(css).show();
            	};
            };
            function suggestClose() {
                $suggest.hide().removeClass(SUGGESTOVER);
                if(ie6) {
                    $suggestShim.hide();
                };
            };
        	
        	var selectBusy = false /* 防止一直按键时候不停触发keydown */, triggerChange = true /*for ie*/,
            keyHandler = { //没有上下条的时候，要回到input内
            	'move': function(down) {
        			if(! $suggest.is(":visible"))
        				return;
            		if($currItem.length) {
        				$currItem.removeClass(CURRITEM);
            			if(down) {
                			$currItem = $currItem.next();
                		} else {
                			$currItem = $currItem.prev();
                		};
            		} else {
            			$currItem = $suggest.find(c.item + (down ? ':first' : ':last'));
            		};
            		
        			if($currItem.length) {
            			$currItem.addClass(CURRITEM);
            			// key up or down : when the item has the focus, this input value is set value of the item text
//            			$t.val($currItem.text());
//            			$t.val(c.getCurrItemValue($currItem)); 
            		} else {
            			$t.val($t.attr('curr-value'));//.focus()
            		};
            		selectBusy = true; //或者setTimeout每隔一段时间就设置一次selectBusy = false,这样可以缓慢移动
            	},
            	'select': function(e) { //选择
            		if($currItem.length) {
            			$t.val(c.getCurrItemValue($currItem));
            			c.onselect($currItem, $t);
            		} else {
                        c.unselect($t, e);
                    };
            		suggestClose();
            	}
            };
        	//input需要绑定的变量
        	var inputEvents = {};
			inputEvents[KEYUP] = function(e) { //监听方向键
            	selectBusy = false;
            	//sequentialTimeId && clearTimeout(sequentialTimeId);
            	if(ie) {
            		var kc = e.keyCode;
                	if(kc === UP || kc === DOWN || kc === TAB || kc === ENTER || kc === ESC) { //for IE: 因为ie使用keyup判断change事件，需要排除控制键,并且事件绑定在前，保证第一次就生效
                		triggerChange = false;
                	} else {
                		triggerChange = true;
                	}; 
            	};
            };
			inputEvents[BLUR] = function(e){ //失去焦点触发
            	if(! $suggest.hasClass(SUGGESTOVER)) { //焦点先于点击，这里判断防止失去焦点后直接隐藏，导致点击选择项目无效
            		suggestClose();
            		c.onblur($t, e);
            	};
            };
			inputEvents[KEYDOWN] = function(e) { //监听方向键
        		var kc = e.keyCode;
            	if(kc === UP || kc === DOWN ) { //方向键
            		if(! selectBusy) {
            			keyHandler.move(kc === DOWN);
            			
                		if(c.sequential) { //是否开启了连续按键响应
                			sequentialTimeId = setTimeout(function(){
                				selectBusy = 0;
                			}, c.delay);
                		};
            		};
            	} else if(kc === TAB || kc === ENTER) {
            		keyHandler.select(e);
            		if(kc === ENTER)
            			e.preventDefault();
            	} else if(kc == ESC) {
            		$t.val($t.attr('curr-value'));
            		suggestClose();
            	}; 
            };
        	inputEvents[CHANGE] = function(e) { //值改变触发
                if($t.data('changeDelayId')) {
                    clearTimeout($t.data('changeDelayId'));
                };

                //延迟毫秒
                var changeDelayTime = 300;
                var changeDelayId = setTimeout(function(){

                    if(ie && ! triggerChange) {
                        return;
                    };
                    var value = $.trim($t.val());
                    if(value) {

                        if(!c.doquery()){ // 如果有需要关联过来的其他对象，则判断
                        	return false;
                        }
                    	
                        $t.attr('curr-value', value); //keep input value，这里的操作导致IE不能使用propertychange事件绑定，会造成死循环，故使用textchange事件扩展插件
                        var param = {}, queryNames = c.queryName ? c.queryName : $t.attr('name'); //如果未设置参数查询名字，默认使用input自身name
                        param["queryName"] = queryNames;
                        param["queryParam"] =  encodeURIComponent(value);
                        if(c.relname) param["relName"] = c.relname; // 如果有关联的参数则将参数名字添加进去
                        if(c.relparam) param["relParam"] = c.relparam($t); // 如果有关联的参数则将参数添加进去
                        
                        // console.log(param);
                        var xhr = $.ajax({
                        	type: c.type,
                        	url:URL,
                        	data:param,
                        	datatype:'json',
                        	success:function(data){
                        		 var _xhr = $t.data('cache-xhr');
                                 if(_xhr && _xhr.readyState != 4) {
                                     _xhr.abort();
                                 };

                                 if(data) {
                                 	$suggest.html("").append(c.processData(data, $t));
                                     fixes();
                                     $suggest.show();
                                     $currItem = $(); //有新数据，重置$currItem
                                 } else {
                                     $suggest.hide();
                                 };
                        	},
                        	error:(function(request,status,err){
    							var errText = request.responseText;
    							var ErrMessage = "页面出现"+request.status+"错误信息，\n";
    							if (!errText) {
    								return false;
    							}
    							ErrMessage += "错误内容为："+request.statusText+"\n"+errText.substring(errText.indexOf("<pre>")+5,errText.indexOf("</pre>"));
    							alert(ErrMessage);
    							// $("#"+c.selectstatusid).html("");
    						})
                        });
                        /*var xhr = $.getJSON(URL, param, function(data){
                            var _xhr = $t.data('cache-xhr');
                            if(_xhr && _xhr.readyState != 4) {
                                _xhr.abort();
                            };

                            if(data) {
                            	$suggest.html("").append(c.processData(data));
                                fixes();
                                $suggest.show();
                                $currItem = $(); //有新数据，重置$currItem
                            } else {
                                $suggest.hide();
                            };
                        }); */ 
                        $t.data('cache-xhr', xhr);
                    } else {
                        $suggest.hide();
                    };
                    c.textchange($t, e); //执行配置中的textchange，顺便提供一个有用的api
                }, changeDelayTime);

                $t.data('changeDelayId', changeDelayId);
			};
			
    		$t.bind(inputEvents);
        });
    };
})(jQuery);