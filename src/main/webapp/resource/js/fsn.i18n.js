(function($) {
    var fsn = window.fsn = window.fsn || {};

    if (!fsn.zh) {
        fsn.zh = {};
    }

    $.extend(true, fsn.zh, {
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
        /******************************************/
       
        "ID": "ID",
        "Template Name": "模版",
        "Template": "模版",
        "Version": "版本",
        "Create By": "创建者",
        "Last Upate Time": "最后更新时间",
        "Delete": "删除",
        "Destroy": "删除",
        "Preview": "预览",
        "Edit": "编辑",
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
        "TemplateNameLabel": "名称",
        "CategoryLabel": "分类",
        "GroupLabel": "组",
        "Save": "保存",
        "List of Field or Group:": "字段和字段组:",
        "Locales:": "Locales:",
        "Please Add Locale!": "请添加至少一个Locale!",
        "Name Must Be Unique!": "名称必须唯一!",
        "Please Add Field Or Group!": "请添加至少一个字段!",
        "Please Input Template Name!": "请输入模版名称!",
        "Field Name...": "请输入字段名称",
        "Size...": "请输入长度",
        "Template Name...": "请输入模版名称",
        "Add Template (Roles - Sales Manager):": "添加模版 (Roles - Sales Manager):",
        "Management Templates (Roles - Sales Manager):": "模版管理 (Roles - Sales Manager):",
        "Update Template (Roles - Sales Manager):": "更新模版 (Roles - Sales Manager):",
        "About <span class='font_blue_underline'>{0}</span> results!": "共有 <span class='font_blue_underline'>{0}</span> 条纪录！",
        "Error! This item has been added!": "错误！不可重复添加！",
        'A Template "<span class="font_blue_underline">{0}</span> is {1} {2}!': '模版"<span class="font_blue_underline">{0}</span>{1}{2}!',
        "created": "创建",
        "Created": "创建",
        "updated": "更新",
        "Updated": "更新",
        "Update": "更新",
        "failed": "失败",
        "Failed": "失败",
        "successfully": "成功",
        "Successfully": "成功",
        //page information
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
        "Create Instrument": "添加仪器设备",
        "Management Instruments (Roles - Sales Manager):": "仪器设备管理(Roles - Sales Manager):",
        "Instrument": "仪器设备",
        "Chinese Name": "中文名称",
        
        //
        "Submit":"提交",
        "Cancel":"取消",
        "PRIORITY VALUES":[{label:"低", value:0}, {label:"中等", value:1}, {label:"高", value:2}],
        
        "Add new record" : "新增",
        "Are you sure you want to delete this record?" : "你确定要删除这条记录吗？",
    });

    if (!fsn.instrument) {
        fsn.instrument = {};
    }
    if (!fsn.instrument.zh) {
        fsn.instrument.zh = {};
    }

    $.extend(true, fsn.instrument.zh, {
        "Instrument": "仪器设备",
    });


    if (!fsn.serviceOrder) {
        fsn.serviceOrder = {};
    }
    if (!fsn.serviceOrder.zh) {
        fsn.serviceOrder.zh = {};
    }

    $.extend(true, fsn.serviceOrder.zh, {
        "Service Order": "委托登记单",
        "Create Service Order":"添加委托登记单",
        "Management Service Order (Roles - Sales Manager):": "委托登记单管理(Roles - Sales Manager):",
        "Service Order": "委托登记单号",
        "Client": "委托单位",
        "SheetNo":"委托登记号",
        "sheetNo":"委托登记号",
        "Sheet No":"委托登记号",
        "Sheet No Must Be Unique!":"委托登记号必须惟一",
    });



    if (!fsn.serviceRequest) {
        fsn.serviceRequest = {};
    }
    if (!fsn.serviceRequest.zh) {
        fsn.serviceRequest.zh = {};
    }

    $.extend(true, fsn.serviceRequest.zh, {
        "Subject": "主题",
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
    });


    if (!fsn.upload) {
        fsn.upload = {};
    }
    if (!fsn.upload.zh) {
        fsn.upload.zh = {};
    }

    $.extend(true, fsn.upload.zh, {
        "retry":"重试",
        "remove":"删除",
        "cancel":"取消",
        "drop files here to upload":"drop files here to upload",
        "failed":"失败",
        "uploaded":"上传成功",
        "Upload files":"已上传文件",
        "uploading":"上传中",
        "select":"选取文件",
        "Upload File Failed":"上传文件失败",
         
    });

 
})(jQuery);
