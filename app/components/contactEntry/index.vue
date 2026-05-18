<template>
  <view class="contact-entry">
    <view class="entry-item" v-for="item in actions" :key="item.key" @click="$emit('select', item.key)">
      <view class="entry-inner">
        <view class="icon-wrap">
          <text class="iconfont" :class="item.icon"></text>
        </view>
        <view class="entry-label">{{ item.label }}</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'ContactEntry',
  props: {
    actions: {
      type: Array,
      default: () => [],
    },
  },
};
</script>

<style scoped lang="scss">
.contact-entry {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10rpx;
}

.entry-item {
  width: 25%;
  padding: 0 10rpx;
  box-sizing: border-box;
}

.entry-inner {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx 12rpx 20rpx;
  text-align: center;
}

.icon-wrap {
  width: 84rpx;
  height: 84rpx;
  margin: 0 auto 12rpx;
  border-radius: 42rpx;
  background: #f4f7fb;
  display: flex;
  align-items: center;
  justify-content: center;
}

.iconfont {
  font-size: 40rpx;
  color: #2d3a4d;
}

.entry-label {
  font-size: 24rpx;
  color: #303133;
  line-height: 32rpx;
}
</style>
