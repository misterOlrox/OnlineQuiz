<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <section class="hero is-small">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    Here you can see your games
                </h1>
                <h2 class="subtitle">
                    You have ${games?size} games
                </h2>
            </div>
        </div>
    </section>


    <div class="box"
         style="background: linear-gradient(to top, #4ccceb, #02faa8);
                margin-left: 10%;margin-right: 10%;
                margin-top: 2%"
    >
        <label class="label has-text-centered">Answers</label>
        <div class="table-container">
            <table class="table is-bordered">
                <thead>
                <tr>
                    <th class="has-text-centered">Game id</th>
                    <th class="has-text-centered">Quiz name</th>
                    <th class="has-text-centered">Start date</th>
                    <th class="has-text-centered">Finish date</th>
                    <th class="has-text-centered">Correct answers</th>
                    <th class="has-text-centered">Time for question</th>
                    <th class="has-text-centered">State</th>
                </tr>
                </thead>
                <tbody>

                <#list games as game>
                    <tr>
                        <td class="has-text-centered">${game.id}</td>
                        <td class="has-text-centered">${game.prototype.name}</td>
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
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</@c.page>