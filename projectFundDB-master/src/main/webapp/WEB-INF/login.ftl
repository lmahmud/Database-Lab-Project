<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Login" lglo=""></@myheader>

<body>
    <main role="main" class="container">
        <div class="jumbotron shadow">
            <h1 class="display-4">Login</h1>
            <form action="/login" method="post">
                <div class="form-group">
                    <label for="email">Email address</label>
                    <input id="email" name="user" maxlength="50" type="email" class="form-control"
                        placeholder="Enter email">
                </div>
                <div class="form-group">
                    <label for="password">Password/Secret Code <small class="text-muted">
                            (max: 10 characters)</small></label>
                    <input id="password" name="pwd" maxlength="10" type="password" class="form-control"
                        placeholder="Password">
                </div>
                <button type="submit" class="btn btn-primary">Log in</button>
            </form>
            <br><br>
            <p class="lead">Or</p><br>
            <a href="/register" class="btn btn-outline-primary">Register</a>
            <br><br>
            <div><p>Dummy Account. <br> Email address = dummy@dummy.com
                <br>password = 123456</p></div>
        </div>
    </main>
</body>

</html>