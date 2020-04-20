<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>

    <#if profileOwner.equals(user)>
        <#assign yourProfile = true>
    </#if>

    <section class="hero is-small is-link">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    ${profileOwner.username}
                </h1>
                <h2 class="subtitle">
                    <#if yourProfile??>
                        It is your stats
                    <#else>
                        It is ${profileOwner.username}'s stats
                    </#if>
                </h2>
            </div>
        </div>
    </section>


    <div class="content" style="margin: 5%">
        <div class="bd-example">
            <div class="content is-medium">
                <h3>Quiz played: ${quizPlayed}</h3>
                <h3>Quiz created: ${quizCreated}</h3>
                <h3>Questions added: ${questionsAdded}</h3>
                <h3>Correct answers count: ${correctAnswers}</h3>
            </div>
        </div>
    </div>

</@c.page>