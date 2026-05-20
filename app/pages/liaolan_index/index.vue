<template>
  <view class="liaolan-home">
    <view class="page-container">
      <!-- Navbar -->
      <liaolanNavbar
          title="辽缆"
          @leftClick="handleMenuClick"
          @rightClick="handleSearchClick"
      />

      <!-- Search Bar (Mobile) -->
      <view class="search-bar">
        <view class="search-input-wrapper">
          <text class="iconfont icon-sousuo search-icon"></text>
          <input
              class="search-input"
              type="text"
              placeholder="输入关键词搜索产品"
              placeholder-class="search-placeholder"
              @focus="handleSearchFocus"
          />
        </view>
      </view>

      <!-- Hero Section -->
      <view class="hero-section">
        <swiper class="hero-swiper" :indicator-dots="true" :autoplay="true" :interval="3000" :duration="500" indicator-color="rgba(255,255,255,0.5)" indicator-active-color="#ffffff">
          <swiper-item v-for="(item, index) in heroImages" :key="index">
            <view class="hero-slide">
              <image class="hero-image" :src="item.image" mode="aspectFill"></image>
              <view class="hero-overlay"></view>
              <view class="hero-content">
                <h2 class="hero-title">{{ item.title }}</h2>
                <p class="hero-desc">{{ item.description }}</p>
              </view>
            </view>
          </swiper-item>
        </swiper>
      </view>

      <!-- Quick Navigation -->
      <view class="quick-nav">
        <view class="quick-nav-grid">
          <view
              class="quick-nav-item"
              v-for="(item, index) in quickNavs"
              :key="index"
              @click="handleNavClick(item.path)"
          >
            <view class="quick-nav-icon" :class="{ active: currentNavIndex === index }">
              <text class="iconfont" :class="item.icon"></text>
            </view>
            <text class="quick-nav-label">{{ item.label }}</text>
          </view>
        </view>
      </view>

      <!-- News Scroll -->
      <view class="news-section">
        <view class="news-wrapper">
          <view class="news-icon">
            <text class="iconfont icon-laba"></text>
          </view>
          <view class="news-content">
            <swiper class="news-swiper" :vertical="true" :autoplay="true" :interval="3000" :circular="true" :display-items-count="1">
              <swiper-item v-for="(item, index) in newsList" :key="index">
                <text class="news-text">{{ item }}</text>
              </swiper-item>
            </swiper>
          </view>
          <view class="news-tag">
            <text>最新</text>
          </view>
        </view>
      </view>

      <!-- Featured Products -->
      <view class="products-section">
        <view class="section-header">
          <view class="section-title-wrapper">
            <view class="section-title-bar"></view>
            <h3 class="section-title">推荐产品</h3>
          </view>
          <view class="section-more" @click="goToProducts">
            <text>全部</text>
            <text class="iconfont icon-xiangyou"></text>
          </view>
        </view>
        <view class="products-grid">
          <view
              class="product-card"
              v-for="(product, index) in products"
              :key="index"
              @click="goToProductDetail(product.id)"
          >
            <view class="product-image-wrapper">
              <image class="product-image" :src="product.image" mode="aspectFill"></image>
              <view class="product-tag" v-if="product.tag">{{ product.tag }}</view>
            </view>
            <view class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <p class="product-spec">{{ product.spec }}</p>
            </view>
          </view>
        </view>
      </view>

      <!-- Contact Us -->
      <view class="contact-section">
        <view class="section-header">
          <view class="section-title-wrapper">
            <view class="section-title-bar"></view>
            <h3 class="section-title">联系我们</h3>
          </view>
        </view>
        <view class="contact-content">
          <view class="contact-item">
            <view class="contact-icon">
              <text class="iconfont icon-ic_location4"></text>
            </view>
            <view class="contact-info">
              <h4 class="contact-label">总部地址</h4>
              <p class="contact-value">辽宁省沈阳市铁西区工业南路88号</p>
            </view>
          </view>
          <view class="contact-item">
            <view class="contact-icon">
              <text class="iconfont icon-dianhua"></text>
            </view>
            <view class="contact-info">
              <h4 class="contact-label">全国销售热线</h4>
              <p class="contact-phone">400-888-9999</p>
            </view>
            <view class="contact-call-btn" @click="handleCall">
              <text>拨打</text>
            </view>
          </view>
        </view>
        <view class="map-placeholder">
          <image class="map-image" src="https://lh3.googleusercontent.com/aida-public/AB6AXuDoZLgVC9rxGbggTtcYRnCHOY4yoVfvE1jNe29uz6K1uRY8K6_rYpDhDjHn49SyQovc8nCLCXYdwzoilj2ynuo327_mfgKal4oqiwQci2Y8Ki2DDZKvjBjOmV0Ua--EsxvmY84e-Ox-_C6Aw_x1u8BBTiqWTmzkQ2Mc6FK4VVuP-9FKj-ARFdtPwdUgx77y-X-cTAmDrX9i_pj2RWN3UkV2DL_BuHLA4qVdDaoBzrpumXLK-MQyOP-br97BySYaqmaLYndZHwsfQkM" mode="aspectFill"></image>
          <view class="map-overlay">
            <view class="map-label">查看地图</view>
          </view>
        </view>
      </view>

      <!-- Bottom Padding -->
      <view class="bottom-padding"></view>
    </view>
    <!-- Tab Bar -->
    <tab-bar></tab-bar>
  </view>
</template>

<script>
import liaolanNavbar from '@/components/liaolanNavbar/index.vue';
import tabBar from '@/components/tab-bar/index.vue';

export default {
  components: {
    liaolanNavbar,
    tabBar
  },
  data() {
    return {
      currentNavIndex: -1,
      heroImages: [
        {
          image: 'https://lh3.googleusercontent.com/aida-public/AB6AXuBsPyFf-hVVDObopFwfy0mffLQ4pFJbB0MPmzhTUli4K9ycwL-WXaEiTKJjTyil83D-S1gCm7ar5hJijy739EO_wYbU_M9NwPv_nvihzaz89Wr6mH7f3C8nWFNIdOuAF4xgMqCns6AH1DrJIiwXXMYbE-y-wlgFvXHFpaC-CPIYzwkumLNNTA6p6sQq24HK1YQA7SeInRx2W61rTEpAD8SBcQQdaGtAprzlq_P4PDpJ3V7N2zJLqgfTRJGrXCUeiFLKjGw7VGiiVSA',
          title: '铸就工业脊梁',
          description: '高品质特种电缆研发与制造，为全球重工业提供稳定可靠的电力传输解决方案。'
        }
      ],
      quickNavs: [
        { label: '产品', icon: 'icon-shangpin', path: '/pages/goods_cate/goods_cate' },
        { label: '新闻', icon: 'icon-tuwen', path: '/pages/news/news_list/index' },
        { label: '下载', icon: 'icon-xiazai5', path: '/pages/download/index' },
        { label: '关于', icon: 'icon-shuoming', path: '/pages/about/index' }
      ],
      newsList: [
        '辽缆集团入围2024国家电网重点采购目录',
        '公司荣获国家级高新技术企业认定',
        '新产品KVV系列控制电缆正式投产'
      ],
      products: [
        {
          id: 1,
          name: 'YJV 高压交联电缆',
          spec: '8.7/15kV 铜芯交联聚乙烯',
          image: 'https://lh3.googleusercontent.com/aida-public/AB6AXuD7s_LSVVIhtCLSx2OHKIDLxnBS_TnfuhC5Dm8Yri4Oof8mXgawg0nfTZwuk-MYICc2YEgGLXfAf5KZqRcqlUFTeUYFmoVNzQ2NrzC3XDlMbfIFo_cnYj2UMLlzAwv2pWjz727Nm1Jq-QPLWvIH40JgOeJytHiNVBnmpLpb9R3gnPpqeyziCaXiV3UbiQy7l9FPoMb-YCDiSAbpiPBRCNbU2YjM1_3PFX0dqc1D0ad1FEHk5BStwkoCA-zeud6V00MjYmDeDvbA4Rk',
          tag: '现货'
        },
        {
          id: 2,
          name: 'KVVP 屏蔽控制电缆',
          spec: '铜芯聚氯乙烯绝缘及护套',
          image: 'https://lh3.googleusercontent.com/aida-public/AB6AXuBY-yxwIjASvdMDjKEH2kTdE9HEMhKzJbr331yKNGQF2K-ujJ41wouX2xnHh9fmst2rkryJstZnUBWCqE6c5pw1TGHCkMmTQM21ArO3OTiZcRY8qqZYKmNCZcGgOakP4HiNo6ty4_NiwzJuyJg25RDOqKpRRq0tQw1lEQqRVpAEfOPeDwcitkRRIf7dgkp7RJ32amyaaE1vQliIaVRqNu6VhuK_zI5YWW8U58j1WtD-m76LX1AaAtylywl3HzXkvFji4tZBRH4ItiY',
          tag: ''
        }
      ]
    };
  },
  methods: {
    handleMenuClick() {
      uni.showToast({
        title: '菜单功能开发中',
        icon: 'none'
      });
    },
    handleSearchClick() {
      uni.navigateTo({
        url: '/pages/goods/goods_search/index'
      });
    },
    handleSearchFocus() {
      uni.navigateTo({
        url: '/pages/goods/goods_search/index'
      });
    },
    handleNavClick(path) {
      if (path.includes('goods_cate')) {
        uni.switchTab({
          url: path
        });
      } else {
        uni.navigateTo({
          url: path
        });
      }
    },
    goToProducts() {
      uni.navigateTo({
        url: '/pages/goods/goods_list/index'
      });
    },
    goToProductDetail(id) {
      uni.navigateTo({
        url: `/pages/goods/goods_details/index?id=${id}`
      });
    },
    handleCall() {
      uni.makePhoneCall({
        phoneNumber: '400-888-9999',
        fail: () => {
          uni.showToast({
            title: '拨打失败',
            icon: 'none'
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
$primary-color: #003da6;
$primary-container: #0052d9;
$on-primary-container: #cbd6ff;
$background: #f9f9ff;
$surface-container: #ebedf9;
$surface-container-high: #e5e8f3;
$surface-container-lowest: #ffffff;
$on-surface: #181c23;
$secondary: #5c5f60;
$outline-variant: #c3c6d7;

.liaolan-home {
  background-color: $background;
  min-height: 100vh;
}

.page-container {
  max-width: 750px;
  margin: 0 auto;
  padding-bottom: 120rpx;
}

// Top AppBar
.top-appbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: $surface-container-lowest;
  border-bottom: 1px solid $outline-variant;
  height: 88rpx;
}

.top-appbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 88rpx;
  padding: 0 32rpx;
}

.top-appbar-left,
.top-appbar-right {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.3s;

  &:active {
    background-color: $surface-container;
  }

  .iconfont {
    font-size: 48rpx;
    color: $primary-color;
  }
}

.top-appbar-title {
  font-size: 32rpx;
  font-weight: 600;
  color: $primary-color;
}

// Search Bar
.search-bar {
  padding: 24rpx 32rpx;
  margin-top: 148rpx;
}

.search-input-wrapper {
  position: relative;
  width: 100%;
  height: 80rpx;
  background-color: $surface-container;
  border-radius: 40rpx;
  display: flex;
  align-items: center;
  padding: 0 32rpx;
}

.search-icon {
  font-size: 36rpx;
  color: $secondary;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  height: 80rpx;
  font-size: 28rpx;
  color: $on-surface;
}

.search-placeholder {
  color: $secondary;
  font-size: 28rpx;
}

// Hero Section
.hero-section {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.hero-swiper {
  width: 100%;
  height: 400rpx;
  border-radius: 24rpx;
  overflow: hidden;
}

.hero-slide {
  position: relative;
  width: 100%;
  height: 400rpx;
}

.hero-image {
  width: 100%;
  height: 100%;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
}

.hero-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 48rpx;
}

.hero-title {
  font-size: 48rpx;
  font-weight: 600;
  color: #ffffff;
  margin-bottom: 16rpx;
}

.hero-desc {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.5;
  max-width: 600rpx;
}

// Quick Navigation
.quick-nav {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.quick-nav-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24rpx;
}

.quick-nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
}

.quick-nav-icon {
  width: 96rpx;
  height: 96rpx;
  background-color: $surface-container-high;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;

  &.active,
  &:active {
    background-color: $primary-container;

    .iconfont {
      color: $on-primary-container;
    }
  }

  .iconfont {
    font-size: 48rpx;
    color: $primary-color;
    transition: color 0.3s;
  }
}

.quick-nav-label {
  font-size: 24rpx;
  color: $on-surface;
  font-weight: 500;
}

// News Section
.news-section {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.news-wrapper {
  display: flex;
  align-items: center;
  background-color: $surface-container-lowest;
  border: 1px solid $outline-variant;
  border-radius: 16rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.02);
}

.news-icon {
  width: 64rpx;
  height: 64rpx;
  background-color: $surface-container-high;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;

  .iconfont {
    font-size: 32rpx;
    color: $primary-color;
  }
}

.news-content {
  flex: 1;
  height: 40rpx;
  overflow: hidden;
}

.news-swiper {
  height: 100%;
}

.news-text {
  font-size: 28rpx;
  color: $on-surface;
  line-height: 40rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.news-tag {
  padding: 0 16rpx;
  border-left: 1px solid $outline-variant;
  margin-left: 24rpx;

  text {
    font-size: 20rpx;
    color: $secondary;
    font-weight: 500;
  }
}

// Products Section
.products-section {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.section-title-wrapper {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.section-title-bar {
  width: 4rpx;
  height: 40rpx;
  background-color: $primary-color;
  border-radius: 4rpx;
}

.section-title {
  font-size: 40rpx;
  font-weight: 600;
  color: $on-surface;
}

.section-more {
  display: flex;
  align-items: center;
  gap: 8rpx;
  font-size: 24rpx;
  color: $secondary;

  .iconfont {
    font-size: 28rpx;
  }
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
}

.product-card {
  background-color: $surface-container-lowest;
  border: 1px solid $outline-variant;
  border-radius: 16rpx;
  overflow: hidden;
  transition: box-shadow 0.3s;

  &:active {
    box-shadow: 0 8rpx 16rpx rgba(0, 0, 0, 0.1);
  }
}

.product-image-wrapper {
  position: relative;
  width: 100%;
  aspect-ratio: 1;
  background-color: $surface-container-high;
}

.product-image {
  width: 100%;
  height: 100%;
}

.product-tag {
  position: absolute;
  top: 16rpx;
  left: 16rpx;
  padding: 8rpx 16rpx;
  background-color: $primary-container;
  color: $on-primary-container;
  font-size: 20rpx;
  font-weight: 500;
  border-radius: 8rpx;
}

.product-info {
  padding: 24rpx;
}

.product-name {
  font-size: 32rpx;
  font-weight: 600;
  color: $on-surface;
  margin-bottom: 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-spec {
  font-size: 24rpx;
  color: $secondary;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

// Contact Section
.contact-section {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.contact-content {
  background-color: $surface-container-lowest;
  border: 1px solid $outline-variant;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
}

.contact-item {
  display: flex;
  align-items: flex-start;
  gap: 24rpx;
  margin-bottom: 32rpx;

  &:last-child {
    margin-bottom: 0;
  }
}

.contact-icon {
  width: 64rpx;
  height: 64rpx;
  background-color: $surface-container-high;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  .iconfont {
    font-size: 28rpx;
    color: $primary-color;
  }
}

.contact-info {
  flex: 1;
}

.contact-label {
  font-size: 28rpx;
  font-weight: 500;
  color: $on-surface;
  margin-bottom: 8rpx;
}

.contact-value {
  font-size: 24rpx;
  color: $secondary;
}

.contact-phone {
  font-size: 32rpx;
  font-weight: 600;
  color: $primary-color;
  margin-top: 8rpx;
}

.contact-call-btn {
  padding: 16rpx 32rpx;
  background-color: $primary-color;
  color: #ffffff;
  font-size: 28rpx;
  font-weight: 500;
  border-radius: 16rpx;
  transition: background-color 0.3s;

  &:active {
    background-color: darken($primary-color, 10%);
  }
}

.map-placeholder {
  position: relative;
  width: 100%;
  height: 256rpx;
  background-color: $surface-container;
  border-radius: 16rpx;
  overflow: hidden;
}

.map-image {
  width: 100%;
  height: 100%;
  opacity: 0.8;
}

.map-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(249, 249, 255, 0.9);
  backdrop-filter: blur(4rpx);
}

.map-label {
  padding: 12rpx 24rpx;
  background-color: rgba(255, 255, 255, 0.9);
  border: 1px solid $outline-variant;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: $on-surface;
  box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.05);
}

// Bottom Padding
.bottom-padding {
  height: 120rpx;
}
</style>
