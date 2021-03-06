var currentLang = '';
var bottom = '';

$(document).ready(function () {
    if (currentLang == '') {
        currentLang = $('#dd_menu_link').data('currentLang');
    }
    getLanguage();
    setLocaleFields();
});

function status(response) {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}

function text(response) {
    return response.text()
}

function sendNewPass() {
    let password0 = $('#newPass').val();
    let password1 = $('#newPass0').val();
    if (password0 === password1) {
        let tmp = {};
        tmp['password'] = password0;
        tmp['token'] = getUrlParameter('token');
        let tmpSend = JSON.stringify(tmp);
        sendNewPassReq(tmpSend);
    } else {
        showErrorPassword('Passwords don\'t match!');
        setTimeout(hideErrorPassword, 5000);
    }
}

function showErrorPassword(x) {
    $('#errorMessagePassword').text(x);
    $('#collapsePassword').attr('class', 'collapse show');
}

function hideErrorPassword() {
    $('#collapsePassword').attr('class', 'collapse');
}

function sendNewPassReq(x) {
    fetch("/newPassword", {
        method: 'POST',
        body: x,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    }).then(status)
        .then(text)
        .then(function (resp) {
            if (resp === "passError") {
                showErrorPassword('The password must be between 8 and 64 and must contain numbers and characters in the upper and lower registers, without spaces!');
                setTimeout(hideErrorPassword, 5000);
            } else {
                if (resp === "notValid") {
                    showModal('Not valid url', 'danger');
                    setTimeout(hideModal, 5000);
                } else {
                    showModal('Password successfully saved!', 'success');
                    setTimeout(hideModal, 2000);
                }
            }
        });
}

function showModal(x, y) {
    $('#idMessagesSuccess').attr('class', 'alert alert-' + y);
    $('#idMessagesSuccess').text(x);
    $('#staticBackdrop').modal();
}

function hideModal() {
    $('#staticBackdrop').modal('hide');
    redir();
}

function redir() {
    var delay = 1000;
    setTimeout("document.location.href='/'", delay);
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;
    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}