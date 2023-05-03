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

function transformTypedChar(charStr) {
    if (charStr == "," || charStr == ".") {
        return "-";
    }
    else {
        return charStr;
    }

}

function getInputSelection(el) {
	var start = 0,
		end = 0,
		normalizedValue, range,
		textInputRange, len, endRange;
	if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
		start = el.selectionStart;
		end = el.selectionEnd;
	} else {
		range = document.selection.createRange();
		if (range && range.parentElement() == el) {
			len = el.value.length;
			normalizedValue = el.value.replace(/\r\n/g, "\n");
			// Create a working TextRange that lives only in the input
			textInputRange = el.createTextRange();
			textInputRange.moveToBookmark(range.getBookmark());
			// Check if the start and end of the selection are at the very end
			// of the input, since moveStart/moveEnd doesn't return what we want
			// in those cases
			endRange = el.createTextRange();
			endRange.collapse(false);
			if (textInputRange.compareEndPoints("StartToEnd", endRange) > -1) {
				start = end = len;
			} else {
				start = -textInputRange.moveStart("character", -len);
				start += normalizedValue.slice(0, start).split("\n").length - 1;
				if (textInputRange.compareEndPoints("EndToEnd", endRange) > -1) {
					end = len;
				} else {
					end = -textInputRange.moveEnd("character", -len);
					end += normalizedValue.slice(0, end).split("\n").length - 1;
				}
			}
		}
	}
	return {
		start: start,
		end: end
	};
}

function offsetToRangeCharacterMove(el, offset) {
	return offset - (el.value.slice(0, offset).split("\r\n").length - 1);
}

function setInputSelection(el, startOffset, endOffset) {
	el.focus();
	if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
		el.selectionStart = startOffset;
		el.selectionEnd = endOffset;
	} else {
		var range = el.createTextRange();
		var startCharMove = offsetToRangeCharacterMove(el, startOffset);
		range.collapse(true);
		if (startOffset == endOffset) {
			range.move("character", startCharMove);
		} else {
			range.moveEnd("character", offsetToRangeCharacterMove(el, endOffset));
			range.moveStart("character", startCharMove);
		}
		range.select();
	}
}

$("input").keypress(function(evt) {
	if (evt.which) {
		var charStr = String.fromCharCode(evt.which);
		var transformedChar = transformTypedChar(charStr);
		if (transformedChar != charStr) {
			var sel = getInputSelection(this),
				val = this.value;
			this.value = val.slice(0, sel.start) + transformedChar + val.slice(sel.end);
			// Move the caret
			setInputSelection(this, sel.start + 1, sel.start + 1);
			return false;
		}
	}
});