function adminPriviliges(id) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var isChecked = $("#role").prop("checked");
    var url = isChecked ? "/admin/census/addrole/" + id : "/admin/census/removerole/" + id;

    $.ajax({
        url: url,
        type: "POST",
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