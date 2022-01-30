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
        url: "rest/search/search",
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

$(document).ready(function() {
	if(!getCurrentUser()) {
		$('#my-profile-a').hide();
		$('#messages').hide();
		$('#log-out-a').hide();
		$('#buttons').hide();
		$('.sidebar').append('<a class="menu-item" id="log-in" onclick="logIn()"><span><i class="uil uil-sign-in-alt"></i></span><h3>Log In</h3></a>');
		$('.sidebar').append('<a class="menu-item" id="register" onclick="register()"><span><i class="uil uil-user-circle"></i></span><h3>Create an account</h3></a>');
	}
})

function goToFeed() {
	if(!getCurrentUser()) {
		window.location.href = "unlogged.html";
	} else {
		window.location.href = "feed.html";
	}
}

function logIn() {
	window.location.href = "index.html";
}

function register() {
	window.location.href = "register.html";
}

function getCurrentUser() {
	$.ajax({
		url: "rest/profile/",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			var user = data.responseJSON;
			return user;
		}
	});
}

function compare(an, bn) {
	if(an > bn) {
		return 1;
	}
	if(an < bn) {
		return -1;
	}
	return 0;
}

function clear() {
	$("#sort-name-asc").removeClass('active-sort');
	$("#sort-name-dsc").removeClass('active-sort');
	$("#sort-surname-asc").removeClass('active-sort');
	$("#sort-surname-dsc").removeClass('active-sort');
	$("#sort-birth-asc").removeClass('active-sort');
	$("#sort-birth-dsc").removeClass('active-sort');
}

$("#sort-name-asc").click(function() {
	var items = $('div .person');
	items.sort(function(a, b){
		return compare($(a).attr('data-name'), $(b).attr('data-name'))
	});
	items.appendTo('#users');
	clear();
	$("#sort-name-asc").addClass('active-sort');
})

$("#sort-name-dsc").click(function() {
	var items = $('div .person');
	items.sort(function(a, b){
		return compare($(b).attr('data-name'), $(a).attr('data-name'))
	});
	items.appendTo('#users');
	clear();
	$("#sort-name-dsc").addClass('active-sort');
})

$("#sort-surname-asc").click(function() {
	var items = $('div .person');
	items.sort(function(a, b){
		return compare($(a).attr('data-surname'), $(b).attr('data-surname'))
	});
	items.appendTo('#users');
	clear();
	$("#sort-surname-asc").addClass('active-sort');
})

$("#sort-surname-dsc").click(function() {
	var items = $('div .person');
	items.sort(function(a, b){
		return compare($(b).attr('data-surname'), $(a).attr('data-surname'))
	});
	items.appendTo('#users');
	clear();
	$("#sort-surname-dsc").addClass('active-sort');
})

$("#sort-birth-asc").click(function() {
	var items = $('div .person');
	items.sort(function(a, b){
		return compare($(a).attr('data-date'), $(b).attr('data-date'))
	});
	items.appendTo('#users');
	clear();
	$("#sort-birth-asc").addClass('active-sort');	
})

$("#sort-birth-dsc").click(function() {
	var items = $('div .person');
	items.sort(function(a, b){
		return compare($(b).attr('data-date'), $(b=a).attr('data-date'))
	});
	items.appendTo('#users');
	clear();
	$("#sort-birth-dsc").addClass('active-sort');
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
				user.dateOfBirth + '"><div class="profile-picture"><img src="images/userPictures/' + user.id + '/' + user.profilePicture + '">'
				+ '</div><div class="person-body"><h5>' + user.name + ' ' + user.surname + '</h5><p class="text-muted">' + user.dateOfBirth + '</p></div></div>');
	}
}