<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <title>Online Quiz</title>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
        <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
    </head>
    <body>
    <#include "navbar.ftlh">
    <div class="bd-content" style="margin-bottom: 4%; margin-top: 6%">
        <div class="content">
            <#nested>
        </div>
    </div>
    </body>
    </html>
</#macro>