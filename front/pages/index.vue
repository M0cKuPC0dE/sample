<template>
  <div>
    <navbar />
    <sidebar />
    <nuxt-child/>
  </div>
</template>

<script>
import Navbar from '~components/Navbar'
import Sidebar from '~components/Sidebar'

export default {
  components: {
    Navbar,
    Sidebar
  }
}
</script>
