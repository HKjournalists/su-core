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
			type : "post",
			url : fsn.getHttpPrefix() + "/recycle/getRecords?type=3",
			success:function(data){
				console.log(data);
				$('#content_div').html(data);
			}
		});
	</script>
</html>