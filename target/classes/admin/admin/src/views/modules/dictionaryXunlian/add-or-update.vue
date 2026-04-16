<template>
  <div class="addEdit-block">
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="110px"
      class="detail-form-content"
    >
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="编码" prop="codeIndex">
            <el-input
              v-model="ruleForm.codeIndex"
              readonly
              placeholder="系统自动生成"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="训练计划类型名称" prop="indexName">
            <el-input
              v-model="ruleForm.indexName"
              :readonly="type === 'info'"
              placeholder="请输入训练计划类型名称"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="beizhu">
            <el-input
              v-model="ruleForm.beizhu"
              :readonly="type === 'info'"
              placeholder="请输入备注（可选）"
              clearable
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
const DIC_CODE = 'xunlian_types'
const DIC_NAME = '训练计划类型'

export default {
  props: ['parent'],
  data() {
    return {
      id: null,
      type: 'edit',
      ruleForm: {
        id: '',
        dicCode: DIC_CODE,
        dicName: DIC_NAME,
        codeIndex: '',
        indexName: '',
        superId: 0,
        beizhu: ''
      },
      rules: {
        codeIndex: [{ required: true, message: '编码不能为空', trigger: 'blur' }],
        indexName: [{ required: true, message: '请输入名称', trigger: 'blur' }]
      }
    }
  },
  methods: {
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        dicCode: DIC_CODE,
        dicName: DIC_NAME,
        codeIndex: '',
        indexName: '',
        superId: 0,
        beizhu: ''
      }
      if (this.id) {
        this.info(this.id)
      } else {
        this.loadMaxCodeIndex()
      }
    },
    loadMaxCodeIndex() {
      this.$http({
        url: 'dictionary/maxCodeIndex',
        method: 'post',
        data: { dicCode: DIC_CODE }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm.codeIndex = data.maxCodeIndex
        } else {
          this.$message.error(data.msg || '获取编码失败')
        }
      })
    },
    info(id) {
      this.$http({
        url: `dictionary/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = Object.assign(
            {
              id: '',
              dicCode: DIC_CODE,
              dicName: DIC_NAME,
              codeIndex: '',
              indexName: '',
              superId: 0,
              beizhu: ''
            },
            data.data || {}
          )
        } else {
          this.$message.error(data.msg || '加载失败')
        }
      })
    },
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        const formData = {
          ...this.ruleForm,
          dicCode: DIC_CODE,
          dicName: DIC_NAME,
          superId: this.ruleForm.superId || 0
        }
        this.$http({
          url: `dictionary/${!formData.id ? 'save' : 'update'}`,
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
</style>

