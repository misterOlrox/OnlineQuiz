<#assign
    know = Session.SPRING_SECURITY_CONTEXT??
>

<#if know>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isAdmin = user.isAdmin()
        isModerator = user.isModerator()
        currentUserId = user.getId()
    >

    <#else>
        <#assign
            name = "unknown"
            isAdmin = false
            currentUserId = -1
            isModerator = false
        >
</#if>