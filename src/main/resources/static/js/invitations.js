let selectedToInvite = new Map();
const inviteJS = {

    showAvailableUsers: function () {


        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/content/available/themes",
            dataType: 'json',
            timeout: 100000,
            success: function (themes) {
                console.log("SUCCESS: ", themes);
                prototypeForm.showAvailableThemes(themes);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
            done: function (d) {
                console.log("DONE");
            }
        });
    },

    postInvite: function () {
        let invite = {
            invitedUsersIds: selectedToInvite.values()
        };

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/invite",
            data: JSON.stringify(invite),
            dataType: 'json',
            timeout: 100000,
            success: function (response) {
                console.log("Success invite");
                inviteJS.showSuccessInvite(response);
            },
            error: function (e) {
                console.log("Error " + e);
            },
            done: function (d) {
                console.log("DONE");
            }
        });
    },

    showSuccessInvite: function(response) {

    }
};