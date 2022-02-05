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
	if(!user.admin) {
		$("#number-of-posts").html(user.posts.length + " Posts");
		$("#number-of-photos").html(user.posts.length + " Photos"); // TODO
		$("#number-of-friends").html(user.friends.length + " Friends"); // kada je
																		// nula
																		// izbaci 1
																		// ?
	}
	$("#date-of-birth").html(printDate(user.dateOfBirth));
	$("#profile-bio-text").html(user.biography);
	if(user.profilePicture == "")
		$(".profile-info .profile-photo img").attr("src", "images/default.jpg");
	else
		$(".profile-info .profile-photo img").attr("src", "images/userPictures/" + user.id + "/" + user.profilePicture);
	$(".profile-name-surname").html(user.name + " " + user.surname)
}

$(document).ready(function() {
	$.ajax({
		url: "rest/profile/getUser",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var user = data.responseJSON;
			setBio(user);
			if(user.admin) {
				$(".menu").hide();
				$(".showing").hide();
			}
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
	$.ajax({
        url: "rest/logout/logout",
        type: "GET",
        contentType: "application/json",
        complete: function(data) {
			window.location.href = "index.html";
        }
    });
}

// edit profile
$("#edit-profile").click(function() {
	$(".edit-profile-card").fadeIn();
	$.ajax({
		url: "rest/profile/getUser",
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
			if (ret!=null) {
				var data = JSON.parse(ret.responseText);
				$("#resultEdit").html("Successfully edited profile");
				$("#date-of-birth").html(printDate(data.dateOfBirth));
				$("#profile-bio-text").html(data.biography);
				$(".profile-name-surname").html(data.name + " " + data.surname);
			} else {
				$("#resultEdit").html("Unable to edit profile. Enter valid data.");
			}
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
    					console.log("jsm");
    				} 
    				if (user.id == comments[i].authorId || user.admin) {
    					$('#' + comments[i].id).append('<span class="del" onclick="deleteComment(\'' + comments[i].id + '\',\'' + postID.id + '\')"><i class="uil uil-trash-alt"></i></span>');
    				} 
    			});
        	}
        }
    });
}

function deletePhoto() {
	var x = $("#post-image img").attr("id");
	var postID = JSON.stringify({ id: x });
	$.ajax({
        url: "rest/profile/deletePhoto",
        type: "DELETE",
        data: postID,
        contentType: "application/json",
        complete: function(data) {
        	loadPhotos(data.responseJSON);
        	$(".photo-details-card").fadeOut();
        	$('body').removeClass('stop-scrolling');
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
        '<small>' + printDateTime(postData.posted) + '</small>',
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
			});
		}
	});
})

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
			$('.friendship #' + userId).parent().fadeOut();
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
					$('#' + comment.id).append('<span class="edit" style="float:left;" onclick="editComment(\'' + comment.id + '\',\'' + comment.text + '\',\'' + id + '\')"><i class="uil uil-edit"></i></span>');
				} 
				if (user.id == comment.authorId || user.admin) {
					$('#' + comment.id).append('<span class="del" style="float:left;" onclick="deleteComment(\'' + comment.id + '\',\'' + id + '\')"><i class="uil uil-trash-alt"></i></span>');
				} 
			});
			$("#comment-text").val('');
			$("#photo-comments").scrollTop($("#photo-comments")[0].scrollHeight);
        }
    });
});

