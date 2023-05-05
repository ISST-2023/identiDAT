function uploadFile(form) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/jd/" + form.getAttribute('data-jd_id') + "/files",
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
                $('#file_'+id).remove();
            }
        },

        error: function(response) {
            alert("Error:  " + response.status + " - " + response.data);
        }
    });
}

function editFile(fileId, alreadyIn=false) {
    if (!alreadyIn) {
        $('#file_' + fileId).find('.fileUploaded').find('p').hide();
        $('#file_' + fileId).find('.fileUploaded').find('input').show();
        $('#file_' + fileId).find('.fileUpdated').find('p').hide();
        $('#file_' + fileId).find('.actionButtons').find('.mainMode').hide();
        $('#file_' + fileId).find('.actionButtons').find('.editMode').show();
    } else {
        $('#file_' + fileId).find('.fileUploaded').find('p').show();
        $('#file_' + fileId).find('.fileUploaded').find('input').hide();
        $('#file_' + fileId).find('.fileUpdated').find('p').show();
        $('#file_' + fileId).find('.actionButtons').find('.mainMode').show();
        $('#file_' + fileId).find('.actionButtons').find('.editMode').hide();
    }
    
}

function updateFile(form) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/jd/" + form.getAttribute('data-jd_id') + "/files/" +  form.getAttribute('data-file_id') + "/update",
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