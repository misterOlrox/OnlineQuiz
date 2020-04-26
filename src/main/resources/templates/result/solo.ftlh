<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <section class="hero is-small">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 id="qtitle" class="title">
                    Result with id ${finishedGame.id}
                </h1>
            </div>
        </div>
    </section>

    <section class="hero is-small is-bold is-info">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <#if name == finishedGame.participant.username>
                    <h1 class="title">
                        <#if name == finishedGame.participant.username>You<#else>${finishedGame.participant.username}</#if>
                    </h1>
                <#else>
                    <h4 class="subtitle">
                        User
                    </h4>
                    <h1 class="title">
                        ${finishedGame.participant.username}
                    </h1>
                </#if>
                <h4 class="subtitle">
                    correctly answered
                    <p></p>
                    ${finishedGame.correctAnswersCount} / ${prototype.numberOfQuestions} questions.
                </h4>
                <h6 class="subtitle">
                    Game started on ${finishedGame.getCreationTimeString()}
                    <p></p>
                    And finished on ${finishedGame.getFinishTimeString()}
                </h6>
            </div>
        </div>
    </section>

    <div class="box" style="background-color: hsl(160, 90%, 70%);margin-left: 10%;margin-right: 10%;margin-top: 2%">
        <label class="label has-text-centered">Answers</label>
        <div class="table-container">
            <table class="table is-bordered">
                <thead>
                <tr>
                    <th>Question</th>
                    <th>Your answer</th>
                    <th>Correct answer</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>

                <#list userAnswerList as userAnswer>
                    <tr>
                        <td>${userAnswer.quizQuestion.question}</td>
                        <td class="has-text-centered"><#if userAnswer.answer?has_content>${userAnswer.answer}<#else><i
                                    class="fa fa-times" aria-hidden="true"></i></#if></td>
                        <td class="has-text-centered">${userAnswer.quizQuestion.correctAnswer}</td>
                        <td class="has-text-centered">
                            <#assign state = userAnswer.status.name()>
                            <span class="tag is-medium
                                <#if state == "CORRECT">
                                is-success
                                <#elseif state == "TIMEOUT">
                                is-warning
                                <#else>
                                is-danger
                                </#if>
                                is-light"
                            >
                            ${userAnswer.status}
                        </span>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</@c.page>