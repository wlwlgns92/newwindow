
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