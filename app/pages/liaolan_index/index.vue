<template>
  <view class="liaolan-home">
    <!-- Top Navigation -->
    <view class="top-nav">
      <view class="nav-title">沈阳辽缆电线电缆有限公司</view>
    </view>

    <view class="page-container">
      <!-- Search Box -->
      <view class="search-box" @click="handleSearchClick">
        <text class="iconfont icon-sousuo search-icon"></text>
        <text class="search-placeholder">搜索产品</text>
      </view>

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
        <!-- 加载骨架屏 -->
        <view class="bento-grid" v-if="loadingCategories">
          <view v-for="i in 3" :key="i" class="bento-item skeleton-bento" :class="{'bento-large': i === 1}">
            <view class="bento-content">
              <view class="bento-name-skeleton"></view>
            </view>
          </view>
        </view>
        <!-- 实际数据 -->
        <view class="bento-grid" v-else>
          <view
            class="bento-item"
            :class="{'bento-large': index === 0}"
            v-for="(item, index) in bentoCategories"
            :key="item.id"
            @click="goToProductCate(item.id)"
          >
            <image class="bento-bg" :src="item.image" mode="aspectFill"></image>
            <view class="bento-overlay"></view>
            <view class="bento-content">
              <view class="bento-name-wrapper">
                <h4 class="bento-name">{{ item.name }}</h4>
              </view>
              <view class="bento-arrow" v-if="index === 0">
                <text class="iconfont icon-xiangyou"></text>
              </view>
              <view class="bento-icon" v-else>
                <text class="iconfont" :class="item.icon"></text>
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
        <!-- 加载骨架 -->
        <view class="news-list" v-if="loadingNews">
          <view v-for="i in 2" :key="i" class="news-item skeleton-news">
            <view class="news-image-skeleton"></view>
            <view class="news-info-skeleton">
              <view class="news-date-skeleton"></view>
              <view class="news-title-skeleton"></view>
              <view class="news-summary-skeleton"></view>
            </view>
          </view>
        </view>
        <!-- 实际数据 -->
        <view class="news-list" v-else>
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
import { getCategoryList } from '@/api/store.js';
import { getArticleList } from '@/api/api.js';

// Bento 图标配置
const BENTO_TAG_CONFIG = [
  { icon: 'icon-xiangyou' },
  { icon: 'icon-shezhi' },
  { icon: 'icon-huoyan' }
];

// 默认兜底数据
const DEFAULT_CATEGORIES = [
  {
    id: 1,
    name: '高压电力电缆',
    image: 'https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260623175706254_675021430310.png',
    icon: 'icon-xiangyou'
  },
  {
    id: 2,
    name: '精密控制电缆',
    image: 'https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260623175803168_675021487225.png',
    icon: 'icon-shezhi'
  },
  {
    id: 3,
    name: '特种防火电缆',
    image: 'https://www.lslnii.com/upload/NFSImgFile/appl/images/2025/12/20260623175829410_675021513468.png',
    icon: 'icon-huoyan'
  }
];

// 默认新闻兜底数据
const DEFAULT_NEWS = [
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
];
export default {
  components: {
    tabBar
  },
  data() {
    return {
      bentoCategories: [],
      loadingCategories: true,
      newsList: [],
      loadingNews: true,
    };
  },
  onLoad() {
    this.fetchCategories();
    this.fetchNews();
  },
  methods: {
    fetchCategories() {
      getCategoryList().then(res => {
        const list = res.data || [];
        if (list.length > 0) {
          this.bentoCategories = list.slice(0, 3).map((item, index) => ({
            id: item.id,
            name: item.name,
            image: DEFAULT_CATEGORIES[index] ? DEFAULT_CATEGORIES[index].image : '',
            icon: BENTO_TAG_CONFIG[index] ? BENTO_TAG_CONFIG[index].icon : 'icon-xiangyou'
          }));
        } else {
          this.bentoCategories = DEFAULT_CATEGORIES;
        }
      }).catch(() => {
        this.bentoCategories = DEFAULT_CATEGORIES;
      }).finally(() => {
        this.loadingCategories = false;
      });
    },
    fetchNews() {
      getArticleList(900192, { page: 1, limit: 2 }).then(res => {
        const list = (res.data && res.data.list) ? res.data.list : [];
        if (list.length > 0) {
          this.newsList = list.map(item => ({
            id: item.id,
            image: item.imageInput || '',
            date: item.createTime ? item.createTime.substring(0, 10) : '',
            title: item.title || '',
            summary: item.synopsis || ''
          }));
        } else {
          this.newsList = DEFAULT_NEWS;
        }
      }).catch(() => {
        this.newsList = DEFAULT_NEWS;
      }).finally(() => {
        this.loadingNews = false;
      });
    },
    handleSearchClick() {
      uni.switchTab({
        url: '/pages/goods_cate/goods_cate'
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
    goToProductCate(cid) {
      uni.navigateTo({
        url: `/pages/goods/goods_list/index?cid=${cid}`
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
  height: 158rpx;
  padding: 40rpx 32rpx 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba($surface, 0.85);
  backdrop-filter: blur(40rpx);
  -webkit-backdrop-filter: blur(40rpx);

  .nav-title {
    font-size: 32rpx;
    font-weight: 700;
    color: $primary;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

// ========== Search Box ==========
.search-box {
  height: 72rpx;
  margin: 0 32rpx 24rpx;
  background: #ffffff;
  border: 1rpx solid rgba($outline-variant, 0.5);
  border-radius: 36rpx;
  display: flex;
  align-items: center;
  padding: 0 32rpx;
  gap: 16rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);

  .search-icon {
    font-size: 32rpx;
    color: $on-surface-variant;
  }

  .search-placeholder {
    font-size: 28rpx;
    color: $on-surface-variant;
  }
}

.page-container {
  padding-top: calc(158rpx + 24rpx);
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

  .bento-name-wrapper {
    margin-top: 12rpx;
  }

  .bento-name {
    font-size: 34rpx;
    font-weight: 700;
    color: $on-surface;
    line-height: 1.3;
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

// ========== Bento Skeleton ==========
.skeleton-bento {
  background: $surface-container;
  border: 1rpx solid rgba($outline-variant, 0.2);
  box-shadow: none;

  .bento-name-skeleton {
    width: 280rpx;
    height: 40rpx;
    background: linear-gradient(90deg, $surface-container 25%, rgba($outline-variant, 0.4) 50%, $surface-container 75%);
    background-size: 200% 100%;
    animation: shimmer 1.5s infinite;
    border-radius: 8rpx;
    margin-top: 16rpx;
  }
}

@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
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

// ========== News Skeleton ==========
.skeleton-news {
  pointer-events: none;
  border-color: rgba($outline-variant, 0.15);
  box-shadow: none;

  .news-image-skeleton {
    width: 192rpx;
    height: 192rpx;
    border-radius: 24rpx;
    flex-shrink: 0;
    background: linear-gradient(90deg, $surface-container 25%, rgba($outline-variant, 0.4) 50%, $surface-container 75%);
    background-size: 200% 100%;
    animation: shimmer 1.5s infinite;
  }

  .news-info-skeleton {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 16rpx;
  }

  .news-date-skeleton {
    width: 140rpx;
    height: 24rpx;
    border-radius: 6rpx;
    background: linear-gradient(90deg, $surface-container 25%, rgba($outline-variant, 0.4) 50%, $surface-container 75%);
    background-size: 200% 100%;
    animation: shimmer 1.5s infinite;
  }

  .news-title-skeleton {
    width: 100%;
    height: 36rpx;
    border-radius: 6rpx;
    background: linear-gradient(90deg, $surface-container 25%, rgba($outline-variant, 0.4) 50%, $surface-container 75%);
    background-size: 200% 100%;
    animation: shimmer 1.5s infinite;
  }

  .news-summary-skeleton {
    width: 70%;
    height: 24rpx;
    border-radius: 6rpx;
    background: linear-gradient(90deg, $surface-container 25%, rgba($outline-variant, 0.4) 50%, $surface-container 75%);
    background-size: 200% 100%;
    animation: shimmer 1.5s infinite;
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
