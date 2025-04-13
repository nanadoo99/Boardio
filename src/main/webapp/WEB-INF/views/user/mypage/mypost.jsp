<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<section id="post-section" class="container mt-4">
    <div id="post">
        <c:if test="${postDto.contentState == 'BLOCKED'}">
            <div class="alert alert-danger">관리자에 의해 차단된 게시글입니다.</div>
        </c:if>

        <div class="d-flex justify-content-between align-items-end mb-3">
            <h2 id="title" class="mb-0 post-title">${postDto.title}</h2>
            <div>
                <span class="text-muted">${postDto.userId}</span>
                <span>&nbsp|&nbsp</span>
                <span class="text-muted">${postDto.formattedCreatedAtTime}</span>
            </div>

            <input type="hidden" id="postPno" name="pno" value="${postDto.pno}"/>
            <input type="hidden" id="postBlocked" value="${postDto.contentState == 'BLOCKED' ? true : false}"/>
        </div>
        <hr>

        <div id="postContent" class="mb-4">${postDto.content}</div>

    </div>
    <div class="d-flex justify-content-between">
        <div id="btnBox">
            <c:if test="${fn:contains(authUser.userRole, 'ADMIN')}">
                <button type="button" id="adminBtn" onclick="location.href='<c:url value='/admin/posts/${postDto.pno}'/>'" class="btn btn-secondary">관리자페이지에서 보기</button>
            </c:if>
            <button type="button" id="backBtn" onclick="location.href='<c:url value='/user/mypost/list'/>'"
                    class="btn btn-outline-secondary">목록
            </button>
        </div>
        <div id="btnBox1">
            <c:if test="${postDto.contentState != 'BLOCKED'}">
                <button type="button" id="updateBtn" onclick="mypostEdit();" class="btn btn-outline-primary">수정</button>
                <button type="button" id="deleteBtn" onclick="mypostDelete();" class="btn btn-outline-danger">삭제
                </button>
            </c:if>
        </div>
    </div>
</section>

<section id="comment-section" class="container mt-4">
    <h3 class="mb-3">댓글</h3>
    <div id="comments" class="mb-3"></div>

    <c:if test="${postDto.contentState != 'BLOCKED'}">
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
    </c:if>
</section>

<!-- 모달 -->
<section id="reportPop" class="modal" tabindex="-1" data-bs-backdrop="true">
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
                    <label for="reportPopBrno">신고 사유</label>
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
    $(function () {
        var mode = "${mode}";
        if (mode === "update") {
            alert("수정 완료");
        }

        readComments();
    });

    function mypostEdit() {
        var pno = $('#postPno').val();
        location.href = contextPath + "/user/mypost/edit/" + pno;
    }

    function mypostDelete() {
        if (confirm("게시글을 삭제하시겠습니까?")) {
            var pno = $('#postPno').val();
            location.href = contextPath + "/user/mypost/deleteFileSetFromServer/" + pno;
        }
    }
</script>

<script src="/static/js/commentUser.js"></script>