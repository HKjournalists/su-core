 $(document).ready(function() {
	 var fsn = window.fsn = window.fsn || {};
	 var enRegiste = window.fsn.enRegiste = window.fsn.enRegiste || {};
	 var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	 portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	 enRegiste.edit_id = null;
	 enRegiste.initialize=function(){
		 enRegiste.initView();
		 $("#back").bind("click", enRegiste.back);
	 };
	 
	 //返回按钮
	 enRegiste.back = function(){
		window.location.href = "admin_enRegiste-list.html"; 
	 };
	 
	 enRegiste.initView=function(){
		 var params = fsn.getUrlVars();
		 enRegiste.edit_id=params[params[0]];
		 if(enRegiste.edit_id){
			 isNew=false;
			 $.ajax({
					url:portal.HTTP_PREFIX + "/account/loadbus/" + enRegiste.edit_id,
					type:"GET",
					dataType:"json",
					success:function(returnValue){
						if(returnValue.data!=null){
							var bus = returnValue.data;
							$("#bName").val(bus.name);
							$("#bLicNo").val(bus.licNo);
							$("#personInCharge").val(bus.personInCharge);
							$("#orgNo").val(bus.orgCode);
							$("#bType").val(bus.busType);
							$("#bAddr").val(bus.regAddr);
							$("#regDate").val(bus.regDate);
							$("#bMail").val(bus.email);
							$("#linkMan").val(bus.linkMan);
							$("#linkTel").val(bus.linkTel);
							var orgImage = '<label>暂未上传相关证照</label>';
							if(bus.orgImage!=null && bus.orgImage!=""){
								orgImage = '<a href="'+bus.orgImage+'" title="组织机构证" target="_blank" ><img src="'+ bus.orgImage +'" width="340px" /></a>';
							}
							$("#orgImage").html(orgImage);
							var licImage = '<label>暂未上传相关证照</label>';
							if(bus.licImage!=null && bus.licImage != ""){
								licImage ='<a href="' + bus.licImage + '" title="营业执照证" target="_blank" ><img src="'+ bus.licImage +'" width="340px" /></a>';
							}
							$("#licImage").html(licImage);
						}
					}
			});
		 }
	 };
	enRegiste.initialize();
	
 });