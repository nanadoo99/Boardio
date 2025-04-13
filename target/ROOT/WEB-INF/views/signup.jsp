<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container d-flex justify-content-center align-items-center">
    <div class="card p-4 m-5 shadow" style="width: 100%; max-width: 500px;">
        <h3 class="text-center mb-4">회원가입</h3>
        <%--<form id="userInfoForm" name="userInfoForm">
            <!-- 아이디 입력 -->
            <div class="mb-3">
                <div class="d-flex justify-content-between">
                    <label for="id" class="form-label">아이디</label>
                    <div id="idChk" class="form-text"></div>
                </div>
                <input type="text" class="form-control" id="id" name="id" value="${userDto.id}"
                       placeholder="아이디를 입력하세요"/>
                <div id="idValid" class="form-text">아이디는 ...</div>
            </div>

            <!-- 이메일 입력 -->
            <div class="mb-3">
                <div class="d-flex justify-content-between">
                    <label for="email" class="form-label">이메일</label>
                    <div id="emailChk" class="form-text"></div>
                </div>
                <div class="d-flex">
                    <input type="text" class="form-control me-2" id="email" name="email" value="${userDto.email}"
                           placeholder="이메일을 입력하세요"/>
                    <button type="button" class="btn btn-outline-secondary" style="white-space: nowrap;"
                            id="sendEmailAuth">이메일 인증
                    </button>
                </div>
                <div id="emailValid" class="form-text">이메일 형식을 확인하세요.</div>
            </div>

            <!-- 이메일 인증번호 입력 -->
            <div id="emailAuthDiv" class="mb-3" style="display:none;">
                <div class="d-flex justify-content-between">
                    <label for="emailAuth" class="form-label">인증번호</label>
                    <div id="timer" class="form-text w-25">남은 시간: 05:00</div> <!-- 타이머 표시 -->
                </div>
                <div class="d-flex">
                    <input type="text" class="form-control me-2" id="emailAuth" name="emailAuth" placeholder="인증번호를 입력하세요"/>
                    <button type="button" class="btn btn-outline-primary" id="checkEmailAuth" style="white-space: nowrap;">인증번호 확인</button>
                </div>
                <div id="emailAuthValid" class="form-text"></div>
            </div>

            <!-- 비밀번호 입력 -->
            <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <input type="password" class="form-control" id="password" name="password" value="${userDto.password}"
                       placeholder="비밀번호를 입력하세요"/>
                <div id="passwordValid" class="form-text">비밀번호는 ...</div>
            </div>

            <!-- 비밀번호 확인 -->
            <div class="mb-3">
                <div class="d-flex justify-content-between">
                    <label for="passwordConfirm" class="form-label">비밀번호 확인</label>
                    <div id="passwordConfirmValid" class="form-text"></div>
                </div>
                <input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm"
                       placeholder="비밀번호를 다시 입력하세요"/>
            </div>

            <!-- 회원가입 버튼 -->
            <button type="button" class="btn btn-primary w-100 mt-3" id="signupBtn">회원가입</button>
        </form>--%>
        <form:form id="userInfoForm" modelAttribute="userDto">

            <!-- 아이디 입력 -->
            <div class="mb-3">
                <div class="d-flex justify-content-between">
                    <label for="id" class="form-label">아이디</label>
                    <div id="idChk" class="form-text"></div>
                </div>
                <form:input path="id" cssClass="form-control" id="id" placeholder="아이디를 입력하세요" />
                <form:errors path="id" cssClass="form-text text-danger"/>
            </div>

            <!-- 이메일 입력 -->
            <div class="mb-3">
                <div class="d-flex justify-content-between">
                    <label for="email" class="form-label">이메일</label>
                    <div id="emailChk" class="form-text"></div>
                </div>
                <div class="d-flex">
                    <form:input path="email" cssClass="form-control me-2" id="email" placeholder="이메일을 입력하세요" />
                    <button type="button" class="btn btn-outline-secondary" style="white-space: nowrap;" id="sendEmailAuth">
                        이메일 인증
                    </button>
                </div>
                <form:errors path="email" cssClass="form-text text-danger"/>
            </div>

            <!-- 이메일 인증번호 입력 -->
            <div id="emailAuthDiv" class="mb-3" style="display:none;">
                <div class="d-flex justify-content-between">
                    <label for="emailAuth" class="form-label">인증번호</label>
                    <div id="timer" class="form-text w-25">남은 시간: 05:00</div>
                </div>
                <div class="d-flex">
                    <form:input path="emailAuth" cssClass="form-control me-2" id="emailAuth" placeholder="인증번호를 입력하세요" />
                    <button type="button" class="btn btn-outline-primary" id="checkEmailAuth" style="white-space: nowrap;">
                        인증번호 확인
                    </button>
                </div>
                <form:errors path="emailAuth" cssClass="form-text text-danger"/>
            </div>

            <!-- 비밀번호 입력 -->
            <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <form:password path="password" cssClass="form-control" id="password" placeholder="비밀번호를 입력하세요" />
                <form:errors path="password" cssClass="form-text text-danger"/>
            </div>

            <!-- 비밀번호 확인 -->
            <div class="mb-3">
                <div class="d-flex justify-content-between">
                    <label for="passwordConfirm" class="form-label">비밀번호 확인</label>
                    <div id="passwordConfirmValid" class="form-text"></div>
                </div>
                <form:password path="passwordConfirm" cssClass="form-control" id="passwordConfirm" placeholder="비밀번호를 다시 입력하세요" />
                <form:errors path="passwordConfirm" cssClass="form-text text-danger"/>
            </div>

            <!-- 회원가입 버튼 -->
            <button type="submit" class="btn btn-primary w-100 mt-3" id="signupBtn">회원가입</button>

        </form:form>

        <!-- 구분선 -->
        <div class="d-flex align-items-center mt-3">
            <hr class="flex-grow-1">
            <span class="px-2">or</span>
            <hr class="flex-grow-1">
        </div>

        <!-- Google로 회원가입 -->
        <div class="text-center mt-3">
            <a href="<c:url value='/oauth2/authorization/google'/>" class="btn btn-outline-danger w-100">Google로
                회원가입하기</a>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="/static/js/userInfoValidSignup.js"></script>