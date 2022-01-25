$(document).ready(function() {
	$("#message-search").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$(".message h5").filter(function() {
			$(this).parent().parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
});


// chats
$(document).ready(function() {
	$.ajax({
		url: "rest/messages/chats",
		type: "GET",
		contentType: "application/json",
		complete: function(data) {
			$('.messages').empty();
			var userChats = data.responseJSON;
			userChats.forEach(function(item) {
				$('.messages').append(makeChatTemplate(item));
			});
		}
	});
})
$(document).ready(function() {
	function send() {
		var text = $('#new-message-text').val();
		if (text=="") {
			return ;
		}
		try {
			socket.send(text);
			message(text);
		} catch(exception) {
			message(exception);
		}
	}
	
	function message(msg) {
		$('.chat-messages').append(makeDmsTemplate(msg, "left", item.otherUser));
		//$('.chat-messages').append(msg+'</p>')
	}
	
	$('#send-message').click(function() {
		send();
	})
	
	$('#new-message-text').keypress(function(event){
		if (event.keyCode=='13') {
			send();
		}
	})
	var socket
	try{
		//var WebSocket = require('ws')
		socket = new WebSocket("ws://localhost:9000/WebProject/websocket/echoAnnotation");
		message('<p>connect: Socket Status: '+socket.readyState);
		socket.onopen = function() {
			message('<p>connect: Socket status: ' + socket.readyState + ' (open)');
		}
		socket.onmessage = function(msg) {
			message('<p>on: Recieved: ' + msg.data);
		}
		socket.onclose = function() {
			message('<p>onclose: Socket Status: '+socket.readyState+ ' (closed)');
			socket = null;
		}
	} catch(exception) {
		message('<p>Error: ' + exception);
	}
})




function makeChatTemplate(chat) {
	var cardTemplate = [
		'<div class="message" id="',
		chat.chat.id,
		'"><div class="profile-picture">',
		'<img src="images/userPictures/',
		chat.otherParticipant.id + "/" + chat.otherParticipant.profilePicture,
		'">',
		'<div class="active"></div>',
		'</div>',
		'<div class="message-body">',
		'<h5>',
		chat.otherParticipant.name + " " + chat.otherParticipant.surname,
		'</h5>',
		'<p class="text-muted">Volim te!</p>',
		'</div>',
		'</div>'
	];
	return $(cardTemplate.join(''));
}


//enter chat
$(".messages").on('click', 'div.message', function() {
	var id = $(this).attr('id');
	$.ajax({
		url: "rest/messages/chat",
		type: "POST",
		data: id,
		dataType: "json",
		contentType: "application/json",
		complete: function(data) {
			$('.chat-messages').empty();
			var chatDms = data.responseJSON;
			$("#profile-picture-top").attr("src","images/userPictures/" + chatDms[0].otherUser.id + "/" + chatDms[0].otherUser.profilePicture);
			$("#profile-name-top").html(chatDms[0].otherUser.name + " " + chatDms[0].otherUser.surname);
			chatDms.forEach(function(item) {
				if (item.dm.sender == item.loggedUser.id) {
					$('.chat-messages').append(makeDmsTemplate(item.dm.content, "right", item.loggedUser));
				} else {
					$('.chat-messages').append(makeDmsTemplate(item.dm.content, "left", item.otherUser));
				}
			})
			
			var socket
			try{
				//var WebSocket = require('ws')
				socket = new WebSocket("ws://localhost:9000/WebProject/websocket/echoAnnotation");
				message('<p>connect: Socket Status: '+socket.readyState);
				socket.onopen = function() {
					message('<p>connect: Socket status: ' + socket.readyState + ' (open)');
				}
				socket.onmessage = function(msg) {
					message('<p>on: Recieved: ' + msg.data);
				}
				socket.onclose = function() {
					message('<p>onclose: Socket Status: '+socket.readyState+ ' (closed)');
					socket = null;
				}
			} catch(exception) {
				message('<p>Error: ' + exception);
			}
			
		}
	});
});


function makeDmsTemplate(content, position, sender) {
	var cardTemplate;
	if (position=="right") {
		cardTemplate = [
		'<div class="message-',
		position,
		'">',
		'<div class="message-container">',
		'<div class="message-text">',
		'<span>',
		,content,
		'</span>',
		'</div>',
		'<div class="profile-picture">',
		'<img src="images/userPictures/',
		sender.id + "/" + sender.profilePicture,
		'">',
		'</div>',
		'</div>',
		'</div>']
	} else {
		cardTemplate = [
		'<div class="message-',
		position,
		'">',
		'<div class="message-container">',
		'<div class="profile-picture">',
		'<img src="images/userPictures/',
		sender.id + "/" + sender.profilePicture,
		'">',
		'</div>',
		'<div class="message-text">',
		'<span>',
		,content,
		'</span>',
		'</div>',
		'</div>',
		'</div>']
	}
	return $(cardTemplate.join(''));
}



function goToHomepage() {
	window.location.href = "feed.html";
}

function logOut() {
	window.location.href = "index.html";
}

function goToMyProfile() {
	window.location.href = "profile.html";
}