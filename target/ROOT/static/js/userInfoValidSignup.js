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
            $('#emailValid').css('color', 'red').text('이메일 형식을 확인하세요.');
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
        form.attr('action', contextPath + '/auth/signup');
        form.attr('method', 'post');
        form.submit();

    });
    // 이메일 인증 요청
    $('#sendEmailAuth').on('click', async function () {
        var email = $.trim($('#email').val());

        if (!isValidEmailFormat(email)) {
            alert('유효한 이메일 주소를 입력하세요.');
            return;
        }

/*        if (await isUniqueEmail(email) === -1) {
            alert("이미 가입된 이메일입니다.");
            return;
        }*/

        // 스피너 표시
        $("#spinner-overlay").removeClass("d-none");

        let status = await sendEmailAuth(email);
        if (status === 'success') {
            startTimer(timeLimit); // 타이머 시작
            $('#emailAuthDiv').show();
            // 요청 성공 후 스피너 숨김
            $("#spinner-overlay").addClass("d-none");
            alert('인증 이메일이 전송되었습니다.');
        } else {
            alert('이메일 전송에 실패했습니다.');
            // 요청 성공 후 스피너 숨김
            $("#spinner-overlay").addClass("d-none");
        }
    });

    // 이메일 인증번호 확인
    $('#checkEmailAuth').on('click', async function () {
        var email = $.trim($('#email').val());
        var authNum = $.trim($('#emailAuth').val());

        if (!authNum) {
            alert('인증번호를 입력하세요.');
            return;
        }

        let response = await checkEmailAuth(email, authNum);
        if (response === 'ok') {
            alert('이메일 인증이 완료되었습니다.');
            $('#emailAuthValid').css('color', 'green').text('인증 완료');

            clearInterval(timerInterval); // 타이머 정지
            $('#emailAuthDiv').hide(); // 타이머가 포함된 이메일 인증 영역 가리기
        } else {
            alert('인증번호가 올바르지 않습니다.');
            $('#emailAuthValid').css('color', 'red').text('인증 실패');
        }
    });

});

// 이메일 인증 요청 함수
async function sendEmailAuth(email) {
    try {
        const response = await $.ajax({
            type: 'POST',
            url: contextPath + '/auth/signup/mail/send',
            contentType: 'application/json',
            data: JSON.stringify({email: email})
        });
        return response.status;
    } catch (error) {
        console.error(error);
        return null;
    }
}

// 이메일 인증 확인 함수
async function checkEmailAuth(email, authNum) {
    try {
        const response = await $.ajax({
            type: 'POST',
            url: contextPath + '/auth/signup/mail/verify',
            contentType: 'application/json',
            data: JSON.stringify({email: email, authNum: authNum})
        });
        return response;
    } catch (error) {
        console.error(error);
        return null;
    }
}

let timerInterval; // 타이머를 저장할 변수
let timeLimit = 300; // 타이머 제한 시간 (5분 = 300초)

// 타이머 시작 함수
function startTimer(duration) {
    let timer = duration;
    const timerDisplay = $('#timer');

    timerInterval = setInterval(function () {
        let minutes = Math.floor(timer / 60);
        let seconds = timer % 60;

        // 타이머 출력
        timerDisplay.text(`남은 시간: ${minutes < 10 ? '0' + minutes : minutes}:${seconds < 10 ? '0' + seconds : seconds}`);

        // 타이머 종료 처리
        if (--timer < 0) {
            clearInterval(timerInterval);
            alert('인증 시간이 만료되었습니다. 이메일 인증을 다시 요청하세요.');
            $('#emailAuthDiv').hide();
        }
    }, 1000);
}

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
    return new Promise((resolve, reject) => {
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