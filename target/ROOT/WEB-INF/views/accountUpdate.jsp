<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<main>
    회원가입 페이지

    <form id="userInfoForm" name="userInfoForm">
        <div>
            <label for="id">아이디</label>
            <input type="text" id="id" name="id" value="${userDto.id}"/>
            <span id="idValid">아이디는 ...  </span>
            <span id="idChk"></span>
        </div>
        <c:if test="${mode == 'signup'}">
            <div>
                <label for="email">이메일</label>
                <input type="text" id="email" name="email" value="${userDto.email}"/>
                <span id="emailValid">이메일 형식을 확인하세요.</span>
                <span id="emailChk"></span>
            </div>
        </c:if>
        <div>
            <label for="password">비밀번호</label>
            <input type="password" id="password" name="password" value="${userDto.password}"/>
            <span id="passwordValid">비밀번호는 ...  </span>
        </div>
        <div>
            <label for="passwordConfirm">비밀번호 확인</label>
            <input type="password" id="passwordConfirm" name="passwordConfirm"/>
            <span id="passwordConfirmValid">비밀번호 확인 ...  </span>
        </div>
        <div>
            <button type="button" id="signupBtn">회원가입</button>
        </div>
    </form>
</main>

<script>
    var mode = "${mode}";
</script>

<script src="/static/js/userInfoValid.js"></script>