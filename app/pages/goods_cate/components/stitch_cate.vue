<template>
	<view class="category-page" :style="{height: winHeight + 'px'}">
		<view class="search-bar">
			<view class="search-input" @click="goSearch">
				<text class="search-icon">&#xe60c;</text>
				<text class="search-placeholder">搜索电缆型号、分类</text>
			</view>
		</view>
		<view class="category-body">
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
						<view v-if="activeLevel1Index === index" class="sidebar-indicator"></view>
						<text class="sidebar-text" :class="{ 'sidebar-text-active': activeLevel1Index === index }">{{ item.name }}</text>
					</view>
				</view>
			</scroll-view>
			<scroll-view class="content-area" scroll-y scroll-with-animation>
				<view v-if="currentLevel1" class="content-inner">
					<view class="level1-banner">
						<view class="level1-banner-bg"></view>
						<view class="level1-banner-content">
							<text class="level1-banner-title">{{ currentLevel1.name }}</text>
							<text v-if="currentLevel1.description" class="level1-banner-desc">{{ currentLevel1.description }}</text>
						</view>
					</view>
					<view v-for="(level2, l2Idx) in currentLevel1.child" :key="level2.id" class="level2-section">
						<view class="level2-header" @click="toggleLevel2(l2Idx)">
							<view class="level2-header-left">
								<view class="level2-dot"></view>
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
	.category-page {
		display: flex;
		flex-direction: column;
		background: #f9f9ff;
		overflow: hidden;
	}

	.search-bar {
		padding: 16rpx 24rpx 12rpx;
		background: #ffffff;
		flex-shrink: 0;
	}

	.search-input {
		display: flex;
		align-items: center;
		height: 72rpx;
		background: #ebedf9;
		border-radius: 36rpx;
		padding: 0 28rpx;
	}

	.search-icon {
		font-size: 28rpx;
		color: #737686;
		margin-right: 14rpx;
	}

	.search-placeholder {
		font-size: 26rpx;
		color: #737686;
	}

	.category-body {
		flex: 1;
		display: flex;
		overflow: hidden;
	}

	.sidebar {
		width: 176rpx;
		background: #ffffff;
		flex-shrink: 0;
		height: 100%;
	}

	.sidebar-nav {
		padding: 8rpx 0;
	}

	.sidebar-item {
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 30rpx 8rpx;
		transition: all 0.25s ease;
	}

	.sidebar-item.active {
		background: #f1f3ff;
	}

	.sidebar-indicator {
		position: absolute;
		left: 0;
		top: 50%;
		transform: translateY(-50%);
		width: 8rpx;
		height: 44rpx;
		background: #003da6;
		border-radius: 0 6rpx 6rpx 0;
	}

	.sidebar-text {
		font-size: 24rpx;
		color: #5c5f60;
		text-align: center;
		line-height: 1.5;
		transition: all 0.25s ease;
	}

	.sidebar-text-active {
		color: #003da6;
		font-weight: 600;
	}

	.content-area {
		flex: 1;
		height: 100%;
		background: #f9f9ff;
	}

	.content-inner {
		padding: 16rpx 20rpx;
	}

	.level1-banner {
		position: relative;
		border-radius: 16rpx;
		overflow: hidden;
		margin-bottom: 20rpx;
		height: 140rpx;
	}

	.level1-banner-bg {
		position: absolute;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(135deg, #003da6 0%, #0052d9 50%, #2a7fff 100%);
	}

	.level1-banner-content {
		position: relative;
		z-index: 1;
		padding: 28rpx 32rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		height: 100%;
	}

	.level1-banner-title {
		font-size: 36rpx;
		font-weight: 700;
		color: #ffffff;
		margin-bottom: 8rpx;
		letter-spacing: 2rpx;
	}

	.level1-banner-desc {
		font-size: 22rpx;
		color: rgba(255, 255, 255, 0.85);
		line-height: 1.4;
	}

	.level2-section {
		margin-bottom: 16rpx;
		background: #ffffff;
		border-radius: 16rpx;
		overflow: hidden;
		border: 1rpx solid #e5e8f3;
	}

	.level2-header {
		padding: 24rpx 24rpx;
		background: #f1f3ff;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.level2-header-left {
		display: flex;
		align-items: flex-start;
		flex: 1;
	}

	.level2-dot {
		width: 6rpx;
		height: 28rpx;
		background: #003da6;
		border-radius: 3rpx;
		margin-right: 14rpx;
		margin-top: 4rpx;
		flex-shrink: 0;
	}

	.level2-header-text {
		display: flex;
		flex-direction: column;
	}

	.level2-title {
		font-size: 28rpx;
		font-weight: 600;
		color: #181c23;
		line-height: 1.4;
	}

	.level2-desc {
		font-size: 22rpx;
		color: #737686;
		margin-top: 4rpx;
		line-height: 1.4;
	}

	.level2-arrow-wrap {
		width: 44rpx;
		height: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 50%;
		background: rgba(0, 61, 166, 0.08);
		transition: all 0.3s ease;
		flex-shrink: 0;
		margin-left: 12rpx;
	}

	.level2-arrow-wrap.arrow-expanded {
		transform: rotate(180deg);
		background: rgba(0, 61, 166, 0.15);
	}

	.level2-arrow-icon {
		font-size: 22rpx;
		color: #003da6;
		font-weight: 700;
	}

	.level2-body {
		max-height: 0;
		overflow: hidden;
		transition: max-height 0.35s cubic-bezier(0.4, 0, 0.2, 1);
	}

	.level2-body.body-expanded {
		max-height: 8000rpx;
	}

	.level3-group {
		border-bottom: 1rpx solid #f0f1f8;
	}

	.level3-group:last-child {
		border-bottom: none;
	}

	.level3-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 22rpx 24rpx 22rpx 36rpx;
	}

	.level3-header-left {
		display: flex;
		align-items: center;
		flex: 1;
	}

	.level3-dot {
		width: 8rpx;
		height: 8rpx;
		background: #0052d9;
		border-radius: 50%;
		margin-right: 14rpx;
		flex-shrink: 0;
	}

	.level3-title {
		font-size: 26rpx;
		font-weight: 600;
		color: #181c23;
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
		color: #737686;
		font-weight: 700;
	}

	.level3-body {
		max-height: 0;
		overflow: hidden;
		transition: max-height 0.3s cubic-bezier(0.4, 0, 0.2, 1);
	}

	.level3-body.body-expanded {
		max-height: 5000rpx;
	}

	.level4-tags {
		padding: 0 24rpx 24rpx 36rpx;
		display: flex;
		flex-wrap: wrap;
		gap: 16rpx;
	}

	.level4-tag {
		display: flex;
		align-items: center;
		background: #ffffff;
		border: 1rpx solid #e5e8f3;
		border-radius: 12rpx;
		padding: 14rpx 20rpx;
		transition: all 0.2s ease;
		max-width: 100%;
	}

	.level4-tag:active {
		border-color: #003da6;
		background: #f1f3ff;
		transform: scale(0.97);
	}

	.level4-tag-icon {
		width: 56rpx;
		height: 56rpx;
		border-radius: 10rpx;
		background: linear-gradient(135deg, #dbe1ff 0%, #b4c5ff 100%);
		margin-right: 16rpx;
		overflow: hidden;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.level4-tag-img {
		width: 100%;
		height: 100%;
	}

	.level4-tag-icon-text {
		font-size: 24rpx;
		font-weight: 700;
		color: #003da6;
	}

	.level4-tag-info {
		display: flex;
		flex-direction: column;
		overflow: hidden;
		flex: 1;
	}

	.level4-tag-name {
		font-size: 24rpx;
		font-weight: 500;
		color: #181c23;
		line-height: 1.4;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.level4-tag-desc {
		font-size: 20rpx;
		color: #737686;
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
