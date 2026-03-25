<template>
  <div class="file-upload-wrap">
    <el-upload
      ref="upload"
      :action="getActionUrl"
      list-type="picture-card"
      :multiple="multiple"
      :limit="limit"
      :headers="headers"
      :file-list="fileList"
      :on-exceed="handleExceed"
      :on-preview="handleUploadPreview"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadErr"
      :before-upload="handleBeforeUpload"
      class="upload-card"
    >
      <i class="el-icon-plus"></i>
      <div slot="tip" class="el-upload__tip upload-tip">{{ tip }}</div>
    </el-upload>

    <el-dialog
      :visible.sync="dialogVisible"
      width="640px"
      append-to-body
      custom-class="upload-preview-dialog"
      title="图片预览"
    >
      <div class="dialog-image-wrap">
        <img class="dialog-image" :src="dialogImageUrl" alt="预览图片">
      </div>
    </el-dialog>
  </div>
</template>

<script>
import storage from '@/utils/storage'

export default {
  props: ['tip', 'action', 'limit', 'multiple', 'fileUrls'],
  data() {
    return {
      dialogVisible: false,
      dialogImageUrl: '',
      fileList: [],
      fileUrlList: [],
      headers: {}
    }
  },
  computed: {
    getActionUrl() {
      return '/' + this.$base.name + '/' + this.action
    }
  },
  watch: {
    fileUrls() {
      this.init()
    }
  },
  mounted() {
    this.headers = {
      Token: storage.get('Token')
    }
    this.init()
  },
  methods: {
    init() {
      const source = String(this.fileUrls || '').trim()
      if (!source) {
        this.fileList = []
        this.fileUrlList = []
        return
      }

      const token = storage.get('Token') || storage.get('token') || ''
      const urls = source.split(',').map(item => item.trim()).filter(Boolean)

      this.fileUrlList = urls
      this.fileList = urls.map((url, index) => {
        const cleanUrl = url.split('?')[0]
        return {
          name: String(index + 1),
          url: token ? cleanUrl + '?token=' + token : cleanUrl
        }
      })
    },
    handleBeforeUpload() {},
    handleUploadSuccess(res, file, fileList) {
      if (res && res.code === 0) {
        const uploadedUrl = 'upload/' + res.file
        fileList[fileList.length - 1].url = uploadedUrl
        this.setFileList(fileList)
        this.$emit('change', this.fileUrlList.join(','))
      } else {
        this.$message.error((res && res.msg) || '上传失败')
      }
    },
    handleUploadErr() {
      this.$message.error('文件上传失败')
    },
    handleRemove(file, fileList) {
      this.setFileList(fileList)
      this.$emit('change', this.fileUrlList.join(','))
    },
    handleUploadPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleExceed() {
      this.$message.warning('最多上传 ' + this.limit + ' 张图片')
    },
    setFileList(fileList) {
      const token = storage.get('Token') || storage.get('token') || ''
      const fileArray = []
      const fileUrlArray = []

      fileList.forEach(item => {
        const cleanUrl = (item.url || '').split('?')[0]
        if (!cleanUrl) return
        fileArray.push({
          name: item.name,
          url: token ? cleanUrl + '?token=' + token : cleanUrl
        })
        fileUrlArray.push(cleanUrl)
      })

      this.fileList = fileArray
      this.fileUrlList = fileUrlArray
    }
  }
}
</script>

<style lang="scss" scoped>
.file-upload-wrap {
  .upload-tip {
    color: #6b86a1 !important;
    line-height: 1.6;
  }

  ::v-deep .el-upload--picture-card,
  ::v-deep .el-upload-list--picture-card .el-upload-list__item {
    border-radius: 16px;
    border-color: #cfe0f1;
    background: linear-gradient(180deg, #fbfdff, #eef5ff);
  }

  ::v-deep .el-upload--picture-card {
    width: 148px;
    height: 148px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s ease;
  }

  ::v-deep .el-upload--picture-card:hover {
    border-color: #0d8bf2;
    box-shadow: 0 0 0 3px rgba(13, 139, 242, 0.12);
  }

  ::v-deep .el-upload-list--picture-card .el-upload-list__item {
    width: 148px;
    height: 148px;
    overflow: hidden;
  }

  ::v-deep .el-upload-list__item-actions {
    border-radius: 16px;
    background-color: rgba(7, 34, 65, 0.58);
  }
}

.dialog-image-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 260px;
}

.dialog-image {
  max-width: 100%;
  max-height: 520px;
  border-radius: 16px;
  box-shadow: 0 16px 30px rgba(12, 43, 77, 0.16);
}
</style>

