function loadUser(user, logged) {
	setBio(user);
	$("#edit-profile").hide();

}

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
	$("#number-of-friends").html(user.friends.length + " Friends"); // kada je nula izbaci 1 ?
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
			var author = userPhotos[0].author;
			for (let i = userPhotos.length - 1; i >= 0; i -= 3) {
				var src1 = '"images/userPictures/' + author + '/' + userPhotos[i].picture + '"';
				$("#c1").append('<img class="post-image" src=' + src1 + '>');
				if (i - 1 >= 0) {
					var src2 = '"images/userPictures/' + author + '/' + userPhotos[i - 1].picture + '"';
					$("#c2").append('<img class="post-image" src=' + src2 + '>');
				}
				if (i - 2 >= 0) {
					var src3 = '"images/userPictures/' + author + '/' + userPhotos[i - 2].picture + '"';
					$("#c3").append('<img class="post-image" src=' + src3 + '>');
				}
			}
		}
	});
})


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


$(".btn-primary").click(function() {
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

$(".post-image").click(function() {
	$(".photo-details-card").fadeIn();
	$('body').addClass('stop-scrolling');
})

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
	var cardTemplate = [
		'<div class="feed"><div class="head"><div class="user"><div class="profile-picture">',
		'<img src="',
		'images/userPictures/' + user.id + '/' + user.profilePicture,
		'"></div><div class="ingo">',
		'<h3>' + user.username + '</h3>',
		'<small>' + postData.posted + '</small>',
		'</div></div><span class="edit"><i class="uil uil-ellipsis-h"></i></span></div><br><div class="caption">',
		'<p>' + postData.description + '</p></div>',
		'<div class="comments text-muted" id="view-comments">',
		'<p>View all ' + postData.comments.length + ' comments</p>',
		'</div><div class="add-comment">',
		'<input type="text" placeholder="Type comment..." id="comment-text"><span><i id="add-comment" class="uil uil-enter"></i></span>',
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
				/*createFriendCard(item, function(data1) {
					$('.friendships').append(data1);
				} )*/
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
		'<div class="btn" id="open-chat">',
		'<i class="uil uil-message"></i>',
		'<label>Send a Message</label>',
		'</div>',
		'<div class="btn" id="remove-friend">',
		'<i class="uil uil-multiply"></i>',
		'<label>Remove Friend</label>',
		'</div>',
		'</div>',
		'</div>'
	];
	return $(cardTemplate.join(''));
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