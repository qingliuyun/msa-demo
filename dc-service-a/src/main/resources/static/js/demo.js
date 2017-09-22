/**
 * Created by LeoYee on 2016-07-26.
 */
$(function () {
    $("#hello-btn").on('click','',function(){
        demoFunc.hello();
    })
    $("#url-btn").on('click','',function(){
        demoFunc.url();
    })
});
var demoFunc = {
    hello: function () {
        var contextPath = demoFunc.getContextPath();
        var url = contextPath + "/invoke.do?service="+$('#hello-service').val()
                            +'&contextPath='+$('#hello-context').val();
        $.ajax({
            global: false,
            type: "GET",
            async: false,
            url: url,
            success: function (data) {
                alert(data);
            }
        })
    },
    getContextPath: function () {
        var href = window.location.href;
        var f_index = href.indexOf('/', 7);
        var s_index = href.indexOf('/', f_index + 1);
        return href.substring(f_index, s_index);
    }
}
