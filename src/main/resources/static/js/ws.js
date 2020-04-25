'use strict';

let stompClient = null;
let username = null;
let connectedToWs = false;

const ws = {

    onConnectedListeners: [],
    onlineUsers: [],
    listenersOnlineInfoAll: [],
    listenersInvites: [],

    connect: function () {
        username = appContext.username;
        if (username !== '') {
            console.log("Username is " + username);
            let socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            ws.listenersOnlineInfoAll.push(this.logOnlineInfo);
            stompClient.connect({}, ws.onConnected, ws.onError);
        } else {
            console.log("Don't open websocket connection: user is not authenticated");
        }
    },

    onConnected: function () {
        console.log("On connected event started");
        connectedToWs = true;
        stompClient.subscribe('/topic/online', ws.onTopicOnlineReceived);
        stompClient.subscribe('/user/queue/invites', ws.onInviteReceived);
        ws.onConnectedListeners.forEach(listener => listener())
    },

    onError: function (error) {
        console.log("Could't connect through websocket: " + error);
    },

    onTopicOnlineReceived: function (data) {
        let info = JSON.parse(data.body);
        if (info.type === "online.topic.info.all") {
            ws.onlineUsers = info.onlineUsers;
            ws.listenersOnlineInfoAll.forEach(listener => listener(info.onlineUsers));
        } else if (info.type === "online.topic.info.new") {
            console.log("online.topic.info.new, id= " + info.newUser.id + ", name=" + info.newUser.username);
        }
    },

    logOnlineInfo: function(onlineUsers) {
        for (let i = 0; i < onlineUsers.length; i++) {
            let onlineUser = onlineUsers[i];
            console.log("online.topic.info.all: " + onlineUser.username);
        }
    },

    onInviteReceived: function (data) {
        let invitation = JSON.parse(data.body);
        ws.listenersInvites.forEach(listener => listener(invitation));
    },
};

ws.connect();