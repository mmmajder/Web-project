$(document).ready(function() {
    $("#message-search").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $(".message h5").filter(function() {
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

function goToMyProfile() {
    window.location.href = "profile.html";
}