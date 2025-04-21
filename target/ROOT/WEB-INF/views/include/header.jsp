<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>


<nav class="navbar navbar-expand-lg py-2">
    <div class="container">
        <a class="navbar-brand" href="<c:url value='/'/>">Home</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <li class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 col-md-8">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/user/post?page=1'/>">게시판</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/user/announces?page=1'/>">공지사항</a>
                </li>
                <c:if test="${authUser != null}">
                    <li class="nav-item dropdown me-2">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            마이페이지
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="<c:url value='/user/mypost/list'/>">작성 글</a></li>
                            <li><a class="dropdown-item" href="<c:url value='/user/mycomments/list'/>">작성 댓글</a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="<c:url value='/user/profile'/>">회원정보</a></li>
                        </ul>
                    </li>
                </c:if>
                <c:if test="${fn:contains(authUser.userRole, 'ADMIN')}">
                    <li class="nav-item">
                        <button class="btn btn-outline-success" type="button"
                                onclick="location.href='<c:url value='/admin'/>'">관리자페이지
                        </button>
                    </li>
                </c:if>
            </ul>

            <div class="text-end">
                <c:if test="${authUser != null}">
                    <button class="btn btn-dark" type="button" onclick="show_notification_modal()">알림</button>
                </c:if>
                <c:set var="loginOut" value="${authUser == null? 'login' : 'logout'}"/>
                <c:set var="loginOutURL" value="${authUser == null? '/auth/login' : '/auth/logout'}"/>
                <button class="btn btn-outline-primary me-2" type="button"
                        onclick="location.href='<c:url value='${loginOutURL}'/>'">${loginOut}</button>
                <c:if test="${authUser == null}">
                    <button class="btn btn-primary me-2" type="button"
                            onclick="location.href='<c:url value='/auth/signup'/>'">sign up
                    </button>
                </c:if>
            </div>
    </div>
    </div>
</nav>

<c:if test="${authUser != null}">
    <section id="notification-modal" class="modal" tabindex="-1" data-bs-backdrop="true">
        <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">알림 목록</h5>
                    <button type="button" class="btn-close" onclick="close_notification_modal();" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table class="table table-hover">
                        <colgroup>
                            <col style="width: 4rem;">
                            <col style="width: 10rem;">
                            <col style="">
                            <col style="width: 20rem;">
                            <col style="width: 14rem;">
                        </colgroup>
                        <thead>
                        <tr>
                            <th></th>
                            <th>종류</th>
                            <th></th>
                            <th>내용</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody id="notification-list"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </section>
</c:if>

<script>
    const notificationModal = new bootstrap.Modal(document.getElementById('notification-modal'));

    $(document).ready(function () {
        $.ajax({
            url: contextPath + '/user/notification/count',
            type: 'GET',
            success: function (data) {
                $('#notification-count').text(data.count == '0' ? '' : data.count);
            }
        });
    });

    var socket = new SockJS('/websocket');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/user/queue/newComment', function (message) {
            alert(message.body); // 새 댓글 알림 표시
        });


        stompClient.subscribe('/user/queue/newComment/count', function (message) {
            $('#notification-count').text(message.body);
        });
    });

    function show_notification_modal() {
        $.ajax({
            url: contextPath + '/user/notification/list',
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                complete_notification_list(response.list);
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function complete_notification_list(list) {
        var target = $('#notification-list');
        var cont = "";
        if (list.length > 0) {
            var targetTextMaxLength = 16;
            var total = list.length;
            list.forEach(function (notificationDto, index) {
                var targetText = notificationDto.targetParentText;
                var targetChildText = notificationDto.targetChildText;
                var trStyle = '';
                var targetUri = contextPath + "/user/notification/" + notificationDto.id;

                if (!targetText) {
                    targetText = "";
                } else if (targetText.length > targetTextMaxLength) {
                    targetText = targetText.substring(0, targetTextMaxLength) + "...";
                }

                if (!targetChildText) {
                    targetChildText = "";
                } else if (targetChildText.length > targetTextMaxLength) {
                    targetChildText = targetChildText.substring(0, targetTextMaxLength) + "...";
                }

                if (!notificationDto.isRead) {
                    trStyle += "style='cursor: pointer; background-color: lightblue;'";
                } else {
                    trStyle += "style='cursor: pointer;'";
                }

                cont += "<tr " + trStyle + " onclick=\"location.href = \'" + targetUri + "\'\">";
                cont += "   <td>" + (total - index) + "</td>";
                cont += "   <td>" + notificationDto.typeKorName + "</td>";
                cont += "   <td>" + targetText + "</td>";
                cont += "   <td>" + targetChildText + "</td>";
                cont += "   <td>" + notificationDto.formattedCreatedAtTime + "</td>";
                cont += "</tr>";
            });

        } else {
            cont += "<tr><td colspan='3'>no data</td></tr>";
        }
        target.html(cont);
        $('#notification-count').text("");
        const notificationModal = new bootstrap.Modal(document.getElementById('notification-modal'));
        notificationModal.show();
        // $('#notification-modal').show();
    }

    function notification_link(type, idx) {
        alert(type);

    }

    function close_notification_modal() {
        // 리스트를 지운다
        $('#notification-modal').hide();
    }

</script>
