<template>
  <view class="case-detail-page">
    <view class="nav-bar">
      <view class="nav-left" @click="goBack">
        <text class="iconfont icon-xiangzuo"></text>
      </view>
      <view class="nav-title">案例详情</view>
      <view class="nav-right"></view>
    </view>
    
    <scroll-view class="content-scroll" scroll-y>
      <view class="cover-section">
        <swiper
          v-if="caseData.projectImages && caseData.projectImages.length > 0"
          class="cover-swiper"
          :indicator-dots="true" 
          :autoplay="true" 
          :interval="3000"
        >
          <swiper-item v-for="(img, index) in caseData.projectImages" :key="index">
            <image :src="img" mode="aspectFill" />
          </swiper-item>
        </swiper>
      </view>
      
      <view class="project-header">
        <view class="project-name">
          {{ caseData.projectName || caseData.title }}
        </view>
        <view class="project-tags">
          <text class="tag-item" v-if="caseData.industryCategory">
            {{ caseData.industryCategory }}
          </text>
          <text class="tag-item" v-if="caseData.regionCategory">
            {{ caseData.regionCategory }}
          </text>
          <text class="tag-item" v-if="caseData.projectType">
            {{ caseData.projectType }}
          </text>
        </view>
      </view>
      
      <view class="info-card" v-if="caseData.projectAddress || caseData.projectPeriod || caseData.supplyProducts || caseData.implementationResult">
        <view class="info-item" v-if="caseData.projectAddress">
          <view class="info-label">项目地点</view>
          <view class="info-value">{{ caseData.projectAddress }}</view>
        </view>
        <view class="info-item" v-if="caseData.projectPeriod">
          <view class="info-label">项目周期</view>
          <view class="info-value">{{ caseData.projectPeriod }}</view>
        </view>
        <view class="info-item" v-if="caseData.supplyProducts">
          <view class="info-label">供货产品</view>
          <view class="info-value">{{ caseData.supplyProducts }}</view>
        </view>
        <view class="info-item" v-if="caseData.implementationResult">
          <view class="info-label">实施效果</view>
          <view class="info-value">{{ caseData.implementationResult }}</view>
        </view>
      </view>
      
      <view class="content-section" v-if="caseData.projectBackground">
        <view class="section-title">
          <view class="title-bar"></view>
          <text>项目背景</text>
        </view>
        <view class="section-content">
          <jyf-parser :html="caseData.projectBackground" />
        </view>
      </view>
      
      <view class="content-section">
        <view class="section-title">
          <view class="title-bar"></view>
          <text>案例详情</text>
        </view>
        <view class="section-content">
          <jyf-parser :html="caseData.content" />
        </view>
      </view>
      
      <view class="product-section" v-if="caseData.productList.length > 0">
        <view class="section-title">
          <view class="title-bar"></view>
          <text>关联产品</text>
        </view>
        <scroll-view class="product-scroll" scroll-x>
          <view class="product-list">
            <view 
              v-for="product in caseData.productList" 
              :key="product.id"
              class="product-card"
              @click="goToProduct(product.id)"
            >
              <image :src="product.image" mode="aspectFill" class="product-image" />
              <view class="product-name">{{ product.storeName }}</view>
              <view class="product-price">¥{{ product.price }}</view>
            </view>
          </view>
        </scroll-view>
      </view>
      
      <view class="contact-section">
        <view class="contact-btn" @click="goToContact">
          <text class="iconfont icon-dianhua"></text>
          <text>联系我们</text>
        </view>
      </view>
      
      <view class="bottom-gap"></view>
    </scroll-view>
  </view>
</template>

<script>
import { getCaseDetails } from '@/api/api.js';
import parser from "@/components/jyf-parser/jyf-parser";

export default {
  components: {
    "jyf-parser": parser
  },
  data() {
    return {
      id: 0,
      caseData: {
        projectImages: [],
        imageInput: '',
        title: '',
        content: '',
        projectName: '',
        industryCategory: '',
        regionCategory: '',
        projectType: '',
        projectAddress: '',
        projectPeriod: '',
        supplyProducts: '',
        implementationResult: '',
        projectBackground: '',
        productList: []
      },
      tagStyle: {
        img: 'width:100%;display:block;',
        table: 'width:100%',
        video: 'width:100%'
      }
    };
  },
  onLoad(options) {
    if (options.id) {
      this.id = options.id;
      this.getCaseDetail();
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },
    getCaseDetail() {
      uni.showLoading({ title: '加载中...' });
      getCaseDetails(this.id).then(res => {
        uni.hideLoading();
        const data = res.data;
        let projectImages = [];
        try {
          projectImages = data.coverImages ? JSON.parse(data.coverImages) : [];
        } catch (e) {
          projectImages = [];
        }
        this.caseData = {
          ...data,
          projectImages: projectImages,
          productList: data.productList || []
        };
      }).catch(() => {
        uni.hideLoading();
      });
    },
    goToProduct(id) {
      uni.navigateTo({
        url: '/pages/goods/goods_details/index?id=' + id
      });
    },
    goToContact() {
      uni.navigateTo({
        url: '/pages/contact/index'
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.case-detail-page {
  min-height: 100vh;
  background-color: #f9f9ff;
}

.nav-bar {
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

.content-scroll {
  padding-bottom: 20px;
}

.cover-section {
  width: 100%;
}

.cover-swiper {
  height: 192px;
  
  swiper-item {
    image {
      width: 100%;
      height: 100%;
    }
  }
}

.cover-single {
  position: relative;
  height: 192px;
  
  image {
    width: 100%;
    height: 100%;
  }
}

.cover-tags {
  position: absolute;
  bottom: 12px;
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

.project-header {
  padding: 16px;
  background-color: #ffffff;
}

.project-name {
  font-size: 20px;
  font-weight: 600;
  color: #181c23;
  line-height: 28px;
  margin-bottom: 12px;
}

.project-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  background-color: #f9f9ff;
  color: #434654;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
}

.info-card {
  margin: 16px;
  padding: 16px;
  background-color: #ffffff;
  border: 1px solid #c3c6d7;
  border-radius: 8px;
}

.info-item {
  margin-bottom: 12px;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.info-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.info-value {
  font-size: 14px;
  color: #181c23;
  line-height: 22px;
}

.content-section {
  margin: 16px;
  padding: 16px;
  background-color: #ffffff;
  border-radius: 8px;
}

.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.title-bar {
  width: 4px;
  height: 20px;
  background-color: #0f5de3;
  border-radius: 2px;
  margin-right: 8px;
}

.section-title text {
  font-size: 16px;
  font-weight: 600;
  color: #181c23;
}

.section-content {
  font-size: 14px;
  color: #434654;
  line-height: 22px;
}

.product-section {
  margin: 16px;
}

.product-scroll {
  white-space: nowrap;
  margin-top: 12px;
}

.product-list {
  display: flex;
  gap: 12px;
}

.product-card {
  width: 140px;
  flex-shrink: 0;
  background-color: #ffffff;
  border: 1px solid #c3c6d7;
  border-radius: 8px;
  overflow: hidden;
}

.product-image {
  width: 140px;
  height: 140px;
}

.product-name {
  padding: 8px;
  font-size: 12px;
  color: #181c23;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  padding: 0 8px 8px;
  font-size: 14px;
  font-weight: 600;
  color: #003da6;
}

.contact-section {
  margin: 16px;
  padding: 16px;
}

.contact-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  height: 44px;
  background-color: #0f5de3;
  color: #ffffff;
  font-size: 16px;
  border-radius: 8px;
  transition: all 0.2s;
  
  &:active {
    background-color: #0c4db8;
  }
}

.bottom-gap {
  height: 40px;
}
</style>
