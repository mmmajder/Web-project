// show friend requests
function createFriendReqCard(cardData) {
	console.log(cardData);
	var cardTemplate = [
		'<div class="request">',
		'<div class="info">',
		'<div class="profile-picture">',
		'<img src="',
		'images/userPictures/' + cardData.id + '/' + cardData.profilePicture,
		'"></div><div><h5>',
		cardData.name + ' ' + cardData.surname,
		'</h5><p class="text-muted">',
		cardData.numberOfMutualFriends + ' multural friends',
		'</p></div></div><div class="action">',
		'<button class="btn btn-primary">Accept</button>',
		'<button class="btn decline">Decline</button>',
		'</div></div>'
	];
	return $(cardTemplate.join(''));
}

$(document).ready(function() {
	$.ajax({
		url: "rest/friendRequest/",
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
        url: "rest/feed/",
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
	var picLoc = $("#add-img").val().split('\\').pop();
	var des = $("#post-text").val();
	if(des != "" || picLoc != "") {
		$.ajax({
	        url: "rest/feed/createNewPost",
	        type: "POST",
	        contentType: "application/json",
	        data: {
	        	pictureLocation: picLoc,
	        	picture: postAsPicture,
	        	description: des
	        },
	        complete: function(data) {
	        	var newPost = data.responseJSON;
	        	console.log(newPost);
				createNewPost(newPost, function(p) {
					$('#feeds').prepend(p);
					console.log("prependovo sam");
				});
	        }
	    });
	}
}

$("#remove-pic").click(function() {
	$("#add-img").val('');
})

// navbar icons
function goToMyProfile() {
	window.location.href = "profile.html";
}

function goToSearch() {
	window.location.href = "search.html";
}

function goToMessages() {
	window.location.href = "messages.html";
}

function logOut() {
	window.location.href = "index.html";
}

// remove active class from all menu items
const changeActiveItem2 = () => {
	$('.menu-item').forEach(item => {
		item.classList.remove('active');
	})
}

function changeActiveItem() {
	$('.menu-item').removeClass('active');
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
	if(postData.picture) {
		postPic = '<div class="post-photo"><img src="images/userPictures/' + postData.author + '/' + postData.pictureLocation + '"></div>';
	}
	var cardTemplate = [
        '<div class="feed"><div class="head"><div class="user"><div class="profile-picture">',
        '<img src="',
        'images/userPictures/' + user.id + '/' + user.profilePicture,
        '"></div><div class="ingo">',
        '<h3>' + user.name + ' ' + user.surname + '</h3>',
        '<small>' + postData.posted + '</small>',
        '</div></div><span class="edit"><i class="uil uil-ellipsis-h"></i></span></div><br><div class="caption">',
        '<p>' + postData.description + '</p></div>',
        postPic,
        '<div class="comments text-muted" id="view-comments">',
        '<p>View all comments</p>',
        '</div><div class="add-comment">',
        '<input type="text" placeholder="Type comment..." id="comment-text"><span><i id="add-comment" class="uil uil-enter"></i></span>',
        '</div>'
    ];
    return $(cardTemplate.join(''));
}


