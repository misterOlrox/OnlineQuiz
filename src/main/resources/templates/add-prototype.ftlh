<#import "parts/common.ftlh" as c>

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
        <form action="/setup/solo" method="post" onsubmit="onSubmit()">

            <div class="field ">
                <label class="label" for="time">Time for one question</label>
                <div class="control has-icons-left has-icons-right">
                    <div class="select">
                        <label>
                            <select name="time">
                                <!-- For develop -->
                                <option value="0:03">0:03</option>
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

            <div class="field" style="margin-top: 20px">
                <label class="label">Add questions for quiz</label>
                <p></p>
                Available themes
            </div>

            <nav class="panel">
                <p class="panel-heading has-text-centered">
                    Available themes and questions
                </p>
                <p class="panel-tabs">
                    <a id="filter-public" class="is-active">Public</a>
                    <a id="filter-private">Private</a>
                </p>

                <div id="main-panel">
                    <#-- JS insert here -->
                </div>
            </nav>

            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Start"/>
            </div>
        </form>
    </div>

    <script src="/static/js/prototypeForm.js"></script>
</@c.page>
