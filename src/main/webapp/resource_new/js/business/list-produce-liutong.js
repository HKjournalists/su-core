$(document).ready(function(){
	
	var fsn = window.fsn = window.fsn || {};
	var upload = window.fsn.upload = window.fsn.upload ||{};
	var root = window.fsn.root = window.fsn.root || {};
	fsn.HTTP_PREFIX = fsn.getHttpPrefix();
	root.aryOrgAttachments = new Array();
	root.aryliceNoAttachments = new Array();
    root.validateQsNoResult = true;
	
	/*初始化方法*/
	fsn.initialize = function(){
		fsn.initProduceGrid("produce_grid", fsn.produceColumn, fsn.produceDS);
		fsn.initGridOfToolBar("qs_grid", fsn.qsColumn, null,"toolbar_template");
		fsn.initWindow("busDetailWin","生产企业详细信息","800px","700px");
		fsn.initWindow("uploadQSFileWin","上传生产许可证图片","350px","350px");
		fsn.initWindow("toSaveWindow","保存状态","500px","");
		$("#btn_save").bind("click",root.save);
	};

	root.FirmatDs = function(){
		$.ajax({
			url:fsn.HTTP_PREFIX +  "/product/loadlistFormatqs",
			type:"GET",
			dataType:"json",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					return returnValue.data;
				}
			}
		});
	};
	
	fsn.initAllUpload = function(){
		$("#uploadLiceNoFiles_div").html("<input id='uploadLiceNofiles' type='file' />");
		upload.buildUpload("uploadLiceNofiles",root.aryliceNoAttachments,"liceNoFileMsg","uploadLiceNofiles");
		$("#uploadOrgfiles_div").html("<input id='uploadOrgfiles' type='file' />");
		upload.buildUpload("uploadOrgfiles",root.aryOrgAttachments,"orgCodeFileMsg","uploadOrgfiles");
	};
	
	/*生产企业信息Grid列*/
	fsn.produceColumn = [
	                     {field:"producerName",title:lims.l("enterpriseName"),width: 100},
	                     {field:"fullFlag",title:lims.l("infoIsFull"),width: 30, template: function(dataItem) {
	                    	var style ="style='color:green;'";
	                    	if(!dataItem.fullFlag) style ="style='color:red;'";
	                    	return "<strong "+style+">" + (dataItem.fullFlag?'是':'否') + "</strong>";                 		 
	                     }},
	                     {field:"msg",title:lims.l("Message"),width: 100, filterable: false, template: function(dataItem) {
	                    	 var color = "black";
		                	 if(dataItem.msg=="审核通过"){
		                		 color = "green";
		                	 }else if(dataItem.msg=="审核退回"){
		                		 color = "red";
		                	 }
		                	 return "<strong style='color:"+color+";'>" + (dataItem.msg==null?"":dataItem.msg) + "</strong>";               		 
		                     }},
	                     { command: [{
                       	  name:"View",
		                      text: lims.l("View Ditail"),
							  click: function(e){
								  e.preventDefault();
								  var editRow = $(e.target).closest("tr");
								  var item = this.dataItem(editRow);
								  root.producerId = item.producerId;
								  root.getProduceInfo(item);
							  }
						  },
						  ], title:lims.l("Operation"), width: 40 }
	                     ];
	
	/*编辑grid时 指定某一列不可编辑*/
	root.noneEditCell=function(container,options){
   	 	var input = $("<input disabled='disabled'/>");
   	 	input.attr("name", options.field);
   	 	input.attr("class", "k-textbox");
   	 	input.appendTo(container);
	 };
     
     //加载qs录入规则               
    root.loadQsFormat = function(container, options){
			$('<input data-value-field="name" data-bind="value:' + options.field + '"/>')
			.appendTo(container)
			.kendoDropDownList({
				autoBind: false,
                optionLabel:"请选择...",
                dataTextField: "formetName",
                dataValueField: "id",
				dataSource: {
                    transport: {
                read: {
                    type: "GET",
                    dataType: "json",
                    async: true,
                    contentType: "application/json",
                    url: fsn.HTTP_PREFIX +  "/product/loadlistFormatqs"
                }
            },
            schema: {
                data: function(d) {
                    return d.data;
                }
            }
                }
			});
		}
        
        //格式化qs号码
    root.validateQsNo = function(domObj) {
        var qsNo = $(domObj).val().trim();
        qsNo = qsNo.toUpperCase();//将字符串转为大写
        var model = root.formet.formetName.substring(3,root.formet.formetName.length);
        root.validateQsNoResult = root.QsNOStandard(model,qsNo,model.length);
    };
    
    root.QsNOStandard = function(model,qsNo,length){
        var qsNumber = qsNo.indexOf(")XK")==2?qsNo.substr(5):qsNo.substr(2);
        qsNumber = qsNumber.replace(/-/g,"").replace(/\s+/g,"");
        var status =  /^\d+$/.test(qsNumber);
        if (!status) {
            fsn.initNotificationMes("您输入的QS号不符合所选择的QS录入规则 <strong>QS录入规则</strong>!", false);
            return;
        }
        var result = false;
        switch (model) {
            case "QSxxxx xxxx xxxx":
                if(qsNo.indexOf("QS")>=0&&qsNo.indexOf(" ")>=6&&qsNo.indexOf("-")<0 && length==16){
                    result = true;
                }else{
                    result = false;
                    fsn.initNotificationMes("您输入的QS号不符合所选择的QS录入规则 <strong>QS录入规则</strong>!", false);
                }
                break;
            case "QSxx-xxxxx-xxxxx":  
                if(qsNo.indexOf("QS")>=0&&qsNo.indexOf(" ")<0&&qsNo.indexOf("-")>=4 && length==16){
                    result = true;
                }else{
                    result = false;
                    fsn.initNotificationMes("您输入的QS号不符合所选择的QS录入规则 <strong>QS录入规则</strong>!", false);
                }
                break;
            case "XKxx-xxx-xxxxx":
                if(qsNo.indexOf("XK")==0&&qsNo.indexOf(" ")<0&&qsNo.indexOf("-")>=4 && length==14){
                    result = true;
                }else{
                    result = false;
                    fsn.initNotificationMes("您输入的QS号不符合所选择的QS录入规则 <strong>QS录入规则</strong>!", false);
                }
                break;
            case "(?)XKxx-xxx-xxxxx":
                if(qsNo.indexOf(")XK")==2&&qsNo.indexOf(" ")<0&&qsNo.indexOf("-")>=7 && length==17){
                    result = true;
                }else{
                    result = false;
                    fsn.initNotificationMes("您输入的QS号不符合所选择的QS录入规则 <strong>QS录入规则</strong>!", false);
                }
                break;
            default:
                ;
        }
        return result
     }

        root.editqsNo = function(container, options){
            var formatName = options.model.qsInstance.qsnoFormat.formetName;
            var selectFormatFlag = false; 
            if( root.FirmatDs ){
            var formatDs =  root.FirmatDs;
                for(var i = 0 ; i< formatDs.length; i++){
                    if(formatName != null && formatName != "" && formatDs[i].formetName == formatName){
                        selectFormatFlag = true; 
                        root.formet = formatDs[i];
                        break ;
                    }
                }
            }else{
                fsn.initNotificationMes("请先选择QS号输入规则!", false);
                return;
            }
            $('<input onblur="fsn.root.validateQsNo(this)" data-value-field="name" data-bind="value:' + options.field + '"/>')
			.appendTo(container)
            if(!selectFormatFlag && formatName==""){
                fsn.initNotificationMes("请先选择QS号输入规则!", false);
                return;
            }
        }
	
	/*生产企业信息Qs Grid列*/
	fsn.qsColumn = [
	                 {field:"qsInstance.qsnoFormat.formetName",title:"QS录入规则",width: 60,editor:root.loadQsFormat},
                     {field:"qsInstance.qsNo",title:lims.l("Production license No"),editor:root.editqsNo,width: 40},
	                 {field:"passFlag",title:lims.l("Status"),width: 20, editor:root.noneEditCell, template:function(item){
	                	 var color = "black";
	                	 if(item.passFlag=="审核通过"){
	                		 color = "green";
	                	 }else if(item.passFlag=="审核退回"){
	                		 color = "red";
	                	 }
	                	 return "<strong style='color:"+color+";'>" + item.passFlag + "</strong>"; 
	                 }},
	                 {field:"msg",title:lims.l("Message"),width: 40, editor:root.noneEditCell},
	                 { command: [{
	               	  name:"View",
	                      text: lims.l("upload Production license picture"),
						  click: function(e){
							  e.preventDefault();
							  $(".qHide").css("display","inline");
							  var editRow = $(e.target).closest("tr");
							  var ltQs = this.dataItem(editRow);
							  var qsInstance = ltQs.qsInstance;
							  root.currentQsAttachments=qsInstance.qsAttachments;
	            		  	  $("#listProDep").html("");
	            		  	  $("#QsMsg").html("(注意:文件大小不能超过10M！ 可支持文件格式：png .bmp .jpeg .jpg )");
	            		  	  if(qsInstance.qsAttachments.length==0){$("#btn_clearQsFiles").hide();}
	            		  	  if(qsInstance.qsAttachments){
	            		  		  upload.setAttachments(qsInstance.qsAttachments,"btn_clearQsFiles","qsAttachmentsListView","delQsFilesTemplate");
	            		  		$("#qsAttachmentsListView button").hide();
	            		  	  }
	            		  	  if(ltQs!=null&&ltQs.passFlag == "审核通过"){
	            		  		  $(".qHide").css("display","none");
	            		  	  }
	            		  	  $("#uploadQsFilesdiv").html("");
	            		  	  $("#uploadQsFilesdiv").html("<input id='uploadQsfiles' type='file' />");
	            		  	  upload.buildUpload("uploadQsfiles",qsInstance.qsAttachments,"QsMsg","uploadQsfiles");
	            		  	  $("#btn_clearQsFiles").bind("click",root.clearQSFiles);
	            		  	  $("#uploadQSFileWin").data("kendoWindow").open().center();
						  }
					  },
					  ], title:lims.l("uploda picture"), width: 40 }
	               ];
	
	fsn.produceDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = "null";
            		if(options.filter){
            			configure = filter.configure(options.filter);
            		}
					return fsn.HTTP_PREFIX + "/liutong/getListProducer/" + configure+ "/" + options.page + "/" + options.pageSize ;
				},
                dataType : "json",
                contentType : "application/json"
            }
        },
        batch : true,
        page:1,
        pageSize: 10,
        schema: {
            data : function(returnValue) {
            	return returnValue.data!=null?returnValue.data:[];
            },
            total : function(returnValue) {       
                return returnValue.counts;
            }     
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	/*新增一行QS信息*/
	fsn.qsGridAddItem = function(){
        if(root.validateQsNoResult){
            var qsIns = {qsnoFormat: {formetName: ""},qsNo:"",qsAttachments:new Array()};
		    var item = {qsInstance:qsIns,producerId:root.producerId,passFlag:"等待审核"};
		    $("#qs_grid").data("kendoGrid").dataSource.add(item);
        }else{
            fsn.initNotificationMes("请先完善当前行的内容!", false);
        }
		
	};
	
	/*删除一行QS信息*/
	fsn.qsGriDelItem = function(){
		var size = $("#qs_grid").data("kendoGrid").select().length;
		if(size == 1){
			var selectItem = $("#qs_grid").data("kendoGrid").select()[0];
			var selectQsModel = $("#qs_grid").data("kendoGrid").dataItem(selectItem);
			if(selectQsModel.id==null){
				$("#qs_grid").data("kendoGrid").dataSource.remove(selectQsModel);
			}else{
				fsn.initNotificationMes(lims.l("The currently selected QS information is not allowed to delete, select the new QS information")+"!", false);
			}
		}else{
			fsn.initNotificationMes(lims.l("Please select a row to delete QS information")+"!", false);
		}
	};
	
	function getAttachmentsByFlag(flag){
		if(flag == "QS") return root.currentQsAttachments;
		if(flag == "ORG") return root.aryOrgAttachments;
		if(flag == "LIC") return root.aryliceNoAttachments;
	};
	
	root.removeQsRes = function(resId,fileName){
		var attachments = getAttachmentsByFlag("QS");
		upload.removeRes(attachments,resId,fileName,"qsAttachmentsListView","btn_clearQsFiles");
	};
	
	root.removeOrgRes = function(resId,fileName){
		var attachments = getAttachmentsByFlag("ORG");
		upload.removeRes(attachments,resId,fileName,"orgAttachmentsListView","btn_clearOrgFiles");
	};
	
	root.removeLicRes = function(resId,fileName){
		var attachments = getAttachmentsByFlag("LIC");
		upload.removeRes(attachments,resId,fileName,"liceNoAttachmentsListView","btn_clearliceNoFiles");
	};
	
	root.clearAttachments = function(target, listViewId, clearBtnId){
		switch(target){
		case "QS": root.currentQsAttachments.length=0;break;
		case "ORG": root.aryOrgAttachments.length=0;break;
		case "LIC": root.aryliceNoAttachments.length=0;break;
		}
		$("#"+listViewId).data("kendoListView").dataSource.data([]);
		$("#"+clearBtnId).hide();
	};
	
	root.closeWindow = function(winId){
		$("#"+winId).data("kendoWindow").close();
	};
	
	/*清空窗口中上一次显示的值*/
	root.initWindoVal = function(){
		root.aryOrgAttachments.length=0;
		root.aryliceNoAttachments.length=0;
		$("#orgCode").val("");
		$("#orgCodeStatus").val("");
		$("#orgCodeMsg").val("");
		$("#licenseNo").val("");
		$("#liceStatus").val("");
		$("#liceMsg").val("");
		document.getElementById("licenseNo").disabled=false;
		document.getElementById("orgCode").disabled=false;
		$(".oHide").css("display","inline");
		$(".lHide").css("display","inline");
		fsn.initAllUpload();
	};
	
	root.setWindowVal = function(listFileValue,listQs){
		root.initWindoVal();
		if(listFileValue == null && listQs == null) return;
		var orgCodeIns = null;
		var liceNo = null;
		root.orgCodeId = null;
		root.LiceNoId = null;
		for(var i=0;i<listFileValue.length;i++){
			if(listFileValue[i].display=="组织机构代码"){
				orgCodeIns = listFileValue[i];
				if(orgCodeIns.attachments!=null&&orgCodeIns.attachments.length>0){
					for(var j=0;j<orgCodeIns.attachments.length;j++){
						root.aryOrgAttachments.push(orgCodeIns.attachments[j]);
					}
				}
				root.orgCodeId = orgCodeIns.id;
				$("#orgCode").val(orgCodeIns.value);
				$("#orgCodeStatus").val(orgCodeIns.passFlag);
				$("#orgCodeMsg").val(orgCodeIns.msg);
			}else{
				liceNo = listFileValue[i];
				root.LiceNoId = liceNo.id;
				if(liceNo.attachments!=null&&liceNo.attachments.length>0){
					for(var k=0;k<liceNo.attachments.length;k++){
						root.aryliceNoAttachments.push(liceNo.attachments[k]);
					}
				}
				$("#licenseNo").val(liceNo.value);
				$("#liceStatus").val(liceNo.passFlag);
				$("#liceMsg").val(liceNo.msg);
			}
		}
		upload.setAttachments(root.aryOrgAttachments,"btn_clearOrgFiles","orgAttachmentsListView","delOrgFilesTemplate");
		upload.setAttachments(root.aryliceNoAttachments,"btn_clearliceNoFiles","liceNoAttachmentsListView","delLiceNoFilesTemplate");
		$("#liceNoAttachmentsListView button").hide();
		$("#orgAttachmentsListView button").hide();
		if(liceNo!=null&&liceNo.passFlag == "审核通过"){
			$(".lHide").css("display","none");
			document.getElementById("licenseNo").disabled=true;
		}
		if(orgCodeIns!=null&&orgCodeIns.passFlag == "审核通过"){
			$(".oHide").css("display","none");
			document.getElementById("orgCode").disabled=true;
		} 
		$("#qs_grid").data("kendoGrid").setDataSource(new kendo.data.DataSource({data:listQs,page:1,pageSize:10}));
		$("#busDetailWin").data("kendoWindow").open().center();
	};
	
	root.getProduceInfo = function(item){
		if(item==null) return;
		$.ajax({
			url:fsn.HTTP_PREFIX + "/liutong/getProducerById/"+item.producerId,
			type:"GET",
			dataType:"json",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					$("#businessName").val(item.producerName);
					root.setWindowVal(returnValue.listFieldValue,returnValue.listQs);
				}
			}
		});
	};
	
	root.validate=function(){
		var licenseNo = $("#licenseNo").val().trim();
		if(licenseNo == ""){
			fsn.initNotificationMes(lims.l("The business license cannot be empty")+"!", false);
			return false;
		}
		if(root.aryliceNoAttachments.length<1){
			fsn.initNotificationMes(lims.l("Business license")+lims.l("the pictures cannot be empty")+"!", false);
			return false;
		}
		var orgCode = $("#orgCode").val().trim();
		if(orgCode == ""){
			fsn.initNotificationMes(lims.l("Organization code cannot be empty")+"!", false);
			return false;
		}else{
//			var org=/^[A-Z0-9]{8}(\-)[A-Z0-9]{1}$/;
//			 if(!($("#orgCode").val().trim()).match(org)){
//			 	fsn.initNotificationMes("组织结构代码必须由8位数字(或大写字母)和一位数字(或者大写字母)组成！例如：1111AAAA-A",false);
//            	return false;
//            }
		}
		if(root.aryOrgAttachments.length<1){
			fsn.initNotificationMes(lims.l("Organization code certificate")+lims.l("the pictures cannot be empty")+"!", false);
			return false;
		}
		var qsItems = $("#qs_grid").data("kendoGrid").dataSource.data();
		if(qsItems!=null){
			for(var i=0;i<qsItems.length;i++){
				var item = qsItems[i];
                var model = item.qsInstance.qsnoFormat.formetName.substring(3,item.qsInstance.qsnoFormat.formetName.length);
				var validateQS = root.QsNOStandard(model,item.qsInstance.qsNo.toUpperCase(),model.length);
                if(!validateQS){
                    fsn.initNotificationMes("在QS信息中，第"+(i+1)+"行的QS号格式不正确,请输入对应规则的QS号!", false);
                    return false;
                }
                var qsModel = item.qsInstance;
                if(qsItems[i].passFlag=="审核通过")continue;
				if(qsModel.qsAttachments==null || qsModel.qsAttachments.length<1){
					fsn.initNotificationMes("在QS信息中第"+(i+1)+"行的QS图片没有上传!", false);
					return false;
				}
			}
		}
		return true;
	};
	
	/*封装保存的数据*/
	root.createInstance = function(){
		var liutongProducer={
			producerId:root.producerId,
			producerName:$("#businessName").val().trim(),
			msg:"等待审核",
		};
		var fieldValue = new Array();
		var orgInstance = {
			id:root.orgCodeId,
			value:$("#orgCode").val().trim(),
			display:"组织机构代码",
			producerId:root.producerId,
			fullFlag:true,
			passFlag:($("#orgCodeStatus").val().trim()!="审核通过"?"等待审核":"审核通过"),
			attachments:root.aryOrgAttachments,
		};
		fieldValue.push(orgInstance);
		var licInstance = {
				id:root.LiceNoId,
				value:$("#licenseNo").val().trim(),
				display:"营业执照号",
				producerId:root.producerId,
				fullFlag:true,
				passFlag:($("#liceStatus").val().trim()!="审核通过"?"等待审核":"审核通过"),
				attachments:root.aryliceNoAttachments,
			};
		fieldValue.push(licInstance);
		var qsItems = $("#qs_grid").data("kendoGrid").dataSource.data();
		var listQs = new Array();
		var qsFg = true;
		if(qsItems!=null && qsItems.length>0){
			for(var i=0;i<qsItems.length;i++){
				if(qsItems[i].qsInstance.qsNo=="") continue;
                
                var qsnoFormatId = 1;
                var formatName = qsItems[i].qsInstance.qsnoFormat.formetName;
                for(var j = 0 ; j< root.FirmatDs.length; j++){
                    if(formatName != null && formatName != "" && root.FirmatDs[j].formetName == formatName){
                        qsnoFormatId = root.FirmatDs[j].id;
                        break ;
                    }
                }
				var qsIs = {
					id:qsItems[i].id,
					producerId:root.producerId,
					fullFlag:true,
					passFlag:(qsItems[i].passFlag!="审核通过"?"等待审核":"审核通过"),
					qsInstance:{
						qsNo:qsItems[i].qsInstance.qsNo,
						qsAttachments:qsItems[i].qsInstance.qsAttachments,
                        qsnoFormat:{id:qsnoFormatId}
					}
				};
				if(qsIs.passFlag!="审核通过") qsFg = false;
				listQs.push(qsIs);
			}
		}
		if(qsFg&&licInstance.passFlag=="审核通过"&&orgInstance.passFlag=="审核通过"){
			liutongProducer.msg="审核通过";
		}
		var liutongVo={
			liutongToProduce:liutongProducer,
			fieldValues:fieldValue,
			listLtQs:listQs
		};
		return liutongVo;
	};
	
	root.save = function(){
        if(!root.validateQsNoResult){
            fsn.initNotificationMes("请先完善生产许可证信息!", false);
            return;
        }
		if(!root.validate()) return;
		$("#busDetailWin").data("kendoWindow").close();
		var saveModel = root.createInstance();
		
		$("#toSaveWindow label").html("正在保存，请稍候...");
		$("#toSaveWindow").data("kendoWindow").open().center();
		$.ajax({
			url:fsn.HTTP_PREFIX + "/liutong/saveModel",
			type:"POST",
			timeout:600000,   //10min
			dataType:"json",
			contentType: "application/json; charset=utf-8",
            data: JSON.stringify(saveModel),
			success:function(returnValue){
				$("#toSaveWindow").data("kendoWindow").close();
				if(returnValue.result.status == "true"){
					fsn.produceDS.read();
//					root.setWindowVal(returnValue.data.fieldValues,returnValue.data.listLtQs);
					fsn.initNotificationMes(lims.l("Saved successfully")+"!", true);
				}else{
					fsn.initNotificationMes(lims.l("Save failed")+"!", false);
				}
			}
		});
	};
	
	fsn.initProduceGrid=function(formId,column,dataSource){
		$("#"+formId).kendoGrid({
			dataSource:dataSource==null?[]:dataSource,
	   		editable: false,
	   		selectable:true,
	   		filterable: {
				extra:false,
				messages: lims.gridfilterMessage(),
				operators: {
					  string: lims.gridfilterOperators()
				}
			},
			pageable: {
	            refresh: true,
	            pageSizes: 5,
	            messages: lims.gridPageMessage(),
	        },
			 columns:column,
		});
	};
	
	fsn.initGridOfToolBar=function(formId,column,ds,toolbarId){
		 $("#"+formId).kendoGrid({
		 dataSource:ds==null?[]:ds,
   		 editable: true,
   		 selectable:true,
   		 pageable: {
   			 messages: lims.gridPageMessage()
   		 },
   		 toolbar: [
		          {template:kendo.template($("#"+toolbarId).html())}
		          ],
		     columns:column,
		 });
	 };
	
	fsn.initWindow = function(formId,title,w,h){
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