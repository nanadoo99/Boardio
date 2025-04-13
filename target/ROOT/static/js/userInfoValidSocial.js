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

$('#signupBtn').on('click', async function (event) {
    var id = $.trim($('#id').val());

    if (!id) {
        alert("아이디를 입력하세요.");
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

    event.preventDefault();
    let form = $('#userInfoForm');

    form.attr('action', contextPath + '/user/profile/' + id);
    form.attr('method', 'post');
    form.submit();

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
