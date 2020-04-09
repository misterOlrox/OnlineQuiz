<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <section class="hero is-small ${(signUpSuccessful?? || loggedOutMsg??)?string('is-success', '')}"
             style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Add question
                </h1>
                <h2 class="subtitle">
                    <#if isAdmin>
                        You are admin and can add questions without waiting of approvals
                    <#else>
                        Here you can add question. Other players can play with this questions only after approvals
                    </#if>
                </h2>
            </div>
        </div>
    </section>

    <div id="container" class="container" style="width: 50%">
        <form id="form" action="/add-question" method="post" onsubmit="addWrongAnswerToModel()">

            <div class="field">
                <label class="label" for="question">Question</label>
                <div class="control">
                    <input type="text"
                           name="question"
                           id="question"
                           class="textarea is-info"
                           autofocus="autofocus"
                           placeholder="Question">
                </div>
            </div>
            <#if questionError??>
                <p class="help is-danger">
                    ${questionError}
                </p>
            </#if>

            <div class="field">
                <label class="label" for="correctAnswer">Correct Answer</label>
                <div class="control has-icons-left has-icons-right">
                    <input type="text"
                           id="correctAnswer"
                           name="correctAnswer"
                           class="input ${(correctAnswerError??)?string('is-danger', '')}"
                           placeholder="Correct answer">
                </div>
            </div>
            <#if correctError??>
                <p class="help is-danger">
                    ${correctError}
                </p>
            </#if>

            <div class="field" id="firstWrong">
                <label class="label" for="wrongAnswer1">Wrong Answer 1</label>
                <div class="control has-icons-left has-icons-right">
                    <input type="text"
                           id="wrongAnswer1"
                           name="wrongAnswer"
                           class="input ${(correctAnswerError??)?string('is-danger', '')}"
                           placeholder="Wrong answer">
                </div>
            </div>
            <#if wrongError??>
                <p class="help is-danger">
                    ${wrongError}
                </p>
            </#if>

            <div id="buttonForAddWrong" class="container has-text-centered" style="margin-top: 5%">
                <p class="button is-warning is-rounded is-medium" onclick="addWrongAnswer()">
                    Add wrong answer
                </p>
            </div>


            <input type="hidden" id="wrongAnswers" name="wrongAnswers"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Add question"/>
            </div>
            <#if success??>
                <p class="help is-success">
                    ${success}
                </p>
            </#if>
        </form>
    </div>

    <script>
        var c = 1;
        function addWrongAnswer() {
            let form = document.getElementById('form');
            let newWrong = document.createElement("div");
            c++;
            newWrong.classList.add('field');
            newWrong.innerHTML += '<label class="label" for="wrongAnswer'+c+'">Wrong Answer'+c+'</label>\n' +
                '                <div class="control has-icons-left has-icons-right">\n' +
                '                    <input type="text"\n' +
                '                           name="wrongAnswer"\n' +
                '                           autofocus="autofocus"\n' +
                '                           class="input"\n' +
                '                           placeholder="Wrong answer">\n' +
                '                </div>\n';

            form.insertBefore(newWrong, document.getElementById('buttonForAddWrong'));
            return false;
        }

        function addWrongAnswerToModel() {
            let wrongAnswerList = document.getElementsByName("wrongAnswer");
            let wrongAnswers = document.getElementById('wrongAnswers');
            let res = [];
            for (let i = 0; i < wrongAnswerList.length; i++) {
                res.push(wrongAnswerList[i].value);
            }
            wrongAnswers.value = res;

            return true;
        }
    </script>
</@c.page>

