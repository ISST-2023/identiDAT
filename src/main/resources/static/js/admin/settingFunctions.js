function editSetting(form) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/settings/" + form[0].value,
        type: "POST",
        data: $(form).serialize(),
        dataType: "text",

        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function(response) {
                alert("Éxito: " + response);
            }
        },

        error: function(response) {
            alert("Error:  " + response.status);
        }
    });
}