$(document).ready(function() {
					var fsn = window.fsn = window.fsn || {};
					var resorts = window.fsn.resorts = window.fsn.resorts || {};
					var portal = fsn.portal = fsn.portal || {}; // portal命名空间
					portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
					portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
					resorts.id = null;
					resorts.name = null;
					resorts.edit_id = null;
					resorts.aryLogoAttachments = new Array();
					resorts.aryInfoAttachments = new Array();

					try {
						resorts.edit_id = $.cookie("user_0_edit_resort").id;
						$.cookie("user_0_edit_resort", JSON.stringify({}), {
							path : '/'
						});
					} catch (e) {
					}
					
					resorts.initialize = function() {
						
						resorts.dropDown("resortType");
						
						/* 初始化上传控件 */
						 $("#upload_resortLogo_div").html("<input id='upload_resortLogo' type='file' />");
						 resorts.buildUpload("upload_resortLogo", resorts.aryLogoAttachments);
						 
						 $("#upload_resortInfo_div").html("<input id='upload_resortInfo' type='file' />");
						 resorts.buildUpload("upload_resortInfo", resorts.aryInfoAttachments);
						
						 resorts.hidden(); 
						 
						// 弹窗
						resorts.initKendoWindow("sure", "确定录入景区吗？", "300px","60px", false);

						// 失去焦点验证手机号码格式和地址格式
						$("#reserveTelephone").bind("blur",resorts.validaReserveTelephone);
						$("#resortAddress").bind("blur",resorts.validaResortAddress);
						
					};

					
					resorts.hidden = function(){
						if(resorts.edit_id != null){
							 $("#save").hide();
							 $("#edit").show();
							 resorts.getResortById(resorts.edit_id);
						 }else{
							 $("#edit").hide();
							 $("#clear").hide();
						 }
					};
					
					
					/*
					 * 打开保存弹窗
					 */
					resorts.save = function() {
						$("#sure").data("kendoWindow").open().center();
					};

					/*
					 * 关闭弹窗
					 */
					resorts.cancel = function() {
						$("#sure").data("kendoWindow").close();
					};

					/*
					 * 后台保存景区
					 */
					resorts.saveResort = function() {
						if (!resorts.validaResort()) {
							return;
						}
						var resort = resorts.createResort();
						$.ajax({
							url : portal.HTTP_PREFIX + "/resorts/save",
							type : "POST",
							dataType : "json",
							contentType : "application/json; charset=utf-8",
							data : JSON.stringify(resort),
							success : function(returnVal) {
								if (returnVal.status == true) {
									lims.initNotificationMes("景区录入成功", true);
									window.location.href = "resorts.html";
								} else {
									lims.initNotificationMes("景区录入失败", false);
								}
								resorts.cancel();
							}
						});
					};

					
					//根据ID获取景区
					resorts.getResortById = function(id){
						if(id != null && id != ""){
							TZID = id;
						}
						$.ajax({
							url:fsn.getHttpPrefix() + "/resorts/getResortById/"+TZID,
							type:"GET",
							dataType: "json",
							async:false,
							success:function(returnValue){
								if (returnValue.status == true) {
									var resortsData = returnValue.resorts;
									resorts.setResorts(resortsData);
								} 
							}
						});
					};
					
					/*
					 * 给页面赋值
					 */
					resorts.setResorts = function(data){
						var mws = this, _resort = data;
						mws.cacheModel = _resort; 
						if (!portal.isBlank(_resort)) {   
							$("#name").val(_resort.name);
							$("#resortLevel").val(_resort.level);
							$("#resortPrice").val(_resort.resortPrice);
							$("#reserveTelephone").val(_resort.reserveTelephone);
							$("#resortType").data("kendoDropDownList").value(_resort.resortType);
							var address = _resort.resortAddress;
							var resortAddress = address.split("--");
							$("#resortAddress").val(resortAddress[0]);
							$("#streetAddress").val(resortAddress[1]);
							$("#longOrlat").val(_resort.rank);
							$("#resortInfo").val(_resort.resortInfo);
							$("#placeName").val(_resort.placeName);
							
							resorts.setLogoAttachments("upload_resortLogo",_resort.logoAttachments);
							resorts.setInfoAttachments("upload_resortInfo",_resort.infoAttachments);
						}
					};
					
					
					/* 加载logo图片 */
					resorts.setLogoAttachments = function(id,logoAttachments){
						
						resorts.aryLogoAttachments.length = 0;
						 if(logoAttachments.length>0){
							 for(var i=0;i<logoAttachments.length;i++){
								 resorts.aryLogoAttachments.push(logoAttachments[i]);
							 }
						 }
						resorts.loadIMAG(id,logoAttachments);
						//resorts.aryLogoAttachments = logoAttachments;
					 };
					 
					 /* 加载景区简介图片 */
					 resorts.setInfoAttachments = function(id,InfoAttachments){
						 resorts.aryInfoAttachments.length = 0;
						 if(InfoAttachments.length>0){
							 for(var i=0;i<InfoAttachments.length;i++){
								 resorts.aryInfoAttachments.push(InfoAttachments[i]);
							 }
						 }
						 //resorts.aryInfoAttachments = InfoAttachments;
						 resorts.loadIMAG(id,InfoAttachments);
					 };
					 
					 
					 /*
					  * 页面显示图片
					  */
					 resorts.loadIMAG = function(id,attachments){
						 console.log(attachments);
						 var imgData = attachments;
						 var img = "";
						 for(var k=0;k<imgData.length;k++){
							 var url = imgData[k].url;
							 var  fileUrl = url;
							 if(url == null){
								 fileUrl = imgData[k].file;
								 url = "data:"+imgData[k].type.rtName+";base64,"+imgData[k].file+"";
							 }
							 img+="<div id='"+id+"_img_"+k+"'  style='position: relative;width: 128px;height: 128px;float: left;display: inline'>";
							 img+="<img id='"+id+"_"+k+"' src='"+url+"' style='width: 128px;height:128px;' onclick='fsn.resorts.amplification(this.src)'>";
							 img+="<div class=deleteBtn onclick=fsn.resorts.delSelectMethodsImg('"+id+"_"+k+"','"+fileUrl+"','"+id+"')>x";
							 img+="</div>";
							 img+="</div>";
							 $("#"+id+"_img").html(img);
							 
							 $("#"+id+"_div").find("ul").remove();
							 $("#"+id+"_div").find("em").remove();
							 $("#"+id+"_div").find("strong ").remove();
						 }
					 };
					 
					
					 /*
					  * 保存编辑修改后的resort
					  */
					 resorts.editResort = function(){
						 var sure = resorts.validaResort();
						 if(sure){
							 var editResort = resorts.createResort();
							 $.ajax({
									url : portal.HTTP_PREFIX + "/resorts/edit/" + resorts.edit_id,
									type : "POST",
									dataType : "json",
									contentType : "application/json; charset=utf-8",
									data : JSON.stringify(editResort),
									success : function(returnVal) {
										if (returnVal.status == true) {
											lims.initNotificationMes("编辑成功", true);
											window.location.href = "resorts.html";
										} else {
											lims.initNotificationMes("编辑失败", false);
										}
									}
								});
							 }else{
								 return;
							 }
					 };
					 
					
					/*
					 * 验证保存对象是否完整
					 */
					resorts.validaResort = function() {
						if ($("#name").val().trim() == "") {
							lims.initNotificationMes("景区名称不能为空", false);
							return false;
						}
						if ($("#resortLevel").val().trim() == "") {
							lims.initNotificationMes("景区等级不能为空", false);
							return false;
						}
						if ($("#resortPrice").val().trim() == "") {
							lims.initNotificationMes("景区价格不能为空", false);
							return false;
						}
						if ($("#reserveTelephone").val().trim() == "") {
							lims.initNotificationMes("订票电话不能为空", false);
							return false;
						}
						if ($("#resortType").val().trim() == ""
								|| $("#resortType").val().trim() == "请选择...") {
							lims.initNotificationMes("景区类型不能为空", false);
							return false;
						}
						if ($("#resortAddress").val().trim() == "") {
							lims.initNotificationMes("景区地址不能为空", false);
							return false;
						}
						if ($("#streetAddress").val().trim() == "") {
							lims.initNotificationMes("景区详细地址不能为空", false);
							return false;
						}
						if ($("#longOrlat").val().trim() == "") {
							lims.initNotificationMes("景区经纬度不能为空", false);
							return false;
						}
						if ($("#resortInfo").val().trim() == "") {
							lims.initNotificationMes("景区简介不能为空", false);
							return false;
						}
						if ($("#placeName").val().trim() == "") {
							lims.initNotificationMes("经纬度的覆盖物不能为空", false);
							return false;
						}
						if (resorts.aryLogoAttachments.length == 0) {
							lims.initNotificationMes("请上传景区logo图片！", false);
							return false;
						}
						if (resorts.aryInfoAttachments.length == 0) {
							lims.initNotificationMes("请上传景区简介图片！", false);
							return false;
						}
						return true;

					};

					/*
					 * 保存成功，清空页面
					 */
					resorts.clear = function() {
						$("#name").val("");
						$("#resortLevel").val("");
						$("#resortPrice").val("");
						$("#reserveTelephone").val("");
						$("#resortType").data("kendoDropDownList").value("");
						$("#longOrlat").val("");
						$("#placeName").val("");
						$("#resortInfo").val("");
						$("#resortAddress").val("");
						$("#streetAddress").val("");
					};

					/*
					 * 创建页面保存对象
					 */
					resorts.createResort = function() {
						var lat = "";
						var lng = "";
						var saveResort = {
							name : $("#name").val().trim(),
							level : $("#resortLevel").val().trim(),
							resortPrice : $("#resortPrice").val().trim(),
							reserveTelephone : $("#reserveTelephone").val()
									.trim(),
							resortType : $("#resortType").val().trim(),
							rank : $("#longOrlat").val().trim(),
							resortInfo : $("#resortInfo").val().trim(),
							resortAddress : $("#resortAddress").val().trim()
									+ "--" + $("#streetAddress").val().trim(),
							longitude : lng,
							latitude : lat,
							placeName : $("#placeName").val().trim(),
							logoAttachments : resorts.aryLogoAttachments,
							infoAttachments : resorts.aryInfoAttachments
						};
						return saveResort;
					};

					/*
					 * 初始化弹框方法
					 */
					resorts.initKendoWindow = function(winId, title, width,
							height, isVisible) {
						$("#" + winId).kendoWindow({
							title : title,
							width : width,
							height : height,
							modal : true,
							visible : isVisible,
							actions : [ "Close" ]
						});
					};

					/* 按控件id初始化上传按钮的显示文字 */
					resorts.getShowName = function(id) {
						if (id == "upload_resortLogo") {
							return lims.l("上传logo图片");
						} else {
							return lims.l("上传简介图片");
						}
					};

					/*
					 * 景区类型
					 */
					resorts.dropDown = function(id) {
						$("#" + id).kendoDropDownList(
								{
									dataSource : {
										data : [ "风景区", "文博院馆", "寺庙观堂",
												"旅游度假区", "自然保护区", "主题公园",
												"森林公园", "地质公园", "游乐园", "动物园",
												"植物园", "工农业旅游", "科教文化", "其他", ]
									},
									optionLabel : "请选择...",
									animation : {
										open : {
											effects : "zoom:in",
											duration : 300
										}
									}
								});
					};

					/* 验证手机号码是否正常 */
					resorts.validaReserveTelephone = function() {
						// var
						// isMobile=/^(?:(?:0\d{2,3})-)?(?:\d{7,8})(-(?:\d{3,}))?$/;
						// // 固定电话
						var isPhone = /^(^(\d{3,4}-)?\d{7,8})$|(^(0|86|17951)?(13[0-9]|15[0123456789]|18[0123456789]|14[57])[0-9]{8}$)/;
						if (($("#reserveTelephone").val().trim()) != "") {
							if (!(($("#reserveTelephone").val().trim())
									.match(isPhone))) {
								resorts.validateInputStyle("reserveTelephone",
										"订票电话不正确！", false);
								return false;
							} else {
								resorts.validateInputStyle("reserveTelephone",
										"", true);
							}
						} else {
							lims.initNotificationMes("订票电话不能为空", false);
						}
						return true;
					};


					/*
					 * 验证景区门票填写没有
					 */
					resorts.validaResortPrice = function() {
						var _keyword = $("#resortPrice").val().trim();
						if (_keyword != undefined && _keyword != '') {
							var index = _keyword.indexOf("0");
							var length = _keyword.length;
							if (index == 0 && length > 1) {/* 0开头的数字串 */
								var reg = /^[0]{1}[.]{1}[0-9]{1,2}$/;
								if (!reg.test(_keyword)) {
									lims.initNotificationMes(
											'请输入正确的价格格式（如：xx.xx）！', false);
									return false;
								} else {
									return true;
								}
								;
							} else {/* 非0开头的数字 */
								var reg = /^[1-9]{1}[0-9]{0,10}[.]{0,1}[0-9]{0,2}$/;
								if (!reg.test(_keyword)) {
									lims.initNotificationMes(
											'请输入正确的价格格式（如果：xx.xx/xxx）！', false);
									return false;
								} else {
									return true;
								}
								;
							}
							;
						} else {
							lims.initNotificationMes("景区价格不能为空", false);
						}

					};


					/*
					 * 验证省市县填写没有
					 */
					resorts.validaResortAddress = function() {
						var text = $("#resortAddress").val().trim();
						if (text == "") {
							return;
						} // 若为空则不进行校验
						var strs = new Array();
						strs = text.split("-"); // 分割字符串
						if (strs.length < 2 || strs.length > 3) { // 判断字符串数是否为3位
							lims.initNotificationMes(
									'格式只能为：</br>【省-市】或【省-市-区（县）】</br>请重新填写！',
									false);
							$("#" + id).val("");
							return;
						} else {
							// 判断每个字符串是否有为空
							for (var i = 0; i < strs.length; i++) {
								if (strs[i].trim() == "") {
									lims.initNotificationMes(
											'省、市、区（县）不能为空</br>请重新填写！', false);
									$("#" + id).val("");
									return;
								}
							}
							var reg = /^[\u4e00-\u9fa5]{1,10}$/g; // 校验是否全为汉字
							var str = "";
							if (strs.length < 2) {
								str = strs[0].trim() + strs[1].trim()
										+ strs[2].trim();
							} else {
								str = strs[0].trim() + strs[1].trim();
							}
							if (!reg.test(str)) {
								lims.initNotificationMes('只能输入汉字字符，请重新填写！',
										false);
								$("#" + id).val("");
								return;
							}
						}
					};


					/* 验证图片 */
					resorts.validateCommonPhoto = function(status) {
						if (status == undefined || status == 0) {
							if (resorts.aryLogoAttachments.length < 1) {
								lims.initNotificationMes('请上传景区logo图片！', false);
								resorts.wrong("upload_resortLogo", "select");
								return false;
							}
						}
						if (status == undefined || status == 1) {
							if (resorts.aryLicenseAttachments.length < 1) {
								lims.initNotificationMes('请上传景区简介图片！', false);
								resorts.wrong("upload_resortInfo", "select");
								return false;
							}
						}
						return true;
					};

					/*
					 * 验证某个字段时，添加或取消输入框的样式
					 * formId:输入框的id，msg：提示信息，valiResult：验证结果，true或false
					 */
					resorts.validateInputStyle = function(formId, msg,
							valiResult) {
						if (!valiResult) {
							var parentDiv = $("#" + formId).parent();
							parentDiv.find("span").remove();
							var span = $("<span class='fieldFormatStyle'>"
									+ msg + "</span>");
							parentDiv.append(span);
						} else {
							var span = $("#" + formId).next();
							if (span.length > 0) {
								span.remove();
							}
						}
					};

					/*
					 * 给经纬度设置值
					 */
					resorts.setRanks = function(data) {
						var lat = data.lat;
						var lng = data.lng;
						var ranks = lat + "--" + lng;
						$("#ranks").val(ranks);
					};

					/* 初始化上传控件 */
					resorts.buildUpload = function(id, attachments) {
						/* 初始化上传控件 */
						$("#" + id)
								.kendoUpload(
										{
											async : {
												saveUrl : portal.HTTP_PREFIX
														+ "/resource/kendoUI/addResources/product",
												removeUrl : portal.HTTP_PREFIX
														+ "/resource/kendoUI/removeResources",
												autoUpload : true,
												removeVerb : "POST",
												removeField : "fileNames",
												saveField : "attachments"
											},
											localization : {
												select : resorts
														.getShowName(id),
												retry : lims.l("retry",
														'upload'),
												remove : lims.l("remove",
														'upload'),
												cancel : lims.l("cancel",
														'upload'),
												dropFilesHere : lims
														.l(
																"drop files here to upload",
																'upload'),
												statusFailed : lims.l("failed",
														'upload'),
												statusUploaded : lims.l(
														"uploaded", 'upload'),
												statusUploading : lims.l(
														"uploading", 'upload'),
												uploadSelectedFiles : lims.l(
														"Upload files",
														'upload')
											},
											multiple : true,
											upload : function(e) {
												var files = e.files;
												if (attachments.length
														+ e.files.length > 5) {
													return false;
												}
												$
														.each(
																files,
																function() {
																	if (this.size > 1040000) {
																		lims
																				.initNotificationMes(
																						'单个文件的大小不能超过1M！',
																						false);
																		e
																				.preventDefault();
																		return;
																	}
																	var extension = this.extension
																			.toLowerCase();
																	if (extension != ".png"
																			&& extension != ".bmp"
																			&& extension != ".jpeg"
																			&& extension != ".jpg") {
																		lims
																				.initNotificationMes(
																						'图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!',
																						false);
																		e
																				.preventDefault();
																	}
																});
											},
											success : function(e) {
												if (e.response.typeEror) {
													lims.initNotificationMes(
															"必须上传图片格式", false);
													return;
												}
												if (e.response.morSize) {
													lims.initNotificationMes(
															"上传材料大小必须小1M",
															false);
													return;
												}
												if (e.operation == "upload") {
													attachments.push(e.response.results[0]);
													var imgData = attachments;
													var img = "";
													for(var k=0;k<imgData.length;k++){
														var url = imgData[k].url;
														var  fileUrl = url;
														if(url == null){
															fileUrl = imgData[k].file;
															url = "data:"+imgData[k].type.rtName+";base64,"+imgData[k].file+"";
														}
														img+="<div id='"+id+"_img_"+k+"'  style='position: relative;width: 128px;height: 128px;float: left;display: inline'>";
														img+="<img id='"+id+"_"+k+"' src='"+url+"' style='width: 128px;height:128px;' onclick='fsn.resorts.amplification(this.src)'>";
														img+="<div class=deleteBtn onclick=fsn.resorts.delSelectMethodsImg('"+id+"_"+k+"','"+fileUrl+"','"+id+"')>x";
														img+="</div>";
														img+="</div>";
														$("#"+id+"_img").html(img);
														
														$("#"+id+"_div").find("ul").remove();
														$("#"+id+"_div").find("em").remove();
														$("#"+id+"_div").find("strong ").remove();
													}
													console.log(attachments);
													// $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png
													// .bmp .jpeg .jpg )");
												} else if (e.operation == "remove") {
													for (var i = 0; i < attachments.length; i++) {
														if (attachments[i].name == e.files[0].name) {
															while ((i + 1) < attachments.length) {
																attachments[i] = attachments[i + 1];
																i++;
															}
															attachments.pop();
															break;
														}
													}
													console.log(attachments);
												}
											},
											remove : function(e) {
												// $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png
												// .bmp .jpeg .jpg )");
											},
											error : function(e) {
												// $("#"+msg).html(lims.l("An
												// exception occurred while
												// uploading the file! Please
												// try again later ..."));
											}
										});

					};
					
					
					resorts.amplification = function(url){
						window.open ( url, "_blank" );
					};
					
					
					resorts.getMap = function(){
						 if( data.mapProductAddrList.length>0){
                             //经度
                             $("#longitude").val(data.mapProductAddrList[0].lng);
                             //纬度
                             $("#latitude").val(data.mapProductAddrList[0].lat);
                              //经度--纬度
                             $("#longOrlat").val(data.mapProductAddrList[0].lng+"--"+data.mapProductAddrList[0].lat);
                             //进度纬度位置地名
                             $("#placeName").val(data.mapProductAddrList[0].describe);
                         }
					};
					
					
					/*
					 * 删除图片div
					 */
					resorts.delSelectMethodsImg = function(id,url,fileId){
						if(fileId == "upload_resortLogo"){
							if(resorts.aryLogoAttachments != null && resorts.aryLogoAttachments.length>0){
								for(var s in resorts.aryLogoAttachments){
									if((resorts.aryLogoAttachments[s].file == null && resorts.aryLogoAttachments[s].url==url) || (resorts.aryLogoAttachments[s].url==null && resorts.aryLogoAttachments[s].file == url)){
										$.extend(resorts.aryLogoAttachments[s],resorts.aryLogoAttachments[resorts.aryLogoAttachments.length-1]);
										resorts.aryLogoAttachments.pop();
										break;
									}
								}
							}
							$("#"+fileId+"_img_e").show();
							$("#"+fileId+"_log").html("建议不超过1M,支持png,bmp,jpeg,jpg格式.");
						}else{
							if(resorts.aryInfoAttachments != null && resorts.aryInfoAttachments.length>0){
								for(var s in resorts.aryInfoAttachments){
									if((resorts.aryInfoAttachments[s].file == null && resorts.aryInfoAttachments[s].url==url) || (resorts.aryInfoAttachments[s].url==null && resorts.aryInfoAttachments[s].file == url)){
										$.extend(resorts.aryInfoAttachments[s],resorts.aryInfoAttachments[resorts.aryInfoAttachments.length-1]);
										resorts.aryInfoAttachments.pop();
										break;
									}
								}
							}
						}
						$("img[id='"+id+"']").parent().remove();
					};
					
					
					resorts.initialize();
});
