<!DOCTYPE html>
<html lang="en">

<#include "header.ftl" />
<@myheader title="Project" lglo=lglo!"Login"></@myheader>

<body>
    <h1 class="display-4 text-center">${title!"New Project"}</h1>
    <main role="main" class="container">
        <div class="jumbotron shadow">
            <form method="post">
                <div class="form-group">
                    <label for="title">Title :</label>
                    <input id="title" name="title" maxlength="50" type="text" class="form-control" value="${(pj.title)!""}"
                        placeholder="Enter title">
                </div>
                <div class="form-group">
                    <label for="flimt">Finance Limit : €</label>
                    <input id="flimit" name="flimit" maxlength="10" type="number" class="form-control" value="${(pj.funding_limit)!""}"
                        placeholder="Amount">
                </div>
                <div class="form-group">
                    <label for="category">Category</label>
                    <select multiple class="form-control" id="category" name="category" value="${(pj.category_name)!}">
                        <#list categories as ct>
                            <option>${ct.id}-${ct.name}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group">
                    <label for="pred">Predecessor</label>
                    <select class="form-control" id="pred" name="predecessor" value="${(pj.predecessor_title)!"0-None"}">
                        <option>0-None</option>
                        <#list preds as pd>
                         <option>${pd.id}-${pd.title}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea name="description" class="form-control" id="description"
                        style="max-height: 50vh;">${(pj.description)!""}</textarea>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </main>

    <script src="/js/textarea_autoexpand.js"></script>
    <script>
        const pred_id = "${(pj.predecessor_id)!""}";
        const pred_title = "${(pj.predecessor_title)!""}";
        let pred_val = "0-None";
        if (pred_id.length !== 0 && pred_title.length !== 0) {
            pred_val = pred_id + "-" + pred_title;
        }
        document.getElementById("pred").value = pred_val;

        const cat_id = "${(pj.category_id)!""}";
        const cat_name = "${(pj.category_name)!""}";
        let cat_val = "${categories[0].id}-${categories[0].name}";
        if (cat_id.length !== 0 && cat_name.length !== 0) {
            cat_val = cat_id + "-" + cat_name;
        }
        document.getElementById("category").value = cat_val;
    </script>

</body>

</html>