'use strict';

let messageInput = document.querySelector('#message');
let stompClient = null;
let username = null;
let soloGameId = null;
let connectedToWs = false;
let timeForQuestion = null;
let numberOfQuestions = null;
let timeLeft = 0;
let timerId = null;

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
        connectedToWs = true;
        stompClient.subscribe('/topic/solo/game/info/' + soloGameId, onSoloGameInfoReceived);
    }
}

function onError(error) {
    console.log("Could't connect through websocket: " + error);
}

function onSoloGameInfoReceived(data) {
    let info = JSON.parse(data.body);
    if (info.type === "timeout.info") {
        if (info.timeout === true) {
            postAnswerToSoloGame(null);
        }
        timeLeft = 0;
    }
    if (info.type === 'game.process.info') {
        timeForQuestion = info.timeForQuestionInMillis;
        numberOfQuestions = info.numberOfQuestions;
        console.log("Get game.process.info: timeForQuestion:"
            + timeForQuestion +
            " numberOfQuestions "
            + numberOfQuestions
        );
    }
}

function updateClock(update) {
    let timeLeftElem = document.getElementById("timeLeftInfo");
    let mins = parseInt(timeLeft / 60000);
    let secs = parseInt((timeLeft - mins * 60000) / 1000);
    if (secs <= 9) {
        secs = '0' + secs;
    }
    timeLeftElem.innerText = "Time left - " + mins + ':' + secs;
    if (!update) {
        timeLeft -= 100;
    }
    if (timeLeft < 0) {
        console.log("Time left < 0, postingAnswer null");
        postAnswerToSoloGame(null);
    }
}

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

function parseGetQuestionResp(nextQuestion) {
    if (nextQuestion == null || nextQuestion.result === true) {
        window.location.replace("/result/solo/" + soloGameId);
    }

    let question = document.getElementById("question");
    let qtitle = document.getElementById("qtitle");
    let btns = document.getElementById("answers");

    question.innerText = nextQuestion.question;
    timeLeft = nextQuestion.timeLeft + 100;
    if (timerId !== null) {
        clearTimeout(timerId);
    }
    updateClock(false);
    timerId = setInterval(function () {
        updateClock();
    }, 100);
    qtitle.innerText = 'Question';
    btns.innerHTML = '';
    qtitle.innerText += " " + (nextQuestion.number + 1);
    let possAnswers = nextQuestion.answers;
    for (let i = 0; i < possAnswers.length; i++) {
        let answ = possAnswers[i];
        btns.innerHTML += "<button id=\"btn" + i + "\" name=\"answer\" class=\"button is-rounded is-medium is-success\""
            + " style=\"margin: 5px 10px;background-color: hsl(160, 60%, 30%)\">"
            + answ
            + "</button>";
    }

    for (let i = 0; i < possAnswers.length; i++) {
        document
            .getElementById("btn" + i)
            .addEventListener("click", (event) => {
                postAnswerToSoloGame(event.target.innerText)
            });
    }
}

function postAnswerToSoloGame(answer) {
    let answerJson = {
        value: answer
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
            if (data.ended === true) {
                console.log("data.ended === true");
                window.location.replace("/result/solo/" + soloGameId);
            } else if (data.hasOwnProperty("nextQuestion")
                && data.hasOwnProperty("prevResult")
                && data.nextQuestion !== null
            ) {
                console.log("data.hasOwnProperty(\"nextQuestion\") && data.hasOwnProperty(\"prevResult\") && data.nextQuestion !== null");
                parseGetQuestionResp(data.nextQuestion);
                showAnswerResult(data.prevResult);
            } else if (data.prevResult !== null && data.ended === false) {
                console.log("data.prevResult !== null && data.result === false");
                showAnswerResult(data.prevResult);
                window.location.replace("/result/solo/" + soloGameId);
            } else {
                window.location.reload();
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
