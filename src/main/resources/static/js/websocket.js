'use strict';

let messageInput = document.querySelector('#message');
let stompClient = null;
let username = null;
let soloGameId = null;

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
    soloGameId = wsContext.gameId;
    if (soloGameId !== -1) {
        stompClient.subscribe('/topic/solo.game.info/' + soloGameId, onSoloGameInfoReceived);
        stompClient.subscribe('/topic/solo.game/' + soloGameId, onSoloGameReceived);
        stompClient.subscribe('/topic/solo.game/' + soloGameId + '/answer.res', onAnswerResult);
        getSoloGameQuestion(soloGameId);
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
    qtitle.innerText = 'Question';
    btns.innerHTML = '';
    qtitle.innerText += " " + (body.number + 1);
    let possAnswers = body.answers;
    for (let i = 0; i < possAnswers.length; i++) {
        let answ = possAnswers[i];
        btns.innerHTML += "<button name=\"answer\" class=\"button is-rounded is-medium is-success\""
            + " style=\"margin: 5px 10px;background-color: hsl(160, 60%, 30%)\""
            + " onclick='sendAnswerToSoloGame(\"" + answ.toString() + "\")'>"
            + answ
            + "</button>";
    }
}

function getSoloGameQuestion(id) {
    stompClient.send("/solo.game/" + id + "/get.question");
}

function sendAnswerToSoloGame(answer) {
    let answerJson = {
        value: answer
    };
    stompClient.send("/solo.game/" + soloGameId + "/answer", {}, JSON.stringify(answerJson));
}

function onAnswerResult(data) {

}

connect();