/* global $ */
if (process.BROWSER_BUILD) {
  var tree = [
    {
      text: 'พระราชบัญญัติ',
      icon: 'fa fa-folder',
      tags: ['3'],
      nodes: [
        {
          text: 'พระราชกำหนด',
          icon: 'fa fa-folder',
          tags: ['3'],
          nodes: [
            {
              text: 'ระเบียบ',
              icon: 'fa fa-folder',
              tags: ['3']
            }
          ]
        },
        {
          text: 'พระราชกฤษฎีกา',
          icon: 'fa fa-folder',
          tags: ['3'],
          nodes: [
            {
              text: 'ประกาศ',
              icon: 'fa fa-folder',
              tags: ['3']
            }
          ]
        }
      ]
    }
  ]
  $('#treeview').treeview({ data: tree, showTags: true })
}
