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
            var user = JSON.parse(data.responseText)
            console.log(user)
            $("#profile-user-name").html("@" + user.username);
            $("#number-of-posts").html(user.posts.length + " Posts");
            $("#number-of-photos").html(user.posts.length + " Photos"); // TODO
            $("#number-of-friends").html(user.friends.length + " Friends"); // kada je nula izbaci 1 ?
            $("#date-of-birth").html(user.dateOfBirth);
            $("#profile-bio-text").html(user.biography);
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