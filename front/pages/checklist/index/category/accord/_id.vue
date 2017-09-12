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
        <div class="col-sm-12">
          <div class="white-box">
            <div class="row row-in">
              <div class="col-lg-6 col-sm-12 row-in-br">
                <ul class="col-in">
                  <li>
                    <span class="circle circle-md bg-success">
                      <i class="ti-clipboard"></i>
                    </span>
                  </li>
                  <li class="col-last">
                    <h3 class="counter text-right m-t-15">23</h3>
                  </li>
                  <li class="col-middle">
                    <h4>สอดคล้อง</h4>
                    <div class="progress">
                      <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                        <span class="sr-only">40% Complete (success)</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
              <div class="col-lg-6 col-sm-12 b-0">
                <ul class="col-in">
                  <li>
                    <span class="circle circle-md bg-warning">
                      <i class="ti-wallet"></i>
                    </span>
                  </li>
                  <li class="col-last">
                    <h3 class="counter text-right m-t-15">76</h3>
                  </li>
                  <li class="col-middle">
                    <h4>ไม่สอดคล้อง</h4>
                    <div class="progress">
                      <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                        <span class="sr-only">40% Complete (success)</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
              <div class="col-lg-6 col-sm-12 row-in-br  b-r-none">
                <ul class="col-in">
                  <li>
                    <span class="circle circle-md bg-danger">
                      <i class="ti-wallet"></i>
                    </span>
                  </li>
                  <li class="col-last">
                    <h3 class="counter text-right m-t-15">76</h3>
                  </li>
                  <li class="col-middle">
                    <h4>ไม่เกี่ยวข้อง</h4>
                    <div class="progress">
                      <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                        <span class="sr-only">40% Complete (success)</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
              <div class="col-lg-6 col-sm-12  b-0">
                <ul class="col-in">
                  <li>
                    <span class="circle circle-md bg-info">
                      <i class="fa fa-dollar"></i>
                    </span>
                  </li>
                  <li class="col-last">
                    <h3 class="counter text-right m-t-15">83</h3>
                  </li>
                  <li class="col-middle">
                    <h4>ยังไม่ดำเนินการ</h4>
                    <div class="progress">
                      <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                        <span class="sr-only">40% Complete (success)</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
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

            <div class="row">
              <div class="col-md-12">
                <div>
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>ชื่อกฎหมาย</th>
                        <th>หน้าที่ตามกฎหมาย</th>
                        <th>ประเภท</th>
                        <th class="text-center">ประเมิน</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr :key="accord.id" v-for="accord in category.accords">
                        <td>{{accord.legalDuty.compliance.legalName}}</td>
                        <td>{{accord.legalDuty.name}}</td>
                        <td>
                          <span class="" v-if="accord.accordType === 'LICENSE'">ใบอนุญาต</span>
                          <span class="" v-if="accord.accordType === 'EVIDENCE'">กฎหมายทั่วไป</span>
                        </td>
                        <td class="text-center">
                          <nuxt-link :to="'/checklist/category/accord/'+category.id+'/compliance/'+accord.legalDuty.id" class="btn btn-sm btn-info" data-toggle="tooltip" title="" title="ประเมิน">
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
      .get('/api/legalcategory/view/' + context.params.id, { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { category: response.data }
      })
      .catch((e) => {
        context.redirect('/checklist/login')
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
          self.$router.replace('/checklist/login')
        })
    }
  }
}
</script>
