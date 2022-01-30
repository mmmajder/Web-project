function clear() {
	$('.showing div').removeClass('visible');
	$('.navbar label').removeClass('active');
}

$('#photos').click(function() {
	clear();
	$(this).addClass('active');
	$('.photos').addClass('visible');
});

$('#friends').click(function() {
	clear();
	$(this).addClass('active');
	$('.friends').addClass('visible');
});

function setBio(user) {
	$("#profile-user-name").html("@" + user.username);
	$("#number-of-posts").html(user.posts.length + " Posts");
	$("#number-of-photos").html(user.posts.length + " Photos"); // TODO
	$("#number-of-friends").html(user.friends.length + " Friends"); // kada je
																	// nula
																	// izbaci 1
																	// ?
	$("#date-of-birth").html(user.dateOfBirth);
	$("#profile-bio-text").html(user.biography);
	$(".profile-info .profile-photo img").attr("src", "images/userPictures/" + user.id + "/" + user.profilePicture);
	$(".profile-name-surname").html(user.name + " " + user.surname)
}

$(document).ready(function() {
	$.ajax({
		url: "rest/profile/",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var user = data.responseJSON;
			setBio(user);
		}
	});

	$.ajax({
		url: "rest/profile/photos",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var userPhotos = data.responseJSON;
			if (userPhotos.length == 0)
				return;
			loadPhotos(userPhotos);
		}
	});
})

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
	window.location.href = "index.html";
}

// edit profile
$("#edit-profile").click(function() {
	$(".edit-profile-card").fadeIn();
	$.ajax({
		url: "rest/profile/",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var user = data.responseJSON;
			$("#name").val(user.name);
			$("#surname").val(user.surname);
			$("#dateOfBirth").val(user.dateOfBirth);
			$("#biography").val(user.biography);
			$("#password").val("");
			$("#password2").val("");
			if (user.private)
				$("#privacy").val("private");
			else
				$("#privacy").val("public");
			if (user.gender == "MALE")
				$("#gender").val("male");
			else
				$("#gender").val("female");
		}
	});
})

function getEditData() {
	return {
		name: $("#name").val(),
		surname: $("#surname").val(),
		dateOfBirth: $("#dateOfBirth").val(),
		biography: $("#biography").val(),
		password: $("#password").val(),
		privacy: $("#privacy").val(),
		gender: $("#gender").val()
	};
}


$("#edit-profile-save-changed").click(function() {
	var pass1 = $("#password").val();
	var pass2 = $("#password2").val();
	if (pass1 != pass2) {
		$("#resultEdit").html("Invalid input");
		return;
	}
	$.ajax({
		url: "rest/profile/editProfile",
		type: "POST",
		data: JSON.stringify(getEditData()),
		contentType: "application/json",
		dataType: "json",
		complete: function(ret) {
			console.log(ret);
			var data = JSON.parse(ret.responseText);
			$("#resultEdit").html("Successfully edited profile");
			$("#date-of-birth").html(data.dateOfBirth);
			$("#profile-bio-text").html(data.biography);
			$(".profile-name-surname").html(data.name + " " + data.surname)
		}
	});

});


$(".cancel-btn").click(function() {
	$(".edit-profile-card").fadeOut();
})

$("#cancel-edit").click(function() {
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
        	$("#posted").html(x.posted);
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
			loadComments(comments);
        }
    });
}

function loadComments(comments) {
	for (let i = comments.length - 1; i >= 0; i -= 1) {
		$("#comments-content").append(makeComment(comments[i]));
	}
}

function deletePost() {
	var x = $("#post-image img").attr("id");
	var postID = JSON.stringify({ id: x });
	$.ajax({
        url: "rest/profile/delete",
        type: "DELETE",
        data: postID,
        contentType: "application/json",
        complete: function(data) {
        	alert("photo deleted.");
        	loadPhotos(data.responseJSON);
        }
    });
}

function setProfilePhoto() {
	var x = $("#post-image img").attr("id");
	var postID = JSON.stringify({ id: x });
	$.ajax({
        url: "rest/profile/setProfilePhoto",
        type: "POST",
        data: postID,
        contentType: "application/json",
        complete: function(data) {
        	$(".profile-photo img").attr("src", data.responseText);
        	alert("profile photo changed.");
        }
    });
}

$('#posts').click(function() {
    clear();
    $(this).addClass('active');
    $('.posts').addClass('visible');
    $.ajax({
        url: "rest/profile/posts",
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

// get friends
$("#friends").click(function() {
	clear();
	$(this).addClass('active');
	$('.friends').addClass('visible');
	$.ajax({
		url: "rest/profile/friends",
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
		}
	});
})

function makeFriendTemplate(user) {
	var cardTemplate = [
		'<div class="friendship">',
		'<div class="profile-picture">',
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


function getLogged(callback) {
	$.ajax({
		url: "rest/profile/",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var loggedUser = data.responseJSON;
			callback(loggedUser);
		}
	});

}

$(".friend-name").click(function() {
	var id = $(this).attr('id');
	var url = "rest/profile/viewOtherProfile?loggedId=" + "&userId=" + id;
	$.ajax({
		url: url,
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var user = data.responseJSON;
			getLogged(function(loggedUser) {
				loadUser(user, logged);
			})
		}
	});
});

$("#add-comment").click(function() {
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
			$("#comments-content").append(makeComment(comment));
        }
    });
});

function makeComment(comment, postID) {
	var cardTemplate = [
		'<div class="message-left" id="' + comment.id + '">',
        '<div class="message-container">',
        '<div class="profile-picture">',
        '<img src="images/userPictures/' + comment.authorId + '/' + comment.profilePicture + '">',
        '</div><div>' + comment.name + ' ' + comment.lastname + '  ',
        '</div><div class="message-text"><span style="font-size:10px;">',
        comment.text,
        '</span></div></div><small style="font-size:8px;margin-left:1rem;color:black;">Last edited: ' + comment.lastEdited,
        '  </small><span class="edit"><i class="uil uil-edit" onclick="editComment(\'' + comment + ',' + postID + '\')"></i></span></div>'
	];
	return $(cardTemplate.join(''));
}

function editComment(comment, postID) {
	let text = prompt("Edit comment", comment.text);
	  if (text != null) {
		var c = JSON.stringify({text: text, postID: id});
		  $.ajax({
				url: "rest/profile/editComment",
				type: "POST",
				data: c,
				contentType: "application/json",
				dataType: "json",
				complete: function(data) {
					comment = data.responseJSON;
					$('#' + comment.id + ' small').empty();
					$('#' + comment.id + ' small').append('Last edited: ' + comment.lastEdited);
					$('#' + comment.id + ' .message-text').empty();
					$('#' + comment.id + ' .message-text').append('<span style="font-size:10px;">' + comment.text + '</span>');
					event.preventDefault();
		        }
		    });
	  }
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
				$('#' + id + ' .comments-content').append(makeComment(comment, id));
				$('#' + id + ' input').val('');
				event.preventDefault();
	        }
	    });
	} else {
		alert("You can't add empty comment.");
	}
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
        	loadCommentsOnPost(comments, postID);
        	event.preventDefault();
        }
    });
}

function loadCommentsOnPost(comments, id) {
	$('#' + id + ' #view-comments').append('<div class="comment-section"><p>Comments</p><div class="comments-content"' + ' id="' + id + '"></div></div>');
	for (let i = comments.length - 1; i >= 0; i -= 1) {
		$('#' + id + ' #view-comments').append(makeComment(comments[i], id));
	}
}

