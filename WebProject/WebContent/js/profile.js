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
	if ($("#password").val()!=$("#password2").val()) {
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
			$("#resultEdit").html("Successfully edited profile");
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
            //$('#feeds').append(cards);
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


