$(function(){
	// 根据模块来创建命名空间
	(function(){
		var portal = fsn.portal = fsn.portal || {},
			upload = portal.upload = portal.upload || {};
			
		// 这个模块有三个上传model，分别是：unit、brand和poduct；
		(function(){
			var Certification = (function(){
				function _fn(){}
				_fn.prototype.cacheModel = {};
				_fn.prototype.model = {};
				_fn.prototype.model.Certification = function (id,name,message,imgUrl) {
					return {"id":id, "name": name, "message": message,"imgUrl":imgUrl};
				};
				_fn.prototype.Url = (function (todo) {
					var _HTTP_PREFIX = fsn.portal.HTTP_PREFIX;
					var url = {"cert":{}}; // 这里添加模块
					
					url.cert.create = _HTTP_PREFIX+"/portal/upload/cert";
					url.cert.search = _HTTP_PREFIX+"/portal/upload/preload";
					url.cert.uploadimg = _HTTP_PREFIX+"/portal/file/img";
					
					return url;
				}());
				
				_fn.prototype.createCert = function () {
					var id,name,message,imgUrl;
					id = $("#id").val();
					name = $("#name").val();
					if (portal.isBlank(name)) {
						alert("认证名称不能为空！");
						return false;
					}
					message = $("#message").val();
//					if (portal.isBlank(message)) {
//						alert("营养项目每日摄入量不能为空！");
//						return false;
//					}
					imgUrl = $("#imgForm").attr("imgurl");
					if (portal.isBlank(imgUrl)) {
						alert("请选择认证图片！");
						return false;
					}
					return this.model.Certification(id, name, message, imgUrl);
				};
				
				/**
				 * 提交动作调用的方法
				 */
				_fn.prototype.upload = function () {
					var mws = this, _cert = mws.createCert();
					
					if(!_cert){
						return false;
					}
					
					$.ajax({
						url: mws.Url.cert.create,
						type: "POST",
						datatype: "json",
						data: {"model":JSON.stringify(_cert)},
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
				 * 编辑的时候，根据businessUnit对象显示到页面
				 */
				_fn.prototype.writeCert = function (data) {
					if (!portal.isBlank(data)) {
						$.each(data, function(k,v){
							if (!portal.isBlank(v)) {
								switch (k) {
								case "imgUrl":
									var _tm_url_name = v.split("/").pop();
									$("#imgForm").attr("imgurl", v)
									.siblings("img.upload-img-preview").attr("src",v).css("display","block")
									.siblings("span.upload-img-name").find("em").text(_tm_url_name);
									break;
								default:
									$("#"+k).val(v);
									break;
								}
							}
						});
					}
				};
				
				/**
				 * 展示接口,根据Nutrition的ID值或缺数据
				 */
				_fn.prototype.show = function (sid) {
					var _id = sid||portal.getSearchParam(window.location.search, "id"), mws = this;;
					if (!portal.isBlank(_id)) {
						$.ajax({
							url: mws.Url.cert.search + "?id=" + _id + "&type=cert",
							type: "GET",
							datatype: "json",
							success: function (data) {
								if(data.RESTResult.status == 1) {
									mws.writeCert(data.RESTResult.data);
								} else {
									alert(data.RESTResult.message);
								}
							}
						});
					}
					
				};
				
				/**
				 * 重置表单
				 */
				_fn.prototype.reset = function () {
					$("input, textarea").each(function(){
						this.value = this.defaultValue;
					});
					this.cacheModel = null;
					$("#imgForm").attr("imgurl", "")
						.siblings("img.upload-img-preview").attr("src","").css("display","none")
						.siblings("span.upload-img-name").find("em").text("图片名称：请选择图片");
				};
				
				/**
				 * 页面初始化
				 */
				_fn.prototype.init = function () {
					// 在url中如果有id键，则是编辑（修改）
					var _cid = portal.getSearchParam(window.location.search, "id"), mws = this;
					mws.show(_cid);
					
					// 图片ajax上传
					$('#imgForm').ajaxForm(function(data) {
						if(data.RESTResult.status == 0){
							alert(data.RESTResult.message);
							$('#imgForm').attr("imgurl", "");
						}else{
							var _$this = $('#imgForm'),
								_res = data.RESTResult.data/* 返回的数据将是一个{"name":"","path":""} */;
							if (_res) {
								_$this.attr("imgurl", _res.name);
							
								// 这一部分的数据是图片的[伪]预览
								_$this.siblings("img.upload-img-preview").attr("src", _res.path)
									.css({"display":"block"})
									.siblings("span.upload-img-name").find("em").text(_res.name);
							}
						}
		            });
					
					$("#imgUrl").on("change", function(){
						var _$file = $(this);
						
						if(portal.isBlank(_$file.val())){
							_$file.parent().siblings("img.upload-img-preview").attr("src","").css("display","none")
								.siblings("span.upload-img-name").find("em").text("请选择图片");
							return false;
						} else {
							// 检查选中文件是否是图片
							var _bImg = portal.isImg(_$file.val());
							
							if(!_bImg){
								alert("请选择JPG、PNG等格式的图片文件！");
								return false;
							}
							
							// 如果需要限制文件的尺寸的话,
							var _bFileSize = portal.checkFileSize(_$file);
							console.log(_bFileSize);
							var _img = _$file.parent().siblings("img.upload-img-preview");
							console.log(_img.css("width"));
							
							// 初始化form
							var _$form = $('#imgForm');
							$('#imgForm').attr("imgurl", "");
							_$form[0].action = mws.Url.cert.uploadimg;
							_$form.submit();
						}
					});
					
					// save事件\reset事件
					$(".k-button").on("click", function(){
						var _this = $(this), _span = _this.find("span.ui-button-text");
						switch(_span.attr("data-fsn-text")) {
						case 'Save':
							mws.upload();
							break;
						case 'Reset':
							mws.reset();
							break;
						default: break;
						}
					});
				};
				
				return _fn;
			})();
			this.Certification = new Certification();
		}).call(upload);
		
		upload.Certification.init();
	})();
});