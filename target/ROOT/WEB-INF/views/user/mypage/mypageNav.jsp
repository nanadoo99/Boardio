<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="topMenu">
    <ul>
        <li><a href="<c:url value='/user/mypost/list'/>">작성 글</a></li>
        <li><a href="<c:url value='/user/mycomments/list'/>">작성 댓글</a></li>
<%--        <li><a href="<c:url value='/user/mypage/mylike'/>">좋아요 누른 글</a></li>--%>
        <li><a href="<c:url value='/user/profile'/>">회원정보 수정</a></li>
    </ul>
</div>


2depthNav

