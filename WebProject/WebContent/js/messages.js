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
	var chatId = $(this).attr('id');
	$.ajax({
		url: "rest/messages/chat",
		type: "POST",
		data: chatId,
		dataType: "json",
		contentType: "application/json",
		complete: function(data) {
			$('.chat-messages').empty();
			var chatDms = data.responseJSON;
			$("#profile-picture-top").attr("src","images/userPictures/" + chatDms.otherUser.id + "/" + chatDms.otherUser.profilePicture);
			$("#profile-name-top").html(chatDms.otherUser.name + " " + chatDms.otherUser.surname);
			chatDms.dms.forEach(function(item) {
				if (item.sender == chatDms.loggedUser.id) {
					$('.chat-messages').append(makeDmsTemplate(item.content, "right", chatDms.loggedUser));
				} else {
					$('.chat-messages').append(makeDmsTemplate(item.content, "left", chatDms.otherUser));
				}
			})
			function send() {
				var text = $('#new-message-text').val();
				if (text=="") {
					return ;
				}
				try {
					socket.send(text);
				//	chatDms.dms.append()
				//	save(text, chatDms.chat, chatDms.loggedUser)
					message(text, "right", chatDms.loggedUser);
				} catch(exception) {
				}
			}
	
			function message(msg, position, user) {
				$('.chat-messages').append(makeDmsTemplate(msg, position, user));
			}
			
			$('#send-message').click(function() {
				send();
				$('#new-message-text').val("");
			})
			
			$('#new-message-text').keypress(function(event){
				if (event.keyCode=='13') {
					send();
					$('#new-message-text').val("");
				}
			})
			
			var socket
			try{
				socket = new WebSocket("ws://localhost:9000/WebProject/websocket/echoAnnotation");
				socket.onopen = function() {
					//message('<p>connect: Socket status: ' + socket.readyState + ' (open)');
				}
				socket.onmessage = function(msg) {
					message(msg.data, "left", chatDms.otherUser);
				}
				socket.onclose = function() {
					socket = null;
				}
			} catch(exception) {
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

//save message in file
function save(content, chat, sender) {
	data
	$.ajax({
		url: "rest/messages/saveMessage",
		type: "POST",
		data: chatId,
		dataType: "json",
		contentType: "application/json",
		complete: function(data) {
		}
});


function goToHomepage() {
	window.location.href = "feed.html";
}

function logOut() {
	window.location.href = "index.html";
}

function goToMyProfile() {
	window.location.href = "profile.html";
}