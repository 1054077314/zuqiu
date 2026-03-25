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
          <el-form-item label="赛事名称" prop="saishiName">
            <el-input
              v-model="ruleForm.saishiName"
              :readonly="type === 'info'"
              placeholder="请输入赛事名称"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="赛事地点" prop="saishiAddress">
            <el-input
              v-model="ruleForm.saishiAddress"
              :readonly="type === 'info'"
              placeholder="请输入赛事地点"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="赛事类型" prop="saishiTypes">
            <template v-if="type === 'info'">
              <el-input v-model="ruleForm.saishiValue" readonly />
            </template>
            <template v-else>
              <el-select v-model="ruleForm.saishiTypes" placeholder="请选择赛事类型" clearable>
                <el-option
                  v-for="item in saishiTypesOptions"
                  :key="item.codeIndex"
                  :label="item.indexName"
                  :value="item.codeIndex"
                />
              </el-select>
            </template>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="赛事图片" prop="saishiPhoto">
            <template v-if="type === 'info'">
              <div v-if="ruleForm.saishiPhoto">
                <img
                  v-for="(item, index) in (ruleForm.saishiPhoto || '').split(',')"
                  :key="index"
                  :src="$base.url + item"
                  class="preview-image"
                  alt="赛事图片"
                >
              </div>
              <span v-else>无</span>
            </template>
            <file-upload
              v-else
              tip="上传赛事图片"
              action="file/upload"
              :limit="1"
              :multiple="false"
              :fileUrls="ruleForm.saishiPhoto ? $base.url + ruleForm.saishiPhoto : ''"
              @change="saishiPhotoUploadChange"
            />
          </el-form-item>
        </el-col>
<el-col :span="24">
          <el-form-item label="赛事介绍" prop="saishiContent">
            <template v-if="type === 'info'">
              <div class="content-view" v-html="ruleForm.saishiContent" />
            </template>
            <editor
              v-else
              v-model="ruleForm.saishiContent"
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
      saishiTypesOptions: [],
      ruleForm: {
        id: '',
        saishiName: '',
        saishiPhoto: '',
        saishiAddress: '',        saishiTypes: '',
        saishiValue: '',
        saishiContent: '',
        saishiDelete: 1
      },
      rules: {
        saishiName: [{ required: true, message: '请输入赛事名称', trigger: 'blur' }],
        saishiPhoto: [{ required: true, message: '请上传赛事图片', trigger: 'change' }],
        saishiAddress: [{ required: true, message: '请输入赛事地点', trigger: 'blur' }],        saishiTypes: [{ required: true, message: '请选择赛事类型', trigger: 'change' }],
        saishiContent: [{ required: true, message: '请输入赛事介绍', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadSaishiTypes()
  },
  methods: {
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        saishiName: '',
        saishiPhoto: '',
        saishiAddress: '',        saishiTypes: '',
        saishiValue: '',
        saishiContent: '',
        saishiDelete: 1
      }
      if (this.id) {
        this.info(this.id)
      }
    },
    loadSaishiTypes() {
      this.$http({
        url: 'dictionary/page',
        method: 'get',
        params: {
          page: 1,
          limit: 100,
          dicCode: 'saishi_types'
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.saishiTypesOptions = data.data.list || []
        }
      })
    },
    info(id) {
      this.$http({
        url: `saishi/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const detail = data.data || {}
          this.ruleForm = Object.assign(
            {
              id: '',
              saishiName: '',
              saishiPhoto: '',
              saishiAddress: '',              saishiTypes: '',
              saishiValue: '',
              saishiContent: '',
              saishiDelete: 1
            },
            detail
          )
          if (this.ruleForm.saishiContent) {
            const base = this.$base.url.replace(/\/+$/, '')
            this.ruleForm.saishiContent = this.ruleForm.saishiContent
              .replaceAll('src="upload/', `src="${base}/upload/`)
              .replaceAll('src="/upload/', `src="${base}/upload/`)
          }
        } else {
          this.$message.error(data.msg || '加载失败')
        }
      })
    },
    saishiPhotoUploadChange(fileUrls) {
      this.ruleForm.saishiPhoto = fileUrls
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('saishiPhoto')
      }
    },
},
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        const formData = { ...this.ruleForm }
        formData.saishiContent = (formData.saishiContent || '')
          .replaceAll(this.$base.url, '')
          .replaceAll(this.$base.url.replace(/\/+$/, '') + '/', '')
        delete formData.saishiValue
        this.$http({
          url: `saishi/${!formData.id ? 'save' : 'update'}`,
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

.link-btn {
  color: #409eff;
  text-decoration: none;
}

.content-view {
  min-height: 100px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 8px;
}

.editor {
  min-height: 300px;
}
</style>


