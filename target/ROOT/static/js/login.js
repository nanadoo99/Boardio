$(document).ready(function() {

    $('#submit').on('click', function() {
        alert("dff2");
        // 기본 폼 제출을 막습니다.
        // event.preventDefault();
        var loginId = $('#loginId').val();
        var loginPassword = $('#loginPassword').val();

        if(!loginId || !loginPassword) {
            alert("아이디와 비밀번호를 입력하세요.");
            return;
        }
        // var form = stringifyForm($('#loginForm'));

        var header = $("meta[name='_csrf_header']").attr('content');
        var token = $("meta[name='_csrf']").attr('content');

        $.ajax({
            type: 'POST'
            ,url: contextPath + '/auth/login'
            ,data: JSON.stringify({id:loginId, password:loginPassword})
            , beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            } ,contentType: 'application/json; charset=UTF-8'
            ,success: function(response) {
                if (response.success) {

                    let targetUrl;
                    if (!response.orginURI || response.orginURI === '') {
                        targetUrl = contextPath + '/';
                    } else {
                        targetUrl = response.orginURI.startsWith('/')
                            ? contextPath + response.orginURI
                            : contextPath + '/' + response.orginURI;
                    }
                    location.href = targetUrl;
                    alert(response.message);
                } else {
                    // 로그인 실패 시 에러 메시지 표시
                    alert("failed");
                }
            },  error: function(jqXHR, textStatus, errorThrown) {
                // AJAX 요청 실패 처리
                console.error('AJAX Error: ' + textStatus, errorThrown);
                $('#errorMessage').text('로그인 요청에 실패했습니다. 다시 시도해주세요.');
            }
        });
    });
});