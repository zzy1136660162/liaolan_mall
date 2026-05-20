<template>
	<!-- 文章列表 -->
	<view>
		<view class="articleList" :style="[boxStyle]" v-if="articleList.length">
			<view v-if="listStyle">
				<navigator :url='"/pages/news/news_details/index?id="+item.id' hover-class='none' :style="[itemStyle]"
					 v-for="(item,index) in articleList" :key='index'
					class="item acea-row row-between-wrapper">
					<view class="pictrue">
						<easy-loadimage :image-src="item.imageInput" :radius="dataConfig.contentStyle.val"></easy-loadimage>
					</view>
					<view class="text">
						<view class="name line2" :style="[titleColor]">{{item.title}}</view>
						<view class="time" :style="[timeColor]">{{item.updateTime}}</view>
					</view>
				</navigator>
			</view>
			<view v-else>
				<navigator  :url='"/pages/news/news_details/index?id="+item.id' hover-class='none'
					:style="[itemStyle]" v-for="(item,index) in articleList" :key='index'
					class="item acea-row row-between-wrapper">
					<view class="text">
						<view class="name line2" :style="[titleColor]">{{item.title}}</view>
						<view class="time" :style="[timeColor]">{{item.updateTime}}</view>
					</view>
					<view class="pictrue">
						<easy-loadimage :image-src="item.imageInput" :radius="dataConfig.contentStyle.val"></easy-loadimage>
					</view>
				</navigator>
			</view>
		</view>
	</view>
</template>
<script>
	// +----------------------------------------------------------------------
	// | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
	// +----------------------------------------------------------------------
	// | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
	// +----------------------------------------------------------------------
	// | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
	// +----------------------------------------------------------------------
	// | Author: CRMEB Team <admin@crmeb.com>
	// +----------------------------------------------------------------------
	import easyLoadimage from '@/components/base/easy-loadimage.vue';
	export default {
		name: 'homeArticle',
		props: {
			dataConfig: {
				type: Object,
				default: () => {}
			},
		},
		data() {
			return {}
		},
		components: {
			easyLoadimage
		},
		computed: {
			//布局样式
			listStyle() {
				return this.dataConfig.layoutConfig.tabVal === 0
			},
			//文章分类
			articleList() {
				return this.dataConfig.selectConfig.articleList
			},
			//最外层盒子的样式
			boxStyle() {
				return {
					borderRadius: this.dataConfig.bgStyle.val * 2 + 'rpx',
					background: `linear-gradient(${this.dataConfig.bgColor.color[0].item}, ${this.dataConfig.bgColor.color[1].item})`,
					margin: this.dataConfig.mbConfig.val * 2 + 'rpx' + ' ' + this.dataConfig.lrConfig.val * 2 + 'rpx' +
						' ' + 0,
					padding: this.dataConfig.upConfig.val * 2 + 'rpx' + ' ' + 0 + ' ' + this.dataConfig.downConfig.val *
						2 + 'rpx'
				}
			},
			//文章间距
			itemStyle() {
				return {
					'margin-bottom': this.dataConfig.contentConfig.val * 2 + 'rpx'
				}
			},
			//时间颜色
			timeColor() {
				return {
					'color': this.dataConfig.timeColor.color[0].item
				}
			},
			//标题颜色
			titleColor() {
				return {
					'color': this.dataConfig.titleColor.color[0].item
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	$primary: #003da6;
	$primary-container: #0052d9;
	$tertiary-container: #895000;
	$surface: #f9f9ff;
	$surface-lowest: #ffffff;
	$surface-low: #f1f3ff;
	$outline-variant: #c3c6d7;
	$surface-variant: #dfe2ed;
	$on-surface: #181c23;
	$secondary: #5c5f60;
	$on-surface-variant: #434654;
	$white: #ffffff;
	$r-sm: 8rpx;
	$r-md: 14rpx;
	$r-lg: 20rpx;
	$ease: cubic-bezier(0.22, 1, 0.36, 1);

	.articleList {

		.item {
			background: $surface-lowest;
			border: 1rpx solid $outline-variant;
			border-radius: $r-md;
			padding: 16rpx;
			margin-bottom: 16rpx;
			box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.03);
			transition: transform 0.15s $ease;

			&:active {
				transform: scale(0.98);
			}

			&:last-child {
				margin-bottom: 0 !important;
			}

			.text {
				width: 60%;

				.name {
					font-size: 30rpx;
					color: $on-surface;
					height: 82rpx;
				}

				.time {
					font-size: 30rpx;
					color: $secondary;
					margin-top: 40rpx;
				}
			}

			.pictrue {
				width: 37%;
				height: 156rpx;
				border-radius: $r-sm;
				overflow: hidden;

				image {
					width: 100%;
					height: 100%;
					border-radius: 6rpx;
				}
			}
		}
	}
</style>