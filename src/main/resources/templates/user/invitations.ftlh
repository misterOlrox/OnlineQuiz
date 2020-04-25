<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<#assign pageSpecificScripts = ["/static/js/invitationsPage.js"]
    in c
>

<@c.page>
    <section class="hero is-small">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    You invitations
                </h1>
                <h2 class="subtitle">
                    Here you can see your invitations from other users
                </h2>
            </div>
        </div>
    </section>


    <div class="box"
         style="background: linear-gradient(to top, #eb005f, #02faa8);
                margin-left: 10%;margin-right: 10%;
                margin-top: 2%"
    >
        <label class="label has-text-centered">Invitations</label>
        <table class="table is-bordered">
            <thead>
            <tr>
                <th class="has-text-centered">From</th>
                <th class="has-text-centered">Quiz name</th>
                <th class="has-text-centered">Number of questions</th>
                <th class="has-text-centered">Time for questions</th>
                <th class="has-text-centered">Action</th>
            </tr>
            </thead>

            <tbody id="invite-table-body">
                <!-- insert JS -->
            </tbody>
        </table>
    </div>
</@c.page>