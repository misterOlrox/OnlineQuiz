'use strict';

let stompClient = null;
let username = null;
let connectedToWs = false;

const ws = {

    onlineUsers: [],

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
        connectedToWs = true;
        stompClient.subscribe('/topic/online', ws.onTopicOnlineReceiving);
    },

    onError: function (error) {
        console.log("Could't connect through websocket: " + error);
    },

    onTopicOnlineReceiving: function (data) {
        let info = JSON.parse(data.body);
        if (info.type === "online.topic.info.all") {
            for (let i = 0; i < info.onlineUsers.length; i++) {
                let onlineUser = info.onlineUsers[i];
                console.log("online.topic.info.all: " + onlineUser.username);
            }
            ws.onlineUsers = info.onlineUsers;
        } else if (info.type === "online.topic.info.new") {
            console.log("online.topic.info.new, id= " + info.newUser.id + ", name=" + info.newUser.username);
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