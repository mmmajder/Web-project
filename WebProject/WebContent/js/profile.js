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
    $("#friends-search").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $(".friendship h3").filter(function() {
            $(this).parent().parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});