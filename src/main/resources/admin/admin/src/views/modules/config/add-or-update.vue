<template>
  <div class="addEdit-block banner-edit-page">
    <div class="form-intro" v-if="type !== 'info'">
      <div>
        <h3>{{ ruleForm.id ? '编辑轮播图' : '新增轮播图' }}</h3>
        <p>支持上传多张图片，上传顺序即首页展示顺序。</p>
      </div>
    </div>

    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="110px"
      class="detail-form-content"
    >
      <el-row :gutter="18">
        <el-col :span="12">
          <el-form-item label="轮播图名称" prop="name">
            <el-input
              v-model="ruleForm.name"
              :readonly="type === 'info'"
              placeholder="例如：首页轮播"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="轮播图片" prop="value">
            <div class="upload-tip" v-if="type !== 'info'">
              建议上传横向图片，并保持统一尺寸，首页展示会更整齐。
            </div>

            <template v-if="type === 'info'">
              <div v-if="parseImages(ruleForm.value).length" class="preview-wrap">
                <div
                  v-for="(item, index) in parseImages(ruleForm.value)"
                  :key="index"
                  class="preview-card"
                >
                  <img :src="toImageUrl(item)" class="preview-image" alt="轮播图">
                  <span class="preview-order">第 {{ index + 1 }} 张</span>
                </div>
              </div>
              <div v-else class="empty-inline">暂无图片</div>
            </template>

            <file-upload
              v-else
              tip="上传轮播图片，可连续上传多张"
              action="file/upload"
              :limit="8"
              :multiple="true"
              :fileUrls="ruleForm.value ? toImageUrl(ruleForm.value) : ''"
              @change="valueUploadChange"
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
        name: '',
        value: ''
      },
      rules: {
        name: [{ required: true, message: '请输入轮播图名称', trigger: 'blur' }],
        value: [{ required: true, message: '请至少上传一张轮播图', trigger: 'change' }]
      }
    }
  },
  methods: {
    toImageUrl(path) {
      if (!path) return ''
      return /^https?:\/\//i.test(path) ? path : this.$base.url + path
    },
    parseImages(value) {
      if (!value) return []
      return String(value)
        .split(',')
        .map(item => item.trim())
        .filter(Boolean)
    },
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        name: '',
        value: ''
      }
      if (this.id) {
        this.info(this.id)
      }
    },
    info(id) {
      this.$http({
        url: 'config/info/' + id,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = Object.assign({
            id: '',
            name: '',
            value: ''
          }, data.data || {})
        } else {
          this.$message.error((data && data.msg) || '加载失败')
        }
      })
    },
    valueUploadChange(fileUrls) {
      this.ruleForm.value = fileUrls
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('value')
      }
    },
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        const formData = Object.assign({}, this.ruleForm)
        this.$http({
          url: 'config/' + (!formData.id ? 'save' : 'update'),
          method: 'post',
          data: formData
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.parent.showFlag = true
            this.parent.addOrUpdateFlag = false
            this.parent.getDataList()
          } else {
            this.$message.error((data && data.msg) || '保存失败')
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
.banner-edit-page {
  .form-intro {
    margin-bottom: 14px;
    padding: 16px 18px;
    border-radius: 18px;
    background: linear-gradient(180deg, #f8fbff, #eef5ff);
    border: 1px solid #d9e7f8;
  }

  .form-intro h3 {
    margin: 0;
    color: #163f65;
    font-size: 22px;
  }

  .form-intro p {
    margin: 6px 0 0;
    color: #6b86a1;
    font-size: 13px;
  }

  .upload-tip {
    margin-bottom: 10px;
    color: #6a8096;
    font-size: 13px;
  }

  .preview-wrap {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
  }

  .preview-card {
    position: relative;
    width: 168px;
    height: 108px;
    border-radius: 14px;
    overflow: hidden;
    border: 1px solid #d9e7f8;
    background: #f3f8fe;
  }

  .preview-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .preview-order {
    position: absolute;
    left: 10px;
    top: 10px;
    min-width: 56px;
    height: 26px;
    padding: 0 10px;
    border-radius: 999px;
    background: rgba(7, 34, 65, 0.72);
    color: #fff;
    font-size: 12px;
    line-height: 26px;
    text-align: center;
  }

  .empty-inline {
    color: #6d87a0;
  }
}
</style>
