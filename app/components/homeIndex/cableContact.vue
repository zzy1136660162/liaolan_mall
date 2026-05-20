<template>
  <view class="cable-contact">
    <!-- Section Title -->
    <view class="section-header">
      <view class="header-left">
        <view class="blue-bar"></view>
        <text class="header-title">联系我们</text>
      </view>
    </view>

    <!-- Contact Cards -->
    <view class="contact-cards">
      <!-- Sales Hotline -->
      <view class="contact-item">
        <view class="contact-icon">
          <text class="icon-text">📞</text>
        </view>
        <view class="contact-info">
          <text class="contact-label">全国销售热线</text>
          <text class="contact-value">400-888-9999</text>
        </view>
        <view class="contact-action" @click="callPhone('400-888-9999')">
          <text class="action-text">拨打</text>
        </view>
      </view>

      <!-- Technical Support -->
      <view class="contact-item">
        <view class="contact-icon">
          <text class="icon-text">🔧</text>
        </view>
        <view class="contact-info">
          <text class="contact-label">技术支持</text>
          <text class="contact-value">024-8888-6666</text>
        </view>
        <view class="contact-action" @click="callPhone('024-88886666')">
          <text class="action-text">拨打</text>
        </view>
      </view>

      <!-- Address -->
      <view class="contact-item">
        <view class="contact-icon">
          <text class="icon-text">📍</text>
        </view>
        <view class="contact-info">
          <text class="contact-label">总部地址</text>
          <text class="contact-value addr">辽宁省沈阳市铁西区工业南路88号</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'cableContact',
  methods: {
    callPhone(phone) {
      uni.makePhoneCall({
        phoneNumber: phone,
        fail: () => {}
      });
    }
  }
}
</script>

<style lang="scss" scoped>
$primary: #003da6;
$primary-container: #0052d9;
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

.cable-contact {
  margin: 12rpx 24rpx 24rpx;
  padding: 20rpx;
  background: $surface-lowest;
  border: 1rpx solid $outline-variant;
  border-radius: $r-md;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
  animation: fadeIn 0.4s $ease both;

  .section-header {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
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
        margin-right: 14rpx;
      }

      .header-title {
        font-size: 32rpx;
        font-weight: 600;
        color: $on-surface;
      }
    }
  }

  .contact-cards {
    display: flex;
    flex-direction: column;
    gap: 20rpx;

    .contact-item {
      display: flex;
      align-items: center;
      padding: 20rpx;
      background: $surface-low;
      border-radius: $r-md;
      transition: transform 0.15s $ease;

      &:active {
        transform: scale(0.98);
      }

      .contact-icon {
        width: 72rpx;
        height: 72rpx;
        background: $surface-lowest;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
        flex-shrink: 0;

        .icon-text {
          font-size: 32rpx;
        }
      }

      .contact-info {
        flex: 1;
        display: flex;
        flex-direction: column;

        .contact-label {
          font-size: 24rpx;
          color: $on-surface-variant;
          margin-bottom: 4rpx;
        }

        .contact-value {
          font-size: 28rpx;
          color: $primary;
          font-weight: 600;

          &.addr {
            font-size: 24rpx;
            color: $on-surface;
            font-weight: 400;
          }
        }
      }

      .contact-action {
        padding: 12rpx 28rpx;
        background: $primary;
        color: $white;
        border-radius: $r-md;
        font-size: 24rpx;
        font-weight: 500;
        transition: background 0.2s $ease;

        &:active {
          background: $primary-container;
        }

        .action-text {
          color: $white;
        }
      }
    }
  }
}
</style>
