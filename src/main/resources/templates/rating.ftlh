<#import "parts/common.ftlh" as c>

<@c.page>
    <section class="hero is-small is-bold" style="margin-bottom: 5%">
        <div class="hero-body">
            <div class="container has-text-centered">
                <h1 class="title">
                    Top 10 by ${statType}
                </h1>
                <h2 class="subtitle">
                    Here you can see top 10 users by ${statType}
                </h2>
            </div>
        </div>
    </section>

    <div class="box"
         style="background: linear-gradient(to top, #ff4b00, #02ffab);
                margin-left: 10%;margin-right: 10%;
                margin-top: 2%"
    >
        <label class="label has-text-centered"><h2>Top</h2></label>
        <table class="table is-bordered">
            <thead>
            <tr>
                <th class="has-text-centered" style="width: 25%">Place</th>
                <th class="has-text-centered" style="width: 50%">Name</th>
                <th class="has-text-centered" style="width: 25%">${statType}</th>
            </tr>
            </thead>
            <tbody>

            <#if userStats??>
                <#list userStats as userStat>
                    <tr>
                        <td class="has-text-centered">${userStat?counter}</td>
                        <td class="has-text-centered"><a href="/user/profile/${userStat.user.id}">${userStat.user.username}</a></td>
                        <td class="has-text-centered">${userStat.value}</td>
                    </tr>
                <#else>
                    Top is empty
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>