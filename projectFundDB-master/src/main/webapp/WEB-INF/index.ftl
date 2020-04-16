<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Home" lglo=lglo!"Login"></@myheader>

<body>
  <div class="container-fluid" >
    <div class="row justify-content-start">
        <#list projects as sp>
      <div class="col-sm-2 mb-2">
        <div class="row justify-content-center">
          <div class="mcard card" style="width: 17rem;">
            <img src="/${sp.icon}" alt="img" class="card-img-top">
            <div class="card-body" style="text-align: center">
              <h4 class="card-title">${sp.title}</h4>
              <h6 class="card-subtitle">By <a href="/view_profile?email=${sp.creator_email}">${sp.creator_name}</a></h6>
              <h6 class="card-body">Status: ${sp.status}<br>Current: ${sp.currently_funded} €</h6>
              <a href="/view_project?id=${sp.id}" class="btn btn-block btn-primary ">View</a>
            </div>
          </div>
        </div>
      </div>
      </#list>

    </div>

  </div>

</body>

</html>