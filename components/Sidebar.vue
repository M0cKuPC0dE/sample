<template>
  <div class="navbar-default sidebar" role="navigation">
    <div class="sidebar-nav" style="overflow: hidden; width: auto; height: 100%;">
      <div class="sidebar-head">
        <h3>
          <span class="fa-fw open-close">
            <i class="ti-close ti-menu"></i>
          </span>
          <span class="hide-menu">Navigation</span>
        </h3>
      </div>
      <ul class="nav" id="side-menu">
        <li>
          <nuxt-link to="/masterdata" class="waves-effect">
            <i class="zmdi zmdi-archive fa-fw"></i>
            <span class="hide-menu">Master Data</span>
          </nuxt-link>
        </li>
        <li>
          <a href="widgets.html" class="waves-effect">
            <i class="zmdi zmdi-male-alt fa-fw"></i>
            <span class="hide-menu">Administrator</span>
          </a>
        </li>
        <li>
          <a href="widgets.html" class="waves-effect">
            <i class="zmdi zmdi-accounts-alt fa-fw"></i>
            <span class="hide-menu">Coordinator</span>
          </a>
        </li>
        <li>
          <a href="widgets.html" class="waves-effect">
            <i class="zmdi zmdi-account-circle fa-fw"></i>
            <span class="hide-menu">Owner</span>
          </a>
        </li>
        <li>
          <a href="widgets.html" class="waves-effect">
            <i class="zmdi zmdi-account-box-o fa-fw"></i>
            <span class="hide-menu">Reviewer</span>
          </a>
        </li>
        <li>
          <a href="widgets.html" class="waves-effect">
            <i class="zmdi zmdi-mood fa-fw"></i>
            <span class="hide-menu">Approver</span>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>
