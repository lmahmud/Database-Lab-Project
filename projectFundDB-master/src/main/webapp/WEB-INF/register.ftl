<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Register" lglo=lglo!"Login"></@myheader>

<body>
    <main role="main" class="container">
        <div class="jumbotron shadow">
            <h1 class="display-4">Register</h1>
            <form>
                <div class="form-group">
                    <label for="email">Email address</label>
                    <input id="email" name="email" maxlength="50" type="email" class="form-control"
                        placeholder="Enter email">
                </div>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input id="name" name="name" maxlength="50" type="text" class="form-control"
                           placeholder="Enter Name">
                </div>
                <div class="form-group">
                    <label for="password">Password/Secret Code <small class="text-muted">
                            (max: 20characters)</small></label>
                    <input id="password" name="pass" maxlength="10" type="password" class="form-control"
                        placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <input id="description" name="description" maxlength="50" type="text" class="form-control"
                           placeholder="description">
                </div>
                <div class="form-group">
                    <label for="balance">Initial account balance (Temp function) €</label>
                    <input id="balance" name="balance" maxlength="10" type="number" class="form-control"
                        placeholder="1000">
                </div>
                <button type="submit" class="btn btn-primary">Register</button>
            </form>
        </div>
    </main>
</body>

</html>