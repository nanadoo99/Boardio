<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<section id="searchSection" class="card border-light text-dark bg-light mb-3">
    <div class="card-body">
        <form id="searchForm" class="row g-2" onsubmit="return false;">
            <input type="hidden" id="page" name="page" value="1"/>

            <div class="col d-flex align-items-center col-12 g-2">
                <div class="input-group w-50 me-2">
                    <span class="input-group-text">게시글 작성일</span>
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
                        <input type="radio" name="option2" value="ALL" checked>
                        전체
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <label>
                        <input type="radio" name="option2"
                               value="REPORTED" ${sc.option2 == 'REPORTED' ? 'checked' : ''}>
                        신고 처리대기
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <label>
                        <input type="radio" name="option2"
                               value="BLOCKED"  ${sc.option2 == 'BLOCKED' ? 'checked' : ''}>
                        차단
                    </label>
                </div>
            </div>


            <div class="col col-md-2">
                <select class="form-select" name="option1">
                    <option value="" selected>전체</option>
                    <option value="pno" ${sc.option1 == 'pno' ? 'selected' : ''}>글번호</option>
                    <option value="title" ${sc.option1 == 'title' ? 'selected' : ''}>제목</option>
                    <option value="content" ${sc.option1 == 'content' ? 'selected' : ''}>내용</option>
                    <option value="writerId" ${sc.option1 == 'writerId' ? 'selected' : ''}>작성자 아이디</option>
                    <option value="writerUno" ${sc.option1 == 'writerUno' ? 'selected' : ''}>작성자 회원번호</option>
                </select>
            </div>

            <div class="col  col-md-6">
                <input class="form-control" type="text" name="keyword" placeholder="검색어를 입력하세요" value="${sc.keyword}"/>
            </div>

            <div class="col col-md-2">
                <button class="btn btn-primary" type="submit" onclick="postSearch();">검색</button>
            </div>
        </form>
    </div>
</section>

<section id="listSection" class="mb-4">
    <table class="table table-hover">
        <colgroup>
            <col style="width: 4%;"> <!-- 번호 -->
            <col style="width: 6%;"> <!-- 글번호 -->
            <col style="width: 8%;"> <!-- 작성자 -->
            <col style="width: 8%;"> <!-- 회원번호 -->
            <col style=""> <!-- 제목 -->
            <col style="width: 4%;"> <!-- 댓글수 -->
            <col style="width: 6%;"> <!-- 조회수 -->
            <col style="width: 14%;"> <!-- 작성일 -->
            <col style="width: 10%;"> <!-- 상태 -->
            <col style="width: 8%;"> <!-- 최근신고일 -->
            <col style="width: 8%;"> <!-- 차단일 -->
        </colgroup>
        <thead>
        <tr>
            <th></th>
            <th>글번호</th>
            <th>작성자</th>
            <th>회원 번호</th>
            <th>제목</th>
            <th>댓글</th>
            <th>조회수</th>
            <th>작성일</th>
            <th>상태</th>
            <th>최근신고일</th>
            <th>차단일</th>
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
            $('#startDt').val(defaultStartDt);
            startDt = defaultStartDt;
        }

        if (!endDt) {
            $('#endDt').val(defaultEndDt);
            endDt = defaultEndDt;
        }

        $('#calendar').daterangepicker({
            showDropdowns: true,
            startDate: startDt,
            endDate: endDt,
            locale: {
                format: 'YYYY-MM-DD'
            }
        });

        <%--postList(${param.page});--%>
        postUrlList();
    });

    $('#calendar').on('apply.daterangepicker', function (ev, picker) {
        var startDt = picker.startDate.format('YYYY-MM-DD');
        var endDt = picker.endDate.format('YYYY-MM-DD');

        $('#startDt').val(startDt);
        $('#endDt').val(endDt);

        postUrlList();
    });

    $('input[name="option2"]').change(function () {
        postUrlList();
    });

    $('#calendar').on('cancel.daterangepicker', function (ev, picker) {
        var startDt = setStartDt();
        var endDt = setEndDt();

        $('#startDt').val(startDt);
        $('#endDt').val(endDt);
        $(this).val(startDt + " - " + endDt);
    });

    function postSearch() {
        if (!$('#startDt').val() || !$('#endDt').val()) {
            alert("검색기간을 설정하세요");
            return;
        }

        postUrlList();
    }

    // 제출한 검색조건을 url에 반영하고 리스트를 불러옴.
    function postUrlList() {
        // url에 제출한 검색조건 반영
        var formData = $('#searchForm').serialize();
        changeURL(formData);

        postList(1);
    }

    function postList(page) {
        applyQueryParamsToForm();

        $('#page').val(page);

        $.ajax({
            url: contextPath + '/admin/posts/list',
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

    function postSel(pno) {
        var param = window.location.href.split('?')[1];
        location.href = contextPath + '/admin/posts/' + pno + '?' + param;
    }

    // 전체기간 선택
    function handleEntirePeriod(checkbox) {
        var checkbox = $(checkbox);  // jQuery 객체로 변환
        var calendar = $('#calendar')

        if (checkbox.is(':checked')) {
            calendar.prop('disabled', true);
        } else {
            calendar.prop('disabled', false);
        }

        postUrlList();
    }

    function listCont(list, ph) {
        var cont = '';

        if (list.length != 0) {
            var target = $('#listCont');
            var num = ph.totalCnt - (ph.sc.page - 1) * ph.sc.pageSize;
            target.empty();

            list.forEach(function (postDto, index) {
                cont += '<tr style="cursor: pointer;" onclick="postSel(' + postDto.pno + ');" class="postTr" id="postTr' + postDto.pno + '">';
                cont += '<td>' + (num - index) + '</td>'; // 번호
                cont += '<td>' + postDto.pno + '</td>'; // 글번호
                cont += '<td>' + postDto.userId + '</td>'; // 작성자
                cont += '<td>' + postDto.uno + '</td>'; // 회원 번호
                cont += '<td class="text-ellipsis" >' + postDto.title + '</td>'; // 제목
                cont += '<td>' + postDto.cmtCnt + '</td>'; // 댓글
                cont += '<td>' + postDto.views + '</td>'; // 조회수
                cont += '<td class="createdAt">' + postDto.formattedCreatedAtTime + '</td>'; // 작성일
                cont += '<td>' + postContentState(postDto) + '</td>' // 상태
                cont += '<td>' + (postDto.formattedReportedAt || '') + '</td>'; //최근신고일
                cont += '<td>' + (postDto.formattedBlockedAt || '') + '</td>'; //차단일
                cont += '</tr>';
            });
        } else {
            cont += "<tr><td colspan='11'>No data</td></tr>";
        }

        $('#listCont').html(cont);
    }

    function postContentState(postDto) {
        switch (postDto.contentState) {
            case 'REPORTED':
                return postDto.contentStateKorNm + " (" + postDto.rpnoCnt + "건)";
            case 'BLOCKED':
                return postDto.contentStateKorNm;
            case 'UNREPORTED_UNBLOCKED':
                return "";
            default:
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
                cont += '<a class="page-link" onclick="postList(' + (beginPage - 1) + ')">';
                cont += '   <span aria-hidden="true">&laquo;</span>';
                cont += '</a>'
                cont += '</li>'
            }
            for (var i = beginPage; i <= endPage; i++) {
                if ($('#page').val() == i) { // 태그있는지 확인, val()로 수정할것
                    cont += '<li class="page-item active" style="cursor: pointer"><a class="page-link" onclick="postList(' + i + ')">' + i + '</a></li>';
                } else {
                    cont += '<li class="page-item" style="cursor: pointer"><a class="page-link" onclick="postList(' + i + ')">' + i + '</a></li>';
                }
            }
            if (ph.showNext) {
                cont += '<li class="page-item" style="cursor: pointer">';
                cont += '<a class="page-link" onclick="postList(' + (endPage + 1) + ')">';
                cont += '   <span aria-hidden="true">&raquo;</span>';
                cont += '</a>'
                cont += '</li>'
            }
        }
        $('#pagination').html(cont);
    }

</script>