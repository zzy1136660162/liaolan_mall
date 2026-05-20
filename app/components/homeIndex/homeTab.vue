<template>
	<!-- tab选项卡 -->
	<view class="index-product-wrapper" :style="[mbConfig]">
		<view class="nav-bd longTab" :style="[tabBgColor]">
			<scroll-view scroll-x="true" style="white-space: nowrap; display: flex" :scroll-left="tabLeft">
				<view class="longItem"
					:style="'color:' + (index == ProductNavindex ? checkColor : fontColor)+';--color:'+checkColor"
					:data-index="index" :class="index===ProductNavindex?'click':''" v-for="(item,index) in navList"
					:key="index" :id="'id'+index" @click="ProductNavTab(item, index)">{{ item.val }}
				</view>
			</scroll-view>
		</view>
		<view :style="[boxStyle]">
			<!-- 单列 -->
			<block v-if="itemStyle == 0">
				<view class="listA" :style="[gridGap]">
					<view class="item" v-for="(item, index) in tempArr" :key="index" @click="goDetail(item)">
						<view class="pictrue">
							<easy-loadimage :image-src="item.image"
								:radius="dataConfig.contentStyle.val"></easy-loadimage>
								<view v-if="item.activityStyle" :style="{ backgroundImage: `url(${item.activityStyle})` }" class="border-picture"></view>
						</view>
						<view class="text-info text-add">
							<view>
								<view class="title line2" :style="[titleColor]" v-if="showArr.includes(0)">
									<span>{{ item.storeName }}</span>
								</view>
							</view>
							<view v-if="item.productTags && item.productTags.locationUnderTitle.length">
								<text
									v-for="items in item.productTags.locationUnderTitle.length>3?item.productTags.locationUnderTitle.slice(0,3):item.productTags.locationUnderTitle"
									:key="items.id" class="mr10 tagSolid">{{items.tagName}}</text>
							</view>
							<view class="price acea-row row-middle" :style="[priceColor]">
								<view v-if="showArr.includes(1)">
									￥<span class="num semiBold">{{item.price}}</span>
								</view>
							</view>
							<view class="old-price" v-if="showArr.includes(2)" :style="[soldColor]">
								已售 {{ item.sales || 0 }} {{ item.unitName }}
							</view>
						</view>
					</view>
				</view>
			</block>
			<!-- 两列 -->
			<block v-if="itemStyle == 1">
				<view class="listC" :style="[gridGap]">
					<view class="item" :style="[contentStyle]" v-for="(item, index) in tempArr" :key="index"
						@click="goDetail(item)">
						<view class="pictrue">
							<easy-loadimage :image-src="item.image"
								:radius="dataConfig.contentStyle.val"></easy-loadimage>
								<view v-if="item.activityStyle" :style="{ backgroundImage: `url(${item.activityStyle})` }" class="border-picture"></view>
						</view>
						<view class="text-info">
							<view class="title line2" :style="[titleColor]" v-if="showArr.includes(0)">
								<span>{{ item.storeName }}</span>
							</view>
							<view v-if="item.productTags && item.productTags.locationUnderTitle.length">
								<text
									v-for="items in item.productTags.locationUnderTitle.length>3?item.productTags.locationUnderTitle.slice(0,3):item.productTags.locationUnderTitle"
									:key="items.id" class="mr10 tagSolid">{{items.tagName}}</text>
							</view>
							<view class="row-middle price" :style="[priceColor]">
								<view v-if="showArr.includes(1)">
									￥<span class="num semiBold">{{item.price}}</span>
								</view>
								<view class="old-price ml10 " v-if="showArr.includes(2)" :style="[soldColor]">
									已售 {{ item.sales || 0 }} {{ item.unitName }}
								</view>
							</view>
						</view>
					</view>
				</view>
			</block>
			<!-- 三列 -->
			<block v-if="itemStyle == 2">
				<view class="listB" :style="[gridGap]">
					<view class="item" v-for="(item, index) in tempArr" :key="index" @click="goDetail(item)">
						<view class="pictrue">
							<easy-loadimage :image-src="item.image"
								:radius="dataConfig.contentStyle.val"></easy-loadimage>
								<view v-if="item.activityStyle" :style="{ backgroundImage: `url(${item.activityStyle})` }" class="border-picture"></view>
						</view>
						<view class="text-info">
							<view class="title line2" :style="[titleColor]" v-if="showArr.includes(0)">
								<span>{{ item.storeName }}</span>
							</view>
							<view v-if="item.productTags && item.productTags.locationUnderTitle.length">
								<text
									v-for="items in item.productTags.locationUnderTitle.length>3?item.productTags.locationUnderTitle.slice(0,3):item.productTags.locationUnderTitle"
									:key="items.id" class="mr10 tagSolid">{{items.tagName}}</text>
							</view>
							<view class="price" :style="[priceColor]">
								<view v-if="showArr.includes(1)">
									￥<span class="num semiBold">{{item.price}}</span>
								</view>
							</view>
							<view class="old-price " v-if="showArr.includes(2)" :style="[soldColor]">
								已售 {{ item.sales || 0 }} {{ item.unitName }}
							</view>
						</view>
					</view>
				</view>
			</block>
			<!-- 大图 -->
			<block v-if="itemStyle == 3 && tempArr.length">
				<view class="listBig" :style="[gridGap]">
					<view class="itemBig" v-for="(item,index) in tempArr" :key="index" @click="goDetail(item)">
						<view class="img-box">
							<easy-loadimage :image-src="item.image"
								:radius="dataConfig.contentStyle.val"></easy-loadimage>
							<view v-if="item.activityStyle" :style="{ backgroundImage: `url(${item.activityStyle})` }" class="border-picture"></view>
						</view>
						<view class="name line2" :style="[titleColor]" v-if="showArr.includes(0)">
							<span>{{item.storeName}}</span>
						</view>
						<view style="padding: 0 8px;"
							v-if="item.productTags && item.productTags.locationUnderTitle.length">
							<text
								v-for="items in item.productTags.locationUnderTitle.length>3?item.productTags.locationUnderTitle.slice(0,3):item.productTags.locationUnderTitle"
								:key="items.id" class="mr10 tagSolid">{{items.tagName}}</text>
						</view>
						<slot name="center"></slot>
						<view class="row-middle price" :style="[priceColor]">
							<span v-if="showArr.includes(1)">
								￥<span class="num semiBold">{{item.price}}</span>
							</span>
							<view class="old-price" v-if="showArr.includes(2)" :style="[soldColor]">
								已售 {{ item.sales || 0 }} {{ item.unitName }}
							</view>
						</view>
					</view>
				</view>
			</block>
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
	import {
			getProductslist,productByidsApi
		} from '@/api/store.js';
	let app = getApp();
	import easyLoadimage from '@/components/base/easy-loadimage.vue';
	export default {
		name: 'homeTab',
		props: {
			dataConfig: {
				type: Object,
				default: () => {}
			}
		},
		data() {
			return {
				//普通价格
				svipPriceStyle: {
					svipBox: {
						height: '26rpx',
						borderRadius: '60rpx 56rpx 56rpx 20rpx',
					},
					icon: {
						height: '26rpx',
						fontSize: '18rpx',
						borderRadius: '12rpx 0 12rpx 2rpx'
					},
					price: {
						fontSize: '38rpx'
					},
					svipPrice: {
						fontSize: '22rpx'
					}
				},
				//svip价格
				svipIconStyle: {
					svipBox: {
						height: '26rpx',
						borderRadius: '24rpx 40rpx 40rpx 0.4rpx',
					},
					price: {
						fontSize: '38rpx'
					},
					svipPrice: {
						fontSize: '18rpx'
					}
				},
				tempArr: [],
				iSshowH: false,
				ProductNavindex: 0,
				itemStyle: 0, //样式类型
				themeColor: '#f00',
				titleConfig: 1, //标题位置
				infoColor: '#999',
				goodType: 3,
				loadend: false,
				loading: false,
				page: 1,
				isWidth: 0, //每个导航栏占位
				tabLeft: 0,
				limit: 0 ,//分页条数
				themeColor:this.$options.filters.filterTheme(app.globalData.theme)
			};
		},
		components: {
			easyLoadimage,
		},
		created() {
			let that = this
			uni.getSystemInfo({
				success(e) {
					that.isWidth = (e.windowWidth) / 5;
				}
			})
		},
		computed: {
			//标签文字颜色
			fontColor() {
				return this.dataConfig.fontColor.color[0].item
			},
			//选中颜色
			checkColor() {
				return this.dataConfig.checkThemeStyleConfig.tabVal?this.dataConfig.checkColor.color[0].item:this.themeColor
			},
			//选项卡背景颜色
			tabBgColor() {
				return {
					background: `linear-gradient(${this.dataConfig.tabBgColor.color[0].item}, ${this.dataConfig.tabBgColor.color[1].item})`,
				};
			},
			//页面间距
			mbConfig() {
				return {
					marginTop: this.dataConfig.mbConfig.val ? this.dataConfig.mbConfig.val + 'px' : 0
				}
			},
			//分类列表
			navList() {
				return this.dataConfig.tabItemConfig.list;
			},
			//最外层盒子的样式
			boxStyle() {
				return {
					borderRadius: this.dataConfig.bgStyle.val * 2 + 'rpx',
					background: `linear-gradient(${this.dataConfig.bgColor.color[0].item}, ${this.dataConfig.bgColor.color[1].item})`,
					margin: this.dataConfig.topConfig.val * 2 + 'rpx' + ' ' + this.dataConfig.lrConfig.val * 2 + 'rpx' +
						' ' + 0,
					padding: this.dataConfig.upConfig.val * 2 + 'rpx' + ' ' + 0 + ' ' + this.dataConfig.downConfig.val *
						2 + 'rpx'
				}
			},
			//商品间距
			gridGap() {
				return {
					'grid-gap': this.dataConfig.contentConfig.val * 2 + 'rpx'
				}
			},
			//图片的圆角和高度
			imgStyle() {
				return {
					'border-radius': this.dataConfig.contentStyle.val * 2 + 'rpx',
				}
			},
			//价格颜色
			priceColor() {
				return {
					'color': this.dataConfig.priceThemeStyleConfig.tabVal?this.dataConfig.priceColor.color[0].item:this.themeColor,
				}
			},
			//商品名称颜色
			titleColor() {
				return {
					'color': this.dataConfig.titleColor.color[0].item,
				}
			},
			//已售数量
			soldColor() {
				return {
					'color': this.dataConfig.soldColor.color[0].item,
				}
			},
			showArr(){
				return this.dataConfig.tabItemConfig.list[this.ProductNavindex].activeList.showContent
			},
			//商品名称
			titleShow() {
				if (this.dataConfig.typeConfig.activeValue.includes(0)) {
					return true;
				} else {
					return false;
				}
			},
			//价格
			priceShow() {
				if (this.dataConfig.typeConfig.activeValue.includes(1)) {
					return true;
				} else {
					return false;
				}
			},
			//销量
			soldShow() {
				if (this.dataConfig.typeConfig.activeValue.includes(2)) {
					return true;
				} else {
					return false;
				}
			},
			//内容圆角
			contentStyle() {
				return {
					'border-radius': this.dataConfig.contentStyle.val ? this.dataConfig.contentStyle.val + 'px' : '0'
				};
			},
		},
		mounted() {
			//默认加载第一项的商品数据
			if (this.navList) {
				this.itemStyle = this.navList[0].activeList ? this.navList[0].activeList.styleType : 0;
				if (this.navList[0].activeList && this.navList[0].activeList.activeProTabIndex == 0) {
					this.getProductByids(this.navList[0].activeList.goods);
				} else {
					this.limit = this.navList[0].activeList ? this.navList[0].activeList.num : 3;
					if (this.navList[0].activeList) {
						this.getGroomList(this.navList[0].activeList);
					}
				}
			}
		},
		//uniapp小程序用deep重写组件样式不生效 
		options: {
			styleIsolation: 'shared'
		},
		methods: {
			//根据商品id集合查询对应商品
			getProductByids(data) {
				if(!data.length) return;
				uni.showLoading({
					title: '加载中...'
				});
				let ids = data.map((item) => item.id).join(',');
				productByidsApi(ids).then((res) => {
						this.tempArr = res.data;
						uni.hideLoading();
					})
					.catch(res => {
						uni.hideLoading();
					});
			},
			// 选项卡切换点击事件；商品类型选择除第一个指定商品，加载商品从平台端获取数据，其余选项均请求接口加载
			changeTab(item, index) {
				this.tempArr = [];
				if (item.activeList.activeProTabIndex == 0) {
					this.getProductByids(item.activeList.goods);
				} else {
					this.page = 1;
					this.loadend = false;
					this.getGroomList(item.activeList);
				}
			},
			// 商品列表
			getGroomList(item) {
				let cid = item.activeValue; //分类id
				let goodsSort = item.goodsSort // 商品排序，0综合，1按销量，2按价格
				let priceOrder = '';
				let salesOrder = '';
				if (this.loadend) return false;
				if (this.loading) return false;
				if (goodsSort === 0) {
					priceOrder = '';
					salesOrder = '';
				} else if (goodsSort === 1) {
					priceOrder = '';
					salesOrder = 'desc';
				} else {
					priceOrder = 'desc';
					salesOrder = '';
				}
				getProductslist({
						page: this.page,
						limit: this.limit,
						cid: cid,
						priceOrder: priceOrder,
						salesOrder: salesOrder
					}).then((res) => {
						let list = res.data.list;
						this.tempArr = this.$util.SplitArray(list, this.tempArr);
						let loadend = list.length < this.limit;
						this.loadend = loadend;
						this.loading = false;
						this.page = this.page + 1;
					})
					.catch(res => {
						this.loading = false;
					});
			},
			// 选项卡切换
			ProductNavTab(item, index) {
				this.ProductNavindex = index;
				this.itemStyle = this.navList[index].activeList.styleType;
				this.$nextTick(() => {
					let id = 'id' + index;
					this.tabLeft = (index - 2) * this.isWidth //设置下划线位置
				})
				this.limit = item.activeList.num;
				this.changeTab(item, index);
			},
			goDetail(item) {
				uni.navigateTo({
				    url: `/pages/goods/goods_details/index?id=${item.id}`
				})
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

	@keyframes cardIn {
		from { opacity: 0; transform: translateY(16rpx); }
		to { opacity: 1; transform: translateY(0); }
	}

	.longTab {
		.longItem {
			height: 70rpx;
			display: inline-block;
			line-height: 70rpx;
			text-align: center;
			font-size: 28rpx;
			color: $on-surface-variant;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
			margin-right: 46rpx;
			transition: color 0.2s $ease;
			&.click {
				font-weight: 600;
				font-size: 30rpx;
				position: relative;

				&::after {
					content: '';
					width: 40rpx;
					height: 4rpx;
					background: $primary;
					position: absolute;
					bottom: 0;
					left: 50%;
					transform: translateX(-50%);
					transition: all 0.3s $ease;
				}
			}
		}
	}

	.index-product-wrapper {

		&.on {
			min-height: 1500rpx;
		}

		.nav-bd {
			height: 70rpx;
			line-height: 70rpx;
			padding-left: 20rpx;
			background: $surface-lowest;
			border-bottom: 1rpx solid $outline-variant;

			.item {
				display: inline-block;
				font-size: 28rpx;
				color: $on-surface-variant;
				font-weight: 400;
				padding-right: 48rpx;
				transition: color 0.2s $ease;

				&.on {
					border-radius: 0;
				}
			}
		}
	}

	.text-add {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.tagSolid {
		display: inline-block;
		background: $surface-low;
		border: 1rpx solid rgba($primary, 0.12);
		color: $primary;
		border-radius: 4rpx;
		font-size: 18rpx;
		line-height: 28rpx;
		padding: 0 8rpx;
	}

	.listBig {
		display: grid;
		grid-template-rows: auto;
		grid-template-columns: repeat(1, 1fr);
		padding: 0 20rpx;

		.itemBig {
			width: 100%;
			background: $surface-lowest;
			border: 1rpx solid $outline-variant;
			border-radius: $r-md;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
			overflow: hidden;
			animation: cardIn 0.4s $ease both;
			transition: transform 0.15s $ease;

			&:active {
				transform: scale(0.97);
			}

			.img-box {
				width: 100%;
				height: 710rpx;
				position: relative;
			}

			.name {
				font-size: 28rpx;
				font-weight: 500;
				color: $on-surface;
				margin-top: 16rpx;
			}

			.price {
				font-weight: 700;
				font-size: 12px;
				color: $tertiary-container;
				margin-top: 10rpx;

				.num {
					font-size: 32rpx;
					font-weight: 700;
					color: $tertiary-container;
					margin-right: 10rpx;
				}

				.old-price {
					color: $secondary;
					font-weight: normal;
				}
			}
		}
	}

	.listA {
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		grid-template-rows: auto;
		width: 100%;
		padding: 0 20rpx;

		.item {
			display: flex;
			width: 100%;
			background: $surface-lowest;
			border: 1rpx solid $outline-variant;
			border-radius: $r-md;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
			overflow: hidden;
			animation: cardIn 0.4s $ease both;
			transition: transform 0.15s $ease;

			&:active {
				transform: scale(0.97);
			}

			.pictrue {
				width: 220rpx;
				height: 220rpx;
				position: relative;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.text-info {
				margin-left: 14rpx;
				flex: 1
			}
		}
	}

	.listB {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		grid-template-rows: auto;
		width: 100%;
		padding: 0 20rpx;

		.item {
			background: $surface-lowest;
			border: 1rpx solid $outline-variant;
			border-radius: $r-md;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
			overflow: hidden;
			animation: cardIn 0.4s $ease both;
			transition: transform 0.15s $ease;

			&:active {
				transform: scale(0.97);
			}

			.pictrue {
				width: 100%;
				height: 220rpx;
				position: relative;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.text-info {
				padding: 10rpx 0;
			}
		}
	}

	.listC {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		grid-template-rows: auto;
		width: 100%;
		padding: 0 20rpx;

		/deep/.origin-img,
		/deep/.easy-loadimage {
			border-bottom-left-radius: 0 !important;
			border-bottom-right-radius: 0 !important;
		}

		.item {
			background-color: $surface-lowest;
			border: 1rpx solid $outline-variant;
			border-radius: $r-md;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
			overflow: hidden;
			animation: cardIn 0.4s $ease both;
			transition: transform 0.15s $ease;

			&:active {
				transform: scale(0.97);
			}

			.pictrue {
				width: 100%;
				height: 345rpx;
				overflow: hidden;
				position: relative;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.text-info {
				padding: 16rpx 0 16rpx 16rpx;

				.title {
					width: 300rpx;
				}
			}
		}
	}

	.text-info {
		.title {
			width: 100%;
			height: 80rpx;
			line-height: 40rpx;
			color: $on-surface;
			font-weight: 500;
		}

		.old-price {
			font-weight: normal;
			font-size: 24rpx;
			color: $secondary;
		}

		.price {
			font-size: 36rpx;
			font-weight: 700;
			color: $tertiary-container;

			text {
				padding-bottom: 4rpx;
				font-size: 26rpx;
				font-weight: normal;
			}

			.num {
				font-weight: 700;
				color: $tertiary-container;
			}
		}
	}

	.mer_badge {
		padding: 0 4rpx;
		background-color: $primary;
		color: $white;
		font-size: 20rpx;
		display: inline-block;
		border-radius: 4rpx;
		line-height: 28rpx;
		height: 28rpx;
	}
</style>