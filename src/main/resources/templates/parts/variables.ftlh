<#include "security.ftlh">
<script>
    var wsContext = {
        username : "<#if user??>${user.getUsername()?json_string}<#else></#if>"
    }
</script>