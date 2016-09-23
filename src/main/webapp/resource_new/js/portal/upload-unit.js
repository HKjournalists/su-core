$(function(){
	// 根据模块来创建命名空间
	(function(){
		var portal = fsn.portal = fsn.portal || {},
			upload = portal.upload = portal.upload || {};
			
		// 这个模块有三个上传model，分别是：unit、brand和poduct；
		(function(){
			var BusinessUnit = (function(){
				function fnBusinessUnit(){}
				
				/**
				 * 企业类型
				 */
				fnBusinessUnit.prototype.BusinessType = function (id, code, name) {
					return {"id":id, "code": code, "name": name};
				};
				
				/**
				 * 
				 */
				fnBusinessUnit.prototype.BusinessCategory = function (id, code, name) {
					return {"id":id, "code": code, "name": name};
				};
				
				/**
				 * 企业
				 */
				fnBusinessUnit.prototype.BusinessUnit = function(id, name, address, address2,
						logo, website, type, note, orgCode, area, category, personInCharge, idCardNo, regFund, economicNature,
						businessScope, postalCode, contact, telephone, mobile, tester, licenseNo, licenseUnit, licenseStart,
						licenseEnd, exportHygieneNo, exportHygieneRange, exportHygieneStart, exportHygieneEnd, iso9001Cert,
						iso9001Unit, iso9001Start, iso9001End, iso14000Cert, iso14000Unit, iso14000Start, iso14000End, haccpCert,
						haccpUnit, haccpStart, haccpEnd, otherCert, otherUnit, enterpriseScale, totalPopulation, techPopulation,
						floorSpace, structureArea, fixedAssets, circulatingFund, annualTotalOutputValue, annualSales, annualtax,
						annualProfit) {
					return {
						"id" : id,
						"name" : name,
						"address" : address,
						"address2" : address2,
						"logo" : logo,
						"website" : website,
						"type" : type,
						"note" : note,
						"orgCode" : orgCode,
						"area" : area,
						"category" : category,
						"personInCharge" : personInCharge,
						"idCardNo" : idCardNo,
						"regFund" : regFund,
						"economicNature" : economicNature,
						"businessScope" : businessScope,
						"postalCode" : postalCode,
						"contact" : contact,
						"telephone" : telephone,
						"mobile" : mobile,
						"tester" : tester,
						"licenseNo" : licenseNo,
						"licenseUnit" : licenseUnit,
						"licenseStart" : licenseStart,
						"licenseEnd" : licenseEnd,
						"exportHygieneNo" : exportHygieneNo,
						"exportHygieneRange" : exportHygieneRange,
						"exportHygieneStart" : exportHygieneStart,
						"exportHygieneEnd" : exportHygieneEnd,
						"iso9001Cert" : iso9001Cert,
						"iso9001Unit" : iso9001Unit,
						"iso9001Start" : iso9001Start,
						"iso9001End" : iso9001End,
						"iso14000Cert" : iso14000Cert,
						"iso14000Unit" : iso14000Unit,
						"iso14000Start" : iso14000Start,
						"iso14000End" : iso14000End,
						"haccpCert" : haccpCert,
						"haccpUnit" : haccpUnit,
						"haccpStart" : haccpStart,
						"haccpEnd" : haccpEnd,
						"otherCert" : otherCert,
						"otherUnit" : otherUnit,
						"enterpriseScale" : enterpriseScale,
						"totalPopulation" : totalPopulation,
						"techPopulation" : techPopulation,
						"floorSpace" : floorSpace,
						"structureArea" : structureArea,
						"fixedAssets" : fixedAssets,
						"circulatingFund" : circulatingFund,
						"annualTotalOutputValue" : annualTotalOutputValue,
						"annualSales" : annualSales,
						"annualtax" : annualtax,
						"annualProfit" : annualProfit
					};
				};
				
				/**
				 * url
				 * @param {String}
				 */
				fnBusinessUnit.prototype.getUrl = function (todo) {
					var url = {};
					/*
					url.create = portal.HTTP_PREFIX+"/portal/upload/unit";
					url.update = portal.HTTP_PREFIX+"/portal/upload/unit";
					url.search = portal.HTTP_PREFIX+"/portal/upload/preload";
					*/
					url.create = portal.HTTP_PREFIX+"/portal/businessunit";
					url.update = portal.HTTP_PREFIX+"/portal/businessunit";
					url.search = portal.HTTP_PREFIX+"/portal/businessunit/preload/";
					url.uploadimg = portal.HTTP_PREFIX+"/portal/file/img";
					url.searcharea = portal.CONTEXT_PATH + "/resource/js/area.min.json";
					return url[todo];
				};
				
				fnBusinessUnit.prototype.initAreaSelectLink = function(url,defaultArry) {
					$("#area").doselectmore({
						chckvalarry:defaultArry,
						dataresult : "arealist",
						namekey : "n",
						idkey: "id", // 本级ID
						cnamekey: "c", // 子集key
						url : url
						
					});
				};
				
				fnBusinessUnit.prototype.createBusinessType = function () {
					if($("#type").val() == "0"){
						return this.BusinessCategory(null, null, null);
					}else{
						return this.BusinessCategory($("#type").val(), null, null);
					}
				};
				
				fnBusinessUnit.prototype.createBusinessCategory = function () {
					if($("#category").val() == "0"){
						return this.BusinessCategory(null, null, null);
					}else{
						return this.BusinessCategory($("#category").val(), null, $("#category :selected").text());
					}
				};
				
				fnBusinessUnit.prototype.createBusinessUnit = function () {
					// 这里做检测
					var name,address,address2,orgCode,area,category,personInCharge,regFund,businessScope,
						licenseNo,licenseStart,totalPopulation,techPopulation;
					name=$("#name").val();
					if(portal.isBlank(name)) {
						$("#name").focus();
						alert("企业名称不能为空！");
						return false;
					}
					address=$("#address").val();
					if(portal.isBlank(address)) {
						$("#address").focus();
						alert("注册地址不能为空！");
						return false;
					}
					address2=$("#address2").val();
					if(portal.isBlank(address2)) {
						$("#address2").focus();
						alert("生产地址不能为空！");
						return false;
					}
					orgCode=$("#orgCode").val();
					if(portal.isBlank(orgCode)) {
						$("#orgCode").focus();
						alert("组织机构代码不能为空！");
						return false;
					}
					area=$("input[name=area]").val();
					if(portal.isBlank(area) || area == "请选择...") {
						$("#area0").focus();
						alert("请选择所属区域！");
						return false;
					}
					category=$("#category").val();
					if(portal.isBlank(category)) {
						$("#category").focus();
						alert("主体分类不能为空！");
						return false;
					}
					personInCharge=$("#personInCharge").val();
					if(portal.isBlank(personInCharge)) {
						$("#personInCharge").focus();
						alert("法定代表人不能为空！");
						return false;
					}
					regFund=$("#regFund").val();
					if(portal.isBlank(regFund)) {
						$("#regFund").focus();
						alert("注册资金不能为空！");
						return false;
					}
					businessScope=$("#businessScope").val();
					if(portal.isBlank(businessScope)) {
						$("#businessScope").focus();
						alert("经营范围不能为空！");
						return false;
					}
					licenseNo=$("#licenseNo").val();
					if(portal.isBlank(licenseNo)) {
						$("#licenseNo").focus();
						alert("营业执照注册号不能为空！");
						return false;
					}
					licenseStart=$("#licenseStart").val();
					if(portal.isBlank(licenseStart)) {
						$("#licenseStart").focus();
						alert("营业执照注册号发证日期不能为空！");
						return false;
					}
					totalPopulation=$("#totalPopulation").val();
					if(portal.isBlank(totalPopulation)) {
						$("#totalPopulation").focus();
						alert("企业总人数不能为空！");
						return false;
					}
					techPopulation=$("#techPopulation").val();
					if(portal.isBlank(techPopulation)) {
						$("#techPopulation").focus();
						alert("专业技术人员数不能为空！");
						return false;
					}
					var _BusinessUnit = this;
					var _bu = _BusinessUnit.BusinessUnit($("#id").val(), name, address, address2,
							$("#logoForm").attr("imgurl"), $("#website").val(), _BusinessUnit.createBusinessType(),
							$("#note").val(), orgCode, area,_BusinessUnit.createBusinessCategory(),  personInCharge,
							$("#idCardNo").val(), regFund, $("#economicNature").val(), businessScope, $("#postalCode").val(), 
							$("#contact").val(), $("#telephone").val(), $("#mobile").val(), $("#tester").val(), licenseNo, $("#licenseUnit").val(),
							licenseStart, $("#licenseEnd").val(), $("#exportHygieneNo").val(), $("#exportHygieneRange").val(), $("#exportHygieneStart").val(),
							$("#exportHygieneEnd").val(), $('input[name="iso9001Cert"]:checked').val(), $("#iso9001Unit").val(), $("#iso9001Start").val(),
							$("#iso9001End").val(), $('input[name="iso14000Cert"]:checked').val(), $("#iso14000Unit").val(), $("#iso14000Start").val(),
							$("#iso14000End").val(), $('input[name="haccpCert"]:checked').val(), $("#haccpUnit").val(), $("#haccpStart").val(), $("#haccpEnd").val(),
							$("#otherCert").val(), $("#otherUnit").val(), $("#enterpriseScale").val(), totalPopulation, techPopulation, $("#floorSpace").val(), $("#structureArea").val(),
							$("#fixedAssets").val(), $("#circulatingFund").val(), $("#annual-total-outputValue").val(), $("#annualSales").val(), $("#annualTax").val(), $("#annualProfit").val()
							);
					return _bu;
				};
				
				fnBusinessUnit.prototype.saveData = function(){
					var _BusinessUnit = this, 
					_bu = _BusinessUnit.createBusinessUnit();
				
					if(!_bu){
						return false;
					}
				};
				
				/**
				 * 提交动作调用的方法
				 */
				fnBusinessUnit.prototype.upload = function () {
					var mws = this,_bu = mws.saveData();
					
					$.ajax({
						url: _BusinessUnit.getUrl("create"),
						type: "POST",
						datatype: "json",
						data: JSON.stringify(_bu),
						contentType:'application/json;charset=UTF-8', 
						success: function(data){
							alert(data.RESTResult.message);
							var _href = window.location.href;
							if (_href.indexOf("?") != -1) {
								window.location.href = _href.substring(0, _href.indexOf("?"));
							}else{
								mws.cacheModel = null;
								mws.reset();
							}
						}
					});
				};
				
				/**
				 * createParentDropdown
				 */
				fnBusinessUnit.prototype.createDropdown = function (data, $drop, $dom) {
					var dd = $drop.kendoDropDownList({
						dataTextField: "name",
			            dataValueField: "id",
			            dataSource: data,
			           	change : function(){
			           		$dom.val(this.value());
			            }
			      	  }).data("kendoDropDownList");
					return dd;
				};
				
				/**
				 * 编辑的时候，根据businessUnit对象显示到页面
				 */
				fnBusinessUnit.prototype.writeBusinessUnit = function (data) {
					// 先初始化type
					var dd = this.createDropdown(data.businessTypes, $("#typeDropdown"), $("#type"));
					var cc = this.createDropdown(data.businessCategories, $("#categoryDropdown"), $("#category"));
					if(data.unit!=null){
						$.each(data.unit, function (k, v) {
							if (!portal.isBlank(v)) {
								switch(k){
								case "type" :
								case "category" :
									$("#"+k).val(v.id);
									break;
								case "iso9001Cert" :
									var radio_o=$("input[name="+iso9001Cert+"]");
									for(var i=0;i<radio_o.length;i++){
										radio_o[i].value==v?radio_o[i].checked=true:radio_o[i].checked=false;
									}
									break;
								case "iso14000Cert" :
									var radio_o=$("input[name="+iso14000Cert+"]");
									for(var i=0;i<radio_o.length;i++){
										radio_o[i].value==v?radio_o[i].checked=true:radio_o[i].checked=false;
									}
									break;
								case "haccpCert" :
									var radio_o=$("input[name="+haccpCert+"]");
									for(var i=0;i<radio_o.length;i++){
										radio_o[i].value==v?radio_o[i].checked=true:radio_o[i].checked=false;
									}
									break;
								case "economicNature" :
								case "enterpriseScale" :
									//$("#"+k).find("option[value="+v+"]").attr("selected",true);
									break;
								default :
									$("#"+k).val(v);
									break;
								}
							}
						});
						// logo图片
						var _logo_url = data.unit.logo;
						if (!portal.isBlank(_logo_url)) {
							var _logo_url_name = _logo_url.split("/").pop();
							$("#logoForm").attr("imgurl", _logo_url)
								.siblings("img.upload-img-preview").attr("src",_logo_url).css("display","block")
								.siblings("span.upload-img-name").find("em").text(_logo_url_name);
						}
					}
					
					var $unitType = $("#type");
					if($unitType.val() != ""){
	            		dd.value($unitType.val());
	            	}else{
	            		$unitType.val(dd.value());
	            	}

					var $unitCategory = $("#category");
					if($unitCategory.val() != ""){
	            		cc.value($unitCategory.val());
	            	}else{
	            		$unitCategory.val(cc.value());
	            	}
				};
				
				/**
				 * 展示接口,根据BusinessUnit的ID值或缺数据
				 */
				fnBusinessUnit.prototype.show = function (sid) {
					var _id = sid||portal.getSearchParam(window.location.search, "id"), _BusinessUnit = this,
					_sid=_id==""?-1:_id;
					$.ajax({
						//url: _BusinessUnit.getUrl("search") + "?id=" + _id + "&type=unit",
						url: _BusinessUnit.getUrl("search") + _sid,
						type: "GET",
						datatype: "json",
						success: function (data) {
							var _area="";
							if(data.RESTResult.status == 1) {
								_BusinessUnit.writeBusinessUnit(data.RESTResult.data);
								if(data.RESTResult.data.unit!=null){
									_area=data.RESTResult.data.unit.area;
								}
							} else {
								alert(data.RESTResult.message);
							}
							_BusinessUnit.getArea(_area);
						}
					});
				};
				
				/**
				 * 请求区域数据
				 */
				fnBusinessUnit.prototype.getArea=function(area){
					var _BusinessUnit=this,array=_BusinessUnit.getAreaArray(area);
					_BusinessUnit.initAreaSelectLink(_BusinessUnit.getUrl("searcharea"), array);
				};
				
				/**
				 * 重置表单
				 */
				fnBusinessUnit.prototype.reset = function () {
					$("input").each(function(){
						this.value = this.defaultValue;
					});
					$("#logoForm").siblings("img.upload-img-preview").attr("src","").css("display","none")
					.siblings("span.upload-img-name").find("em").text("请选择图片");
				};
				
				/**
				 * 文件上传，依赖Kendo
				 */
				fnBusinessUnit.prototype.fileUpload = {};
				// 选中文件后执行
				fnBusinessUnit.prototype.fileUpload.onSelect = function (e) {
					// 判断未见类型
				};
				// 上传时执行
				fnBusinessUnit.prototype.fileUpload.onUpload = function (e) {
					console.log(e);
				};
				// 上传成功后执行
				fnBusinessUnit.prototype.fileUpload.onSuccess = function (e) {
					
					var data = e.response,
						dom = e.sender.element[0],
						_imgurl;
					
					if(data.RESTResult.status == 0){
						alert(data.RESTResult.message);
					}else{
						// dom.imgurl = data.RESTResult.message;
						_imgurl = dom.imgurl;
						if (_imgurl==null || _imgurl == "") {
							dom.imgurl = data.RESTResult.message;
						} else {
							dom.imgurl = _imgurl+"|"+data.RESTResult.message;
						}
					}
				};
				// 点击删除时执行
				fnBusinessUnit.prototype.fileUpload.onRemove = function (e) {
					console.log(e);
					// 设置删除参数
					var _async = e.sender.options.async, imgurl = e.sender.element[0].imgurl;
					if (imgurl){
						_async.removeUrl = _async.removeUrl + "?f=" + imgurl;
					}
				};
				// 请求失败后执行
				fnBusinessUnit.prototype.fileUpload.onError = function (e) {
					
				};
				// 调用kendo API初始化file控件
				fnBusinessUnit.prototype.fileUpload.initFile = function ($file) {
					var mws = this;
					var _$file = $file.kendoUpload({
                    	multiple: false, /* 是否支持文件多选 */
                        async: {
                        	/* 保存文件url */
                            saveUrl: portal.HTTP_PREFIX+"/portal/upload/imgs",
                            /* 删除请求url */
                            removeUrl: portal.HTTP_PREFIX+"/portal/upload/img",
                            /* 删除请求type */
                            removeVerb: "DELETE",
                            /* 是否开启选中文件后立即上传功能 */
                            autoUpload: true
                        },
                        localization: {
                        	cacel: "取消",
                        	retry: "重试",
                        	remove: "删除",
                        	select: "请选择图片...",
                        },
                        error: mws.onError,
                        remove: mws.onRemove,
                        select: mws.onSelect,
                        success: mws.onSuccess,
                        upload: mws.onUpload
                    });
					// 这里调整file控件的显示
                    _$file.next().parent().css("width", "120px");
                    return _$file;
				};
				
				/**
				 * 页面初始化
				 */
				fnBusinessUnit.prototype.init = function () {
					// 在url中如果有id键，则是编辑（修改）
					var _unitid = fsn.getSearchParam(window.location.search, "id"), _BusinessUnit = this;
					_BusinessUnit.show(_unitid);
					
					$("#panelbar").kendoPanelBar({
			    	    animation: {
			    	        open : { effects: "fadeIn" }
			    	    },
			    	    expandMode: "single"
			    	});
					
					//验证手机号码
					$("#contact").on({
						//验证手机号码
						blur:function(){
							var _isphone = portal.checkIsPhone($(this));
							if(_isphone){
								return true;
							}else{
								alert("联系方式格式不正确");
								return false;
							}
						}
					});
					
					// save事件\reset事件
					$(".k-button").on("click", function(){
						var _this = $(this), _span = _this.find("span.ui-button-text");
						switch(_span.attr("data-fsn-text")) {
						case 'Save':
							_BusinessUnit.saveData();
							break;
						case 'Upload':
							_BusinessUnit.upload();
							break;
						default: break;
						}
					});
					// 图片上传，依赖KENDO
					//_BusinessUnit.fileUpload.initFile($("#logo"));
					
					// 图片ajax上传，依赖jquery.form.js
					$('#logoForm').ajaxForm(function(data) { 
						if(data.RESTResult.status == 0){
							alert(data.RESTResult.message);
							$('#logoForm').attr("imgurl", "");
						}else{
							// 这一部分的内容是要在提交数据的时候发送给service的
							var _$this = $('#logoForm'),
								_res = data.RESTResult.data/* 返回的数据将是一个{"name":"","path":""} */,
								_imgurl;
							if (_res) {
								_imgurl = _$this.attr("imgurl");
								if (portal.isBlank(_imgurl)) {
									_$this.attr("imgurl", _res.name);
								} else {
									_$this.attr("imgurl", "|"+_res.name);
								}
								// 这一部分的数据是图片的[伪]预览
								_$this.siblings("img.upload-img-preview").attr("src", _res.path)
									.css({"display":"block"})
									.siblings("span.upload-img-name").find("em").text(_res.name);
							}
						}
						// alert(data.RESTResult.message);
		            });
					$("#logo").on("change", function(){
						
						var _$file = $(this);
						
						if(portal.isBlank(_$file.val())){
							_$file.parent().siblings("img.upload-img-preview").attr("src","").css("display","none")
								.siblings("span.upload-img-name").find("em").text("请选择图片");
							return false;
						} else {
							var _bImg = portal.isImg(_$file.val());
							
							// 检查选中文件是否是图片
							if(!_bImg){
								alert("请选择JPG、PNG等格式的图片文件！");
								return false;
							}
							
							// 如果需要限制文件的尺寸的话,
							// var _bFileSize = portal.checkFileSize(_$file);
							
							// 初始化form
							var _$form = $('#logoForm');
							$('#imgForm').attr("imgurl", "");
							_$form[0].action = _BusinessUnit.getUrl("uploadimg");
							_$form.submit();
						}
					});
				};
				fnBusinessUnit.prototype.getAreaArray=function(area){
					var _array=[],len=area==null?0:area.length/2;
					for(var i=0;i<len;i++){
						_array[i]=area.substring(0,i*2+2);
					}
					return _array;
				};
				
				return fnBusinessUnit;
			})();
			this.BusinessUnit = new BusinessUnit();
		}).call(upload);
		
		upload.BusinessUnit.init();
	})();
});