<#import "../parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    All users
                </h1>
                <h2 class="subtitle">
                    Here you can see all users and edit their roles
                </h2>
            </div>
        </div>
    </section>

    <div class="box"
         style="background: linear-gradient(to top, #ffa100, #02ffab);
                margin-left: 10%;margin-right: 10%;
                margin-top: 2%"
    >
        <label class="label has-text-centered">Users</label>
        <table class="table is-bordered">
            <thead>
            <tr>
                <th class="has-text-centered" style="width: 40%">Name</th>
                <th class="has-text-centered" style="width: 40%">Roles</th>
                <th class="has-text-centered" style="width: 20%">Edit</th>
            </tr>
            </thead>
            <tbody>

            <#if users??>
                <#list users as user>
                    <tr>
                        <td class="has-text-centered">${user.username}</td>
                        <td class="has-text-centered">
                            <#list user.roles as role>
                                ${role}<#sep>, </#list>
                        </td>
                        <td class="has-text-centered">
                            <a class="button is-small is-info" href="/admin/user-edit/${user.id}">
                                Edit
                            </a>
                        </td>
                    </tr>
                <#else>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>