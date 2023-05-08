$(document).ready(function() {
    $('#posForm').click(function() {
        if ($(this).value.is(':checked')) {
            $(this).siblings('fieldset').show();
        } else {
            $(this).siblings('fieldset').hide();
            //$(this).siblings('fieldset').children('span').children('#position').prop("checked", false);
        }
    });
});

function uploadPosition(posForm) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/census/positions" + posForm[0].value + "/newposition",
        type: "POST",
        data: new FormData(posForm),
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

function deletePosition(id) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/census/positions/" + id + "/deleteposition",
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

function uploadDepartment(depForm) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/census/positions" + depForm[0].value + "/newpdepartment",
        type: "POST",
        data: new FormData(depForm),
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

function deleteDepartment(id) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/census/positions/" + id + "/deletedepartment",
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

function uploadCommission(comForm) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/census/positions" + comForm[0].value + "/newcommission",
        type: "POST",
        data: new FormData(comForm),
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

function deleteCommission(id) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/census/positions/" + id + "/deletecommission",
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