/**
 * Created by pc on 2016/10/19.
 */
var dialog = {
    //成功提示
    success:function(message){
        layer.alert(message, {
            title:"成功提示",
            icon: 1,
            skin: 'layer-ext-moon'
        })
    },
    //错误提示
    error: function (message) {
        layer.alert(message, {
            title:"错误提示",
            icon: 6,
            skin: 'layer-ext-moon'
        })
    },
    //确认跳转提示
    confirm: function (message,url) {
        layer.alert(message,{
            icon:3,
            btn:['否','是'],
            yes: function () {
                location.href = url;
            }
        })
    },
    //确认提示
    toConfirm: function (message) {
        layer.alert(message,{
            icon:3,
            btn:['是']
        })
    },
    //捕获页面元素
    prompt: function (obj) {
        layer.open({
            type: 1,
            shade: false,
            title: false, //不显示标题
            content: obj, //捕获的元素
            cancel: function(index){
                layer.close(index);
                this.content.show();
            }
        });
    },
    //提示层
    message: function (meg) {
        layer.msg(meg);
    },
    //tip小提示
    tip: function (tip,select) {
        layer.tips(tip,select);
    },
    //tip小提示2，4秒后消失
    tip2: function (tip, select) {
        layer.tips(tip, select, {
            tips: [1, '#3595CC'],
            time: 4000
        });
    }

}