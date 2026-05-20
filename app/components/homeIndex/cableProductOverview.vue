<template>
  <view class="cable-overview">
    <view class="section-header">
      <view class="header-left">
        <view class="blue-bar"></view>
        <text class="header-title">电缆产品总览</text>
      </view>
      <view class="header-right" @click="goAllProducts">
        <text class="more-text">查看全部</text>
        <text class="iconfont icon-xiangyou"></text>
      </view>
    </view>

    <view class="tab-filter">
      <scroll-view scroll-x style="white-space: nowrap; display: flex" show-scrollbar="false">
        <view class="tab-item" :class="{ active: activeTab === index }" v-for="(tab, index) in tabs" :key="index" @click="switchTab(index)">
          {{ tab.name }}
        </view>
      </scroll-view>
    </view>

    <view class="product-grid" v-if="productList.length">
      <view class="product-card" v-for="(item, index) in productList" :key="index" @click="goDetail(item)">
        <view class="card-image">
          <easy-loadimage :image-src="item.image" :radius="8"></easy-loadimage>
          <view class="stock-tag" v-if="item.stock > 0">现货</view>
        </view>
        <view class="card-info">
          <view class="product-name line2">{{ item.storeName }}</view>
          <view class="product-specs" v-if="item.productTags && item.productTags.locationUnderTitle.length">
            <text class="spec-tag" v-for="(tag, ti) in item.productTags.locationUnderTitle.slice(0, 2)" :key="ti">{{ tag.tagName }}</text>
          </view>
          <view class="price-row">
            <view class="product-price">
              <text class="price-symbol">￥</text>
              <text class="price-value">{{ item.price }}</text>
            </view>
            <view class="sales-count">已售{{ item.sales || 0 }}</view>
          </view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-if="!loading && !productList.length">
      <text class="empty-text">暂无电缆产品</text>
    </view>

    <view class="load-more" @click="loadMore" v-if="!loadEnd && productList.length">
      <text>加载更多</text>
    </view>
  </view>
</template>

<script>
import { getProductslist } from '@/api/store.js';
import easyLoadimage from '@/components/base/easy-loadimage.vue';

export default {
  name: 'cableProductOverview',
  components: { easyLoadimage },
  data() {
    return {
      tabs: [
        { name: '推荐', type: 0 },
        { name: '热销', type: 1 },
        { name: '新品', type: 2 },
        { name: '电力电缆', type: 3, cid: '' },
        { name: '控制电缆', type: 3, cid: '' },
        { name: '通信电缆', type: 3, cid: '' }
      ],
      activeTab: 0,
      productList: [],
      page: 1,
      limit: 10,
      loadEnd: false,
      loading: false
    }
  },
  mounted() {
    this.getProductList();
  },
  methods: {
    switchTab(index) {
      this.activeTab = index;
      this.page = 1;
      this.productList = [];
      this.loadEnd = false;
      this.getProductList();
    },
    getProductList() {
      if (this.loading || this.loadEnd) return;
      this.loading = true;
      let params = {
        page: this.page,
        limit: this.limit
      };
      let tab = this.tabs[this.activeTab];
      if (tab.type === 1) {
        params.salesOrder = 'desc';
      } else if (tab.type === 2) {
        params.isNew = 1;
      } else if (tab.type === 3 && tab.cid) {
        params.cid = tab.cid;
      }
      getProductslist(params).then(res => {
        let list = res.data.list || [];
        if (this.page === 1) {
          this.productList = list;
        } else {
          this.productList = this.productList.concat(list);
        }
        this.loadEnd = this.page >= res.data.totalPage;
        this.loading = false;
        this.page++;
      }).catch(() => {
        this.loading = false;
      });
    },
    loadMore() {
      this.getProductList();
    },
    goDetail(item) {
      uni.navigateTo({
        url: `/pages/goods/goods_details/index?id=${item.id}`
      });
    },
    goAllProducts() {
      uni.switchTab({
        url: '/pages/goods_cate/goods_cate'
      });
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
    transform: translateY(16rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.cable-overview {
  margin: 12rpx 24rpx;
  padding: 20rpx;
  background: $surface-lowest;
  border: 1rpx solid $outline-variant;
  border-radius: $r-md;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
  animation: fadeIn 0.4s $ease both;

  .section-header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24rpx;

    .header-left {
      display: flex;
      flex-direction: row;
      align-items: center;

      .blue-bar {
        width: 6rpx;
        height: 32rpx;
        background: $primary;
        border-radius: 3rpx;
        margin-right: 12rpx;
      }

      .header-title {
        color: $on-surface;
        font-weight: 600;
        font-size: 32rpx;
      }
    }

    .header-right {
      display: flex;
      flex-direction: row;
      align-items: center;
      color: $secondary;
      font-size: 24rpx;
      transition: opacity 0.2s $ease;

      &:active {
        opacity: 0.6;
      }

      .more-text {
        margin-right: 4rpx;
      }
    }
  }

  .tab-filter {
    margin-bottom: 20rpx;

    .tab-item {
      display: inline-block;
      padding: 12rpx 24rpx;
      font-size: 26rpx;
      color: $on-surface-variant;
      background: $surface-low;
      border-radius: 32rpx;
      margin-right: 16rpx;
      transition: all 0.2s $ease;

      &.active {
        color: $white;
        background: $primary;
        font-weight: 500;
      }
    }
  }

  .product-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16rpx;

    .product-card {
      background: $surface-lowest;
      border: 1rpx solid $outline-variant;
      border-radius: $r-md;
      overflow: hidden;
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
      transition: transform 0.15s $ease;

      &:active {
        transform: scale(0.97);
      }

      .card-image {
        position: relative;
        aspect-ratio: 1;
        background: $surface-low;

        .stock-tag {
          position: absolute;
          top: 12rpx;
          left: 12rpx;
          background: $primary-container;
          color: $white;
          font-size: 18rpx;
          padding: 2rpx 12rpx;
          border-radius: 4rpx;
        }
      }

      .card-info {
        padding: 16rpx;

        .product-name {
          font-size: 26rpx;
          color: $on-surface;
          font-weight: 500;
          line-height: 36rpx;
          height: 72rpx;
          overflow: hidden;

          &.line2 {
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
          }
        }

        .product-specs {
          margin-top: 8rpx;
          display: flex;
          gap: 8rpx;

          .spec-tag {
            font-size: 18rpx;
            color: $primary;
            background: $surface-low;
            border: 1rpx solid rgba($primary, 0.15);
            border-radius: 4rpx;
            padding: 0 8rpx;
            line-height: 28rpx;
          }
        }

        .product-price {
          margin-top: 12rpx;
          color: $tertiary-container;

          .price-symbol {
            font-size: 24rpx;
          }

          .price-value {
            font-size: 34rpx;
            font-weight: 700;
          }
        }

        .price-row {
          display: flex;
          align-items: baseline;
          justify-content: space-between;
          margin-top: 12rpx;

          .sales-count {
            font-size: 22rpx;
            color: $secondary;
          }
        }
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 60rpx 0;

    .empty-text {
      font-size: 28rpx;
      color: $on-surface-variant;
    }
  }

  .load-more {
    text-align: center;
    padding: 20rpx;
    color: $secondary;
    font-size: 26rpx;
    border-top: 1rpx solid $outline-variant;
    margin-top: 16rpx;
  }
}
</style>
