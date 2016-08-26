$(function(){
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
		map.centerAndZoom(point,12);
		map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
		map.addControl(top_right_navigation);  
		map.enableScrollWheelZoom(true);//设置鼠标滚轮可缩放
		
		/**删除覆盖物*/
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
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
			marker.addContextMenu(markerMenu);
			var label = new BMap.Label(label,{offset:new BMap.Size(20,-10)});
			marker.setLabel(label);
			markerList.push(marker);
		}
		
		$("#add-location").click(function(){
			addMarker(map.getCenter(),$.trim($("#label").val()));
		});

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

	function setPlace(){
		function myFun(){
			var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
			map.centerAndZoom(pp, 18);
		}
		var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: myFun
		});
		local.search(myValue);
	}
	$("#save").click(function(){
		var data={};
		data.productId=$("#barcodeId").data("kendoComboBox").value();
		data.productName=$("#barcodeId").data("kendoComboBox").text();
		if(data.productId==""){
			lims.initNotificationMes('必须选择所属产品！', false);
			return false;
		}
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
		var type="POST";
		if(id){
			type="PUT";
			data.id=id;
		}
		$.ajax({
			type:type,
			url:fsn.HTTP_PREFIX+"/mapProduct/save",
			data:JSON.stringify(data),
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			success:function(rs){
				if(rs.status==2){
					lims.initNotificationMes('该产品已经存在', false);
				}else if(rs.status){
					location.href="map-product.html";
				}else{
					lims.initNotificationMes('保存失败,请联系管理员', false);
				}
			}
		});
		return false;
	});
	$("#barcodeId").kendoComboBox({
	        dataTextField: "name",
	        dataValueField: "id",
	        dataSource: [],
	        filter: "startswith",
	        minLength: 0,
	        index:0,
	    });
	    var id=getQueryString("id");
	    var param='';
	    if(id){
	   		param='?id='+id;
	   		$.get(portal.HTTP_PREFIX+"/mapProduct/getInfoByid"+param,function(rs){
				   	$("#barcodeId").data("kendoComboBox").value(rs.mapProduct.productId);
					point = new BMap.Point(rs.mapProduct.lng,rs.mapProduct.lat);
					map.centerAndZoom(point,12);
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
		$.ajax({
			url:portal.HTTP_PREFIX + "/product/getAllProductsByOrg",
			type:"GET",
			dataType:"json",
			async:false,
			success:function(returnValue){
				$("#barcodeId").data("kendoComboBox").setDataSource(returnValue.productList);
				$("#barcodeId").data("kendoComboBox").refresh();
			},
		});
});