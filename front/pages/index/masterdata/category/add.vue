<template>
  <form v-on:submit.prevent="onSave">
    <div class="form-body">
      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <label class="control-label">{{ $t('category.name') }}</label>
            <input v-model="name" type="text" class="form-control" placeholder="ชื่อหมวดหมู่" required oninvalid="this.setCustomValidity('กรุณากรอกข้อมูล')">
          </div>
        </div>
      </div>
    </div>
    <div class="form-actions">
      <button type="submit" class="btn btn-success">
        <i class="fa fa-check"></i>{{ $t('buttons.save') }}</button>
    </div>
  </form>
</template>

<script>
import { mapGetters } from 'vuex'
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  props: ['selected', 'categories'],
  data: function () {
    return {
      name: '',
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
      http.post('/api/category', { parent: { id: self.category.id }, name: self.name }, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/masterdata' })
        })
    }
  }
}
</script>
