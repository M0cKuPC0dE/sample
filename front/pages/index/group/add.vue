<template>
  <div id="page-wrapper">
    <div class="container-fluid">

      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">{{ $t('menu.group') }}</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>{{ $t('menu.dashboards') }}</li>
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
                <label class="col-md-12">ผู้ดูแล</label>
                <div class="col-md-12">
                  <div class="ui-widget">
                    <input id="search-coordinate" type="text" class="form-control" placeholder="ผู้ดูแล">
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

              <div class="row">
                <div class="col-md-12">
                  <div id="allview" class="treeview"></div>
                </div>
              </div>

              <div class="form-actions text-center">
                <button type="submit" class="btn btn-success m-r-10">
                  <i class="fa fa-check"></i> {{ $t('buttons.save') }}</button>
                <nuxt-link to="/group" class="btn btn-info">
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
        context.redirect('/login')
      })
  },
  data: function () {
    return {
      category: { id: 'null' },
      legalgroup: {
        buName: '',
        coordinates: [],
        compliances: []
      }
    }
  },
  mounted: function () {
    this.allview(this.categories)
    this.initSuggestion()
  },
  methods: {
    onSave: function () {
      var self = this
      http.post('/api/legalgroup', self.legalgroup, { headers: { Authorization: 'bearer ' + cookie(this).AT } })
        .then(response => {
          self.$router.push({ path: '/group' })
        })
        .catch((e) => {
          self.$router.replace('/login')
        })
    },
    allview: function (categories) {
      var self = this
      $('#allview').treeview({
        expandIcon: 'glyphicon glyphicon-chevron-right',
        collapseIcon: 'glyphicon glyphicon-chevron-down',
        showTags: false,
        showCheckbox: true,
        data: self.cat2node(categories),
        onNodeChecked: function (event, data) {
          if (data.nodes) {
            data.nodes.forEach(function (node) {
              $('#allview').treeview('checkNode', [node.nodeId, { silent: true }])
              if (node.value) { self.addCompliance(node.value) }
              if (node.nodes) {
                node.nodes.forEach(function (subNode) {
                  $('#allview').treeview('checkNode', [subNode.nodeId, { silent: true }])
                  if (subNode.value) { self.addCompliance(subNode.value) }
                })
              }
            })
          } else {
            if (data.value) { self.addCompliance(data.value) }
          }
        },
        onNodeUnchecked: function (event, data) {
          if (data.nodes) {
            data.nodes.forEach(function (node) {
              $('#allview').treeview('uncheckNode', [node.nodeId, { silent: true }])
              if (node.value) { self.removeCompliance(node.value) }
              if (node.nodes) {
                node.nodes.forEach(function (subNode) {
                  $('#allview').treeview('uncheckNode', [subNode.nodeId, { silent: true }])
                  if (subNode.value) { self.removeCompliance(subNode.value) }
                })
              }
            })
          } else {
            if (data.value) { self.removeCompliance(data.value) }
          }
        }
      })
      $('#allview').treeview('collapseAll', { silent: true })
    },
    cat2node: function (categories) {
      var self = this
      var nodes = []
      if (!categories) return

      categories.forEach(function (category) {
        var node = {
          text: category.name,
          icon: 'fa fa-folder',
          selectable: false,
          nodes: category.childs.length === 0 ? [] : self.cat2node(category.childs)
        }
        var compNode = self.compliance2node(category.compliances)
        node.nodes = compNode.concat(node.nodes)
        node.nodes = node.nodes.length === 0 ? '' : node.nodes
        nodes.push(node)
      })
      return nodes
    },
    compliance2node: function (compliances) {
      var nodes = []
      if (!compliances) return

      compliances.forEach(function (compliance) {
        var node = {
          text: compliance.legalName,
          icon: 'fa fa-file-text-o',
          selectable: false,
          value: compliance
        }
        nodes.push(node)
      })
      return nodes
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
              response(self.searchTransform(data))
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
    searchTransform: function (data) {
      var nodes = []
      if (data.success.code === 200) {
        data.success.data.forEach(function (user) {
          var node = {
            userId: user.user_info.id,
            label: user.user_info.fullname.th,
            value: user.user_info.fullname.th
          }
          nodes.push(node)
        })
      }
      return nodes
    },
    removeCoordinate: function (val) {
      var checker = $.grep(this.legalgroup.coordinates, function (obj) {
        return obj.userId !== val.userId
      })
      this.$set(this.legalgroup, 'coordinates', checker)
    },
    addCompliance: function (compliance) {
      this.legalgroup.compliances.push(compliance)
      this.$set(this.legalgroup, 'compliances', this.legalgroup.compliances)
    },
    removeCompliance: function (compliance) {
      var compliances = $.grep(this.legalgroup.compliances, function (elm) {
        return elm.id !== compliance.id
      })
      this.$set(this.legalgroup, 'compliances', compliances)
    }
  }
}
</script>
