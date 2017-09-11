<template>
  <form class="form-horizontal" v-on:submit.prevent="onSave">
    <div class="form-group">
      <label class="col-md-12">
        <span class="help">{{ $t('compliance.category') }}</span>
      </label>
      <div class="col-md-12">
        <input type="text" class="form-control" :value="category.name" readonly required>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-12">
        <span class="help">{{ $t('compliance.legalname') }}</span>
      </label>
      <div class="col-md-12">
        <input type="text" class="form-control" v-model="compliance.legalName" required> </div>
    </div>
    <div class="form-group">
      <label class="col-md-12">{{ $t('compliance.publicdate') }}</label>
      <div class="col-md-3">
        <select class="form-control" v-model="date.publicDate" required>
          <option value="">วัน</option>
          <option :key="n" v-for="n in (1, 31)" :value="n">{{n}}</option>
        </select>
      </div>

      <div class="col-md-6">
        <select class="form-control" v-model="date.publicMonth" required>
          <option value="">เดือน</option>
          <option :key="n" v-for="(n,index) in getMonth()" :value="index+1">{{n}}</option>
        </select>
      </div>

      <div class="col-md-3">
        <select class="form-control" v-model="date.publicYear" required>
          <option value="">ปี</option>
          <option :key="n" v-for="n in getYear()" :value="n">{{n+543}}</option>
        </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-12">{{ $t('compliance.effectivedate') }}</label>
      <div class="col-md-3">
        <select class="form-control" v-model="date.effectiveDate" required>
          <option value="">วัน</option>
          <option :key="n" v-for="n in (1, 31)" :value="n">{{n}}</option>
        </select>
      </div>

      <div class="col-md-6">
        <select class="form-control" v-model="date.effectiveMonth" required>
          <option value="">เดือน</option>
          <option :key="n" v-for="(n,index) in getMonth()" :value="index+1">{{n}}</option>
        </select>
      </div>

      <div class="col-md-3">
        <select class="form-control" v-model="date.effectiveYear" required>
          <option value="">ปี</option>
          <option :key="n" v-for="n in getYear()" :value="n">{{n+543}}</option>
        </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-12">{{ $t('compliance.department') }}</label>
      <div class="col-md-12">
        <input type="text" class="form-control" v-model="compliance.department" required> </div>
    </div>
    <div class="form-group">
      <label class="col-md-12">{{ $t('compliance.ministry') }}</label>
      <div class="col-md-12">
        <input type="text" class="form-control" v-model="compliance.ministry" required> </div>
    </div>
    <div class="form-group">
      <label class="col-md-12">{{ $t('compliance.important') }}</label>
      <div class="col-md-12">
        <textarea class="form-control" rows="5" v-model="compliance.important" required></textarea>
      </div>
    </div>

    <div class="form-group">
      <label class="col-md-12">แท็ก</label>
      <div class="col-md-12">
        <input type="text" :value="compliance.tags" class="form-control" id="tags" placeholder="เพิ่มแท็ก"></input>
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-12">
        <div class="table-responsive">
          <table class="table">
            <thead>
              <tr>
                <th>ไฟล์เอกสาร</th>
                <th class="text-center">จัดการ</th>
              </tr>
            </thead>
            <tbody>
              <tr :key="file.index" v-for="(file,index) in compliance.legalFiles">
                <td>{{file.name}}</td>
                <td class="text-center">
                  <a href="javascript:void(0)" v-on:click="onConfirmDelete('file',index)" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="ลบ">
                    <i class="ti-trash"></i>
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="form-group" v-if="!files.file">
      <div class="col-md-12 text-center is-fileinput">
        <span class="btn btn-info btn-file">
          <i class="zmdi zmdi-swap-vertical"></i>
          {{ $t('buttons.upload.file') }}
          <input style="display:" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,.pdf,.ppf,.doc,.docx" v-on:change="onBrowse('http://localhost:8080/api/legalupload',$event)">
        </span>
      </div>
    </div>
    <div class="form-group" v-if="files.file">
      <div class="col-md-12">
        <progressUpload :props="files.file" :url="files.url" :fileid="$route.params.id"></progressUpload>
      </div>
    </div>

    <button type="submit" class="btn btn-success waves-effect waves-light m-r-10">บันทึก</button>
    <button type="button" data-toggle="modal" data-target="#responsive-modal" class="btn btn-danger waves-effect waves-light m-r-10">ลบข้อกฎหมาย</button>
    <nuxt-link to="/checklist/masterdata" class="btn btn-info">
      <i class="fa fa-chevron-left"></i> ย้อนกลับ</nuxt-link>

    <div id="responsive-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
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

    <div id="masterdata-add-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
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
            <button type="button" class="btn btn-danger waves-effect waves-light" v-on:click="onDeleteList">ลบ</button>
          </div>
        </div>
      </div>
    </div>

  </form>
</template>

<script>
/* global $ */
import http from '~/utils/http'
import cookie from '~/utils/cookie'
import ProgressUpload from '~/components/ProgressUpload'

export default {
  props: ['selected', 'categories'],
  components: {
    ProgressUpload
  },
  asyncData: function (context) {
    return http
      .get('/api/compliance/' + context.params.id, { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        var date = {
          publicDate: response.data.publicDate.split('/')[0].replace(/^0+/, ''),
          publicMonth: response.data.publicDate.split('/')[1].replace(/^0+/, ''),
          publicYear: response.data.publicDate.split('/')[2].replace(/^0+/, ''),
          effectiveDate: response.data.effectiveDate.split('/')[0].replace(/^0+/, ''),
          effectiveMonth: response.data.effectiveDate.split('/')[1].replace(/^0+/, ''),
          effectiveYear: response.data.effectiveDate.split('/')[2].replace(/^0+/, '')
        }
        return { compliance: response.data, date: date }
      })
      .catch((e) => {
        context.redirect('/checklist/login')
      })
  },
  data: function () {
    return {
      date: {
        publicDate: '',
        publicMonth: '',
        publicYear: '',
        effectiveDate: '',
        effectiveMonth: '',
        effectiveYear: ''
      },
      files: {},
      category: { id: 'null' },
      legalDuty: '',
      deleteIndex: {},
      deleteType: ''
    }
  },
  watch: {
    selected: function (val) {
      this.$set(this, 'category', val)
    }
  },
  created: function () {
    this.$on('onCompleteUpload', function (json) {
      var obj = {}
      obj['file'] = undefined
      this.$set(this, 'files', obj)
      this.compliance.legalFiles.push(JSON.parse(json))
      this.$set(this.compliance, 'legalFiles', this.compliance.legalFiles)
    })
  },
  mounted: function () {
    this.$set(this, 'category', this.compliance.category)
    $('#tags').tagsinput()
    $('#publicdate')
      .datepicker({ language: 'th', format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
      .on('changeDate', () => { this.compliance.publicDate = $('#publicdate').val() })
    $('#effectivedate')
      .datepicker({ language: 'th', format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
      .on('changeDate', () => { this.compliance.effectiveDate = $('#effectivedate').val() })
  },
  methods: {
    onSave: function () {
      var self = this
      self.compliance.publicDate = self.date.publicDate + '/' + self.date.publicMonth + '/' + self.date.publicYear
      self.compliance.effectiveDate = self.date.effectiveDate + '/' + self.date.effectiveMonth + '/' + self.date.effectiveYear
      self.compliance.category = this.category
      http.post('/api/compliance', self.compliance, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/checklist/masterdata' })
        })
        .catch((e) => {
          self.$router.replace('/checklist/login')
        })
    },
    onBrowse: function (url, e) {
      var obj = {}
      obj['file'] = e.target.files[0]
      obj['url'] = url
      this.$set(this, 'files', obj)
    },
    addLegalDuty: function () {
      if (this.legalDuty === '') return
      this.compliance.legalDuties.push({ name: this.legalDuty })
      this.$set(this.compliance, 'legalDuties', this.compliance.legalDuties)
      this.$set(this, 'legalDuty', '')
    },
    onDelete: function () {
      var self = this
      $('#responsive-modal').modal('hide')
      return http
        .delete('/api/compliance/' + this.$route.params.id, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$router.push('/checklist/masterdata')
        })
        .catch((e) => {
          self.$router.replace('/checklist/login')
        })
    },
    onConfirmDelete: function (type, index) {
      $('#masterdata-add-modal').modal('show')
      this.$set(this, 'deleteIndex', index)
      this.$set(this, 'deleteType', type)
    },
    onDeleteList: function () {
      $('#masterdata-add-modal').modal('hide')
      if (this.deleteType === 'file') {
        this.compliance.legalFiles.splice(this.deleteIndex, 1)
      } else {
        this.compliance.legalDuties.splice(this.deleteIndex, 1)
      }
    },
    getYear: function () {
      var year = []
      for (var i = 1900; i < new Date().getFullYear() + 2; i++) {
        year.push(i)
      }
      return year
    },
    getMonth: function () {
      return ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม']
    }
  }
}
</script>
