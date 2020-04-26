<#import "../parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    User '${user.username}'
                </h1>
                <h2 class="subtitle">
                    Roles editing
                </h2>
            </div>
        </div>
    </section>

    <div class="container " style="width: 50%">
        <form action="/admin/user-edit/" method="post">
            <div class="box" style="margin-top: 20px; background-color: hsl(171, 100%, 41%)">
                <label class="label has-text-centered">${user.username}'s roles</label>
                <div class="table-container">
                    <table class="table">
                        <thead>
                        <tr>
                            <th class="has-text-right" style="width: 70%">Role</th>
                            <th class="has-text-centered" style="width: 30%">Enabled</th>
                        </tr>
                        </thead>
                        <tbody>

                        <#list roles as role>
                            <tr>
                                <td class="has-text-right">
                                    ${role}
                                </td>
                                <td class="has-text-centered">
                                    <div class="field">
                                        <label class="checkbox">
                                            <input type="checkbox"
                                                   name="${role}"
                                                    ${user.roles?seq_contains(role)?string("checked", "")}
                                            >
                                        </label>
                                    </div>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
            <#if success??>
                <h4 class="help is-success has-text-centered">
                    Success!
                </h4>
            </#if>

            <input type="hidden" value="${user.id}" name="userId">

            <div class="container has-text-centered" style="margin-top: 5%">
                <a href="/admin/user-list" class="button is-active is-rounded is-medium" style="margin: 2%">Go back</a>
                <input class="button is-link is-rounded is-medium" type="submit" value="Submit" style="margin: 2%"/>
            </div>
        </form>
    </div>
</@c.page>