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

                if (commentDto.pcno != commentDto.cno) { // 멘션 표시
                    cmtElements += "<span class='h5 text-muted'>∟</span>";
                    cmtElements += "<div id='cmtBox" + commentDto.cno + "'  class='card mb-3' style='margin-left: 3rem;'>";
                } else {
                    cmtElements += "<div id='cmtBox" + commentDto.cno + "'  class='card mb-3'>";
                }
                cmtElements += "<div class='card-body'>";
                cmtElements += "   <input type='hidden' id='cno" + commentDto.cno + "' value='" + commentDto.cno + "'/>";
                if (commentDto.del) {
                    cmtElements += " <span>삭제된 댓글입니다.</span>";
                } else {
                    cmtElements += completeCommentUser(commentDto);
                }
                cmtElements += "</div>";
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
    comments += "<div class='w-75'>";
    comments += "   <textarea id='cmtText" + commentDto.cno + "' ' class='card-text w-75' readonly>" + commentDto.comment + "</textarea>";
    comments += "   <small type='text' class='card-text text-muted' >" + commentDto.formattedCreatedAtTime + "</small>";
    comments += "</div>";

    return comments;
}

function completeCommentUser(commentDto) {
    var postBlocked = $('#postBlocked').val();
    var cmtElements = '';

    if (commentDto.contentState == "BLOCKED") {
        cmtElements += " <span>관리자에 의해 차단된 댓글입니다.</span>";
    } else {
        cmtElements += "   <div class='card-title'>";
        // cmtElements += "       <a data-pcno='" + commentDto.pcno + "' data-uno='" + commentDto.uno + " 'data-userid='" + commentDto.userId + "'>" + commentDto.userId + "</a>";
        cmtElements += "       <span class='h5'>" + commentDto.userId + "</span>";
        if (commentDto.pcno == commentDto.cno) { // 멘션 기능
            cmtElements += "       <a style='cursor: pointer;' class='text-primary fs-6' data-pcno='" + commentDto.pcno + "' data-uno='" + commentDto.uno + " 'data-userid='" + commentDto.userId + "'>대댓글 달기</a>";
        }
        cmtElements += "   </div>";
        cmtElements += "<div class='d-flex justify-content-between align-items-end'>";
        cmtElements += completeCommentText(commentDto);
        if (commentDto.uno) {
            cmtElements += "<div class='btn-group'>";
            cmtElements += commentBtn(commentDto, postBlocked);
            cmtElements += "</div>";
        }
        cmtElements += "</div>";
    }

    return cmtElements;
}

function commentBtn(commentDto, postBlocked) {
    var cmtElements = '';
    var cno = commentDto.cno;

    if (commentDto.uno == authUserUno && postBlocked != "BLOCKED") { // 댓글 작성자에게 보이는 버튼
        cmtElements += "   <input type='hidden' id='pcno" + commentDto.pcno + "' value='" + commentDto.pcno + "'/>";
        cmtElements += "   <input type='hidden' id='cmtUno" + cno + "' value='" + commentDto.uno + "'/>";
        cmtElements += "   <button type='button' id='cmtUpdateBtn" + commentDto.cno + "' class='btn btn-outline-primary btn-sm' onclick='toggleUpdate(" + cno + ")'>수정</button>";
        cmtElements += "   <button type='button' id='cmtPostBtn" + cno + "' class='btn btn-outline-success btn-sm' style='display: none;' onclick='postComment(" + cno + ")'>등록</button>";
        cmtElements += "   <button type='button' id='cmtCancelBtn" + cno + "' class='btn btn-outline-secondary btn-sm' style='display: none;' onclick='toggleUpdate(" + cno + ")'>취소</button>";
        cmtElements += "   <button type='button' id='cmtDeleteBtn" + cno + "' class='btn btn-outline-danger btn-sm' onclick='deleteComment(" + cno + ")'>삭제</button>";
    } else {
        if (!commentDto.rcno) {
            cmtElements += "   <button type='button' id='cmtReportBtn" + cno + "' class='btn btn-warning btn-sm'  data-bs-toggle='modal' data-bs-target='#reportPop' onclick='reportCommentPopOpen(" + cno + ")'>신고</button>";
        } else {
            cmtElements += "   <button type='button' id='cmtReportCancelBtn" + commentDto.rcno + "' class='btn btn-warning btn-sm' onclick='reportCommentCancel(" + cno + ")'>신고 취소</button>";
            cmtElements += "   <button type='button' id='cmtReportChangeBtn" + commentDto.rcno + "' class='btn btn-outline-dark btn-sm' data-bs-toggle='modal' data-bs-target='#reportPop' onclick='reportCommentChangePopOpen(" + cno + ")'>신고 사유 변경</button>";
            cmtElements += "   <input type='hidden' id='brno" + cno + "' value='" + commentDto.brno + "'>";
        }
    }

    return cmtElements;
}

/*// 댓글 버튼 클릭
$('.btn-group').on('click', 'button', function () {
    alert("clicked");
    var cno = $(this).siblings('input[type="hidden"][id^="cno"]').val();
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
});*/

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
        url: contextPath + '/user/comments',
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
        url: contextPath + '/user/comments/' + cno,
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
        url: contextPath + '/user/comments/' + cno,
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

    // $("#reportPop").show();
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
        url: contextPath + '/user/comments/' + cno + '/report',
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
        url: contextPath + '/user/comments/' + cno + '/report',
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
        type: 'DELETE'
        , url: contextPath + '/user/comments/' + cno + '/report'
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
    $('html, body').animate({
        scrollTop: $('#comment-text').offset().top
    }, 500);

    var pcno = $(this).data('pcno');
    var uno = $(this).data('uno');
    var userId = $(this).data('userid');

    $('#comment-reply').data('pcno', pcno).data('uno', uno);
    $('#comment-reply').text(userId);
    $('#reply-cancel').show();
});
/*
// 멘션 취소
$('#reply-cancel').on('click', function () {
    cancelReply();
});*/

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
