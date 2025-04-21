<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<section id="searchSection" class="card border-light text-dark bg-light mb-3">
    <div class="card-body">
        <form id="searchForm" class="row g-2" onsubmit="return false;">
            <input type="hidden" id="page" name="page" value="1"/>

            <div class="col d-flex align-items-center">
                <div class="form-check form-check-inline">
                    <label>
                        <input type="radio" name="option2" value="" checked>
                        전체
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <label>
                        <input type="radio" name="option2" value="USER"  ${sc.option2 == 'USER' ? 'checked' : ''}>
                        사용자
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <label>
                        <input type="radio" name="option2" value="ADMIN" ${sc.option2 == 'ADMIN' ? 'checked' : ''}>
                        관리자
                    </label>
                </div>
            </div>
            <div class="col col-md-2">
                <select class="form-select" name="option1">
                    <option value="" selected>전체</option>
                    <option value="uno" ${sc.option1 == 'uno' ? 'selected' : ''}>사용자 번호</option>
                    <option value="id" ${sc.option1 == 'id' ? 'selected' : ''}>아이디</option>
                    <option value="email" ${sc.option1 == 'email' ? 'selected' : ''}>이메일</option>
                </select>
            </div>
            <div class="col">
                <input class="form-control" type="text" name="keyword" placeholder="검색어를 입력하세요" value="${sc.keyword}"/>
            </div>
            <div class="col">
                <button class="btn btn-primary" type="submit" onclick="userUrlList();">검색</button>
            </div>
        </form>
    </div>
</section>

<section id="listSection" class="table-responsive mb-4">
    <table class="table table-hover">
        <colgroup>
            <col style="width: 4%;"> <!-- 번호 -->
            <col style="width: 8%;"> <!-- 회원번호 -->
            <col style="width: 12%;"> <!-- 권한 -->
            <col style="width: 8%;"> <!-- 아이디 -->
            <col style="">
            <col style="width: 4%;"> <!-- 소셜 -->
            <col style="width: 4%;"> <!-- 잠김 -->
            <col style="width: 8%;"> <!-- 로그인 실패횟수 -->
            <col style="width: 8%;"> <!-- 가입일 -->
            <col style="width: 6%;"> <!-- 게시글 -->
            <col style="width: 4%;"> <!-- 댓글 -->
            <col style="width: 4%;"> <!-- 방문 -->
            <col style="width: 8%;"> <!-- 최근방문일 -->
        </colgroup>
        <thead>
        <tr>
            <th></th>
            <th>회원번호</th>
            <th>권한</th>
            <th>아이디</th>
            <th>이메일</th>
            <th>소셜</th>
            <th>계정 잠김</th>
            <th>로그인 실패횟수</th>
            <th>가입일</th>
            <th>게시글</th>
            <th>댓글</th>
            <th>방문</th>
            <th>최근방문일</th>
        </tr>
        </thead>
        <tbody id="listCont"></tbody>
    </table>
</section>

<section id="paginationSection">
    <nav aria-label="Page navigation">
        <ul id="pagination" class="pagination justify-content-center"></ul>
    </nav>
</section>

<script>
    $(function () {
        userList(${param.page});
    });


    $('input[name="option2"]').change(function () {
        userUrlList();
    });

    // 제출한 검색조건을 url에 반영하고 리스트를 불러옴.
    function userUrlList() {
        // url에 제출한 검색조건 반영
        var formData = $('#searchForm').serialize();
        changeURL(formData);

        userList(1);
    }

    function userList(page) {
        applyQueryParamsToForm();

        $('#page').val(page);

        $.ajax({
            url: contextPath + '/admin/user-management/list',
            type: 'GET',
            data: $('#searchForm').serialize(),
            dataType: 'json',
            success: function (response) {
                var list = response.list;
                var ph = response.ph;
                var sc = response.sc;

                listCont(list, ph);
                pagination(ph);
                changeURLOnlyPage(page); // 입력값이 아닌, 넘겨진 검색값 그대로.
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function userSel(uno) {
        var param = window.location.href.split('?')[1];
        location.href = contextPath + '/admin/user-management/' + uno + '?' + param;
    }

    function listCont(list, ph) {
        var cont = "";

        if (list.length != 0) {
            var target = $('#listCont');
            var num = ph.totalCnt - (ph.sc.page - 1) * ph.sc.pageSize;
            target.empty();

            list.forEach(function (userDto, index) {
                cont += '<tr style="cursor: pointer;" onclick="userSel(' + userDto.uno + ');" class="userTr" id="userTr' + userDto.uno + '">';
                cont += '<td>' + (num - index) + '</td>'; // 번호
                cont += '<td>' + userDto.uno + '</td>'; // 사용자 번호
                cont += '<td>' + userDto.userRole + '</td>'; // 권한
                cont += '<td>' + userDto.id + '</td>'; // 아이디
                cont += '<td>' + userDto.email + '</td>'; // 이메일
                cont += '<td>' + (userDto.social ? 'Y' : '') + '</td>'; // 소셜 로그인
                cont += '<td>' + (userDto.nonlocked ? '' : 'Y') + '</td>' // 계정 잠김
                cont += '<td>' + userDto.failures + '</td>'; // 로그인 실패 횟수
                cont += '<td>' + userDto.formattedCreatedAt + '</td>'; // 가입일
                cont += '<td>' + userDto.countPost + '</td>'; // 게시글 수
                cont += '<td>' + userDto.countComment + '</td>'; // 댓글 수
                cont += '<td>' + userDto.countVisit + '</td>'; // 방문 횟수
                cont += '<td>' + userDto.formattedLastVisitDate + '</td>'; // 최근 방문일
                cont += '</tr>';
            });
        } else {
            cont += "<tr><td colspan='13'>No data</td></tr>";
        }

        $('#listCont').html(cont);
    }

    // 페이지네이션 업데이트
    function pagination(ph) {
        var totalCnt = ph.totalCnt;
        var beginPage = ph.beginPage;
        var endPage = ph.endPage;
        var cont = '';

        if (totalCnt != 0) {
            if (ph.showPrev) {
                cont += '<li class="page-item" style="cursor: pointer">';
                cont += '<a class="page-link" onclick="userList(' + (beginPage - 1) + ')">';
                cont += '   <span aria-hidden="true">&laquo;</span>';
                cont += '</a>'
                cont += '</li>'
            }
            for (var i = beginPage; i <= endPage; i++) {
                if ($('#page').val() == i) { // 태그있는지 확인, val()로 수정할것
                    cont += '<li class="page-item active" style="cursor: pointer"><a class="page-link" onclick="userList(' + i + ')">' + i + '</a></li>';
                } else {
                    cont += '<li class="page-item" style="cursor: pointer"><a class="page-link" onclick="userList(' + i + ')">' + i + '</a></li>';
                }
            }
            if (ph.showNext) {
                cont += '<li class="page-item" style="cursor: pointer">';
                cont += '<a class="page-link" onclick="userList(' + (endPage + 1) + ')">';
                cont += '   <span aria-hidden="true">&raquo;</span>';
                cont += '</a>'
                cont += '</li>'
            }
        }
        $('#pagination').html(cont);
    }

</script>