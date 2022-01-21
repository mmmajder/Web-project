$(document).ready(function() {
    $("#people-search-name").change(search());
    $("#people-search-last-name").change(search());
    $("#start-date").change(search());
    $("#end-date").change(search());
});

function getSearchData() {
	return {
			  Name: $("#people-search-name").val(),
			  LastName = $("#people-search-last-name").val(),
			  Start: $("#start-date").val(),
			  End : $("#end-date").val()
			};
}

function search() {
    
    var data = getSearchData();
    var s = JSON.stringify(data);
    
	$.ajax({
        url: "rest/search/",
        type: "POST",
        data: s,
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
			fillData(data);
        }
    });
}

function fillData(data) {
	$("#users").empty();
	for(var user of data) {
		$("#users").append('<div class="person"><div class="profile-picture"><img src="' + user.profilePicture + '">'
				+ '</div><div class="person-body"><h5>' + user.name + user.surname + '</h5><p class="text-muted">8 mutual friends</p></div></div>');
	}
}