<template>
  <view class="liaolan-home">
    <!-- 大气背景层 -->
    <view class="bg-mesh"></view>
    <view class="bg-blob bg-blob-1"></view>
    <view class="bg-blob bg-blob-2"></view>

    <view class="page-container">
      <!-- Navbar -->
      <liaolanNavbar
          title="沈阳辽缆电线电缆有限公司"
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
          <view class="search-action">
            <text>搜索</text>
          </view>
        </view>
      </view>

      <!-- Hero Section -->
      <view class="hero-section">
        <swiper class="hero-swiper" :indicator-dots="true" :autoplay="true" :interval="3000" :duration="500" indicator-color="rgba(255,255,255,0.4)" indicator-active-color="#ffffff">
          <swiper-item v-for="(item, index) in heroImages" :key="index">
            <view class="hero-slide">
              <image class="hero-image" :src="item.image" mode="aspectFill"></image>
              <view class="hero-overlay"></view>
              <view class="hero-deco hero-deco-1"></view>
              <view class="hero-deco hero-deco-2"></view>
              <view class="hero-content">
                <view class="hero-badge">
                  <text>工业脊梁 · 品质之选</text>
                </view>
                <h2 class="hero-title">{{ item.title }}</h2>
                <p class="hero-desc">{{ item.description }}</p>
                <view class="hero-btn">
                  <text>了解更多</text>
                  <text class="iconfont icon-xiangyou"></text>
                </view>
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
            <view class="quick-nav-icon">
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
            <view class="section-title-group">
              <h3 class="section-title">推荐产品</h3>
              <text class="section-subtitle">精选优质电缆 工业首选</text>
            </view>
          </view>
          <view class="section-more" @click="goToProducts">
            <text>查看全部</text>
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
              <view class="product-shine"></view>
              <view class="product-tag" v-if="product.tag">{{ product.tag }}</view>
            </view>
            <view class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <p class="product-spec">{{ product.spec }}</p>
              <view class="product-bottom">
                <view class="product-price">
                  <text class="price-symbol">¥</text>
                  <text class="price-value">{{ product.price || '咨询' }}</text>
                </view>
                <view class="product-arrow">
                  <text class="iconfont icon-xiangyou"></text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- Contact Us -->
      <view class="contact-section">
        <view class="section-header">
          <view class="section-title-wrapper">
            <view class="section-title-bar"></view>
            <view class="section-title-group">
              <h3 class="section-title">联系我们</h3>
              <text class="section-subtitle">全国服务热线 期待与您合作</text>
            </view>
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
          <view class="contact-divider"></view>
          <view class="contact-item">
            <view class="contact-icon contact-icon-accent">
              <text class="iconfont icon-dianhua"></text>
            </view>
            <view class="contact-info">
              <h4 class="contact-label">全国销售热线</h4>
              <p class="contact-phone">400-888-9999</p>
            </view>
            <view class="contact-call-btn" @click="handleCall">
              <text class="iconfont icon-dianhua"></text>
              <text>立即拨打</text>
            </view>
          </view>
        </view>
        <view class="map-placeholder">
          <image class="map-image" src="https://lh3.googleusercontent.com/aida-public/AB6AXuDoZLgVC9rxGbggTtcYRnCHOY4yoVfvE1jNe29uz6K1uRY8K6_rYpDhDjHn49SyQovc8nCLCXYdwzoilj2ynuo327_mfgKal4oqiwQci2Y8Ki2DDZKvjBjOmV0Ua--EsxvmY84e-Ox-_C6Aw_x1u8BBTiqWTmzkQ2Mc6FK4VVuP-9FKj-ARFdtPwdUgx77y-X-cTAmDrX9i_pj2RWN3UkV2DL_BuHLA4qVdDaoBzrpumXLK-MQyOP-br97BySYaqmaLYndZHwsfQkM" mode="aspectFill"></image>
          <view class="map-overlay">
            <view class="map-card">
              <text class="iconfont icon-ic_location4"></text>
              <text class="map-label">查看地图导航</text>
            </view>
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
import { getProducts } from '@/api/api.js';

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
          image: 'https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260603141410866_673280054735.jpg',
          title: '铸就工业脊梁',
          description: '高品质特种电缆研发与制造，为全球重工业提供稳定可靠的电力传输解决方案。'
        }
      ],
      quickNavs: [
        { label: '产品', icon: 'icon-shangpin', path: '/pages/goods_cate/goods_cate' },
        { label: '新闻', icon: 'icon-tuwen', path: '/pages/news/news_list/index' },
        { label: '下载', icon: 'icon-xiazai5', path: '/pages/downloads/index' },
        { label: '关于', icon: 'icon-shuoming', path: '/pages/about/index' }
      ],
      newsList: [
        '辽缆集团入围2024国家电网重点采购目录',
        '公司荣获国家级高新技术企业认定',
        '新产品KVV系列控制电缆正式投产'
      ],
      products: [],
      imgHost: ''
    };
  },
  onLoad() {
    this.imgHost = this.$Cache.get('imgHost') || '';
    this.fetchProducts();
  },
  methods: {
    fetchProducts() {
      getProducts({ page: 1, limit: 2 }).then(res => {
        const list = (res.data && res.data.list) ? res.data.list : [];
        this.products = list.map(item => ({
          id: item.id,
          name: item.storeName,
          image: item.image,
          price: item.price,
          spec: item.storeInfo || '',
          tag: this.getActivityTag(item.activity)
        }));
      });
    },
    getActivityTag(activity) {
      const activityMap = { '1': '秒杀', '2': '砍价', '3': '拼团' };
      return activityMap[activity] || '';
    },
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
// ========== 主题色：明亮蓝色系 ==========
$primary: #0f5de3;          // 品牌主蓝
$primary-light: #3a7bff;    // 浅蓝
$primary-deep: #0947b8;     // 深蓝
$primary-soft: #eaf1ff;     // 极浅蓝（卡片底/背景）
$primary-fade: #f4f8ff;     // 淡蓝（背景）
$primary-glow: #6ea3ff;     // 蓝色高光

$on-primary: #ffffff;
$on-surface: #0e1729;
$on-surface-muted: #5b6478;
$outline: #dfe5f2;
$outline-light: #ecf0fa;

$bg-base: #f4f8ff;          // 全局背景（极淡蓝）
$surface: #ffffff;
$surface-2: #f7faff;

$accent-cyan: #36c6ff;      // 点缀青蓝
$accent-violet: #8a9bff;    // 辅助淡紫蓝

// ========== 页面容器 ==========
.liaolan-home {
  position: relative;
  min-height: 100vh;
  background: linear-gradient(180deg, #eaf2ff 0%, #f4f8ff 40%, #ffffff 100%);
  overflow: hidden;
}

// 背景网格 Mesh 渐变
.bg-mesh {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 600rpx;
  background:
    radial-gradient(ellipse 80% 60% at 20% 10%, rgba(58, 123, 255, 0.18) 0%, transparent 60%),
    radial-gradient(ellipse 60% 50% at 90% 20%, rgba(54, 198, 255, 0.15) 0%, transparent 60%),
    radial-gradient(ellipse 70% 50% at 50% 0%, rgba(138, 155, 255, 0.12) 0%, transparent 70%);
  pointer-events: none;
  z-index: 0;
}

.bg-blob {
  position: fixed;
  border-radius: 50%;
  filter: blur(80rpx);
  pointer-events: none;
  z-index: 0;
}
.bg-blob-1 {
  width: 360rpx;
  height: 360rpx;
  top: -120rpx;
  right: -100rpx;
  background: radial-gradient(circle, rgba(58, 123, 255, 0.35) 0%, transparent 70%);
}
.bg-blob-2 {
  width: 320rpx;
  height: 320rpx;
  top: 300rpx;
  left: -120rpx;
  background: radial-gradient(circle, rgba(54, 198, 255, 0.25) 0%, transparent 70%);
}

.page-container {
  position: relative;
  z-index: 1;
  max-width: 750rpx;
  margin: 0 auto;
  padding-bottom: 140rpx;
}

// ========== Search Bar ==========
.search-bar {
  padding: 24rpx 32rpx 8rpx;
  margin-top: 148rpx;
}

.search-input-wrapper {
  position: relative;
  width: 100%;
  height: 88rpx;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20rpx);
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  padding: 0 12rpx 0 32rpx;
  box-shadow:
    0 8rpx 24rpx rgba(15, 93, 227, 0.08),
    0 2rpx 6rpx rgba(15, 93, 227, 0.05),
    inset 0 0 0 1rpx rgba(255, 255, 255, 0.6);
  border: 1rpx solid rgba(255, 255, 255, 0.8);
}

.search-icon {
  font-size: 36rpx;
  color: $primary;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  height: 88rpx;
  font-size: 28rpx;
  color: $on-surface;
}

.search-placeholder {
  color: #98a2b8;
  font-size: 28rpx;
}

.search-action {
  height: 64rpx;
  padding: 0 28rpx;
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  border-radius: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6rpx 16rpx rgba(15, 93, 227, 0.28);

  text {
    font-size: 26rpx;
    color: #ffffff;
    font-weight: 600;
    letter-spacing: 2rpx;
  }
}

// ========== Hero Section ==========
.hero-section {
  padding: 16rpx 32rpx 0;
  margin-bottom: 40rpx;
}

.hero-swiper {
  width: 100%;
  height: 440rpx;
  border-radius: 32rpx;
  overflow: hidden;
  box-shadow:
    0 24rpx 48rpx rgba(15, 93, 227, 0.18),
    0 8rpx 16rpx rgba(15, 93, 227, 0.08);
}

.hero-slide {
  position: relative;
  width: 100%;
  height: 440rpx;
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
  background:
    linear-gradient(135deg, rgba(15, 71, 184, 0.55) 0%, rgba(15, 93, 227, 0.25) 50%, transparent 100%),
    linear-gradient(180deg, transparent 30%, rgba(0, 0, 0, 0.4) 100%);
}

.hero-deco {
  position: absolute;
  border-radius: 50%;
  filter: blur(40rpx);
  pointer-events: none;
}
.hero-deco-1 {
  width: 220rpx;
  height: 220rpx;
  top: -60rpx;
  right: -60rpx;
  background: radial-gradient(circle, rgba(54, 198, 255, 0.4) 0%, transparent 70%);
}
.hero-deco-2 {
  width: 180rpx;
  height: 180rpx;
  bottom: 100rpx;
  left: -40rpx;
  background: radial-gradient(circle, rgba(138, 155, 255, 0.3) 0%, transparent 70%);
}

.hero-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 40rpx 44rpx 48rpx;
  z-index: 2;
}

.hero-badge {
  display: inline-block;
  padding: 8rpx 20rpx;
  background: rgba(255, 255, 255, 0.18);
  border: 1rpx solid rgba(255, 255, 255, 0.35);
  border-radius: 999rpx;
  margin-bottom: 20rpx;
  backdrop-filter: blur(20rpx);

  text {
    font-size: 22rpx;
    color: #ffffff;
    font-weight: 500;
    letter-spacing: 1rpx;
  }
}

.hero-title {
  font-size: 56rpx;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 16rpx;
  line-height: 1.2;
  letter-spacing: 1rpx;
  text-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
}

.hero-desc {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.92);
  line-height: 1.6;
  max-width: 580rpx;
  margin-bottom: 28rpx;
}

.hero-btn {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  padding: 14rpx 28rpx;
  background: #ffffff;
  border-radius: 999rpx;
  box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.12);

  text {
    font-size: 26rpx;
    color: $primary;
    font-weight: 600;
  }

  .iconfont {
    font-size: 22rpx;
    color: $primary;
  }
}

// ========== Quick Navigation ==========
.quick-nav {
  padding: 0 32rpx;
  margin-bottom: 40rpx;
}

.quick-nav-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20rpx);
  border-radius: 28rpx;
  padding: 32rpx 16rpx;
  box-shadow:
    0 12rpx 32rpx rgba(15, 93, 227, 0.06),
    0 2rpx 6rpx rgba(15, 93, 227, 0.04);
  border: 1rpx solid rgba(255, 255, 255, 0.8);
}

.quick-nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
  transition: transform 0.3s ease;

  &:active {
    transform: scale(0.94);
  }
}

.quick-nav-icon {
  width: 104rpx;
  height: 104rpx;
  background: linear-gradient(135deg, #ffffff 0%, $primary-soft 100%);
  border-radius: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  box-shadow:
    0 8rpx 20rpx rgba(15, 93, 227, 0.08),
    inset 0 0 0 1rpx rgba(255, 255, 255, 0.9);
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

  &::after {
    content: '';
    position: absolute;
    top: -3rpx;
    left: -3rpx;
    right: -3rpx;
    bottom: -3rpx;
    border-radius: 31rpx;
    background: linear-gradient(135deg, $primary 0%, $accent-cyan 100%);
    opacity: 0;
    z-index: -1;
    transition: opacity 0.3s;
  }

  &.active,
  &:active {
    background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
    transform: translateY(-2rpx);
    box-shadow: 0 12rpx 24rpx rgba(15, 93, 227, 0.28);

    &::after {
      opacity: 1;
    }

    .iconfont {
      color: #ffffff;
      transform: scale(1.08);
    }
  }

  .iconfont {
    font-size: 48rpx;
    color: $primary;
    transition: all 0.3s;
  }
}

.quick-nav-label {
  font-size: 24rpx;
  color: $on-surface;
  font-weight: 500;
  letter-spacing: 0.5rpx;
}

// ========== News Section ==========
.news-section {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.news-wrapper {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #ffffff 0%, $primary-soft 100%);
  border: 1rpx solid rgba(255, 255, 255, 0.9);
  border-radius: 20rpx;
  padding: 22rpx 24rpx;
  box-shadow:
    0 8rpx 24rpx rgba(15, 93, 227, 0.06),
    0 2rpx 6rpx rgba(15, 93, 227, 0.04);
}

.news-icon {
  width: 64rpx;
  height: 64rpx;
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  box-shadow: 0 6rpx 14rpx rgba(15, 93, 227, 0.28);

  .iconfont {
    font-size: 30rpx;
    color: #ffffff;
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
  font-size: 26rpx;
  color: $on-surface;
  line-height: 40rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: 500;
}

.news-tag {
  padding: 0 18rpx;
  margin-left: 20rpx;
  background: linear-gradient(135deg, rgba(15, 93, 227, 0.1) 0%, rgba(54, 198, 255, 0.1) 100%);
  border-radius: 999rpx;
  height: 36rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  text {
    font-size: 20rpx;
    color: $primary;
    font-weight: 600;
    letter-spacing: 1rpx;
  }
}

// ========== Section Common ==========
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28rpx;
  padding: 0 4rpx;
}

.section-title-wrapper {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.section-title-bar {
  width: 6rpx;
  height: 44rpx;
  background: linear-gradient(180deg, $primary 0%, $accent-cyan 100%);
  border-radius: 6rpx;
  box-shadow: 0 4rpx 8rpx rgba(15, 93, 227, 0.25);
}

.section-title-group {
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.section-title {
  font-size: 40rpx;
  font-weight: 700;
  color: $on-surface;
  letter-spacing: 0.5rpx;
  line-height: 1.2;
}

.section-subtitle {
  font-size: 22rpx;
  color: $on-surface-muted;
  letter-spacing: 0.5rpx;
}

.section-more {
  display: flex;
  align-items: center;
  gap: 6rpx;
  padding: 10rpx 20rpx;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 999rpx;
  border: 1rpx solid $outline-light;
  box-shadow: 0 4rpx 10rpx rgba(15, 93, 227, 0.04);

  text {
    font-size: 24rpx;
    color: $primary;
    font-weight: 500;
  }

  .iconfont {
    font-size: 22rpx;
    color: $primary;
  }
}

// ========== Products Section ==========
.products-section {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
}

.product-card {
  background: #ffffff;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow:
    0 12rpx 28rpx rgba(15, 93, 227, 0.08),
    0 2rpx 6rpx rgba(15, 93, 227, 0.04);
  border: 1rpx solid rgba(255, 255, 255, 0.9);
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

  &:active {
    transform: translateY(-4rpx);
    box-shadow:
      0 20rpx 40rpx rgba(15, 93, 227, 0.14),
      0 4rpx 8rpx rgba(15, 93, 227, 0.06);
  }
}

.product-image-wrapper {
  position: relative;
  width: 100%;
  aspect-ratio: 1;
  background: linear-gradient(135deg, $primary-fade 0%, $primary-soft 100%);
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
}

.product-shine {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, transparent 40%, rgba(255, 255, 255, 0.25) 50%, transparent 60%);
  pointer-events: none;
}

.product-tag {
  position: absolute;
  top: 16rpx;
  left: 16rpx;
  padding: 8rpx 18rpx;
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  color: #ffffff;
  font-size: 20rpx;
  font-weight: 600;
  border-radius: 999rpx;
  box-shadow: 0 6rpx 14rpx rgba(15, 93, 227, 0.3);
  letter-spacing: 1rpx;
}

.product-info {
  padding: 24rpx 24rpx 22rpx;
}

.product-name {
  font-size: 30rpx;
  font-weight: 600;
  color: $on-surface;
  margin-bottom: 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  letter-spacing: 0.5rpx;
}

.product-spec {
  font-size: 22rpx;
  color: $on-surface-muted;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 18rpx;
  line-height: 1.4;
}

.product-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 16rpx;
  border-top: 1rpx dashed $outline-light;
}

.product-price {
  display: flex;
  align-items: baseline;
  gap: 2rpx;
  color: $primary;
}

.price-symbol {
  font-size: 24rpx;
  font-weight: 600;
}

.price-value {
  font-size: 32rpx;
  font-weight: 700;
  letter-spacing: 0.5rpx;
}

.product-arrow {
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  background: $primary-soft;
  display: flex;
  align-items: center;
  justify-content: center;

  .iconfont {
    font-size: 22rpx;
    color: $primary;
  }
}

// ========== Contact Section ==========
.contact-section {
  padding: 0 32rpx;
  margin-bottom: 48rpx;
}

.contact-content {
  background: linear-gradient(135deg, #ffffff 0%, $primary-soft 100%);
  border: 1rpx solid rgba(255, 255, 255, 0.9);
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow:
    0 12rpx 28rpx rgba(15, 93, 227, 0.08),
    0 2rpx 6rpx rgba(15, 93, 227, 0.04);
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.contact-divider {
  height: 1rpx;
  background: linear-gradient(90deg, transparent 0%, $outline 50%, transparent 100%);
  margin: 28rpx 0;
}

.contact-icon {
  width: 72rpx;
  height: 72rpx;
  background: linear-gradient(135deg, $primary-soft 0%, #ffffff 100%);
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: inset 0 0 0 1rpx rgba(255, 255, 255, 0.9);

  .iconfont {
    font-size: 32rpx;
    color: $primary;
  }
}

.contact-icon-accent {
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  box-shadow: 0 8rpx 18rpx rgba(15, 93, 227, 0.28);

  .iconfont {
    color: #ffffff;
  }
}

.contact-info {
  flex: 1;
  min-width: 0;
}

.contact-label {
  font-size: 24rpx;
  font-weight: 500;
  color: $on-surface-muted;
  margin-bottom: 8rpx;
  letter-spacing: 0.5rpx;
}

.contact-value {
  font-size: 26rpx;
  color: $on-surface;
  font-weight: 500;
  line-height: 1.4;
}

.contact-phone {
  font-size: 36rpx;
  font-weight: 700;
  color: $primary;
  letter-spacing: 1rpx;
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.contact-call-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 18rpx 28rpx;
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  color: #ffffff;
  font-size: 26rpx;
  font-weight: 600;
  border-radius: 999rpx;
  box-shadow: 0 8rpx 20rpx rgba(15, 93, 227, 0.32);
  transition: all 0.3s;
  flex-shrink: 0;

  .iconfont {
    font-size: 24rpx;
    color: #ffffff;
  }

  &:active {
    transform: scale(0.96);
    box-shadow: 0 4rpx 10rpx rgba(15, 93, 227, 0.24);
  }
}

.map-placeholder {
  position: relative;
  width: 100%;
  height: 280rpx;
  background: $primary-soft;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow:
    0 12rpx 28rpx rgba(15, 93, 227, 0.08),
    0 2rpx 6rpx rgba(15, 93, 227, 0.04);
  border: 1rpx solid rgba(255, 255, 255, 0.9);
}

.map-image {
  width: 100%;
  height: 100%;
  opacity: 0.9;
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
  background: linear-gradient(135deg, rgba(15, 93, 227, 0.15) 0%, rgba(54, 198, 255, 0.1) 100%);
  backdrop-filter: blur(2rpx);
}

.map-card {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 18rpx 32rpx;
  background: rgba(255, 255, 255, 0.95);
  border: 1rpx solid rgba(255, 255, 255, 0.9);
  border-radius: 999rpx;
  box-shadow: 0 10rpx 24rpx rgba(15, 93, 227, 0.18);

  .iconfont {
    font-size: 26rpx;
    color: $primary;
  }
}

.map-label {
  font-size: 26rpx;
  color: $primary;
  font-weight: 600;
  letter-spacing: 0.5rpx;
}

// ========== Bottom Padding ==========
.bottom-padding {
  height: 140rpx;
}
</style>
