<template>
  <div id="page-wrapper">
    <div class="container-fluid">
  
      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">Master Data</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>Dashboard</li>
            <li class="active">Master Data</li>
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
              <div class="col-md-4">
                <button class="btn btn-block btn-info btn-rounded">Add Category</button>
              </div>
              <div class="col-md-4">
                <button class="btn btn-block btn-info btn-rounded">Add Compliant</button>
              </div>
            </div>
  
            <div class="row">
              <div class="col-md-4">
                <div id="treeview" class="treeview">
                  <ul class="list-group">
                    <li class="list-group-item node-treeview" data-nodeid="0" style="color:undefined;background-color:undefined;">
                      <span class="icon expand-icon glyphicon glyphicon-minus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชบัญญัติ
                      <span class="badge">3</span>
                    </li>
                    <li class="list-group-item node-treeview" data-nodeid="1" style="color:undefined;background-color:undefined;">
                      <span class="indent"></span>
                      <span class="icon expand-icon glyphicon glyphicon-plus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชกำหนด
                      <span class="badge">3</span>
                    </li>
                    <li class="list-group-item node-treeview" data-nodeid="3" style="color:undefined;background-color:undefined;">
                      <span class="indent"></span>
                      <span class="icon expand-icon glyphicon glyphicon-plus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชกฤษฎีกา
                      <span class="badge">3</span>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="col-md-8">
  
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>ข้อกฎหมาย</th>
                        <th>วันหมดอายุ</th>
                        <th>สถานะ</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>1</td>
                        <td>ข้อกฎหมาย 1</td>
                        <td>30/4/2560</td>
                        <td>
                          <span class="label label-danger">Expire</span>
                        </td>
                        <tr>
                          <td>2</td>
                          <td>ข้อกฎหมาย 2</td>
                          <td>31/12/2560</td>
                          <td>
                            <span class="label label-success">Active</span>
                          </td>
                        </tr>
                        <tr>
                          <td>3</td>
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
  
          </div>
        </div>
      </div>
  
    </div>
  </div>
</template>
