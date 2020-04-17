<#import "../parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered ">
                <h1 class="title">
                    Solo quiz
                </h1>
                <h2 class="subtitle">
                    Set up your next quiz
                </h2>
            </div>
        </div>
    </section>

    <div class="container " style="width: 50%">
        <form action="/setup/solo" method="post" onsubmit="onSubmit()">

            <div class="field ">
                <label class="label" for="time">Time for one question</label>
                <div class="control has-icons-left has-icons-right">
                    <div class="select">
                        <label>
                            <select name="time">
                                <!-- For develop --><option value="0:03">0:03</option>
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

            <div class="field">
                <label class="label" for="quantity">How many questions</label>
                <div class="control has-icons-left has-icons-right">
                    <input class="input ${(quantityError??)?string('is-danger', '')}"
                           type="number"
                           id="quantity"
                           placeholder="Count of questions"
                           name="quantity"
                           value="10">
                </div>

                <#if quantityError??>
                    <p class="help is-danger">
                        ${quantityError}
                    </p>
                </#if>
            </div>

            <div class="box" style="margin-top: 20px; background-color: hsl(160, 90%, 70%)">
                <label class="label has-text-centered" for="time">Themes</label>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Theme name</th>
                        <th>Selected</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list themes as theme>
                        <tr>
                            <td>${theme.themeName}</td>
                            <td>
                                <div class="field">
                                    <label class="checkbox">
                                        <input type="checkbox" name="themeCheckbox" id="${theme.id}" checked>
                                    </label>
                                </div>
                            </td>
                        </tr>

                    </#list>
                    </tbody>
                </table>
            </div>
            <input type="hidden" id="selectedThemes" name="selectedThemes"/>
            <#if selectedThemesError??>
                <p class="help is-danger">
                    ${selectedThemesError}
                </p>
            </#if>

            <div class="container has-text-centered" style="margin-top: 5%">
                <input class="button is-link is-rounded is-medium" type="submit" value="Start"/>
            </div>
        </form>
    </div>


    <script>
        function onSubmit() {
            let checkboxes = document.getElementsByName("themeCheckbox");
            let selectedThemes = document.getElementById('selectedThemes');
            let res = [];
            for (let i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].checked) {
                    res.push(checkboxes[i].id);
                }
            }
            selectedThemes.value = res;

            return true;
        }
    </script>
</@c.page>
