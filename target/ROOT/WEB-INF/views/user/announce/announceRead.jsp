<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<section id="announcement-detail" class="container mt-4">
    <input type="hidden" id="searchCondition" value="${sc.queryString}"/>
    <input type="hidden" id="ano" value="${announceDto.ano}"/>

    <div>
        <h2 id="title" class="mb-0 post-title">${announceDto.title}</h2>

    </div>
    <hr>
    <div class="text-end mb-3">
        <span class="text-muted">${announceDto.formattedPostedAt}</span>
    </div>

    <div id="content" class="mb-4">${announceDto.content}</div>

    <div class="d-flex justify-content-between">
        <div id="btnBox">
            <c:if test="${fn:contains(authUser.userRole, 'ADMIN')}">
                <button type="button" id="adminBtn" onclick="adminAnnounce();" class="btn btn-secondary">관리자페이지에서 보기
                </button>
            </c:if>
            <button class="btn btn-outline-secondary" onclick="backToList()">목록</button>
        </div>

        <div id="btnBox1">
            <c:if test="${fn:contains(authUser.userRole, 'ADMIN')}">
                <button type="button" onclick="location.href = contextPath + '/admin/announces/new';"
                        class="btn btn-outline-secondary">새글쓰기
                </button>
            </c:if>
            <c:if test="${(authUser.userRole == 'ADMIN' && announceDto.uno == sessionScope.authUser.uno) || authUser.userRole == 'SUPER_ADMIN'}">
                <button type="button" class="btn btn-outline-primary" onclick="announceEdit();">수정</button>
                <button type="button" class="btn btn-outline-danger" onclick="announceDelete();">삭제</button>
            </c:if>
        </div>
    </div>
</section>

<section id="comment-section" class="container mt-4">
    <div class="card">
        <h5 class="card-body">첨부파일</h5>
        <c:if test="${announceDto.fileDtoList.size() > 0}">
            <ul class="list-group list-group-flush">
                <c:forEach var="fileDto" items="${announceDto.fileDtoList}">
                    <li class="list-group-item">
                        <span>${fileDto.fileOrgNm}</span>
                        <a href="<c:url value='/user/announces/files/${fileDto.fno}'/>" class="btn btn-link">다운로드</a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>
</section>

<script>
    function backToList() {
        var sc = $('#searchCondition').val();
        location.href = contextPath + "/user/announces" + sc;
    }

    function adminAnnounce() {
        var sc = $('#searchCondition').val();
        location.href = contextPath + "/admin/announces/" + $('#ano').val() + sc;
    }

    function announceEdit() {
        var ano = $('#ano').val();
        var sc = $('#searchCondition').val();
        location.href = contextPath + "/admin/announces/" + ano + "/form" +  sc;
    }

    function announceDelete() {
        if (confirm("공지를 삭제하시겠습니까?")) {
            var ano = $('#ano').val();
            var sc = $('#searchCondition').val();

            $.ajax({
                url: contextPath + "/admin/announces/" + ano, // DELETE 요청을 보낼 URL
                type: 'DELETE', // DELETE 메서드 설정
                success: function(response) {
                    alert("삭제되었습니다.");
                    // 삭제 후 이동할 페이지
                    location.href = contextPath + "/user/announces";
                }, error: function (response) {
                    var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                    alert(errorMsg);
                }
            });
        }
    }
</script>
