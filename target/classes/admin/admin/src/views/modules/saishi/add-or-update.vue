<template>
  <div class="addEdit-block">
    <div v-if="type === 'info'" class="saishi-detail-page">
      <div class="saishi-detail-card">
        <h1 class="detail-title">{{ ruleForm.saishiName || '未命名赛事' }}</h1>

        <div class="detail-meta">
          <span>{{ ruleForm.saishiValue || '未分类' }}</span>
          <span class="meta-divider">｜</span>
          <span>{{ ruleForm.saishiAddress || '未填写地点' }}</span>
        </div>

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

        <div class="detail-actions">
          <el-button type="primary" class="detail-back-btn" @click="back">返回</el-button>
        </div>
      </div>
    </div>

    <el-form
      v-else
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
  max-width: 900px;
  margin: 0 auto;
  padding: 8px 0 0;
}

.saishi-detail-page {
  width: 100%;
}

.saishi-detail-card {
  background: #ffffff;
  border: 1px solid #dbeafe;
  border-radius: 14px;
  box-shadow: 0 8px 20px rgba(37, 99, 235, 0.08);
  padding: 28px 30px 20px;
}

.detail-title {
  margin: 0;
  font-size: 38px;
  line-height: 1.25;
  color: #1e3a8a;
  font-weight: 800;
}

.detail-meta {
  margin-top: 12px;
  margin-bottom: 24px;
  color: #2563eb;
  font-size: 15px;
  line-height: 1.6;
}

.meta-divider {
  margin: 0 8px;
  color: #93c5fd;
}

.detail-image-section {
  margin-bottom: 24px;
}

.detail-image-box {
  position: relative;
  width: 100%;
  min-height: 220px;
  border-radius: 12px;
  overflow: hidden;
  background: #f8fbff;
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
  height: 340px;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  min-height: 220px;
  border-radius: 12px;
  border: 1px dashed #c7dcff;
  background: #f8fbff;
  color: #6b7280;
  font-size: 15px;
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
  margin: 0 0 12px;
  color: #1d4ed8;
  font-size: 19px;
  font-weight: 700;
}

.detail-content {
  color: #1f2937;
  line-height: 1.95;
  word-break: break-word;
  white-space: pre-wrap;
}

.content-empty {
  color: #9ca3af;
  line-height: 1.8;
}

.detail-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 18px;
}

.detail-back-btn {
  border: none;
  border-radius: 999px;
  padding: 10px 26px;
  font-weight: 600;
  background: linear-gradient(135deg, #60a5fa, #2563eb);
  box-shadow: 0 8px 16px rgba(37, 99, 235, 0.24);
}

.detail-back-btn:hover,
.detail-back-btn:focus {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
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

  .detail-title {
    font-size: 30px;
  }

  .detail-image {
    height: 220px;
  }

  .image-placeholder {
    min-height: 170px;
  }
}
</style>

