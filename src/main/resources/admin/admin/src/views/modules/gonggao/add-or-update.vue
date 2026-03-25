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
        <el-col :span="12">
          <el-form-item label="公告名称" prop="gonggaoName">
            <el-input
              v-model="ruleForm.gonggaoName"
              :readonly="type === 'info'"
              placeholder="请输入公告名称"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="公告类型" prop="gonggaoTypes">
            <template v-if="type === 'info'">
              <el-input v-model="ruleForm.gonggaoValue" readonly />
            </template>
            <template v-else>
              <el-select v-model="ruleForm.gonggaoTypes" placeholder="请选择公告类型" clearable>
                <el-option
                  v-for="item in gonggaoTypesOptions"
                  :key="item.codeIndex"
                  :label="item.indexName"
                  :value="item.codeIndex"
                />
              </el-select>
            </template>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="公告图片" prop="gonggaoPhoto">
            <template v-if="type === 'info'">
              <div v-if="ruleForm.gonggaoPhoto">
                <img
                  v-for="(item, index) in (ruleForm.gonggaoPhoto || '').split(',')"
                  :key="index"
                  :src="$base.url + item"
                  class="preview-image"
                  alt="公告图片"
                >
              </div>
              <span v-else>无</span>
            </template>
            <file-upload
              v-else
              tip="上传公告图片"
              action="file/upload"
              :limit="1"
              :multiple="false"
              :fileUrls="ruleForm.gonggaoPhoto ? $base.url + ruleForm.gonggaoPhoto : ''"
              @change="gonggaoPhotoUploadChange"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="公告详情" prop="gonggaoContent">
            <template v-if="type === 'info'">
              <div class="content-view" v-html="ruleForm.gonggaoContent" />
            </template>
            <editor
              v-else
              v-model="ruleForm.gonggaoContent"
              class="editor"
              action="file/upload"
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
      ruleForm: {
        id: '',
        gonggaoName: '',
        gonggaoPhoto: '',
        gonggaoTypes: '',
        gonggaoContent: '',
        gonggaoValue: ''
      },
      gonggaoTypesOptions: [],
      rules: {
        gonggaoName: [{ required: true, message: '请输入公告名称', trigger: 'blur' }],
        gonggaoPhoto: [{ required: true, message: '请上传公告图片', trigger: 'change' }],
        gonggaoTypes: [{ required: true, message: '请选择公告类型', trigger: 'change' }],
        gonggaoContent: [{ required: true, message: '请输入公告详情', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadGonggaoTypes()
  },
  methods: {
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        gonggaoName: '',
        gonggaoPhoto: '',
        gonggaoTypes: '',
        gonggaoContent: '',
        gonggaoValue: ''
      }
      if (this.id) {
        this.info(this.id)
      }
    },
    loadGonggaoTypes() {
      this.$http({
        url: 'dictionary/page',
        method: 'get',
        params: {
          page: 1,
          limit: 100,
          dicCode: 'gonggao_types'
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.gonggaoTypesOptions = data.data.list || []
        }
      })
    },
    info(id) {
      this.$http({
        url: `gonggao/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const detail = data.data || {}
          this.ruleForm = Object.assign(
            {
              id: '',
              gonggaoName: '',
              gonggaoPhoto: '',
              gonggaoTypes: '',
              gonggaoContent: '',
              gonggaoValue: ''
            },
            detail
          )
          if (this.ruleForm.gonggaoContent) {
            this.ruleForm.gonggaoContent = this.ruleForm.gonggaoContent.replaceAll(
              'src="upload/',
              `src="${this.$base.url}upload/`
            )
          }
        } else {
          this.$message.error(data.msg || '加载失败')
        }
      })
    },
    gonggaoPhotoUploadChange(fileUrls) {
      this.ruleForm.gonggaoPhoto = fileUrls
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('gonggaoPhoto')
      }
    },
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        const formData = { ...this.ruleForm }
        formData.gonggaoContent = (formData.gonggaoContent || '').replaceAll(this.$base.url, '')
        this.$http({
          url: `gonggao/${!formData.id ? 'save' : 'update'}`,
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

.editor {
  min-height: 300px;
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
