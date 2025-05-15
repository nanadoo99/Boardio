<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!-- JS for full calender -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.9.0/fullcalendar.min.js"></script>
<!-- bootstrap css and js -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>


<div id="calendar"></div>

<!-- Start popup dialog box -->
<div class="modal fade" id="event_entry_modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-xl" role="document">
        <input type="hidden" id="bannerId" name="id"/>
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalLabel">배너 등록</h5>
                <button type="button" class="btn-close" onclick="closeModal();" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="postedAt" class="form-label">노출 시작일</label>
                            <input type="date" name="postedAt" id="postedAt" class="form-control onlydatepicker"
                                   placeholder="시작일" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="unpostedAt" class="form-label">노출 종료일</label>
                            <input type="date" name="unpostedAt" id="unpostedAt" class="form-control" placeholder="종료일"
                                   required>
                        </div>
                    </div>
                    <!-- 공지 사항 연결 -->
                    <div class="mt-3">
                        <label class="form-label">연결 공지 사항</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="announce-title" placeholder="선택된 공지 없음" value="" readonly/>
                            <button class="btn btn-outline-primary" id="announce-link" onclick="announceLink();" style="display: none;">보러가기</button>
                        </div>
                        <input type="hidden" id="ano"/>
                        <input type="hidden" id="announce-postedAt"/>
                        <a class="d-block mt-2 text-decoration-none" href="<c:url value='/admin/announces'/>" id="announce-page-link" target="_blank">전체 공지 목록 보기 &gt;</a>
                    </div>
                    <!-- 검색 조건 -->
                    <div class="mt-4" id="announce-search-bar">
                        <h6>공지 사항 검색</h6>
                        <form id="searchForm" class="row g-2" onsubmit="return false;">
                            <div class="col-auto align-self-center">
                                <div class="form-check">
                                    <input type="radio" class="form-check-input" name="option2" value="myAnnounce" id="myAnnounce">
                                    <label class="form-check-label" for="myAnnounce">내가 쓴 글</label>
                                </div>
                            </div>
                            <div class="col-auto align-self-center">
                                <div class="form-check">
                                    <input type="radio" class="form-check-input" name="option2" value="everyAnnounce" id="everyAnnounce" checked="">
                                    <label class="form-check-label" for="everyAnnounce">전체</label>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <select class="form-select" name="option1">
                                    <option value="" selected="">전체</option>
                                    <option value="ano">글번호</option>
                                    <option value="content">내용</option>
                                    <option value="title">제목</option>
                                    <option value="writer" id="option1-writerId">작성자 아이디</option>
                                    <option value="writer" id="option1-writerUno">작성자 회원번호</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <input class="form-control" type="text" name="keyword" placeholder="검색어를 입력하세요">
                            </div>
                            <div class="col-auto">
                                <button class="btn btn-primary" type="submit" onclick="getAnnounceList2();">검색</button>
                            </div>
                        </form>
                    </div>
                    <!-- 검색 결과 -->
                    <div class="mt-3" id="announce-search-result">
                        <h6>검색 결과</h6>
                        <div class="table-responsive" style="max-height: 200px;">
                            <table class="table table-hover table-sm">
                                <thead class="table-light">
                                <tr>
                                    <th style="width: 8%;">글번호</th>
                                    <th>제목</th>
                                    <th style="width: 12%;">작성자</th>
                                    <th style="width: 12%;">게시일</th>
                                    <th style="width: 12%;">작성일</th>
                                    <th style="width: 12%;"></th>
                                </tr>
                                </thead>
                                <tbody id="listCont">
                                <!-- 데이터가 동적으로 추가될 부분 -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="row mt-3">
                    <div class="col-sm-12">
                        <div class="form-group" id="image-upload">
                            <div class="form-group" id="image-org">
                                <h6>현재 배너 이미지</h6>
                                <img id="banner-upload-thumbnail"
                                     style="object-fit: contain; width: 230px; height: 230px;"
                                     src=""/>
                                <span id="banner-upload-image-name"></span>
                            </div>
                            <label for="banner-image-input" class="form-label">배너 이미지 선택</label>
                            <input type="file" name="image" id="banner-image-input" class="form-control"
                                   accept=".jpg, .jpeg, .png" required>
                        </div>
                        <div class="form-group" id="image-download">
                            <h6>현재 배너 이미지</h6>
                            <img id="banner-read-thumbnail" style="object-fit: contain; width: 230px; height: 230px;"
                                 src=""/>
                            <span id="banner-read-image-name"></span>
                            <a href="#" id="banner-image-download">다운로드</a>
                        </div>
                    </div>
                </div>
                <div class="row mt-3">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <h6 for="banner-memo">메모</h6>
                            <textarea name="memo" id="banner-memo" class="form-control"> </textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer mt-3">
                <button type="button" class="btn btn-primary" id="modal-btn1" onclick=""></button>
                <button type="button" class="btn btn-secondary" id="modal-btn2" onclick="" hidden></button>
            </div>
        </div>
    </div>
</div>
<!-- End popup dialog box -->

<script>
    $(function () {
        display_events();
    }); //end document.ready block

    function display_events() {
        var events = new Array();
        $.ajax({
            url: contextPath + '/admin/banners/list',
            dataType: 'json',
            success: function (response) {
                var list = response.list;
                $.each(list, function (i, item) {
                    var announceDto = item.announceDto;
                    events.push({
                        event_id: item.id,  // response[i] 대신 item 사용
                        // title: item.announceDto.title,
                        title: item.announceDto.title,
                        start: moment(item.postedAt),
                        end: moment(item.unpostedAt).add(1, 'days'),  // 종료 날짜를 하루 더함
                        allDay: true
                        /* color: item.color,  // 백엔드에서 추가한 color 사용
                         url: item.url  // 백엔드에서 추가한 url 사용*/
                    });
                });

                var calendar = $('#calendar').fullCalendar({
                    defaultView: 'month',
                    timeZone: 'local',
                    editable: false,
                    selectable: true,
                    selectHelper: true,
                    select: function (start, end) { // 새로 추가하기 위해 날짜를 눌렀을 때.
                        // 현재 날짜와 선택된 날짜 비교
                        var today = new Date();
                        today.setHours(0, 0, 0, 0);

                        if (start < today) {
                            alert('새로운 배너 등록은 오늘 날짜부터 추가할 수 있습니다.');
                            return;
                        }
                        $('#postedAt').val(moment(start).format('YYYY-MM-DD'));
                        $('#unpostedAt').val(moment(start).format('YYYY-MM-DD'));

                        $('#postedAt').prop('min', moment(today).format('YYYY-MM-DD'));
                        $('#unpostedAt').prop('min', moment(start).format('YYYY-MM-DD'));

                        getAnnounceList2();
                        create_mode();
                        $('#event_entry_modal').modal('show');
                    },
                    events: events,
                    eventRender: function (event, element, view) { // 일정을 눌렀을 때.
                        element.bind('click', function () {
                            // 이벤트 정보를 바탕으로 모달창에 값을 설정
                            $('#postedAt').val(moment(event.start).format('YYYY-MM-DD'));
                            $('#unpostedAt').val(moment(event.end).subtract(1, 'days').format('YYYY-MM-DD')); // 날짜에서 하루 빼기

                            // 모달창 띄우기
                            $('#bannerId').val(event.event_id);
                            read_mode(event.event_id);
                            $('#event_entry_modal').modal('show');
                        });
                    }
                }); //end fullCalendar block
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });//end ajax block
    }

    function save_event() {
        var postedAt = $("#postedAt").val();
        var unpostedAt = $("#unpostedAt").val();
        var ano = $('#ano').val();
        var announcePostedAt = $('#announce-postedAt').val();
        var bannerImage = $('#banner-image-input')[0].files[0];        // 파일 선택
        var memo = $('#banner-memo').val();
        var alertMsg = '';

        if (postedAt == "") {
            alertMsg += "시작일을 선택하세요.";
        }

        if (unpostedAt == "") {
            alertMsg += "종료일을 선택하세요.";
        }

        if (ano == "") {
            alertMsg += "연결할 공지사항을 선택하세요.";
        }

        if (!$('#banner-image-input').val()) {
            alertMsg += "배너 이미지를 선택하세요.";
        }

        if (bannerImage.size > 3 * 1024 * 1024) {
            var fileSizeMB = (bannerImage.size / (1024 * 1024)).toFixed(2);
            alert("파일 크기가 3MB를 초과했습니다. 현재 크기: " + fileSizeMB + "MB");
            $('#banner-image-input').val(""); // 선택된 파일 초기화
            return false;
        }

        if (alertMsg.length != 0) {
            alert(alertMsg);
            return
        }

        // 배너 공개일이 공지 노출일보다 작을 경우, return
        if (postedAt < moment(announcePostedAt).format('YYYY-MM-DD')) {
            alert("게시되지 않은 공지사항은 등록할 수 없습니다.");
            return false;
        }

        var formData = new FormData();
        formData.append("postedAt", postedAt);
        formData.append("unpostedAt", unpostedAt);
        formData.append("ano", ano);
        formData.append("image", bannerImage); // 파일 추가
        formData.append("memo", memo);

        $.ajax({
            url: contextPath + "/admin/banners",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false, // 데이터를 쿼리문자열로 변환하지 않도록 설정.
            dataType: 'json',
            success: function (response) {
                closeModal();
                location.reload();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
        return false;
    }

    function read_mode(id) {
        var today = new Date();
        today.setHours(0, 0, 0, 0);
        var endDt = new Date($('#unpostedAt').val());
        endDt.setHours(0, 0, 0, 0);

        // 정보를 받아와서
        getSelectedEvent(id);

        // 버튼명과 동작을 변경하고
        $('#modalLabel').text("배너");
        if (endDt >= today) { // 노출 종료일 >= 오늘 >> 수정가능
            updateBtnOn();
            deleteBtnOn();
        } else {
            updateCreateBtnOff();
            deleteCancelBtnOff();
        }
        $('#image-upload').hide();
        $('#image-download').show();
        readonlyTrue();
        hideSearchBar();

        // 보여준다.
        $('#event_entry_modal').modal('show');
    }

    function updateCreateBtnOff() { // 수정 및 등록 버튼 비활성화
        $('#modal-btn1').text("");
        $('#modal-btn1').attr('onclick', '');
        $('#modal-btn1').attr('hidden', true);
    }

    function updateBtnOn() { // 수정버튼 활성화
        $('#modal-btn1').text("배너 수정");
        $('#modal-btn1').attr('onclick', 'update_mode()');
        $('#modal-btn1').attr('hidden', false);
    }

    function createBtnOn() { // 등록버튼 활성화
        $('#modal-btn1').text("등록");
        $('#modal-btn1').attr('onclick', 'save_event()');
        $('#modal-btn1').attr('hidden', false);
    }

    function deleteCancelBtnOff() { // 삭제 및 취소버튼 비활성화
        $('#modal-btn2').text("");
        $('#modal-btn2').attr('onclick', '');
        $('#modal-btn2').attr('hidden', true);
    }

    function deleteBtnOn() { // 삭제버튼 활성화
        $('#modal-btn2').text("배너 삭제");
        $('#modal-btn2').attr('onclick', 'deleteEvent()');
        $('#modal-btn2').attr('hidden', false);
    }

    function cancelBtnOn() { // 취소버튼 활성화
        $('#modal-btn2').text("취소");
        $('#modal-btn2').attr('onclick', 'closeModal()');
        $('#modal-btn2').attr('hidden', false);
    }

    function create_mode() {
        readonlyFalse();

        $('#bannerId').val("");
        $('#banner-image-input').val("");
        $('#banner-memo').val("");

        $('#image-upload').show();
        $('#image-org').hide();
        $('#image-download').hide();

        // 버튼명과 동작을 변경하고
        $('#modalLabel').text("배너 등록");

        showSearchBar();
        createBtnOn();
        cancelBtnOn();

    }

    function update_mode() {
        readonlyFalse();
        // 정보를 받아와서
        getSelectedEvent($("#bannerId").val());
        $('#image-upload').show();
        $('#image-download').hide();

        // 버튼명과 동작을 변경하고
        $('#modalLabel').text("배너 수정");

        $('#modal-btn1').text("수정 완료");
        $('#modal-btn1').attr('onclick', 'updateEvent()');
        $('#modal-btn1').attr('hidden', false);

        showSearchBar();
        cancelBtnOn();

    }

    // 시작일이 변하면
    $('#postedAt').on('input', function () {
        // 시작일이 종료일 이후로 설정되면, 종료일에도 동일한 날짜를 넣는다.
        var postedAt = $(this).val();
        var unpostedAt = $('#unpostedAt').val();
        if (postedAt > unpostedAt) {
            $('#unpostedAt').val(postedAt);
        }
        $('#unpostedAt').prop('min', postedAt);

        // 공지를 새로 불러온다.
        getAnnounceList2();
    }, {passive: true});

    // 종료일이 변하면
    $('#unpostedAt').on('input', function () {
        // 종료일이 시작일 이전으로 설정되면, 시작일에도 동일한 날짜를 넣는다.
        var unpostedAt = $(this).val();
        var postedAt = $('#postedAt').val();
        if (postedAt > unpostedAt) {
            $('#postedAt').val(unpostedAt);
            $('#postedAt').prop('max', unpostedAt);
        }

        // 공지를 새로 불러온다.
        getAnnounceList2();
    }, {passive: true});

    // 날짜 선택 기준, 공개된 공지사항을 가져오고 공지페이지 이동 버튼의 쿼리값을 수정한다.
    function getAnnounceList() {
        var postedAt = $('#postedAt').val();
        $.ajax({
            url: contextPath + "/admin/banners/announceList",
            type: "GET",
            dataType: 'json',
            data: {startDt: postedAt},
            success: function (response) {
                completeAnnounceList(response.list);
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function getAnnounceList2() {
        var postedAt = $('#postedAt').val();

        $.ajax({
            url: contextPath + "/admin/banners/announceList2",
            type: "GET",
            dataType: 'json',
            data: $('#searchForm').serialize() + "&startDt=1900-01-01&endDt=" + postedAt,
            success: function (response) {
                completeAnnounceList2(response.list);
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    // getAnnounceList() 에서 가져온 공지사항 리스트로 태그를 완성한다.
    function completeAnnounceList2(list) {
        var cont = '';

        if (list.length != 0) {
            var target = $('#listCont');
            target.empty();

            list.forEach(function (announceDto, index) {
                var fileCnt = '';
                if (announceDto.fileCnt > 0) {
                    fileCnt += ' <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-floppy2-fill" viewBox="0 0 16 16"> <path d="M12 2h-2v3h2z"/> <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v13A1.5 1.5 0 0 0 1.5 16h13a1.5 1.5 0 0 0 1.5-1.5V2.914a1.5 1.5 0 0 0-.44-1.06L14.147.439A1.5 1.5 0 0 0 13.086 0zM4 6a1 1 0 0 1-1-1V1h10v4a1 1 0 0 1-1 1zM3 9h10a1 1 0 0 1 1 1v5H2v-5a1 1 0 0 1 1-1"/> </svg>';
                }

                cont += '<tr style="cursor: pointer;" onclick="announceSel(' + announceDto.ano + ', \'' + announceDto.title + '\', \'' + announceDto.formattedPostedAt + '\');">';
                cont += '<td>' + announceDto.ano + '</td>';  // 글번호
                cont += '<td>' + announceState(announceDto) + announceDto.title + fileCnt + '</td>';   // 상태(게시전)제목
                cont += '<td class="createdAt">' + announceDto.userId + '</td>';  // 작성자
                cont += '<td class="createdAt">' + announceDto.formattedPostedAt + '</td>';  // 게시일
                cont += '<td class="createdAt">' + announceDto.formattedCreatedAt + '</td>';  // 작성일
                cont += '<td><a href="' + contextPath + '/admin/announces/' + announceDto.ano + '" target="_blank">보러가기</a></td>';  // 작성일
                cont += '</tr>';
            });
        } else {
            cont += "<tr><td colspan='6'>No data</td></tr>";
        }

        $('#listCont').html(cont);
    }

    // 연결된 공지 보러가기
    function announceLink() {
        var ano = $('#ano').val();
        if (ano) {
            window.open(contextPath + '/admin/announces/' + ano, '_blank');
        }
    }

    function announceSel(ano, title, postedAt) {
        $('#announce-link').show();
        $('#announce-title').val(title);
        $('#ano').val(ano);
        $('#announce-postedAt').val(postedAt);
    }

    function announceState(announceDto) {
        var postedAt = new Date(announceDto.postedAt);

        // 현재 날짜 (자정을 기준으로)
        var today = new Date();
        today.setHours(0, 0, 0, 0); // 오늘 날짜의 시간을 00:00:00으로 설정

        if (postedAt > today) {
            return "(게시전)";
        } else {
            return "";
        }
    }

    function updateEvent() {
        var bannerId = $("#bannerId").val();
        var postedAt = $("#postedAt").val();
        var unpostedAt = $("#unpostedAt").val();
        var ano = $('#ano').val();
        var announcePostedAt = $('#announce-postedAt').val();
        var bannerImage = $('#banner-image-input')[0].files[0];
        var memo = $('#banner-memo').val();
        var alertMsg = '';

        if (postedAt == "") {
            alertMsg += "시작일을 선택하세요.";
        }

        if (unpostedAt == "") {
            alertMsg += "종료일을 선택하세요.";
        }

        if (ano == "") {
            alertMsg += "연결할 공지사항을 선택하세요.";
        }

        if (alertMsg.length != 0) {
            alert(alertMsg);
            return
        }

        // 배너 공개일이 공지 노출일보다 작을 경우, return
        if (postedAt < moment(announcePostedAt).format('YYYY-MM-DD')) {
            alert("게시되지 않은 공지사항은 등록할 수 없습니다.");
            return false;
        }

        var formData = new FormData();
        formData.append("id", bannerId);
        formData.append("postedAt", postedAt);
        formData.append("unpostedAt", unpostedAt);
        formData.append("ano", ano);
        if (bannerImage) {
            formData.append("image", bannerImage); // 파일 추가
        }

        if (bannerImage) {
            if (bannerImage.size > 3 * 1024 * 1024) {
                var fileSizeMB = (bannerImage.size / (1024 * 1024)).toFixed(2);
                alert("파일 크기가 3MB를 초과했습니다. 현재 크기: " + fileSizeMB + "MB");
                $('#banner-image-input').val(""); // 파일 선택 초기화
                return false;
            }
        }

        formData.append("memo", memo);

        console.log(ano);

        $.ajax({
            url: contextPath + "/admin/banners/" + bannerId,
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function (response) {
                closeModal();
                location.reload();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }


    function deleteEvent() {
        var bannerId = $("#bannerId").val();

        if (bannerId == "") {
            alert("배너가 선택되지 않았습니다.");
            return false;
        }

        if (!confirm("배너를 삭제하시겠습니까?")) return;

        $.ajax({
            url: contextPath + "/admin/banners/" + bannerId,
            type: "DELETE",
            dataType: 'json',
            success: function (response) {
                closeModal();
                location.reload();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function getSelectedEvent(id) {
        $.ajax({
            type: "GET",
            // asyc: false,
            url: contextPath + "/admin/banners/" + id,
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                var bannerDto = response.bannerDto;

                $('#postedAt').val(moment(bannerDto.postedAt).format('YYYY-MM-DD'));
                $('#unpostedAt').val(moment(bannerDto.unpostedAt).format('YYYY-MM-DD'));
                //  '연결된 공지' 칸 채우기
                $('#announce-title').val(bannerDto.announceTitle);
                $('#ano').val(bannerDto.ano);
                $('#announce-postedAt').val(bannerDto.formattedAnnouncePostedAtTime);
                $('#announce-link').show();

                $('#banner-upload-thumbnail').attr('src', bannerDto.fileDto.uploadPath);
                $('#banner-upload-image-name').text(bannerDto.fileDto.fileOrgNm);
                $('#image-org').show();

                // $('#banner-read-thumbnail').attr('src', "/t1/upload/banner/" + bannerDto.fileDto.fileUidNm);
                $('#banner-read-thumbnail').attr('src', bannerDto.fileDto.uploadPath);
                $('#banner-read-image-name').text(bannerDto.fileDto.fileOrgNm);

                $('#banner-image-download').prop('href', '<c:url value="/admin/banners/"/>' + id + '/image');
                $('#banner-memo').val(bannerDto.memo);

                $('select[name="option1"]').val('ano');
                $('input[name="keyword"]').val(bannerDto.ano);
                getAnnounceList2();
            },
            error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function closeModal() {
        readonlyFalse();
        clearAnnounce();
        $('#bannerId').val("");
        $('#event_entry_modal').modal('hide');

        $('#banner-upload-thumbnail').attr('src', "");
        $('#banner-upload-image-name').text("");
        $('#image-org').hide();

        // 검색창 초기화
        $('#everyAnnounce').prop('checked', true);
        $('select[name="option1"]').val("");
        $('input[name="keyword"]').val("");

        /*        $('#modalLabel').text("배너 등록");
                $('#modal-btn1').text("등록");
                $('#modal-btn1').attr('onclick', 'save_event()');
                $('#modal-btn2').attr('hidden', true);*/

        $('#modalLabel').text("")
        $('#modal-btn1').text("");
        $('#modal-btn1').attr('onclick', '');
        $('#modal-btn1').attr('hidden', true);
        $('#modal-btn2').text("");
        $('#modal-btn2').attr('onclick', '');
        $('#modal-btn2').attr('hidden', true);
    }

    function readonlyTrue() {
        $('#postedAt').prop('readonly', true);
        $('#unpostedAt').prop('readonly', true);
        $('#banner-memo').prop('disabled', true);
        $('#announce-search').hide();
    }

    function readonlyFalse() {
        $('#postedAt').prop('readonly', false);
        $('#unpostedAt').prop('readonly', false);
        $('#banner-memo').prop('disabled', false);
        $('#announce-search').show();
    }

    // '연결된 공지' 칸 비우기
    function clearAnnounce() {
        $('#announce-link').hide();
        $('#ano').val("");
        $('#announce-title').val("");
        $('#announce-postedAt').val("");
    }

    // 검색창 보이기
    function showSearchBar() {
        $('#announce-search-bar').show();
        $('#announce-search-result').show();
    }

    // 검색창 숨기기
    function hideSearchBar() {
        $('#announce-search-bar').hide();
        $('#announce-search-result').hide();
    }

</script>