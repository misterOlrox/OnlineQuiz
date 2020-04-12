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
    stompClient.subscribe('/topic/public', onMessageReceived);
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    );
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
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    console.log("onMessageReceived started");
    console.log("Payload:" + payload);
}

connect();