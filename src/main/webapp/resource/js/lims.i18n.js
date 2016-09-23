(function($) {
    var lims = window.lims = window.lims || {};

    if (!lims.zh) {
        lims.zh = {};
    }

    $.extend(true, lims.zh, {
    	"Today":"今天",
    	"WIN_CONFIRM":"确认",
    	"WIN_DEFAULT_MSG":"确认进行该操作吗？",
    	"WIN_BLANK":" ",
    	"Keyword":"关键字",
        "ID": "ID",
        "Template Name": "模板",
        "Template": "模板",
        "Create Template":"创建模板",
        "Add Template:":"新增表单模板：",
        "Update Template:":"更新表单模板：",
        "Template Management:":"表单模板管理：",
        "Version": "版本",
        "Create By": "创建者",
        "Last Upate Time": "最后更新时间",
        "Delete": "删除",
        "Destroy": "删除",
        "Preview": "预览",
        "Clear" : "清空",
        "Edit": "编辑",
        "Config":"配置",
        "OK": "确定",
        "DeleteConfirmation": "你确定要删除这条纪录吗?",
        "Are you sure that you want to delete this record?": "你确定要删除这条纪录吗?",
        "Search": "搜索",
        "Yes": "是",
        "No": "否",
        "Size": "长度",
        "Field Name": "字段名称",
        "Field Type": "类型",
        "Field Locale": "Locale",
        "Order": "顺序",
        "Create Locale": "创建Locale",
        "LocaleHeader": "Locale",
        "LocaleDisplayNameHeader": "显示名称",
        "Place Holder": "提示信息",
        "Tool Tip": "提示",
        "Description": "备注",
        "Add": "添加",
        "Continue_to_add" : "下一条数据录入",
        "TemplateNameLabel": "名称",
        "CategoryLabel": "分类",
        "GroupLabel": "组",
        "Save": "保存",
        "Update": "更新",
        "Save As New Template" : "保存为新模版",
        "List of Field or Group:": "字段和字段组:",
        "Locales:": "Locales:",
        "Please Add Locale!": "请添加至少一个Locale!",
        "Name Must Be Unique!": "名称必须唯一!",
        "Please Add Field Or Group!": "请添加至少一个字段!",
        "Please Input Template Name!": "请输入模版名称!",
        "Field Name...": "请输入字段名称",
        "Please enter an integer (minimum":"请输入整数 (最小为",
        "Size...": "请输入长度",
        "Template Name...": "请输入模版名称",
        "Add Template (Roles - Sales Manager):": "添加模版 (Roles - Sales Manager):",
        "Management Templates (Roles - Sales Manager):": "模版管理 (Roles - Sales Manager):",
        "Update Template (Roles - Sales Manager):": "更新模版 (Roles - Sales Manager):",
        "About <span class='font_blue_underline'>{0}</span> results!": "共有 <span class='font_blue_underline'>{0}</span> 条纪录！",
        "Error! This item has been added!": "错误！不可重复添加！",
        'A Template "<span class="font_blue_underline">{0}</span>" is {1} {2}!': '模版"<span class="font_blue_underline">{0}</span>"{1}{2}!',
        'A Template "<span class="font_blue_underline">{0}</span> is {1} {2}!': '模版"<span class="font_blue_underline">{0}</span>"{1}{2}!',
        
        'A Service Order "<span class="font_blue_underline">{0}</span>" is {1} {2}!': '"委托登记单"<span class="font_blue_underline">{0}</span>"{1}{2}!',
        'A Service Order "<span class="font_blue_underline">{0}</span> is {1} {2}!': '"委托登记单"<span class="font_blue_underline">{0}</span>"{1}{2}!',
        
        "created": "创建",
        "Created": "创建",
        "updated": "更新",
        "Updated": "更新",
        "Update": "更新",
        "failed": "失败",
        "Failed": "失败",
        "successfully": "成功",
        "Successfully": "成功",
        "RECEIVE_BTN":"接受",
        //page information
        "Management Instruments:":"仪器管理：",
        "No items to display": "没有纪录显示",
        "items per page": "条每页",
        "of {0}": "of {0}",
        "Go to the last page": "最后一页",
        "Go to the first page": "第一页",
        "Go to the next page": "后一页",
        "Go to the previous page": "前一页",
        "Refresh": "刷新",
        "Page": "页",
        "{0} - {1} of {2} items": "{0} - {1} 共 {2} 条",
        //
        //filter  information
        "Show items with value that:" : "显示需要项目:",
        "and" : "并且",
        "or" : "或者",
        "Filter" : "过滤",
        "Clear" : "清空",
        "Starts with" : "以开头",
        "Ends with" : "以结尾",
        "Contains" : "包含",
        "Does not contain" : "不包含",
        "Is equal to" : "等于",
        "Is not equal to" : "不等于",
        "Greater than or equal to" : "大于或等于",
        "Greater than" : "大于",
        "Less than or equal to" : "小于或等于",
        "Less than" : "小于",
        //
        "Create Instrument": "添加仪器设备",
        "Management Instruments (Roles - Sales Manager):": "仪器设备管理(Roles - Sales Manager):",
        "Instrument": "仪器设备",
        "Chinese Name": "中文名称",
        
        "Create": "创建",
        //
        "Submit":"提交",
        "Cancel":"取消",
        "PRIORITY VALUES":[{label:"低", value:0}, {label:"中等", value:1}, {label:"高", value:2}],
        
        //judgementstandard information
        "Determine the standard name":"判定标准名称",
        "Display Name":"显示名称",
        "Select":"选择",
        "Selection Criterion":"选择判定标准",
        
        //productgroup information
        "Product Name":"商品名称",
        "Product Selection":"商品选择",
        
        //samplestore information
        "Sample Name":"样品名称",
        "Sample Number":"样品编号",
        "Unit":"单位",
        "Inventory":"库存量",
        "Stock Option":"库存选择",
        
        "LinearRegression":"是否为线性回归",
        //
        "TestResultFlagLabel" : "是否为检测结果",
        "GOBACK_CONFIRM_MSG":"退回原因",
        "PREVIEW_TEST_ITEMS":"预览检测项目",
        "BINDING_RAW_RECORD":"绑定原始记录",
        "FILL_RAW_RECORD":"填写原始记录",
        "receive task":"确认接受该检测任务？",
        "binding raw record":"确认绑定检测项目到指定的原始记录模版？",
        "ARE YOU SURE DELETE?" : "确定删除此模版吗？",
        "A Field" : "一条记录",
        "removed Successfully" : "删除成功！",
        "Currently field " : "当前记录",
        " related by another business record, so cannot be remove" : "因与其他业务数据关联，所以删除失败！",
    });

    if (!lims.instrument) {
        lims.instrument = {};
    }
    if (!lims.instrument.zh) {
        lims.instrument.zh = {};
    }

    $.extend(true, lims.instrument.zh, {
        "Instrument": "仪器设备",
    });


    if (!lims.serviceOrder) {
        lims.serviceOrder = {};
    }
    if (!lims.serviceOrder.zh) {
        lims.serviceOrder.zh = {};
    }

    $.extend(true, lims.serviceOrder.zh, {
        "Service Order": "委托登记单",
        "Create Service Order":"添加委托登记单",
        "Management Service Order (Roles - Sales Manager):": "委托登记单管理(Roles - Sales Manager):",
        "Service Order": "委托登记单号",
        "Client": "委托单位",
        "SheetNo":"委托登记号",
        "sheetNo":"委托登记号",
        "Sheet No":"委托登记号",
        "Sheet No Must Be Unique!":"委托登记号必须惟一",
        'A Service Order "<span class="font_blue_underline">{0}</span>" is {1} {2}!':' 一个委托登记单 "<span class="font_blue_underline">{0}</span>" {1}{2}!'
    });



    if (!lims.serviceRequest) {
        lims.serviceRequest = {};
    }
    if (!lims.serviceRequest.zh) {
        lims.serviceRequest.zh = {};
    }

    $.extend(true, lims.serviceRequest.zh, {
        "Subject": "主题",
        "FileType":"可选文件类型",
        "Description":"请求描述",
        "Management Service Request (Roles - Sales Manager):": "Service Request管理(Roles - Sales Manager):",
        "Request User": "请求用户",
        "Create Time": "创建时间",
        "Comment Count":"评论数",
        "Priority":"优先级",
        "Show":"查看",
        "Comment":"评论",
        "Attachments":"附件",
        "Attachment":"附件",
        "Uploaded Attachments":"已上传文件",
        "Create Service Request":"创建Service Request",
        "Add Service Request":"添加Service Request",
        "Update Service Request":"更新Service Request",
        "Note" : "(注意：文件大小不能超过10M)",
    });


    if (!lims.upload) {
        lims.upload = {};
    }
    if (!lims.upload.zh) {
        lims.upload.zh = {};
    }

    $.extend(true, lims.upload.zh, {
        "retry":"重试",
        "remove":"删除",
        "cancel":"取消",
        "drop files here to upload":"drop files here to upload",
        "failed":"失败",
        "uploaded":"上传成功",
        "Upload files":"已上传文件",
        "uploading":"上传中",
        "select":"选取文件",
        "Upload File  Failed !":"上传文件类型或大小错误(文件不得大于10M)！！",
    });
    
    
    if (!lims.testCase) {
        lims.testCase = {};
    }
    if (!lims.testCase.zh) {
        lims.testCase.zh = {};
    }

    $.extend(true, lims.testCase.zh, {
        "Name":"名称",
        "Management Test Case (Roles - Sales Manager):":"Test Case管理(Roles - Sales Manager):",
        "TestCaseNameLabel":"名称",
        "Name...":"测试名称",
        "Create Test Case":"创建测试用例",
        "Add Data Grid":"添加检测数据",
        "Grid":"数据列表",
        "Add Test Record":"添加检测纪录",
    });

    if (!lims.rawrecord) {
        lims.rawrecord = {};
    }
    if (!lims.rawrecord.zh) {
        lims.rawrecord.zh = {};
    }
    $.extend(true, lims.rawrecord.zh, {
        "Raw Record (Roles - Sales Manager):":"原始纪录管理(Roles - Sales Manager):",
        "Sample No":"样品编号",
        "Test Item":"检测项目",
        "Raw Record":"原始纪录",
        "Count: #=count#":"共有#=count#条检测项目",
        "Sample No #=value# (Count: #=count#)":"样品编号: #=value#",
        "Processing": "前期处理过程",
        "Test Result": "检测结果",
        "Show Result": "展示结果",
        "Value Unit": "单位",
        "Test Result Qualified": "是否合格",
        "Note": "备注", 
        "testStandardName": "检测标准", 
        "judgmentStandardName" : "判定标准", 
        "qualification" : "技术指标",
        "specification" : "技术指标",
        "specifications" : "技术指标",
        "QUALIFICATION" : "技术指标",
        "Add Parallel" : "添加平行样本",
        "Remove Parallel" : "删除平行样本",
        "You Can't Remove Parallel!" : "你不能删除此平行样本\n需要保留一个样本检测项和至少一个平行项",
    });
 
})(jQuery);
