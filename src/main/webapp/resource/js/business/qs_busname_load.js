(function($) {
	var fsn = window.fsn = window.fsn || {};
	var HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var business_unit = window.fsn.business_unit = window.fsn.business_unit || {};
	var KEYWORD = ""; //关键字 即企业名称
	var PAGE = 1; //当前页数
	var PAGE_SIZE = 10; //每页请求的数量
    
    /**
     * 生产许可证中生产企业名称，滚动分页事件
     * @author tangxin 2015-05-29
     */
	 function busNameScrollFun(){
		 /* 当前滚动条滚动的高度 */
		 var scrollTop = $("#busunitName_listbox").scrollTop();
		 /* 滚动窗体的高度 */
		 var windowHeight = $("#busunitName_listbox").height();
		 /* 滚动窗体中企业名称条目 */
		 var listLi = $("#busunitName_listbox li");
		 var scrollHeight = 0; //可以滚动的总高度
		 for(var i=0;i<listLi.length;i++){
			 var h = listLi[i].offsetHeight;
			 scrollHeight += h;
		 }
		 scrollHeight = parseInt(scrollHeight);
		 var subNub = 0;
		 if(PAGE != -1)	subNub = PAGE * 2;
		 /* 当滚动条滚到底部时，请求下一页数据 */
		 if((scrollTop + windowHeight) == (scrollHeight - subNub) && PAGE != -1){
		 	$("#busunitName_listbox").scrollTop(scrollTop - 30);
		 	PAGE += 1;
		 	var listDs = searchBusNameByKeyword(KEYWORD, PAGE, PAGE_SIZE);
		 	if(listDs == null) return;
		 	if(listDs.length < 10) PAGE = -1;
		 	for(var j=0;j<listDs.length;j++){
		 		$("#busunitName").data("kendoAutoComplete").dataSource.add(listDs[j]);
		 	}
		 	$("#busunitName").data("kendoAutoComplete").refresh();
		 }
	 };
    
	 /**
	  * 格式化企业名称数据
	  * @author tangxin 2015-05-29
	  */
	 function formatBusNameAry(listName){
		 if(listName == null && listName.length < 1){
			 return null;
		 }
		 var listAry = [];
		 for(var i=0;i<listName.length;i++){
			 listAry.push({name:listName[i]});
		 }
		 return listAry;
	 }
	 
    /**
     * 根据关键字分页查收企业名称
     * @author tangxin 2015-05-29
     */
    function searchBusNameByKeyword(keyword, page, pageSize){
    	var paramKeyword = "";
    	if(keyword != null && keyword != ""){
    		paramKeyword = "&name=" + keyword;
    	}
    	var listBus = null;
    	$.ajax({
    		url:HTTP_PREFIX + "/business/searchAllBusUnitName?page=" + page + "&pageSize=" + pageSize + paramKeyword,
    		type:"GET",
    		async:false,
    		success:function(returnData){
    			if(returnData.result.status == "true"){
    				listBus = formatBusNameAry(returnData.data);
    			}
    		}
    	});
    	return listBus;
    }
    
    /**
     * 初始化QS号信息中，生产企业下拉选择控件
     * @author tangxin 2015-05-29
     */
    business_unit.initQSInfoBusName = function(){
    	$("#busunitName").kendoAutoComplete({
    	    dataSource: [],
    	    filter: "contains",
    	    placeholder: "搜索...",
    	    dataTextField: "name",
    	    dataBound: function() {
    	        if (KEYWORD == this.value().trim()) {
    	            return;
    	        }
    	        KEYWORD = this.value().trim();
    	        //关键字被更改后，将page设置为1
    	        PAGE = 1;
    	        var listName = searchBusNameByKeyword(KEYWORD, PAGE, PAGE_SIZE);
    	        this.setDataSource(listName);
    	        this.refresh();
    	        this.search(KEYWORD);
    	    },
    	});
    	/* 为滚动条绑定滚动事件 */
    	$("#busunitName_listbox").scroll(function() {
    		busNameScrollFun();
    	});
    };
    	   
})(jQuery);