function rdelete(rnum) {

    $.ajax({
        url: "/admin/delete",
        data: {"rnum" : rnum},
        success: function(result) {
            if(result == 1) {
                location.href="/admin/roomlist";
            }else{
                alert("삭제 실패");
            }
        }
    });
}

function activeupdate(rnum, upactive) {

    $.ajax({
        url: "/admin/activeupdate",
        data: {"rnum" : rnum, "upactive" : upactive},
        success: function(result){
            if(result == 1 ) {
                location.href="/admin/roomlist";
            } else {
                $("#activemsg").html("현재 동일한 상태입니다.");
            }
        }
    });
}