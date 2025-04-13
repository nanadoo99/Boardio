<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!-- JS for full calender -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.9.0/fullcalendar.min.js"></script>
<!-- bootstrap css and js -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>


<div id="calendar"></div>

<script>
    $(function () {
        display_events();
    }); //end document.ready block

    function display_events() {
        var events = new Array();
        $.ajax({
            url: contextPath + '/admin/announces/schedule/data',
            dataType: 'json',
            success: function (response) {
                var list = response.list;
                $.each(list, function (i, announceDto) {
                    events.push({
                        event_id: announceDto.ano,  // response[i] 대신 item 사용
                        // title: item.announceDto.title,
                        title: announceDto.title,
                        start: moment(announceDto.postedAt),
                        allDay: true
                    });
                });

                var calendar = $('#calendar').fullCalendar({
                    defaultView: 'month',
                    timeZone: 'local',
                    editable: false,
                    selectable: true,
                    selectHelper: true,
                    select: function (start) { // 새로 추가하기 위해 날짜를 눌렀을 때.
                        // 현재 날짜와 선택된 날짜 비교
                        var today = new Date();
                        today.setHours(0, 0, 0, 0);

                        if (start < today) {
                            alert('새로운 게시 등록은 오늘 날짜부터 추가할 수 있습니다.');
                            return;
                        }
                        window.open(contextPath + '/admin/announces/new?postedAt=' + start, '_blank');
                    },
                    events: events,
                    eventRender: function (event, element, view) { // 일정을 눌렀을 때.
                        element.bind('click', function () {
                            window.open(contextPath + '/admin/announces/' + event.event_id, '_blank');
                        });
                    }
                }); //end fullCalendar block
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });//end ajax block
    }


</script>