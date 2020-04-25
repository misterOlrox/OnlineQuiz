<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <title>Online Quiz</title>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <#include "variables.ftlh">

        <link rel="stylesheet" href="/webjars/bulma/0.8.2/css/bulma.min.css">
        <script defer src="/webjars/font-awesome/5.13.0/js/all.js"></script>
        <script src="/webjars/jquery/3.1.1-1/jquery.js"></script>
        <script src="/webjars/sockjs-client/1.0.2/sockjs.min.js"></script>
        <script src="/webjars/stomp-websocket/2.3.3/stomp.js"></script>
    </head>
    <body>

    <#include "navbar.ftlh">

    <div class="bd-content" style="margin-bottom: 4%; margin-top: 3%">
        <div class="content">
            <#nested>
        </div>
    </div>

    <#include "notification-box.ftlh">

    <script src="/static/js/ws.js"></script>
    <script src="/static/js/notifications.js"></script>
    <#if pageSpecificScripts??>
        <#list pageSpecificScripts as script>
            <script src=${script}></script>
        </#list>
    </#if>
    </body>
    </html>
</#macro>