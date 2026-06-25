<template>
  <view class="case-page">
    <!-- Page Header -->
    <view class="page-header">
      <p class="header-desc">沈阳辽缆助力城市建设与工业升级</p>
    </view>

    <!-- Category Filter Chips -->
    <view class="filter-nav">
      <view class="filter-scroll">
        <view
          v-for="item in industryList"
          :key="item.value"
          class="filter-chip"
          :class="{ active: selectedIndustry === item.value }"
          @click="selectIndustry(item.value)"
        >
          {{ item.label }}
        </view>
      </view>
    </view>

    <!-- Bento Grid Cases -->
    <view class="bento-grid">
      <view
        v-for="(item, index) in caseList"
        :key="item.id"
        class="case-card"
        :class="[{ 'card-large': index === 0 }, { 'card-horizontal': index > 0 && index % 4 === 3 }]"
        @click="goToDetail(item.id)"
      >
        <!-- Large horizontal layout -->
        <template v-if="index > 0 && index % 4 === 3">
          <view class="card-h-left">
            <image :src="item.imageInput" mode="aspectFill" />
          </view>
          <view class="card-h-right">
            <text class="card-tag" v-if="item.industryCategory">{{ item.industryCategory }}</text>
            <h3 class="card-title">{{ item.projectName || item.title }}</h3>
            <p class="card-desc">{{ item.supplyProducts || item.projectDescription || '' }}</p>
          </view>
        </template>
        <!-- Standard cards (large first, rest square) -->
        <template v-else>
          <view class="card-img" :class="{ 'card-img-large': index === 0 }">
            <image :src="item.imageInput" mode="aspectFill" />
            <view class="card-tags" v-if="item.industryCategory || item.isFeatured">
              <text class="card-tag-blue">{{ item.industryCategory }}</text>
              <text class="card-tag-accent" v-if="item.isFeatured || index === 0">{{ index === 0 ? '重点项目' : '推荐' }}</text>
            </view>
          </view>
          <view class="card-body" :class="{ 'card-body-large': index === 0 }">
            <h3 class="card-title">{{ item.projectName || item.title }}</h3>
            <p class="card-desc">{{ item.supplyProducts || item.projectDescription || '' }}</p>
            <view class="card-link" v-if="index === 0">
              查看详情 <text class="iconfont icon-xiangyou"></text>
            </view>
          </view>
        </template>
      </view>
    </view>

    <!-- Load More -->
    <view class="load-more" v-if="!noMore">
      <view class="load-btn" @click="loadMore">
        加载更多案例
      </view>
    </view>
    <view v-if="loading" class="loading-tip">加载中...</view>
    <view v-if="noMore && caseList.length > 0" class="loading-tip">— 已加载全部案例 —</view>

    <tab-bar></tab-bar>
  </view>
</template>

<script>
import { getCaseList, getCaseIndustryCategories } from '@/api/api.js';
import tabBar from '@/components/tab-bar/index.vue';

export default {
  components: {
    tabBar
  },
  data() {
    return {
      urlDomain: this.$Cache.get("imgHost"),
      industryList: [],
      selectedIndustry: '',
      caseList: [],
      page: 1,
      limit: 10,
      loading: false,
      noMore: false
    };
  },
  onLoad() {
    this.loadIndustryCategories();
    this.getCaseList();
  },
  methods: {
    loadIndustryCategories() {
      getCaseIndustryCategories().then(res => {
        const list = [{ value: '', label: '全部案例' }];
        (res.data || []).forEach(item => {
          list.push({ value: item, label: item });
        });
        this.industryList = list;
      });
    },
    selectIndustry(value) {
      this.selectedIndustry = value;
      this.resetAndLoad();
    },
    resetAndLoad() {
      this.page = 1;
      this.caseList = [];
      this.noMore = false;
      this.getCaseList();
    },
    getCaseList() {
      if (this.loading || this.noMore) return;
      this.loading = true;
      
      let data = {
        page: this.page,
        limit: this.limit
      };
      
      if (this.selectedIndustry) {
        data.industryCategory = this.selectedIndustry;
      }
      
      getCaseList(data).then(res => {
        this.loading = false;
        if (res.data.list.length > 0) {
          this.caseList = this.caseList.concat(res.data.list);
          this.page++;
        } else {
          this.noMore = true;
        }
      }).catch(() => {
        this.loading = false;
      });
    },
    loadMore() {
      this.getCaseList();
    },
    goToDetail(id) {
      uni.navigateTo({
        url: '/pages/case_details/index?id=' + id
      });
    }
  }
}
</script>

<style lang="scss" scoped>
// ========== 主题色 ==========
$primary: #0061a5;
$primary-container: #0099ff;
$on-primary: #ffffff;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$surface: #f9f9fc;
$surface-container-lowest: #ffffff;
$outline-variant: #bfc7d5;
$tertiary: #006875;

.case-page {
  width: 100%;
  min-height: 100vh;
  background: $surface;
  padding: 0 40rpx 200rpx;
  box-sizing: border-box;
}

// ========== Page Header ==========
.page-header {
  padding: 40rpx 0 24rpx;
}

.header-title {
  font-size: 52rpx;
  font-weight: 700;
  color: $on-surface;
  margin-bottom: 8rpx;
}

.header-desc {
  font-size: 26rpx;
  color: $on-surface-variant;
}

// ========== Filter Chips ==========
.filter-nav {
  margin-bottom: 40rpx;
}

.filter-scroll {
  display: flex;
  gap: 16rpx;
}

.filter-chip {
  flex-shrink: 0;
  padding: 16rpx 36rpx;
  font-size: 26rpx;
  font-weight: 600;
  border-radius: 999rpx;
  background: rgba($tertiary, 0.1);
  color: $tertiary;
  transition: all 0.2s;

  &.active {
    background: $primary;
    color: $on-primary;
  }

  &:active {
    transform: scale(0.95);
  }
}

// ========== Bento Grid ==========
.bento-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24rpx;
}

.case-card {
  background: $surface-container-lowest;
  border: 1rpx solid rgba($outline-variant, 0.4);
  border-radius: 24rpx;
  overflow: hidden;
  transition: all 0.3s;

  &:active {
    border-color: $primary;
  }
}

// ========== Large card (first) ==========
.card-large {
  grid-column: span 2;
}

.card-img-large {
  height: 380rpx;
}

// ========== Horizontal card ==========
.card-horizontal {
  grid-column: span 2;
  display: flex;
  flex-direction: row;

  .card-h-left {
    width: 33%;
    overflow: hidden;

    image {
      width: 100%;
      height: 100%;
    }
  }

  .card-h-right {
    width: 67%;
    padding: 32rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 12rpx;
  }
}

// ========== Card Image ==========
.card-img {
  position: relative;
  height: 320rpx;
  overflow: hidden;

  image {
    width: 100%;
    height: 100%;
  }
}

.card-tags {
  position: absolute;
  top: 20rpx;
  left: 20rpx;
  display: flex;
  gap: 12rpx;
}

.card-tag-blue {
  background: rgba($primary, 0.85);
  color: $on-primary;
  font-size: 20rpx;
  font-weight: 500;
  padding: 6rpx 16rpx;
  border-radius: 999rpx;
}

.card-tag-accent {
  background: rgba($tertiary, 0.12);
  backdrop-filter: blur(10rpx);
  color: $tertiary;
  font-size: 20rpx;
  font-weight: 500;
  padding: 6rpx 16rpx;
  border-radius: 999rpx;
}

// ========== Card Body ==========
.card-body {
  padding: 24rpx 28rpx;
}

.card-body-large {
  padding: 32rpx;
}

.card-tag {
  display: inline-block;
  background: rgba($tertiary, 0.1);
  color: $tertiary;
  font-size: 20rpx;
  font-weight: 500;
  padding: 4rpx 12rpx;
  border-radius: 999rpx;
  margin-bottom: 8rpx;
}

.card-title {
  font-size: 30rpx;
  font-weight: 600;
  color: $on-surface;
  line-height: 1.4;
  margin-bottom: 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-desc {
  font-size: 24rpx;
  color: $on-surface-variant;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-link {
  display: flex;
  align-items: center;
  gap: 6rpx;
  margin-top: 16rpx;
  font-size: 26rpx;
  font-weight: 600;
  color: $primary;

  .iconfont {
    font-size: 24rpx;
  }
}

// ========== Load More ==========
.load-more {
  display: flex;
  justify-content: center;
  margin-top: 60rpx;
}

.load-btn {
  padding: 24rpx 64rpx;
  border: 4rpx solid $primary;
  border-radius: 999rpx;
  font-size: 28rpx;
  font-weight: 600;
  color: $primary;
  transition: all 0.2s;

  &:active {
    background: rgba($primary, 0.05);
    transform: scale(0.95);
  }
}

.loading-tip {
  text-align: center;
  padding: 32rpx;
  color: $on-surface-variant;
  font-size: 24rpx;
}
</style>
