<#import "parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold  is-danger" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Error
                </h1>
                <h2 class="subtitle">
                    <#if errorDescription??>
                        ${errorDescription}
                    <#else>
                        Unknown error
                    </#if>
                </h2>
            </div>
        </div>
    </section>

    <div class="container has-text-centered" style="margin-top: 5%">
        <a href="/" class="button is-link is-rounded is-medium" type="submit">Go Home</a>
    </div>
</@c.page>