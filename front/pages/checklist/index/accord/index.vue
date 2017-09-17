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
                    <h3 class="counter text-right m-t-15">{{progress.accord}}</h3>
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
                    <h3 class="counter text-right m-t-15">{{progress.notAccord}}</h3>
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
                    <h3 class="counter text-right m-t-15">{{progress.notConcern}}</h3>
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
                    <h3 class="counter text-right m-t-15">{{progress.inprogress}}</h3>
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
                <div :key="index" v-for="(category,index) in categories">
                  <strong :key="coordinator.id" v-for="(coordinator,coIndex) in category.legalGroup.coordinates">
                    {{coIndex===0?'Coordinator ':''}} {{coordinator.nameTh + ' '}}
                  </strong>
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th class="col-md-2">ชื่อกฎหมาย</th>
                        <th>หน้าที่ตามกฎหมาย</th>
                        <th>ประเภท</th>
                        <th class="text-center">Process</th>
                        <th class="col-md-2 text-center">สถานะ</th>
                        <th class="text-center">ประเมิน</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr :key="accord.id" v-for="(accord,acIndex) in category.accords">
                        <td>{{acIndex+1}}</td>
                        <td>{{accord.legalDuty.compliance.legalName}}</td>
                        <td>
                          <nuxt-link :to="'/checklist/accord/'+category.id+'/compliance/'+accord.legalDuty.id">{{accord.legalDuty.name}}</nuxt-link>
                        </td>
                        <td>
                          <span class="" v-if="accord.legalDuty.legalType === 'LICENSE'">ใบอนุญาต</span>
                          <span class="" v-if="accord.legalDuty.legalType === 'EVIDENCE'">กฎหมายทั่วไป</span>
                        </td>
                        <td class="text-center">{{calculatePosition(accord)}}</td>
                        <td class="text-center">
                          <span class="label label-success" v-if="accord.accorded === 'ACCORDED'">สอดคล้อง</span>
                          <span class="label label-danger" v-if="accord.accorded === 'NOT_ACCORDED'">ไม่สอดคล้อง</span>
                          <span class="label label-info" v-if="accord.accorded === 'NOT_CONCERN'">ไม่เกี่ยวข้อง</span>
                          <span class="label label-primary" v-if="!accord.accorded">ยังไม่ดำเนินการ</span>
                        </td>
                        <td class="text-center">
                          <nuxt-link :to="'/checklist/accord/'+category.id+'/compliance/'+accord.legalDuty.id" class="btn btn-sm btn-info" data-toggle="tooltip" title="" title="ประเมิน">
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
      .get('/api/legalcategory/list', { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { categories: response.data }
      })
      .catch((e) => {
        context.redirect('/checklist/login')
      })
  },
  mounted: function () {
    $('[data-toggle="tooltip"]').tooltip()
    this.calculateProgress()
    this.calculatePosition()
  },
  data: function () {
    return {
      progress: {},
      position: ''
    }
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
    },
    calculateProgress: function () {
      var data = {
        accord: 0,
        notAccord: 0,
        notConcern: 0,
        inprogress: 0
      }
      this.categories.forEach(function (category) {
        category.accords.forEach(function (accord) {
          if (accord.accorded === 'ACCORDED') {
            data.accord = data.accord + 1
          } else if (accord.accorded === 'NOT_ACCORDED') {
            data.notAccord = data.notAccord + 1
          } else if (accord.accorded === 'NOT_CONCERN') {
            data.notConcern = data.notConcern + 1
          } else {
            data.inprogress = data.inprogress + 1
          }
        })
      })
      this.$set(this, 'progress', data)
    },
    calculatePosition: function (accord) {
      if (!accord) return
      var data = 'Completed'
      if (accord.accept === null && accord.accorded === null && accord.approve === null) {
        data = 'Owner'
      } else if (accord.accept === null && accord.accorded !== null && (accord.approve === null || accord.approve === false)) {
        data = 'Coordinator'
      } else if (accord.accept !== null && accord.accept !== false && accord.accorded !== null && accord.approve === null) {
        data = 'Approver'
      } else if (accord.accept === false) {
        data = 'Owner'
      }
      return data
    }
  }
}
</script>
