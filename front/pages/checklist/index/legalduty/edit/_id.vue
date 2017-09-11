<template>
  <form class="form-horizontal" v-on:submit.prevent="onSave">
    <div class="form-group">
      <label class="col-md-12">
        <span class="help">ชื่อกฎหมาย</span>
      </label>
      <div class="col-md-12">
        <input type="text" class="form-control readonly" :value="compliance.legalName" required>
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
import { mapGetters } from 'vuex'
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  props: ['selected'],
  asyncData: function (context) {
    return http
      .get('/api/legalduty/' + context.params.id, { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { legalDuty: response.data }
      })
      .catch((e) => {
        context.redirect('/checklist/login')
      })
  },
  data: function () {
    return {
      compliance: {},
      legalDuty: {
        name: '',
        compliance: {}
      }
    }
  },
  watch: {
    selected: function (val) {
      this.$set(this, 'compliance', val)
    }
  },
  computed: mapGetters({
    initCompliance: 'category/compliance'
  }),
  mounted: function () {
    this.$set(this, 'compliance', this.initCompliance)
  },
  methods: {
    onSave: function () {
      var self = this
      this.legalDuty.compliance = { id: this.compliance.id }
      return http
        .post('/api/legalduty', this.legalDuty, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$router.replace('/checklist/legalduty')
        })
        .catch((e) => {
          self.$router.replace('/checklist/login')
        })
    }
  }
}
</script>
