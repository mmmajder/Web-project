$(document).ready(function() {
	function send() {
		var text = $('#text').val();
		if (text=="") {
			message("<p>Please enter message");
			return
		}
		try {
			socket.send(text);
			message('<p>Sent: ' + text);
		} catch (exception) {
			message("error: " + exception);
		}
	}
	function message(msg) {
		console.log(msg);
		$('#chatlog').append(msg+'</p');
	}
	
	$('#text').keypress(function(event) {
		if(event.keyCode == '13') {
			send();
		}
	})
	
	$('#send').keypress(function() {
		send();
	});
	
	
		
		
	
	
})

var socket = new WebSocket(host);
message()