<template>
  <div id="page-wrapper">
    <div class="container-fluid">

      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">{{ $t('menu.group') }}</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>{{ $t('menu.dashboard') }}</li>
            <li class="active">{{ $t('menu.group') }}</li>
          </ol>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="white-box">

            <div class="row">
              <div class="col-md-12">
                <h3 class="box-title">{{ $t('menu.group') }}</h3>
              </div>
            </div>

            <form class="form-horizontal" v-on:submit.prevent="onSave">
              <div class="form-group">
                <label class="col-md-12">หน่วยงาน</label>
                <div class="col-md-12">
                  <input type="text" class="form-control" placeholder="หน่วยงาน" v-model="legalgroup.buName" required>
                </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">ผู้ประสานงาน</label>
                <div class="col-md-12">
                  <div class="ui-widget">
                    <input id="search-coordinate" type="text" class="form-control" placeholder="ผู้ประสานงาน">
                  </div>
                </div>
                <div class="col-md-12 p-t-20 p-l-20 p-r-20">
                  <ul class="list-group list-group-full">
                    <li class="list-group-item" :key="coordinate.userId" v-for="coordinate in legalgroup.coordinates">
                      <span class="badge badge-danger" v-on:click="removeCoordinate(coordinate)">
                        <i class="fa fa-times"></i>
                      </span> {{coordinate.value}} </li>
                  </ul>
                </div>
              </div>

              <div class="form-actions text-center">
                <button type="submit" class="btn btn-success m-r-10">
                  <i class="fa fa-check"></i> {{ $t('buttons.save') }}</button>
                <nuxt-link to="/checklist/coordinator" class="btn btn-info">
                  <i class="fa fa-chevron-left"></i> {{ $t('buttons.back') }}
                </nuxt-link>
              </div>
            </form>

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

export default {
  asyncData: function (context) {
    return http
      .get('/api/category/compliance', { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { categories: response.data }
      })
      .catch((e) => {
        context.redirect('/checklist/login')
      })
  },
  data: function () {
    return {
      category: { id: 'null' },
      legalgroup: {
        buName: '',
        coordinates: [],
        legalDuties: []
      }
    }
  },
  mounted: function () {
    this.initSuggestion()
  },
  methods: {
    onSave: function () {
      var self = this
      http.post('/api/legalgroup', self.legalgroup, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/checklist/coordinator' })
        })
        .catch((e) => {
          self.$router.replace('/checklist/login')
        })
    },
    initSuggestion: function () {
      var self = this
      $('#search-coordinate').autocomplete({
        source: function (request, response) {
          $.ajax({
            url: 'https://api.mitrphol.com:3001/employee/find',
            dataType: 'json',
            headers: {
              'Api-Key': '$2y$10$Pc0lTscxUAlq9O5V8Arwau6VpgLlMEj9xLAPymFqbay2mbM3qJJee',
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            method: 'POST',
            data: {
              keyword: request.term
            },
            success: function (data) {
              response(self.searchTransform(data, request.term))
            }
          })
        },
        minLength: 3,
        select: function (event, ui) {
          var checker = $.grep(self.legalgroup.coordinates, function (obj) {
            return obj.userId === ui.item.userId
          })
          if (checker.length === 0) {
            self.legalgroup.coordinates.push(ui.item)
            self.$set(self.legalgroup, 'coordinates', self.legalgroup.coordinates)
          }
        },
        close: function (el) {
          el.target.value = ''
        }
      })
    },
    searchTransform: function (data, term) {
      var nodes = []
      if (data.success) {
        data.success.data.forEach(function (user) {
          var node = {
            userId: user.user_info.id,
            label: user.user_info.fullname.th,
            value: user.user_info.fullname.th
          }
          nodes.push(node)
        })
      }

      var ust = [
        { userId: '99999999', label: 'วิจะยะ กลิ่นเกษร', value: 'วิจะยะ กลิ่นเกษร' },
        { userId: '99999998', label: 'กิตติยา คล้ายสังข์', value: 'กิตติยา คล้ายสังข์' },
        { userId: '99999997', label: 'อชิรวิชย์ สุวรรณโรจน์', value: 'อชิรวิชย์ สุวรรณโรจน์' },
        { userId: '99999996', label: 'อาภรณ์ สิงห์โต', value: 'อาภรณ์ สิงห์โต' },
        { userId: '99999995', label: 'กมลทิพย์ ศรีรอด', value: 'กมลทิพย์ ศรีรอด' },
        { userId: '99999994', label: 'อรุณทิพย์ กวาวทอง', value: 'อรุณทิพย์ กวาวทอง' },
        { userId: '99999993', label: 'วรางคณา ศิริมา', value: 'วรางคณา ศิริมา' }
      ]

      let ustNode = ust.find(o => o.label.search(term) !== -1)
      if (ustNode) {
        nodes.push(ustNode)
      }

      return nodes
    },
    removeCoordinate: function (val) {
      var checker = $.grep(this.legalgroup.coordinates, function (obj) {
        return obj.userId !== val.userId
      })
      this.$set(this.legalgroup, 'coordinates', checker)
    }
  }
}
</script>
