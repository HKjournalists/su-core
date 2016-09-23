 $(document).ready(function() {
	 
	 var lims = window.lims = window.lims || {};
	 var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	 var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	 portal.HTTP_PREFIX =fsn.getHttpPrefix(); // 业务请求前缀
	 
	 /*以下方法提供图片合成pdf调用，纯js代码*/
	 /*全局变量，拖动的对象*/
	 var dragobj={};
	 var isNewSort = true; 
	 window.onerror=function(){return false;};
	 fsn.domid=0;
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
	 
	 fsn.initPicToPdfWin=function(){
		 isNewSort = true;
	     on_ini();
	 };

	 fsn.bindEvent = function(){
		 var o=document.getElementsByName("moImg");
	     for(var i=0;i<o.length;i++){
	         o[i].onmousedown=fsn.addevent;
	     }
	 };
	 
	 fsn.addevent = function (e){
	     if(dragobj.o!=null)
	         return false;
	     e=e||event;
	     dragobj.o=this.parentNode;
	     dragobj.xy=getxy(dragobj.o);
	     dragobj.tx=getxy(document.getElementById("pictureToPdfWindow"));
	     dragobj.xx=new Array((e.x-dragobj.xy[1]+dragobj.tx[1]),(e.y-dragobj.xy[0]+dragobj.tx[0]));
	     //dragobj.o.className = 'dragging';
	     dragobj.o.style.width=dragobj.xy[2]+"px";
	     dragobj.o.style.height=dragobj.xy[3]+"px";
	     dragobj.o.style.left=(e.x-dragobj.xx[0])+"px";
	     dragobj.o.style.top=(e.y-dragobj.xx[1])+"px";
	     dragobj.o.style.position="absolute";
	     dragobj.o.style.filter='alpha(opacity=60)';        //添加拖动透明效果
	     var om=document.createElement("div");
	     dragobj.otemp=om;
	 	 om.style.cssText = "float:left";
	     om.style.width=dragobj.xy[2]+"px";
	     om.style.height=dragobj.xy[3]+"px";
	     om.style.border = "1px dashed red";    //ikaiser添加，实现虚线框
	     dragobj.o.parentNode.insertBefore(om,dragobj.o);
	     return false;
	 }
	 document.onselectstart=function(){return false;};
	 window.onfocus=function(){document.onmouseup();};
	 window.onblur=function(){document.onmouseup();};
	 document.onmouseup=function(){
	     if(dragobj.o!=null){
	         dragobj.o.style.width="auto";
	         dragobj.o.style.height="auto";
	         dragobj.otemp.parentNode.insertBefore(dragobj.o,dragobj.otemp);
	         dragobj.o.style.position="";
	         oDel(dragobj.otemp);
	         dragobj={};
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
	     dragobj.tx=getxy(document.getElementById("pictureToPdfWindow"));
	     /*
	      *鼠标的X坐标>移动对象的L&&<(L+W)&&鼠标的y坐标>T&&<t+h 
	      */
	     if(e.x>a[1] && e.x<(a[1]+a[2]) && e.y>(a[0]-dragobj.tx[0]) && e.y<(a[0]+a[3])){
	         if(e.x<(a[1]+a[2]/2))
	             return 1;
	         else
	             return 2;
	     }else
	         return 0;
	 }
	 //将当前拖动层在拖动时可变化大小，预览效果
	 function createtmpl(e, elm){
	     for(var i=0;i<fsn.domid;i++){
	         if(document.getElementById("m"+i) == null) //已经移出的层不再遍历
	             continue;
	         if(document.getElementById("m"+i)==dragobj.o)
	             continue;
	         var b=inner(document.getElementById("m"+i),e);
	         if(b==0)
	             continue;
	         dragobj.otemp.style.width=document.getElementById("m"+i).offsetWidth;
	         elm.style.width = document.getElementById("m"+i).offsetWidth;
	         //1为下移，2为上移
	         fsn.pdfRes = null;
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

	 lims.initPicToPdfUpload = function(imgs,attachments){
		 $("#uploadRepPicDiv").html("<input id='uploadRepPics' type='file' />");
		 lims.root.buildUpload("uploadRepPics",attachments,"fileEroMsg", "report");
		 /* 当是上传卫生证图片而不是上传报告图片时，更改按钮上的文字 */
		 if(!lims.isFromTestReport){
			 $("div:has(#uploadRepPics) > span").html("上传卫生证图片");
		 }
		 fsn.pdfRes = null;
		 if(imgs==null||attachments==null) return;
		 var mo = $("<div class='mo' id='m"+fsn.domid+"' data-fname-text='"+imgs.fileName+"'>");
		 var img = $("<img name='moImg' src='data:image/jpg;base64,"+imgs.file+"'/>");
		 var a = $("<a class='ico_close_circle'></a>");
		 $(a).click(function(){
			 fsn.pdfRes = null;
			 fsn.domid-=1;
			 $(this).parent().remove();
		 });
		 mo.append(img,a);
		 $("#dom0").append(mo);
		 fsn.bindEvent();
		 fsn.domid+=1;
	 };
	 
	 /*打开图片转pdf窗口*/
     lims.openPicToPdfWindow = function(){
    	 /*合成pdf窗口页面初始化*/
		 fsn.initPicToPdfWin();
		 /*合成pdf窗口页面上传图片控件初始化*/
		 lims.initPicToPdfUpload(null,lims.root.aryRepAttachments);
		 /* 在每次为按钮绑定事件之前，先删除之前绑定的事件，避免点击按钮时触发两次事件 */
         lims.hidePicToPdfButton();
		 lims.showPicToPdfButton();
    	 $("#pictureToPdfWindow").data("kendoWindow").open().center();
     };
     
     /*打开图片转pdf窗口  来自卫生证上传*/
     lims.openPicToPdfWinFromS = function(){
    	 //是否是报告图片 
    	 lims.isFromTestReport=false;
    	 lims.openPicToPdfWindow();
     };
     
     /*打开图片转pdf窗口 来自PDF上传*/
     lims.openPicToPdfWin = function(){
    	 if(lims.root.aryRepAttachments.length>0){
    		 lims.initNotificationMes('您已经上传过pdf文件了，如果需要利用图片合成pdf，请先删除上传的文件。',false);
    		 return;
    	 }
    	//是否是报告图片
    	 lims.isFromTestReport=true;
    	 lims.openPicToPdfWindow();
     };
	 
     /*根据页面调整的顺序，来对root.aryRepAttachments集合排序*/
     lims.sortPicToPdf = function(){
    	 if(fsn.domid==null||fsn.domid<1||fsn.pdfRes!=null) return;
    	 var sortAryAttachments = new Array();
    	 var listMo = $("div.mo");
    	 for(var i=0;i<fsn.domid;i++){
    		 var fileName = $(listMo[i]).attr("data-fname-text");
    		 for(var j=0;j<lims.root.aryRepAttachments.length;j++){
    			 if(fileName==lims.root.aryRepAttachments[j].fileName){
    				 sortAryAttachments.push(lims.root.aryRepAttachments[j]);
    				 break;
    			 }
    		 }
    	 }
    	 lims.root.aryRepAttachments.length = 0;
    	 for(var k=0;k<sortAryAttachments.length;k++){
    		 lims.root.aryRepAttachments.push(sortAryAttachments[k]);
    	 }
     };
     
     /* 在报告录入界面展示合成pdf后的方法 */
     function showSuccPdfFun(pdfRes){
    	 if(pdfRes == null) {return;}
    	 var dataSource = new kendo.data.DataSource();
		 $("#btn_clearRepFiles").show();
		 lims.root.aryRepAttachments.length=0;
		 lims.root.aryRepAttachments.push(fsn.pdfRes);
		 dataSource.add({attachments:fsn.pdfRes});
		 $("#repAttachmentsListView").kendoListView({
	         dataSource: dataSource,
	         template:kendo.template($("#uploadedFilesTemplate").html()),
	     });
		 lims.addDisabledToBtn(["upload_report_files","openT2P_btn"]);
		 /* 关闭窗口 */
		 fsn.domid=0;
    	 $("#dom0").html("");
    	 $("#pictureToPdfWindow").data("kendoWindow").close();
     }
     /* 在报告录入界面展示卫生证图片合成pdf后的方法 */
     function showSuccPdfFunFromS(pdfRes){
    	 if(pdfRes == null) {return;}
    	 var dataSource = new kendo.data.DataSource();
		 //$("#btn_clearRepFiles").show();
    	 lims.root.aryRepAttachments.length=0;
    	 lims.root.aryRepAttachments=[];
		 lims.root.sanPdfRepAttachments.length=0;
		 lims.root.sanPdfRepAttachments.push(fsn.pdfRes);
		 dataSource.add({attachments:fsn.pdfRes});
		 $("#sanAttachmentsListView").kendoListView({
	         dataSource: dataSource,
	         template:kendo.template($("#uploadedSanFilesTemplate").html()),
	     });
		 $("#SanListView").show();
		 lims.addDisabledToBtn(["openS2P_btn"]);
		 /* 关闭窗口 */
		 fsn.domid=0;
    	 $("#dom0").html("");
    	 $("#pictureToPdfWindow").data("kendoWindow").close();
     }
     
     /* 图片合成pdf成功后，对pdf的处理方法  */
     function convertPdfSuccFun(isView,pdfRes){
    	 /* 如果是预览操作，合成成功后直接在浏览器中打开pdf */
    	 if(isView){
        	window.open(pdfRes.url);
    	 } else {
    		 if(lims.isFromTestReport){
				 showSuccPdfFun(fsn.pdfRes);
	    	 }else{
	    		 showSuccPdfFunFromS(fsn.pdfRes);
	    	 }
    	 }
     }
     
     /*发送请求，在后台将图片合成pdf*/
     fsn.pdfRes = null; /*图片合成pdf后临时使用的变量*/
     lims.savePicturesToPdf = function(reportNo,isView){
    	 /*1.调整图片顺序*/
    	 lims.sortPicToPdf();
    	 if(lims.root.aryRepAttachments==null||lims.root.aryRepAttachments.length<1){
    		 lims.initNotificationMes('请上传需要合成pdf的图片!',false);
    		 return;
    	 }
    	 var resVo={
    			 listResource:lims.root.aryRepAttachments,
    	 };
    	 if(!lims.isFromTestReport){
    		 for(var i=0;i<lims.root.aryRepAttachments.length;i++){
    			 lims.root.sanRepAttachments.push(lims.root.aryRepAttachments[i]);
    		 }
    	 }
    	 $("#saving_msg").html("正在后台合成pdf，请稍候...");
 		 $("#toSaveWindow").data("kendoWindow").open().center();
    	 $.ajax({
				url : portal.HTTP_PREFIX + "/resource/picturesToPdf?reportNo=" + reportNo,
				type : "POST",
				datatype : "json",
				//async:false,
				contentType: "application/json; charset=utf-8",
				data : JSON.stringify(resVo),
				success : function(returnValue) {
					$("#toSaveWindow").data("kendoWindow").close();
					if(returnValue.pdf != null){
						fsn.pdfRes=returnValue.pdf;
						/* 图片合成pdf成功后，对pdf的处理方法  */
						convertPdfSuccFun(isView,fsn.pdfRes);
					}else{
						lims.initNotificationMes('后台合成pdf时出现异常!',false);
					}
				}
		});
     };
     
     /*图片转换pdf 查看pdf*/
     lims.viewPdf = function(reportNo){
    	 if(fsn.pdfRes!=null&&fsn.pdfRes.url){
    		 window.open(fsn.pdfRes.url);
    	 }else if(lims.root.aryRepAttachments.length>0&&fsn.domid>0){
    		 lims.savePicturesToPdf(reportNo,true);
    	 }else{
    		 lims.root.aryRepAttachments.length = 0;
    		 lims.initNotificationMes('请上传需要合成pdf的图片!',false);
    	 }
     };
     
     /*图片转pdf页面ok按钮点击事件*/
     lims.picToPdfFun = function(reportNo){
    	 if(lims.root.aryRepAttachments.length>0&&fsn.domid>0){
    		 if(fsn.pdfRes==null||!fsn.pdfRes.url){
            	 lims.savePicturesToPdf(reportNo,false);
    		 }else{
    			 /* 在报告录入界面显示合成后的pdf */
    			 if(lims.isFromTestReport){
    				 showSuccPdfFun(fsn.pdfRes);
    	    	 }else{
    	    		 showSuccPdfFunFromS(fsn.pdfRes);
    	    	 }
    			 
    		 }
    	 }else{
    		 lims.root.aryRepAttachments.length = 0;
    		 lims.initNotificationMes('请上传需要合成pdf的图片!',false);
    	 }
     };
     
     /*图片转pdf页面Cancel按钮点击事件*/
     lims.closePicToPdfWin = function(){
    	 /*如果用户点击了取消按钮*/
    	 lims.root.aryRepAttachments.length=0;
    	 fsn.domid=0;
    	 $("#dom0").html("");
    	 $("#pictureToPdfWindow").data("kendoWindow").close();
     };
     
     /**
      * 执行图片合成pdf功能的上传图片之前，屏蔽相关按钮的操作
      * @author ZhangHui 2015/5/7
      */
     lims.hidePicToPdfButton = function(){
    	 $("#p2pReview_btn").unbind();
    	 $("#p2pOk_btn").unbind();
    	 $("#p2pCancel_btn").unbind();
     };
     
     /**
      * 执行图片合成pdf功能的上传图片之后，打开相关按钮的操作
      * @author ZhangHui 2015/5/7
      */
     lims.showPicToPdfButton = function(){
    	 $("#p2pReview_btn").bind("click", lims.ReviewPdf);
    	 $("#p2pOk_btn").bind("click", lims.picToPdfWinOk);
    	 $("#p2pCancel_btn").bind("click", lims.closePicToPdfWin);
     };
     
     /**
      * 图片转pdf时的预览方法
      * @author ZhangHui 2015/4/3
      */
     lims.ReviewPdf = function(){
    	 var reportNo=null;
    	 if(lims.isFromTestReport){
    		 reportNo=$("#tri_reportNo").val().trim();
    	 }else{
    		 reportNo=$("#sanitary_cert_no").val().trim();
    	 }
         lims.viewPdf(reportNo);
     };
     
     /**
      * 图片合成pdf时的确定按钮事件
      */
     lims.picToPdfWinOk = function(){
    	 var reportNo=null;
    	 if(lims.isFromTestReport){
    		 reportNo=$("#tri_reportNo").val().trim();
    	 }else{
    		 reportNo=$("#sanitary_cert_no").val().trim();
    	 }
         lims.picToPdfFun(reportNo);
     };
 });