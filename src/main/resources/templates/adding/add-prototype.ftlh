<#import "../parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 0%">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    Create your own quiz
                </h1>
                <h2 class="subtitle">
                    Here you can create your quiz in which other users can play
                </h2>
            </div>
        </div>
    </section>

    <div class="container " style="width: 80%">
        <form action="/add-prototype" method="post" onsubmit="prototypeForm.submitListener()">

            <div class="field">
                <label class="label" for="prototypeName">Name</label>
                <div class="control has-icons-left has-icons-right">
                    <input type="text" id="prototypeName" name="prototypeName" autofocus="autofocus" class="input"
                           placeholder="Prototype Name" <#if prototypeName??>value="${prototypeName}"</#if>>
                </div>

                <#if prototypeNameError??>
                    <p class="help is-danger">
                        ${prototypeNameError}
                    </p>
                </#if>
            </div>

            <div class="field ">
                <label class="label" for="time">Time for one question</label>
                <div class="control has-icons-left has-icons-right">
                    <div class="select">
                        <label>
                            <select name="time">
                                <option value="0:30">0:30</option>
                                <option value="1:00">1:00</option>
                                <option value="2:00">2:00</option>
                            </select>
                        </label>
                    </div>
                    <span class="icon is-small is-left">
                        <i class="fas fa-clock"></i>
                    </span>
                </div>
            </div>

            <div class="field" style="margin-top: 2%">
                <label class="label" for="isPrivate">Is private?</label>
                    <span class="icon is-small is-left">
                       <i class="fas fa-low-vision"></i>
                    </span>
                <label class="checkbox has-text-centered" for="isPrivate" style="margin-left: 10px">
                    <input type="checkbox" name="isPrivate" <#if isPrivate??>checked</#if> >
                </label>
            </div>

            <div class="field" style="margin-top: 20px;">
                <label class="label" for="time">Selected Questions</label>
                <div id="selected-questions">
                    <#-- JS insert here -->
                </div>
            </div>

            <nav class="panel">
                <p class="panel-heading has-text-centered">
                    Available themes and questions
                </p>
                <p class="panel-tabs">
                </p>

                <div id="main-panel">
                    <#-- JS insert here -->
                </div>
            </nav>

            <div id="pagination">
                <#-- JS insert here -->
            </div>

            <#if selectedQuestionsError??>
                <p class="help is-danger">
                    ${selectedQuestionsError}
                </p>
            </#if>
            <input type="hidden" id="selectedQuestions" name="selectedQuestions"/>
            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Add"/>
            </div>
        </form>
    </div>

    <script src="/static/js/prototypeForm.js"></script>
</@c.page>
