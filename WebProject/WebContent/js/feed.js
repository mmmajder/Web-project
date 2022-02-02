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
        url: "rest/feed/getUserPosts",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
        	$('#feeds').empty();
            var userPosts = data.responseJSON;
            userPosts.forEach(function(item) {
				createPost(item, function(data1) {
					$('#feeds').append(data1);
					event.preventDefault();
				} )
            });
        }
	});
	
	$.ajax({
        url: "rest/login/testlogin",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
            var user = data.responseJSON;
            $("#logged-user-username").html("@" + user.username);
            if(user.admin) {
            	$(".create-post").hide();
            	$(".friend-requests").hide();
            }
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
	var delPost = '';
	if (user.admin || user.id == postData.author) {
		delPost = '<span class="delete-post" onclick="deletePost(\'' + postData.id + '\',\'' + user.admin + '\')"><i class="uil uil-trash-alt"></i></span>';
	}
	var cardTemplate = [
        '<div class="feed" id="' + postData.id + '"><div class="head"><div onclick="goToOtherProfile(\'' + user.id + '\')" class="user"><div class="profile-picture">',
        '<img src="',
        'images/userPictures/' + user.id + '/' + user.profilePicture,
        '"></div><div class="ingo">',
        '<h3>' + user.name + ' ' + user.surname + '</h3>',
        '<small>' + printDateTime(postData.posted) + '</small>',
        '</div></div>' + delPost + '</div><br><div class="caption">',
        '<p>' + postData.description + '</p></div>',
        postPic,
        '<div class="comments text-muted" id="view-comments" onclick="viewComments(\'' + postData.id + '\')">',
        '<p>View all comments</p>',
        '</div><br><div class="add-comment">',
        '<input type="text" placeholder="Type comment..."><span><i id="add-comment" onclick="addComment(\'' + postData.id + '\')" class="uil uil-enter"></i></span>',
        '</div>'
    ];
    return $(cardTemplate.join(''));
}

function deletePost(postID, isAdmin) {
	if (confirm('Are you sure you want to delete this post?')) {
		var post = JSON.stringify({ id: postID });
		if(isAdmin) {
			$.ajax({
		        url: "rest/profile/deletePostByAdmin",
		        type: "DELETE",
		        data: post,
		        contentType: "application/json",
		        complete: function(data) {
		        	$("#feeds #" + postID).fadeOut();
		        }
		    });
		} else {
			$.ajax({
		        url: "rest/profile/delete",
		        type: "DELETE",
		        data: post,
		        contentType: "application/json",
		        complete: function(data) {
		        	$("#feeds #" + postID).fadeOut();
		        }
		    });
		}
	}
}
