'use strict';

let stompClient = null;
let username = null;
let connectedToWs = false;

const ws = {

    connect: function () {
        username = appContext.username;
        if (username !== '') {
            console.log("Username is " + username);
            let socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, ws.onConnected, ws.onError);
        } else {
            console.log("Don't open websocket connection: user is not authenticated");
        }
    },

    onConnected: function () {
        console.log("On connected event started");
        soloGameId = appContext.gameId;
        if (soloGameId !== -1) {
            connectedToWs = true;
            stompClient.subscribe('/topic/solo/game/info/' + soloGameId, ws.onSoloGameInfoReceived);
        }
    },

    onError: function (error) {
        console.log("Could't connect through websocket: " + error);
    },

    onSoloGameInfoReceived: function (data) {
        let info = JSON.parse(data.body);
        if (info.type === "timeout.info") {
            if (info.timeout === true) {
                postAnswerToSoloGame(null);
            }
            timeLeft = 0;
        }
        if (info.type === 'game.process.info') {
            console.log("Get game.process.info: timeForQuestion:"
                + timeForQuestion +
                " numberOfQuestions "
                + numberOfQuestions
            );

        }
    },

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

};

ws.connect();