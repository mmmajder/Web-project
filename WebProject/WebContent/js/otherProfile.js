function getOtherProfile(callback) {
	$.ajax({
		url: "rest/otherProfile/otherUser",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(otherProfile) {
			callback(otherProfile.responseJSON);
		}
	});
}

function arePostsPrivate() {
	$.ajax({
		url: "rest/otherProfile/arePostsPrivate",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(isPrivate) {
			return isPrivate.responseJSON=="true";
		}
	});
}

// setting display of menu
function clear() {
	$('.showing div').removeClass('visible');
	$('.navbar label').removeClass('active');
	$('.private').hide();
}

$('#photos').click(function() {
	clear();
	$(this).addClass('active');
	showPhotos();
});

// display photos
function showPhotos() {
	if(!arePostsPrivate()) {
		$.ajax({
			url: "rest/otherProfile/photos",
			type: "GET",
			contentType: "application/json",
			dataType: "json",
			complete: function(data) {
				var userPhotos = data.responseJSON;
				if (userPhotos.length == 0)
					return;
				loadPhotos(userPhotos);
				$('.photos').addClass('visible');
				$('#photos').addClass('active');
			}
		});
	} else {
		$('.private').show();
	}
}

function loadPhotos(userPhotos) {
	var file = '"images/userPictures/' + userPhotos[0].author + '/';
	$("#c1").empty();
	$("#c2").empty();
	$("#c3").empty();
	for (let i = userPhotos.length - 1; i >= 0; i -= 3) {
		var src1 = file + userPhotos[i].pictureLocation + '"';
		var id1 = userPhotos[i].id;
		$("#c1").append('<img class="post-image" onclick="viewdetails()" id="' + id1 + '" src=' + src1 + '>');
		if (i - 1 >= 0) {
			var src2 = file + userPhotos[i - 1].pictureLocation + '"';
			var id2 = userPhotos[i - 1].id;
			$("#c2").append('<img class="post-image" onclick="viewdetails()" id="' + id2 + '" src=' + src2 + '>');
		}
		if (i - 2 >= 0) {
			var src3 = file + userPhotos[i - 2].pictureLocation + '"';
			var id3 = userPhotos[i - 2].id;
			$("#c3").append('<img class="post-image" onclick="viewdetails()" id="' + id3 + '" src=' + src3 + '>');
		}
	}
}

function redirectIfLoggedSelected() {
	getOtherProfile(function(otherProfile) {
		getLogged(function(loggedUser) {
			if (otherProfile.id == loggedUser.id) {
				window.open("profile.html", '_self').focus();
			}
		})	
	})
}

$(document).ready(function() {
	redirectIfLoggedSelected();
	
	getOtherProfile(function(otherProfile){
		setBio(otherProfile);
	});
	clear();
	showPhotos();
	
	$.ajax({
        url: "rest/login/testlogin",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
            var user = data.responseJSON;
            if(user != null) {
            	$("#logged-user-username").html("@" + user.username);
            }
        }
	});
	
	$.ajax({
        url: "rest/otherProfile/getRelationStatus",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
            var relation = data.responseJSON;
        	$('#add-remove-friend').html(relation.friendStatus);
            if(relation.friendStatus == "") { 
            	$('#add-remove-friend').hide();
            } 
        	$('#send-message').html(relation.sendMessage);
            if(relation.sendMessage == "") { 
            	$('#send-message').hide();
            } 
        }
	});
});

function addRemoveFriend() {
	var id = $("#add-remove-friend").attr("user-id");
	
	if($('#add-remove-friend').html() == "Block user") {
		$.ajax({
			url: "rest/friendRequest/blockUser",
			type: "POST",
			contentType: "application/json",
			data: id,
			complete: function() {
	        	$('#add-remove-friend').html("Unblock user");
            	$('#send-message').html("Send a message");
            }
		});
	} 
	
	else if($('#add-remove-friend').html() == "Unblock user") {
		$.ajax({
			url: "rest/friendRequest/unblockUser",
			type: "POST",
			contentType: "application/json",
			data: id,
			complete: function() {
	        	$('#add-remove-friend').html("Block user");
            	$('#send-message').html("Send a message");
            }
		});
	} 
	
	else if($('#add-remove-friend').html() == "Remove friend") {
		$.ajax({
			url: "rest/profile/removeFriend",
			type: "POST",
			data: id,
			contentType: "application/json",
			dataType: "json",
			complete: function() {
	        	$('#add-remove-friend').html("Add friend");
            	$('#send-message').hide();
            }
	    });
	} 
	
	else if($('#add-remove-friend').html() == "Add friend") {
		// TO DO za Milana <3
	} 
	
	else if($('#add-remove-friend').html() == "Accept") {
		$.ajax({
			url: "rest/friendRequest/accept",
			type: "POST",
			contentType: "application/json",
			data: id,
			complete: function() {
	        	$('#add-remove-friend').html("Remove friend");
            	$('#send-message').html("Send a message");
            }
		});
	} 
	
	else if($('#add-remove-friend').html() == "Unsend request") {
		$.ajax({
			url: "rest/friendRequest/unsendRequest",
			type: "POST",
			contentType: "application/json",
			data: $('#send-message').attr("user-id"),
			complete: function() {
	        	$('#add-remove-friend').html("Add friend");
            	$('#send-message').hide();
			}
		});
	}
}

function sendMessage() {
	if($('#send-message').html() == "Send a message") {
		openChat($('#send-message').attr("user-id"));
	} else if($('#send-message').html() == "Decline") {
		$.ajax({
			url: "rest/friendRequest/deny",
			type: "POST",
			contentType: "application/json",
			data: $('#send-message').attr("user-id"),
			complete: function() {
	        	$('#add-remove-friend').html("Add friend");
            	$('#send-message').hide();
			}
		});
	}
}

$("#friends").click(function() {
	clear();
	$(this).addClass('active');
	showFriends();
});

function showFriends() {
	if(!arePostsPrivate()) {
		$.ajax({
			url: "rest/otherProfile/friends",
			type: "GET",
			contentType: "application/json",
			complete: function(data) {
				$('.friendships').empty();
				var userFriends = data.responseJSON;
				userFriends.forEach(function(item) {
					$('.friendships').append(makeFriendTemplate(item));
					/*
					 * createFriendCard(item, function(data1) {
					 * $('.friendships').append(data1); } )
					 */
				});
				$('.friends').addClass('visible');
			}
		});
	} else {
		$('.private').show();
	}
}

function makeFriendTemplate(user) {
	var cardTemplate = [
		'<div class="friendship">',
		'<div class="profile-picture" onclick="goToOtherProfile(\'' + user.id + '\')" >',
		'<img src="images/userPictures/',
		user.id+ "/" +user.profilePicture,
		'">',
		'</div>',
		'<div class="friend-name" id="',
		user.id,
		'">',
		'<h3>',
		user.name + " " + user.surname,
		'</h3>',
		'</div>',
		'<div class="buttons">',
		'<div class="btn" id="open-chat" onclick="openChat(\'' + user.id + '\')">',
		'<i class="uil uil-message"></i>',
		'<label>Send a Message</label>',
		'</div>',
		'<div class="btn" id="remove-friend" onclick="removeFriend(\'' + user.id + '\')">',
		'<i class="uil uil-multiply"></i>',
		'<label>Remove Friend</label>',
		'</div>',
		'</div>',
		'</div>'
	];
	return $(cardTemplate.join(''));
}

function removeFriend(userId) {
	$.ajax({
		url: "rest/profile/removeFriend",
		type: "POST",
		data: userId,
		contentType: "application/json",
		dataType: "json",
		complete: function() {
			// add fade out
		}
    });
}

function openChat(userId) {
	$.ajax({
		url: "rest/messages/openedChat",
		type: "POST",
		data: userId,
		contentType: "application/json",
		dataType: "json",
		complete: function() {
			window.location.href = "messages.html";
		}
    });
	
} 

function showPosts() {
	if(!arePostsPrivate()) {
		$.ajax({
	        url: "rest/otherProfile/posts",
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
	            $('.posts').addClass('visible');
	        }
	    });
	} else {
		$('.private').show();
	}
}

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


$('#posts').click(function() {
    clear();
    $(this).addClass('active');
    showPosts();
});


function setBio(user) {
	$("#profile-user-name").html("@" + user.username);
	$("#number-of-posts").html(user.posts.length + " Posts");
	$("#number-of-photos").html(user.posts.length + " Photos"); // TODO
	$("#number-of-friends").html(user.friends.length + " Friends"); // kada je
																	// nula
																	// izbaci 1
																	// ?
	$("#date-of-birth").html(printDate(user.dateOfBirth));
	$("#profile-bio-text").html(user.biography);
	$(".profile-info .profile-photo img").attr("src", "images/userPictures/" + user.id + "/" + user.profilePicture);
	$(".profile-name-surname").html(user.name + " " + user.surname);
	$("#add-remove-friend").attr("user-id", user.id);
	$("#send-message").attr("user-id", user.id);
}

// friendship filter
$(document).ready(function() {
	$("#friends-search").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$(".friendship h3").filter(function() {
			$(this).parent().parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
});

function goToHomepage() {
	window.location.href = "feed.html";
}

function logOut() {
	$.ajax({
        url: "rest/logout/logout",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
			window.location.href = "index.html";
        }
    });
}

function goToProfile() {
	window.location.href = "profile.html";
}

$(".cancel-btn").click(function() {
	$(".edit-profile-card").fadeOut();
})

$("#cancel-photo").click(function() {
	$(".photo-details-card").fadeOut();
	$('body').removeClass('stop-scrolling');
})

$("#photos").on('click', 'img', function() {
	$(".photo-details-card").fadeIn();
	$('body').addClass('stop-scrolling');
})

function viewdetails() {
	$(".photo-details-card").fadeIn();
	$('body').addClass('stop-scrolling');
	var postID = JSON.stringify({ id: event.target.id });
	$.ajax({
        url: "rest/profile/photoDetails",
        type: "POST",
        data: postID,
        contentType: "application/json",
        complete: function(data) {
        	var x = data.responseJSON;
        	$("#post-image img").attr("src", "images/userPictures/" + x.author + "/" + x.pictureLocation);
        	$("#posted").html(printDateTime(x.posted));
        	$("#image-description").html(x.description);
        	$("#post-image img").attr("id", x.id);
        }
    });
	
	// load comments
	$.ajax({
        url: "rest/profile/loadComments",
        type: "POST",
        data: postID,
        contentType: "application/json",
        complete: function(data) {
        	var comments = data.responseJSON;
        	$("#comments-content").empty();
        	if (comments.length == 0)
				return;
        	for (let i = comments.length - 1; i >= 0; i -= 1) {
        		$("#comments-content").append(makeComment(comments[i], $("#post-image img").attr("id")));
    			getLogged(function(user) {
    				if (user.id == comments[i].authorId) {
    					$('#' + comments[i].id).append('<span class="edit" onclick="editComment(\'' + comments[i].id + '\',\'' + comments[i].text + '\',\'' + postID.id + '\')"><i class="uil uil-edit"></i></span>');
    				} 
    				if (user.id == comments[i].authorId || user.admin) {
    					$('#' + comments[i].id).append('<span class="del" onclick="deleteComment(\'' + comments[i].id + '\',\'' + postID.id + '\')"><i class="uil uil-trash-alt"></i></span>');
    				} 
    			});
        	}
        }
    });
}

$("#add-comment-image").click(function() {
	var t = $("#comment-text").val();
	var id = $("#post-image img").attr("id");
	var c = JSON.stringify({text: t, postID: id});
	$.ajax({
		url: "rest/profile/addComment",
		type: "POST",
		data: c,
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			comment = data.responseJSON;
			$("#comments-content").append(makeComment(comment, id));
			getLogged(function(user) {
				if (user.id == comment.authorId) {
					$('#' + comment.id).append('<span class="edit" onclick="editComment(\'' + comment.id + '\',\'' + comment.text + '\',\'' + id + '\')"><i class="uil uil-edit"></i></span>');
				} 
				if (user.id == comment.authorId || user.admin) {
					$('#' + comment.id).append('<span class="del" onclick="deleteComment(\'' + comment.id + '\',\'' + id + '\')"><i class="uil uil-trash-alt"></i></span>');
				} 
			});
        }
    });
});

function makeCardTemplate(user, postData) {
	var postPic = '';
	if (postData.pictureLocation != "") {
		postPic = '<div class="post-photo"><img src="images/userPictures/' + postData.author + '/' + postData.pictureLocation + '"></div>';
	}
	var delPost = '';
	if (user.admin || user.id == postData.author) {
		delPost = '<span class="delete-post" onclick="deletePost(\'' + postData.id + '\',\'' + postData.author + '\')"><i class="uil uil-trash-alt"></i></span>';
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

function deletePost(postId, author) {
	if (confirm('Are you sure you want to delete this post?')) {
		var postJSON = JSON.stringify({ postId: postId, text: "Your post has been deleted" });
		getLogged((loggedUser) => {
			if(loggedUser.admin==true) {
				$.ajax({
			        url: "rest/profile/deletePostByAdmin",
			        type: "DELETE",
			        data: postJSON,
			        contentType: "application/json",
			        complete: function(data) {
			        	$("#feeds #" + postId).fadeOut();
						socket.send("deletedByAdminPost"+ postId + "user" + author + "admin" + loggedUser.id);
			        }
			    });
			} else {
				$.ajax({
			        url: "rest/profile/deletePost",
			        type: "DELETE",
			        data: postId,
			        contentType: "application/json",
			        complete: function(data) {
			        	$("#feeds #" + postId).fadeOut();
			        }
			    });
			}
			}
		);
		
		
	}
}

