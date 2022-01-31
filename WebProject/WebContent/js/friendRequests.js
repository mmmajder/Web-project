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