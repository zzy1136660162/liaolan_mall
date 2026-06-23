<template>
  <view class="liaolan-home">
    <!-- Top Navigation -->
    <view class="top-nav">
      <view class="nav-left" @click="handleMenuClick">
        <text class="iconfont icon-caidan"></text>
      </view>
      <view class="nav-title">沈阳辽缆电线电缆有限公司</view>
      <view class="nav-right" @click="handleSearchClick">
        <text class="iconfont icon-sousuo"></text>
      </view>
    </view>

    <view class="page-container">
      <!-- Hero Section -->
      <view class="hero-section">
        <image class="hero-bg" src="https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260603141410866_673280054735.jpg" mode="aspectFill"></image>
        <view class="hero-overlay"></view>
        <view class="hero-content">
          <view class="hero-badge">
            <text>SINCE 2005</text>
          </view>
          <h2 class="hero-title">连接世界<br>品质辽缆</h2>
          <p class="hero-desc">源自沈阳，专注高性能电线电缆研发与制造，为现代基建提供强韧动力。</p>
          <view class="hero-btns">
            <view class="hero-btn-primary" @click="goToAbout">
              <text>了解品牌</text>
            </view>
            <view class="hero-btn-secondary" @click="goToTech">
              <text>核心技术</text>
            </view>
          </view>
        </view>
      </view>

      <!-- Product Categories: Bento Grid -->
      <view class="categories-section">
        <view class="section-header">
          <view class="section-title-group">
            <h3 class="section-title">产品中心</h3>
            <text class="section-subtitle">满足各类严苛工况的专业方案</text>
          </view>
          <text class="section-more" @click="goToProducts">查看全部</text>
        </view>
        <view class="bento-grid">
          <!-- High Voltage - Full Width -->
          <view class="bento-item bento-large" @click="goToProductCate('high_voltage')">
            <image class="bento-bg" src="https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260623175706254_675021430310.png" mode="aspectFill"></image>
            <view class="bento-overlay"></view>
            <view class="bento-content">
              <view class="bento-tag">
                <text>HIGH VOLTAGE</text>
              </view>
              <h4 class="bento-name">高压电力电缆</h4>
              <view class="bento-arrow">
                <text class="iconfont icon-xiangyou"></text>
              </view>
            </view>
          </view>
          <!-- Control Cable -->
          <view class="bento-item" @click="goToProductCate('control')">
            <image class="bento-bg" src="https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260623175803168_675021487225.png" mode="aspectFill"></image>
            <view class="bento-overlay"></view>
            <view class="bento-content">
              <view class="bento-tag bento-tag-yellow">
                <text>CONTROL</text>
              </view>
              <h4 class="bento-name">精密控制电缆</h4>
              <view class="bento-icon">
                <text class="iconfont icon-shezhi"></text>
              </view>
            </view>
          </view>
          <!-- Fire Resistant -->
          <view class="bento-item" @click="goToProductCate('fireproof')">
            <image class="bento-bg" src="https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260623175829410_675021513468.png" mode="aspectFill"></image>
            <view class="bento-overlay"></view>
            <view class="bento-content">
              <view class="bento-tag bento-tag-red">
                <text>FIREPROOF</text>
              </view>
              <h4 class="bento-name">特种防火电缆</h4>
              <view class="bento-icon">
                <text class="iconfont icon-huoyan"></text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- Strength Stats -->
      <view class="stats-section">
        <view class="stats-item">
          <view class="stats-value">30+</view>
          <view class="stats-label">行业经验</view>
        </view>
        <view class="stats-divider"></view>
        <view class="stats-item">
          <view class="stats-value">100k</view>
          <view class="stats-label">年产值</view>
        </view>
        <view class="stats-divider"></view>
        <view class="stats-item">
          <view class="stats-value">500+</view>
          <view class="stats-label">核心工程</view>
        </view>
      </view>

      <!-- Enterprise News -->
      <view class="news-section">
        <view class="section-header">
          <h3 class="section-title">企业动态</h3>
          <text class="iconfont icon-shezhi news-filter"></text>
        </view>
        <view class="news-list">
          <view class="news-item" v-for="(item, index) in newsList" :key="index" @click="goToNewsDetail(item.id)">
            <view class="news-image">
              <image :src="item.image" mode="aspectFill"></image>
            </view>
            <view class="news-info">
              <text class="news-date">{{ item.date }}</text>
              <h5 class="news-title">{{ item.title }}</h5>
              <p class="news-summary">{{ item.summary }}</p>
            </view>
          </view>
        </view>
      </view>

      <!-- Bottom Padding -->
      <view class="bottom-padding"></view>
    </view>

    <!-- Bottom Navigation Bar -->
    <tab-bar></tab-bar>

    <!-- FAB: Contact Button -->
    <view class="fab-button" @click="handleContact">
      <text class="iconfont icon-kefu"></text>
    </view>
  </view>
</template>

<script>
import tabBar from '@/components/tab-bar/index.vue';

export default {
  components: {
    tabBar
  },
  data() {
    return {
      newsList: [
        {
          id: 1,
          image: 'https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260603141410866_673280054735.jpg',
          date: '2024.11.20',
          title: '沈阳辽缆荣获省级"专精特新"企业称号',
          summary: '公司在高性能特种电缆研发领域取得新突破...'
        },
        {
          id: 2,
          image: 'https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260603141410866_673280054735.jpg',
          date: '2024.11.15',
          title: '参与沈阳市轨道交通10号线供电系统建设',
          summary: '辽缆牌电力电缆成功通过极端工况测试...'
        }
      ]
    };
  },
  onLoad() {
    this.fetchNews();
  },
  methods: {
    fetchNews() {
      // 可调用API获取新闻列表
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
    goToAbout() {
      uni.navigateTo({
        url: '/pages/about/index'
      });
    },
    goToTech() {
      uni.navigateTo({
        url: '/pages/tech/index'
      });
    },
    goToProducts() {
      uni.switchTab({
        url: '/pages/goods_cate/goods_cate'
      });
    },
    goToProductCate(type) {
      uni.navigateTo({
        url: `/pages/goods/goods_list/index?category=${type}`
      });
    },
    goToNewsDetail(id) {
      uni.navigateTo({
        url: `/pages/news/news_detail/index?id=${id}`
      });
    },
    handleContact() {
      uni.makePhoneCall({
        phoneNumber: '400-888-9999'
      });
    }
  }
};
</script>

<style lang="scss" scoped>
// ========== 主题色 ==========
$primary: #0061a5;
$primary-light: #0099ff;
$secondary: #fdd400;
$tertiary: #00a4b7;
$error: #ba1a1a;

$on-primary: #ffffff;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$on-secondary: #ffffff;
$on-tertiary: #ffffff;
$on-error: #ffffff;

$surface: #f9f9fc;
$surface-container: #eeeef0;
$surface-container-low: #f3f3f6;
$outline-variant: #bfc7d5;

$secondary-container: #fdd400;
$primary-container: #0099ff;
$tertiary-container: #00a4b7;
$error-container: #ffdad6;

$on-secondary-container: #6f5c00;
$on-primary-container: #002f54;
$on-tertiary-container: #004f58;
$on-error-container: #93000a;

// ========== 页面容器 ==========
.liaolan-home {
  position: relative;
  min-height: 100vh;
  background: $surface;
  overflow-x: hidden;
}

// ========== Top Navigation ==========
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 50;
  height: 128rpx;
  padding: 40rpx 32rpx 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba($surface, 0.85);
  backdrop-filter: blur(40rpx);
  -webkit-backdrop-filter: blur(40rpx);

  .nav-left,
  .nav-right {
    width: 80rpx;
    height: 80rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: all 0.3s;

    &:active {
      transform: scale(0.92);
      background: rgba($primary, 0.08);
    }

    .iconfont {
      font-size: 40rpx;
      color: $primary;
    }
  }

  .nav-title {
    font-size: 32rpx;
    font-weight: 700;
    color: $primary;
    flex: 1;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.page-container {
  padding-top: 188rpx;
  padding-bottom: 200rpx;
}

// ========== Hero Section ==========
.hero-section {
  position: relative;
  height: 560rpx;
  overflow: hidden;
  margin: 0 32rpx;
  border-radius: 32rpx;

  .hero-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }

  .hero-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba($primary-container, 0.6) 0%, rgba($primary, 0.4) 50%, rgba(0, 0, 0, 0.2) 100%);
  }

  .hero-content {
    position: relative;
    z-index: 10;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding: 48rpx;
  }

  .hero-badge {
    display: inline-flex;
    align-items: center;
    padding: 12rpx 24rpx;
    background: $secondary-container;
    border-radius: 999rpx;
    margin-bottom: 24rpx;
    width: fit-content;

    text {
      font-size: 22rpx;
      font-weight: 500;
      color: $on-secondary-container;
      letter-spacing: 2rpx;
    }
  }

  .hero-title {
    font-size: 64rpx;
    font-weight: 800;
    color: $on-primary;
    line-height: 1.2;
    margin-bottom: 20rpx;
    letter-spacing: -1rpx;
  }

  .hero-desc {
    font-size: 28rpx;
    color: rgba($on-primary, 0.9);
    line-height: 1.6;
    max-width: 600rpx;
    margin-bottom: 32rpx;
  }

  .hero-btns {
    display: flex;
    gap: 24rpx;

    .hero-btn-primary {
      padding: 24rpx 48rpx;
      background: #ffffff;
      border-radius: 24rpx;
      box-shadow: 0 10rpx 40rpx rgba($primary, 0.2);

      text {
        font-size: 30rpx;
        font-weight: 600;
        color: $primary;
      }

      &:active {
        transform: scale(0.96);
      }
    }

    .hero-btn-secondary {
      padding: 24rpx 48rpx;
      background: transparent;
      border: 4rpx solid rgba(255, 255, 255, 0.4);
      border-radius: 24rpx;

      text {
        font-size: 30rpx;
        font-weight: 600;
        color: #ffffff;
      }

      &:active {
        transform: scale(0.96);
      }
    }
  }
}

// ========== Section Common ==========
.categories-section,
.news-section {
  padding: 0 32rpx;
  margin-top: 64rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 32rpx;
}

.section-title-group {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.section-title {
  font-size: 40rpx;
  font-weight: 700;
  color: $on-surface;
}

.section-subtitle {
  font-size: 24rpx;
  color: $on-surface-variant;
}

.section-more {
  font-size: 28rpx;
  font-weight: 600;
  color: $primary;
}

// ========== Product Categories: Bento Grid ==========
.bento-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
}

.bento-item {
  position: relative;
  height: 352rpx;
  border-radius: 32rpx;
  overflow: hidden;
  background: #ffffff;
  border: 1rpx solid rgba($outline-variant, 0.3);
  box-shadow: 0 10rpx 40rpx rgba($primary, 0.1);

  .bento-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0.3;
  }

  .bento-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(180deg, transparent 0%, rgba(0, 0, 0, 0.1) 100%);
  }

  .bento-content {
    position: relative;
    z-index: 10;
    height: 100%;
    padding: 32rpx;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .bento-tag {
    display: inline-flex;
    padding: 8rpx 16rpx;
    background: $primary;
    border-radius: 12rpx;

    text {
      font-size: 20rpx;
      font-weight: 500;
      color: $on-primary;
      letter-spacing: 1rpx;
    }
  }

  .bento-tag-yellow {
    background: $secondary-container;

    text {
      color: $on-secondary-container;
    }
  }

  .bento-tag-red {
    background: $error-container;

    text {
      color: $on-error-container;
    }
  }

  .bento-name {
    font-size: 30rpx;
    font-weight: 600;
    color: $on-surface;
    margin-top: 16rpx;
  }

  .bento-arrow {
    width: 80rpx;
    height: 80rpx;
    background: $primary;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    align-self: flex-end;

    .iconfont {
      font-size: 32rpx;
      color: $on-primary;
    }
  }

  .bento-icon {
    align-self: flex-end;

    .iconfont {
      font-size: 48rpx;
      color: $primary;
    }
  }

  &:active {
    transform: scale(0.98);
  }
}

.bento-large {
  grid-column: span 2;
  height: 352rpx;
}

// ========== Strength Stats ==========
.stats-section {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin: 64rpx 32rpx;
  padding: 48rpx 32rpx;
  background: $surface-container-low;
  border-radius: 48rpx;
}

.stats-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;

  .stats-value {
    font-size: 48rpx;
    font-weight: 800;
    color: $primary;
  }

  .stats-label {
    font-size: 22rpx;
    font-weight: 500;
    color: $on-surface-variant;
    letter-spacing: 1rpx;
  }
}

.stats-divider {
  width: 2rpx;
  height: 80rpx;
  background: $outline-variant;
}

// ========== Enterprise News ==========
.news-section {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .news-filter {
      font-size: 36rpx;
      color: $on-surface-variant;
    }
  }
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.news-item {
  display: flex;
  gap: 24rpx;
  padding: 24rpx;
  background: #ffffff;
  border: 1rpx solid rgba($outline-variant, 0.3);
  border-radius: 32rpx;
  transition: all 0.3s;

  &:active {
    transform: scale(0.98);
    box-shadow: 0 10rpx 40rpx rgba($primary, 0.12);
  }

  .news-image {
    width: 192rpx;
    height: 192rpx;
    border-radius: 24rpx;
    overflow: hidden;
    flex-shrink: 0;

    image {
      width: 100%;
      height: 100%;
    }
  }

  .news-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 12rpx;

    .news-date {
      font-size: 22rpx;
      font-weight: 500;
      color: $primary;
      letter-spacing: 1rpx;
    }

    .news-title {
      font-size: 30rpx;
      font-weight: 600;
      color: $on-surface;
      line-height: 1.4;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .news-summary {
      font-size: 24rpx;
      color: $on-surface-variant;
      line-height: 1.5;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}

// ========== FAB Button ==========
.fab-button {
  position: fixed;
  bottom: 240rpx;
  right: 48rpx;
  z-index: 40;
  width: 112rpx;
  height: 112rpx;
  background: $primary;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10rpx 40rpx rgba($primary, 0.3);
  transition: all 0.3s;

  &:active {
    transform: scale(0.9);
  }

  .iconfont {
    font-size: 48rpx;
    color: $on-primary;
  }
}

// ========== Bottom Padding ==========
.bottom-padding {
  height: 200rpx;
}
</style>
