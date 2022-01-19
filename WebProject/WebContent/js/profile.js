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
    $(".edit-profile").show();
})