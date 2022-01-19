

$(function(){

    $("#m_id").keyup(function(){
                // 해당 태그에 키보드가 눌렸을때 이벤트
    var m_id = $("#m_id").val();
    var idj = /^[a-z0-9]{5,15}$/

    if(!idj.test(m_id)) {
         $("#idcheck").html("영소문자 5~15글자만 가능합니다.");
         return false;
    }else {
    // 아이디중복체크 비동기 통신
       $.ajax({
            url: "/member/idcheck",
            method : "post",
            data : {"m_id" : m_id},
            success : function(result) {
               if(result == 1) {
                $("#idcheck").html("현재 사용중인 아이디 입니다.");
                return false; // 폼 전송 막기
               }else {
                 $("#idcheck").html("사용가능");
               }
            } //success end
        }); // ajax end
    } // 정규식 else end
    }); // 키보드업 end

    $("#m_password").keyup(function(){
       var m_password = $("#m_password").val();
       var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/

       if(!pwj.test(m_password)) {
        $("#pwcheck").html("숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용");
        return false;
       }
       else {
        $("#pwcheck").html("사용가능");
       }
    }); // keyup end

     $("#m_passwordconfirm").keyup(function(){
        var m_password = $("#m_password").val();
        var m_passwordconfirm = $("#m_passwordconfirm").val();
        var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/

        if(!pwj.test(m_passwordconfirm)) {
                $("#pwcheck").html("숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용");
                return false;
        }
        else if(m_password != m_passwordconfirm) {
             $("#pwcheck").html("비밀번호가 일치하지 않습니다.");
             return false;
        }
        else {
            $("#pwcheck").html("사용가능");
        }
     }); // keyup end

    $("#m_name").keyup(function(){
        var namej =/^[A-Aa-a가-힣]{1,15}$/;
        var m_name = $("#m_name").val();

        if(!namej.test(m_name)) {
             $("#namecheck").html("영대문자/한글 1~15글자 허용");
             return false;
        }else {
             $("#namecheck").html("사용가능");
        }
    });

    $("#m_phone").keyup(function(){
        var phonej = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/
        var m_phone =$("#m_phone").val();
        if(!phonej.test(m_phone)) {
            $("#phonecheck").html("000-0000-0000 형식으로 입력해 주세요 ");
            $("input[type='submit']").prop("disabled", true);
            return false;
        } else {
            $("#phonecheck").html("사용가능");
        }
    }); // 연락처 keyup end

   //이메일 유효성 검사
   // input에서 type을 email로 지정하면 따로 유효성검사 필요없다.

    // 전송버튼 활성화


    $("#m_email").keyup(function(){
                    // 해당 태그에 키보드가 눌렸을때 이벤트
        var m_email = $("#m_email").val();
        var emailj = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

        if(!emailj.test(m_email)) {
             $("#emailcheck").html("영소문자 5~15글자만 가능합니다.");
             return false;
        }else {
        // 이메일중복체크 비동기 통신
           $.ajax({
                url: "/member/emailcheck",
                method : "post",
                data : {"m_email" : m_email},
                success : function(result) {
                   if(result == 1) {
                    $("#emailcheck").html("중복된 이메일이 존재합니다.");

                    return false; // 폼 전송 막기
                   }else {
                     $("#emailcheck").html("사용가능");
                   }
                } //success end
            }); // ajax end
        } // 정규식 else end
        }); // 키보드업 end
       $("#sample3_postcode").keyup(function(){
        var address1 = $("#sample3_postcode").val();
        if(address1.indexOf("/") != -1){ $("#addresscheck").html("/ 포함 불가 "); return false; }
        if(address1 != null) { $("#addresscheck").html("사용가능"); }
       });
       $("#sample3_address").keyup(function(){
        var address2 = $("#sample3_address").val();
        if(address2.indexOf("/") != -1){ $("#addresscheck").html("/ 포함 불가 "); return false; }
        if(address2 != null) { $("#addresscheck").html("사용가능"); }
       });

       $("#sample3_detailAddress").keyup(function(){
        var address3 = $("#sample3_detailAddress").val();
        if(address3.indexOf("/") != -1){ $("#addresscheck").html("/ 포함 불가 "); return false; }
        if(address3 != null) { $("#addresscheck").html("사용가능"); }
       });
       $("#sample3_extraAddress").keyup(function(){
        var address4 = $("#sample3_extraAddress").val();
        if(address4.indexOf("/") != -1){  $("#addresscheck").html("/ 포함 불가 "); return false; }
        if(address4 != null) { $("#addresscheck").html("사용가능"); }
       });

       $("#formsubmit").click(function(){
       if( $("idcheck").html() != "사용가능" ){
        alert("사용불가능한 아이디 입니다.");
       }
       else if ($("pwcheck").html() != "사용가능") {
       alert("사용불가능한 비밀번호 입니다.");
       }
       else if ($("namecheck").html() != "사용가능" ) {
       alert("사용불가능한 이름입니다.");
       }
       else if ($("phonecheck").html() != "사용가능" ) {
       alert("사용불가능한 연락처입니다.");
       }
       else if ($("emailcheck").html() != "사용가능" ) {
       alert("사용불가능한 이메일 입니다.");
       }
       else if ($("addresscheck").html() != "사용가능" ) {
       alert("사용불가능한 주소입니다.");
       }else {
       $("input[type='submit']").prop("disabled", false);
        $("form").submit(); // 모든 유효성 검사 통과시 폼 전송
       }
       });
}); // 함수 end