<template>
  <div class="addEdit-block">
    <detail-showcase v-if="type === 'info'" module-name="saishi" :record="ruleForm" :base-url="$base.url" @back="back" />
    <div v-if="false" class="saishi-detail-page">
      <div class="saishi-detail-card">
        <div class="detail-header">
          <div>
            <h1 class="detail-title">{{ ruleForm.saishiName || '未命名赛事' }}</h1>

            <div class="detail-meta">
              <span>{{ ruleForm.saishiValue || '未分类' }}</span>
              <span class="meta-divider">｜</span>
              <span>{{ ruleForm.saishiAddress || '未填写地点' }}</span>
            </div>
          </div>
          <el-button type="primary" class="detail-back-btn top-back" @click="back">返回</el-button>
        </div>

        <div class="detail-body">
          <div class="detail-image-section">
            <div class="detail-image-box" :class="{ 'is-empty': !ruleForm.saishiPhoto }">
              <img
                v-if="ruleForm.saishiPhoto"
                :src="$base.url + (ruleForm.saishiPhoto || '').split(',')[0]"
                class="detail-image"
                alt="赛事图片"
                @error="$event.target.style.display='none'; $event.target.parentNode.classList.add('is-empty')"
              >
              <div class="image-placeholder">暂无赛事图片</div>
            </div>
          </div>

          <div class="detail-content-section">
            <h3 class="content-title">赛事介绍</h3>
            <div
              v-if="ruleForm.saishiContent"
              class="detail-content"
              v-html="ruleForm.saishiContent"
            />
            <div v-else class="content-empty">暂无赛事介绍</div>
          </div>
        </div>
      </div>
    </div>

    <el-form
      v-if="type !== 'info'"
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
              placeholder="请输入赛事名称"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="赛事地点" prop="saishiAddress">
            <el-input
              v-model="ruleForm.saishiAddress"
              placeholder="请输入赛事地点"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="赛事类型" prop="saishiTypes">
            <el-select v-model="ruleForm.saishiTypes" placeholder="请选择赛事类型" clearable>
              <el-option
                v-for="item in saishiTypesOptions"
                :key="item.codeIndex"
                :label="item.indexName"
                :value="item.codeIndex"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="赛事图片" prop="saishiPhoto">
            <file-upload
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
            <editor
              v-model="ruleForm.saishiContent"
              class="editor"
              action="file/upload"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item class="btn">
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="back">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import DetailShowcase from '@/components/common/DetailShowcase.vue'
export default { components: { DetailShowcase },
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
        saishiAddress: '',
        saishiTypes: '',
        saishiValue: '',
        saishiContent: '',
        saishiDelete: 1
      },
      rules: {
        saishiName: [{ required: true, message: '请输入赛事名称', trigger: 'blur' }],
        saishiPhoto: [{ required: true, message: '请上传赛事图片', trigger: 'change' }],
        saishiAddress: [{ required: true, message: '请输入赛事地点', trigger: 'blur' }],
        saishiTypes: [{ required: true, message: '请选择赛事类型', trigger: 'change' }],
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
        saishiAddress: '',
        saishiTypes: '',
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
              saishiAddress: '',
              saishiTypes: '',
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
      if (this.type === 'info') {
        this.$router.push({ path: '/home' })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.addEdit-block {
  width: 100%;
  max-width: 1240px;
  margin: 0 auto;
  padding: 4px 0 0;
  box-sizing: border-box;
}

.saishi-detail-page {
  width: 100%;
}

.saishi-detail-card {
  background: #ffffff;
  border: 1px solid #d8e1ee;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(15, 23, 42, 0.05);
  padding: 26px 28px;
  box-sizing: border-box;
}

.detail-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 24px;
  padding-bottom: 18px;
  border-bottom: 1px solid #e2e8f0;
}

.detail-title {
  margin: 0;
  font-size: 28px;
  line-height: 1.25;
  color: #102a66;
  font-weight: 800;
}

.detail-meta {
  margin-top: 10px;
  color: #2563eb;
  font-size: 14px;
  line-height: 1.6;
}

.meta-divider {
  margin: 0 8px;
  color: #93c5fd;
}

.detail-body {
  display: block;
  padding-top: 22px;
}

.detail-image-section {
  margin-bottom: 22px;
}

.detail-image-box {
  position: relative;
  width: 100%;
  min-height: 170px;
  border-radius: 10px;
  overflow: hidden;
  background: #f7f9fc;
}

.detail-image-box .image-placeholder {
  display: none;
}

.detail-image-box.is-empty .image-placeholder {
  display: flex;
}

.detail-image {
  display: block;
  width: 100%;
  height: 240px;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  min-height: 170px;
  border-radius: 10px;
  border: 1px dashed #c8d6ea;
  background: #f7f9fc;
  color: #6b7280;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.detail-content-section {
  padding: 0;
  border: 0;
  background: transparent;
}

.content-title {
  margin: 0 0 10px;
  color: #0b57d0;
  font-size: 20px;
  font-weight: 800;
}

.detail-content {
  color: #1f2937;
  font-size: 15px;
  line-height: 1.8;
  word-break: break-word;
  white-space: pre-wrap;
}

.content-empty {
  color: #9ca3af;
  line-height: 1.8;
}

.detail-back-btn {
  border: none;
  border-radius: 8px;
  padding: 9px 24px;
  font-weight: 600;
  background: #2563eb;
  box-shadow: 0 6px 14px rgba(37, 99, 235, 0.2);
}

.detail-back-btn:hover,
.detail-back-btn:focus {
  background: #1d4ed8;
}

.top-back {
  flex: 0 0 auto;
}

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

@media (max-width: 768px) {
  .addEdit-block {
    max-width: 100%;
    padding: 6px 0 0;
  }

  .saishi-detail-card {
    padding: 18px 14px 14px;
  }

  .detail-header {
    gap: 14px;
    padding-bottom: 14px;
  }

  .detail-title {
    font-size: 24px;
  }

  .detail-body {
    padding-top: 18px;
  }

  .detail-image {
    height: 200px;
  }

  .image-placeholder {
    min-height: 150px;
  }
}
</style>
