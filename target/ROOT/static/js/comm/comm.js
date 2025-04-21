
function applyQueryParamsToForm() {
    var queryParams = getQueryParameters(); // 쿼리스트링 파라미터 가져오기
    var $form = $('#searchForm'); // 폼 선택자

    // 각 파라미터를 폼에 반영
    $.each(queryParams, function (key, value) {
        var $input = $form.find('[name="' + key + '"]');

        if(key === 'startDt') {
            value = value ? value : defaultStartDt;
        } else if(key === 'endDt') {
            value = value ? value : defaultEndDt;
        }

        // radio 타입인 경우
        if ($input.is(':radio')) {
            $input.filter('[value="' + value + '"]').prop('checked', true);
        } else {
            $input.val(value); // 기본 input 타입은 value 설정
        }
    });
}

function getQueryParameters() {
    var queryParams = {};
    var queryString = window.location.search.substring(1);
    var vars = queryString.split('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        var key = decodeURIComponent(pair[0]);
        var value = decodeURIComponent(pair[1]);
        queryParams[key] = value;
    }
    return queryParams;
}

function changeURL(formData) {
    var currentUrl = window.location.href.split('?')[0]; // 현재 URL에서 쿼리스트링 제외한 부분 가져오기
    var newUrl = currentUrl + '?' + formData; // 새로운 URL 생성
    history.replaceState(null, null, newUrl); // URL 업데이트 (페이지 이동 없음)
}

function changeURLOnlyPage(pageNo) {
    // 현재 URL 가져오기
    var url = window.location.href;

    // URL에서 '?' 이후의 부분을 가져오기 (쿼리스트링만 추출)
    var queryString = url.split('?')[1];

    if (queryString) {
        var params = queryString.split('&');

        // 파라미터 배열을 순회하며 'page=' 파라미터를 찾기
        var updatedParams = params.map(function (param) {
            // '='를 기준으로 파라미터 이름과 값으로 분할
            var pair = param.split('=');
            var paramName = pair[0];
            var paramValue = pair[1];

            // 'page=' 파라미터를 찾았을 때
            if (paramName === 'page') {
                // 파라미터 값을 새로운 페이지 번호로 설정
                paramValue = pageNo;
            }

            // 다시 조합하여 파라미터 문자열 생성
            return paramName + '=' + paramValue;
        });

        // 변경된 파라미터 배열을 '&'로 조인하여 쿼리스트링 생성
        var updatedQueryString = updatedParams.join('&');

        // 새로운 URL 생성
        var newUrl = url.split('?')[0] + '?' + updatedQueryString;

        // 새로운 URL로 페이지 이동 (해당 로직에 따라 적절한 처리를 추가하세요)
        history.replaceState(null, null, newUrl);
        // window.location.href = newUrl; // 실제로 페이지 이동을 원할 경우 사용
    }
}

$.fn.deserialize = function (serializedString) {
    var $form = $(this);
    var data = serializedString.split("&");
    $.each(data, function () {
        var pair = this.split("=");
        var name = decodeURIComponent(pair[0]);
        var value = decodeURIComponent(pair[1]);
        $form.find("[name='" + name + "']").val(value);
    });
};

// ===== 팝업창 관련
$(window).click(function (event) {
    if ($(event.target).is(".popup")) {
        $(".popup").hide();
    }
});