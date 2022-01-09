function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function login() {
    var $form = $("#login");
    var data = getFormData($form);

    var s = JSON.stringify(data);
    $.ajax({
        url: "rest/demo/login",
        type: "POST",
        data: s,
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
            window.open("feed.html", '_self').focus();
            //$( "#resultLogin" ).html( s );
        }
    });
}