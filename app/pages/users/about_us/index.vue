<template>
  <view class="about-page">
    <!-- 自定义导航栏 -->
    <view class="nav-bar" :style="{ paddingTop: statusBarHeight + 'px' }">
      <view class="nav-bar__content">
        <view class="nav-bar__back" @click="goBack">
          <text class="iconfont icon-xiangzuo"></text>
        </view>
        <text class="nav-bar__title">关于辽缆</text>
        <view class="nav-bar__right"></view>
      </view>
    </view>
    <view :style="{ height: statusBarHeight + 44 + 'px' }"></view>

    <!-- 加载中 -->
    <view v-if="loading" class="loading-wrap">
      <text class="loading-text">加载中...</text>
    </view>

    <!-- 页面内容 -->
    <view v-else-if="blockList.length > 0">

      <!-- ========== 1. 公司介绍 Hero区 ========== -->
      <view v-if="getBlock('company_intro')" class="hero-section">
        <view class="hero-bg">
          <image
            v-if="getBlockImages('company_intro').length > 0"
            :src="getBlockImages('company_intro')[0]"
            class="hero-bg__img"
            mode="aspectFill"
          />
          <view class="hero-bg__overlay"></view>
        </view>
        <view class="hero-content">
          <text class="hero-content__title">{{ getBlock('company_intro').title || '铸就工业脊梁' }}</text>
          <text class="hero-content__subtitle">{{ getBlock('company_intro').subTitle || '' }}</text>
          <view class="hero-content__desc" v-if="getBlock('company_intro').content">
            <rich-text :nodes="getBlock('company_intro').content"></rich-text>
          </view>
        </view>
      </view>

      <!-- ========== 2. 发展历程 时间线 ========== -->
      <view v-if="getBlock('development_history')" class="section section--gray">
        <view class="section-header">
          <view class="section-header__bar"></view>
          <text class="section-header__text">{{ getBlock('development_history').title || '发展历程' }}</text>
        </view>
        <view class="timeline">
          <view class="timeline__line"></view>
          <view
            v-for="(item, index) in timelineData"
            :key="index"
            class="timeline-item"
          >
            <view class="timeline-item__dot" :class="{ 'timeline-item__dot--active': index === 0 }"></view>
            <view class="timeline-item__content">
              <text class="timeline-item__year" :class="{ 'timeline-item__year--active': index === 0 }">{{ item.year }}</text>
              <text class="timeline-item__event">{{ item.event }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- ========== 3. 资质荣誉 图片网格 ========== -->
      <view v-if="getBlock('qualification_honor')" class="section">
        <view class="section-header">
          <view class="section-header__bar"></view>
          <text class="section-header__text">{{ getBlock('qualification_honor').title || '资质荣誉' }}</text>
        </view>
        <view v-if="getBlock('qualification_honor').content" class="honor-intro">
          <text class="honor-intro__text">{{ getBlock('qualification_honor').content }}</text>
        </view>
        <view class="honor-grid">
          <view
            v-for="(url, index) in getBlockImages('qualification_honor')"
            :key="index"
            class="honor-grid__item"
            @click="previewImage(getBlockImages('qualification_honor'), index)"
          >
            <image :src="url" class="honor-grid__img" mode="aspectFill" />
          </view>
        </view>
      </view>

      <!-- ========== 4. 生产实力 图文混排 ========== -->
      <view v-if="getBlock('production_strength')" class="section section--gray">
        <view class="section-header">
          <view class="section-header__bar"></view>
          <text class="section-header__text">{{ getBlock('production_strength').title || '生产实力' }}</text>
        </view>
        <view class="strength-content">
          <view v-if="getBlockImages('production_strength').length > 0" class="strength-content__images">
            <image
              v-for="(url, index) in getBlockImages('production_strength')"
              :key="index"
              :src="url"
              class="strength-content__img"
              mode="aspectFill"
              @click="previewImage(getBlockImages('production_strength'), index)"
            />
          </view>
          <view v-if="getBlock('production_strength').content" class="strength-content__text">
            <rich-text :nodes="getBlock('production_strength').content"></rich-text>
          </view>
        </view>
      </view>

      <!-- ========== 5. 企业文化 ========== -->
      <view v-if="getBlock('company_culture')" class="section">
        <view class="section-header">
          <view class="section-header__bar"></view>
          <text class="section-header__text">{{ getBlock('company_culture').title || '企业文化' }}</text>
        </view>
        <view v-if="getBlock('company_culture').content" class="culture-content">
          <rich-text :nodes="getBlock('company_culture').content"></rich-text>
        </view>
        <view v-if="getBlockImages('company_culture').length > 0" class="culture-images">
          <image
            v-for="(url, index) in getBlockImages('company_culture')"
            :key="index"
            :src="url"
            class="culture-images__img"
            mode="aspectFill"
            @click="previewImage(getBlockImages('company_culture'), index)"
          />
        </view>
      </view>

      <!-- ========== 6. 联系方式 ========== -->
      <view v-if="getBlock('contact_info')" class="section section--gray">
        <view class="section-header">
          <view class="section-header__bar"></view>
          <text class="section-header__text">{{ getBlock('contact_info').title || '联系我们' }}</text>
        </view>
        <view class="contact-list">
          <view class="contact-card" @click="callPhone" v-if="contactData.phone">
            <view class="contact-card__icon contact-card__icon--blue">
              <text class="iconfont icon-dianhua"></text>
            </view>
            <view class="contact-card__info">
              <text class="contact-card__label">联系电话</text>
              <text class="contact-card__value">{{ contactData.phone }}</text>
            </view>
            <text class="contact-card__arrow iconfont icon-xiangyou"></text>
          </view>
          <view class="contact-card" @click="openMap" v-if="contactData.address">
            <view class="contact-card__icon contact-card__icon--orange">
              <text class="iconfont icon-dizhi"></text>
            </view>
            <view class="contact-card__info">
              <text class="contact-card__label">公司地址</text>
              <text class="contact-card__value">{{ contactData.address }}</text>
            </view>
            <text class="contact-card__arrow iconfont icon-xiangyou"></text>
          </view>
          <view class="contact-card" v-if="contactData.email">
            <view class="contact-card__icon contact-card__icon--green">
              <text class="iconfont icon-youxiang"></text>
            </view>
            <view class="contact-card__info">
              <text class="contact-card__label">电子邮箱</text>
              <text class="contact-card__value">{{ contactData.email }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 底部间距 -->
      <view style="height: 40rpx;"></view>
    </view>

    <!-- 空状态 -->
    <view v-else class="empty-wrap">
      <text class="empty-text">暂无内容</text>
    </view>
  </view>
</template>

<script>
import { getAboutInfo } from "@/api/about.js";

export default {
  data() {
    return {
      statusBarHeight: 0,
      loading: true,
      blockList: [],
    };
  },
  computed: {
    /** 发展历程时间线数据 */
    timelineData() {
      const block = this.getBlock("development_history");
      if (!block || !block.content) return [];
      try {
        return JSON.parse(block.content);
      } catch (e) {
        return [];
      }
    },
    /** 联系方式结构化数据 */
    contactData() {
      const block = this.getBlock("contact_info");
      if (!block || !block.content) return {};
      try {
        return JSON.parse(block.content);
      } catch (e) {
        return {};
      }
    },
  },
  onLoad() {
    const sysInfo = uni.getSystemInfoSync();
    this.statusBarHeight = sysInfo.statusBarHeight || 0;
    this.getData();
  },
  methods: {
    /** 获取页面数据 */
    getData() {
      this.loading = true;
      getAboutInfo()
        .then((res) => {
          this.blockList = res || [];
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },

    /** 根据 blockKey 获取板块 */
    getBlock(key) {
      return this.blockList.find((item) => item.blockKey === key);
    },

    /** 获取板块的图片数组 */
    getBlockImages(key) {
      const block = this.getBlock(key);
      if (!block || !block.imageUrls) return [];
      try {
        return JSON.parse(block.imageUrls);
      } catch (e) {
        return [];
      }
    },

    /** 返回上一页 */
    goBack() {
      uni.navigateBack({ delta: 1 });
    },

    /** 预览图片 */
    previewImage(urls, index) {
      uni.previewImage({
        urls: urls,
        current: index,
      });
    },

    /** 拨打电话 */
    callPhone() {
      if (!this.contactData.phone) return;
      // #ifdef H5
      window.location.href = `tel:${this.contactData.phone}`;
      // #endif
      // #ifdef MP || APP-PLUS
      uni.makePhoneCall({ phoneNumber: this.contactData.phone });
      // #endif
    },

    /** 打开地图导航 */
    openMap() {
      const data = this.contactData;
      // #ifdef H5
      if (data.mapUrl) {
        window.open(data.mapUrl, "_blank");
      }
      // #endif
      // #ifdef MP || APP-PLUS
      if (data.latitude && data.longitude) {
        uni.openLocation({
          latitude: parseFloat(data.latitude),
          longitude: parseFloat(data.longitude),
          name: data.address || "辽缆工业",
          address: data.address || "",
        });
      }
      // #endif
    },
  },
};
</script>

<style scoped lang="scss">
/* ===== 颜色 Token ===== */
$color-primary: #003da6;
$color-primary-soft: #e8edfa;
$color-primary-container: #0052d9;
$text-primary: #333333;
$text-secondary: #666666;
$text-tertiary: #999999;
$bg-page: #f7f8fa;
$bg-surface: #ffffff;
$bg-section-gray: #f1f3ff;
$border-default: #eaecef;
$shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.04);
$radius-xs: 4px;
$radius-sm: 6px;
$radius-md: 8px;

/* ===== 导航栏 ===== */
.nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
  background-color: $bg-surface;
  border-bottom: 1px solid $border-default;

  &__content {
    display: flex;
    align-items: center;
    height: 44px;
    padding: 0 16px;
  }

  &__back {
    width: 44px;
    height: 44px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: $color-primary;
    font-size: 18px;
  }

  &__title {
    flex: 1;
    text-align: center;
    font-size: 17px;
    font-weight: 600;
    color: $text-primary;
  }

  &__right {
    width: 44px;
  }
}

/* ===== 加载状态 ===== */
.loading-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300rpx;
}

.loading-text {
  font-size: 28rpx;
  color: $text-tertiary;
}

/* ===== 板块标题 ===== */
.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24rpx;

  &__bar {
    width: 4px;
    height: 20px;
    background-color: $color-primary;
    border-radius: 2px;
  }

  &__text {
    font-size: 32rpx;
    font-weight: 600;
    color: $text-primary;
    line-height: 44rpx;
  }
}

/* ===== 板块容器 ===== */
.section {
  padding: 48rpx 32rpx;
  background-color: $bg-surface;

  &--gray {
    background-color: $bg-section-gray;
  }
}

/* ===== Hero 区 ===== */
.hero-section {
  position: relative;
  width: 100%;
  height: 640rpx;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;

  &__img {
    width: 100%;
    height: 100%;
  }

  &__overlay {
    position: absolute;
    inset: 0;
    background: linear-gradient(to top, rgba(24, 28, 35, 0.9), rgba(24, 28, 35, 0.4), transparent);
  }
}

.hero-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 48rpx 32rpx;
  color: #ffffff;

  &__title {
    display: block;
    font-size: 48rpx;
    font-weight: 600;
    line-height: 64rpx;
    margin-bottom: 16rpx;
  }

  &__subtitle {
    display: block;
    font-size: 28rpx;
    line-height: 40rpx;
    margin-bottom: 12rpx;
    opacity: 0.9;
  }

  &__desc {
    font-size: 26rpx;
    line-height: 40rpx;
    opacity: 0.85;
  }
}

/* ===== 发展历程 时间线 ===== */
.timeline {
  position: relative;
  padding-left: 24rpx;
  margin-top: 24rpx;

  &__line {
    position: absolute;
    left: 12rpx;
    top: 16rpx;
    bottom: 16rpx;
    width: 2px;
    background-color: $border-default;
  }
}

.timeline-item {
  position: relative;
  padding-left: 40rpx;
  margin-bottom: 40rpx;

  &:last-child {
    margin-bottom: 0;
  }

  &__dot {
    position: absolute;
    left: 4rpx;
    top: 8rpx;
    width: 20rpx;
    height: 20rpx;
    border-radius: 50%;
    background-color: $border-default;
    border: 4px solid $bg-section-gray;

    &--active {
      background-color: $color-primary;
      border-color: $bg-section-gray;
    }
  }

  &__year {
    display: block;
    font-size: 32rpx;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: 8rpx;

    &--active {
      color: $color-primary;
    }
  }

  &__event {
    font-size: 28rpx;
    color: $text-secondary;
    line-height: 44rpx;
  }
}

/* ===== 资质荣誉 ===== */
.honor-intro {
  margin-bottom: 24rpx;

  &__text {
    font-size: 28rpx;
    color: $text-secondary;
    line-height: 44rpx;
  }
}

.honor-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;

  &__item {
    width: calc((100% - 32rpx) / 3);
    aspect-ratio: 3 / 4;
    border-radius: $radius-sm;
    overflow: hidden;
    box-shadow: $shadow-sm;
  }

  &__img {
    width: 100%;
    height: 100%;
  }
}

/* ===== 生产实力 ===== */
.strength-content {
  &__images {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
    margin-bottom: 24rpx;
  }

  &__img {
    width: 100%;
    height: 360rpx;
    border-radius: $radius-sm;
  }

  &__text {
    font-size: 28rpx;
    color: $text-secondary;
    line-height: 44rpx;
  }
}

/* ===== 企业文化 ===== */
.culture-content {
  font-size: 28rpx;
  color: $text-secondary;
  line-height: 44rpx;
  margin-bottom: 24rpx;
}

.culture-images {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;

  &__img {
    width: 100%;
    height: 360rpx;
    border-radius: $radius-sm;
  }
}

/* ===== 联系方式 ===== */
.contact-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.contact-card {
  display: flex;
  align-items: center;
  padding: 28rpx 24rpx;
  background-color: $bg-surface;
  border-radius: $radius-md;
  border: 1px solid $border-default;
  box-shadow: $shadow-sm;

  &__icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 24rpx;
    flex-shrink: 0;

    .iconfont {
      font-size: 36rpx;
      color: #ffffff;
    }

    &--blue {
      background-color: $color-primary;
    }

    &--orange {
      background-color: #ff7d00;
    }

    &--green {
      background-color: #19be6b;
    }
  }

  &__info {
    flex: 1;
    min-width: 0;
  }

  &__label {
    display: block;
    font-size: 24rpx;
    color: $text-tertiary;
    margin-bottom: 4rpx;
  }

  &__value {
    display: block;
    font-size: 28rpx;
    color: $text-primary;
    font-weight: 500;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__arrow {
    color: $text-tertiary;
    font-size: 24rpx;
    margin-left: 12rpx;
  }
}

/* ===== 空状态 ===== */
.empty-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 600rpx;
}

.empty-text {
  font-size: 28rpx;
  color: $text-tertiary;
}
</style>
