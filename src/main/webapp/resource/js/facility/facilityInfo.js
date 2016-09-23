$(document).ready(function() {
    var fsn = window.fsn = window.fsn || {};
    var facility = window.fsn.facility = window.fsn.facility || {};
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    var business_unit = window.fsn.business_unit = window.fsn.business_unit || {};
        portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
        facility.imgResource  = new Array();


        facility.initialize = function(){
        /**
         * 初始化企业页码tabStrip
         */
        facility.initTabStrip();
        facility.initFacilityInfoGrid("FACILITY_INVENTORY_GRID",facility.facility_columns);
        facility.initEquipmentInfoGrid("MAINTENANCE_RECORD_GRID",facility.maintenance_columns);
        //facility.buildUploadFacility("upload_facility_files", facility.imgResource , "upload_facility_files_log");


        $("#submitFacilityInfo").bind("click", facility.submitFacilityInfo);
        $("#submitMaintenanceInfo").bind("click", facility.submitMaintenanceInfo);
        $("#submitOperateInfo").bind("click", facility.submitOperateInfo);
        /**
         * 初始化规模信息
         */
        facility.initOperateInfo();
        /**
         * 日期控件
         */
        $("input[id=buyingTime],input[id=maintenanceTime],input[id=m_maintenanceTime]").kendoDatePicker({
            format: "yyyy-MM-dd",
            height: 30,
            culture: "zh-CN",
//  	        max: new Date(),
            animation: {
                close: {
                    effects: "fadeOut zoom:out",
                    duration: 300
                },
                open: {
                    effects: "fadeIn zoom:in",
                    duration: 300
                }
            }
        });
        $("#ADDFACILITYINFO_WIN").kendoWindow({
            width: "700",
            height:"auto",
            title: "设备信息",
            visible: false,
            resizable: false,
            draggable:false,
            modal: true
        });
        $("#MAINTENANCEINFO_WIN").kendoWindow({
            width: "450",
            top:5,
            height:"auto",
            title: "养护记录",
            visible: false,
            resizable: false,
            draggable:false,
            modal: true
        });
        $("#DIV_IMG_WIN").kendoWindow({
            width: "900",
            height:"700",
            title: "设备图片",
            visible: false,
            resizable: false,
            draggable:false,
            modal: true
        });
            $("#operateScope").kendoComboBox();
    };
    facility.initTabStrip = function(){
        facility.tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
        facility.tabStrip.select(0);        // Select by jQuery selector
        // tabStrip.select(1);
        /**
         * 初始化
         //      */
        var g = $("#tabstrip").data("kendoTabStrip");
        if(!g){
            g =  $("#tabstrip").kendoTabStrip({
                animation:  {
                    open: {
                        effects: "fadeIn"
                    }
                }
            });
        };
    };


    /* 初始化上传控件  */
    facility.buildUploadFacility = function(id, attachments, msg){
        $("#"+id).kendoUpload({
            async: {
                saveUrl: fsn.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
                removeUrl: fsn.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments"
            },localization: {
                select: lims.l(id),
                retry:lims.l("retry",'upload'),
                remove:lims.l("remove",'upload'),
                cancel:lims.l("cancel",'upload'),
                dropFilesHere: lims.l("drop files here to upload",'upload'),
                statusFailed: lims.l("failed",'upload'),
                statusUploaded: lims.l("uploaded",'upload'),
                statusUploading: lims.l("uploading",'upload'),
                uploadSelectedFiles: lims.l("Upload files",'upload')
            },
            multiple:true,
            upload: function(e){
                var files = e.files;
                $.each(files, function () {
                    if(this.size>10400000){
                        lims.initNotificationMes('单个文件的大小不能超过10M！',false);
                        e.preventDefault();
                        return;
                    }
                    var extension = this.extension.toLowerCase();
                    if(extension != ".png" && extension != ".bmp" && extension != ".jpeg"&&extension != ".jpg"){
                        lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!',false);
                        e.preventDefault();
                    }
                });
            },
            success: function(e){
                if(this.name.length > 100){
                    lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
                    e.preventDefault();
                    return;
                }
                if(e.response.typeEror){
                    $("#"+msg).html(lims.l("Wrong file type, the file will not be saved, please delete the re-upload png, bmp, jpeg file format!"));
                    return;
                }
                if(e.response.morSize){
                    $("#"+msg).html(lims.l("The file you uploaded more than 10M, re-upload, delete png, bmp, jpeg file format!"));
                    return;
                }
                if (e.operation == "upload") {
                    attachments.push(e.response.results[0]);
                    console.log(e.response.results[0]);
                    console.log(attachments.length);
                    if(attachments!=null&&attachments.length>0){
                        var img ="";
                        for(var k =0 ;k<attachments.length;k++){
                            var fileUrl = ""
                            var url = ""
                               if(attachments[k].type !=undefined&&attachments[k].file !=undefined&&attachments[k].file!=null){
                                   fileUrl =attachments[k].file;
                                   url = "data:"+attachments[k].type.rtName+";base64,"+attachments[k].file
                               }else if(attachments[k].url != null && attachments[k].url !=''){
                                   url = attachments[k].url;
                                   fileUrl =url;
                               }
                            if(url != ""){
                                img+="<div id='"+id+"_img_"+k+"'  style='position: relative;width: 128px;height: 128px;float: left;display: inline'>";
                                img+="<img id='"+id+"_"+k+"' src='"+url+"' style='width: 128px;height:128px;' >";
                                img+="<div class=deleteBtn onclick=fsn.facility.delSelectFacilityImg('"+id+"_"+k+"','"+fileUrl+"')>x";
                                img+="</div>";
                                img+="</div>";
                            }else{
                                break;
                            }
                        };
                        $("#"+id+"_img_s").show();
                        $("#"+id+"_img_s").html(img);
                        $("#"+id+"_img_e").show();
                        $("#"+id+"_div").html("<input id='"+id+"' type='file' />");
                        facility.buildUploadFacility(id, facility.imgResource, id+"_log");
                    }
                    $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
                }else if(e.operation == "remove"){
                    lims.log(e.response);
                    for(var i=0; i<attachments.length; i++){
                        if(attachments[i].name == e.files[0].name){
                            while((i+1)<attachments.length){
                                attachments[i]=attachments[i+1];
                                i++;
                            }
                            attachments.pop();
                            break;
                        }
                    }
                }
            },
            remove:function(e){
                $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
            },
            error:function(e){
                $("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
            }
        });
    };


    /**
     * 加载设备信息相关的grid列表
     * wb 2016.9.14
     *
     */
    facility.initFacilityInfoGrid = function(id,columns){

        //方法给个0参数，表示获取设备信息
        $("#" + id).kendoGrid({
            dataSource: [],
            filterable: {
                messages: fsn.gridfilterMessage(),
                operators: {
                    string: fsn.gridfilterOperators(),
                    date: fsn.gridfilterOperatorsDate(),
                    number: fsn.gridfilterOperatorsNumber()
                }
            },
            width:1050,
            sortable: true,
            resizable: true,
            selectable: true,
            pageable: {
                refresh: true,
                pageSizes: [10, 20,50,80,100],
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });

        facility.facility_dataSource();

    }
    /**
     * 加载查看养护记录grid列表
     * wb 2016.9.14
     *
     */
    facility.initEquipmentInfoGrid = function(id,columns){

        //方法给个0参数，表示获取设备信息
        $("#"+id).kendoGrid({
            dataSource: [],
            filterable: {
                messages: fsn.gridfilterMessage(),
                operators: {
                    string: fsn.gridfilterOperators(),
                    date: fsn.gridfilterOperatorsDate(),
                    number: fsn.gridfilterOperatorsNumber()
                }
            },
            width:1050,
            sortable: true,
            resizable: true,
            selectable: true,
            pageable: {
                refresh: true,
                pageSizes: [10, 20,50,80,100],
                messages: fsn.gridPageMessage()
            },
            columns: columns
        });
    };
    /**
     * 设备信息列表
     * wb 2.16.9.14
     * @type {{field: string, title: string, width: number, filterable: boolean}[]}
     */
    facility.facility_columns = [
        { field: "facilityName", title:"名称", width: 35, filterable:false},
        { field: "manufacturer", title:"生产厂家",width: 35 , filterable:false},
        { field: "facilityType", title:"型号", width: 20, filterable:false},
        { field: "facilityCount", title:"数量", width: 20, filterable:false},
        { field: "buyingTime", title:"采购时间", width: 38,template : '#= fsn.formatGridDate(buyingTime)#', filterable:false},
        { field: "application", title:"用途", width: 30, filterable:false},
        { field: "remark", title:"备注", width: 30, filterable:false},
        { field: "facilityArry", title:"养护记录", width: 30, filterable:false,
            template: function(e) {
                var checked = e.buyingTime;
                var tag = "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick=fsn.facility.facility_dataSource("+ e.id +",'"+checked+"')>查看</a>";
                return tag;
            }
        },
        { field: "resourceId", title:"设备图片", width: 30, filterable:false,
            //template: function(e) {
            //    var tag ="";
            //    if(e.resource!=null&&e.resource.url!=null){
            //        tag = "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick=fsn.facility.seefacilityImg('"+ e.resource.url +"')>查看</a>";
            //    }else{
            //        tag = "无图片"
            //    }
            //    return tag;
            //}
            command: [
                {
                    name: "review",
                    text: "<span class='k-icon k-review'></span>" + fsn.l("查看"),
                    click: function (e) {
                        e.preventDefault();
                        // 当前选中的认证信息所在的行 从 0 开始计 ，在验证认证信息是否重复时需要该标记
                        //var rowIndex = $(e.currentTarget).closest("tr").index();
                        var delItem = this.dataItem($(e.currentTarget).closest("tr"));


                        facility.seefacilityImg(delItem.facilityArry,1);
                    }
                }]
        },
        {title: fsn.l("Operation"),
            width: 58,
        command: [
        {
            name: "edit",
            text: "<span class='k-icon k-edit'></span>" + fsn.l("Edit"),
            click: function (e) {
                e.preventDefault();
                // 当前选中的认证信息所在的行 从 0 开始计 ，在验证认证信息是否重复时需要该标记
                //var rowIndex = $(e.currentTarget).closest("tr").index();
                var delItem = this.dataItem($(e.currentTarget).closest($(e.currentTarget).closest("tr")));
                console.log(delItem.facilityArry)
                facility.addFacilityInfo(delItem,1);
            }
        },{
                name: "delete",
                text: "<span class='k-icon k-delete'></span>" + fsn.l("Delete"),
                click: function (e) {
                    // 删除认证信息
                    e.preventDefault();
                    var delItem = this.dataItem($(e.currentTarget).closest("tr"));
                    facility.deleteFacilityInfo(delItem.id);

                }
            }]
        }
    ]


    /**
     * 养护记录列表
     * wb 2016.9.14
     * @type {{field: string, title: string, width: number, filterable: boolean}[]}
     */
    facility.maintenance_columns = [
        { field: "maintenanceName", title:"养护人", width: 35, filterable:false},
        { field: "maintenanceTime", title:"养护时间",width: 35 , template : '#= fsn.formatGridDate(maintenanceTime)#',filterable:false},
        { field: "maintenanceContent", title:"养护内容", width: 30, filterable:false},
        { field: "remark", title:"备注", width: 30, filterable:false},
        {title: fsn.l("Operation"),
            width: 30,
            command: [
                {
                    name: "edit",
                    text: "<span class='k-icon k-edit'></span>" + fsn.l("Edit"),
                    click: function (e) {
                        e.preventDefault();
                        // 当前选中的认证信息所在的行 从 0 开始计 ，在验证认证信息是否重复时需要该标记
                        //var rowIndex = $(e.currentTarget).closest("tr").index();
                        var delItem = this.dataItem($(e.currentTarget).closest("tr"));
                        facility.addMaintenanceInfo(delItem,1);
                    }
                },{
                    name: "delete",
                    text: "<span class='k-icon k-delete'></span>" + fsn.l("Delete"),
                    click: function (e) {
                        // 删除认证信息
                        e.preventDefault();
                        var delItem = this.dataItem($(e.currentTarget).closest("tr"));
                        facility.deleteMaintenanceInfo(delItem.id);

                    }
                }]
        }
    ]
    facility.facility_dataSource  = function(facilityId,checkedDate){
        var facilityParam = $("#facilityName").val();
        if(facilityId != undefined){
            $("#maintenance_id").show();
            $("#facility_id").hide();
            $("#m_facility_id").val(facilityId);
            if(checkedDate != undefined){
                $("#date_temp").val(checkedDate);
            }
            facilityParam = $("#maintenanceTime").val();
        }else{
            $("#facility_id").show();
            $("#maintenance_id").hide();

        };
        var dataSource = new kendo.data.DataSource({
            transport: {
                read : {
                    type : "GET",
                    data:{facilityParam:facilityParam},
                    url : function(options){
                        var url = null
                        if(facilityId != undefined){
                            url = portal.HTTP_PREFIX +"/facility/getByFacilityId/"+facilityId+"/"+ options.page + "/" + options.pageSize;
                        }else{
                            url = portal.HTTP_PREFIX +"/facility/facilityList/"+ options.page + "/" + options.pageSize;
                        };
                        return url
                    },
                    dataType : "json",
                    contentType : "application/json"
                }
            },
            schema: {
                data : function(data) {
                    return data.data;
                },
                total : function(data) {
                    return  data.total;//总条数
                }
            },
            batch : true,
            page:1,
            pageSize : 20, //每页显示个数
            serverPaging : true,
            serverFiltering : true,
            serverSorting : true
        });


        if(facilityId != undefined){
            var grid = $("#MAINTENANCE_RECORD_GRID").data("kendoGrid");
            grid.setDataSource(dataSource);
            grid.refresh();
        }else{
            var grid = $("#FACILITY_INVENTORY_GRID").data("kendoGrid");
            grid.setDataSource(dataSource);
            grid.refresh();

        };
    };
    facility.cleanInfo = function(){
        $("#facilityName").val("");
        $("#maintenanceTime").val("");
    };
    facility.addFacilityInfo = function(data,status){
        facility.imgResource.length = 0;
        if(data != undefined && data != 0 && status==1){
            $("#facilityInfo_id").val(data.id);
            $("#facility_Name").val(data.facilityName);
            $("#manufacturer").val(data.manufacturer);
            $("#facilityType").val(data.facilityType);
            $("#facilityCount").val(data.facilityCount);
            $("#buyingTime").val(fsn.formatGridDate(data.buyingTime));
            $("#application").val(data.application);
            $("#remark").val(data.remark);
            if(data.facilityArry != undefined && data.facilityArry !=null&&data.facilityArry.length>0) {
                facility.imgResource = facility.setFacilityArryData(data.facilityArry, 'upload_facility_files');
            }
        }else{
            $("#upload_facility_files_img_s").hide();
            $("#facilityInfo_id").val("");
            $("#facility_Name").val("");
            $("#manufacturer").val("");
            $("#facilityType").val("");
            $("#facilityCount").val("");
            $("#buyingTime").val("");
            $("#application").val("");
            $("#remark").val("");
        }
        $("#upload_facility_files_div").html("<input type='file' id='upload_facility_files'>")
        facility.buildUploadFacility("upload_facility_files", facility.imgResource , "upload_facility_files_log");
        $("#ADDFACILITYINFO_WIN").data("kendoWindow").open().center()
    };
    /**
     * 给图片对象初始化赋值
     * @param attachments
     */
    facility.setFacilityArryData = function(attachments,id){
        var img ="";
        for(var k = 0 ;k < attachments.length;k++){
            facility.imgResource.push(attachments[k]);
            var url = attachments[k].url;
            var fileUrl = "";
                if(attachments[k].url == null || attachments[k].url == ""){
                    if(attachments[k].type != undefined && attachments[k].file!=null){
                        url = "data:"+attachments[k].type.rtName+";base64,"+attachments[k].file;
                        fileUrl = attachments[k].file;
                    }
                }
              if(url != null && url!=""){
                  img+="<div id='"+id+"_img_"+k+"'  style='position: relative;width: 128px;height: 128px;float: left;display: inline'>";
                  img+="<img id='"+id+"_"+k+"' src='"+url+"' style='width: 128px;height:128px;'>";
                  img+="<div class=deleteBtn onclick=fsn.facility.delSelectFacilityImg('"+id+"_"+k+"','"+fileUrl+"')>x";
                  img+="</div>";
                  img+="</div>";
                  $("#"+id+"_img_s").show();
                  $("#"+id+"_img_s").html(img);
              }
        };
        return attachments;
    };
    facility.delSelectFacilityImg = function(id,url){
        if(facility.imgResource != null&&facility.imgResource.length>0){
            for(var s = 0 ;s < facility.imgResource.length;s++){
                if((facility.imgResource[s].file == null && facility.imgResource[s].url==url) || (facility.imgResource[s].url==null && facility.imgResource[s].file == url)){
                    $.extend(facility.imgResource[s],facility.imgResource[facility.imgResource.length-1]);
                    facility.imgResource.pop();
                    break;
                }
            }
        }
        $("img[id='"+id+"']").parent().remove();
      return false;
    };
    facility.closeAddFacilityInfo = function(status){
        if(status==0){
            $("#ADDFACILITYINFO_WIN").data("kendoWindow").close();
        }else{
            $("#MAINTENANCEINFO_WIN").data("kendoWindow").close();
        }
    };
    /**
     * 返回到设备信息页面
     *  @author: wu 2016.9.16
     */
    facility.back_FacilityInfo = function(){
        $("#facility_id").show();
        $("#maintenance_id").hide();
    };
    /**
     * 保存设备信息
     * @author: wu 2016.9.16
     */
    facility.submitFacilityInfo = function(){
        if(!facility.verifyFacilityInfo()){
            return ;
        }
        $("#k_window").data("kendoWindow").open().center();
        //$("#submitFacilityInfo").unbind("click");
        var facilityInfo = facility.createFacilityInfo();
        $.ajax({
            url: portal.HTTP_PREFIX + "/facility/facilitySaveOrEdit",
            type: "POST",
            data:JSON.stringify(facilityInfo),
            contentType: "application/json; charset=utf-8",
            success: function(returnVal){
                $("#k_window").data("kendoWindow").close();
                if(returnVal.status == true){
                    fsn.initNotificationMes("设备信息保存成功!", true);
                    facility.closeAddFacilityInfo(0);
                    facility.facility_dataSource();
                }
            }
        });
    };
    /**
     * 信息验证
     */
    facility.verifyFacilityInfo = function(){

        var facilityName = $("#facility_Name").val();
        if(facilityName == null || facilityName == ''){
            fsn.initNotificationMes("设备名称不能为空!", false);
            return false;
        }
        var manufacturer = $("#manufacturer").val();
        if(manufacturer == null || manufacturer == ''){
            fsn.initNotificationMes(" 生产厂家不能为空!", false);
            return false;
        }
        var facilityType = $("#facilityType").val();
        if(facilityType == null || facilityType == ''){
            fsn.initNotificationMes("设备型号不能为空!", false);
            return false;
        }
        var facilityCount = $("#facilityCount").val();
        if(facilityCount == null || facilityCount == ''){
            fsn.initNotificationMes("设备数量不能为空!", false);
            return false;
        }

        var buyingTime = $("#buyingTime").val().trim();

        if(buyingTime == null || buyingTime == ''){
            fsn.initNotificationMes("采购时间不能为空!", false);
            return false;
        }

        var result = buyingTime.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
        if(result==null){
            lims.initNotificationMes("日期格式不正确!", false);
            return false;
        }
        var application = $("#application").val();

        if(application == null || application == ''){
            fsn.initNotificationMes("设备用途不能为空!", false);
            return false;
        }
        return true;
    };

    facility.createFacilityInfo = function(){
        var facilityInfo = {
            id:$("#facilityInfo_id").val(),
            facilityName:$("#facility_Name").val(),
            manufacturer:$("#manufacturer").val(),
            facilityType:$("#facilityType").val(),
            facilityCount:$("#facilityCount").val(),
            buyingTime:$("#buyingTime").val(),
            application:$("#application").val(),
            remark:$("#remark").val(),
            facilityArry:facility.imgResource
        };
        return facilityInfo;
    };
    facility.deleteFacilityInfo = function(id){
        $.ajax({
            url: portal.HTTP_PREFIX + "/facility/deleteFacilityInfo/"+id,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            success: function(returnVal){
                if(returnVal.status == true){
                    fsn.initNotificationMes("设备信息删除成功!", true);
                    facility.facility_dataSource();
                }
            }
        });
    };

    facility.addMaintenanceInfo = function(data,status){
        if(data != undefined && data != 0 && status==1){
            $("#m_id").val(data.id);
            $("#maintenanceName").val(data.maintenanceName);
            $("#m_maintenanceTime").val(fsn.formatGridDate(data.maintenanceTime));
            $("#maintenanceContent").val(data.maintenanceContent);
            $("#m_remark").val(data.remark);
        }else{
            $("#m_id").val("");
            $("#maintenanceName").val("");
            $("#m_maintenanceTime").val("");
            $("#maintenanceContent").val("");
            $("#m_remark").val("");
        }
        $("#MAINTENANCEINFO_WIN").data("kendoWindow").open().center();
    }

    facility.createMaintenanceInfo = function(facilityId){
        var maintenanceInfo = {
            id:$("#m_id").val(),
            facilityId:facilityId,
            maintenanceName:$("#maintenanceName").val(),
            maintenanceTime:$("#m_maintenanceTime").val(),
            maintenanceContent:$("#maintenanceContent").val(),
            remark:$("#m_remark").val()
        };
        return maintenanceInfo;
    };

    facility.submitMaintenanceInfo = function() {
        var  maintenanceName = $("#maintenanceName").val();
        if(maintenanceName == null || maintenanceName == ''){
            fsn.initNotificationMes("养护人不能为空!", false);
            return;
        }
        var  maintenanceTime = $("#m_maintenanceTime").val();
        if(maintenanceTime == null || maintenanceTime == ''){
            fsn.initNotificationMes("养护时间不能为空!", false);
            return;
        }

        var result = maintenanceTime.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
        if(result==null){
            fsn.initNotificationMes("日期格式不正确!", false);
            return false;
        }

        var  maintenanceTimeTemp = new Date(maintenanceTime).getTime();

        var facilityDateTemp = $("#date_temp").val();
        if(maintenanceTimeTemp>=facilityDateTemp){
            lims.initNotificationMes("养护时间必须大于或者等于采购时间!", false);
            return false;
        }
        var maintenanceContent = $("#maintenanceContent").val();
        if(maintenanceContent == null || maintenanceContent == ''){
            fsn.initNotificationMes("养护内容不能为空!", false);
            return;
        }

        $("#k_window").data("kendoWindow").open().center();

        var facilityId = $("#m_facility_id").val();
        var maintenanceInfo = facility.createMaintenanceInfo(facilityId);
            $.ajax({
                url: portal.HTTP_PREFIX + "/facility/maintenanceSaveOrEdit",
                type: "POST",
                data: JSON.stringify(maintenanceInfo),
                contentType: "application/json; charset=utf-8",
                success: function (returnVal) {
                    $("#k_window").data("kendoWindow").close();
                    if (returnVal.status == true) {
                        facility.imgResource.length = 0;
                        fsn.initNotificationMes("养护记录保存成功!", true);
                        facility.closeAddFacilityInfo(1);
                        facility.facility_dataSource(facilityId);
                    }
                }
            });
    };
    facility.deleteMaintenanceInfo = function(id){
        $.ajax({
            url: portal.HTTP_PREFIX + "/facility/delMaintenanceInfo/"+id,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            success: function(returnVal){
                if(returnVal.status == true){
                    fsn.initNotificationMes("养护记录删除成功!", true);
                    var facilityId = $("#m_facility_id").val();
                    facility.facility_dataSource(facilityId);
                }
            }
        });
    };
    /**
     * 初始化规模信息
     */
    facility.initOperateInfo = function(){
        $.ajax({
            url: portal.HTTP_PREFIX + "/operate/findById",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            success: function (returnVal) {
                if (returnVal.status == true) {
                    facility.setOperateInfo(returnVal.data);
                }
            }
        });
    };
    facility.setOperateInfo = function(data){
        if(data == undefined || data == null){
          return false;
        }
        $("#operate_id").val(data.id);
        $("#operateType").val(data.operateType);
        $("#operateScope").val(data.operateScope);
        $("#personCount").val(data.personCount);
        $("#floorArea").val(data.floorArea);
    };
    facility.cleanOperateInfo = function(){
        $("#operateType").val("");
        $("#operateScope").val("");
        $("#personCount").val("");
        $("#floorArea").val("");
    };
    /**
     * 保存规模信息
     * wb 2016.9.17
     */
    facility.submitOperateInfo = function(){
        var operateType = $("#operateType").val();
        if(operateType==null||operateType==''){
            fsn.initNotificationMes("经营类型不能为空!", false);
            return;
        }
        var operateScope = $("#operateScope").val();
        if(operateScope==null||operateScope==''){
            fsn.initNotificationMes("经营规模不能为空!", false);
            return;
        }
        var personCount = $("#personCount").val();
        if(personCount==null||personCount==''){
            fsn.initNotificationMes("企业人数不能为空!", false);
            return;
        }
        var floorArea = $("#floorArea").val();
        if(floorArea==null||floorArea==''){
            fsn.initNotificationMes("占地面积不能为空!", false);
            return;
        }
        $("#k_window").data("kendoWindow").open().center();
        //$("#submitOperateInfo").unbind("click");
        var OperateInfo = facility.createOperateInfo();
        $.ajax({
            url: portal.HTTP_PREFIX + "/operate/saveOrEdit",
            type: "POST",
            data: JSON.stringify(OperateInfo),
            contentType: "application/json; charset=utf-8",
            success: function (returnVal) {
                $("#k_window").data("kendoWindow").close();
                if (returnVal.status == true) {
                    fsn.initNotificationMes("规模信息保存成功!", true);
                    facility.setOperateInfo(returnVal.data);
                    //facility.tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
                    //facility.tabStrip.select(3);        // Select by jQuery selector
                }
            }
        });
    };

    facility.createOperateInfo = function(){
       var  operateScope = $("#operateScope").val();
       // var  operateScope =  $("#operateScope").find("option:selected").text();//选中的文本
        var operateInfo = {
            id:$("#operate_id").val(),
            operateType:$("#operateType").val(),
            operateScope:$("#operateScope").val(),
            personCount:$("#personCount").val(),
            floorArea:$("#floorArea").val()
        };
        return operateInfo;
    };
    facility.editBusinessInfo = function(status){
        if(status == 0){
          $("#A_show_button").hide();
          $("#A_hide_button").show();
            $("#A_show").hide();
            $("#A_edit").show();
        }else if(status == 1){
            business_unit.setValue(business_unit.editBusiness, "produce");
            $("#A_show_button").show();
            $("#A_hide_button").hide();
            $("#A_show").show();
            $("#A_edit").hide();
      }else if(status == 2){
          $("#B_show_button").hide();
          $("#B_hide_button").show();
          $("#B_show").hide();
          $("#B_edit").show();
      }else{
            business_unit.setValue(business_unit.editBusiness, "produce");
            $("#B_show").show();
            $("#B_edit").hide();
            $("#B_show_button").show();
            $("#B_hide_button").hide();

      }
    };
    facility.seefacilityImg = function(imgs){

        var slides = $("#slides");
        var img ="<div class=\"slides_container\">";
        for(var i=0;i<imgs.length; i++)
        {
            img =img+ '<div class="slide"><img style="width: 849px;height: 638px" src="'+imgs[i].url+'"/></div>';

        }
        img =img+ '</div><a href="#" class="prev"><img src="../../resource/js/slides/img/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>'+
        '<a href="#" class="next"><img src="../../resource/js/slides/img/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>';
        slides.html(img);
        $('#slides').slides();
        $("#DIV_IMG_WIN").data("kendoWindow").open();

        //window.parent.location.href = imgUrl;
        //window.open ( imgUrl, "_blank" );
        //$("#DIV_IMG_ID").attr("src",imgUrl);
        //$("#DIV_IMG_WIN").data("kendoWindow").open().center()
    };

    //procurement.showQualifiedImg=function (imgs) {
    //
    //};
    facility.initialize();
})