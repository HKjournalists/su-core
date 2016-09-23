(function($){
	$.fn.doselectmore = function(settings, relfunc) {
		var conf =$.extend({
			bdisabled:false, // 在写好select后是否关闭它的编辑功能，setter：disabled:true/false
			dataresult:false, // 请求返回的数据所在的位置，如果为false，这可用数据就是data，如果有值值可用数据位data[dataresult]
			namekey: "name", // 下拉选框的文本
			pnamekey: "uid", // 数据类型中上级ID的名称
			idkey: "code", // 本级ID
			cnamekey: "children", // 子集key
			
			selectstatusid:false,
			
			selectname:this[0].id||"sel", // 创建select的名字前缀
			selectclass:'sel_default', // select默认样式
			optionclass:'sel_default_options', // select option ‘请选择’的默认样式
			
			pname:false, // 上级select的name属性值
			nname:false, // 本级name属性值
			
			method: "POST", // ajax请求方式
			datatype: "json", // 返回的数据类型
			param:{}, // 要传到后台的
			
			pval:null, // 上级ID值
			chckval:null, // 设置预选中对象
			chckvalarry:null, // 设置预选中对象组
			
			vl:0, // 级数
			url: false, // url请求数据地址
			data: false, // 数据存储
			
			relselid:false, // 要额外关联的select
			relurl:false, // 额外关联的地址
			relparamname:false, // 额外关联要传递的参数名
			relstatusid:false, // 额外关联select的状态提示容器
			relfunc:function(data, c){ // 额外关联的select function
				var _$os = $("#"+c.relselid);
				_$os.empty();
				_$os.append('<option class="sel_default_options">请选择...</option>');
				$.each(data, function(i, v){
					_$os.append('<option value="'+v[c.idkey]+'" >'+v[c.namekey]+'</option>');
				});
				_$os.attr("disabled", false); // 打开select
			}
		}, settings);
		
		var me = $(this); // 当前select对象
		
		if(conf.relselid){ // 如果有额外的关联select对象
			$("#"+conf.relselid).attr("disabled", true); // 关闭select
		}
		
		(function(){
			if(!conf.nname){ // 如果没有预设本级的name属性值
				conf.nname = (conf.selectname+(conf.vl+1)); // 定义命名规则
			}

			if((!conf.pname)&&conf.vl>0){ // 如果上级name属性不存在并且不是第一级，设置pname的属性
				conf.pname = conf.selectname+(conf.vl-1);
			}
		
			// 如果配置对象中存在数据
			if(!conf.data){
				if (conf.url) {
//					var param = {};
					$("#"+conf.selectstatusid).html("加载中...").css("color", "green");
					$.ajax({
						type: conf.method,
						url: conf.url,
						data: conf.param,
						dataType: conf.datatype,
						success: function(data){
							conf.data=conf.dataresult?data[conf.dataresult]:data; // 将获取的数据保存到conf.data中
							conf.pval=(conf.data)[0][conf.pnamekey];
							selectmorebuilder(me,conf); // 调用create option的函数
							$("#"+conf.selectstatusid).html("");
						},
						error:(function(request,status,err){
							var errText = request.responseText;
							var ErrMessage = "页面出现"+request.status+"错误信息，\n";
							ErrMessage += "错误内容为："+request.statusText+"\n"+errText.substring(errText.indexOf("<pre>")+5,errText.indexOf("</pre>"));
							alert(ErrMessage);
							$("#"+conf.selectstatusid).html("");
						})
					});
				}
			}else{//如果conf.data中已经存在数据
				conf.pval=conf.data[0]?conf.data[0][conf.pnamekey]:false;
				selectmorebuilder(me,conf);// 调用create option的函数
				
				// 如果有需要额外关联的select
				if(conf.relselid){
					if($("input[name="+conf.selectname+"]").val()!="请选择..."){
						var _relparam_ = {};
						if(conf.relparamname) 
							_relparam_[""+conf.relparamname] = $("input[name="+conf.selectname+"]").val();
						// 如果select后面有可以用于提示的文本
						if(conf.relstatusid){
							$("#"+conf.relstatusid).html("加载中...").css("color", "green");
						}
						$.ajax({
							type: conf.method,
							url: conf.relurl,
							data: _relparam_,
							dataType: conf.datatype,
							success: function(data){
								conf.relfunc(data, conf);
								$("#"+conf.relstatusid).html("");
							}
						});
					}else{
						$("#"+conf.relselid).attr("disabled", true); // 关闭select
					}
				}
			}
		})();

		// 根据传入的select对象和配置对象cf
		function selectmorebuilder(thisme,cf) {

			// 新建一个select
			var select = $("<select></select>");
			select.attr({
				name:cf.selectname+conf.vl,
				id:cf.selectname+conf.vl,
				nname:cf.nname,
				pname:cf.pname,
				vl:cf.vl,
				'class':cf.selectclass
			});
			// 包裹select控件的div容器
			var sdiv = null;
			// 如果是第一级，新建div
			if (conf.vl == 0) {
				sdiv = $("<div></div>");
				thisme.after(sdiv).remove();
				sdiv.append("<input type=\"hidden\" name=\"" + cf.selectname + "\">");
				sdiv.append(select);
				sdiv.get(0).t=cf;
			}else{// 否则
				thisme.removeselectmore();
				sdiv = $("input[name="+cf.selectname+"]").parent();
				sdiv.append(select);
			}
			// 清空新建的select控件的内容
			select.empty();

			// 生成option选项

			var counti = 0;
			if(cf.data && cf.data.length>0){
				select.append("<option class='"+cf.optionclass+"' selected>请选择...</option>");
				$.each(cf.data, function(i, item){
					if(item[cf.pnamekey]==cf.pval){
						select.append("<option value='"+item[cf.idkey]+"' pid='"+item[cf.pnamekey]+"'>"+item[cf.namekey]+"</option>");
						counti++;
					}
				});
				
				// 默认第一个被选中
				select[0].selectedIndex = 0;
				
			}

			if (counti == 0) {
				select.remove();
			}
			else {
				select.bind("change",function(){

					var _pval = $(this).val(), _vl = cf.vl + 1, _selected = this.selectedIndex;
					var _data = (cf.data)[_selected-1]?((cf.data)[_selected-1][cf.cnamekey]?(cf.data)[_selected-1][cf.cnamekey]:[]) : [];
					var nselect = $("#" + $(this).attr("nname"));
					if (nselect.length == 0) {
						nselect = $("<select></select>");
						sdiv.append(nselect);
					}

					nselect.doselectmore({
						selectstatusid:cf.selectstatusid,
						namekey: cf.namekey,
						pnamekey: cf.pnamekey,
						idkey: cf.idkey,
						cnamekey:cf.cnamekey,
						selectname: cf.selectname,
						param: cf.param,
						pval: _pval,
						vl: _vl,
						chckvalarry:sdiv.get(0).t.chckvalarry,
						data: _data,
						relselid: cf.relselid,
						relurl:cf.relurl,
						relfunc: cf.refunc,
						relparamname: cf.relparamname,
						relstatusid: cf.relstatusid,
						selectclass: cf.selectclass
					});
				});
				if(cf.chckvalarry!=null){// 如果设置了默认选中的选项，就设置它被选中
					if(cf.chckvalarry.length>=cf.vl)
						select.val(cf.chckvalarry[cf.vl]);
					select.change();
				}
				if(cf.vl==1&&cf.chckvalarry!=null&&cf.chckvalarry[1]!=select.val()){
					var dcf = sdiv.get(0).t;
					dcf.chckvalarry=null;
					dcf.chckval=null;
					sdiv.get(0).t=dcf;
				}
				
				// select.change();

				// $("input[type=hidden][name="+cf.selectname+"]").val($("input[type=hidden][name="+cf.selectname+"]").getselectmoreval());
			}
			var _arrValAndName_ = $("input[type=hidden][name="+cf.selectname+"]").getselectmoreval();
			$("input[type=hidden][name="+cf.selectname+"]").val(_arrValAndName_.v).attr('vname',_arrValAndName_.vn);
		}
		
	};
	$.fn.getselectmoreval = function(){
		var me =  $(this);
		if(me.size()==0) return;
		var sdiv = me.parent();
		if(sdiv.size()==0) return;
		
		var cf = sdiv.get(0).t;
		var nselect = $("#" + cf.selectname+cf.vl);
		
		var arr = [];
		while(nselect.size()>0){
			arr.v = nselect.val();
			arr.vn = nselect.find("option:selected").text();
			nselect = $("#" +nselect.attr("nname"));
		}
		return arr;
	};
	$.fn.setselectmoreval = function(idv){
		var me =  $(this);
		if(me.size()==0) return;
		var sdiv = me.parent();
		if(sdiv.size()==0) return;
		var cf = sdiv.get(0).t;
		cf.chckval=idv;
		if(idv!=null){
			var pid=cf.chckval;
			var k=1;
			cf.chckvalarry = new Array();
			cf.chckvalarry.push(pid);
			while(k>0){
				k=0;
				$.each(cf.data, function(i, item){
					if(item[cf.idkey]==pid&&item[cf.pnamekey]!=cf.pval){
						pid=item[cf.pnamekey];
						cf.chckvalarry.unshift(pid);
						k++;
					}
				});
			}
		}
		sdiv.get(0).t=cf;
		var nselect = $("#" + cf.selectname+cf.vl);
		nselect.val(cf.chckvalarry[0]);
		nselect.change();
	};
	$.fn.removeselectmore = function(){
		if($(this).attr("nname")!=null){
			$("#"+$(this).attr("nname")).removeselectmore();
		}
		$(this).remove();
	};
})(jQuery);