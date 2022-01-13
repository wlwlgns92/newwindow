
function boarddelete(b_num) {

    $.ajax({
        url: "/board/bdelete",
        data : {"b_num" : b_num},
        success: function(data){
            if(data == 1){
                alert("게시물 삭제 완료");
                location.href="/board/boardlist";
            }else{
                alert("게시물 삭제 실패 [ 관리자 문의 ]");
            }
        }
    });
}

function boardwrite() {

// 1. 폼 태그 가져오기 [ 폼 안에 있는 input 사용 가능 ]
 var formData = new FormData(form);

   $.ajax({
   type: "POST",
   url : "/board/boardwritecontroller",
   data : formData,
   processData: false,
   contentType : false, // 첨부파일 보낼때 사용
   success : function(data) {
       if(data == 1) {
        alert("게시물 작성 완료 ");
        location.href="/board/boardlist";
       }else {
        alert(" 게시물 작성 실패 ");
       }
   }
   });
}