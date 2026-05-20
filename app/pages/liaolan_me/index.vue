<template>
  <view class="liaolan-me" :data-theme="theme">
    <view class="page-content">
      <!-- 用户信息头部 -->
      <view class="user-header">
        <view class="header-bg"></view>
        <view class="header-content">
          <view class="user-info">
            <view class="avatar-box">
              <image class="avatar" :src="userInfo.avatar || defaultAvatar" mode="aspectFill"></image>
            </view>
            <view class="user-detail">
              <text class="username">{{ userInfo.nickname || '游客用户' }}</text>
              <text class="company">{{ userInfo.company || '公司名称' }}</text>
              <view class="vip-badge" v-if="userInfo.isVip">
                <text class="vip-icon">★</text>
                <text class="vip-text">VIP 采购商</text>
              </view>
            </view>
            <view class="settings-btn" @click="goSettings">
              <text class="iconfont icon-shezhi"></text>
            </view>
          </view>
          <!-- 快速统计 -->
          <view class="quick-stats">
            <view class="stat-item" @click="goPage('/pages/users/user_goods_collection/index')">
              <text class="stat-num">{{ userInfo.favoriteCount || 12 }}</text>
              <text class="stat-label">常购清单</text>
            </view>
            <view class="stat-item" @click="goPage('/pages/users/user_coupon/index')">
              <text class="stat-num">{{ userInfo.couponCount || 5 }}</text>
              <text class="stat-label">优惠券</text>
            </view>
            <view class="stat-item" @click="goPage('/pages/users/user_integral/index')">
              <text class="stat-num">{{ formatNumber(userInfo.integral || 1250) }}</text>
              <text class="stat-label">积分</text>
            </view>
          </view>
        </view>
      </view>

      <view class="page-body">
        <!-- 订单管理 -->
        <view class="order-card">
          <view class="card-header">
            <view class="header-left">
              <view class="header-line"></view>
              <text class="header-title">订单管理</text>
            </view>
            <view class="header-right" @click="goPage('/pages/users/order_list/index')">
              <text class="view-all">查看全部订单</text>
              <text class="iconfont icon-xiangyou"></text>
            </view>
          </view>
          <view class="order-menu">
            <view class="menu-item" @click="goPage('/pages/users/order_list/index?status=0')">
              <view class="icon-box">
                <text class="iconfont icon-daifukuan"></text>
                <view class="badge" v-if="orderData.unPaidCount > 0">{{ orderData.unPaidCount > 99 ? '99+' : orderData.unPaidCount }}</view>
              </view>
              <text class="menu-text">待付款</text>
            </view>
            <view class="menu-item" @click="goPage('/pages/users/order_list/index?status=1')">
              <view class="icon-box">
                <text class="iconfont icon-daifahuo"></text>
              </view>
              <text class="menu-text">待发货</text>
            </view>
            <view class="menu-item" @click="goPage('/pages/users/order_list/index?status=2')">
              <view class="icon-box">
                <text class="iconfont icon-daishouhuo"></text>
                <view class="badge" v-if="orderData.receivedCount > 0">{{ orderData.receivedCount > 99 ? '99+' : orderData.receivedCount }}</view>
              </view>
              <text class="menu-text">已发货</text>
            </view>
            <view class="menu-item" @click="goPage('/pages/users/order_list/index')">
              <view class="icon-box">
                <text class="iconfont icon-daipingjia"></text>
              </view>
              <text class="menu-text">全部订单</text>
            </view>
          </view>
        </view>

        <!-- 服务与工具 -->
        <view class="service-card">
          <view class="card-header">
            <view class="header-left">
              <view class="header-line"></view>
              <text class="header-title">服务与工具</text>
            </view>
          </view>
          <view class="service-list">
            <view class="service-item" @click="goPage('/pages/users/user_address_list/index')">
              <view class="service-icon">
                <text class="iconfont icon-dizhi"></text>
              </view>
              <text class="service-text">收货地址</text>
              <text class="iconfont icon-xiangyou service-arrow"></text>
            </view>
            <view class="service-item" @click="goPage('/pages/users/user_sample/index')">
              <view class="service-icon">
                <text class="iconfont icon-shangpin"></text>
              </view>
              <text class="service-text">我的样品</text>
              <text class="iconfont icon-xiangyou service-arrow"></text>
            </view>
            <view class="service-item" @click="goKefu">
              <view class="service-icon">
                <text class="iconfont icon-kefu"></text>
              </view>
              <text class="service-text">专属客服</text>
              <text class="service-info">工号: L802</text>
              <text class="iconfont icon-xiangyou service-arrow"></text>
            </view>
            <view class="service-item" @click="goPage('/pages/users/user_feedback/index')">
              <view class="service-icon">
                <text class="iconfont icon-shuoming"></text>
              </view>
              <text class="service-text">在线反馈</text>
              <text class="iconfont icon-xiangyou service-arrow"></text>
            </view>
            <view class="service-item" @click="goPage('/pages/users/web_page/index?webUrl=https://www.liaolan.com/about&title=关于我们')">
              <view class="service-icon">
                <text class="iconfont icon-gengduo"></text>
              </view>
              <text class="service-text">关于我们</text>
              <text class="iconfont icon-xiangyou service-arrow"></text>
            </view>
          </view>
        </view>

        <!-- 退出登录 -->
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </view>
    </view>

    <!-- 底部导航 -->
    <tab-bar></tab-bar>
  </view>
</template>

<script>
import { mapGetters } from "vuex";
import { getMenuList } from '@/api/user.js';
import { orderData } from '@/api/order.js';
import tabBar from '@/components/tab-bar/index.vue';

export default {
  components: {
    tabBar
  },
  computed: {
    ...mapGetters(['isLogin', 'uid'])
  },
  data() {
    return {
      theme: '',
      defaultAvatar: 'https://img.crmeb.com/crmebimage/perset/staticImg/f.png',
      userInfo: {
        nickname: '张建国',
        company: '沈阳机电设备制造有限公司',
        avatar: '',
        isVip: true,
        favoriteCount: 12,
        couponCount: 5,
        integral: 1250
      },
      orderData: {
        unPaidCount: 2,
        unShippedCount: 0,
        receivedCount: 5,
        evaluatedCount: 0,
        refundCount: 0
      },
      MyMenus: []
    }
  },
  onLoad() {
    this.theme = this.$Cache.get('theme') || 'theme4';
    this.getUserInfo();
    this.getOrderData();
    this.getMyMenus();
  },
  onShow() {
    if (this.isLogin) {
      this.getUserInfo();
      this.getOrderData();
    }
  },
  methods: {
    formatNumber(num) {
      if (num >= 1000) {
        return (num / 1000).toFixed(1).replace(/\.0$/, '') + ',000';
      }
      return num.toString();
    },
    getUserInfo() {
      if (this.uid) {
        this.$store.dispatch('USERINFO').then(res => {
          if (res) {
            this.userInfo = {
              ...this.userInfo,
              ...res,
              isVip: res.vip || false
            };
          }
        });
      }
    },
    getOrderData() {
      if (!this.uid) return;
      orderData().then(res => {
        this.orderData = {
          unPaidCount: res.data.unPaidCount || 0,
          unShippedCount: res.data.unShippedCount || 0,
          receivedCount: res.data.receivedCount || 0,
          evaluatedCount: res.data.evaluatedCount || 0,
          refundCount: res.data.refundCount || 0
        };
      }).catch(err => {
        console.log('获取订单数据失败', err);
      });
    },
    getMyMenus() {
      getMenuList().then(res => {
        this.MyMenus = res.data.routine_my_menus || [];
      }).catch(err => {
        console.log('获取菜单失败', err);
      });
    },
    goPage(url) {
      if (!this.isLogin) {
        this.goLogin();
        return;
      }
      uni.navigateTo({
        url: url
      });
    },
    goSettings() {
      if (!this.isLogin) {
        this.goLogin();
        return;
      }
      uni.navigateTo({
        url: '/pages/infos/user_info/index'
      });
    },
    goKefu() {
      // #ifdef APP-PLUS || MP
      uni.navigateTo({
        url: '/pages/users/kefu/index'
      });
      // #endif
      // #ifdef H5
      if (this.chatUrl) {
        location.href = this.chatUrl;
      }
      // #endif
    },
    goLogin() {
      uni.navigateTo({
        url: '/pages/users/login/index'
      });
    },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            this.$store.commit("LOGOUT");
            this.$store.commit('UPDATE_LOGIN', '');
            this.$store.commit('UPDATE_USERINFO', {});
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            });
            setTimeout(() => {
              uni.navigateTo({
                url: '/pages/users/login/index'
              });
            }, 1500);
          }
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.liaolan-me {
  min-height: 100vh;
  background-color: #f9f9ff;
  padding-bottom: 120rpx;
}

.page-content {
  min-height: 100vh;
}

/* 用户信息头部 */
.user-header {
  position: relative;
  background: linear-gradient(135deg, #003da6 0%, #0052d9 100%);
  padding: 120rpx 32rpx 48rpx 32rpx;
  overflow: hidden;
}

.header-bg {
  position: absolute;
  top: 0;
  right: 0;
  width: 400rpx;
  height: 400rpx;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  transform: translateY(-50%);
}

.header-content {
  position: relative;
  z-index: 10;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar-box {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  background: rgba(255, 255, 255, 0.1);
}

.avatar {
  width: 100%;
  height: 100%;
}

.user-detail {
  flex: 1;
  margin-left: 24rpx;
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 48rpx;
  font-weight: 600;
  color: #ffffff;
  line-height: 1.2;
}

.company {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
}

.vip-badge {
  display: inline-flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  border-radius: 24rpx;
  padding: 8rpx 24rpx;
  margin-top: 16rpx;
  width: fit-content;
}

.vip-icon {
  color: #ffb86f;
  font-size: 24rpx;
  margin-right: 8rpx;
}

.vip-text {
  font-size: 20rpx;
  color: #ffffff;
}

.settings-btn {
  width: 64rpx;
  height: 64rpx;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.settings-btn .iconfont {
  font-size: 32rpx;
  color: #ffffff;
}

/* 快速统计 */
.quick-stats {
  display: flex;
  margin-top: 48rpx;
  padding-top: 32rpx;
  border-top: 1rpx solid rgba(255, 255, 255, 0.1);
}

.stat-item {
  flex: 1;
  text-align: center;
  padding: 16rpx 0;
}

.stat-num {
  font-size: 40rpx;
  font-weight: 600;
  color: #ffffff;
}

.stat-label {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
  display: block;
}

/* 页面主体 */
.page-body {
  padding: 32rpx;
}

/* 订单卡片 */
.order-card {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.02);
  border: 1rpx solid #dfe2ed;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 24rpx;
  border-bottom: 1rpx solid rgba(115, 118, 134, 0.3);
}

.header-left {
  display: flex;
  align-items: center;
}

.header-line {
  width: 4rpx;
  height: 32rpx;
  background: #003da6;
  border-radius: 2rpx;
  margin-right: 16rpx;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #181c23;
}

.header-right {
  display: flex;
  align-items: center;
}

.view-all {
  font-size: 24rpx;
  color: #737686;
}

.header-right .iconfont {
  font-size: 24rpx;
  color: #737686;
  margin-left: 4rpx;
}

.order-menu {
  display: flex;
  justify-content: space-between;
  margin-top: 24rpx;
}

.menu-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.icon-box {
  position: relative;
  width: 96rpx;
  height: 96rpx;
  background: #f1f3ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.icon-box .iconfont {
  font-size: 48rpx;
  color: #003da6;
}

.badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  min-width: 32rpx;
  height: 32rpx;
  background: #ba1a1a;
  color: #ffffff;
  font-size: 16rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
  border: 2rpx solid #ffffff;
}

.menu-text {
  font-size: 24rpx;
  color: #434654;
}

/* 服务卡片 */
.service-card {
  background: #ffffff;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.02);
  border: 1rpx solid #dfe2ed;
  margin-bottom: 32rpx;
}

.service-list {
  padding: 0 32rpx;
}

.service-item {
  display: flex;
  align-items: center;
  padding: 32rpx 0;
  border-bottom: 1rpx solid rgba(115, 118, 134, 0.1);
}

.service-item:last-child {
  border-bottom: none;
}

.service-icon {
  width: 64rpx;
  height: 64rpx;
  background: rgba(180, 197, 255, 0.2);
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.service-icon .iconfont {
  font-size: 32rpx;
  color: #003da6;
}

.service-text {
  flex: 1;
  font-size: 28rpx;
  color: #181c23;
}

.service-info {
  font-size: 24rpx;
  color: #737686;
  margin-right: 8rpx;
}

.service-arrow {
  font-size: 28rpx;
  color: #c3c6d7;
}

/* 退出按钮 */
.logout-btn {
  width: 100%;
  height: 96rpx;
  background: #ffffff;
  border: 1rpx solid rgba(115, 118, 134, 0.3);
  border-radius: 16rpx;
  font-size: 32rpx;
  color: #ba1a1a;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 32rpx;
}
</style>
