<template>
	<view :data-theme="theme">
		<view class="newsDetail" :style="{ backgroundColor: bgColor }">
			<view class="title">{{ articleInfo.title }}</view>
			<view class="list acea-row row-middle">
				<view class="label">{{ articleInfo.author }}</view>
				<view class="item">{{ articleInfo.createTime }}</view>
				<view class="item"><text class="iconfont icon-liulan"></text>{{ articleInfo.visit }}</view>
			</view>
			<view class="conter">
				<jyf-parser :html="content" ref="article" :tag-style="tagStyle"></jyf-parser>
			</view>
			<view class="picTxt acea-row row-between-wrapper" v-if="store_info.id">
				<view class="pictrue">
					<image :src="store_info.image"></image>
				</view>
				<view class="text">
					<view class="name line1">{{ store_info.storeName }}</view>
					<view class="money price_color">
						￥<text class="num">{{ store_info.price }}</text>
					</view>
					<view class="y_money">￥{{ store_info.otPrice }}</view>
				</view>
				<navigator :url="'/pages/goods/goods_details/index?id=' + store_info.id" hover-class="none" class="label">
					<text class="span">查看商品</text>
				</navigator>
			</view>
			<!-- #ifdef H5 -->
			<button class="bnt bg_color" hover-class="none" @click="listenerActionSheet" v-if="this.$wechat.isWeixin()">
				和好友一起分享
			</button>
			<!-- #endif -->
			<!-- #ifdef MP -->
			<button class="bnt bg_color" open-type="share" hover-class="none">和好友一起分享</button>
			<!-- #endif -->
		</view>
		<shareInfo @setShareInfoStatus="setShareInfoStatus" :shareInfoStatus="shareInfoStatus"></shareInfo>
		<view class="article_theme"></view>
	</view>
</template>

<script>
	import { getArticleDetails } from '@/api/api.js';
	import { getProductDetail } from '@/api/store.js';
	import shareInfo from '@/components/shareInfo';
	import parser from '@/components/jyf-parser/jyf-parser';

	let app = getApp();

	export default {
		components: {
			shareInfo,
			'jyf-parser': parser
		},
		data() {
			return {
				id: 0,
				articleInfo: {},
				store_info: {},
				content: '',
				shareInfoStatus: false,
				tagStyle: {
					img: 'width:100%;display:block;',
					table: 'width:100%',
					video: 'width:100%'
				},
				productId: 0,
				theme: app.globalData.theme,
				bgColor: '#ffffff'
			};
		},
		onLoad(options) {
			if (options.hasOwnProperty('id')) {
				this.id = options.id;
			} else {
				// #ifndef H5
				uni.navigateBack({
					delta: 1
				});
				// #endif
				// #ifdef H5
				history.back();
				// #endif
			}
		},
		onShow() {
			this.getArticleOne();
		},
		// #ifdef MP
		onShareAppMessage() {
			const shareMeta = this.buildShareMeta();
			return {
				title: shareMeta.title,
				imageUrl: shareMeta.imageUrl,
				desc: shareMeta.desc,
				path: shareMeta.path
			};
		},
		onShareTimeline() {
			const shareMeta = this.buildShareMeta();
			return {
				title: shareMeta.title,
				query: `id=${this.id}`,
				imageUrl: shareMeta.imageUrl
			};
		},
		// #endif
		methods: {
			getArticleOne() {
				getArticleDetails({
					id: this.id
				}).then((res) => {
					const article = res.data || {};
					const articleTitle = article.title || '';
					uni.setNavigationBarTitle({
						title: articleTitle.length > 7 ? `${articleTitle.substring(0, 7)}...` : articleTitle
					});
					this.$set(this, 'articleInfo', article);
					this.$set(this, 'productId', article.productId);
					if (article.productId) {
						this.goodInfo(article.productId);
					}
					this.content = article.content || '';
					// #ifdef H5
					this.setH5Meta();
					if (this.$wechat.isWeixin()) {
						this.setShareInfo();
					}
					// #endif
				});
			},
			buildShareMeta() {
				const articleInfo = this.articleInfo || {};
				const seoInfo = articleInfo.seoInfo || {};
				const imageUrl = seoInfo.shareImage || articleInfo.imageInput || '';
				return {
					title: seoInfo.shareTitle || articleInfo.title || '',
					desc: seoInfo.seoDescription || articleInfo.shareSynopsis || articleInfo.synopsis || '',
					imageUrl,
					link: typeof location !== 'undefined' ? location.href : '',
					path: `/pages/news/news_details/index?id=${this.id}`
				};
			},
			goodInfo(id) {
				getProductDetail(id).then((res) => {
					this.$set(this, 'store_info', res.data.storeInfo ? res.data.storeInfo : {});
				});
			},
			listenerActionSheet() {
				this.shareInfoStatus = true;
			},
			setShareInfoStatus() {
				this.shareInfoStatus = false;
			},
			setShareInfo() {
				const shareMeta = this.buildShareMeta();
				this.$wechat.wechatEvevt(['updateAppMessageShareData', 'updateTimelineShareData'], {
					desc: shareMeta.desc,
					title: shareMeta.title,
					link: shareMeta.link,
					imgUrl: shareMeta.imageUrl
				});
			},
			setH5Meta() {
				const articleInfo = this.articleInfo || {};
				const seoInfo = articleInfo.seoInfo || {};
				const title = seoInfo.seoTitle || articleInfo.title || '';
				const keywords = seoInfo.seoKeywords || '';
				const description = seoInfo.seoDescription || articleInfo.shareSynopsis || articleInfo.synopsis || '';
				if (typeof document === 'undefined') {
					return;
				}
				document.title = title;
				this.upsertMeta('keywords', keywords);
				this.upsertMeta('description', description);
			},
			upsertMeta(name, content) {
				if (typeof document === 'undefined') {
					return;
				}
				let meta = document.querySelector(`meta[name="${name}"]`);
				if (!meta) {
					meta = document.createElement('meta');
					meta.setAttribute('name', name);
					document.head.appendChild(meta);
				}
				meta.setAttribute('content', content || '');
			},
			bgTheme(value) {
				this.bgColor = value;
			}
		}
	};
</script>

<style lang="scss" scoped>
	.newsDetail {
		padding: 30rpx 0;
	}

	.newsDetail .title {
		padding: 0 30rpx;
		font-size: 34rpx;
		color: #282828;
		font-weight: bold;
		line-height: 1.5;
	}

	.newsDetail .list {
		margin: 28rpx 30rpx 0 30rpx;
		padding-bottom: 25rpx;
	}

	.newsDetail .list .label {
		font-size: 30rpx;
		color: #B1B2B3;
	}

	.newsDetail .list .item {
		margin-left: 27rpx;
		font-size: 30rpx;
		color: #B1B2B3;
	}

	.newsDetail .list .item .iconfont {
		font-size: 28rpx;
		margin-right: 10rpx;
	}

	.newsDetail .list .item .iconfont.icon-shenhezhong {
		font-size: 26rpx;
	}

	.newsDetail .picTxt {
		width: 690rpx;
		height: 200rpx;
		border-radius: 20rpx;
		border: 1px solid #e1e1e1;
		position: relative;
		margin: 30rpx auto 0 auto;
	}

	.newsDetail .picTxt .pictrue {
		width: 200rpx;
		height: 200rpx;
	}

	.newsDetail .picTxt .pictrue image {
		width: 100%;
		height: 100%;
		border-radius: 20rpx 0 0 20rpx;
		display: block;
	}

	.newsDetail .picTxt .text {
		width: 460rpx;
	}

	.newsDetail .picTxt .text .name {
		font-size: 30rpx;
		color: #282828;
	}

	.newsDetail .picTxt .text .money {
		font-size: 24rpx;
		margin-top: 40rpx;
		font-weight: bold;
	}

	.price_color {
		@include price_color(theme);
	}

	.newsDetail .picTxt .text .money .num {
		font-size: 36rpx;
	}

	.newsDetail .picTxt .text .y_money {
		font-size: 26rpx;
		color: #999;
		text-decoration: line-through;
	}

	.newsDetail .picTxt .label {
		position: absolute;
		background-color: #303131;
		width: 160rpx;
		height: 50rpx;
		right: -7rpx;
		border-radius: 25rpx 0 6rpx 25rpx;
		text-align: center;
		line-height: 50rpx;
		bottom: 24rpx;
	}

	.newsDetail .picTxt .label .span {
		background-image: linear-gradient(to right, #fff71e 0%, #f9b513 100%);
		-webkit-background-clip: text;
		-webkit-text-fill-color: transparent;
	}

	.newsDetail .picTxt .label:after {
		content: ' ';
		position: absolute;
		width: 0;
		height: 0;
		border-bottom: 8rpx solid #303131;
		border-right: 8rpx solid transparent;
		top: -7rpx;
		right: 0;
	}

	.newsDetail .bnt {
		color: #fff;
		font-size: 30rpx;
		width: 690rpx;
		height: 90rpx;
		border-radius: 45rpx;
		margin: 48rpx auto;
		text-align: center;
		line-height: 90rpx;
	}

	.bg_color {
		@include main-bg_color(theme);
	}
</style>
