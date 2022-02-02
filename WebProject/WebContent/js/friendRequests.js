// show friend requests
function createFriendReqCard(user) {	
	var cardTemplate = [
		'<div class="request" id="',
		user.id,
		'">',
		'<div class="info" onclick="goToOtherProfile(\'' + user.id + '\')" >',
		'<div class="profile-picture">',
		'<img src="',
		'images/userPictures/' + user.id + '/' + user.profilePicture,
		'"></div><div><h5>',
		user.name + ' ' + user.surname,
		'</h5><p class="text-muted">',
		user.numberOfMutualFriends + ' mutual friends',
		'</p></div></div><div class="action">',
		'<button class="btn btn-primary" onclick="acceptRequest(\'' + user.id + '\')">Accept</button>',
		'<button class="btn decline" onclick="declineRequest(\'' + user.id + '\')">Decline</button>',
		'</div></div>'
	];
	return $(cardTemplate.join(''));
}

function acceptRequest(senderId) {
	$.ajax({
		url: "rest/friendRequest/accept",
		type: "POST",
		contentType: "application/json",
		data: senderId,
		complete: function() {
			$(".friend-requests #" + senderId).fadeOut();
		}
	});
}

function declineRequest(senderId) {
	$.ajax({
		url: "rest/friendRequest/deny",
		type: "POST",
		contentType: "application/json",
		data: senderId,
		complete: function() {
			$(".friend-requests #" + senderId).fadeOut();
		}
	});
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
});