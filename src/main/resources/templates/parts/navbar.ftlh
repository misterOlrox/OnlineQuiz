<#include "security.ftlh">
<nav class="navbar is-primary"></nav>
<nav class="navbar is-primary is-fixed-top">
    <div class="navbar-brand">
        <a class="navbar-item" href="/">
            <img src="/images/logo.jpg" alt="Logo"
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
            <#if user??>
                <div class="navbar-item has-dropdown is-hoverable">
                    <a class="navbar-link" href="/user/profile">
                        ${user.username}
                    </a>
                    <div class="navbar-dropdown is-boxed">
                        <a class="navbar-item" href="/user/profile">
                            Your profile
                        </a>
                        <a class="navbar-item" href="/user/games">
                            Your games
                        </a>
                        <a class="navbar-item" href="/user/invitations">
                            Your invitations
                        </a>
                        <a class="navbar-item" href="/user/shared">
                            Your shared quiz
                        </a>

                        <hr class="navbar-divider">
                        <a class="navbar-item" style="color: crimson" href="/logout">
                            Sign-out
                        </a>
                    </div>
                </div>
            </#if>

            <#if isAdmin>
                <div class="navbar-item has-dropdown is-hoverable">
                    <a class="navbar-link" href="/admin/user-list">
                        Admin features
                    </a>
                    <div class="navbar-dropdown is-boxed">
                        <a class="navbar-item" href="/admin/user-list">
                            Edit users
                        </a>
                    </div>
                </div>
            </#if>

            <#if isModerator>
                <div class="navbar-item has-dropdown is-hoverable">
                    <a class="navbar-link" href="/moderator/questions-approvals">
                        Moderator features
                    </a>
                    <div class="navbar-dropdown is-boxed">
                        <a class="navbar-item" href="/moderator/questions-approvals">
                            Approve questions
                        </a>
                    </div>
                </div>
            </#if>

            <div class="navbar-item has-dropdown is-hoverable">
                <a class="navbar-link">
                    Quiz
                </a>
                <div class="navbar-dropdown is-boxed">
                    <a class="navbar-item" href="/intro/solo">
                        Play random solo quiz
                    </a>
                    <a class="navbar-item" href="/intro/shared-public">
                        Play public shared quiz
                    </a>
                    <hr class="navbar-divider">
                    <a class="navbar-item" href="/add-prototype">
                        Add shared quiz
                    </a>
                </div>
            </div>

            <#if user??>
                <div class="navbar-item has-dropdown is-hoverable">
                    <a class="navbar-link" href="/add-question">
                        Questions
                    </a>
                    <div class="navbar-dropdown is-boxed">
                        <a class="navbar-item" href="/add-question">
                            Add question
                        </a>
                        <#if isAdmin || isModerator>
                            <a class="navbar-item" href="/add-theme">
                                Add theme
                            </a>
                        </#if>
                    </div>
                </div>
            </#if>

            <div class="navbar-item has-dropdown is-hoverable">
                <a class="navbar-link">
                    Ratings
                </a>
                <div class="navbar-dropdown is-boxed">
                    <a class="navbar-item" href="/rating/quiz-played">
                        Quiz played Top
                    </a>
                    <a class="navbar-item" href="/rating/quiz-created">
                        Quiz created Top
                    </a>
                    <a class="navbar-item" href="/rating/questions-added">
                        Questions added Top
                    </a>
                    <a class="navbar-item" href="/rating/correct-answers">
                        Correct answers count Top
                    </a>
                </div>
            </div>
        </div>

        <div class="navbar-end">
            <div class="navbar-item">
                <div class="field is-grouped">
                    <#if user?? && user.isActive()>
                        <form action="/logout" method="post">
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
                                Sign Up
                            </a>
                        </p>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</nav>
<script>
    document.addEventListener('DOMContentLoaded', () => {

        // Get all "navbar-burger" elements
        const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

        // Check if there are any navbar burgers
        if ($navbarBurgers.length > 0) {

            // Add a click event on each of them
            $navbarBurgers.forEach(el => {
                el.addEventListener('click', () => {

                    // Get the target from the "data-target" attribute
                    const target = el.dataset.target;
                    const $target = document.getElementById(target);

                    // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
                    el.classList.toggle('is-active');
                    $target.classList.toggle('is-active');

                });
            });
        }

    });
</script>
