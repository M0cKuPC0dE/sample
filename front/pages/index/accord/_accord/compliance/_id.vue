<template>
  <div id="page-wrapper">
    <div class="container-fluid">

      <div class="row bg-title">
        <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
          <h4 class="page-title">{{ $t('compliance.legalname') }}</h4>
        </div>
        <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
          <ol class="breadcrumb">
            <li>หน้าหลัก</li>
            <li class="active">{{ $t('compliance.legalname') }}</li>
          </ol>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="white-box">

            <div class="row">
              <div class="col-md-12">
                <h3 class="box-title">{{compliance.legalDuty}}</h3>
              </div>
            </div>

            <form class="form-horizontal">

              <div class="form-group">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>{{ $t('compliance.category') }}</strong>
                    </label>
                    <div class="col-md-12">
                      {{compliance.category.name}}
                    </div>
                  </div>
                </div>

                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>{{ $t('compliance.effectivedate') }}</strong>
                    </label>
                    <div class="col-md-12">
                      {{compliance.effectiveDate}}
                    </div>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">
                  <strong>{{ $t('compliance.legalname') }}</strong>
                </label>
                <div class="col-md-12">
                  {{compliance.legalName}}
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>{{ $t('compliance.department') }}</strong>
                    </label>
                    <div class="col-md-12">
                      {{compliance.department}}
                    </div>
                  </div>
                </div>
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>{{ $t('compliance.ministry') }}</strong>
                    </label>
                    <div class="col-md-12">
                      {{compliance.ministry}}
                    </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>หน่วยงานที่เกี่ยวข้อง</strong>
                    </label>
                    <div class="col-md-12">
                    </div>
                  </div>
                </div>

                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>ฝ่ายที่เกี่ยวข้อง</strong>
                    </label>
                    <div class="col-md-12">
                    </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <div class="form-group">
                    <label class="col-md-12">
                      <strong>แผนกที่เกี่ยวข้อง</strong>
                    </label>
                    <div class="col-md-12">
                    </div>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="col-md-12">
                  <strong>{{ $t('compliance.important') }}</strong>
                </label>
                <div class="col-md-12">
                  {{compliance.important}}
                </div>
              </div>

              <div class="row">
                <div class="col-md-6">
                  <strong>ประเมินความสอดคล้อง</strong>
                  <div class="radio radio-success">
                    <input type="radio" name="radio" id="radio1" value="option1" checked>
                    <label for="radio1"> สอดคล้อง </label>
                  </div>
                  <div class="radio radio-danger">
                    <input type="radio" name="radio" id="radio2" value="option2">
                    <label for="radio2"> ไม่สอดคล้อง </label>
                  </div>
                  <div class="radio">
                    <input type="radio" name="radio" id="radio3" value="option3">
                    <label for="radio3"> ไม่เกี่ยวข้อง </label>
                  </div>
                </div>
                <div class="col-md-6">
                  <strong>หมายเหตุ</strong>
                  <div>
                    <textarea class="form-control" rows="3"></textarea>
                  </div>
                </div>
              </div>

            </form>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import http from '~/utils/http'
import cookie from '~/utils/cookie'

export default {
  async asyncData (context) {
    let compliace = await http
      .get('/api/compliance/' + context.params.id, { headers: { Authorization: 'bearer ' + cookie(context).AT } })
      .catch((e) => {
        context.redirect('/login')
      })

    return {
      compliance: compliace.data
    }
  }
}
</script>
