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
      <label class="col-sm-12">{{ $t('compliance.legalduty') }}</label>
      <div class="col-sm-12">
        <textarea class="form-control" rows="5" v-model="compliance.legalDuty" required></textarea>
      </div>
    </div>
    <button type="submit" class="btn btn-success waves-effect waves-light m-r-10">บันทึก</button>
    <button type="button" data-toggle="modal" data-target="#responsive-modal" class="btn btn-danger waves-effect waves-light m-r-10">ลบข้อกฎหมาย</button>

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

  </form>
</template>

<script>
/* global $ */
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  props: ['selected', 'categories'],
  asyncData: function (context) {
    return http
      .get('/api/compliance/' + context.params.id, { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { compliance: response.data }
      })
      .catch((e) => {
        context.redirect('/login')
      })
  },
  data: function () {
    return {
      category: { id: 'null' }
    }
  },
  watch: {
    selected: function (val) {
      this.$set(this, 'category', val)
    }
  },
  mounted: function () {
    this.$set(this, 'category', this.compliance.category)
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
      self.compliance.category = this.category
      http.post('/api/compliance', self.compliance, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/masterdata' })
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    },
    onDelete: function () {
      var self = this
      $('#responsive-modal').modal('hide')
      return http
        .delete('/api/compliance/' + this.$route.params.id, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$router.push('/masterdata')
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    }
  }
}
</script>
