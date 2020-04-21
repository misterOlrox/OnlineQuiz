'use strict';

let soloGameId = null;
let timeLeft = 0;
let timerId = null;
let possAnswers = [];

let progressBar = document.getElementById("progress-bar");
progressBar.max = appContext.numberOfQuestions;

function getQuestion() {
    soloGameId = appContext.gameId;

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/game/solo/" + soloGameId + "/question",
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

function parseGetQuestionResp(nextQuestion) {
    console.log("Parsing questions from" + nextQuestion.toString());

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
    progressBar.value = nextQuestion.number;
    possAnswers = nextQuestion.answers;
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
                postAnswerToSoloGame(i)
            });
    }
}

function postAnswerToSoloGame(answerInd) {
    let answer = possAnswers[answerInd];
    let answerJson = {
        value: answer
    };

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/game/solo/" + soloGameId + "/answer",
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
    let notificationBox = document.getElementById("prevInfo");
    if (data) {
        if (data.status === "CORRECT") {
            notificationBox.innerHTML = "Your answer was correct!";
            notificationBox.className = "notification is-success is-light";
        } else if (data.status === "WRONG") {
            notificationBox.innerHTML =
                "<p>Your previous answer was incorrect!</p>"
                + "<p>Correct answer: " + data.correctAnswer + "</p>"
                + "<p>Your answer: " + data.yourAnswer + "</p>";
            notificationBox.className = "notification is-danger is-light";
        } else if (data.status === "UNKNOWN") {
            notificationBox.innerHTML =
                "<p>Your previous answer was incorrect!</p>"
                + "<p>Correct answer: " + data.correctAnswer + "</p>";
            notificationBox.className = "notification is-danger is-light";
        } else if (data.status === "TIMEOUT") {
            notificationBox.innerHTML = "You were late with the answer";
            notificationBox.className = "notification is-warning is-light";
        } else {
            notificationBox.hidden = true;
            return;
        }
        notificationBox.hidden = false;
    } else {
        notificationBox.hidden = true;
    }
}
