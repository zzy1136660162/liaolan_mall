<template>
  <view class="cable-category-nav">
    <view class="title-bar">
      <view class="title-left">
        <view class="title-indicator"></view>
        <text class="title-text">产品分类</text>
      </view>
      <view class="title-right" @tap="goAllCategory">
        <text class="title-more">全部分类 ></text>
      </view>
    </view>
    <view class="category-grid" v-if="categoryList.length">
      <view
        v-for="(item, index) in categoryList"
        :key="index"
        class="category-item"
        @tap="goCategory(item)"
      >
        <view class="category-icon">
          <image v-if="item.extra" :src="item.extra" mode="aspectFit" class="icon-img"></image>
          <text v-else class="icon-text">{{ item.name.charAt(0) }}</text>
        </view>
        <text class="category-name">{{ item.name }}</text>
      </view>
    </view>
    <view class="category-grid" v-else>
      <view class="category-loading">加载中...</view>
    </view>
  </view>
</template>

<script>
import { getCategoryList } from '@/api/store.js';

export default {
  name: 'cableCategoryNav',
  data() {
    return {
      categoryList: []
    }
  },
  mounted() {
    this.loadCategory();
  },
  methods: {
    loadCategory() {
      getCategoryList().then(res => {
        if (res.data && res.data.length > 0) {
          this.categoryList = res.data.slice(0, 8);
        }
      }).catch(() => {});
    },
    goCategory(item) {
      if (!item || !item.id) return;
      uni.navigateTo({
        url: `/pages/goods/goods_list/index?cid=${item.id}&title=${encodeURIComponent(item.name)}`
      });
    },
    goAllCategory() {
      uni.switchTab({ url: '/pages/goods_cate/goods_cate' });
    }
  }
}
</script>

<style lang="scss" scoped>
$primary: #003da6;
$primary-container: #0052d9;
$tertiary-container: #895000;
$surface: #f9f9ff;
$surface-lowest: #ffffff;
$surface-low: #f1f3ff;
$outline-variant: #c3c6d7;
$on-surface: #181c23;
$on-surface-variant: #434654;
$secondary: #5c5f60;
$white: #ffffff;
$r-sm: 8rpx;
$r-md: 14rpx;
$r-lg: 20rpx;
$ease: cubic-bezier(0.22, 1, 0.36, 1);

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.cable-category-nav {
  margin: 16rpx 24rpx 8rpx;
  padding: 20rpx;
  background: $surface-lowest;
  border: 1rpx solid $outline-variant;
  border-radius: $r-md;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
  animation: fadeIn 0.4s $ease both;
}

.title-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24rpx;
}

.title-left {
  display: flex;
  align-items: center;
}

.title-indicator {
  width: 6rpx;
  height: 32rpx;
  background: $primary;
  border-radius: 3rpx;
  margin-right: 12rpx;
}

.title-text {
  font-size: 30rpx;
  font-weight: 600;
  color: $on-surface;
}

.title-right {
  display: flex;
  align-items: center;
}

.title-more {
  font-size: 24rpx;
  color: $secondary;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24rpx 16rpx;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: transform 0.15s $ease;

  &:active {
    transform: scale(0.95);
  }
}

.category-icon {
  width: 88rpx;
  height: 88rpx;
  background: $surface-low;
  border-radius: $r-md;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8rpx;
  overflow: hidden;

  .icon-img {
    width: 60rpx;
    height: 60rpx;
  }

  .icon-text {
    font-size: 36rpx;
    font-weight: 600;
    color: $primary;
  }
}

.category-name {
  font-size: 24rpx;
  font-weight: 500;
  color: $on-surface-variant;
  text-align: center;
  max-width: 120rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.category-loading {
  grid-column: 1 / -1;
  text-align: center;
  color: $secondary;
  font-size: 26rpx;
  padding: 40rpx 0;
}
</style>
