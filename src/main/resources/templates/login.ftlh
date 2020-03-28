<#import "parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small ${(signUpSuccessful?? || loggedOutMsg??)?string('is-success', '')}" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Login
                </h1>
                <h2 class="subtitle">
                    <#if signUpSuccessful??>
                        You are successfully registered. Enter your username and password
                    <#elseif loggedOutMsg??>
                        You are logged out
                    <#else>
                        Enter your username and password
                    </#if>
                </h2>
            </div>
        </div>
    </section>

    <div class="container" style="width: 50%">
        <form action="/login" method="post">

            <div class="field">
                <label class="label" for="username">Username</label>
                <div class="control has-icons-left has-icons-right">
                    <input type="text" id="username" name="username" autofocus="autofocus"
                           class="input ${(loginError??)?string('is-danger', '')}"
                           placeholder="Username">
                    <span class="icon is-small is-left">
                        <i class="fas fa-user"></i>
                    </span>
                </div>
            </div>

            <div class="field">
                <label class="label" for="password">Password</label>
                <div class="control has-icons-left has-icons-right">
                    <input class="input ${(loginError??)?string('is-danger', '')}" type="password" id="password"
                           name="password" placeholder="Password">
                    <span class="icon is-small is-left">
                        <i class="fas fa-key"></i>
                    </span>
                </div>

                <#if loginError??>
                    <p class="help is-danger">
                        ${loginError}
                    </p>
                </#if>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Log in"/>
            </div>
        </form>
    </div>
</@c.page>

