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

function fillData(x) {
	$("#users").empty();
	for(var user of x) {
		$("#users").append('<div class="person"><div class="profile-picture"><img src="images/userPictures/' + user.id + '/' + user.profilePicture + '">'
				+ '</div><div class="person-body"><h5>' + user.name + ' ' + user.surname + '</h5><p class="text-muted">8 mutual friends</p></div></div>');
	}
}