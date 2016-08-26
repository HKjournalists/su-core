//search bar effect

$(".J_SearchTab").mouseenter(function() {
    $(".search-triggers").addClass("triggers-hover");

});

$(".J_SearchTab").mouseleave(function() {
    $(".search-triggers").removeClass("triggers-hover");

});

$("#type2").click(function() {
    var tmpStr = $("#type1_des").html();
    $("#type1_des").html($("#type2_des").html());
    $("#type2_des").html(tmpStr);
    tmpStr == "产品" ? $("#type").val("item") : $("#type").val("enterprise");
});

$("#type1").mouseenter(function() {
    $("#type1").addClass("triggers-focus");
});
$("#type1").mouseleave(function() {
    $("#type1").removeClass("triggers-focus");
});