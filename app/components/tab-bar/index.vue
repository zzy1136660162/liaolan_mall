<template>
	<view class="tab-bar-wrapper">
		<view class="tab-bar safe-area-bottom">
			<view
				v-for="(item, index) in tabList"
				:key="index"
				class="tab-item"
				:class="{ 'tab-item-active': currentPath === item.pagePath }"
				@tap="switchTab(item)"
			>
				<image
					class="tab-icon"
					:src="currentPath === item.pagePath ? item.selectedIconPath : item.iconPath"
					mode="aspectFit"
				/>
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
					iconPath: '/static/tabBar/index.png',
					selectedIconPath: '/static/tabBar/indexd.png'
				},
				{
					text: '商城',
					pagePath: '/pages/goods_cate/goods_cate',
					iconPath: '/static/tabBar/mall.png',
					selectedIconPath: '/static/tabBar/malld.png'
				},
				{
					text: '案例',
					pagePath: '/pages/case/case_list/index',
					iconPath: '/static/tabBar/case.png',
					selectedIconPath: '/static/tabBar/cased.png'
				},
				{
					text: '我的',
					pagePath: '/pages/liaolan_me/index',
					iconPath: '/static/tabBar/my.png',
					selectedIconPath: '/static/tabBar/myd.png'
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
	background-color: #ffffff;
}

.tab-bar {
	display: flex;
	justify-content: space-around;
	align-items: center;
	height: 150rpx;
	padding-bottom: env(safe-area-inset-bottom);
	background-color: #ffffff;
	border-top: 1rpx solid #e5e5e5;
}

.tab-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 11rpx 19rpx;
	border-radius: 20rpx;
	transition: all 0.3s ease;
	background-color: transparent;
}

.tab-item-active {
	background-color: #0f5de3;
}

.tab-icon {
	width: 48rpx;
	height: 48rpx;
	margin-bottom: 4rpx;
}

.tab-text {
	font-size: 22rpx;
	font-weight: 400;
	color: #666666 !important;
	line-height: 1;
}

.tab-item-active .tab-text {
	color: #ffffff !important;
}

.safe-area-bottom {
	padding-bottom: constant(safe-area-inset-bottom);
	padding-bottom: env(safe-area-inset-bottom);
}
</style>
