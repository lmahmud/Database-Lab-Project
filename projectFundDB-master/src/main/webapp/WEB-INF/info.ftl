<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title=pageTitle!"Error" lglo=lglo!"Login"></@myheader>

<body>
    <main role="main" class="container">
        <div class="jumbotron shadow border border-info" style="text-align: center;">
            <h1>${pageTitle!"Error"}</h1>
            <p class="lead">${msg!""}</p>
            <a class="btn btn-lg btn-primary" href="/" role="button">Go Home »</a>
        </div>
    </main>


    <script>
        setTimeout(function() {
            window.location.replace("${redirect_url!"/"}");
        }, ${time!1500});
    </script>

</body>

</html>