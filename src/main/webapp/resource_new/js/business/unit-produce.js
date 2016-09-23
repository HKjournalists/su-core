$(document).ready(function() {
    var fsn = window.fsn = window.fsn || {};
    var business_unit = window.fsn.business_unit = window.fsn.business_unit || {};
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    business_unit.id = null;
    business_unit.name = null;
    fsn.business_send = false;
    business_unit.editLic = null;
    business_unit.flagLic = false;
    business_unit.orgExistsFlag = false;
    business_unit.aryOrgAttachments = new Array();
    business_unit.aryLicenseAttachments = new Array();
    business_unit.aryQsAttachments = new Array();
    business_unit.aryLogoAttachments = new Array();
    business_unit.certMap = new Map();
    business_unit.editQs = null;
    business_unit.editFlag = false;
    business_unit.editBusiness = null;
    business_unit.formetType = "-"; //用于用户输入qs号的分隔
    business_unit.formetLength = 12; //用于限制qs号的输入长度
    business_unit.formetValue = "QS"; // qs号的字母部分
    business_unit.formetId = 1; //qs规则的默认id
    fsn.imgMax = 2; //页面引导图片id
    fsn.src = "http://qa.fsnrec.com/portal/guid/unit/unit_produce_"; //引导原地址
    business_unit.initialize = function() {
        /* 初始化上传控件 */
        $("#upload_logo_div").html("<input id='upload_logo_files' type='file' />");
        business_unit.buildUpload("upload_logo_files", business_unit.aryLogoAttachments, "proFileMsgLogo");
        $("#upload_license_div").html("<input id='upload_license_files' type='file' />");
        business_unit.buildUpload("upload_license_files", business_unit.aryLicenseAttachments, "proFileMsgLicense");
        $("#upload_org_div").html("<input id='upload_orgnization_files' type='file' />");
        business_unit.buildUpload("upload_orgnization_files", business_unit.aryOrgAttachments, "proFileMsgOrg");
        $("#upload_certification_div").html("<input id='upload_certification_files' type='file' />");
        business_unit.buildUpload("upload_certification_files", business_unit.certMap, "hideMsg");
        // 事件绑定
        $("#btn_clearOrgFiles").bind("click", business_unit.clearOrgFiles);
        $("#btn_clearLicenseFiles").bind("click", business_unit.clearLicenseFiles);
        $("#btn_clearQsFiles").bind("click", business_unit.clearQsFiles);
        $("span[class='k-select']").bind("click", business_unit.changeComboboxStyle);
        //$("#qsFname").bind("click",business_unit.changeComboboxStyle);
        $("#btn_clearOrgFiles").hide();
        $("#btn_clearLicenseFiles").hide();
        $("#btn_clearQsFiles").hide();
        business_unit.initGrid("productionLic", business_unit.gridDataSource, business_unit.column, "toolbar_template");
        business_unit.initKendoWindow("k_window", "保存状态", "300px", "60px", false);
        business_unit.initKendoWindow("addProLicWindow", "", "1300px", "550px", false);
        business_unit.initKendoWindow("PromptWindow", "删除提示框", "450px", "100px", false);
        business_unit.initKendoWindow("wordWindow", "信息提示框", "450px", "100px", false);
        business_unit.initView();
        $("#addProLicWindow").data("kendoWindow").bind("close", function(){
             $(".provinceCityAll").css("display","none"); //关闭选择地址的样式
        });//点击关闭窗体时关闭地址选择框
        $("#save").bind("click", business_unit.save);
        $("#reset").bind("click", business_unit.reset);
        $("#addProLic").bind("click", business_unit.addProLic);
        $("#saveProLic").bind("click", business_unit.saveProLic);
        $("#sureWindow").bind("click",
        function() {
            $("#wordWindow").data("kendoWindow").close();
        })
        //失去焦点做提示
        $("#telephone").bind("blur", business_unit.validaTetelephone);
        $("#postalCode").bind("blur", business_unit.validaPostalCode);
        $("#email").bind("blur", business_unit.validateEmail);
        $("#bus_mainAddr").bind("blur",
        function() {
            verifyAddress("bus_mainAddr");
        });
        $("#license_mainAddr").bind("blur",
        function() {
            verifyAddress("license_mainAddr");
        });
        $("#org_mainAddr").bind("blur",
        function() {
            verifyAddress("org_mainAddr");
        });
        $("#accommodation_mainAddr").bind("blur",
        function() {
            verifyAddress("accommodation_mainAddr");
        });
        $("#qs_mainAddr").bind("blur",
        function() {
            verifyAddress("qs_mainAddr");
        });
        /*组织机构代码输入框失去焦点验证*/
        $("#orgCode").bind("blur",function() {
        	   var orgCode = $("#orgCode").val().trim();
        	   if(orgCode=="") return;
        	   if(business_unit.editBusiness==null) business_unit.editBusiness={};
        	   var orgId = business_unit.editBusiness.organization;
        	   business_unit.verificationOrgCode(orgCode,orgId);
        });
    };
    
    /**
     * 通过企业id和qszhenghao
     */
    business_unit.getPro2Bus = function(qsNo) {
        var pro2Bus = 0;
        if(qsNo!=null&&qsNo!=""){
        	$.ajax({
                url: portal.HTTP_PREFIX + "/business/getCountByQs/" + qsNo,
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnValue) {
                    pro2Bus = returnValue.data;
                }
            });
        }
        return pro2Bus;
    };

    business_unit.column = [{
        field: "qsNo",
        title: lims.l("生产许可证编号"),
        width: 40,
        height: 30
    },
    {
        field: "busunitName",
        title: lims.l("生产企业名称"),
        width: 90,
        height: 30
    },
    {
        field: "productName",
        title: lims.l("产品名称"),
        width: 40,
        height: 30
    },
    {
        command: [{
            name: "edit",
            text: lims.localized("编辑"),
            click: function(e) {
                e.preventDefault();
                var editRow = $(e.target).closest("tr");
                var temp = this.dataItem(editRow);
                business_unit.editQs = temp;
                business_unit.editFlag = true;
                business_unit.viewProLic(temp);
            }
        },
        {
            text: lims.localized("删除"),
            click: function(e) {
                e.preventDefault();
                var editRow = $(e.target).closest("tr");
                var temp = this.dataItem(editRow);
                var pro2Bus = business_unit.getPro2Bus(temp.qsNo);
                if (pro2Bus > 0) {
                    lims.initNotificationMes('生产许可证编号已被企业绑定，不能删除！', false);
                } else {
                    $("#PromptWindow").data("kendoWindow").open().center();
                    $("#confirmWindow").unbind("click");
                    $("#cancelWindow").unbind("click");
                    $("#confirmWindow").bind("click",
                    function() {
                        $("#productionLic").data("kendoGrid").dataSource.remove(temp);
                        $("#PromptWindow").data("kendoWindow").close();
                    });
                    $("#cancelWindow").bind("click",
                    function() {
                        $("#PromptWindow").data("kendoWindow").close();
                    });
                }
            }
        },
        ],
        title: lims.l("Operation"),
        width: 50
    }];
    business_unit.gridDataSource = new kendo.data.DataSource({
        data: [],
        batch: true,
        page: 1,
        serverPaging: true,
        serverFiltering: true,
        serverSorting: true
    });
    business_unit.initGrid = function(formId, ds, column, toolbarId) {
        $("#" + formId).kendoGrid({
            dataSource: ds == null ? [] : ds,
            selectable: true,
            pageable: {
                messages: lims.gridPageMessage(),
            },
            toolbar: [{
                template: kendo.template($("#" + toolbarId).html())
            }],
            columns: column,
        });
    };

    //点击添加按钮打开生产许可证窗口
    business_unit.addProLic = function() {
        $("#qs_msg").hide();
        var addFormat = $("#qsFname").data("kendoDropDownList");
        addFormat.readonly(false);
        $("#qsNo").attr("readonly",false);
        addFormat.dataSource.read();
        addFormat.value(1);
        var item = addFormat.dataItem(0);
        business_unit.formetType = item.formetType;
        business_unit.formetLength = item.formetLength;
        business_unit.formetValue = item.formetValue;
        $("#showQsFormat").html(item.formetValue);
        business_unit.editFlag = false;
        //如果是仁怀的只能上传一个生产许可证
        var total = $("#productionLic").data("kendoGrid").dataSource.total();
        if (business_unit.editBusiness != null && business_unit.editBusiness.type == "仁怀市白酒生产企业" && total > 0) {
            lims.initNotificationMes("当前仁怀市白酒生产企业只能上传一条生产许可证信息，否则企业登记表中无法展示。", false);
            return;
        }
        business_unit.clearProLic();
        //初始化生产许可证图片上传控件
        $("#upload_qs_div").html("");
        $("#upload_qs_div").html("<input id='upload_qs_files' type='file' />");
        business_unit.buildUpload("upload_qs_files", business_unit.aryQsAttachments, "proFileMsgQs");
        $("#addProLicWindow").data("kendoWindow").open().center();
    };
    //清空生产许可证窗口里面的内容
    business_unit.clearProLic = function() {
        $("#qsNo").val("");
        $("#busunitName").val("");
        $("#productName").data("kendoDropDownList").value("");
        $("#qsStartTime").data("kendoDatePicker").value("");
        $("#qsEndTime").data("kendoDatePicker").value("");
        $("#accommodation_mainAddr").val("");
        $("#accommodation_streetAddress").val("");
        $("#qs_mainAddr").val("");
        $("#qs_streetAddress").val("");
        $("#checkType").val("");
        $("#addQs").show();
        $("#btn_clearQsFiles").hide();
        business_unit.aryQsAttachments = [];
        if ($("#qsAttachmentsListView").data("kendoListView")) {
            $("#qsAttachmentsListView").data("kendoListView").dataSource.data([]);
        }
    };
    //点击编辑按钮查看生产许可证信息
    business_unit.viewProLic = function(viewItem) {
        business_unit.changeComboboxStyle();
        var qsFormat = $("#qsFname").data("kendoDropDownList");
        qsFormat.value(viewItem.qsnoFormat != null ? viewItem.qsnoFormat.id: 1);
        var item = qsFormat.dataItem();
        business_unit.formetType = item.formetType;
        business_unit.formetLength = item.formetLength;
        business_unit.formetValue = item.formetValue;
        $("#showQsFormat").html(item.formetValue);
        $("#qsNo").val(viewItem.qsNo.replace(item.formetValue, ""));
        $("#busunitName").val(viewItem.busunitName);
        $("#productName").data("kendoDropDownList").value(viewItem.productName);
        $("#qsStartTime").data("kendoDatePicker").value(viewItem.startTime != null ? viewItem.startTime.substr(0, 10) : "");
        $("#qsEndTime").data("kendoDatePicker").value(viewItem.endTime != null ? viewItem.endTime.substr(0, 10) : "");
        setAddressValue(viewItem.accommodation, viewItem.accOtherAddress, "accommodation_mainAddr", "accommodation_streetAddress");
        setAddressValue(viewItem.productionAddress, viewItem.proOtherAddress, "qs_mainAddr", "qs_streetAddress");
        $("#checkType").val(viewItem.checkType);
        $("#addQs").show();
        $("#btn_clearQsFiles").hide();
        business_unit.setQsAttachments(viewItem.qsAttachments);
        var pro2Bus = business_unit.getPro2Bus(viewItem.qsNo);
        $("#qs_msg").hide();
        qsFormat.readonly(false);
        $("#qsNo").attr("readonly", false);
        if (pro2Bus > 0) {
            qsFormat.readonly();
            $("#qsNo").attr("readonly", "readonly");
            $("#qs_msg").show();
        }
        $("#addProLicWindow").data("kendoWindow").open().center();
    };

    //保存生产许可证信息
    business_unit.saveProLic = function() {
        if (!business_unit.validateMyDate()) {
            return;
        }
        var firstPart = $("#showQsFormat").html().indexOf("?");
        if (firstPart!=-1) {
            lims.initNotificationMes("您选择的输入规则没有加载出正确的省份简称，请在企业基本信息中完善企业地址保存后再试！", false);
            return;
        }
        var qs_formatId = $("#qsFname").data("kendoDropDownList")._old;
        var qsnoFormat = {
            id: qs_formatId
        }
        if (business_unit.editFlag) {
            //编辑
        	var newQSNo = $("#showQsFormat").html() + $("#qsNo").val().trim();
        	if(business_unit.editQs.qsNo != newQSNo){
        		business_unit.editQs.oldQSNo = business_unit.editQs.qsNo;
        	}
            business_unit.editQs.qsNo = newQSNo;
            business_unit.editQs.busunitName = $("#busunitName").val().trim();
            business_unit.editQs.productName = $("#productName").data("kendoDropDownList").value().trim();
            business_unit.editQs.startTime = $("#qsStartTime").val().trim();
            business_unit.editQs.endTime = $("#qsEndTime").val().trim();
            business_unit.editQs.accommodation = $("#accommodation_mainAddr").val().trim().replace(/-/g, "") + $("#accommodation_streetAddress").val().trim();
            business_unit.editQs.accOtherAddress = $("#accommodation_mainAddr").val().trim() + "--" + $("#accommodation_streetAddress").val().trim();
            business_unit.editQs.productionAddress = $("#qs_mainAddr").val().trim().replace(/-/g, "") + $("#qs_streetAddress").val().trim();
            business_unit.editQs.proOtherAddress = $("#qs_mainAddr").val().trim() + "--" + $("#qs_streetAddress").val().trim();
            business_unit.editQs.checkType = $("#checkType").val().trim();

            business_unit.editQs.qsnoFormat = qsnoFormat;
            business_unit.editQs.qsAttachments = business_unit.aryQsAttachments;
            business_unit.editFlag = false;
            $("#productionLic").data("kendoGrid").refresh();
            $("#wordWindow").data("kendoWindow").open().center();
        } else {
            //新增
            var productionLicense = {
                qsNo: $("#showQsFormat").html() + $("#qsNo").val().trim(),
                busunitName: $("#busunitName").val().trim(),
                productName: $("#productName").data("kendoDropDownList").value().trim(),
                startTime: $("#qsStartTime").val().trim(),
                endTime: $("#qsEndTime").val().trim(),
                accommodation: $("#accommodation_mainAddr").val().trim().replace(/-/g, "") + $("#accommodation_streetAddress").val().trim(),
                accOtherAddress: $("#accommodation_mainAddr").val().trim() + "--" + $("#accommodation_streetAddress").val().trim(),
                productionAddress: $("#qs_mainAddr").val().trim().replace(/-/g, "") + $("#qs_streetAddress").val().trim(),
                proOtherAddress: $("#qs_mainAddr").val().trim() + "--" + $("#qs_streetAddress").val().trim(),
                checkType: $("#checkType").val().trim(),
                qsAttachments: business_unit.aryQsAttachments,
                qsnoFormat: qsnoFormat,
            };
            $("#productionLic").data("kendoGrid").dataSource.add(productionLicense);
            $("#productionLic").data("kendoGrid").refresh();
        }
        business_unit.aryQsAttachments = [];
        $("#addProLicWindow").data("kendoWindow").close();
    };

    business_unit.initView = function() {
        isNew = false;
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/getBusinessByOrg",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(returnValue) {
                business_unit.editBusiness = returnValue.data;
                business_unit.setValue(returnValue.data, "produce");
                fsn.initialCertification(returnValue.data.listOfCertification);
            }
        });
    };
    //封装生产许可证信息
    business_unit.createProductionLicense = function() {
        var items = [];
        var girdItems = $("#productionLic").data("kendoGrid").dataSource.data();
        var j = 0;
        for (var i = 0; i < girdItems.length; i++) {
            if (girdItems[i].qsNo.trim() == "") {
                j++;
                continue;
            }
            items[i - j] = {
                qsNo: girdItems[i].qsNo,
                busunitName: girdItems[i].busunitName,
                productName: girdItems[i].productName,
                startTime: girdItems[i].startTime,
                endTime: girdItems[i].endTime,
                accommodation: girdItems[i].accommodation,
                accOtherAddress: girdItems[i].accOtherAddress,
                productionAddress: girdItems[i].productionAddress,
                proOtherAddress: girdItems[i].proOtherAddress,
                checkType: girdItems[i].checkType,
                qsAttachments: girdItems[i].qsAttachments,
                qsnoFormat: girdItems[i].qsnoFormat,
                oldQSNo: girdItems[i].oldQSNo
            };
        }
        return items;
    };

    function Cert() {
        var setName = function(name) {
            this.name = name;
        };
        this.name;
        this.setName = setName;
    }
    function CertResource() {
        var set = function(name, url) {
            this.name = name;
            this.url = url;
        };
        this.name;
        this.url;
        this.set = set;
    }

    // 定义一个map集合，用来存放认证信息的图片
    function Map() {
        var struct = function(key, value) {
            this.key = key;
            this.value = value;
        };
        var put = function(key, value) {
            for (var i = 0; i < this.arr.length; i++) {
                if (this.arr[i].key === key) {
                    this.arr[i].value = value;
                    return;
                }
            };
            this.arr[this.arr.length] = new struct(key, value);
        };
        var get = function(key) {
            for (var i = 0; i < this.arr.length; i++) {
                if (this.arr[i].key === key) {
                    return this.arr[i].value;
                }
            }
            return null;
        };
        var remove = function(key) {
            var v;
            for (var i = 0; i < this.arr.length; i++) {
                v = this.arr.pop();
                if (v.key === key) {
                    continue;
                }
                this.arr.unshift(v);
            }
        };
        var size = function() {
            return this.arr.length;
        };
        var isEmpty = function() {
            return this.arr.length <= 0;
        };
        this.arr = new Array();
        this.get = get;
        this.put = put;
        this.remove = remove;
        this.size = size;
        this.isEmpty = isEmpty;
    }

    //改变qs号格式选择的下拉列表
    business_unit.changeComboboxStyle = function() {

        var qsFname_list = document.getElementById("qsFname-list");
        $("#qsFname-list").animate({
            width: '300px',
        });
        //qsFname_list.style.width="280px";
        var qsFname_listbox = document.getElementById("qsFname_listbox");
        qsFname_listbox.style.width = "300px";
    }

    // 封装认证信息
    business_unit.getCerts = function() {
        var busUnitCerts = [];
        var certs = $("#certification-grid").data("kendoGrid").dataSource.data();
        var j = 0;
        for (var i = 0; i < certs.length; i++) {
            if (certs[i].name == "") {
                j++;
                continue;
            }
            busUnitCerts[i - j] = {
                id: certs[i].id == null ? "": certs[i].id,
                cert: new Cert(),
                certResource: business_unit.certMap.get(i),
                endDate: certs[i].endDate,
            };
            busUnitCerts[i - j].cert.setName(certs[i].cert);
            if (busUnitCerts[i - j].certResource == null) {
                var certResource = new CertResource();
                certResource.set(certs[i].certResource, certs[i].documentUrl);
                busUnitCerts[i - j].certResource = certResource;
            }
        }
        return busUnitCerts;
    };

    business_unit.createInstance = function() {

        /* 营业执照信息  */
        var license = {
            licenseNo: $("#licenseNo").val().trim(),
            licensename: $("#licenseName").val().trim(),
            legalName: $("#legalName").val().trim(),
            startTime: $("#licenseStartTime").val().trim(),
            endTime: $("#licenseEndTime").val().trim(),
            registrationTime: $("#registrationTime").val().trim(),
            issuingAuthority: $("#issuingAuthority").val().trim(),
            subjectType: $("#subjectType").val().trim(),
            businessAddress: $("#license_mainAddr").val().trim().replace(/-/g, "") + $("#license_streetAddress").val().trim(),
            otherAddress: $("#license_mainAddr").val().trim() + "--" + $("#license_streetAddress").val().trim(),
            toleranceRange: $("#toleranceRange").val().trim(),
            registeredCapital: $("#registeredCapital").val().trim(),
        };
        /* 组织机构代码信息  */
        var orgInstitution = {
            orgCode: $("#orgCode").val().trim(),
            orgName: $("#orgName").val().trim(),
            startTime: $("#orgStartTime").val().trim(),
            endTime: $("#orgEndTime").val().trim(),
            unitsAwarded: $("#unitsAwarded").val().trim(),
            orgType: $("#orgType").val().trim(),
            orgAddress: $("#org_mainAddr").val().trim().replace(/-/g, "") + $("#org_streetAddress").val().trim(),
            otherAddress: $("#org_mainAddr").val().trim() + "--" + $("#org_streetAddress").val().trim(),
        };
        /* 生产许可证信息  */
        var productionLicenses = business_unit.createProductionLicense();

        var subBusiness = {
            id: business_unit.id,
            name: $("#name").val().trim(),
            address: $("#bus_mainAddr").val().trim().replace(/-/g, "") + $("#bus_streetAddress").val().trim(),
            otherAddress: $("#bus_mainAddr").val().trim() + "--" + $("#bus_streetAddress").val().trim(),
            personInCharge: $("#personInCharge").val().trim(),
            contact: $("#contact").val().trim(),
            telephone: $("#telephone").val().trim(),
            postalCode: $("#postalCode").val().trim(),
            email: $("#email").val().trim(),
            fax: $("#fax").val().trim(),
            about: $("#product_about").val().trim(),
            website: $("#website").val().trim(),
            license: license,
            orgInstitution: orgInstitution,
            logoAttachments: business_unit.aryLogoAttachments,
            orgAttachments: business_unit.aryOrgAttachments,
            licAttachments: business_unit.aryLicenseAttachments,
            productionLicenses: productionLicenses,
            listOfCertification: business_unit.getCerts(),
            taxRegAttachments: null,
            liquorAttachments: null,
        };
        return subBusiness;
    };

    /* 保存 */
    business_unit.save = function() {
        /* 校验数据的有效性  */
        fsn.clearErrorStyle();
        if (!business_unit.validateFormat()) {
            return;
        }
        /*校验其他认证信息*/
        if (!business_unit.validateCert()) {
            return;
        }
        // 数据封装
        var subBusiness = business_unit.createInstance();
        $("#winMsg").html("正在修改数据，请稍候....");
        $("#k_window").data("kendoWindow").open().center();
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/business-unit",
            type: "PUT",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(subBusiness),
            success: function(returnVal) {
                $("#k_window").data("kendoWindow").close();
                if (returnVal.result.status == "true") {
                    lims.initNotificationMes("企业信息修改成功", true);
                    business_unit.setOrgAttachments(returnVal.data.orgAttachments);
                    business_unit.setLicenseAttachments(returnVal.data.licAttachments);
                    business_unit.setQsAttachments(returnVal.data.qsAttachments);
                    business_unit.setLogoAttachments(returnVal.data.logoAttachments);
                    $("#productionLic").data("kendoGrid").dataSource.data(returnVal.data.productionLicenses);
                    $("#productionLic").data("kendoGrid").refresh();
                    fsn.initialCertification(returnVal.data.listOfCertification);
                } else {
                    lims.initNotificationMes("企业信息修改失败", false);
                }
            }
        });
    };

    /* 校验数据的有效性 */
    business_unit.validateFormat = function() {
        if (!business_unit.validateCommon()) {
            return false;
        }
        return true;
    };

    business_unit.validateMyDate = function() {
        if ($("#qsNo").val().trim() == "") {
            lims.initNotificationMes('【生产许可证信息】中的证书编号不能为空！', false);
            return false;
        }
        if ($("#qsFname").val().trim() == "") {
            lims.initNotificationMes('【生产许可证信息】中的证书编号填写格式不能正确！', false);
            return false;
        }

        var qsNo = ($("#qsNo").val().trim()).replace(/\s*/g, "").replace(/-/g, "").replace(/\s+/g, "");
        var formatlength = business_unit.formetLength;
        var status = /^[0-9]*$/.test(qsNo);
        if (!status) {
            lims.initNotificationMes("【生产许可证信息】中的证书编号格式不正确！。", false);
            return false;
        }
        if (qsNo.length != formatlength) {
            lims.initNotificationMes("【生产许可证信息】中的证书编号格式不正确！。", false);
            return false;
        }

        if ($("#busunitName").val().trim() == "") {
            lims.initNotificationMes('【生产许可证信息】中的企业名称不能为空！', false);
            return false;
        }
        if ($("#productName").val().trim() == "" || $("#productName").val().trim() == "--请选择分类--") {
            lims.initNotificationMes('【生产许可证信息】中的产品名称不能为空！', false);
            return false;
        }
        if ($("#qs_mainAddr").val().trim() == "") {
            lims.initNotificationMes('【生产许可证信息】中的生产地址不能为空！', false);
            return false;
        }
        if ($("#qs_streetAddress").val().trim() == "") {
            lims.initNotificationMes('【生产许可证信息】中的生产地址的街道地址不能为空！', false);
            return false;
        }
        if (!fsn.validateMustDate("qsStartTime", "生产许可证的起始日期")) {
            return false;
        }
        if (!fsn.validateMustDate("qsEndTime", "生产许可证的截止日期")) {
            return false;
        }
        var startDate = $("#qsStartTime").data("kendoDatePicker").value();
        var endDate = $("#qsEndTime").data("kendoDatePicker").value();
        if ((endDate - startDate) < 1) {
            lims.initNotificationMes('生产许可证的起始日期不能大于或等于截止日期！', false);
            return false;
        }
        if (business_unit.aryQsAttachments.length < 1) {
            lims.initNotificationMes('请上传生产许可证图片！', false);
            return false;
        }

        return true;
    };
    //格式化qs号码
    business_unit.formatQsNo = function(obj, value) {
        var result = [];
        $("#qsNo").attr("maxlength", business_unit.formetLength + 2);
        var type = business_unit.formetType;
        switch (business_unit.formetLength) {
        case 12:
            // 格式：QSxx-xxxxx-xxxxx  2-5-5
            for (var i = 0; i < value.length; i++) {
                if (type == "-") {
                    var qs_No = $("#qsNo").val();
                    lastChar = qs_No.substring(qs_No.length - 1);
                    if (i == 2 && $("#qsNo").val().trim().length == 3 && lastChar != "-") {
                        result.push(type + value.charAt(i));
                    } else if (i == 8 && $("#qsNo").val().trim().length == 9 && lastChar != "-") {
                        result.push(type + value.charAt(i));
                    } else {
                        result.push(value.charAt(i));
                    }
                } else {
                    value = value.replace(/\s*/g, "");
                    if (i % 4 == 0 && i != 0) {
                        result.push(type + value.charAt(i));
                    } else {
                        result.push(value.charAt(i));
                    }
                }
            }
            obj.value = result.join("");
            break;
        case 10:
            //格式：XKxx-xxx-xxxxx)
            for (var i = 0; i < value.length; i++) {
                var qs_No = $("#qsNo").val();
                lastChar = qs_No.substring(qs_No.length - 1);
                if (i == 2 && $("#qsNo").val().trim().length == 3 && lastChar != "-") {
                    result.push(type + value.charAt(i));
                } else if (i == 6 && $("#qsNo").val().trim().length == 7 && lastChar != "-") {
                    result.push(type + value.charAt(i));
                } else {
                    result.push(value.charAt(i));
                }
            }
            obj.value = result.join("");
            break;
        default:
            ;
        }
    }

    //加载产品分类下拉列表的 DataSource
    business_unit.listCategoryDataSource = function(url) {
        return new kendo.data.DataSource({
            transport: {
                read: {
                    type: "GET",
                    dataType: "json",
                    async: true,
                    contentType: "application/json",
                    url: portal.HTTP_PREFIX + url,
                }
            },
            schema: {
                data: function(d) {
                    return d.data;
                }
            }
        });
    }

    //将用户选择的信息填到input框中
    business_unit.onSelectProName = function(e) {
        var productName = this.dataItem(e.item.index());
        $("#productName").data("kendoDropDownList").value(productName);
    }

    business_unit.changeQsFormat = function(e) {
        var format = this.dataItem(e.item.index());
        var oldValue = $("#qsFname").data("kendoDropDownList").value();
        if(oldValue!=format.id){
            $("#qsNo").val("");
        }
        var formatId = format.id;
        $("#showQsFormat").html(format.formetValue);
        business_unit.formetType = format.formetType;
        business_unit.formetValue = format.formetValue;
        business_unit.formetLength = format.formetLength;
    }

    business_unit.listCategory = business_unit.listCategoryDataSource("/product/loadProduCtcategory"); // 加载产品名称分类
    business_unit.listFormatQs = business_unit.listCategoryDataSource("/product/loadlistFormatqs"); // 加载企业qs号绑定标准
    //初始化初始化产品名称分类的下啦列表    
    $("#productName").kendoDropDownList({
        dataTextField: "name",
        dataValueField: "name",
        dataSource: business_unit.listCategory != null ? business_unit.listCategory: [],
        optionLabel: "--请选择分类--",
        select: business_unit.onSelectProName,
    });

    //初始化初始化qs格式选择的下啦列表      
    $("#qsFname").kendoDropDownList({
        dataTextField: "formetName",
        dataValueField: "id",
        dataSource: business_unit.listFormatQs != null ? business_unit.listFormatQs: [],
        select: business_unit.changeQsFormat,
    });

    /* 初始化时间控件  */
    $("#licenseStartTime,#licenseEndTime,#orgStartTime," + "#orgEndTime,#registrationTime,#qsStartTime," + "#qsEndTime,#test_time_format").kendoDatePicker({
        format: "yyyy-MM-dd",
        height: 30,
        culture: "zh-CN",
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

    /*其他认证信息的截止日期*/
    fsn.buildDatePicker = function(container, options) {
        var input = $("<input/>");
        input.attr("name", options.field);
        input.appendTo(container);
        input.kendoDatePicker({
            format: "yyyy-MM-dd",
            height: 30,
            value: options.model.endDate == "" ? null: new Date(options.model.endDate),
            culture: "zh-CN",
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
    };

    /*验证认证信息关联的产品数量*/
    fsn.validateBusUnitCert = function(busCertId) {
        var count = -1;
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/countProductByBusinessCertId/" + busCertId,
            type: "GET",
            async: false,
            success: function(data) {
                if (data.result.status == "true") {
                    count = data.count;
                    if (count > 0) {
                        lims.initNotificationMes("当前有" + count + "条产品关联到该认证，不允许删除！", false);
                    }
                } else {
                    lims.initNotificationMes("验证该认证信息是否关联产品时后台出现异常！" + data.result.errorMessage, false);
                }
            },
        });
        return count;
    };

    // 初始化认证信息
    fsn.initialCertification = function(cerDs) {
        if (cerDs == null || cerDs.length < 1) {
            cerDs = [];
        } else {
            if (cerDs[0].certResource != null && cerDs[0].certResource.name != "暂未上传...") {
                $("#upload_cert").hide();
            }
            for (var i = 0; i < cerDs.length; i++) {
                cerDs[i].cert = cerDs[i].cert.name;
                if (cerDs[i].certResource != null) {
                    cerDs[i].documentUrl = cerDs[i].certResource.url;
                    cerDs[i].certResource = cerDs[i].certResource.name;
                } else {
                    cerDs[i].certResource = "";
                }
                //dataSource_[i].endDate = dataSource_[i].endDate.substring(0,10);
            }
        }
        /*其他认证信息如果存在就销毁改grid*/
        if ($("#certification-grid").data("kendoGrid")) {
            $("#certification-grid").data("kendoGrid").destroy();
        }

        /*初始化其他认证信息grid*/
        $("#certification-grid").kendoGrid({
            dataSource: cerDs == null ? [] : new kendo.data.DataSource({
                data: cerDs,
                page: 1,
                pageSize: 1000
            }),
            navigatable: true,
            editable: true,
            pageable: {
                messages: lims.gridPageMessage(),
            },
            toolbar: [{
                template: kendo.template($("#toolbar_template_cert").html())
            }],
            columns: [{
                fild: "id",
                title: "id",
                editable: false,
                width: 1
            },
            {
                field: "cert",
                title: "认证类别",
                editor: certNameDropDownEditor,
                width: 60
            },
            {
                field: "certResource",
                title: "上传图片名称",
                editor: certResourceEditor,
                width: 60
            },
            {
                field: "endDate",
                title: "有效期截止时间（如：2014-01-01）",
                editor: fsn.buildDatePicker,
                template: '#=endDate=fsn.formatGridDate(endDate)#',
                width: 50
            },
            {
                command: [{
                    name: "Remove",
                    text: "<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
                    click: function(e) {
                        e.preventDefault();
                        var delItem = this.dataItem($(e.currentTarget).closest("tr"));
                        if (delItem.id != null) {
                            var count = fsn.validateBusUnitCert(delItem.id);
                            if (count == 0) {
                                $("#certification-grid").data("kendoGrid").dataSource.remove(delItem);
                            }
                        } else {
                            $("#certification-grid").data("kendoGrid").dataSource.remove(delItem);
                        }

                    }
                }],
                title: fsn.l("Operation"),
                width: 30
            }],
        });

        /*认证信息gird的单元格的编辑事件*/
        function certResourceEditor(container, options) {
            if (options.model.documentUrl) {
                window.open(options.model.documentUrl);
                return;
            }
            $("<span>" + options.model.certResource + "</span>").appendTo(container);
        };

        fsn.addCert = function() {
            $("#upload_cert ul.k-upload-files").remove();
            var certifications = $("#certification-grid").data("kendoGrid").dataSource.data();
            if (certifications.length < 1) {
                $("#certification-grid").data("kendoGrid").dataSource.add({
                    cert: "",
                    certResource: "暂未上传...",
                    endDate: ""
                });
                $("#upload_cert").show();
                return false;
            }
            if (certifications[certifications.length - 1].cert == "" || certifications[certifications.length - 1].cert == "请选择...") {
                lims.initNotificationMes("请选择上传认证列表第" + certifications.length + "行的【认证类型】！", false);
                return false;
            }
            var endDate = certifications[certifications.length - 1].endDate;
            //var validateEndDate = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(endDate);
            if (!endDate) {
                lims.initNotificationMes("请正确填写认证列表第" + certifications.length + "行的【有效期截止时间】！", false);
                return false;
            }
            if (certifications[certifications.length - 1].certResource == "暂未上传...") {
                lims.initNotificationMes("请先上传认证列表第" + certifications.length + "行的认证图片！", false);
                return false;
            }
            $("#certification-grid").data("kendoGrid").dataSource.add({
                cert: "",
                certResource: "暂未上传...",
                endDate: ""
            });
            $("#upload_cert").show();
            return true;
        };

        /*标准认证的数据源*/
        fsn.stdCertDS = new kendo.data.DataSource({
            transport: {
                read: portal.HTTP_PREFIX + "/product/getStandCertifications"
            },
            schema: {
                data: function(returnValue) {
                    return returnValue.data; //响应到页面的数据
                }
            }
        });

        /*其他认证信息的下拉选项*/
        function certNameDropDownEditor(container, options) {
            if (fsn.stdCertDS == null) {
                fsn.stdCertDS.read();
            }
            $('<input required data-text-field="name" data-value-field="name" data-bind="value:' + options.field + '"/>').appendTo(container).kendoDropDownList({
                autoBind: false,
                optionLabel: "请选择...",
                dataTextField: "name",
                dataValueField: "id",
                dataSource: fsn.stdCertDS,
                index: 0,
            });
        };
    };

    //校验地址格式是否正确
    verifyAddress = function(id) {
        //判断地址选择窗口是否打开，若是没开打则检验
        if ($(".provinceCityAll").is(":hidden")) {
            var text = $("#" + id).val().trim();
            if (text == "") {
                return;
            } //若为空则不进行校验
            var strs = new Array();
            strs = text.split("-"); //分割字符串
            if (strs.length < 2 || strs.length > 3) { //判断字符串数是否为3位
                lims.initNotificationMes('格式只能为：</br>【省-市】或【省-市-区（县）】</br>请重新填写！', false);
                $("#" + id).val("");
                return;
            } else {
                //判断每个字符串是否有为空
                for (var i = 0; i < strs.length; i++) {
                    if (strs[i].trim() == "") {
                        lims.initNotificationMes('省、市、区（县）不能为空</br>请重新填写！', false);
                        $("#" + id).val("");
                        return;
                    }
                }
                var reg = /^[\u4e00-\u9fa5]{1,10}$/g; //校验是否全为汉字
                var str = "";
                if (strs.length < 2) {
                    str = strs[0].trim() + strs[1].trim() + strs[2].trim();
                } else {
                    str = strs[0].trim() + strs[1].trim();
                }
                if (!reg.test(str)) {
                    lims.initNotificationMes('只能输入汉字字符，请重新填写！', false);
                    $("#" + id).val("");
                    return;
                }
            }
        }
    };

    //初始化验证器
    $("#name").kendoValidator().data("kendoValidator");
    $("#licenseNo").kendoValidator().data("kendoValidator");
    $("#personInCharge").kendoValidator().data("kendoValidator");
    $("#email").kendoValidator().data("kendoValidator");
    $("#licenseName").kendoValidator().data("kendoValidator");
    $("#legalName").kendoValidator().data("kendoValidator");
    $("#orgCode").kendoValidator().data("kendoValidator");
    $("#orgName").kendoValidator().data("kendoValidator");
    $("#bus_mainAddr").kendoValidator().data("kendoValidator");
    $("#org_mainAddr").kendoValidator().data("kendoValidator");
    $("#license_mainAddr").kendoValidator().data("kendoValidator");
    business_unit.initialize();
});