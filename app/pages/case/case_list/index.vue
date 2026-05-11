<template>
  <view class="case-list-page">
    <view class="nav-bar">
      <view class="nav-left" @click="goBack">
        <text class="iconfont icon-xiangzuo"></text>
      </view>
      <view class="nav-title">客户案例</view>
      <view class="nav-right"></view>
    </view>

    <view class="category-tabs">
      <view class="filter-label">行业分类</view>
      <scroll-view class="tabs-scroll" scroll-x>
        <view class="tabs-container">
          <view 
            v-for="item in industryList" 
            :key="item.value"
            :class="['tab-item', { active: selectedIndustry === item.value }]"
            @click="selectIndustry(item.value)"
          >
            {{ item.label }}
          </view>
        </view>
      </scroll-view>
    </view>
    
    <scroll-view class="content-scroll" scroll-y @scrolltolower="loadMore">
      <view class="section-header">
        <view class="header-bar"></view>
        <view class="header-title">工业应用案例</view>
      </view>
      <view class="header-desc">
        深入了解辽缆电缆如何以高可靠性的工业电缆，为各行业的关键基础设施提供动力。
      </view>
      
      <view class="case-list">
        <view 
          v-for="item in caseList" 
          :key="item.id"
          class="case-card"
          @click="goToDetail(item.id)"
        >
          <view class="case-image">
            <image :src="item.imageInput" mode="aspectFill" />
            <view class="case-tags" v-if="item.industryCategory">
              <text class="tag-primary">{{ item.industryCategory }}</text>
            </view>
          </view>
          
          <view class="case-content">
            <view class="case-title">{{ item.projectName || item.title }}</view>
            
            <view class="case-tags-row" v-if="item.industryCategory || item.regionCategory || item.projectType">
              <text class="tag-item" v-if="item.industryCategory">
                {{ item.industryCategory }}
              </text>
              <text class="tag-item" v-if="item.regionCategory">
                {{ item.regionCategory }}
              </text>
              <text class="tag-item" v-if="item.projectType">
                {{ item.projectType }}
              </text>
            </view>
            
            <view class="case-desc" v-if="item.supplyProducts">
              {{ item.supplyProducts }}
            </view>
            
            <view class="case-action">
              <view class="btn-detail">查看项目详情</view>
            </view>
          </view>
        </view>
      </view>
      
      <view v-if="loading" class="loading-tip">加载中...</view>
      <view v-if="noMore" class="loading-tip">没有更多了</view>
    </scroll-view>
  </view>
</template>

<script>
import { getCaseList, getCaseIndustryCategories } from '@/api/api.js';

export default {
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
        const list = [{ value: '', label: '全部分类' }];
        (res.data || []).forEach(item => {
          list.push({ value: item, label: item });
        });
        this.industryList = list;
      });
    },
    goBack() {
      uni.navigateBack();
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
        url: '/pages/case/case_details/index?id=' + id
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.case-list-page {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  background-color: #f9f9ff;
}

.nav-bar {
  width: 100%;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 44px;
  margin-top: 50rpx;
  padding: 0 16px;
  background-color: #ffffff;
  border-bottom: 1px solid #c3c6d7;
  position: sticky;
  top: 0;
  z-index: 99;
}

.nav-title {
  font-size: 16px;
  font-weight: 600;
  color: #181c23;
}

.nav-left, .nav-right {
  width: 60px;
}

.category-tabs {
  width: 100%;
  box-sizing: border-box;
  background-color: #ffffff;
  padding: 12px 16px;
  position: sticky;
  top: calc(45px + 50rpx);
  z-index: 98;
}
.filter-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
  margin-top: 8px;
}

.filter-label:first-child {
  margin-top: 0;
}

.tabs-scroll {
  width: 100%;
  white-space: nowrap;
}

.tabs-container {
  display: inline-flex;
  gap: 12px;
}

.tab-item {
  display: inline-block;
  padding: 8px 16px;
  font-size: 12px;
  border-radius: 4px;
  background-color: #f9f9ff;
  border: 1px solid #c3c6d7;
  color: #434654;
  transition: all 0.2s;
  
  &.active {
    background-color: #dbe1ff;
    border-color: #dbe1ff;
    color: #00174b;
  }
}

.content-scroll {
  width: 100%;
  flex: 1;
  box-sizing: border-box;
  padding: 16px;
  overflow-y: auto;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.header-bar {
  width: 4px;
  height: 24px;
  background-color: #003da6;
  border-radius: 2px;
  margin-right: 8px;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #181c23;
  line-height: 28px;
}

.header-desc {
  font-size: 14px;
  color: #434654;
  line-height: 22px;
  margin-bottom: 24px;
}

.case-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.case-card {
  background-color: #ffffff;
  border: 1px solid #c3c6d7;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(0,0,0,0.04);
}

.case-image {
  position: relative;
  height: 192px;
  
  image {
    width: 100%;
    height: 100%;
  }
}

.case-tags {
  position: absolute;
  top: 12px;
  left: 12px;
  display: flex;
  gap: 8px;
}

.tag-primary {
  background-color: #dbe1ff;
  color: #00174b;
  font-size: 10px;
  padding: 4px 8px;
  border-radius: 4px;
}

.case-content {
  padding: 16px;
}

.case-title {
  font-size: 16px;
  font-weight: 600;
  color: #181c23;
  line-height: 24px;
  margin-bottom: 12px;
}

.case-tags-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.tag-item {
  background-color: #f9f9ff;
  color: #434654;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
}

.case-desc {
  font-size: 14px;
  color: #434654;
  line-height: 22px;
  margin-bottom: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.case-action {
  margin-top: 12px;
}

.btn-detail {
  width: 100%;
  height: 44px;
  background-color: #003da6;
  color: #ffffff;
  font-size: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  
  &:active {
    transform: scale(0.98);
    background-color: #002d7a;
  }
}

.loading-tip {
  text-align: center;
  padding: 16px;
  color: #909399;
  font-size: 14px;
}
</style>
