<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <section class="hero is-small ${(signUpSuccessful?? || loggedOutMsg??)?string('is-success', '')}"
             style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Add theme
                </h1>
                <h2 class="subtitle">
                    Here you can add new theme
                </h2>
            </div>
        </div>
    </section>

    <div id="container" class="container" style="width: 50%">
        <form id="form" action="/add-theme" method="post">

            <div class="field">
                <label class="label" for="themeName">Theme name</label>
                <div class="control">
                    <input type="text"
                           name="themeName"
                           id="themeName"
                           class="input is-info"
                           autofocus="autofocus"
                           placeholder="Theme name">
                </div>
            </div>
            <#if themeError??>
                <p class="help is-danger">
                    ${themeError}
                </p>
            </#if>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Add theme"/>
            </div>
            <#if success??>
                <p class="help is-success">
                    ${success}
                </p>
            </#if>
        </form>
    </div>
</@c.page>

