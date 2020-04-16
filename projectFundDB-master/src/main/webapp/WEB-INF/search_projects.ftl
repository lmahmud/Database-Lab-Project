<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Comment" lglo=lglo!"Login"></@myheader>

<body>
    <h1 class="display-4 text-center">${title!"New Comment"}</h1>
    <main role="main" class="container">
        <div class="jumbotron shadow">
            <form method="post">
                <div class="form-group">
                    <label for="title">Title :</label>
                    <input id="title" name="title" maxlength="50" type="text" class="form-control" placeholder="Enter title">
                </div>
                <br>
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
        </div>
    </main>

    <script src="/js/textarea_autoexpand.js"></script>

</body>

</html>