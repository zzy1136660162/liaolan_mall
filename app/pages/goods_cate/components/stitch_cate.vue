<template>
	<view class="mall-page" :style="{height: winHeight + 'px'}">
		<!-- 主区域：侧边栏 + 内容区 -->
		<view class="mall-body">
			<!-- 左侧分类导航 -->
			<scroll-view class="sidebar" scroll-y>
				<view class="sidebar-list">
					<view
						v-for="(item, index) in level1List"
						:key="item.id"
						class="sidebar-item"
						:class="{ active: activeLevel1 === index }"
						@click="selectLevel1(index)"
					>
						<view class="sidebar-icon-box" :class="{ 'icon-active': activeLevel1 === index }">
							<image v-if="item._svgIcon" class="sidebar-svg-icon" :src="item._svgIcon" mode="aspectFit"></image>
							<text v-else class="iconfont" :class="item._icon"></text>
						</view>
						<text class="sidebar-label" :class="{ 'label-active': activeLevel1 === index }">{{ item.name }}</text>
					</view>
				</view>
			</scroll-view>

			<!-- 右侧内容区 -->
			<scroll-view class="content-area" scroll-y>
				<view v-if="currentLevel1" class="content-inner">
					<!-- 分类横幅 -->
					<view class="category-banner">
						<image v-if="currentLevel1.extra" class="banner-bg" :src="currentLevel1.extra" mode="aspectFill"></image>
						<view class="banner-overlay"></view>
						<view class="banner-info">
							<h2 class="banner-title">{{ currentLevel1.name }}</h2>
							<p class="banner-desc">{{ currentLevel1.description || '工业级安全标准 · 纯铜核心' }}</p>
						</view>
					</view>

					<!-- 子分类卡片网格 -->
					<view class="card-grid">
						<view
							v-for="item in currentLevel2List"
							:key="item.id"
							class="card-item"
							@click="goProductList(item)"
						>
							<view class="card-header">
								<text class="card-tag">{{ getTagLabel(item.name) }}</text>
								<text class="iconfont icon-xiangyou card-arrow"></text>
							</view>
							<view class="card-image-box">
								<image v-if="item.extra" class="card-image" :src="item.extra" mode="aspectFill"></image>
								<view v-else class="card-image-placeholder">
									<text class="iconfont icon-shangpin"></text>
								</view>
							</view>
							<h3 class="card-name">{{ item.name }}</h3>
							<p class="card-desc">{{ item.description || '专业工业级电缆解决方案' }}</p>
						</view>
					</view>
					<view class="content-spacer"></view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
import { getCategoryList } from '@/api/store.js';

// 侧边栏图标映射（null 表示使用 SVG）
const CATEGORY_SVG_ICONS = ['/static/icon/power.svg', null, null, null, null];
const CATEGORY_ICONS = ['', 'icon-shezhi', 'icon-tongji', 'icon-remen', 'icon-gongneng'];

export default {
	data() {
		return {
			winHeight: 0,
			categoryTree: [],
			activeLevel1: 0
		}
	},
	computed: {
		level1List() {
			// 为每个一级分类预分配图标
			return (this.categoryTree || []).map((item, index) => ({
				...item,
				_icon: CATEGORY_ICONS[index] || '',
				_svgIcon: CATEGORY_SVG_ICONS[index] || ''
			}));
		},
		currentLevel1() {
			if (this.level1List.length === 0) return null;
			return this.level1List[this.activeLevel1] || null;
		},
		currentLevel2List() {
			if (!this.currentLevel1) return [];
			return this.currentLevel1.child || [];
		}
	},
	created() {
		uni.getSystemInfo({
			success: (res) => {
				this.winHeight = res.windowHeight;
			}
		});
		this.loadCategoryData();
	},
	onShow() {
		this.loadCategoryData();
	},
	methods: {
		loadCategoryData() {
			getCategoryList().then(res => {
				this.categoryTree = res.data || [];
			});
		},
		selectLevel1(index) {
			if (this.activeLevel1 === index) return;
			this.activeLevel1 = index;
		},
		// 根据分类名生成英文标签
		getTagLabel(name) {
			if (!name) return 'CABLE';
			const map = {
				'高压': 'HIGH VOLTAGE',
				'低压': 'LOW VOLTAGE',
				'电力': 'POWER',
				'控制': 'CONTROL',
				'通信': 'COMM',
				'特种': 'SPECIAL',
				'阻燃': 'FIRE RESISTANT',
				'耐火': 'FIREPROOF',
				'屏蔽': 'SHIELDED',
				'铠装': 'ARMORED',
				'光伏': 'PV SOLAR',
				'光纤': 'FIBER OPTIC',
				'船用': 'MARINE',
				'矿用': 'MINING'
			};
			for (const [key, val] of Object.entries(map)) {
				if (name.includes(key)) return val;
			}
			return 'CABLE';
		},
		goProductList(item) {
			uni.navigateTo({
				url: '/pages/goods/goods_list/index?cid=' + item.id + '&title=' + encodeURIComponent(item.name || '')
			});
		}
	}
}
</script>

<style scoped lang="scss">
// ========== 主题色 ==========
$primary: #0061a5;
$on-primary: #ffffff;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$surface: #f9f9fc;
$surface-container-low: #f3f3f6;
$outline-variant: #bfc7d5;
$tertiary: #006875;

// ========== 页面容器 ==========
.mall-page {
	display: flex;
	flex-direction: column;
	background: $surface;
	overflow: hidden;
}

// ========== 主区域 ==========
.mall-body {
	flex: 1;
	display: flex;
	overflow: hidden;
}

// ========== 左侧导航 ==========
.sidebar {
	width: 192rpx;
	flex-shrink: 0;
	height: 100%;
	background: $surface-container-low;
	border-right: 1rpx solid rgba($outline-variant, 0.3);
}

.sidebar-list {
	display: flex;
	flex-direction: column;
	padding: 24rpx 0;
}

.sidebar-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 28rpx 16rpx;
	gap: 12rpx;
	transition: all 0.2s;
	color: $on-surface-variant;

	&.active {
		color: $primary;
		font-weight: 700;
	}

	&:active {
		opacity: 0.7;
	}
}

.sidebar-icon-box {
	width: 88rpx;
	height: 88rpx;
	border-radius: 24rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.2s;

	.iconfont {
		font-size: 40rpx;
		color: $on-surface-variant;
	}

	.sidebar-svg-icon {
		width: 44rpx;
		height: 44rpx;
	}

	&.icon-active {
		background: rgba($primary, 0.1);

		.iconfont {
			color: $primary;
		}
	}

	.sidebar-item:active & {
		transform: scale(0.9);
	}
}

.sidebar-label {
	font-size: 22rpx;
	font-weight: 500;
	letter-spacing: 1rpx;
	color: $on-surface-variant;

	&.label-active {
		color: $primary;
	}
}

// ========== 右侧内容区 ==========
.content-area {
	flex: 1;
	height: 100%;
	min-width: 0;
	overflow: hidden;
}

.content-inner {
	padding: 0 24rpx;
	box-sizing: border-box;
	width: 100%;
}

// ========== 分类横幅 ==========
.category-banner {
	position: relative;
	height: 256rpx;
	border-radius: 32rpx;
	overflow: hidden;
	margin-bottom: 32rpx;

	.banner-bg {
		position: absolute;
		width: 100%;
		height: 100%;
	}

	.banner-overlay {
		position: absolute;
		inset: 0;
		background: linear-gradient(135deg, rgba($primary, 0.45) 0%, transparent 70%);
	}

	.banner-info {
		position: relative;
		z-index: 1;
		height: 100%;
		display: flex;
		flex-direction: column;
		justify-content: center;
		padding: 32rpx 40rpx;
	}

	.banner-title {
		font-size: 44rpx;
		font-weight: 800;
		color: $on-primary;
		margin-bottom: 12rpx;
		text-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
	}

	.banner-desc {
		font-size: 24rpx;
		color: rgba($on-primary, 0.9);
		line-height: 1.5;
	}
}

// ========== 子分类卡片网格 ==========
.card-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 24rpx;
}

.card-item {
	background: #ffffff;
	border: 1rpx solid rgba($outline-variant, 0.3);
	border-radius: 24rpx;
	padding: 24rpx;
	transition: all 0.3s;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.03);
	overflow: hidden;
	min-width: 0;

	&:active {
		border-color: $primary;
		transform: translateY(-2rpx);
		box-shadow: 0 12rpx 40rpx rgba($primary, 0.12);
	}
}

.card-header {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	margin-bottom: 16rpx;
}

.card-tag {
	font-size: 18rpx;
	font-weight: 500;
	color: $tertiary;
	background: rgba($tertiary, 0.1);
	padding: 4rpx 12rpx;
	border-radius: 999rpx;
	letter-spacing: 1rpx;
}

.card-arrow {
	font-size: 28rpx;
	color: $outline-variant;
	transition: color 0.3s;

	.card-item:active & {
		color: $primary;
	}
}

.card-image-box {
	width: 100%;
	height: 160rpx;
	background: $surface-container-low;
	border-radius: 16rpx;
	overflow: hidden;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 16rpx;
}

.card-image {
	width: 100%;
	height: 100%;
}

.card-image-placeholder {
	.iconfont {
		font-size: 64rpx;
		color: rgba($primary, 0.25);
	}
}

.card-name {
	font-size: 28rpx;
	font-weight: 600;
	color: $on-surface;
	margin-bottom: 8rpx;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.card-desc {
	font-size: 22rpx;
	color: $on-surface-variant;
	line-height: 1.4;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.content-spacer {
	height: 60rpx;
}
</style>
