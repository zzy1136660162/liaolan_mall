<template>
  <view class="downloads-page">
    <!-- Hero -->
    <view class="hero">
      <text class="hero-title">资源下载中心</text>
      <text class="hero-desc">获取最新的产品目录、技术文档与企业认证，助力您的工程项目高效推进。</text>
    </view>

    <!-- Filter Chips -->
    <scroll-view class="filter-scroll" scroll-x>
      <view class="filter-chips">
        <view class="chip" :class="{ active: activeCategory === 0 }" @click="selectCategory(0)">全部资源</view>
        <view
          v-for="item in categories"
          :key="item.id"
          class="chip"
          :class="{ active: activeCategory === item.id }"
          @click="selectCategory(item.id)"
        >{{ item.name }}</view>
      </view>
    </scroll-view>

    <!-- Search -->
    <view class="search-row">
      <view class="search-box">
        <text class="iconfont icon-sousuo search-icon"></text>
        <input
          v-model="keywords"
          class="search-input"
          confirm-type="search"
          placeholder="搜索资料名称"
          @confirm="doSearch"
        />
      </view>
    </view>

    <!-- Bento Grid -->
    <view class="bento-grid" v-if="list.length > 0">
      <view v-for="item in list" :key="item.id" class="doc-card" :class="item.coverUrl ? 'doc-card-image' : 'doc-card-icon'">
        <!-- Image Card: with cover preview -->
        <template v-if="item.coverUrl">
          <view class="card-cover">
            <image :src="item.coverUrl" mode="aspectFill" class="cover-img" />
            <view class="cover-tag">{{ item.categoryName || item.fileTypeLabel }}</view>
          </view>
          <view class="card-body">
            <text class="card-title">{{ item.title }}</text>
            <text class="card-desc">{{ item.description || '暂无描述' }}</text>
            <view class="card-footer">
              <text class="card-meta">{{ item.fileTypeLabel }} · {{ item.fileSize || '未知大小' }}</text>
              <view class="card-btn" @click="handleDownload(item)">
                <text class="btn-text">下载</text>
                <text class="iconfont icon-xiazailiang btn-icon"></text>
              </view>
            </view>
          </view>
        </template>
        <!-- Icon Card: without image (certifications etc.) -->
        <template v-else>
          <view class="card-icon-row">
            <view class="card-icon-box">
              <text class="iconfont icon-wendang card-icon"></text>
            </view>
            <view class="card-icon-info">
              <text class="card-tag-sm">{{ item.categoryName || item.fileTypeLabel }}</text>
              <text class="card-title">{{ item.title }}</text>
            </view>
          </view>
          <text class="card-desc">{{ item.description || '暂无描述' }}</text>
          <view class="card-footer">
            <text class="card-meta">{{ item.fileTypeLabel }} · {{ item.fileSize || '未知大小' }}</text>
            <view class="card-btn" @click="handleDownload(item)">
              <text class="btn-text">下载</text>
              <text class="iconfont icon-xiazailiang btn-icon"></text>
            </view>
          </view>
        </template>
      </view>
    </view>

    <!-- Empty / Load More -->
    <view v-if="loaded && list.length === 0" class="empty-box">暂无资料</view>
    <view v-if="list.length > 0 && !finished" class="more-btn" @click="getList">加载更多</view>

    <!-- Help Section -->
    <view class="help-section">
      <text class="help-title">未找到所需资源？</text>
      <text class="help-desc">如果您需要特定的技术参数或未公开的资质证明，请联系我们的技术支持团队。</text>
      <view class="help-btn" @click="goContact">咨询在线客服</view>
    </view>

    <!-- Form Popup -->
    <view v-if="showForm" class="mask" @click="closeForm"></view>
    <view v-if="showForm" class="form-popup">
      <text class="form-title">下载登记</text>
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
    goContact() {
      uni.navigateTo({
        url: '/pages/contact/index'
      })
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

<style lang="scss" scoped>
// ========== 主题色 ==========
$primary: #0061a5;
$primary-container: #0099ff;
$tertiary: #006875;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$background: #f9f9fc;
$surface-container-lowest: #ffffff;
$surface-container-low: #f3f3f6;
$outline-variant: #bfc7d5;

.downloads-page {
  min-height: 100vh;
  background: $background;
  padding: 0 40rpx 160rpx;
  box-sizing: border-box;
}

// ========== Hero ==========
.hero {
  padding: 48rpx 0 40rpx;
  text-align: center;
}

.hero-title {
  display: block;
  font-size: 52rpx;
  font-weight: 800;
  color: $primary;
  letter-spacing: -0.02em;
  line-height: 1.2;
  margin-bottom: 16rpx;
}

.hero-desc {
  display: block;
  font-size: 26rpx;
  color: $on-surface-variant;
  line-height: 1.6;
  max-width: 600rpx;
  margin: 0 auto;
}

// ========== Filter Chips ==========
.filter-scroll {
  white-space: nowrap;
  margin-bottom: 32rpx;
}

.filter-chips {
  display: flex;
  gap: 16rpx;
  padding: 4rpx 0;
}

.chip {
  display: inline-flex;
  align-items: center;
  flex-shrink: 0;
  padding: 18rpx 36rpx;
  font-size: 26rpx;
  font-weight: 600;
  border-radius: 999rpx;
  background: rgba($tertiary, 0.08);
  color: $tertiary;
  transition: all 0.2s;

  &.active {
    background: $primary-container;
    color: #ffffff;
  }

  &:active {
    transform: scale(0.95);
  }
}

// ========== Search ==========
.search-row {
  margin-bottom: 32rpx;
}

.search-box {
  display: flex;
  align-items: center;
  background: $surface-container-lowest;
  border: 2rpx solid rgba($outline-variant, 0.4);
  border-radius: 20rpx;
  padding: 0 24rpx;
  height: 88rpx;
}

.search-icon {
  font-size: 36rpx;
  color: $on-surface-variant;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  color: $on-surface;
  height: 88rpx;
}

// ========== Bento Grid ==========
.bento-grid {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.doc-card {
  background: $surface-container-lowest;
  border: 2rpx solid rgba($outline-variant, 0.3);
  border-radius: 24rpx;
  padding: 32rpx;
  display: flex;
  flex-direction: column;
  transition: all 0.2s;
  box-shadow: 0 10rpx 20rpx -5rpx rgba($primary-container, 0.1);

  &:active {
    border-color: $primary-container;
  }
}

// Image card spans full width
.doc-card-image {
  grid-column: span 2;
  padding: 0;
  overflow: hidden;
}

.card-cover {
  position: relative;
  width: 100%;
  aspect-ratio: 4 / 3;
  overflow: hidden;
}

.cover-img {
  width: 100%;
  height: 100%;
}

.cover-tag {
  position: absolute;
  top: 16rpx;
  right: 16rpx;
  background: rgba($tertiary, 0.12);
  color: $tertiary;
  font-size: 20rpx;
  font-weight: 500;
  padding: 6rpx 20rpx;
  border-radius: 999rpx;
}

.doc-card-image .card-body {
  padding: 28rpx 32rpx 32rpx;
  display: flex;
  flex-direction: column;
}

// Icon card header
.card-icon-row {
  display: flex;
  gap: 20rpx;
  align-items: flex-start;
  margin-bottom: 16rpx;
}

.card-icon-box {
  width: 88rpx;
  height: 88rpx;
  background: rgba($primary, 0.06);
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.card-icon {
  font-size: 44rpx;
  color: $primary;
}

.card-icon-info {
  flex: 1;
  min-width: 0;
}

.card-tag-sm {
  display: inline-block;
  background: rgba($tertiary, 0.08);
  color: $tertiary;
  font-size: 20rpx;
  font-weight: 500;
  padding: 4rpx 16rpx;
  border-radius: 999rpx;
  margin-bottom: 8rpx;
}

// Card common
.card-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: $on-surface;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-desc {
  display: block;
  font-size: 24rpx;
  color: $on-surface-variant;
  line-height: 1.5;
  margin-top: 12rpx;
  margin-bottom: 16rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  flex: 1;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
}

.card-meta {
  font-size: 22rpx;
  color: #707884;
}

.card-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  background: $primary-container;
  color: #ffffff;
  padding: 14rpx 32rpx;
  border-radius: 16rpx;
  font-size: 26rpx;
  font-weight: 600;
  box-shadow: 0 10rpx 20rpx -5rpx rgba($primary-container, 0.3);
  transition: all 0.2s;

  &:active {
    transform: scale(0.95);
  }
}

.btn-icon {
  font-size: 28rpx;
}

// ========== Load More ==========
.more-btn {
  height: 84rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: $primary;
  font-size: 26rpx;
  font-weight: 500;
  margin-top: 32rpx;
}

.empty-box {
  height: 84rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: $on-surface-variant;
  font-size: 26rpx;
  margin-top: 60rpx;
}

// ========== Help Section ==========
.help-section {
  margin-top: 80rpx;
  background: rgba($primary-container, 0.08);
  border: 2rpx solid rgba($primary-container, 0.15);
  border-radius: 32rpx;
  padding: 48rpx 40rpx;
  text-align: center;
}

.help-title {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: $primary;
  margin-bottom: 12rpx;
}

.help-desc {
  display: block;
  font-size: 24rpx;
  color: $on-surface-variant;
  line-height: 1.6;
  margin-bottom: 32rpx;
}

.help-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 3rpx solid $primary;
  color: $primary;
  padding: 20rpx 56rpx;
  border-radius: 999rpx;
  font-size: 26rpx;
  font-weight: 600;
  transition: all 0.2s;

  &:active {
    background: $primary;
    color: #ffffff;
  }
}

// ========== Form Popup ==========
.mask {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: rgba(24, 28, 35, 0.45);
  z-index: 100;
}

.form-popup {
  position: fixed;
  left: 40rpx;
  right: 40rpx;
  top: 50%;
  transform: translateY(-50%);
  background: #fff;
  border-radius: 24rpx;
  padding: 40rpx 32rpx;
  z-index: 101;
}

.form-title {
  display: block;
  font-size: 34rpx;
  font-weight: 600;
  color: $on-surface;
  margin-bottom: 28rpx;
}

.form-input,
.form-textarea {
  width: 100%;
  box-sizing: border-box;
  border: 2rpx solid rgba($outline-variant, 0.4);
  border-radius: 16rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  margin-bottom: 20rpx;
  background: #fff;
  color: $on-surface;
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
  gap: 20rpx;
}

.form-cancel,
.form-submit {
  flex: 1;
  height: 84rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.form-cancel {
  background: rgba($on-surface-variant, 0.06);
  color: $on-surface-variant;
}

.form-submit {
  background: $primary-container;
  color: #ffffff;
}
</style>
