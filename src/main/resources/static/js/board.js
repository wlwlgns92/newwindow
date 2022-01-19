
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
// 썸머노트 실행
$(document).ready(function() {
  $('#summernote').summernote({
    lang: 'ko-KR' ,  // 메뉴 한글 버전 ,
     minHeight : 400 , // 최소 높이
     maxHeight : null ,
     placeholder : "내용 입력"

  } );

});
// 댓글 등록
function replywrite(bnum) {

    var rcontent = $("#rcontent").val();
    if(rcontent == "") { alert("내용을 작성해 주세요 "); return; }
   $.ajax({
    url: "/board/replywrite",
    data : {"bnum" : bnum, "rcontent" : rcontent},
    success: function(data) {
        if(data == 1) {
            alert("댓글 작성 성공");
            // 특정 태그만 새로고침 [ jquery ]
            $("#replytable").load(location.href+"#replytable");
        }else {
            alert("댓글 작성 실패");
        }
    }
   });
}

function replydelete(rnum) {

    $.ajax({
        url : "/board/replydelete",
        data : {"rnum" : rnum},
        success: function(data) {
            if(data == 1) {
                alert("댓글 삭제 성공");
                $("#replytable").load(location.href+"#replytable");
            }
            else {
                alert("댓글 삭제 실패");
            }
        }
    });
}