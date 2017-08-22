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
                <h3 class="box-title">เพิ่มข้อกฎหมาย</h3>
              </div>
            </div>

            <form class="form-horizontal" v-on:submit.prevent="onSave">

              <div class="row">
                <div class="col-md-3 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">สถานะ</label>
                    <div class="col-md-12"> </div>
                  </div>
                </div>

                <div class="col-md-6 col-md-offset-3 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">วันที่มีผลบังคับใช้</label>
                    <div class="col-md-12">
                      <input type="date" class="form-control" placeholder="วันที่มีผลบังคับใช้"> </div>
                  </div>
                </div>

              </div>

              <div class="form-group">
                <label class="col-md-12">ชื่อกฏหมาย</label>
                <div class="col-md-12">
                  <input type="text" class="form-control" placeholder="ชื่อกฏหมาย"> </div>
              </div>

              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">หน่วยงานกำกับ</label>
                    <div class="col-md-12">
                      <input type="text" class="form-control" placeholder="หน่วยงานกำกับ"> </div>
                  </div>
                </div>
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">กระทรวง</label>
                    <div class="col-md-12">
                      <input type="text" class="form-control" placeholder="กระทรวง"> </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">BU ที่เกี่ยวข้อง</label>
                    <div class="col-md-12">
                      <input type="text" class="form-control" placeholder="BU ที่เกี่ยวข้อง"> </div>
                  </div>
                </div>

                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">ฝ่ายที่เกี่ยวข้อง</label>
                    <div class="col-md-12">
                      <input type="text" class="form-control" placeholder="ฝ่ายที่เกี่ยวข้อง"> </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">แผนกที่เกี่ยวข้อง</label>
                    <div class="col-md-12">
                      <input type="text" class="form-control" placeholder="แผนกที่เกี่ยวข้อง"> </div>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">รายละเอียดกฏหมาย</label>
                <div class="col-md-12">
                  <textarea class="form-control" placeholder="รายละเอียดกฏหมาย" rows="5"></textarea>
                </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">Keyword Tags</label>
                <div class="col-md-12">
                  <input type="text" class="form-control" placeholder="Keyword Tags"> </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">ไฟล์แนบ</label>
                <div class="col-md-12">
                  <input required="" style="display:" type="file" accept="application/pdf" v-on:change="onBrowse('http://localhost:8080/api/localeupload/' + filename,$event)">
                </div>
              </div>

              <div class="form-actions">
                <button type="submit" class="btn btn-success">
                  <i class="fa fa-check"></i> บันทึก</button>
              </div>
            </form>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import ProgressUpload from '~components/ProgressUpload'
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  components: {
    ProgressUpload
  },
  data: function () {
    return {
      filename: '',
      error: '',
      files: {},
      objupload: {}
    }
  },
  created: function () {
    this.$on('onCompleteUpload', function (index) {
      var obj = {}
      obj[index] = undefined
      this.$set(this, 'files', obj)
    })
  },
  methods: {
    onBrowse: function (url, e) {
      var obj = {}
      obj['file'] = e.target.files[0]
      obj['url'] = url
      this.objupload = obj
    },
    onSave: function () {
      this.$set(this, 'files', this.objupload)

      var self = this
      http.post('/api/category', { parent: { id: self.parent }, name: self.name }, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/masterdata' })
        })
    }
  }
}
</script>
