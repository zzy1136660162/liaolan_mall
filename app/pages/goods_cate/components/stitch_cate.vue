<template>
	<view class="category-page" :style="{height: winHeight + 'px'}">
		<!-- 背景层 -->
		<view class="bg-mesh"></view>
		<view class="bg-blob bg-blob-1"></view>
		<view class="bg-blob bg-blob-2"></view>

		<!-- 搜索栏 -->
		<view class="search-bar">
			<view class="search-input" @click="goSearch">
				<text class="search-icon">&#xe60c;</text>
				<text class="search-placeholder">搜索电缆型号、分类</text>
				<view class="search-action">
					<text>搜索</text>
				</view>
			</view>
		</view>

		<!-- 主体内容 -->
		<view class="category-body">
			<!-- 左侧分类导航 -->
			<scroll-view class="sidebar" scroll-y :scroll-into-view="'cat-' + activeLevel1Index" scroll-with-animation>
				<view class="sidebar-nav">
					<view
						v-for="(item, index) in level1List"
						:key="item.id"
						:id="'cat-' + index"
						class="sidebar-item"
						:class="{ active: activeLevel1Index === index }"
						@click="selectLevel1(index)"
					>
						<view class="sidebar-text" :class="{ 'sidebar-text-active': activeLevel1Index === index }">{{ item.name }}</view>
						<view v-if="activeLevel1Index === index" class="sidebar-indicator"></view>
					</view>
				</view>
			</scroll-view>

			<!-- 右侧内容区 -->
			<scroll-view class="content-area" scroll-y scroll-with-animation>
				<view v-if="currentLevel1" class="content-inner">
					<!-- 顶部品牌横幅 -->
					<view class="level1-banner">
						<view class="level1-banner-deco level1-banner-deco-1"></view>
						<view class="level1-banner-deco level1-banner-deco-2"></view>
						<view class="level1-banner-deco level1-banner-deco-3"></view>
						<view class="level1-banner-content">
							<view class="level1-banner-tag">
								<text>CATEGORY</text>
							</view>
							<text class="level1-banner-title">{{ currentLevel1.name }}</text>
							<text v-if="currentLevel1.description" class="level1-banner-desc">{{ currentLevel1.description }}</text>
						</view>
					</view>

					<!-- 二级分类 -->
					<view v-for="(level2, l2Idx) in currentLevel1.child" :key="level2.id" class="level2-section">
						<view class="level2-header" @click="toggleLevel2(l2Idx)">
							<view class="level2-header-left">
								<view class="level2-bar"></view>
								<view class="level2-header-text">
									<text class="level2-title">{{ level2.name }}</text>
									<text v-if="level2.description" class="level2-desc">{{ level2.description }}</text>
								</view>
							</view>
							<view class="level2-arrow-wrap" :class="{ 'arrow-expanded': expandedLevel2[l2Idx] }">
								<text class="level2-arrow-icon">∨</text>
							</view>
						</view>
						<view class="level2-body" :class="{ 'body-expanded': expandedLevel2[l2Idx] }">
							<view v-for="level3 in level2.child" :key="level3.id" class="level3-group">
								<view class="level3-header" @click="toggleLevel3(level3.id)">
									<view class="level3-header-left">
										<view class="level3-dot"></view>
										<text class="level3-title">{{ level3.name }}</text>
									</view>
									<view class="level3-arrow-wrap" :class="{ 'arrow-expanded': expandedLevel3[level3.id] }">
										<text class="level3-arrow-icon">∨</text>
									</view>
								</view>
								<view class="level3-body" :class="{ 'body-expanded': expandedLevel3[level3.id] }">
									<view class="level4-tags">
										<view
											v-for="level4 in level3.child"
											:key="level4.id"
											class="level4-tag"
											@click="goProductList(level4)"
										>
											<view class="level4-tag-icon">
												<image v-if="level4.extra" :src="level4.extra" class="level4-tag-img" mode="aspectFill"></image>
												<text v-else class="level4-tag-icon-text">{{ level4.name.charAt(0) }}</text>
											</view>
											<view class="level4-tag-info">
												<text class="level4-tag-name">{{ level4.name }}</text>
												<text v-if="level4.description" class="level4-tag-desc">{{ level4.description }}</text>
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>
					<view class="content-bottom-spacer"></view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	import { getCategoryList } from '@/api/store.js';
	export default {
		data() {
			return {
				winHeight: 0,
				categoryTree: [],
				activeLevel1Index: 0,
				expandedLevel2: {},
				expandedLevel3: {}
			}
		},
		computed: {
			level1List() {
				return this.categoryTree || [];
			},
			currentLevel1() {
				if (this.level1List.length === 0) return null;
				return this.level1List[this.activeLevel1Index] || null;
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
					if (this.categoryTree.length > 0) {
						this.$set(this.expandedLevel2, 0, true);
						if (this.categoryTree[0].child && this.categoryTree[0].child.length > 0) {
							let firstL2 = this.categoryTree[0].child[0];
							if (firstL2.child && firstL2.child.length > 0) {
								this.$set(this.expandedLevel3, firstL2.child[0].id, true);
							}
						}
					}
				});
			},
			selectLevel1(index) {
				if (this.activeLevel1Index === index) return;
				this.activeLevel1Index = index;
				this.expandedLevel2 = {};
				this.expandedLevel3 = {};
				this.$set(this.expandedLevel2, 0, true);
				if (this.currentLevel1 && this.currentLevel1.child && this.currentLevel1.child.length > 0) {
					let firstL2 = this.currentLevel1.child[0];
					if (firstL2.child && firstL2.child.length > 0) {
						this.$set(this.expandedLevel3, firstL2.child[0].id, true);
					}
				}
			},
			toggleLevel2(l2Idx) {
				this.$set(this.expandedLevel2, l2Idx, !this.expandedLevel2[l2Idx]);
			},
			toggleLevel3(l3Id) {
				this.$set(this.expandedLevel3, l3Id, !this.expandedLevel3[l3Id]);
			},
			goProductList(item) {
				if (!item || !item.id) {
					console.error('分类数据异常');
					return;
				}
				uni.navigateTo({
					url: '/pages/goods/goods_list/index?cid=' + item.id + '&title=' + encodeURIComponent(item.name || '')
				});
			},
			goSearch() {
				uni.navigateTo({
					url: '/pages/goods/goods_list/index'
				});
			}
		}
	}
</script>

<style scoped lang="scss">
	// ========== 主题色：明亮蓝色系 ==========
	$primary: #0f5de3;
	$primary-light: #3a7bff;
	$primary-deep: #0947b8;
	$primary-soft: #eaf1ff;
	$primary-fade: #f4f8ff;
	$primary-glow: #6ea3ff;

	$on-surface: #0e1729;
	$on-surface-muted: #5b6478;
	$outline: #dfe5f2;
	$outline-light: #ecf0fa;

	$accent-cyan: #36c6ff;
	$accent-violet: #8a9bff;

	.category-page {
		position: relative;
		display: flex;
		flex-direction: column;
		background: linear-gradient(180deg, #eaf2ff 0%, #f4f8ff 35%, #ffffff 100%);
		overflow: hidden;
	}

	// 背景层
	.bg-mesh {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 500rpx;
		background:
			radial-gradient(ellipse 70% 50% at 30% 0%, rgba(58, 123, 255, 0.15) 0%, transparent 60%),
			radial-gradient(ellipse 60% 50% at 80% 20%, rgba(54, 198, 255, 0.12) 0%, transparent 60%);
		pointer-events: none;
		z-index: 0;
	}
	.bg-blob {
		position: absolute;
		border-radius: 50%;
		filter: blur(80rpx);
		pointer-events: none;
		z-index: 0;
	}
	.bg-blob-1 {
		width: 280rpx;
		height: 280rpx;
		top: 100rpx;
		right: -100rpx;
		background: radial-gradient(circle, rgba(58, 123, 255, 0.22) 0%, transparent 70%);
	}
	.bg-blob-2 {
		width: 260rpx;
		height: 260rpx;
		top: 400rpx;
		left: -120rpx;
		background: radial-gradient(circle, rgba(54, 198, 255, 0.18) 0%, transparent 70%);
	}

	// ========== 搜索栏 ==========
	.search-bar {
		position: relative;
		z-index: 2;
		padding: 20rpx 24rpx 16rpx;
		flex-shrink: 0;
	}

	.search-input {
		display: flex;
		align-items: center;
		height: 80rpx;
		background: rgba(255, 255, 255, 0.9);
		backdrop-filter: blur(20rpx);
		border-radius: 40rpx;
		padding: 0 12rpx 0 28rpx;
		box-shadow:
			0 8rpx 24rpx rgba(15, 93, 227, 0.08),
			0 2rpx 6rpx rgba(15, 93, 227, 0.04);
		border: 1rpx solid rgba(255, 255, 255, 0.8);
	}

	.search-icon {
		font-size: 30rpx;
		color: $primary;
		margin-right: 14rpx;
		font-weight: 600;
	}

	.search-placeholder {
		flex: 1;
		font-size: 26rpx;
		color: #98a2b8;
		letter-spacing: 0.5rpx;
	}

	.search-action {
		height: 60rpx;
		padding: 0 24rpx;
		background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 6rpx 14rpx rgba(15, 93, 227, 0.28);

		text {
			font-size: 24rpx;
			color: #ffffff;
			font-weight: 600;
			letter-spacing: 2rpx;
		}
	}

	// ========== 主体内容 ==========
	.category-body {
		position: relative;
		z-index: 1;
		flex: 1;
		display: flex;
		overflow: hidden;
	}

	// ========== 左侧导航 ==========
	.sidebar {
		width: 168rpx;
		flex-shrink: 0;
		height: 100%;
		padding: 8rpx 6rpx 8rpx 8rpx;
	}

	.sidebar-nav {
		background: rgba(255, 255, 255, 0.7);
		backdrop-filter: blur(20rpx);
		border-radius: 24rpx;
		padding: 8rpx 0;
		box-shadow:
			0 8rpx 24rpx rgba(15, 93, 227, 0.06),
			0 2rpx 6rpx rgba(15, 93, 227, 0.03);
		border: 1rpx solid rgba(255, 255, 255, 0.8);
		overflow: hidden;
	}

	.sidebar-item {
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 22rpx 4rpx;
		margin: 4rpx 8rpx;
		border-radius: 16rpx;
		transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
		overflow: hidden;
	}

	.sidebar-item.active {
		background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
		box-shadow:
			0 8rpx 20rpx rgba(15, 93, 227, 0.32),
			inset 0 0 0 1rpx rgba(255, 255, 255, 0.2);
		transform: scale(1.02);
	}

	.sidebar-text {
		position: relative;
		z-index: 2;
		font-size: 24rpx;
		color: $on-surface-muted;
		text-align: center;
		line-height: 1.35;
		letter-spacing: 0.5rpx;
		font-weight: 500;
		transition: all 0.3s;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		word-break: break-all;
	}

	.sidebar-text-active {
		color: #ffffff;
		font-weight: 700;
		font-size: 26rpx;
		letter-spacing: 0.5rpx;
	}

	.sidebar-indicator {
		position: absolute;
		right: -6rpx;
		top: 50%;
		transform: translateY(-50%);
		width: 0;
		height: 0;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
		border-right: 10rpx solid $primary-light;
		filter: drop-shadow(0 0 6rpx rgba(15, 93, 227, 0.4));
	}

	// ========== 右侧内容区 ==========
	.content-area {
		flex: 1;
		height: 100%;
		min-width: 0;
		overflow: hidden;
	}

	.content-inner {
		padding: 4rpx 16rpx 0;
		overflow: hidden;
	}

	// ========== 顶部品牌横幅 ==========
	.level1-banner {
		position: relative;
		border-radius: 24rpx;
		overflow: hidden;
		margin-bottom: 20rpx;
		height: 180rpx;
		background: linear-gradient(135deg, $primary 0%, $primary-light 60%, $accent-cyan 100%);
		box-shadow:
			0 12rpx 28rpx rgba(15, 93, 227, 0.22),
			0 4rpx 10rpx rgba(15, 93, 227, 0.08);
	}

	.level1-banner-deco {
		position: absolute;
		border-radius: 50%;
		pointer-events: none;
	}
	.level1-banner-deco-1 {
		width: 200rpx;
		height: 200rpx;
		top: -70rpx;
		right: -40rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.25) 0%, transparent 70%);
		filter: blur(30rpx);
	}
	.level1-banner-deco-2 {
		width: 140rpx;
		height: 140rpx;
		bottom: -40rpx;
		left: -30rpx;
		background: radial-gradient(circle, rgba(54, 198, 255, 0.4) 0%, transparent 70%);
		filter: blur(30rpx);
	}
	.level1-banner-deco-3 {
		width: 70rpx;
		height: 70rpx;
		top: 24rpx;
		right: 80rpx;
		background: rgba(255, 255, 255, 0.18);
		filter: blur(8rpx);
	}

	.level1-banner-content {
		position: relative;
		z-index: 1;
		padding: 18rpx 24rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		height: 100%;
		overflow: hidden;
	}

	.level1-banner-tag {
		display: inline-block;
		align-self: flex-start;
		padding: 3rpx 12rpx;
		background: rgba(255, 255, 255, 0.2);
		border: 1rpx solid rgba(255, 255, 255, 0.35);
		border-radius: 999rpx;
		margin-bottom: 6rpx;
		backdrop-filter: blur(10rpx);

		text {
			font-size: 18rpx;
			color: #ffffff;
			font-weight: 700;
			letter-spacing: 2rpx;
			line-height: 1.2;
		}
	}

	.level1-banner-title {
		font-size: 32rpx;
		font-weight: 700;
		color: #ffffff;
		margin-bottom: 4rpx;
		letter-spacing: 1rpx;
		line-height: 1.2;
		text-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.12);
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.level1-banner-desc {
		font-size: 20rpx;
		color: rgba(255, 255, 255, 0.92);
		line-height: 1.4;
		letter-spacing: 0.5rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		word-break: break-all;
	}

	// ========== 二级分类 ==========
	.level2-section {
		margin-bottom: 16rpx;
		background: rgba(255, 255, 255, 0.92);
		backdrop-filter: blur(10rpx);
		border-radius: 20rpx;
		overflow: hidden;
		box-shadow:
			0 8rpx 24rpx rgba(15, 93, 227, 0.06),
			0 2rpx 6rpx rgba(15, 93, 227, 0.03);
		border: 1rpx solid rgba(255, 255, 255, 0.9);
	}

	.level2-header {
		padding: 20rpx 20rpx;
		background: linear-gradient(135deg, rgba(15, 93, 227, 0.04) 0%, rgba(54, 198, 255, 0.04) 100%);
		display: flex;
		align-items: center;
		justify-content: space-between;
		border-bottom: 1rpx solid transparent;
		transition: all 0.3s;

		&.active,
		&:active {
			background: linear-gradient(135deg, rgba(15, 93, 227, 0.08) 0%, rgba(54, 198, 255, 0.06) 100%);
		}
	}

	.level2-header-left {
		display: flex;
		align-items: center;
		flex: 1;
		min-width: 0;
	}

	.level2-bar {
		width: 6rpx;
		height: 28rpx;
		background: linear-gradient(180deg, $primary 0%, $accent-cyan 100%);
		border-radius: 6rpx;
		margin-right: 12rpx;
		flex-shrink: 0;
		box-shadow: 0 2rpx 6rpx rgba(15, 93, 227, 0.3);
	}

	.level2-header-text {
		display: flex;
		flex-direction: column;
		flex: 1;
		min-width: 0;
		overflow: hidden;
	}

	.level2-title {
		font-size: 28rpx;
		font-weight: 700;
		color: $on-surface;
		line-height: 1.4;
		letter-spacing: 0.5rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.level2-desc {
		font-size: 20rpx;
		color: $on-surface-muted;
		margin-top: 2rpx;
		line-height: 1.4;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.level2-arrow-wrap {
		width: 40rpx;
		height: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 50%;
		background: rgba(15, 93, 227, 0.1);
		transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
		flex-shrink: 0;
		margin-left: 12rpx;
	}

	.level2-arrow-wrap.arrow-expanded {
		transform: rotate(180deg);
		background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
		box-shadow: 0 6rpx 14rpx rgba(15, 93, 227, 0.3);
	}

	.level2-arrow-icon {
		font-size: 22rpx;
		color: $primary;
		font-weight: 700;
		transition: color 0.3s;
	}

	.level2-arrow-wrap.arrow-expanded .level2-arrow-icon {
		color: #ffffff;
	}

	.level2-body {
		max-height: 0;
		overflow: hidden;
		transition: max-height 0.4s cubic-bezier(0.4, 0, 0.2, 1);
	}

	.level2-body.body-expanded {
		max-height: 8000rpx;
	}

	// ========== 三级分类 ==========
	.level3-group {
		border-bottom: 1rpx solid $outline-light;
	}

	.level3-group:last-child {
		border-bottom: none;
	}

	.level3-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 20rpx 20rpx 20rpx 28rpx;
		transition: background-color 0.2s;

		&:active {
			background-color: rgba(15, 93, 227, 0.04);
		}
	}

	.level3-header-left {
		display: flex;
		align-items: center;
		flex: 1;
		min-width: 0;
	}

	.level3-dot {
		width: 10rpx;
		height: 10rpx;
		background: linear-gradient(135deg, $primary 0%, $accent-cyan 100%);
		border-radius: 50%;
		margin-right: 12rpx;
		flex-shrink: 0;
		box-shadow: 0 0 0 3rpx rgba(15, 93, 227, 0.12);
	}

	.level3-title {
		font-size: 25rpx;
		font-weight: 600;
		color: $on-surface;
		letter-spacing: 0.5rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.level3-arrow-wrap {
		width: 36rpx;
		height: 36rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: all 0.3s ease;
		flex-shrink: 0;
	}

	.level3-arrow-wrap.arrow-expanded {
		transform: rotate(180deg);
	}

	.level3-arrow-icon {
		font-size: 20rpx;
		color: $on-surface-muted;
		font-weight: 700;
		transition: color 0.3s;
	}

	.level3-arrow-wrap.arrow-expanded .level3-arrow-icon {
		color: $primary;
	}

	.level3-body {
		max-height: 0;
		overflow: hidden;
		transition: max-height 0.35s cubic-bezier(0.4, 0, 0.2, 1);
	}

	.level3-body.body-expanded {
		max-height: 5000rpx;
	}

	// ========== 四级标签（产品入口） ==========
	.level4-tags {
		padding: 4rpx 20rpx 20rpx 28rpx;
		display: flex;
		flex-wrap: wrap;
		gap: 12rpx;
	}

	.level4-tag {
		display: flex;
		align-items: center;
		background: linear-gradient(135deg, #ffffff 0%, $primary-fade 100%);
		border: 1rpx solid $outline-light;
		border-radius: 14rpx;
		padding: 12rpx 16rpx;
		transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
		max-width: 100%;
		min-width: 0;
		flex: 0 0 auto;
		box-shadow: 0 2rpx 8rpx rgba(15, 93, 227, 0.04);
	}

	.level4-tag:active {
		border-color: $primary;
		background: linear-gradient(135deg, $primary-soft 0%, #ffffff 100%);
		transform: scale(0.97);
		box-shadow:
			0 8rpx 20rpx rgba(15, 93, 227, 0.16),
			0 0 0 2rpx rgba(15, 93, 227, 0.08);
	}

	.level4-tag-icon {
		width: 52rpx;
		height: 52rpx;
		border-radius: 12rpx;
		background: linear-gradient(135deg, $primary-soft 0%, #dbe7ff 100%);
		margin-right: 12rpx;
		overflow: hidden;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		box-shadow: inset 0 0 0 1rpx rgba(255, 255, 255, 0.9);
	}

	.level4-tag-img {
		width: 100%;
		height: 100%;
	}

	.level4-tag-icon-text {
		font-size: 24rpx;
		font-weight: 700;
		background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
		-webkit-background-clip: text;
		background-clip: text;
		-webkit-text-fill-color: transparent;
	}

	.level4-tag-info {
		display: flex;
		flex-direction: column;
		overflow: hidden;
		flex: 1;
		min-width: 0;
	}

	.level4-tag-name {
		font-size: 23rpx;
		font-weight: 600;
		color: $on-surface;
		line-height: 1.35;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		letter-spacing: 0.3rpx;
	}

	.level4-tag-desc {
		font-size: 19rpx;
		color: $on-surface-muted;
		line-height: 1.3;
		margin-top: 2rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.content-bottom-spacer {
		height: 60rpx;
	}
</style>
