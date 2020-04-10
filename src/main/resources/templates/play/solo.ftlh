<#import "../parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    Solo quiz
                </h1>
                <h2 class="subtitle">
                    Your game id: ${gameId}
                </h2>
            </div>
        </div>
    </section>

    <div class="container has-text-centered" style="margin-top: 5%">
        <a href="/play/solo/${gameId}" class="button is-link is-rounded is-medium">Answer</a>
    </div>
</@c.page>