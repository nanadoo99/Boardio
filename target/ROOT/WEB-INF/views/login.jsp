<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container p-5 d-flex justify-content-center align-items-center">
    <div class="card p-4 m-5 shadow" style="width: 100%; max-width: 400px;">
        <h3 class="text-center mb-4">로그인</h3>
        <form action="<c:url value='/auth/login'/>" method="post"> <%--name="loginForm" --%>
            <div class="mb-3">
                <label for="loginEmail" class="form-label">이메일</label>
                <input type="text" class="form-control" id="loginEmail" name="email" value="${userDto.email}" placeholder="아이디를 입력하세요"/>
            </div>
            <div class="mb-3">
                <label for="loginPassword" class="form-label">비밀번호</label>
                <input type="password" class="form-control" id="loginPassword" name="password" value="${userDto.password}" placeholder="비밀번호를 입력하세요"/>
            </div>
            <div class="mb-3 d-flex justify-content-between">
                <a href="<c:url value='/auth/reset-password'/>" class="text-decoration-none">비밀번호를 잊으셨나요?</a>
            </div>
            <button type="submit" class="btn btn-primary w-100">로그인</button>
        </form>
        <div class="d-flex align-items-center mt-3">
            <hr class="flex-grow-1">
            <span class="px-2">또는</span>
            <hr class="flex-grow-1">
        </div>
        <div class="text-center mt-3">
            <a href="<c:url value='/oauth2/authorization/google'/>" class="btn btn-outline-danger w-100">Google로 로그인하기</a>
        </div>
        <div class="text-center mt-3">
            <a href="<c:url value='/auth/signup'/>" class="text-decoration-none">회원가입</a>
        </div>
    </div>
</div>