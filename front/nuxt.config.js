const webpack = require('webpack')

module.exports = {
  /*
  ** Headers of the page
  */
  head: {
    title: 'Mitrphol Compliance',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: 'Nuxt.js project' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ],
    bodyAttrs: {
      class: 'fix-header hide-sidebar'
    }
  },
  /*
  ** Css Loader
  */
  css: [
    { src: 'animate.css/animate.css' },
    { src: 'bootstrap/dist/css/bootstrap.css' },
    { src: 'metismenu/dist/metisMenu.css' },
    { src: 'font-awesome/css/font-awesome.css' },
    { src: 'material-design-iconic-font/dist/css/material-design-iconic-font.css' },
    { src: 'bootstrap-treeview/dist/bootstrap-treeview.min.css' },
    { src: '~assets/less/icons/themify-icons/themify-icons.css' },
    { src: '~assets/less/style.less', lang: 'less' },
    { src: '~assets/less/colors/megna.less', lang: 'less' }
  ],
  /*
  ** Plugins Loader
  */
  plugins: [
    { src: '~plugins/bootstrap' },
    { src: '~plugins/metismenu' },
    { src: '~plugins/treeview' },
    { src: '~plugins/i18n' }
  ],
  /*
  ** Customize the progress-bar color
  */
  loading: { color: '#3B8070' },
  /*
  ** Build configuration
  */
  build: {
    vendor: [
      'jquery',
      'bootstrap',
      'metismenu/dist/metisMenu.js',
      'bootstrap-treeview/dist/bootstrap-treeview.min.js',
      'vue-i18n'
    ],
    plugins: [
      new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery',
        'window.jQuery': 'jquery'
      })
    ],
    /*
    ** Run ESLINT on save
    */
    extend(config, ctx) {
      if (ctx.isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }
  }
}
