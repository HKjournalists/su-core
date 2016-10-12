	//wuzhoubo 2016-10-10
    //loading自执行函数
    (function loading() {
        var rotateReg = 0,
                _PageHeight = document.documentElement.clientHeight,
                _PageWidth = document.documentElement.clientWidth,//计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）,
                _LoadingTop = _PageHeight > 48 ? (_PageHeight - 48) / 2 : 0,
                _LoadingLeft = _PageWidth > 48 ? (_PageWidth - 48) / 2 : 0;//在页面未加载完毕之前显示的loading Html自定义内容

        var loadingDiv = document.createElement("div");//loading覆盖层
        var loadingImg = document.createElement("div");//gif图片div
                                                                                 
        loadingDiv.setAttribute("id","loadingDiv");
        loadingDiv.setAttribute("style",'position:absolute;background-color:#fff;left:0;width:100%;height:' + _PageHeight + 'px;top:0;opacity:1;filter:alpha(opacity=100);z-index:100000;"');
        loadingImg.setAttribute("style",'position:absolute;top:'+_LoadingTop+'px;left:'+_LoadingLeft+'px;width:48px;height:48px;background:url("/fsn-core/resource/img/loading.gif") center center no-repeat;');
        loadingDiv.appendChild(loadingImg);
        document.body.appendChild(loadingDiv);//呈现loading效果

        document.onreadystatechange = completeLoading;
        function completeLoading() {
            if (document.readyState == "complete") {
                var loadingMask = document.getElementById('loadingDiv');
                loadingMask.parentNode.removeChild(loadingMask);
            }
        }
    }());
