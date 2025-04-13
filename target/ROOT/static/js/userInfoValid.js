$(function () {
    $('#password, #passwordConfirm').keyup(function () {
        var password = $.trim($('#password').val());
        var passwordConfirm = $.trim($('#passwordConfirm').val());

        if (!isValidPasswordFormat(password)) {
            $('#passwordValid').css('color', 'red');
        } else {
            $('#passwordValid').css('color', '');
        }

        if (passwordConfirm !== "" && password !== passwordConfirm) {
            $('#passwordConfirmValid').css('color', 'red');
            $('#passwordConfirmValid').text('비밀번호가 일치하지 않습니다.');
        } else {
            $('#passwordConfirmValid').css('color', '');
            $('#passwordConfirmValid').text('');
        }
    });

    $('#id').keyup(async function () {
        var id = $.trim($(this).val());
        // 아이디 유효성 검사 결과 표시
        if (!isValidIdFormat(id)) {
            $('#idValid').css('color', 'red');
        } else {
            $('#idValid').css('color', '');
            var isUnique = await isUniqueId(id);
            switch (isUnique) {
                case 1:
                    $('#idChk').text('사용가능한 아이디입니다.');
                    break;
                case 0:
                    $('#idChk').text('');
                    break;
                case -1:
                    $('#idChk').text('이미 사용 중인 아이디입니다.');
                    break;
            }
        }
    });


    $('#email').keyup(async function () {
        var email = $.trim($(this).val());
        // 이메일 유효성 검사 결과 표시
        if (!isValidEmailFormat(email)) {
            $('#emailValid').css('color', 'red');
        } else {
            $('#emailValid').css('color', '');
            var isUnique = await isUniqueEmail(email);
            switch (isUnique) {
                case 1:
                    $('#emailChk').text('가입가능한 이메일입니다.');
                    break;
                case 0:
                    $('#emailChk').text('');
                    break;
                case -1:
                    $('#emailChk').text('이미 가입된 이메일입니다.');
                    break;
            }
        }
    });

    $('#signupBtn').on('click', async function (event) {
        var id = $.trim($('#id').val());
        var email = $.trim($('#email').val());
        var password = $.trim($('#password').val());
        var passwordConfirm = $.trim($('#passwordConfirm').val());


        if (mode === "signup") {

            if (!id || !email || !password || !passwordConfirm) {
                alert("아이디, 이메일, 비밀번호를 모두 입력하세요.");
                return;
            }

            if (!isValidEmailFormat(email)) {
                alert("이메일 형식이 올바르지 않습니다.");
                return;
            }

            if (await isUniqueEmail(email) === -1) {
                alert("이미 가입된 이메일입니다.");
                return;
            }
        }


        if (!id || !password || !passwordConfirm) {
            alert("아이디, 비밀번호를 모두 입력하세요.");
            return;
        }

        if (!isValidIdFormat(id)) {
            alert("아이디 형식이 올바르지 않습니다.");
            return;
        }

        if (await isUniqueId(id) === -1) {
            alert("이미 사용중인 아이디입니다.");
            return;
        }

        if (!isValidPasswordFormat(password)) {
            alert("비밀번호 형식이 올바르지 않습니다.");
            return;
        }

        if (password !== passwordConfirm) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        event.preventDefault();
        let form = $('#userInfoForm');
        if (mode === "signup") {
            form.attr('action', contextPath + '/auth/signup');
            form.attr('method', 'post');
            form.submit();
        } else if (mode === "profile") {
            form.attr('action', contextPath + '/user/profile/' + id);
            form.attr('method', 'post');
            form.submit();
        } else {
            alert("에러 발생. 관리자에게 문의하세요");
        }
    });
});

function isUniqueId(id) {
    return new Promise((resolve, reject) => {
        if (id != authUserId) {
            $.ajax({
                type: 'POST',
                url: contextPath + '/auth/check-id',
                data: {id: id},
                success: function (response) {
                    if (response == "Avail") {
                        resolve(1);
                    } else {
                        resolve(-1);
                    }
                },
                error: function (error) {
                    reject(error);
                }
            });
        } else {
            resolve(0);
        }
    });

}

function isValidIdFormat(id) {
    let idRegex = /^[a-zA-Z0-9]{1,20}$/; // 검토: 유효성 검사
    return idRegex.test(id);
}


function isUniqueEmail(email) {
    var id = $('#id').val();
    return new Promise((resolve, reject) => {
        if (id != authUserId) {
            $.ajax({
                type: 'POST',
                url: contextPath + '/auth/check-email',
                data: {email: email},
                success: function (response) {
                    if (response == "Avail") {
                        resolve(1);
                    } else {
                        resolve(-1);
                    }
                },
                error: function (error) {
                    reject(error);
                }
            });
        } else {
            resolve(0);
        }
    });

}

function isValidEmailFormat(email) {
    let idRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/; // 검토: 유효성 검사
    return idRegex.test(email);
}


function isValidPasswordFormat(password) {
    let passwordRegex = /^[0-9]+$/; // 검토: 유효성 검사
    return passwordRegex.test(password);
}