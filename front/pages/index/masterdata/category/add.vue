<template>
  <form v-on:submit.prevent="onSave">
    <div class="form-body">
      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <label class="control-label">ชื่อหมวดหมู่</label>
            <input v-model="name" type="text" class="form-control" placeholder="ชื่อหมวดหมู่" required oninvalid="this.setCustomValidity('กรุณากรอกข้อมูล')">
          </div>
        </div>
      </div>
    </div>
    <div class="form-actions">
      <button type="submit" class="btn btn-success">
        <i class="fa fa-check"></i> บันทึก</button>
    </div>
  </form>
</template>

<script>
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  props: ['category'],
  data: function () {
    return {
      name: ''
    }
  },
  mounted: function () {
    console.log(this.$route)
  },
  methods: {
    onSave: function () {
      var self = this
      http.post('/api/category', { parent: { id: self.category.id }, name: self.name }, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/masterdata' })
        })
    }
  }
}
</script>
