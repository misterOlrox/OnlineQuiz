'use strict';

let messageInput = document.querySelector('#message');
let stompClient = null;
let username = null;

function connect() {
    username = wsContext.username;
    if (username !== '') {
        console.log("Username is " + username);
        let socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    } else {
        console.log("Don't open websocket connection: user is not authenticated");
    }
}

function onConnected() {
    console.log("On connected event started");
    let gameId = wsContext.gameId;
    if (gameId !== -1) {
        stompClient.subscribe('/topic/solo.game.info/' + gameId, onSoloGameInfoReceived);
        stompClient.subscribe('/topic/solo.game/' + gameId, onSoloGameReceived);
        getSoloGameQuestion(gameId);
    }
}

function onError(error) {
    console.log("Could't connect through websocket: " + error);
}

function sendMessage(event) {
    console.log("Send message started");
    let messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        let chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onSoloGameInfoReceived(payload) {
    console.log("Message received");
}

function onSoloGameReceived(data) {
    let question = document.getElementById("question");
    let qtitle = document.getElementById("qtitle");
    let btns = document.getElementById("answers");

    let body = JSON.parse(data.body);

    question.innerText = body.question;
    qtitle.innerText += " " + (body.number + 1);
    let possAnswers = body.answers;
    for (let i = 0; i < possAnswers.length; i++) {
        btns.innerHTML += "<button class=\"button is-rounded is-medium is-success\""
                            + " style=\"margin-left: 10px;margin-right: 10px;background-color: hsl(160, 60%, 30%)\">"
                                    + possAnswers[i]
                        + "</button>";
    }
}

function getSoloGameQuestion(id) {
    stompClient.send("/solo.game/" + id + "/get.question");
}

function sendAnswerToSoloGame(answer) {

}

connect();