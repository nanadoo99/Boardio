<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container d-flex justify-content-center align-items-center">
    <div class="card p-4 m-5 shadow" style="width: 100%; max-width: 500px;">
        <h3 class="card-title text-center mb-2">비밀번호 재설정</h3>
        <h6 class="card-subtitle text-center mb-4 text-body-secondary">가입하신 이메일로 임시 비밀번호를 보내드립니다.</h6>
        <form id="loginForm" name="loginForm" action="<c:url value='/auth/reset-password'/>" method="post">
            <div>
                <label for="loginEmail" class="form-label">가입 이메일</label>
                <input type="text" class="form-control" id="loginEmail" name="email" value="${email}" placeholder="email"/>
            </div>
            <button type="submit" class="btn btn-primary mt-3 w-100" id="submit">임시 비밀번호 전송</button>
        </form>
    </div>
</div>

<script>
    $(document).ready(function() {
        $("#loginForm").on("submit", function() {
            // 스피너 표시
            $("#spinner-overlay").removeClass("d-none");
        });
    });
</script>