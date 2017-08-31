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
            <span class="hide-menu">{{ $t('menu.masterdata') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/group" class="waves-effect">
            <i class="zmdi zmdi-group fa-fw"></i>
            <span class="hide-menu">{{ $t('menu.group') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/category" class="waves-effect">
            <i class="zmdi zmdi-ungroup fa-fw"></i>
            <span class="hide-menu">จัดหมวดหมู่กฎหมาย</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/category/list" class="waves-effect">
            <i class="zmdi zmdi-collection-bookmark fa-fw"></i>
            <span class="hide-menu">หมวดหมู่กฎหมาย</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/accord" class="waves-effect">
            <i class="zmdi zmdi-check-all fa-fw"></i>
            <span class="hide-menu">ประเมินความสอดคล้อง</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/approve" class="waves-effect">
            <i class="zmdi zmdi-check-circle-u fa-fw"></i>
            <span class="hide-menu">อนุมัติ</span>
          </nuxt-link>
        </li>
        <li v-if="false">
          <nuxt-link to="/coordinator" class="waves-effect">
            <i class="zmdi zmdi-accounts-alt fa-fw"></i>
            <span class="hide-menu">{{ $t('menu.coordinator') }}</span>
          </nuxt-link>
        </li>
        <li v-if="false">
          <nuxt-link to="/owner" class="waves-effect">
            <i class="zmdi zmdi-account-circle fa-fw"></i>
            <span class="hide-menu">{{ $t('menu.owner') }}</span>
          </nuxt-link>
        </li>
        <li v-if="false">
          <nuxt-link to="/reviewer" class="waves-effect">
            <i class="zmdi zmdi-account-box-o fa-fw"></i>
            <span class="hide-menu">{{ $t('menu.reviewer') }}</span>
          </nuxt-link>
        </li>
        <li v-if="false">
          <nuxt-link to="/approver" class="waves-effect">
            <i class="zmdi zmdi-mood fa-fw"></i>
            <span class="hide-menu">{{ $t('menu.approver') }}</span>
          </nuxt-link>
        </li>
        <li v-if="false">
          <nuxt-link to="/uploaddocument" class="waves-effect">
            <i class="zmdi zmdi-swap-vertical fa-fw"></i>
            <span class="hide-menu">{{ $t('menu.fileupload') }}</span>
          </nuxt-link>
        </li>
        <li>
          <nuxt-link to="/language" class="waves-effect">
            <i class="zmdi zmdi-text-format fa-fw"></i>
            <span class="hide-menu">{{ $t('menu.languageupload') }}</span>
          </nuxt-link>
        </li>
        <li v-if="false">
          <nuxt-link to="/compliance/add" class="waves-effect">
            <i class="zmdi zmdi-archive fa-fw"></i>
            <span class="hide-menu">Compliance Data</span>
          </nuxt-link>
        </li>
      </ul>
    </div>
  </div>
</template>
