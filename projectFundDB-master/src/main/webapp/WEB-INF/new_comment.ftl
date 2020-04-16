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
                    <label for="text">Description</label>
                    <textarea name="text" class="form-control" id="text"
                        style="max-height: 50vh;">${(pj.description)!""}</textarea>
                </div>
                <div class="form-check-inline">
                    <label class="form-check-label" for="anonym">Anonymous Comment ? :</label>
                    <input type="checkbox" class="form-check-input" name="anonym" id="anonym">
                </div>
                <br>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </main>

    <script src="/js/textarea_autoexpand.js"></script>

</body>

</html>