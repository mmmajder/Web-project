function getSearchData() {
	return {
			 name: $("#people-search-name").val(),
			 lastName: $("#people-search-last-name").val(),
			 start: $("#start-date").val(),
			 end : $("#end-date").val()
			};
}

$("#search-btn").click(function search() {
    
    var s = JSON.stringify(getSearchData());
    
	$.ajax({
        url: "rest/search/",
        type: "POST",
        data: s,
        contentType: "application/json",
        dataType: "json",
        complete: function(ret) {
        	console.log(ret);
			fillData(ret.responseJSON);
        }
    });
})

$("#sort-name-asc").click(function() {
	$('#users').find('.person').sort(function (a, b) {
		   return $(a).attr('data-name') - $(b).attr('data-name');
		}).appendTo('#users');
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

function fillData(x) {
	$("#users").empty();
	for(var user of x) {
		$("#users").append('<div class="person" data-name="$' + user.name + '" data-surname="$' + user.surname + '" data-date="' + 
				user.dateOfBirth + '"<div class="profile-picture"><img src="images/userPictures/' + user.id + '/' + user.profilePicture + '">'
				+ '</div><div class="person-body"><h5>' + user.name + ' ' + user.surname + '</h5><p class="text-muted">' + user.dateOfBirth + '</p></div></div>');
	}
}