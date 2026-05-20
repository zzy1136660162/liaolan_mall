<template>
	<!-- 标题 -->
	<view class="title-box acea-row row-between row-middle" :style="[...boxStyle]" @click="goPage">
		<view class="acea-row row-middle" >
			<view :style="[...titleStyle]">{{ titleTxt }}</view>
			<view class="ml6" :style="[...titleFuStyle]">{{ titleFuTxt }}</view>
		</view>
		<view v-if="!selectShow" :style="[...titleRightStyle]">{{ titleRightTxt }}<text :style="[...titleRightStyle]"  class="iconfont icon-xiangyou"></text></view>
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
	export default {
		name: 'titles',
		props: {
			dataConfig: {
				type: Object,
				default: () => {}
			},
		},
		computed: {
			//最外层盒子的样式
			boxStyle() {
				return [{
						'border-radius': 2*this.dataConfig.bgTopStyle.val +
							'rpx' +
							' ' +
							2*this.dataConfig.bgTopStyle.val +
							'rpx' +
							' ' +
							2*this.dataConfig.bgDownStyle.val +
							'rpx' +
							' ' +
							2*this.dataConfig.bgDownStyle.val +
							'rpx',
					},
					{
						'background-image': this.selectStyle ?
							`linear-gradient(to right,${this.dataConfig.bgColor.color[0].item}, ${this.dataConfig.bgColor.color[1].item})` :
							`url(${this.bgImgUrl})`,
					},
					{
						margin: 2*this.dataConfig.mbConfig.val + 'rpx' + ' ' + 2*this.dataConfig.lrConfig.val + 'rpx' + ' ' +
							0
					},
					{
						padding: 2*this.dataConfig.upConfig.val + 'rpx' + ' ' + '20rpx' + ' ' + 2*this.dataConfig.downConfig
							.val + 'rpx'
					},
				];
			},
			titleStyle() {
				return [{
						'font-weight': this.dataConfig.textStyle.list[this.dataConfig.textStyle.tabVal].style,
					},
					{
						'font-style': this.dataConfig.textStyle.list[this.dataConfig.textStyle.tabVal].style
					},
					{
						fontSize: 2*this.dataConfig.fontSize.val + 'rpx',
					},
					{
						color: this.dataConfig.fontColor.color[0].item
					},
				];
			},
			titleFuStyle() {
				return [{
						fontSize: 2*this.dataConfig.fontFuSize.val + 'rpx',
					},
					{
						color: this.dataConfig.fontFuColor.color[0].item
					},
				];
			},
			titleRightStyle() {
				return [{
						fontSize: 2*this.dataConfig.fontRightSize.val + 'rpx',
					},
					{
						color: this.dataConfig.fontRightColor.color[0].item
					},
				];
			},
		},
		data() {
			return {
				configObj: null,
				titleTxt: '',
				titleFuTxt: '',
				titleRightTxt: '',
				link: '',
				txtPosition: '',
				txtStyle: '',
				fontSize: 0,
				mTOP: 0,
				titleColor: '',
				themeColor: '',
				prConfig: 0,
				bgStyle: 0,
				pageData: {},
				selectShow: '',
				selectStyle: '',
				bgImgUrl: '',
			};
		},
		created() {
			this.setConfig()
		},
		methods: {
			setConfig(data) {
				this.configObj = this.data;
				this.titleTxt = this.dataConfig.titleConfig.val;
				this.titleFuTxt = this.dataConfig.titleFuConfig.val;
				this.titleRightTxt = this.dataConfig.titleRightConfig.val;
				this.link = this.dataConfig.linkConfig.val;
				this.bgImgUrl = this.dataConfig.bgImg.url;
				this.selectShow = this.dataConfig.selectShow.tabVal;
				this.selectStyle = this.dataConfig.selectStyle.tabVal;
			},
			goPage() {
				this.$util.navigateTo(this.dataConfig.linkConfig.val)
			}
		}
	}
</script>

<style scoped lang="scss">
	$primary: #003da6;
	$surface: #f9f9ff;
	$surface-lowest: #ffffff;
	$outline-variant: #c3c6d7;
	$on-surface: #181c23;
	$on-surface-variant: #434654;
	$secondary: #5c5f60;
	$r-sm: 8rpx;
	$r-md: 14rpx;
	$ease: cubic-bezier(0.22, 1, 0.36, 1);

	.title-box{
		background-repeat: no-repeat;
		object-fit: contain;
		position: relative;
		padding-left: 20rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 6rpx;
			height: 32rpx;
			background: $primary;
			border-radius: 3rpx;
		}
	}
	.title {
		font-size: 32rpx;
		color: $on-surface;
		text-align: center;

		&.left {
			text-align: left;
		}

		&.right {
			text-align: right;
		}

		&.blod {
			font-weight: bold;
		}

		&.italics {
			font-style: italic;
		}
	}
	.ml6 {
	  margin-left: 12rpx;
	}
	.iconfont {
		color: $secondary;
		transition: color 0.2s $ease;
	}
</style>