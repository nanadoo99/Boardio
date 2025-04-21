<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<section id="searchSection" class="card border-light text-dark bg-light mb-3">
    <div class="card-body">
        <form id="searchForm" class="row g-2" onsubmit="return false;">
            <input type="hidden" id="page" name="page" value="1"/>

            <div class="col d-flex align-items-center col-12 g-2">
                <div class="input-group w-50 me-2">
                    <span class="input-group-text">공지 공개일</span>
                    <input type="text" class="form-control" id="calendar" value="${sc.startDt} - ${sc.endDt}" readonly/>
                </div>

                <input type="hidden" id="startDt" name="startDt" value="${sc.startDt}"/>
                <input type="hidden" id="endDt" name="endDt" value="${sc.endDt}"/>

                <div class="d-flex align-items-center form-switch ">
                    <input class="form-check-input me-1" type="checkbox" role="switch" name="option3" id="entirePeriod"
                           onclick="handleEntirePeriod(this);">
                    <label class="form-check-label" for="entirePeriod">전체기간</label>
                </div>
            </div>

            <div class="col d-flex align-items-center col-12">
                <div class="form-check form-check-inline">
                    <label>
                        <input type="radio" name="option2"
                               value="myAnnounce" ${sc.option2 == 'myAnnounce' ? 'checked' : ''}>
                        내가 쓴글
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <input type="radio" name="option2"
                           value="everyAnnounce"  ${sc.option2 != 'myAnnounce' ? 'checked' : ''}>
                    전체
                    </label>
                </div>
            </div>

            <div class="col col-md-2">
                <select class="form-select" name="option1">
                    <option value="" ${sc.option1 == '' || !sc.option1 ? 'selected' : ''}>전체</option>
                    <option value="ano" ${sc.option1 == 'ano' ? 'selected' : ''}>글번호</option>
                    <option value="content" ${sc.option1 == 'content' ? 'selected' : ''}>내용</option>
                    <option value="title" ${sc.option1 == 'title' ? 'selected' : ''}>제목</option>
                    <option value="writer" id="option1-writerId" ${sc.option1 == 'writerId' ? 'selected' : ''}>작성자 아이디
                    </option>
                    <option value="writer" id="option1-writerUno" ${sc.option1 == 'writerUno' ? 'selected' : ''}>작성자
                        회원번호
                    </option>
                </select>
            </div>

            <div class="col col-md-6">
                <input class="form-control" type="text" name="keyword" placeholder="검색어를 입력하세요" value="${sc.keyword}"/>
            </div>
            <div class="col col-md-2">
                <button class="btn btn-primary" type="submit" onclick="announceSearch();">검색</button>
            </div>
        </form>
    </div>
</section>

<section align="right" class="mb-3">
    <button type="button" onclick="location.href = contextPath + '/admin/announces/new';" class="btn btn-outline-secondary btn-sm">새글쓰기</button>
</section>

<section id="listSection" class="mb-4">
    <table class="table table-hover">
        <colgroup>
            <col style="width: 4%;"> <!-- 번호 -->
            <col style="width: 6%;"> <!-- 글번호 -->
            <col style="">
            <col style="width: 8%;"> <!-- 작성자 -->
            <col style="width: 8%;"> <!-- 게시일 -->
            <col style="width: 16%;"> <!-- 작성일 -->
        </colgroup>
        <thead>
        <tr>
            <th></th>
            <th>글번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>게시일</th>
            <th>작성일</th>
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
        var startDt = $('#startDt').val();
        var endDt = $('#endDt').val();

        if (!startDt || startDt == "") {
            $('#startDt').val(setStartDt());
            startDt = setStartDt();
        }

        if (!endDt) {
            $('#endDt').val(setEndDt());
            endDt = setEndDt();
        }

        $('#calendar').daterangepicker({
            showDropdowns: true,
            startDate: startDt,
            endDate: endDt,
            locale: {
                format: 'YYYY-MM-DD'
            }
        });

        <%--announceList(${param.page});--%>
        announceUrlList();
    });

    $('#calendar').on('apply.daterangepicker', function (ev, picker) {
        var startDt = picker.startDate.format('YYYY-MM-DD');
        var endDt = picker.endDate.format('YYYY-MM-DD');

        $('#startDt').val(startDt);
        $('#endDt').val(endDt);

    });

    $('input[name="option2"]').change(function () {
        // 선택된 값 확인
        var selectedValue = $(this).val();

        if (selectedValue === 'myAnnounce') {
            // 값이 'myAnnounce'일 때 option 숨기기
            $('#option1-writerId').hide();
            $('#option1-writerUno').hide();
        } else {
            // 값이 'myAnnounce'가 아닐 때 option 보이기
            $('#option1-writerId').show();
            $('#option1-writerUno').show();
        }

        announceUrlList();
    });

    $('#calendar').on('cancel.daterangepicker', function (ev, picker) {
        var startDt = setStartDt();
        var endDt = setEndDt();

        $('#startDt').val(startDt);
        $('#endDt').val(endDt);
        $(this).val(startDt + " - " + endDt);

        announceUrlList();
    });

    function setEndDt() {
        return moment().startOf('day').format('YYYY-MM-DD');
    }

    function setStartDt() {
        return moment().subtract(1, 'month').startOf('day').format('YYYY-MM-DD');
    }

    function announceSearch() {
        if (!$('#startDt').val() || !$('#endDt').val()) {
            alert("검색기간을 설정하세요");
            return;
        }

        announceUrlList();
    }

    // 제출한 검색조건을 url에 반영하고 리스트를 불러옴.
    function announceUrlList() {
        // url에 제출한 검색조건 반영
        var formData = $('#searchForm').serialize();
        changeURL(formData);

        announceList(1);
    }

    function announceList(page) {
        applyQueryParamsToForm();
        $('#page').val(page);

        $.ajax({
            url: contextPath + '/admin/announces/data',
            type: 'GET',
            data: $('#searchForm').serialize(), // 쿼리스트링으로 변경
            dataType: 'json',
            success: function (response) {
                var list = response.list;
                var ph = response.ph;

                listCont(list, ph);
                pagination(ph);
                changeURLOnlyPage(page); // 입력값이 아닌, 넘겨진 검색값 그대로.
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function announceSel(ano) {
        var param = window.location.href.split('?')[1];
        location.href = contextPath + '/admin/announces/' + ano + '?' + param;
    }

    function handleEntirePeriod(checkbox) {
        var checkbox = $(checkbox);  // jQuery 객체로 변환
        var calendar = $('#calendar')

        if (checkbox.is(':checked')) {
            calendar.prop('disabled', true);
        } else {
            calendar.prop('disabled', false);
        }

        announceUrlList();
    }

    function listCont(list, ph) {
        var cont = '';

        if (list.length != 0) {
            var target = $('#listCont');
            target.empty();
            var num = ph.totalCnt - (ph.sc.page - 1) * ph.sc.pageSize;

            list.forEach(function (announceDto, index) {
                var fileCnt = '';
                if(announceDto.fileCnt > 0) {
                    fileCnt += ' <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-floppy2-fill" viewBox="0 0 16 16"> <path d="M12 2h-2v3h2z"/> <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v13A1.5 1.5 0 0 0 1.5 16h13a1.5 1.5 0 0 0 1.5-1.5V2.914a1.5 1.5 0 0 0-.44-1.06L14.147.439A1.5 1.5 0 0 0 13.086 0zM4 6a1 1 0 0 1-1-1V1h10v4a1 1 0 0 1-1 1zM3 9h10a1 1 0 0 1 1 1v5H2v-5a1 1 0 0 1 1-1"/> </svg>';
                }

                cont += '<tr style="cursor:pointer;" onclick="announceSel(' + announceDto.ano + ');" class="announceTr" id="announceTr' + announceDto.cno + '">';
                cont += '<td>' + (num - index) + '</td>';
                cont += '<td>' + announceDto.ano + '</td>';  // 글번호
                cont += '<td>' + announceState(announceDto) + announceDto.title + fileCnt +  '</td>';   // 상태(게시전)제목
                cont += '<td class="createdAt">' + announceDto.userId + '</td>';  // 작성자
                cont += '<td class="createdAt">' + announceDto.formattedPostedAt + '</td>';  // 게시일
                cont += '<td class="createdAt">' + announceDto.formattedCreatedAtTime + '</td>';  // 작성일
                cont += '</tr>';
            });
        } else {
            cont += "<tr><td colspan='5'>No data</td></tr>";
        }

        $('#listCont').html(cont);
    }

    function announceState(announceDto) {
        var postedAt = new Date(announceDto.postedAt);

        // 현재 날짜 (자정을 기준으로)
        var today = new Date();
        today.setHours(0, 0, 0, 0); // 오늘 날짜의 시간을 00:00:00으로 설정

        if(postedAt >  today) {
            return "(게시전)";
        } else {
            return "";
        }
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