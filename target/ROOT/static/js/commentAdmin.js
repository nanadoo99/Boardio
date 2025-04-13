function readComments() {
    var pno = $('#postPno').val();

    $.ajax({
        url: contextPath + '/admin/comments/' + pno + '/comments',
        type: 'GET',
        success: function (response) {
            var commentList = response.comments;
            $('#comments').empty();
            $('#commentCount').text(commentList.length);

            var cmtElements = '';

            commentList.forEach(function (commentDto) {

                if (commentDto.pcno != commentDto.cno) { // 멘션 표시
                    cmtElements += "<span class='h5 text-muted'>∟</span>";
                    cmtElements += "<div id='cmtBox" + commentDto.cno + "'  class='card mb-3' style='margin-left: 3rem;'>";
                } else {
                    cmtElements += "<div id='cmtBox" + commentDto.cno + "'  class='card mb-3'>";
                }
                cmtElements += "<div class='card-body'>";
                cmtElements += "<span class='text-muted'>댓글 번호: " + commentDto.cno + "</span>";
                cmtElements += "   <input type='hidden' id='cno" + commentDto.cno + "' value='" + commentDto.cno + "'/>";
                if (commentDto.del) {
                    cmtElements += " <span>삭제된 댓글입니다.</span>";
                } else {
                    cmtElements += completeCommentAdmin(commentDto);
                }
                cmtElements += "</div>";
                cmtElements += "</div>";
            });
            $('#comments').append(cmtElements);
            $('#post-section').show();
        },
        error: function (error) {
            alert("댓글을 불러오지 못했습니다.");
        }
    });
}

function completeCommentText(commentDto) {
    var cmtElements = '';
    cmtElements += "<div class='w-75 me-3'>";
    // comments += "   <input type='text' id='cmtText" + commentDto.cno + "' value='" + commentDto.comment + "' class='card-text w-75' readonly/>";
    cmtElements += "   <textarea type='text' id='cmtText" + commentDto.cno + "' class='card-text w-100' readonly>" + commentDto.comment + "</textarea>";
    if (commentDto.contentState === "BLOCKED") {
        cmtElements += "       <div> 차단됨: " + commentDto.brText + "</div>";
    }
    cmtElements += "</div>";
    cmtElements += "   <input type='hidden' id='pcno" + commentDto.pcno + "' value='" + commentDto.pcno + "'/>";
    cmtElements += "   <input type='hidden' id='cmtUno" + commentDto.cno + "' value='" + commentDto.uno + "'/>";

    return cmtElements;
}

function completeCommentAdmin(commentDto) {
    var cmtElements = '';

    cmtElements += "   <div class='card-title'>";
    // cmtElements += "       <a data-pcno='" + commentDto.pcno + "' data-uno='" + commentDto.uno + " 'data-userid='" + commentDto.userId + "'>" + commentDto.userId + "</a>";
    cmtElements += "       <span class='h5'>" + commentDto.userId + "</span>";
    cmtElements += "   <small type='text' class='card-text text-muted' >" + commentDto.formattedCreatedAtTime + "</small>";
    cmtElements += "   </div>";
    cmtElements += "<div class='d-flex justify-content-between align-items-end'>";
    cmtElements += completeCommentText(commentDto);
    cmtElements += commentBtn(commentDto);
    cmtElements += "</div>";

    return cmtElements;
}

function commentBtn(commentDto) {
    var cmtElements = '';
    var cno = commentDto.cno;

    cmtElements += "<div class='btn-group d-flex align-items-end'>";
    if (commentDto.rcnoCnt > 0) {
        cmtElements += "   <button class='btn btn-secondary btn-sm' id='cmtReportHistoryBtn" + commentDto.cno + "' data-bs-toggle='modal' data-bs-target='#commentReportHistoryPop' onclick='reportCommentHistory(" + commentDto.cno + " )'>유저 신고(" + commentDto.rcnoCnt + ")</button>";
    }
    if (commentDto.contentState === "BLOCKED") {
        cmtElements += "   <button class='btn btn-outline-success btn-sm' data-bs-toggle='modal' data-bs-target='#commentBlockPop' id='cmtBlockChangeBtn" + commentDto.cno + "' onclick='commentBlockChangePopOpen(" + commentDto.cno + " )'>차단 사유 변경</button>";
        cmtElements += "   <button class='btn btn-outline-danger btn-sm' id='cmtUnblockBtn" + commentDto.cno + "' onclick='commentUnblock(" + commentDto.cno + " )'>차단 해제</button>";
    } else {
        cmtElements += "   <button class='btn btn-outline-danger btn-sm' id='cmtBlockBtn" + commentDto.cno + "' data-bs-toggle='modal' data-bs-target='#commentBlockPop' onclick='commentBlockPopOpen(" + commentDto.cno + " )'>차단</button>";
    }
    cmtElements += "</div>";

    return cmtElements;
}

// ====== 댓글 신고 관련 ====== //

// 댓글 신고 내역 - 리스트
function reportCommentHistory(cno) {
    // 데이터 불러오기 - 에러 존재하지 않는 댓글 일때.
    //보여주기(요약, 리스트)
    $.ajax({
        type: 'POST'
        , url: contextPath + '/admin/comments/' + cno + '/reports'
        , dataType: 'json'
        , success: function (response) {
            var reportSummary = response.reportSummary;
            var contentSummary = '';
            if (reportSummary && reportSummary.length > 0) {
                reportSummary.forEach(function (summary) {
                    contentSummary += '<tr>';
                    contentSummary += '   <td>' + summary.brText + '</td>';
                    contentSummary += '   <td>' + summary.brnoCnt + '</td>';
                    contentSummary += '</tr>';
                });
            } else {
                contentSummary = '<tr><td colspan="2">No Data</td></tr>';
            }

            var reportList = response.reportList;
            var contentList = '';
            if (reportList && reportList.length > 0) {
                reportList.forEach(function (reportedCmtDto) {
                    contentList += '<tr>';
                    contentList += '   <td>' + reportedCmtDto.formattedReportedAtTime + '</td>';
                    contentList += '   <td>' + reportedCmtDto.brText + '</td>';
                    contentList += '   <td>' + reportedCmtDto.reportUserId + '</td>';
                    contentList += '</tr>';
                });
            } else {
                contentList = '<tr><td colspan="3">No Data</td></tr>';
            }
            $('#commentReportHistoryDetailTbody').html(contentList);
            $('#commentReportHistorySummaryTbody').html(contentSummary);
            $('#commentReportHistoryPop').show();

        }, error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}

// 댓글 신고 내역 - 닫기
function commentReportHistoryPopClose() {
    $('#commentReportHistoryPop').hide();
    $('#commentReportHistorySummaryTbody').html("");
    $('#commentReportHistoryDetailTbody').html("");
}

// ====== 댓글 차단 ====== //

// 차단
function commentBlockPopOpen(cno) {

    $('#commentBlockPopTitle').text("댓글 차단");
    $('#blockCommentBtn').attr("onclick", "blockComment(" + cno + ");");

    $('#commentBlockPop').show();
}

function commentBlockPopClose() {
    $('#commentBlockPop').modal('hide');

    $('#commentBlockPopCno').val("");
    $('#commentBlockPopComment').val("");
    $('#commentBlockPopUno').val("");
    $('#commentBlockPopForm input[type="checkbox"]').prop('checked', false);
}


function blockComment(cno) {
    if (!confirm("댓글을 차단하시겠습니까?")) return;
    if ($('input[name="brnoList"]:checked').length === 0) {
        alert("차단 사유를 선택하세요.");
        return;
    }
    $('#commentBlockPopIdx').attr("name", "");
    $('#commentBlockPopIdx').val(cno);
    $('#commentBlockPopUno').val(authUserUno);

    $.ajax({
        type: 'POST',
        url: contextPath + '/admin/comments/' + cno + '/blockNew',
        data: $('#commentBlockPopForm').serialize(),
        dataType: 'json',
        success: function (response) {
            alert("댓글이 차단되었습니다.");
            blockPopClose();
        },
        error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}

// 댓글 차단 변경

function commentBlockChangePopOpen(cno) {

    $('#commentBlockPopTitle').text("댓글 차단 사유 변경");
    $('#blockCommentBtn').attr("onclick", "blockChangeComment(" + cno + ");");

    // $('#commentBlockPop').show();
}

function blockChangeComment(cno) {
    if (!confirm("차단 사유를 변경하시겠습니까?")) return;
    if ($('input[name="brnoList"]:checked').length === 0) {
        alert("차단 사유를 선택하세요.");
        return;
    }
    $('#commentBlockPopIdx').attr("name", "cno");
    $('#commentBlockPopIdx').val(cno);

    $.ajax({
        url: contextPath + '/admin/comments/' + cno + '/blockUpdate',
        type: 'PUT',
        // data: $('#commentBlockPopForm').serialize(),
        contentType: 'application/json',
        data: JSON.stringify({
            cno: cno,
            brnoList: $('input[name="brnoList"]:checked').map(function () {
                return $(this).val();
            }).get()
        }),
        dataType: 'json',
        success: function (response) {
            alert("게시글이 차단 사유가 변경되었습니다.");
            blockCommentPopClose();
        },
        error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}

function blockCommentPopClose() {
    $('#commentBlockPop').modal('hide');

    $('#commentBlockPopTitle').text("");
    $('#commentBlockPopIdx').attr("name", "");
    $('#commentBlockPopIdx').val("");
    $('#commentBlockPopUno').val("");
    $('#commentBlockPostBtn').attr("onclick", "");
    $("#commentBlockPop input[name='brnoList']").prop("checked", false);

    readPost($('#postPno').val());
}

// 댓글 차단 해제
function commentUnblock(cno) {
    if (!confirm("댓글 차단을 해제하시겠습니까?")) return;

    $.ajax({
        type: 'DELETE',
        url: contextPath + '/admin/comments/' + cno + '/block',
        dataType: 'JSON',
        success: function (data) {
            alert("댓글 차단이 해제되었습니다.");
            readPost($('#postPno').val());
        },
        error: function (response) {
            var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
            alert(errorMsg);
        }
    });
}