<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-4 m-b-10">
        <nuxt-link to="/masterdata/category/edit" class="btn btn-block btn-warning">{{ $t('buttons.category.edit') }}</nuxt-link>
      </div>
      <div class="col-md-4 m-b-10">
        <nuxt-link to="/masterdata/category/add" class="btn btn-block btn-info">{{ $t('buttons.category.add') }}</nuxt-link>
      </div>
      <div class="col-md-4 m-b-10">
        <nuxt-link to="/masterdata/compliance/add" class="btn btn-block btn-info">เ{{ $t('buttons.compliance.add') }}</nuxt-link>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 m-b-10 p-0">
        <form v-on:submit.prevent="onSearch">
          <div class="form-group">
            <div class="input-group bootstrap-touchspin">
              <input type="text" class="form-control" style="display: block;" v-model="search">
              <span class="input-group-btn">
                <button class="btn btn-default btn-outline" type="submit">
                  <i class="fa fa-search"></i>
                </button>
              </span>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div class="row">
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>{{ $t('compliance.legalduty') }}</th>
              <th class="text-center">{{ $t('compliance.status') }}</th>
              <th class="text-center">{{ $t('compliance.management') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr :key="index" v-for="(compliance,index) in compliances">
              <td>{{compliance.legalDuty}}</td>
              <td class="text-center">
                <span class="label label-success">ACTIVE</span>
              </td>
              <td class="text-center">
                <nuxt-link :to="'/compliance/'+compliance.id" class="text-inverse p-r-10" title="" data-toggle="tooltip" title="เปิด">
                  <i class="ti-search"></i>
                </nuxt-link>
                <nuxt-link :to="'/masterdata/compliance/edit/'+compliance.id" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="แก้ไข">
                  <i class="ti-marker-alt"></i>
                </nuxt-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</template>

<script>
/* global $ */
import { mapGetters } from 'vuex'
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  props: ['selected'],
  asyncData: function (context) {
    return http
      .get('/api/compliance', { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { compliances: response.data }
      })
      .catch((e) => {
        context.redirect('/login')
      })
  },
  data: function () {
    return {
      search: ''
    }
  },
  watch: {
    selected: function (val) {
      var self = this
      var url = val.id === 'null' ? '/api/compliance' : '/api/compliance/category/' + val.id
      return http
        .get(url, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          this.$set(this, 'compliances', response.data)
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    }
  },
  computed: mapGetters({
    initCategory: 'category/category'
  }),
  mounted: function () {
    $('[data-toggle="tooltip"]').tooltip()

    var self = this
    var url = this.initCategory.id && this.initCategory.id !== 'null' ? '/api/compliance/category/' + this.initCategory.id : '/api/compliance'
    return http
      .get(url, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
      .then((response) => {
        this.$set(this, 'compliances', response.data)
      })
      .catch((e) => {
        self.$router.replace('/login')
      })
  },
  methods: {
    onSearch: function () {
      var self = this
      return http
        .post('/api/compliance/search', { search: this.search, category: this.selected.id }, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$set(this, 'compliances', response.data)
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    }
  }
}
</script>
