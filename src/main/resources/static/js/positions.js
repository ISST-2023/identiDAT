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