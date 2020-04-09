<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <section class="hero is-small is-info" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Welcome to online quiz
                </h1>
                <h3 class="subtitle">
                    <#if user??>
                        Hello, ${user.getUsername()}!
                    <#else>
                        Hello, guest!
                    </#if>
                </h3>
            </div>
        </div>
    </section>
</@c.page>