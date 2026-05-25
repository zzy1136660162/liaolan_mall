<template>
  <view class="downloads-page" :data-theme="theme">
    <view class="hero">
      <view class="hero-mark"></view>
      <view class="hero-title">资料下载</view>
      <view class="hero-desc">获取最新的产品资料、技术文档和资质证书。</view>
    </view>
    <view class="search-box">
      <input
        v-model="keywords"
        confirm-type="search"
        placeholder="按标题或文件类型搜索"
        @confirm="doSearch"
      />
      <view class="search-btn" @click="doSearch">搜索</view>
    </view>
    <scroll-view class="tabs" scroll-x>
      <view class="tab" :class="{ active: activeCategory === 0 }" @click="selectCategory(0)">全部</view>
      <view
        v-for="item in categories"
        :key="item.id"
        class="tab"
        :class="{ active: activeCategory === item.id }"
        @click="selectCategory(item.id)"
      >
        {{ item.name }}
      </view>
    </scroll-view>
    <view class="material-list">
      <view v-for="item in list" :key="item.id" class="material-card">
        <view class="material-main">
          <view class="file-badge" :class="item.fileBadgeClass">{{ item.fileTypeLabel }}</view>
          <view class="material-info">
            <view class="material-title">{{ item.title }}</view>
            <view class="material-meta">
              <text>{{ item.categoryName || '未分类' }}</text>
              <text class="dot"></text>
              <text>{{ item.needForm ? '需填写表单' : '公开资料' }}</text>
              <text class="dot"></text>
              <text>{{ item.downloadCount || 0 }} 次下载</text>
            </view>
            <view class="material-desc">{{ item.description || '暂无描述' }}</view>
          </view>
        </view>
        <view class="download-btn" @click="handleDownload(item)">下载</view>
      </view>
    </view>
    <view v-if="loaded && list.length === 0" class="empty-box">暂无资料</view>
    <view v-if="list.length > 0 && !finished" class="more-btn" @click="getList">加载更多</view>
    <view v-if="showForm" class="mask" @click="closeForm"></view>
    <view v-if="showForm" class="form-popup">
      <view class="form-title">下载登记</view>
      <input v-model="form.companyName" class="form-input" placeholder="请输入公司名称" />
      <input v-model="form.contactName" class="form-input" placeholder="请输入联系人姓名" />
      <input v-model="form.phone" class="form-input" placeholder="请输入手机号" type="number" maxlength="11" />
      <textarea v-model="form.requirementDesc" class="form-textarea" placeholder="请输入需求说明"></textarea>
      <view class="form-actions">
        <view class="form-cancel" @click="closeForm">取消</view>
        <view class="form-submit" @click="submitForm">提交</view>
      </view>
    </view>
  </view>
</template>

<script>
import {
  getDownloadCategoryList,
  getDownloadMaterialList,
  getDownloadMaterialFile,
  saveDownloadMaterialRecord
} from '@/api/api.js'

const app = getApp()

export default {
  data() {
    return {
      theme: app.globalData.theme,
      categories: [],
      list: [],
      page: 1,
      limit: 10,
      finished: false,
      loaded: false,
      loading: false,
      activeCategory: 0,
      keywords: '',
      cacheKeywords: '',
      showForm: false,
      currentItem: null,
      form: {
        companyName: '',
        contactName: '',
        phone: '',
        requirementDesc: ''
      }
    }
  },
  onLoad() {
    this.getCategories()
    this.resetAndLoad()
  },
  onReachBottom() {
    this.getList()
  },
  methods: {
    getCategories() {
      getDownloadCategoryList().then((res) => {
        this.categories = res.data.list || []
      })
    },
    resetAndLoad() {
      this.page = 1
      this.finished = false
      this.loaded = false
      this.list = []
      this.getList()
    },
    getList() {
      if (this.loading || this.finished) return
      this.loading = true
      const params = {
        page: this.page,
        limit: this.limit
      }
      if (this.cacheKeywords) {
        params.keywords = this.cacheKeywords
      }
      if (this.activeCategory) {
        params.categoryId = this.activeCategory
      }
      getDownloadMaterialList(params)
        .then((res) => {
          const rows = res.data.list || []
          this.list = this.list.concat(rows.map((item) => this.normalizeMaterial(item)))
          this.finished = rows.length < this.limit
          this.page += 1
          this.loaded = true
        })
        .finally(() => {
          this.loading = false
        })
    },
    doSearch() {
      this.cacheKeywords = this.keywords
      this.resetAndLoad()
    },
    selectCategory(id) {
      this.activeCategory = id
      this.resetAndLoad()
    },
    handleDownload(item) {
      if (item.needForm) {
        this.currentItem = item
        this.showForm = true
        return
      }
      this.fetchAndDownload(item.id)
    },
    closeForm() {
      this.showForm = false
      this.currentItem = null
      this.form = {
        companyName: '',
        contactName: '',
        phone: '',
        requirementDesc: ''
      }
    },
    submitForm() {
      if (!this.form.phone) {
        return this.$util.Tips({ title: '请输入手机号' })
      }
      if (!/^1\d{10}$/.test(this.form.phone)) {
        return this.$util.Tips({ title: '手机号格式不正确' })
      }
      const materialId = this.currentItem && this.currentItem.id
      const phone = this.form.phone
      saveDownloadMaterialRecord({
        materialId,
        companyName: this.form.companyName,
        contactName: this.form.contactName,
        phone,
        requirementDesc: this.form.requirementDesc,
        sourcePage: 'download_center'
      }).then(() => {
        this.closeForm()
        this.fetchAndDownload(materialId, phone)
      })
    },
    fetchAndDownload(materialId, phone) {
      if (!materialId) return
      getDownloadMaterialFile(materialId, phone ? { phone } : {})
        .then((res) => {
          this.downloadFile(res.data)
          this.list = this.list.map((item) => {
            if (item.id !== materialId) return item
            return Object.assign({}, item, {
              downloadCount: (item.downloadCount || 0) + 1
            })
          })
        })
        .catch((err) => {
          this.$util.Tips({ title: err || '下载失败' })
        })
    },
    downloadFile(url) {
      // #ifdef H5
      window.open(url, '_blank')
      // #endif
      // #ifndef H5
      uni.downloadFile({
        url,
        success: (res) => {
          if (res.statusCode === 200) {
            uni.openDocument({
              filePath: res.tempFilePath,
              showMenu: true
            })
          }
        }
      })
      // #endif
    },
    normalizeMaterial(item) {
      const fileType = item.fileType || 'file'
      return Object.assign({}, item, {
        fileTypeLabel: this.formatFileType(fileType),
        fileBadgeClass: this.fileBadgeClass(fileType)
      })
    },
    formatFileType(type) {
      return (type || 'file').toUpperCase()
    },
    fileBadgeClass(type) {
      return (type || '').toLowerCase() === 'pdf' ? 'is-pdf' : 'is-doc'
    }
  }
}
</script>

<style lang="scss">
.downloads-page {
  min-height: 100vh;
  background: #f7f8fc;
  padding: 24rpx;
  box-sizing: border-box;
}

.hero {
  padding: 36rpx 0 24rpx;
}

.hero-mark {
  width: 8rpx;
  height: 48rpx;
  border-radius: 4rpx;
  background: #0f5bd7;
  margin-bottom: 16rpx;
}

.hero-title {
  font-size: 44rpx;
  line-height: 60rpx;
  color: #1b2230;
  font-weight: 600;
}

.hero-desc {
  margin-top: 12rpx;
  font-size: 26rpx;
  line-height: 40rpx;
  color: #6a7280;
}

.search-box {
  display: flex;
  align-items: center;
  background: #fff;
  border: 1px solid #d7dce8;
  border-radius: 12rpx;
  overflow: hidden;
}

.search-box input {
  flex: 1;
  height: 88rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.search-btn {
  width: 144rpx;
  height: 88rpx;
  background: #0f5bd7;
  color: #fff;
  font-size: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tabs {
  white-space: nowrap;
  margin: 28rpx 0 20rpx;
}

.tab {
  display: inline-flex;
  align-items: center;
  height: 72rpx;
  margin-right: 20rpx;
  color: #707789;
  font-size: 28rpx;
  border-bottom: 4rpx solid transparent;
}

.tab.active {
  color: #0f5bd7;
  border-bottom-color: #0f5bd7;
  font-weight: 600;
}

.material-card {
  background: #fff;
  border: 1px solid #d7dce8;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.material-main {
  display: flex;
  align-items: flex-start;
}

.file-badge {
  width: 88rpx;
  height: 88rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22rpx;
  font-weight: 600;
  flex-shrink: 0;
}

.file-badge.is-pdf {
  color: #c9342f;
  background: #fde9e7;
}

.file-badge.is-doc {
  color: #0f5bd7;
  background: #e9f0ff;
}

.material-info {
  flex: 1;
  margin-left: 20rpx;
  min-width: 0;
}

.material-title {
  font-size: 30rpx;
  line-height: 42rpx;
  color: #1b2230;
  font-weight: 600;
}

.material-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  color: #7b8495;
  font-size: 22rpx;
  margin-top: 10rpx;
}

.dot {
  width: 6rpx;
  height: 6rpx;
  border-radius: 50%;
  background: #b5bcc8;
  margin: 0 10rpx;
}

.material-desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 36rpx;
  color: #5d6675;
}

.download-btn {
  height: 84rpx;
  margin-top: 20rpx;
  border-radius: 12rpx;
  background: #0f5bd7;
  color: #fff;
  font-size: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.more-btn,
.empty-box {
  height: 84rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6f7788;
  font-size: 26rpx;
}

.mask {
  position: fixed;
  inset: 0;
  background: rgba(24, 28, 35, 0.45);
}

.form-popup {
  position: fixed;
  left: 24rpx;
  right: 24rpx;
  top: 50%;
  transform: translateY(-50%);
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx 24rpx;
  z-index: 10;
}

.form-title {
  font-size: 34rpx;
  font-weight: 600;
  color: #1b2230;
  margin-bottom: 24rpx;
}

.form-input,
.form-textarea {
  width: 100%;
  box-sizing: border-box;
  border: 1px solid #d7dce8;
  border-radius: 12rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  margin-bottom: 18rpx;
  background: #fff;
}

.form-input {
  height: 84rpx;
}

.form-textarea {
  height: 180rpx;
  padding-top: 20rpx;
}

.form-actions {
  display: flex;
  gap: 16rpx;
}

.form-cancel,
.form-submit {
  flex: 1;
  height: 84rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.form-cancel {
  background: #edf1f7;
  color: #4f596a;
}

.form-submit {
  background: #0f5bd7;
  color: #fff;
}
</style>