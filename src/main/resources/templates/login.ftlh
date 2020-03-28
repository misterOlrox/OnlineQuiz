<#import "parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small ${(signUpSuccessful??)?string('is-success', '')}" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Login
                </h1>
                <h2 class="subtitle">
                <#if signUpSuccessful??>
                    You are successfully registered. Enter your username and password
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
                    <input type="text" id="username" name="username" autofocus="autofocus" class="input is-success"
                           placeholder="Username">
                    <span class="icon is-small is-left">
      <i class="fas fa-user"></i>
    </span>
                    <span class="icon is-small is-right">
      <i class="fas fa-check"></i>
    </span>
                </div>
                <p class="help is-success">This username is available</p>
            </div>

            <div class="field">
                <label class="label" for="password">Password</label>
                <div class="control has-icons-left has-icons-right">
                    <input class="input is-danger" type="password" id="password" name="password">
                    <span class="icon is-small is-left">
      <i class="fas fa-key"></i>
    </span>
                    <span class="icon is-small is-right">
      <i class="fas fa-exclamation-triangle"></i>
    </span>
                </div>
                <p class="help is-danger">This email is invalid</p>
            </div>

            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Log in"/>
            </div>
        </form>
    </div>
</@c.page>

