function getLogged(callback) {
	$.ajax({
		url: "rest/profile/getUser",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var loggedUser = data.responseJSON;
			callback(loggedUser);
		}
	});

}

function printDate(data) {
    var year = data.split('-')[0];
    var month = data.split('-')[1];
    var day = data.split('-')[2];
    return day + "/" + month + "/" + year;
}

function printDateTime(data) {
    var date = data.split('T')[0];
    var time = data.split('T')[1];
    var hours = time.split(':')[0];
    var minutes = time.split(':')[1];
    return printDate(date) + ' ' + hours + ':' + minutes;
}