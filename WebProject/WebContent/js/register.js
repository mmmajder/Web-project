function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function register() {
    var $form = $("#register");
    var data = getFormData($form);

    var s = JSON.stringify(data);
    $.ajax({
        url: "rest/register/",
        type: "POST",
        data: s,
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
			console.log(data.responseText);
			if (data.responseText) {
				window.open("feed.html", '_self').focus(); 
			} else {
				$("#resultRegister").empty();
				$("#resultRegister").append("User with this username already exist");
			}
            window.open("feed.html", '_self').focus();
        }
    });
}