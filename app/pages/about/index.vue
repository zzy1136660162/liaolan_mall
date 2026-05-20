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
      <view class="loading-spinner"></view>
      <text class="loading-text">加载中...</text>
    </view>

    <!-- 页面内容 -->
    <view v-else-if="blockList.length > 0" class="page-body">

      <!-- ========== Hero 区 ========== -->
      <view class="hero-section">
        <image
            v-if="heroBanner.bgImage"
            :src="heroBanner.bgImage"
            class="hero-bg__img"
            mode="aspectFill"
        />
        <view v-else class="hero-bg__placeholder"></view>
        <view class="hero-overlay"></view>
        <view class="hero-content">
          <text class="hero-title">{{ heroBanner.title }}</text>
          <text class="hero-subtitle">{{ heroBanner.subtitle }}</text>
        </view>
      </view>

      <!-- ========== 企业简介 + 核心数据 ========== -->
      <view v-if="getBlock('company_intro')" class="section">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">企业简介</text>
        </view>
        <view class="intro-card">
          <rich-text :nodes="getBlock('company_intro').content || ''"></rich-text>
        </view>
        <!-- 核心数据展示 - 2x2网格 -->
        <view v-if="statsData.length > 0" class="bento-grid">
          <view
              v-for="(item, index) in statsData"
              :key="index"
              class="bento-card"
          >
            <view class="bento-icon-wrap">
              <image src="/static/icons/stitch/material-symbols/outline/factory.svg" class="icon-svg-small" mode="aspectFit" />
            </view>
            <text class="bento-value">{{ item.value }}</text>
            <text class="bento-label">{{ item.label }}</text>
          </view>
        </view>
      </view>

      <!-- ========== 核心优势 ========== -->
      <view v-if="advantagesData.length > 0" class="section section--alt">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ (getBlock('core_advantages') || {}).title || '核心优势' }}</text>
        </view>
        <view class="advantage-list">
          <view class="advantage-card" v-for="(item, index) in advantagesData" :key="index">
            <view class="advantage-icon-wrap">
              <image src="/static/icons/stitch/material-symbols/outline/verified.svg" class="icon-svg-small" mode="aspectFit" />
            </view>
            <view class="advantage-body">
              <text class="advantage-title">{{ item.title }}</text>
              <text class="advantage-desc">{{ item.desc }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- ========== 领导致辞 ========== -->
      <view v-if="leaderData" class="section">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ (getBlock('leader_speech') || {}).title || '领导致辞' }}</text>
        </view>
        <view class="leader-card">
          <view class="leader-img-wrap">
            <image
                v-if="leaderData.avatar"
                :src="leaderData.avatar"
                class="leader-img"
                mode="widthFix"
            />
            <view v-else class="leader-img-placeholder">
              <text class="leader-placeholder-text">"</text>
            </view>
          </view>
          <view class="leader-body">
            <text class="leader-quote-icon">"</text>
            <text class="leader-quote">{{ leaderData.quote }}</text>
            <view class="leader-divider"></view>
            <view class="leader-meta">
              <text class="leader-name">{{ leaderData.name }}</text>
              <text class="leader-role">{{ leaderData.title }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- ========== 发展历程 ========== -->
      <view v-if="timelineData.length > 0" class="section section--alt">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ (getBlock('development_history') || {}).title || '发展历程' }}</text>
        </view>
        <view class="timeline">
          <view
              v-for="(item, index) in timelineData"
              :key="index"
              class="timeline-item"
          >
            <view class="timeline-dot" :class="{ active: index === 0 }"></view>
            <view class="timeline-line" v-if="index < timelineData.length - 1"></view>
            <view class="timeline-content">
              <text class="timeline-year" :class="{ active: index === 0 }">{{ item.year }}</text>
              <text class="timeline-event">{{ item.event }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- ========== 核心团队 ========== -->
      <view v-if="teamData.length > 0" class="section">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ (getBlock('core_team') || {}).title || '核心团队' }}</text>
        </view>
        <view class="team-grid">
          <view class="team-card" v-for="(item, index) in teamData" :key="index">
            <view class="team-avatar-wrap">
              <image v-if="item.avatar" :src="item.avatar" class="team-avatar" mode="widthFix" />
              <view v-else class="team-avatar-placeholder">
                <text class="avatar-text">{{ item.name ? item.name.charAt(0) : '' }}</text>
              </view>
            </view>
            <text class="team-name">{{ item.name }}</text>
            <text class="team-role">{{ item.title }}</text>
          </view>
        </view>
      </view>

      <!-- ========== 资质荣誉 ========== -->
      <view v-if="honorData" class="section section--alt">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ (getBlock('qualification_honor') || {}).title || '资质荣誉' }}</text>
        </view>
        <view v-if="honorData.text" class="honor-intro">
          <text class="honor-text">{{ honorData.text }}</text>
        </view>
        <view v-if="honorData.tags && honorData.tags.length > 0" class="honor-tags">
          <view class="honor-tag" v-for="(tag, index) in honorData.tags" :key="index">
            <text class="honor-tag__text">{{ tag }}</text>
          </view>
        </view>
        <view v-if="getBlockImages('qualification_honor').length > 0" class="honor-images">
          <image
              v-for="(url, index) in getBlockImages('qualification_honor')"
              :key="index"
              :src="url"
              class="honor-img"
              mode="aspectFill"
              @click="previewImage(getBlockImages('qualification_honor'), index)"
          />
        </view>
      </view>

      <!-- ========== 生产实力 ========== -->
      <view v-if="getBlock('production_strength') && getBlock('production_strength').content" class="section">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ getBlock('production_strength').title || '生产实力' }}</text>
        </view>
        <view class="intro-card">
          <rich-text :nodes="getBlock('production_strength').content"></rich-text>
        </view>
      </view>

      <!-- ========== 企业文化 ========== -->
      <view v-if="getBlock('company_culture') && getBlock('company_culture').content" class="section section--alt">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ getBlock('company_culture').title || '企业文化' }}</text>
        </view>
        <view class="intro-card">
          <rich-text :nodes="getBlock('company_culture').content"></rich-text>
        </view>
      </view>

      <!-- ========== 联系方式 ========== -->
      <view v-if="contactData && (contactData.phone || contactData.address)" class="section">
        <view class="section-header">
          <view class="section-accent"></view>
          <text class="section-title">{{ (getBlock('contact_info') || {}).title || '联系我们' }}</text>
        </view>
        <view class="contact-list">
          <view class="contact-card" @click="callPhone" v-if="contactData.phone">
            <view class="contact-icon-wrap contact-icon-wrap--blue">
              <image src="/static/icons/stitch/material-symbols/outline/call.svg" class="icon-svg" mode="aspectFit" />
            </view>
            <view class="contact-info">
              <text class="contact-label">联系电话</text>
              <text class="contact-value">{{ contactData.phone }}</text>
            </view>
            <view class="contact-btn">拨打</view>
          </view>
          <view class="contact-card" @click="openMap" v-if="contactData.address">
            <view class="contact-icon-wrap contact-icon-wrap--blue">
              <image src="/static/icons/stitch/material-symbols/outline/location_on.svg" class="icon-svg" mode="aspectFit" />
            </view>
            <view class="contact-info">
              <text class="contact-label">公司地址</text>
              <text class="contact-value">{{ contactData.address }}</text>
            </view>
            <view class="contact-btn">导航</view>
          </view>
          <view class="contact-card" v-if="contactData.email">
            <view class="contact-icon-wrap contact-icon-wrap--blue">
              <image src="/static/icons/stitch/material-symbols/filled/mail.svg" class="icon-svg" mode="aspectFit" />
            </view>
            <view class="contact-info">
              <text class="contact-label">电子邮箱</text>
              <text class="contact-value">{{ contactData.email }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 底部 -->
      <view class="page-footer">
        <text class="page-footer__text">© 2024 辽宁辽缆集团有限公司</text>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-else class="empty-wrap">
      <text class="empty-text">暂无内容</text>
    </view>
  </view>
</template>

<script>
import { getAboutFullInfo } from "@/api/about.js";

export default {
  data() {
    return {
      statusBarHeight: 0,
      loading: true,
      blockList: [],
      navList: [],
      blockMap: {},     // 缓存：blockKey → block对象
    };
  },
  computed: {
    /** Hero横幅数据 */
    heroBanner() {
      const block = this.blockMap["hero_banner"];
      if (block) {
        let extra = {};
        try { extra = JSON.parse(block.content || "{}"); } catch (e) {}
        const images = this._parseImageUrls(block.imageUrls);
        return {
          title: block.title || "铸就工业脊梁",
          subtitle: block.subTitle || "",
          bgImage: extra.bgImage || (images.length > 0 ? images[0] : ""),
        };
      }
      return { title: "铸就工业脊梁", subtitle: "", bgImage: "" };
    },
    /** 企业统计数据（JSON数组） */
    statsData() {
      return this._parseBlockContent("company_stats", []);
    },
    /** 核心优势数据（JSON数组） */
    advantagesData() {
      return this._parseBlockContent("core_advantages", []);
    },
    /** 领导致辞数据（JSON对象） */
    leaderData() {
      return this._parseBlockContent("leader_speech", null);
    },
    /** 发展历程数据（JSON数组） */
    timelineData() {
      return this._parseBlockContent("development_history", []);
    },
    /** 核心团队数据（JSON数组） */
    teamData() {
      return this._parseBlockContent("core_team", []);
    },
    /** 资质荣誉数据（JSON对象，含text和tags） */
    honorData() {
      const block = this.blockMap["qualification_honor"];
      if (!block || !block.content) return null;
      try {
        const parsed = JSON.parse(block.content);
        if (typeof parsed === "string") {
          return { text: parsed, tags: [] };
        }
        return parsed;
      } catch (e) {
        return { text: block.content, tags: [] };
      }
    },
    /** 联系方式数据（JSON对象） */
    contactData() {
      return this._parseBlockContent("contact_info", {});
    },
  },
  onLoad() {
    const sysInfo = uni.getSystemInfoSync();
    this.statusBarHeight = sysInfo.statusBarHeight || 0;
    this.getData();
  },
  methods: {
    /**
     * 修复后端返回的双重/错误前缀图片 URL
     * 后端 ResponseRouter.prefixImage 会自动给 crmebimage/ 加 CDN 前缀，
     * 如果数据库存了完整 URL 就会出现双重前缀，如：
     *   http://127.0.0.1:20500/http://127.0.0.1:20500/crmebimage/...
     *   /http://127.0.0.1:20500/crmebimage/...
     * 修复后统一为: http://127.0.0.1:20500/crmebimage/...
     */
    fixImageUrl(url) {
      if (!url) return '';
      let fixed = url;
      // 循环剥离多余的 http://127.0.0.1:20500/ 前缀（含前面可能带的 /）
      const prefix = 'http://127.0.0.1:20500/';
      while (fixed.includes('/' + prefix) || fixed.includes(prefix + prefix)) {
        fixed = fixed.replace('/' + prefix, prefix);
        fixed = fixed.replace(prefix + prefix, prefix);
      }
      // 如果是相对路径 crmebimage/...，加上完整前缀
      if (fixed.startsWith('crmebimage/') || fixed.startsWith('uploadf/')) {
        fixed = prefix + fixed;
      }
      return fixed;
    },
    getData() {
      this.loading = true;
      getAboutFullInfo()
          .then((res) => {
            // 后端返回的是数组，需要适配
            const data = res.data || [];
            // 如果返回的是数组，直接使用；如果是对象，取 blockList
            this.blockList = Array.isArray(data) ? data : (data.blockList || []);
            this.navList = Array.isArray(data) ? [] : (data.navList || []);
            // 修复所有板块中的图片 URL
            this.blockList.forEach((item) => {
              if (item.imageUrls) {
                try {
                  const urls = JSON.parse(item.imageUrls);
                  item.imageUrls = JSON.stringify(urls.map((u) => this.fixImageUrl(u)));
                } catch (e) {}
              }
              // 修复 content 中可能含有的 avatar URL
              if (item.content) {
                try {
                  const parsed = JSON.parse(item.content);
                  if (parsed.avatar) {
                    parsed.avatar = this.fixImageUrl(parsed.avatar);
                    item.content = JSON.stringify(parsed);
                  } else if (Array.isArray(parsed)) {
                    let changed = false;
                    parsed.forEach((p) => {
                      if (p.avatar) {
                        p.avatar = this.fixImageUrl(p.avatar);
                        changed = true;
                      }
                    });
                    if (changed) item.content = JSON.stringify(parsed);
                  }
                } catch (e) {}
              }
            });
            // 构建 blockMap 缓存，避免 computed 中反复 find
            const map = {};
            this.blockList.forEach((item) => {
              if (item.blockKey) map[item.blockKey] = item;
            });
            this.blockMap = map;
            this.loading = false;
          })
          .catch(() => {
            this.loading = false;
          });
    },
    /** 模板中获取板块（仍保留，供模板直接调用） */
    getBlock(key) {
      return this.blockMap[key] || null;
    },
    /** 解析板块的图片URL列表 */
    getBlockImages(key) {
      const block = this.blockMap[key];
      return this._parseImageUrls(block ? block.imageUrls : null);
    },
    /** 内部：安全解析JSON图片URL */
    _parseImageUrls(jsonStr) {
      if (!jsonStr) return [];
      try { return JSON.parse(jsonStr); } catch (e) { return []; }
    },
    /** 内部：安全解析板块content中的JSON数据 */
    _parseBlockContent(key, defaultVal) {
      const block = this.blockMap[key];
      if (!block || !block.content) return defaultVal;
      try { return JSON.parse(block.content); } catch (e) { return defaultVal; }
    },
    goBack() {
      uni.navigateBack({ delta: 1 });
    },
    handleNavClick(nav) {
      switch (nav.linkType) {
        case 'page':
          if (nav.linkUrl) {
            uni.navigateTo({ url: nav.linkUrl, fail: () => { uni.switchTab({ url: nav.linkUrl }); } });
          }
          break;
        case 'web':
          if (nav.linkUrl) {
            // #ifdef H5
            window.open(nav.linkUrl, '_blank');
            // #endif
            // #ifdef MP
            uni.navigateTo({ url: `/pages/webview/index?url=${encodeURIComponent(nav.linkUrl)}` });
            // #endif
          }
          break;
        case 'phone':
          if (nav.linkUrl) {
            // #ifdef MP || APP-PLUS
            uni.makePhoneCall({ phoneNumber: nav.linkUrl });
            // #endif
          }
          break;
        case 'map':
          this.openMap();
          break;
      }
    },
    previewImage(urls, index) {
      uni.previewImage({ urls, current: index });
    },
    callPhone() {
      if (!this.contactData || !this.contactData.phone) return;
      // #ifdef MP || APP-PLUS
      uni.makePhoneCall({ phoneNumber: this.contactData.phone });
      // #endif
    },
    openMap() {
      const data = this.contactData;
      if (!data) return;
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
/* ===== 蓝白商务风配色变量 ===== */
$blue-primary: #003da6;
$blue-container: #0052d9;
$blue-soft: #dbe1ff;
$blue-bg: #e8edff;
$surface-bright: #f9f9ff;
$surface-container: #ebedf9;
$surface-low: #f1f3ff;
$on-surface: #181c23;
$on-surface-variant: #434654;
$outline: #737686;
$outline-variant: #c3c6d7;
$text-white: #ffffff;
$border-light: #ebedf9;
$radius-sm: 8rpx;
$radius-md: 16rpx;
$radius-lg: 24rpx;

.about-page { min-height: 100vh; background-color: $surface-bright; }
.page-body { padding-bottom: env(safe-area-inset-bottom); }

/* ===== 导航栏 ===== */
.nav-bar {
  position: fixed; top: 0; left: 0; right: 0; z-index: 999;
  background-color: $blue-primary;
  box-shadow: 0 4rpx 16rpx rgba(0,61,166,0.3);
  &__content { display: flex; align-items: center; height: 44px; padding: 0 16px; }
  &__back { width: 44px; height: 44px; display: flex; align-items: center; justify-content: center; color: $text-white; font-size: 18px; }
  &__title { flex: 1; text-align: center; font-size: 17px; font-weight: 600; color: $text-white; }
  &__right { width: 44px; }
}

/* ===== Loading ===== */
.loading-wrap { display: flex; flex-direction: column; justify-content: center; align-items: center; height: 60vh; }
.loading-spinner { width: 60rpx; height: 60rpx; border: 4rpx solid $border-light; border-top-color: $blue-primary; border-radius: 50%; animation: spin 0.8s linear infinite; margin-bottom: 20rpx; }
@keyframes spin { to { transform: rotate(360deg); } }
.loading-text { font-size: 26rpx; color: $outline; }

/* ===== Section 通用 ===== */
.section { padding: 44rpx 32rpx; background-color: $surface-bright; &--alt { background-color: $surface-container; } }
.section-header { display: flex; align-items: center; margin-bottom: 28rpx; }
.section-accent { width: 6rpx; height: 36rpx; background-color: $blue-primary; border-radius: 3rpx; margin-right: 14rpx; }
.section-title { font-size: 34rpx; font-weight: 600; color: $on-surface; }

/* ===== Hero 区 ===== */
.hero-section { position: relative; width: 100%; height: 640rpx; overflow: hidden; }
.hero-bg__img { width: 100%; height: 100%; }
.hero-bg__placeholder { width: 100%; height: 100%; background: linear-gradient(135deg, $blue-primary 0%, $blue-container 100%); }
.hero-overlay { position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: linear-gradient(180deg, rgba(0,61,166,0.2) 0%, rgba(24,28,35,0.88) 100%); }
.hero-content { position: absolute; bottom: 72rpx; left: 40rpx; right: 40rpx; }
.hero-title { font-size: 52rpx; font-weight: 600; color: $text-white; line-height: 72rpx; display: block; margin-bottom: 12rpx; }
.hero-subtitle { font-size: 28rpx; color: rgba(235,237,249,0.9); line-height: 44rpx; display: block; }

/* ===== 企业简介 ===== */
.intro-card { background-color: #ffffff; border: 1rpx solid $outline-variant; border-radius: $radius-sm; padding: 28rpx; margin-bottom: 20rpx; }
.bento-grid { display: flex; flex-wrap: wrap; gap: 16rpx; }
.bento-card {
  width: calc(50% - 8rpx); background-color: $surface-low; border-radius: $radius-sm; padding: 28rpx;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
}
.bento-icon-wrap { width: 56rpx; height: 56rpx; border-radius: 50%; background-color: $blue-soft; display: flex; align-items: center; justify-content: center; margin-bottom: 16rpx; }
.bento-icon-text { font-size: 28rpx; color: $blue-primary; font-weight: 600; }
.bento-value { font-size: 36rpx; font-weight: 600; color: $on-surface; margin-bottom: 6rpx; }
.bento-label { font-size: 22rpx; color: $outline; }

/* ===== 核心优势 ===== */
.advantage-list { display: flex; flex-direction: column; gap: 20rpx; }
.advantage-card {
  background-color: #ffffff; border: 1rpx solid $outline-variant; border-radius: $radius-sm; padding: 28rpx;
  display: flex; flex-direction: row; align-items: flex-start;
}
.advantage-icon-wrap {
  width: 72rpx; height: 72rpx; border-radius: 50%; background-color: $blue-container;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0; margin-right: 20rpx;
}
.advantage-icon-text { font-size: 32rpx; color: $text-white; font-weight: 600; }
.advantage-body { flex: 1; }
.advantage-title { font-size: 28rpx; font-weight: 600; color: $on-surface; display: block; margin-bottom: 8rpx; }
.advantage-desc { font-size: 24rpx; color: $on-surface-variant; line-height: 40rpx; display: block; }

/* ===== 领导致辞 ===== */
.leader-card { background-color: #ffffff; border: 1rpx solid $outline-variant; border-radius: $radius-sm; overflow: hidden; }
.leader-img-wrap { height: 400rpx; width: 100%; background-color: $surface-container; overflow: hidden; }
.leader-img { width: 100%; }
.leader-img-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, $blue-primary 0%, $blue-container 100%); }
.leader-placeholder-text { font-size: 80rpx; color: rgba(255,255,255,0.6); font-weight: 600; }
.leader-body { padding: 28rpx; }
.leader-quote-icon { font-size: 80rpx; color: $blue-soft; font-family: Georgia, serif; line-height: 1; display: block; margin-bottom: -20rpx; }
.leader-quote { font-size: 26rpx; color: $on-surface-variant; line-height: 44rpx; font-style: italic; display: block; margin-bottom: 24rpx; }
.leader-divider { height: 1rpx; background-color: $outline-variant; margin-bottom: 20rpx; }
.leader-meta { display: flex; flex-direction: row; justify-content: space-between; align-items: center; }
.leader-name { font-size: 28rpx; font-weight: 600; color: $on-surface; }
.leader-role { font-size: 22rpx; color: $blue-primary; }

/* ===== 发展历程 ===== */
.timeline { position: relative; padding-left: 40rpx; }
.timeline-item { position: relative; padding-bottom: 40rpx; }
.timeline-item:last-child { padding-bottom: 0; }
.timeline-dot {
  position: absolute; left: -40rpx; top: 8rpx; width: 24rpx; height: 24rpx; border-radius: 50%;
  background-color: $outline; border: 4rpx solid $surface-container;
  &.active { background-color: $blue-primary; border-color: $surface-low; }
}
.timeline-line { position: absolute; left: -30rpx; top: 36rpx; bottom: 0; width: 4rpx; background-color: $outline-variant; }
.timeline-content { padding-left: 16rpx; }
.timeline-year { font-size: 28rpx; font-weight: 600; color: $outline; display: block; margin-bottom: 8rpx; &.active { color: $blue-primary; } }
.timeline-event { font-size: 24rpx; color: $on-surface-variant; line-height: 40rpx; display: block; }

/* ===== 核心团队 ===== */
.team-grid { display: flex; flex-wrap: wrap; gap: 20rpx; }
.team-card {
  width: calc(50% - 10rpx); background-color: #ffffff; border: 1rpx solid $outline-variant;
  border-radius: $radius-sm; overflow: hidden; display: flex; flex-direction: column; align-items: center;
}
.team-avatar-wrap { width: 100%; height: 340rpx; background-color: $surface-container; overflow: hidden; }
.team-avatar { width: 100%; }
.team-avatar-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background-color: $blue-bg; }
.avatar-text { font-size: 64rpx; color: $blue-primary; font-weight: 600; }
.team-name { font-size: 26rpx; font-weight: 600; color: $on-surface; margin-top: 16rpx; }
.team-role { font-size: 22rpx; color: $blue-primary; margin-top: 6rpx; padding-bottom: 20rpx; }

/* ===== 资质荣誉 ===== */
.honor-intro { padding: 24rpx; margin-bottom: 20rpx; background-color: #ffffff; border: 1rpx solid $outline-variant; border-radius: $radius-sm; }
.honor-text { font-size: 26rpx; color: $on-surface-variant; line-height: 44rpx; }
.honor-tags { display: flex; flex-wrap: wrap; gap: 16rpx; margin-bottom: 24rpx; }
.honor-tag {
  padding: 12rpx 24rpx; background-color: $blue-bg; border-radius: 30rpx; border: 1rpx solid rgba(0,61,166,0.12);
  &__text { font-size: 22rpx; color: $blue-primary; font-weight: 500; }
}
.honor-images { display: flex; flex-wrap: wrap; gap: 16rpx; }
.honor-img { width: 100%; height: 360rpx; border-radius: $radius-sm; }

/* ===== 联系方式 ===== */
.contact-list { display: flex; flex-direction: column; gap: 16rpx; }
.contact-card {
  display: flex; align-items: center; padding: 28rpx 24rpx;
  background-color: #ffffff; border: 1rpx solid $outline-variant; border-radius: $radius-sm;
}
.contact-icon-wrap {
  width: 72rpx; height: 72rpx; border-radius: 50%; background-color: $blue-container;
  display: flex; align-items: center; justify-content: center; margin-right: 24rpx; flex-shrink: 0;
  .iconfont { font-size: 36rpx; color: $text-white; }
  &--blue { background: linear-gradient(135deg, $blue-primary, $blue-container); }
}
.contact-info { flex: 1; min-width: 0; }
.contact-label { display: block; font-size: 22rpx; color: $outline; margin-bottom: 6rpx; }
.contact-value { display: block; font-size: 28rpx; color: $on-surface; font-weight: 600; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.contact-btn {
  padding: 12rpx 32rpx; font-size: 24rpx; font-weight: 600; color: $text-white;
  background-color: $blue-primary; border-radius: 30rpx; flex-shrink: 0;
}

/* ===== Footer ===== */
.page-footer { padding: 48rpx 32rpx 40rpx; text-align: center; background-color: $surface-bright; }
.page-footer__text { font-size: 22rpx; color: $outline-variant; }

/* ===== Empty ===== */
.empty-wrap { display: flex; flex-direction: column; justify-content: center; align-items: center; height: 60vh; }
.empty-text { font-size: 28rpx; color: $outline; }

/* ===== SVG图标支持 ===== */
.icon-svg { width: 40rpx; height: 40rpx; filter: brightness(0) invert(1); }
.icon-svg-small { width: 36rpx; height: 36rpx; filter: brightness(0) invert(1); }

/* ===== 增强的视觉效果 ===== */
.bento-card, .advantage-card, .contact-card, .team-card {
  transition: all 0.3s ease;
  &:active { transform: scale(0.98); }
}
.contact-card {
  &:active { transform: translateX(4rpx); }
}
.contact-btn {
  transition: all 0.3s ease;
  &:active { transform: scale(0.95); }
}
.honor-tag {
  transition: all 0.3s ease;
  &:active { transform: scale(0.95); }
}

/* ===== 渐变增强 ===== */
.nav-bar {
  background: linear-gradient(135deg, $blue-primary 0%, $blue-container 100%) !important;
}
.section-accent {
  background: linear-gradient(180deg, $blue-primary, $blue-container) !important;
}
.bento-icon-wrap {
  background: linear-gradient(135deg, $blue-primary, $blue-container) !important;
  box-shadow: 0 4rpx 12rpx rgba(0,61,166,0.3) !important;
}
.advantage-icon-wrap {
  background: linear-gradient(135deg, $blue-primary, $blue-container) !important;
  box-shadow: 0 4rpx 12rpx rgba(0,61,166,0.3) !important;
}
.contact-icon-wrap {
  background: linear-gradient(135deg, $blue-primary, $blue-container) !important;
  box-shadow: 0 4rpx 12rpx rgba(0,61,166,0.3) !important;
}
.contact-btn {
  background: linear-gradient(135deg, $blue-primary, $blue-container) !important;
  box-shadow: 0 4rpx 12rpx rgba(0,61,166,0.3) !important;
}
.timeline-dot.active {
  background: linear-gradient(135deg, $blue-primary, $blue-container) !important;
  box-shadow: 0 0 12rpx rgba(0,61,166,0.5) !important;
}
.leader-divider {
  background: linear-gradient(90deg, $blue-primary, $blue-soft) !important;
}

/* ===== 阴影增强 ===== */
.intro-card, .leader-card, .honor-intro {
  box-shadow: 0 4rpx 16rpx rgba(0,61,166,0.08) !important;
}
.honor-img {
  box-shadow: 0 4rpx 12rpx rgba(0,61,166,0.1) !important;
}
.bento-card, .team-card {
  box-shadow: 0 2rpx 8rpx rgba(0,61,166,0.06) !important;
}
.bento-value {
  color: $blue-primary !important;
  font-weight: 700 !important;
}
.hero-title {
  text-shadow: 0 2rpx 8rpx rgba(0,0,0,0.3) !important;
}
</style>