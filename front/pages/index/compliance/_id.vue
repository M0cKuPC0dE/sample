<template>
  <div id="page-wrapper">
    <div class="container-fluid">
  
      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">เพิ่มข้อกฎหมาย</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>Dashboard</li>
            <li class="active">เพิ่มข้อกฎหมาย</li>
          </ol>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="white-box">
  
            <div class="row">
              <div class="col-md-12">
                <h3 class="box-title">{{compliance.legalDuty}}</h3>
              </div>
            </div>
  
            <form class="form-horizontal">
  
              <div class="form-group">
                <label class="col-md-12">
                  <strong>หมวดหมู่</strong>
                </label>
                <div class="col-md-12">
                  {{compliance.category.name}}
                </div>
              </div>
  
              <div class="row">
                <div class="col-md-3 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>สถานะ</strong>
                    </label>
                    <div class="col-md-12">
                      <span class="label label-success">ACTIVE</span>
                    </div>
                  </div>
                </div>
  
                <div class="col-md-6 col-md-offset-3 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>วันที่มีผลบังคับใช้</strong>
                    </label>
                    <div class="col-md-12">
                      {{compliance.effectiveDate}}
                    </div>
                  </div>
                </div>
              </div>
  
              <div class="form-group">
                <label class="col-md-12">
                  <strong>ชื่อกฏหมาย</strong>
                </label>
                <div class="col-md-12">
                  {{compliance.legalName}}
                </div>
              </div>
  
              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>หน่วยงานกำกับ</strong>
                    </label>
                    <div class="col-md-12">
                      {{compliance.department}}
                    </div>
                  </div>
                </div>
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>กระทรวง</strong>
                    </label>
                    <div class="col-md-12">
                      {{compliance.ministry}}
                    </div>
                  </div>
                </div>
              </div>
  
              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>หน่วยงานที่เกี่ยวข้อง</strong>
                    </label>
                    <div class="col-md-12">
                    </div>
                  </div>
                </div>
  
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>ฝ่ายที่เกี่ยวข้อง</strong>
                    </label>
                    <div class="col-md-12">
                    </div>
                  </div>
                </div>
              </div>
  
              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>แผนกที่เกี่ยวข้อง</strong>
                    </label>
                    <div class="col-md-12">
                    </div>
                  </div>
                </div>
              </div>
  
              <div class="form-group">
                <label class="col-md-12">
                  <strong>รายละเอียดกฏหมาย</strong>
                </label>
                <div class="col-md-12">
                  {{compliance.important}}
                </div>
              </div>
  
              <div class="form-group">
                <label class="col-md-12">
                  <strong>Keyword Tags</strong>
                </label>
                <div class="col-md-12">
                </div>
              </div>
  
              <div class="form-group">
                <div class="col-md-12 m-b-10">
                  <nuxt-link :to="'/masterdata/compliance/edit/'+compliance.id" class="btn btn-block btn-warning">แก้ไขข้อกฎหมาย</nuxt-link>
                </div>
              </div>
  
            </form>
  
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  asyncData: function (context) {
    return http
      .get('/api/compliance/' + context.params.id, { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { compliance: response.data }
      })
      .catch((e) => {
        context.redirect('/login')
      })
  }
}
</script>
