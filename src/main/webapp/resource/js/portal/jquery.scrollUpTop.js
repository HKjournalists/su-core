(function ($) {
	$.scrollUp = function (options) {
		// 设置
		var opt = {
			backTopDom: null, // 设定这个对象实现回到顶部效果
			scrollName: 'scrollUp', // 生成元素ID
			topDistance: '100', // 当滚动到距离顶部300px的时候显示回到顶部图标 (px)
			topSpeed: 300, // 返回顶部时的速度 (ms)
			animation: 'fade', // Fade（淡入淡出式）, slide（幻灯片式）, none（无特效）
			animationInSpeed: 200, // 动画进入速度(ms)
			animationOutSpeed: 200, // 动画退出速度 (ms)
			scrollText: '回到顶部' // 显示文本
		};
		// 如果自定制了设置，加载设置
		if (options) {
			opt = $.extend(opt, options);
		}
		
		var _backTop = opt.backTopDom;
		
		// 生成回到顶部图标元素
		if (_backTop == null || !_backTop[0]) {
			_backTop = $('<a/>', {
				id: opt.scrollName, 
				href: '#top',
				title: opt.scrollText,
				text: opt.scrollText}).appendTo('body');
			
			// 回到顶部按钮的css设置，注意其中的position，这是关键
			_backTop.css({'display':'none','position':'fixed','z-index':'2147483647'});
			
			// 在window中注册滚动事件，当页面向下滚动超过td时出现回到顶部的图标
			$(window).scroll(function(){	
				// 淡入淡出特效
				if (opt.animation === "fade") {
					$(($(window).scrollTop() > opt.topDistance)?_backTop.fadeIn(opt.animationInSpeed):_backTop.fadeOut(opt.animationOutSpeed));
				}
				// 幻灯片特效
				else if (opt.animation === "slide") {
					$(($(window).scrollTop() > opt.topDistance)?_backTop.slideDown(opt.animationInSpeed):_backTop.slideUp(opt.animationOutSpeed));
				}
				// 无特效
				else {
					$(($(window).scrollTop() > opt.topDistance)?_backTop.show(0):_backTop.hide(0));
				}
			});
			
		} else {
			_backTop = $(_backTop).attr("href", "#top");
		}
		
		// 反回顶部功能
		_backTop.click( function(event) {
			$('html, body').animate({scrollTop:0}, opt.topSpeed);
			return false;
		});

	};
}(jQuery));