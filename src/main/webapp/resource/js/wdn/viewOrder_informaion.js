$(function() {

    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var view=fsn.view=fsn.view||{};
    var orderInfo = JSON.parse(window.sessionStorage.getItem("currentOrder"));

    view.init = function(){
        $("#contact").html(orderInfo.userRealName);
        $("#phone").html(orderInfo.phone);
        $("#email").html(orderInfo.email);
        $("#journal").html(orderInfo.journal);
        $("#isn").html(orderInfo.isn);
        $("#title").html(orderInfo.title);
        $("#authors").html(orderInfo.authors);
        $("#year").html(orderInfo.year);
        $("#volume").html(orderInfo.volume);
        $("#issue").html(orderInfo.issue);
        $("#startPage").html(orderInfo.startPage);
        $("#endPage").html(orderInfo.endPage);
        $("#status").html(orderInfo.status);
    }

    view.init();

});
