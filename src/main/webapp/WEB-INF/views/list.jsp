<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<script>
    let sucess = "${sucess}"
    if(sucess=="DELETE") alert("삭제 성공");

    let failed = "${failed}"
    if(failed == "READ") alert("게시물 읽기 실패");
</script>

<h1>게시판 리스트</h1>
<button onclick="location.href = '<c:url value="/user/post/create"/>'">글쓰기</button>

<form action='<c:url value="/user/post/list"/>' method="get">
    <select name="option">
        <option value="" ${ph.sc.option == '' ? 'selected' : ''}>전체</option>
        <option value="id" ${ph.sc.option == 'id' ? 'selected' : ''}>작성자</option>
        <option value="title" ${ph.sc.option == 'title' ? 'selected' : ''}>제목</option>
        <option value="content" ${ph.sc.option == 'content' ? 'selected' : ''}>내용</option>
    </select>
    <input type="text" name="keyword" value='<c:out value="${ph.sc.keyword}"/>'/>
    <button type="submit">검색</button>
</form>


<table>
    <tr>
        <th class="pno">번호</th>
        <th class="index">번호</th>
        <th class="title">제목</th>
        <th class="user">이름</th>
        <th class="createdAt">등록일</th>
        <th class="views">조회수</th>
    </tr>
    <c:forEach var="postDto" items="${list}" varStatus="status">
        <c:set var="index" value="${totalCnt - (ph.sc.page-1) * (ph.sc.pageSize) - (status.index)}"/>
        <tr>
                <%--            검토: pno - DB 값이 아닌 자동생성 (삭제 처리되어 안보일 경우 대비) --%>
            <td class="pno">${postDto.pno}</td>
            <td class="index">${index} , ${status.index}</td>
            <td class="title"><a href="<c:url value='/user/post/read${ph.sc.queryString}&pno=${postDto.pno}'/>">${postDto.title}</a></td>
            <td class="user">${postDto.userId}</td>
            <c:choose>
                <c:when test="${postDto.createdAt.time >= startOfToday}">
                    <td class="createdAt"><fmt:formatDate value="${postDto.createdAt}" pattern="HH:mm" type="time"/></td>
                </c:when>
                <c:otherwise>
                   <td class="createdAt"><fmt:formatDate value="${postDto.createdAt}" pattern="yyyy-MM-dd" type="date"/></td>
                </c:otherwise>
            </c:choose>
            <td class="views">${postDto.views}</td>
        </tr>
    </c:forEach>
</table>

<div class="paging-container">
    <div class="paging">
        <c:if test="${totalCnt==null || totalCnt==0}">
            <div> 게시물이 없습니다. </div>
        </c:if>
        <c:if test="${totalCnt!=null && totalCnt!=0}">
            <c:if test="${ph.showPrev}">
                <a class="page" href="<c:url value="/user/post/list${ph.sc.getQueryString(ph.beginPage-1)}"/>">&lt;</a>
            </c:if>
            <c:forEach var="i" begin="${ph.beginPage}" end="${ph.endPage}">
                <a class="page ${i==ph.sc.page? "paging-active" : ""}" href="<c:url value="/user/post/list${ph.sc.getQueryString(i)}"/>">${i}</a>
            </c:forEach>
            <c:if test="${ph.showNext}">
                <a class="page" href="<c:url value="/user/post/list${ph.sc.getQueryString(ph.endPage+1)}"/>">&gt;</a>
            </c:if>
        </c:if>
<%--        <c:if test="${totalCnt!=null && totalCnt!=0}">--%>
<%--            <c:if test="${ph.showPrev}">--%>
<%--                <a href="<c:url value='/user/post/list?page=${ph.beginPage - 1}&pageSize=${ph.sc.pageSize}'/>">&lt;</a>--%>
<%--            </c:if>--%>
<%--            <c:forEach var="i" begin="${ph.beginPage}" end="${ph.endPage}">--%>
<%--                <a href="<c:url value='/user/post/list?page=${i}&pageSize=${ph.sc.pageSize}'/>">${i}</a>--%>
<%--            </c:forEach>--%>
<%--            <c:if test="${ph.showNext}">--%>
<%--                <a href="<c:url value='/user/post/list?page=${ph.endPage + 1}&pageSize=${ph.sc.pageSize}'/>">&gt;</a>--%>
<%--            </c:if>--%>
<%--        </c:if>--%>
    </div>
</div>