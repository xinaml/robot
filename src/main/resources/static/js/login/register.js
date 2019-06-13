
var Register = (function () {

    function Register() {

    }

    Register.prototype.checkOn = function() {
        var formData = $("#register_form").serialize();
        var password = $("input[name=password]").val().trim();
        var rePassword = $("input[name=rePassword]").val().trim();
       if(password!=rePassword){
           $("#msg").text("密码不一致");
           return;
       }
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/register" ,
            data: formData,
            success: function (rs) {
                if(rs.code==0){
                    alert("注册成功")
                    window.location.href="/login"; //跳转到上一个未登陆页
                }else {
                    $("#msg").text(rs.msg);
                }
            },
            error : function() {
                alert("异常！");
            }
        });
    }

    return Register;
})();