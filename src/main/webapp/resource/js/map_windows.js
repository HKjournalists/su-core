$(function(){
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var product = upload.product = upload.product || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	var map_windows=fsn.map_windows=fsn.map_windows || {};
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var mapProductId = null;
	var productId = null;
	var clentW =document.body.scrollHeight;
	    var htm='<div id="mapWindow" style="display: block;width:100%;height:'+clentW+'px;position:absolute;top:0;left:0;background-color:rgba(0,0,0,0.5);z-index:9999;">'
	        +'<div style="display:block;width:700px;height:430px;overflow:hidden;position:absolute;left:50%;top:50%;margin-top:-350px;margin-left:-215px;border:1px solid #00a878;border-radius:5px;">'
            +'<div id="search" style="background-color:#FFF;">'
            +'<input type="text" placeholder="输入关键字搜索地理位置" class="k-textbox" id="keyword" style="width:200px;height:30px;line-height:25px;" />'
            +'<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>'
            +'<input type="text" placeholder="输入覆盖物文字标注" class="k-textbox" id="label" style="margin-left:5px;width:200px;height:30px;line-height:25px;" />'
            +'<input type="button" id="add-location" class="k-button k-button-icontext k-grid-add" value="添加覆盖物" style="margin-left:5px;"/>'
            +'<input type="button" id="save" onclick="return save();" class="k-button k-button-icontext k-grid-add" style="margin-left:5px;"value="确定" />'
            +'<span id="closeWindow" style="display:block;float:right;width:31px;height:31px;text-align:center;cursor:pointer;">x</span>'
            +'</div>'
            +'<div id="allmap" style="width: 700px;height:400px;overflow: hidden;font-family:"微软雅黑";"></div>'
            +'</div>'
            +'</div>';
        map_windows.openwidows=function(name){
            $("#map_windows").append(htm);
            function getQueryString(name) {
            		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            		var r = window.location.search.substr(1).match(reg);
            		if (r != null) return unescape(r[2]); return null;
            	}
            var markerList=[];
            		var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
            		var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
            		var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
            		/*缩放控件type有四种类型:
            		BMAP_NAVIGATION_CONTROL_SMALL：仅包含平移和缩放按钮；BMAP_NAVIGATION_CONTROL_PAN:仅包含平移按钮；BMAP_NAVIGATION_CONTROL_ZOOM：仅包含缩放按钮*/
            		// 百度地图API功能
            		var map = new BMap.Map("allmap");
            		var point = new BMap.Point(116.331398,39.897445);
            		map.centerAndZoom(point,map.getZoom());
            		map.addControl(top_left_control);
            		map.addControl(top_left_navigation);
            		map.addControl(top_right_navigation);
            		map.enableScrollWheelZoom(true);//设置鼠标滚轮可缩放

                    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
                        {"input" : "keyword"
                        ,"location" : map
                    });

                    function G(id) {
                    			return document.getElementById(id);
                    		}
                    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
                        var str = "";
                        var _value = e.fromitem.value;
                        var value = "";
                        if (e.fromitem.index > -1) {
                            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                        }
                        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

                        value = "";
                        if (e.toitem.index > -1) {
                            _value = e.toitem.value;
                            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                        }

                        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
                        G("searchResultPanel").innerHTML = str;
                    });
                    var myValue;
                        ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
                            var _value = e.item.value;
                            myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                            G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
                            setPlace();
                        });
                            var id=getQueryString("id");
                            var param='';
                            if(id){
                                param='?id='+id;
                                $.get(portal.HTTP_PREFIX+"/mapProduct/getInfoByid"+param,function(rs){
                                        $("#barcodeId").data("kendoComboBox").value(rs.mapProduct.productId);
                                        point = new BMap.Point(rs.mapProduct.lng,rs.mapProduct.lat);
                                        map.centerAndZoom(point,map.getZoom());
                                        for(var i in rs.mapProduct.mapProductAddrList){
                                            var _point=new BMap.Point(rs.mapProduct.mapProductAddrList[i].lng,rs.mapProduct.mapProductAddrList[i].lat);
                                            addMarker(_point,rs.mapProduct.mapProductAddrList[i].describe);
                                        }
                                },'json');
                            }else{
                                 var geolocation = new BMap.Geolocation().getCurrentPosition(function(r){
                                    if(this.getStatus() == BMAP_STATUS_SUCCESS){
                                        map.panTo(r.point);
                                    }
                                    else {
                                        alert('failed'+this.getStatus());
                                    }
                                },{enableHighAccuracy: true});
                            }
                        $("#add-location").click(function(){
                        if(markerList.length==0){
                        addMarker(map.getCenter(),$.trim($("#label").val()));
                        }else{
                        lims.initNotificationMes('只可添加一个覆盖物！', false);
                        }
                        });
                        var removeMarker = function(e,ee,marker){
                                    for(var i in markerList){
                                        if(markerList[i]==marker){
                                            if(i!=0){
                                                var _marker={};
                                                $.extend(_marker,markerList[i],true);
                                                markerList[i]=markerList[0];
                                                markerList[0]=_marker;
                                            }
                                            markerList.shift();
                                        }
                                    }
                                    map.removeOverlay(marker);
                                }
                        function addMarker(point,label){
                                    //创建右键菜单
                                    var markerMenu=new BMap.ContextMenu();
                                    markerMenu.addItem(new BMap.MenuItem('删除',removeMarker.bind(marker)));
                                    var marker = new BMap.Marker(point);// 创建标注
                                    map.addOverlay(marker);             // 将标注添加到地图中
                                    marker.enableDragging();//可拖拽
                                    //marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                                    marker.addContextMenu(markerMenu);
                                    var label = new BMap.Label(label,{offset:new BMap.Size(20,-10)});
                                    marker.setLabel(label);
                                    markerList.push(marker);
                                }
                        function setPlace(){
                                function myFun(){
                                    var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                                    map.centerAndZoom(pp, map.getZoom() + 6);
                                }
                                var local = new BMap.LocalSearch(map, { //智能搜索
                                  onSearchComplete: myFun
                                });
                                local.search(myValue);
                            }
                        $("#closeWindow").on("click",function(){
                                     $("#mapWindow").remove();
                                })

                      save = function(){
                      		var data={};
                      		if(markerList.length==0){
                      			lims.initNotificationMes('必须添加覆盖物在地图上！', false);
                      			return false;
                      		}
                      		data.mapProductAddrList=[];
                      		for(var i in markerList){
                      			data.mapProductAddrList[i]={};
                      			data.mapProductAddrList[i].lat=markerList[i].getPosition().lat;
                      			data.mapProductAddrList[i].lng=markerList[i].getPosition().lng;
                      			data.mapProductAddrList[i].describe=markerList[i].getLabel().content;
                      		}
                      		data.lat=map.getCenter().lat;
                      		data.lng=map.getCenter().lng;

                      	/*	$.ajax({

                      			type:type,
                      			url:fsn.http_prefix+"/mapproduct/save",
                      			data:json.stringify(data),
                      			datatype: "json",
                      			contenttype: "application/json; charset=utf-8",
                      			success:function(rs){
                      				if(rs.status==2){
                      					lims.initnotificationmes('该产品已经存在', false);
                      				}else if(rs.status){
                      					location.href="map-product.html";
                      				}else{
                      					lims.initnotificationmes('保存失败,请联系管理员', false);
                      				}
                      			}
                      		});*/
                      		$("#mapWindow").remove();
                              if( data.mapProductAddrList.length>0){
                                  //经度
                                  $("#longitude").val(data.mapProductAddrList[0].lng);
                                  //纬度
                                  $("#latitude").val(data.mapProductAddrList[0].lat);
                                   //经度--纬度
                                  $("#longOrlat").val(data.mapProductAddrList[0].lng+"--"+data.mapProductAddrList[0].lat);
                                  //进度纬度位置地名
                                  $("#placeName").val(data.mapProductAddrList[0].describe);
                              }
                          return data;

                      	};
                      	function  alertV(){
                      	    $(".tangram-suggestion-main").css("z-index","10000");
                      	}
                   setTimeout(alertV,2000);
        }
        map_windows.viewwidows=function(data){
                    $("#map_windows").append(htm);
                    var markerList=[];
                    		var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
                    		var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
                    		var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
                    		/*缩放控件type有四种类型:

                    			BMAP_NAVIGATION_CONTROL_SMALL：仅包含平移和缩放按钮；BMAP_NAVIGATION_CONTROL_PAN:仅包含平移按钮；BMAP_NAVIGATION_CONTROL_ZOOM：仅包含缩放按钮*/
                    		// 百度地图API功能
                    		var map = new BMap.Map("allmap");
                    		var point = new BMap.Point(116.331398,39.897445);
                    		map.centerAndZoom(point,map.getZoom());
                    		map.addControl(top_left_control);
                    		map.addControl(top_left_navigation);
                    		map.addControl(top_right_navigation);
                    		map.enableScrollWheelZoom(true);//设置鼠标滚轮可缩放

                            var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
                                {"input" : "keyword"
                                ,"location" : map
                            });

                            function G(id) {
                            			return document.getElementById(id);
                            		}
                            ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
                                var str = "";
                                var _value = e.fromitem.value;
                                var value = "";
                                if (e.fromitem.index > -1) {
                                    value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                                }
                                str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

                                value = "";
                                if (e.toitem.index > -1) {
                                    _value = e.toitem.value;
                                    value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                                }

                                str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
                                G("searchResultPanel").innerHTML = str;
                            });
                            var myValue;
                                ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
                                    var _value = e.item.value;
                                    myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                                    G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
                                    setPlace();
                                });


                                $("#add-location").click(function(){
                                if(markerList.length==0){
                                addMarker(map.getCenter(),$.trim($("#label").val()));
                                }else{
                                lims.initNotificationMes('只可添加一个覆盖物！', false);
                                }
                                });
                                var removeMarker = function(e,ee,marker){
                                            for(var i in markerList){
                                                if(markerList[i]==marker){
                                                    if(i!=0){
                                                        var _marker={};
                                                        $.extend(_marker,markerList[i],true);
                                                        markerList[i]=markerList[0];
                                                        markerList[0]=_marker;
                                                    }
                                                    markerList.shift();
                                                }
                                            }
                                            map.removeOverlay(marker);
                                        }
                                function addMarker(point,label){
                                            //创建右键菜单
                                            var markerMenu=new BMap.ContextMenu();
                                            markerMenu.addItem(new BMap.MenuItem('删除',removeMarker.bind(marker)));
                                            var marker = new BMap.Marker(point);// 创建标注
                                            map.addOverlay(marker);             // 将标注添加到地图中
                                            marker.enableDragging();//可拖拽
                                            //marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                                            marker.addContextMenu(markerMenu);
                                            var label = new BMap.Label(label,{offset:new BMap.Size(20,-10)});
                                            marker.setLabel(label);
                                            markerList.push(marker);
                                        }
                                function setPlace(){
                                        function myFun(){
                                            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                                            map.centerAndZoom(pp, map.getZoom() + 6);
                                        }
                                        var local = new BMap.LocalSearch(map, { //智能搜索
                                          onSearchComplete: myFun
                                        });
                                        local.search(myValue);
                                    }
                                $("#closeWindow").on("click",function(){
                                             $("#mapWindow").remove();
                                        })

                              save = function(){
                              		var data={};
                              		if(markerList.length==0){
                              			lims.initNotificationMes('必须添加覆盖物在地图上！', false);
                              			return false;
                              		}
                              		data.mapProductAddrList=[];
                              		for(var i in markerList){
                              			data.mapProductAddrList[i]={};
                              			data.mapProductAddrList[i].lat=markerList[i].getPosition().lat;
                              			data.mapProductAddrList[i].lng=markerList[i].getPosition().lng;
                              			data.mapProductAddrList[i].describe=markerList[i].getLabel().content;
                              		}
                              		data.lat=map.getCenter().lat;
                              		data.lng=map.getCenter().lng;
                              		$("#mapWindow").remove();
                              		return data;

                              	};
                              	function  alertV(){
                              	    $(".tangram-suggestion-main").css("z-index","10000");
                                    if(data!=null){
                                    point = new BMap.Point(data.lng,data.lat);
                                    map.centerAndZoom(point,map.getZoom());
                                    addMarker(point,data.describe);
                                    }else{
                                         var geolocation = new BMap.Geolocation().getCurrentPosition(function(r){
                                            if(this.getStatus() == BMAP_STATUS_SUCCESS){
                                                map.panTo(r.point);
                                            }
                                            else {
                                                alert('failed'+this.getStatus());
                                            }
                                        },{enableHighAccuracy: true});
                                    }
                              	}
                           setTimeout(alertV,500);
                }

		
		
		//初始化页面
		product.initialize = function(){
			$("#CONFIRM_COMMON_WIN").kendoWindow({
				width: "480",
				height:"auto",
				title: "导入已有产品抽样地点",
				visible: false,
				resizable: false,
				draggable:false,
				modal: true
			});
		};
		product.initialize();
		
});
