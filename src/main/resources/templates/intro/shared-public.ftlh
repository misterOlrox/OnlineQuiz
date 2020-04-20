<#import "../parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Shared quiz
                </h1>
                <h2 class="subtitle">
                    Here you can choose quiz created by others to play
                </h2>
            </div>
        </div>
    </section>

    <div class="box"
         style="background: linear-gradient(to top, #ff4b00, #02ffab);
                margin-left: 10%;margin-right: 10%;
                margin-top: 2%"
    >
        <label class="label has-text-centered">Quiz Prototypes</label>
        <table class="table is-bordered">
            <thead>
            <tr>
                <th class="has-text-centered">Name</th>
                <th class="has-text-centered">Author</th>
                <th class="has-text-centered">Number of questions</th>
                <th class="has-text-centered">Time for question</th>
                <th class="has-text-centered">Play</th>
            </tr>
            </thead>
            <tbody>

            <#if prototypes??>
                <#list prototypes as prototype>
                    <tr>
                        <td class="has-text-centered">${prototype.name}</td>
                        <td class="has-text-centered">${prototype.creator.username}</td>
                        <td class="has-text-centered">${prototype.numberOfQuestions}</td>
                        <td class="has-text-centered">${prototype.timeForQuestionInSeconds} seconds</td>
                        <td class="has-text-centered">
                            <a href="/play/public-shared/${prototype.id}" class="button is-success">
                                <i class="fas fa-play" style="margin-right: 10px"></i>
                                Play
                            </a>
                        </td>
                    </tr>
                <#else>
                    There isn't any prototypes
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>