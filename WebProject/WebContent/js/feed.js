// messages search
$(document).ready(function() {
    $("#people-search").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $(".person h5").filter(function() {
            $(this).parent().parent().toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});


// navbar icons
function goToMyProfile() {
    window.location.href = "profile.html";
}

function goToMessages() {
    window.location.href = "messages.html";
}

function logOut() {
    window.location.href = "index.html";
}

// remove active class from all menu items
const changeActiveItem2 = () => {
    $('.menu-item').forEach(item => {
        item.classList.remove('active');
    })
}

function changeActiveItem() {
    $('.menu-item').removeClass('active');
}

// notifications popup
$(document).ready(function() {
	$('#imeId').html(localStorage.user);
	//$('#imeId').html("Cao");
    $('.menu-item').each(function() {
        $(this).click(function() {
            changeActiveItem();
            $(this).addClass('active');
            if (this.id != 'notifications') {
                $('.notifications-popup').fadeOut();
            } else {
                $('.notifications-popup').fadeIn();
                $('#notifications .notifications-count').hide();
            }
        });
    });
});

// add new image
function getImage(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            $('#add-post-image').attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

// add comment
$(".uil-enter").click(function() {
	
	$.ajax({
		url: "rest/feed/addComment",
		type: "POST",
		data: $("#add-comment"),
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			$("#add-comment").html("");
			var user = JSON.parse(data.responseText)
			console.log(user)
			$("#profile-user-name").html("@" + user.username);
			$("#number-of-posts").html(user.posts.length + " Posts");
			$("#number-of-photos").html(user.posts.length + " Photos");			// TODO
			$("#number-of-friends").html(user.friends.length + " Friends");		// kada je nula izbaci 1 ?
			$("#date-of-birth").html(user.dateOfBirth);
			$("#profile-bio-text").html(user.biography);
		}
	});
})



// friends request animation
$(".decline").click(function() {
    $(this).parent().parent().fadeOut('slow');
})

/*
// opening view comments
$("#view-comments").onclick(function() {
    $("#view-comments-card").show();
})
$("#view-comments-card").onclick(function(e) {
    if (e.target.classList.contains('view-comments')) {
        $("#view-comments-card").hide();
    }
})*/
const comment = document.getElementsByClassName("view-comments");
const card = document.getElementByClassName("view-comments-card");

const openPostCard = () => {
    card.style.display = 'grid';
}
const closePostCard = (e) => {
    if (!e.target.classList.contains('view-comments')) {
        card.style.display = 'none';
    }
}

comment.addEventListener('click', openPostCard);
card.addEventListener('click', closePostCard);


/*
// theme card opening and closing 
const openThemeCard = () => {
    themeCard.style.display = 'grid';
}
const closeThemeCard = (e) => {
    if (e.target.classList.contains('customize-theme')) {
        themeCard.style.display = 'none';
    }
}
theme.addEventListener('click', openThemeCard);
themeCard.addEventListener('click', closeThemeCard);

// theme changing
let lightColorLightness;
let whiteColorLightness;
let darkColorLightness;

const changeBackground = () => {
    root.style.setProperty('--light-color-lightness', lightColorLightness);
    root.style.setProperty('--white-color-lightness', whiteColorLightness);
    root.style.setProperty('--dark-color-lightness', darkColorLightness);
}

// set light theme
theme1.addEventListener('click', () => {
    darkColorLightness = '17%';
    whiteColorLightness = '100%';
    lightColorLightness = '95%';
    body.style.backgroundColor = '#F0EEF6';
    body.style.backgroundImage = '';

    theme1.classList.add('active');
    theme2.classList.remove('active');
    theme3.classList.remove('active');
    changeBackground();
});

// set gradient theme
theme2.addEventListener('click', () => {
    body.style.backgroundColor = '#8BC6EC';
    body.style.backgroundImage = 'linear-gradient(90deg, #9599E2 0%, #8BC6EC 100%)';

    theme1.classList.remove('active');
    theme2.classList.add('active');
    theme3.classList.remove('active');
});

// set dark theme
theme3.addEventListener('click', () => {
    darkColorLightness = '95%';
    whiteColorLightness = '10%';
    lightColorLightness = '0%';
    body.style.backgroundColor = '#241E38';
    body.style.backgroundImage = '';

    theme1.classList.remove('active');
    theme2.classList.remove('active');
    theme3.classList.add('active');
    changeBackground();
});*/