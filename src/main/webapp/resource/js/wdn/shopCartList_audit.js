$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var affirmOrder = fsn.affirmOrder = fsn.affirmOrder || {};
	var wdnServiceBaseUrl = fsn.getHttpPrefix() + "/sampling/import/";
	var shoppingCatData = JSON.parse(window.sessionStorage
			.getItem("shoppingCat"));
	var i = 0;
	stepBar.init("stepBar", {
		step : 2,
		change : false,
		animation : true
	});

	affirmOrder.submit = function() {
	    var fillUserInfoFlag = $('#defaultForm').data("bootstrapValidator").isValid();
	    if (!fillUserInfoFlag){
	        $('#defaultForm').data("bootstrapValidator").validate();
	        return;
	    }
		var ds = $("#grid").data("kendoGrid").dataSource.data();
		if (ds && ds.length > 0) {
			ds.forEach(function(item) {
				$.ajax({
							url : fsn.getHttpPrefix() + "/wdn/submitOrder",
							type : "GET",
							data : {
								title : item.title,
								journal : (item.journal == "" ? "None"
										: item.journal),
								author : item.authors,
								year : item.year,
								volume : item.volume,
								issue : item.issue,
								issn : item.isn.replace("/", "__"),
								startPage : item.startpage,
								endPage : item.endpage,
								dataSource : item.datasource,
								applyName:$("#username").val(),
								applyPhone:$("#phone").val(),
								applyMail:$("#email").val(),
								applyAddress:$("#address").val()
							},
							contentType : "application/json; charset=utf-8",
							async : false,
							success : function(data) {
								if (data.status == true) {
									shoppingCatData = [];
									window.sessionStorage.setItem(
											"shoppingCat", shoppingCatData);
									window.open(fsn.getContextPath()
											+ "/views/wdn/success_order.html",
											"_self");
								}
							},
						});
			});
		}
	};
	affirmOrder.fillUserInfo = function() {
		$.ajax({
			url : fsn.getHttpPrefix() + "/wdn/getUserInfo",
			type : "GET",
			contentType : "application/json; charset=utf-8",
			async : false,
			success : function(data) {
				if (data.status == true) {
					var user = data.user;
					$("#username").val(user.realUserName);
					$("#phone").val(data.phone);
					$("#email").val(data.email);
					$("#address").val(data.addr);
				}
			}
		});
	}

	$('#defaultForm').bootstrapValidator({
		// live: 'disabled',
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			username : {
				validators : {
					notEmpty : {
						message : '姓名不能为空'
					},
				}
			},
			email : {
				validators : {
					notEmpty : {
						message : '邮件地址不能为空'
					},
					emailAddress : {
						message : '请输入有效的邮件地址'
					},
				}
			},
			phone : {
				validators : {
					notEmpty : {
						message : '电话号码不能为空'
					},
					phone : {
						message : '请输入有效的电话号码',
						country : 'GB',
					},
				}
			},
			address : {
				validators : {
					notEmpty : {
						message : '地址不能为空'
					},
				}
			},
			select : {
				validators : {
					notEmpty : {
						message : '请选择委托馆'
					},
				}
			},
		}
	});

	$("#grid").kendoGrid({
		columns : [ {
			field : "order",
			title : "序号",
			width : 60,
		// template:function(e){
		// return i+=1;
		// }
		}, {
			field : "title",
			title : "文献名称",
		}, {
			field : "datasource",
			title : "文献来源",
		}, {
			field : "authors",
			title : "文献作者",
		}, {
			field : "year",
			title : "日期",
		} ],
		dataSource : {
			data : shoppingCatData,
			schema : {
				data : function(e) {
					shoppingCatData.forEach(function(item) {
						i += 1;
						item.order = i;
					});
					return shoppingCatData;
				},
				total : function(d) {
					return d.length;
				}
			},
			page : 1,
			pageSize : 10, // 每页显示个数
		},
		pageable : {
			refresh : true,
			pageSizes : true,
			messages : fsn.gridPageMessage(),
		}
	});

	affirmOrder.fillUserInfo();

});
