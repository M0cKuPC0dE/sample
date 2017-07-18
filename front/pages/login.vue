<template>
  <section id="wrapper" class="new-login-register">
    <div class="lg-info-panel">
      <div class="inner-panel"></div>
    </div>
    <div class="new-login-box">
      <div class="white-box">
        <h3 class="box-title m-b-0">Sign In to Admin</h3>
        <small>Enter your details below</small>
        <form class="form-horizontal new-lg-form" role="form" v-on:submit.prevent="onSubmit" autocomplete="off">
          <div id="errmsg" class="alert alert-danger alert-light alert-dismissible" role="alert" v-if="error != ''">
            <button type="button" class="close" aria-label="Close" v-on:click="closeAlert()">
              <i class="zmdi zmdi-close"></i>
            </button>
            <strong>
              <i class="zmdi zmdi-close-circle"></i> Error!
            </strong>
            {{error}}
          </div>
  
          <div class="form-group  m-t-20">
            <div class="col-xs-12">
              <label>Email Address</label>
              <input class="form-control" type="text" required="" placeholder="Username" v-model="credentials.username">
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12">
              <label>Password</label>
              <input class="form-control" type="password" required="" placeholder="Password" v-model="credentials.password">
            </div>
          </div>
          <div class="form-group text-center m-t-20">
            <div class="col-xs-12">
              <button class="btn btn-info btn-lg btn-block btn-rounded text-uppercase waves-effect waves-light" type="submit">
                Log In
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>

<script>
/* global $ */
import { mapGetters } from 'vuex'

export default {
  name: 'login',
  data: function () {
    return {
      credentials: {
        username: '',
        password: ''
      }
    }
  },

  methods: {
    onSubmit: function () {
      if (process.BROWSER_BUILD) {
        $(document).ready(function () {
          $('.alert').show()
          if (!$('.alert').is(':visible')) {
            $('button[type="submit"]').prop('disabled', true)
          }
        })
      }
      this.$store.dispatch('auth/login', this)
    },
    closeAlert: function () {
      $('.alert').hide()
      this.$store.dispatch('auth/clearerror', this)
    }
  },
  watch: {
    error: function () {
      $('button[type="submit"]').prop('disabled', false)
    }
  },
  computed: mapGetters({
    error: 'auth/error'
  })
}
</script>

<style>
.new-login-register {
  position: fixed;
  height: 100%;
}

.new-login-register .lg-info-panel {
  background: url(~assets/images/login-register.jpg) center center/cover no-repeat!important;
  width: 500px;
  height: 100%;
  position: fixed;
}

.new-login-register .new-login-box {
  margin-left: 50%;
  margin-top: 10%;
  width: 400px;
}

.white-box {
  background: #fff;
  padding: 25px;
  margin-bottom: 30px;
}

@media (max-width: 1350px) {
  .new-login-register .lg-info-panel {
    width: 450px;
  }
  .new-login-register .new-login-box {
    margin-left: 500px;
  }
}
</style>

