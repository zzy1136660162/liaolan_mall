<template>
	<!-- 搜索框 -->
	<view>
		<view class="mp-header">
			<!-- #ifdef MP || APP-PLUS -->
			<view class="sys-head tui-skeleton" :style="{ height: `${isSmallPage?0:statusBarHeight}px` }"></view>
			<!-- #endif -->
			<!-- #ifdef APP -->
			<view class="serch-box tui-skeleton" :style="[boxStyle]" style="height: 43px;">
			<!-- #endif -->
			<!-- #ifndef APP -->
			<view class="serch-box tui-skeleton" :style="[boxStyle]">
			<!-- #endif -->
				<view class="serch-wrapper flex">
					<view v-if="logoConfig" class="logo skeleton-rect">
						<image :src="logoUrl" mode=""></image>
					</view>
					<navigator :style="[contentStyle]" v-if="hotWords.length > 0"
						:url="'/pages/goods/goods_search/index?searchVal='+searchVal"
						:class="logoConfig&&!isSmallPage ? 'input' : logoConfig&&isSmallPage?'uninput':!logoConfig&&!isSmallPage?'uninput':'maxInput'" hover-class="none" class=" input skeleton-rect">
						<view class='swiperTxt'>
							<swiper :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval"
								:duration="duration" vertical="true" circular="true" @change="textChange">
								<block v-for="(item,index) in hotWords" :key='index'>
									<swiper-item catchtouchmove='catchTouchMove'>
										<view class='acea-row row-between-wrapper'>
											<view class='text'>
												<view class='newsTitle line1'><text class="iconfont icon-sousuo"></text><text>{{item.val}}</text></view>
											</view>
										</view>
									</swiper-item>
								</block>
							</swiper>
						</view>
					</navigator>
					<navigator :style="[contentStyle]" hover-class="none" v-else url="/pages/goods/goods_search/index"
						:class="logoConfig&&!isSmallPage ? 'input' : logoConfig&&isSmallPage?'uninput':!logoConfig&&!isSmallPage?'uninput':'maxInput'" class="skeleton-rect">
						<text class="line1">{{placeWords}}</text>
						<text class="iconfont icon-xiazai5"></text>
					</navigator>
				</view>
			</view>

		</view>
		<view :style="'height:'+marTop+'px;'"></view>
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
	let app = getApp();
	export default {
		name: 'headerSerch',
		props: {
			dataConfig: {
				type: Object,
				default: () => {}
			},
			//是否为微页面
			isSmallPage: {
				type: Boolean,
				default: false
			},
			//是否开始滚动
			isScrolled: {
				type: Boolean,
				default: false
			},
		},
		data() {
			return {
				statusBarHeight: app.globalData.statusBarHeight,
				indicatorDots: false,
				autoplay: true,
				duration: 500,
				marTop: 0,
				searchH: 0,
				searchVal: '',
				searchTop:0,
				searchRight:0,
				searchHeight:0,
				statusWidth:0,
				searchBoxHeight:0
			};
		},
		computed: {
			//轮播切换时间
			interval(){
				return this.dataConfig.titleConfig.val * 1000
			},
			//判断logo图是否展示
			logoConfig() {
				return this.dataConfig.logoConfig.url && this.dataConfig.searConfig.tabVal === 1
			},
			//logo图
			logoUrl() {
				if(this.isScrolled&&this.dataConfig.logoFixConfig.url){
					return this.dataConfig.logoFixConfig.url
				}else{
					return this.dataConfig.logoConfig.url
				}
			},
			//最外层盒子的样式
			boxStyle() {
				return {
					borderRadius: this.dataConfig.bgStyle.val * 2 + 'rpx',
					background: `linear-gradient(${this.dataConfig.bgColor.color[0].item}, ${this.dataConfig.bgColor.color[1].item})`,
					margin: 0 + ' ' + this.dataConfig.lrConfig.val * 2 + 'rpx' +
						' ' + 0,
					// #ifdef MP
					height:this.searchBoxHeight + 'px',
					// #endif
				}
			},
			//搜索热词
			hotWords() {
				return this.dataConfig.hotWords.list
			},
			//内容圆角
			contentStyle() {
				return {
					borderRadius: this.dataConfig.contentStyle.val ? this.dataConfig.contentStyle.val + 'px' : '0',
					background: this.dataConfig.borderColor.color[0].item,
					color: this.dataConfig.textColor.color[0].item,
					textAlign: this.dataConfig.textPosition.list[this.dataConfig.textPosition.tabVal].style,
					// #ifdef MP
					height:this.searchHeight + 'px',
					flex:!this.isSmallPage?1:'',
					marginRight:!this.isSmallPage?(this.statusWidth + this.searchRight+'px'):'',
					// #endif
				}
			},
			//搜索提示语
			placeWords(){
				return this.dataConfig.placeWords.val;
			}
		},
		mounted() {
			// 使用nextTick，确保页面更新结束后，再请求高度
			// #ifdef MP || APP-PLUS
			this.$nextTick(() => {
				setTimeout(() => {
					// 获取小程序头部高度
					let info = uni.createSelectorQuery().in(this).select(".serch-box");
					info.boundingClientRect((data)=> {
						this.marTop = this.isSmallPage ? data.height :data.height + this.statusBarHeight
					}).exec()
				}, 100)
			})
			// #endif

			// #ifdef MP
			const res = uni.getMenuButtonBoundingClientRect()
			const statusHeight = res.top //胶囊距离顶部
			const statusRight = res.right //胶囊右边界坐标
			const jnHeight = res.height //胶囊高度
			this.statusWidth= res.width
			this.searchTop=statusHeight-this.statusBarHeight
			this.searchHeight=jnHeight
			this.searchBoxHeight = this.searchTop*2 + jnHeight
			//搜索框宽度计算
			uni.getSystemInfo({
				success:res=>{
					this.searchRight=res.windowWidth-statusRight-this.dataConfig.lrConfig.val
				}
			})
			// #endif

			// #ifdef H5
			this.marTop = 43
			// #endif
		},
		methods: {
			textChange(e) {
				let {
					current,
					source
				} = e.detail;
				if (source === 'autoplay' || source === 'touch') {
					this.searchVal = this.hotWords[e.detail.current]['val'];
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	$primary: #003da6;
	$primary-container: #0052d9;
	$surface: #f9f9ff;
	$surface-low: #f1f3ff;
	$outline-variant: #c3c6d7;
	$on-surface: #181c23;
	$on-surface-variant: #434654;
	$white: #ffffff;
	$r-sm: 8rpx;
	$r-md: 14rpx;
	$ease: cubic-bezier(0.22, 1, 0.36, 1);

	.ml40 {
		margin-left: 40rpx;
	}

	.sys-head {
		background: $surface-low;
	}

	.header {
		width: 100%;
		background: $white;

		.btn {
			position: relative;

			.iconfont {
				font-size: 45rpx;
			}
		}

		.iconnum {
			min-width: 6px;
			color: #fff;
			border-radius: 15rpx;
			position: absolute;
			right: -10rpx;
			top: -10rpx;
			font-size: 10px;
			padding: 0 4px;
		}

		.serch-wrapper {
			align-items: center;
			padding: 20rpx 24rpx 20rpx 24rpx;

			.logo {
				width: 152rpx;
				height: 60rpx;
			}
		}
	}

	.swiperTxt {
		width: 100%;
		height: 100%;
		line-height: 58rpx;
		overflow: hidden;
	}

	.swiperTxt .text {
		width: 100%;
	}

	.swiperTxt .text .label {
		font-size: 20rpx;
		color: $primary;
		width: 64rpx;
		height: 30rpx;
		border-radius: 40rpx;
		text-align: center;
		line-height: 28rpx;
		border: 2rpx solid rgba($primary, 0.3);
	}

	.swiperTxt .text .newsTitle {
		font-size: 24rpx;
	}

	.swiperTxt swiper {
		height: 100%;
	}

	.mp-header {
		z-index: 90;
		position: fixed;
		left: 0;
		top: 0;
		width: 100%;

		.logo {
			line-height: 0;
		}

		.serch-wrapper {
			height: 100%;
			align-items: center;
			padding: 20rpx 30rpx;

			image {
				width: 152rpx;
				height: 60rpx;
				margin-right: 20rpx;
			}

			.input,
			.uninput {
				display: flex;
				align-items: center;
				/* #ifdef MP*/
				width: 50%;
				/* #endif */
				/* #ifdef H5 || APP*/
				width: 100%;
				flex: 1;
				/* #endif */
				height: 58rpx;
				line-height: 58rpx;
				padding: 0 20rpx 0 54rpx;
				background: $surface-low;
				border: 1px solid $outline-variant;
				border-radius: 16rpx;
				color: $on-surface-variant;
				font-size: 28rpx;
				position: relative;
				box-sizing: border-box;
				transition: background 0.3s $ease, box-shadow 0.3s $ease, border-color 0.3s $ease;

				.iconfont {
					left: 14rpx;
					font-size: 26rpx;
					color: $primary;
				}

				.line1 {
					display: inline-block;
					width: 400rpx;
				}
			}

			.uninput {
				/* #ifdef MP */
				width: 75%;
				/* #endif */
				/* #ifndef MP */
				width: 100%;
				/* #endif */
			}
		}

		&.scrolled .serch-wrapper {
			.input,
			.uninput {
				background: $white;
				box-shadow: 0 2rpx 12rpx rgba(0, 61, 166, 0.08);
				border-color: rgba(195, 198, 215, 0.5);
			}
		}
	}

	.maxInput {
		width: 100% !important;
	}
</style>
