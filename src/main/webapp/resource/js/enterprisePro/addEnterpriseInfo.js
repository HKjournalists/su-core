$(document).ready(function(){
	 var fsn = window.fsn = window.fsn || {};
	 var business_unit = window.fsn.business_unit = window.fsn.business_unit ||{};
	 fsn.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	 fsn.business={};
	 fsn.listFieldValue=new Array();
	 fsn.listBrands=new Array();
	 fsn.listPdfDS=null;
	 business_unit.aryOrgAttachments = new Array();  //组织机构图片
	 business_unit.aryLicenseAttachments = new Array(); //营业执照图片
	 business_unit.aryQsAttachments = new Array();   //qs证图片
	 business_unit.aryTaxAttachments = new Array();  // 税务登记图片
	 business_unit.aryLiquorAttachments = new Array(); // 酒类销售许可证图片
	
	 fsn.initialize=function(){
		 $("#upload_tax_div").html("<input id='upload_tax_files' type='file' />");
		 business_unit.buildUpload("upload_tax_files",business_unit.aryTaxAttachments,"taxFileMsg");
		 $("#upload_liquor_div").html("<input id='upload_liquor_files' type='file' />");
		 business_unit.buildUpload("upload_liquor_files",business_unit.aryLiquorAttachments,"liquoroFileMsg");
		 $("#btn_clearTaxFiles").bind("click",business_unit.clearTaxFiles);
		 $("#btn_clearLiquorFiles").bind("click",business_unit.clearLiquorFiles);
		 $("#btn_clearTaxFiles").hide();
		 $("#btn_clearLiquorFiles").hide();
		 $("#btn_clearProDepFiles").hide();
		 fsn.initGrid("sccj_grid",fsn.gridDataSource,fsn.column,"toolbar_template");
		 fsn.initGrid("gkjc_grid",fsn.gridDataSource2,fsn.column2,"toolbar_template2");
		 fsn.initGridNotoolbar("pdf_grid",fsn.listPdfDS,fsn.pdfColumn);
		 $("#next_btn_2").bind("click", fsn.savePage2);
		 $("#next_btn_3").bind("click", fsn.savePage3);
		 //$("#uploadPdf_btn").bind("click", fsn.uploadBusInfoPdf);
		 //$("#viewListPdf_btn").bind("click", fsn.viewListPdf);
		 $("#btn_no").bind("click",fsn.closeWarWin);
		 fsn.initKendoWindow("k_window","保存状态","300px","60px",false);
		 fsn.initKendoWindow("fillTablWindow","","","",false);
		 fsn.initKendoWindow("warningWin","警告","500px","200px",false);
		 fsn.initKendoWindow("uploadProDepWin","上传车间图片","650px","",false);
		 fsn.getCurrentBusiness();
		 $(".gTop").bind("mouseenter",function(){fsn.buileAnimate(".gTop a","800px","250px");});
		 $(".gTop").bind("mouseleave",function(){fsn.buileAnimate(".gTop a","100%",'100%');});
		 
	 };
	 
	 
	 fsn.column=[
	              { field: "id",width:1,height:30},
	              { field: "name", title:lims.l("名称"),width:40,height:30},
	              { field: "address", title:lims.l("详细地址（镇、村、组）"),width: 90,height:30 },
	              { field: "legalName", title:lims.l("负责人"),width: 40 ,height:30},
	              { field: "telephone", title:lims.l("联系电话"),width: 50 ,height:30},
	              { command: [{name:"edit",text:lims.l("车间图片"), 
	            	  click:function(e){
	            		 //上传车间图片的处理方法
            		  	e.preventDefault();
            		  	var delItem = this.dataItem($(e.currentTarget).closest("tr"));
            		  	business_unit.currentDepAtts=delItem.depAttachments;
            		  	$("#listProDep").html("");
            		  	$("#proDepMsg").html("(注意:文件大小不能超过10M！ 可支持文件格式：png .bmp .jpeg .jpg )");
            		  	if(delItem.depAttachments.length==0){$("#btn_clearProDepFiles").hide();}
            		  	if(delItem.depAttachments){
            		  		business_unit.setProDepAttachments(delItem.depAttachments);
            		  	}
            		  	$("#uploadProDep_div").html("");
            		  	$("#uploadProDep_div").html("<input id='upload_proDep_files' type='file' />");
            		  	business_unit.buildUpload("upload_proDep_files",delItem.depAttachments,"proDepMsg");
            		  	$("#btn_clearProDepFiles").bind("click",business_unit.clearProDepFiles);
            		  	$("#uploadProDepWin").data("kendoWindow").open().center();
            	  		}},
	                    {name:"Remove",
            	  		text:"<span class='k-icon k-cancel'></span>" + lims.l("删除"), 
             	        click:function(e){
             	    	e.preventDefault();
             	    	var delItem = this.dataItem($(e.currentTarget).closest("tr"));
             	    	$("#sccj_grid").data("kendoGrid").dataSource.remove(delItem);
                 }}], title:"操作", width: 60 }
	            ];
	 
	 fsn.column2=[
	              {field: "id", title: "id", editable: false, width:1},
	              { field: "name", title:lims.l("名称"),width:40,height:30},
	              { field: "address", title:lims.l("详细地址（镇、村、组）"),width: 90,height:30 },
	              { field: "legalName", title:lims.l("负责人"),width: 40 ,height:30},
	              { field: "year", title:lims.l("年限"),width: 40 ,height:30},
	              { field: "inCommissionNum", title:lims.l("投产窖池数"),width: 40 ,height:30},
	              { field: "telephone", title:lims.l("联系电话"),width: 50 ,height:30},
	              { command: [{name:"Remove",
	            	  text:"<span class='k-icon k-cancel'></span>" + lims.l("删除"), 
                 	    click:function(e){
                 	    	e.preventDefault();
                 	    	var delItem = this.dataItem($(e.currentTarget).closest("tr"));
                 	    	$("#gkjc_grid").data("kendoGrid").dataSource.remove(delItem);
                 }}], title:"操作", width: 30 }
	            ];
	 
	 fsn.deleteBusPdfById=function(){
		 if(fsn.delItem==null){return;}
		 $.ajax({
			 url:fsn.HTTP_PREFIX+"/business/removeBusUnitPdf/"+fsn.delItem.id+"/"+fsn.businessId,
			 type:"POST",
			 success:function(data){
				 $("#warningWin").data("kendoWindow").close();
				 if(data.result.success){
					 $("#pdf_grid").data("kendoGrid").dataSource.remove(fsn.delItem);
					 lims.initNotificationMes("pdf删除成功！", true);
				 }else if(data.result.offlineFlag){
					 location.reload(true);     
				 }else{
					 lims.initNotificationMes("pdf删除失败！", false);
				 }
			 }
		 });
	 };
	 
	 fsn.closeWarWin=function(){
		$("#warningWin").data("kendoWindow").close(); 
	 };
	 
	 fsn.pdfColumn=[
	                { field: "id", title:"",width: 1 },
	                { field: "fileName", title:"文件名",width: 100},
	                { field: "uploadDate", title:"上传日期",width: 100},
	                { command: [/*{name:"Remove",
	                	text:"<span class='k-icon k-cancel'></span>" + "删除", 
	                	click:function(e){
	                		e.preventDefault();
	                		fsn.delItem = this.dataItem($(e.currentTarget).closest("tr"));
	                		$("#btn_yes").bind("click",fsn.deleteBusPdfById);
	                		$("#tsText").html("你确定要删除pdf吗，该操作不可恢复。");
	                		$("#warningWin").data("kendoWindow").open().center();
	                	}},*/
	                	{name:"edit",
		                	text:"查看", 
		                	click:function(e){
		                		e.preventDefault();
		                		var item = this.dataItem($(e.currentTarget).closest("tr"));
		                		window.open(item.url);
		                	}},
		                	/*{name:"edit",
			                	text:"<span class='k-icon k-cancel'></span>" + "下载", 
			                	click:function(e){
			                		e.preventDefault();
			                		var item = this.dataItem($(e.currentTarget).closest("tr"));
			                	}}*/
	                	], title:"操作", width: 30 } 
	                ];
	 
	 fsn.gridDataSource2 = new kendo.data.DataSource({
	     	data:[{ name:"", address:"", legalName:"",inCommissionNum:"",year:"",telephone:""},
	     	     { name:"", address:"", legalName:"",inCommissionNum:"",year:"",telephone:""},
	     	     { name:"", address:"", legalName:"",inCommissionNum:"",year:"",telephone:""},
	     	     { name:"", address:"", legalName:"",inCommissionNum:"",year:"",telephone:""}],
	     	     batch : true,
	             page:1,
	             serverPaging : true,
	             serverFiltering : true,
	             serverSorting : true
	           });
	 
	 fsn.gridDataSource = new kendo.data.DataSource({
	     	data:[{ name:"", address:"", legalName:"",telephone:"",depAttachments:new Array()},
	     	      { name:"", address:"", legalName:"",telephone:"",depAttachments:new Array()},
	     	      { name:"", address:"", legalName:"",telephone:"",depAttachments:new Array()},
	     	      { name:"", address:"", legalName:"",telephone:"",depAttachments:new Array()}],
	     	     batch : true,
	             page:1,
	             serverPaging : true,
	             serverFiltering : true,
	             serverSorting : true
	           });
	 
	 fsn.getListBusUnitPdf=function(){
		 fsn.listPdfDS=new kendo.data.DataSource({
				transport: {
		            read: {
		            	url : function(options){
		            		if(options.filter){
		            			var configure = filter.configure(options.filter);
		            			return fsn.HTTP_PREFIX + "/business/getBusUnitListPdf/" + options.page + "/" + options.pageSize+"/"+fsn.businessId;
		            		}
							return fsn.HTTP_PREFIX + "/business/getBusUnitListPdf/" + options.page + "/" + options.pageSize+"/"+fsn.businessId;;
						},
		                dataType : "json",
		                contentType : "application/json"
		            }
		        },
		        batch : true,
		        page:1,
		        pageSize: 5,
		        schema: {
		            data : function(returnValue) {
		                return returnValue.listPdf;
		            },
		            total : function(returnValue) {       
		                return returnValue.total;
		            }  
		        },
		        serverPaging : true,
		        serverFiltering : true,
		        serverSorting : true
			});
	 };
	 
	 fsn.grid_AddItem=function(){
    	 $("#sccj_grid").data("kendoGrid").dataSource.add({ name:"", address:"", leader:"",phone:"",depAttachments:new Array()});
     };
     fsn.grid_AddItem2=function(){
    	 $("#gkjc_grid").data("kendoGrid").dataSource.add({ name:"", address:"", legalName:"",inCommissionNum:"",year:"",telephone:""});
     };
	 
	 fsn.initGrid=function(formId,ds,column,toolbarId){
		 $("#"+formId).kendoGrid({
			 dataSource:ds==null?[]:ds,
    		 navigatable: true,
    		 editable: true,
    		 pageable: {
    			 messages: lims.gridPageMessage(),
    		 },
    		 toolbar: [
 		          {template:kendo.template($("#"+toolbarId).html())}
 		          ],
 		     columns:column,
		 });
	 };
		 
	fsn.initGridNotoolbar=function(formId,ds,column){
		$("#"+formId).kendoGrid({
			dataSource:ds==null?[]:ds,
	    	navigatable: true,
	    	editable: false,
	    	pageable: {
	    		messages: lims.gridPageMessage(),
	    	},
	    	columns:column,
		});
	 };
	 
	 $("#tabstrip").kendoTabStrip({
	   animation: {
	   open: {
	     duration: 200,
	     effects: "expand:vertical"
	    }
	   },
	   navigatable:false,
	   collapsible: true,
	});

	fsn.goNext=function(curFormId,nextForMId){
		$("."+curFormId).attr("style","display:none;");
		$("."+nextForMId).slideDown("slow");
	};
	
	fsn.goPrevious=function(curFormId,nextForMId){
		$("."+curFormId).attr("style","display:none;");
		$("."+nextForMId).slideDown("slow");
	};

	
	
	 /* 初始化时间控件  */
	$("#establishTime,#orgStartTime,#orgEndTime,#registrationTime,#qsStartTime," +
			"#qsEndTime,#test_time_format,#liquorStartTime,#liquorEndTime").kendoDatePicker({
   	 format: "yyyy-MM-dd",
   	 height:30,
   	 culture : "zh-CN",
   	 animation: {
   	   close: {
   	     effects: "fadeOut zoom:out",
   	     duration: 300
   	   },
   	   open: {
   	     effects: "fadeIn zoom:in",
   	     duration: 300
   	   }
   	  }
   	});
	
	fsn.goNextAndSave=function(){
		fsn.createInstanceStep2();
		//提交更新
		fsn.submit(fsn.business);
	};
	
	//封装生产车间布点情况信息
	fsn.getListProducingDepartment=function(){
		var items=[];
   	 var girdItems=$("#sccj_grid").data("kendoGrid").dataSource.data();
   	 var j=0;
   	 for(var i=0;i<girdItems.length;i++){
   		 if(girdItems[i].name.trim()==""){
   			 j++;
   			 continue;
   		 }
   		 items[i-j]={
   				id:girdItems[i].id,
   				name:girdItems[i].name,
   				address:girdItems[i].address,
   				legalName:girdItems[i].legalName,
   				telephone:girdItems[i].telephone,
   				depAttachments:girdItems[i].depAttachments,
   				departmentFlag:true,
   		 };
   	 }
   	 return items;
	};
	
	business_unit.setAddrByStreetAdd=function(streetAddr){
		if(!streetAddr){return "";}
		var index=streetAddr.indexOf("镇");
		if(index>=0){
			$("#addrZhen").val(streetAddr.substring(0,index));
			streetAddr=streetAddr.substring(index+1);
		};
		index=streetAddr.indexOf("村");
		if(index>=0){
			$("#addrCun").val(streetAddr.substring(0,index));
			streetAddr=streetAddr.substring(index+1);
		};
		index=streetAddr.indexOf("组");
		if(index>=0){
			$("#addrZu").val(streetAddr.substring(0,index));
		};
	};
	
	 /*公司办公地址址赋值*/
    business_unit.setAddrValue=function(otherAddress){
    	if(!otherAddress){
    		return;
    	}
    	var addShi="";
    	var tmpAryVal=otherAddress.split("--");
    	var tmpAry2=tmpAryVal[0].split("-");
    	if(tmpAry2.length>1){
    		if(tmpAry2[0]==tmpAry2[1].substring(0,2)){
    			addShi=addShi+tmpAry2[1];
    		}else{
    			addShi=addShi+tmpAry2[0]+tmpAry2[1];
    		}
    		if(tmpAry2.length>2){
    			addShi=addShi+tmpAry2[2];
    		}
    	}else{
    		addShi=tmpAry2[0];
    	}
    	if(addShi.indexOf("市")>-1){
			addShi=addShi.substring(0,addShi.length-1);
		}
		$("#addrShi").val(addShi);
    	business_unit.setAddrByStreetAdd(tmpAryVal[1]);
    };
	
    fsn.getAddrOfPage=function(isStreetAddr){
    	var addr=$("#addrShi").val().trim();
    	if(isStreetAddr){
    		addr=addr.length>0?addr+"市--":"";
    	}else{
    		addr=addr.length>0?addr+"市":"";
    	}
    	if($("#addrZhen").val().trim().length>0){
    		addr=addr+$("#addrZhen").val().trim()+"镇";
    	}
    	if($("#addrCun").val().trim().length>0){
    		addr=addr+$("#addrCun").val().trim()+"村";
    	}
    	if($("#addrZu").val().trim().length>0){
    		addr=addr+$("#addrZu").val().trim()+"组";
    	}
    	return addr;
    };
    
	//数据封装1.1
	fsn.createInstanceStep1=function(){
		fsn.business={
				id:fsn.businessId,
				name:$("#businessName").val().trim(),
				//personInCharge:$("#personInCharge").val().trim(),
				//address:fsn.getAddrOfPage(false),
				//otherAddress:fsn.getAddrOfPage(true),
				//telephone:$("#telephone").val().trim(),
				proDepartments:fsn.getListProducingDepartment(),
				step:"step1",
				listOfCertification:null,
				logoAttachments:null,
				productionLicenses: null,
				licAttachments: business_unit.aryLicenseAttachments,
	            orgAttachments: business_unit.aryOrgAttachments,
	            //qsAttachments: business_unit.aryQsAttachments,
	            taxRegAttachments:business_unit.aryTaxAttachments,
	            liquorAttachments:business_unit.aryLiquorAttachments,
		};
	};
	
	//数据封装1.2
	fsn.createInstanceStep2=function(){
		fsn.createInstanceStep1();
		fsn.listFieldValue.length=0;
		fsn.listBrands.length=0;
		var listFields=$("div.step1 input[name!='']");
		for(var i=0;i<listFields.length;i++){
			//if(!$(listFields[i]).val().trim()){continue;}
			//如果input不是品牌
			if($(listFields[i]).attr("name")=="brand"){
				continue;
				//对品牌单独封装
				//if(!$(listFields[i]).val().trim()){continue;}
				/*var brand={
					id:$(listFields[i]).val().trim().length<1?null:$(listFields[i]).attr("data-id-text").trim(),
					name:$(listFields[i]).val().trim(),
				};
				fsn.listBrands.push(brand);*/
			}else {
				var field_={
						id:$(listFields[i]).attr("id"),
						fieldName:$(listFields[i]).attr("name"),
					};
				var fieldVal={
						value:$(listFields[i]).val().trim(),
						busunitId:fsn.businessId,
						templateId:1,
						field:field_,
				};
				//对股东信息单独封装id
				if($(listFields[i]).attr("name")=="majorShareholder"){
					if($(listFields[i]).val().trim().length>0){
						fieldVal.id=$(listFields[i]).attr("data-id-text").trim();
						fsn.listFieldValue.push(fieldVal);
					}
				}else{
					fsn.listFieldValue.push(fieldVal);
				}
			}
		}
		fsn.business.fieldValues=fsn.listFieldValue;
		//fsn.business.brands=fsn.listBrands;
	};
	
	//第一次保存成功之后，给brand赋值
	fsn.setBrandId=function(listBrand){
		for(var i=0;i<listBrand.length;i++){
			var brandName=listBrand[i].name;
			var listBrandInput=$("input[name='brand']");
			for(var j=0;j<listBrandInput.length;j++){
				if(brandName==$(listBrandInput[j]).val().trim()){
					$(listBrandInput[j]).attr("data-id-text",listBrand[i].id);
				}
			}
		}
	};
	
	//第一次保存成功之后，给股东赋值id
	fsn.setMajorShId=function(listFields){
		if(!listFields){return;}
		var listMSinput=$("input[name='majorShareholder']");
		for(var i=0;i<listFields.length;i++){
			if(listFields[i].field&&listFields[i].field.id==32){
				$(listMSinput[i]).attr("data-id-text",listFields[i].id);
				$(listMSinput[i]).val(listFields[i].value);
			}
		}
	};
	
	//数据封装2
	fsn.createInstancePage2=function(){
		/* 营业执照信息  */
    	var license = {
        		licenseNo: $("#licenseNo").val().trim(),
        		licensename: $("#licenseName").val().trim(),
        		legalName: $("#legalName").val().trim(),
        		establishTime: $("#establishTime").val().trim(),
        		registrationTime: $("#registrationTime").val().trim(),
        		subjectType: $("#subjectType").val().trim(),
        		businessAddress: $("#license_Address").val().trim(),
        		toleranceRange: $("#toleranceRange").val().trim(),
        		registeredCapital: $("#registeredCapital").val().trim(),
        		practicalCapital: $("#practicalCapital").val().trim(),
        		rhFlag:true,
        	};
    	/* 组织机构代码信息  */
        var orgInstitution = {
        		orgCode:$("#orgCode").val().trim(),
        		orgName: $("#orgName").val().trim(),
        		startTime: $("#orgStartTime").val().trim(),
        		endTime: $("#orgEndTime").val().trim(),
        		unitsAwarded: $("#unitsAwarded").val().trim(),
        		orgType: $("#orgType").val().trim(),
        		orgAddress: $("#orgAddress").val().trim(),
        		registerNo: $("#registerNo").val().trim(),
        		rhFlag:true,
        	};
        /* 生产许可证信息  */
        var productionLicense = {
        		qsNo: $("#qsNo").val().trim(),
        		busunitName: $("#busunitName").val().trim(),
        		productName: $("#productName").val().trim(),
        		startTime: $("#qsStartTime").val().trim(),
        		endTime: $("#qsEndTime").val().trim(),
        		accommodation: $("#accommodation").val().trim(),       		
        		productionAddress: $("#qsBusinessAddress").val().trim(),       		
        		checkType: $("#checkType").val().trim(),
        		qsAttachments:business_unit.aryQsAttachments,
        		rhFlag:true,
        };
        var productionLicenses=(productionLicense.qsNo==""?[]:[productionLicense]);
        /* 税务登记信息  */
        var taxRegisterLicense = {
        		taxerName: $("#taxerName").val().trim(),
        		legalName: $("#taxLegalName").val().trim(),
        		address: $("#taxAddress").val().trim(),
        		registerType: $("#registerType").val().trim(),
        		businessScope: $("#taxBusinessScope").val().trim(),
        		approveSetUpAuthority: $("#approveSetUpAuthority").val().trim(),       		
        		issuingAuthority: $("#taxIssuingAuthority").val().trim(),       		
        		withholdingObligations: $("#withholdingObligations").val().trim(),
        };
        /* 酒类销售许可证信息  */
        var liquorSalesLicense = {
        		certificateNo: $("#LiquorSalesLicNo").val().trim(),
        		legalName: $("#liquorLegalName").val().trim(),
        		address: $("#liquorAddress").val().trim(),
        		businessType: $("#liquorBusinessType").val().trim(),
        		businessScope: $("#liquorBusinessScope").val().trim(),
        		startTime: $("#liquorStartTime").val().trim(),       		
        		endTime: $("#liquorEndTime").val().trim(),       		
        		issuingAuthority: $("#liquorIssuingAuthority").val().trim(),
        };
        var subBusiness = {
                id: fsn.businessId,
                name:$("#businessName").val().trim(),
                step:"step2",
                listOfCertification:null,
                logoAttachments:null,
                license: license,
                orgInstitution: orgInstitution,
                productionLicenses: productionLicenses,
                taxRegister:taxRegisterLicense,
                liquorSalesLicense:liquorSalesLicense,
                licAttachments: business_unit.aryLicenseAttachments,
                orgAttachments: business_unit.aryOrgAttachments,
                //qsAttachments: business_unit.aryQsAttachments,
                taxRegAttachments:business_unit.aryTaxAttachments,
            	liquorAttachments:business_unit.aryLiquorAttachments,
                
            };
        return subBusiness;
	};
	
	// 封装第3页挂靠酒厂
	 fsn.createInstancePage3 = function(){
	    	 var subDepartments=[];
	    	 var subDepartment=$("#gkjc_grid").data("kendoGrid").dataSource.data();
	    	 var j=0;
	    	 for(var i=0;i<subDepartment.length;i++){
	    		 if(subDepartment[i].name==""){
	    			 j++;
	    			 continue;
	    		 }
	    		 subDepartments[i-j]={
	    				 id: subDepartment[i].id,
	    				 name: subDepartment[i].name,
	    				 address: subDepartment[i].address,
	    				 legalName: subDepartment[i].legalName,
	    				 year: subDepartment[i].year,
	    				 inCommissionNum: subDepartment[i].inCommissionNum,
	    				 telephone: subDepartment[i].telephone,
	    				 departmentFlag:false
	    		 };
	    	 }
	    	 var subBusiness = {
	                 id: fsn.businessId,
	                 name:$("#businessName").val().trim(),
	                 subDepartments:subDepartments,
	                 step:"step3",
	                 listOfCertification:null,
	                 logoAttachments:null,
	                 productionLicenses: null,
                	 licAttachments: business_unit.aryLicenseAttachments,
    	             orgAttachments: business_unit.aryOrgAttachments,
    	             //qsAttachments: business_unit.aryQsAttachments,
    	             taxRegAttachments:business_unit.aryTaxAttachments,
    	             liquorAttachments:business_unit.aryLiquorAttachments,
	             };
	    	 return subBusiness;
	     };
	
	fsn.validateBrand=function(){
		var flag = true;
		if(fsn.listBrands!=null&&fsn.listBrands.length>0){
			for(var i=0;i<fsn.listBrands.length;i++){
				var bname=fsn.listBrands[i].name;
				if(bname.length<1){continue;}
				for(var j=i;j<fsn.listBrands.length-1;j++){
					if(bname==fsn.listBrands[j+1].name){
						lims.initNotificationMes("品牌:【"+bname+"】重复，请修改!", false);
						return false;
					}
				}
			}
			var valiiBus={
					id:fsn.businessId,
					brands:fsn.listBrands,
			};
			$.ajax({
				url:fsn.HTTP_PREFIX+"/business/business-unit/validateUniqueBrands",
				type:"PUT",
				async:false,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(valiiBus),
				success: function(returnValue) {
					if(returnValue.result.success){
						if(returnValue.error_brands.length>0){
							var listBrandName="";
							for(var k=0;k<returnValue.error_brands.length;k++){
								listBrandName = listBrandName + "【" + returnValue.error_brands[k]+"】";
							}
							lims.initNotificationMes("以下品牌已经被占用,"+listBrandName, false);
							flag = false;
						}
					}else{
						lims.initNotificationMes("后台品牌验证出现异常！", false);
						flag = false;
					}
		       }
			});
		}
		return flag;
	};
	     
	//更新操作
	fsn.submit=function(business){
		if(!business){return;}
		//if(!fsn.validateBrand()){return;}
		$("#winMsg").html("正在保存当前页面的数据，请稍候....");
	    $("#k_window").data("kendoWindow").open().center();
		$.ajax({
			url:fsn.HTTP_PREFIX+"/business/business-unit",
			type:"PUT",
			contentType: "application/json; charset=utf-8",
            data: JSON.stringify(business),
            success: function(returnValue) {
            	$("#k_window").data("kendoWindow").close();
            	if(returnValue.result.success){
            		//fsn.setBrandId(returnValue.data!=null?returnValue.data.brands:[]);
            		fsn.setMajorShId(returnValue.data!=null?returnValue.data.fieldValues:[]);
            		lims.initNotificationMes("当前页面信息保存成功！", true);
            		var bus=returnValue.data;
            		if(bus.proDepartments&&bus.proDepartments.length>0){
        				$("#sccj_grid").data("kendoGrid").dataSource.data(bus.proDepartments);
        			}else{
        				$("#sccj_grid").data("kendoGrid").setDataSource(fsn.gridDataSource);
        			}
            		//下一步
            		fsn.goNext("step1","step2");
            	}else if(returnValue.result.offlineFlag){
            		lims.initNotificationMes("用户登录信息已经失效，您需要从新登录！", false);
            		fsn.goToLogin();
            	}else{
            		lims.initNotificationMes("当前页面信息保存失败！", false);
            	}
            	
            }
		});
	};
	
	//给企业基本信息赋值
	fsn.setBusinessVaule=function(bus){
		if(bus){
			$("#businessName").val(bus.name);
			$("#personInCharge").val(bus.personInCharge);
			$("#telephone").val(bus.telephone);
			$("#address").val(bus.address);
			if(bus.proDepartments&&bus.proDepartments.length>0){
				$("#sccj_grid").data("kendoGrid").dataSource.data(bus.proDepartments);
			}else{
				$("#sccj_grid").data("kendoGrid").setDataSource(fsn.gridDataSource);
			}
			business_unit.setAddrValue(bus.otherAddress);
		}else{
			$("#businessName").val("");
			$("#personInCharge").val("");
			$("#telephone").val("");
			$("#address").val("");
		}
	};
	
	//给商标赋值
	fsn.setBrandValue=function(listBrand){
		if(listBrand){
			var len=listBrand.length;
			len=len>8?8:len;
			listBrandInput=$("input[name='brand']");
			for(var i=0;i<len;i++){
				$(listBrandInput[i]).val(listBrand[i].name);
				$(listBrandInput[i]).attr("data-id-text",listBrand[i].id);
			}
		}
	};
	
	//给企业扩展字段赋值
	fsn.setFieldValue=function(listFields){
		if(listFields){
			var len=0;
			var listShareholderInput = $("input[name='majorShareholder']");
			for(var i=0;i<listFields.length;i++){
				if(listFields[i].field&&listFields[i].field.id!=32){
					$(".step1 input[id='"+listFields[i].field.id+"']").val(listFields[i].value);
				}else{
					if(listFields[i].value.length<1){continue;}
					if(len<8){
						$(listShareholderInput[len]).val(listFields[i].value);
						$(listShareholderInput[len]).attr("data-id-text",listFields[i].id);
						len+=1;
					}
				}
			}
		}
	};
	
	//给页面1 企业信息赋值
	fsn.setPage1Value=function(business){
		fsn.setBusinessVaule(business);
		fsn.setBrandValue(business.brands);
		fsn.setFieldValue(business.fieldValues);
	};
	
	//给页面2 证照信息赋值
	fsn.setPage2Value=function(bus){
		
		/* 营业执照信息  */
		if(bus.license!=null){
			$("#licenseNo").val(bus.license.licenseNo);
			$("#licenseName").val(bus.license.licensename);
			$("#legalName").val(bus.license.legalName);
			$("#establishTime").data("kendoDatePicker").value(bus.license.establishTime);
			$("#registrationTime").data("kendoDatePicker").value(bus.license.registrationTime);
			$("#subjectType").val(bus.license.subjectType);
			$("#license_Address").val(bus.license.businessAddress);
			$("#toleranceRange").val(bus.license.toleranceRange);
			$("#registeredCapital").val(bus.license.registeredCapital);
			$("#practicalCapital").val(bus.license.practicalCapital);
			// 获取营业执照图片
	        if(bus.licAttachments != null){
	        	business_unit.setLicenseAttachments(bus.licAttachments);
	        }
		}
		
		/* 组织机构代码信息  */
		if(bus.orgInstitution!=null){			
			$("#orgCode").val(bus.orgInstitution.orgCode);
			$("#orgName").val(bus.orgInstitution.orgName);
			$("#orgStartTime").data("kendoDatePicker").value(bus.orgInstitution.startTime);
			$("#orgEndTime").data("kendoDatePicker").value(bus.orgInstitution.endTime);
			$("#unitsAwarded").val(bus.orgInstitution.unitsAwarded);
			$("#orgType").val(bus.orgInstitution.orgType);
			$("#orgAddress").val(bus.orgInstitution.orgAddress);
			$("#registerNo").val(bus.orgInstitution.registerNo);
			// 获取组织机构代码图片
	        if(bus.orgAttachments != null){
	        	business_unit.setOrgAttachments(bus.orgAttachments);
	        }
		}
		
		/* 生产许可证信息  */
		if(bus.productionLicenses.length>0){
			var productionLicense=bus.productionLicenses[0];
			$("#qsNo").val(productionLicense.qsNo);
			$("#busunitName").val(productionLicense.busunitName);
			$("#productName").val(productionLicense.productName);
			$("#qsStartTime").data("kendoDatePicker").value(productionLicense.startTime);
			$("#qsEndTime").data("kendoDatePicker").value(productionLicense.endTime);
			$("#accommodation").val(productionLicense.accommodation);       		
			$("#qsBusinessAddress").val(productionLicense.productionAddress);       		
			$("#checkType").val(productionLicense.checkType);
			// 获取生产许可证图片
	        if(productionLicense.qsAttachments != null){
	        	business_unit.setQsAttachments(productionLicense.qsAttachments);
	       }
		}
		
		/* 税务登记信息  */
		if(bus.taxRegister!=null){
			$("#taxerName").val(bus.taxRegister.taxerName);
			$("#taxLegalName").val(bus.taxRegister.legalName);
			$("#taxAddress").val(bus.taxRegister.address);
			$("#registerType").val(bus.taxRegister.registerType);
			$("#taxBusinessScope").val(bus.taxRegister.businessScope);
			$("#approveSetUpAuthority").val(bus.taxRegister.approveSetUpAuthority);       		
			$("#taxIssuingAuthority").val(bus.taxRegister.issuingAuthority);       		
			$("#withholdingObligations").val(bus.taxRegister.withholdingObligations);
			//获取税务登记图片
	        if(bus.taxRegAttachments != null){
	        	business_unit.setTaxRegAttachments(bus.taxRegAttachments);
	        }
		}
		
		/* 酒类销售许可证信息  */
		if(bus.liquorSalesLicense!=null){
			$("#LiquorSalesLicNo").val(bus.liquorSalesLicense.certificateNo);
			$("#liquorLegalName").val(bus.liquorSalesLicense.legalName);
			$("#liquorAddress").val(bus.liquorSalesLicense.address);
			$("#liquorBusinessType").val(bus.liquorSalesLicense.businessType);
			$("#liquorBusinessScope").val(bus.liquorSalesLicense.businessScope);
			$("#liquorStartTime").data("kendoDatePicker").value(bus.liquorSalesLicense.startTime);     		
			$("#liquorEndTime").data("kendoDatePicker").value(bus.liquorSalesLicense.endTime);      		
			$("#liquorIssuingAuthority").val(bus.liquorSalesLicense.issuingAuthority);
			// 获取酒类销售许可证图片
	        if(bus.liquorAttachments != null){
	        	business_unit.setLiquorAttachments(bus.liquorAttachments);
	        }
		}
	};
	
	fsn.setPage3Vaule=function(bus){
		if(bus.subDepartments.length!=0){			
			$("#gkjc_grid").data("kendoGrid").dataSource.data(bus.subDepartments);
		}
	};
	
	//获取当前企业基本信息
	fsn.getCurrentBusiness=function(){
		 	$.ajax({
		 		anysc:false,
				url:fsn.HTTP_PREFIX+"/business/getBusinessByOrg",
				type:"GET",
				success:function(returnValue){
					if(returnValue.result.status=="true"){
						fsn.businessId=returnValue.data.id;
						if(returnValue.data.wdaBackFlag){
							$(".gTop a").css("display","inline");
							$(".gTop a span").html(returnValue.data.wdaBackMsg);
						}
						fsn.setPage1Value(returnValue.data);
						fsn.setPage2Value(returnValue.data);
						fsn.setPage3Vaule(returnValue.data);
						/*隐藏营业执照号、组织机构、生产企业证图片的删除按钮*/
						$("#licenseAttachmentsListView button").hide();
						$("#qsAttachmentsListView button").hide();
						$("#orgAttachmentsListView button").hide();
					}
				}
		}); 
	 };
	
	 //刷新第二页的图片显示列表
	 fsn.flushPage2ListRes=function(bus){
		 // 获取酒类销售许可证图片
		 if(bus.liquorAttachments != null){
		 	business_unit.setLiquorAttachments(bus.liquorAttachments);
		 }
		 //获取税务登记图片
		 if(bus.taxRegAttachments != null){
		 	business_unit.setTaxRegAttachments(bus.taxRegAttachments);
		 }
		 // 获取生产许可证图片
		 if(bus.productionLicenses.length>0){
		 	business_unit.setQsAttachments(bus.productionLicenses[0].qsAttachments);
		 }
		 // 获取组织机构代码图片
		 if(bus.orgAttachments != null){
		 	business_unit.setOrgAttachments(bus.orgAttachments);
		 }
		 // 获取营业执照图片
		 if(bus.licAttachments != null){
		 	business_unit.setLicenseAttachments(bus.licAttachments);
		 }
	 };
	 
	 fsn.savePage2=function(){
		 $("#winMsg").html("正在保存当前页面的数据，请稍候....");
	     $("#k_window").data("kendoWindow").open().center();
		 var subBusiness = fsn.createInstancePage2();
		 $.ajax({
	            url: fsn.HTTP_PREFIX + "/business/business-unit",
	            type: "PUT",
	            dataType: "json",
	            contentType: "application/json; charset=utf-8",
	            data: JSON.stringify(subBusiness),
	            success: function(returnVal){
	            	$("#k_window").data("kendoWindow").close();
	            	if(returnVal.result.status == "true"){
	            		lims.initNotificationMes("当前页面信息保存成功！", true);
				 		//fsn.flushPage2ListRes(returnVal.data);
				 		fsn.goNext("step2","step3");
	                }else if(returnVal.result.offlineFlag){
	            		lims.initNotificationMes("用户登录信息已经失效，您需要从新登录！", false);
	            		fsn.goToLogin();
	            	}else {
	                	lims.initNotificationMes("当前页面信息保存失败！", false);
	                }
	            	
	            }
	        });
	 };
	 
	 fsn.savePage3=function(){
		 $("#winMsg").html("正在保存当前页面的数据，请稍候....");
	     $("#k_window").data("kendoWindow").open().center();
		 var subBusiness = fsn.createInstancePage3();
		 if(subBusiness){
			 $.ajax({
		            url: fsn.HTTP_PREFIX + "/business/business-unit",
		            type: "PUT",
		            dataType: "json",
		            contentType: "application/json; charset=utf-8",
		            data: JSON.stringify(subBusiness),
		            success: function(returnVal){
		            	$("#k_window").data("kendoWindow").close();
		            	if(returnVal.result.status == "true"){
		            		$("#gkjc_grid").data("kendoGrid").dataSource.data(returnVal.data.subDepartments);
		            		lims.initNotificationMes("当前页面信息保存成功！", true);					 		
		                }else if(returnVal.result.offlineFlag){
		            		lims.initNotificationMes("用户登录信息已经失效，您需要从新登录！", false);
		            		fsn.goToLogin();
		            	}else {
		                	lims.initNotificationMes("当前页面信息保存失败！", false);
		                }
		            }
		        });
		 }
		 
	 };
	 
	 fsn.uploadBusInfoPdf=function(){
		 $("#winMsg").html("正在上传pdf，请稍候....");
	     $("#k_window").data("kendoWindow").open().center();
		 var subBusiness = fsn.createInstancePage3();
		 if(subBusiness){
			 $.ajax({
		            url: fsn.HTTP_PREFIX + "/business/saveBusinessToPdf",
		            type: "PUT",
		            dataType: "json",
		            contentType: "application/json; charset=utf-8",
		            data: JSON.stringify(subBusiness),
		            success: function(returnVal){
		            	$("#k_window").data("kendoWindow").close();
		            	if(returnVal.result.success){
		            		$("#gkjc_grid").data("kendoGrid").dataSource.data(returnVal.data.subDepartments);
		            		$(".gTop a").css("display","none");
					 		lims.initNotificationMes("pdf上传成功", true);					 		
		                }else if(returnVal.result.offlineFlag){
		            		lims.initNotificationMes("用户登录信息已经失效，您需要从新登录！", false);
		            		fsn.goToLogin();
		            	}else {
					 		lims.initNotificationMes("pdf上传失败", false);
		                }
		            }
		        });
		 }
	 };
	 
	 fsn.viewListPdf=function(){
		 fsn.goNext("step3","listPdf_div");
		 fsn.getListBusUnitPdf();
		 $("#pdf_grid").data("kendoGrid").setDataSource(fsn.listPdfDS);
	 };
	 
	 fsn.initKendoWindow=function(winId,title,width,height,isVisible){
	    	$("#"+winId).kendoWindow({
				title:title,
				width:width,
				height:height,
				modal:true,
				visible:isVisible,
				actions:["Close"]
			});
	 };
	
	 fsn.closeUpdWin=function(){
		$("#uploadProDepWin").data("kendoWindow").close();
	 };
	 
	 fsn.openFillTablWindow=function(){
		$("#fillTablWindow").data("kendoWindow").open().center();
	 };
	 
	 fsn.buileAnimate = function buileAnimate(target,widthP,heightP){
		 if(heightP=="100%"){
			 $(".goTab li span").hide();
		 }else{
			 $(".goTab li span").show();
		 }
		$(target).animate({
			right:"0px",
			opacity:'1',
			width:widthP,
			height:heightP,
		});
	 };
	 
	 fsn.initialize();
});