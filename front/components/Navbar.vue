<template>
  <nav class="navbar navbar-default navbar-static-top m-b-0">
    <div class="navbar-header">
      <div class="top-left-part">
        <!-- Logo -->
        <nuxt-link class="logo" to="/">
          <!-- Logo icon image, you can use font-icon also -->
          <b>
            <!--This is light logo icon-->
            <img src="~assets/images/logo.png" alt="home" class="light-logo">
          </b>
        </nuxt-link>
      </div>
      <!-- /Logo -->
      <!-- Search input and Toggle icon -->
      <ul class="nav navbar-top-links navbar-left">
        <li>
          <a href="javascript:void(0)" class="open-close waves-effect waves-light">
            <i class="ti-menu"></i>
          </a>
        </li>
        <!-- .Task dropdown -->
        <li class="dropdown" v-if="false">
          <a class="dropdown-toggle waves-effect waves-light" data-toggle="dropdown" href="#">
            <i class="zmdi zmdi-notifications"></i>
            <div class="notify">
              <span class="heartbit"></span>
              <span class="point"></span>
            </div>
          </a>
          <ul class="dropdown-menu dropdown-tasks animated bounceInDown">
            <li>
              <a href="#">
                <div>
                  <p>
                    <strong>Task 1</strong>
                    <span class="pull-right text-muted">40% Complete</span>
                  </p>
                  <div class="progress progress-striped active">
                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                      <span class="sr-only">40% Complete (success)</span>
                    </div>
                  </div>
                </div>
              </a>
            </li>
            <li class="divider"></li>
            <li>
              <a href="#">
                <div>
                  <p>
                    <strong>Task 2</strong>
                    <span class="pull-right text-muted">20% Complete</span>
                  </p>
                  <div class="progress progress-striped active">
                    <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 20%">
                      <span class="sr-only">20% Complete</span>
                    </div>
                  </div>
                </div>
              </a>
            </li>
            <li class="divider"></li>
            <li>
              <a href="#">
                <div>
                  <p>
                    <strong>Task 3</strong>
                    <span class="pull-right text-muted">60% Complete</span>
                  </p>
                  <div class="progress progress-striped active">
                    <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
                      <span class="sr-only">60% Complete (warning)</span>
                    </div>
                  </div>
                </div>
              </a>
            </li>
            <li class="divider"></li>
            <li>
              <a href="#">
                <div>
                  <p>
                    <strong>Task 4</strong>
                    <span class="pull-right text-muted">80% Complete</span>
                  </p>
                  <div class="progress progress-striped active">
                    <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 80%">
                      <span class="sr-only">80% Complete (danger)</span>
                    </div>
                  </div>
                </div>
              </a>
            </li>
            <li class="divider"></li>
            <li>
              <a class="text-center" href="#">
                <strong>See All Tasks</strong>
                <i class="fa fa-angle-right"></i>
              </a>
            </li>
          </ul>
        </li>
        <li class="dropdown">
          <a class="dropdown-toggle waves-effect waves-light" data-toggle="dropdown" href="#">
            <strong>{{this.selected}}</strong>
          </a>
          <ul class="dropdown-menu dropdown animated bounceInDown">
            <li :key="index" v-for="(langcode,index) in locales" v-on:click="setLanguage(langcode)">
              <a href="#">
                <div>
                  <p>
                    <strong>{{langcode}}</strong>
                  </p>
                </div>
              </a>
            </li>
          </ul>
        </li>
      </ul>
      <ul class="nav navbar-top-links navbar-right pull-right active">
        <li class="in" v-if="false">
          <form role="search" class="app-search hidden-sm hidden-xs m-r-10">
            <input type="text" placeholder="Search..." class="form-control">
            <a href="" class="active">
              <i class="fa fa-search"></i>
            </a>
          </form>
        </li>
        <li class="dropdown">
          <a class="dropdown-toggle profile-pic" data-toggle="dropdown" href="#">
            <img src="~assets/images/user.jpg" alt="user-img" width="36" class="img-circle">
            <b class="hidden-xs">ณัฐจักร์</b>
            <span class="caret"></span>
          </a>
          <ul class="dropdown-menu dropdown-user animated flipInY">
            <li>
              <div class="dw-user-box">
                <div class="u-img">
                  <img src="~assets/images/user.jpg" alt="user">
                </div>
                <div class="u-text">
                  <h4>ณัฐจักร์ พูพัฒน์ศิริกร</h4>
                  <p class="text-muted">Administrator</p>
                </div>
              </div>
            </li>
            <li role="separator" class="divider"></li>
            <li>
              <a href="#" v-on:click="logout(this)">
                <i class="fa fa-power-off"></i> Logout</a>
            </li>
          </ul>
          <!-- /.dropdown-user -->
        </li>
        <!-- /.dropdown -->
      </ul>
    </div>
    <!-- /.navbar-header -->
    <!-- /.navbar-top-links -->
    <!-- /.navbar-static-side -->
  </nav>
</template>

<script>
/* global $ */
import { mapGetters } from 'vuex'
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  name: 'navbar',
  data: function () {
    return { selected: this.$store.state.locale }
  },
  mounted: function () {
    this.init()
    $('.open-close').on('click', function () {
      $('body').toggleClass('show-sidebar').toggleClass('hide-sidebar')
      $('.sidebar-head .open-close i').toggleClass('ti-menu')
    })
  },
  methods: {
    init: function () {
      http
        .get('/api/locales', { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          let data = response.data
          data.forEach((locale) => {
            this.localeMessage(locale.code)
            if (locale.code !== 'TH') {
              this.$store.state.locales.push(locale.code)
            }
          })
        }).catch((e) => {
          this.$router.replace('/login')
        })
    },
    localeMessage: function (code) {
      http
        .get('/api/locales/' + code, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          let data = response.data
          this.$i18n.setLocaleMessage(code, data)
          if (this.$store.state.locale === code) {
            this.$i18n.locale = code
          }
        }).catch((e) => {
          this.$router.replace('/login')
        })
    },
    setLanguage: function (val) {
      this.selected = val
      this.$i18n.locale = val
      this.$store.dispatch('changeLanguage', val)
    },
    logout: function () {
      this.$store.dispatch('auth/logout', this)
    }
  },
  computed: mapGetters({
    authenticated: 'auth/authenticated',
    name: 'auth/name',
    authority: 'auth/authority',
    locale: 'locale',
    locales: 'locales'
  })
}

</script>
