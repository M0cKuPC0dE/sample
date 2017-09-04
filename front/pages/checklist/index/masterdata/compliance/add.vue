<template>
  <form class="form-horizontal" v-on:submit.prevent="onSave">
    <div class="form-group">
      <label class="col-md-12">
        <span class="help">{{ $t('compliance.category') }}</span>
      </label>
      <div class="col-md-12">
        <input type="text" class="form-control readonly" :value="category.name" required>
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
      <div class="col-md-12">
        <div class="input-group">
          <input type="text" class="form-control" id="publicdate" v-model="compliance.publicDate" required></input>
          <span class="input-group-addon">
            <i class="glyphicon glyphicon-calendar"></i>
          </span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-12">{{ $t('compliance.effectivedate') }}</label>
      <div class="col-md-12">
        <div class="input-group">
          <input type="text" class="form-control" id="effectivedate" v-model="compliance.effectiveDate" required></input>
          <span class="input-group-addon">
            <i class="glyphicon glyphicon-calendar"></i>
          </span>
        </div>
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
        <input type="text" class="form-control" id="tags" placeholder="เพิ่มแท็ก"></input>
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
    <div class="form-group">
      <div class="col-md-12">
        <div class="table-responsive">
          <table class="table">
            <thead>
              <tr>
                <th>หน้าที่ตามกฎหมาย</th>
                <th class="text-center">จัดการ</th>
              </tr>
            </thead>
            <tbody>
              <tr :key="index" v-for="(legalduty,index) in compliance.legalDuties">
                <td>{{legalduty.name}}</td>
                <td class="text-center">
                  <a href="javascript:void(0)" v-on:click="onConfirmDelete('legalduty',index)" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="ลบ">
                    <i class="ti-trash"></i>
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-12">{{ $t('compliance.legalduty') }}</label>
      <div class="col-sm-12">
        <textarea class="form-control" rows="5" v-model="legalDuty"></textarea>
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-12 text-center">
        <button type="button" v-on:click="addLegalDuty" class="btn btn-info btn-file">
          <i class="fa fa-arrow-up"></i>
          เพิ่มหน้าที่ตามกฎหมาย
        </button>
      </div>
    </div>
    <button type="submit" class="btn btn-success waves-effect waves-light m-r-10">บันทึก</button>
    <nuxt-link to="/checklist/masterdata" class="btn btn-info">
      <i class="fa fa-chevron-left"></i> ย้อนกลับ
    </nuxt-link>

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
            <button type="button" class="btn btn-danger waves-effect waves-light" v-on:click="onDelete">ลบ</button>
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<script>
/* global $ */
import { mapGetters } from 'vuex'
import http from '~/utils/http'
import cookie from '~/utils/cookie'
import ProgressUpload from '~/components/ProgressUpload'

export default {
  props: ['selected', 'categories'],
  components: {
    ProgressUpload
  },
  data: function () {
    return {
      files: {},
      category: { id: 'null' },
      legalDuty: '',
      deleteIndex: {},
      deleteType: '',
      compliance: {
        legalName: '',
        year: '',
        publicDate: '',
        effectiveDate: '',
        department: '',
        ministry: '',
        important: '',
        legalDuties: [],
        tags: '',
        legalFiles: [],
        category: {
          id: 'null'
        }
      }
    }
  },
  watch: {
    selected: function (val) {
      this.$set(this, 'category', val)
    }
  },
  computed: mapGetters({
    initCategory: 'category/category'
  }),
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
    this.$set(this, 'category', this.initCategory)
    $('#tags').tagsinput()
    $('#publicdate')
      .datepicker({ language: 'th', thaiyear: true, format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
      .on('changeDate', () => { this.compliance.publicDate = $('#publicdate').val() })
    $('#effectivedate')
      .datepicker({ language: 'th', thaiyear: true, format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
      .on('changeDate', () => { this.compliance.effectiveDate = $('#effectivedate').val() })

    $('.readonly').on('keydown paste', function (e) {
      e.preventDefault()
    })
  },
  methods: {
    onSave: function () {
      var self = this
      self.compliance.category = this.category
      self.compliance.tags = $('#tags').val()
      http.post('/api/compliance', self.compliance, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/checklist/masterdata' })
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
    onConfirmDelete: function (type, index) {
      $('#masterdata-add-modal').modal('show')
      this.$set(this, 'deleteIndex', index)
      this.$set(this, 'deleteType', type)
    },
    onDelete: function () {
      $('#masterdata-add-modal').modal('hide')
      if (this.deleteType === 'file') {
        this.compliance.legalFiles.splice(this.deleteIndex, 1)
      } else {
        this.compliance.legalDuties.splice(this.deleteIndex, 1)
      }
    }
  }
}
</script>
