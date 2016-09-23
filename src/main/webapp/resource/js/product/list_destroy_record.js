$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
	var portal = fsn.portal = fsn.portal || {};
	var product = upload.product = upload.product || {};
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	portal.edit_id = null;

	//判断cookie不为空时设置为空
	try {
		portal.edit_id = $.cookie("user_0_edit_product").id;
		if(portal.edit_id!=null){
			$.cookie("user_0_edit_product", JSON.stringify({}), {
				path : '/'
			});
		}
	} catch (e) {
	}

	product.initialize = function(){
		upload.buildGrid("product", this.templatecolumns, this.templateDS);
		$("#product .k-grid-content tr td:first-child a").removeAttr("class");
		$("#product .k-grid-content").attr("style","height: 364px");
		product.initPopup("showQualifiedPopup","查看");
	};
	/**
    	 * 初始化弹框方法
    	 */
    product.initPopup = function(id,title) {
    		var window = $("#"+id);
    		if (!window.data("kendoWindow")) {
    			window.kendoWindow({
    				title:title ,
    				modal:true
    				/*visible : false,
    				resizable : false*/
    			});
    		}
    		window.data("kendoWindow").center();
    	};
	product.templatecolumns = [
	                           {field: "id",title: "序号",width: 25,filterable: false},
	                           {field: "name",title: "产品名称",width: 50,filterable: false},
	                           {field: "barcode",title: "产品条码",width: 50,filterable: false},
	                           {field: "format",title: "规格",width: 50,filterable: false},
	                           {field: "batch",title: "批次",width: 40,filterable: false},
	                           {field: "number",title: "处理数量",width: 40,filterable: false},
	                           {field: "problem_describe",title: "处理原因",width: 40,filterable: false},
	                           {field: "process_time",title: "处理时间",template: '#=process_time=fsn.formatGridDate(process_time)#',width: 40,filterable: false},
	                           {field: "deal_address",title: "处理地点",width: 40,filterable: false},
							   {field: "deal_person",title: "处理人",width: 40,filterable: false},
							   {field: "process_mode",title: "处理方式",width: 40,filterable: false},
							   {field: "remark",title: "备注",width: 40,filterable: false},
							   		{ command: [{
                               				text: "处理证明",
                               				click: function(e){
                               					e.preventDefault();
                               					var editRow = $(e.target).closest("tr");
                               					var temp = this.dataItem(editRow);
                               					product.showQualifiedImg(temp.recAttachments);
                               				}
                               			},
                               		], title:"操作", width: 40, }
	                          /* {width:60,title: fsn.l("Operation"),
	                        	   template:function(e){
	                        		   var tag="<a  onclick='return fsn.upload.product.edit("+e.id+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> </span>" + fsn.l('Edit') + "</a>";
	                        		   tag+="<a id='"+e.id+"' class='k-button k-button-icontext k-grid-ViewDetail delete'><span class='k-icon k-delete'> </span>" + "删除" + "</a>";
	                        		   return tag
	                        	   }
	                           }*/];

	product.templateDS = new kendo.data.DataSource({
		transport: {
			read : {
				type : "GET",
				async:false,
				url : function(options){
					var condition="";
					if(options.filter){
						condition = filter.configure(options.filter);
					}
					if($.trim($("#search_keyword").val())!=''){
						condition+='?keyword='+$.trim($("#search_keyword").val());
					}else{
						condition+='?keyword='+"";
					}
					return portal.HTTP_PREFIX + "/product/getListDestroy"+condition;
				},
				dataType : "json",
				contentType : "application/json"
			},
		},
		schema: {
			data : function(returnValue) {
				return returnValue.list;  //响应到页面的数据
			},
			total : function(returnValue) {
				return returnValue.count;   //总条数
			}         
		},
		batch : true,
		page:1,
		pageSize : 10, //每页显示个数
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true
	});
    product.showQualifiedImg=function (imgs) {
    		var slides = $("#slides");
    		var img ="<div class=\"slides_container\">";
    		for(var i=0;i<imgs.length; i++)
    		{
    			 img =img+ '<div class="slide"><img style="width: 849px;height: 638px" src="'+imgs[i].url+'"/></div>';

    		}
    		img =img+ '</div><a href="#" class="prev"><img src="../../resource/js/slides/img/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>'+
    			'<a href="#" class="next"><img src="../../resource/js/slides/img/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>';
    		slides.html(img);
    		$('#slides').slides();
    		$("#showQualifiedPopup").data("kendoWindow").open();
    	};
	// 编辑
	product.edit = function(id){
		window.location.href = fsn.getResourcePrefix()+"/views/product/add_destroy_record.html?id="+id;
	};
	/*删除数据*/
	$(document).on("click",".delete",function(){
		var id=$(this).attr('id');
		if(confirm("确定要删除这条数据吗？")){
			$.ajax({
				url : portal.HTTP_PREFIX+ "/product/delete?id="+id,
				type : "DELETE",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				success : function(rs) {
					if (rs.status) {
						fsn.initNotificationMes('删除成功',true);
						$("#product").data("kendoGrid").dataSource.read();
					} else {
						fsn.initNotificationMes('删除失败',true);
					}
				}
			});
		}
		return false;
	});
	$("#search-btn").click(function(){
		$("#product").data("kendoGrid").dataSource.read();
	});
	product.initialize();
});