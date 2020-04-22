let modal = document.getElementById('prototype-modal');
let modalBody = document.getElementById('modal-body');

const inviteJS = {

    selectedPrototypeId: null,

    showAvailableUsers: function (prototypeId) {
        console.log("Selected prototypeId = " + prototypeId);
        inviteJS.selectedPrototypeId = prototypeId;
        modal.classList.add("is-active");

        inviteJS.updateAvailableUsers(ws.onlineUsers);
        ws.listenersOnlineInfoAll.push(inviteJS.updateAvailableUsers);
    },

    closeAvailableUsers: function () {
        inviteJS.selectedPrototypeId = null;
        modal.classList.remove("is-active");
    },

    updateAvailableUsers: function (onlineUsers) {
        modalBody.innerHTML = '';
        onlineUsers.forEach(user => {
            if (user.username === appContext.username) {
                return;
            }

            let prototypeId = inviteJS.selectedPrototypeId;

            modalBody.innerHTML += `
            <div id="invite-${prototypeId}-${user.id}" class="content is-medium">
                    <strong>${user.username}</strong>
                    <a id="button-${prototypeId}-${user.id}" class="button is-link media-right" onclick="inviteJS.postInvite(${prototypeId}, ${user.id})">Send</a>
                </div>
            `
        })
    },

    postInvite: function (prototypeId, invitedId) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/invite?prototypeId=" + prototypeId + "&invitedId=" + invitedId,
            timeout: 100000,
            success: function (response) {
                console.log("Success invite");
                inviteJS.showSuccessInvite(prototypeId, invitedId);
            },
            error: function (e) {
                console.log("Error " + e.errorMessage);
            },
            done: function (d) {
                console.log("DONE");
            }
        });
    },

    showSuccessInvite: function (prototypeId, invitedId) {
        document.getElementById("button-" + prototypeId + "-" + invitedId)
            .setAttribute("disabled", "disabled");
    }
};