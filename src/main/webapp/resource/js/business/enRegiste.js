 $(document).ready(function() {
	 var fsn = window.fsn = window.fsn || {};
	 var enRegiste = window.fsn.enRegiste = window.fsn.enRegiste || {};
	 var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	 portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	 portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	 enRegiste.edit_id = null;
	 enRegiste.initialize=function(){
		 enRegiste.initKendoWindow("k_window","保存状态","300px","60px",false);
		 enRegiste.initView();	 
		 $("#spprove").bind("click",enRegiste.spprove);
		 $("#nonPlacet").bind("click",fsn.openReturnWin);
		 $("#updateBtn").bind("click", enRegiste.updateSignFlag);
	 };
	 enRegiste.initView=function(){
		 var params = fsn.getUrlVars();
		 enRegiste.edit_id=params[params[0]];
		 if(enRegiste.edit_id){
			 isNew=false;
			 $.ajax({
					url:portal.HTTP_PREFIX + "/business/getEnRegisteById/" + enRegiste.edit_id,
					type:"GET",
					dataType:"json",
					success:function(returnValue){
						$("#save").hide();
						$("#userName").val(returnValue.enRegiste.userName);	
						$("#email").val(returnValue.enRegiste.email);
						$("#organizationNo").val(returnValue.enRegiste.organizationNo);
						$("#licenseNo").val(returnValue.enRegiste.licenseNo);
						$("#productNo").val(returnValue.enRegiste.productNo);
						$("#passNo").val(returnValue.enRegiste.passNo);
						$("#serviceNo").val(returnValue.enRegiste.serviceNo);
						$("#enterpriteName").val(returnValue.enRegiste.enterpriteName);	
						$("#enterptiteAddress").val(returnValue.enRegiste.enterptiteAddress);
						$("#legalPerson").val(returnValue.enRegiste.legalPerson);
						$("#telephone").val(returnValue.enRegiste.telephone);
						$("#enterptiteType").val(returnValue.enRegiste.enterpriteType);
						enRegiste.setAttachments(returnValue.enRegiste.orgAttachments,"orgAttachmentsListView");
						enRegiste.setAttachments(returnValue.enRegiste.licAttachments,"licAttachmentsListView");
						enRegiste.setAttachments(returnValue.enRegiste.qsAttachments,"proAttachmentsListView");
						enRegiste.setAttachments(returnValue.enRegiste.disAttachments,"passAttachmentsListView");
						enRegiste.setAttachments(returnValue.enRegiste.liquorAttachments,"serAttachmentsListView");
						document.getElementById("signFlagN").checked = true;
						$("#updateBtn").hide();
						if(returnValue.enRegiste.status=="审核通过"){
							$("#spprove").hide();
							$("#nonPlacet").hide();
							$("#updateBtn").show();
							if(returnValue.enRegiste.signFlag){
								document.getElementById("signFlagY").checked = true;
							}
						}else if(returnValue.enRegiste.enterpriteType.indexOf("生产企业")>-1){
								document.getElementById("signFlagY").checked = true;
						}
					}
			});
		 }
	 };
	 
	 enRegiste.spprove=function(){
		 var signFlag = document.getElementById("signFlagY").checked;
		 $("#winMsg").html("正在处理，请稍候....");
	     $("#k_window").data("kendoWindow").open().center();
		 $.ajax({
				url:portal.HTTP_PREFIX + "/business/spprove/"+enRegiste.edit_id + "/" + signFlag,
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				success:function(data){						
					 	if(data.result.success){
					 		$("#k_window").data("kendoWindow").close();
					 		 lims.initNotificationMes("企业"+$("#enterpriteName").val().trim()+"审核通过", true);
					 		 $("#spprove").hide("slow");
					 		$("#nonPlacet").hide("slow");
					 		setTimeout('returnView()',1500);
					 	}else{
					 		$("#k_window").data("kendoWindow").close();
					 		if(data.result.errorMessage=="用户名已存在，审核失败！"){
					 			lims.initNotificationMes("用户名已存在，审核失败！", false);
					 		}else{
					 			lims.initNotificationMes("企业"+$("#enterpriteName").val().trim()+"审核通过失败", false);
					 		}
					 	}
				}
		});
	 };
	
	 /*修改企业签名状态*/
	 enRegiste.updateSignFlag = function(){
		 var signFlag = document.getElementById("signFlagY").checked;
		 var passFlag = document.getElementById("passFlagY").checked;
		 var busName = $("#enterpriteName").val();
		 $("#winMsg").html("正在处理，请稍候....");
	     $("#k_window").data("kendoWindow").open().center();
		 $.ajax({
				url:portal.HTTP_PREFIX + "/business/updateSignStatus/"+busName + "/" + signFlag+"/"+passFlag,
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				success:function(data){						
					 	if(data.result.success){
					 		$("#k_window").data("kendoWindow").close();
					 		lims.initNotificationMes("企业签名状态和报告通过状态修改成功!", true);
					 	}else{
					 		$("#k_window").data("kendoWindow").close();
					 		lims.initNotificationMes("修改企业签名和报告通过时出现后台异常,修改失败！", false);
					 	}
				}
		});
	 };
	 
	 enRegiste.setAttachments=function(proAttachments,id){
			var dataSource=new kendo.data.DataSource();
			if(proAttachments.length>0){				 
				 for(var i=0;i<proAttachments.length;i++){					
					 dataSource.add({attachments:proAttachments[i]});
				 }
			 }
			 $("#"+id).kendoListView({
	             dataSource: dataSource,
	             template:kendo.template($("#uploadedFilesTemplate").html()),
	         });
		 };
	enRegiste.returnView=function(){
		$("#spprove").hide("slow");
		//window.location.href=fsn.CONTEXT_PATH + "/views/lab/test-result-manage.html";
	};
	fsn.initReturnWindow(function() {
		//var from=returnData.pdfReport.split("/")[2];
		
		var returnMes=$("#RETURN_MES1").val().trim();
		enRegiste.NoPassReturn(returnMes);
		$("#RETURN_MES1").val("");
		fsn.closeReturnWin();
	}, undefined,"退回原因",true);
	
	enRegiste.NoPassReturn=function(returnMes){
		$("#winMsg").html("正在处理，请稍候....");		 
        $("#k_window").data("kendoWindow").open().center();
		$.ajax({
			url:portal.HTTP_PREFIX + "/business/noPassReturn/" + enRegiste.edit_id,
			type:"GET",
			dataType: "json",
            contentType: "application/json; charset=utf-8",
			data:{"returnMes":encodeURIComponent(returnMes)},
			success:function(data){
				if(data.result.success){
					$("#k_window").data("kendoWindow").close();
			 		 lims.initNotificationMes("企业"+$("#enterpriteName").val().trim()+"退回成功，且邮件已发送", true);
					//fsn.msg("msg_success",data.result,fsn.l("企业"+$("#enterpriteName").val().trim()+"退回成功，且邮件已发送"));
					$("#spprove").hide("slow");
					$("#nonPlacet").hide("slow");
					setTimeout('returnView()',1500);
				}else{
					$("#k_window").data("kendoWindow").close();
			 		 lims.initNotificationMes("企业"+$("#enterpriteName").val().trim()+"退回失败", false);
					//fsn.msg("msg_success",data.result,fsn.l("企业"+$("#enterpriteName").val().trim()+"退回失败"));
				}
			}
		  });
	};
	 returnView=function(){
		 window.location.href="/fsn-core/views/business/enRegiste-list.html";
	 };
	 enRegiste.initKendoWindow=function(winId,title,width,height,isVisible){
	    	$("#"+winId).kendoWindow({
				title:title,
				width:width,
				height:height,
				modal:true,
				visible:isVisible,
				actions:["Close"]
			});
	    };
	enRegiste.initialize();
	
 });