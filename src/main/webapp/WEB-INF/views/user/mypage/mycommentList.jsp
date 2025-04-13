<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<section id="listSection" class="mb-4">
    <table id="listCont" class="table table-hover">
        <colgroup>
            <col style="width: 6%;"> <!-- 번호 -->
            <col style="">
            <col style="width: 30%;">
            <col style="width: 16%;">
        </colgroup>
        <thead>
        <tr>
            <th></th>
            <th>댓글내용</th>
            <th>게시글</th>
            <th>작성일</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty commentList}">
                <c:forEach var="item" items="${commentList}" varStatus="status">
                    <tr style='cursor:pointer;' onclick="location.href='<c:url value='/user/mypost/read/${item.pno}'/>'">
                        <td>${ph.totalCnt - (ph.sc.page-1) * ph.sc.pageSize - status.index}</td>
                        <td>${item.comment}</td>
                        <td>${item.postTitle}</td>
                        <td>${item.formattedUpdatedAt}</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="4" style="text-align: center;">게시글이 없습니다.</td>
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
                    <a class="page-link" href="<c:url value='/user/mycomments/list?page=${ph.beginPage-1}'/>">
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
                <a class="page-link" href="<c:url value='/user/mycomments/list?page=${i}'/>">${i}</a>
                </li>
            </c:forEach>
            <c:if test="${ph.showNext}">
                <li class="page-item" style="cursor: pointer">
                    <a class="page-link" href="<c:url value='/user/mycomments/list?page=${ph.endPage+1}'/>">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</section>