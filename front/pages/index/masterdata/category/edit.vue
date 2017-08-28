<template>
  <form v-on:submit.prevent="onSave">
    <div class="form-body">
      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <label class="control-label">ชื่อหมวดหมู่</label>
            <input v-model="category.name" type="text" class="form-control" placeholder="ชื่อหมวดหมู่" required oninvalid="this.setCustomValidity('กรุณากรอกข้อมูล')">
          </div>
        </div>
      </div>
    </div>
    <div class="form-actions">
      <button type="submit" class="btn btn-success m-r-10">
        <i class="fa fa-check"></i> บันทึก</button>
      <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#remove-category-modal">
        <i class="fa fa-times"></i> ลบหมวดหมู่</button>
    </div>
  
    <div id="remove-category-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
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

export default {
  props: ['selected', 'categories'],
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
  computed: mapGetters({
    initCategory: 'category/category'
  }),
  mounted: function () {
    this.$set(this, 'category', this.initCategory)
  },
  methods: {
    onSave: function () {
      var self = this
      http.post('/api/category/update', self.category, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/masterdata' })
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    },
    onDelete: function () {
      var self = this
      $('#remove-category-modal').modal('hide')
      return http
        .delete('/api/category/' + this.category.id, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then((response) => {
          self.$router.push('/masterdata')
        })
        .catch((e) => {
          self.$router.push('/masterdata')
        })
    }
  }
}
</script>
