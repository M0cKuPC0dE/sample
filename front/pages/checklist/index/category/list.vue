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
                <h3 class="box-title">หมวดหมู่กฎหมาย</h3>
              </div>
            </div>

            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th class="col-xs-5">ฝ่าย</th>
                        <th class="col-xs-5">แผนก</th>
                        <th class="text-center">จัดการ</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr :key="index" v-for="(category,index) in categories">
                        <td>{{category.party}}</td>
                        <td>{{category.department}}</td>
                        <td class="text-center">
                          <nuxt-link :to="'/checklist/category/edit/'+category.id" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="แก้ไข">
                            <i class="ti-marker-alt"></i>
                          </nuxt-link>
                          <nuxt-link :to="'/checklist/category/copy/'+category.id" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="คัดลอก">
                            <i class="ti-stamp"></i>
                          </nuxt-link>
                          <a href="javascript:void(0)" v-on:click="onConfirmDelete(category)" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="ลบ">
                            <i class="ti-trash"></i>
                          </a>
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

    <div id="category-remove-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
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
  data: function () {
    return {
      deleteCategory: {}
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
          self.$router.replace('/login')
        })
    },
    onDelete: function () {
      var self = this
      $('#category-remove-modal').modal('hide')
      return http
        .delete('/api/legalcategory/' + this.deleteCategory.id, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.onLoad(self.selected)
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    },
    onConfirmDelete: function (category) {
      $('#category-remove-modal').modal('show')
      this.$set(this, 'deleteCategory', category)
    }
  }
}
</script>
