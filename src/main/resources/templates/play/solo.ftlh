<#import "../parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 id="qtitle" class="title">
                    Question
                </h1>
                <h2 id="question" class="subtitle"></h2>
            </div>
        </div>
    </section>

    <div id="answers" class="box has-text-centered" style="background-color: hsl(160, 90%, 70%)">

    </div>

    <section class="hero is-small is-bold">
        <div class="hero-body">
            <div class="container has-text-left ">
                <h3 class="title">
                    Solo quiz
                </h3>
                <h4 class="subtitle">
                    Your game id: ${gameId}
                </h4>
            </div>
        </div>
    </section>

    <script>
        window.onload = function () {
            getQuestion();
        }
    </script>
</@c.page>