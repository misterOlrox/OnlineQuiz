<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <section class="hero is-small">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    Approve some questions
                </h1>
                <h2 class="subtitle">
                    Approved questions can appear in random solo games
                </h2>
            </div>
        </div>
    </section>


    <div class="box"
         style="background: linear-gradient(to top, #c39feb, #02faa8);
                margin-left: 5%;margin-right: 5%;
                margin-top: 2%"
    >
        <label class="label has-text-centered">Unapproved questions</label>
        <div class="table-container">
            <table class="table is-bordered">
                <thead>
                <tr>
                    <th class="has-text-centered">Question</th>
                    <th class="has-text-centered">Correct answer</th>
                    <th class="has-text-centered">Wrong answers</th>
                    <th class="has-text-centered">Themes</th>
                    <th class="has-text-centered">Approve</th>
                </tr>
                </thead>
                <tbody>

                <#list questions as question>
                    <tr>
                        <td class="has-text-centered">${question.question}</td>
                        <td class="has-text-centered">${question.correctAnswer}</td>
                        <td class="has-text-centered">
                            [<#list question.wrongAnswers as wrongAnswer>
                            "${wrongAnswer.value}"<#sep>,
                            </#list>]
                        </td>
                        <td class="has-text-centered">
                            [<#list question.themes as theme>
                            "${theme.themeName}"<#sep>,
                            </#list>]
                        </td>
                        <td class="has-text-centered">
                            <a href="/moderator/question-approve/${question.id?c}" class="button is-small is-info">Approve</a>
                        </td>
                    </tr>
                <#else>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</@c.page>