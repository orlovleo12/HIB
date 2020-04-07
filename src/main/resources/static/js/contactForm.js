let senderNameInput = $("#sender-name");
let senderEmailInput = $("#sender-email");
let senderMessageInput = $("#sender-message");
let feedbackModal = $("#feedback-modal");
let messageTemplate = "Hello, I m interested in the book ";

$(document).on('click', '#send-feedback-request', function f() {
    $('#hidden_submit_btn_feedback').click();
});

$('#feedback-form').submit(async function(e) {
    let FeedbackRequest = {
        senderName: senderNameInput.val(),
        senderEmail: senderEmailInput.val(),
        content: senderMessageInput.val()
    };
    await fetch("/feedback-request", {
        method: 'POST',
        body: JSON.stringify(FeedbackRequest),
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    });
    feedbackModal.modal('hide');
});

$(document).on('click', '#ask-question', async () => {
    let location = window.location + '';
    senderMessageInput.val(messageTemplate + '' + location.substr(0, location.indexOf("?")));
    feedbackModal.modal('show');
});