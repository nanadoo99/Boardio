<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    int status = response.getStatus();
    String statusMsg = status + "에러가 발생했습니다.\n";

    if(status == 403) {
        statusMsg += "권한이 없습니다.";
    }else if (status == 404) {
        statusMsg += "경로를 확인하세요.";
    } else if (status == 405) {
        statusMsg += "요청방식을 확인하세요.";
    }
%>

<h5><%=statusMsg%></h5>