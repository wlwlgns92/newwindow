function notereply(nnum, ncontent){
    $("#rcontent").html("문의내용: " + ncontent);
    $("#nnum").val(nnum);
    $("#notereply").modal("show");

    // 읽음 처리
        $.ajax({
            url:"/room/nreadupdate",
            data:{"nnum" : nnum},
            success: function(data){
            }
        });
}

function notereplywrite(){
 var nreply = $("#nreply").val();
 var nnum = $("#nnum").val();

 alert(nreply);
 alert(nnum);

 $.ajax({
    url: "/member/notereplywrite",
    data: {"nnum" : nnum, "nreply" : nreply},
    success: function(data){
        if(data == 1) {
            alert("답변완료");
            location.href = "/member/notelist";
        }else{
            alert("답변실패");
        }
    }
 });
}