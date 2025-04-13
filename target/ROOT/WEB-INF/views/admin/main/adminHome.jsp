<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div>
    <div id="count">
        <div id="today">
            <span>오늘</span>
            <div id="count-today"></div>
        </div>
        <div id="total">
            <span>전체</span>
            <div id="count-total"></div>
        </div>
    </div>
    <div id="date">
        <div id="date-update"></div>
        <span>기준</span>
    </div>
    <button type="button" id="backBtn" onclick="refreshCount()" class="btn">새로고침</button>

</div>

<script>
    $(function() {
        refreshCount();
    });

    function refreshCount(){
        $.ajax({
            url: contextPath + '/admin/visitors/count',
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                var today = response.today.count;
                var total = response.total.count;
                var lastest_update = response.updateDateTime;

                $('#count-today').text(today);
                $('#count-total').text(total);
                $('#date-update').text(lastest_update);
            }
        });
    }
</script>