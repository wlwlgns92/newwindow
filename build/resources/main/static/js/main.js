$(function() { //JS load 될때 무조건 1번 실행

    $.ajax({
        url: "/nreadcount",
        success: function(data) {

        }
    });
});



function login() {

var m_id = $("#login_m_id").val();
var m_password = $("#login_m_password").val();
var memberdto = {"m_id" : m_id, "m_password" : m_password};


    $.ajax({
        url: "/member/logincontroller", // 보내는곳
        data : JSON.stringify(memberdto),      // 전송 데이터 값
        method : "Post",     // Get, POST 방식중 선택
        contentType : "application/json", // ajax 타입
        success : function(result) { // 성공시 반환값
            if(result == 1) {
                location.href="/";
            } else {
                $("#loginfail").html("아이디 혹은 비밀번호가 다릅니다.");
                // $("#태그아이디").html();  태그 안에 html 추가
            }
        }


    });
}