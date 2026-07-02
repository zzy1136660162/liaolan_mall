<template>
  <view class="cate-page" :style="{height: winHeight + 'px'}">
    <!-- 搜索栏 -->
    <view class="top-bar">
      <view class="search-box" @click="goSearch">
        <text class="iconfont icon-sousuo"></text>
        <text class="placeholder">搜索商品</text>
      </view>
    </view>
    <view class="main-body">
      <!-- ========== 左侧栏：一 + 二级 ========== -->
      <scroll-view class="left-panel" scroll-y>
        <view v-for="(l1, i) in level1List" :key="l1.id" class="l1-group">
          <view class="l1-row" :class="{ active: activeLevel1 === i }" @click="toggleLevel1(i)">
            <text class="l1-name">{{ l1.name }}</text>
            <text class="l1-arrow" :class="{ open: activeLevel1 === i && l1Expanded }">▾</text>
          </view>
          <view class="l2-list" v-if="activeLevel1 === i && l1Expanded">
            <view
              v-for="l2 in l1.child" :key="l2.id"
              class="l2-row"
              :class="{ active: selectedLevel2 && selectedLevel2.id === l2.id }"
              @click="selectLevel2(l2)"
            >{{ l2.name }}</view>
          </view>
        </view>
      </scroll-view>

      <!-- ========== 右侧栏 ========== -->
      <view class="right-panel">
        <scroll-view class="right-scroll" scroll-y>
          <view class="right-inner">

            <!-- 空态 -->
            <view class="empty-hint" v-if="!selectedLevel2 && !selectedLevel4">
              <text class="hint-icon">☜</text>
              <text class="hint-text">请选择左侧分类</text>
            </view>

            <!-- 商品列表（四级选中后） -->
            <view class="goods-section" v-if="selectedLevel4">
              <view class="goods-header">
                <view class="goods-back" @click="backToLevel3">
                  <text class="iconfont icon-xiangzuo"></text>
                  <text>返回</text>
                </view>
                <text class="goods-title">{{ selectedLevel4Name }}</text>
              </view>
              <view class="goods-grid">
                <view v-for="item in productList" :key="item.id" class="goods-card" @click="goProductDetail(item)">
                  <view class="goods-img-box">
                    <image :src="item.image" mode="aspectFill" class="goods-img"></image>
                  </view>
                  <view class="goods-info">
                    <text class="goods-name">{{ item.storeName }}</text>
                    <view class="goods-price-row">
                      <text class="goods-price">¥{{ item.price }}</text>
                      <text class="goods-sold" v-if="item.sales">已售{{ item.sales }}</text>
                    </view>
                  </view>
                </view>
              </view>
              <view class="goods-empty" v-if="productLoaded && productList.length === 0">暂无商品</view>
              <view class="goods-loading" v-if="productLoading">加载中...</view>
            </view>

            <view class="spacer"></view>
          </view>
        </scroll-view>

        <!-- ====== 分类选择遮罩层 ====== -->
        <view class="overlay" v-if="showOverlay" @click="closeOverlay">
          <view class="overlay-popup" @click.stop>
            <view class="overlay-header">
              <view class="overlay-bread" v-if="overlayLevel3">
                <text class="overlay-back" @click="overlayLevel3 = null">
                  <text class="iconfont icon-xiangzuo"></text> 返回
                </text>
                <text class="overlay-arrow">></text>
                <text class="overlay-title">{{ overlayLevel3.name }}</text>
              </view>
              <text class="overlay-title" v-else>{{ selectedLevel2 ? selectedLevel2.name : '' }}</text>
              <text class="overlay-close" @click="closeOverlay">✕</text>
            </view>
            <scroll-view class="overlay-body" scroll-y>
              <!-- 三级 -->
              <view class="l3-list" v-if="!overlayLevel3">
                <view v-for="item in level3List" :key="item.id" class="l3-card" @click="openLevel4(item)">
                  <view class="l3-icon-box">
                    <text class="iconfont icon-shangpin"></text>
                  </view>
                  <view class="l3-info">
                    <text class="l3-name">{{ item.name }}</text>
                    <text class="l3-count" v-if="item.child && item.child.length">{{ item.child.length }} 个型号</text>
                  </view>
                  <text class="iconfont icon-xiangyou l3-arrow"></text>
                </view>
              </view>
              <!-- 四级 -->
              <view class="l4-chips" v-else>
                <view v-for="item in overlayLevel4List" :key="item.id" class="l4-chip" @click="selectLevel4(item)">
                  <text class="l4-model">{{ item.name }}</text>
                  <text class="l4-desc" v-if="item.description">{{ item.description }}</text>
                </view>
              </view>
            </scroll-view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getCategoryList, getProductslist } from '@/api/store.js';

export default {
  data() {
    return {
      winHeight: 0,
      categoryTree: [],
      activeLevel1: 0,
      l1Expanded: false,
      selectedLevel2: null,
      // 遮罩层
      showOverlay: false,
      overlayLevel3: null,
      // 已选四级 → 商品
      selectedLevel4: null,
      selectedLevel3ForBack: null, // 记录四级对应的三级，用于返回
      selectedLevel4Name: '',
      productList: [],
      productLoading: false,
      productLoaded: false,
    }
  },
  computed: {
    level1List() {
      const tree = this.categoryTree || [];
      return Array.isArray(tree) ? tree : [];
    },
    level3List() {
      const l2 = this.selectedLevel2;
      return (l2 && Array.isArray(l2.child)) ? l2.child : [];
    },
    overlayLevel4List() {
      const l3 = this.overlayLevel3;
      return (l3 && Array.isArray(l3.child)) ? l3.child : [];
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
        const data = res.data || [];
        const ensureArr = (list) => {
          if (!Array.isArray(list)) return [];
          return list.map(item => ({ ...item, child: ensureArr(item.child) }));
        };
        this.categoryTree = ensureArr(data);
      });
    },
    toggleLevel1(index) {
      if (this.activeLevel1 === index) {
        this.l1Expanded = !this.l1Expanded;
      } else {
        this.activeLevel1 = index;
        this.l1Expanded = true;
      }
      this.selectedLevel2 = null;
      this.selectedLevel4 = null;
    },
    selectLevel2(item) {
      this.selectedLevel2 = item;
      this.selectedLevel4 = null;
      this.overlayLevel3 = null;
      this.showOverlay = true;
    },
    // 遮罩中点击三级 → 看四级
    openLevel4(item) {
      this.overlayLevel3 = item;
    },
    closeOverlay() {
      this.showOverlay = false;
      this.overlayLevel3 = null;
    },
    // 选中四级 → 关闭遮罩，拉商品
    selectLevel4(item) {
      this.selectedLevel3ForBack = this.overlayLevel3;
      this.showOverlay = false;
      this.overlayLevel3 = null;
      this.selectedLevel4 = item.id;
      this.selectedLevel4Name = item.name;
      this.loadProducts(item.id);
    },
    // 返回 → 回到四级分类展示
    backToLevel3() {
      if (this.selectedLevel3ForBack) {
        this.overlayLevel3 = this.selectedLevel3ForBack;
        this.showOverlay = true;
      }
      this.selectedLevel4 = null;
      this.productList = [];
      this.productLoaded = false;
    },
    loadProducts(cid) {
      this.productLoading = true;
      this.productLoaded = false;
      this.productList = [];
      getProductslist({ cid, page: 1, limit: 20 }).then(res => {
        this.productList = res.data.list || [];
        this.productLoaded = true;
      }).catch(() => {
        this.productLoaded = true;
      }).finally(() => {
        this.productLoading = false;
      });
    },
    goProductDetail(item) {
      uni.navigateTo({
        url: '/pages/goods/goods_details/index?id=' + item.id
      });
    },
    goSearch() {
      uni.navigateTo({ url: '/pages/goods/goods_search/index' });
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
$surface-low: #f3f3f6;
$outline-variant: #bfc7d5;
$tertiary: #006875;

.cate-page {
  width: 100%; display: flex; flex-direction: column;
  background: $surface; overflow: hidden; box-sizing: border-box;
}

// 搜索
.top-bar { padding: 16rpx 24rpx; background: $surface-card; flex-shrink: 0; }
.search-box {
  display: flex; align-items: center; height: 68rpx;
  background: rgba($on-surface-variant, 0.06); border-radius: 34rpx; padding: 0 24rpx;
  .iconfont { font-size: 28rpx; color: $on-surface-variant; margin-right: 12rpx; }
  .placeholder { font-size: 26rpx; color: rgba($on-surface-variant, 0.5); }
}

// 主体
.main-body { flex: 1; display: flex; overflow: hidden; }

// 左栏
.left-panel { width: 200rpx; flex-shrink: 0; height: 100%; background: $surface-low; border-right: 1rpx solid rgba($outline-variant,0.25); }
.l1-group { border-bottom: 1rpx solid rgba($outline-variant,0.15); }
.l1-row {
  display: flex; align-items: center; justify-content: space-between;
  padding: 26rpx 16rpx 26rpx 20rpx; box-sizing: border-box;
  &.active { background: rgba($primary,0.06); .l1-name { color: $primary; font-weight: 700; } }
}
.l1-name { font-size: 24rpx; color: $on-surface; font-weight: 500; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.l1-arrow { font-size: 18rpx; color: $on-surface-variant; flex-shrink: 0; transition: transform 0.2s; &.open { transform: rotate(180deg); } }
.l2-list { padding: 0 0 8rpx; animation: fadeSlideIn 0.2s ease; }
@keyframes fadeSlideIn { from { opacity:0; transform:translateY(-8rpx); } to { opacity:1; transform:translateY(0); } }
.l2-row {
  padding: 22rpx 16rpx 22rpx 36rpx; font-size: 22rpx; color: $on-surface-variant;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap; box-sizing: border-box;
  &.active { background: $surface-card; color: $primary; font-weight: 600; }
}

// 右栏
.right-panel { flex: 1; height: 100%; position: relative; overflow: hidden; }
.right-scroll { height: 100%; }
.right-inner { padding: 0 20rpx; }

// 空态
.empty-hint { display: flex; flex-direction: column; align-items: center; padding-top: 200rpx; }
.hint-icon { font-size: 48rpx; color: rgba($on-surface-variant,0.25); margin-bottom: 16rpx; }
.hint-text { font-size: 26rpx; color: rgba($on-surface-variant,0.4); }

// 三级卡片
.l3-card {
  display: flex; align-items: center; background: $surface-card;
  border: 1rpx solid rgba($outline-variant,0.3); border-radius: 16rpx;
  padding: 24rpx 20rpx; margin-bottom: 16rpx; box-sizing: border-box;
  &:active { border-color: $primary; }
}
.l3-icon-box {
  width: 68rpx; height: 68rpx; background: rgba($primary,0.06); border-radius: 14rpx;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0; margin-right: 18rpx;
  .iconfont { font-size: 32rpx; color: $primary; }
}
.l3-info { flex: 1; min-width: 0; }
.l3-name { display: block; font-size: 26rpx; font-weight: 600; color: $on-surface; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.l3-count { display: block; font-size: 22rpx; color: $tertiary; margin-top: 4rpx; }
.l3-arrow { font-size: 26rpx; color: $outline-variant; flex-shrink: 0; }

// ===== 遮罩层（从上方弹出） =====
.overlay {
  position: absolute; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.4); z-index: 10;
  display: flex; align-items: flex-start;
}
.overlay-popup {
  width: 100%;
  background: $surface-card;
  border-radius: 0 0 32rpx 32rpx;
  display: flex; flex-direction: column;
  animation: slideDown 0.25s ease;
}
@keyframes slideDown { from { transform: translateY(-100%); } to { transform: translateY(0); } }
.overlay-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 32rpx 32rpx 20rpx; flex-shrink: 0;
  border-bottom: 1rpx solid rgba($outline-variant,0.15);
}
.overlay-bread {
  display: flex; align-items: center; flex: 1;
}
.overlay-back {
  display: flex; align-items: center; gap: 4rpx;
  font-size: 24rpx; color: $primary;
  .iconfont { font-size: 26rpx; }
}
.overlay-arrow { font-size: 20rpx; color: $outline-variant; margin: 0 8rpx; }
.overlay-title { font-size: 30rpx; font-weight: 700; color: $on-surface; }
.overlay-close { font-size: 32rpx; color: $on-surface-variant; padding: 8rpx; flex-shrink: 0; }
.overlay-body { flex: 1; padding: 16rpx 32rpx 40rpx; }
.l3-list {
  display: flex; flex-direction: column;
}
.l4-chips { display: flex; flex-wrap: wrap; margin: 0 -8rpx; }
.l4-chip {
  padding: 18rpx 28rpx; margin: 8rpx 8rpx 0;
  background: rgba($tertiary,0.05); border: 1rpx solid rgba($tertiary,0.12);
  border-radius: 12rpx; box-sizing: border-box;
  &:active { background: rgba($tertiary,0.12); }
}
.l4-model { display: block; font-size: 24rpx; font-weight: 500; color: $on-surface; line-height: 1.4; }
.l4-desc { display: block; font-size: 20rpx; color: $on-surface-variant; margin-top: 4rpx; }

// ===== 商品区 =====
.goods-section { padding-top: 8rpx; }
.goods-header { display: flex; align-items: center; padding: 12rpx 0 20rpx; }
.goods-back { display: flex; align-items: center; gap: 4rpx; font-size: 24rpx; color: $primary; .iconfont { font-size: 28rpx; } }
.goods-title { font-size: 28rpx; font-weight: 700; color: $on-surface; margin-left: 12rpx; }
.goods-grid { display: flex; flex-wrap: wrap; margin: 0 -10rpx; }
.goods-card {
  width: calc(50% - 20rpx); margin: 0 10rpx 20rpx; box-sizing: border-box;
  background: $surface-card; border: 1rpx solid rgba($outline-variant,0.3);
  border-radius: 16rpx; overflow: hidden;
  &:active { border-color: $primary; }
}
.goods-img-box { width: 100%; height: 200rpx; background: rgba($on-surface-variant,0.04); overflow: hidden; }
.goods-img { width: 100%; height: 100%; }
.goods-info { padding: 16rpx 18rpx; }
.goods-name { font-size: 24rpx; color: $on-surface; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; display: block; }
.goods-price-row { display: flex; align-items: baseline; justify-content: space-between; margin-top: 8rpx; }
.goods-price { font-size: 28rpx; font-weight: 700; color: $primary; }
.goods-sold { font-size: 20rpx; color: $on-surface-variant; }
.goods-empty, .goods-loading { text-align: center; padding: 80rpx 0; font-size: 26rpx; color: $on-surface-variant; }

.spacer { height: 40rpx; }
</style>
