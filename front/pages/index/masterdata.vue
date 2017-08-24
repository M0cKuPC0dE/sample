<template>
  <div id="page-wrapper">
    <div class="container-fluid">
  
      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">{{ $t('masterdatas.title') }}</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>{{ $t('dashboards.title') }}</li>
            <li class="active">{{ $t('masterdatas.title') }}</li>
          </ol>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="white-box">
  
            <div class="row">
              <div class="col-md-4">
                <h3 class="box-title">Manage Master Data</h3>
              </div>
            </div>
  
            <div class="row">
              <div class="col-md-4 m-b-10">
                <div>
                  <div id="category" class="treeview"></div>
                  <nuxt-link to="/masterdata/category/add" class="btn btn-block btn-info btn-rounded">เพิ่มหมวดหมู่</nuxt-link>
                </div>
              </div>
              <div class="col-md-8">
                {{category}}
                <nuxt-child :category="category"></nuxt-child>
  
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
        return { categories: response.data, category: { id: 'null' } }
      })
      .catch((e) => {
        context.redirect('/login')
      })
  },
  mounted: function () {
    this.renderTreeview(this.categories)
  },
  watch: {
    $route: function (val) {
      var self = this
      if (val.path === '/masterdata') {
        return http
          .get('/api/category', { headers: { Authorization: 'bearer ' + cookie(self).AT } })
          .then((response) => {
            self.category = { id: 'null' }
            self.renderTreeview(response.data)
          })
          .catch((e) => {
            self.$router.replace('/login')
          })
      }
    }
  },
  methods: {
    renderTreeview: function (categories) {
      var self = this
      $('#category').treeview({
        expandIcon: 'glyphicon glyphicon-chevron-right',
        collapseIcon: 'glyphicon glyphicon-chevron-down',
        showTags: true,
        data: self.cat2node(categories),
        onNodeSelected: function (event, data) {
          self.$nextTick(function () {
            self.category = data.category
          })
        }
      })
      $('#category').treeview('collapseAll', { silent: true })
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
