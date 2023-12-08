// 注销js
function logout() {
    if (confirm("是否确定注销?")) {
        // 删除后端在线信息, (删除后端Session)
        // 跳转到登录页面
        jQuery.ajax({
            url:"/user/logout",
            type:"POST",
            data:{

            },
            success:function(ret) {

            }
        });
        location.href = "login.html";
    }
}