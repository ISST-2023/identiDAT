function uploadFile(form) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/jd/" + form[0].value + "/files",
        type: "POST",
        data: new FormData(form),
        contentType: false,
        processData: false,

        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function(response) {
                location.reload();
            }
        },

        error: function(response) {
            alert("Error:  " + response.status + " - " + response.data);
        }
    });
}


function deleteFile(jd, id) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/jd/" + jd + "/files/" + id + "/delete",
        type: "POST",
        contentType: false,
        processData: false,

        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function(response) {
                $('#'+id).remove();
            }
        },

        error: function(response) {
            alert("Error:  " + response.status + " - " + response.data);
        }
    });
}