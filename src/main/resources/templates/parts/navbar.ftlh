<#include "security.ftlh">
<nav class="navbar is-primary"></nav>
<nav class="navbar is-primary is-fixed-top">
    <div class="navbar-brand">
        <a class="navbar-item" href="/">
            <img src="https://github.com/misterOlrox/QuizImages/blob/master/logo.jpg?raw=true" alt="Logo"
                 width="35" height="35">
        </a>
        <div class="navbar-burger burger" data-target="navbarExampleTransparentExample">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>

    <div id="navbarExampleTransparentExample" class="navbar-menu">
        <div class="navbar-start">
            <a class="navbar-item" href="https://bulma.io/">
                Home
            </a>
            <div class="navbar-item has-dropdown is-hoverable">
                <a class="navbar-link" href="https://bulma.io/documentation/overview/start/">
                    Docs
                </a>
                <div class="navbar-dropdown is-boxed">
                    <a class="navbar-item" href="https://bulma.io/documentation/overview/start/">
                        Overview
                    </a>
                    <a class="navbar-item" href="https://bulma.io/documentation/modifiers/syntax/">
                        Modifiers
                    </a>
                    <a class="navbar-item" href="https://bulma.io/documentation/columns/basics/">
                        Columns
                    </a>
                    <a class="navbar-item" href="https://bulma.io/documentation/layout/container/">
                        Layout
                    </a>
                    <a class="navbar-item" href="https://bulma.io/documentation/form/general/">
                        Form
                    </a>
                    <hr class="navbar-divider">
                    <a class="navbar-item" href="https://bulma.io/documentation/elements/box/">
                        Elements
                    </a>
                    <a class="navbar-item is-active" href="https://bulma.io/documentation/components/breadcrumb/">
                        Components
                    </a>
                </div>
            </div>
        </div>

        <div class="navbar-end">
            <div class="navbar-item">
                <div class="field is-grouped">
                    <#if user?? && user.isActive()>
                        <form action="/logout" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <p class="control">
                                <button class="bd-tw-button button is-primary" type="submit">
                                    Sign Out
                                </button>
                            </p>
                        </form>
                    <#else>
                        <p class="control">
                            <a class="bd-tw-button button is-primary" href="/login">
                                Log In
                            </a>
                        </p>
                        <p class="control">
                            <a class="button" href="/sign-up">
                                Sing Up
                            </a>
                        </p>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</nav>
