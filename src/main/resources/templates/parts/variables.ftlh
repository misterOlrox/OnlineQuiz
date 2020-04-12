<#include "security.ftlh">
<script>
    var wsContext = {
        username : "<#if user??>${user.getUsername()?json_string}<#else></#if>",
        gameId : <#if gameId??>${gameId}<#else>-1</#if>
    }
</script>