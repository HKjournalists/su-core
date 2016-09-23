<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<title>西部电子商务数据接入</title>
		<script src="../../resource_new/kendoui/js/jquery.min.js"></script>
		<script src="../../resource_new/js/fsn.i18n.js"></script>
		<script src="../../resource_new/js/less.js" type="text/javascript"></script>
		<script src="../../resource_new/js/jquery.cookie.js"></script>
		<script src="../../resource_new/js/filter.js"></script>
		<script src="../../resource_new/js/fsn.js"></script>
		<script src="../../resource_new/js/util.js"></script>
	</head>
	
	<body>
		<div id='content_div'>
		</div>
	</body>
	<script type="text/javascript">
		$.ajax({
			type : "get",
			url : fsn.getHttpPrefix() + "/data_access/western_electronic_show",
			success:function(data){
				var data_count = data.data_count;
				var s = '';
				if (data_count==-1) {
					//未获取到数据接入情况
					s += '<div style="font-size:25px;">';
					s += '未获取到数据接入情况！';
					s += '</div>';
				}
				if (data_count == 0) {
					//未进行过数据接入
					s += '<input type="button" id="western_electronic_btn" class="jump-btn btn btn-success btn-lg" value="接入西部电子商务数据" onclick="western_electronic_access()"/>';
					s += '<div style="color:#0099CC;font-size:25px;">';
					s += '小贴士：接入数据可能会耗费一段时间，示西部电子商务数据量而定<br>最好在无用户使用的时段进行数据接入';
					s += '</div>';
				}
				var western_electronic = null;
				var complete_num = 0;
				var company_num = 0;
				var product_num = 0;
				var report_num = 0;
				var trace_num = 0;
				var success_num = 0;
				var fail_num = 0;
				var percent = null;
				if (data_count == 1) {
					western_electronic = data.western_electronic;
					complete_num = western_electronic.complete_num;
					company_num = western_electronic.company_num;
					product_num = western_electronic.product_num;
					report_num = western_electronic.report_num;
					trace_num = western_electronic.trace_num;
					success_num = western_electronic.success_num;
					fail_num = western_electronic.fail_num;
					percent = western_electronic.percent;
				}
				if (data_count==1 && western_electronic.status==2) {
					//数据接入中，显示当前处理进度
					s += '<div style="font-size:25px;">';
					s += '正在接入：已接入';
					s += complete_num;
					s += '家企业，总共';
					s += company_num;
					s += '家企业，进度：';
					s += percent;
					s += '</div>';
					if (fail_num > 0) {
						s += '<div style="font-size:25px;color:red;">接入失败企业数：';
						s += fail_num;
						s += '家';
					}
				}
				if (data_count==1 && western_electronic.status==1) {
					//数据接入完成
					s += '<div style="font-size:25px;">';
					s += '<div>接入完成！</div>';
					s += '<div>接入企业' + complete_num + '家</div>';
					s += '<div>接入产品' + product_num + '个</div>';
					s += '<div>接入检测报告' + report_num + '条</div>';
					s += '<div>接入产品追溯信息' + trace_num + '条</div>';
					if (fail_num > 0) {
						s += '<div style="font-size:25px;color:red;">接入失败企业数：';
						s += fail_num;
						s += '家';
					}
					s += '</div>';
				}
				$("#content_div").html(s);
			}
		});
		
		function western_electronic_access() {
			//接入西部电子商务数据
			$.ajax({
				type : "post",
				url : fsn.getHttpPrefix() + "/data_access/western_electronic_access",
				success:function(data){
					var flg = data.flg;
					if (flg == true || flg == 'true') {
						$('#western_electronic_btn').attr('disabled',"true");//防止重复操作
						$('#content_div').append('<div style="font-size:25px;">数据接入中，5秒后自动刷新页面！</div>');
						setTimeout('refresh()',5000);
					}
				}
			});
		}
		
		function refresh() {
			window.location.reload();
		}
		
		function western_electronic_product() {
			$.ajax({
				type : "post",
				url : "http://218.95.175.162:16004/FoodSecurity/services/product/productByCompanyId?companyId=1",
				success:function(data){
					console.log(data);
				}
			});
		}
	</script>
</html>