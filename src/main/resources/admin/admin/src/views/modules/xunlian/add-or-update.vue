<template>
  <div class="addEdit-block">
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="100px"
      class="detail-form-content"
    >
      <el-row :gutter="16">
        <el-col :span="12" v-if="sessionTable !== 'yonghu'">
          <el-form-item label="用户" prop="yonghuId">
            <template v-if="type === 'info'">
              <el-input v-model="ruleForm.yonghuName" readonly />
            </template>
            <template v-else>
              <el-select v-model="ruleForm.yonghuId" placeholder="请选择用户" filterable clearable @change="yonghuChange">
                <el-option
                  v-for="item in yonghuOptions"
                  :key="item.id"
                  :label="item.yonghuName"
                  :value="item.id"
                />
              </el-select>
            </template>
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="sessionTable !== 'yonghu'">
          <el-form-item label="用户编号">
            <el-input v-model="yonghuForm.yonghuUuidNumber" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="sessionTable !== 'yonghu'">
          <el-form-item label="手机号">
            <el-input v-model="yonghuForm.yonghuPhone" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="计划名称" prop="xunlianName">
            <el-input
              v-model="ruleForm.xunlianName"
              :readonly="type === 'info'"
              placeholder="请输入训练计划名称"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="计划编号" prop="xunlianUuidNumber">
            <el-input v-model="ruleForm.xunlianUuidNumber" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="计划类型" prop="xunlianTypes">
            <template v-if="type === 'info'">
              <el-input v-model="ruleForm.xunlianValue" readonly />
            </template>
            <template v-else>
              <el-select v-model="ruleForm.xunlianTypes" placeholder="请选择计划类型" clearable>
                <el-option
                  v-for="item in xunlianTypesOptions"
                  :key="item.codeIndex"
                  :label="item.indexName"
                  :value="item.codeIndex"
                />
              </el-select>
            </template>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="训练科目" prop="xunlianKemu">
            <el-input
              v-model="ruleForm.xunlianKemu"
              :readonly="type === 'info'"
              placeholder="请输入训练科目"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="日期" prop="xunlianTime">
            <template v-if="type === 'info'">
              <el-input v-model="ruleForm.xunlianTime" readonly />
            </template>
            <el-date-picker
              v-else
              v-model="ruleForm.xunlianTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择日期"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="计划图片" prop="xunlianPhoto">
            <template v-if="type === 'info'">
              <div v-if="ruleForm.xunlianPhoto">
                <img
                  v-for="(item, index) in (ruleForm.xunlianPhoto || '').split(',')"
                  :key="index"
                  :src="$base.url + item"
                  class="preview-image"
                  alt="计划图片"
                >
              </div>
              <span v-else>无</span>
            </template>
            <file-upload
              v-else
              tip="上传计划图片"
              action="file/upload"
              :limit="1"
              :multiple="false"
              :fileUrls="ruleForm.xunlianPhoto ? $base.url + ruleForm.xunlianPhoto : ''"
              @change="xunlianPhotoUploadChange"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="计划介绍" prop="xunlianContent">
            <template v-if="type === 'info'">
              <div class="content-view" v-html="ruleForm.xunlianContent" />
            </template>
            <el-input
              v-else
              v-model="ruleForm.xunlianContent"
              type="textarea"
              :rows="4"
              placeholder="请输入计划介绍"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item class="btn">
        <el-button v-if="type !== 'info'" type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="back">{{ type === 'info' ? '返回' : '取消' }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  props: ['parent'],
  data() {
    return {
      id: null,
      type: 'edit',
      sessionTable: '',
      userId: '',
      yonghuOptions: [],
      yonghuForm: {
        yonghuUuidNumber: '',
        yonghuName: '',
        yonghuPhone: ''
      },
      xunlianTypesOptions: [],
      ruleForm: {
        id: '',
        yonghuId: '',
        yonghuName: '',
        xunlianName: '',
        xunlianUuidNumber: '',
        xunlianPhoto: '',
        xunlianTypes: '',
        xunlianValue: '',
        xunlianKemu: '',
        xunlianTime: '',
        xunlianContent: '',
        xunlianDelete: 1
      },
      rules: {
        yonghuId: [{ required: true, message: '请选择用户', trigger: 'change' }],
        xunlianName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
        xunlianUuidNumber: [{ required: true, message: '计划编号不能为空', trigger: 'blur' }],
        xunlianPhoto: [{ required: true, message: '请上传计划图片', trigger: 'change' }],
        xunlianTypes: [{ required: true, message: '请选择计划类型', trigger: 'change' }],
        xunlianKemu: [{ required: true, message: '请输入训练科目', trigger: 'blur' }],
        xunlianTime: [{ required: true, message: '请选择日期', trigger: 'change' }],
        xunlianContent: [{ required: true, message: '请输入计划介绍', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.sessionTable = this.$storage.get('sessionTable') || ''
    this.userId = this.$storage.get('userId') || ''
    this.loadYonghuOptions()
    this.loadXunlianTypes()
  },
  methods: {
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        yonghuId: this.sessionTable === 'yonghu' ? Number(this.userId) : '',
        yonghuName: '',
        xunlianName: '',
        xunlianUuidNumber: `${Date.now()}`,
        xunlianPhoto: '',
        xunlianTypes: '',
        xunlianValue: '',
        xunlianKemu: '',
        xunlianTime: '',
        xunlianContent: '',
        xunlianDelete: 1
      }
      this.yonghuForm = {
        yonghuUuidNumber: '',
        yonghuName: '',
        yonghuPhone: ''
      }
      if (this.sessionTable === 'yonghu' && this.ruleForm.yonghuId) {
        this.yonghuChange(this.ruleForm.yonghuId)
      }
      if (this.id) {
        this.info(this.id)
      }
    },
    loadYonghuOptions() {
      this.$http({
        url: 'yonghu/page',
        method: 'get',
        params: { page: 1, limit: 1000 }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.yonghuOptions = data.data.list || []
        }
      })
    },
    loadXunlianTypes() {
      this.$http({
        url: 'dictionary/page',
        method: 'get',
        params: {
          page: 1,
          limit: 100,
          dicCode: 'xunlian_types'
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.xunlianTypesOptions = data.data.list || []
        }
      })
    },
    yonghuChange(id) {
      if (!id) {
        this.yonghuForm = {
          yonghuUuidNumber: '',
          yonghuName: '',
          yonghuPhone: ''
        }
        return
      }
      this.$http({
        url: `yonghu/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.yonghuForm = data.data || {}
          this.ruleForm.yonghuName = this.yonghuForm.yonghuName || ''
        }
      })
    },
    info(id) {
      this.$http({
        url: `xunlian/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = Object.assign(
            {
              id: '',
              yonghuId: '',
              yonghuName: '',
              xunlianName: '',
              xunlianUuidNumber: '',
              xunlianPhoto: '',
              xunlianTypes: '',
              xunlianValue: '',
              xunlianKemu: '',
              xunlianTime: '',
              xunlianContent: '',
              xunlianDelete: 1
            },
            data.data || {}
          )
          if (this.ruleForm.yonghuId) {
            this.yonghuChange(this.ruleForm.yonghuId)
          }
        } else {
          this.$message.error(data.msg || '加载失败')
        }
      })
    },
    xunlianPhotoUploadChange(fileUrls) {
      this.ruleForm.xunlianPhoto = fileUrls
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('xunlianPhoto')
      }
    },
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        const formData = { ...this.ruleForm }
        if (this.sessionTable === 'yonghu') {
          formData.yonghuId = Number(this.userId)
        }
        delete formData.yonghuName
        delete formData.xunlianValue
        this.$http({
          url: `xunlian/${!formData.id ? 'save' : 'update'}`,
          method: 'post',
          data: formData
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.parent.showFlag = true
            this.parent.addOrUpdateFlag = false
            this.parent.getDataList()
          } else {
            this.$message.error(data.msg || '保存失败')
          }
        })
      })
    },
    back() {
      this.parent.showFlag = true
      this.parent.addOrUpdateFlag = false
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-form-content {
  padding: 8px 4px;
}

.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #ebeef5;
  margin-right: 12px;
}

.content-view {
  min-height: 100px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 8px;
}
</style>

