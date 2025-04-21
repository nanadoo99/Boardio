<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<section id="listSection" class="mb-4">
    <table id="listCont" class="table table-hover">
        <colgroup>
            <col style="width: 6%;"> <!-- 번호 -->
            <col style="">
            <col style="width: 6%;"> <!-- 조회수 -->
            <col style="width: 6%;"> <!-- 글번호 -->
            <col style="width: 16%;"> <!-- 작성일 -->
            <col style="width: 20%;">
        </colgroup>
        <thead>
        <tr>
            <th></th>
            <th>제목</th>
            <th>조회수</th>
            <th>글번호</th>
            <th>작성일</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty postList}">
                <c:forEach var="postDto" items="${postList}" varStatus="status">
                    <tr style="cursor:pointer;"
                        onclick="location.href='<c:url value='/user/mypost/read/${postDto.pno}'/>'">
                        <td>${ph.totalCnt - (ph.sc.page-1) * ph.sc.pageSize - status.index}</td>
                        <td>${postDto.title}</td>
                        <td>${postDto.views}</td>
                        <td>${postDto.pno}</td>
                        <td>${postDto.formattedCreatedAtTime}</td>
                        <td><c:if test="${postDto.contentState == 'BLOCKED'}">관리자에 의해 차단되었습니다.</c:if></td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="6" style="text-align: center;">게시글이 없습니다.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</section>

<section id="paginationSection">
    <nav aria-label="Page navigation">
        <ul id="pagination" class="pagination justify-content-center">
            <c:if test="${ph.showPrev}">
                <li class="page-item" style="cursor: pointer">
                    <a class="page-link" href="<c:url value='/user/mypost/list?page=${ph.beginPage-1}'/>">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>
            <c:forEach begin="${ph.beginPage}" end="${ph.endPage}" var="i" step="1">
                <c:choose>
                    <c:when test="${sc.page == i}">
                        <li class="page-item active" style="cursor: pointer">
                    </c:when>
                    <c:otherwise>
                        <li class="page-item" style="cursor: pointer">
                    </c:otherwise>
                </c:choose>
                <a class="page-link" href="<c:url value='/user/mypost/list?page=${i}'/>">${i}</a>
                </li>
            </c:forEach>
            <c:if test="${ph.showNext}">
                <li class="page-item" style="cursor: pointer">
                    <a class="page-link" href="<c:url value='/user/mypost/list?page=${ph.endPage+1}'/>">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</section>

<script>
    $(function () {
        var mode = "${mode}";
        if (mode === "delete") {
            alert("삭제 완료");
        }
    });
</script>