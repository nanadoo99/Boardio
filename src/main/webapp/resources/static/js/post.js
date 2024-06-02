$(function () {

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

    function readComments() {
        $.ajax({
            url: "<c:url value='/user/comment?pno=${postDto.pno}'/>",
            type: 'GET',
            success: function (data) {
                $('#comments').empty();
                data.forEach(function (commentDto) {
                    $('#comments').append(
                        '<div>' + commentDto.comment +
                        '   <input type="text" id="comment ' + commentDto.cno + '" value="' + commentDto.comment + '" style="display: none;"/>' +
                        '   <button onclick="updateComment(' + commentDto.cno + ')">수정</button> ' +
                        '   <button onclick="remainComment(' + commentDto.cno + ')" style="display: none;">취소</button> ' +
                        '   <button onclick="deleteComment(' + commentDto.cno + ')">삭제</button>' +
                        '</div>');
                });
            },
            error: function (error) {
                alert("댓글을 불러오지 못했습니다.");
            }
        });
    }

    $('#createCmtBtn').on('click', function () {
        $.ajax({
            url: "<c:url value='/user/comment'/>",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                comment: $('#comment-text').val(),
                pno: ${postDto.pno}
            }),
            success: function (response) {
                $('#comment-text').val('');
                readComments();
            },
            error: function (error) {
                alert("댓글 등록 실패");
            }
        });
    });

    $('#comment-section').on('click',)

});
