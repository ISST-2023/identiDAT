function updateAssistance(jd, id, button) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/admin/jd/" + jd + "/assistance/" + id + "/" + button.getAttribute('data-status'),
        type: "POST",
        contentType: false,
        processData: false,

        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function(response) {
                switch(button.getAttribute('data-status')) {
                    case '0':
                        $('#member_'+id).find('.assist').show()
                        $('#member_'+id).find('.assist').removeClass('btn-success');
                        $('#member_'+id).find('.assist').addClass('btn-outline-success');
                        $('#member_'+id).find('.assist').attr('data-status', '1');

                        $('#member_'+id).find('.btnCell').attr('data-assists', 'false');

                        $('#member_'+id).find('.miss').show()
                        $('#member_'+id).find('.miss').removeClass('btn-danger');
                        $('#member_'+id).find('.miss').addClass('btn-outline-danger');
                        $('#member_'+id).find('.miss').attr('data-status', '2');

                        $('#member_'+id).find('.excuse').show();
                        $('#member_'+id).find('.excuse').removeClass('btn-secondary');
                        $('#member_'+id).find('.excuse').addClass('btn-outline-secondary');
                        $('#member_'+id).find('.excuse').attr('data-status', '3');

                        $('#quorumCounter').html($('#members').find('.btn-success').length)
                        break;
                    case '1':
                        $('#member_'+id).find('.assist').removeClass('btn-outline-success');
                        $('#member_'+id).find('.assist').addClass('btn-success');

                        $('#member_'+id).find('.assist').attr('data-status', '0');

                        $('#member_'+id).find('.btnCell').attr('data-assists', 'true');

                        $('#member_'+id).find('.miss').hide();

                        $('#member_'+id).find('.excuse').hide();

                        $('#quorumCounter').html($('#members').find('.btn-success').length)
                        break;
                    case '2':
                        $('#member_'+id).find('.assist').hide()

                        $('#member_'+id).find('.miss').removeClass('btn-outline-danger');
                        $('#member_'+id).find('.miss').addClass('btn-danger');
                        $('#member_'+id).find('.miss').attr('data-status', '0');

                        $('#member_'+id).find('.btnCell').attr('data-assists', 'false');

                        $('#member_'+id).find('.excuse').hide()
                        break;
                    case '3':
                        $('#member_'+id).find('.assist').hide()

                        $('#member_'+id).find('.miss').hide()

                        $('#member_'+id).find('.excuse').removeClass('btn-outline-secondary');
                        $('#member_'+id).find('.excuse').addClass('btn-secondary');
                        $('#member_'+id).find('.excuse').attr('data-status', '0');

                        $('#member_'+id).attr('data-assists', 'false');

                        break;
                }
            }
        },

        error: function(response) {
            alert("Error:  " + response.status + " - " + response.data);
        }
    });
}

$(document).ready(() => {
    $('#quorumCounter').html($('#members').find('.btn-success').length)
})