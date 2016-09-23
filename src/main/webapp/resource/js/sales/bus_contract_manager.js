$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var contract = window.contract = window.contract || {};
	contract.model = {id:null};
	var httpPrefix = fsn.getHttpPrefix();
	var GRID_COLUMN = null;
	var GRID_DS = null;
	var DEL_WINDOW = null;
	var ADD_CONTRACT_WINDOW = null;
	var CONTRACT_GRID = null;
	var MSGWIN = null;
	var indexNo = 0;
	/* 合同附件支持的文件的格式 */
	var FILE_EXT = ".txt;.xml;.pdf;.doc;.docx;.xls;.xlsx;.ppt;.pptx;.zip;.rar;.gz;.jpg;.jpeg;.bmp;.png;.gif";
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-04-29
	 */
	function initComponent(){
		upload.buildGridByBoolbar("contractGrid",GRID_COLUMN,GRID_DS,"300","toolbar_template_contract");
		fsn.initConfirmWindow(delContractConfirmFun,null,"资料删除后将无法找回，确认需要删除此电子合同吗？","");
		DEL_WINDOW = $("#CONFIRM_COMMON_WIN").data("kendoWindow");
		MSGWIN = saleskendoUtil.initKendoWindow("messageWindow","310","65","保存状态",false,
				true,false,["Close"],"messageLabel","正在保存请稍候...");
		ADD_CONTRACT_WINDOW = saleskendoUtil.initKendoWindow("ADD_CONTRACT_WIN","530","","电子合同信息",
				false,true,false,["Close"],null,"");
		CONTRACT_GRID = $("#contractGrid").data("kendoGrid");
		$("#contractAdd_yes_btn").bind("click", saveContractFun);
		$("#contractAdd_no_btn").bind("click", closeAddContractWinFun);
		initMsgStyle();
	}
	
	GRID_COLUMN = [
		     { field:"id",title:fsn.l("id"),editable: false,width:20, template:function(model){
		    	 return ++indexNo;
		     }},
		     { field: "name", title:fsn.l("Contract Name"), width: 60},
		     { field: "updateTime", title:fsn.l("Update Time"), width: 60, template:function(model){
		    	 return fsn.formatGridDate(model.updateTime);
		     }},
		     { field: "remark", title:fsn.l("Note"), width: 100, template:function(model){
		    	 return '<span class="long_text">' + model.remark + '</span>';
		     }},
		     { command: [{name:"View",
		       	    text:"<span class='k-icon k-i-search'></span>" + fsn.l("View"),
		       	    click:function(e){
		       	    	e.preventDefault();
		       	    	var viewModel = CONTRACT_GRID.dataItem($(e.currentTarget).closest("tr"));
		       	    	viewModel.isView = true;
		       	    	if(viewModel.resource == null || viewModel.resource.length < 1 
		       	    			|| viewModel.resource[0].id == null){
		       	    		viewModel.resource = getContractRes(viewModel.guid);
		       	    	}
		       	    	contract.openAddContractWin(viewModel);
		       	    }
		     	},{name:"Edit",
		       	    text:"<span class='k-icon k-edit'></span>" + fsn.l("Edit"),
		       	    click:function(e){
		       	    	e.preventDefault();
		       	    	var editModel = CONTRACT_GRID.dataItem($(e.currentTarget).closest("tr"));
		       	    	editModel.isView = false;
		       	    	if(editModel.resource == null || editModel.resource.length < 1 
		       	    			|| editModel.resource[0].id == null){
		       	    		editModel.resource = getContractRes(editModel.guid);
		       	    	}
		       	    	contract.openAddContractWin(editModel);
		       	    }
		     	},{name:"Delete",
		   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
		   	    click:function(e){
		   	    	e.preventDefault();
		   	    	var delItem = CONTRACT_GRID.dataItem($(e.currentTarget).closest("tr"));
		   	    	contract.delId = delItem.id;
		   	    	if(DEL_WINDOW != null) {
		   	    		DEL_WINDOW.open().center();
		   	    	}
		   	    }
		      }], title: fsn.l("Operation"), width: 60}                     
		   ];
	
	/* 电子合同 grid 的dataSource */ 
	GRID_DS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = null;
            		if(options.filter) {
            			configure = filter.configure(options.filter);
            		}
            		indexNo = (options.page - 1) * options.pageSize;
            		return httpPrefix + "/sales/electdate/contract/getListContract/" + configure+"/" + options.page + "/" + options.pageSize;
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
                return returnValue.data;
            },
            total : function(returnValue) {       
                return returnValue.totals;
            }     
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	/**
	 * 通过全局guid 获取资源列表
	 * @author tangxin 2015-04-30
	 */
	function getContractRes(guid){
		if(!guid) return;
		var listRes = null;
		$.ajax({
	           url: httpPrefix + "/sales/electdate/getListResourceByGuid/" + guid ,
	           type: "GET",
	           async:false,
	           dataType: "json",
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  listRes = returnData.data;
	              } 
	          }
		});
		return listRes;
	}
	
	/**
	 * 下载合同附件
	 * @author tangxin 2015-04-30
	 */
	contract.downdLoad = function(resId) {
		 if(resId) {
			 var httpUrl = httpPrefix + "/resource/download/" + resId +"?type=sales";
			 lims.downloadByUrl(httpUrl);
		 } else {
			 fsn.initNotificationMes(fsn.l("The current resource can not find the download path") + "!", false);
		 }
	};
	
	/**
	 * 删除合同附件
	 * @author tangxin 2015-04-30
	 */
	contract.delContractRes = function(){
		if(contract.model != null){
			var res = contract.model.resource;
			if(res != null) {
				res.length = 0;
				$("#listView-div").html("");
				$("#uploadContractFile").removeAttr("disabled");
				$("#updateFile-div div.k-upload-button").removeAttr("disabled");
			}
		}
	};
	
	/**
	 * 合同资源编辑状态
	 * @author tangxin 2015-04-30
	 */
	function contractEditStatus(model){
		if(model != null && model.resource != null && model.resource.length > 0){
			var res = model.resource[0];//符合附件只能有一个
			$("#uploadContractFile").attr("disabled","disabled");
			$("#updateFile-div div.k-upload-button").attr("disabled","disabled");
			var str = '<a href="'+res.url+'" target="_black"><span>'+res.fileName+'</span></a>&nbsp;&nbsp;&nbsp;'+
		    '<button class="k-button" onclick="return contract.delContractRes();"><span class="k-icon k-cancel"></span>删除</button>';
			$("#listView-div").html(str);
		}
	}
	
	/**
	 * 打开信息电子合同窗口
	 * @author tangxin 2015-04-29
	 */
	contract.openAddContractWin = function(model){
		saleskendoUtil.clearAllMsg();
		$("#contractName").removeAttr("readonly");
		$("#remark").removeAttr("readonly");
		$("#updateFile-div").show();
		$(".div-contract-btn").show();
		$("span.ht_attr_span").show();
		$("#listView-div").html("");
		if(model == null) {
			contract.model = {id:null};
			$("#contractName").val("");
			$("#remark").val("");
		} else {
			$("#contractName").val(model.name);
			$("#remark").val(model.remark);
		}
		if(model != null && model.isView){
			$("#contractName").attr("readonly","readonly");
			$("#remark").attr("readonly","readonly");
			$("#updateFile-div").hide();
			$(".div-contract-btn").hide();
			$("span.ht_attr_span").hide();
			var res = model.resource;
			if(res != null && res.length > 0) {
				var nm = res[0].fileName;
				var aid = res[0].id;
				$("#listView-div").html('<span><a href="javascript:void(0);" onclick="return contract.downdLoad('+aid+')" title="点击下载附件">'+ nm +'</a>');
			}
		} else {
			contract.model = {
					id:(model == null ? null : model.id),
					resource:(model == null || model.resource == null) ? [] : model.resource
			};
			$("#updateFile-div").html("<input id='uploadContractFile' type='file' />");
			saleskendoUtil.buildUpload("uploadContractFile",contract.model.resource,"Upload Contract File",FILE_EXT,false,null,null);
			contractEditStatus(contract.model);
		}
		if(ADD_CONTRACT_WINDOW != null) {
			ADD_CONTRACT_WINDOW.open().center();
		}
	};
	
	/**
	 * 关闭新增电子合同窗口
	 * @author tangxin 2015-04-29
	 */
	function closeAddContractWinFun(){
		if(ADD_CONTRACT_WINDOW != null) {
			ADD_CONTRACT_WINDOW.close();
		}
	}
	
	/**
	 * 验证合同名称是否重复
	 * @author tangxin 2015-04-29
	 */
	function validateName(name){
		if(!name) return false;
		var vid = (contract.model == null ? -1 : contract.model.id);
		vid = (vid == null ? -1 : vid);
		var result = false;
		$.ajax({
	           url: httpPrefix + "/sales/electdate/contract/countName/" + name + "/" + vid,
	           type: "GET",
	           async:false,
	           dataType: "json",
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  result = (returnData.count > 0 ? false : true);
	              } 
	          }
		});
		return result;
	}
	
	function initMsgStyle(){
		var agent=navigator.userAgent;
	    if(agent.indexOf("Chrom") > -1){
	    	$("#v_name_span").addClass("v_span_chrom");
	    	$("#v_remark_span").addClass("v_span_chrom");
	    }else if(agent.indexOf("Firefox") > -1){
	    	$("#v_name_span").addClass("v_name_span_fire");
	    	$("#v_remark_span").addClass("v_remark_span_fire");
	    }
	}
	
	$("#contractName").blur(function(){
		var val = $(this).val().trim();
		if(val == ""){
			saleskendoUtil.msg("v_name_span","请填写合同名称！");
		}else{
			saleskendoUtil.clearMsg("v_name_span");
		}
	});
	
	$("#remark").blur(function(){
		var val = $(this).val().trim();
		if(val.length > 200){
			saleskendoUtil.msg("v_remark_span","备注信息不允许超过200字！");
		}else{
			saleskendoUtil.clearMsg("v_remark_span");
		}
	});
	
	/**
	 * 页面信息验证
	 * @author tangxin 2015-04-29
	 */
	function validate(){
		saleskendoUtil.clearAllMsg();
		var cName = $("#contractName").val().trim();
		var cNote = $("#remark").val().trim();
		if(cName == "") {
			saleskendoUtil.msg("v_name_span","请填写合同名称！");
			return false;
		}
		if(cName.length > 100) {
			saleskendoUtil.msg("v_name_span","合同名称不允许超过100字！");
			return false;
		}
		if(cNote.length > 200) {
			saleskendoUtil.msg("v_remark_span","备注信息不允许超过200字！");
			return false;
		}
		if(!validateName(cName)){
			saleskendoUtil.msg("v_name_span","合同名称已经存在，请重新输入！");
			return false;
		}
		var res = (contract.model != null ? contract.model.resource : []);
		if(res == null || res.length < 1){
			fsn.initNotificationMes(fsn.l("请上传电子合同附件！"), false);
			return false;
		}
		return true;
	}
	
	/**
	 * 封装合同Model
	 * @author tangxin 2015-04-29
	 */
	function createContractModel(){
		if(contract.model == null) {
			contract.model = {id:null};
		}
		contract.model.name = $("#contractName").val().trim();
		contract.model.remark = $("#remark").val().trim();
		var res = contract.model.resource;
		contract.model.resource = null;
		if(res != null && res.length >0 &&res[0].delStatus == 0) {
			contract.model.resource = (res != null ? res[0] : null);
		}
	}
	
	/**
	 * 保存电子合同信息
	 * @author tangxin 2015-04-29
	 */
	function saveContractFun(){
		if(!validate()){
			return;
		}
		createContractModel();
		MSGWIN.open().center();
		$.ajax({
	           url: httpPrefix + "/sales/electdate/contract/" + (contract.model.id == null ? "create" : "update"),
	           type: contract.model.id == null ? "POST" : "PUT",
	           dataType: "json",
	           data: JSON.stringify(contract.model),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   ADD_CONTRACT_WINDOW.close();
	        	   MSGWIN.close();
	              if(returnData.result.status == "true"){
	            	  fsn.initNotificationMes(fsn.l("保存成功！"), true);
	            	  GRID_DS.read();
	              }else {
	            	  fsn.initNotificationMes(fsn.l("保存失败！"), false);
	              }
	           },
	           error: function(e) {
	        	   fsn.initNotificationMes(fsn.l("服务器异常！"), false);
	           }
	    });
	}
	
	/**
	 * 确定删除电子合同的button事件
	 * @author tangxin 2015-04-29
	 */
	function delContractConfirmFun(){
		if(contract.delId == null) {
			return ;
		}
		$.ajax({
           url: httpPrefix + "/sales/electdate/contract/delContractById/" + contract.delId,
           type: "DELETE",
           dataType: "json",
           success: function(returnData) {
              if(returnData.result.status == "true"){
            	  fsn.initNotificationMes(fsn.l("删除成功！"), true);
            	  fsn.fleshGirdPageFun(GRID_DS);
              } else {
            	  fsn.initNotificationMes(fsn.l("删除失败！"), false);
              }
          }
		});
	}
	
	/* 初始化页面 */
	initComponent();
	
});