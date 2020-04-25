'use strict';

let notificationBox = document.getElementById("notification-box");


class Notification {
    constructor(name) {
        this.name = name;
    }
}

let notifications = {
    enabled: true,

    show: function () {
        let seen = localStorage.getItem("invites-seen");
        if (seen === 'false') {
            notificationBox.hidden = false;
        } else if (seen === 'true') {
            notificationBox.hidden = true;
        }
    },

    onNewInvite: function (invite) {
        console.log("On new invite");
        if (notifications.enabled) {
            notificationBox.hidden = false;
            localStorage.setItem("invites-seen", 'false');
        }
    },

    close: function () {
        notificationBox.hidden = true;
        localStorage.setItem("invites-seen", 'true');
    }

};


notifications.show();
ws.listenersInvites.push(notifications.onNewInvite);