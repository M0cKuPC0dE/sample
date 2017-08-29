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

            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-md-12">{{ $t('group.name') }}</label>
                <div class="col-md-12">
                  <input type="text" class="form-control" placeholder="Group Name"> </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">{{ $t('group.assignto') }}</label>
                <div class="col-md-12">
                  <input type="text" class="form-control" placeholder="Assign To"> </div>
              </div>
            </form>

            <div class="row">
              <div class="col-md-12">
                <div id="allview" class="treeview"></div>
              </div>
            </div>

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
      .get('/api/category', { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .then((response) => {
        return { categories: response.data }
      })
      .catch((e) => {
        context.redirect('/login')
      })
  },
  data: function () {
    return { category: { id: 'null' } }
  },
  mounted: function () {
    this.allview(this.categories)
  },
  methods: {
    allview: function (categories) {
      var self = this
      $('#allview').treeview({
        expandIcon: 'glyphicon glyphicon-chevron-right',
        collapseIcon: 'glyphicon glyphicon-chevron-down',
        showTags: false,
        showCheckbox: true,
        data: self.cat2node(categories),
        onNodeChecked: function (event, data) {
          data.nodes.forEach(function (node) {
            $('#allview').treeview('checkNode', [node.nodeId, { silent: true }])
          })
        },
        onNodeUnchecked: function (event, data) {
          data.nodes.forEach(function (node) {
            $('#allview').treeview('uncheckNode', [node.nodeId, { silent: true }])
          })
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
          category: category,
          tags: [category.childs.length],
          nodes: self.cat2node(category.childs)
        }
        nodes.push(node)
      })
      return nodes
    }
  }
}
</script>

