<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <section class="hero is-small">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 id="qtitle" class="title">
                    Result with id ${result.id}
                </h1>
            </div>
        </div>
    </section>

    <section class="hero is-small is-bold is-info">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <#if name == result.participant.username>
                    <h1 class="title">
                        <#if name == result.participant.username>You<#else>${result.participant.username}</#if>
                    </h1>
                <#else>
                    <h4 class="subtitle">
                        User
                    </h4>
                    <h1 class="title">
                        ${result.participant.username}
                    </h1>
                </#if>
                <h4 class="subtitle">
                    correctly answered
                    <p></p>
                    ${result.correctAnswersCount} / ${game.numberOfQuestions} questions.
                </h4>
            </div>
        </div>
    </section>
</@c.page>