<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container p-5 d-flex justify-content-center align-items-center">
    <div class="card p-4 m-5 shadow" style="width: 100%; max-width: 400px;">
        <h5 class="text-center mb-4">회원정보 수정을 위해<br>다시 한 번 로그인해주세요.</h5>
        <form id="loginForm" name="loginForm" action="<c:url value='/auth/doubleLogin'/>" method="post">
            <div class="mb-3">
                <label for="loginEmail" class="form-label">이메일</label>
                <input type="email" class="form-control" id="loginEmail" name="email" value="${userDto.email}" required>
            </div>
            <div class="mb-3">
                <label for="loginPassword" class="form-label">비밀번호</label>
                <input type="password" class="form-control" id="loginPassword" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">로그인</button>
        </form>
    </div>
</div>