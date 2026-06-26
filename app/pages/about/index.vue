<template>
  <view class="about-page">
    <!-- Glass NavBar -->
    <view class="nav-bar" :style="{ paddingTop: statusBarHeight + 'px' }">
      <view class="nav-bar__content">
        <view class="nav-bar__back" @click="goBack">
          <text class="iconfont icon-xiangzuo"></text>
        </view>
        <text class="nav-bar__title">关于我们</text>
        <view class="nav-bar__right"></view>
      </view>
    </view>

    <!-- 加载中 -->
    <view v-if="loading" class="loading-wrap">
      <view class="loading-spinner"></view>
      <text class="loading-text">加载中...</text>
    </view>

    <view v-else-if="blockList.length > 0" class="page-body">

      <!-- Hero -->
      <view class="hero-section">
        <image v-if="heroBanner.bgImage" :src="heroBanner.bgImage" class="hero-bg__img" mode="aspectFill" />
        <view v-else class="hero-bg__placeholder"></view>
        <view class="hero-overlay"></view>
        <view class="hero-content">
          <view class="hero-badge">关于我们</view>
          <text class="hero-title">{{ heroBanner.title }}</text>
          <text class="hero-subtitle" v-if="heroBanner.subtitle">{{ heroBanner.subtitle }}</text>
        </view>
      </view>

      <!-- 企业简介 -->
      <view v-if="getBlock('company_intro')" class="section">
        <text class="section-title">连接未来的工业纽带</text>
        <view class="intro-card">
          <rich-text :nodes="getBlock('company_intro').content || ''"></rich-text>
        </view>
      </view>

      <!-- 核心数据 -->
      <view v-if="statsData.length > 0" class="section">
        <text class="section-title">核心实力</text>
        <view class="values-grid">
          <view v-for="(item, index) in statsData" :key="index" class="value-card" :class="['value-card--' + (index % 4)]">
            <view class="value-icon-wrap">
              <text class="iconfont" :class="item.iconClass"></text>
            </view>
            <text class="value-title">{{ item.value }}</text>
            <text class="value-desc">{{ item.label }}</text>
          </view>
        </view>
      </view>

      <!-- 核心优势 -->
      <view v-if="advantagesData.length > 0" class="section section--dim">
        <text class="section-title">{{ (getBlock('core_advantages') || {}).title || '核心优势' }}</text>
        <view class="advantage-list">
          <view class="advantage-card" v-for="(item, index) in advantagesData" :key="index">
            <view class="advantage-icon-wrap">
              <text class="iconfont" :class="item.iconClass"></text>
            </view>
            <view class="advantage-body">
              <text class="advantage-title">{{ item.title }}</text>
              <text class="advantage-desc">{{ item.desc }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 生产与质控 -->
      <view v-if="getBlock('production_strength') && getBlock('production_strength').content" class="section">
        <view class="section-header-row">
          <text class="section-title">生产与质控</text>
          <text class="section-more">实时工况</text>
        </view>
        <view class="intro-card">
          <rich-text :nodes="getBlock('production_strength').content"></rich-text>
        </view>
      </view>

      <!-- 领导致辞（可选） -->
      <view v-if="leaderData" class="section section--dim">
        <text class="section-title">{{ (getBlock('leader_speech') || {}).title || '领导致辞' }}</text>
        <view class="leader-card">
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

      <!-- 发展历程（可选） -->
      <view v-if="timelineData.length > 0" class="section">
        <text class="section-title">{{ (getBlock('development_history') || {}).title || '发展历程' }}</text>
        <view class="timeline">
          <view v-for="(item, index) in timelineData" :key="index" class="timeline-item">
            <view class="timeline-dot" :class="{ active: index === 0 }"></view>
            <view class="timeline-line" v-if="index < timelineData.length - 1"></view>
            <view class="timeline-content">
              <text class="timeline-year" :class="{ active: index === 0 }">{{ item.year }}</text>
              <text class="timeline-event">{{ item.event }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 资质荣誉（可选） -->
      <view v-if="honorData" class="section section--dim">
        <text class="section-title">{{ (getBlock('qualification_honor') || {}).title || '资质荣誉' }}</text>
        <view class="honor-tags" v-if="honorData.tags && honorData.tags.length > 0">
          <view class="honor-tag" v-for="(tag, idx) in honorData.tags" :key="idx">
            <text class="honor-tag__text">{{ tag }}</text>
          </view>
        </view>
        <view v-if="getBlockImages('qualification_honor').length > 0" class="honor-images">
          <image v-for="(url, idx) in getBlockImages('qualification_honor')" :key="idx" :src="url" class="honor-img" mode="aspectFill" @click="previewImage(getBlockImages('qualification_honor'), idx)" />
        </view>
      </view>

      <!-- 联系我们 -->
      <view class="section">
        <text class="section-title">{{ (getBlock('contact_info') || {}).title || '联系我们' }}</text>
        <view class="contact-card" v-if="contactData && (contactData.phone || contactData.address)">
          <view class="contact-row" v-if="contactData.address">
            <view class="contact-icon-box">
              <text class="iconfont icon-dizhi"></text>
            </view>
            <view class="contact-text">
              <text class="contact-row-label">总部地址</text>
              <text class="contact-row-value">{{ contactData.address }}</text>
            </view>
          </view>
          <view class="contact-row" v-if="contactData.phone">
            <view class="contact-icon-box">
              <text class="iconfont icon-dianhua"></text>
            </view>
            <view class="contact-text">
              <text class="contact-row-label">咨询热线</text>
              <text class="contact-row-value contact-phone">{{ contactData.phone }}</text>
            </view>
          </view>
          <view class="contact-row" v-if="contactData.email">
            <view class="contact-icon-box">
              <text class="iconfont icon-youxiang"></text>
            </view>
            <view class="contact-text">
              <text class="contact-row-label">商务邮箱</text>
              <text class="contact-row-value">{{ contactData.email }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 需求表单 -->
      <view class="section">
        <text class="section-title">提交您的需求</text>
        <view class="form-card">
          <view class="form-group">
            <text class="form-label">姓名 / 称呼</text>
            <input v-model="form.name" class="form-input" placeholder="请输入您的姓名" />
          </view>
          <view class="form-group">
            <text class="form-label">联系电话</text>
            <input v-model="form.phone" class="form-input" type="number" maxlength="11" placeholder="请输入您的联系电话" />
          </view>
          <view class="form-group">
            <text class="form-label">需求描述</text>
            <textarea v-model="form.desc" class="form-textarea" placeholder="请简述您的产品需求或合作意向..." />
          </view>
          <view class="form-submit-btn" @click="submitForm">
            <text>立即提交</text>
            <text class="iconfont icon-feiji"></text>
          </view>
          <text class="form-hint">我们将在2个工作日内与您联系</text>
        </view>
      </view>

      <!-- 底部 -->
      <view class="page-footer">
        <text class="page-footer__text">© 2024 辽宁辽缆集团有限公司</text>
      </view>
    </view>

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
      blockMap: {},
      form: {
        name: '',
        phone: '',
        desc: ''
      },
      iconMap: {
        factory: 'icon-dianpu',
        engineering: 'icon-tuandui',
        verified: 'icon-yishoucang2',
        precision_manufacturing: 'icon-jingyanzhi',
        handshake: 'icon-tuandui',
      },
    };
  },
  computed: {
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
    statsData() { return this._parseBlockContent("company_stats", []); },
    advantagesData() { return this._parseBlockContent("core_advantages", []); },
    leaderData() { return this._parseBlockContent("leader_speech", null); },
    timelineData() { return this._parseBlockContent("development_history", []); },
    teamData() { return this._parseBlockContent("core_team", []); },
    honorData() {
      const block = this.blockMap["qualification_honor"];
      if (!block || !block.content) return null;
      try {
        const parsed = JSON.parse(block.content);
        if (typeof parsed === "string") return { text: parsed, tags: [] };
        return parsed;
      } catch (e) { return { text: block.content, tags: [] }; }
    },
    contactData() { return this._parseBlockContent("contact_info", {}); },
  },
  onLoad() {
    const sysInfo = uni.getSystemInfoSync();
    this.statusBarHeight = sysInfo.statusBarHeight || 0;
    this.getData();
  },
  methods: {
    fixImageUrl(url) {
      if (!url) return '';
      let fixed = url;
      const prefix = 'http://127.0.0.1:20500/';
      while (fixed.includes('/' + prefix) || fixed.includes(prefix + prefix)) {
        fixed = fixed.replace('/' + prefix, prefix);
        fixed = fixed.replace(prefix + prefix, prefix);
      }
      if (fixed.startsWith('crmebimage/') || fixed.startsWith('uploadf/')) {
        fixed = prefix + fixed;
      }
      return fixed;
    },
    getData() {
      this.loading = true;
      getAboutFullInfo().then((res) => {
        const raw = res.data || {};
        const data = raw.data || raw.blockList || raw;
        this.blockList = Array.isArray(data) ? data : (data.blockList || []);
        this.navList = Array.isArray(data) ? [] : (data.navList || []);
        this.blockList.forEach((item) => {
          if (item.imageUrls) {
            try {
              const urls = JSON.parse(item.imageUrls);
              item.imageUrls = JSON.stringify(urls.map((u) => this.fixImageUrl(u)));
            } catch (e) {}
          }
          if (item.content) {
            try {
              const parsed = JSON.parse(item.content);
              if (parsed.avatar) { parsed.avatar = this.fixImageUrl(parsed.avatar); item.content = JSON.stringify(parsed); }
              else if (Array.isArray(parsed)) {
                let changed = false;
                parsed.forEach((p) => { if (p.avatar) { p.avatar = this.fixImageUrl(p.avatar); changed = true; } });
                if (changed) item.content = JSON.stringify(parsed);
              }
            } catch (e) {}
          }
        });
        const map = {};
        this.blockList.forEach((item) => { if (item.blockKey) map[item.blockKey] = item; });
        // 用iconMap给统计数据和优势数据加上iconClass
        ['company_stats', 'core_advantages'].forEach(key => {
          const block = map[key];
          if (block && block.content) {
            try {
              const arr = JSON.parse(block.content);
              block.content = JSON.stringify(arr.map(it => ({
                ...it,
                iconClass: this.iconMap[it.icon] || 'icon-dianpu'
              })));
            } catch (e) {}
          }
        });
        this.blockMap = map;
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    getBlock(key) { return this.blockMap[key] || null; },
    getBlockImages(key) {
      const block = this.blockMap[key];
      return this._parseImageUrls(block ? block.imageUrls : null);
    },
    _parseImageUrls(jsonStr) {
      if (!jsonStr) return [];
      try { return JSON.parse(jsonStr); } catch (e) { return []; }
    },
    _parseBlockContent(key, defaultVal) {
      const block = this.blockMap[key];
      if (!block || !block.content) return defaultVal;
      try { return JSON.parse(block.content); } catch (e) { return defaultVal; }
    },
    goBack() { uni.navigateBack({ delta: 1 }); },
    previewImage(urls, index) { uni.previewImage({ urls, current: index }); },
    submitForm() {
      if (!this.form.phone) {
        return uni.showToast({ title: '请输入联系电话', icon: 'none' });
      }
      if (!/^1\d{10}$/.test(this.form.phone)) {
        return uni.showToast({ title: '手机号格式不正确', icon: 'none' });
      }
      uni.showToast({ title: '提交成功，我们会尽快联系您', icon: 'success' });
      this.form = { name: '', phone: '', desc: '' };
    },
  },
};
</script>

<style scoped lang="scss">
// ===== 主题色 =====
$primary: #0061a5;
$primary-container: #0099ff;
$tertiary: #006875;
$secondary: #705d00;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$surface: #f9f9fc;
$surface-container-lowest: #ffffff;
$surface-container: #eeeef0;
$outline-variant: #bfc7d5;

.about-page { min-height: 100vh; background-color: $surface; }
.page-body { padding-bottom: env(safe-area-inset-bottom); }

// ===== 导航栏 Glass =====
.nav-bar {
  position: fixed; top: 0; left: 0; right: 0; z-index: 999;
  background: rgba($surface, 0.85);
  backdrop-filter: blur(24rpx);
  -webkit-backdrop-filter: blur(24rpx);
  &__content { display: flex; align-items: center; height: 44px; padding: 0 16px; }
  &__back { width: 44px; height: 44px; display: flex; align-items: center; justify-content: center; color: $primary; font-size: 18px; }
  &__title { flex: 1; text-align: center; font-size: 17px; font-weight: 700; color: $primary; }
  &__right { width: 44px; }
}

// ===== Loading =====
.loading-wrap { display: flex; flex-direction: column; justify-content: center; align-items: center; height: 60vh; }
.loading-spinner { width: 60rpx; height: 60rpx; border: 4rpx solid $outline-variant; border-top-color: $primary; border-radius: 50%; animation: spin 0.8s linear infinite; margin-bottom: 20rpx; }
@keyframes spin { to { transform: rotate(360deg); } }
.loading-text { font-size: 26rpx; color: #707884; }

// ===== Section =====
.section { padding: 44rpx 40rpx; &--dim { background: $surface-container; } }
.section-title { display: block; font-size: 36rpx; font-weight: 700; color: $on-surface; margin-bottom: 28rpx; }
.section-header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 28rpx;
  .section-title { margin-bottom: 0; }
}
.section-more { font-size: 24rpx; color: $primary; font-weight: 500; }

// ===== Hero =====
.hero-section { position: relative; width: 100%; height: 640rpx; overflow: hidden; }
.hero-bg__img { width: 100%; height: 100%; }
.hero-bg__placeholder { width: 100%; height: 100%; background: linear-gradient(135deg, $primary 0%, $primary-container 100%); }
.hero-overlay { position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: linear-gradient(180deg, rgba($primary, 0.1) 0%, rgba(24,28,35,0.85) 100%); }
.hero-content { position: absolute; bottom: 72rpx; left: 40rpx; right: 40rpx; }
.hero-badge {
  display: inline-block;
  padding: 8rpx 24rpx;
  background: rgba($tertiary, 0.15);
  color: #9cf0ff;
  font-size: 22rpx;
  font-weight: 500;
  border-radius: 999rpx;
  margin-bottom: 16rpx;
  border: 1rpx solid rgba(156, 240, 255, 0.25);
}
.hero-title { display: block; font-size: 52rpx; font-weight: 700; color: #ffffff; line-height: 1.25; margin-bottom: 12rpx; }
.hero-subtitle { display: block; font-size: 26rpx; color: rgba(255,255,255,0.8); line-height: 1.5; }

// ===== 企业简介 =====
.intro-card {
  background: $surface-container-lowest;
  border: 1rpx solid rgba($outline-variant, 0.3);
  border-radius: 24rpx;
  padding: 32rpx;
  font-size: 28rpx;
  color: $on-surface-variant;
  line-height: 1.8;
}

// ===== 核心数据 2x2 =====
.values-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24rpx; }
.value-card {
  padding: 36rpx 28rpx;
  border-radius: 24rpx;
  display: flex;
  flex-direction: column;
  gap: 12rpx;
  background: $surface-container-lowest;
  border: 1rpx solid rgba($outline-variant, 0.3);
  &--0 { background: $surface-container-lowest; }
  &--1 { background: $primary-container; color: #fff;
    .value-title, .value-desc { color: #fff; }
    .value-icon-wrap { background: rgba(255,255,255,0.2); }
    .iconfont { color: #fff; }
  }
  &--2, &--3 { background: $surface-container-lowest; }
}
.value-icon-wrap {
  width: 72rpx; height: 72rpx;
  background: rgba($primary, 0.08);
  border-radius: 16rpx;
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 4rpx;
  .iconfont { font-size: 36rpx; color: $primary; }
}
.value-title { font-size: 36rpx; font-weight: 700; color: $on-surface; }
.value-desc { font-size: 24rpx; color: $on-surface-variant; }

// ===== 核心优势 =====
.advantage-list { display: flex; flex-direction: column; gap: 20rpx; }
.advantage-card {
  background: $surface-container-lowest;
  border: 1rpx solid rgba($outline-variant, 0.3);
  border-radius: 24rpx;
  padding: 28rpx;
  display: flex; flex-direction: row; align-items: flex-start;
  &:active { border-color: $primary-container; }
}
.advantage-icon-wrap {
  width: 72rpx; height: 72rpx;
  background: rgba($primary, 0.08);
  border-radius: 16rpx;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0; margin-right: 20rpx;
  .iconfont { font-size: 36rpx; color: $primary; }
}
.advantage-body { flex: 1; }
.advantage-title { display: block; font-size: 28rpx; font-weight: 600; color: $on-surface; margin-bottom: 8rpx; }
.advantage-desc { display: block; font-size: 24rpx; color: $on-surface-variant; line-height: 1.6; }

// ===== 领导致辞 =====
.leader-card {
  background: $surface-container-lowest;
  border: 1rpx solid rgba($outline-variant, 0.3);
  border-radius: 24rpx;
  overflow: hidden;
}
.leader-body { padding: 36rpx; }
.leader-quote-icon { display: block; font-size: 80rpx; color: rgba($primary, 0.15); line-height: 1; margin-bottom: -24rpx; font-family: Georgia, serif; }
.leader-quote { display: block; font-size: 26rpx; color: $on-surface-variant; line-height: 1.7; font-style: italic; margin-bottom: 28rpx; }
.leader-divider { height: 2rpx; background: rgba($outline-variant, 0.4); margin-bottom: 20rpx; }
.leader-meta { display: flex; justify-content: space-between; align-items: center; }
.leader-name { font-size: 28rpx; font-weight: 600; color: $on-surface; }
.leader-role { font-size: 24rpx; color: $primary; }

// ===== 发展历程 =====
.timeline { position: relative; padding-left: 40rpx; }
.timeline-item { position: relative; padding-bottom: 40rpx; &:last-child { padding-bottom: 0; } }
.timeline-dot {
  position: absolute; left: -38rpx; top: 6rpx; width: 20rpx; height: 20rpx;
  border-radius: 50%; background: $outline-variant;
  &.active { background: $primary; box-shadow: 0 0 12rpx rgba($primary, 0.4); }
}
.timeline-line { position: absolute; left: -30rpx; top: 30rpx; bottom: 0; width: 4rpx; background: rgba($outline-variant, 0.4); }
.timeline-content { padding-left: 16rpx; }
.timeline-year { display: block; font-size: 28rpx; font-weight: 600; color: $on-surface-variant; margin-bottom: 6rpx; &.active { color: $primary; } }
.timeline-event { display: block; font-size: 24rpx; color: $on-surface-variant; line-height: 1.6; }

// ===== 资质荣誉 =====
.honor-tags { display: flex; flex-wrap: wrap; gap: 16rpx; margin-bottom: 24rpx; }
.honor-tag {
  padding: 12rpx 28rpx;
  background: rgba($tertiary, 0.06);
  border: 1rpx solid rgba($tertiary, 0.15);
  border-radius: 999rpx;
  &__text { font-size: 24rpx; color: $tertiary; font-weight: 500; }
}
.honor-images { display: flex; flex-wrap: wrap; gap: 16rpx; }
.honor-img { width: 100%; height: 400rpx; border-radius: 24rpx; }

// ===== 联系我们 =====
.contact-card {
  background: $surface-container-lowest;
  border: 1rpx solid rgba($outline-variant, 0.2);
  border-radius: 32rpx;
  padding: 36rpx 32rpx;
  display: flex; flex-direction: column; gap: 32rpx;
}
.contact-row { display: flex; align-items: flex-start; gap: 24rpx; }
.contact-icon-box {
  width: 72rpx; height: 72rpx;
  background: rgba($primary, 0.08);
  border-radius: 16rpx;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  .iconfont { font-size: 36rpx; color: $primary; }
}
.contact-text { flex: 1; min-width: 0; }
.contact-row-label { display: block; font-size: 22rpx; color: $on-surface-variant; margin-bottom: 4rpx; }
.contact-row-value { display: block; font-size: 28rpx; color: $on-surface; font-weight: 500; line-height: 1.5; word-break: break-all; }
.contact-phone { color: $primary; font-weight: 600; }

// ===== 需求表单 =====
.form-card {
  background: $surface-container-lowest;
  border: 1rpx solid rgba($outline-variant, 0.3);
  border-radius: 24rpx;
  padding: 36rpx 32rpx;
}
.form-group { margin-bottom: 24rpx; }
.form-label { display: block; font-size: 24rpx; color: #707884; font-weight: 500; margin-bottom: 10rpx; margin-left: 8rpx; }
.form-input, .form-textarea {
  width: 100%; box-sizing: border-box;
  background: rgba($surface-container, 0.5);
  border: none;
  border-radius: 24rpx;
  padding: 0 28rpx;
  font-size: 28rpx;
  color: $on-surface;
}
.form-input { height: 96rpx; }
.form-textarea { height: 200rpx; padding-top: 24rpx; }
.form-submit-btn {
  display: flex; align-items: center; justify-content: center; gap: 12rpx;
  width: 100%; height: 104rpx;
  background: $primary-container;
  color: #fff;
  font-size: 32rpx; font-weight: 600;
  border-radius: 32rpx;
  box-shadow: 0 20rpx 40rpx -10rpx rgba($primary-container, 0.3);
  margin-top: 8rpx;
  &:active { transform: scale(0.97); opacity: 0.9; }
  .iconfont { font-size: 36rpx; }
}
.form-hint { display: block; text-align: center; font-size: 22rpx; color: #707884; margin-top: 20rpx; }

// ===== Footer =====
.page-footer { padding: 48rpx 40rpx 60rpx; text-align: center; }
.page-footer__text { font-size: 22rpx; color: rgba($on-surface-variant, 0.5); }

// ===== Empty =====
.empty-wrap { display: flex; justify-content: center; align-items: center; height: 60vh; }
.empty-text { font-size: 28rpx; color: $on-surface-variant; }
</style>
