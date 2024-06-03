<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<main>
    로그인 페이지입니다.

    <form id="loginForm" name="loginForm" action="<c:url value='/public/login'/>" method="post">
        <input type="text" id="redirctURL" name="redirctURL" value="${redirctURL}"/>
        <%-- 검토: js에서 하나씩 보내는데, 굳이 form 태그를 사용할 필요가 있을까.  --%>
        <div>
            <label for="loginId">아이디</label>
            <input type="text" id="loginId" name="id" value="nki"/>
        </div>
        <div>
            <label for="loginPassword">비밀번호</label>
            <input type="text" id="loginPassword" name="password" value="1234"/>
        </div>
        <div>
            <button type="submit" id="submit">로그인</button>
            <a href="<c:url value='/public/signup'/>">회원가입</a>
        </div>
    </form>
</main>

<script>
//     $(document).ready(function () {
//
//         $('#submit').on('click', function () {
//             alert("dff2");
// // 기본 폼 제출을 막습니다.
// // event.preventDefault();
//             var loginId = $('#loginId').val();
//             var loginPassword = $('#loginPassword').val();
//
//             if (!loginId || !loginPassword) {
//                 alert("아이디와 비밀번호를 입력하세요.");
//                 return;
//             }
//             console.log(contextPath + '/login');
// // var form = stringifyForm($('#loginForm'));
//
//             var header = $("meta[name='_csrf_header']").attr('content');
//             var token = $("meta[name='_csrf']").attr('content');
//
//             $.ajax({
//                 type: 'POST'
//                 , url: contextPath + '/auth/login'
//                 , data: JSON.stringify({id: loginId, password: loginPassword})
//                 , beforeSend: function (xhr) {
//                     xhr.setRequestHeader(header, token);
//                 }, contentType: 'application/json; charset=UTF-8'
//                 , success: function (response) {
//                     if (response.success) {
//
//                         let targetUrl;
//                         if (!response.orginURI || response.orginURI === '') {
//                             targetUrl = contextPath + '/';
//                         } else {
//                             targetUrl = response.orginURI.startsWith('/')
//                                 ? contextPath + response.orginURI
//                                 : contextPath + '/' + response.orginURI;
//                         }
//                         location.href = targetUrl;
//                         alert(response.message);
//                     } else {
// // 로그인 실패 시 에러 메시지 표시
//                         alert("failed");
//                     }
//                 }, error: function (jqXHR, textStatus, errorThrown) {
// // AJAX 요청 실패 처리
//                     console.error('AJAX Error: ' + textStatus, errorThrown);
//                     $('#errorMessage').text('로그인 요청에 실패했습니다. 다시 시도해주세요.');
//                 }
//             });
//         });
//     });
</script>