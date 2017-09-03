<template>
  <div id="page-wrapper">
    <div class="container-fluid">

      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">{{ $t('menu.masterdata') }}</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>{{ $t('dashboards.title') }}</li>
            <li class="active">{{ $t('menu.masterdata') }}</li>
          </ol>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="white-box">

            <div class="row">
              <div class="col-md-4">
                <h3 class="box-title">ประเมินความสอดคล้อง</h3>
              </div>
            </div>

            <div class="row" :key="index" v-for="(category,index) in categories">
              <div class="col-md-12">
                <span>
                  <strong>ฝ่าย</strong> {{category.party}}
                  <strong>หน่วยงาน</strong> {{category.department}}
                </span>
                <div class="table-responsive">
                  <table class="table table-hover manage-u-table">
                    <thead>
                      <tr>
                        <th class="col-xs-5">ข้อกฎหมาย</th>
                        <th class="col-xs-5">ความสอดคล้อง</th>
                        <th class="text-center">จัดการ</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr :key="accord.id" v-for="accord in category.accords">
                        <td>{{accord.compliance.legalName}}</td>
                        <td>
                          <span class="label label-success" v-if="accord.accorded === 'ACCORDED'">สอดคล้อง</span>
                          <span class="label label-danger" v-if="accord.accorded === 'NOT_ACCORDED'">ไม่สอดคล้อง</span>
                          <span class="label label-info" v-if="accord.accorded === 'NOT_CONCERN'">ไม่เกี่ยวข้อง</span>
                        </td>
                        <td class="text-center">
                          <nuxt-link :to="'/checklist/accord/'+category.id+'/compliance/'+accord.compliance.id" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="ประเมิน">
                            <i class="ti-marker-alt"></i>
                          </nuxt-link>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script>
/* global $ */
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  asyncData: function (context) {
    return http
      .get('/api/legalcategory', { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { categories: response.data }
      })
      .catch((e) => {
        context.redirect('/login')
      })
  },
  mounted: function () {
    $('[data-toggle="tooltip"]').tooltip()
  },
  methods: {
    onLoad: function () {
      var self = this
      http
        .get('/api/legalcategory', { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$set(self, 'categories', response.data)
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    }
  }
}
</script>
