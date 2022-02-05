$(document).ready(function() {	
	$.ajax({
        url: "rest/feed/getPublicFeed",
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

//navbar icons
function goToSearch() {
	window.location.href = "search.html";
}

function logIn() {
	window.location.href = "index.html";
}

function register() {
	window.location.href = "register.html";
}

//remove active class from all menu items
const changeActiveItem2 = () => {
	$('.menu-item').forEach(item => {
		item.classList.remove('active');
	})
}

function changeActiveItem() {
	$('.menu-item').removeClass('active');
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


function makeCardTemplate(user, postData) {
	var postPic = '';
	if (postData.pictureLocation != "") {
		postPic = '<div class="post-photo"><img src="images/userPictures/' + postData.author + '/' + postData.pictureLocation + '"></div>';
	}
	var cardTemplate = [
        '<div class="feed" id="' + postData.id + '"><div class="head"><div  onclick="goToOtherProfile(\'' + user.id + '\')" class="user"><div class="profile-picture">',
        '<img src="',
        'images/userPictures/' + user.id + '/' + user.profilePicture,
        '"></div><div class="ingo">',
        '<h3>' + user.name + ' ' + user.surname + '</h3>',
        '<small>' + printDateTime(postData.posted) + '</small>',
        '</div></div></div><br><div class="caption">',
        '<p>' + postData.description + '</p></div>',
        postPic,
        '<div class="comments text-muted" id="view-comments" onclick="viewComments(\'' + postData.id + '\')">',
        '<p>View all comments</p>',
        '</div>'
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