<%-- 삭제예정 0811
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <meta name="_csrf" content="${_csrf.token}">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.css">
    <link rel="stylesheet" type="text/css" href="/t1/static/css/comm/comm.css"/>

    <script src="/t1/static/js/jquery-3.7.1.min.js"></script>
    <script src="/t1/static/js/comm/comm.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css">
    <script src="https://cdn.jsdelivr.net/npm/moment/min/moment.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    <script>
        var contextPath = '${pageContext.request.contextPath}';
        var authUserUno = '${sessionScope.authUser.uno}';
        var authUserId = '${sessionScope.authUser.id}';
        var authUserRole = '${sessionScope.authUser.userRole}';
        var isAdminPage = true;

        document.addEventListener('DOMContentLoaded', function () {
            var error = '${error}';
            if (error && error !== "null") {
                alert(error);
            }

            var msg = '${msg}';
            if (msg && msg !== "null") {
                alert(msg);
            }
        });
    </script>

</head>

<body>

<tiles:insertAttribute name="header"/>
<tiles:insertAttribute name="main"/>
<tiles:insertAttribute name="footer"/>


</body>
</html>
--%>
