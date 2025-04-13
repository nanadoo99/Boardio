/*
function readComments() {
    var pno = $('#postPno').val();

    $.ajax({
        url: contextPath + '/user/comments/posts/' + pno,
        type: 'GET',
        success: function (response) {
            var commentList = response.comments;
            $('#comments').empty();
            var cmtElements = '';

            commentList.forEach(function (commentDto) {

                if (commentDto.pcno != commentDto.cno) { // 답글 표시
                    cmtElements += "ㄴ<br>";
                }
                cmtElements += "<div id='cmtBox" + commentDto.cno + "'>";
                cmtElements += "   <input type='hidden' id='cno" + commentDto.cno + "' value='" + commentDto.cno + "'/>";
                if (commentDto.del) {
                    cmtElements += " <span>삭제된 댓글입니다.</span>";
                } else {
                    cmtElements += completeCommentUser(commentDto);
                }
                cmtElements += "</div>";
            });
            $('#comments').append(cmtElements);
            $('#post-section').show();
        }, error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}

function completeCommentText(commentDto) {
    var comments = '';
    comments += "   <span>";
    comments += "       <a data-pcno='" + commentDto.pcno + "' data-uno='" + commentDto.uno + " 'data-userid='" + commentDto.userId + "'>" + commentDto.userId + "</a>";
    comments += "   </span>";
    comments += "   <input type='text' id='cmtText" + commentDto.cno + "' value='" + commentDto.comment + "' readonly/>";
    comments += "   <input type='hidden' id='pcno" + commentDto.pcno + "' value='" + commentDto.pcno + "'/>";
    comments += "   <input type='hidden' id='cmtUno" + commentDto.cno + "' value='" + commentDto.uno + "'/>";

    return comments;
}

function completeCommentUser(commentDto) {
    var postBlocked = $('#postBlocked').val();
    var cmtElements = '';

    if (commentDto.contentState == "BLOCKED") {
        cmtElements += " <span>관리자에 의해 차단된 댓글입니다.</span>";
    } else {
        cmtElements += completeCommentText(commentDto);
        if(commentDto.uno) {
            cmtElements += commentBtn(commentDto, postBlocked);
        }
    }

    return cmtElements;
}

function commentBtn(commentDto, postBlocked) {
    var cmtElements = '';

    if (commentDto.uno == authUserUno && postBlocked != "BLOCKED") { // 댓글 작/성자에게 보이는 버튼
        cmtElements += "   <button id='cmtUpdateBtn" + commentDto.cno + "'>수정</button>";
        cmtElements += "   <button id='cmtPostBtn" + commentDto.cno + "' style='display: none;'>등록</button>";
        cmtElements += "   <button id='cmtCancelBtn" + commentDto.cno + "' style='display: none;'>취소</button>";
        cmtElements += "   <button id='cmtDeleteBtn" + commentDto.cno + "'>삭제</button>";
    } else {
        if (!commentDto.rcno) {
            cmtElements += "   <button id='cmtReportBtn" + commentDto.cno + "'>신고</button>";
        } else {
            cmtElements += "   <button id='cmtReportCancelBtn" + commentDto.rcno + "'>신고 취소</button>";
            cmtElements += "   <button id='cmtReportChangeBtn" + commentDto.rcno + "'>신고 사유 변경</button>";
            cmtElements += "   <input type='hidden' id='brno" + commentDto.cno + "' value='" + commentDto.brno + "'>";
        }
    }

    return cmtElements;
}

// 댓글 버튼 클릭
$('#comment-section').on('click', 'button', function () {
    var cno = $(this).siblings('input[id^="cno"]').val();
    var id = $(this).attr('id');

    if (id.includes('cmtUpdateBtn')) {
        toggleUpdate(cno);
    } else if (id.includes('cmtPostBtn')) {
        postComment(cno);
    } else if (id.includes('cmtCancelBtn')) {
        toggleUpdate(cno);
    } else if (id.includes('cmtDeleteBtn')) {
        deleteComment(cno);
    } else if (id.includes('cmtReportBtn')) {
        reportCommentPopOpen(cno);
    } else if (id.includes('cmtReportChangeBtn')) {
        reportCommentChangePopOpen(cno);
    } else if (id.includes('cmtReportCancelBtn')) {
        reportCommentCancel(cno);
    }

});

// 댓글 등록
$('#createCmtBtn').on('click', function () {
    var pno = $('#postPno').val();
    var comment = $('#comment-text').val();
    var pcno = $('#comment-reply').data('pcno');
    var uno = $('#comment-reply').data('uno');

    if (comment == '' || comment == null) {
        alert("댓글을 입력해주세요");
        return;
    }

    if (pcno != '' && pcno != null) {
        var jsonData = JSON.stringify({
            pno: pno,
            comment: comment,
            pcno: pcno,
            uno: uno
        });
    } else {
        var jsonData = JSON.stringify({
            pno: pno,
            comment: comment
        });
    }

    $.ajax({
        url: contextPath + '/user/comment',
        type: 'POST',
        contentType: 'application/json',
        data: jsonData,
        success: function (response) {
            $('#comment-text').val('');
            readComments();
            cancelReply();
        }, error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
});

// 댓글 수정모드
function toggleUpdate(cno) {
    var cmtText = 'cmtText' + cno;
    var updateBtn = 'cmtUpdateBtn' + cno;
    var postBtn = 'cmtPostBtn' + cno;
    var cancelBtn = 'cmtCancelBtn' + cno;
    var deleteBtn = 'cmtDeleteBtn' + cno;

    if ($('#' + cmtText).attr('readonly')) { // 수정 모드 on
        $('#' + cmtText).attr('readonly', false).focus();
        toggleCmtBtn(0);
        $('#' + updateBtn).hide();
        $('#' + postBtn).show();
        $('#' + cancelBtn).show();
        $('#' + deleteBtn).hide();
    } else { // 수정 모드 off
        $('#' + cmtText).attr('readonly', true);
        toggleCmtBtn(1);
        $('#' + updateBtn).show();
        $('#' + postBtn).hide();
        $('#' + cancelBtn).hide();
        $('#' + deleteBtn).show();
    }
}

// 댓글 수정
function postComment(cno) {
    var cmtText = 'cmtText' + cno;
    var comment = $('#' + cmtText).val();

    if (comment == '' || comment == null) {
        alert("댓글을 입력해주세요");
        return;
    }

    var jsonData = JSON.stringify({
        cno: cno,
        comment: comment
    });

    $.ajax({
        url: contextPath + '/user/comment',
        type: 'PATCH',
        contentType: 'application/json',
        data: jsonData,
        success: function (response) {
            toggleUpdate(cno);
        },
        error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        },
        complete: function () {
            readComments();
        }
    });
}

// 댓글 삭제
function deleteComment(cno) {
    if (!confirm("댓글을 삭제하시겠습니까?")) return;
    $.ajax({
        url: contextPath + '/user/comment/' + cno,
        type: 'DELETE',
        error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        },
        complete: function () {
            readComments();
        }
    });
}

// 댓글 신고 - 팝업 열기
function reportCommentPopOpen(cno) {
    $('#reportPopTitle').text("댓글 신고");
    $('#reportBtn').attr("onclick", "reportComment(" + cno + ");");
    $('#reportBtn').text("신고");

    $("#reportPop").show();
}

// 댓글 신고
function reportComment(cno) {
    var brno = $("#reportPopBrno").val();
    if (brno == "" || !brno) {
        alert("신고 사유를 선택하세요.");
        return;
    }
    $("#reportPopIdx").attr("name", "cno");
    $("#reportPopIdx").val(cno);
    $("#reportPopText").val($('#cmtText' + cno).val());
    $("#reportPopReportUno").val($('#cmtUno' + cno).val());

    $.ajax({
        type: 'POST',
        url: contextPath + '/user/comment/report',
        data: $("#reportPopForm").serialize(),
        success: function (response) {
            alert("신고가 접수되었습니다.");
            reportPopClose();
            readPost($('#postPno').val());
        }, error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}

// 댓글 신고 - 사유 변경 팝업 열기
function reportCommentChangePopOpen(cno) {
    $('#reportPopTitle').text("댓글 신고 사유 변경");
    $('#reportBtn').attr("onclick", "reportCommentChange(" + cno + ");");
    $('#reportBtn').text("변경");
    $('#reportPopBrno').val($('#brno' + cno).val());

    $("#reportPop").show();
}

// 댓글 신고 - 사유 변경
function reportCommentChange(cno) {
    var brno = $("#reportPopBrno").val();
    if (brno == "" || !brno) {
        alert("신고 사유를 선택하세요.");
        return;
    }
    $("#reportPopIdx").attr("name", "cno");
    $("#reportPopIdx").val(cno);
    $("#reportPopText").val($('#cmtText' + cno).val());
    $("#reportPopReportUno").val($('#cmtUno' + cno).val());

    $.ajax({
        type: 'POST',
        url: contextPath + '/user/comment/report',
        data: $("#reportPopForm").serialize(),
        dataType: 'json',
        success: function (response) {
            alert("사유가 변경되었습니다.");
            reportPopClose();
            readPost($('#postPno').val());
        }, error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}

// 댓글 신고 - 취소
function reportCommentCancel(cno) {
    if (!confirm("댓글 신고를 취소하시겠습니까?")) return;
    $.ajax({
        type: 'POST'
        , url: contextPath + '/user/comment/deleteReport/' + cno
        , dataType: 'json'
        , success: function (response) {
            alert("신고가 취소되었습니다.");
            readPost($('#postPno').val());
        }, error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}

// 멘션
$('#comment-section').on('click', 'a', function () {
    var pcno = $(this).data('pcno');
    var uno = $(this).data('uno');
    var userId = $(this).data('userid');

    $('#comment-reply').data('pcno', pcno).data('uno', uno);
    $('#comment-reply').text(userId.toString());
    $('#reply-cancel').show();
});

// 멘션 취소
$('#reply-cancel').on('click', function () {
    cancelReply();
});

// 멘션 취소
function cancelReply() {
    $('#comment-reply').removeData('pcno');
    $('#comment-reply').removeData('uno');
    $('#comment-reply').text(' ');
    $('#reply-cancel').hide();
}

function toggleCmtBtn(chk) {
    if (chk == 0) {
        $("[id^='cmtBox'] button").hide();
    } else {
        $("[id^='cmtUpdateBtn']").show();
        $("[id^='cmtDeleteBtn']").show();
    }
}
*/
