/* global $ */
if (process.BROWSER_BUILD) {
  $('.open-close').on('click', function () {
    $('body').toggleClass('show-sidebar').toggleClass('hide-sidebar')
    $('.sidebar-head .open-close i').toggleClass('ti-menu')
  })
}
