<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Project" lglo=lglo!"Login"></@myheader>

<body>
    <h3 class="display-4 text-center">Project Info</h3>
    <main role="main" class="container">
        <div class="jumbotron shadow">
            <div class="container ">
                <div class="row justify-content-center">
                    <div class="col text-center">
                        <div class="row justify-content-center">
                            <img src="/${pj.icon}" class="shadow" width="100px">
                        </div><br>
                        <h4>${pj.title}</h4>
                        <p class="lead">By <a href="/view_profile?email=${pj.creator_email}">${pj.creator_name}</a></p>
                        <p>${pj.description}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="progress border border-dark">
                            <div id="pgb" style="width: 1%" class="progress-bar
                            progress-bar-striped progress-bar-animated bg-success" 
                            role="progressbar"><a id="pgr">0</a></div>
                          </div>
                        <b>Finance Limit : </b> <a id="flimit">${pj.funding_limit}</a> €<br>
                        <b>Currently funded : </b> <a id="curr">${pj.currently_funded}</a> €<br>
                        <b>Status : </b> ${pj.status} <br>
                        <b>Category : </b> ${pj.category_name} <br>
                        <b>Predecessor : </b> <a href="/view_project?id=${pj.predecessor_id!""}">${pj.predecessor_title!""}</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow mb-4 bg-light">
            <div class="card-body justify-content-between">
                <div class="container" style="text-align: center;">
                    <a href="/new_project_fund?id=${pj.id}" class="ml-2 btn btn-success <#if pj.status!="offen">disabled</#if>">Donate</a>
                    <#if own_project!false>
                        <a href="/delete_project?id=${pj.id}" class="ml-2 btn btn-danger">Delete Project</a>
                        <a href="/edit_project?id=${pj.id}" class="ml-2 btn btn-info">Edit Project</a>
                    </#if>

                </div>
            </div>
        </div>


        <div class="jumbotron shadow">
            <div class="container-fluid">
                <h4 class="text-left">List of donations</h4>
                <ul class="list-group">
                    <#list donations as ds>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <#if (ds.visibility)!false> ${ds.donor_name} <#else>  <i>Anonymous</i> </#if>
                            <span class="badge badge-primary badge-pill" style="font-size: medium">${ds.amount} €</span>
                        </li>
                    </#list>
                </ul>
            </div>
        </div>

        <div class="jumbotron shadow">
            <a href="/new_comment?id=${pj.id}" style="float: right" class="ml-2 btn btn-info <#if pj.status!="offen">disabled</#if>">Comment</a>
            <h4 class="text-left">Comments</h4>
            <ul class="list-group">
                <#list comments as cm>
                <li class="list-group-item">
                    <i><#if (cm.visibility)!false>${cm.commenter_name} <#else> Anonymous</#if>: </i>${cm.text}</li>
                </#list>
            </ul>
        </div>

    </main>

    <script>
        const flimit = parseFloat(document.getElementById("flimit").innerText);
        const curr = parseFloat(document.getElementById("curr").innerText);
        const percent = Math.round(curr*100/flimit).toString() + "%";
        document.getElementById("pgr").innerText = percent;
        document.getElementById("pgb").style.width = percent;
    </script>
</body>

</html>