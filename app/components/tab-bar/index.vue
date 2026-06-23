<template>
	<view class="tab-bar-wrapper">
		<view class="tab-bar">
			<view
				v-for="(item, index) in tabList"
				:key="index"
				class="tab-item"
				:class="{ 'tab-item-active': currentPath === item.pagePath }"
				@tap="switchTab(item)"
			>
				<text class="iconfont" :class="item.icon"></text>
				<text class="tab-text">{{ item.text }}</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'tab-bar',
	data() {
		return {
			currentPath: '',
			tabList: [
				{
					text: '首页',
					pagePath: '/pages/liaolan_index/index',
					icon: 'icon-shouye1'
				},
				{
					text: '商城',
					pagePath: '/pages/goods_cate/goods_cate',
					icon: 'icon-gouwuche'
				},
				{
					text: '案例',
					pagePath: '/pages/case/case_list/index',
					icon: 'icon-fenlei'
				},
				{
					text: '我的',
					pagePath: '/pages/liaolan_me/index',
					icon: 'icon-yonghu'
				}
			]
		};
	},
	watch: {
		currentPath(val) {
			this.setTabBarActive(val);
		}
	},
	mounted() {
		setTimeout(() => {
			this.updateCurrentPath();
		}, 100);
	},
	onShow() {
		this.updateCurrentPath();
	},
	methods: {
		updateCurrentPath() {
			const pages = getCurrentPages();
			if (pages && pages.length > 0) {
				const currentPage = pages[pages.length - 1];
				this.currentPath = '/' + currentPage.route;
			}
		},
		switchTab(item) {
			if (this.currentPath === item.pagePath) return;
			uni.switchTab({
				url: item.pagePath,
				fail: () => {
					uni.navigateTo({ url: item.pagePath });
				}
			});
		},
		setTabBarActive(path) {}
	}
};
</script>

<style>
.tab-bar-wrapper {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	z-index: 1000;
	background: rgba(249, 249, 252, 0.8);
	backdrop-filter: blur(40rpx);
	-webkit-backdrop-filter: blur(40rpx);
	box-shadow: 0 -20rpx 40rpx -10rpx rgba(0, 153, 255, 0.15);
}

.tab-bar {
	display: flex;
	justify-content: space-around;
	align-items: center;
	height: 160rpx;
	padding: 0 24rpx;
	padding-bottom: env(safe-area-inset-bottom);
}

.tab-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	gap: 4rpx;
	padding: 8rpx 32rpx;
	border-radius: 24rpx;
	transition: all 0.2s;
}

.tab-item:active {
	transform: scale(0.9);
}

.tab-item .iconfont {
	font-size: 44rpx;
	color: #3f4753;
}

.tab-item .tab-text {
	font-size: 22rpx;
	font-weight: 500;
	color: #3f4753;
	letter-spacing: 1rpx;
}

.tab-item-active {
	background: rgba(0, 97, 165, 0.1);
}

.tab-item-active .iconfont {
	color: #0061a5;
}

.tab-item-active .tab-text {
	color: #0061a5;
}
</style>
