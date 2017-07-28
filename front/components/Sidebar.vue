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
            <span class="hide-menu">{{ $t('masterdatas.title') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/administrator" class="waves-effect">
            <i class="zmdi zmdi-male-alt fa-fw"></i>
            <span class="hide-menu">{{ $t('administrators.title') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/coordinator" class="waves-effect">
            <i class="zmdi zmdi-accounts-alt fa-fw"></i>
            <span class="hide-menu">{{ $t('coordinators.title') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/owner" class="waves-effect">
            <i class="zmdi zmdi-account-circle fa-fw"></i>
            <span class="hide-menu">{{ $t('owners.title') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/reviewer" class="waves-effect">
            <i class="zmdi zmdi-account-box-o fa-fw"></i>
            <span class="hide-menu">{{ $t('reviewers.title') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/approver" class="waves-effect">
            <i class="zmdi zmdi-mood fa-fw"></i>
            <span class="hide-menu">{{ $t('approvers.title') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/import" class="waves-effect">
            <i class="zmdi zmdi-swap-vertical fa-fw"></i>
            <span class="hide-menu">{{ $t('imports.title') }}</span>
          </nuxt-link>
        </li>
      </ul>
    </div>
  </div>
</template>
