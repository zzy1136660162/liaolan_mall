<template>
  <view class="contact-page">
    <view>
      <view class="hero">
        <view class="hero-title">{{ contactInfo.companyName || '联系我们' }}</view>
        <view class="hero-desc" v-if="contactInfo.companyAddress">{{ contactInfo.companyAddress }}</view>
      </view>

      <view class="section">
        <contact-entry :actions="entryActions" @select="handleEntry"></contact-entry>
      </view>

      <view class="card">
        <view class="section-title">联系信息</view>
        <view class="info-row" v-if="contactInfo.consumerHotline">
          <text class="label">联系电话</text>
          <text class="value">{{ contactInfo.consumerHotline }}</text>
        </view>
        <view class="info-row" v-if="contactInfo.companyAddress">
          <text class="label">公司地址</text>
          <text class="value">{{ contactInfo.companyAddress }}</text>
        </view>
        <view class="empty-tip" v-if="!contactInfo.consumerHotline && !contactInfo.companyAddress">暂未配置联系信息</view>
      </view>

      <view class="card" v-if="contactInfo.serviceQrcode || contactInfo.serviceLink">
        <view class="section-title">客服二维码</view>
        <view class="qrcode-box" v-if="contactInfo.serviceQrcode" @click="previewQrcode">
          <image :src="contactInfo.serviceQrcode" class="qrcode-image" mode="aspectFill"></image>
          <view class="qrcode-tip">点击预览二维码</view>
        </view>
        <view class="empty-tip" v-else>已配置在线客服入口，可通过上方“在线客服”进入</view>
      </view>

      <view class="card" id="message-card">
        <view class="section-title">在线留言</view>
        <view class="form-item">
          <view class="form-label">公司名称</view>
          <input v-model="form.companyName" class="form-input" maxlength="100" placeholder="请输入公司名称（选填）" />
        </view>
        <view class="form-item">
          <view class="form-label">联系人姓名</view>
          <input v-model="form.name" class="form-input" maxlength="32" placeholder="请输入联系人姓名" />
        </view>
        <view class="form-item">
          <view class="form-label">手机号</view>
          <input v-model="form.mobile" class="form-input" maxlength="11" type="number" placeholder="请输入手机号" />
        </view>
        <view class="form-item textarea-item">
          <view class="form-label">留言内容</view>
          <textarea
            v-model="form.content"
            class="form-textarea"
            maxlength="500"
            placeholder="请输入您的需求或留言内容"
          />
        </view>
        <button class="submit-btn" :loading="submitLoading" @click="submitForm">提交留言</button>
      </view>
    </view>
  </view>
</template>

<script>
import contactEntry from '@/components/contactEntry/index.vue';
import { getContactInfo, saveContactMessage } from '@/api/api.js';
import { checkPhone } from '@/utils/validate.js';

const app = getApp();

export default {
  components: {
    contactEntry,
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      sourcePage: 'contact_page',
      contactInfo: {
        companyName: '',
        consumerHotline: '',
        companyAddress: '',
        mapLink: '',
        serviceLink: '',
        serviceQrcode: '',
      },
      entryActions: [
        { key: 'message', label: '在线留言', icon: 'icon-bianji' },
        { key: 'service', label: '在线客服', icon: 'icon-kefu' },
        { key: 'phone', label: '联系电话', icon: 'icon-dianhua' },
        { key: 'map', label: '地图导航', icon: 'icon-dingwei' },
      ],
      form: {
        companyName: '',
        name: '',
        mobile: '',
        content: '',
      },
    };
  },
  onLoad(options) {
    if (options.sourcePage) {
      this.sourcePage = options.sourcePage;
    }
    this.getInfo();
  },
  methods: {
    getInfo() {
      this.loading = true;
      getContactInfo()
        .then((res) => {
          this.contactInfo = Object.assign({}, this.contactInfo, res.data || res);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleEntry(type) {
      switch (type) {
        case 'message':
          this.scrollToMessage();
          break;
        case 'service':
          this.openService();
          break;
        case 'phone':
          this.callPhone();
          break;
        case 'map':
          this.openMap();
          break;
        default:
          break;
      }
    },
    scrollToMessage() {
      uni.pageScrollTo({
        selector: '#message-card',
        duration: 200,
      });
    },
    openService() {
      if (this.contactInfo.serviceLink) {
        this.openWebLink(this.contactInfo.serviceLink, '在线客服');
        return;
      }
      if (this.contactInfo.serviceQrcode) {
        this.previewQrcode();
        return;
      }
      this.$util.Tips({ title: '暂未配置在线客服' });
    },
    callPhone() {
      if (!this.contactInfo.consumerHotline) {
        this.$util.Tips({ title: '暂未配置联系电话' });
        return;
      }
      uni.makePhoneCall({
        phoneNumber: this.contactInfo.consumerHotline,
      });
    },
    openMap() {
      if (!this.contactInfo.mapLink) {
        this.$util.Tips({ title: '暂未配置地图导航' });
        return;
      }
      this.openWebLink(this.contactInfo.mapLink, '地图导航');
    },
    previewQrcode() {
      if (!this.contactInfo.serviceQrcode) {
        this.$util.Tips({ title: '暂未配置客服二维码' });
        return;
      }
      uni.previewImage({
        urls: [this.contactInfo.serviceQrcode],
      });
    },
    openWebLink(url, title) {
      // #ifdef H5
      if (!app.globalData.isIframe) {
        location.href = url;
        return;
      }
      // #endif
      const parts = url.split('?');
      const route = parts.length > 1
        ? `/pages/users/web_page/index?webUel=${parts[0]}&title=${title}&${parts[1]}`
        : `/pages/users/web_page/index?webUel=${url}&title=${title}`;
      uni.navigateTo({
        url: route,
      });
    },
    submitForm() {
      if (!this.form.name) {
        this.$util.Tips({ title: '请输入联系人姓名' });
        return;
      }
      if (!this.form.mobile) {
        this.$util.Tips({ title: '请输入手机号' });
        return;
      }
      if (!checkPhone(this.form.mobile)) {
        this.$util.Tips({ title: '请输入正确的手机号码' });
        return;
      }
      if (!this.form.content) {
        this.$util.Tips({ title: '请输入留言内容' });
        return;
      }
      this.submitLoading = true;
      saveContactMessage({
        companyName: this.form.companyName,
        name: this.form.name,
        mobile: this.form.mobile,
        content: this.form.content,
        sourcePage: this.sourcePage || 'contact_page',
      })
        .then(() => {
          this.$util.Tips({ title: '提交成功' });
          this.form = {
            companyName: '',
            name: '',
            mobile: '',
            content: '',
          };
        })
        .finally(() => {
          this.submitLoading = false;
        });
    },
  },
};
</script>

<style scoped lang="scss">
.contact-page {
  min-height: 100vh;
  background: #f5f6f8;
}

.hero {
  padding: 40rpx 30rpx 24rpx;
  background: #ffffff;
}

.hero-title {
  font-size: 40rpx;
  color: #303133;
  line-height: 56rpx;
  font-weight: 600;
}

.hero-desc {
  margin-top: 12rpx;
  font-size: 26rpx;
  color: #606266;
  line-height: 38rpx;
}

.section {
  padding: 24rpx 30rpx 0;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  margin: 24rpx 30rpx 0;
  padding: 28rpx 24rpx;
}

.section-title {
  font-size: 30rpx;
  color: #303133;
  line-height: 42rpx;
  margin-bottom: 24rpx;
}

.info-row {
  display: flex;
  align-items: flex-start;
  padding: 14rpx 0;
}

.label {
  width: 140rpx;
  font-size: 24rpx;
  color: #909399;
  line-height: 36rpx;
  flex-shrink: 0;
}

.value {
  flex: 1;
  font-size: 26rpx;
  color: #303133;
  line-height: 36rpx;
}

.empty-tip {
  font-size: 24rpx;
  color: #909399;
  line-height: 36rpx;
}

.qrcode-box {
  text-align: center;
}

.qrcode-image {
  width: 260rpx;
  height: 260rpx;
  border-radius: 12rpx;
}

.qrcode-tip {
  margin-top: 16rpx;
  font-size: 24rpx;
  color: #909399;
}

.form-item {
  margin-bottom: 20rpx;
}

.form-label {
  font-size: 24rpx;
  color: #606266;
  line-height: 34rpx;
  margin-bottom: 12rpx;
}

.form-input,
.form-textarea {
  width: 100%;
  background: #f7f8fa;
  border-radius: 12rpx;
  box-sizing: border-box;
  font-size: 26rpx;
  color: #303133;
}

.form-input {
  height: 88rpx;
  padding: 0 24rpx;
}

.form-textarea {
  padding: 22rpx 24rpx;
}

.textarea-item {
  margin-bottom: 28rpx;
}

.form-textarea {
  min-height: 220rpx;
}

.submit-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 44rpx;
  font-size: 28rpx;
  color: #fff;
  background: #2d6cdf;
}

.submit-btn::after {
  border: 0;
}
</style>
