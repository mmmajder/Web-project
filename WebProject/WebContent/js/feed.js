// sidebar
const menuItems = document.querySelectorAll('.menu-item');

// messages
const messagesNotification = document.querySelector('#messages');
const messages = document.querySelector('.messages');
const message = messages.querySelectorAll('.message');
const messageSearch = document.querySelector('#message-search');

// theme
const theme = document.querySelector('#themes');
const themeCard = document.querySelector('.customize-theme');
const root = document.querySelector(':root');
const theme1 = document.querySelector('.theme-1');
const theme2 = document.querySelector('.theme-2');
const theme3 = document.querySelector('.theme-3');
const body = document.querySelector('body');


// remove active class from all menu items
const changeActiveItem = () => {
    menuItems.forEach(item => {
        item.classList.remove('active');
    })
}

// setting notifications to popup when selected
menuItems.forEach(item => {
    item.addEventListener('click', () => {
        changeActiveItem();
        item.classList.add('active');
        if (item.id != 'notifications') {
            document.querySelector('.notifications-popup').style.display = 'none';
        } else {
            document.querySelector('.notifications-popup').style.display = 'block';
            document.querySelector('#notifications .notifications-count').style.display = 'none';
        }
    })
})

// search chat filter
const searchMessage = () => {
    const value = messageSearch.value.toLowerCase();
    message.forEach(user => {
        let name = user.querySelector('h5').textContent.toLowerCase();
        if (name.indexOf(value) != -1) {
            user.style.display = 'flex';
        } else {
            user.style.display = 'none';
        }
    })
}
messageSearch.addEventListener('keyup', searchMessage);

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
});

document.getElementById("my-profile").onclick = function() {
    location.href = "profile.html";
};