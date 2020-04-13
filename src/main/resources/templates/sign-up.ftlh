<#import "parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Sign Up
                </h1>
                <h2 class="subtitle">
                    Enter your username for registration
                </h2>
            </div>
        </div>
    </section>

    <div class="container" style="width: 50%">
        <form action="/sign-up" method="post">

            <div class="field">
                <label class="label" for="username">Username</label>
                <div class="control has-icons-left has-icons-right">
                    <input type="text" id="username" name="username" autofocus="autofocus" class="input"
                           placeholder="Username">
                    <span class="icon is-small is-left">
                        <i class="fas fa-user"></i>
                    </span>
                </div>

                <#if usernameError??>
                    <p class="help is-danger">
                        ${usernameError}
                    </p>
                </#if>
            </div>

            <div class="field">
                <label class="label" for="password">Password</label>
                <div class="control has-icons-left has-icons-right">
                    <input class="input ${(passwordError??)?string('is-danger', '')}" type="password" id="password"
                           name="password" placeholder="Password">
                    <span class="icon is-small is-left">
                        <i class="fas fa-key"></i>
                    </span>
                </div>

                <#if passwordError??>
                    <p class="help is-danger">
                        ${passwordError}
                    </p>
                </#if>
            </div>

            <div class="field">
                <label class="label" for="passwordRepeat">Repeat password</label>
                <div class="control has-icons-left has-icons-right">
                    <input class="input ${(passwordError??)?string('is-danger', '')}" type="password"
                           id="passwordRepeat" name="passwordRepeat" placeholder="Password">
                    <span class="icon is-small is-left">
                        <i class="fas fa-key"></i>
                    </span>
                </div>

                <#if passwordRepeatError??>
                    <p class="help is-danger">
                        ${passwordRepeatError}
                    </p>
                </#if>
            </div>

            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Sign up"/>
            </div>
        </form>
    </div>
</@c.page>