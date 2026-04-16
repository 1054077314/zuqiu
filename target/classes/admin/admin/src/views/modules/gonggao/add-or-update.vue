<template>
  <div class="addEdit-block">
    <div v-if="type === 'info'" class="gonggao-detail-page">
      <div class="gonggao-detail-card">
        <div class="detail-header">
          <h1 class="detail-title">{{ ruleForm.gonggaoName || '未命名公告' }}</h1>
          <span class="detail-type-tag">{{ ruleForm.gonggaoValue || '未分类' }}</span>
        </div>

        <div class="detail-image-section">
          <div class="detail-image-box" :class="{ 'is-empty': !ruleForm.gonggaoPhoto }">
            <img
              v-if="ruleForm.gonggaoPhoto"
              :src="$base.url + (ruleForm.gonggaoPhoto || '').split(',')[0]"
              class="detail-image"
              alt="公告图片"
              @error="$event.target.style.display='none'; $event.target.parentNode.classList.add('is-empty')"
            >
            <div class="image-placeholder">暂无公告图片</div>
          </div>
        </div>

        <div class="detail-content-section">
          <h3 class="content-title">公告详情</h3>
          <div
            v-if="ruleForm.gonggaoContent"
            class="detail-content"
            v-html="ruleForm.gonggaoContent"
          />
          <div v-else class="content-empty">暂无公告详情</div>
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
      <el-row :gutter="16" class="detail-row-first">
        <el-col :span="12">
          <el-form-item label="公告名称" prop="gonggaoName">
            <el-input
              v-model="ruleForm.gonggaoName"
              placeholder="请输入公告名称"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="公告类型" prop="gonggaoTypes">
            <el-select v-model="ruleForm.gonggaoTypes" placeholder="请选择公告类型" clearable>
              <el-option
                v-for="item in gonggaoTypesOptions"
                :key="item.codeIndex"
                :label="item.indexName"
                :value="item.codeIndex"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="公告图片" prop="gonggaoPhoto">
            <file-upload
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
            <editor
              v-model="ruleForm.gonggaoContent"
              class="editor"
              action="file/upload"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item class="btn">
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button class="back-btn" @click="back">取消</el-button>
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

.gonggao-detail-page {
  width: 100%;
}

.gonggao-detail-card {
  background: #ffffff;
  border: 1px solid #dbeafe;
  border-radius: 14px;
  box-shadow: 0 8px 20px rgba(37, 99, 235, 0.09);
  padding: 26px 30px 20px;
}

.detail-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 28px;
  flex-wrap: wrap;
}

.detail-title {
  margin: 0;
  font-size: 36px;
  line-height: 1.28;
  color: #1e3a8a;
  font-weight: 800;
}

.detail-type-tag {
  display: inline-flex;
  align-items: center;
  padding: 6px 14px;
  border-radius: 999px;
  background: #eff6ff;
  border: 1px solid #bfdbfe;
  color: #1d4ed8;
  font-size: 13px;
  font-weight: 600;
}

.detail-image-section {
  margin-bottom: 26px;
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
  height: 320px;
  object-fit: cover;
  border-radius: 12px;
  background: #f8fbff;
}

.image-placeholder {
  width: 100%;
  min-height: 220px;
  border-radius: 12px;
  border: 1px dashed #c7dcff;
  background: #f8fbff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6b7280;
  font-size: 15px;
}

.detail-content-section {
  border-radius: 0;
  border: none;
  background: transparent;
  padding: 0;
}

.content-title {
  margin: 0 0 14px;
  font-size: 18px;
  line-height: 1.4;
  color: #1d4ed8;
  font-weight: 700;
}

.detail-content {
  color: #1f2937;
  line-height: 1.95;
  word-break: break-word;
  white-space: pre-wrap;
  background: #ffffff;
  padding: 2px 0;
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
  transform: translateY(-1px);
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.3);
}

.detail-form-content {
  border-radius: 16px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.62);
  box-shadow: 0 12px 26px rgba(15, 23, 42, 0.08);
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
  padding: 24px 22px 18px;
}

.detail-form-content ::v-deep .el-form-item__label {
  color: #111827;
  font-weight: 700;
  letter-spacing: 0.01em;
}

.detail-form-content ::v-deep .el-input__inner,
.detail-form-content ::v-deep .el-textarea__inner,
.detail-form-content ::v-deep .el-select .el-input__inner {
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 10px;
}

.detail-form-content ::v-deep .el-input__inner:focus,
.detail-form-content ::v-deep .el-textarea__inner:focus {
  border-color: rgba(59, 130, 246, 0.5);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.12);
}

.editor {
  min-height: 300px;
}

.detail-form-content ::v-deep .btn .el-form-item__content {
  border-top: none !important;
  margin-left: 0 !important;
  justify-content: flex-end;
  padding-top: 16px;
  position: relative;
}

.detail-form-content ::v-deep .btn .el-form-item__content::before {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  height: 1px;
  background: linear-gradient(
    90deg,
    rgba(0, 0, 0, 0),
    rgba(59, 130, 246, 0.35),
    rgba(0, 0, 0, 0)
  );
}

.back-btn {
  border-radius: 10px;
}

@media (max-width: 768px) {
  .addEdit-block {
    max-width: 100%;
    padding: 6px 0 0;
  }

  .gonggao-detail-card {
    padding: 18px 14px 14px;
  }

  .detail-title {
    font-size: 28px;
  }

  .detail-image {
    height: 220px;
  }

  .image-placeholder {
    min-height: 170px;
  }

  .detail-form-content {
    padding: 18px 14px 14px;
  }

  .detail-row-first ::v-deep .el-col {
    width: 100% !important;
    max-width: 100% !important;
    flex: 0 0 100% !important;
  }
}
</style>

