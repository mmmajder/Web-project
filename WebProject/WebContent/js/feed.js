// show friend requests
function createFriendReqCard(cardData) {
	console.log(cardData);
	
	var cardTemplate = [
		'<div class="request id="',
		cardData.id,
		'">',
		'<div class="info">',
		'<div class="profile-picture">',
		'<img src="',
		'images/userPictures/' + cardData.id + '/' + cardData.profilePicture,
		'"></div><div><h5>',
		cardData.name + ' ' + cardData.surname,
		'</h5><p class="text-muted">',
		cardData.numberOfMutualFriends + ' mutual friends',
		'</p></div></div><div class="action">',
		'<button class="btn btn-primary" id="',
		cardData.id,
		'">Accept</button>',
		'<button class="btn decline" id="',
		cardData.id,
		'">Decline</button>',
		'</div></div>'
	];
	return $(cardTemplate.join(''));
}

$(".friend-requests").on('click', 'button.btn-primary', function() {
	var senderId = $(this).attr('id');
	$.ajax({
		url: "rest/friendRequest/accept",
		type: "POST",
		contentType: "application/json",
		data: senderId,
		complete: function() {
			$(this).parent().fadeOut('slow');
		}
		
	});
});

$(".friend-requests").on('click', 'button.decline', function() {
	var senderId = $(this).attr('id');
	$.ajax({
		url: "rest/friendRequest/deny",
		type: "POST",
		contentType: "application/json",
		data: senderId,
		complete: function() {
			$(this).parent().fadeOut('slow');
		}
		
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

$(document).ready(function() {
	$.ajax({
		url: "rest/messages/notSeenMessages",
		type: "GET",
		contentType: "application/json",
		complete: function(notSeenMessage) {
			if (notSeenMessage.responseJSON) {
				$(".messages-count").css('visibility', 'visible');
			}
			else {
				$(".messages-count").css('visibility', 'hidden');
			}
		}
	});
});

function receiveMessage(msg) {
	$(".messages-count").css('visibility', 'visible');
}


$(document).ready(function() {
	$.ajax({
		url: "rest/friendRequest/getRequests",
		type: "GET",
		contentType: "application/json",
		complete: function(data) {
			$('.friend-requests').empty();
			$('.friend-requests').append('<h4>Friendship Requests</h4>');
			var friendRequests = data.responseJSON;
			friendRequests.forEach(function(item, i) {
				$('.friend-requests').append(createFriendReqCard(item));
			});
		}
	});
	
	$.ajax({
        url: "rest/feed/getUserPosts",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
        	$('#feeds').empty();
            var userPosts = data.responseJSON;
            userPosts.forEach(function(item) {
				createPost(item, function(data1) {
					$('#feeds').append(data1);
				} )
            });
        }
	});
});

$("#post").click(function() {
	event.preventDefault();
	addNewPost("false");
});
$("#postAsPhoto").click(function() {
	event.preventDefault();
	addNewPost("true");
});

function addNewPost(postAsPicture) {
	var picLoc = $('#add-post-image').attr('src');
	var des = $("#post-text").val();
	var d = JSON.stringify({
						    	pictureLocation: picLoc,
						    	picture: postAsPicture,
						    	description: des
						    });
	if((des != "" && postAsPicture=="false") || (picLoc != "" && postAsPicture=="true")) {
		$.ajax({
	        url: "rest/feed/createNewPost",
	        type: "POST",
	        contentType: "application/json",
	        data: d,
	        complete: function(data) {
	        	var newPost = data.responseJSON;
	        	console.log(newPost);
				createNewPost(newPost, function(p) {
					if(postAsPicture == "false") {
						$('#feeds').prepend(p);
					} else {
						window.location.href = "profile.html";
					}
				});
				$("#post-text").val('');
				$('#add-post-image').attr('src', '');
				event.preventDefault();
	        }
	    });
	} else {
		alert("You can't add empty post.")
	}
}

$("#remove-pic").click(function() {
	$("#add-img").val('');
	$('#add-post-image').attr('src', '');
})
window.onbeforeunload = function(event)
{
    socket.close();
};

// navbar icons
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

function logOut() {
	socket.close();
	window.location.href = "index.html";
}

// add new image
function getImage(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#add-post-image').attr('src', e.target.result);
		};
		reader.readAsDataURL(input.files[0]);
	}
}

// friends request animation
$(".decline").click(function() {
	$(this).parent().parent().fadeOut('slow');
})

var createPost = function(postData, callback) {
	$.ajax({
        url: "rest/search/userById",
        type: "POST",
        data: { id: postData.author },
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
			user = data.responseJSON;
			callback(makeCardTemplate(user, postData));
        }
    });
}

var createNewPost = function(postData, callback) {
	$.ajax({
        url: "rest/login/testlogin",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
			user = data.responseJSON;
			callback(makeCardTemplate(user, postData));
        }
    });
}

function makeCardTemplate(user, postData) {
	var postPic = '';
	if (postData.pictureLocation != "") {
		postPic = '<div class="post-photo"><img src="images/userPictures/' + postData.author + '/' + postData.pictureLocation + '"></div>';
	}
	var cardTemplate = [
        '<div class="feed" id="' + postData.id + '"><div class="head"><div class="user"><div class="profile-picture">',
        '<img src="',
        'images/userPictures/' + user.id + '/' + user.profilePicture,
        '"></div><div class="ingo">',
        '<h3>' + user.name + ' ' + user.surname + '</h3>',
        '<small>' + postData.posted + '</small>',
        '</div></div><span class="edit"><i class="uil uil-ellipsis-h"></i></span></div><br><div class="caption">',
        '<p>' + postData.description + '</p></div>',
        postPic,
        '<div class="comments text-muted" id="view-comments" onclick="viewComments(\'' + postData.id + '\')">',
        '<p>View all comments</p>',
        '</div><div class="add-comment">',
        '<input type="text" placeholder="Type comment..."><span><i id="add-comment" onclick="addComment(\'' + postData.id + '\')" class="uil uil-enter"></i></span>',
        '</div>'
    ];
    return $(cardTemplate.join(''));
}

function addComment(id) {
	var value = $('#' + id + ' input').val();
	if(value != '') {
		var c = JSON.stringify({text: value, postID: id});
		$.ajax({
			url: "rest/profile/addComment",
			type: "POST",
			data: c,
			contentType: "application/json",
			dataType: "json",
			complete: function(data) {
				comment = data.responseJSON;
				$('#' + id + ' .comments-content').append(makeComment(comment));
				$('#' + id + ' input').val('');
				event.preventDefault();
	        }
	    });
	} else {
		alert("You can't add empty comment.");
	}
}

function makeComment(comment) {
	var cardTemplate = [
		'<div class="message-left" id="' + comment.id + '">',
        '<div class="message-container">',
        '<div class="profile-picture">',
        '<img src="images/userPictures/' + comment.authorId + '/' + comment.profilePicture + '">',
        '</div><div>' + comment.name + ' ' + comment.lastname + '  ',
        '</div><div class="message-text"><span style="font-size:10px;">',
        comment.text,
        '</span></div></div><small style="font-size:8px;margin-left:1rem;color:black;">Last edited: ' + comment.lastEdited + '  </small><span class="edit"><i class="uil uil-edit"></i></span></div>'
	];
	return $(cardTemplate.join(''));
}

function viewComments(postID) {
	$.ajax({
        url: "rest/profile/loadComments",
        type: "POST",
        data: postID,
        contentType: "application/json",
        complete: function(data) {
        	var comments = data.responseJSON;
        	$('#' + postID + ' #view-comments').empty();
        	if (comments.length == 0) {
        		$('#' + postID + ' #view-comments').append("<p>No comments here.</p>");
        		event.preventDefault();
        		return;
        	}
			loadComments(comments, postID);
        	event.preventDefault();
        }
    });
}

function loadComments(comments, id) {
	$('#' + id + ' #view-comments').append('<p>Comments</p><div class="comments-content"' + ' id="' + id + '"></div>');
	for (let i = comments.length - 1; i >= 0; i -= 1) {
		$('#' + id + ' #view-comments').append(makeComment(comments[i]));
	}
}
