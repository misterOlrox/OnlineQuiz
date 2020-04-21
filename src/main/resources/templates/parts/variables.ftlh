<#include "security.ftlh">
<script>
    var appContext = {
        username : "<#if user??>${user.getUsername()?json_string}<#else></#if>",
        gameId : <#if gameId??>${gameId}<#else>null</#if>,
        numberOfQuestions: <#if numberOfQuestions??>${numberOfQuestions}<#else>null</#if>
    }
</script>