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

function goToOtherProfile(id) {
	$.ajax({
		url: "rest/profile/viewOtherProfile",
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: id,
		complete: function(data) {
			window.open("otherProfile.html", '_self').focus();
		}
	});
}

var socket
$(document).ready(function() {
	try{
		socket = new WebSocket("ws://localhost:8088/WebProject/websocket/echoAnnotation");
		socket.onopen = function() {
			console.log("otvoren soket")
		}
		socket.onmessage = function(msg) {
			if (msg.data.startsWith("deletedByAdminPost")) { 
				deletedInfoMessage(msg.data, "post")				
			} else if (msg.data.startsWith("deletedByAdminComment")) { 
				deletedInfoMessage(msg.data, "comment")				
			} else {
				receiveMessage(msg.data, "left");
			}
			
		}
		connection.onerror = function (error) { 	
			console.log('WebSocket Error ' + error); 
		}; 
		socket.onclose = function() {
			console.log("zatvoren soket")
			socket = null;
		}
	} catch(exception) {
		console.log(exception);
	}
})

function redirectIfError() {
	window.location.href = "error404.html";
}

$(document).ready(function() { 
		
});

window.onbeforeunload = function(event)
{
    socket.close();
};

function goToHomepage() {
	socket.close();
	window.location.href = "feed.html";
}

function logOut() {
	socket.close();
	$.ajax({
        url: "rest/logout/logout",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
			window.location.href = "index.html";
        }
    });
}

function goToMyProfile() {
	socket.close();
	window.location.href = "profile.html";
}

function goToSearch() {
	socket.close();
	window.location.href = "search.html";
}

function goToMessages() {
	socket.close();
	window.location.href = "messages.html";
}
