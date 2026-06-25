<template>
  <view class="case-detail-page">
    <scroll-view class="content-scroll" scroll-y>
      <!-- Hero Section -->
      <view class="hero-section">
        <view class="hero-image-wrap">
          <image
            :src="caseData.projectImages[0] || caseData.imageInput"
            mode="aspectFill"
            class="hero-image"
          />
          <view class="hero-overlay"></view>
          <view class="hero-content">
            <text class="hero-tag" v-if="caseData.industryCategory">
              {{ caseData.industryCategory }}
            </text>
            <text class="hero-title">{{ caseData.projectName || caseData.title }}</text>
          </view>
        </view>
      </view>

      <!-- Project Overview Bento -->
      <view class="section overview-section">
        <view class="section-header">
          <text class="section-title-text">项目概况</text>
          <text class="section-label" v-if="caseData.projectType">{{ caseData.projectType }}</text>
        </view>
        <view class="bento-grid">
          <view class="bento-desc glass-card">
            <text class="bento-desc-text">
              {{ caseData.projectBackground || caseData.content || '' }}
            </text>
          </view>
          <view class="bento-card bento-card-primary">
            <text class="iconfont icon-dizhi bento-icon"></text>
            <view class="bento-card-bottom">
              <text class="bento-label">项目地点</text>
              <text class="bento-value">{{ caseData.projectAddress || '-' }}</text>
            </view>
          </view>
          <view class="bento-card bento-card-secondary">
            <text class="iconfont icon-rili bento-icon"></text>
            <view class="bento-card-bottom">
              <text class="bento-label">项目周期</text>
              <text class="bento-value">{{ caseData.projectPeriod || '-' }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- Supply Products -->
      <view class="section supply-section" v-if="caseData.supplyProducts">
        <view class="section-header">
          <text class="section-title-text">供货产品</text>
        </view>
        <scroll-view class="supply-scroll" scroll-x>
          <view class="supply-list">
            <view
              v-for="(product, idx) in supplyProductList"
              :key="idx"
              class="supply-card"
            >
              <view class="supply-icon-wrap">
                <text class="iconfont" :class="supplyIcons[idx % supplyIcons.length]"></text>
              </view>
              <text class="supply-card-title">{{ product }}</text>
              <text class="supply-card-desc">专业级品质保障</text>
            </view>
          </view>
        </scroll-view>
      </view>

      <!-- Implementation Result -->
      <view class="section result-section" v-if="caseData.implementationResult">
        <view class="section-header">
          <text class="section-title-text">实施效果</text>
        </view>
        <view class="result-card">
          <text class="result-text">{{ caseData.implementationResult }}</text>
        </view>
      </view>

      <!-- Project Background -->
      <view class="section bg-section" v-if="caseData.projectBackground">
        <view class="section-header">
          <text class="section-title-text">项目背景</text>
        </view>
        <view class="content-card">
          <jyf-parser :html="caseData.projectBackground" />
        </view>
      </view>

      <!-- Case Detail Content -->
      <view class="section content-section" v-if="caseData.content">
        <view class="section-header">
          <text class="section-title-text">案例详情</text>
        </view>
        <view class="content-card">
          <jyf-parser :html="caseData.content" />
        </view>
      </view>

      <!-- Related Products -->
      <view class="section product-section" v-if="caseData.productList && caseData.productList.length > 0">
        <view class="section-header">
          <text class="section-title-text">关联产品</text>
        </view>
        <scroll-view class="product-scroll" scroll-x>
          <view class="product-list">
            <view
              v-for="product in caseData.productList"
              :key="product.id"
              class="product-card"
              @click="goToProduct(product.id)"
            >
              <image :src="product.image" mode="aspectFill" class="product-image" />
              <view class="product-info">
                <text class="product-name">{{ product.storeName }}</text>
                <text class="product-price">¥{{ product.price }}</text>
              </view>
            </view>
          </view>
        </scroll-view>
      </view>

      <!-- CTA Section -->
      <view class="section cta-section">
        <view class="cta-btn" @click="goToContact">
          <text class="iconfont icon-dianhua"></text>
          <text>联系我们</text>
        </view>
      </view>

      <view class="bottom-gap"></view>
    </scroll-view>
  </view>
</template>

<script>
import { getCaseDetails } from '@/api/api.js';
import parser from "@/components/jyf-parser/jyf-parser";

export default {
  components: {
    "jyf-parser": parser
  },
  data() {
    return {
      id: 0,
      caseData: {
        projectImages: [],
        imageInput: '',
        title: '',
        content: '',
        projectName: '',
        industryCategory: '',
        regionCategory: '',
        projectType: '',
        projectAddress: '',
        projectPeriod: '',
        supplyProducts: '',
        implementationResult: '',
        projectBackground: '',
        productList: []
      },
      supplyIcons: ['icon-dianlan', 'icon-dianlan1', 'icon-dianxian', 'icon-dianxian1'],
      tagStyle: {
        img: 'width:100%;display:block;',
        table: 'width:100%',
        video: 'width:100%'
      }
    };
  },
  computed: {
    supplyProductList() {
      if (!this.caseData.supplyProducts) return [];
      return this.caseData.supplyProducts.split(/[,，、\n]/).map(s => s.trim()).filter(Boolean);
    }
  },
  onLoad(options) {
    if (options.id) {
      this.id = options.id;
      this.getCaseDetail();
    }
  },
  methods: {
    getCaseDetail() {
      uni.showLoading({ title: '加载中...' });
      getCaseDetails(this.id).then(res => {
        uni.hideLoading();
        const data = res.data;
        let projectImages = [];
        try {
          projectImages = data.coverImages ? JSON.parse(data.coverImages) : [];
        } catch (e) {
          projectImages = [];
        }
        this.caseData = {
          ...data,
          projectImages: projectImages,
          productList: data.productList || []
        };
      }).catch(() => {
        uni.hideLoading();
      });
    },
    goToProduct(id) {
      uni.navigateTo({
        url: '/pages/goods/goods_details/index?id=' + id
      });
    },
    goToContact() {
      uni.navigateTo({
        url: '/pages/contact/index'
      });
    }
  }
}
</script>

<style lang="scss" scoped>
// ========== 主题色 ==========
$primary: #0061a5;
$primary-fixed: #d2e4ff;
$secondary-fixed: #ffe170;
$tertiary: #006875;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$background: #f9f9fc;
$surface-container-lowest: #ffffff;
$surface-container-low: #f3f3f6;
$outline-variant: #bfc7d5;
$on-primary-container: #002f54;

.case-detail-page {
  min-height: 100vh;
  background-color: $background;
}

.content-scroll {
  padding-bottom: 20rpx;
}

// ========== Section Common ==========
.section {
  padding: 0 40rpx;
  margin-bottom: 80rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32rpx;
}

.section-title-text {
  font-size: 34rpx;
  font-weight: 600;
  color: $primary;
}

.section-label {
  font-size: 22rpx;
  color: #707884;
  font-family: "JetBrains Mono", monospace;
  text-transform: uppercase;
  letter-spacing: 2rpx;
}

// ========== Hero ==========
.hero-section {
  padding: 0 40rpx;
  margin-bottom: 80rpx;
}

.hero-image-wrap {
  position: relative;
  height: 480rpx;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 16rpx 40rpx rgba(0, 0, 0, 0.12);
}

.hero-image {
  width: 100%;
  height: 100%;
}

.hero-overlay {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: linear-gradient(to top, rgba($primary, 0.8), transparent);
}

.hero-content {
  position: absolute;
  bottom: 48rpx;
  left: 40rpx;
  right: 40rpx;
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.hero-tag {
  display: inline-block;
  align-self: flex-start;
  background: rgba(0, 164, 183, 0.2);
  backdrop-filter: blur(16rpx);
  -webkit-backdrop-filter: blur(16rpx);
  padding: 8rpx 24rpx;
  border-radius: 999rpx;
  font-size: 22rpx;
  font-weight: 500;
  color: #9cf0ff;
  border: 2rpx solid rgba(156, 240, 255, 0.3);
}

.hero-title {
  font-size: 40rpx;
  font-weight: 700;
  color: #ffffff;
  line-height: 1.3;
}

// ========== Bento Grid ==========
.bento-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24rpx;
}

.bento-desc {
  grid-column: span 2;
  padding: 36rpx;
}

.glass-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(24rpx);
  -webkit-backdrop-filter: blur(24rpx);
  border: 2rpx solid rgba($outline-variant, 0.4);
  border-radius: 24rpx;
}

.bento-desc-text {
  font-size: 26rpx;
  color: $on-surface-variant;
  line-height: 1.7;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

.bento-card {
  padding: 36rpx;
  border-radius: 24rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.bento-card-primary {
  background: $primary-fixed;
}

.bento-card-secondary {
  background: $secondary-fixed;
}

.bento-icon {
  font-size: 48rpx;
  margin-bottom: 16rpx;
}

.bento-card-primary .bento-icon {
  color: $primary;
}

.bento-card-secondary .bento-icon {
  color: #705d00;
}

.bento-card-bottom {
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.bento-label {
  font-size: 18rpx;
  color: rgba($on-primary-container, 0.6);
  text-transform: uppercase;
  letter-spacing: 2rpx;
}

.bento-card-primary .bento-label {
  color: rgba(#00497e, 0.6);
}

.bento-card-secondary .bento-label {
  color: rgba(#544600, 0.6);
}

.bento-value {
  font-size: 28rpx;
  font-weight: 600;
}

.bento-card-primary .bento-value {
  color: $primary;
}

.bento-card-secondary .bento-value {
  color: #705d00;
}

// ========== Supply Products ==========
.supply-section {
  .section-header {
    padding: 0;
  }
}

.supply-scroll {
  white-space: nowrap;
}

.supply-list {
  display: flex;
  gap: 24rpx;
  padding: 8rpx 40rpx 8rpx 0;
}

.supply-card {
  min-width: 280rpx;
  flex-shrink: 0;
  background: $surface-container-lowest;
  border: 2rpx solid $outline-variant;
  border-radius: 24rpx;
  padding: 40rpx 32rpx;
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  transition: border-color 0.2s;

  &:active {
    border-color: $primary;
  }
}

.supply-icon-wrap {
  width: 96rpx;
  height: 96rpx;
  background: rgba($primary, 0.08);
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  .iconfont {
    font-size: 44rpx;
    color: $primary;
  }
}

.supply-card-title {
  font-size: 28rpx;
  font-weight: 600;
  color: $on-surface;
}

.supply-card-desc {
  font-size: 24rpx;
  color: $on-surface-variant;
  white-space: normal;
}

// ========== Result ==========
.result-card {
  background: $on-surface;
  padding: 48rpx 40rpx;
  border-radius: 32rpx;
  box-shadow: 0 20rpx 40rpx -10rpx rgba($primary, 0.3);
}

.result-text {
  font-size: 28rpx;
  color: #9fcaff;
  line-height: 1.8;
}

// ========== Content Card ==========
.content-card {
  background: $surface-container-lowest;
  border-radius: 24rpx;
  padding: 40rpx;
}

// ========== Related Products ==========
.product-scroll {
  white-space: nowrap;
}

.product-list {
  display: flex;
  gap: 24rpx;
  margin-top: 8rpx;
}

.product-card {
  width: 280rpx;
  flex-shrink: 0;
  background: $surface-container-lowest;
  border: 2rpx solid $outline-variant;
  border-radius: 24rpx;
  overflow: hidden;

  &:active {
    border-color: $primary;
  }
}

.product-image {
  width: 280rpx;
  height: 280rpx;
}

.product-info {
  padding: 20rpx 24rpx;
}

.product-name {
  font-size: 24rpx;
  color: $on-surface;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: block;
}

.product-price {
  font-size: 28rpx;
  font-weight: 600;
  color: $primary;
  display: block;
  margin-top: 8rpx;
}

// ========== CTA ==========
.cta-section {
  padding-bottom: 40rpx;
}

.cta-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
  width: 100%;
  height: 112rpx;
  background: $primary;
  color: #ffffff;
  font-size: 32rpx;
  font-weight: 600;
  border-radius: 24rpx;
  box-shadow: 0 20rpx 40rpx -10rpx rgba(0, 153, 255, 0.3);
  transition: all 0.2s;

  &:active {
    transform: scale(0.95);
    opacity: 0.9;
  }

  .iconfont {
    font-size: 36rpx;
  }
}

// ========== Bottom ==========
.bottom-gap {
  height: 80rpx;
}
</style>
