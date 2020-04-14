'use strict';

let messageInput = document.querySelector('#message');
let stompClient = null;
let username = null;
let soloGameId = null;

function getQuestion() {
    soloGameId = wsContext.gameId;

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/game/solo/" + soloGameId + "/question",
        dataType: 'json',
        timeout: 100000,
        success: function (data) {
            console.log("SUCCESS: ", data);
            parseGetQuestionResp(data);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        },
        done: function (e) {
            console.log("DONE");
        }
    });
}

// function connect() {
//     username = wsContext.username;
//     if (username !== '') {
//         console.log("Username is " + username);
//         let socket = new SockJS('/ws');
//         stompClient = Stomp.over(socket);
//         stompClient.connect({}, onConnected, onError);
//     } else {
//         console.log("Don't open websocket connection: user is not authenticated");
//     }
// }
//
// function onConnected() {
//     console.log("On connected event started");
//     soloGameId = wsContext.gameId;
//     if (soloGameId !== -1) {
//         stompClient.subscribe('/topic/solo.game.info/' + soloGameId, onSoloGameInfoReceived);
//         getSoloGameQuestion(soloGameId);
//     }
// }
//
// function onError(error) {
//     console.log("Could't connect through websocket: " + error);
// }
//
// function sendMessage(event) {
//     console.log("Send message started");
//     let messageContent = messageInput.value.trim();
//     if (messageContent && stompClient) {
//         let chatMessage = {
//             sender: username,
//             content: messageInput.value,
//             type: 'CHAT'
//         };
//         stompClient.send("/chat.sendMessage", {}, JSON.stringify(chatMessage));
//         messageInput.value = '';
//     }
//     event.preventDefault();
// }
//
// function onSoloGameInfoReceived(payload) {
//     console.log("Message received");
// }

function parseGetQuestionResp(response) {
    let question = document.getElementById("question");
    let qtitle = document.getElementById("qtitle");
    let btns = document.getElementById("answers");

    question.innerText = response.question;
    qtitle.innerText = 'Question';
    btns.innerHTML = '';
    qtitle.innerText += " " + (response.number + 1);
    let possAnswers = response.answers;
    for (let i = 0; i < possAnswers.length; i++) {
        let answ = possAnswers[i];
        btns.innerHTML += "<button id=\"btn" + i + "\" name=\"answer\" class=\"button is-rounded is-medium is-success\""
            + " style=\"margin: 5px 10px;background-color: hsl(160, 60%, 30%)\">"
            + answ
            + "</button>";
    }

    for (let i = 0; i < possAnswers.length; i++) {
        document.getElementById("btn" + i).addEventListener("click", postAnswerToSoloGame);
    }
}

function postAnswerToSoloGame(event) {
    let answerJson = {
        value: event.target.innerText
    };

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/game/solo/" + soloGameId + "/answer",
        data: JSON.stringify(answerJson),
        dataType: 'json',
        timeout: 100000,
        success: function (data) {
            console.log(data);
            if (data.hasOwnProperty("nextQuestion") && data.hasOwnProperty("prevResult") && data.nextQuestion !== null) {
                parseGetQuestionResp(data.nextQuestion);
                showAnswerResult(data.prevResult);
            } else if (data.prevResult !== null && data.resultId !== null) {
                showAnswerResult(data.prevResult);
                window.location.replace("/result/solo/" + data.resultId);
            }
        },
        error: function (data) {
            if (data.hasOwnProperty("responseJSON")) {
                console.log("Error: " + data.responseJSON.errorMessage)
            } else {
                console.log("Unknown error");
            }
        },
        done: function (e) {
            console.log("DONE");
        }
    });
}

function showAnswerResult(data) {

}
