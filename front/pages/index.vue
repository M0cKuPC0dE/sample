<template>
  <div>
    <LibraryNavbar></LibraryNavbar>
    <div class="library-wrapper">
      <div class="container-fluid">
        <div class="row m-t-10 p-t-20">
          <div class="col-md-12">
            <form>
              <div class="form-group">
                <div class="input-group bootstrap-touchspin">
                  <input id="search-tree" type="text" class="form-control" style="display: block" autocomplete="off">
                  <span class="input-group-btn">
                    <button type="button" class="btn btn-default btn-outline">
                      <i class="fa fa-search"></i>
                    </button>
                  </span>
                </div>
              </div>
            </form>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
            <div id="allview" class="treeview"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/* global $ */
import http from '~/utils/http'
import LibraryNavbar from '~/components/LibraryNavbar'

export default {
  layout: 'index',
  async asyncData (context) {
    let categories = await http
      .get('/category/compliance')
    return {
      categories: categories.data
    }
  },
  components: {
    LibraryNavbar
  },
  data: function () {
    return {
      lastPattern: ''
    }
  },
  mounted: function () {
    this.allview(this.categories)
    $('#search-tree').on('keyup', this.search)
  },
  methods: {
    allview: function (categories) {
      var self = this
      $('#allview').treeview({
        expandIcon: 'glyphicon glyphicon-chevron-right',
        collapseIcon: 'glyphicon glyphicon-chevron-down',
        showTags: false,
        showCheckbox: false,
        data: self.cat2node(categories)
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
        if (node.nodes.length !== 0) {
          nodes.push(node)
        }
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
    search: function (e) {
      var pattern = $('#search-tree').val()
      if (pattern === this.lastPattern) {
        return
      }
      this.lastPattern = pattern
      var tree = $('#allview').treeview(true)
      this.reset(tree)
      if (pattern.length < 3) {
        tree.clearSearch()
      } else {
        tree.search(pattern)
        var roots = tree.getSiblings(0)
        roots.push(tree.getNode(0))
        var unrelated = this.collectUnrelated(roots)
        tree.disableNode(unrelated, { silent: true })
      }
    },
    reset: function (tree) {
      tree.collapseAll()
      tree.enableAll()
    },
    collectUnrelated: function (nodes) {
      var unrelated = []
      var self = this
      $.each(nodes, function (i, n) {
        if (!n.searchResult && !n.state.expanded) {
          unrelated.push(n.nodeId)
        }
        if (!n.searchResult && n.nodes) {
          $.merge(unrelated, self.collectUnrelated(n.nodes))
        }
      })
      return unrelated
    }
  }
}
</script>

<style lang="scss">
.library-wrapper {
  background-color: #edf1f5;
  margin-top: 60px;
  width: 100%;
  min-height: calc(100vh - 60px);
  .row {
    background-color: #fff;
  }
  #allview .node-disabled {
    display: none;
  }
}
</style>
