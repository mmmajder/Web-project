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

    $.ajax({
        url: "rest/profile/posts",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
            var userPosts = data.responseJSON;
            if (userPosts.length == 0)
                return;
            var author = userPosts[0].author;
            //var profilePicture = '"images/userPictures/' + author + '/' + userPosts[i].profilePicture + '"';
            var profilePicture = "images/default.jpg";

            for (let i = userPosts.length - 1; i >= 0; i--) {
                var postPhoto = '"images/userPictures/' + author + '/' + userPosts[i].picture + '"';
                var postID = userPosts[i].id;

                $('#feeds').html($('<div>', { class: 'feed', id: postID }));
                $('#' + postID).html($('<div>', { class: 'head' }));
                $('#' + postID + " .head").html($('<div>', { class: 'user' }));
                $('#' + postID + " .head .user").html($('<div>', { class: 'profile-picture' }));
                $('#' + postID + " .head .user .profile-picture").append('<img src=' + profilePicture + '>');
                $('#' + postID + " .head .user").html($('<div>', { class: 'ingo' }));
                $('#' + postID + " .head .user .ingo").append('<h3>' + userPosts[i].id + '</h3>');
                $('#' + postID + " .head .user .ingo").append('<small>' + userPosts[i].posted + '</small>');
                $('#' + postID + " .head").html($('<div>', { class: 'edit' }));
                $('#' + postID + " .head .edit").append('<i class="uil uil-ellipsis-h"></i>');

                $('#' + postID).html($('<div>', { class: 'caption' }));
                $('#' + postID + " .caption").append('<p>' + userPosts[i].caption + '</p>');

                $('#' + postID).html($('<div>', { class: 'interaction' }));
                //$('#' + postID + " .interaction").html($('<div>', { class: 'interaction-buttons' }));
                //$('#' + postID + " .interaction .interaction-buttons").append('<span class="like"><i class="uil uil-thumbs-up"></i></span>');
                //$('#' + postID + " .interaction .interaction-buttons").append('<p>' + userPosts[i].likedBy);

                $('#' + postID).html($('<div>', { class: 'comments text-muted', id: 'view-comments' }));
                $('#' + postID + "#view-comments").append('<p>View all ' + userPosts[i].comments.length + ' comments</p>');

                $('#' + postID).html($('<div>', { class: 'add-comment' }));
                $('#' + postID + ".add-comment").append('<input type="text" placeholder="Type comment..." id="comment-text"><span><i id="add-comment" class="uil uil-enter"></i></span>');
            }
        }
    });
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
            var user = data.responseJSON;
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