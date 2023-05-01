$('.selector > input').click(function() {
    if ($(this).is(':checked')) {
        $(this).siblings('fieldset').show();
    } else {
        $(this).siblings('fieldset').hide();
        $(this).siblings('fieldset').children('span').children('#position').prop("checked", false);
    }
});

function sendTokenRequest() {
    tokenRequest = {};

    $('#tokenRequest').children('span').each((i, e) => {
        degreeCheck = $(e).siblings('.degree')
        console.log("Degree checkbox: " + degreeCheck);

        if (degreeCheck.is(':checked')) {
            console.log("Is active");
            positions = degreeCheck.siblings('fieldset');

            positions.each((i, e) => {
                postitionCheck = $(e).find('.position');
                console.log("Position checkbox: " + positionCheck);

                if (postitionCheck.is(':checked')) {
                    console.log("Also active");
                    requestGroups = null;
                    postitionInput = postitionCheck.siblings('.groups');

                    if (postitionInput !== null) {
                        userInput = postitionInput.val()
                        console.log("User input is: " + userInput);
                        groupRegEx = /([1-4][1-6],?)+/;

                        console.log(userInput.match(groupRegEx));
                    }
                }
            })
            tokenRequest[degreeCheck.val()] = null;
        }
    });

}

const submitToken = (event) => {
    event.preventDefault();
    const data = new FormData(document.getElementById("tokenRequest"));
    const values = Object.fromEntries(data.entries());
    console.log( {values} );

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url  : "/admin/tokens/saveToken",
        type : "POST",
        data : JSON.stringify(values),
        dataType : "text",
        contentType: "application/json",

        beforeSend: function( xhr ) {
            xhr.setRequestHeader(header, token);
        },

        success: function (response) {
            alert("Éxito:  "+ JSON.stringify(response));
        },
        error: function(response) {
             alert("Error:  "+ JSON.stringify(response));
        }
    });
    
}

