window.onload = function () {
    notifications.close();
    notifications.enabled = false;
};

let tableBody = document.getElementById("invite-table-body");

let invitationsPage = {
    sub: function () {
        stompClient.subscribe('/user/queue/invites/all', invitationsPage.onInvitesAllReceived);
    },

    getCurrentInvites: function () {
        console.log("Trying to get all current invites for user " + username);
        stompClient.send("/queue/invites/all", {}, {});
    },

    onInvitesAllReceived: function (data) {
        console.log("All invites received for invitations page");
        let invites = JSON.parse(data.body);
        invites.forEach(invite => invitationsPage.renderOneInvite(invite));
    },

    renderOneInvite: function (invite) {
        let row = document.createElement('tr');
        row.id = 'invite-' + invite.id;
        row.innerHTML = `
                        <td class="has-text-centered">
                            <a href="/user/profile/${invite.sender.id}">
                                ${invite.sender.username}
                            </a>
                        </td>
                        <td class="has-text-centered">${invite.gamePrototype.name}</td>
                        <td class="has-text-centered">${invite.gamePrototype.numberOfQuestions}</td>
                        <td class="has-text-centered">${invite.gamePrototype.timeForQuestionsInSeconds} seconds</td>
                        <td class="has-text-centered">
                            <a class="button is-success" style="margin: 5px">Accept</a>
                            <a 
                                class="button is-danger"
                                style="margin: 5px" 
                                onclick="invitationsPage.decline(${invite.id})">
                                    Decline
                            </a>
                        </td>
                `;

        tableBody.append(row);
    },

    decline: function (inviteId) {
        stompClient.send("/queue/invite/" + inviteId + "/decline");
        document.getElementById('invite-' + inviteId).remove();
        console.log("Declined invite");
    }
};

ws.onConnectedListeners.push(invitationsPage.sub);
ws.onConnectedListeners.push(invitationsPage.getCurrentInvites);
ws.listenersInvites.push(invitationsPage.renderOneInvite);