 $(document).ready(function() {
	 var drag = window.drag = window.drag || {}; // 全局命名空间
	 var httpRrefix = fsn.getHttpPrefix(); // 业务请求前缀
	 drag.sortFun = null;
	 drag.currentFormId = null
	 
	 /*以下方法提供图片合成pdf调用，纯js代码*/
	 /*全局变量，拖动的对象*/
	 var dragobj={};
	 window.onerror=function(){return false;};
	 drag.domid = 0;
	 function on_ini(){
	     String.prototype.inc=function(s){return this.indexOf(s)>-1?true:false;};
	     var agent=navigator.userAgent;
	     window.isOpr=agent.inc("Opera");
	     window.isIE=agent.inc("IE") && !isOpr;
	     window.isMoz=agent.inc("Mozilla") && !isOpr && !isIE;
	     if(isMoz){
	         Event.prototype.__defineGetter__("x",function(){return this.clientX+2;});
	         Event.prototype.__defineGetter__("y",function(){return this.clientY+2;});
	     }
	     basic_ini();
	 }
	 
	 function basic_ini(){
	     window.oDel=function(obj){if(obj!=null){obj.parentNode.removeChild(obj);}};
	 }
	 
	 drag.initDrag=function(fromId, sortFun){
		 initHtml(fromId);
	     on_ini();
	     drag.bindEvent();
	     drag.currentFormId = fromId;
	     drag.sortFun = sortFun;
	 };

	 drag.bindEvent = function(){
		 var o=document.getElementsByName("moImg");
	     for(var i=0;i<o.length;i++){
	         o[i].onmousedown=drag.addevent;
	         o[i].style.cursor = "move";
	     }
	 };
	 
	 drag.unBindEvent = function(){
		 var o=document.getElementsByName("moImg");
	     for(var i=0;i<o.length;i++){
	         o[i].onmousedown = null;
	         o[i].style.cursor = "auto";
	     }
	 };
	 
	 drag.addevent = function (e){
	     if(dragobj.o!=null)
	         return false;
	     e=e||event;
	     dragobj.o=this.parentNode;
	     dragobj.xy=getxy(dragobj.o);
	     dragobj.tx=getxy(document.getElementById("tabstrip"));
	     dragobj.xx=new Array((e.x-dragobj.xy[1]+dragobj.tx[1]),(e.y-dragobj.xy[0]+dragobj.tx[0]));
	     dragobj.o.style.width="210px";//dragobj.xy[2]+"px";
	     dragobj.o.style.height="160px";//dragobj.xy[3]+"px";
	     dragobj.o.style.left=(e.x-dragobj.xx[0]+10)+"px";
	     dragobj.o.style.top=(e.y-dragobj.xx[1]-10)+"px";
	     dragobj.o.style.position="absolute";
	     dragobj.o.style.filter='alpha(opacity=60)';        //添加拖动透明效果
	     var om=document.createElement("div");
	     dragobj.otemp=om;
	 	 om.style.cssText = "float:left";
	     om.style.width="210px";//dragobj.xy[2]+"px";
	     om.style.height="160px";//dragobj.xy[3]+"px";
	     om.style.border = "1px dashed red";//ikaiser添加，实现虚线框
	     dragobj.o.parentNode.insertBefore(om,dragobj.o);
	     return false;
	 };
	 
	 document.onselectstart=function(){return false;};
	 window.onfocus=function(){document.onmouseup();};
	 window.onblur=function(){document.onmouseup();};
	 
	 document.onmouseup=function(){
	     if(dragobj.o!=null){
	         dragobj.o.style.width="210px";//"auto";
	         dragobj.o.style.height="160px";//"120px";
	         dragobj.otemp.parentNode.insertBefore(dragobj.o,dragobj.otemp);
	         dragobj.o.style.position="";
	         oDel(dragobj.otemp);
	         dragobj={};
	         if(drag.sortFun != null){
	        	 drag.sortFun(drag.currentFormId);
	         }
	     }
	 };
	 
	 document.onmousemove=function(e){
	     e=e||event;
	     if(dragobj.o!=null){
	    	 dragobj.o.style.left=(e.x-dragobj.xx[0])+"px";
	         dragobj.o.style.top=(e.y-dragobj.xx[1])+"px";
	         createtmpl(e, dragobj.o);    //传递当前拖动对象
	     }
	 };
	 function getxy(e){
	     var a=new Array();
	     var t=e.offsetTop;
	     var l=e.offsetLeft;
	     var w=e.offsetWidth;
	     var h=e.offsetHeight;
	     while(e=e.offsetParent){
	         t+=e.offsetTop;
	         l+=e.offsetLeft;
	     }
	     a[0]=t;a[1]=l;a[2]=w;a[3]=h;
	   return a;
	 }
	 function inner(o,e){
	     var a=getxy(o);
	     dragobj.tx=getxy(document.getElementById("tabstrip"));
	     /*
	      *鼠标的X坐标>移动对象的L&&<(L+W)&&鼠标的y坐标>T&&<t+h 
	      */
	     if(e.x>a[1] && e.x<(a[1]+a[2]) && e.y>(a[0]-dragobj.tx[0]) && e.y<(a[0]+a[3])){
	         if(e.x<(a[1]+a[2]/2)){
	        	 return 1;
	         } else {
	        	 return 2;
	         }
	     }else{
	    	 return 0;
	     }
	 }
	 
	 /**
	  * 将当前拖动层在拖动时可变化大小，预览效果
	  * @param e 鼠标移动时的传递的事件对象
	  * @parem elm 拖动的对象
	  */
	 function createtmpl(e, elm){
	     for(var i=0;i<drag.domid;i++){
	    	 var mi = document.getElementById("m"+i);
	         if(mi == null) continue; //已经移出的层不再遍历
	         if(mi == dragobj.o) continue;
	         var b=inner(mi, e);
	         if(b==0) continue;
	         dragobj.otemp.style.width = mi.offsetWidth;
	         elm.style.width = mi.offsetWidth;
	         //1为下移，2为上移
	         drag.pdfRes = null;
	         if(b==1){
	        	 document.getElementById("m"+i).parentNode.insertBefore(dragobj.otemp,document.getElementById("m"+i));
	         }else{
	             if(document.getElementById("m"+i).nextSibling==null){
	            	 document.getElementById("m"+i).parentNode.appendChild(dragobj.otemp);
	             }else{
	            	 document.getElementById("m"+i).parentNode.insertBefore(dragobj.otemp,document.getElementById("m"+i).nextSibling);
	             }
	         }
	         return
	     }
	         if(document.getElementById("dom0").innerHTML.inc("div")||document.getElementById("dom0").innerHTML.inc("DIV"))
	             return;
	         var op=getxy(document.getElementById("dom0"));
	         if(e.x>(op[1]+10) && e.x<(op[1]+op[2]-10)){
	        	 document.getElementById("dom0").appendChild(dragobj.otemp);
	             dragobj.otemp.style.width=(op[2]-10)+"px";
	         }
	 }
	 
	 /**
	  * 创建支持拖动的html标签区域
	  */
	 function initHtml(formId){
		 if(formId == null) return;
		 var str ='<div class="content"><div class="left" id="dom0"></div></div>';
		 $("#"+formId).html(str);
	 }
	 
     /*根据页面调整的顺序，来对attachments集合排序*/
     function sortAttachments(attachments){
    	 if(attachments == null) return;
    	 /* 将原资源数组的排序字段这位默认值 -1 */
    	 for(var t=0;t<attachments.length;t++){
    		 attachments[t].orderNumber = -1;
    	 }
    	 var listMo = $("div.mo");
    	 for(var i=0;i<drag.domid;i++){
    		 var guid = $(listMo[i]).attr("data-guid-text");
    		 for(var j=0;j<attachments.length;j++){
    			 if(guid==attachments[i].guid){
    				 attachments[j].orderNumber = (i+1);
    				 break;
    			 }
    		 }
    	 }
     }
     
     /**
      * 向可拖动区域添加资源,参数必需为数组
      * @author tangxin 2015-05-07
      */
     drag.addAttachments = function(formId,attachments){
    	 if(attachments == null) return;
    	 var dom0 = $("div#"+formId+" div#dom0");
    	 var listDiv = $("div#"+formId+" div.mo");
    	 for(var j=0;j<listDiv.length;j++){
    		 $(listDiv[j]).attr("id","m"+(j+1));
    	 }
    	 var len = listDiv.length;
    	 drag.domid = len;
    	 for(var i=0;i<attachments.length;i++){
    		 drag.domid += 1;
    		 var div = $("<div rid="+attachments[i].id+"  id=\"m"+(i+len)+"\" class=\"mo\"  style=\"width: auto; height: 120px; top: 12px;\"></div>");
    		 var span = '<span class="name_span">'+attachments[i].fileName+'</span><a title="移除照片" class="ico_close_circle"></a>';
    		 var img=document.createElement("img");
    		 img.src=attachments[i].url;
    		 img.onmousedown=drag.addevent;
    		 div.append($(img));
    		 div.append(span);
    		 $(div).mouseover(function(){
    			 $(this).find("a.ico_close_circle").css("display","block");
    		 });
    		 $(div).mouseout(function(){
    			 $(this).find("a.ico_close_circle").css("display","none");
    		 });
    		 dom0.append(div);
    	 }
     };
     
     /**
      * 向可拖动区域添加资源,单个文件
      * @author tangxin 2015-05-07
      */
     drag.addSortAttachment = function(formId, attachment, removeImgFun){
    	 if(attachment == null) return;
    	 var url="http://fsnrec.com:8080/portal/img/product/temp/temp.jpg";
 		 if(attachment.url!=null&&attachment.url!=""){
 			url=attachment.url;
 		 }
    	 var dom0 = $("div#"+formId+" div#dom0");
    	 var listDiv = $("div#"+formId+" div.mo");
    	 var len = listDiv.length;
    	 for(var j=0;j<len;j++){
    		 $(listDiv[j]).attr("id","m"+j);
    	 }
    	 drag.domid = len + 1;
    	 attachment.sort = (listDiv.length + 1);
		 var div = $("<div oid="+attachment.id+" fid="+attachment.sortFieldId+" id=\"m"+(listDiv.length)+"\" class=\"mo\"  style=\"width: 210px; height: 160px; top: 10px;\"></div>");
		 var span = '<span class="name_span name_span_mov">'+attachment.name+'</span><a oid="'+attachment.id+'" title="移除照片" class="ico_close_circle"></a>';
		 var img=document.createElement("img");
		 img.name = "moImg";
		 img.src=url;
		 img.onmousedown=drag.addevent;
		 div.append($(img));
		 div.append(span);
		 $(div).mouseover(function(){
			 $(this).find("a.ico_close_circle").css("display","block");
		 });
		 $(div).mouseout(function(){
			 $(this).find("a.ico_close_circle").css("display","none");
		 });
		 $(div).find("a.ico_close_circle").click(function(){
			 $(this).parent().remove();
			 if(removeImgFun != null){
				 removeImgFun($(this).attr("oid"), false);
			 }
		 });
		 dom0.append(div);
     };
     
 });