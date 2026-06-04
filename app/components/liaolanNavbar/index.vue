<template>
	<view class="liaolan-navbar" :style="navbarStyle">
		<view class="navbar-content" :style="{ paddingTop: statusBarHeight }">
			<view class="navbar-left" @click="handleLeftClick">
				<text class="iconfont icon-caidan"></text>
			</view>
			<view class="navbar-title">{{ title }}</view>
			<view class="navbar-right" @click="handleRightClick">
				<text class="iconfont icon-ic_search"></text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'LiaolanNavbar',
	props: {
		title: {
			type: String,
			default: '杈界紗'
		}
	},
	data() {
		return {
			statusBarHeight: '20px'
		};
	},
	computed: {
		navbarStyle() {
			return {
				paddingTop: this.statusBarHeight
			};
		}
	},
	created() {
		this.getSystemInfo();
	},
	methods: {
		getSystemInfo() {
			const systemInfo = uni.getSystemInfoSync();
			this.statusBarHeight = systemInfo.statusBarHeight + 'px';
		},
		handleLeftClick() {
			this.$emit('leftClick');
		},
		handleRightClick() {
			this.$emit('rightClick');
		}
	}
};
</script>

<style scoped lang="scss">
$primary-color: #0f5de3;
$primary-container: #0f5de3;
$surface-container-lowest: #ffffff;
$outline-variant: #c3c6d7;

.liaolan-navbar {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 1000;
	background-color: $surface-container-lowest;
	border-bottom: 1px solid $outline-variant;
}

.navbar-content {
	display: flex;
	justify-content: space-between;
	align-items: center;
	height: 148rpx;
	padding: 0 32rpx;
}

.navbar-left,
.navbar-right {
	width: 64rpx;
	height: 64rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: 50%;
	transition: background-color 0.3s;

	&:active {
		background-color: rgba($primary-color, 0.1);
	}

	.iconfont {
		font-size: 48rpx;
		color: $primary-color;
	}
}

.navbar-title {
	font-size: 32rpx;
	font-weight: 600;
	color: $primary-color;
}
</style>
