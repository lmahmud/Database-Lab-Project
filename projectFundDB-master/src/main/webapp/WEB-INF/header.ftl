<#macro myheader title="" lglo="Login">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${title}</title>

    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <#--<script src="/js/jquery-3.4.1.min.js"></script>
    <script src="/js/bootstrap.bundle.min.js"></script>-->
  </head>

  <nav class="navbar navbar-expand-lg navbar-dark fixed-top bg-dark shadow" style="background-color: #D3D3D3;">
    <a class="navbar-brand" href="/">
      <svg class="icon-logo-star" height="30px" width="30px">
        <symbol id="icon-logo-star" viewBox="0 0 362.62 388.52">
          <path
                  d="M156.58 239l-88.3 64.75c-10.59 7.06-18.84 11.77-29.43 11.77-21.19 0-38.85-18.84-38.85-40 0-17.69 14.13-30.64 27.08-36.52l103.6-44.74-103.6-45.92C13 142.46 0 129.51 0 111.85 0 90.66 18.84 73 40 73c10.6 0 17.66 3.53 28.25 11.77l88.3 64.75-11.74-104.78C141.28 20 157.76 0 181.31 0s40 18.84 36.5 43.56L206 149.52l88.3-64.75C304.93 76.53 313.17 73 323.77 73a39.2 39.2 0 0138.85 38.85c0 18.84-12.95 30.61-27.08 36.5l-103.61 45.91L335.54 239c14.13 5.88 27.08 18.83 27.08 37.67 0 21.19-18.84 38.85-40 38.85-9.42 0-17.66-4.71-28.26-11.77L206 239l11.77 104.78c3.53 24.72-12.95 44.74-36.5 44.74s-40-18.84-36.5-43.56z">
          </path>
        </symbol>
        <use href="#icon-logo-star"></use>
      </svg>
      CrowdFunding
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
          <a class="nav-link mynavlink" href="/">Home</a>
        </li>
        <li class="nav-item active">
          <a class="nav-link mynavlink" href="/myaccount">My Account</a>
        </li>
        <li class="nav-item active">
          <a class="nav-link mynavlink" href="/new_project">Create Project</a>
        </li>
        <li class="nav-item active">
          <a class="nav-link mynavlink" href="/search_projects">Search Projects</a>
        </li>
      </ul>
      <a class="btn btn-outline-light text-uppercase" href="/${lglo}">${lglo}</a>
    </div>
  </nav>
</#macro>
