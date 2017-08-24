<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-4"></div>
      <div class="col-md-4 m-b-10">
        <nuxt-link to="/masterdata/category/add" class="btn btn-block btn-info">เพิ่มหมวดหมู่</nuxt-link>
      </div>
      <div class="col-md-4 m-b-10">
        <nuxt-link to="/masterdata/compliance/add" class="btn btn-block btn-info">เพิ่มข้อกฎหมาย</nuxt-link>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 m-b-10 p-0">
        <div class="form-group">
          <div class="input-group bootstrap-touchspin">
            <input type="text" class="form-control" style="display: block;">
            <span class="input-group-btn">
              <button class="btn btn-default btn-outline" type="button">
                <i class="fa fa-search"></i>
              </button>
            </span>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>หน้าที่ตามกฎหมาย</th>
              <th class="text-center">สถานะ</th>
              <th class="text-center">จัดการ</th>
            </tr>
          </thead>
          <tbody>
            <tr :key="index" v-for="(compliance,index) in compliances">
              <td>{{compliance.legalDuty}}</td>
              <td class="text-center">
                <span class="label label-danger">Expire</span>
              </td>
              <td class="text-center">
                <nuxt-link :to="'/compliance/'+compliance.id" class="text-inverse p-r-10" title="" data-toggle="tooltip" title="เปิด">
                  <i class="ti-search"></i>
                </nuxt-link>
                <a href="javascript:void(0)" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="แก้ไข">
                  <i class="ti-marker-alt"></i>
                </a>
                <a href="javascript:void(0)" class="text-inverse" title="" data-toggle="tooltip" title="ลบ">
                  <i class="ti-trash"></i>
                </a>
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
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
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
  mounted: function () {
    $('[data-toggle="tooltip"]').tooltip()
  }
}
</script>
