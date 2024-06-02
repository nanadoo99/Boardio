<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<script src="${pageContext.request.contextPath}/static/js/post.js"></script>

<script>
    let failed = "${failed}"
    if (failed == "CREATE") alert("등록 실패");
    if (failed == "UPDATE") alert("수정 실패");
    if (failed == "DELETE") alert("삭제 실패");
</script>

<form action="" id="postForm">
    <h2>게시물 <span id="mode"></span></h2>
    <input type="text" name="pno" value="${mode == "create"? 0 : postDto.pno}" readonly/>
    <input type="text" name="title" value="${postDto.title}"/>
    <textarea name="content" cols="30" rows="20">${postDto.content}</textarea>
    <button type="button" id="newBtn" class="btn" style="display: none;">새글쓰기</button>
    <button type="button" id="postBtn" class="btn" style="display: none;">등록</button>
    <c:if test='${postDto.uno == authUser.uno && mode == "read"}'>
        <button type="button" id="updateBtn" class="btn">수정</button>
        <button type="button" id="deleteBtn" class="btn">삭제</button>
    </c:if>
    <button type="button" id="listBtn" class="btn" style="display: none;">목록</button>
    <button type="button" id="backBtn" class="btn" style="display: none;">돌아가기</button>
</form>

<div id="comment-section">
    <h2>Comments</h2>
    <div id="comments"></div>
    <span id="comment-reply"></span>
    <a id="reply-cancel" style="display: none">취소</a>
    <textarea id="comment-text" placeholder="Write your comment here"></textarea><br>
    <button type="button" id="createCmtBtn" class="btn">Submit</button>
</div>

<script>

    $(document).ready(function () {
        var mode = "${mode}"

        if (mode == "create") {
            $('#mode').text("작성");
            $('#postBtn').show();
            $('#backBtn').show();
            $('input[name="title"]').attr("readonly", false);
            $('textarea[name="content"]').attr("readonly", false);
        } else if (mode == "read") {
            $('#mode').text("읽기");
            $('#newBtn').show();
            $('#listBtn').show();
            $('input[name="title"]').attr("readonly", true);
            $('textarea[name="content"]').attr("readonly", true);
        } else if (mode == "update") {
            $('#mode').text("수정");
            $('#postBtn').show();
            $('#backBtn').show();
            $('input[name="title"]').attr("readonly", false);
            $('textarea[name="content"]').attr("readonly", false);
        }


        $('#newBtn').on("click", function () {
            if (mode == 'create') {
                if (!confirm("작성중이던 글이 지워집니다.")) return;
            }

            if (mode == 'update') {
                if (!confirm("수정중이던 내용이 지워집니다.")) return;
            }

            location.href = "<c:url value='/user/post/create'/>"
        });

        $('#postBtn').on("click", function () {
            let form = $('#postForm');

            if (mode == "create") {
                form.attr('action', "<c:url value='/user/post/create'/>");
            } else if (mode == "update") {
                form.attr('action', "<c:url value='/user/post/update${sc.queryString}'/>");
            }

            form.attr('method', 'post');
            form.submit();
        });

        $('#updateBtn').on("click", function () {
            let form = $('#postForm');
            form.attr('action', "<c:url value='/user/post/updateMode${sc.queryString}'/>");
            form.attr('method', 'post');
            form.submit();
        })

        $('#deleteBtn').on("click", function () {
            if (!confirm("삭제하시겠습니까?")) return;
            let form = $('#postForm');
            form.attr('action', "<c:url value='/user/post/delete${sc.queryString}'/>");
            form.attr('method', 'post');
            form.submit();
        });

        $('#listBtn').on("click", function () {
            location.href = "<c:url value='/user/post/list${sc.queryString}'/>";
        });

        $('#backBtn').on("click", function () {
            if (confirm("변경사항이 저장되지 않습니다. 정말 이전페이지로 돌아가시겠습니까?")) {
                history.back();
            }
        });

        readComments();

        // 댓글 불러오기
        function readComments() {
            $.ajax({
                url: "<c:url value='/user/comment?pno=${postDto.pno}'/>",
                type: 'GET',
                success: function (data) {
                    $('#comments').empty();
                    var comments = '';
                    data.forEach(function (commentDto) {
                        comments += "pcno: " + commentDto.pcno + ", cno: " + commentDto.cno;
                        if (commentDto.pcno != commentDto.cno) { // 답글 표시
                            comments += "ㄴ<br>";
                        }
                        comments += "<div id='cmtBox" + commentDto.cno + "'>";
                        comments += "   <input type='hidden' id='cno" + commentDto.cno + "' value='" + commentDto.cno + "'/>";
                        if (commentDto.del != 1) {
                            comments += "   <span>";
                            comments += "       <a data-pcno='" + commentDto.pcno + "' data-uno='" + commentDto.uno + " 'data-userid='" + commentDto.userId + "'>" + commentDto.userId + "</a>";
                            comments += "   </span>";
                            comments += "   <input type='text' id='cmtText" + commentDto.cno + "' value='" + commentDto.comment + "' readonly/>";
                            if (commentDto.uno == ${authUser.uno}) { // 댓글 작성자에게 보이는 버튼
                                comments += "   <button id='cmtUpdateBtn" + commentDto.cno + "'>수정</button>";
                                comments += "   <button id='cmtPostBtn" + commentDto.cno + "' style='display: none;'>등록</button>";
                                comments += "   <button id='cmtCancelBtn" + commentDto.cno + "' style='display: none;'>취소</button>";
                                comments += "   <button id='cmtDeleteBtn" + commentDto.cno + "'>삭제</button>";
                            }
                            comments += "   <input type='hidden' id='pcno" + commentDto.pcno + "' value='" + commentDto.pcno + "'/>";
                        } else { // 삭제되었을 경우
                            comments += " <span>삭제된 댓글입니다.</span>";
                        }
                        comments += "</div>";

                    });
                    $('#comments').append(comments);
                },
                error: function (error) {
                    alert("댓글을 불러오지 못했습니다.");
                }
            });
        }

        // 댓글 버튼 클릭
        $('#comment-section').on('click', 'button', function () {
            var cno = $(this).siblings('input[id^="cno"]').val();
            var id = $(this).attr('id');

            if (id.includes('cmtUpdateBtn')) {
                toggleUpdate(cno);
            } else if (id.includes('cmtPostBtn')) {
                postComment(cno);
            } else if (id.includes('cmtCancelBtn')) {
                toggleUpdate(cno);
            } else if (id.includes('cmtDeleteBtn')) {
                deleteComment(cno);
            }

        });

        // 댓글 등록
        $('#createCmtBtn').on('click', function () {
            var pno = ${postDto.pno};
            var comment = $('#comment-text').val();
            var pcno = $('#comment-reply').data('pcno');
            var uno = $('#comment-reply').data('uno');

            if(comment == '' || comment == null) {
                alert("댓글을 입력해주세요");
                return;
            }

            if(pcno != '' && pcno != null) {
                var jsonData = JSON.stringify({
                    pno: pno,
                    comment: comment,
                    pcno : pcno,
                    uno : uno
                });
            } else {
                var jsonData = JSON.stringify({
                    pno: pno,
                    comment: comment
                });
            }

            $.ajax({
                url: "<c:url value='/user/comment'/>",
                type: 'POST',
                contentType: 'application/json',
                data: jsonData,
                success: function (response) {
                    $('#comment-text').val('');
                    readComments();
                },
                error: function (error) {
                    alert("댓글 등록 실패");
                }
            });
        });

        // 댓글 수정모드
        function toggleUpdate(cno) {
            var cmtText = 'cmtText' + cno;
            var updateBtn = 'cmtUpdateBtn' + cno;
            var postBtn = 'cmtPostBtn' + cno;
            var cancelBtn = 'cmtCancelBtn' + cno;
            var deleteBtn = 'cmtDeleteBtn' + cno;

            if ($('#' + cmtText).attr('readonly')) { // 수정 모드 on
                $('#' + cmtText).attr('readonly', false).focus();
                toggleCmtBtn(0);
                $('#' + updateBtn).hide();
                $('#' + postBtn).show();
                $('#' + cancelBtn).show();
                $('#' + deleteBtn).hide();
            } else { // 수정 모드 off
                $('#' + cmtText).attr('readonly', true);
                toggleCmtBtn(1);
                $('#' + updateBtn).show();
                $('#' + postBtn).hide();
                $('#' + cancelBtn).hide();
                $('#' + deleteBtn).show();
            }
        }

        // 댓글 수정
        function postComment(cno) {
            var cmtText = 'cmtText' + cno;
            var comment = $('#' + cmtText).val();

            if(comment == '' || comment == null) {
                alert("댓글을 입력해주세요");
                return;
            }

            var jsonData = JSON.stringify({
                cno: cno,
                comment: comment
            });

            $.ajax({
                url: "<c:url value='/user/comment'/>",
                type: 'PATCH',
                contentType: 'application/json',
                data: jsonData,
                success: function (response) {
                    toggleUpdate(cno);
                },
                error: function (error) {
                    alert("댓글 등록 실패");
                },
                complete: function () {
                    readComments();
                }
            });
        }

        // 댓글 삭제
        function deleteComment(cno) {
            if (!confirm("댓글을 삭제하시겠습니까?")) return;
            $.ajax({
                url: '<c:url value="/user/comment/' + cno + '" />',
                type: 'DELETE',
                error: function (error) {
                    alert("댓글 삭제 실패");
                },
                complete: function () {
                    readComments();
                }
            });
        }

        // 멘션
        $('#comment-section').on('click', 'a', function () {
            var pcno = $(this).data('pcno');
            var uno = $(this).data('uno');
            var userId = $(this).data('userid');

            $('#comment-reply').data('pcno', pcno).data('uno', uno);
            $('#comment-reply').text(userId.toString());
            $('#reply-cancel').show();
        });

        // 멘션 취소
        $('#reply-cancel').on('click', function () {
            cancelReply();
        });

        // 멘션 취소
        function cancelReply() {
            $('#comment-reply').removeData('pcno');
            $('#comment-reply').removeData('uno');
            $('#comment-reply').text(' ');
            $('#reply-cancel').hide();
        }

        function toggleCmtBtn(chk) {
            if (chk == 0) {
                $("[id^='cmtBox'] button").hide();
            } else {
                $("[id^='cmtUpdateBtn']").show();
                $("[id^='cmtDeleteBtn']").show();
            }
        }
    });


</script>