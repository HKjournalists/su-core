$(function(){
	// 根据模块来创建命名空间
	(function(){
		var portal = fsn.portal = fsn.portal || {},
			upload = portal.upload = portal.upload || {};
			
		// 这个模块有三个上传model，分别是：unit、brand和poduct；
		(function(){
			var NutritionSTD = (function(){
				function _fn(){}
				_fn.prototype.cacheModel = {};
				_fn.prototype.model = {};
				_fn.prototype.model.Nutrition = function (id,name,dailyIntake,unit) {
					return {"id":id, "name": name, "dailyIntake": dailyIntake,"unit":unit};
				};
				_fn.prototype.Url = (function (todo) {
					var _HTTP_PREFIX = fsn.portal.HTTP_PREFIX;
					var url = {"nutri":{}}; // 这里添加模块
					
					url.nutri.create = _HTTP_PREFIX+"/portal/upload/nutri";
					url.nutri.update = _HTTP_PREFIX+"/portal/upload/nutri";
					url.nutri.search = _HTTP_PREFIX+"/portal/upload/preload";
					
					return url;
				}());
				
				_fn.prototype.createNutri = function () {
					var id,name,dailyIntake,unit;
					id = $("#id").val();
					name = $("#name").val();
					if (portal.isBlank(name)) {
						alert("营养项目名称不能为空！");
						return false;
					}
					dailyIntake = $("#dailyIntake").val();
					if (portal.isBlank(dailyIntake)) {
						alert("营养项目每日摄入量不能为空！");
						return false;
					}
					unit = $("#unit").val();
					if (portal.isBlank(unit)) {
						alert("营养项目单位不能为空！");
						return false;
					}
					return this.model.Nutrition(id, name, dailyIntake, unit);
				};
				
				/**
				 * 提交动作调用的方法
				 */
				_fn.prototype.upload = function () {
					var mws = this, _nutri = mws.createNutri();
					
					if(!_nutri){
						return false;
					}
					
					$.ajax({
						url: mws.Url.nutri.create,
						type: "POST",
						datatype: "json",
						data: {"model":JSON.stringify(_nutri)},
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
				_fn.prototype.writeNutri = function (data) {
					if (!portal.isBlank(data)) {
						$.each(data, function(k,v){
							$("#"+k).val(v);
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
							url: mws.Url.nutri.search + "?id=" + _id + "&type=nutri",
							type: "GET",
							datatype: "json",
							success: function (data) {
								if(data.RESTResult.status == 1) {
									mws.writeNutri(data.RESTResult.data);
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
					$("input").each(function(){
						this.value = this.defaultValue;
					});
				};
				
				/**
				 * 页面初始化
				 */
				_fn.prototype.init = function () {
					// 在url中如果有id键，则是编辑（修改）
					var _nid = portal.getSearchParam(window.location.search, "id"), mws = this;
					mws.show(_nid);
					
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
			this.NutritionSTD = new NutritionSTD();
		}).call(upload);
		
		upload.NutritionSTD.init();
	})();
});