$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var view = window.view = window.view || {};
	var httpPrefix = fsn.getHttpPrefix();
	
	try {
		view.PRODUCT_ID = $.cookie("cook_viewReport_productId").productId;
		/*$.cookie("cook_edit_promotion", JSON.stringify(null), {
			path : '/'
		});*/
	} catch (e) {}
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-06
	 */
	function initComponent(){
		initViewReport();
	}
	
	function viewPdf(pdfPath){
		if(pdfPath == null) return;
		var pdfHtml = ''+
		'<!--[if IE]>'+
		'<object classid="clsid:CA8A9780-280D-11CF-A24D-444553540000" width="800" height="1050" border="0">'+  
		'<param name="_Version" value="65539">'+
		'<param name="_ExtentX" value="20108">'+  
		'<param name="_ExtentY" value="10866">'+  
		'<param name="_StockProps" value="0">'+ 
		'<param name="SRC" value="'+pdfPath+'">'+
		'</object>'+ 
		'<![endif]-->'+    
		'<!--[if !IE]> <!-->'+
		'<object data="'+pdfPath+'" type="application/pdf" width="800" height="1050">'+       
		'alt : <a href="http://get.adobe.com/cn/reader">Adobe Reader.pdf</a>'+
		'</object>'+
		'<!--<![endif]-->';
		$("#pdf_content").html(pdfHtml);
	}
	
	/**
	 * 页面字段赋值
	 * @author tangxin 2015-05-06
	 */
	function setPageField(viewReport){
		if(viewReport == null) return;
		$("#PRO_NAME").html(viewReport.productName);
		$("#PRO_FORMAT").html(viewReport.productFormat);
		$("#PRO_DESC").html(viewReport.productDesc);
		$("#REP_TYPE").html(viewReport.reportType);
		$("#REP_SELF").html("共"+viewReport.selfNumber+"份");
		$("#REP_CENSOR").html("共"+viewReport.censorNumber+"份");
		$("#REP_SAMPLE").html("共"+viewReport.sampleNumber+"份");
		viewPdf(viewReport.pdfPath);
	}
	
	/**
	 * 初始化页面预览的报告
	 * @author tangxin 2015-05-06
	 */
	function initViewReport(){
		if(view.PRODUCT_ID == null) {
			return;
		}
		$.ajax({
	        url: httpPrefix + "/sales/photos/getSelfReport/" + view.PRODUCT_ID ,
	        type: "GET",
	        dataType: "json",
	        success: function(returnData) {
	           if(returnData.result.status == "true"){
	         	  if(returnData.reportVO != null) {
	         		 setPageField(returnData.reportVO);
	         	  }
	           }
	       }
		});
	}
	
	/* 页面初始化 */
	initComponent();
});