$(function() {
	var upload = window.fsn.upload = window.fsn.upload || {};
	var product = upload.product = upload.product || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	//判断cookie不为空时设置为空
	try {
		var edit_id = $.cookie("user_0_edit_product").id;
		if(edit_id!=null){
			$.cookie("user_0_edit_product", JSON.stringify({}), {
			path : '/fsn-core/views/portal/'
		});
		}
    } catch (e) {
    }
	portal.businessType="";
	product.initialize = function(type){
		portal.businessType=type;
			$("#delMemberWindow").kendoWindow({
				width:360,
				height:150,
				visible: false,
				title:"删除人员",
			});
		var dataSource =portal.listViewDS();
		$("#pager").kendoPager({
			dataSource: dataSource,
			messages:fsn.gridPageMessage()
		});

		$("#my_product_grid").kendoListView({
			dataSource: dataSource,
			template: kendo.template($("#template").html()) ,
		});
		$("#my_product_grid").removeClass("k-widget");
		product.initComponent();
	};
	
	/**
	 *初始化数据
	 */
	portal.loadDataSource = function() {
		var dataSource=portal.listViewDS();
		$("#my_product_grid").data("kendoListView").setDataSource(dataSource);
		$("#pager").data("kendoPager").destroy();

		 $("#pager").kendoPager({
			dataSource: dataSource ,
			 messages:fsn.gridPageMessage()
		});
		$("#my_product_grid").removeClass("k-widget");

	};
	portal.listViewDS=function(){
		var mytemplateDS = new kendo.data.DataSource({
			transport: {
				read : {
					type : "GET",
					async:false,
					url : function(options){
						var configure = null;
						if(options.filter){
							configure = filter.configure(options.filter);
						}
						return portal.HTTP_PREFIX + "/member/getMembers/" + configure + "/" + options.page + "/" + options.pageSize + "/key=" + $("#key").val().trim();
					},
					dataType : "json",
					contentType : "application/json"
				},
			},
			schema: {
				data : function(returnValue) {
					return returnValue.data.listOfMember;  //响应到页面的数据
				},
				total : function(returnValue) {
					return returnValue.data.counts;   //总条数
				}
			},
			batch : true,
			page:1,
			pageSize : 6, //每页显示个数
			serverPaging : true,
			serverFiltering : true,
			serverSorting : true
		});
		return mytemplateDS;
	} ;

	
	product.initComponent = function(){
		
		portal.delMember = function(proId){
			$("#tempMemberId").val(proId);
			$("#delMemberWindow").data("kendoWindow").open().center();
		};
		
		/* 确定删除产品 */
		$("#delMember_yes_btn").click(function(){
			var id = $("#tempMemberId").val().trim();
			if(!portal.isBlank(id)){
				portal.deleteMember(id);//删除产品
			}
			$("#delMemberWindow").data("kendoWindow").close();
		});
		$("#tempMemberId").val("");
		$("#delMember_no_btn").click(function(){
			$("#delMemberWindow").data("kendoWindow").close();
		});
		
	};
	
	/**
 * 编辑
 * @author tangxin
 */
portal.edit = function(id){
	/* md5加密pid */ 
	var canshu = "?"+id+"&"+$.md5(""+id);
	if(portal.businessType=="sc"){
		window.location.href = "/fsn-core/views/member/member_sc.html"+canshu;
	}   else{
		window.location.href = "/fsn-core/views/member/member.html"+canshu;
	}
};
/**
 * 删除产品
 * @author ZhangHui 2015/4/14
 */
portal.deleteMember = function(proId){
	 $.ajax({
		   url: portal.HTTP_PREFIX + "/member/delete/" + proId,
		   type: "DELETE",
		   dataType: "json",
		   async:false,
		   contentType: "application/json; charset=utf-8",
		   success: function(returnValue) {
			  	 if(returnValue.result.status == "true"){
			  		/*fsn.fleshGirdPageFun(portal.mytemplateDS);*/
			  		product.initialize();
					fsn.initNotificationMes( "删除成功",true);
			  	 }else{
				 	fsn.initNotificationMes( "删除失败",false);
				 }
				 
		   }
	 });
};
 	

});