<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Profile" lglo=lglo!"Login"></@myheader>

<body>
    <main role="main" class="container">
        <h3 class="display-4 text-center">${(user.name)!""}</h3>
        <h5 class="text-center">${(user.email)!""}</h5>
        <p class="text-center">${(user.description)!""}</p>
        <#if own_profile!false >
        <h6 class="text-center text-info">Balance : ${(user.account_balance)!""} €</h6>
        </#if>
        <br>

        <div class="jumbotron shadow">
            <h4 class="text-left">Created Projects</h4>
            <div class="container-fluid">
                <div class="row justify-content-start">

                    <#list cpjs as pj>
                    <div class="col-sm mb-2">
                        <div class="row justify-content-center">
                            <div class="mcard card" style="width: 10rem;">
                                <img src="/${pj.icon}" alt="img" class="card-img-top">
                                <div class="card-body">
                                    <h6 class="card-title">${pj.title}</h6>
                                    <div class="card-text">Status : ${pj.status}</div>
                                    <a href="/view_project?id=${pj.id}" class="btn btn-block btn-primary">View</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    </#list>

                </div>
            </div>
        </div>

        <div class="jumbotron shadow">
            <h4 class="text-left">Donated Projects </h4>(Anonymous donations are not shown.)
            
            <div class="container-fluid">
                <div class="row justify-content-start">

                    <#list donpjs as dp>
                        <#if dp.donation.visibility>
                            <#assign pj=dp.project>
                    <div class="col-sm mb-2">
                        <div class="row justify-content-center">
                            <div class="mcard card" style="width: 10rem;">
                                <img src="/${pj.icon}" alt="img" class="card-img-top">
                                <div class="card-body">
                                    <h6 class="card-title">${pj.title}</h6>
                                    <div class="card-text">Status : ${pj.status}</div>
                                    <div class="card-text">Donated : ${dp.donation.amount} €</div>
                                    <a href="/view_project?id=${pj.id}" class="btn btn-block btn-primary">View</a>
                                </div>
                            </div>
                        </div>
                    </div>
                        </#if>
                    </#list>

                </div>
            </div>


        </div>
    </main>

</body>

</html>