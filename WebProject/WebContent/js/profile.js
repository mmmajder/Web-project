function clear() {
    $('.showing div').removeClass('visible');
    $('.navbar label').removeClass('active');
}

$('#photos').click(function() {
    clear();
    $(this).addClass('active');
    $('.photos').addClass('visible');
});

$('#posts').click(function() {
    clear();
    $(this).addClass('active');
    $('.posts').addClass('visible');
});

$('#friends').click(function() {
    clear();
    $(this).addClass('active');
    $('.friends').addClass('visible');
});

$(document).ready(function() {
    $.ajax({
        url: "rest/profile/",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
            var user = JSON.parse(data.responseText);
            $("#profile-user-name").html("@" + user.username);
            $("#number-of-posts").html(user.posts.length + " Posts");
            $("#number-of-photos").html(user.posts.length + " Photos"); // TODO
            $("#number-of-friends").html(user.friends.length + " Friends"); // kada je nula izbaci 1 ?
            $("#date-of-birth").html(user.dateOfBirth);
            $("#profile-bio-text").html(user.biography);
            $(".profile-info .profile-photo img").attr("src", "images/userPictures/" + user.id + "/" + user.profilePicture);
        }
    });
    
    $.ajax({
        url: "rest/profile/photos",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
            var userPhotos = JSON.parse(data.responseText);
            if(userPhotos.length == 0)
            	return;
            var author = userPhotos[0].author;
            for(let i = userPhotos.length-1; i >= 0; i-=3) {
            	var src1 = '"images/userPictures/' + author + '/' + userPhotos[i].picture + '"';
            	$("#c1").append('<img class="post-image" src=' + src1 + '>');
            	if(i-1 >= 0) {
                	var src2 = '"images/userPictures/' + author + '/' + userPhotos[i-1].picture + '"';
                	$("#c2").append('<img class="post-image" src=' + src2 + '>');
            	}
            	if(i-2 >= 0) {
                	var src3 = '"images/userPictures/' + author + '/' + userPhotos[i-2].picture + '"';
                	$("#c3").append('<img class="post-image" src=' + src3 + '>');
            	}
            }
        }
    });
    
    $.ajax({
        url: "rest/profile/posts",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
            var userPosts = JSON.parse(data.responseText);
            if(userPosts.length == 0)
            	return;
            var author = userPosts[0].author;
        	var profilePicture = '"images/userPictures/' + author + '/' + userPosts[0].picture + '"';
        	
            for(let i = userPosts.length-1; i >= 0; i-=3) {
            	var postPhoto = '"images/userPictures/' + author + '/' + userPhotos[i].picture + '"';
            	'<div class="feed"><div class="head"><div class="user"><div class="profile-picture"><img src="images/profile-1.jpg"></div>
                                <div class="ingo">
                                    <h3>Andjela Miskovic</h3>
                                    <small>5 HOURS AGO</small>
                                </div>
                            </div>

                            <span class="edit">
                                <i class="uil uil-ellipsis-h"></i>
                            </span>
                        </div><br>

                        <div class="caption">
                            <p>Andjela Miskovic je postavila neku sliku.</p>
                        </div>

                        <div class="post-photo">
                            <img src="images/profile-1.jpg">
                        </div>

                        <div class="interaction">
                            <div class="interaction-buttons">
                                <span class="like">
                                    <i class="uil uil-thumbs-up"></i>
                                </span>
                                <span class="comment">
                                    <i class="uil uil-comment-dots"></i>
                                </span>
                            </div>
                        </div>

                        <div class="liked-by">
                            <p>Liked by <b>Milan Ajder</b> and others...</p>
                        </div>

                        <div class="comments text-muted view-comments">
                            <p>View all 5 comments</p>
                        </div>
                        <div class="add-comment">
                        	<span>
                                <img src="images/profile-1.jpg">
                            </span>
                        	<input type="text" placeholder="Add comment" id="add-comment">
                        	<button class="uil-enter"></button>
                        </div>

                    </div>'
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


$("#edit-profile").click(function() {
    $(".edit-profile-card").fadeIn();
    $.ajax({
        url: "rest/profile/",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
            var user = JSON.parse(data.responseText)
            console.log(user)
            $("#name").val(user.name);
            $("#surname").val(user.surname);
            $("#dateOfBirth").val(user.dateOfBirth);
            $("#biography").val(user.biography);
        }
    });
})

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