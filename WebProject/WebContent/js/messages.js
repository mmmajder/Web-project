$(document).ready(function() {
	$("#message-search").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$(".message h5").filter(function() {
			$(this).parent().parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
});
var socket
$(document).ready(function() {
	try{
		socket = new WebSocket("ws://localhost:8088/WebProject/websocket/echoAnnotation");
		socket.onopen = function() {
			console.log("otvoren soket")
		}
		socket.onmessage = function(msg) {
			console.log("stigao")
			receiveMessage(msg.data, "left");
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

function receiveMessage(msg, position) {
	var sender = msg.split("recieverId")[0].split("senderId")[1];
	var receiver = msg.split("recieverId")[1];
	var realMessage = msg.split("senderId")[0];
	if (selectedChat) {
		if (selectedChat.loggedUser.id==receiver && selectedChat.otherUser.id==sender) {
			seenMessage();
			$('.chat-messages').append(makeDmsTemplate(realMessage, position, selectedChat.otherUser));
			
		}
		else {
			initChats();
		}
	} else {
		initChats();
	}
	
}

function seenMessage(){
	if(selectedChat) {
		var s = JSON.stringify(selectedChat);
		$.ajax({
			url: "rest/messages/seen",
			type: "POST",
			data: s,
			contentType: "application/json",
			dataType: "json",
			complete: function() {
				initChats();
			}
		})
	}
}


// chats
function initChats() {
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
}

function openChatIfNeeded() {
	$.ajax({
		url: "rest/messages/openChatIfNeeded",
		type: "GET",
		contentType: "application/json",
		complete: function(data) {
			chat(data);
		}
	});
}


$(document).ready(function() {
	initChats();
	openChatIfNeeded();
	
	$.ajax({
        url: "rest/login/testlogin",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
            var user = data.responseJSON;
            $("#logged-user-username").html("@" + user.username);
        }
	});
})

function makeChatTemplate(chatHeadData) {
	var color = 'white';
	if (!chatHeadData.chat.seen && chatHeadData.lastSender==chatHeadData.otherParticipant.id) {
		color = 'grey';
	}
	var cardTemplate = [
		'<div class="message" id="',
		chatHeadData.chat.id,
		'" style="background-color:',
		color,
		';"',
		'><div class="profile-picture">',
		'<img src="images/userPictures/',
		chatHeadData.otherParticipant.id + "/" + chatHeadData.otherParticipant.profilePicture,
		'">',
		'<div class="active"></div>',
		'</div>',
		'<div class="message-body">',
		'<h5>',
		chatHeadData.otherParticipant.name + " " + chatHeadData.otherParticipant.surname,
		'</h5>',
		'<p class="text-muted">',
		chatHeadData.content,
		'</p>',
		'<p>',
		printDateTime(chatHeadData.lastMessage),
		"</p>",
		'</div>',
		'</div>'
	];
	return $(cardTemplate.join(''));
}

var selectedChat;

//enter chat
$(".messages").on('click', 'div.message', function() {
	var chatId = $(this).attr('id');
	$(this).css('background-color', 'white');
	$.ajax({
		url: "rest/messages/chat",
		type: "POST",
		data: chatId,
		dataType: "json",
		contentType: "application/json",
		complete: function(data) {
			chat(data);
		}
	});
});

function chat(data) {
	$('.chat-messages').empty();
	var chatDms = data.responseJSON;
	selectedChat = chatDms;
	seenMessage();		
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
			var editedText = text + "senderId" + selectedChat.loggedUser.id + "recieverId" + selectedChat.otherUser.id;
			save(selectedChat, text);
			socket.send(editedText);			
			sendMessage(text);
			//initChats();
		} catch(exception) {
			console.log(exception);
		}
	}

	function sendMessage(msg) {
		$('.chat-messages').append(makeDmsTemplate(msg, "right", chatDms.loggedUser));
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
}


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
function save(chatDms, content) {
	data = {
		chat: chatDms.chat,
		sender: chatDms.loggedUser,
		reciever: chatDms.otherUser, 
		content: content
	};
	var s = JSON.stringify(data);
	$.ajax({
		url: "rest/messages/saveMessage",
		type: "POST",
		data: s,
		dataType: "json",
		contentType: "application/json",
		complete: function(retData) {
			if (retData) {
				initChats();
			}
		}
});
}

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
	window.location.href = "index.html";
}

function goToMyProfile() {
	socket.close();
	window.location.href = "profile.html";
}