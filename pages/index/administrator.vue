<template>
  <div id="page-wrapper">
    <div class="container-fluid">
  
      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">Compliant Group</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>Dashboard</li>
            <li class="active">Compliant Group</li>
          </ol>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="white-box">
  
            <div class="row">
              <div class="col-md-12">
                <h3 class="box-title">Manage Compliant Group</h3>
              </div>
            </div>
  
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-md-12">Group Name</label>
                <div class="col-md-12">
                  <input type="text" class="form-control" placeholder="Group Name"> </div>
              </div>
  
              <div class="form-group">
                <label class="col-md-12">Assign To</label>
                <div class="col-md-12">
                  <input type="text" class="form-control" placeholder="Assign To"> </div>
              </div>
            </form>
  
            <div class="row">
              <div class="col-md-5">
                <div id="treeview1" class="treeview">
                  <ul class="list-group">
                    <li class="list-group-item node-treeview2" data-nodeid="0" style="color:undefined;background-color:undefined;">
                      <span class="icon expand-icon glyphicon glyphicon-minus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชบัญญัติ
                      <span class="badge">3</span>
                    </li>
                    <li class="list-group-item node-treeview2" data-nodeid="1" style="color:undefined;background-color:undefined;">
                      <span class="indent"></span>
                      <span class="icon expand-icon glyphicon glyphicon-plus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชกำหนด
                      <span class="badge">0</span>
                    </li>
                    <li class="list-group-item node-treeview2" data-nodeid="3" style="color:undefined;background-color:undefined;">
                      <span class="indent"></span>
                      <span class="icon expand-icon glyphicon glyphicon-plus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชกฤษฎีกา
                      <span class="badge">2</span>
                    </li>
                    <li class="list-group-item node-treeview2" data-nodeid="7" style="color:undefined;background-color:undefined;">
                      <span class="icon glyphicon"></span>
                      <span class="icon node-icon fa fa-file-text-o"></span>ข้อกฎหมาย 1</li>
                  </ul>
                </div>
              </div>
              <div class="col-md-2">
                <button class="btn btn-block btn-info btn-rounded">Add &gt;&gt;</button>
                <button class="btn btn-block btn-info btn-rounded">&lt;&lt; Remove </button>
              </div>
              <div class="col-md-5">
                <div id="treeview2" class="treeview">
                  <ul class="list-group">
                    <li class="list-group-item node-treeview2" data-nodeid="0" style="color:undefined;background-color:undefined;">
                      <span class="icon expand-icon glyphicon glyphicon-minus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชบัญญัติ
                      <span class="badge">3</span>
                    </li>
                    <li class="list-group-item node-treeview2" data-nodeid="1" style="color:undefined;background-color:undefined;">
                      <span class="indent"></span>
                      <span class="icon expand-icon glyphicon glyphicon-plus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชกำหนด
                      <span class="badge">0</span>
                    </li>
                    <li class="list-group-item node-treeview2" data-nodeid="3" style="color:undefined;background-color:undefined;">
                      <span class="indent"></span>
                      <span class="icon expand-icon glyphicon glyphicon-plus"></span>
                      <span class="icon node-icon fa fa-folder"></span>พระราชกฤษฎีกา
                      <span class="badge">2</span>
                    </li>
                    <li class="list-group-item node-treeview2" data-nodeid="7" style="color:undefined;background-color:undefined;">
                      <span class="icon glyphicon"></span>
                      <span class="icon node-icon fa fa-file-text-o"></span>ข้อกฎหมาย 1</li>
                  </ul>
                </div>
              </div>
            </div>
  
          </div>
        </div>
      </div>
  
    </div>
  </div>
</template>
