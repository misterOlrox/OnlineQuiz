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
        <label class="label has-text-centered">Quiz Prototypes</label>
        <table class="table is-bordered">
            <thead>
            <tr>
                <th class="has-text-centered">Prototype id</th>
                <th class="has-text-centered">Name</th>
                <th class="has-text-centered">Number of questions</th>
                <th class="has-text-centered">Time for question</th>
                <th class="has-text-centered">Is private?</th>
            </tr>
            </thead>
            <tbody>

            <#if prototypes??>
                <#list prototypes as prototype>
                    <tr>
                        <td class="has-text-centered">${prototype.id}</td>
                        <td class="has-text-centered">${prototype.name}</td>
                        <td class="has-text-centered">${prototype.numberOfQuestions}</td>
                        <td class="has-text-centered">${prototype.timeForQuestionInSeconds} seconds</td>
                        <td class="has-text-centered">
                            <#assign type = prototype.type>
                            <#if type == "SOLO_SHARED_PRIVATE">
                                Yes
                            <#elseif type == "SOLO_SHARED_PUBLIC">
                                No
                            <#else>
                                Unknown
                            </#if>
                        </td>
                    </tr>
                <#else>
                    You haven't any prototypes
                </#list>
            </#if>
            </tbody>
        </table>
    </div>

    <div class="container has-text-centered" style="margin-top: 5%">
        <a href="/add-prototype" class="button is-primary is-rounded is-medium">Add new</a>
    </div>
</@c.page>