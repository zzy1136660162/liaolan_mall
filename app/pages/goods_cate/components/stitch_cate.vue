<template>
  <view class="cate-page" :style="{height: winHeight + 'px'}">
    <!-- 搜索栏 -->
    <view class="top-bar">
      <view class="search-box" @click="goSearch">
        <text class="iconfont icon-sousuo"></text>
        <text class="placeholder">搜索商品</text>
      </view>
    </view>

    <!-- 顶部横向一级分类 tabs -->
    <scroll-view class="l1-tabs" scroll-x>
      <view class="l1-list">
        <view
          v-for="(item, index) in level1List"
          :key="item.id"
          class="l1-chip"
          :class="{ active: activeLevel1 === index }"
          @click="selectLevel1(index)"
        >{{ item.name }}</view>
      </view>
    </scroll-view>

    <!-- 面包屑 -->
    <view class="breadcrumb" v-if="currentLevel2">
      <text class="bread-item" @click="goLevel(1)">{{ currentLevel1.name }}</text>
      <text class="bread-arrow">></text>
      <text class="bread-item" @click="goLevel(2)">{{ currentLevel2.name }}</text>
      <template v-if="currentLevel3">
        <text class="bread-arrow">></text>
        <text class="bread-item active">{{ currentLevel3.name }}</text>
      </template>
    </view>

    <!-- 主内容区 -->
    <scroll-view class="main-scroll" scroll-y>
      <view class="scroll-inner">

        <!-- 用途提示 -->
        <view class="usage-tip" v-if="currentLevel1.description && !currentLevel2">
          <text class="usage-text">用途：{{ currentLevel1.description }}</text>
        </view>

        <!-- 二级/三级：双列卡片 -->
        <view class="card-grid" v-if="!isLeafLevel">
          <view
            v-for="item in currentCards"
            :key="item.id"
            class="card-item"
            @click="onCardClick(item)"
          >
            <view class="card-img-box">
              <image v-if="item.extra" class="card-img" :src="item.extra" mode="aspectFill"></image>
              <text v-else class="iconfont icon-shangpin card-placeholder"></text>
            </view>
            <text class="card-name">{{ item.name }}</text>
            <text class="card-desc" v-if="item.description">{{ item.description }}</text>
          </view>
        </view>

        <!-- 四级：全宽文字列表 -->
        <view class="leaf-list" v-else>
          <view
            v-for="item in currentCards"
            :key="item.id"
            class="leaf-item"
            @click="onCardClick(item)"
          >
            <text class="leaf-name">{{ item.name }}</text>
            <text class="iconfont icon-xiangyou leaf-arrow"></text>
          </view>
        </view>

        <view class="spacer"></view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getCategoryList } from '@/api/store.js';

export default {
  data() {
    return {
      winHeight: 0,
      categoryTree: [],
      activeLevel1: 0,
      selectedLevel2: null,
      selectedLevel3: null,
    }
  },
  computed: {
    level1List() {
      return this.categoryTree || [];
    },
    currentLevel1() {
      return this.level1List[this.activeLevel1] || null;
    },
    currentLevel2() {
      return this.selectedLevel2;
    },
    currentLevel3() {
      return this.selectedLevel3;
    },
    currentCards() {
      if (this.currentLevel3) return this.currentLevel3.child || [];
      if (this.currentLevel2) return this.currentLevel2.child || [];
      if (this.currentLevel1) return this.currentLevel1.child || [];
      return [];
    },
    isLeafLevel() {
      return !!this.currentLevel3;
    }
  },
  created() {
    uni.getSystemInfo({
      success: (res) => { this.winHeight = res.windowHeight; }
    });
    this.loadCategoryData();
  },
  onShow() {
    this.loadCategoryData();
  },
  methods: {
    loadCategoryData() {
      getCategoryList().then(res => {
        this.categoryTree = res.data || [];
      });
    },
    selectLevel1(index) {
      this.activeLevel1 = index;
      this.selectedLevel2 = null;
      this.selectedLevel3 = null;
    },
    goLevel(level) {
      if (level === 1) {
        this.selectedLevel2 = null;
        this.selectedLevel3 = null;
      } else if (level === 2) {
        this.selectedLevel3 = null;
      }
    },
    onCardClick(item) {
      if (!this.currentLevel2) {
        this.selectedLevel2 = item;
        this.selectedLevel3 = null;
      } else if (!this.currentLevel3) {
        this.selectedLevel3 = item;
      } else {
        uni.navigateTo({
          url: '/pages/goods/goods_list/index?cid=' + item.id + '&title=' + encodeURIComponent(item.name || '')
        });
      }
    },
    goSearch() {
      uni.navigateTo({
        url: '/pages/goods/goods_search/index'
      });
    }
  }
}
</script>

<style scoped lang="scss">
$primary: #0061a5;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$surface: #f9f9fc;
$surface-card: #ffffff;
$outline-variant: #bfc7d5;
$tertiary: #006875;

.cate-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  background: $surface;
  overflow: hidden;
  box-sizing: border-box;
}

// ========== 搜索栏 ==========
.top-bar {
  padding: 16rpx 32rpx;
  background: $surface-card;
}
.search-box {
  display: flex;
  align-items: center;
  height: 68rpx;
  background: rgba($on-surface-variant, 0.06);
  border-radius: 34rpx;
  padding: 0 24rpx;
  .iconfont { font-size: 28rpx; color: $on-surface-variant; margin-right: 12rpx; }
  .placeholder { font-size: 26rpx; color: rgba($on-surface-variant, 0.5); }
}

// ========== 一级 tabs ==========
.l1-tabs {
  white-space: nowrap;
  padding: 20rpx 0 28rpx;
  flex-shrink: 0;
}
.l1-list {
  display: flex;
  gap: 16rpx;
  padding: 0 32rpx;
}
.l1-chip {
  flex-shrink: 0;
  padding: 18rpx 36rpx;
  font-size: 28rpx;
  font-weight: 600;
  border-radius: 999rpx;
  background: rgba($tertiary, 0.06);
  color: $tertiary;
  &.active {
    background: $primary;
    color: #fff;
  }
}

// ========== 面包屑 ==========
.breadcrumb {
  display: flex;
  align-items: center;
  padding: 0 32rpx 16rpx;
  flex-shrink: 0;
}
.bread-item {
  font-size: 22rpx;
  color: $primary;
  &:active { opacity: 0.6; }
  &.active { color: $on-surface; font-weight: 600; }
}
.bread-arrow {
  font-size: 18rpx;
  color: $outline-variant;
  margin: 0 8rpx;
}

// ========== 用途提示 ==========
.usage-tip {
  background: rgba($tertiary, 0.06);
  border-radius: 16rpx;
  padding: 16rpx 24rpx;
  margin: 0 32rpx 24rpx;
  border: 1rpx solid rgba($tertiary, 0.1);
}
.usage-text {
  font-size: 24rpx;
  color: $on-surface-variant;
  line-height: 1.6;
}

// ========== 主滚动区 ==========
.main-scroll {
  flex: 1;
  overflow: hidden;
}
.scroll-inner {
  padding: 0 32rpx;
}

// ========== 双列卡片 ==========
.card-grid {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10rpx;
}
.card-item {
  width: calc(50% - 20rpx);
  margin: 0 10rpx 20rpx;
  box-sizing: border-box;
  background: $surface-card;
  border: 1rpx solid rgba($outline-variant, 0.3);
  border-radius: 20rpx;
  padding: 24rpx;
  &:active { border-color: $primary; }
}
.card-img-box {
  width: 100%;
  height: 180rpx;
  background: rgba($on-surface-variant, 0.04);
  border-radius: 14rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
  overflow: hidden;
}
.card-img {
  width: 100%;
  height: 100%;
}
.card-placeholder {
  font-size: 48rpx;
  color: rgba($primary, 0.15);
}
.card-name {
  display: block;
  font-size: 26rpx;
  font-weight: 600;
  color: $on-surface;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.card-desc {
  display: block;
  font-size: 22rpx;
  color: $on-surface-variant;
  margin-top: 6rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

// ========== 四级文字列表 ==========
.leaf-list {
  display: flex;
  flex-direction: column;
}
.leaf-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: $surface-card;
  border: 1rpx solid rgba($outline-variant, 0.3);
  border-radius: 16rpx;
  padding: 28rpx 24rpx;
  margin-bottom: 16rpx;
  box-sizing: border-box;
  &:active { border-color: $primary; }
}
.leaf-name {
  font-size: 26rpx;
  color: $on-surface;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}
.leaf-arrow {
  font-size: 28rpx;
  color: $outline-variant;
  flex-shrink: 0;
}

.spacer { height: 60rpx; }
</style>
