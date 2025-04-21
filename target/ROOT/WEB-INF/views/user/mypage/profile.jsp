<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container p-5  d-flex justify-content-center align-items-center">
    <div class="card p-4 m-5 shadow text-start" style="width: 100%; max-width: 400px;">
        <h2 class="text-center mb-4">회원정보 수정</h2>
        <form id="userInfoForm" name="userInfoForm">
            <div class="mb-3">
                <div class="d-flex justify-content-between">
                    <label for="id">아이디</label>
                    <div id="idChk" class="form-text"></div>
                </div>
                <input type="text" class="form-control" id="id" name="id" value="${userDto.id}" required/>
                <div id="idValid" class="form-text">아이디는 ... </div>
            </div>

            <c:if test="${!sessionScope.authUser.social}">
                <div class="mb-3">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" required/>
                    <p id="passwordValid" class="form-text">비밀번호는 ... </p>
                </div>
                <div class="mb-3">
                    <label for="passwordConfirm">비밀번호 확인</label>
                    <input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm" required/>
                    <div id="passwordConfirmValid" class="form-text">비밀번호 확인 ... </div>
                </div>
            </c:if>

            <button type="button" id="signupBtn" class="btn btn-primary w-100">회원정보 변경</button>
        </form>
    </div>
</div>

<div class="text-center">
    <a id="deleteAccount" href="<c:url value='/auth/deactivate'/>" class="text-primary">회원탈퇴</a>
</div>
<script>
    var mode = "${mode}";

    $('#deleteAccount').click(function (event) {
        event.preventDefault(); // 기본 링크 동작 막기

        // 사용자에게 확인 메시지를 표시
        if (confirm("정말로 회원탈퇴를 하시겠습니까?")) {
            // 사용자가 확인을 누른 경우, 원래의 href로 이동
            window.location.href = $(this).attr('href');
        } else {
            // 취소를 누른 경우, 아무 작업도 하지 않음
        }
    });
</script>
<c:choose>
    <c:when test="${!sessionScope.authUser.social}">
        <script src="/static/js/userInfoValid.js"></script>
    </c:when>
    <c:otherwise>
        <script src="/static/js/userInfoValidSocial.js"></script>
    </c:otherwise>
</c:choose>
