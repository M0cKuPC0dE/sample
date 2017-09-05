<template>
  <div id="page-wrapper">
    <div class="container-fluid">

      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">{{ $t('compliance.legalname') }}</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>หน้าหลัก</li>
            <li class="active">{{ $t('compliance.legalname') }}</li>
          </ol>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="white-box">

            <div class="row">
              <div class="col-md-12">
                <h3 class="box-title">{{accord.legalDuty.name}}</h3>
              </div>
            </div>

            <form class="form-horizontal" v-on:submit.prevent="onSave">

              <div class="form-group">

                <div class="col-md-12 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>{{ $t('compliance.effectivedate') }}</strong>
                    </label>
                    <div class="col-md-12">
                      {{accord.legalDuty.compliance.effectiveDate}}
                    </div>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">
                  <strong>{{ $t('compliance.legalname') }}</strong>
                </label>
                <div class="col-md-12">
                  {{accord.legalDuty.compliance.legalName}}
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>{{ $t('compliance.department') }}</strong>
                    </label>
                    <div class="col-md-12">
                      {{accord.legalDuty.compliance.department}}
                    </div>
                  </div>
                </div>
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>{{ $t('compliance.ministry') }}</strong>
                    </label>
                    <div class="col-md-12">
                      {{accord.legalDuty.compliance.ministry}}
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
                  <strong>{{ $t('compliance.important') }}</strong>
                </label>
                <div class="col-md-12">
                  {{accord.legalDuty.compliance.important}}
                </div>
              </div>

              <div class="row">
                <div class="col-md-6">
                  <strong>ประเมินความสอดคล้อง</strong>
                  <div class="radio radio-success">
                    <input type="radio" name="radio" id="radio1" value="ACCORDED" v-model="accord.accorded" required>
                    <label for="radio1"> สอดคล้อง </label>
                  </div>
                  <div class="radio radio-danger">
                    <input type="radio" name="radio" id="radio2" value="NOT_ACCORDED" v-model="accord.accorded" required>
                    <label for="radio2"> ไม่สอดคล้อง </label>
                  </div>
                  <div class="radio">
                    <input type="radio" name="radio" id="radio3" value="NOT_CONCERN" v-model="accord.accorded" required>
                    <label for="radio3"> ไม่เกี่ยวข้อง </label>
                  </div>
                </div>
                <div class="col-md-6">
                  <strong>หมายเหตุ</strong>
                  <div>
                    <textarea class="form-control" rows="3" v-model="accord.remark"></textarea>
                  </div>
                </div>
              </div>

              <div class="form-group m-t-20">
                <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table">
                      <thead>
                        <tr>
                          <th>วันแจ้งเตือน</th>
                          <th>วันหมดอายุ</th>
                          <th>ใบอนุญาติ</th>
                          <th class="text-center">จัดการ</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr :key="file.index" v-for="(file,index) in accord.licenseFiles">
                          <td class="col-md-3">
                            <div class="input-group">
                              <input type="text" class="form-control" :id="'warningDate-'+index" v-model="file.warningDate" required></input>
                              <span class="input-group-addon">
                                <i class="glyphicon glyphicon-calendar"></i>
                              </span>
                            </div>
                          </td>
                          <td class="col-md-3">
                            <div class="input-group">
                              <input type="text" class="form-control" :id="'expireDate-'+index" v-model="file.expireDate" required></input>
                              <span class="input-group-addon">
                                <i class="glyphicon glyphicon-calendar"></i>
                              </span>
                            </div>
                          </td>
                          </td>
                          <td style="vertical-align: middle;">{{file.name}}</td>
                          <td style="vertical-align: middle;" class="text-center">
                            <a href="javascript:void(0)" v-on:click="onConfirmDelete('license',index)" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="ลบ">
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
                    นำเข้าใบอนุญาติ
                    <input style="display:" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,.pdf,.ppf,.doc,.docx" v-on:change="onBrowse('http://localhost:8080/api/licenseupload',$event)">
                  </span>
                </div>
              </div>

              <div class="form-group m-t-20">
                <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table">
                      <thead>
                        <tr>
                          <th>หลักฐาน</th>
                          <th class="text-center">จัดการ</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr :key="file.index" v-for="(file,index) in accord.evidenceFiles">
                          </td>
                          <td style="vertical-align: middle;">{{file.name}}</td>
                          <td style="vertical-align: middle;" class="text-center">
                            <a href="javascript:void(0)" v-on:click="onConfirmDelete('evidence',index)" class="text-inverse p-r-10" data-toggle="tooltip" title="" title="ลบ">
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
                    นำเข้าเอกสาร
                    <input style="display:" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,.pdf,.ppf,.doc,.docx" v-on:change="onBrowse('http://localhost:8080/api/evidenceupload',$event)">
                  </span>
                </div>
              </div>

              <div class="form-group" v-if="files.file">
                <div class="col-md-12">
                  <progressUpload :props="files.file" :url="files.url" :fileid="$route.params.id"></progressUpload>
                </div>
              </div>

              <div class="form-actions text-center m-t-20">
                <button type="submit" class="btn btn-success m-r-10">
                  <i class="fa fa-check"></i> บันทึก</button>
                <nuxt-link to="/checklist/accord" class="btn btn-info">
                  <i class="fa fa-chevron-left"></i> ย้อนกลับ
                </nuxt-link>
              </div>

            </form>

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
import ProgressUpload from '~/components/ProgressUpload'

export default {
  components: {
    ProgressUpload
  },
  async asyncData (context) {
    let accord = await http
      .get('/api/accord/' + context.params.accord + '/' + context.params.id, { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .catch((e) => {
        context.redirect('/login')
      })

    return {
      accord: accord.data
    }
  },
  data: function () {
    return {
      files: {},
      deleteIndex: {},
      deleteType: ''
    }
  },
  created: function () {
    this.$on('onCompleteUpload', function (json) {
      var obj = {}
      obj['file'] = undefined
      this.$set(this, 'files', obj)
      if ('warningDate' in JSON.parse(json)) {
        this.accord.licenseFiles.push(JSON.parse(json))
        this.$set(this.accord, 'licenseFiles', this.accord.licenseFiles)
      } else {
        this.accord.evidenceFiles.push(JSON.parse(json))
        this.$set(this.accord, 'evidenceFiles', this.accord.evidenceFiles)
      }
    })
  },
  mounted: function () {
    for (var i = 0; i < this.accord.licenseFiles.length; i++) {
      this.showCalendar(i, this.accord.licenseFiles[i])
    }
  },
  updated: function () {
    for (var i = 0; i < this.accord.licenseFiles.length; i++) {
      this.showCalendar(i, this.accord.licenseFiles[i])
    }
  },
  methods: {
    onSave: function () {
      var self = this
      self.accord.legalCategory = {}
      self.accord.legalCategory.id = this.$route.params.accord
      http.post('/api/accord', self.accord, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/checklist/accord' })
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    },
    onBrowse: function (url, e) {
      var obj = {}
      obj['file'] = e.target.files[0]
      obj['url'] = url
      this.$set(this, 'files', obj)
    },
    showCalendar: function (index, file) {
      $('#warningDate-' + index)
        .datepicker({ language: 'th', thaiyear: true, format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
        .on('changeDate', () => { file.warningDate = $('#warningDate-' + index).val() })
      $('#expireDate-' + index)
        .datepicker({ language: 'th', thaiyear: true, format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
        .on('changeDate', () => { file.expireDate = $('#expireDate-' + index).val() })
      return true
    },
    onConfirmDelete: function (type, index) {
      $('#masterdata-add-modal').modal('show')
      this.$set(this, 'deleteIndex', index)
      this.$set(this, 'deleteType', type)
    },
    onDelete: function () {
      $('#masterdata-add-modal').modal('hide')
      if (this.deleteType === 'license') {
        this.accord.licenseFiles.splice(this.deleteIndex, 1)
      } else {
        this.accord.evidenceFiles.splice(this.deleteIndex, 1)
      }
    }
  }
}
</script>
