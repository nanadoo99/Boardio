<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<section id="post-section" class="container mt-4">
    <div id="post">
        <div class="d-flex justify-content-between align-items-end mb-3">
            <h2 id="postTitle" class="mb-0 post-title"></h2>
            <div>
                <span class="text-muted" id="postWriter"></span>
                <span>&nbsp|&nbsp</span>
                <span class="text-muted" id="postCreatedAt"></span>
            </div>

            <input type="hidden" id="postPno" value=""/>
            <input type="hidden" id="postBlocked" value=""/>
        </div>
        <hr>

        <div id="postContent"></div>

        <div class="d-flex justify-content-between">
            <div id="btnBox">
                <c:if test="${fn:contains(authUser.userRole, 'ADMIN')}">
                    <button type="button" id="adminBtn" onclick="adminPost();" class="btn btn-secondary">관리자페이지에서 보기</button>
                </c:if>
                <button class="btn btn-outline-secondary" onclick="backToList()">목록</button>
                <button type="button" onclick="location.href = contextPath + '/user/post/postNew';"
                        class="btn btn-outline-primary">새글쓰기
                </button>
            </div>
            <div id="btnBox1">

            </div>
        </div>
        <div id="msg"></div>

    </div>
</section>

<section id="comment-section" class="container mt-4">
    <h5 class="mb-3">댓글</h5>
    <div id="comments" class="mb-3"></div>

    <div class="card mt-3">
        <div class="card-body">
            <span id="comment-reply" class="h5"></span>
            <button id="reply-cancel" style="display: none" class="btn btn-outline-secondary btn-sm mb-2"
                    onclick="cancelReply();">대댓글 취소
            </button>
            <textarea id="comment-text" class="form-control mb-2" placeholder="댓글을 작성하세요"></textarea>
            <button type="button" id="createCmtBtn" class="btn btn-success">댓글 등록</button>
        </div>
    </div>
</section>

<input type="hidden" id="searchCondition" value="${sc.queryString}"/>

<section id="reportPop" class="modal" tabindex="-1" data-bs-backdrop="true" style="display: none">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="reportPopTitle" class="modal-title"></h5>
                <button type="button" class="btn-close" onclick="reportPopClose();" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="reportPopForm" onsubmit="return false;">
                    <input type="hidden" name="" id="reportPopIdx"/>
                    <input type="hidden" name="text" id="reportPopText"/>
                    <input type="hidden" name="reportUno" id="reportPopReportUno"/>
                    <label class="mb-1" for="reportPopBrno">신고 사유</label>
                    <select class="form-select" id="reportPopBrno" name="brno">
                        <option value="" selected disabled>선택해주세요.</option>
                        <c:forEach var="brDto" items="${brList}">
                            <option value="${brDto.brno}">${brDto.brText}</option>
                        </c:forEach>
                    </select>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" id="reportBtn" class="btn btn-warning" onclick="">신고</button>
            </div>
        </div>
    </div>
</section>

<script>
    var postContent = "";
    var postPno = "";

    $(function () {

        readPost(${pno});
        var mode = "${mode}";
        if (mode === "create") {
            alert("등록 완료");
        } else if (mode === "update") {
            alert("수정 완료");

        }

    });

    function backToList() {
        var sc = $('#searchCondition').val();
        location.href = contextPath + "/user/post" + sc;
    }

    function readPost(pno) {
        $("#postPno").val(pno);
        $.ajax({
            type: "POST",
            url: contextPath + "/user/post/read/" + pno,
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                var postDto = response.postDto;
                if (postDto.blocked) {
                    alert("관리자에 의해 가려진 게시글입니다.");
                    backToList();
                    return;
                }
                postContent = postDto.content;
                postPno = postDto.pno;
                completePost(postDto);
                editPostBtns(response);
                readComments();
            },
            error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
                backToList();
            }
        });
    }

    function completePost(postDto) {
        $("#postTitle").text(postDto.title);
        $("#postCreatedAt").text(postDto.formattedCreatedAtTime);
        $("#postWriter").text(postDto.userId);
        $("#postContent").html(postDto.content);
        $("#postBlocked").html(postDto.contentState);
    }

    function postEdit() {
        var pno = $('#postPno').val();
        location.href = contextPath + "/user/post/postEdit/" + pno;
    }

    function postDelete() {
        if (confirm("게시글을 삭제하시겠습니까?")) {
            var pno = $('#postPno').val();
            var sc = $('#searchCondition').val();
            location.href = contextPath + "/user/post/postDelete/" + pno + sc;
        }
    }

    function adminPost() {
        var sc = $('#searchCondition').val();
        location.href = contextPath + "/admin/posts/" + $('#postPno').val() + sc;
    }

    function editPostBtns(response) {
        var tags = "";
        var postDto = response.postDto;
        var postReport = response.postReport;

        if (authUserUno == postDto.uno) {
            if (!postDto.blocked) tags += "<button type='button' class='btn btn-outline-primary mx-2' onclick='postEdit();'>수정</button>";
            tags += "<button type='button' class='btn btn-outline-danger' onclick='postDelete();'>삭제</button>";
        } else if (postReport) { // 신고한 게시글일 경우
            tags += "<input type='hidden' id='postReportBrno' value='" + postReport.brno + "'/>";
            tags += "<button type='button' class='btn btn-warning mx-2' data-bs-toggle='modal' data-bs-target='#reportPop' onclick='deleteReport();'>신고 취소</button>";
            tags += "<button type='button' class='btn btn-outline-dark' data-bs-toggle='modal' data-bs-target='#reportPop' onclick='reportPostChangePopOpen();'>신고 사유 변경</button>";
        } else {
            tags += "<button type='button' class='btn btn-warning' data-bs-toggle='modal' data-bs-target='#reportPop' onclick='reportPostPopOpen();'>신고</button>";

        }
        $("#btnBox1").html(tags)
    }

    // ===== 게시글 신고 ===== //

    // 게시글 신고 창열기
    function reportPostPopOpen() {
        $('#reportPopTitle').text("게시글 신고");
        $('#reportBtn').attr("onclick", "reportPost();");
        $('#reportBtn').text("신고");
        $("#reportPop").show();
    }

    // 게시글 신고 창열기 - 사유 변경
    function reportPostChangePopOpen() {
        $('#reportPopTitle').text("게시글 신고 사유 변경");
        $('#reportBtn').attr("onclick", "reportPost();");
        // 값 선택되어있게
        $('#reportPopBrno').val($('#postReportBrno').val());
        $('#reportBtn').text("변경");
        $("#reportPop").show();
    }

    // 게시글 신고
    function reportPost() {
        var brno = $("#reportPopBrno").val();
        if (brno == "" || !brno) {
            alert("신고 사유를 선택하세요.");
            return;
        }
        $("#reportPopIdx").attr("name", "pno");
        $("#reportPopIdx").val(postPno);
        $("#reportPopText").val(postContent);
        $("#reportPopReportUno").val(authUserUno);

        $.ajax({
            type: "POST",
            url: contextPath + "/user/post/report",
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

    // 신고 창 닫기
    function reportPopClose() {
        $('#reportPop').modal('hide');

        $('#reportPopTitle').text("");
        $("#reportPopIdx").attr("name", "");
        $("#reportPopIdx").val("");
        $("#reportPopBrno").val("");
        $("#reportPopText").val("");
        $("#reportPopReportUno").val("");
    }

    // 게시글 신고 취소
    function deleteReport() {
        if (!confirm("게시글 신고를 취소하시겠습니까?")) return;
        var pno = $('#postPno').val();
        $.ajax({
            type: 'POST'
            , url: contextPath + '/user/post/deleteReport/' + pno
            , dataType: 'json'
            , success: function (response) {
                alert("신고가 취소되었습니다.");
                readPost(pno);
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }
</script>
<script src="/static/js/commentUser.js"></script>