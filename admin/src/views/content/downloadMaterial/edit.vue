<template>
  <div class="divBox">
    <pages-header
      :title="$route.params.id ? '编辑资料' : '新增资料'"
      back-url="/content/downloadMaterialManager"
    />
    <el-card class="box-card mt14">
      <div class="components-container">
        <el-form ref="form" label-width="110px" :model="form">
          <el-form-item label="资料标题" prop="title" :rules="[{ required: true, message: '请填写资料标题', trigger: ['blur', 'change'] }]">
            <el-input v-model="form.title" class="selWidth" maxlength="255" />
          </el-form-item>
          <el-form-item label="资料分类" prop="categoryId" :rules="[{ required: true, message: '请选择资料分类', trigger: ['blur', 'change'] }]">
            <el-select v-model="form.categoryId" class="selWidth" placeholder="请选择资料分类">
              <el-option v-for="item in categoryTreeData" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="资料简介" prop="description">
            <el-input v-model="form.description" class="selWidth" type="textarea" :rows="3" maxlength="500" />
          </el-form-item>
          <el-form-item label="下载文件" prop="fileUrl" :rules="[{ required: true, message: '请上传资料文件', trigger: 'change' }]">
            <el-input v-model="form.fileUrl" class="selWidth" placeholder="请通过上传选择文件">
              <el-button slot="append" @click="modalFileTap">选择文件</el-button>
            </el-input>
          </el-form-item>
          <el-form-item label="文件类型" prop="fileType">
            <el-input v-model="form.fileType" class="selWidth" maxlength="32" placeholder="可留空自动识别" />
          </el-form-item>
          <el-form-item label="封面图" prop="coverImage">
            <div class="upLoadPicBox" @click="modalPicTap">
              <div v-if="form.coverImage" class="pictrue"><img :src="form.coverImage"></div>
              <div v-else class="upLoad"><i class="el-icon-camera cameraIconfont" /></div>
            </div>
          </el-form-item>
          <el-form-item label="公开展示">
            <el-switch v-model="form.isPublic" />
          </el-form-item>
          <el-form-item label="需要留资">
            <el-switch v-model="form.needForm" />
          </el-form-item>
          <el-form-item label="启用状态">
            <el-switch v-model="form.status" />
          </el-form-item>
          <el-form-item label="排序">
            <el-input-number v-model="form.sort" :min="0" :max="99999" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as categoryApi from '@/api/categoryApi.js'
import { createDownloadMaterial, getDownloadMaterialInfo, updateDownloadMaterial } from '@/api/downloadMaterial'
import { Debounce } from '@/utils/validate'

export default {
  data() {
    return {
      loading: false,
      categoryTreeData: [],
      form: {
        title: '',
        categoryId: null,
        description: '',
        fileUrl: '',
        fileType: '',
        coverImage: '',
        isPublic: true,
        needForm: false,
        status: true,
        sort: 0
      }
    }
  },
  mounted() {
    this.handlerGetCategoryTreeData()
    if (this.$route.params.id) {
      this.getInfo()
    }
  },
  methods: {
    handlerGetCategoryTreeData() {
      categoryApi.listCategroy({ type: 2, status: '' }).then((data) => {
        this.categoryTreeData = data
      })
    },
    getInfo() {
      getDownloadMaterialInfo(this.$route.params.id).then((data) => {
        this.form = {
          title: data.title || '',
          categoryId: data.categoryId,
          description: data.description || '',
          fileUrl: data.fileUrl || '',
          fileType: data.fileType || '',
          coverImage: data.coverImage || '',
          isPublic: data.isPublic,
          needForm: data.needForm,
          status: data.status,
          sort: data.sort || 0
        }
      })
    },
    modalPicTap() {
      this.$modalUpload((img) => {
        this.form.coverImage = img[0].sattDir
      }, '1', 'content')
    },
    modalFileTap() {
      this.$modalUpload((img) => {
        this.form.fileUrl = img[0].sattDir
      }, 'file', 'content')
    },
    handleSubmit: Debounce(function() {
      this.$refs.form.validate((valid) => {
        if (!valid) return
        this.loading = true
        const request = this.$route.params.id
          ? updateDownloadMaterial(this.$route.params.id, this.form)
          : createDownloadMaterial(this.form)
        request
          .then(() => {
            this.$message.success(this.$route.params.id ? '编辑资料成功' : '新增资料成功')
            this.$router.push({ path: '/content/downloadMaterialManager' })
          })
          .finally(() => {
            this.loading = false
          })
      })
    })
  }
}
</script>

<style scoped>
.selWidth {
  width: 460px !important;
}
</style>
