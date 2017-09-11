<template>
  <form class="form-horizontal" v-on:submit.prevent="onSave">
    <div class="form-group">
      <label class="col-md-12">
        <span class="help">ชื่อกฎหมาย</span>
      </label>
      <div class="col-md-12">
        <input type="text" class="form-control readonly" :value="selected.legalName" required>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-12">หน้าที่ตามกฎหมาย</label>
      <div class="col-sm-12">
        <textarea class="form-control" rows="5" v-model="legalDuty.name" required></textarea>
      </div>
    </div>
    <button type="submit" class="btn btn-success waves-effect waves-light m-r-10">บันทึก</button>
    <nuxt-link to="/checklist/legalduty" class="btn btn-info">
      <i class="fa fa-chevron-left"></i> ย้อนกลับ
    </nuxt-link>

  </form>
</template>

<script>
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  props: ['selected'],
  data: function () {
    return {
      legalDuty: {
        name: '',
        compliance: {}
      }
    }
  },
  methods: {
    onSave: function () {
      var self = this
      this.legalDuty.compliance = { id: this.selected.id }
      return http
        .post('/api/legalduty', this.legalDuty, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$router.replace('/checklist/legalduty')
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    }
  }
}
</script>
