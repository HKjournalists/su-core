$(document).ready(function() {
	var roleId, testerId;
	/**
	 * 第二步：初始化已发布的检测报告Grid
	 */
	var publish_grid = new fsn.TestResult1({
		has_add_btn : false,
		gridDiv : "#test_result_publish_grid",
		criteria : {
			roleId : roleId,
			testerId : testerId,
			publishFlag : true,
			page:1,
			pageSize:10
		},
		page_size : 10
	});
	publish_grid.initGrid();
});