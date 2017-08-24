<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-4"></div>
      <div class="col-md-4 m-b-10">
        <nuxt-link to="/masterdata/category/add" class="btn btn-block btn-info">เพิ่มหมวดหมู่</nuxt-link>
      </div>
      <div class="col-md-4 m-b-10">
        <nuxt-link to="/masterdata/compliance/add" class="btn btn-block btn-info">เพิ่มข้อกฎหมาย</nuxt-link>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 m-b-10 p-0">
        <div class="form-group">
          <div class="input-group bootstrap-touchspin">
            <input type="text" class="form-control" style="display: block;">
            <span class="input-group-btn">
              <button class="btn btn-default btn-outline" type="button">
                <i class="fa fa-search"></i>
              </button>
            </span>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>ข้อกฎหมาย</th>
              <th>วันหมดอายุ</th>
              <th>สถานะ</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>ข้อกฎหมาย 1</td>
              <td>30/4/2560</td>
              <td>
                <span class="label label-danger">Expire</span>
              </td>
              <tr>
                <td>ข้อกฎหมาย 2</td>
                <td>31/12/2560</td>
                <td>
                  <span class="label label-success">Active</span>
                </td>
              </tr>
              <tr>
                <td>ข้อกฎหมาย 3</td>
                <td>31/12/2560</td>
                <td>
                  <span class="label label-success">Active</span>
                </td>
              </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
