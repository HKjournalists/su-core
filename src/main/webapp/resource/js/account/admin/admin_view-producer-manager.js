$(document).ready(function(){
	var fsn = window.fsn = window.fsn || {};
	var root = window.fsn.root = window.fsn.root || {};
	fsn.HTTP_PREFIX = fsn.getHttpPrefix();
	root.aryOrgAttachments = new Array();
	root.aryLiceNoAttachments = new Array();
	root.delAryOrgAttachments = new Array();
	root.delAryLiceAttachments = new Array();
	
	root.checkLis = null;//营业执照信息是否通过标识
	root.checkOrg = null;//组织机构信息是否通过标识
	root.checkQs = false;//生产许可证信息是否通过标识
	root.listQsInfo = null;//保存Qs列表信息
	root.Qs = null;//保存Qs号
	
	/*初始化方法*/
	fsn.initialize = function(){
		try { 
			new fsn.TestResult().initView();
		} catch(e){
			
		}
		root.initGrid("qs_grid", root.qsColumn, []);
		root.initWindow("busDetailWin","生产企业详细信息","800px","700px");
		root.initWindow("viewQsWindow","生产许可证图片","500px","350px");
		root.initWindow("tipsWindow","提示","500px","");
		if(!fsn.isStruct){
			//$("#producer_View").show();//css("display", "block");
			$("#producer_View").bind("click",root.openProducerWindow);
			$("#btn_saveQs").bind("click",root.saveQS);
		}
	};
	
	/*生产企业信息Qs Grid列*/
	root.qsColumn = [
	                 {field:"qsInstance.qsNo",title:fsn.l("Production license No"),width: 100},
	                 {field:"passFlag",title:fsn.l("Current Status"),width: 100},
	                 { command: [{
	               	  name:"View",
	                      text: fsn.l("View picture"),
						  click: function(e){
							  e.preventDefault();
							  var editRow = $(e.target).closest("tr");
							  root.currentQsModel = this.dataItem(editRow);
							  var qsInstance = root.currentQsModel.qsInstance;
							  root.Qs = qsInstance.qsNo;
							  $("input[name='winQsNo']").val(qsInstance.qsNo);
								  if(root.currentQsModel.passFlag=="审核通过"){
									  document.getElementById("qsNoY").checked=true;
									  $(".qsNoTrMsg").fadeOut("slow");
								  }else{
									  $(".qsNoTrMsg").fadeIn("slow");
									  document.getElementById("qsNoN").checked=true;
									  $("#qsNoMsg").val(root.currentQsModel.msg);
								  }
							  root.currentQsAttachments=qsInstance.qsAttachments;
							  root.setAttachments(qsInstance.qsAttachments,"qsAttachmentsListView","delQsFilesTemplate");
	            		  	  $("#viewQsWindow").data("kendoWindow").open().center();
						  }
					  },
					  ], title:fsn.l("Operation"), width: 40 }
	               ];
	
	root.openTipsWindow = function(winId,msg){
		$("#tipsMsg").html(msg);
		$("#"+winId).data("kendoWindow").open().center();
	};
	
	root.closeWindow = function(winIds){
		if(root.checkLis){
			document.getElementById("liceY").checked = true;
		}else if(!root.checkLis){
			document.getElementById("liceN").checked = true;
		}
		if(root.checkOrg){
			document.getElementById("orgCodeY").checked = true;
		}else if(!root.checkOrg){
			document.getElementById("orgCodeN").checked = true;
		}
		
		if(root.listQsInfo!=null){
			root.checkQs = true;
			var qsInfo = root.listQsInfo;
			for(var i=0;i<qsInfo.length;i++){
				if(qsInfo[i].passFlag!="审核通过"){
					root.checkQs = false;
					break;
				}
			}
		}else{
			root.checkQs = false;
		}
		
		if(winIds==null&&winIds.length<1) return;
		for(var i=0;i<winIds.length;i++){
			$("#"+winIds[i]).data("kendoWindow").close();
		}
	};
	
	$("input[type='radio']").click(function(){
		var liceTr = $(this).attr("name");
		var strId = $(this).attr("id");
		var status = strId.substr(strId.length-1);
		if(status.indexOf("Y")==-1){
			if(strId.indexOf("liceN")!=-1){
				root.checkLis = false;
			}else if(strId.indexOf("orgCodeN")!=-1){
				root.checkOrg = false;
			}
			$("."+liceTr).fadeIn("slow");
		}else{
			if(strId.indexOf("liceY")!=-1){
				root.checkLis = true;
			}else if(strId.indexOf("orgCodeY")!=-1){
				root.checkOrg = true;
			}
			$("."+liceTr).fadeOut("slow");
		}
		if(strId.indexOf("qsNoY")!=-1){
			if(root.listQsInfo!=null){
				var qsInfo = root.listQsInfo;
				for(var i=0;i<qsInfo.length;i++){
					if(qsInfo[i].qsInstance.qsNo==root.Qs){
						qsInfo[i].passFlag="审核通过";
					}
				}
			}
		}else if(strId.indexOf("qsNoN")!=-1){
			if(root.listQsInfo!=null){
				var qsInfo = root.listQsInfo;
				for(var i=0;i<qsInfo.length;i++){
					if(qsInfo[i].qsInstance.qsNo==root.Qs){
						qsInfo[i].passFlag="审核退回";
					}
				}
			}
		}
	});
	
	root.setAttachments = function(attachments,listViewId,templateId){
		if(attachments==null||attachments.length<1){
			$("#"+listViewId).html("无图片");
			return;
		}
		var dataSource = new kendo.data.DataSource();
		if(attachments!=null&&attachments.length>0){
			for(var i=0;i<attachments.length;i++){
				dataSource.add({attachments:attachments[i]});
			}
		}
		$("#"+listViewId).kendoListView({
			dataSource: dataSource,
			template:kendo.template($("#"+templateId).html()),
		});
	};
	
	/*删除指定的图片*/
	root.removeRes=function(attachments,resId,listViewId){
		var dataSource = new kendo.data.DataSource();
		if(attachments){
			if(resId!=null){
				for(var i=0; i<attachments.length; i++){
				     if(attachments[i].id == resId){
				        while((i+1)<attachments.length){
				        	attachments[i] = attachments[i+1];
				        	i++;
				        }
				        attachments.pop();
				        break;
				      }
				 }
			}
			if(attachments.length>0){
				for(i=0; i<attachments.length; i++){
					dataSource.add({attachments:attachments[i]});
				}
			}
			$("#"+listViewId).data("kendoListView").setDataSource(dataSource);
		}
	};
	
	/*删除QS资源*/
	root.removeQsRes = function(resId){
		root.removeRes(root.currentQsAttachments,resId,"qsAttachmentsListView");
	};
	
	/*删除组织机构资源*/
	root.removeOrgRes = function(resId){
		root.delAryOrgAttachments.push({id:resId});
		root.removeRes(root.aryOrgAttachments,resId,"orgAttachmentsListView");
	};
	
	/*删除营业执照资源*/
	root.removeLicRes = function(resId){
		root.delAryLiceAttachments.push({id:resId});
		root.removeRes(root.aryLiceNoAttachments,resId,"liceNoAttachmentsListView");
	};
	
	/*设置页面Radio 的选择状态，公用*/
	root.setStauts=function(passFlag,radYId,radNId,trId,msgId,msg){
		if(trId=="liceTrMsg"){
			if(root.checkLis==null){
				if(passFlag=="审核通过"){
					document.getElementById(radYId).checked=true;
					root.checkLis = true;
					$("."+trId).fadeOut("slow");
					$("#"+msgId).val("");
				}else{
					document.getElementById(radNId).checked=true;
					root.checkLis = false;
					$("#"+msgId).val(msg);
				}
			}else{
				if(root.checkLis){
					document.getElementById(radYId).checked=true;
					$("."+trId).fadeOut("slow");
					$("#"+msgId).val("");
				}else if(!root.checkLis){
					document.getElementById(radNId).checked=true;
					$("#"+msgId).val(msg);
				}
			}
		}
		if(trId=="orgCodeTrMsg"){
			if(root.checkOrg==null){
				if(passFlag=="审核通过"){
					document.getElementById(radYId).checked=true;
					root.checkOrg = true;
					$("."+trId).fadeOut("slow");
					$("#"+msgId).val("");
				}else{
					root.checkOrg = false;
					document.getElementById(radNId).checked=true;
					$("#"+msgId).val(msg);
				}
			}else{
				if(root.checkOrg){
					document.getElementById(radYId).checked=true;
					$("."+trId).fadeOut("slow");
					$("#"+msgId).val("");
				}else if(!root.checkOrg){
					document.getElementById(radNId).checked=true;
					$("#"+msgId).val(msg);
				}
			}
		}
	};
	
	/*隐藏或显示指定的标签*/
	root.hideOrShow=function(formIds,style){
		if(formIds==null||formIds.length<1) return;
		for(var i=0;i<formIds.length;i++){
			$("."+formIds[i]).css("display",style);
		}
	};
	
	root.setProducerVal = function(listFileValue,listQs){
		$("#orgCode").val("");
		$("#licenseNo").val("");
		root.hideOrShow(["liceTrStatus","liceTrMsg","orgCodeTrStatus","orgCodeTrMsg"],"none");
		if(listFileValue == null && listQs == null) return;
		root.orgCodeId = null;
		root.LiceNoId = null;
		for(var i=0;i<listFileValue.length;i++){
			if(listFileValue[i].display=="组织机构代码"){
				root.aryOrgAttachments=listFileValue[i].attachments;
				root.producerId = listFileValue[i].producerId,
				root.orgCodeId = listFileValue[i].id;
				$("#orgCode").val(listFileValue[i].value);
				root.hideOrShow(["orgCodeTrStatus","orgCodeTrMsg"],"inline");
				root.setStauts(listFileValue[i].passFlag,"orgCodeY","orgCodeN","orgCodeTrMsg","orgCodeMsg",listFileValue[i].msg);
			}else{
				root.producerId = listFileValue[i].producerId,
				root.LiceNoId = listFileValue[i].id;
				root.aryLiceNoAttachments = listFileValue[i].attachments;
				$("#licenseNo").val(listFileValue[i].value);
				root.hideOrShow(["liceTrStatus","liceTrMsg"],"inline");
				root.setStauts(listFileValue[i].passFlag,"liceY","liceN","liceTrMsg","liceMsg",listFileValue[i].msg);
			}
		}
		root.setAttachments(root.aryOrgAttachments,"orgAttachmentsListView","delOrgFilesTemplate");
		root.setAttachments(root.aryLiceNoAttachments,"liceNoAttachmentsListView","delLiceNoFilesTemplate");
		if(root.listQsInfo==null){
			root.listQsInfo = listQs;
		}
		$("#qs_grid").data("kendoGrid").setDataSource(new kendo.data.DataSource({data:root.listQsInfo,page:1,pageSize:10}));
	};
	
	root.getProducer = function(){
		$("#businessName").val("");
		if(fsn.testReport==null) return;
		var busUnit = fsn.testReport.sample.producer;
		var org = fsn.testReport.organization;
		$.ajax({
			url:fsn.HTTP_PREFIX + "/liutong/getProducerById/"+busUnit.id +"?organization="+org,
			type:"GET",
			dataType:"json",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					$("#businessName").val(busUnit.name);
					root.setProducerVal(returnValue.listFieldValue,returnValue.listQs);
					$("#busDetailWin").data("kendoWindow").open().center();
				}
			},
		});
	};
	
	root.openProducerWindow = function(){
		fsn.liutong = true;
		root.delAryOrgAttachments.length = 0;
		root.delAryLiceAttachments.length = 0;
		root.getProducer();
	};
	
	root.saveQS=function(){
		$("#viewQsWindow").data("kendoWindow").close();
		if(root.currentQsModel==null) return;
		var qspassFlag = "审核通过";
		var returnMsg = "";
		if(document.getElementById("qsNoN").checked){
			qspassFlag = "审核退回";
			returnMsg = $("#qsNoMsg").val().trim();
		}
		root.currentQsModel.passFlag = qspassFlag;
		root.currentQsModel.msg = returnMsg;
		$("#qs_grid").data("kendoGrid").refresh();
	};
	
	/*封装保存的数据*/
	fsn.createInstance = function(){
		if(root.orgCodeId==null&&root.LiceNoId==null) return null;
		var liutongProducer={
				organization:fsn.testReport.organization,
				producerId:root.producerId,
				producerName:$("#businessName").val().trim(),
				msg:"审核通过",
			};
		var fieldValue = new Array();
		var orgPassflag = "审核通过";
		var returnMsg = "";
		if(document.getElementById("orgCodeN").checked){
			orgPassflag="审核退回";
			producerPass = false;
			returnMsg=$("#orgCodeMsg").val().trim();
		} 
		var orgInstance = {
			id:root.orgCodeId,
			display:"组织机构代码",
			passFlag:orgPassflag,
			msg:returnMsg,
			attachments:root.delAryOrgAttachments,//被删除的资源ID
		};
		fieldValue.push(orgInstance);
		var licPassflag = "审核通过";
		returnMsg = "";
		if(document.getElementById("liceN").checked){
			licPassflag="审核退回";
			producerPass = false;
			returnMsg = $("#liceMsg").val().trim();
		} 
		var licInstance = {
				id:root.LiceNoId,
				display:"营业执照号",
				passFlag:licPassflag,
				msg:returnMsg,
				attachments:root.delAryLiceAttachments,//被删除的资源ID
			};
		fieldValue.push(licInstance);
		
		var qsItems = $("#qs_grid").data("kendoGrid").dataSource.data();
		var listQs = new Array();
		var qsIsPass = true;
		if(qsItems!=null && qsItems.length>0){
			for(var i=0;i<qsItems.length;i++){
				var qsIs = {
					id:qsItems[i].id,
					producerId:root.producerId,
					fullFlag:(qsItems[i].qsInstance.qsAttachments.length>0),
					passFlag:qsItems[i].passFlag,
					msg:qsItems[i].msg,
					qsInstance:qsItems[i].qsInstance, //保留的QS集合
				};
				if(qsItems[i].passFlag=="审核退回"){
					qsIsPass = false;
				}
				listQs.push(qsIs);
			}
		}
		if(!qsIsPass||licInstance.passFlag=="审核退回"||orgInstance.passFlag=="审核退回"){
			liutongProducer.msg="审核退回";
		}
		var liutongVo={
			liutongToProduce:liutongProducer,
			fieldValues:fieldValue,
			listLtQs:listQs,
		};
		return liutongVo;
	};
	
	fsn.goBackProducer=function(){
		var liutongVo = fsn.createInstance();
		var produceId = null;
		var org = null;
		if(fsn.testReport!=null) {
			produceId = fsn.testReport.sample.producer.id;
			org = fsn.testReport.organization;
		}
		var result = true;
		$.ajax({
			url:fsn.HTTP_PREFIX + "/liutong/approved?orgId="+org+"&produceId="+produceId+"&passFlag="+false,
			type:"PUT",
			dataType:"json",
			async:false,
			contentType: "application/json; charset=utf-8",
            data: JSON.stringify(liutongVo),
			success:function(returnValue){
				if(returnValue.result.status != "true"){
					result = false;
				}
			},
			error:function(e){
				result = false;
			}
		});
		return result;
	};
	
	root.initGrid=function(formId,column,dataSource){
		$("#"+formId).kendoGrid({
			 dataSource:dataSource==null?[]:dataSource,
	   		 navigatable: true,
	   		 editable: false,
	   		 pageable: {
	   			 messages: fsn.gridPageMessage(),
	   		 },
			 columns:column,
		});
	};
	
	root.initWindow = function(formId,title,w,h){
		$("#"+formId).kendoWindow({
			title:title,
			width:w,
			height:h,
			modal:true,
			visible:false,
			actions:["Close"]
		});
	};
	
	fsn.initialize();
});