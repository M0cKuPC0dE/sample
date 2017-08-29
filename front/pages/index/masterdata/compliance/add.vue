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
      <label class="col-sm-12">{{ $t('compliance.legalduty') }}</label>
      <div class="col-sm-12">
        <textarea class="form-control" rows="5" v-model="compliance.legalDuty" required></textarea>
        <span class="help-block">
          <small>A block of help text that breaks onto a new line and may extend beyond one line.</small>
        </span>
      </div>
    </div>
    <button type="submit" class="btn btn-success waves-effect waves-light m-r-10">บันทึก</button>
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
      category: { id: 'null' },
      compliance: {
        legalName: '',
        year: '',
        publicDate: '',
        effectiveDate: '',
        department: '',
        ministry: '',
        importain: '',
        legalDuty: '',
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
  mounted: function () {
    this.$set(this, 'category', this.initCategory)
    $('#publicdate')
      .datepicker({ language: 'th', format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
      .on('changeDate', () => { this.compliance.publicDate = $('#publicdate').val() })
    $('#effectivedate')
      .datepicker({ language: 'th', format: 'dd/mm/yyyy', orientation: 'bottom left', autoclose: !0, todayHighlight: !0 })
      .on('changeDate', () => { this.compliance.effectiveDate = $('#effectivedate').val() })

    $('.readonly').on('keydown paste', function (e) {
      e.preventDefault()
    })
  },
  methods: {
    onSave: function () {
      var self = this
      self.compliance.category = this.category
      http.post('/api/compliance', self.compliance, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/masterdata' })
        })
    }
  }
}
</script>
