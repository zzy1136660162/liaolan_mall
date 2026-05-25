<template>
	<!-- 导航组 -->
	<view v-show="menus.length" :style="[boxStyle]">
		<view v-if="isMany === 1">
			<view class="swiper">
				<swiper :interval="interval" :duration="duration" :style="'height:'+ navHigh +'px;'"
					@change='bannerfun'>
					<block>
						<swiper-item v-for="(item,indexw) in menuList" :key="indexw">
							<view class="menu acea-row" :id="'nav' + indexw" :style="[gridColumns]">
								<view :style="[titleColor]" class="item" :class="number===1?'four':number===2?'five':''"
									v-for="(itemn,indexn) in item.list" :key="indexn"
									@click="menusTap(itemn.info[1].value)">
									<view class="pictrue skeleton-radius">
										<easy-loadimage :image-src="itemn.img" :radius="dataConfig.contentStyle.val">
										</easy-loadimage>
									</view>
									<view class="menu-txt">{{ itemn.info[0].value }}</view>
								</view>
							</view>
						</swiper-item>
					</block>
				</swiper>
			</view>
			<view class="dot acea-row row-center-wrapper" v-if="docConfig<2 && menuList.length>1">
				<view class="dot-item line_dot-item" :style="active==index?'background:'+ dotColor:''"
					v-for="(item,index) in menuList"></view>
			</view>
		</view>
		<view class="nav oneNav" v-else>
			<scroll-view scroll-x="true" style="white-space: nowrap; display: flex" show-scrollbar="false">
				<block v-for="(item, index) in menus" :key="index">
					<view class="item" v-show="item.status" :style="[titleColor]" @click="menusTap(item.info[1].value)">
						<view class="pictrue skeleton-radius">
							<easy-loadimage :image-src="item.img" :radius="dataConfig.contentStyle.val">
							</easy-loadimage>
						</view>
						<view class="menu-txt">{{ item.info[0].value }}</view>
					</view>
				</block>
			</scroll-view>
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
		name: 'menus',
		props: {
			dataConfig: {
				type: Object,
				default: () => {}
			}
		},
		components: {
			easyLoadimage
		},
		data() {
			return {
				interval: 3000,
				duration: 500,
				menus: this.dataConfig.menuConfig.list || [],
				rowsNum: this.dataConfig.rowsNum.tabVal,
				number: this.dataConfig.number.tabVal,
				isMany: this.dataConfig.tabConfig.tabVal,
				docConfig: 0,
				dotColor: '#003da6',
				menuList: [],
				active: 0,
				navHigh: 0
			};
		},
		computed: {
			//最外层盒子的样式
			boxStyle() {
				return {
					borderRadius: this.dataConfig.bgStyle.val * 2 + 'rpx',
					background: `linear-gradient(${this.dataConfig.bgColor.color[0].item}, ${this.dataConfig.bgColor.color[1].item})`,
					margin: this.dataConfig.mbConfig.val * 2 + 'rpx' + ' ' + this.dataConfig.lrConfig.val * 2 + 'rpx' +' ' + 0,
					padding: this.dataConfig.upConfig.val * 2 + 'rpx' + ' ' + 0 + ' ' + this.dataConfig.downConfig.val *2 + 'rpx'
				}
			},
			//分几行展示，一行展示多少个
			gridColumns() {
				if (this.dataConfig.number.tabVal == 0) {
					return {
						gridRowGap: this.dataConfig.contentConfig.val * 2 + 'rpx',
						gridTemplateColumns: 'repeat(3, 1fr)'
					}
				} else if (this.dataConfig.number.tabVal == 1) {
					return {
						gridRowGap: this.dataConfig.contentConfig.val * 2 + 'rpx',
						gridTemplateColumns: 'repeat(4, 1fr)'
					}
				} else {
					return {
						gridRowGap: this.dataConfig.contentConfig.val * 2 + 'rpx',
						gridTemplateColumns: 'repeat(5, 1fr)'
					}
				}
			},
			//标题颜色
			titleColor() {
				return {
					'color': this.dataConfig.titleColor.color[0].item,
				}
			},
			//内容圆角
			menuStyle() {
				return {
					'border-radius': this.dataConfig.contentStyle.val + 'px'
				}
			}
		},
		mounted() {
			if (this.rowsNum === 0) {
				if (this.number === 0) {
					this.pageNum(6)
				} else if (this.number === 1) {
					this.pageNum(8)
				} else {
					this.pageNum(10)
				}
			} else if (this.rowsNum === 1) {
				if (this.number === 0) {
					this.pageNum(9)
				} else if (this.number === 1) {
					this.pageNum(12)
				} else {
					this.pageNum(15)
				}
			} else {
				if (this.number === 0) {
					this.pageNum(12)
				} else if (this.number === 1) {
					this.pageNum(16)
				} else {
					this.pageNum(20)
				}
			}
			this.$nextTick(() => {
				if (this.menuList.length && this.isMany===1) {
					let that = this
					// #ifdef H5
					that.menuHeight()
					// #endif
					// #ifndef H5
					setTimeout(() => {
						that.menuHeight()
					}, 150)
					// #endif
				}
			})
		},
		methods: {
			bannerfun(e) {
				this.active = e.detail.current;
			},
			menuHeight() {
				let that = this;
				const query = uni.createSelectorQuery().in(this);
				query.select('#nav0').boundingClientRect(data => {
					that.navHigh = data.height;
				}).exec();
			},
			pageNum(num) {
				let menus = this.menus.filter(item=>item.status);
				let count = Math.ceil(menus.length / num);
				let goodArray = new Array();
				for (let i = 0; i < count; i++) {
					let list = menus.slice(i * num, i * num + num);
					if (list.length)
						goodArray.push({
							list: list
						});
				}
				this.$set(this, 'menuList', goodArray);
			},
			menusTap(url) {
				this.$util.navigateTo(url);
			}
		}
	};
</script>

<style lang="scss" scoped>
	$primary: #003da6;
	$primary-container: #0052d9;
	$surface: #f9f9ff;
	$surface-low: #f1f3ff;
	$surface-lowest: #ffffff;
	$outline-variant: #c3c6d7;
	$on-surface: #181c23;
	$on-surface-variant: #434654;
	$white: #ffffff;
	$r-sm: 8rpx;
	$r-md: 14rpx;
	$ease: cubic-bezier(0.22, 1, 0.36, 1);

	.dot {
		width: 100%;
		margin-top: 30rpx;

		.instruct {
			width: 50rpx;
			height: 36rpx;
			line-height: 36rpx;
			background-color: $primary;
			color: $white;
			border-radius: 16rpx;
			font-size: 24rpx;
			text-align: center;
		}

		.dot-item {
			width: 10rpx;
			height: 10rpx;
			background: $outline-variant;
			border-radius: 50%;
			margin: 0 4px;
			transition: all 0.3s $ease;

			&.line_dot-item {
				width: 20rpx;
				height: 5rpx;
				border-radius: 3rpx;
			}
		}
	}

	.nav {
		.item {
			width: 160rpx;
			text-align: center;
			font-size: 24rpx;
			display: inline-block;
			color: $on-surface-variant;
			transition: opacity 0.2s $ease;

			&:active {
				opacity: 0.7;
			}

			.pictrue {
				width: 90rpx;
				height: 90rpx;
				margin: 0 auto;
				background-color: $surface-low;
				border-radius: $r-md;
				overflow: hidden;

				image {
					width: 100%;
					height: 100%;
					border-radius: $r-md;
				}

				&.on {
					image {
						border-radius: 0;
					}
				}
			}

			.menu-txt {
				margin-top: 15rpx;
				color: $on-surface-variant;
				font-weight: 500;
			}
		}
	}

	.swiper {
		z-index: 20;
		position: relative;
		overflow: hidden;
		background-color: $surface-lowest;

		.menu {
			display: grid;
			grid-template-rows: auto;
			width: 100%;
			padding: 0 20rpx;

			.item {
				color: $on-surface-variant;
				transition: opacity 0.2s $ease;

				&:active {
					opacity: 0.7;
				}

				.pictrue {
					width: 90rpx;
					height: 90rpx;
					margin: 0 auto;
					background-color: $surface-low;
					border-radius: $r-md;
					overflow: hidden;

					image {
						width: 100%;
						height: 100%;
						border-radius: $r-md;
					}

					&.on {
						image {
							border-radius: 0;
						}
					}
				}

				.menu-txt {
					text-align: center;
					font-size: 12px;
					margin-top: 14rpx;
					color: $on-surface-variant;
					font-weight: 500;
				}
			}
		}

		swiper,
		.swiper-item {
			width: 100%;
			display: block;
		}
	}
</style>
