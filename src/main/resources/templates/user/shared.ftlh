<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <section class="hero is-small">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    Shared quiz
                </h1>
                <h2 class="subtitle">
                    Here you can see quiz you created
                </h2>
            </div>
        </div>
    </section>


    <div class="box"
         style="background: linear-gradient(to top, #ffe267, #02faa8);
                margin-left: 10%;margin-right: 10%;
                margin-top: 2%"
    >
        <label class="label has-text-centered">Answers</label>
        <table class="table is-bordered">
            <thead>
            <tr>
                <th class="has-text-centered">Game id</th>
                <th class="has-text-centered">Start date</th>
                <th class="has-text-centered">Finish date</th>
                <th class="has-text-centered">Correct answers</th>
                <th class="has-text-centered">Time for question</th>
                <th class="has-text-centered">State</th>
            </tr>
            </thead>
            <tbody>

            <#if games??>
                <#list games as game>
                    <tr>
                        <td class="has-text-centered">${game.id}</td>
                        <td class="has-text-centered">${game.getCreationTimeString()}</td>
                        <td class="has-text-centered">${game.getFinishTimeString()}</td>
                        <td class="has-text-centered">${game.correctAnswersCount}
                            / ${game.prototype.numberOfQuestions}</td>
                        <td class="has-text-centered">${game.prototype.timeForQuestionInSeconds} seconds</td>
                        <td class="has-text-centered">
                            <#assign state = game.status>
                            <#if state == "FINISHED">
                                Game is finished
                                <p></p>
                                <a href="/result/solo/${game.id}" class="button is-link">Results</a>
                            <#elseif state == "IN_PROGRESS">
                                Game is in progress
                                <p></p>
                                <a href="/play/solo/${game.id}" class="button is-warning">Continue</a>
                            <#elseif state == "INTERRUPTED">
                                Game was interupted
                            <#else>
                                Unknown state
                            </#if>
                        </td>
                    </tr>
                <#else>
                    You haven't any games
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>