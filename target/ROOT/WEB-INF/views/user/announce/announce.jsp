<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<section id="searchSection" class="card border-light text-dark bg-light mb-3">
    <div class="card-body">
        <form id="searchForm" class="row g-2" onsubmit="return false;">
            <input type="hidden" id="page" name="page" value="1"/>
            <input type="hidden" id="startDt" name="startDt" value="${sc.startDt}"/>
            <input type="hidden" id="endDt" name="endDt" value="${sc.endDt}"/>

            <div class="col col-md-2">
                <select class="form-select" name="option1">
                    <option value="" ${sc.option1 == '' || !sc.option1 ? 'selected' : ''}>전체</option>
                    <option value="title" ${sc.option1 == 'title' ? 'selected' : ''}>제목</option>
                    <option value="content" ${sc.option1 == 'content' ? 'selected' : ''}>내용</option>
                </select>
            </div>

            <div class="col">
                <input class="form-control" type="text" name="keyword" placeholder="검색어를 입력하세요" value="${sc.keyword}" />
            </div>

            <div class="col">
                <button class="btn btn-primary" type="submit" onclick="announceUrlList();">검색</button>
            </div>
        </form>
    </div>
</section>

<section id="listSection" class="mb-4">
    <table class="table table-hover">
        <colgroup>
            <col style="width: 8%;"> <!-- 번호 -->
            <col style="">
            <col style="width: 12%;"> <!-- 글번호 -->
            <col style="width: 16%;">
        </colgroup>
        <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>글번호</th>
                <th>게시일</th>
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
        announceList(${param.page}); // 페이지 로드 시 공지 리스트 불러오기
    });

    // 제출한 검색조건을 url에 반영하고 리스트를 불러옴.
    function announceUrlList() {
        var formData = $('#searchForm').serialize();
        changeURL(formData); // URL 변경 함수 호출
        announceList(1); // 1페이지로 초기화
    }

    function announceList(page) {
        applyQueryParamsToForm(); // 폼에 쿼리 파라미터 적용
        $('#page').val(page); // 현재 페이지 설정

        $.ajax({
            url: contextPath + '/user/announces/list',
            type: 'GET',
            data: $('#searchForm').serialize(),
            dataType: 'json',
            success: function (response) {
                var list = response.list;
                var ph = response.ph;

                listCont(list, ph); // 리스트 콘텐츠 업데이트
                pagination(ph); // 페이지네이션 업데이트
                changeURLOnlyPage(page); // 페이지 URL 변경
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    // 공지 선택 시 이동
    function announceSel(ano) {
        var param = window.location.href.split('?')[1];
        location.href = contextPath + '/user/announces/' + ano + '?' + param;

    }

    // 리스트 콘텐츠를 업데이트
    function listCont(list, ph) {
        var cont = '';
        var target = $('#listCont');
        target.empty(); // 기존 내용을 비움

        if (list.length !== 0) {
            var num = ph.totalCnt - (ph.sc.page - 1) * ph.sc.pageSize;

            list.forEach(function (announceDto, index) {
                var fileCnt = '';
                if(announceDto.fileCnt > 0) {
                    fileCnt += ' <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-floppy2-fill" viewBox="0 0 16 16"> <path d="M12 2h-2v3h2z"/> <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v13A1.5 1.5 0 0 0 1.5 16h13a1.5 1.5 0 0 0 1.5-1.5V2.914a1.5 1.5 0 0 0-.44-1.06L14.147.439A1.5 1.5 0 0 0 13.086 0zM4 6a1 1 0 0 1-1-1V1h10v4a1 1 0 0 1-1 1zM3 9h10a1 1 0 0 1 1 1v5H2v-5a1 1 0 0 1 1-1"/> </svg>';
                }

                cont += '<tr style="cursor: pointer;" onclick="announceSel(' + announceDto.ano + ');" class="announceTr" id="announceTr' + announceDto.cno + '">';
                cont += '<td>' + (num - index) + '</td>';  // 번호
                cont += '<td>' + announceDto.title + fileCnt + '</td>'; // 상태(게시전) 제목
                cont += '<td>' + announceDto.ano + '</td>';  // 글번호
                cont += '<td class="createdAt">' + announceDto.formattedPostedAt + '</td>';  // 게시일
                cont += '</tr>';
            });

            target.html(cont); // 내용 업데이트
        } else {
            noData();
        }
    }

    function noData() {
        var cont = '';
        cont += "<tr><td colspan='4'>No data</td></tr>";
        $('#listCont').empty();
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
                cont += '<a class="page-link" onclick="announceList(' + (beginPage - 1) + ')">';
                cont += '   <span aria-hidden="true">&laquo;</span>';
                cont += '</a>'
                cont += '</li>'
            }
            for (var i = beginPage; i <= endPage; i++) {
                if ($('#page').val() == i) { // 태그있는지 확인, val()로 수정할것
                    cont += '<li class="page-item active" style="cursor: pointer"><a class="page-link" onclick="announceList(' + i + ')">' + i + '</a></li>';
                } else {
                    cont += '<li class="page-item" style="cursor: pointer"><a class="page-link" onclick="announceList(' + i + ')">' + i + '</a></li>';
                }
            }
            if (ph.showNext) {
                cont += '<li class="page-item" style="cursor: pointer">';
                cont += '<a class="page-link" onclick="announceList(' + (endPage + 1) + ')">';
                cont += '   <span aria-hidden="true">&raquo;</span>';
                cont += '</a>'
                cont += '</li>'
            }
        }
        $('#pagination').html(cont);
    }

</script>
