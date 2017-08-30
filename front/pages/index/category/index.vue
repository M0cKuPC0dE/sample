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
                <h3 class="box-title">จัดหมวดหมู่กฎหมาย</h3>
              </div>
            </div>

            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th class="col-xs-10">หน่วยงาน</th>
                        <th class="text-center">จัดการ</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr :key="index" v-for="(legalgroup,index) in groups">
                        <td>{{legalgroup.buName}}</td>
                        <td class="text-center">
                          <nuxt-link :to="'/category/manage/'+legalgroup.id" class="text-inverse p-r-10" data-toggle="tooltip" title="จัดหมวดหมู่">
                            <i class="ti-direction-alt"></i>
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

    <div id="group-remove-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title">ยืนยันการลบ</h4>
          </div>
          <div class="modal-body">
            ต้องการลบใช่หรือไม่
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">ปิด</button>
            <button type="button" class="btn btn-danger waves-effect waves-light" v-on:click="onDelete">ลบ</button>
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
      .get('/api/legalgroup', { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { groups: response.data }
      })
      .catch((e) => {
        context.redirect('/login')
      })
  },
  mounted: function () {
    $('[data-toggle="tooltip"]').tooltip()
  },
  data: function () {
    return {
      deleteGroup: {}
    }
  },
  methods: {
    onLoad: function () {
      var self = this
      http
        .get('/api/legalgroup', { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$set(self, 'groups', response.data)
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    },
    onDelete: function () {
      var self = this
      $('#group-remove-modal').modal('hide')
      return http
        .delete('/api/legalgroup/' + this.deleteGroup.id, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.onLoad(self.selected)
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    },
    onConfirmDelete: function (legalgroup) {
      $('#group-remove-modal').modal('show')
      this.$set(this, 'deleteGroup', legalgroup)
    }
  }
}
</script>
