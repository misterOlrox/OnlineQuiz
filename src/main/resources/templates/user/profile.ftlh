<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <section class="hero is-small is-link">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    ${name}
                </h1>
                <h2 class="subtitle">
                    It is your profile
                </h2>
            </div>
        </div>
    </section>


    <div class="content" style="margin: 5%">
        <div class="bd-example">
            <div class="content is-medium">
                <h1>Hello World</h1>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla accumsan, metus ultrices eleifend
                    gravida,
                    nulla nunc varius lectus, nec rutrum justo nibh eu lectus. Ut vulputate semper dui. Fusce erat odio,
                    sollicitudin vel erat vel, interdum mattis neque.</p>
                <h2>Second level</h2>
                <p>Curabitur accumsan turpis pharetra <strong>augue tincidunt</strong> blandit. Quisque condimentum
                    maximus
                    mi, sit amet commodo arcu rutrum id. Proin pretium urna vel cursus venenatis. Suspendisse potenti.
                    Etiam
                    mattis sem rhoncus lacus dapibus facilisis. Donec at dignissim dui. Ut et neque nisl.</p>
                <ul>
                    <li>In fermentum leo eu lectus mollis, quis dictum mi aliquet.</li>
                    <li>Morbi eu nulla lobortis, lobortis est in, fringilla felis.</li>
                    <li>Aliquam nec felis in sapien venenatis viverra fermentum nec lectus.</li>
                    <li>Ut non enim metus.</li>
                </ul>
            </div>
        </div>
    </div>

</@c.page>