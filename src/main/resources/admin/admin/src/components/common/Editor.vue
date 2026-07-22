<template>
  <div>
    <el-upload
      class="avatar-uploader"
      :action="getActionUrl"
      name="file"
      :headers="header"
      :show-file-list="false"
      :on-success="uploadSuccess"
      :on-error="uploadError"
      :before-upload="beforeUpload"
    />

    <QuillEditor
      ref="myQuillEditor"
      v-model:content="content"
      class="editor"
      content-type="html"
      :options="editorOption"
      @blur="onEditorBlur"
      @focus="onEditorFocus"
      @update:content="onEditorChange"
    />
  </div>
</template>

<script>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

const toolbarOptions = [
  ['bold', 'italic', 'underline', 'strike'],
  ['blockquote', 'code-block'],
  [{ header: 1 }, { header: 2 }],
  [{ list: 'ordered' }, { list: 'bullet' }],
  [{ script: 'sub' }, { script: 'super' }],
  [{ indent: '-1' }, { indent: '+1' }],
  [{ size: ['small', false, 'large', 'huge'] }],
  [{ header: [1, 2, 3, 4, 5, 6, false] }],
  [{ color: [] }, { background: [] }],
  [{ font: [] }],
  [{ align: [] }],
  ['clean'],
  ['image']
]

export default {
  components: {
    QuillEditor
  },
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    value: {
      type: String,
      default: ''
    },
    action: {
      type: String,
      default: ''
    },
    maxSize: {
      type: Number,
      default: 4000
    }
  },
  emits: ['update:modelValue', 'input'],
  data() {
    return {
      content: this.modelValue || this.value || '',
      quillUpdateImg: false,
      editorOption: {
        placeholder: '',
        theme: 'snow',
        modules: {
          toolbar: {
            container: toolbarOptions,
            handlers: {
              image: value => {
                if (value) {
                  const input = document.querySelector('.avatar-uploader input')
                  if (input) {
                    input.click()
                  }
                }
              }
            }
          }
        }
      },
      header: {
        Token: this.$storage.get('Token')
      }
    }
  },
  computed: {
    getActionUrl() {
      return `/${this.$base.name}/` + this.action
    }
  },
  watch: {
    modelValue(value) {
      this.setContent(value)
    },
    value(value) {
      this.setContent(value)
    }
  },
  methods: {
    setContent(value) {
      const nextValue = value || ''
      if (nextValue !== this.content) {
        this.content = nextValue
      }
    },
    onEditorBlur() {},
    onEditorFocus() {},
    onEditorChange(value) {
      this.$emit('update:modelValue', value)
      this.$emit('input', value)
    },
    beforeUpload() {
      this.quillUpdateImg = true
    },
    uploadSuccess(res) {
      const editor = this.$refs.myQuillEditor
      const quill = editor && editor.getQuill ? editor.getQuill() : null
      if (res.code === 0 && quill) {
        const selection = quill.getSelection(true)
        const index = selection ? selection.index : quill.getLength()
        quill.insertEmbed(index, 'image', this.$base.url + 'upload/' + res.file)
        quill.setSelection(index + 1)
      } else {
        this.$message.error('图片插入失败')
      }
      this.quillUpdateImg = false
    },
    uploadError() {
      this.quillUpdateImg = false
      this.$message.error('图片插入失败')
    }
  }
}
</script>

<style>
.editor {
  line-height: normal !important;
}
.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "请输入链接地址:";
}
.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0px;
  content: "保存";
  padding-right: 0px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "请输入视频地址:";
}
.ql-container {
  height: 400px;
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文本";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "标题1";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "标题2";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "标题3";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "标题4";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "标题5";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "标题6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "标准字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "衬线字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等宽字体";
}
</style>
