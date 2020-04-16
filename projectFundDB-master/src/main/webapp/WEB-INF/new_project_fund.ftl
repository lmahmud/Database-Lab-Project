<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Comment" lglo=lglo!"Login"></@myheader>

<body>
    <h1 class="display-4 text-center">${title!"New Donation"}</h1>
    <main role="main" class="container">
        <div class="jumbotron shadow">
            <form method="post">
                <div class="form-group">
                    <label for="amount">Amount (€) :</label>
                    <input id="amount" name="amount" maxlength="10" type="number" class="form-control" value="1"
                           placeholder="Amount">
                </div>
                <div class="form-check-inline">
                    <label class="form-check-label" for="anonym">Anonymous Donations ? :</label>
                    <input type="checkbox" class="form-check-input" name="anonym" id="anonym">
                </div>
                <br>
                <button type="submit" class="btn btn-primary">Donate</button>
            </form>
        </div>
    </main>

    <script src="/js/textarea_autoexpand.js"></script>

</body>

</html>