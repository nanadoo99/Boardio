<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<main>
    회원가입 페이지

    <form id="signupForm" name="signupForm">
        <%-- 검토: js에서 하나씩 보내는데, 굳이 form 태그를 사용할 필요가 있을까.  --%>
            <input type="text" id="redirctURL" name="redirctURL" value="${redirctURL}"/>
            <div>
            <label for="signupId">아이디</label>
            <input type="text" id="signupId" name="id" value="${user.id}"/>
            <span id="idValid">아이디는 ...  </span>
            <span id="idChk"></span>
        </div>
        <div>
            <label for="signupPassword">비밀번호</label>
            <input type="text" id="signupPassword" name="password" value="${user.password}"/>
            <span id="passwordValid">비밀번호는 ...  </span>
        </div>
        <div>
            <button type="button" id="signupBtn">회원가입</button>
        </div>
    </form>
</main>

<script>

    $(document).ready(function () {
        $('#signupPassword').keyup(function () {
            var id = $.trim($(this).val());
            if (!isValidPasswordFormat(id)) {
                $('#passwordValid').css('color', 'red');
            } else {
                $('#passwordValid').css('color', '');
            }
        });

        $('#signupId').keyup(function () {
            var id = $.trim($(this).val());
            var isValidId = isValidIdFormat(id);
            if (isValidId && id.length >= 3) {
                $.ajax({
                    type: 'POST',
                    url: "<c:url value='/public/idChk'/>",
                    data: {id: id},
                    success: function(response) {
                        if(response == "Avail") {
                            $('#idChk').text('사용가능한 아이디입니다.');
                        } else {
                            $('#idChk').text('이미 사용중인 아이디입니다.');
                        }
                    }
                });
            } else {
                $('#idChk').text('');
            }
            // 아이디 유효성 검사 결과 표시
            if (!isValidId) {
                $('#idValid').css('color', 'red');
            } else {
                $('#idValid').css('color', '');
            }
        });

        $('#signupBtn').on('click', function (event) {
            var id = $.trim($('#signupId').val());
            var password = $.trim($('#signupPassword').val());

            if (!id || !password) {
                alert("아이디와 비밀번호를 입력하세요.");
                return;
            }

            if (!isValidIdFormat(id)) {
                alert("아이디 형식이 올바르지 않습니다.");
                return;
            }

            if (!isValidPasswordFormat(password)) {
                alert("비밀번호 형식이 올바르지 않습니다.");
                return;
            }

            event.preventDefault();
            let form = $('#signupForm');

            form.attr('action', "<c:url value='/public/signup'/>");
            form.attr('method', 'post');
            form.submit();
        });
    });

    function isValidIdFormat(id) {
        let idRegex = /^(?=.*[a-z])[a-z0-9]{4,12}$/;
        // return idRegex.test(id); 체크 - 유효성 검사
        return true;
    }

    function isValidPasswordFormat(password) {
        let passwordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*])[a-z0-9!@#$%^&*]{6,}$/;
        // return passwordRegex.test(password); 체크 - 유효성 검사
        return true;
    }

</script>