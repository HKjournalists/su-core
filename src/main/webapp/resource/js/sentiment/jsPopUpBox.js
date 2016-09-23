// JavaScript Document
(function(){
	  $.MsgBox = {
		        Confirm: function (title, msg, callback) {
		            GenerateHtml(title, msg);
		            btnOk(callback);
		            btnNo();
		        }
    }
	  
	  //生成Html
	  var GenerateHtml = function (title, msg){
		  var _overlay = '<div id="win_overlay"class="k-overlay" style="display: block; z-index: 10002; opacity: 0.5;"></div>';
		  $("body").append(_overlay);
		  var _html ='<div id="win_content" class="k-widget k-window" style="z-index: 10008;opacity: 1; -webkit-transform: scale(1);">'+
		  			 	 '<div class="k-window-titlebar k-header">&nbsp;<span class="k-window-title">'+title+'</span>'+
		  			 	/*	'<div class="k-window-actions">'+
		  			 		'<a  id="winClose" role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-close">Close</span></a>'+
		  			 	    '</div>'+*/
		  			     '</div>'+
			  			 '<div class="k-popup-edit-form k-window-content k-content" data-role="window" tabindex="0">'+
			  			 	'<div class="k-edit-form-container">'+
			  			 		'<p class="k-popup-message">'+msg+'</p>'+
			  			 		'<div class="k-edit-buttons k-state-default">'+
			  			 			'<a id="win_ok" class="k-button k-gantt-cancel" href="#"><span class="k-icon k-update"></span>确定</a>'+
			  			 			'<a id="wind_on" class="k-primary k-button k-gantt-delete" href="#"><span class="k-icon k-cancel"></span>取消</a>'+
			  			 		'</div>'+
			  			 	'</div>'+
			  			 '</div>'+
		  			 '</div>';
		  $("body").append(_html);
		  var _widht = document.documentElement.clientWidth;  //屏幕宽
	      var _height = document.documentElement.clientHeight; //屏幕高
	      var boxWidth = $("#win_content").width();
	      var boxHeight = $("#win_content").height();
	        
	      
	      //让提示框居中
	       $("#win_content").animate({
	    	   	 top: (_height - boxHeight) / 2 +$(window).scrollTop()+ "px", 
	    	   	 left:(_widht - boxWidth) / 2+ "px",
	    	   "-webkit-transition":"left 0.3s ease-out",
	       },400);
	  }
	  
	  //确定按钮事件
	   var btnOk = function (callback) {
	        $("#win_ok").click(function () {
	            $("#win_content").remove();
	            $("#win_overlay").remove();
	            if (typeof (callback) == 'function') {
	                callback();
	            }
	        });
	    }
	    //取消按钮事件
	    var btnNo = function () {
	        $("#wind_on,#win_close").click(function () {
	            $("#win_content").remove();
	            $("#win_overlay").remove();
	        });
	   }
})();


