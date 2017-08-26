<template>
  <div id="page-wrapper">
    <div class="container-fluid">
      <h3>Import File</h3>
      <div class="form-group" v-if="!files.file">
        <div class="col-md-12 text-center is-fileinput">
          <input style="display:" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" v-on:change="onBrowse('http://localhost:8080/api/csvupload',$event)">
        </div>
      </div>

      <div class="form-group" v-if="files.file">
        <div class="col-md-12">
          <progressUpload :props="files.file" :url="files.url" :fileid="$route.params.id"></progressUpload>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProgressUpload from '~components/ProgressUpload'

export default {
  components: {
    ProgressUpload
  },
  data: function () {
    return {
      files: {}
    }
  },
  created: function () {
    this.$on('onCompleteUpload', function (index) {
      var obj = {}
      obj[index] = undefined
      this.$set(this, 'files', obj)
    })
  },
  methods: {
    onBrowse: function (url, e) {
      var obj = {}
      obj['file'] = e.target.files[0]
      obj['url'] = url
      this.$set(this, 'files', obj)
    }
  }
}
</script>
