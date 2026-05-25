<template>
  <div class="divBox">
    <el-card :bordered="false" shadow="never" class="ivu-mt" :body-style="{ padding: '24px' }">
      <div slot="header" class="clearfix">
        <span>联系方式配置</span>
      </div>
      <el-form ref="configForm" :model="configForm" :rules="configRules" label-width="120px" size="small">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="configForm.companyName" maxlength="100" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="consumerHotline">
              <el-input v-model="configForm.consumerHotline" maxlength="20" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司地址" prop="companyAddress">
              <el-input v-model="configForm.companyAddress" placeholder="请输入公司地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地图链接" prop="mapLink">
              <el-input v-model="configForm.mapLink" placeholder="请输入腾讯/百度地图H5链接" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="在线客服链接" prop="serviceLink">
              <el-input v-model="configForm.serviceLink" placeholder="请输入客服H5链接" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客服二维码" prop="serviceQrcode">
              <el-input v-model="configForm.serviceQrcode" placeholder="请输入客服二维码图片地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="configForm.serviceQrcode" class="config-preview">
          <el-image
            :src="configForm.serviceQrcode"
            :preview-src-list="[configForm.serviceQrcode]"
            fit="cover"
            class="qrcode-preview"
          />
          <span class="preview-tip">二维码预览</span>
        </div>
        <el-form-item>
          <el-button v-hasPermi="['admin:contact:config:save']" type="primary" :loading="configLoading" @click="submitConfig">
            保存配置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card :bordered="false" shadow="never" class="ivu-mt mt14" :body-style="{ padding: 0 }">
      <div class="padding-add">
        <el-form inline size="small">
          <el-form-item label="联系人：">
            <el-input v-model="searchPram.name" clearable placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="手机号：">
            <el-input v-model="searchPram.mobile" clearable maxlength="11" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="来源页面：">
            <el-select v-model="searchPram.sourcePage" clearable placeholder="请选择来源页面">
              <el-option v-for="item in sourceOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="处理状态：">
            <el-select v-model="searchPram.status" clearable placeholder="请选择处理状态">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="handleSearch">搜索</el-button>
            <el-button size="small" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table v-loading="listLoading" :data="listData.list" size="mini" class="table" highlight-current-row>
        <el-table-column prop="id" label="ID" min-width="60" />
        <el-table-column prop="companyName" label="公司名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="name" label="联系人" min-width="120" />
        <el-table-column prop="mobile" label="手机号" min-width="130" />
        <el-table-column label="来源页面" min-width="120">
          <template slot-scope="scope">
            <span>{{ formatSource(scope.row.sourcePage) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="留言内容" min-width="260" show-overflow-tooltip />
        <el-table-column label="处理状态" min-width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="statusTagType(scope.row.status)">{{ scope.row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handleUserName" label="处理人" min-width="100" />
        <el-table-column prop="handleTime" label="处理时间" min-width="160" />
        <el-table-column prop="createdAt" label="提交时间" min-width="160" />
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <a v-hasPermi="['admin:contact:message:update']" @click="openHandleDialog(scope.row)">处理</a>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="searchPram.page"
        :page-sizes="constants.page.limit"
        :layout="constants.page.layout"
        :total="listData.total"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog :visible.sync="handleDialog.visible" title="处理留言" width="520px" :close-on-click-modal="false">
      <el-form ref="handleForm" :model="handleDialog.form" :rules="handleRules" label-width="100px" size="small">
        <el-form-item label="联系人">
          <div>{{ handleDialog.data.name || '-' }} / {{ handleDialog.data.mobile || '-' }}</div>
        </el-form-item>
        <el-form-item label="来源页面">
          <div>{{ formatSource(handleDialog.data.sourcePage) }}</div>
        </el-form-item>
        <el-form-item label="留言内容">
          <div class="content-box">{{ handleDialog.data.content || '-' }}</div>
        </el-form-item>
        <el-form-item label="处理状态" prop="status">
          <el-select v-model="handleDialog.form.status" placeholder="请选择处理状态" style="width: 100%">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理备注" prop="handleRemark">
          <el-input
            v-model="handleDialog.form.handleRemark"
            type="textarea"
            :rows="4"
            maxlength="255"
            show-word-limit
            placeholder="请输入处理备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleDialog.visible = false">取 消</el-button>
        <el-button type="primary" size="small" :loading="submitLoading" @click="submitHandle">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getContactConfig, getContactMessageList, handleContactMessage, saveContactConfig } from '@/api/contactMessage.js'

const urlValidator = (rule, value, callback) => {
  if (!value) {
    callback()
    return
  }
  if (/^https?:\/\/.+/.test(value)) {
    callback()
    return
  }
  callback(new Error('请输入以 http:// 或 https:// 开头的链接'))
}

const assetValidator = (rule, value, callback) => {
  if (!value) {
    callback()
    return
  }
  if (/^(https?:\/\/.+|\/.+|[A-Za-z0-9_./-]+)$/.test(value)) {
    callback()
    return
  }
  callback(new Error('请输入正确的二维码图片地址'))
}

const contactPhonePattern = /^$|^(1[3-9]\d{9}|0\d{2,3}-?\d{7,8}|400-?\d{3}-?\d{4})$/

export default {
  data() {
    return {
      constants: this.$constants,
      configLoading: false,
      listLoading: false,
      submitLoading: false,
      configForm: {
        companyName: '',
        consumerHotline: '',
        companyAddress: '',
        mapLink: '',
        serviceLink: '',
        serviceQrcode: ''
      },
      configRules: {
        companyName: [{ max: 100, message: '公司名称长度不能超过100个字符', trigger: 'blur' }],
        consumerHotline: [{ pattern: contactPhonePattern, message: '请输入正确的联系电话', trigger: 'blur' }],
        companyAddress: [{ max: 255, message: '公司地址长度不能超过255个字符', trigger: 'blur' }],
        mapLink: [{ validator: urlValidator, trigger: 'blur' }],
        serviceLink: [{ validator: urlValidator, trigger: 'blur' }],
        serviceQrcode: [{ validator: assetValidator, trigger: 'blur' }]
      },
      searchPram: {
        name: null,
        mobile: null,
        sourcePage: null,
        status: null,
        page: 1,
        limit: this.$constants.page.limit[0]
      },
      listData: {
        list: [],
        total: 0
      },
      statusOptions: [
        { label: '待处理', value: 0 },
        { label: '已联系', value: 1 },
        { label: '无效线索', value: 2 },
        { label: '转商机', value: 3 }
      ],
      sourceOptions: [
        { label: '联系我们页', value: 'contact_page' },
        { label: '商品详情', value: 'product_detail' },
        { label: '用户中心', value: 'user_center' }
      ],
      handleDialog: {
        visible: false,
        data: {},
        form: {
          status: 0,
          handleRemark: ''
        }
      },
      handleRules: {
        status: [{ required: true, message: '请选择处理状态', trigger: 'change' }],
        handleRemark: [{ max: 255, message: '处理备注长度不能超过255个字符', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadConfig()
    this.getList()
  },
  methods: {
    loadConfig() {
      getContactConfig().then((data) => {
        this.configForm = Object.assign({}, this.configForm, data || {})
      })
    },
    submitConfig() {
      this.$refs.configForm.validate((valid) => {
        if (!valid) {
          return
        }
        this.configLoading = true
        const payload = {
          companyName: this.configForm.companyName,
          consumerHotline: this.configForm.consumerHotline,
          companyAddress: this.configForm.companyAddress,
          mapLink: this.configForm.mapLink,
          serviceLink: this.configForm.serviceLink,
          serviceQrcode: this.configForm.serviceQrcode
        }
        saveContactConfig(payload)
          .then(() => {
            this.$message.success('保存成功')
            this.loadConfig()
          })
          .finally(() => {
            this.configLoading = false
          })
      })
    },
    getList() {
      this.listLoading = true
      getContactMessageList(this.searchPram)
        .then((data) => {
          this.listData = data
        })
        .finally(() => {
          this.listLoading = false
        })
    },
    handleSearch() {
      this.searchPram.page = 1
      this.getList()
    },
    handleReset() {
      this.searchPram = {
        name: null,
        mobile: null,
        sourcePage: null,
        status: null,
        page: 1,
        limit: this.$constants.page.limit[0]
      }
      this.getList()
    },
    handleSizeChange(val) {
      this.searchPram.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.searchPram.page = val
      this.getList()
    },
    openHandleDialog(row) {
      this.handleDialog.data = Object.assign({}, row)
      this.handleDialog.form = {
        status: row.status,
        handleRemark: row.handleRemark || ''
      }
      this.handleDialog.visible = true
      this.$nextTick(() => {
        this.$refs.handleForm && this.$refs.handleForm.clearValidate()
      })
    },
    submitHandle() {
      this.$refs.handleForm.validate((valid) => {
        if (!valid) {
          return
        }
        this.submitLoading = true
        handleContactMessage(this.handleDialog.data.id, this.handleDialog.form)
          .then(() => {
            this.$message.success('处理成功')
            this.handleDialog.visible = false
            this.getList()
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },
    statusTagType(status) {
      const map = {
        0: 'info',
        1: 'success',
        2: 'danger',
        3: 'warning'
      }
      return map[status] || 'info'
    },
    formatSource(value) {
      const matched = this.sourceOptions.find((item) => item.value === value)
      return matched ? matched.label : value || '-'
    }
  }
}
</script>

<style scoped lang="scss">
.mt14 {
  margin-top: 14px;
}

.config-preview {
  display: flex;
  align-items: center;
  margin: 0 0 18px 120px;
}

.qrcode-preview {
  width: 72px;
  height: 72px;
  border: 1px solid #ebeef5;
}

.preview-tip {
  margin-left: 12px;
  color: #909399;
  font-size: 12px;
}

.content-box {
  line-height: 20px;
  color: #606266;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
