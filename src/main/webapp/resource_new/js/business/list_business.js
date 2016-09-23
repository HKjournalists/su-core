	$(function() {
		var upload = window.lims.upload = window.lims.upload || {};
		var business = upload.business = upload.business || {};
		var fsn = window.fsn = window.fsn || {}; // 全局命名空间
		var portal = fsn.portal = fsn.portal || {}; // portal命名空间
		portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
		portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
		business.isNew = null;
		business.id = null;
		
		business.initialize = function(){
			business.buildGrid("business",business.templatecolumns, business.templateDS);
			business.initKendoWindow("ADD_WIN","新增商户","500px","400px",false);
			$("#add").bind("click", business.add);
			$("#confirmWindow").bind("click", business.save);
			$("#cancelWindow").bind("click", business.cancel);
			
			$("#license").bind("blur",business.validateLicense);
			$("#telephone").bind("blur",business.validateTetelephone);
		    $("#email").bind("blur",business.validateEmail);
		};
		
		business.templatecolumns = [
					 {field: "name",title: "商户名称",
					 	template: function(dataItem){
					 		if(dataItem.business!=null&&dataItem.business.name!=null){
								return dataItem.business.name;
							}else{
								return dataItem.name;
							}
					 }},
                     {field: "license",title: "营业执照号",
						 template: function(dataItem){
					 		if(dataItem.business!=null&&dataItem.business.license.licenseNo!=null){
								return dataItem.business.license.licenseNo;
							}else{
								return dataItem.license;
							}
					 }},
                     {field: "personInCharge",title: "负责人",
					 	 template: function(dataItem){
					 		if(dataItem.business!=null&&dataItem.business.personInCharge!=null){
								return dataItem.business.personInCharge;
							}else{
								return dataItem.personInCharge;
							}
					 }},
                     {field: "telephone",title: "联系电话",
						 template: function(dataItem){
					 		if(dataItem.business!=null&&dataItem.business.personInCharge!=null){
								return dataItem.business.telephone;
							}else{
								return dataItem.telephone;
							}
					 }},
                     {field: "email",title: "邮箱",
						 template: function(dataItem){
					 		if(dataItem.business!=null&&dataItem.business.personInCharge!=null){
								return dataItem.business.email;
							}else{
								return dataItem.email;
							}
					 }},
					 {
					 	title: "操作",
						width:"20%",
					 	template: function(dataItem){
							if(dataItem.business==null){
								var  sendEmail = "<div align='center'><button  class='k-button' onclick=edit("+dataItem.id+")><span class='k-icon k-update'></span>编辑</button>";
								var edit = "<button  class='k-button' onclick=sendEmail("+dataItem.id+")><span class='k-icon k-update'></span>发送邮件</button></div>";
								return sendEmail+"&nbsp;&nbsp;"+edit;
							}else{
								return "<div align='center'><a href='/fsn-core/ui/portal/unit-liutong?id="+dataItem.business.id+"' target='_blank'><button  class='k-button'><span class='k-icon k-search'></span>查看详细</button></a></div>";
							}
					 	}
					 },];
		
		business.templateDS =  new kendo.data.DataSource({
			transport: {
	            read : {
	                type : "GET",
	                async:false,
	                url : function(options){
	                	var configure = null;
	                	if(options.filter){
	            			configure = filter.configure(options.filter);
	            		}
	                	return portal.HTTP_PREFIX + "/business/market/getBusiness/" + configure + "/" + options.page + "/" + options.pageSize;
	                },
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	 data : function(returnValue) {
	        		 return returnValue.data;  //响应到页面的数据
	             },
	             total : function(returnValue) {
	            	 return returnValue.counts;   //总条数
	             }         
	        },
	        batch : true,
	        page:1,
	        pageSize : 10, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
		
		business.buildGrid = function(id,columns,ds){
			$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				extra: false,
				messages: lims.gridfilterMessage(),
				operators: {
					string: lims.gridfilterOperators(),
				    date: lims.gridfilterOperatorsDate(),
				    number: lims.gridfilterOperatorsNumber()
				}
			},
			height:470,
	        width: "100%",
	        sortable: true,
	        resizable: true,
	        toolbar: [
	        		  {template: kendo.template($("#toolbar_template").html())}
	                  ],
	        selectable: true,
	        pageable: {
	            refresh: true,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
		};  

		//保存新增商户
		business.save = function(){
			if (!business.validateFormat()) {
				return;
			};
			var model = business.setModel();
			if (business.isNew) {
				//校验当前商户是否已经属于该市场
				$.ajax({
					url: fsn.getHttpPrefix() + "/business/market/verifyBusiness",
					type: "POST",
					dataType: "json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(model),
					success: function(dataValue){
						if (dataValue.result.status == "true") {
							if (dataValue.count > 0) {
								fsn.initNotificationMes("该营业执照号已经入驻到当前市场！", false);
							}
							else {
								//新增商户到该市场
								$.ajax({
									url: fsn.getHttpPrefix() + "/business/market/addBusiness",
									type: "POST",
									dataType: "json",
									contentType: "application/json; charset=utf-8",
									data: JSON.stringify(model),
									success: function(dataValue){
										if (dataValue.result.status == "true") {
											fsn.initNotificationMes("新增成功！", true);
											$("#ADD_WIN").data("kendoWindow").close();
											$("#business").data("kendoGrid").dataSource.read();
										}
										else {
											fsn.initNotificationMes("新增失败！", false);
										}
									}
								});
							}
						}
					}
				});
			}else{
				model.id = business.id;
				$.ajax({
					url: fsn.getHttpPrefix() + "/business/market/editBusiness",
					type: "POST",
					dataType: "json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(model),
					success: function(dataValue){
						if (dataValue.result.status == "true") {
							fsn.initNotificationMes("编辑成功！", true);
							$("#ADD_WIN").data("kendoWindow").close();
							$("#business").data("kendoGrid").dataSource.read();
						}
						else {
							fsn.initNotificationMes("编辑成功失败！", false);
						}
						business.id=null;
						business.isNew=null;
					}
				});
			}
		};
		
		business.cancel = function(){
			$("#ADD_WIN").data("kendoWindow").close();
		};
		
		//发送Email
		sendEmail = function(id){
			$.ajax({
				url: fsn.getHttpPrefix() + "/business/getCountEmail/"+id,
				type:"get",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				success:function(dataValue){					
					if(dataValue.result.status=="true"){
						//判断当天是否已经发过5次
						if(dataValue.count>=5){
							fsn.initNotificationMes("该商户今天已经发送了5次，不能再发送！",false);
							return;
						}else{
							var model={
								id:id,
							}
						 	$.ajax({
								url: fsn.getHttpPrefix() + "/business/sendEmailToBusiness",
								type:"POST",
								dataType:"json",
								contentType: "application/json; charset=utf-8",
								data: JSON.stringify(model),
								success:function(dataValue){
									if(dataValue.result.status=="true"){
										fsn.initNotificationMes("每个商户每天可发送邮件5次！</br>该商户今天还可以发送"+dataValue.count+"次！",true);
										
									}else{
										fsn.initNotificationMes("邮件发送失败！",false);
									}
								}
							})
						}
					}
				}
			})
		}
		
		business.add = function(){
			business.clear();
			business.isNew = true;
			$("#ADD_WIN").data("kendoWindow").open().center();
		};
		
		edit = function(id){
			var dataItem = $("#business").data("kendoGrid").dataItem($("#business").data("kendoGrid").select());
			business.setValue(dataItem);
			business.isNew = false;
			business.id = dataItem.id;
			$("#ADD_WIN").data("kendoWindow").open().center();
		}
		
		//查看详细信息
		review = function(id){
			window.location.pathname = "/fsn-core/ui/portal/unit-liutong?id="+id;
		}
		
		business.getCountEmail = function(id){
			$.ajax({
				url: fsn.getHttpPrefix() + "/business/getCountEmail/"+id,
				type:"get",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				success:function(dataValue){
					if(dataValue.result.status=="true"){
						return dataValue.count;
					}
				}
			})
		}
		
		business.clear = function(){	
			business.validateInputStyle("license","正确！",true);
			business.validateInputStyle("telephone","正确！",true);
			business.validateInputStyle("email","正确！",true);
			$("#businessName").val("");
			$("#license").val("");
			$("#personInCharge").val("");
			$("#telephone").val("");
			$("#email").val("");
			$("#license_label").css("color","#BEBFC3");
		}
		
		business.setValue = function(dataItem){
			business.clear();
			$("#businessName").val(dataItem.name);
			$("#license").val(dataItem.license);
			$("#personInCharge").val(dataItem.personInCharge);
			$("#telephone").val(dataItem.telephone);
			$("#email").val(dataItem.email);	
		}
		
		business.setModel = function(){
			var name = $("#businessName").val();
			var license = $("#license").val();
			var personInCharge = $("#personInCharge").val();
			var telephone = $("#telephone").val();
			var email = $("#email").val();
			var model = {
				name:name,
				license:license,
				personInCharge:personInCharge,
				telephone:telephone,
				email:email,
			}
			return model;
		}
		
		/*验证营业执照号格式*/
    business.validateLicense=function(){
    	 var license = $("#license").val().trim();
    	 var reg =/^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{13}$|^[A-Za-z0-9]{18}$/;
    	  if(!reg.test(license)){
    		  business.validateInputStyle("license","营业执照格式错误！",false);
			  $("#license_label").css("color","red");
    		  return false;
    	}else{
			 $("#license_label").css("color","#BEBFC3");
    		 business.validateInputStyle("license","正确！",true);
    	}
    	return true;
    };
		
		/*验证手机格式*/
		 business.validateTetelephone = function(){
        var isPhone=/^(?:\+86)?(?:13\d|15\d|18\d)\d{8}$/;  // 手机
        if(($("#telephone").val().trim())!=""){
             if(!(($("#telephone").val().trim()).match(isPhone))){
            	 business.validateInputStyle("telephone","手机号码不正确！",false);
            	 return false;
             }else{
            	 business.validateInputStyle("telephone","",true);
             }
        }
        return true;
    };
	
	/*验证邮箱格式*/
    business.validateEmail=function(){
    	 var email = $("#email").val().trim();
    	 var reg =/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    	  if(!reg.test(email)){
    		  business.validateInputStyle("email","格式不正确！",false);
    		  return false;
    	}else{
    		 business.validateInputStyle("email","正确！",true);
    	}
    	return true;
    };
	
	business.validateFormat = function(){
		if($("#businessName").val().trim()==""){
			fsn.initNotificationMes("请填写商户名称！",false);
			return;
		}
		if($("#license").val().trim()==""){
			fsn.initNotificationMes("请填写营业执照号！",false);
			return;
		}
		if($("#personInCharge").val().trim()==""){
			fsn.initNotificationMes("请填写负责人！",false);
			return;
		}	
		if($("#telephone").val().trim()==""){
			fsn.initNotificationMes("请填写联系电话！",false);
			return;
		}
		if($("#email").val().trim()==""){
			fsn.initNotificationMes("请填写邮箱！",false);
			return;
		}
		if(!business.validateLicense()){
				fsn.initNotificationMes("营业执照号无效！",false);
				return;
		}
		if(!business.validateTetelephone()){
				fsn.initNotificationMes("联系电话无效！",false);
				return;
		}
		if(!business.validateEmail()){
				fsn.initNotificationMes("邮箱格式不正确！",false);
				return;
		}
		return true;
	}
	
	    /*验证某个字段时，添加或取消输入框的样式
     * formId:输入框的id，msg：提示信息，valiResult：验证结果，true或false 
     */
    business.validateInputStyle=function(formId,msg,valiResult){
    	if(!valiResult){
    		var parentDiv=$("#"+formId).parent();
    		parentDiv.find("span").remove();
  		  	var span=$("<span class='fieldFormatStyle'>"+msg+"</span>");
  		  	parentDiv.append(span);
    	}else{
    		var span=$("#"+formId).next();
    		if(span.length>0){span.remove();}
    	}
    };
		
		business.initKendoWindow=function(winId,title,width,height,isVisible){
	    	$("#"+winId).kendoWindow({
				title:title,
				width:width,
				height:height,
				modal:true,
				visible:isVisible,
			});
    	};
	
	business.initialize();
	})	